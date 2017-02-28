package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.workflow.storage.runtime.dao.RelationHISDAO;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.sequence.Sequence;

public class RelationHISProcessBean extends
		AbstractRunTimeProcessBean<RelationHIS> implements RelationHISProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3039102706825589912L;

	public RelationHISProcessBean(String applicationId) {
		super(applicationId);
	}

	protected IRuntimeDAO getDAO() throws Exception {
		// return new OracleRelationHISDAO(getConnection());
		// ApplicationVO app=getApplicationVO(getApplicationId());

		return new RuntimeDaoManager().getRelationHisDAO(getConnection(),
				getApplicationId());
	}

	public RelationHIS findRelHISByCondition(String docid, String startnodeid,
			String endnodeid, boolean ispassed) throws Exception {

		return ((RelationHISDAO) getDAO()).findRelHISByCondition(docid,
				startnodeid, endnodeid, ispassed);
	}

	public Collection<RelationHIS> queryRelationHIS(String docid,
			String flowid, String endnodeid) throws Exception {
		return ((RelationHISDAO) getDAO()).queryRelationHIS(docid, flowid,
				endnodeid);
	}

	public Collection<RelationHIS> doQuery(String docid, String flowid)
			throws Exception {
		return ((RelationHISDAO) getDAO()).query(docid, flowid);
	}

	/**
	 * 获取最后一条历史记录
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            流程(flow) id
	 * @return 最后一条历史记录
	 * @throws Exception
	 */
	@Deprecated
	public RelationHIS doViewLast(String docid, String flowid) throws Exception {
		return ((RelationHISDAO) getDAO()).find(docid, flowid);
	}

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
			String flowOperation) throws Exception {
		return ((RelationHISDAO) getDAO()).findLastRelationHIS(docid, flowid,
				flowOperation);
	}

	public Collection<RelationHIS> doQueryByStartNode(String docid,
			String flowStateId, String snodeid, String flowOperation)
			throws Exception {
		return ((RelationHISDAO) getDAO()).queryRelationHIS(docid, flowStateId,
				snodeid, flowOperation);
	}

	public Collection<String> queryStartNodeHis(String docid, String flowid,
			String endnodeid) throws Exception {
		return ((RelationHISDAO) getDAO()).queryStartNodeHis(docid, flowid,
				endnodeid);
	}

	/**
	 * create the value object.
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doCreate(cn.myapps.base.dao.ValueObject)
	 *      Create a value object
	 * @param vo
	 *            The value object.
	 * @throws Exception
	 */
	public void doCreate(ValueObject vo) throws Exception {
		try {
			// PersistenceUtils.beginTransaction();
			if (vo.getId() == null || vo.getId().trim().length() == 0) {
				vo.setId(Sequence.getSequence());
			}

			if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
				vo.setSortId(Sequence.getTimeSequence());
			}

			if (vo.getApplicationid()==null) {
				vo.setApplicationid(getApplicationId());
			}
			((RelationHISDAO) getDAO()).create(vo);
			// PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			// PersistenceUtils.rollbackTransaction();
		}
	}

	public RelationHIS doViewLastByEndNode(String docid, String flowStateId,
			String endnodeid) throws Exception {
		return ((RelationHISDAO) getDAO()).findLastByEndNode(docid, flowStateId,
				endnodeid);
	}

	public RelationHIS doViewLastByStartNode(String docid, String flowid,String flowstatertId,
			String startnodeid) throws Exception {
		return ((RelationHISDAO) getDAO()).findLastByStartNode(docid, flowid,flowstatertId,
				startnodeid);
	}

	public Collection<RelationHIS> doQueryByDocIdAndFlowStateId(String docid,
			String flowStateId) throws Exception {
		return ((RelationHISDAO) getDAO()).queryByDocIdAndFlowStateId(docid, flowStateId);
	}

	public RelationHIS doViewLastByDocIdAndFolowStateId(String docId,
			String flowStateId) throws Exception {
		return ((RelationHISDAO) getDAO()).findByDocIdAndFlowStateId(docId, flowStateId);
	}
	
	public Collection<RelationHIS> doAllQueryByDocIdAndFlowStateId(String docid,
			String flowStateId) throws Exception {
		return ((RelationHISDAO) getDAO()).queryAllByDocIdAndFlowStateId(docid, flowStateId);
	}

	public Collection<RelationHIS> doQueryBySQL(String sql) throws Exception {
		return ((RelationHISDAO) getDAO()).getDatas(sql);
	}

	public RelationHIS getCompleteRelationHIS(String docid, String flowStateId)
			throws Exception {
		return ((RelationHISDAO) getDAO()).findCompleteRelationHIS(docid, flowStateId);
	}
	
	public void doRemoveByDocument(String docId) throws Exception{
		((RelationHISDAO) getDAO()).removeByDocument(docId);
	}

	public Collection<FlowHistoryVO> getFlowHistorysByFolowStateId(
			String folowStateId) throws Exception {
		return ((RelationHISDAO) getDAO()).queryFlowHistorysByFolowStateId(folowStateId);
	}
}
