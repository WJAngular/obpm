package cn.myapps.core.homepage.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface ReminderProcess extends IDesignTimeProcess<Reminder> {

	public DataPackage<Reminder> doQuery(ParamsTable params, WebUser user)
			throws Exception;

	public Reminder doViewByForm(String formId, String applicationId)
			throws Exception;

	public DataPackage<Reminder> getReminderByApplication(ParamsTable params,
			String applicationid, String _currpage, String _pagelines)
			throws Exception;

	public DataPackage<Reminder> getReminderByHomepage(ParamsTable params,
			String homepageId, String applicationid, String _currpage,
			String _pagelines) throws Exception;
}
