package cn.myapps.core.workflow.engine;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONException;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.WorkflowException;
import cn.myapps.core.workflow.element.AutoNode;
import cn.myapps.core.workflow.element.CompleteNode;
import cn.myapps.core.workflow.element.Element;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ImageUtil;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.Relation;
import cn.myapps.core.workflow.element.StartNode;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.element.SuspendNode;
import cn.myapps.core.workflow.engine.instruction.InstructionExecutor;
import cn.myapps.core.workflow.engine.instruction.PersistentInstruction;
import cn.myapps.core.workflow.engine.state.StateCreator;
import cn.myapps.core.workflow.notification.ejb.NotificationProcess;
import cn.myapps.core.workflow.notification.ejb.NotificationProcessBean;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.sequence.Sequence;

public class StateMachine {
	static Logger log = Logger.getLogger(StateMachine.class);

	public static BillDefiProcess getBillDefiProcess()
			throws ClassNotFoundException {
		return (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
	}

	public static NodeRTProcess getNodeRTProcess(String applicationId)
			throws Exception {
		// return new NodeRTProcessBean(applicationId);
		return (NodeRTProcess) ProcessFactory.createRuntimeProcess(
				NodeRTProcess.class, applicationId);
	}

	public static FlowStateRTProcess getFlowStateRTProcess(String applicationId)
			throws Exception {
		return (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(
				FlowStateRTProcess.class, applicationId);
	}

	public static RelationHISProcess getRelationHISProcess(String applicationId)
			throws Exception {
		return (RelationHISProcess) ProcessFactory.createRuntimeProcess(
				RelationHISProcess.class, applicationId);
	}

	public static ActorRTProcess getActorRTProcess(String applicationId)
			throws Exception {
		return (ActorRTProcess) ProcessFactory.createRuntimeProcess(
				ActorRTProcess.class, applicationId);
	}

	public static DocumentProcess getDocumentProcess(String applicationId)
			throws Exception {
		return (DocumentProcess) ProcessFactory.createRuntimeProcess(
				DocumentProcess.class, applicationId);
	}

	public static void doFlow(ParamsTable params, FlowStateRT instance,
			String currid, String[] nextids, WebUser user, String flowOption,
			String attitude, Environment evt) throws Exception {

		Document doc = instance.getDocument();
		BillDefiVO flowVO = instance.getFlowVO();
		if (doc == null) {
			throw new OBPMValidateException(
					"Could not do flow with null document");
		}
		if (flowVO == null) {
			throw new OBPMValidateException(
					"Could not do flow with null BillDefiVO");
		}
		
		NodeRTProcess nodeRTProcess = getNodeRTProcess(flowVO
				.getApplicationid());
		//把从数据库查询改成从内存中获取
		NodeRT currNodeRT = instance.getNodertByNodeid(currid);
		/*NodeRT currNodeRT = nodeRTProcess.doViewByNodeid(doc.getId(), instance
				.getId(), currid);*/
		
		// 获取所有下一步结点
		FlowDiagram fd = flowVO.toFlowDiagram();
		Collection<Node> nodeList = fd.getNodeListByIds(nextids);

		Node currNode = (Node) fd.getElementByID(currid);
		boolean allPassed = true;
		int residual = nodeList.size();// //剩余节点处理数

		// 转移到下一个节点，并创建下一个节点的NodeRT
		for (Iterator<Node> iter = nodeList.iterator(); iter.hasNext();) {
			Node nextNode = (Node) iter.next();
			ParamsTable _params = new ParamsTable(params);
			_params.setSessionid(params.getSessionid());
			_params.setContextPath(params.getContextPath());
			boolean passed = true;

			if (FlowType.RUNNING2RUNNING_INTERVENTION.equals(flowOption)) {
				passed = flowTo(_params, instance, currNode, nextNode, user,
						flowOption, attitude, residual, evt);
			} else {
				if (currNode instanceof StartNode) {
					passed = start2Next((StartNode) currNode, nextNode,
							_params, instance, user, flowOption, attitude,
							residual);
				}
				if (currNode instanceof SuspendNode) {
					passed = suspend2Next((SuspendNode) currNode, nextNode,
							_params, instance, user, flowOption, attitude,
							residual);
				}
				if (currNode instanceof SubFlow) {
					passed = subFlow2Next((SubFlow) currNode, nextNode,
							_params, instance, user, flowOption, attitude,
							residual);
				}
				if (currNode instanceof ManualNode) {
					passed = manual2Next((ManualNode) currNode, nextNode,
							_params, instance, user, flowOption, attitude,
							residual);
				}
				if (currNode instanceof AutoNode) {
					passed = auto2Next((AutoNode) currNode, nextNode, _params,
							instance, user, flowOption, attitude, residual);
				}
				if (currNode instanceof CompleteNode
						&& FlowType.RUNNING2RUNNING_RETRACEMENT
								.equals(flowOption)) {
					passed = flowTo(_params, instance, currNode, nextNode,
							user, flowOption, attitude, residual, evt);
				}
			}
			residual--;
			// 其中一个节点不通过则不删除原节点
			if (!passed) {
				allPassed = false;
			}
		}

		if (allPassed) {
			// 审批后撤除当前节点状态
			if (!instance.isLoopAction()) {// 非节点循环提交动作
				NodeRT origNodeRT = instance.removeNodeRT(currNode.id);
				if (origNodeRT != null) {
					//添加指令、完成流转后才执行更新数据库指令
					InstructionExecutor.getInstance().put(new PersistentInstruction(nodeRTProcess, origNodeRT.getId(), PersistentInstruction.ACTION_DELETE));
					//nodeRTProcess.doRemove(origNodeRT.getId());
				}
			}

			// 文档审批完成后删除所有节点状态
			if (instance.isComplete()) {
				//添加指令、完成流转后才执行更新数据库指令
				InstructionExecutor.getInstance().put(new PersistentInstruction(nodeRTProcess,"doRemoveByFlowStateRT",new Object[]{instance.getId()}));
				//nodeRTProcess.doRemoveByFlowStateRT(instance.getId());
			}


		}

		// 设置抄送人
		if (currNode instanceof ManualNode
				&& ((ManualNode) currNode).isCarbonCopy) {
			if (currNodeRT != null) {
				Collection<Circulator> circulatorList = nodeRTProcess.getCirculatorList(params, instance.getDocument(),currNodeRT, (ManualNode) currNode);
				if(!circulatorList.isEmpty()){
					Collection<BaseUser> userList = new ArrayList<BaseUser>();
					UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
                    for(Circulator cir : circulatorList){
                    	String userId = cir.getUserId();
                    	UserVO _cir = (UserVO) userProcess.doView(userId);
                    	userList.add(_cir);
                    }					
        			NotificationProcess notificationProcess = (NotificationProcess) ProcessFactory.createRuntimeProcess(NotificationProcess.class, doc.getApplicationid());
					notificationProcess.notifycarbonCopy2Circulator(doc, currNode, userList,FlowType.FLOW_TYPE_MESSAGE.get(flowOption),user);
				}
				
				
			}
		}

		updateFlowState(instance);
		// 更新图片
		updateImage(instance,currid,nextids);
	}
	
	public static void updateImage(FlowStateRT instance) throws Exception {
		updateImage(instance,null,null);
	}

	public static void updateImage(FlowStateRT instance,String currNodeId,String[] nextNodeIds) throws Exception {
		// 更新图片
		try {
			BillDefiVO flow = instance.getFlowVO();
			flow.clearDiagram(); // 清空流程图缓存
			if (flow != null) {
				// 改变流程图,改变当前节点,并改变已走线路
				FlowDiagram fd = changeFlowState(instance,currNodeId,nextNodeIds);
				// 设置图片背景色
				if (instance.isTerminated()) {
					fd.setBackground(new Color(255, 255, 200));
				}
				// 生成流程图
				toJpegImage(instance, fd);
				// 生成手机用流程图
				toFlowImage(instance, fd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("{*[core.workflow.toImage.error]*}", e);
		}
	}

	/**
	 * 更新流程实例
	 */
	public static void updateFlowState(FlowStateRT instance) throws Exception {
		if (instance == null) {
			return;
		}
		instance.setStateLabel("");
		instance.setAuditorNames("");
		instance.getDocument().setStateLabel("");
		instance.getDocument().setAuditorNames("");
		instance.getDocument().setStateLabelInfo(null);
		instance.getDocument().setPrevAuditNode(null);
		instance.getDocument().setPrevAuditUser(null);
		instance.getDocument().setState(instance.getId());
		instance.getDocument().setState(instance);
		instance.getDocument().setStateInt(instance.getState());
		
		if (instance.isTemp()) {// 持久化流程实例
			//添加指令、完成流转后才执行更新数据库指令
			InstructionExecutor.getInstance().put(new PersistentInstruction(getFlowStateRTProcess(instance.getDocument().getApplicationid()), instance, PersistentInstruction.ACTION_CREATE));
			instance.setTemp(false);
			/*getFlowStateRTProcess(instance.getDocument().getApplicationid())
					.doCreate(instance);*/
		} else {// 更新流程实例
			//添加指令、完成流转后才执行更新数据库指令
			InstructionExecutor.getInstance().put(new PersistentInstruction(getFlowStateRTProcess(instance.getDocument().getApplicationid()), instance, PersistentInstruction.ACTION_UPDATE));
			/*getFlowStateRTProcess(instance.getDocument().getApplicationid())
					.doUpdate(instance);*/
		}

	}

	private static boolean start2Next(StartNode currNode, Node nextNode,
			ParamsTable params, FlowStateRT instance, WebUser user,
			String flowOption, String attitude, int residual) throws Exception {
		if (nextNode instanceof ManualNode) {
			return toNext(params, instance, FlowType.START2RUNNING,
					FlowState.RUNNING, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof SubFlow) {
			return toNext(params, instance, FlowType.START2SUBFLOW,
					FlowState.RUNNING, currNode, nextNode, user, attitude,
					residual);
		}else if (nextNode instanceof StartNode) {
			throw new OBPMValidateException("{*[cn.myapps.core.workflow.tips.invalid_next_node]*}");
		}
		return false;
	}

	/**
	 * 挂起到恢复的节点
	 * 
	 * @throws Exception
	 */
	private static boolean suspend2Next(SuspendNode currNode, Node nextNode,
			ParamsTable params, FlowStateRT instance, WebUser user,
			String flowOption, String attitude, int residual) throws Exception {
		if (nextNode instanceof ManualNode) {
			return toNext(params, instance, FlowType.SUSPEND2RUNNING,
					FlowState.RUNNING, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof AutoNode) {
			return toNext(params, instance, FlowType.SUSPEND2AUTO,
					FlowState.SUSPEND, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof SubFlow) {
			return toNext(params, instance, FlowType.SUSPEND2SUBFLOW,
					FlowState.SUSPEND, currNode, nextNode, user, attitude,
					residual);
		}else if (nextNode instanceof StartNode) {
			throw new OBPMValidateException("{*[cn.myapps.core.workflow.tips.invalid_next_node]*}");
		}
		return false;
	}

	/**
	 * 子流程到下一个节点
	 * 
	 * @throws Exception
	 */
	private static boolean subFlow2Next(SubFlow currNode, Node nextNode,
			ParamsTable params, FlowStateRT instance, WebUser user,
			String flowOption, String attitude, int residual) throws Exception {
		if (nextNode instanceof ManualNode) {// Running->Running
			return toNext(params, instance, FlowType.SUBFLOW2RUNNING,
					FlowState.RUNNING, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof CompleteNode) {// Running->Complete
			return toNext(params, instance, FlowType.RUNNING2COMPLETE,
					FlowState.COMPLETE, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof AutoNode) {// Running->Auto
			return toNext(params, instance, FlowType.RUNNING2AUTO,
					FlowState.AUTO, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof SuspendNode) {// Running->Suspend
			return toNext(params, instance, FlowType.RUNNING2SUSPEND,
					FlowState.SUSPEND, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof SubFlow) {// Running->SubFlow
			return toNext(params, instance, FlowType.RUNNING2SUBFLOW,
					FlowState.SUBFLOW, currNode, nextNode, user, attitude,
					residual);
		}else if (nextNode instanceof StartNode) {
			throw new OBPMValidateException("{*[cn.myapps.core.workflow.tips.invalid_next_node]*}");
		}
		return false;
	}

	/**
	 * 手动节点到下一个节点
	 * 
	 * @throws Exception
	 */
	private static boolean manual2Next(ManualNode currNode, Node nextNode,
			ParamsTable params, FlowStateRT instance, WebUser user,
			String flowOption, String attitude, int residual) throws Exception {
		if (nextNode instanceof ManualNode) {// Running->Running
			return toNext(params, instance, flowOption, FlowState.RUNNING,
					currNode, nextNode, user, attitude, residual);
		} else if (nextNode instanceof CompleteNode) {// Running->Complete
			return toNext(params, instance, FlowType.RUNNING2COMPLETE,
					FlowState.COMPLETE, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof AutoNode) {// Running->Auto
			return toNext(params, instance, FlowType.RUNNING2AUTO,
					FlowState.AUTO, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof SuspendNode) {// Running->Suspend
			return toNext(params, instance, FlowType.RUNNING2SUSPEND,
					FlowState.SUSPEND, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof SubFlow) {// Running->SubFlow
			return toNext(params, instance, FlowType.RUNNING2SUBFLOW,
					FlowState.SUBFLOW, currNode, nextNode, user, attitude,
					residual);
		}else if (nextNode instanceof StartNode) {
			throw new OBPMValidateException("{*[cn.myapps.core.workflow.tips.invalid_next_node]*}");
		}
		return false;
	}

	/**
	 * 自动节点到下一个节点
	 * 
	 * @throws Exception
	 */
	private static boolean auto2Next(AutoNode currNode, Node nextNode,
			ParamsTable params, FlowStateRT instance, WebUser user,
			String flowOption, String attitude, int residual) throws Exception {
		if (nextNode instanceof ManualNode) {// Auto->Running
			return toNext(params, instance, FlowType.AUTO2RUNNING,
					FlowState.RUNNING, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof CompleteNode) {// Auto->Complete
			return toNext(params, instance, FlowType.AUTO2COMPLETE,
					FlowState.COMPLETE, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof SuspendNode) { // Auto->Suspend
			return toNext(params, instance, FlowType.AUTO2SUSPEND,
					FlowState.SUSPEND, currNode, nextNode, user, attitude,
					residual);
		} else if (nextNode instanceof AutoNode) { // Auto->Auto
			return toNext(params, instance, FlowType.AUTO2AUTO, FlowState.AUTO,
					currNode, nextNode, user, attitude, residual);
		} else if (nextNode instanceof SubFlow) { // Auto->SubFlow
			return toNext(params, instance, FlowType.AUTO2SUBFLOW,
					FlowState.SUBFLOW, currNode, nextNode, user, attitude,
					residual);
		}else if (nextNode instanceof StartNode) {
			throw new OBPMValidateException("{*[cn.myapps.core.workflow.tips.invalid_next_node]*}");
		}
		return false;
	}

	private static boolean toNext(ParamsTable params, FlowStateRT instance,
			String flowOption, int nextState, Node currNode, Node nextNode,
			WebUser user, String attitude, int residual) throws Exception {
		return flowTo(params, instance, currNode, nextNode, user, flowOption,
				attitude, residual, Environment.getInstance());
	}

	private synchronized static boolean flowTo(ParamsTable params,
			FlowStateRT instance, Node currNode, Node nextNode, WebUser user,
			String flowOption, String attitude, int residual, Environment evt)
			throws Exception {

		boolean isPassed = false;
		Document doc = instance.getDocument();
		BillDefiVO flowVO = instance.getFlowVO();
		try {
			NodeRTProcess nodeRTProcess = getNodeRTProcess(flowVO
					.getApplicationid());
			FlowDiagram fd = flowVO.toFlowDiagram();

			/*从内存中获取 减少数据库读取
			 * NodeRT currNodeRT = nodeRTProcess.doViewByNodeid(doc.getId(),
					instance.getId(), currNode.id);*/
			NodeRT currNodeRT = instance.getNodertByNodeid(currNode.id);

			// 当为开始节点,开启流程
			if (currNodeRT == null && currNode instanceof StartNode) {
				// 当前节点为开始节点
				currNodeRT = nodeRTProcess.doCreate(params, null, instance,
						currNode, flowOption, user);
			} else if (currNodeRT == null && currNode instanceof CompleteNode) {// 当前节点为完成节点
				currNodeRT = new NodeRT(instance, currNode,
						FlowType.RUNNING2COMPLETE);
			}

			if (currNodeRT == null) {
				DocumentProcess docProcess = (DocumentProcess) ProcessFactory
						.createRuntimeProcess(DocumentProcess.class, instance
								.getApplicationid());
				if (docProcess.isImpropriated(doc)) {// 乐观锁校验
					throw new OBPMValidateException(
							"{*[core.util.cannotsave]*}",
							new ImpropriateException(
									"{*[core.util.cannotsave]*}"));
				}
				return false;
			}

			// 创建新的NodeRT
			Date actionTime = new Date();

			// 清空审批人列表
			instance.getDocument().setAuditorList("");
			instance.setAuditorList("");

			// 根据当前节点角色检查是否通过
			String startnodeid = currNode.id;
			String endnodeid = nextNode.id;

			// 执行验证脚本
			Object obj = fd.validate(getRunner(doc, params, user), startnodeid,
					endnodeid);
			if (obj instanceof String && !StringUtil.isBlank(obj.toString())) {
				throw new OBPMValidateException(obj.toString(),
						new ValidationException(obj.toString()));
			}

			if (currNode instanceof ManualNode) {
				isPassed = nodeRTProcess.process(currNodeRT, user, flowOption, residual);

				// 会签模式 更新 Document的审批人
				if (!isPassed
						&& (Integer
								.parseInt(((ManualNode) currNode).passcondition) == ManualNode.PASS_CONDITION_AND || Integer
								.parseInt(((ManualNode) currNode).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND)) {
					String auditorList = instance
							.calculateAuditorList(currNodeRT);
					instance.getDocument().setAuditorList(auditorList);
					instance.setAuditorList(auditorList);
					instance.refreshNoderts(currNodeRT);
				}
			} else {
				isPassed = true;
			}

			if (FlowType.RUNNING2RUNNING_RETRACEMENT.equals(flowOption)
					|| FlowType.RUNNING2RUNNING_INTERVENTION.equals(flowOption)
					|| FlowType.RUNNING2RUNNING_TIMELIMIT.equals(flowOption)
					|| params.getParameterAsBoolean("TIMELIMIT")) {// 流程回撤、干预、超时操作
				isPassed = true;
			}

			if (isPassed) {
				if (isSelfLoop(currNodeRT, nextNode)
						|| !isNextNodeRTExist(instance, nextNode)) {
					// 处理回退和恢复
					if (flowOption.equals(FlowType.SUSPEND2RUNNING)
							|| flowOption.equals(FlowType.RUNNING2RUNNING_BACK)) {
					}

					// 控制聚合
					if (nodeRTProcess.isCreateAble(instance, currNodeRT,
							nextNode)) {
						try {
							instance.getNoderts(); // 由于获取是从数据库中查找，所以在处理前先获取已有的NodeRT
							State nodeState = StateCreator
									.getNodeState(nextNode); // 流程状态
							if (currNode instanceof AutoNode
									&& ((AutoNode) currNode).issplit) {
								params.setParameter("splitToken", currNode.id);
							}
							NodeRT newNodeRT = nodeState.process(params,
									currNodeRT, instance, user, flowOption);
							if (newNodeRT != null) {
								instance.addNodeRT(newNodeRT);
								instance.setState(nodeState.toInt());
							}
						} catch (Exception e) {
							if (e instanceof WorkflowException || e instanceof OBPMValidateException) {
								throw e;
							} else {
								e.printStackTrace();
								throw new OBPMValidateException(
										"{*[core.workflow.users.error]*}", e);
							}
						}
					}
				}

				IRunner runner = JavaScriptFactory.getInstance(params
						.getSessionid(), flowVO.getApplicationid());
				runner.initBSFManager(doc, params, user,
						new ArrayList<ValidateMessage>());

				// 执行流转动作脚本
				fd.runAction(getRunner(doc, params, user), startnodeid,
						endnodeid);

				// 更新前一条历史记录
				updatePreviousRelationHIS(instance, currNode, user, actionTime);

			}
			// 生成流程节点历史
			createOrUpdateRelationHIS(instance, currNode, nextNode, user,
					actionTime, attitude,params.getParameterAsString("_signature"),flowOption);
		} catch (ValidationException ve) {
			if (!StringUtil.isBlank(ve.getMessage())) {
				throw new OBPMValidateException(
						"{*[" + ve.getMessage() + "]*}",
						new ValidationException("{*[" + ve.getMessage() + "]*}"));
			} else {
				throw new OBPMValidateException(
						"{*[core.workflow.validation.error]*}", ve);
			}
		} catch (RunActionException re) {
			re.printStackTrace();
			throw new OBPMValidateException(
					"{*[core.workflow.runscript.error]*}", re);
		} catch (OBPMValidateException e) {
			throw e;
		}

		return isPassed;
	}

	private static boolean isSelfLoop(NodeRT currNodeRT, Node nextNode) {
		return currNodeRT.getNodeid().equals(nextNode.id);
	}

	private static IRunner getRunner(Document doc, ParamsTable params,
			WebUser user) throws Exception {
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
				doc.getApplicationid());
		runner.initBSFManager(doc, params, user,
				new ArrayList<ValidateMessage>());

		return runner;
	}

	/**
	 * 是否为下一个运行中的节点已存在
	 * 
	 * @param flowVO
	 *            流程
	 * @param doc
	 *            文档
	 * @param node
	 *            节点
	 * @return
	 * @throws Exception
	 */
	private static boolean isNextNodeRTExist(FlowStateRT instance, Node node)
			throws Exception {
		boolean isExist = false;// NodeRT已存在，不需要创建新的NodeRT！
		//从内存中的流程实例中获取，而不是直接查询数据库
		Collection<NodeRT> nextNodeRTList = instance.getNoderts();
		/*Collection<NodeRT> nextNodeRTList = getNodeRTProcess(
				instance.getApplicationid()).queryNodeRTByFlowStateIdAndDocId(
				instance.getId(), instance.getDocid());*/
		
		for (Iterator<NodeRT> iter2 = nextNodeRTList.iterator(); iter2
				.hasNext();) {
			NodeRT nrt = (NodeRT) iter2.next();
			if (nrt.getNodeid().equals(node.id)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	public static Collection<Node> getFirstNodeList(Document doc)
			throws Exception {
		Collection<Node> rtn = new ArrayList<Node>();

		BillDefiVO flowVO = doc.getState().getFlowVO();
		if (flowVO != null) {
			rtn = getFirstNodeList(doc.getId(), flowVO);
		}
		return rtn;
	}

	/**
	 * 获取第一步处理结点列表
	 * 
	 * @return Collection of ManualNodes、AutoNodes、...
	 * @throws Exception
	 */
	public static Collection<Node> getFirstNodeList(Document doc,
			String flowid, WebUser user, ParamsTable params) throws Exception {
		Collection<Node> rtn = new ArrayList<Node>();

		BillDefiProcess bp = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) bp.doView(flowid);
		if (flowVO != null) {
			rtn = flowVO.getFirstNodeList(doc, user, params);
		}
		return rtn;
	}

	public static Collection<Node> getFirstNodeList(String docid, String flowid)
			throws Exception {
		BillDefiProcess bp = (BillDefiProcess) ProcessFactory
				.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) bp.doView(flowid);
		return getFirstNodeList(docid, flowVO);

	}

	public static Collection<Node> getFirstNodeList(String docid,
			BillDefiVO flowVO) {
		ArrayList<Node> rtn = new ArrayList<Node>();
		FlowDiagram fd = flowVO.toFlowDiagram();

		Collection<StartNode> startNodeList = fd.getStartNodeList();
		for (Iterator<StartNode> iter = startNodeList.iterator(); iter
				.hasNext();) {
			StartNode startNode = (StartNode) iter.next();
			Collection<Node> nextNodeList = fd.getNextNodeList(startNode.id,null,null,null);

			for (Iterator<Node> iter2 = nextNodeList.iterator(); iter2
					.hasNext();) {
				Node nextNode = (Node) iter2.next();
				if (nextNode instanceof ManualNode) {
					ManualNode manualNode = (ManualNode) nextNode;
					rtn.add(manualNode);
				}
			}
		}
		return rtn;
	}

	public static Node getFirstNode(Document doc, BillDefiVO flowVO,
			WebUser user, ParamsTable params) throws Exception {
		Collection<Node> firstNodeList = flowVO.getFirstNodeList(doc, user,
				params);
		String selectFlow = params.getParameterAsString("selectFlow");
		String _nextids = params.getParameterAsString("_nextids");

		if (firstNodeList != null && firstNodeList.size() > 0) {
			if (!StringUtil.isBlank(_nextids)
					&& !StringUtil.isBlank(selectFlow)) {
				for (Iterator<Node> iterator = firstNodeList.iterator(); iterator
						.hasNext();) {
					Node node = (Node) iterator.next();
					if (node.id.equals(_nextids))
						return node;
				}
			}
			return (Node) firstNodeList.iterator().next();
		}
		return null;
	}

	/**
	 * 验证当前登录用户是否为某个用户的流程代理人
	 * 
	 * @param prinspalIdList
	 * @param flowId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static boolean isAgent(Collection<String> prinspalIdList,
			String flowId, WebUser user) throws Exception {
		for (String userId : prinspalIdList) {
			if (user.isAgent(userId, flowId)) {
				return true;
			}
		}
		return false;

	}

	public static Node getStartNodeByFirstNode(BillDefiVO flowVO, Node firstNode) {
		return (Node) getStartNodeListByFirstNode(flowVO, firstNode).iterator()
				.next();
	}

	/**
	 * 根据第一个节点获取开始节点
	 * 
	 * @param flowVO
	 * @param firstNode
	 * @return
	 */
	public static Collection<StartNode> getStartNodeListByFirstNode(
			BillDefiVO flowVO, Node firstNode) {
		ArrayList<StartNode> rtn = new ArrayList<StartNode>();
		FlowDiagram fd = flowVO.toFlowDiagram();

		// 获取所有endnodeid为firstNode的开始结点列表
		Collection<Element> ems = fd.getAllElements();
		for (Iterator<Element> iter = ems.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			if (element instanceof Relation) {
				Relation r = (Relation) element;
				if (r.endnodeid != null && r.endnodeid.equals(firstNode.id)) {
					Element em = fd.getElementByID(r.startnodeid);
					if (em != null && em instanceof StartNode) {
						rtn.add((StartNode) em);
					}
				}
			}
		}
		return rtn;
	}

	public static Collection<Node> getNextAllowedNode(
			Collection<String> limitStateLabelList, BillDefiVO flowVO,
			String currid) {
		Collection<Node> rtn = new ArrayList<Node>();
		FlowDiagram fd = flowVO.toFlowDiagram();
		Node currNode = fd.getNodeByID(currid);
		boolean issplit = false;
		if (currNode instanceof ManualNode) {
			issplit = ((ManualNode) currNode).issplit;
		}
		Collection<Node> nodeList = getNextAllowedNodeList(limitStateLabelList,
				flowVO, currid);

		if (nodeList.size() == 0) {
			return null;
		} else if (issplit) {
			rtn.addAll(nodeList);
		} else {
			if (nodeList.size() > 0) {
				rtn.add(nodeList.iterator().next());
			}
		}
		return rtn;
	}

	public static Collection<Node> getNextAllowedNodeList(
			Collection<String> limitStateLabelList, BillDefiVO flowVO,
			String currid) {
		Collection<Node> rtn = new ArrayList<Node>();
		FlowDiagram fd = flowVO.toFlowDiagram();
		Collection<Node> nextNodeList = fd.getNextNodeList(currid,null,null,null);
		for (Iterator<Node> iter = nextNodeList.iterator(); iter.hasNext();) {
			Node node = (Node) iter.next();
			if (isAllowed(limitStateLabelList, node)) {
				rtn.add(node);
			}
		}

		return rtn;
	}

	private static boolean isAllowed(Collection<String> limitStateLabelList,
			Node node) {
		return limitStateLabelList == null || limitStateLabelList.size() == 0
				|| limitStateLabelList.contains(node.statelabel);
	}

	public static ManualNode getNextManuleNode(BillDefiVO flowVO, String currid) {
		Collection<Node> nextNodeList = flowVO.getNextNodeList(currid,null,null,null);
		if (!nextNodeList.isEmpty()) {
			for (Iterator<Node> iter = nextNodeList.iterator(); iter.hasNext();) {
				Node node = (Node) iter.next();
				if (node instanceof ManualNode) {
					return ((ManualNode) nextNodeList.iterator().next());
				}
			}
		}

		return new ManualNode(flowVO.toFlowDiagram());
	}
	
	
	/**
	 * 获取当前用户有权限审批的节点(已过时)
	 * @param doc
	 * 		文档对象
	 * @param user
	 * 		当前用户
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static NodeRT getCurrUserNodeRT(Document doc, WebUser user) throws Exception {
		return getCurrUserNodeRT(doc, user, null);
	}

	/**
	 * 根据Document主键(id) 与流程(flow)主键(id),获取当前用户运行时节点
	 * 
	 * @param doc
	 * @param user
	 *            web用户对象
	 * 
	 * @return 当前用户运行时节点
	 * @throws Exception
	 */
	public static NodeRT getCurrUserNodeRT(Document doc, WebUser user,
			String defaultNodeId) throws Exception {
		FlowStateRT state = doc.getState();

		if (state != null) {
			Collection<NodeRT> temp = new ArrayList<NodeRT>();
			Collection<NodeRT> nodertList = state.getNoderts();
			// 获取当前用户有权限审批的流程节点
			for (Iterator<NodeRT> iter = nodertList.iterator(); iter.hasNext();) {
				NodeRT nodert = (NodeRT) iter.next();
				Iterator<ActorRT> it = nodert.getPendingActorRTList()
						.iterator();
				while (it.hasNext()) {
					ActorRT actorrt = (ActorRT) it.next();
					// 节点处理人是否有登录用户或登录用户代理了此流程
					if ((actorrt).isEquals(user)
							|| user.isAgent(actorrt.getActorid(), state
									.getFlowid())) {
						temp.add(nodert);
					}
				}
			}
			if (!temp.isEmpty()) {
				if (!StringUtil.isBlank(defaultNodeId)) {// 获取指定的流程节点
					for (Iterator<NodeRT> iterator = temp.iterator(); iterator
							.hasNext();) {
						NodeRT nodeRT = iterator.next();
						if(nodeRT.getNodeid().equals(defaultNodeId)){
							return nodeRT;
						}
					}
				}
				//返回当前用户有权限处理的第一个节点
				return (NodeRT) temp.toArray()[0];
				
			}
		}
		return null;
	}

	/**
	 * 获取回退结点列表（按历史痕迹回退|定制回退）
	 * 
	 * @return Collection of NodeHises
	 * @throws Exception
	 */
	public static Collection<Node> getBackToNodeList(Document doc,
			BillDefiVO flowVO, NodeRT currNodeRT, WebUser user)
			throws Exception {
		FlowDiagram fd = flowVO.toFlowDiagram();
		Node node = (Node) fd.getElementByID(currNodeRT.getNodeid());
		if (node instanceof ManualNode) {
			if (((ManualNode) node).backType == 1 && ((ManualNode) node).cBack) {
				return getBackToNodeList(flowVO, currNodeRT, user, 0);
			} else if (((ManualNode) node).backType == 0) {
				return getBackToNodeList(doc, flowVO, currNodeRT, user, 0);
			} else
				return getBackToNodeList(doc, flowVO, currNodeRT, user, 0);
		}

		// 非手工节点回退(如：挂起)
		return StateMachine.getBackToNodeList(doc, flowVO, currNodeRT, user,
				FlowState.SUSPEND);
	}

	/**
	 * 获取回退结点列表(按历史痕迹回退)
	 * 
	 * @param doc
	 * @param flowVO
	 * @param currNodeRT
	 * @param user
	 * @param flowState
	 * @return
	 * @throws Exception
	 */
	public static Collection<Node> getBackToNodeList(Document doc,
			BillDefiVO flowVO, NodeRT currNodeRT, WebUser user, int flowState)
			throws Exception {
		ArrayList<Node> rtn = new ArrayList<Node>();
		FlowDiagram fd = flowVO.toFlowDiagram();

		Collection<RelationHIS> hisList = null;
		if (flowState != FlowState.SUSPEND) {
			Collection<Node> backNodeList = getBackToNodeList(
					new LinkedHashSet<Node>(), doc,currNodeRT
							.getNodeid());
			backNodeList.remove(fd.getElementByID(currNodeRT.getNodeid()));
			return backNodeList;
		} else {
			hisList = getRelationHISProcess(flowVO.getApplicationid())
					.queryRelationHIS(doc.getId(), flowVO.getId(),
							currNodeRT.getNodeid());
			if (!hisList.isEmpty()) {
				RelationHIS r = (RelationHIS) hisList.iterator().next();
				String sid = r.getStartnodeid();
				Element em = fd.getElementByID(sid);
				if (em instanceof ManualNode
						&& !sid.equals(currNodeRT.getNodeid())) {
					rtn.add((Node) em);
				}
			}
		}
		return rtn;
	}

	/**
	 * 根据流程历史获取当前登录用户审批过的上一步节点
	 * 
	 * @param doc
	 * @param flowVO
	 * @param currid
	 * @param user
	 * @param flowState
	 * @return
	 * @throws Exception
	 * @author Happy
	 */
	public static Node getBackNodeByHis(Document doc, BillDefiVO flowVO,
			String currid, WebUser user, int flowState) throws Exception {
		Node rnt = null;
		boolean jumpout = false;
		FlowDiagram fd = flowVO.toFlowDiagram();
		Node currNode = (Node) fd.getElementByID(currid);
		Collection<Node> backStepNodes = fd.getBackSetpNode(currNode);
		Collection<Node> hisNodes = new ArrayList<Node>();
		// //

		Collection<RelationHIS> hisList = null;
		hisList = getRelationHISProcess(flowVO.getApplicationid())
				.queryRelationHIS(doc.getId(), flowVO.getId(), currid);
		if (!hisList.isEmpty()) {
			Iterator<RelationHIS> resits = hisList.iterator();
			RelationHIS r = new RelationHIS();
			while (resits.hasNext()) {
				r = resits.next();
				if (r.getFlowOperation().equals(
						FlowType.RUNNING2RUNNING_RETRACEMENT)) {
					continue;
				} else {
					break;
				}
			}
			String sid = r.getStartnodeid();
			Element em = fd.getElementByID(sid);
			Collection<String> userIdList = r.getUserIdList();
			if (em instanceof ManualNode && !sid.equals(currid)
					&& userIdList.contains(user.getId())) {
				hisNodes.add((Node) em);
			}
		}

		// //
		for (Iterator<Node> iterator = hisNodes.iterator(); iterator.hasNext();) {
			Node hisnode = (Node) iterator.next();
			for (Iterator<Node> iterator2 = backStepNodes.iterator(); iterator2
					.hasNext();) {
				Node node = (Node) iterator2.next();
				if (hisnode.id.equals(node.id)) {
					rnt = node;
					jumpout = true;
					break;
				}

			}
			if (jumpout)
				break;

		}
		return rnt;

	}

	/**
	 * 获取回退结点列表(定制回退)
	 * 
	 * @param flowVO
	 * @param currNodeRT
	 * @param user
	 * @param flowState
	 * @return
	 * @throws Exception
	 */
	public static Collection<Node> getBackToNodeList(BillDefiVO flowVO,
			NodeRT currNodeRT, WebUser user, int flowState) throws Exception {
		Collection<Node> rtn = new ArrayList<Node>();
		FlowDiagram fd = flowVO.toFlowDiagram();
		ManualNode node = (ManualNode) fd
				.getElementByID(currNodeRT.getNodeid());
		// 获取当前NodeRT
		if (flowState != FlowState.SUSPEND
				&& !StringUtil.isBlank(node.bnodelist)) {
			String[] bnodelist = node.bnodelist.split(";");
			for (int i = 0; i < bnodelist.length; i++) {
				String ns = bnodelist[i];
				String nodeid = ns.substring(0, 13);
				rtn.add((Node) fd.getElementByID(nodeid));
			}
		}
		return rtn;

	}

	/**
	 * 按流程历史拿回退节点列表
	 * @param tmp
	 * @param doc
	 * @param endnodeid
	 * @return
	 * @throws Exception
	 */
	public static Collection<Node> getBackToNodeList(Set<Node> tmp,
			Document doc, String endnodeid) throws Exception {
		
		FlowDiagram fd = doc.getState().getFlowVO().toFlowDiagram();
		// 获取文档在当前流程实例下的所有流程处理历史
		Collection<RelationHIS> rhs = getRelationHISProcess(
				doc.getApplicationid()).doQueryByDocIdAndFlowStateId(
				doc.getId(), doc.getStateid());
		if (rhs != null && !rhs.isEmpty()) {
			//递归获取已审批的历史节点
			getBackNode(tmp, rhs, fd, endnodeid);
		}
		return tmp;
	}

	private static Collection<Node> getBackNode(Set<Node> rtn,
			Collection<RelationHIS> rhs, FlowDiagram fd, String endnodeid)
			throws Exception {

		if (rhs != null && !rhs.isEmpty()) {
			for (Iterator<RelationHIS> iter = rhs.iterator(); iter.hasNext();) {
				RelationHIS his = (RelationHIS) iter.next();
				if (his.getEndnodeid().equals(endnodeid)) {
					Relation rel = fd.getRelation(his.getStartnodeid(), his
							.getEndnodeid());
					if (his.getStartnodeid().equals(his.getEndnodeid())) { // 自连接跳出循环
						continue;
					}
					if (rel != null) {
						Element em = fd.getElementByID(his.getStartnodeid());
						if (em instanceof ManualNode && rtn.add((Node) em)) {
							getBackNode(rtn, rhs, fd, his.getStartnodeid());
						} else if (em instanceof AutoNode) { // 跳过
							getBackNode(rtn, rhs, fd, his.getStartnodeid());
						}
					}
				}
			}
		}
		return rtn;
	}

	/**
	 * 获取所有经过的节点
	 * 
	 * @param docid
	 * @param flowid
	 * @param currid
	 * @return
	 * @throws Exception
	 */
	public static Collection<RelationHIS> getPassedToNodeList(
			Map<String, RelationHIS> tmp, String docid, BillDefiVO flowVO,
			String startnodeid, String endnodeid) throws Exception {
		FlowDiagram fd = flowVO.toFlowDiagram();

		// 获取所有终点为当前NodeRT的RelationHIS
		Collection<RelationHIS> rhs = getRelationHISProcess(
				flowVO.getApplicationid()).queryRelationHIS(docid,
				flowVO.getId(), endnodeid);

		if (rhs != null && !rhs.isEmpty()) {
			for (Iterator<RelationHIS> iter = rhs.iterator(); iter.hasNext();) {
				RelationHIS his = (RelationHIS) iter.next();
				String snodeid = his.getStartnodeid();
				String enodeid = his.getEndnodeid();
				Element em = fd.getElementByID(snodeid);
				// 检验历史记录是否重复,不重复则把历史记录保存的tmp中
				if ((!tmp.isEmpty() && tmp.get(his.getId()) != null)
						|| em instanceof StartNode) {
					continue;
				} else {
					tmp.put(his.getId(), his);
					// 递归查找上一节点的历史记录
					getPassedToNodeList(tmp, docid, flowVO, enodeid, snodeid);
				}
			}
		}
		return tmp.values();
	}

	/**
	 * 去掉重复的Node
	 * 
	 * @param colls
	 * @return
	 */
	public static Collection<Node> removeDuplicateNode(Collection<Node> colls) {
		return removeDuplicateNode(colls, null);
	}

	/**
	 * 去掉重复和当前的Node
	 * 
	 * @param colls
	 * @return
	 */
	public static Collection<Node> removeDuplicateNode(Collection<Node> colls,
			Node currNode) {
		Collection<Node> tmp = new ArrayList<Node>();
		out: for (Iterator<Node> iter = colls.iterator(); iter.hasNext();) {
			Node node = (Node) iter.next();
			for (Iterator<Node> iterator = tmp.iterator(); iterator.hasNext();) {
				Node tmpNode = (Node) iterator.next();
				if (tmp.size() == 0) {
					tmp.add(node);
				} else {
					if ((node.id).equals(tmpNode.id)) {
						continue out;
					} else if (currNode != null) {
						if ((currNode.id).equals(node.id)) {
							continue out;
						}
					} else {
						continue;
					}
				}
			}
			tmp.add(node);
		}
		return tmp;
	}

	/**
	 * 根据流程，结点主键获取，当前的结点
	 * 
	 * @param flowVO
	 *            流程
	 * @param nodeid
	 *            主键
	 * @return 当前的结点
	 */
	public static Node getCurrNode(BillDefiVO flowVO, String nodeid) {
		Node currnode = null;
		if (flowVO != null) {
			FlowDiagram fd = flowVO.toFlowDiagram();

			Element element = fd.getElementByID(nodeid);

			if (element instanceof Node) {
				currnode = (Node) element;
			}
		}
		return currnode;
	}

	/**
	 * 删除运行时节点
	 * 
	 * @param docid
	 * @param flowid
	 * @throws Exception
	 */
	public static void removeAllNodeRT(String docid, String flowStateId,
			String applicationId) throws Exception {
		Collection<NodeRT> nodertList = getAllNodeRT(docid, flowStateId,
				applicationId);
		for (Iterator<NodeRT> iter = nodertList.iterator(); iter.hasNext();) {
			NodeRT nodert = (NodeRT) iter.next();
			getNodeRTProcess(applicationId).doRemove(nodert.getId());
		}
	}

	/**
	 * 根据文档与相应文档流程，获取所有运行时节点
	 * 
	 * @param docid
	 *            文档主键
	 * @param flowid
	 *            文档流程
	 * @return 所有运行时节点
	 * @throws Exception
	 */
	public static Collection<NodeRT> getAllNodeRT(String docid,
			String flowStateId, String applicationId) throws Exception {
		return getNodeRTProcess(applicationId).doQuery(docid, flowStateId);
	}

	/**
	 * 根据流程实例 获取流程节点的状态标识(statelabel)
	 * 
	 * @param instance
	 *            流程实例
	 * @return
	 * @throws Exception
	 */
	public static Collection<String> getNodeRTStateLabelRTs(FlowStateRT instance)
			throws Exception {
		Collection<String> rtn = new ArrayList<String>();
		if (instance == null) {
			return rtn;
		}
		BillDefiVO flowVO = instance.getFlowVO();
		Collection<NodeRT> colls = StateMachine.getAllNodeRT(instance
				.getDocid(), instance.getId(), instance.getApplicationid());

		for (Iterator<NodeRT> iter = colls.iterator(); iter.hasNext();) {
			NodeRT nodert = (NodeRT) iter.next();
			String nodeid = nodert.getNodeid();
			Node currNode = getCurrNode(flowVO, nodeid);
			if (currNode != null) {
				rtn.add(currNode.statelabel);
			}
		}
		return rtn;
	}

	/**
	 * 查找文档流程状态
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            文档流程 id
	 * @return 当前文档流程状态
	 * @throws Exception
	 */
	public static FlowStateRT getFlowStateRT(String docid, String flowid,
			String applicationId) throws Exception {
		return getFlowStateRTProcess(applicationId)
				.findFlowStateRTByDocidAndFlowid(docid, flowid);
	}

	/**
	 * 新建流程状态(转移到 flowstateProcess 可删除)
	 * 
	 * @param flowVO
	 * @param docid
	 * @return
	 * @throws Exception
	 */
	public static FlowStateRT createFlowStateRT(BillDefiVO flowVO, String docid)
			throws Exception {
		FlowStateRT state = new FlowStateRT();
		state.setId(Sequence.getSequence());
		state.setDocid(docid);
		state.setFlowid(flowVO.getId());
		state.setApplicationid(flowVO.getApplicationid());
		getFlowStateRTProcess(flowVO.getApplicationid()).doCreate(state);

		return state;
	}

	/**
	 * 新建流程状态
	 * 
	 * @param flowVO
	 * @param docid
	 * @return
	 * @throws Exception
	 */
	public static FlowStateRT createFlowStateRT(BillDefiVO flowVO,
			String docid, FlowStateRT parent) throws Exception {
		FlowStateRT state = new FlowStateRT();
		state.setId(Sequence.getSequence());
		state.setDocid(docid);
		state.setFlowid(flowVO.getId());
		state.setApplicationid(flowVO.getApplicationid());
		state.setParent(parent);
		getFlowStateRTProcess(flowVO.getApplicationid()).doCreate(state);

		return state;
	}

	/**
	 * 更新前一条历史记录
	 * 
	 * @param docid
	 * @param flowVO
	 * @param startNode
	 * @param user
	 * @param actionTime
	 * @throws Exception
	 */
	public static void updatePreviousRelationHIS(FlowStateRT instance,
			Node startNode, WebUser user, Date actionTime) throws Exception {
		RelationHISProcess process = getRelationHISProcess(instance
				.getApplicationid());
		// 找到最后一条历史记录
		RelationHIS lastRelHis = process.doViewLastByEndNode(instance
				.getDocid(), instance.getId(), startNode.id);

		if (lastRelHis != null) {
			lastRelHis.setProcesstime(actionTime);
			lastRelHis.setAuditor(user.getId()); // 最后审批用户
			//添加到指令队列
			InstructionExecutor.getInstance().put(new PersistentInstruction(process, lastRelHis, PersistentInstruction.ACTION_UPDATE));
			//process.doUpdate(lastRelHis);
		}
	}

	/**
	 * 生成流程节点历史
	 * 
	 * @param docid
	 * @param flowVO
	 * @param startNode
	 * @param endNode
	 * @param user
	 * @param actionTime
	 * @param attitude
	 * @param flowOption
	 * @return
	 * @throws Exception
	 */
	public static RelationHIS createOrUpdateRelationHIS(FlowStateRT instance,
			Node startNode, Node endNode, WebUser user, Date actionTime,
			String attitude,String signature, String flowOption) throws Exception {
		RelationHISProcess process = getRelationHISProcess(instance
				.getApplicationid());
		
		if(startNode instanceof StartNode && endNode instanceof ManualNode){
			return null ;
		}

		RelationHIS his = process.doViewLastByDocIdAndFolowStateId(instance
				.getDocid(), instance.getId());
		if (his != null && his.startnodeid.equals(startNode.id)
				&& his.endnodeid.equals(endNode.id)) {
			// 会签则在同一历史记录上添加用户
			ActorHIS actorHIS = null;
			if (user.getEmployer() != null) {
				actorHIS = new ActorHIS((new WebUser(user.getEmployer())));
				actorHIS.setAgentid(user.getId());
				actorHIS.setAgentname(user.getName());
			} else {
				actorHIS = new ActorHIS(user);
			}
			actorHIS.setProcesstime(actionTime);
			actorHIS.setAttitude(attitude);
			actorHIS.setSignature(signature);
			his.getActorhiss().add(actorHIS);
			StringBuffer _attitude = new StringBuffer();
			_attitude.append(his.getAttitude());
			_attitude.append(",").append(attitude);
			his.setAttitude(_attitude.toString()); // 审批意见
			//添加到指令队列
			InstructionExecutor.getInstance().put(new PersistentInstruction(process, his, PersistentInstruction.ACTION_UPDATE));
			//process.doUpdate(his);
			return his;
		} else {
			// 新建历史记录
			RelationHIS rhis = new RelationHIS();
			rhis.setId(Sequence.getSequence());
			rhis.setFlowStateId(instance.getId());
			rhis.setFlowid(instance.getFlowid());
			rhis.setFlowname(instance.getFlowVO().getSubject());
			rhis.setDocid(instance.getDocid());
			rhis.setStartnodeid(startNode.id);
			rhis.setStartnodename(startNode.name);
			rhis.setEndnodeid(endNode.id);
			rhis.setEndnodename(endNode.name);
			rhis.setIspassed(false);
			rhis.setActiontime(actionTime);
			rhis.setAttitude(attitude); // 审批意见
			rhis.setAuditor(user.getId());
			ActorHIS actorHIS = null;
			if (user.getEmployer() != null) {
				actorHIS = new ActorHIS((new WebUser(user.getEmployer())));
				actorHIS.setAgentid(user.getId());
				actorHIS.setAgentname(user.getName());
			} else {
				actorHIS = new ActorHIS(user);
			}
			actorHIS.setProcesstime(actionTime);
			actorHIS.setAttitude(attitude);
			actorHIS.setSignature(signature);
			rhis.getActorhiss().add(actorHIS);
			rhis.setFlowOperation(flowOption);
			rhis.setReminderCount(0);
			//添加到指令队列
			InstructionExecutor.getInstance().put(new PersistentInstruction(process, rhis, PersistentInstruction.ACTION_CREATE));
			//process.doCreate(rhis);

			return rhis;

		}
	}

	/**
	 * 生成流程图
	 * 
	 * @author Administrator
	 * 
	 * 
	 */
	public static void toJpegImage(FlowStateRT instance, FlowDiagram fd)
			throws Exception {
		Environment evt = Environment.getInstance();
		String path = DefaultProperty.getProperty("BILLFLOW_DIAGRAMPATH");
		String filepath = evt.getRealPath(path);

		File dir = new File(filepath);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new OBPMValidateException("Failed to create folder");
			}
		}
		filepath = filepath + instance.getId() + ".jpg.temp";
		// File file = new File(filepath);

		new ImageUtil(fd).toImage(filepath);
	}

	public static void toFlowImage(FlowStateRT instance, FlowDiagram fd)
			throws Exception {
		Environment evt = Environment.getInstance();
		String path = DefaultProperty.getProperty("BILLFLOW_DIAGRAMPATH");
		String filepath = evt.getRealPath(path);

		File dir = new File(filepath);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new OBPMValidateException("Failed to create folder");
			}
		}
		filepath = filepath + instance.getId() + "_m.png";
		File file = new File(filepath);
		new ImageUtil(fd).toMobileImage(file);
	}
	
	public static FlowDiagram changeFlowState(FlowStateRT instance)
			throws Exception {
		return changeFlowState(instance, null, null);
	}

	public static FlowDiagram changeFlowState(FlowStateRT instance,String currNodeId,String[] nextNodeIds)
			throws Exception {
		FlowDiagram fd = instance.getFlowVO().toFlowDiagram();
		Collection<RelationHIS> hisList = getRelationHISProcess(
				instance.getApplicationid()).doQueryByDocIdAndFlowStateId(
				instance.getDocid(), instance.getId());
		
		Node starNode = fd.getFirstNode();
		boolean  flag = true;
		for (Iterator<RelationHIS> iter = hisList.iterator(); iter.hasNext();) {
			RelationHIS rhis = (RelationHIS) iter.next();
			
			Relation relation ;
			
			//与开始节点有关的relation
			if(flag){
				relation = fd.getRelation(starNode.id, rhis.startnodeid);
				if (relation != null) {
					relation.ispassed = true;
				}
			  flag = false;
			}
			
			relation = fd.getRelation(rhis.startnodeid, rhis.endnodeid);
			if (relation != null) {
				relation.ispassed = true;
			}
		}
		if(!StringUtil.isBlank(currNodeId) && nextNodeIds!=null){
			for (int i = 0; i < nextNodeIds.length; i++) {
				Relation relation = fd.getRelation(currNodeId, nextNodeIds[i]);
				if (relation != null) {
					relation.ispassed = true;
				}
			}
			
			if(hisList.isEmpty()){
				Relation relation = fd.getRelation(starNode.id, currNodeId);
				if (relation != null) {
					relation.ispassed = true;
				}
			}
		}

		Collection<Node> nodes = fd.getAllNodes();
		Collection<NodeRT> nodeRTList = instance.getNoderts();

		Collection<String> noderts = new ArrayList<String>();
		for (Iterator<NodeRT> iter = nodeRTList.iterator(); iter.hasNext();) {
			NodeRT nodert = (NodeRT) iter.next();
			noderts.add(nodert.getNodeid());
		}

		for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			if (noderts.contains(node.id)) {
				node._iscurrent = true;
			} else {
				node._iscurrent = false;
			}

		}
		return fd;
	}

	public static String getNodeNameListStr(BillDefiVO flowVO, String nodeId) {
		FlowDiagram diagram = flowVO.toFlowDiagram();
		return ((ManualNode) diagram.getElementByID(nodeId))
				.getShortNameListStr();
	}

	public static String getNodeNameListStr(BillDefiVO flowVO, String[] nodeIds) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < nodeIds.length; i++) {
			if (nodeIds[i] != null && nodeIds[i].trim().length() > 0) {
				if (nodeIds[i].endsWith(";"))
					nodeIds[i] = nodeIds[i].substring(0,
							nodeIds[i].length() - 1);
				buffer.append(getNodeNameListStr(flowVO, nodeIds[i]));
			}
		}
		return buffer.toString();
	}

	/**
	 * 判断当前用户是否可以保存文档. 当返回true时为可以保存文档,否则返回false为不可以保存文档. 此实现根据Document id 与
	 * flow(流程) id 查询当前流程的状态.如果当前还没有流程状态,并且根据flow id 获取的第一节点列表不为空时,返回和true.
	 * 否则根据用户获取当前节点,若存在节点时,返回true.
	 * 
	 * @param doc
	 */
	public static boolean isDocSaveUser(Document doc, WebUser webUser,
			ParamsTable params) throws Exception {
		boolean rtn = false;
		if (doc != null && !StringUtil.isBlank(doc.getId())
				&& !StringUtil.isBlank(doc.getStateid())) {
			if (doc.getState() == null || doc.getStateInt() == 0) {
				BillDefiVO flowVO = doc.getState().getFlowVO();
				Collection<Node> firstNodeList = flowVO.getFirstNodeList(doc,
						webUser, params);
				if (firstNodeList != null && !firstNodeList.isEmpty()) {
					rtn = true;
				}
				if (doc.getForm()
						.getActivityByType(ActivityType.START_WORKFLOW) != null) {
					rtn = true;
				}
			} else {
				// 根据用户获取当前节点
				NodeRT nodert = getCurrUserNodeRT(doc, webUser, null);
				if (nodert != null) {
					rtn = true;
				}
			}
		} else {
			rtn = true;
		}
		return rtn;
	}

	/**
	 * 工作委托
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void commissionedWork(Document doc, ParamsTable params,
			WebUser user) throws Exception {
		NodeRTProcess nodeRTProcess = new NodeRTProcessBean(doc
				.getApplicationid());
		PendingProcess pendingProcess = new PendingProcessBean(doc
				.getApplicationid());
		DocumentProcess docProcess = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class, doc
						.getApplicationid());

		String _actorId = params.getParameterAsString("_actorId");
		if (_actorId == null || _actorId.trim().length() <= 0) {
			_actorId = user.getId();
		}
		String[] _newActors = params.getParameterAsString("_newActors").split(
				",");
		FlowStateRT state = doc.getState();
		boolean flag = false;
		try {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			Collection<NodeRT> noderts = state.getNoderts();
			for (Iterator<NodeRT> iterator = noderts.iterator(); iterator
					.hasNext();) {
				NodeRT nodeRT = iterator.next();
				if (nodeRT.getActorIdList().contains(_actorId)) {// 节点处理人包含被委托人
					flag = true;
					Collection<String> actorIdList = nodeRT.getActorIdList();
					actorIdList.remove(_actorId);
					for (String _newActor : _newActors) {
						if (!actorIdList.contains(_newActor))
							actorIdList.add(_newActor);
					}

					nodeRT = nodeRTProcess.doUpdateByActorIds(nodeRT, doc,
							flowVO, (String[]) actorIdList
									.toArray(new String[actorIdList.size()]),
							params);

					doc.setAuditorList(doc.calculateAuditorList(nodeRT));
				}

			}
			if (flag) {
				doc.setState((FlowStateRT) new FlowStateRTProcessBean(flowVO
						.getApplicationid()).doView(doc.getStateid()));
				docProcess.doUpdate(doc);
				pendingProcess.doUpdateByDocument(doc, user);
			}
		} catch (JSONException e) {
			log.warn("", e);
		}
	}

	public static void main(String[] args) throws Exception {
		// Node node = new CompleteNode(new FlowDiagram());
		// State state = getNodeState(node);
	}

}
