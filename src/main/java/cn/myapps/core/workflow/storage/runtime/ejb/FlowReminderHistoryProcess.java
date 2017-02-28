package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IRunTimeProcess;

public interface FlowReminderHistoryProcess extends
		IRunTimeProcess<FlowReminderHistory> {
	
	public void removeByDocument(String docId) throws Exception;

	public Collection<FlowReminderHistory> queryByDocument(String docId) throws Exception;

}
