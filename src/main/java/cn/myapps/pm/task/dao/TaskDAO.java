package cn.myapps.pm.task.dao;

import java.util.Collection;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.dao.BaseDAO;
import cn.myapps.pm.task.ejb.Follower;
import cn.myapps.pm.task.ejb.Task;

public interface TaskDAO extends BaseDAO {
	
	/**
	 * 添加关注人
	 * @param taskId
	 * @return
	 */
	public void addFollowers(String taskId,Collection<Follower> followers) throws Exception;
	
	/**
	 * 删除关注人
	 * @param userId
	 * 		用户主键
	 * @param taskId
	 * 		任务主键
	 * @throws Exception
	 */
	public void deleteFollower(String userId,String taskId) throws Exception;
	
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
	 * 
	 * @param projectId
	 * 		项目主键
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param level
	 *      优先级
	 * @param executor
	 *      执行人名称
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		日期范围
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
	public DataPackage<Task> queryByProject(String projectId,String name,int status,int level,String executer,String creater ,String currDate,String dateRangeType, String tag ,String orderName,String orderBy,int page,int lines, String overdueStatus, WebUser user) throws Exception;
	
	/**
	 * 查询任务的关注人集合
	 * @param taskId
	 * 		任务主键
	 * @return
	 * @throws Exception
	 */
	public Collection<Follower> queryFollowersByTask(String taskId) throws Exception;
	
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
	 * 根据标签查询任务集合
	 * @param projectId
	 * 		项目主键
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
	 * 
	 * @param projectId
	 * 		项目主键
	 * @param name
	 * 		任务名称
	 * @param status
	 * 		任务完成状态
	 * @param level
	 *      优先级
	 * @param executor
	 *      执行人名称
	 * @param currDate
	 * 		当前日期
	 * @param dateRangeType
	 * 		日期范围
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
}
