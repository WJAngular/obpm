package cn.myapps.core.homepage.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.homepage.ejb.Reminder;
import cn.myapps.core.homepage.ejb.ReminderProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.web.DWRHtmlUtils;

public class ReminderUtil {
	/**
	 * reminder options
	 * 
	 * @param selectFieldName
	 * @param viewid
	 * @param def
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public String createReminderOptions(String selectFieldName,
			String application, String def) throws Exception {
		ReminderProcess mp = (ReminderProcess) ProcessFactory
				.createProcess(ReminderProcess.class);
		Collection<Reminder> cool = mp.doSimpleQuery(null, application);
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "{*[Select]*}");
		if (cool != null && cool.size() > 0) {
			for (Iterator<Reminder> iterator = cool.iterator(); iterator.hasNext();) {
				Reminder vo = (Reminder) iterator.next();
				map.put(vo.getId(), vo.getTitle());

			}
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);

	}
	
	/**
	 * 获得提醒集合
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllReminder(String applicationid) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "{*[Select]*}");
		ReminderProcess reminderProcess=(ReminderProcess)ProcessFactory.createProcess(ReminderProcess.class);
		ParamsTable params = new ParamsTable();
		params.setParameter("s_applicationid", applicationid);
		DataPackage<Reminder> datas = reminderProcess.doQuery(params);
		if(datas.rowCount>0){
			for(Iterator<Reminder> ite = datas.datas.iterator();ite.hasNext();){
				Reminder reminder = (Reminder)ite.next();
				map.put(reminder.getId(), reminder.getTitle());
			}
		}
		return map;
	}
}
