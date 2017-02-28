package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;

/**
 * 
 * @author Marky
 * 
 */
public interface FlowStateRTDAO extends IRuntimeDAO {
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
	public FlowStateRT findFlowStateRTByDocidAndFlowid(String docid,
			String flowid) throws Exception;

	/**
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.dao.IBaseDAO#update(cn.myapps.base.dao.ValueObject)
	 */
	public abstract void update(ValueObject vo) throws Exception;

	public void create(ValueObject vo) throws Exception;

	public ValueObject find(String id) throws Exception;

	public Collection<FlowStateRT> queryByParent(String parent)
			throws Exception;
	
	/**
	 * 获取当前文档的流程实例
	 * @param doc
	 * @param user
	 * @param currFlowStateId
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT getCurrFlowStateRT(Document doc, WebUser user,
			String currFlowStateId)throws Exception;
	
	/**
	 * 根据文档ID获取相关流程实例集合
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowStateRT> getFlowStateRTsByDocId(String docId) throws Exception;
	
	/**
	 * 根据文档ID删除关联的流程实例
	 * @param docId
	 * @throws Exception
	 */
	public void doRemoveByDocId(String docId) throws Exception;
	
	/**
	 * 文档是否同时存在多个可执行的流程实例
	 * @param doc
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean isMultiFlowState(Document doc, WebUser user)throws Exception;
	
	
	/**
	 * 文档是否同时存在多个流程实例
	 * @param doc
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean isMultiFlowState(Document doc)throws Exception;
	
	/**
	 * 是否同一批次创建的子流程实例都已经走完
	 * @param subFlowInstance
	 * @return
	 * @throws Exception
	 */
	public boolean isAllSubFlowStateRTComplete(FlowStateRT subFlowInstance)
	throws Exception;
	
	public Collection<FlowStateRT> queryBySQL(String sql) throws Exception;
	
	/**根据文档ID判断是否文档的所有流程实例都已经完成
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public boolean isAllFlowInstancesCompleted(String docId) throws Exception;
	
	/**
	 * 获取文档的主流程实例
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public FlowStateRT getParentFlowStateRT(Document doc) throws Exception;
}
