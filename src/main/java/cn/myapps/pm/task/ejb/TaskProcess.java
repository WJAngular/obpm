package cn.myapps.pm.task.ejb;

import java.util.Collection;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.ejb.BaseProcess;
import cn.myapps.pm.tag.ejb.Tag;

public interface TaskProcess extends BaseProcess<Task> {
	
	public ValueObject doCreate(ValueObject vo,WebUser user) throws Exception;
	
	/**
	 * 更新任务的单个字段
	 * @param taskId
	 * 		任务主键
	 * @param updateField
	 * 		更新的字段名
	 * @param updateValue
	 * 		更新的字段值
	 * @param user
	 * 		用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public Task doSimpleUpdate(String taskId,String updateField,String updateValue,WebUser user) throws Exception;
	
	/**
	 * 添加关注人
	 * @param taskId
	 * 		任务主键
	 * @param followers
	 * 		关注人集合
	 * @param usre
	 * 		操作用户
	 * @return
	 */
	public void addFollowers(String taskId,Collection<Follower> followers,WebUser user) throws Exception;
	
	/**
	 * 删除关注人
	 * @param userId
	 * 		用户主键
	 * @param userName
	 * 		用户名称
	 * @param taskId
	 * 		任务主键
	 * @param usre
	 * 		操作用户
	 * @throws Exception
	 */
	public void deleteFollower(String userId,String userName,String taskId,WebUser user) throws Exception;
	
	/**
	 * 关注任务
	 * @param taskId
	 * 		任务主键
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void doFollow(String taskId,WebUser user) throws Exception;
	
	/**
	 * 取消关注任务
	 * @param taskId
	 * 		任务主键
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void doUnFollow(String taskId,WebUser user) throws Exception;
	
	/**
	 * 完成任务
	 * @param taskId
	 * 		任务主键
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void doComplete(String taskId,WebUser user) throws Exception;
	
	/**
	 * 重做任务
	 * @param taskId
	 * 		用户主键
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void redoTask(String taskId,WebUser user) throws Exception;
	
	/**
	 * 创建子任务
	 * @param taskId
	 * 		任务主键
	 * @param subTask
	 * 		子任务
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void createSubTask(String taskId,SubTask subTask,WebUser user) throws Exception;
	
	/**
	 * 删除子任务
	 * @param taskId
	 * 		任务主键
	 * @param subTaskId
	 * 		子任务主键
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void removeSubTask(String taskId,String subTaskId,WebUser user) throws Exception;
	
	/**
	 * 更新子任务
	 * @param taskId
	 * 		任务主键
	 * @param subTaskId
	 * 		子任务主键
	 * @param subTaskName
	 * 		子任务名称
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public SubTask updateSubTask(String taskId,String subTaskId,String subTaskName,WebUser user) throws Exception;
	

	/**
	 * 完成子任务
	 * @param taskId
	 * 		任务主键
	 * @param subTaskId
	 * 		子任务主键
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public SubTask completeSubTask(String taskId,String subTaskId,WebUser user) throws Exception;
	
	/**
	 * 重做子任务
	 * @param taskId
	 * 		任务主键
	 * @param subTaskId
	 * 		子任务主键
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public SubTask redoSubTask(String taskId,String subTaskId,WebUser user) throws Exception;
	
	/**
	 * 创建备注
	 * @param taskId
	 * 		任务主键
	 * @param remark
	 * 		备注
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void createRemark(String taskId,Remark remark,WebUser user) throws Exception;
	
	/**
	 * 删除备注
	 * @param taskId
	 * 		任务主键
	 * @param remarkId
	 * 		备注主键
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void removeRemark(String taskId,String remarkId,WebUser user) throws Exception;
	
	/**
	 * 更新备注
	 * @param taskId
	 * 		任务主键
	 * @param remarkId
	 * 		备注主键
	 * @param remarContent
	 * 		备注内容
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public Remark updateRemark(String taskId,String remarkId,String remarContent,WebUser user) throws Exception;
	
	/**
	 * 根据条件查询我的任务集合
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		完成状态
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		时间范围
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public Collection<Task> queryMyTasks(String name,int status,String currDate,String dateRangeType,WebUser user) throws Exception;
	
	/**
	 * 根据条件查询我的任务集合
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		完成状态
	 * @param startDate
	 * 		开始日期
	 * @param endDate
	 * 		结束日期
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public Collection<Task> queryMyTasks4CalendarView(String name,int status,String startDate,String endDate,WebUser user) throws Exception;
	
	/**
	 * 给任务添加标签
	 * @param taskId
	 * 		任务主键
	 * @param tagName
	 * 		任务名称
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public Tag addTag(String taskId,String tagName,WebUser user) throws Exception;
	
	/**
	 * 删除任务标签
	 * @param taskId
	 * 		任务主键
	 * @param tagName
	 * 		任务名称
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void removeTag(String taskId, String tagName, WebUser user) throws Exception;
	
	/**
	 * 设置所属项目
	 * @param taskId
	 * 		任务主键
	 * @param projectId
	 * 		项目主键
	 * @param projectName
	 * 		项目名称
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public Task setProject(String taskId,String projectId,String projectName,WebUser user) throws Exception;
	
	/**
	 * 删除所属项目
	 * @param taskId
	 * 		任务主键
	 * @param user
	 * 		操作用户
	 * @throws Exception
	 */
	public void removeProject(String taskId,WebUser user) throws Exception;
	
	/**
	 * 查询任务的关注人集合
	 * @param taskId
	 * 		任务主键
	 * @return
	 * @throws Exception
	 */
	public Collection<Follower> getFollowersByTask(String taskId) throws Exception;
	
	/**
	 * 更新任务执行人
	 * @param taskId
	 * 		任务主键
	 * @param executorId
	 * 		执行人id
	 * @param executorName
	 * 		执行人名称
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public Task updateTaskExecutor(String taskId,String executorId,String executorName,WebUser user) throws Exception;
	
	/**
	 * 查询我关注的任务集合
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		日期范围
	 * @param page
	 * 		当前页码
	 * @param lines
	 * 		单页数据条数
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public Collection<Task> queryMyFollowTasks(String name,int status,String currDate,String dateRangeType,int page,int lines,WebUser user) throws Exception;
	
	/**
	 * 根据项目查询任务集合
	 * @param projectId
	 * 		项目主键
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param level
	 *      优先级
	 * @param executer
	 *      执行者名称
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		时间范围
	 * @param page
	 * 		当前页码
	 * @param lines
	 * 		单页数据条数
	 * @param overdueStatus 
	 * 		过期状态（"overdue"为选定已过期状态）
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Task> queryTasksByProject(String projectId, String name,int status, int level, String executer, String creater,String currDate, String dateRangeType,String tag ,String orderName,String orderBy,int page,int lines, String overdueStatus, WebUser user)throws Exception;
	/**
	 * 根据标签查询任务集合
	 * @param tagName
	 * 		标签名称
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		时间范围
	 * @param page
	 * 		当前页码
	 * @param lines
	 * 		单页数据条数
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public Collection<Task> queryTasksByTag(String tagName,String name,int status,String currDate,String dateRangeType,int page,int lines,WebUser user) throws Exception;
	
	/**
	 * 查询操作用户托付的任务集合
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		时间范围
	 * @param page
	 * 		当前页码
	 * @param lines
	 * 		单页数据条数
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public Collection<Task> queryMyEntrustTasks(String name,int status,String currDate,String dateRangeType,int page,int lines,WebUser user) throws Exception;
	
	public void addAttachment(String id,String attachjson) throws Exception;
	
	public Task deleteAttachment(String id,String key) throws Exception;
	
	/**
	 * 更新任务的状态 
	 * @param taskId
	 *           任务id
	 * @param status
	 *           任务状态
	 * @param user
	 *           操作用户
	 * @return
	 *           更新后的任务
	 * @throws Exception
	 */
	public Task doUpdateTaskStatus(String taskId, int status, WebUser user) throws Exception;

	/**
	 * 根据项目查询任务集合
	 * @param projectId
	 * 		项目主键
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param level
	 *      优先级
	 * @param executer
	 *      执行者名称
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		时间范围
	 * @param page
	 * 		当前页码
	 * @param lines
	 * 		单页数据条数
	 * @param overdueStatus 
	 * 		过期状态（"overdue"为选定已过期状态）
	 * @param user
	 * 		操作用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Task> queryTasks(String projectId, String taskName,Integer status, Integer level, String executerId, String createrId,String currDate, String dateRangeType, String tag,String orderName, String orderBy, Integer page, Integer lines,String overdueStatus, WebUser user)throws Exception;
	
	/**
	 * 更新实例(提供消息提醒功能)
	 * @param task
	 * 
	 * @param user
	 * 
	 * @return
	 * @throws Exception
	 */
	public Task doUpdate(Task task,WebUser user)throws Exception;
}
