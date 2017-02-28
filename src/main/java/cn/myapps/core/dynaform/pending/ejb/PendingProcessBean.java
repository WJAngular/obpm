package cn.myapps.core.dynaform.pending.ejb;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.pending.dao.AbstractPendingDAO;
import cn.myapps.core.dynaform.pending.dao.PendingDAO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.dao.FlowStateRTDAO;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.StringUtil;

public class PendingProcessBean extends AbstractRunTimeProcessBean<PendingVO>
		implements PendingProcess {
	public final static Logger LOG = Logger.getLogger(PendingProcessBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 7961013990579740564L;

	private SummaryCfgProcess summaryCfgProcess;

	public PendingProcessBean(String applicationId) {
		super(applicationId);
		try {
			summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected IRuntimeDAO getDAO() throws Exception {
		RuntimeDaoManager runtimeDao = new RuntimeDaoManager();
		PendingDAO pendingDAO = (PendingDAO) runtimeDao.getPendingDAO(
				getConnection(), getApplicationId());
		pendingDAO.setStateRTDAO((FlowStateRTDAO) runtimeDao.getFlowStateRTDAO(
				getConnection(), getApplicationId()));
		return pendingDAO;
	}

	/**
	 * Create or remove pending for document
	 */
	public PendingVO doCreateOrRemoveByDocument(Document doc, WebUser user)
			throws Exception {
		try {
			if (!StringUtil.isBlank(doc.getStateid())) {
				// this.doRemove(doc.getId());
				// createRemovePendingPushletEvent(doc);

				PendingVO pendingVO = new PendingVO();
				ObjectUtil.copyProperties(pendingVO, doc);
				pendingVO.setDocId(doc.getId());
				pendingVO.setSummary(getSummay(doc, user));
				this.doCreate(pendingVO);

				return pendingVO;
			}

		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	public void doUpdateByDocument(Document doc, WebUser user) throws Exception {
		if (!StringUtil.isBlank(doc.getStateid())) {
			PendingVO pendingVO = findByDocIdAndFlowStateRtId(doc.getId(),
					doc.getStateid());
			if (pendingVO != null) {
				if (!doc.getState().isSubFlowStete()
						&& doc.isAllFlowInstancesCompleted()) {
					this.doRemoveByDocId(pendingVO.getDocId());
				} else {
					String _id = pendingVO.getId();
					ObjectUtil.copyProperties(pendingVO, doc);
					pendingVO.setDocId(doc.getId());
					pendingVO.setId(_id);
					String summary = getSummay(doc, user);
					pendingVO.setSummary(summary);
					this.doUpdate(pendingVO);

					updateSummaryByDocument(doc.getId(), summary);
				}

			} else {
				pendingVO = new PendingVO();
				ObjectUtil.copyProperties(pendingVO, doc);
				pendingVO.setDocId(doc.getId());
				pendingVO.setSummary(getSummay(doc, user));
				this.doCreate(pendingVO);
			}

		}
	}

	public PendingVO findByDocIdAndFlowStateRtId(String docId,
			String flowStateRtId) throws Exception {

		return (PendingVO) ((AbstractPendingDAO) getDAO())
				.findByDocIdAndFlowStateRtId(docId, flowStateRtId);
	}

	private String getSummay(Document doc, WebUser user) throws Exception {
		try {
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess
					.doViewByFormIdAndScope(doc.getFormid(),
							SummaryCfgVO.SCOPE_PENDING);
			if (summaryCfg != null) {
				doc.setStateLabel(null);
				doc.getStateLabel();
				doc.setAuditorNames(null);
				doc.getAuditorNames();
				return summaryCfg.toSummay(doc, user);
			}
			return "";
		} catch (Exception ex) {
			return "";
		}
	}

	public DataPackage<PendingVO> doQueryByFilter(ParamsTable params,
			WebUser user) throws Exception {
		PendingDAO pendingDAO = (PendingDAO) getDAO();
		return pendingDAO.queryByFilter(params, user);
	}

	public long conutByFilter(ParamsTable params, WebUser user)
			throws Exception {
		PendingDAO pendingDAO = (PendingDAO) getDAO();
		return pendingDAO.countByFilter(params, user);
	}

	public void updateSummaryByDocument(String docId, String summary)
			throws Exception {
		((PendingDAO) getDAO()).updateSummaryByDocument(docId, summary);
	}

	public DataPackage<PendingVO> queryByUser(WebUser user) throws Exception {
		return ((PendingDAO) getDAO()).queryByUser(user);
	}
}
