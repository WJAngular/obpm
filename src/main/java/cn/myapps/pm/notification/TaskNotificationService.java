package cn.myapps.pm.notification;

import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.task.ejb.Task;

/**
 * PM消息提醒服务
 * @author Happy
 *
 */
public interface TaskNotificationService {
	
	/**
	 * 新建任务
	 * @param task
	 * 		任务
	 * @param user
	 * 		当前操作人
	 * @throws Exception
	 */
	public void create(Task task,WebUser user) throws Exception;
	
	/**
	 * 完成任务
	 * @param task
	 * 		任务
	 * @param user
	 * 		当前操作人
	 * @throws Exception
	 */
	public void complate(Task task,WebUser user) throws Exception;
	
	/**
	 * 更新任务执行人
	 * @param task
	 * 		任务
	 * @param user
	 * 		当前操作人
	 * @param old_tasker_id
	 *      旧执行人
	 * @throws Exception
	 */
	public void updateExecutor(Task task, String old_tasker_id,WebUser user) throws Exception;
	
	/**
	 * 重做任务
	 * @param task
	 * 		任务
	 * @param user
	 * 		当前操作人
	 * @throws Exception
	 */
	public void undo(Task task,WebUser user) throws Exception;
	
	/**
	 * 更新任务时间
	 * @param task
	 * 		任务
	 * @param user
	 * 		当前操作人
	 * @throws Exception
	 */
	public void updateTime(Task task,WebUser user) throws Exception;
	
	/**
	 * 更新任务状态
	 * @param task
	 *         任务
	 * @param old_Status
	 *         旧状态
	 * @param new_Status
	 *         新状态
	 * @param user
	 *         当前操作人
	 * @throws Exception
	 */
	public void updateTaskStatus(Task task, String old_Status,
			String new_Status, WebUser user)throws Exception;
	
	

}
