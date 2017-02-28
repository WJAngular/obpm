package cn.myapps.pm.task.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.pm.tag.ejb.Tag;
import cn.myapps.pm.util.json.JsonUtil;
import cn.myapps.util.StringUtil;

/**
 * 任务
 * 
 * @author Happy
 * 
 */
public class Task extends ValueObject {

	private static final long serialVersionUID = 5740295754639960896L;

	/**自建类型的任务**/
	public static final int TYPE_CREATE_BY_MYSELF = 0;
	/**他建类型的任务**/
	public static final int TYPE_CREATE_BY_OTHER = 1;
	/**项目指派的任务**/
	public static final int TYPE_ASSING_BY_OBJECT = 2;
	
	/**不重要不紧急**/
	public static final int LEVEL_NOT_IMPORTANT_AND_NOT_URGET = 0;
	/**不重要紧急**/
	public static final int LEVEL_NOT_IMPORTANT_AND_URGET = 1;
	/**重要不紧急**/
	public static final int LEVEL_IMPORTANT_AND_NOT_URGET = 2;
	/**重要紧急**/
	public static final int LEVEL_IMPORTANT_AND_URGET = 3;
	/**JSP页面LEVEL为空**/
	public static final int LEVEL_NULL = -100;
	
	/**不提醒**/
	public static final int REMIND_MODE_NONE = 0;
	/**每天提醒**/
	public static final int REMIND_MODE_DAY = 1;
	/**每周提醒**/
	public static final int REMIND_MODE_WEEK = 2;
	/**每两周提醒**/
	public static final int REMIND_MODE_TWO_WEEK = 3;
	/**每月提醒**/
	public static final int REMIND_MODE_MONTH = 4;
	
	/**新建状态**/
	public static final int STATUS_NEW = 0;
	/**已完成状态**/
	public static final int STATUS_COMPLETE = 1;
	/**处理中状态**/
	public static final int STATUS_IN_PROGRESS = 2;
	/**已解决状态**/
	public static final int STATUS_RESOLVED = 3;
	/**作废状态**/
	public static final int STATUS_REJECT = -1;
	/**JSP页面传值的空状态**/
	public static final int STATUS_NULL = -100;
	/**JSP页面显示非作废非完成状态**/
	public static final int STATUS_ON = 101;
	
	/**已过期的任务**/
	public static final String OVERDUE_STATUS_OVERDUE = "OVERDUE";
	
	
	/**状态Map
	 *       根据字段获取相应的状态
	 * **/ 
	public static final Map<Integer,String> STATUS_MAP = new HashMap<Integer,String>();
	
	static{
		STATUS_MAP.put(STATUS_NEW, "STATUS_NEW");
		STATUS_MAP.put(STATUS_COMPLETE, "STATUS_COMPLETE");
		STATUS_MAP.put(STATUS_IN_PROGRESS, "STATUS_IN_PROGRESS");
		STATUS_MAP.put(STATUS_RESOLVED, "STATUS_RESOLVED");
		STATUS_MAP.put(STATUS_REJECT, "STATUS_REJECT");
	}

	/**
	 * 项目名称
	 */
	private String name;

	/**
	 * 任务类型()
	 */
	private int type;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 项目ID
	 */
	private String projectId;
	
	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 优先级
	 */
	private int level = LEVEL_NOT_IMPORTANT_AND_NOT_URGET;

	/**
	 * 完成状态
	 */
	private int status = STATUS_NEW;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建人ID
	 */
	private String creatorId;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 任务开始日期
	 */
	private Date startDate;

	/**
	 * 任务结束日期
	 */
	private Date endDate;

	/**
	 * 提醒方式
	 */
	private int remindMode = REMIND_MODE_NONE;

	/**
	 * 标签
	 */
	private String tags;

	/**
	 * 执行人名称
	 */
	private String executor;

	/**
	 * 执行人ID
	 */
	private String executorId;

	/**
	 * 子任务(JSON)
	 */
	private String subTasks;

	/**
	 * 备注(JSON)
	 */
	private String remark;

	/**
	 * 关注者集合
	 */
	private Collection<Follower> followers;

	/**
	 * 日志(JSON)
	 */
	private String logs;

	/**
	 * 任务完成时间
	 */
	private Date finishedDate;
	
	/**
	 * 附件
	 */
	private String attachment;
	
	private boolean hasFollow = false;

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getRemindMode() {
		return remindMode;
	}

	public void setRemindMode(int remindMode) {
		this.remindMode = remindMode;
	}

	@JSON(serialize=false)
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getExecutorId() {
		return executorId;
	}

	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}

	@JSON(serialize=false)
	public String getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(String subTasks) {
		this.subTasks = subTasks;
	}

	@JSON(serialize=false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Collection<Follower> getFollowers() {
		if(followers ==null){
			try {
				followers = new TaskProcessBean().getFollowersByTask(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return followers;
	}

	public void setFollowers(Collection<Follower> followers) {
		this.followers = followers;
	}

	@JSON(serialize=false)
	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}
	
	public boolean getHasFollow() {
		return hasFollow;
	}

	public void setHasFollow(boolean hasFollow) {
		this.hasFollow = hasFollow;
	}

	@SuppressWarnings("unchecked")
	@JSON(serialize=true,name="logs")
	public Collection<Log> getLogList(){
		if(!StringUtil.isBlank(logs)){
			try {
				return (Collection<Log>) JsonUtil.toCollection(logs, Log.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return  new ArrayList<Log>();
	}
	
	@SuppressWarnings("unchecked")
	@JSON(serialize=true,name="subTask")
	public Collection<SubTask> getSubTaskList(){
		if(!StringUtil.isBlank(subTasks)){
			try {
				return (Collection<SubTask>) JsonUtil.toCollection(subTasks, SubTask.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return new ArrayList<SubTask>();
	}
	
	@SuppressWarnings("unchecked")
	@JSON(serialize=true,name="remarks")
	public Collection<Remark> getRemarkList(){
		if(!StringUtil.isBlank(remark)){
			try {
				return (Collection<Remark>) JsonUtil.toCollection(remark, Remark.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return  new ArrayList<Remark>();
	}
	
	@JSON(serialize=true,name="tags")
	public Collection<Tag> getTagList(){
		Collection<Tag> list = new ArrayList<Tag>();
		if(!StringUtil.isBlank(tags)){
			try {
				String[] ts = tags.split(",");
				for (int i = 0; i < ts.length; i++) {
					Tag tag = new Tag();
					tag.setName(ts[i].trim());
					list.add(tag);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
}
