package cn.myapps.core.task.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.task.ejb.Task;

public class HibernateTaskDAO extends HibernateBaseDAO<Task> implements TaskDAO {

	public HibernateTaskDAO(String voClassName) {
		super(voClassName);
	}

	public Collection<Task> getTaskByModule(String application, String module)
			throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.module.id='"
				+ module + "'";
		ParamsTable params = new ParamsTable();
		params.setParameter("application", application);
		return getDatas(hql, params);
	}

	public Collection<Task> query(String application) throws Exception {
		String hql = "FROM " + _voClazzName + " vo ORDER BY vo.firstTime";

		ParamsTable params = new ParamsTable();
		params.setParameter("application", application);
		return getDatas(hql, params);
	}

	public void cumulativeTask(Task task) throws Exception {
		this.update(task);
	}

}
