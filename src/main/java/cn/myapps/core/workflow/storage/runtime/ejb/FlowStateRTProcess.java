package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;

public interface FlowStateRTProcess extends IRunTimeProcess<FlowStateRT> {
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
	public abstract FlowStateRT findFlowStateRTByDocidAndFlowid(String docid,
			String flowid) throws Exception;

	/**
	 * 更新参数为继承ValueObject的对象
	 * 
	 * @param object
	 *            继承ValueObject的对象
	 * @throws Exception
	 */
	public abstract void doUpdate(ValueObject object) throws Exception;

	/**
	 * 新建一个对象
	 * 
	 * @param object
	 *            继承ValueObject的对象
	 * @throws Exception
	 */
	public abstract void doCreate(ValueObject object) throws Exception;
	
	
	/**
	 * 根据文档ID获取相关流程实例集合
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowStateRT> getFlowStateRTsByDocId(String docId) throws Exception;
	
	/**根据文档ID判断是否文档的所有流程实例都已经完成
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public boolean isAllFlowInstancesCompleted(String docId) throws Exception;
	
	
	/**
	 * 根据文档ID删除关联的流程实例
	 * @param docId
	 * @throws Exception
	 */
	public void doRemoveByDocId(String docId) throws Exception;

	public Collection<FlowStateRT> getSubStates(String parent) throws Exception;
	
	
	/**
	 * 获取当前用户可执行的流程实例
	 * @param doc
	 * @param user
	 * @param currFlowStateId
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT getCurrFlowStateRT(Document doc, WebUser user,String currFlowStateId) throws Exception;
	
	/**
	 * 当前文档下的用户是否存在可执行的多实例
	 * @param doc
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean isMultiFlowState(Document doc, WebUser user) throws Exception;
	
	
	/**
	 * 当前文档下是否有多个流程实例
	 * @param doc
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean isMultiFlowState(Document doc) throws Exception;
	
	/**
	 * 创建一个瞬时的主流程实例
	 * @param doc
	 * @param flowVO
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT createTransientFlowStateRT(Document doc,BillDefiVO flowVO,WebUser user) throws Exception;
	
	/**
	 * 创建一个瞬时的主流程实例
	 * @param doc
	 * @param flowVO
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT createTransientFlowStateRT(Document doc,String flowId,WebUser user) throws Exception;
	
	
	/**
	 * 创建一个瞬时的子流程实例
	 * @param subFlowDoc
	 * @param subFlowVO
	 * @param parentInstance
	 * @param token
	 * @param node
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT createTransientSubFlowStateRT(Document subFlowDoc,BillDefiVO subFlowVO,FlowStateRT parentInstance,String token,SubFlow node,WebUser user)throws Exception;
	
	/**
	 * 启动流程实例；
	 * @param instance
	 * 		流程实例对象
	 * @param doc
	 * 		文档对象
	 * @param params
	 * 		参数表
	 * @throws Exception
	 */
	public void startFlow(FlowStateRT instance,Document doc,ParamsTable params) throws Exception;
	
	
	/**
	 * 审批文档
	 * @param params
	 * 		参数表
	 * @param instance
	 * 		流程实例对象
	 * @param currNodeId
	 * 		当前节点id
	 * @param nextNodeIds
	 * 		下一步节点数组
	 * @param flowOption
	 * 		流程流转类型
	 * @param comment
	 * 		流程提交备注
	 * @param user
	 * 		当前操作用户
	 * @throws Exception
	 */
	public void doApprove(ParamsTable params, FlowStateRT instance, String currNodeId,
			String[] nextNodeIds, String flowOption, String comment,  WebUser user) throws Exception;
	
	/**
	 * 审批文档
	 * @param instance
	 * 		流程实例
	 * @param currNodeId
	 * 		当前节点id
	 * @param nextNodeIds
	 * 		下一步节点id数组
	 * @param flowOption
	 * 		流转类型代码
	 * @param comment
	 * 		备注
	 * @param params
	 * 		参数表
	 * @param user
	 * 		当前操作用户
	 * @param updateDocument
	 * 		是否需要更新文档
	 * @param updateDocument
	 * 		是否需要更新本实例
	 * @throws Exception
	 */
	public void doApprove(FlowStateRT instance, String currNodeId,
			String[] nextNodeIds, String flowOption, String comment,ParamsTable params,WebUser user,boolean updateDocument,boolean updateinstance) throws Exception;
	
	/**
	 * 同一批次创建的所有子流程实例是否都已经完成
	 * @param subFlowInstance
	 * @return
	 * @throws Exception
	 */
	public boolean isAllSubFlowStateRTComplete(FlowStateRT subFlowInstance) throws Exception;
	
	/**
	 * 异步提交流程到下一步
	 * @param subFlowInstance
	 * @param params
	 * @param user
	 * @throws Exception
	 */
	public void asynchronous2Next(FlowStateRT instance,String subFlowNodeid, ParamsTable params, WebUser user) throws Exception;
	/**
	 * 提交父流程实例到下一步
	 * @param subFlowInstance
	 * @param user
	 * @throws Exception
	 */
	public void doParentFlow2Next(FlowStateRT subFlowInstance,ParamsTable params, WebUser user) throws Exception;
	
	/**
	 * 子流程回调
	 * @param subFlowInstance
	 * @param runner
	 * @param script
	 * @param params
	 * @param user
	 * @throws Exception
	 */
	public void callBack(FlowStateRT subFlowInstance,IRunner runner,String script,ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 运行回调脚本
	 * @param params
	 * @param instance
	 * @param user
	 * @throws Exception
	 */
	public void runCallbackScript(ParamsTable params,FlowStateRT instance, WebUser user) throws Exception;
	
	public Collection<FlowStateRT> doQueryBySQL(String sql) throws Exception;
	
	/**
	 * 获取文档的主流程实例
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT getParentFlowStateRT(Document doc) throws Exception;
}
