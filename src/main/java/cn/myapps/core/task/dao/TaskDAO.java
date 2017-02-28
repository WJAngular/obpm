package cn.myapps.core.task.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.task.ejb.Task;

public interface TaskDAO extends IDesignTimeDAO<Task> {

	public Collection<Task> query(String application) throws Exception;

	public Collection<Task> getTaskByModule(String application, String module)
			throws Exception;
	
	public void cumulativeTask(Task task) throws Exception;
}
