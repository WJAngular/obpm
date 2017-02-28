package cn.myapps.pm.task.ejb;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.activity.ejb.Activity;
import cn.myapps.pm.activity.ejb.ActivityProcess;
import cn.myapps.pm.activity.ejb.ActivityProcessBean;
import cn.myapps.pm.base.dao.BaseDAO;
import cn.myapps.pm.base.dao.DaoManager;
import cn.myapps.pm.base.ejb.AbstractBaseProcessBean;
import cn.myapps.pm.notification.TaskNotificationService;
import cn.myapps.pm.notification.TaskNotificationServiceImpl;
import cn.myapps.pm.project.ejb.ProjectProcess;
import cn.myapps.pm.project.ejb.ProjectProcessBean;
import cn.myapps.pm.tag.ejb.Tag;
import cn.myapps.pm.task.dao.TaskDAO;
import cn.myapps.pm.util.json.JsonUtil;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;


public class TaskProcessBean extends AbstractBaseProcessBean<Task>
		implements TaskProcess {

	private TaskNotificationService taskNotificationService = new TaskNotificationServiceImpl();

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getTaskDAO(getConnection());
	}

	public ValueObject doCreate(ValueObject vo,WebUser user) throws Exception {
		Task task = (Task)vo;
		try {
			beginTransaction();
			if(StringUtil.isBlank(task.getId())){
				task.setId(Sequence.getSequence());
			}
			task.setCreator(user.getName());
			task.setCreatorId(user.getId());
			if(StringUtil.isBlank(task.getExecutorId())){
				task.setExecutor(user.getName());
				task.setExecutorId(user.getId());
			}
			
			task.setCreateDate(new Date());
			if(task.getStartDate() == null){
				task.setStartDate(new Date());
			}
			if(task.getEndDate() == null){
				//结束时间默认为空
				task.setEndDate(null);
			}
			if(StringUtil.isBlank(task.getProjectId())){
				task.setType(Task.TYPE_CREATE_BY_MYSELF);
			}else{
				task.setType(Task.TYPE_ASSING_BY_OBJECT);
			}
			task.setDomainid(user.getDomainid());
			
			Collection<Log> logs = new ArrayList<Log>();
			logs.add(new Log(task, Log.TYPE_CREATE_TASK, user));
			task.setLogs(JsonUtil.list2JSON(logs));
			
			task = (Task) getDAO().create(task);
			
			//更新所属项目的任务数字段
			if(!StringUtil.isBlank(task.getProjectId())){
				ProjectProcess projectProcess = new ProjectProcessBean();
				projectProcess.addTasksTotal(task.getProjectId());
			}
			
			//创建Activity
			ActivityProcess activityProcess = new ActivityProcessBean();
			Activity activity = new Activity(task.getId(), Activity.TYPE_NEW_TASK, user);
			activityProcess.doCreate(activity);
			
			taskNotificationService.create(task, user);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return task;
	}
	
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
	public Task doSimpleUpdate(String taskId,String updateField,String updateValue,WebUser user) throws Exception{
		
		Task task = (Task) doView(taskId);
		try {
			beginTransaction();
			//插入日志
			Collection<Log> logs = task.getLogList();
			logs.add(new Log(task, updateField, updateValue, user));
			task.setLogs(JsonUtil.list2JSON(logs));
			if("projectId".equals(updateField)){
				ProjectProcess projectProcess = new ProjectProcessBean();
				projectProcess.addTasksTotal(task.getProjectId());
			}
			task = (Task) getDAO().update(task);
			
			if("endDate".equalsIgnoreCase(updateField)){
				taskNotificationService.updateTime(task, user);
			}
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return task;
	}

	/* (non-Javadoc)
	 * @see cn.myapps.pm.task.ejb.TaskProcess#addFollowers(java.lang.String, java.util.Collection, cn.myapps.core.user.action.WebUser)
	 */
	public void addFollowers(String taskId, Collection<Follower> followers,WebUser user)
			throws Exception {
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_ADD_FOLLOWER);
			StringBuilder userNames = new StringBuilder();
			for(Follower f :followers){
				userNames.append(f.getUserName()).append(" ");
			}
			log.setSummary(userNames.toString());
			logs.add(log);
			
			((TaskDAO)getDAO()).addFollowers(taskId, followers);
			updateLogs(taskId,JsonUtil.list2JSON(logs));
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see cn.myapps.pm.task.ejb.TaskProcess#deleteFollower(java.lang.String, java.lang.String, java.lang.String, cn.myapps.core.user.action.WebUser)
	 */
	public void deleteFollower(String userId,String userName, String taskId,WebUser user) throws Exception {
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_DELETE_FOLLOWER);
			log.setSummary(userName);
			logs.add(log);
			
			((TaskDAO)getDAO()).deleteFollower(userId, taskId);
			updateLogs(taskId,JsonUtil.list2JSON(logs));
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see cn.myapps.pm.task.ejb.TaskProcess#doFollow(java.lang.String, cn.myapps.core.user.action.WebUser)
	 */
	public void doFollow(String taskId,WebUser user) throws Exception {
		
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_FOLLOW_TASK);
			log.setSummary(user.getName());
			logs.add(log);
			
			Collection<Follower> followers = new ArrayList<Follower>();
			Follower follower = new Follower();
			follower.setUserId(user.getId());
			follower.setUserName(user.getName());
			follower.setDomainId(user.getDomainid());
			followers.add(follower);
			
			((TaskDAO)getDAO()).addFollowers(taskId,followers);
			
			updateLogs(taskId,JsonUtil.list2JSON(logs));
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see cn.myapps.pm.task.ejb.TaskProcess#doUnFollow(java.lang.String, cn.myapps.core.user.action.WebUser)
	 */
	public void doUnFollow(String taskId,WebUser user) throws Exception {
		
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_UNFOLLOW_TASK);
			log.setSummary(user.getName());
			logs.add(log);
			
			((TaskDAO)getDAO()).deleteFollower(user.getId(), taskId);
			
			updateLogs(taskId,JsonUtil.list2JSON(logs));
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public void doComplete(String taskId,WebUser user) throws Exception {
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_COMPLETE_TASK);
			log.setSummary(task.getName());
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("STATUS", Task.STATUS_COMPLETE);
			((TaskDAO)getDAO()).update(items, taskId);
			
			
			//更新所属项目的完成任务数
			if(!StringUtil.isBlank(task.getProjectId())){
				ProjectProcess projectProcess = new ProjectProcessBean();
				projectProcess.addFinishedTasksNum(task.getProjectId());
			}
			
			updateLogs(taskId,JsonUtil.list2JSON(logs));
			//级联创建Activity
			ActivityProcess activityProcess = new ActivityProcessBean();
			Activity activity = new Activity(task.getId(), Activity.TYPE_COMPLETE_TASK, user);
			activityProcess.doCreate(activity);
			commitTransaction();
			
			//发送通知
			taskNotificationService.complate(task, user);
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public void redoTask(String taskId,WebUser user) throws Exception {
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_REDO_TASK);
			log.setSummary(task.getName());
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("STATUS", Task.STATUS_IN_PROGRESS);
			((TaskDAO)getDAO()).update(items, taskId);
			
			
			//更新所属项目的完成任务数
			if(!StringUtil.isBlank(task.getProjectId())){
				ProjectProcess projectProcess = new ProjectProcessBean();
				projectProcess.subtractFinishedTasksNum(task.getProjectId());
			}
			
			updateLogs(taskId,JsonUtil.list2JSON(logs));
			//级联创建Activity
			ActivityProcess activityProcess = new ActivityProcessBean();
			Activity activity = new Activity(task.getId(), Activity.TYPE_REDO_TASK, user);
			activityProcess.doCreate(activity);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	
	@Override
	public void doRemove(String pk) throws Exception {
		try {
			beginTransaction();
			Task task = (Task) doView(pk);
			
			//更新所属项目的任务总数和完成任务数
			if(!StringUtil.isBlank(task.getProjectId())){
				ProjectProcess projectProcess = new ProjectProcessBean();
				projectProcess.subtractTasksTotal(task.getProjectId());
				if(task.getStatus() == Task.STATUS_COMPLETE){
					projectProcess.subtractFinishedTasksNum(task.getProjectId());
				}
			}
			
			getDAO().remove(pk);
			//记录删除Activity
			ActivityProcess activityProcess = new ActivityProcessBean();
			activityProcess.removeByTask(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public void createSubTask(String taskId,SubTask subTask,WebUser user) throws Exception{
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_CREATE_SUB_TASK);
			log.setSummary(subTask.getName());
			logs.add(log);
			
			Collection<SubTask> subTasks = task.getSubTaskList();
			subTasks.add(subTask);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("SUB_TASKS",JsonUtil.list2JSON(subTasks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public void removeSubTask(String taskId,String subTaskId,WebUser user) throws Exception{
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			Collection<SubTask> subTasks = task.getSubTaskList();
			SubTask po = null;
			for(SubTask subTask : subTasks){
				if(subTaskId.equals(subTask.getId())){
					po = subTask;
					break;
				}
			}
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_DELETE_SUB_TASK);
			log.setSummary(po.getName());
			logs.add(log);
			
			subTasks.remove(po);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("SUB_TASKS",JsonUtil.list2JSON(subTasks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public SubTask updateSubTask(String taskId,String subTaskId,String subTaskName,WebUser user) throws Exception{
		SubTask subTask = null;
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			
			List<SubTask> subTasks = (List<SubTask>) task.getSubTaskList();
			int index = 0;
			
			for (Iterator<SubTask> iterator = subTasks.iterator(); iterator.hasNext();) {
				SubTask st = iterator.next();
				if(subTaskId.equals(st.getId())){
					subTask = st;
					break;
				}
				index++;
				
			}
			if(subTask ==null) throw new RuntimeException("找不到对象");
			subTask.setName(subTaskName);
			subTasks.remove(index);
			subTasks.add(index,subTask);
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_UPDATE_SUB_TASK);
			log.setSummary(subTaskName);
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("SUB_TASKS",JsonUtil.list2JSON(subTasks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return subTask;
	}
	
	public SubTask completeSubTask(String taskId,String subTaskId,WebUser user) throws Exception{
		SubTask subTask = null;
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			
			List<SubTask> subTasks = (List<SubTask>) task.getSubTaskList();
			int index = 0;
			
			for (Iterator<SubTask> iterator = subTasks.iterator(); iterator.hasNext();) {
				SubTask st = iterator.next();
				if(subTaskId.equals(st.getId())){
					subTask = st;
					break;
				}
				index++;
				
			}
			if(subTask ==null) throw new RuntimeException("找不到对象");
			subTask.setStatus(SubTask.STATUS_COMPLETE);
			subTasks.remove(index);
			subTasks.add(index,subTask);
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_COMPLETE_SUB_TASK);
			log.setSummary(subTask.getName());
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("SUB_TASKS",JsonUtil.list2JSON(subTasks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return subTask;
	}
	
	public SubTask redoSubTask(String taskId,String subTaskId,WebUser user) throws Exception{
		SubTask subTask = null;
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			
			List<SubTask> subTasks = (List<SubTask>) task.getSubTaskList();
			int index = 0;
			
			for (Iterator<SubTask> iterator = subTasks.iterator(); iterator.hasNext();) {
				SubTask st = iterator.next();
				if(subTaskId.equals(st.getId())){
					subTask = st;
					break;
				}
				index++;
				
			}
			if(subTask ==null) throw new RuntimeException("找不到对象");
			subTask.setStatus(SubTask.STATUS_UNCOMPLETE);
			subTasks.remove(index);
			subTasks.add(index,subTask);
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_REDO_SUB_TASK);
			log.setSummary(subTask.getName());
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("SUB_TASKS",JsonUtil.list2JSON(subTasks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return subTask;
	}
	
	public void createRemark(String taskId,Remark remark,WebUser user) throws Exception{
		try {
			beginTransaction();
			//插入日志
			Task task = (Task) doView(taskId);
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_CREATE_REMAEK);
			log.setSummary(remark.getContent());
			logs.add(log);
			
			Collection<Remark> remarks = task.getRemarkList();
			remarks.add(remark);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("REMARK",JsonUtil.list2JSON(remarks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public void removeRemark(String taskId,String remarkId,WebUser user) throws Exception{
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			Collection<Remark> remarks = task.getRemarkList();
			Remark po = null;
			for(Remark remark : remarks){
				if(remarkId.equals(remark.getId())){
					po = remark;
					break;
				}
			}
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_DELETE_REMAEK);
			log.setSummary(po.getContent());
			logs.add(log);
			
			remarks.remove(po);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("REMARK",JsonUtil.list2JSON(remarks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public Remark updateRemark(String taskId,String remarkId,String remarkContent,WebUser user) throws Exception{
		Remark remark = null;
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			
			List<Remark> remarks = (List<Remark>) task.getRemarkList();
			int index = 0;
			
			for (Iterator<Remark> iterator = remarks.iterator(); iterator.hasNext();) {
				Remark st = iterator.next();
				if(remarkId.equals(st.getId())){
					remark = st;
					break;
				}
				index++;
				
			}
			if(remark ==null) throw new RuntimeException("找不到对象");
			remark.setContent(remarkContent);
			remarks.remove(index);
			remarks.add(index,remark);
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_UPDATE_REMAEK);
			log.setSummary(remarkContent);
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("REMARK",JsonUtil.list2JSON(remarks));
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return remark;
	}

	/**
	 * 更新任务日志
	 * @param taskId
	 * 		任务主键
	 * @param logs
	 * 		日志内容（JSON）
	 * @throws Exception
	 */
	private void updateLogs(String taskId,String logs) throws Exception {
		try {
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("LOGS", logs);
			getDAO().update(items, taskId);
		} catch (Exception e) {
			throw e;
		}
	}

	public Collection<Task> queryMyTasks(String name, int status, String currDate,
			String dateRangeType, WebUser user) throws Exception {
		return ((TaskDAO)getDAO()).queryMyTasks(name, status, currDate, dateRangeType, user);
	}

	public Tag addTag(String taskId, String tagName, WebUser user) throws Exception {
		Tag tag = new Tag();
		tag.setName(tagName);
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			
			String tags = StringUtil.isBlank(task.getTags())? tagName : task.getTags()+", "+tagName+" ";
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_ADD_TAG);
			log.setSummary(tagName);
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("TAGS",tags);
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return tag;
		
	}
	
	public void removeTag(String taskId, String tagName, WebUser user) throws Exception {
		try {
			Task task = (Task) doView(taskId);
			beginTransaction();
			
			StringBuilder tags = new StringBuilder();
			
			String[] arr = task.getTags().split(",");
			for (int i = 0; i < arr.length; i++) {
				if(!tagName.equals(arr[i].trim())){
					tags.append(arr[i]).append(",");
				}
			}
			if(tags.length()>0){
				tags.setLength(tags.length() -1);
			}
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_DELETE_TAG);
			log.setSummary(tagName);
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("TAGS",tags.toString());
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}
	
	
	public Task setProject(String taskId,String projectId,String projectName,WebUser user) throws Exception{
		Task task = (Task) doView(taskId);
		try {
			task.setProjectId(projectId);
			task.setProjectName(projectName);
			beginTransaction();
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_SET_PROJECT);
			log.setSummary(projectName);
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("PROJECT_ID",projectId);
			items.put("PROJECT_NAME",projectName);
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			
			ProjectProcess projectProcess = new ProjectProcessBean();
			
			if(Task.STATUS_COMPLETE == task.getStatus()){
				projectProcess.addFinishedTasksNum(task.getProjectId());;
			}
			
			projectProcess.addTasksTotal(task.getProjectId());
			
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return task;
	}
	
	public void removeProject(String taskId,WebUser user) throws Exception{
		Task task = (Task) doView(taskId);
		try {
			beginTransaction();
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_DELETE_PROJECT);
			log.setSummary(task.getProjectName());
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("PROJECT_ID",null);
			items.put("PROJECT_NAME",null);
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			
			ProjectProcess projectProcess = new ProjectProcessBean();
			
			if(Task.STATUS_COMPLETE == task.getStatus()){
				projectProcess.subtractFinishedTasksNum(task.getProjectId());
			}
			
			projectProcess.subtractTasksTotal(task.getProjectId());
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public Collection<Follower> getFollowersByTask(String taskId)throws Exception{
		return ((TaskDAO)getDAO()).queryFollowersByTask(taskId);
	}

	public Task updateTaskExecutor(String taskId, String executorId,
			String executorName, WebUser user) throws Exception {
		Task task = (Task) doView(taskId);
		//获取旧执行人的Id
		String old_tasker_id = task.getExecutorId();
		try {
			task.setExecutorId(executorId);
			task.setExecutor(executorName);
			beginTransaction();
			
			//插入日志
			Collection<Log> logs = task.getLogList();
			Log log = new Log(user);
			log.setOperationType(Log.TYPE_UPDATE_EXECUTOR);
			log.setSummary(executorName);
			logs.add(log);
			
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("EXECUTER_ID",executorId);
			items.put("EXECUTER",executorName);
			items.put("LOGS",JsonUtil.list2JSON(logs));
			
			((TaskDAO)getDAO()).update(items, taskId);
			
			//　执行者修改后通知发送
		    taskNotificationService.updateExecutor(task,old_tasker_id,user);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return task;
	}
	
	public Collection<Task> queryMyFollowTasks(String name,int status,String currDate,String dateRangeType,int page,int lines,WebUser user) throws Exception{
		return ((TaskDAO)getDAO()).queryMyFollowTasks(name, status, currDate, dateRangeType, page,lines,user);
	}
	
	
	public DataPackage<Task> queryTasksByProject(String projectId, String name,
			int status, int level, String executer, String creater,String currDate, String dateRangeType, String tag ,String orderName,String orderBy,int page,int lines, String overdueStatus, WebUser user)
			throws Exception {
		return ((TaskDAO)getDAO()).queryByProject(projectId, name, status, level, executer,creater, currDate, dateRangeType,tag, orderName,orderBy,page, lines, overdueStatus, user);
	}

	public Collection<Task> queryTasksByTag(String tagName, String name,
			int status, String currDate, String dateRangeType,int page,int lines, WebUser user)
			throws Exception {
		return ((TaskDAO)getDAO()).queryTasksByTag(tagName, name, status, currDate, dateRangeType,page,lines, user);
	}

	public Collection<Task> queryMyTasks4CalendarView(String name, int status,
			String startDate, String endDate, WebUser user) throws Exception {
		return ((TaskDAO)getDAO()).queryMyTasks4CalendarView(name, status, startDate, endDate, user);
	}

	public Collection<Task> queryMyEntrustTasks(String name, int status,
			String currDate, String dateRangeType, int page, int lines,
			WebUser user) throws Exception {
		return ((TaskDAO)getDAO()).queryMyEntrustTasks(name, status, currDate, dateRangeType, page,lines,user);
	}
	
	public void addAttachment(String id,String attachjson) throws Exception{
		((TaskDAO)getDAO()).addAttachment(id, attachjson);
	}

	public Task deleteAttachment(String id,String key) throws Exception{
		return ((TaskDAO)getDAO()).deleteAttachment(id, key);
	}
	
	@Override
	public Task doUpdateTaskStatus(String taskId, int status, WebUser user)
			throws Exception {
		
		Task task = (Task) doView(taskId);
		String status_complete = Task.STATUS_MAP.get(Task.STATUS_COMPLETE);
		String status_reject = Task.STATUS_MAP.get(Task.STATUS_REJECT);
		
		String old_Status = Task.STATUS_MAP.get(task.getStatus());
		String new_Status = Task.STATUS_MAP.get(status);
		try {
			
			beginTransaction();
			//插入日志
			Collection<Log> logs = task.getLogList();
			logs.add(new Log(task, "status",Integer.toString(status), user));
			task.setLogs(JsonUtil.list2JSON(logs));
			
			//更新任务
			task.setStatus(status);
			task = (Task) getDAO().update(task);
			
			//增加/减少项目数
			if(!StringUtil.isBlank(task.getProjectId())){
				if((!old_Status.equals(status_complete) && !old_Status.equals(status_reject)) && (new_Status.equals(status_complete) || new_Status.equals(status_reject))){ // 逻辑：非完成状态转化为 完成状态  ，项目完成数加一
					ProjectProcess projectProcess = new ProjectProcessBean();
					projectProcess.addFinishedTasksNum(task.getProjectId());
				}else if((old_Status.equals(status_complete) || old_Status.equals(status_reject))  && (!new_Status.equals(status_complete) && !new_Status.equals(status_reject))){  // 逻辑：完成状态转化为非完成状态  ，项目完成数减一
					ProjectProcess projectProcess = new ProjectProcessBean();
					projectProcess.subtractFinishedTasksNum(task.getProjectId());
				}
			}
			
			String Activity_Field = null;
			Field declaredField = null;
			int Activity_Value = 0 ;
			
			//获取旧状态相应的Activity字段
			Activity_Field = "TYPE_"+old_Status.substring(7)+"_TASK";
			declaredField = Activity.class.getDeclaredField(Activity_Field);
			Activity_Value = (Integer) declaredField.get(Activity_Field);
			String old_Status_ = Activity.Type_Map.get(Activity_Value);
			
			//获取新状态相应的Activity字段信息
			Activity_Field = "TYPE_"+new_Status.substring(7)+"_TASK";
			declaredField = Activity.class.getDeclaredField(Activity_Field);
			Activity_Value = (Integer) declaredField.get(Activity_Field);
			String new_Status_ = Activity.Type_Map.get(Activity_Value);
			
			//级联创建Activity
			ActivityProcess activityProcess = new ActivityProcessBean();
			Activity activity = new Activity(task.getId(), Activity_Value, user);
			activityProcess.doCreate(activity);
		
			if("STATUS_COMPLETE".equalsIgnoreCase(new_Status)){
				taskNotificationService.complate(task, user);
			}else{
				taskNotificationService.updateTaskStatus(task,old_Status_,new_Status_, user);
			}
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return task;
	}

	@Override
	public DataPackage<Task> queryTasks(String projectId, String taskName,Integer status, Integer level, String executerId, String createrId,String currDate, String dateRangeType, String tag,String orderName, String orderBy, Integer page, Integer lines,String overdueStatus, WebUser user) throws Exception{
		return ((TaskDAO)getDAO()).queryTasks(projectId, taskName, status, level, executerId,createrId, currDate, dateRangeType,tag, orderName,orderBy,page, lines, overdueStatus, user);
	}

	@Override
	public Task doUpdate(Task vo,WebUser user) throws Exception {
		ProjectProcess projectProcess = null ;
		Task task = (Task) doView(vo.getId());
		
		if (task != null) {
	    	
	    String old_ExecutorId = ""; // 原执行者
	    Date old_EndTime = null ;   // 原结束时间
		try {
			beginTransaction();
			//获取日志
			Collection<Log> logs = task.getLogList();
			
			if(!StringUtil.isBlank(vo.getName()) && !vo.getName().equals(task.getName())){//名称修改
				logs.add(new Log(task, "name", vo.getName(), user));
			}
			
			if(!StringUtil.isBlank(vo.getExecutorId()) && !vo.getExecutorId().equals(task.getExecutorId())){//修改执行人
				old_ExecutorId = task.getExecutorId();
				logs.add(new Log(task, "executor", vo.getExecutorId()+","+vo.getExecutor(), user));
			}
			
			if(!compareTime(vo.getStartDate(),task.getStartDate())){//修改开始时间
				if(vo.getStartDate() == null){
					logs.add(new Log(task, "startDate", "", user));
				}else{
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String startDate = sdf.format(vo.getStartDate());
						logs.add(new Log(task, "startDate", startDate, user));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if(!compareTime(vo.getEndDate(),task.getEndDate())){//修改结束时间
				old_EndTime = task.getEndDate();
				if(vo.getEndDate() == null){
					logs.add(new Log(task, "endDate", "", user));
				}else{
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String endDate = sdf.format(vo.getEndDate());
						logs.add(new Log(task, "endDate", endDate, user));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if(vo.getLevel() != task.getLevel()){//修改级别
				logs.add(new Log(task, "level",String.valueOf(vo.getLevel()) , user));
			}
			
			String old_Status = Task.STATUS_MAP.get(task.getStatus());
			String new_Status = Task.STATUS_MAP.get(vo.getStatus());
			String old_StatusLable ="";
			String new_StatusLable ="";
			
			if(!old_Status.equals(new_Status)){ //修改状态
				
				String status_complete = Task.STATUS_MAP.get(Task.STATUS_COMPLETE);
				String status_reject = Task.STATUS_MAP.get(Task.STATUS_REJECT);
				
				//增加/减少完成数目
				if(!StringUtil.isBlank(task.getProjectId())){
					if((!old_Status.equals(status_complete) && !old_Status.equals(status_reject)) && (new_Status.equals(status_complete) || new_Status.equals(status_reject))){ // 逻辑：非完成状态转化为 完成状态  ，项目完成数加一
						if(projectProcess == null){
							projectProcess = new ProjectProcessBean();
						}
						projectProcess.addFinishedTasksNum(task.getProjectId());
					}else if((old_Status.equals(status_complete) || old_Status.equals(status_reject))  && (!new_Status.equals(status_complete) && !new_Status.equals(status_reject))){  // 逻辑：完成状态转化为非完成状态  ，项目完成数减一
						if(projectProcess == null){
							projectProcess = new ProjectProcessBean();
						}
						projectProcess.subtractFinishedTasksNum(task.getProjectId());
					}
				}
				
				String Activity_Field = null;
				Field declaredField = null;
				int Activity_Value = 0 ;
				
				//获取旧状态相应的Activity字段
				Activity_Field = "TYPE_"+old_Status.substring(7)+"_TASK";
				declaredField = Activity.class.getDeclaredField(Activity_Field);
				Activity_Value = (Integer) declaredField.get(Activity_Field);
				old_StatusLable = Activity.Type_Map.get(Activity_Value);
				
				//获取新状态相应的Activity字段信息
				Activity_Field = "TYPE_"+new_Status.substring(7)+"_TASK";
				declaredField = Activity.class.getDeclaredField(Activity_Field);
				Activity_Value = (Integer) declaredField.get(Activity_Field);
				new_StatusLable = Activity.Type_Map.get(Activity_Value);
			}
			
			
	    	if(!StringUtil.isBlank(vo.getAttachment()) && !vo.getAttachment().equals(task.getAttachment())){ //修改附件
	    		task.setAttachment(vo.getAttachment());
	    	}
	    	
	    	if(!StringUtil.isBlank(vo.getTags()) && !vo.getTags().equals(task.getTags())){//更新标签
	    		Log log = new Log(user);
				log.setOperationType(Log.TYPE_ADD_TAG);
				log.setSummary(vo.getTags());
				logs.add(log);
				task.setTags(vo.getTags());
	    	}
	    	
	    	if(!StringUtil.isBlank(vo.getProjectId()) && !vo.getProjectId().equals(task.getProjectId())){ //修改项目
	    		if(projectProcess == null){
	    			projectProcess = new ProjectProcessBean();
	    		}
	    		//1.更新原有项目的任务数量
	    		projectProcess.subtractTasksTotal(task.getProjectId());
	    		if(task.getStatus() == Task.STATUS_COMPLETE){
	    			projectProcess.subtractFinishedTasksNum(task.getProjectId());
	    		}
	    		//2.更新task
	    		task.setProjectId(vo.getProjectId());
		    	task.setProjectName(vo.getProjectName());
		    	
		    	//3.更新现有项目的任务数量
		    	projectProcess.addTasksTotal(task.getProjectId());
		    	if(task.getStatus() == Task.STATUS_COMPLETE){
	    			projectProcess.addFinishedTasksNum(task.getProjectId());
	    		}
	    	}
			task.setLogs(JsonUtil.list2JSON(logs));
			
			task = (Task) getDAO().update(task);
			
			//　执行修改后通知发送
			if(!StringUtil.isBlank(old_ExecutorId) && !old_ExecutorId.equals(task.getExecutorId())){//修改执行人
				taskNotificationService.updateExecutor(task,old_ExecutorId,user);
			}
			if(!compareTime(task.getEndDate(),old_EndTime)){//修改结束时间
				taskNotificationService.updateTime(task, user);
			}
			if(!old_Status.equals(new_Status)){ //状态
				if("STATUS_COMPLETE".equalsIgnoreCase(new_Status)){
					taskNotificationService.complate(task, user);
				}else{
					taskNotificationService.updateTaskStatus(task,old_StatusLable,new_StatusLable, user);
				}
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	 }
		return task;
	}
	
	private Boolean compareTime(Date d1 ,Date d2){
		long time1 = 0 ;
		long time2 = 0 ; 
		
		if(d1 != null){
			time1 = d1.getTime();
		}
		if( d2 != null){          
			time2 = d2.getTime(); 
		}
		return time1 == time2 ;
	}
	
}
