package cn.myapps.webservice;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.webservice.fault.WorkFlowProcessServiceFault;

/**
 * 提供业务流程提交、回退、回撤、批量审批的功能接口
 * @author Ivan
 *
 */
public class WorkFlowProcessService {
	
	/**
	 * 流程处理
	 * 
	 * @param docId
	 *            文档Id
	 * @param currNodeId
	 *            当前结点id
	 * @param nextNodeIds
	 *            下一结点ids
	 * @param userId
	 *            用户id
	 * @param flowOption
	 *            流程操作类型
	 * @param attitude
	 *            审批备注
	 * @param applicationId
	 *            软件id
	 * @throws WorkFlowProcessServiceFault
	 */
	private void process (String docId, String currNodeId, String[] nextNodeIds, String userId, 
			String flowOption, String attitude, String applicationId) 
			throws WorkFlowProcessServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationId);
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);

			Document doc = (Document) docProcess.doView(docId);
			if(doc == null){
				throw new WorkFlowProcessServiceFault("软件(软件ID：" + applicationId + 
						")下找不到文档(文档ID：" + docId + ")");
			}
			UserVO user = (UserVO) process.doView(userId);
			if(user == null){
				throw new WorkFlowProcessServiceFault("找不到用户(用户ID：" + userId + ")");
			}
			if(nextNodeIds == null)
				throw new WorkFlowProcessServiceFault("下一结点id数组不能为空.");
			ParamsTable params = new ParamsTable();
			params.setParameter("_attitude", attitude);
			
			if(StringUtil.isBlank(currNodeId)){
				NodeRT currNode = StateMachine.getCurrUserNodeRT(doc, new WebUser(user),null);
				currNodeId = currNode.getNodeid();
			}
			FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory
					.createRuntimeProcess(FlowStateRTProcess.class, doc.getApplicationid());
			
			stateProcess.doApprove(params, doc.getState(), currNodeId, nextNodeIds, flowOption, "",new WebUser(user));

		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkFlowProcessServiceFault(e.getMessage());
			}
		}
	}

	/**
	 * 传入文档Id、流程Id、用户Id、软件Id,启动流程
	 * @param docId
	 * 			文档Id
	 * @param flowId
	 * 			流程Id
	 * @param userId
	 * 			用户Id
	 * @param applicationId
	 * 			软件Id
	 * @return  -1:失败 ,0:成功
	 * @throws WorkFlowProcessServiceFault
	 */
	public int doStartFlow(String docId,String flowId,String userId ,String applicationId) 
			throws WorkFlowProcessServiceFault {
		int result = -1;
		try {
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationId);
			Document doc = (Document) docProcess.doView(docId);
			if(doc == null){
				throw new WorkFlowProcessServiceFault("软件(软件ID：" + applicationId + 
						")下找不到文档(文档ID：" + docId + ")");
			}
			
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) process.doView(userId);
			if(user == null){
				throw new WorkFlowProcessServiceFault("找不到用户(用户ID：" + userId + ")");
			}
			
			ParamsTable params = new ParamsTable();
			params.setParameter("_flowid", flowId);
			params.setParameter("_attitude", "");
			
			if (StringUtil.isBlank(doc.getParentid()) && docProcess.isNotStart(doc, params)
					&& !StringUtil.isBlank(flowId)) { // 启动流程
				docProcess.doStartFlowOrUpdate(doc, params, new WebUser(user));
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkFlowProcessServiceFault(e.getMessage());
			}
		}
		return result;
	} 
	
	/**
	 * 传入文档Id、下一结点Id、用户Id、审批备注、软件Id 提交流程
	 * @param docId
	 * 			文档Id
	 * @param nextNodeIds
	 * 			下一结点Id
	 * @param userId
	 * 			用户Id
	 * @param attitude
	 * 			审批备注
	 * @param applicationId
	 * 			软件Id
	 * @return  -1:失败 ,0:成功
	 * @throws WorkFlowProcessServiceFault
	 */
	public int doFlow(String docId, String[] nextNodeIds, String userId, String attitude, String applicationId)
			throws WorkFlowProcessServiceFault {
		int result = -1;
		try {
			this.process(docId, null, nextNodeIds, userId, FlowType.RUNNING2RUNNING_NEXT, attitude, applicationId);
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入文档Id、下一结点Id、用户Id、审批备注、软件Id 提交流程
	 * @param docId
	 * 			文档Id
	 * @param nextNodeIds
	 * 			下一结点Id
	 * @param userId
	 * 			用户Id
	 * @param flowType
	 * 			流程提交类型 
	 * @param attitude
	 * 			审批备注
	 * @param applicationId
	 * 			软件Id
	 * @return  -1:失败 ,0:成功
	 * @throws WorkFlowProcessServiceFault
	 */
	public int doFlow(String docId, String[] nextNodeIds, String userId, String flowType,  String attitude, String applicationId) 
			throws WorkFlowProcessServiceFault {
		int result = -1;
		try {
			this.process(docId, null, nextNodeIds, userId, flowType, attitude, applicationId);
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入文档Id、当前结点Id、下一结点Id、用户Id、审批备注、软件Id 回退流程
	 * @param docId
	 * 			文档Id
	 * @param currNodeId
	 * 			当前结点Id
	 * @param nextNodeIds
	 * 			下一结点Id
	 * @param userId
	 * 			用户Id
	 * @param attitude
	 * 			审批备注
	 * @param applicationId
	 * 			软件Id
	 * @return  -1:失败 ,0:成功
	 * @throws WorkFlowProcessServiceFault
	 */
	public int doFlow(String docId, String currNodeId, String[] nextNodeIds, String userId, String attitude, String applicationId)
			throws WorkFlowProcessServiceFault {
		int result = -1;
		try {
			this.process(docId, currNodeId, nextNodeIds, userId, FlowType.RUNNING2RUNNING_NEXT, attitude, applicationId);
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入文档Id、下一结点Id、用户Id、审批备注、软件Id 回退流程
	 * @param docId
	 * 			文档Id
	 * @param backNodeId
	 * 			下一结点Id
	 * @param userId
	 * 			用户Id
	 * @param attitude
	 * 			审批备注
	 * @param applicationId
	 * 			软件Id
	 * @return  -1:失败 ,0:成功
	 * @throws WorkFlowProcessServiceFault
	 */
	public int doFlowBack(String docId, String backNodeId, String userId, String attitude, String applicationId)
			throws WorkFlowProcessServiceFault {
		int result = -1;
		try {
			String [] nextNodeIds = new String[] {backNodeId};
			this.process(docId, null, nextNodeIds, userId, FlowType.RUNNING2RUNNING_BACK, attitude, applicationId);
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入文档Id数组、下一结点数组Id、用户Id、审批备注、软件Id 批量提交流程
	 * @param docIds
	 * 			文档Id数组
	 * @param nextNodeIds
	 * 			下一结点Ids
	 * @param userId
	 * 			用户Id
	 * @param attitude
	 * 			审批备注
	 * @param applicationId
	 * 			软件Id
	 * @return  -1:失败 ,0:成功
	 * @throws WorkFlowProcessServiceFault
	 */
	public int doBatchFlow(String[] docIds, String[] nextNodeIds, String userId, String attitude, String applicationId)
			throws WorkFlowProcessServiceFault {
		int result = -1;
		try {
			if(docIds != null){
				for (int i = 0; i < docIds.length; i++) {
					this.process(docIds[i], null, nextNodeIds, userId, FlowType.RUNNING2RUNNING_NEXT, attitude, applicationId);
				}
			}
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入文档Id、流程Id、用户Id、软件Id 回撤流程
	 * @param docId
	 * 			文档Id
	 * @param flowId
	 * 			流程Id
	 * @param userId
	 * 			用户Id
	 * @param applicationId
	 * 			软件Id
	 * @return  -1:失败 ,0:成功
	 * @throws WorkFlowProcessServiceFault
	 */
	public int doRetracement (String docId, String flowId, String userId, String applicationId)
			throws WorkFlowProcessServiceFault {
		int result = -1;
		try {
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationId);
			Document doc = (Document) proxy.doView(docId);
			if(doc == null){
				throw new WorkFlowProcessServiceFault("软件(软件ID：" + applicationId + 
						")下找不到文档(文档ID：" + docId + ")");
			}
			BillDefiVO flowVO = doc.getState().getFlowVO();
			FlowDiagram fd = flowVO.toFlowDiagram();
			
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) process.doView(userId);
			if(user == null){
				throw new WorkFlowProcessServiceFault("找不到用户(用户ID：" + userId + ")");
			}
			
			ParamsTable params = new ParamsTable();
			
			Node currNode = null;
			if(doc.getState().isComplete()){
				RelationHISProcess procss = (RelationHISProcess)ProcessFactory.createRuntimeProcess(RelationHISProcess.class, doc.getApplicationid());
				RelationHIS his = procss.getCompleteRelationHIS(doc.getId(), doc.getState().getId());
				currNode = (Node) fd.getElementByID(his.getEndnodeid());
				
			}else if(doc.getState().getNoderts()!=null && doc.getState().getNoderts().size()>0){
				NodeRT nodert = (NodeRT) doc.getState().getNoderts().iterator().next();
				currNode = (Node) fd.getElementByID(nodert.getNodeid());
			}
			
			Node nextNode = StateMachine.getBackNodeByHis(doc, flowVO, currNode.id, new WebUser(user), FlowState.RUNNING);
			
			if (nextNode != null) {	
				String submitTo = "[{\"nodeid\":'" + nextNode.id + "',\"isToPerson\":'true',\"userids\":\"["
						+ user.getId() + "]\"},]";
				params.setParameter("submitTo", submitTo);
				params.setParameter("doRetracement", "true");
				
				String[] nextNodeIds = { nextNode.id };
				((DocumentProcess) proxy).doFlow(doc, params, currNode.id, nextNodeIds, 
						FlowType.RUNNING2RUNNING_RETRACEMENT, "", new WebUser(user));
			}else{
				throw new WorkFlowProcessServiceFault("你没有回撤的权限");
			}
			
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowProcessServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkFlowProcessServiceFault(e.getMessage());
			}
		}
		return result;
	}
		
}
