package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.workflow.storage.runtime.dao.FlowReminderHistoryDAO;
import cn.myapps.util.RuntimeDaoManager;

public class FlowReminderHistoryProcessBean extends AbstractRunTimeProcessBean<FlowReminderHistory> implements
		FlowReminderHistoryProcess {
	
	public FlowReminderHistoryProcessBean(String applicationId) {
		super(applicationId);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7802403074398233572L;

	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getFlowReminderHistoryDAO(getConnection(),
				getApplicationId());
	}

	public void removeByDocument(String docId) throws Exception {
		((FlowReminderHistoryDAO)getDAO()).removeByDocument(docId);
	}

	public Collection<FlowReminderHistory> queryByDocument(String docId)
			throws Exception {
		return ((FlowReminderHistoryDAO)getDAO()).queryByDocument(docId);
	}


}
