package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;

public interface NodeRTDAO extends IRuntimeDAO {
	
	/**
	 * @param docid
	 * @param flowid
	 * @return
	 * @throws Exception
	 * @deprecate since 2.6
	 */
	@Deprecated
	public Collection<NodeRT> queryNodeRTByDocidAndFlowid(String docid,
			String flowid) throws Exception;
	
	
	public Collection<NodeRT> queryNodeRTByFlowStateIdAndDocId(String instanceId, String docId) throws Exception;

	/**
	 * 根据文档，文档相应流程查询，获取文档的所有运行时节点
	 * 
	 * @param docid
	 *            document id
	 * @param flowStateId
	 *            流程实例 id
	 * @return 文档全部的流程结点
	 * @throws Exception
	 */
	public Collection<NodeRT> query(String docid, String flowStateId)
			throws Exception;
	
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
	public Collection<NodeRT> queryDeadlineNodes(String docid, String flowStateId)
		throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#create(cn.myapps.base.dao.ValueObject)
	 */
	public void create(ValueObject vo) throws Exception;

	public void remove(String pk) throws Exception;

	public void update(ValueObject vo) throws Exception;

	public NodeRT findByNodeid(String docid, String flowid, String nodeid)
			throws Exception;
	
	public void updatePosition(ValueObject vo) throws Exception;


	public void doUpdateReminderTimes(NodeRT vo) throws Exception;
}
