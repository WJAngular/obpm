package cn.myapps.core.workflow.storage.runtime.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.WorkflowException;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.engine.FlowTicketValidator;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.engine.ValidationException;
import cn.myapps.core.workflow.engine.instruction.InstructionException;
import cn.myapps.core.workflow.engine.instruction.InstructionExecutor;
import cn.myapps.core.workflow.notification.ejb.NotificationProcess;
import cn.myapps.core.workflow.notification.ejb.NotificationProcessBean;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.dao.FlowStateRTDAO;
import cn.myapps.core.xmpp.XMPPNotification;
import cn.myapps.core.xmpp.notification.PendingNotification;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.sequence.Sequence;

public class FlowStateRTProcessBean extends AbstractRunTimeProcessBean<FlowStateRT> implements FlowStateRTProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1242577083144357588L;

	public FlowStateRTProcessBean(String applicationId) {
		super(applicationId);
	}

	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getFlowStateRTDAO(getConnection(), getApplicationId());
	}

	/**
	 * 根据文档Id获取相关的流程实例集合
	 * 
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowStateRT> getFlowStateRTsByDocId(String docId) throws Exception {

		return ((FlowStateRTDAO) getDAO()).getFlowStateRTsByDocId(docId);
	}

	public void doCreate(ValueObject vo) throws Exception {
		FlowStateRT state = (FlowStateRT) vo;
		state.setTemp(false);
		super.doCreate(state);
	}

	/**
	 * 根据文档ID删除关联的流程实例
	 * 
	 * @param docId
	 * @throws Exception
	 */
	public void doRemoveByDocId(String docId) throws Exception {
		((FlowStateRTDAO) getDAO()).doRemoveByDocId(docId);
	}

	/**
	 * 获取当前文档流程状态
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            文档流程 id
	 * @return 当前文档流程状态
	 * @throws Exception
	 */
	public FlowStateRT findFlowStateRTByDocidAndFlowid(String docid, String flowid) throws Exception {
		return ((FlowStateRTDAO) getDAO()).findFlowStateRTByDocidAndFlowid(docid, flowid);
	}

	public void doUpdate(ValueObject vo) throws Exception {
		super.doUpdate(vo);
	}

	public Collection<FlowStateRT> getSubStates(String parent) throws Exception {
		return ((FlowStateRTDAO) getDAO()).queryByParent(parent);
	}

	public FlowStateRT createTransientFlowStateRT(Document doc, String flowId, WebUser user) throws Exception {
		BillDefiProcess process = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) process.doView(flowId);
		if (flowVO != null) {
			return createTransientFlowStateRT(doc, flowVO, user);
		}
		return null;
	}

	public FlowStateRT createTransientFlowStateRT(Document doc, BillDefiVO flowVO, WebUser user) throws Exception {
		FlowStateRT state = new FlowStateRT();
		if(StringUtil.isBlank(doc.getStateid())){
			state.setId(Sequence.getSequence());
		}else{
			state.setId(doc.getStateid());
		}
		state.setApplicationid(doc.getApplicationid());
		state.setFlowName(flowVO.getSubject());
		state.setLastModified(new Date());
		state.setLastModifierId(user.getId());
		state.setInitiator(user.getName());
		state.setAudituser(user.getId());
		state.setDocument(doc);
		state.setFlowVO(flowVO);
		state.setTemp(true);
		state.setPosition(0);
		
		return state;
	}

	public FlowStateRT createTransientSubFlowStateRT(Document subFlowDoc, BillDefiVO subFlowVO,
			FlowStateRT parentInstance, String token, SubFlow node, WebUser user) throws Exception {
		FlowStateRT state = new FlowStateRT();
		state.setId(Sequence.getSequence());
		state.setApplicationid(subFlowDoc.getApplicationid());
		state.setFlowName(subFlowVO.getSubject());
		state.setLastModified(new Date());
		state.setLastModifierId(user.getId());
		state.setInitiator(user.getName());
		state.setAudituser(user.getId());
		state.setDocument(subFlowDoc);
		state.setFlowVO(subFlowVO);
		state.setSubFlowNodeId(node.id);
		state.setParent(parentInstance);
		state.setToken(token);
		state.setCallback(node.callback);
		state.setTemp(true);
		return state;
	}

	public void startFlow(FlowStateRT instance, Document doc, ParamsTable params) throws Exception {

	}

	public boolean isAllSubFlowStateRTComplete(FlowStateRT subFlowInstance) throws Exception {
		return ((FlowStateRTDAO) getDAO()).isAllSubFlowStateRTComplete(subFlowInstance);
	}

	public void callBack(FlowStateRT subFlowInstance, IRunner runner, String script, ParamsTable params, WebUser user)
			throws Exception {

	}

	public FlowStateRT getCurrFlowStateRT(Document doc, WebUser user, String currFlowStateId) throws Exception {

		return ((FlowStateRTDAO) getDAO()).getCurrFlowStateRT(doc, user, currFlowStateId);
	}

	public boolean isMultiFlowState(Document doc, WebUser user) throws Exception {
		return ((FlowStateRTDAO) getDAO()).isMultiFlowState(doc, user);
	}

	public boolean isMultiFlowState(Document doc) throws Exception {
		return ((FlowStateRTDAO) getDAO()).isMultiFlowState(doc);
	}

	public void asynchronous2Next(String instanceId, String subFlowNodeId, ParamsTable params, WebUser user)
			throws Exception {
		FlowStateRT instance = (FlowStateRT) doView(instanceId);
		doApprove(instance, subFlowNodeId, params, user);
	}

	public void asynchronous2Next(FlowStateRT instance, String subFlowNodeid, ParamsTable params, WebUser user)
			throws Exception {

		doApprove(instance, subFlowNodeid, params, user);
	}

	public void doParentFlow2Next(FlowStateRT subFlowInstance, ParamsTable params, WebUser user) throws Exception {
		FlowStateRT instance = subFlowInstance.getParent();
		boolean isUpdate = false;
		if(instance.getDocid().equals(subFlowInstance.getDocid())){
			instance.setDocument(subFlowInstance.getDocument());
		}
		if(!instance.getDocid().equals(subFlowInstance.getDocid())){
			isUpdate = true;
		}
		doApprove(instance, subFlowInstance.getSubFlowNodeId(), params, user, isUpdate);
	}

	private void doApprove(FlowStateRT instance, String currNodeId, ParamsTable params, WebUser user) throws Exception {
		doApprove(instance, currNodeId, params, user, true);
	}

	public void doApprove(ParamsTable params, FlowStateRT instance, String currNodeId, String[] nextNodeIds,
			String flowOption, String comment, WebUser user) throws Exception {
		doApprove(instance, currNodeId, nextNodeIds, flowOption, comment,params,user, true,true);
	}

	/**
	 * 只提交流程，不更新文档状态
	 * 
	 * @param instance
	 * @param currNodeId
	 * @param params
	 * @param user
	 * @param updateDocument
	 * @throws Exception
	 */
	private void doApprove(FlowStateRT instance, String currNodeId, ParamsTable params, WebUser user,
			boolean updateDocument) throws Exception {
		FlowDiagram fd = instance.getFlowVO().toFlowDiagram();
		Node currNode = fd.getNodeByID(currNodeId);
		Collection<Node> nextNodeList = fd.getNextNodeList(currNodeId,instance.getDocument(),params,user);
		

		if (nextNodeList == null || nextNodeList.isEmpty()) {
			throw new OBPMValidateException("没有找到流程的下一步骤");
		}
		if(currNode instanceof SubFlow && ((SubFlow)currNode).issplit){
			params.setParameter("splitToken", currNodeId);
		}

		StringBuffer nextNodeIds = new StringBuffer();
		for (Iterator<Node> it = nextNodeList.iterator(); it.hasNext();) {
			Node n = it.next();
			nextNodeIds.append(n.id).append(",");
			if(currNode instanceof SubFlow && !((SubFlow)currNode).issplit){
				break;
			}
		}
		nextNodeIds.setLength(nextNodeIds.length() - 1);

		doApprove(instance, currNodeId, nextNodeIds.toString().split(","), FlowType.RUNNING2RUNNING_NEXT, "",
				params, user, updateDocument,true);
	}
	

	public void doApprove(FlowStateRT instance, String currNodeId, String[] nextNodeIds,
			String flowOption, String comment, ParamsTable params, WebUser user, boolean updateDocument,boolean updateinstance) throws Exception {
		
		FlowTicketValidator.valid(instance.getDocument());
		
		NotificationProcess notificationProcess = new NotificationProcessBean(getApplicationId());
		DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				getApplicationId());
		try {
//			beginTransaction();
			Document origDoc = (Document) instance.getDocument().clone();

			instance.getDocument().setAuditdate(new Date());
			instance.setAuditdate(new Date());
			instance.getDocument().setAudituser(user.getId());
			instance.setAudituser(user.getId());
			instance.getDocument().setLastFlowOperation(flowOption);
			instance.setLastFlowOperation(flowOption);
			instance.getDocument().setLastmodifier(user.getId());
			
			NodeRT currNodeRT = instance.getNodertByNodeid(currNodeId);
			StateMachine.doFlow(params, instance, currNodeId, nextNodeIds, user, flowOption, comment, Environment.getInstance());
			
			// 处理提交或回退提醒
			if (!flowOption.equals(FlowType.START2RUNNING) && !flowOption.equals(FlowType.SUSPEND2RUNNING)
					&& !flowOption.equals(FlowType.AUTO2RUNNING)) {
				notificationProcess.notifySender(origDoc, instance.getFlowVO(),currNodeRT, user,FlowType.FLOW_TYPE_MESSAGE.get(flowOption)); // 送出提醒
			}			
			
			try {
				beginTransaction();
				if(updateinstance){
					InstructionExecutor.getInstance().execute();
				}
				if (updateDocument) {
					docProcess.doCreateOrUpdate(instance.getDocument(), user);
				}
				commitTransaction();
				//生成流程图（流程图的临时文件改成正式jpg文件）
				String path = DefaultProperty.getProperty("BILLFLOW_DIAGRAMPATH");
				String dirPath = Environment.getInstance().getRealPath(path);
				String tempPath = dirPath + instance.getId() + ".jpg.temp";
				String filepath = dirPath + instance.getId() + ".jpg";
				File tempFile = new File(tempPath);
				if(tempFile.exists()){
					File file = new File(filepath);
					if(file.exists()) file.delete();
					tempFile.renameTo(new File(filepath));
				}
				
				if (!StringUtil.isBlank(instance.getAsyncSubFlowNodeId())) {
					FlowStateRT inst = (FlowStateRT) doView(instance.getId());
					Document pdoc = inst.getDocument();
					if(pdoc.getId().equals(instance.getDocument().getId())){
						pdoc.setFlowTicket(instance.getDocument().getFlowTicket());
					}
					asynchronous2Next(inst, instance.getAsyncSubFlowNodeId(), new ParamsTable(), user);
				}
			} catch (InstructionException e) {
				rollbackTransaction();
				InstructionExecutor.getInstance().clear();
				throw e;
			}
			
			//推送待办通知到Openfire服务器
			sendNotification2Openfire(instance.getDocument(),nextNodeIds,user);
			//推送当前角色
			String[] curr = {PendingNotification.CURRENT_USER};
			sendNotification2Openfire(instance.getDocument(),curr,user);

			if (flowOption.equals(FlowType.RUNNING2RUNNING_NEXT) || flowOption.equals(FlowType.RUNNING2RUNNING_INTERVENTION) || flowOption.equals(FlowType.START2RUNNING)
					|| flowOption.equals(FlowType.AUTO2RUNNING) || flowOption.equals(FlowType.RUNNING2RUNNING_TIMELIMIT)) {
				boolean neednotifyCurrentAuditors = true;
				for (Iterator<NodeRT> iterator = instance.getNoderts().iterator(); iterator
						.hasNext();) {
					NodeRT nodeRT = iterator.next();
					if(ManualNode.PASS_CONDITION_OR!=nodeRT.getPassCondition() && nodeRT.getNodeid().equals(currNodeId)){//会签节点，还没有提交到下一节点，则不发送达到提醒
						neednotifyCurrentAuditors = false;
						break;
					}
				}
				if(neednotifyCurrentAuditors){
					notificationProcess.notifyCurrentAuditors(instance.getDocument(),instance.getFlowVO(),user, currNodeId, nextNodeIds,FlowType.FLOW_TYPE_MESSAGE.get(flowOption)); // 到达提醒
				}
			} else if (flowOption.equals(FlowType.RUNNING2RUNNING_BACK)) {
				notificationProcess.notifyRejectees(instance.getDocument(),instance.getFlowVO(), params, user,FlowType.FLOW_TYPE_MESSAGE.get(flowOption)); // 回退提醒
			}

//			commitTransaction();
			
		} catch (Exception e) {
//			rollbackTransaction();
			InstructionExecutor.getInstance().clear();
			user.removeFromTmpspace(instance.getDocid());
			if(!(e instanceof WorkflowException) && !(e instanceof ValidationException)){
				e.printStackTrace();
			}
			
			throw e;
		}finally{
			FlowTicketValidator.flush(instance.getDocument());
		}
	}
	
	
	/**
	 * 推送待办通知到Openfire服务器
	 * @param doc
	 * @param nextNodeIds
	 * @throws Exception
	 */
	private void sendNotification2Openfire(Document doc,String[] nextNodeIds,BaseUser user) throws Exception {
		try {
			 //发送XMPP信息
			XMPPNotification notification = new PendingNotification(doc, PendingNotification.ACTION_UPDATE,nextNodeIds ,user);
			notification.send();
		} catch (Exception e) {
			throw e;
		}
	}

	public void runCallbackScript(ParamsTable params, FlowStateRT instance, WebUser user) throws Exception {
		try {
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), instance.getApplicationid());
			if (instance.isSubFlowStete()) {
				runner.initBSFManager(instance.getParent().getDocument(), params, user, new ArrayList<ValidateMessage>());
			} else {
				runner.initBSFManager(instance.getDocument(), params, user, new ArrayList<ValidateMessage>());
			}
			if(instance.getParent() ==null) return;
			BillDefiVO pflow = instance.getParent().getFlowVO();
			SubFlow subFlowNode = (SubFlow) pflow.toFlowDiagram().getElementByID(instance.getSubFlowNodeId());
			if (subFlowNode != null && !StringUtil.isBlank(subFlowNode.callbackScript)) {
				runner.run("callback Script:", StringUtil.dencodeHTML(subFlowNode.callbackScript));
			}
		} catch (Exception e) {
			throw new WorkflowException(e.getLocalizedMessage());
		}
	}

	public Collection<FlowStateRT> doQueryBySQL(String sql) throws Exception {
		return ((FlowStateRTDAO) getDAO()).queryBySQL(sql);
	}
	
	public boolean isAllFlowInstancesCompleted(String docId) throws Exception {
		
		return ((FlowStateRTDAO) getDAO()).isAllFlowInstancesCompleted(docId);
	}
	
	/**
	 * 获取文档的主流程实例
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT getParentFlowStateRT(Document doc) throws Exception{
		return ((FlowStateRTDAO) getDAO()).getParentFlowStateRT(doc) ;
	}

}
