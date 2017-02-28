package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IRunTimeProcess;

public interface RelationHISProcess extends IRunTimeProcess<RelationHIS> {
	public abstract RelationHIS findRelHISByCondition(String docid,
			String startnodeid, String endnodeid, boolean ispassed)
			throws Exception;

	public abstract Collection<RelationHIS> queryRelationHIS(String docid,
			String flowid, String endnodeid) throws Exception;

	/**
	 * @param docid
	 * @param flowid
	 * @return
	 * @throws Exception
	 * @deprecated since 2.6 Please use "doQueryByDocIdAndFlowStateId" method
	 */
	@Deprecated
	public abstract Collection<RelationHIS> doQuery(String docid, String flowid)
			throws Exception;
	
	public abstract Collection<RelationHIS> doQueryByDocIdAndFlowStateId(String docid, String flowStateId) throws Exception;
	
	public abstract Collection<RelationHIS> doAllQueryByDocIdAndFlowStateId(String docid, String flowStateId) throws Exception;

	/**
	 * 获取最后一条历史记录
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            流程 id
	 * @return 最后一条历史记录
	 * @throws Exception
	 * @deprecated since 2.6 Please use "doViewLastByDocIdAndFolowStateId" method
	 */
	@Deprecated
	public abstract RelationHIS doViewLast(String docid, String flowid)
			throws Exception;
	
	/**
	 * 获取最后一条历史记录
	 * 
	 * @param docid
	 *            Document id
	 * @param flowStateId
	 *            流程实例 id
	 * @return 最后一条历史记录
	 * @throws Exception
	 */

	public abstract RelationHIS doViewLastByDocIdAndFolowStateId(String docId, String flowStateId)
			throws Exception;

	/**
	 * query start node history exclude duplicate
	 */
	public Collection<String> queryStartNodeHis(String docid, String flowid,
			String endnodeid) throws Exception;

	public abstract void doCreate(ValueObject object) throws Exception;

	/**
	 * 获取最后一条历史记录
	 * 
	 * @param docid
	 * @param flowid
	 * @param flowOperation
	 *            流程操作（查看FlowType）
	 * @return
	 * @throws Exception
	 */
	public RelationHIS doViewLast(String docid, String flowid,
			String flowOperation) throws Exception;

	public abstract Collection<RelationHIS> doQueryByStartNode(String id,
			String flowStateId, String nodeid, String running2running_next)
			throws Exception;

	public RelationHIS doViewLastByEndNode(String docid, String flowStateId,
			String endnodeid) throws Exception;

	public RelationHIS doViewLastByStartNode(String docid, String flowid,String flowstatertId,
			String startnodeid) throws Exception;
	
	public Collection<RelationHIS> doQueryBySQL(String sql) throws Exception;
	
	public RelationHIS getCompleteRelationHIS(String docid,String flowStateId) throws Exception;
	
	public void doRemoveByDocument(String docId) throws Exception;
	
	/**
	 * 获取指定流程实例的流程历史集合
	 * @param folowStateId
	 * 		流程实例id
	 * @return
	 * 		流程历史的集合
	 * @throws Exception
	 */
	public Collection<FlowHistoryVO> getFlowHistorysByFolowStateId(String folowStateId) throws Exception;
}
