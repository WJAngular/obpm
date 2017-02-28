package cn.myapps.core.homepage.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.homepage.ejb.Reminder;

public interface ReminderDAO extends IRuntimeDAO {
	public Reminder findByForm(String formId, String applicationId)
			throws Exception;

	public Collection<Reminder> findReminder(String id) throws Exception;

	public DataPackage<Reminder> getReminderByApplication(ParamsTable params,
			String applicationid, String _currpage, String _pagelines)
			throws Exception;

	public DataPackage<Reminder> getReminderByHomepage(ParamsTable params,
			String homepageId, String applicationid, String _currpage,
			String _pagelines) throws Exception;
}
