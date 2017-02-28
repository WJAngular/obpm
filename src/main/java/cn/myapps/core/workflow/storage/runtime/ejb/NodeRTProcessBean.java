package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;





import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.WorkflowException;
import cn.myapps.core.workflow.element.AutoNode;
import cn.myapps.core.workflow.element.CompleteNode;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.element.SuspendNode;
import cn.myapps.core.workflow.engine.StateMachineUtil;
import cn.myapps.core.workflow.engine.instruction.InstructionExecutor;
import cn.myapps.core.workflow.engine.instruction.PersistentInstruction;
import cn.myapps.core.workflow.notification.ejb.NotificationConstant;
import cn.myapps.core.workflow.notification.ejb.OverDueNotification;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.dao.AbstractNodeRTDAO;
import cn.myapps.core.workflow.storage.runtime.dao.NodeRTDAO;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;

public class NodeRTProcessBean extends AbstractRunTimeProcessBean<NodeRT> implements NodeRTProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4682198953868473304L;

	public NodeRTProcessBean(String applicationId) {
		super(applicationId);
	}

	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getNodeRTDAO(getConnection(), getApplicationId());
	}

	/**
	 * 先拿出角色的集合,再创建一个节点对象
	 * 
	 * @param doc
	 *            文档对象
	 * @param flowVO
	 *            流程对象
	 * @param flowoption
	 *            流程运行的类型
	 * @return 节点对象
	 */
	public NodeRT doCreate(ParamsTable params, NodeRT parentNodert,FlowStateRT instance, Node node,
			String flowoption, BaseUser user) throws Exception {
		if (node != null && instance.getFlowVO() != null) {
			// 分散时，建立分散令牌
			NodeRT nodert = new NodeRT(instance, node, flowoption);
			if (parentNodert != null) {
				nodert.setSplitToken(getSplitToken(params, parentNodert));
				nodert.setParentNodertid(parentNodert.getNodeid());
			}

			if (node instanceof ManualNode) {
				if (FlowType.RUNNING2RUNNING_BACK.equals(flowoption)
						&& (params.getParameterAsString("submitTo") == null || params.getParameterAsString("submitTo")
								.trim().length() == 0)) {
					nodert.setActorrts(getHisActorRTList(instance.getDocument(), nodert, (ManualNode) node,params));
				} else {
					//设置流程审批人
					nodert.setActorrts(getActorRTList(params, instance.getDocument(), nodert, (ManualNode) node,user));
					//设置抄送人
//					nodert.setCirculators(getCirculatorList(params, instance.getDocument(), nodert, (ManualNode) node));
				}

			}
			
			if(node instanceof ManualNode && nodert.getActorrts().isEmpty()){
				throw new OBPMValidateException("\""+node.name+"\"节点没有合法的审批人！",new WorkflowException("\""+node.name+"\"节点没有合法的审批人！"));
			}
			
			if(node instanceof AutoNode || node instanceof SuspendNode){
				ActorRT actrt = new ActorRT(user);
				actrt.setDocId(instance.getDocid());
				nodert.getActorrts().add(actrt);
			}
			
			if(node instanceof AutoNode){
				try {
					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), instance.getApplicationid());
					runner.initBSFManager(instance.getDocument(), params, (WebUser) user, new ArrayList<ValidateMessage>());
					Date time = ((AutoNode)node).getAuditDateTime(runner);
					nodert.setDeadline(time);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
			
			if(node instanceof ManualNode && ((ManualNode)node).isLimited){
				//设置审批时限
				try {
					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), instance.getApplicationid());
					runner.initBSFManager(instance.getDocument(), params, (WebUser) user, new ArrayList<ValidateMessage>());
					Date deadline = ((ManualNode)node).getDeadlineDataTime(runner);
					nodert.setDeadline(deadline);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				
			}

			processActorRTList(nodert);
			if(node instanceof CompleteNode){
				nodert.setComplete(true);
			}else{
				//添加到指令队列
				InstructionExecutor.getInstance().put(new PersistentInstruction(this, nodert, PersistentInstruction.ACTION_CREATE));
				//doCreate(nodert);
			}
			if(parentNodert != null){
				instance.setPrevAuditNode(parentNodert.getStatelabel());
				instance.setPrevAuditUser(user.getName());
			}

			return nodert;
		}
		return null;
	}

	private String getSplitToken(ParamsTable params, NodeRT parentNodeRT) {
		String splitToken = params.getParameterAsString("splitToken") == null ? "" : params
				.getParameterAsString("splitToken");
		String parentToken = parentNodeRT.getSplitToken() == null ? "" : parentNodeRT.getSplitToken();
		// 参数中没有Token则延用上一个节点的Token，有则附加到后面
		if (!StringUtil.isBlank(parentToken)) {
			// token中不包含此节点ID
			if (!StringUtil.isBlank(splitToken) && parentToken.indexOf(splitToken) == -1) {
				splitToken = parentToken + ":" + splitToken;
			} else {
				splitToken = parentToken;
			}
		}
		return splitToken;
	}

	/**
	 * 根据聚合条件判断是否创建下一个节点
	 * 
	 * @param instance
	 *            流程实例
	 * @param nextNode
	 *            下一个节点
	 * @return
	 * @throws Exception
	 */
	public boolean isCreateAble(FlowStateRT instance, NodeRT nodeRT, Node nextNode) throws Exception {
		boolean isgather = false;
		String splitStartNode = ""; // 聚合时的分散开始点
		if (nextNode instanceof ManualNode) {
			isgather = ((ManualNode) nextNode).isgather;
			splitStartNode = ((ManualNode) nextNode).splitStartNode;
		} else if (nextNode instanceof AutoNode) {
			isgather = ((AutoNode) nextNode).isgather;
			splitStartNode = ((AutoNode) nextNode).splitStartNode;
		}else if (nextNode instanceof SubFlow) {
			isgather = ((SubFlow) nextNode).isgather;
			splitStartNode = ((SubFlow) nextNode).splitStartNode;
		}else if (nextNode instanceof CompleteNode) {
			isgather = ((CompleteNode) nextNode).isgather;
			splitStartNode = ((CompleteNode) nextNode).splitStartNode;
		}

		// 如果为聚合
		boolean flag = true;
		if (isgather) {
			//把从数据库查询改成从内存中获取
			Collection<NodeRT> nodertList = instance.getNoderts();
			/*Collection<NodeRT> nodertList = doQuery(instance.getDocid(),instance.getId());*/

			if (nodertList != null) {

				for (Iterator<NodeRT> iter = nodertList.iterator(); iter.hasNext();) {
					NodeRT nrt = (NodeRT) iter.next();

					// 不为当前节点
					if (!nrt.getId().equals(nodeRT.getId())) {
						String splitToken = nrt.getSplitToken();

						String[] tokenIds = splitToken.split(":");
						for (int i = 0; i < tokenIds.length; i++) {
							String tokenId = tokenIds[i];
							// 是需要被聚合的节点
							if (splitStartNode != null && splitStartNode.equals(tokenId)) {
								flag = false;
							}
						}
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 根据角色列表更新当前节点的负责人列表
	 * 
	 * @param nodeRT
	 *            节点对象
	 * @param doc
	 *            文档对象
	 * @param flowVO
	 *            流程节点
	 * @param actorIds
	 *            审批角色标识
	 * @throws Exception
	 */
	public NodeRT doUpdateByActorIds(NodeRT nodeRT, Document doc, BillDefiVO flowVO, String[] actorIds,ParamsTable params)
			throws Exception {
		if (nodeRT != null) {
			FlowDiagram diagram = flowVO.toFlowDiagram();
			Node node = diagram.getNodeByID(nodeRT.getNodeid());
			if (node instanceof ManualNode) {
				// 清空Actor RT列表, 使其能重新加载
				nodeRT.getActorrts().clear();
				doc.getState().getActors().clear();

				int _position = nodeRT.getPosition();
				for (int i = 0; i < actorIds.length; i++) {
					if(StringUtil.isBlank(actorIds[i])) continue;
					ActorRT actorrt = createActorRT(doc, nodeRT, (ManualNode) node, actorIds[i], _position,params);
					actorrt.setNodertid(nodeRT.getId());
					nodeRT.getActorrts().add(actorrt);
					doc.getState().getActors().add(actorrt);
					_position++;
				}
				processActorRTList(nodeRT);
			}

			try {
				beginTransaction();
				doUpdate(nodeRT);
				Collection<NodeRT> noderts = doc.getState().getNoderts();
				for (Iterator<NodeRT> iterator = noderts.iterator(); iterator.hasNext();) {
					NodeRT nodeRT2 = iterator.next();
					if(nodeRT.getId().equals(nodeRT2.getId())){
						noderts.remove(nodeRT2);
						noderts.add(nodeRT);
						break;
					}
					
				}
				commitTransaction();
			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}

		}
		return nodeRT;
	}

	/**
	 * 根据用户获取到用户是不是当前文档的处理人，如果用户是当前处理人，那么将出现下一个节点给用户进行提交
	 * 
	 * @param nodeRT
	 *            当前节点
	 * @param user
	 *            处理者
	 * @throws Exception
	 */
	public boolean process(NodeRT nodeRT, WebUser user, String flowOption, int residual) throws Exception {
		
		ActorRTProcess actorProcess = getActorRTProcess(getApplicationId());

		Collection<ActorRT> actorList = nodeRT.getActorrts();
		
		boolean isProcessor = false;
		ActorRT actorrt = null;

		// 判断用户是否为节点处理人
		for (Iterator<ActorRT> iterator = actorList.iterator(); iterator.hasNext();) {
			actorrt = (ActorRT) iterator.next();
			if (actorrt.isEquals(user) || user.isAgent(actorrt.getActorid(), nodeRT.getFlowid())) {
				isProcessor = true;
				break;
			}
		}

		// 根据不同的通过策略进行处理
		if (isProcessor) {
			switch (nodeRT.getPassCondition()) {
			case ManualNode.PASS_CONDITION_OR:
				nodeRT.setPassed(true);
				break;
			case ManualNode.PASS_CONDITION_AND:
				// 会签情况下其中一人回退则直接回退
				if (flowOption.equals(FlowType.RUNNING2RUNNING_BACK)) {
					nodeRT.setPassed(true);
					break;
				} else {
					if (residual == 1) {
						nodeRT.getActorrts().remove(actorrt);
						//添加指令到队列
						InstructionExecutor.getInstance().put(new PersistentInstruction(actorProcess, actorrt.getId(), PersistentInstruction.ACTION_DELETE));
						//actorProcess.doRemove(actorrt.getId());

						if (nodeRT.getActorrts().isEmpty()) {
							nodeRT.setPassed(true);
						} else {
							actorrt = (ActorRT) nodeRT.getActorrts().toArray()[0];
							actorrt.setPending(true);
							//添加指令到队列
							InstructionExecutor.getInstance().put(new PersistentInstruction(actorProcess, actorrt, PersistentInstruction.ACTION_UPDATE));
							//actorProcess.doUpdate(actorrt);
						}
					} else if (residual > 1 && nodeRT.getActorrts().size() == 1) {
						nodeRT.setPassed(true);
					}
					break;

				}
			case ManualNode.PASS_CONDITION_ORDERLY_AND://顺序会签
				if (flowOption.equals(FlowType.RUNNING2RUNNING_BACK)) {
					nodeRT.setPassed(true);
					break;
				}else {
					if (residual == 1) {
						nodeRT.getActorrts().remove(actorrt);
						//添加指令到队列
						InstructionExecutor.getInstance().put(new PersistentInstruction(actorProcess, actorrt.getId(), PersistentInstruction.ACTION_DELETE));
						//actorProcess.doRemove(actorrt.getId());

						if (nodeRT.getAllActorrts().size()==1) {
							nodeRT.setPassed(true);
						} else {
							nodeRT.setPosition(nodeRT.getPosition()+1);
							nodeRT.setActorrts(null);
							actorrt = (ActorRT) nodeRT.getActorrts().toArray()[0];
							actorrt.setPending(true);
							//添加指令到队列
							InstructionExecutor.getInstance().put(new PersistentInstruction(actorProcess, actorrt, PersistentInstruction.ACTION_UPDATE));
							//actorProcess.doUpdate(actorrt);
							//添加指令到队列
							InstructionExecutor.getInstance().put(new PersistentInstruction(this, "doUpdatePosition", new Object[]{nodeRT}));
							//this.doUpdatePosition(nodeRT);
						}
					} else if (residual > 1 && nodeRT.getActorrts().size() == 1) {
						nodeRT.setPassed(true);
					}
					break;
				}
				
			default:
				break;
			}
		}

		return nodeRT.isPassed();
	}

	/**
	 * 设置代办
	 * 
	 * @param nodert
	 * @throws Exception
	 */
	private void processActorRTList(NodeRT nodert) throws Exception {
		// 设置Pending
		if (!nodert.getActorrts().isEmpty()) {
			switch (nodert.getPassCondition()) {
			case ManualNode.PASS_CONDITION_OR:
			case ManualNode.PASS_CONDITION_AND:
				for (Iterator<ActorRT> iterator = nodert.getActorrts().iterator(); iterator.hasNext();) {
					ActorRT actorRT = (ActorRT) iterator.next();
					actorRT.setPending(true);
				}
				break;
			case ManualNode.PASS_CONDITION_ORDERLY_AND:
				ActorRT actorRT = (ActorRT) nodert.getActorrts().toArray()[0];
				actorRT.setPending(true);
				break;
			default:
				break;
			}

		}
	}

	public ActorRTProcess getActorRTProcess(String applicationId) throws ClassNotFoundException {
		return new ActorRTProcessBean(applicationId);
	}

	/**
	 * 获取历史审批人
	 * 
	 * @param startNodeId
	 * @param endNodeId
	 * @return
	 * @throws Exception
	 */
	private Collection<ActorRT> getHisActorRTList(Document doc, NodeRT nodeRT, ManualNode targetNode,ParamsTable params) throws Exception {
		Collection<ActorRT> rtn = new ArrayList<ActorRT>();
		RelationHISProcess rhisProcess = new RelationHISProcessBean(doc.getApplicationid());
		RelationHIS relationHIS = rhisProcess.doViewLastByStartNode(doc.getId(), doc.getState().getFlowid(), doc.getState().getId(), targetNode.id);
		Collection<BaseUser> userList = relationHIS.getUserList();
		int position =0;//审批顺序
		if((Integer.parseInt(((ManualNode) targetNode).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND)){//顺序会签
			nodeRT.setPosition(1);
		}
		for (Iterator<BaseUser> iterator = userList.iterator(); iterator.hasNext();) {
			BaseUser baseUser = (BaseUser) iterator.next();
			if((Integer.parseInt(((ManualNode) targetNode).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND)){//顺序会签
				position ++;
			}
			ActorRT actorRT = createActorRT(doc, nodeRT, targetNode, baseUser,position,params);
			rtn.add(actorRT);
		}

		return rtn;
	}

	/**
	 * 根据节点中的NameListText获取用户ID与审批者的映射
	 * 
	 * @param node
	 *            所在节点
	 * @param doc
	 *            当前处理的文档
	 * @return 用户ID与审批者的映射
	 * @throws Exception
	 */
	private Collection<ActorRT> getActorRTList(ParamsTable params, Document doc, NodeRT nodeRT, ManualNode node,BaseUser auditor)
			throws Exception {
		// 测试用
		// node.actorEditMode = ManualNode.ACTOR_EDIT_MODE_CODE;
		// node.actorListScript = FileOperate
		// .getFileContentAsString("C:/Java/workspace2/obpm/src/main/java/cn/myapps/core/workflow/storage/runtime/ejb/test.js");

		Collection<ActorRT> rtn = new ArrayList<ActorRT>();
		Collection<BaseUser> userList = StateMachineUtil.getPrincipalList(doc,params, node, doc.getDomainid(), doc
				.getApplicationid(),auditor);

		// 已审批的角色列表
		/* 2011-7-27 by happy
		RelationHISProcess rhisProcess = new RelationHISProcessBean(doc.getApplicationid());
		Collection<RelationHIS> hisList = rhisProcess.doQueryByStartNode(doc.getId(), doc.getStateid(), nodeRT
				.getNodeid(), "'80','83'");
		*/
		int position = 0;//审批顺序
		if((Integer.parseInt(((ManualNode) node).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND)){//顺序会签
			nodeRT.setPosition(1);
		}
		for (Iterator<BaseUser> iterator = userList.iterator(); iterator.hasNext();) {
			BaseUser userVO = (BaseUser) iterator.next();
			/* 2011-7-27 by happy
			if (isCreateActorRT(hisList, userVO, node) && userVO.getStatus() == 1) { // 不是已审批且(会签)则添加
			*/
			if(userVO.getStatus() == 1){// 且用户已经激活
				if((Integer.parseInt(((ManualNode) node).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND)){//顺序会签
					position ++;
				}
				
				ActorRT actorRT = createActorRT(doc, nodeRT, node, userVO,position,params);
				rtn.add(actorRT);
			}
		}

		return rtn;
	}
	
	
	/**
	 * 获取并创建抄送人 
	 * @param params
	 * @param doc
	 * @param nodeRT
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public Collection<Circulator> getCirculatorList(ParamsTable params, Document doc, NodeRT nodeRT, ManualNode node) throws Exception {
		Collection<Circulator> rtn = new ArrayList<Circulator>();
		Collection<BaseUser> userList = StateMachineUtil.getCirculatorList(params,doc, node,doc.getDomainid(), doc.getApplicationid());
		CirculatorProcess process = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class, doc.getApplicationid());
		for (Iterator<BaseUser> iterator = userList.iterator(); iterator.hasNext();) {
			BaseUser userVO = (BaseUser) iterator.next();
			if(userVO.getStatus() == 1){
				Circulator circulator = new Circulator(doc, nodeRT, userVO, null);
				process.doCreate(circulator);
				rtn.add(circulator);
			}
		}
		return rtn;
	}

	@SuppressWarnings("unused")
	private boolean isCreateActorRT(Collection<RelationHIS> hisList, BaseUser userVO, ManualNode node) throws Exception {
		Collection<String> userIdList = new ArrayList<String>();

		for (Iterator<RelationHIS> iterator = hisList.iterator(); iterator.hasNext();) {
			RelationHIS his = (RelationHIS) iterator.next();
			userIdList.addAll(his.getUserIdList());
		}

//		if (node.getPassCondition() == ManualNode.PASS_CONDITION_AND) {
//			return (userIdList.isEmpty() || !userIdList.contains(userVO.getId()));
//		}

		return true;
	}

	private ActorRT createActorRT(Document doc, NodeRT nodeRT, ManualNode node, String userId, int position,ParamsTable params) throws Exception {
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO userVO = (UserVO) userProcess.doView(userId);
		return createActorRT(doc, nodeRT, node, userVO,position,params);
	}

	private ActorRT createActorRT(Document doc, NodeRT nodeRT, ManualNode node, BaseUser userVO,int position,ParamsTable params) throws Exception {
		ActorRT actorRT = new ActorRT(userVO);
		Date deadline = getDeadline(doc, node, userVO,params);
		if (deadline != null) {
			actorRT.setDeadline(new java.sql.Timestamp(deadline.getTime()));
		} else {
			actorRT.setDeadline(null);
		}
		actorRT.setApplicationid(doc.getApplicationid());
		actorRT.setNodertid(nodeRT.getId());
		actorRT.setFlowstatertid(doc.getStateid());
		actorRT.setPosition(position);
		actorRT.setDocId(doc.getId());

		return actorRT;
	}

	/**
	 * 获取当前审批者的最后审批期限
	 * 
	 * @param doc
	 *            文档
	 * @param node
	 *            所在节点
	 * @param calendarId
	 *            工作日历
	 * @return
	 * @throws Exception
	 */
	private Date getDeadline(Document doc, ManualNode node, BaseUser userVO,ParamsTable params) throws Exception {
		CalendarProcess calendarProcess = (CalendarProcess) ProcessFactory.createProcess(CalendarProcess.class);
		String calendarId = userVO.getCalendarType();
		Map<String, Object> strategyMap = node.getNotificationStrategyMap();

		if (strategyMap.containsKey(NotificationConstant.STRTAGERY_OVERDUE)) {
			OverDueNotification notification = new OverDueNotification(getApplicationId());
			ObjectUtil.copyProperties(notification, strategyMap.get(NotificationConstant.STRTAGERY_OVERDUE));
			
			switch (notification.getEditMode()){
			case NotificationConstant.EDIT_MODE_DESIGN:
				switch (notification.getTimeunit()) {
				case NotificationConstant.TIME_UNIT_HOUR:
					return calendarProcess.getNextDateByMinuteCount(doc.getAuditdate(), (int) (notification
							.getLimittimecount() * 60.0), calendarId);
				case NotificationConstant.TIME_UNIT_DAY:
					return calendarProcess.getNextDateByMinuteCount(doc.getAuditdate(), (int) (notification
							.getLimittimecount() * 8 * 60), calendarId);
				default:
					break;
				}
			case NotificationConstant.EDIT_MODE_SCRIPT:
				Calendar calendar = Calendar.getInstance();
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), doc.getApplicationid());
				runner.initBSFManager(doc, params, new WebUser(userVO), new ArrayList<ValidateMessage>());
				Object obj = runner.run("ManualNode [name="+node.name+"] [id="+node.id+"] deadlineScript",  StringUtil.dencodeHTML(Security.decodeBASE64(notification.getLimittimeScript())));
				if(obj instanceof Date){
					calendar.setTime((Date) obj);
				}else if(obj instanceof String){
					Date date = null;
					try {
						date = DateUtil.parseDate((String) obj,
								"yyyy-MM-dd HH:mm:ss");
					} catch (Exception e) {
						try {
							date = DateUtil.parseDate((String) obj,
									"yyyy-MM-dd HH:mm");
						} catch (Exception e2) {
							e2.printStackTrace();
							throw new OBPMValidateException("过期时限格式异常,请参照格式'yyyy-MM-dd HH:mm:ss'或'yyyy-MM-dd HH:mm'",new WorkflowException("过期时限格式异常,请参照格式'yyyy-MM-dd HH:mm:ss'或'yyyy-MM-dd HH:mm'"));
						}
						
					}
					
					calendar.setTime(date);
				}
				return calendar.getTime();
			}
		}

		return null;
	}

	public Collection<NodeRT> queryNodeRTByDocidAndFlowid(String docid, String flowid) throws Exception {
		return ((NodeRTDAO) getDAO()).queryNodeRTByDocidAndFlowid(docid, flowid);
	}
	
	public Collection<NodeRT> queryNodeRTByFlowStateIdAndDocId(String instanceId, String docId) throws Exception{
		
		return ((NodeRTDAO) getDAO()).queryNodeRTByFlowStateIdAndDocId(instanceId,docId);
	}

	/**
	 * 根据Document主键(ID)、流程id以及当前用户查询,返回当前用户运行时节点
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            BillDefiVO流程定义对象 id
	 * @param user
	 *            web用户对象
	 * @return 当前用户获取运行时节点
	 * @throws Exception
	 */
	public NodeRT doView(String docid,String flowid, String flowStateId, WebUser user) throws Exception {
		Collection<NodeRT> nodertList = ((NodeRTDAO) getDAO()).query(docid, flowStateId);
		for (Iterator<NodeRT> iter = nodertList.iterator(); iter.hasNext();) {
			NodeRT nodert = (NodeRT) iter.next();
			Iterator<ActorRT> it = nodert.getPendingActorRTList().iterator();
			while (it.hasNext()) {
				ActorRT actorrt = (ActorRT) it.next();
				if ((actorrt).isEquals(user) || user.isAgent(actorrt.getActorid(), flowid)) {
					return nodert;
				}
			}
		}
		return null;
	}

	/**
	 * 根据文档，文档相应流程查询，获取所有运行时节点
	 * 
	 * @param docid
	 *            document id
	 * @param flowStateId
	 *            流程实例 id
	 * @return 文档全部的流程结点
	 * @throws Exception
	 */
	public Collection<NodeRT> doQuery(String docid, String flowStateId) throws Exception {
		return ((NodeRTDAO) getDAO()).query(docid, flowStateId);
	}
	
	/**
	 * 根据文档和流程实例，获取运行时的所有限时审批节点的集合
	 * @param docid
	 * 	文档ID
	 * @param flowStateId
	 * 	流程实例ID
	 * @return
	 * 	运行时的所有限时审批节点的集合
	 * @throws Exception
	 */
	public Collection<NodeRT> doQueryDeadlineNodes(String docid, String flowStateId)
		throws Exception{
		return ((NodeRTDAO) getDAO()).queryDeadlineNodes(docid, flowStateId);
	}

	/**
	 * 根据流程节点获取流程当前节点
	 * 
	 * @param docid
	 *            文档ID
	 * @param flowid
	 *            流程ID
	 * @param nodeid
	 *            节点ID
	 * @return
	 * @throws Exception
	 */
	public NodeRT doViewByNodeid(String docid, String flowStateId, String nodeid) throws Exception {
		return ((NodeRTDAO) getDAO()).findByNodeid(docid, flowStateId, nodeid);
	}

	public Collection<NodeRT> queryByFlowStateRT(String stateId) throws Exception {
		// OracleNodeRTDAO nodeRTDAO = new OracleNodeRTDAO(getConnection());
		return ((AbstractNodeRTDAO) getDAO()).queryByForeignKey("FLOWSTATERT_ID", stateId);
	}
	
	public void doRemoveByFlowStateRT(String flowstatertId) throws Exception {
		Collection<NodeRT> nodes = queryByFlowStateRT(flowstatertId);
		for (Iterator<NodeRT> iterator = nodes.iterator(); iterator.hasNext();) {
			NodeRT nodeRT = (NodeRT) iterator.next();
			this.doRemove(nodeRT.getId());
		}
	}
	
	public void doUpdatePosition(NodeRT vo) throws Exception {
		((NodeRTDAO) getDAO()).updatePosition(vo);
	}
	
	public void doUpdateReminderTimes(NodeRT vo) throws Exception{
		((NodeRTDAO) getDAO()).doUpdateReminderTimes(vo);
	}
}
