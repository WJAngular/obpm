package cn.myapps.core.task.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface TaskProcess extends IDesignTimeProcess<Task> {
	/**
	 * 根据应用的标识查询定时任务
	 * 
	 * @param application
	 *            应用标识
	 * @return 任务的集合(java.util.Collection)
	 * @throws Exception
	 */
	public Collection<Task> doQuery(String application) throws Exception;

	/**
	 * 获取模块下的定时任务
	 * 
	 * @param application
	 *            应用标识
	 * @param module
	 *            模块标识
	 * @return 任务的集合(java.util.Collection)
	 * @throws Exception
	 */
	public Collection<Task> getTaskByModule(String application, String module) throws Exception;
	
	public void doCumulativeTask(Task task) throws Exception;
}
