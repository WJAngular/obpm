package cn.myapps.core.workflow.notification.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import cn.myapps.base.dao.IRuntimeDAO;

public interface NotificationDAO extends IRuntimeDAO {
	public Collection<Map<String, Object>> queryOverDuePending(Date curDate,
			String applicationId) throws SQLException;
	
	public void updateLastOverDueReminder(String actorrtId) throws Exception;
}
