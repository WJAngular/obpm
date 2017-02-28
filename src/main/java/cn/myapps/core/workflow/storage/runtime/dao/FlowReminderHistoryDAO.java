package cn.myapps.core.workflow.storage.runtime.dao;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowReminderHistory;

public interface FlowReminderHistoryDAO extends IRuntimeDAO {
	
	public void removeByDocument(String docId) throws Exception;
	
	public Collection<FlowReminderHistory> queryByDocument(String docId) throws Exception;

}
