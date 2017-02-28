package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistoryVO;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;

/**
 * 
 * @author Nicholas
 * 
 */
public interface RelationHISDAO extends IRuntimeDAO {
	public abstract RelationHIS findRelHISByCondition(String docid,
			String startnodeid, String endnodeid, boolean ispassed)
			throws Exception;

	public Collection<RelationHIS> queryRelationHIS(String docid,
			String flowid, String endnodeid) throws Exception;

	public Collection<RelationHIS> query(String docid, String flowid)
			throws Exception;

	/**
	 * 获取一条历史记录
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            流程 id
	 * @return 一条历史记录
	 * @throws Exception
	 * @deprecated since 2.6
	 */
	@Deprecated
	public RelationHIS find(String docid, String flowid) throws Exception;
	
	/**
	 * 获取一条历史记录
	 * @param docId
	 * @param flowStateId
	 * @return
	 * @throws Exception
	 */
	public RelationHIS findByDocIdAndFlowStateId(String docId, String flowStateId) throws Exception;

	public RelationHIS findLastRelationHIS(String docid, String flowid,
			String flowOperation) throws Exception;

	public RelationHIS findLastByEndNode(String docid, String flowStateId,
			String endNode) throws Exception;

	/**
	 * 根据开始节点获取最后一条记录
	 * 
	 * @param docid
	 * @param flowid
	 * @param startNode
	 * @return
	 * @throws Exception
	 */
	public RelationHIS findLastByStartNode(String docid, String flowid, String flowstatertId,
			String startNode) throws Exception;

	public Collection<String> queryStartNodeHis(String docid, String flowid,
			String endnodeid) throws Exception;

	public abstract void create(ValueObject vo) throws Exception;

	public abstract Collection<RelationHIS> queryRelationHIS(String docid,
			String flowStateId, String snodeid, String flowOperation)
			throws Exception;
	
	public abstract Collection<RelationHIS> queryByDocIdAndFlowStateId(String docid, String flowStateId) throws Exception;
	
	public abstract Collection<RelationHIS> queryAllByDocIdAndFlowStateId(String docid, String flowStateId) throws Exception;
	
	public Collection<RelationHIS> getDatas(String sql) throws Exception;
	
	public RelationHIS findCompleteRelationHIS(String docid,String flowStateId) throws Exception;
	
	public void removeByDocument(String docId) throws Exception;
	
	/**
	 * 获取指定流程实例的流程历史集合
	 * @param folowStateId
	 * 		流程实例id
	 * @return
	 * 		流程历史的集合
	 * @throws Exception
	 */
	public Collection<FlowHistoryVO> queryFlowHistorysByFolowStateId(String folowStateId) throws Exception;
}
