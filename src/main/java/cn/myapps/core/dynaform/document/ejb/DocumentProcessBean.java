package cn.myapps.core.dynaform.document.ejb;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.constans.Environment;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.DocumentException;
import cn.myapps.core.dynaform.document.dao.DocumentDAO;
import cn.myapps.core.dynaform.document.dql.DQLASTUtil;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.AttachmentUploadField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ImageUploadField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.form.ejb.WordField;
import cn.myapps.core.dynaform.form.ejb.mapping.TableMapping;
import cn.myapps.core.dynaform.pending.dao.PendingDAO;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.dynaform.work.ejb.WorkVO;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.WorkflowException;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.FlowTicketValidator;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.notification.ejb.NotificationProcess;
import cn.myapps.core.workflow.notification.ejb.NotificationProcessBean;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistory;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowReminderHistory;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowReminderHistoryProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.core.workflow.storage.runtime.intervention.ejb.FlowInterventionProcess;
import cn.myapps.core.workflow.storage.runtime.intervention.ejb.FlowInterventionProcessBean;
import cn.myapps.core.workflow.utility.ActivityPermissionList;
import cn.myapps.core.workflow.utility.FieldPermissionList;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.disk.ejb.NDirProcess;
import cn.myapps.km.disk.ejb.NDirProcessBean;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.OBPMDispatcher;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.CacheKey;
import cn.myapps.util.cache.ICacheProvider;
import cn.myapps.util.cache.IMyCache;
import cn.myapps.util.cache.IMyElement;
import cn.myapps.util.cache.MyCacheManager;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.pdf.ConvertHTML2Pdf;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.sequence.Sequence;
/**
 * DocumentProcessBean 为Document逻辑处理实现类.
 * 
 * @author Marky
 * 
 */
public class DocumentProcessBean extends AbstractRunTimeProcessBean<Document>
		implements DocumentProcess {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3115132096995666143L;
	public final static Logger log = Logger
			.getLogger(DocumentProcessBean.class);

	public DocumentProcessBean(String applicationId) {
		super(applicationId);
	}

	/**
	 * 根据主键,返回相应文档对象
	 * 
	 * @param pk
	 *            文档主键
	 * @return 文档对象
	 */
	public ValueObject doView(String pk) throws Exception {
		return ((DocumentDAO) getDAO()).find(pk);
	}

	public Document doStartFlowOrUpdate(final Document doc, ParamsTable params,
			WebUser user) throws Exception {

		if (isNotStart(doc, params)
				&& !StringUtil.isBlank(params.getParameterAsString("_flowid"))) { // 启动流程
			doStartFlow(doc, params, user);
		} else {
			doCreateOrUpdate(doc, user);
			// if(!StringUtil.isBlank(params.getParameterAsString("_isChangeAuditor"))){//优化性能
			// 只有需要改变流程处理人时才执行此方法
			// updateActorRTList(doc, params, user);
			// }
		}

		return doc;
	}

	public Document doNewWithOutItems(Form form, WebUser user,
			ParamsTable params) throws Exception {
		// 清空所有field参数
		Collection<String> fieldNames = form.getAllFieldNames();
		for (Iterator<String> iter = fieldNames.iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			params.removeParameter(name);
		}

		Document newDoc = form.createDocument(params, user);
		// doCreate(newDoc);

		return newDoc;
	}

	public Document doNew(Form form, WebUser user, ParamsTable params)
			throws Exception {
		Document newDoc = form.createDocument(params, user);
		// doCreate(newDoc);

		return newDoc;
	}
	
	public Document doNewWithFlowPermission(Form form, WebUser user, ParamsTable params)
			throws Exception {
		Document newDoc = form.createDocument(params, user);
		String flowId = form.getOnActionFlow();
		if(!StringUtil.isBlank(flowId)){
			BillDefiProcess billDefiProcess = (BillDefiProcess)ProcessFactory.createProcess(BillDefiProcess.class);
			BillDefiVO flowVO = (BillDefiVO) billDefiProcess.doView(flowId);
			if(flowVO != null){
				FlowDiagram fd = flowVO.toFlowDiagram();
				Node startNode = fd.getFirstNode();
				List<Node> nextNodes = (List<Node>) fd.getNextNodeList(startNode.id,newDoc,params,user);
				if(!nextNodes.isEmpty()){
					Node firstNode = nextNodes.get(0);
					if(firstNode instanceof ManualNode){
						ManualNode node = ((ManualNode) firstNode);
						if(!StringUtil.isBlank(node.fieldpermlist)) {
							JSONArray jsonArray = JSONArray.fromObject(StringUtil.dencodeHTML(node.fieldpermlist));
							if (!jsonArray.isEmpty()) {
								for (Iterator<?> iter = jsonArray.iterator(); iter.hasNext();) {
									JSONObject obj = (JSONObject) iter.next();
									String formid = (String) obj.get("formid");
									if (formid.equals(form.getId())) {
										String permissionStr = (String) obj.get("fieldPermList");
										FieldPermissionList fieldPermList = FieldPermissionList.parser(permissionStr);
										newDoc.setFieldPermList(fieldPermList);
										break;
									}
								}
							}
						}
						if(!StringUtil.isBlank(node.activityPermList)){
							JSONArray jsonArray = JSONArray.fromObject(StringUtil.dencodeHTML(node.activityPermList));
							if (!jsonArray.isEmpty()) {
								for (Iterator<?> iter = jsonArray.iterator(); iter.hasNext();) {
									JSONObject obj = (JSONObject) iter.next();
									String formid = (String) obj.get("formid");
									if (formid.equals(form.getId())) {
										JSONArray permissionMap = (JSONArray) obj.get("activityPermList");
										ActivityPermissionList activityPermList = ActivityPermissionList.parser(permissionMap);
										newDoc.setActivityPermList(activityPermList);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		return newDoc;
	}

	/**
	 * 根据用户更新所属用户相应的文档，并对Field 校验.若文档无状态并有流程时开启流程.
	 * 
	 * @param doc
	 *            Document对象
	 * @param params
	 *            参数对象
	 * @param user
	 *            用户对象
	 * @return Document
	 * @throws Exception
	 */
	public Document doStartFlow(final Document doc, ParamsTable params,
			WebUser user) throws Exception {
		try{
			FlowTicketValidator.valid(doc);
			FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory
					.createRuntimeProcess(FlowStateRTProcess.class, doc
							.getApplicationid());
			BillDefiProcess billDefiProcess = (BillDefiProcess) ProcessFactory
					.createProcess(BillDefiProcess.class);
			String _flowid = params.getParameterAsString("_flowid");
			if (!StringUtil.isBlank(_flowid) && !"null".equals(_flowid)) {
				BillDefiVO flowVO = (BillDefiVO) billDefiProcess.doView(_flowid);
				Node firstNode = StateMachine.getFirstNode(doc, flowVO, user,
						params);
				if (firstNode != null) {
					doc.setState(stateProcess.createTransientFlowStateRT(doc,
							_flowid, user));// 创建瞬态流程实例
					doc.setInitiator(user.getId());// 设置流程发起人
					Node startNode = StateMachine.getStartNodeByFirstNode(flowVO,
							firstNode);
					if (startNode != null) {
						String currNodeId = startNode.id;
						String[] nextNodeIds = new String[] { firstNode.id };
						stateProcess.doApprove(params, doc.getState(), currNodeId,
								nextNodeIds, FlowType.START2RUNNING, "",user);
					}
				} else {
					doCreateOrUpdate(doc, user);
				}
	
			} else {
				doCreateOrUpdate(doc, user);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			FlowTicketValidator.flush(doc);
		}
		return doc;
	}
	

	public Document doFlow(final Document doc, ParamsTable params,
			String currNodeId, String[] nextNodeIds, String flowOption,
			String comment, WebUser user) throws Exception {
		
		FlowStateRT instance = doc.getState(currNodeId);
		
		FlowTicketValidator.valid(doc);
		
//		beginTransaction();
		try {
			FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory
					.createRuntimeProcess(FlowStateRTProcess.class, doc
							.getApplicationid());
			String _flowid = params.getParameterAsString("_flowid");
			if((instance ==null && !StringUtil.isBlank(_flowid)) || (instance !=null && instance.isTemp())){
				
				if(stateProcess.findFlowStateRTByDocidAndFlowid(doc.getId(), _flowid) !=null){
					throw new OBPMValidateException("{*[core.form.submit.norepeat]*}", new ImpropriateException("{*[core.form.submit.norepeat]*}")) ;
				}
				//流程实例还未持久化，说明是新建文档且没有启动流程，需要启动流程。
				BillDefiProcess billDefiProcess = (BillDefiProcess) ProcessFactory
						.createProcess(BillDefiProcess.class);
				BillDefiVO flowVO = (BillDefiVO) billDefiProcess.doView(_flowid);
				Node firstNode = StateMachine.getFirstNode(doc, flowVO, user,
						params);
				if(currNodeId.equals(firstNode.id)){
					instance = stateProcess.createTransientFlowStateRT(doc,
							_flowid, user);
					doc.setState(instance);
					doc.setInitiator(user.getId());// 设置流程发起人
					Node startNode = StateMachine.getStartNodeByFirstNode(flowVO,
							firstNode);
					if (startNode != null) {
						String _currNodeId = startNode.id;
						String[] _nextNodeIds = new String[] { firstNode.id };
						stateProcess.doApprove(instance, _currNodeId, _nextNodeIds, FlowType.START2RUNNING, "", params, user, false,false);
					}
				}else {//当前提交的节点不是第一个节点，则抛出异常
					throw new OBPMValidateException("{*[Could not submit to]*},{*[please choose again]*}");
				}
			}else{
				
				//把从数据库查询改成从内存中获取
				NodeRT currNodeRT = instance.getNodertByNodeid(currNodeId);
				/*
				 NodeRTProcess nodeProcess = (NodeRTProcess)ProcessFactory.createRuntimeProcess(NodeRTProcess.class, doc.getApplicationid());
				 NodeRT currNodeRT = (NodeRT)nodeProcess.doViewByNodeid(doc.getId(), doc.getStateid(), currNodeId);
				 */
				
				if (!flowOption.equals(FlowType.RUNNING2RUNNING_RETRACEMENT)
						&& !FlowType.RUNNING2RUNNING_INTERVENTION.equals(flowOption))
					if (currNodeRT != null) {
						if (!isInNextNodeList(doc, currNodeRT, instance.getFlowVO(), user,
								nextNodeIds,flowOption)) {
							throw new OBPMValidateException("{*[Could not submit to]*} "
									+ StateMachine.getNodeNameListStr(instance.getFlowVO(),
											nextNodeIds) + " {*[please choose again]*}.",new DocumentException("{*[Could not submit to]*} "
									+ StateMachine.getNodeNameListStr(instance.getFlowVO(),
											nextNodeIds) + " {*[please choose again]*}.",
									doc)) ;
						}
					}
				
			}
			
			stateProcess.doApprove(params, instance, currNodeId, nextNodeIds,
					flowOption, comment, user);
//			commitTransaction();
		} catch (Exception e) {
//			rollbackTransaction();
			throw e;
		}finally{
			FlowTicketValidator.flush(doc);
		}
		return instance.getDocument();
	}

	/**
	 * 批量审批文档(下一个节点无任何限制)
	 * 
	 * @param docIds
	 *            文档ID数组
	 * @param user
	 *            当前用户
	 * @param evt
	 *            应用环境
	 * @param params
	 *            请求参数
	 * @return successCount 成功条数
	 * @throws Exception
	 */
	public int doBatchApprove(String[] docIds, WebUser user, Environment evt,
			ParamsTable params) throws Exception {
		return doBatchApprove(docIds, user, evt, params, null);
	}

	/**
	 * 批量审批文档
	 * 
	 * @param docIds
	 *            文档ID数组
	 * @param user
	 *            当前用户
	 * @param evt
	 *            应用环境
	 * @param params
	 *            请求参数
	 * @param allowedList
	 *            下一个节点限制列表
	 * @see cn.myapps.core.dynaform.activity.ejb.Activity#getApproveLimit();
	 * 
	 * @return successCount 成功条数
	 */
	public int doBatchApprove(String[] docIds, WebUser user, Environment evt,
			ParamsTable params, Collection<String> allowedList)
			throws Exception {
		if (docIds == null || docIds.length == 0) {
			throw new OBPMValidateException("{*[Please choose one]*}");
		}

		// BillDefiVO flowVO = null;
		FlowStateRT instance = null;
		FlowStateRTProcess stateProcess = null;

		int successCount = 0;
		int failCount = 0;

		for (int i = 0; i < docIds.length; i++) {
			Document doc = (Document) doView(docIds[i]);

			if (doc != null) {
				// -------------------------------- 选择可执行的流程实例
				if (!StringUtil.isBlank(doc.getStateid())) {
					stateProcess = (FlowStateRTProcess) ProcessFactory
							.createRuntimeProcess(FlowStateRTProcess.class, doc
									.getApplicationid());
					if (stateProcess.isMultiFlowState(doc)) {// 有多个流程实例
						FlowStateRT currInstance = stateProcess
								.getCurrFlowStateRT(doc, user, null);// 绑定一个用户可审批的文档实例
						if (currInstance == null) {
							currInstance = stateProcess
									.getParentFlowStateRT(doc);// 将文档加载到主流程实例
						}
						if (currInstance != null) {
							doc.setState(currInstance);
							// doc.setMulitFlowState(stateProcess.isMultiFlowState(doc,
							// user));//是否存在多个可执行实例
						}
					}
				}
				instance = doc.getState();

				if (instance != null) {
					try {
						IRunner runner = JavaScriptFactory.getInstance(params
								.getSessionid(), instance.getFlowVO()
								.getApplicationid());
						runner.initBSFManager(doc, params, user,
								new ArrayList<ValidateMessage>());

						String currNodeId = "";
						// FlowStateRT rt = doc.getState();
						if (instance != null) {
							NodeRT node = instance.getNodeRT(user);
							if (node != null)
								currNodeId = node.getNodeid();
						}

						Collection<Node> nextNodes = StateMachine
								.getNextAllowedNode(allowedList, instance
										.getFlowVO(), currNodeId);

						String flowOption = "";
						String[] nextNodeIds = new String[nextNodes.size()];
						int Count = 0;
						for (Iterator<Node> iter = nextNodes.iterator(); iter
								.hasNext();) {

							Node nextNode = iter.next();

							nextNodeIds[Count] = nextNode.id;

							doc.setLastFlowOperation(FlowType
									.getActionCode(nextNode));
							// 为批量审批时添加审批备注_attitude。 add by by dolly 2011-3-10

							flowOption = FlowType.getActionCode(nextNode);
							Count++;
							successCount++;

						}

						String comment = "";
						if (comment == null || comment.equals("")) {
							if (params.getParameterAsString("_attitude") != null
									&& !params
											.getParameterAsString("_attitude")
											.equals("")) {
								comment = params
										.getParameterAsString("_attitude");
							} else if (params.getParameterAsString("_remark") != null
									&& !params.getParameterAsString("_remark")
											.equals("")) {
								comment = params
										.getParameterAsString("_remark");
							}
							String actid = params
									.getParameterAsString("_activityid");
							if (actid != null && !actid.equals("")) {
								if (params.getParameterAsString("_attitude"
										+ actid) != null
										&& !params.getParameterAsString(
												"_attitude" + actid).equals("")) {
									comment = params
											.getParameterAsString("_attitude"
													+ actid);
								}
							}
						}
						stateProcess.doApprove(params, instance, currNodeId,
								nextNodeIds, flowOption, comment, user);// new
																				// String[]
																				// {
																				// nextNode.id
																				// }

					} catch (Exception e) {
						failCount++;
					}
				}
			}
		}

		log.info("Approve " + docIds.length + " document(s) with " + failCount
				+ " fail(s)");

		return successCount;
	}


	
	/**
	 * 流程催单
	 * @param doc
	 * 		文档对象
	 * @param nodertIds
	 * 		被催单的节点id
	 * @param reminderContent
	 * 		催单内容
	 * @param params
	 * 		参数表
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void doFlowReminder(Document doc,String[] nodertIds,String reminderContent,ParamsTable params,WebUser user) throws Exception{
		FlowReminderHistoryProcess reminderProcess = (FlowReminderHistoryProcess) ProcessFactory.createRuntimeProcess(FlowReminderHistoryProcess.class, doc.getApplicationid());
		NodeRTProcess nodertProcess = (NodeRTProcess) ProcessFactory.createRuntimeProcess(NodeRTProcess.class, getApplicationId());
		
		FlowStateRT instance = doc.getState();
		StringBuffer nodeName = new StringBuffer();
		Collection<NodeRT> noderts = instance.getNoderts();
		
		try {
			beginTransaction();
			
			for (Iterator<NodeRT> iterator = noderts.iterator(); iterator.hasNext();) {
				NodeRT nodeRT = iterator.next();
				for (int i = 0; i < nodertIds.length; i++) {
					if(nodeRT.getId().equals(nodertIds[i])){
						nodeName.append(nodeRT.getName()).append(",");
						//更新相关字段
						nodeRT.setReminderTimes(nodeRT.getReminderTimes()+1);
						nodertProcess.doUpdateReminderTimes(nodeRT);
						break;
					}
				}
			}
			if(nodeName.length()>0){
				nodeName.setLength(nodeName.length()-1);
			}
			//1.生成催单历史记录
			FlowReminderHistory his = new FlowReminderHistory();
			his.setContent(reminderContent);
			his.setUserId(user.getId());
			his.setUserName(user.getName());
			his.setNodeName(nodeName.toString());
			his.setDocId(doc.getId());
			his.setFlowInstanceId(instance.getId());
			his.setApplicationid(doc.getApplicationid());
			his.setDomainid(doc.getDomainid());
			his.setProcessTime(new Date());
			reminderProcess.doCreate(his);
			
			//3.发送消息
			NotificationProcess notificationProcess = (NotificationProcess) ProcessFactory.createRuntimeProcess(NotificationProcess.class, doc.getApplicationid());
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess)ProcessFactory.createProcess(SummaryCfgProcess.class);
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doViewByFormIdAndScope(doc.getFormid(),SummaryCfgVO.SCOPE_PENDING);
			String content = "";
			String title = "";
			if(summaryCfg !=null){
				title = "[催办]" + user.getName() + "对事项“" + summaryCfg.toSummay(doc, user) + "”发起催办提醒";
			} else {
				title = "[催办]" + user.getName() + "发起催办提醒";
			}
			content = reminderContent;
			notificationProcess.sendFlowReminderNotification(doc, nodertIds, title, content, params, user);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	/**
	 * 判断传入的一下步节点是否符合流程设计的流转规则
	 * @param doc
	 * @param currNodeRT
	 * @param flowVO
	 * @param user
	 * @param nextNodeIds
	 * @param flowOption
	 * @return
	 * @throws Exception
	 */
	private boolean isInNextNodeList(Document doc, NodeRT currNodeRT,
			BillDefiVO flowVO, WebUser user, String[] nextNodeIds,String flowOption)
			throws Exception {
		Collection<Node> nodeList = new ArrayList<Node>();
		if(flowOption.equals(FlowType.RUNNING2RUNNING_BACK)){
			Collection<Node> backNodeList = StateMachine.getBackToNodeList(doc,
					flowVO, currNodeRT, user);
			nodeList.addAll(backNodeList);
		}else{
			Collection<Node> nextNodeList = flowVO.getNextNodeList(currNodeRT.getNodeid(),doc,null,user);
			nodeList.addAll(nextNodeList);
		}

		for (int i = 0; i < nextNodeIds.length; i++) {

			boolean flag = false;

			for (Iterator<Node> iter = nodeList.iterator(); iter.hasNext();) {
				Node node = (Node) iter.next();
				if (nextNodeIds[i] != null && nextNodeIds[i].endsWith(";")) {
					nextNodeIds[i] = nextNodeIds[i].substring(0, nextNodeIds[i]
							.length() - 1);
				}
				if (nextNodeIds[i].equals("") || nextNodeIds[i].equals(node.id)) {
					flag = true;
					break;
				}
			}

			if (!flag) {
				return false;
			}
		}

		return true;
	}

	public boolean isDocSaveUser(Document doc, ParamsTable params, WebUser user)
			throws Exception {

		boolean isDocSaveUser = true;

		// if (!doc.getIstmp()) {
		if (doc.getParentid() != null && doc.getParentid().trim().length() > 0) {
			if ("false".equals(params.getParameterAsString("isedit"))) {
				isDocSaveUser = false;
			} else {
				isDocSaveUser = StateMachine.isDocSaveUser(doc.getParent(),
						user, params);
			}
		} else {
			isDocSaveUser = StateMachine.isDocSaveUser(doc, user, params);
		}
		// }

		return isDocSaveUser;
	}

	public Collection<ValidateMessage> doValidate(final Document doc,
			ParamsTable params, WebUser user) throws Exception {
		FormProcess fb = (FormProcess) ProcessFactory
				.createProcess(FormProcess.class);
		Form form = (Form) fb.doView(doc.getFormid());
		if (form != null) {
			return form.validate(doc, params, user);
		}
		return null;
	}

	/**
	 * 改变审批角色列表
	 * 
	 * @param doc
	 *            文档
	 * @param params
	 *            页面参数
	 * @param user
	 *            当前用户
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void updateActorRTList(Document doc, ParamsTable params, WebUser user)
			throws Exception {
		NodeRTProcess nodeRTProcess = new NodeRTProcessBean(getApplicationId());
		UserProcessBean userProcess = new UserProcessBean();
		PendingProcess pendingProcess = new PendingProcessBean(
				getApplicationId());
		NotificationProcess notificationProcess = new NotificationProcessBean(
				getApplicationId());
		FlowStateRTProcess fsProcess = (FlowStateRTProcess) ProcessFactory
				.createRuntimeProcess(FlowStateRTProcess.class,
						getApplicationId());

		FlowStateRT state = doc.getState();
		String currNodeId = params.getParameterAsString("_currid");
		if (StringUtil.isBlank(currNodeId) || state == null) {
			return;
		}

		String auditorJSON = doc.getAuditorList();
		String stateAuditor = state.getAuditorList(); // 获取原来审批人
		Collection<BaseUser> userList = new ArrayList<BaseUser>(); // 流程通知用户
		try {
			Map<?, ?> map = JSONObject.fromObject(auditorJSON);
			BillDefiVO flowVO = doc.getFlowVO();
			Collection<NodeRT> noderts = state.getNoderts();
			for (Iterator<NodeRT> iterator = noderts.iterator(); iterator
					.hasNext();) {
				// NodeRT nodeRT = (NodeRT) iterator.next();
				NodeRT nodeRT = iterator.next();
				Collection<?> actorIdList = (Collection<?>) map.get(currNodeId);
				if (nodeRT.getNodeid().equals(currNodeId)) {
					String[] actorIdArray = (String[]) actorIdList
							.toArray(new String[actorIdList.size()]);
					nodeRTProcess.doUpdateByActorIds(nodeRT, doc, flowVO,
							actorIdArray,params);
					if (actorIdArray.length > 0) {
						for (int i = 0; i < actorIdArray.length; i++) {
							if (stateAuditor.indexOf(actorIdArray[i]) == -1) {
								userList.add((BaseUser) userProcess
										.doView(actorIdArray[i]));
							}
						}
					}
					break;
				}
			}
			doc.setAuditorList(null);
			doc.setAuditorNames(null);
			state.setAuditorNames(null);
			state.setAuditorList(null);
			fsProcess.doUpdate(state);

			notificationProcess.notifyCurrentAuditors(doc, flowVO,
					userList, user);
			pendingProcess.doUpdateByDocument(doc, user);

		} catch (JSONException e) {
			log.warn("", e);
			throw e;
		}
	}

	/**
	 * 判断是否已占用
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public boolean isImpropriated(Document doc) throws Exception {
		return doc != null
				&& doc.getVersions() != (((DocumentDAO) getDAO())
						.findVersions(doc.getId()));
	}

	/**
	 * 将新的文档属性与旧的合并,并保留旧文档所有项目(Item)的值
	 * 
	 * @param dest
	 *            新Document
	 * @return 合并后的Document
	 * @throws Exception
	 */
	public Document mergePO(Document dest, WebUser webUser) throws Exception {
		if (dest != null && dest.getId() != null) {
			Document orig = (Document) ((DocumentDAO) getDAO()).find(dest
					.getId());
			if (orig == null && webUser != null) {
				orig = (Document) webUser.getFromTmpspace(dest.getId());
			}

			try {
				if (orig != null) {
//					dest.set_issubdoc(orig.get_issubdoc());
					dest.setApplicationid(orig.getApplicationid());
					dest.setAuditdate(orig.getAuditdate());
					dest.setAudituser(orig.getAudituser());
					//dest.setAudituserid(orig.getAudituserid());
					dest.setAuditusers(orig.getAuditusers());
					dest.setAuthor(orig.getAuthor());
					dest.setCreated(orig.getCreated());
					dest.setDomainid(orig.getDomainid());
					dest.setIstmp(orig.getIstmp());
					dest.setLastFlowOperation(orig.getLastFlowOperation());
					dest.setLastmodified(orig.getLastmodified());
					dest.setLastmodifier(orig.getLastmodifier());
					// dest.setEditAble(orig.isEditAble(webUser));
					dest.setParent(orig.getParent());
					dest.setParent(orig.getParent());
					dest.setInitiator(orig.getInitiator());
					dest.setMappingId(orig.getMappingId());
					dest.setMulitFlowState(orig.isMulitFlowState());
				}
				return dest;
			} catch (Exception e) {
				e.printStackTrace();
				return dest;
			}
		}
		return dest;
	}

	/**
	 * 根据符合DQL语句以及应用标识查询,返回文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * 
	 * @param dql
	 *            DQL语句
	 * @retur 文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQL(String dql, String domainid)
			throws Exception {
		return ((DocumentDAO) getDAO()).queryByDQL(dql, domainid);
	}

	/**
	 * 根据符合DQL语句以及应用标识查询,返回按设置缓存的此模块下符合条件的文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            DQL语句
	 * @return 设置缓存的此模块下符合条件的文档的DataPackage
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DataPackage<Document> queryByDQLWithCache(String dql, String domainid)
			throws Exception {

		DataPackage<Document> result = null;
		try {
			// signal.sessionSignal++;

			String cacheName = "cn.myapps.core.dynaform.document.ejb.DocumentProcessBean.queryByDQLWithCache(java.lang.String,java.lang.String)";

			ICacheProvider provider = MyCacheManager.getProviderInstance();
			if (provider != null) {
				if (provider.clearByCacheName(cacheName)) {
					JavaScriptFactory.clear();
					log.info("##CLEAN-CACHE-->>" + cacheName);
				}

				IMyCache cache = provider.getCache(cacheName);
				if (cache != null) {

					Class<?>[] parameterTypes = new Class<?>[2];
					parameterTypes[0] = String.class;
					parameterTypes[1] = String.class;

					Method method = DocumentProcessBean.class.getMethod(
							"queryByDQLWithCache", parameterTypes);

					Object[] methodParameters = new Object[2];
					methodParameters[0] = dql;
					methodParameters[1] = "";
					CacheKey cacheKey = new CacheKey(this, method,
							methodParameters);

					IMyElement cachedElement = (IMyElement) cache.get(cacheKey);

					if (cachedElement != null
							&& cachedElement.getValue() != null) {
						log.info("@@CACHED-METHOD-->>" + cacheKey);
						result = (DataPackage<Document>) cachedElement
								.getValue();
					} else {

						result = ((DocumentDAO) getDAO()).queryByDQL(dql,
								domainid);
						cache.put(cacheKey, result);

						return result;
					}
				}
			}

			return result;

		} catch (Exception t) {
			throw t;
		} finally {
			// signal.sessionSignal--;
			// if (signal.sessionSignal <= 0) {
			// PersistenceUtils.closeSession();
			// }
		}
	}

	/**
	 * 根据符合DQL语句以及应用标识查询并分页,返回文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            dql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @return 文档的DataPackage
	 * @throws Exceptio
	 */
	public DataPackage<Document> queryByDQLPage(String dql, int page,
			int lines, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryByDQLPage(dql, page, lines,
				domainid);
	}

	/**
	 * 根据符合DQL语句以及应用标识查询单个文档
	 * 
	 * @param dql
	 * @return
	 * @throws Exception
	 */
	public Document findByDQL(String dql, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).findByDQL(dql, domainid);
	}

	/**
	 * 根据符合SQL语句以及应用标识查询单个文档
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Document findBySQL(String sql, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).findBySQL(sql, domainid);
	}

	/**
	 * 根据符合DQL语句,参数以及应用标识查询,返回按设置缓存的此模块下符合条件的文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            查询语句
	 * @param params
	 *            参数对象
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @return 此模块下符合条件的文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQLWithCache(String dql,
			ParamsTable params, String domainid) throws Exception {
		DataPackage<Document> dpgs = ((DocumentDAO) getDAO()).queryByDQL(dql,
				params, domainid);
		return dpgs;
	}

	/**
	 * 获取DocumentDAO接口
	 * 
	 * @return DocumentDAO接口
	 * @throws Exception
	 */
	protected IRuntimeDAO getDAO() throws Exception {
		// return new OracleDocStaticTblDAO(getConnection());
		// ApplicationVO app=getApplicationVO(getApplicationId());

		return new RuntimeDaoManager().getDocStaticTblDAO(getConnection(),
				getApplicationId());
	}

	protected PendingDAO getPendingDAO() throws Exception {
		return (PendingDAO) new RuntimeDaoManager().getPendingDAO(
				getConnection(), getApplicationId());
	}

	/**
	 * 根据父文档ID(primary key)与子表单名查询，返回所属父Document的子Document集合.
	 * 
	 * @param parentid
	 *            父文档ID(primary key)
	 * @param formName
	 *            子表单名
	 * @return 所属父Document的子Document集合.
	 * @throws Exception
	 */
	public Collection<Document> queryByParentID(String parentid, String formName)
			throws Exception {
		return ((DocumentDAO) getDAO()).queryByParentID(parentid, formName);
	}

	/**
	 * 根据符合DQL语句,最后修改文档日期,以及应用标识查询并分页,返回文档的DataPackage.
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            DQL 语句
	 * @param date
	 *            最后修改文档日期
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示的行数
	 * @return 符合条件的文档的DDataPackage
	 * @throws Exception
	 */
	public Iterator<Document> queryByDQLAndDocumentLastModifyDate(String dql,
			Date date, int page, int lines, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryByDQLAndDocumentLastModifyDate(
				dql, date, page, lines, domainid);
	}

	public long getNeedExportDocumentTotal(String dql, Date date,
			String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).getNeedExportDocumentTotal(dql, date,
				domainid);

	}

	/**
	 * 根据符合DQL语句,参数以及应用标识查询,返回文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            DQL语句
	 * @param params
	 *            参数
	 * @return 符合条件的文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQL(String dql, ParamsTable params,
			String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryByDQL(dql, params, domainid);
	}

	/**
	 * 根据DQL语句,参数表以及应用标识查询 ,返回文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            DQL语句
	 * @param params
	 *            参数
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示的行数
	 * @return 符合条件的文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQLPage(String dql, ParamsTable params,
			int page, int lines, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryByDQLPage(dql, params, page,
				lines, domainid);
	}

	/**
	 * 根据DQL语句以及文档某字段名查询,返回此文档此字段总和
	 * 
	 * @param dql
	 *            dql语句
	 * @param fieldName
	 *            字段名
	 * @return 文档此字段总和
	 * @throws Exception
	 */
	public double sumByDQL(String dql, String fieldName, String domainid)
			throws Exception {
		return ((DocumentDAO) getDAO()).sumByDQL(dql, fieldName, domainid);
	}

	/**
	 * 根据符合DQL执行语句以及应用标识查询并分页,返回文档的数据集
	 * 
	 * @param dql
	 *            DQL语句
	 * @param pos
	 *            页码
	 * @param size
	 *            每页显示行数
	 * @return 文档的数据集
	 * @throws Exceptio
	 */
	public Iterator<Document> iteratorLimitByDQL(String dql, int pos, int size,
			String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).iteratorLimitByDQL(dql, pos, size,
				domainid);
	}

	/**
	 * 根据符合DQL语句以及应用标识查询并分页,返回文档的集合
	 * 
	 * @param dql
	 *            DQL语句
	 * @param pos
	 *            当前页码
	 * @param size
	 *            每页显示的行数
	 * @return 文档的集合
	 */
	public Collection<Document> queryLimitByDQL(String dql, int pos, int size,
			String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryLimitByDQL(dql, pos, size,
				domainid);
	}

	/**
	 * 根据符合DQL语句,模块名,参数以及应用标识查询并分页,返回按设置缓存的此模块下符合条件的文档的DataPackage.
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            DQL语句
	 * @param params
	 *            参数对象
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示的行数
	 * @return 此模块下符合条件的文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQLPageWithCache(String dql,
			ParamsTable params, int page, int lines, String domainid)
			throws Exception {

		DataPackage<Document> dpgs = queryByDQLPage(dql, params, page, lines,
				domainid);
		return dpgs;
	}

	/**
	 * 根据用户创建新Document
	 * 
	 * @param user
	 *            webuser
	 * @param vo
	 *            ValueObject
	 */
	public void doCreate(ValueObject vo, WebUser user) throws Exception {
		if (vo instanceof Document) {
			try {
				beginTransaction();
				// 1.持久化文档
				if (StringUtil.isBlank(vo.getId())) {
					vo.setId(Sequence.getSequence());
				}
				if (((Document) vo).getLastmodified() == null) {
					((Document) vo).setLastmodified(new Date());
				}
				Document doc = (Document) vo;
				doc.refreshFlowInstancesCache();
				doc.setIstmp(false);
				((DocumentDAO) getDAO()).createDocument((Document) vo);
				
				//如果单据存在流程实例， 才会去更新auth表
				String stateid = doc.getStateid();
				if (stateid!=null && stateid.trim().length()>0) {
					createAuth(doc);
				}
				
				// 2. 创建或更新待办列表
				PendingProcess pendingProcess = new PendingProcessBean(
						getApplicationId());
				pendingProcess.doCreateOrRemoveByDocument((Document) vo, user);

				// 3. 创建 流程干预信息
				if (vo instanceof Document
						&& !StringUtil.isBlank(((Document) vo).getStateid())) {
					FlowInterventionProcess interventionProcess = new FlowInterventionProcessBean(
							getApplicationId());
					interventionProcess.doCreateByDocument((Document) vo, user);
				}

				commitTransaction();

				// 4.成功持久化后刷新上下文保存的文档版本
				((Document) vo).setVersions(((Document) vo).getVersions() + 1);
			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}
		}
	}

	/**
	 * 根据主键,删除对应值对象
	 * 
	 * @param id
	 *            主键
	 */
	public void doRemove(String id) throws Exception {
		try {
			Document doc = (Document) ((DocumentDAO) getDAO()).find(id);

			beginTransaction();

			// 删除相关的flowStateRT
			FlowStateRTProcess instanceProcess = new FlowStateRTProcessBean(
					getApplicationId());
			Collection<FlowStateRT> instances = instanceProcess
					.getFlowStateRTsByDocId(id);
			if (instances != null && !instances.isEmpty()) {
				for (FlowStateRT instance : instances) {
					instanceProcess.doRemove(instance.getId());
				}
			}
			// 删除相关的Auth表中信息
			((DocumentDAO) getDAO()).removeAuthByDoc(doc);

			((DocumentDAO) getDAO()).removeDocument(doc);
			getPendingDAO().remove(id);

			// 删除 流程干预信息
			FlowInterventionProcess interventionProcess = new FlowInterventionProcessBean(
					getApplicationId());
			interventionProcess.doRemove(id);

			// 删除 流程代阅信息
			CirculatorProcess cProcess = (CirculatorProcess) ProcessFactory
					.createRuntimeProcess(CirculatorProcess.class,
							getApplicationId());
			cProcess.doRemoveByForeignKey("DOC_ID", id);
			getPendingDAO().removeByDocId(id);
			// 删除流程历史
			if (doc != null && !StringUtil.isBlank(doc.getStateid())) {
				RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory
						.createRuntimeProcess(RelationHISProcess.class,
								getApplicationId());
				hisProcess.doRemoveByDocument(id);
			}

			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	public void doCreate(ValueObject vo) throws Exception {
		if (vo instanceof Document) {
			try {
				if (StringUtil.isBlank(vo.getId())) {
					vo.setId(Sequence.getSequence());
				}
				// vo.setSortId(Sequence.getTimeSequence());

				if (((Document) vo).getLastmodified() == null) {
					((Document) vo).setLastmodified(new Date());
				}

				// ((Document) vo).setIstmp(false);

				Document doc = (Document) vo;
				doc.refreshFlowInstancesCache();
				/*
				 * 判断doc的父doc是否存在且非临时文档 如果校验父表单是否为临时会导致创建子文档时不显示
				 */
				// if (!checkParentExistAndNotIstmp(doc.getParentid())) {
				// doc.setIstmp(true);
				// } else {
				doc.setIstmp(false);
				// }

				beginTransaction();
				((DocumentDAO) getDAO()).createDocument((Document) vo);
				//如果单据存在流程实例， 才会去更新auth表
				String stateid = doc.getStateid();
				if (stateid!=null && stateid.trim().length()>0) {
					createAuth(doc);
				}
				// 2. 创建或更新待办列表
				PendingProcess pendingProcess = new PendingProcessBean(
						getApplicationId());
				pendingProcess.doCreateOrRemoveByDocument((Document) vo, null);
				commitTransaction();
				// 成功持久化后刷新上下文保存的文档版本
				((Document) vo).setVersions(((Document) vo).getVersions() + 1);

			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}
		} else {
			throw new OBPMValidateException("this is not Document ValueObject!");
		}
	}

	/**
	 * 创建或更新
	 * 
	 * @param vo
	 *            文档值对象
	 * 
	 * @param user
	 *            WebUser
	 * @throws Exception
	 */
	public void doCreateOrUpdate(ValueObject vo, WebUser user) throws Exception {
		if (((DocumentDAO) getDAO()).isExist(vo.getId())) {
			doUpdate(vo, user, true, true);
		} else {
			doCreate(vo, user);
		}
	}

	/**
	 * 用于Excel导入的文档创建或更新
	 * 
	 * @param vo
	 *            文档值对象
	 * 
	 * @param user
	 *            WebUser
	 * @throws Exception
	 */
	public void doCreateOrUpdate4ExcelImport(ValueObject vo, WebUser user)
			throws Exception {
		Document doc = (Document) vo;
		doc.setLastmodifier(user.getId());
		doc.setLastmodified(new Date());
		if (!doc.is_new()) {
			((DocumentDAO) this.getDAO()).updateDocument(doc);

		} else {
			doc.setIstmp(false);
			((DocumentDAO) this.getDAO()).createDocument(doc);
		}
	}

	/**
	 * 根据用户,更新文档对象.
	 * 
	 * @param object
	 *            值对象
	 * @param user
	 *            用户
	 */
	public void doUpdate(ValueObject object, WebUser user,
			boolean withVersionControl, boolean isUpdateVersion)
			throws Exception {
		if (object instanceof Document) {
			try {
				beginTransaction();
				
				Document doc = (Document) object;
				doc.refreshFlowInstancesCache();
				//如果单据存在流程实例， 才会去更新auth表
				String stateid = doc.getStateid();
				if (stateid!=null && stateid.trim().length()>0) {
					createAuth(doc);
				}

				doc.setLastmodifier(user.getId());
				doUpdate(doc, withVersionControl, isUpdateVersion);
				// 更新待办列表
				PendingProcess pendingProcess = new PendingProcessBean(
						getApplicationId());
				pendingProcess.doUpdateByDocument(doc, user);
				// 更新流程干预信息
				FlowInterventionProcess interventionProcess = new FlowInterventionProcessBean(
						getApplicationId());
				interventionProcess.doUpdateByDocument(doc, user);
				commitTransaction();
			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}
		}
	}

	/**
	 * 判断本文档的上级文档是否存在并且非临时文档
	 * 
	 * 2011-07-08
	 * 
	 * @author keezzm
	 * 
	 * @param parentId
	 * @return
	 */
	private boolean checkParentExistAndNotIstmp(String parentId) {
		try {
			if (StringUtil.isBlank(parentId)) {
				return true;
			} else {
				Document parent = (Document) doView(parentId);
				if (parent != null && !parent.getIstmp()) {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void doCreate(ValueObject[] vos) throws Exception {
		throw new OBPMValidateException("this method is not be realized!");
	}

	public void doCreate(Collection<ValueObject> vos) throws Exception {
		throw new OBPMValidateException("this method is not be realized!");
	}

	public DataPackage<Document> doQuery(ParamsTable params, WebUser user)
			throws Exception {
		throw new OBPMValidateException("this method is not be realized!");
	}

	/**
	 * 删除主键数组值对象组
	 * 
	 * @param pks
	 *            主键数组
	 */
	public void doRemove(String[] pks) throws Exception {
		try {
			beginTransaction();

			if (pks != null) {
				for (int i = 0; i < pks.length; i++) {
					String id = pks[i];
					if (id.endsWith(";"))
						id = id.substring(0, id.length() - 1);
					Document doc = (Document) doView(id);

					if (doc != null) {
						// 删除相关的flowStateRT
						FlowStateRTProcess instanceProcess = new FlowStateRTProcessBean(
								getApplicationId());
						Collection<FlowStateRT> instances = instanceProcess
								.getFlowStateRTsByDocId(id);
						if (instances != null && !instances.isEmpty()) {
							for (FlowStateRT instance : instances) {
								instanceProcess.doRemove(instance.getId());
							}
						}

						// 删除相关的Auth表中信息
						((DocumentDAO) getDAO()).removeAuthByDoc(doc);

						((DocumentDAO) getDAO()).removeDocument(doc);
						getPendingDAO().removeByDocId(doc.getId());

						// 删除 流程干预信息
						FlowInterventionProcess interventionProcess = new FlowInterventionProcessBean(
								getApplicationId());
						interventionProcess.doRemove(id);

						// 删除 流程代阅信息
						CirculatorProcess cProcess = (CirculatorProcess) ProcessFactory
								.createRuntimeProcess(CirculatorProcess.class,
										getApplicationId());
						cProcess.doRemoveByForeignKey("DOC_ID", id);

						// 删除流程历史
						if (!StringUtil.isBlank(doc.getStateid())) {
							RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory
									.createRuntimeProcess(
											RelationHISProcess.class,
											getApplicationId());
							hisProcess.doRemoveByDocument(id);
						}

					}
				}
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	public void doRemove(ValueObject vo) throws Exception {

		// 删除相关的flowStateRT
		FlowStateRTProcess instanceProcess = new FlowStateRTProcessBean(
				getApplicationId());
		Collection<FlowStateRT> instances = instanceProcess
				.getFlowStateRTsByDocId(vo.getId());
		if (instances != null && !instances.isEmpty()) {
			for (FlowStateRT instance : instances) {
				instanceProcess.doRemove(instance.getId());
			}
		}
		// 删除相关的Auth表中信息
		((DocumentDAO) getDAO()).removeAuthByDoc((Document) vo);

		((DocumentDAO) getDAO()).removeDocument((Document) vo);

		// 删除 流程干预信息
		FlowInterventionProcess interventionProcess = new FlowInterventionProcessBean(
				getApplicationId());
		interventionProcess.doRemove(vo.getId());

		// 删除 流程代阅信息
		CirculatorProcess cProcess = (CirculatorProcess) ProcessFactory
				.createRuntimeProcess(CirculatorProcess.class,
						getApplicationId());
		cProcess.doRemoveByForeignKey("DOC_ID", vo.getId());
		getPendingDAO().removeByDocId(vo.getId());
		
		// 删除流程历史
		if (!StringUtil.isBlank(((Document) vo).getStateid())) {
			RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory
					.createRuntimeProcess(RelationHISProcess.class,
							getApplicationId());
			hisProcess.doRemoveByDocument(vo.getId());
			FlowReminderHistoryProcess flowReminderHistoryProcess = (FlowReminderHistoryProcess)ProcessFactory.createRuntimeProcess(FlowReminderHistoryProcess.class, getApplicationId());
			flowReminderHistoryProcess.removeByDocument(vo.getId());
		}
	}

	public Collection<Document> doSimpleQuery(ParamsTable params)
			throws Exception {
		throw new OBPMValidateException("this method is not be realized!");
	}

	public Collection<Document> doSimpleQuery(ParamsTable params,
			String application) throws Exception {
		throw new OBPMValidateException("this method is not be realized!");
	}

	/**
	 * 更新文档对象数组
	 * 
	 * @param vos
	 *            文档对象数组
	 */

	public void doUpdate(ValueObject[] vos) throws Exception {
		throw new OBPMValidateException("this method is not be realized!");
	}

	/**
	 * 更新文档
	 * 
	 * @param vo
	 * 
	 */
	public void doUpdate(Collection<ValueObject> vos) throws Exception {
		throw new OBPMValidateException("this method is not be realized!");
	}

	/**
	 * 更新文档
	 * 
	 * @param doc
	 *            Document对象
	 * @param params
	 *            参数对象
	 * @param user
	 *            用户对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public void doUpdate(ValueObject vo) throws Exception {
		doUpdate(vo, false, false);
	}

	public void doUpdate(ValueObject vo, boolean withVersionControl,
			boolean isUpdateVersion) throws Exception {
		try {
			Document doc = (Document) vo;
			if (withVersionControl && isImpropriated(doc)) {
				throw new OBPMValidateException("{*[core.util.cannotsave]*}", new ImpropriateException("{*[core.util.cannotsave]*}")) ;
			}

			/*
			 * 判断doc的父doc是否存在且非临时文档
			 */
			if (!checkParentExistAndNotIstmp(doc.getParentid())) {
				doc.setIstmp(true);
			} else {
				doc.setIstmp(false);
			}

			beginTransaction();
			Document oldDoc = (Document) doView(doc.getId());

			// if (oldDoc != null) {// 版本并发控制
			// int currentVersion = oldDoc.getVersions();
			// int oldVersion = doc.getVersions();
			// if (oldVersion < currentVersion)
			// throw new
			// Exception("Version is inconsistent,you should update this document");
			// }

			doc.setLastmodified(new Date());
			
			Document compareDoc = new Document();
			boolean flag = compareDoc.compareFieldValue(oldDoc, doc);
			if (flag) {
				if (oldDoc != null
						&& !StringUtil.isBlank(oldDoc.getLastmodifier())) {
					doc.setLastmodifier(oldDoc.getLastmodifier());
				}
			}
			if (!isUpdateVersion) {
				doc.setVersions(doc.getVersions() - 1);
			}
			doc.refreshFlowInstancesCache();
			((DocumentDAO) getDAO()).updateDocument(doc);

			// 添加日志记录
			if (!flag) {
				TableMapping tableMapping = doc.getForm().getTableMapping();
				String tblname = tableMapping
						.getTableName(DQLASTUtil.TABEL_TYPE_LOG);
				boolean isLogTableExists = ((DocumentDAO) getDAO())
						.checkTable(tblname);
				if (isLogTableExists) {
					((DocumentDAO) getDAO()).createDocument(oldDoc,
							DQLASTUtil.TABEL_TYPE_LOG);
				}
			}

			commitTransaction();
			// 成功持久化后刷新上下文保存的文档版本
			((Document) vo).setVersions(((Document) vo).getVersions() + 1);
		}catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	public Document doViewByCondition(String formName, Map<?, ?> condition,
			WebUser user) throws Exception {
		StringBuffer dql = new StringBuffer();
		dql.append("$formname='" + formName + "'");
		for (Iterator<?> iterator = condition.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			dql
					.append(" AND " + entry.getKey() + "='" + entry.getValue()
							+ "'");
		}

		Document doc = findByDQL(dql.toString(), user.getDomainid());
		if (doc != null) {
			if (StringUtil.isBlank(doc.getId())) {
				return null;
			}
		}

		return doc;
	}

	/**
	 * 根据用户,更新文档对象.
	 * 
	 * @param object
	 *            值对象
	 * @param user
	 *            用户
	 */
	public void doUpdate(ValueObject object, WebUser user) throws Exception {
		if (object instanceof Document) {
			try {
				beginTransaction();

				Document doc = (Document) object;
				doc.refreshFlowInstancesCache();
				//如果单据存在流程实例， 才会去更新auth表
				String stateid = doc.getStateid();
				if (stateid!=null && stateid.trim().length()>0) {
					createAuth(doc);
				}

				doc.setLastmodifier(user.getId());
				doUpdate(doc);
				// 更新待办列表
				PendingProcess pendingProcess = new PendingProcessBean(
						getApplicationId());
				pendingProcess.doUpdateByDocument(doc, user);
				// 更新流程干预信息
				FlowInterventionProcess interventionProcess = new FlowInterventionProcessBean(
						getApplicationId());
				interventionProcess.doUpdateByDocument(doc, user);
				commitTransaction();
			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.myapps.core.dynaform.document.ejb.DocumentProcess#doChangeAuditor(
	 * cn.myapps.core.dynaform.document.ejb.Document,
	 * cn.myapps.base.action.ParamsTable, cn.myapps.core.user.action.WebUser)
	 */
	public void doChangeAuditor(Document doc, ParamsTable params, WebUser user)
			throws Exception {
		try {
			beginTransaction();
			// 1.更改流程审批人、待办信息
			updateActorRTList(doc, params, user);

			// 2.创建历史记录
			FlowStateRT instance = doc.getState();
			RelationHISProcess process = StateMachine
					.getRelationHISProcess(instance.getApplicationid());
			// RelationHIS his =
			// process.doViewLastByDocIdAndFolowStateId(doc.getId(),
			// instance.getId());
			RelationHIS his = new RelationHIS();
			his.setId(Sequence.getSequence());
			his.setFlowStateId(instance.getId());
			his.setFlowid(instance.getFlowid());
			his.setFlowname(instance.getFlowVO().getSubject());
			his.setDocid(instance.getDocid());
			his.setStartnodeid(null);
			his.setStartnodename(doc.getStateLabel());
			his.setEndnodeid(null);
			his.setEndnodename(doc.getStateLabel());
			his.setIspassed(false);
			his.setActiontime(new Date());
			his.setAttitude(params.getParameterAsString("_attitude")); // 审批意见
			his.setAuditor(user.getId());
			ActorHIS actorHIS = null;
			if (user.getEmployer() != null) {
				actorHIS = new ActorHIS((new WebUser(user.getEmployer())));
				actorHIS.setAgentid(user.getId());
				actorHIS.setAgentname(user.getName());
			} else {
				actorHIS = new ActorHIS(user);
			}
			String attitude = params.getParameterAsString("_attitude");
			actorHIS.setProcesstime(new Date());
			actorHIS.setAttitude(attitude);
			actorHIS.setSignature(params.getParameterAsString("_signature"));
			his.getActorhiss().add(actorHIS);
			StringBuffer _attitude = new StringBuffer();
			_attitude.append(his.getAttitude());
			_attitude.append(",").append(attitude);
			his.setAttitude(_attitude.toString()); // 审批意见
			process.doCreate(his);

			// 3.更新文档
			doUpdate(doc, user, true, true);

			// 4.更新AUTH_XX 表
			createAuth(doc);

			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 新建权限记录到Auth_动态表
	 * 
	 * @param doc
	 * @throws Exception
	 */
	public void createAuth(Document doc) throws Exception {
		FormProcess formProcess = (FormProcess) ProcessFactory
				.createProcess(FormProcess.class);
		Form form = (Form) formProcess.doView(doc.getFormid());

		Collection<String> condition = new ArrayList<String>();
		if (doc.getState() != null) {
			doc.getState().setActors(null);
		}
		// 从流程中获取权限值(用户)
		/*
		 * Collection<ActorRT> actors = null; if (doc.getState() != null &&
		 * doc.getState().getActors() != null &&
		 * !doc.getState().getActors().isEmpty()) { actors =
		 * doc.getState().getActors(); } else if
		 * (!doc.get_tempSubFlowActors().isEmpty()) { actors =
		 * doc.get_tempSubFlowActors();// 从文档拿临时存储的子流程当前处理人 }
		 */

		// 从流程中获取权限值(用户)
		Collection<ActorRT> actors = new ArrayList<ActorRT>();
		Collection<FlowStateRT> states = doc.getFlowInstances();
		for (Iterator<FlowStateRT> iterator = states.iterator(); iterator
				.hasNext();) {
			FlowStateRT flowStateRT = iterator.next();
			actors.addAll(flowStateRT.getActors());
		}

		if (actors != null && !actors.isEmpty()) {
			for (Iterator<ActorRT> iterator = actors.iterator(); iterator
					.hasNext();) {
				ActorRT actorrt = (ActorRT) iterator.next();
				if (actorrt.isPending()) {
					condition.add(actorrt.getActorid());
				}
			}
		}

		// 从表单的权限字段中获取权限值(部门)
		FormHelper helper = new FormHelper();
		Map<?, ?> map = helper.getAllAuthorityFields(doc.getFormid());
		if (!map.isEmpty()) {
			for (Iterator<?> iterator = map.values().iterator(); iterator
					.hasNext();) {
				Object obj = (Object) iterator.next();
				if (obj != null && !obj.equals("")) {
					String valueStr = doc.getItemValueAsString(obj.toString());
					if (!StringUtil.isBlank(valueStr)) {
						String[] values = valueStr.split(";");
						for (int i = 0; i < values.length; i++) {
							condition.add(values[i]);
						}
					}
				}
			}
		}

		((DocumentDAO) getDAO()).createAuthDocWithCondition(form.getName(), doc
				.getId(), condition);
	}

	/**
	 * 根据父表单主键,返回Document集合
	 * 
	 * @param parentid
	 * @return 符合父表单主键的Document集合
	 * @throws Exception
	 */
	public Collection<Document> queryByParentID(String parentid)
			throws Exception {
		return ((DocumentDAO) getDAO()).queryByParentID(parentid);
	}

	public static DocumentProcess createMonitoProcess(String applicationid)
			throws CreateProcessException {
		DocumentProcess process = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class, applicationid);
		return process;
	}

	public DataPackage<Document> queryBySQL(String sql, ParamsTable params,
			String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryBySQL(sql, params, domainid);
	}

	// vinsun
	public DataPackage<Document> queryBySQL(String sql, String domainid)
			throws Exception {
		return ((DocumentDAO) getDAO()).queryBySQL(sql, domainid);
	}

	public Collection<Document> queryBySQL(String sql) throws Exception {
		return ((DocumentDAO) getDAO()).queryBySQL(sql, 1, Integer.MAX_VALUE,
				"");
	}

	public DataPackage<Document> queryBySQLPage(String sql, int page,
			int lines, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryBySQLPage(sql, page, lines,
				domainid);
	}

	public DataPackage<Document> queryBySQLPage(String sql, ParamsTable params,
			int page, int lines, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).queryBySQLPage(sql, params, page,
				lines, domainid);
	}

	public long countByDQL(String dql, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).countByDQL(dql, domainid);
	}

	public long countByProcedure(String sql, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).countByProcedure(sql, domainid);
	}

	public double sumBySQL(String sql, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).sumBySQL(sql, domainid);
	}

	public long countBySQL(String sql, String domainid) throws Exception {
		return ((DocumentDAO) getDAO()).countBySQL(sql, domainid);
	}

	/**
	 * 尚未开启流程
	 * 
	 * @param state
	 *            当前流程状态
	 * @return
	 */
	public boolean isNotStart(Document doc, ParamsTable params) {
		try {
			if (doc.getState() != null
					&& doc.getState().getState() != FlowState.START) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Collection<Document> queryModifiedDocuments(Document doc)
			throws Exception {
		Collection<Document> col = null;
		col = ((DocumentDAO) getDAO()).queryModifiedDocuments(doc);
		return col;
	}

	/**
	 * 创建新文档包含子文档
	 * 
	 * @param form
	 *            表单
	 * @param user
	 *            当前用户
	 * @param params
	 *            参数
	 * @param children
	 *            所包含的子文档
	 * @return 新文档
	 */
	public Document doNewWithChildren(Form form, WebUser user,
			ParamsTable params, Collection<Document> children) throws Exception {
		Document root = null;
		try {
			beginTransaction();
			root = form.createDocument(params, user);
			recursiveCreate(root, children);

			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}

		return root;
	}

	/**
	 * 递归创建
	 * 
	 * @param parent
	 *            父文档
	 * @param children
	 *            子文档
	 * @throws Exception
	 */
	protected void recursiveCreate(Document parent,
			Collection<Document> children) throws Exception {
		try {
			doCreate(parent);

			if (children != null && !children.isEmpty()) {
				for (Iterator<Document> iterator = children.iterator(); iterator
						.hasNext();) {
					Document child = (Document) iterator.next();
					Collection<Document> nestedChildren = child.getChilds();

					child.setId(Sequence.getSequence());
					child.setParent(parent);
					if (nestedChildren != null && !nestedChildren.isEmpty()) {
						recursiveCreate(child, nestedChildren);
					} else {
						doCreate(child);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void doRemoveWithChildren(ValueObject vo) throws Exception {
		Document root = null;
		try {
			// beginTransaction();
			root = (Document) vo;
			// recursiveRemove(root, children);
			doRemove(root.getId());
			// commitTransaction();
		} catch (Exception e) {
			// rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 根据表单名称删除
	 * 
	 * @throws Exception
	 */
	public void doRemoveByFormName(Form form) throws Exception {
		try {
			beginTransaction();
			((DocumentDAO) getDAO()).removeDocumentByForm(form);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 根据表单名称修改字段formname
	 * 
	 * @throws Exception
	 */
	public void doChangeFormName(Form form) throws Exception {
		try {
			beginTransaction();
			((DocumentDAO) getDAO()).changeDocumentFormName(form);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 清楚字段数据
	 * 
	 * @throws Exception
	 */
	public void doRemoveDocByFields(Form form, String[] fields)
			throws Exception {
		try {
			beginTransaction();
			((DocumentDAO) getDAO()).removeDocumentByField(form, fields);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 递归删除
	 * 
	 * @param parent
	 *            父文档
	 * @param children
	 *            子文档
	 * @throws Exception
	 */
	protected void recursiveRemove(Document parent,
			Collection<Document> children) throws Exception {
		try {

			if (children != null && !children.isEmpty()) {
				for (Iterator<Document> iterator = children.iterator(); iterator
						.hasNext();) {
					Document child = (Document) iterator.next();
					Collection<Document> nestedChildren = child.getChilds();

					if (nestedChildren != null && !nestedChildren.isEmpty()) {

						// 删除相关的Auth表中信息
						((DocumentDAO) getDAO()).removeAuthByDoc(child);

						recursiveRemove(child, nestedChildren);
						doRemove(child);
					} else {
						doRemove(child);
					}
				}
			}
			doRemove(parent);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public DataPackage<Document> queryByDQLDomainName(String dql,
			String domainName) throws Exception {
		DomainProcess process = (DomainProcess) ProcessFactory
				.createProcess(DomainProcess.class);
		DomainVO vo = process.getDomainByName(domainName);
		if (vo != null) {
			return ((DocumentDAO) getDAO()).queryByDQL(dql, vo.getId());
		} else {
			return null;
		}
	}

	/**
	 * 创建文档头. 此文档头用来保存不同Document的信息. 此方法实现数据库文档表的相应字列插入相应Document属性的值.
	 * 
	 * @param doc
	 *            Document对象
	 * @throws Exception
	 */
	public void createDocumentHead(Document doc) throws Exception {
		((DocumentDAO) getDAO()).createDocumentHead(doc);
	}

	public DataPackage<WorkVO> queryWorkBySQLPage(ParamsTable params, int page,
			int lines, WebUser user) throws Exception {
		return ((DocumentDAO) getDAO()).queryWorkBySQLPage(params, page, lines,
				user);
	}

	public DataPackage<WorkVO> queryWorks(ParamsTable params, WebUser user)
			throws Exception {
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer
				.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
				.parseInt(_pagelines) : 10;
		return ((DocumentDAO) getDAO()).queryWorkBySQLPage(params, page, lines,
				user);
	}

	public DataPackage<Document> queryByProcedure(String procedure,
			ParamsTable params, int page, int lines, String domainid)
			throws Exception {
		return ((DocumentDAO) getDAO()).queryByProcedure(procedure, params,
				page, lines, domainid);
	}

	@SuppressWarnings("unchecked")
	public DataPackage<Document> queryBySQLWithCache(String sql, String domainid)
			throws Exception {
		DataPackage<Document> result = null;
		try {
			// signal.sessionSignal++;

			String cacheName = "cn.myapps.core.dynaform.document.ejb.DocumentProcessBean.queryBySQLWithCache(java.lang.String,java.lang.String)";

			ICacheProvider provider = MyCacheManager.getProviderInstance();
			if (provider != null) {
				if (provider.clearByCacheName(cacheName)) {
					JavaScriptFactory.clear();
					log.info("##CLEAN-CACHE-->>" + cacheName);
				}

				IMyCache cache = provider.getCache(cacheName);
				if (cache != null) {

					Class<?>[] parameterTypes = new Class<?>[2];
					parameterTypes[0] = String.class;
					parameterTypes[1] = String.class;

					Method method = DocumentProcessBean.class.getMethod(
							"queryBySQLWithCache", parameterTypes);

					Object[] methodParameters = new Object[2];
					methodParameters[0] = sql;
					methodParameters[1] = "";
					CacheKey cacheKey = new CacheKey(this, method,
							methodParameters);

					IMyElement cachedElement = (IMyElement) cache.get(cacheKey);

					if (cachedElement != null
							&& cachedElement.getValue() != null) {
						log.debug("@@CACHED-METHOD-->>" + cacheKey);
						result = (DataPackage<Document>) cachedElement
								.getValue();
					} else {

						result = ((DocumentDAO) getDAO()).queryBySQL(sql,
								domainid);
						cache.put(cacheKey, result);

						return result;
					}
				}
			}

			return result;

		} catch (Exception t) {
			throw t;
		} finally {
			// signal.sessionSignal--;
			// if (signal.sessionSignal <= 0) {
			// PersistenceUtils.closeSession();
			// }
		}
	}

	public synchronized String compareAndUpdateItemWord(String filename,
			String fieldname, String formname, String docid) throws Exception {
		String rtn = "";
		try {
			this.beginTransaction();
			rtn = ((DocumentDAO) getDAO()).compareAndUpdateItemWord(filename,
					fieldname, formname, docid);
			this.commitTransaction();
		} catch (Exception e) {
			this.rollbackTransaction();
		}
		return rtn;
	}

	public void doClearRedundancyData() throws Exception {
		((DocumentDAO) getDAO()).doClearRedundancyData();
	}

	public Document doEdit(ParamsTable params, WebUser user,
			String applicationId) throws Exception {
		Document doc = null;
		try {
			this.beginTransaction();
			String _docid = params.getParameterAsString("_docid");
			doc = (Document) this.doView(_docid);
			if (doc == null) {
				doc = new Document();
			}
			FormProcess formPross = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			String formid = params.getParameterAsString("_formid");
			if (StringUtil.isBlank(formid)) {
				formid = doc.getFormid();
			}
			
			
			
			NodeRT nodert =null ;
			//选择可执行的流程实例
			if(!StringUtil.isBlank(doc.getId()) && !StringUtil.isBlank(doc.getStateid())){
				FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, applicationId);
				if(stateProcess.isMultiFlowState(doc)){//有多个流程实例
					FlowStateRT currInstance = stateProcess.getCurrFlowStateRT(doc, user, params.getParameterAsString("_targetInstance"));//绑定一个用户可审批的文档实例
					if(currInstance == null){
						currInstance = doc.getState();//当前流程实例
						if(currInstance==null){
							currInstance = stateProcess.getParentFlowStateRT(doc);//将文档加载到主流程实例
						}
					}
					if(currInstance !=null){
						doc.setState(currInstance);
						doc.setMulitFlowState(stateProcess.isMultiFlowState(doc, user));//是否存在多个可执行实例
					}
				}
				
				//设置审批节点
				String _targetNode = params.getParameterAsString("_targetNode");
				if(!StringUtil.isBlank(doc.getStateid())){
					String defaultNodeId = null;
					if(!StringUtil.isBlank(_targetNode)){
						defaultNodeId = _targetNode;
					}
					nodert = StateMachine.getCurrUserNodeRT(doc, user,defaultNodeId);
					if(nodert !=null) {
						((HttpServletRequest)params.getRequest()).setAttribute("_targetNodeRT", nodert);
					}
				}
			}
			if(nodert !=null) {
				//更新文档阅读状态
				readDocument(doc, user,nodert,applicationId);
			}
			
			Form form = (Form) formPross.doView(formid);
			if (form != null) {
				form.recalculateDocument(doc, params, user);
			}
			
			//设置编辑状态
			boolean isedit =  !StringUtil.isBlank(params.getParameterAsString("isedit")) ? Boolean
					.parseBoolean(params.getParameterAsString("isedit"))
					: true; 
			if(!isedit){
				doc.setEditAbleLoaded(true);
				doc.setEditAble(false);
			}
			
			//标记模板表单编辑模式
			String _templateFormId = params.getParameterAsString("_templateForm");
			if (!StringUtil.isBlank(_templateFormId)) {// 模板表单
				Form _templateForm = (Form) formPross.doView(_templateFormId);
				if (_templateForm != null) {
					doc.setTemplateForm(_templateForm);
				}
			}
			this.commitTransaction();
		} catch (Exception e) {
			this.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return doc;
	}

	/**
	 * 更新文档阅读状态
	 * @param doc
	 * 		文档
	 * @param user
	 * 		用户
	 * @param applicationId
	 * 		软件Id
	 * @throws Exception
	 */
	private void readDocument(Document doc, WebUser user,NodeRT nodert, String applicationId)
			throws Exception {
		try {
			if (doc.getState() != null) {
				Collection<ActorRT> actors = doc.getState().getActors();
				for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
					ActorRT actor = iter.next();
					if (user.getId().equals(actor.getActorid()) && actor.getNodertid().equals(nodert.getId())) {
						if (!actor.getIsread()) {
							actor.setIsread(true);
							ActorRTProcess process = new ActorRTProcessBean(
									applicationId);
							process.doUpdate(actor);
						}
						break;
					}
				}
				// 查找抄送人为当前用户
				CirculatorProcess cProcess = (CirculatorProcess) ProcessFactory
						.createRuntimeProcess(CirculatorProcess.class,
								applicationId);
				Circulator circulator = cProcess.findByCurrDoc(doc.getId(), doc
						.getState().getId(), false, user);
				if (circulator != null) {
					circulator.setRead(true);
					circulator.setReadTime(new Date());
					cProcess.doUpdate(circulator);// 更新为已阅
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public void doArchive(Document doc, WebUser user, ParamsTable params)
			throws Exception {
		try {
			//判断KM功能是否开启,或者KM配置的连接是否正确
			PropertyUtil.reload("km");
			if(PropertyUtil.get(KmConfig.ENABLE).equals("false")){
				throw new WorkflowException("网盘功能未开启,请联系管理员开启网盘后再尝试");
			}else {
				String username = PropertyUtil.get(KmConfig.USER);
				String password = PropertyUtil.get(KmConfig.PASSWORD);
				String driver = PropertyUtil.get(KmConfig.DERVER_CLASS);
				String url = PropertyUtil.get(KmConfig.DERVER_URL);
				
				try {
					Class.forName(driver).newInstance();
					Connection conn = DriverManager.getConnection(url, username,password);
					conn.close();
				} catch (Exception e) {
					throw new WorkflowException("网盘连接配置不正确,请联系管理员检查后再尝试");
				}
			}
			
			
			NDiskProcess ndiskProcess = new NDiskProcessBean();
			NDisk disk = ndiskProcess.getArchiveDisk(user.getDomainid());
			String realPath = params.getHttpRequest().getSession().getServletContext().getRealPath("");
			String newPath = params.getHttpRequest().getSession().getServletContext().getRealPath("ndisk") + File.separator + disk.getId();
			
			NDirProcess ndirProcess = new NDirProcessBean();
			NDir docdir = (NDir) ndirProcess.doView(doc.getId());
			PersistenceUtils.beginTransaction();
			if(docdir.getId() == null){
				//软件文件夹,不存在则创建
				NDir applicationNDir = (NDir) ndirProcess.doView(doc.getApplicationid());
				if(applicationNDir.getId()== null){
					applicationNDir = new NDir();
					applicationNDir.setId(doc.getApplicationid());
					applicationNDir.setName(doc.getState().getFlowVO().getModule().getApplication().getName());
					applicationNDir.setType(NDir.TYPE_ARCHIVE);
					applicationNDir.setParentId(disk.getnDirId());
					applicationNDir.setnDiskId(disk.getId());
					applicationNDir.setPath("\\" + disk.getId());
					applicationNDir.setCreateDate(new Date());
					applicationNDir.setDomainId(user.getDomainid());
					
					ndirProcess.doCreate(applicationNDir);
				}
				
				//模块文件夹,不存在则创建
				NDir moduleNDir = (NDir) ndirProcess.doView(doc.getForm().getModule().getId());
				if(moduleNDir.getId() == null){
					moduleNDir = new NDir();
					moduleNDir.setId(doc.getForm().getModule().getId());
					moduleNDir.setName(doc.getState().getFlowVO().getModule().getName());
					moduleNDir.setType(NDir.TYPE_ARCHIVE);
					moduleNDir.setParentId(applicationNDir.getId());
					moduleNDir.setnDiskId(disk.getId());
					moduleNDir.setPath(applicationNDir.getPath() + "\\" + moduleNDir.getId());
					moduleNDir.setCreateDate(new Date());
					moduleNDir.setDomainId(user.getDomainid());
					
					ndirProcess.doCreate(moduleNDir);
				}
				
				//流程文件夹,不存在则创建
				NDir flowNDir = (NDir) ndirProcess.doView(doc.getState().getFlowid());
				if(flowNDir.getId() == null){
					flowNDir = new NDir();
					flowNDir.setId(doc.getState().getFlowid());
					flowNDir.setName(doc.getState().getFlowVO().getSubject());
					flowNDir.setType(NDir.TYPE_ARCHIVE);
					flowNDir.setParentId(moduleNDir.getId());
					flowNDir.setnDiskId(disk.getId());
					flowNDir.setPath(moduleNDir.getPath() + "\\" + flowNDir.getId());
					flowNDir.setCreateDate(new Date());
					flowNDir.setDomainId(user.getDomainid());
					
					ndirProcess.doCreate(flowNDir);
				}
				
				SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
				SummaryCfgVO summary = summaryCfgProcess.doViewByFormIdAndScope(doc.getFormid(), SummaryCfgVO.SCOPE_PENDING);
				docdir = new NDir();
				docdir.setId(doc.getId());
				if(summary != null){
					docdir.setName(summary.toSummay(doc, user));
				}else {
					docdir.setName(doc.getFormname());
				}
				docdir.setType(NDir.TYPE_ARCHIVE);
				docdir.setParentId(flowNDir.getId());
				docdir.setnDiskId(disk.getId());
				docdir.setPath(flowNDir.getPath() + "\\" + docdir.getId());
				docdir.setCreateDate(new Date());
				docdir.setDomainId(user.getDomainid());
				
				ndirProcess.doCreate(docdir);
			}
			//生成表单pdf文件
			archiveFile(params, user, doc, newPath, realPath, disk, docdir);
			//copy表单附件
			copyAttachment(params, user, doc, newPath, realPath, disk, docdir);
			
			FlowStateRTProcess flowStateRTProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, doc.getApplicationid());
			FlowStateRT flowstatert = doc.getState();
			flowstatert.setArchived(true);
			flowStateRTProcess.doUpdate(flowstatert);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	private void copyAttachment(ParamsTable params, WebUser user, Document doc, String newPath, String realPath, NDisk disk, NDir docdir) throws Exception{
		try {
			Form form = doc.getForm();
			for(Iterator<FormField> it = form.getAllFields().iterator(); it.hasNext();){
				FormField field = it.next();
				if(field instanceof AttachmentUploadField){
					String src = doc.getItemValueAsString(field.getName());
					if(src.startsWith("[")){
						Collection<Object> files = JsonUtil.toCollection(src);
						for(Iterator<Object> iter = files.iterator(); iter.hasNext();){
							Map<String, String> srcs = (Map<String, String>)iter.next();
							String fileName = srcs.get("name");
							String filePath = srcs.get("path");
							if(filePath.length() >0){
								int urlIndex = filePath.lastIndexOf("_/uploads");
	
								if(urlIndex>=0){
									filePath = filePath.substring(urlIndex+1);
								}
								String id = Sequence.getSequence();
								String fileType = fileName.substring(fileName.indexOf(".") + 1);
								cn.myapps.util.file.FileUtil.copyFile(newPath + File.separator + id + "." + fileType, realPath + filePath);
								File file = new File(realPath + filePath);
								this.createFile(id, disk, user, params, fileName, file.length(), docdir);
							}
//							value = value.substring(webIndex + 1,value.length());
						}
					}else {
						String[] attachmentNames = src.split(";");
						String fileName = "";
						for(int i=0; i<attachmentNames.length; i++){
							fileName = attachmentNames[i].substring(attachmentNames[i].lastIndexOf("/") + 1);
							if(!StringUtil.isBlank(fileName)){
								String fileType = fileName.substring(fileName.indexOf(".") + 1);
								String id = Sequence.getSequence();
								cn.myapps.util.file.FileUtil.copyFile(newPath + File.separator + id + "." + fileType, realPath + attachmentNames[i]);
								
								File file = new File(realPath + attachmentNames[i]);
								this.createFile(id, disk, user, params, fileName, file.length(), docdir);
							}
						}
					}
				}else if(field instanceof ImageUploadField){
					String src = doc.getItemValueAsString(field.getName());
					if(src.startsWith("[")){
						Collection<Object> files = JsonUtil.toCollection(src);
						for(Iterator<Object> iter = files.iterator(); iter.hasNext();){
							Map<String, String> srcs = (Map<String, String>)iter.next();
							String fileName = srcs.get("name");
							String filePath = srcs.get("path");
							if(filePath.length() >0){
								int urlIndex = filePath.lastIndexOf("_/uploads");
	
								if(urlIndex>=0){
									filePath = filePath.substring(urlIndex+1);
								}
								String id = Sequence.getSequence();
								String fileType = fileName.substring(fileName.indexOf(".") + 1);
								cn.myapps.util.file.FileUtil.copyFile(newPath + File.separator + id + "." + fileType, realPath + filePath);
								File file = new File(realPath + filePath);
								this.createFile(id, disk, user, params, fileName, file.length(), docdir);
							}
//							value = value.substring(webIndex + 1,value.length());
						}
					}else {
						if(!StringUtil.isBlank(src)){
							String fileName = src.substring(src.lastIndexOf("/") + 1);
							String fileType = fileName.substring(fileName.indexOf(".") + 1);
							String id = Sequence.getSequence();
							cn.myapps.util.file.FileUtil.copyFile(newPath + File.separator + id + "." + fileType, realPath + src);
							
							File file = new File(realPath + src);
							this.createFile(id, disk, user, params, fileName, file.length(), docdir);
						}
					}
				}else if(field instanceof WordField){
					String wordName = doc.getItemValueAsString(field.getName());
					String _realPath = realPath + File.separator + "uploads" + File.separator + "doc" + File.separator;
					String _newPath = newPath + File.separator;
					String id = Sequence.getSequence();
					cn.myapps.util.file.FileUtil.copyFile(_newPath + id + ".doc", _realPath + wordName);
					
					File file = new File(_realPath + wordName);
					this.createFile(id, disk, user, params, field.getName() + ".doc", file.length(), docdir);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	private void archiveFile(ParamsTable params, WebUser user, Document doc, String newPath, String realPath, NDisk disk, NDir docdir) throws Exception{
		StringBuffer html = new StringBuffer();
		FileOutputStream os = null;
		try {
			html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"> \n");
			html.append("<html>\n<head>\n<title></title>\n");
			html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
			
			html.append(getCssContent("/portal/share/css/print.css", params.getHttpRequest(), null));
			
			html.append("</head>\n");
			html.append("<body style=\"padding: 0;margin: 0;\">\n");
			html.append("<div id=\"doc_divid\" class=\"front-align-top\">\n");
			html.append("<div id=\"container\" style=\"width: 100%\">\n");
			html.append("<div id=\"contentTable\" style=\"border-right: 0px;border-left: 0px;\">\n");
			html.append("<table width=\"100%\" border=\"0\" id=\"toAll\" style=\"z-index:1;\"><tr><td width=\"100%\" valign=\"top\" colspan=\"2\">\n");
			html.append(doc.getForm().toPdfHtml(doc, params, user, new ArrayList<ValidateMessage>())+"\n");
			html.append("</td></tr></table>");
			html.append("</div>\n");
			html.append("</div>\n");
			html.append("</div>\n");
			html.append("</body>\n");
			html.append("</html>\n");

			File newPathFile = new File(newPath);
			if (!newPathFile.exists()) {
				newPathFile.mkdirs();
			}
		
			File file = new File(newPath + File.separator + doc.getId() + ".pdf");
			os = new FileOutputStream(file);
			new ConvertHTML2Pdf().generatePDF(html.toString(), os);
			
			//创建文件
			NFileProcess nfileProcess = new NFileProcessBean();
			NFile nfile = (NFile) nfileProcess.doView(doc.getId());
			if(nfile == null){
				this.createFile(doc.getId(), disk, user, params, "正文.pdf", file.length(), docdir);
			}
			
			//复制流程图表并创建文件
			String _realPath = realPath + File.separator + "uploads" + File.separator + "billflow" + File.separator;
			cn.myapps.util.file.FileUtil.copyFile(newPath + File.separator + doc.getState().getId() + ".jpg", _realPath + File.separator + doc.getState().getId() + ".jpg");
			
			File flowImageFile = new File(_realPath + File.separator + doc.getState().getId() + ".jpg");
			NFile _flowImageFile = (NFile) nfileProcess.doView(doc.getState().getId());
			if(_flowImageFile == null){
				this.createFile(doc.getState().getId(), disk, user, params, "流程图表.jpg", flowImageFile.length(), docdir);
			}
			
			//生成流程历史pdf并创建文件
			html.setLength(0);
			html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"> \n");
			html.append("<html>\n<head>\n<title></title>\n");
			html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
			
			html.append("</head>\n");
			html.append("<body style=\"padding: 0;margin: 0;\">\n");
			html.append("<div id=\"doc_divid\" class=\"front-align-top\">\n");
			html.append("<div id=\"container\" style=\"width: 100%\">\n");
			html.append("<div id=\"contentTable\" style=\"border-right: 0px;border-left: 0px;\">\n");
			html.append("<table width=\"100%\" border=\"0\" id=\"toAll\" style=\"z-index:1;\"><tr><td width=\"100%\" valign=\"top\" colspan=\"2\">\n");
			html.append(toHistoryHtml(doc.getId(), doc.getStateid(), doc.getApplicationid(), user, params));
			html.append("</td></tr></table>");
			html.append("</div>\n");
			html.append("</div>\n");
			html.append("</div>\n");
			html.append("</body>\n");
			html.append("</html>\n");
			
			String id = Sequence.getSequence();
			
			File hisfile = new File(newPath + File.separator + id + ".pdf");
			os = new FileOutputStream(hisfile);
			new ConvertHTML2Pdf().generatePDF(html.toString(), os);
			
			this.createFile(id, disk, user, params, "流程历史.pdf", hisfile.length(), docdir);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	private String toHistoryHtml(String docid, String flowstateid, String applicationid, WebUser user, ParamsTable params) throws Exception{
		try {
			if (!StringUtil.isBlank(flowstateid) && !StringUtil.isBlank(applicationid) && !StringUtil.isBlank(docid)) {
				FlowStateRTProcess process = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, applicationid);
				FlowStateRT instance = (FlowStateRT) process.doView(flowstateid);
				if(instance ==null) return null;
				RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class, applicationid);
				Collection<RelationHIS> colls = hisProcess.doAllQueryByDocIdAndFlowStateId(docid, flowstateid);
				FlowHistory his = new FlowHistory();
				his.addAllHis(colls);

				return his.toTextHtml4Archive(user,params);
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "";
	}

	private void createFile(String id, NDisk disk, WebUser user, ParamsTable params, String fileName, long fileSize, NDir docdir) throws Exception{
		try {
			NFileProcess nfileProcess = new NFileProcessBean();
			NFile nfile = new NFile();
			nfile.setId(id);
			nfile.setName(fileName);
			nfile.setTitle(fileName.substring(0, fileName.indexOf(".")));
			nfile.setSize(fileSize);
			nfile.setUrl("\\" + disk.getId() + "\\" + id + fileName.substring(fileName.indexOf(".")));
			nfile.setNDirId(docdir.getId());
			nfile.setMemo("");
			nfile.setCreateDate(new Date());
			nfile.setLastmodify(new Date());
			nfile.setOrigin(NFile.ORIGN_ARCHIVE);
			nfile.setOwnerId(user.getId());
			nfile.setCreatorId(user.getId());
			nfile.setCreator(user.getName());
			nfile.setDomainId(user.getDomainid());
			nfile.setDepartment(user.getDepartmentById(user.getDefaultDepartment()).getName());
			nfile.setDepartmentId(user.getDefaultDepartment());
			nfile.setState(IFile.STATE_ARCHIVE);
			
			cn.myapps.km.base.action.ParamsTable _params = cn.myapps.km.base.action.ParamsTable.convertHTTP(params.getHttpRequest());
			_params.setParameter("_ndiskid", disk.getId());
			NUser nUser = new NUser(user);
			nfileProcess.createFile(nfile, _params, nUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据传入的css路径地址获取css文件内容
	 * @param url
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String getCssContent(String url,HttpServletRequest request,HttpServletResponse response) throws Exception {
		StringBuffer content = new StringBuffer();
		content.append("<style>");
		String filePath = request.getRealPath(new OBPMDispatcher().getDispatchURL(url,request,response));
		content.append(cn.myapps.util.file.FileOperate.getFileContentAsString(filePath));
		content.append("</style>");
		return content.toString();
	}
	
	public String doSynchronouslyMappingFormData(ParamsTable params,
			WebUser user, Form form) throws Exception {
		
		String applicationId = form.getApplicationid();
		UploadProcess uploadProcess = (UploadProcess) ProcessFactory.createRuntimeProcess(UploadProcess.class,params
				.getParameterAsString("application"));

		String domainid = params.getParameterAsString("domainName");
		Collection<FormField> allFormField = form.getAllFields();
		boolean hasAttachment = false;//是否有二进制的字段
		
		for (Iterator<FormField> iterFormField = allFormField.iterator(); iterFormField.hasNext();) {
			FormField formField = (FormField) iterFormField.next();
			if (formField.getType() != null) {
				if (formField.getType().equals("attachmentuploadtodatabase")
						|| formField.getType().equals("imageuploadtodatabase")) {
					hasAttachment = true;
					break;
				}
			}
		}
		
		
		try {
			Collection<String> mappingIds = ((DocumentDAO)getDAO()).queryMappingTablePrimaryKeys(domainid, form, 1, 10000);
			while(!mappingIds.isEmpty()) {
				for (Iterator<String> iterator1 = mappingIds.iterator(); iterator1.hasNext();) {
					String mappingId = iterator1.next();
					Document document = form.createDocument(new ParamsTable(), user, false);
					document.setMappingId(mappingId);
					document.setDomainid(domainid);
					document.setIstmp(false);
					document.setApplicationid(applicationId);
					createDocumentHead(document);
					
					if(hasAttachment){
						synchronouslyMappingFormAttachmentData(form, allFormField, document, mappingId, uploadProcess);
					}
					
				}
				mappingIds = ((DocumentDAO)getDAO()).queryMappingTablePrimaryKeys(domainid, form, 1, 10000);
			}
		} catch (Exception e) {
			throw e;
		}
		
		
		return "success";
	}
	
	/**
	 * 同步映射表单的二进制数据列
	 * @param form
	 * @param allFormField
	 * @param document
	 * @param mappingId
	 * @param uploadProcess
	 * @throws Exception
	 */
	private void synchronouslyMappingFormAttachmentData(Form form,Collection<FormField> allFormField,Document document,String mappingId,UploadProcess uploadProcess) throws Exception {
		for (Iterator<FormField> iterFormField = allFormField.iterator(); iterFormField.hasNext();) {
			FormField formField = (FormField) iterFormField.next();
			if (formField.getType() != null) {
				if (formField.getType().equals("attachmentuploadtodatabase")
						|| formField.getType().equals("imageuploadtodatabase")) {
					System.out.println(form.getTableMapping().getColumnName(formField.getName()));
					if (form.getTableMapping().getColumnName(formField.getName()) != null
							&& !form.getTableMapping().getColumnName(formField.getName())
									.equals("")) {
						UploadVO uploadVO = (UploadVO) uploadProcess.doFindByMappingToUploadVO(form
								.getTableMapping().getColumnName(formField.getName()), document
								.getId()
								+ "_" + formField.getName(), form.getTableMapping().getTableName(),
								form.getTableMapping().getPrimaryKeyName(), mappingId);
						uploadProcess.doCreate(uploadVO);
					}
				}
			}

		}
	}
	
	/**
	 * 根据参数传入的条件查询待办任务
	 * @param user
	 * 		用户
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> doQueryProcessingWorks(WebUser user,String flowId,String subject,int currpage,int pagelines) throws Exception{
		return ((DocumentDAO)getDAO()).queryProcessingWorks(getApplicationId(), user, flowId, subject, currpage, pagelines,false);
	}
	
	/**
	 * 根据参数传入的条件查询待办任务
	 * @param user
	 * 		用户
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	* @param isFlowAgent
	 * 		是否显示代理的代办
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> doQueryProcessingWorks(WebUser user,String flowId,String subject,int currpage,int pagelines,boolean isFlowAgent) throws Exception{
		return ((DocumentDAO)getDAO()).queryProcessingWorks(getApplicationId(), user, flowId, subject, currpage, pagelines,isFlowAgent);
	}
	
	/**
	 * 根据参数传入的条件查询经办任务
	 * @param user
	 * 		用户
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> doQueryProcessedRunningWorks(WebUser user,String flowId,String subject,int currpage,int pagelines) throws Exception{
		return ((DocumentDAO)getDAO()).queryProcessedRunningWorks(getApplicationId(), user, flowId, subject, currpage, pagelines);
	}
	
	/**
	 * 根据参数传入的条件查询历史任务
	 * @param user
	 * 		用户
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param myInitiatedList
	 *      是否为我创办的流程
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> doQueryProcessedCompletedWorks(WebUser user,String flowId,String subject,Boolean myInitiatedList,int currpage,int pagelines) throws Exception{
		return ((DocumentDAO)getDAO()).queryProcessedCompletedWorks(getApplicationId(), user, flowId, subject,myInitiatedList,currpage, pagelines);
	}
	
	/**
	 * 查询所有历史任务
	 * @param user
	 * 		用户
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> doQueryAllProcessedCompletedWorks(WebUser user,String flowId,String subject,int currpage,int pagelines) throws Exception{
		
		ApplicationHelper applicationHelper=new ApplicationHelper();
		Collection<ApplicationVO> list=applicationHelper.getListByWebUser(user);
		
		
		DataPackage<WorkVO> dataPackages =new DataPackage<WorkVO>();
		for (Iterator<ApplicationVO> iterator = list.iterator(); iterator.hasNext();) {
			ApplicationVO applicationVO = (ApplicationVO) iterator.next();
			String appId=applicationVO.getApplicationid();
			setApplicationId(appId);
			DataPackage<WorkVO> datas=((DocumentDAO)getDAO()).queryProcessedCompletedWorks(getApplicationId(), user, flowId, subject,false,currpage, 10);
			dataPackages.getDatas().addAll(datas.getDatas());
			dataPackages.rowCount += datas.rowCount;
		}
		return dataPackages;
	}

	public long countBySQL(String sql, String domainid, boolean warpSql)
			throws Exception {
		return ((DocumentDAO)getDAO()).countBySQL(sql, domainid, warpSql);
	}


}
