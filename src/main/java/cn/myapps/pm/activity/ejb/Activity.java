package cn.myapps.pm.activity.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;

/**
 * 活动日志
 * @author Happy
 *
 */
public class Activity extends ValueObject {
	
	private static final long serialVersionUID = 8617772778370795863L;
	/**新建任务**/
	public static final int TYPE_NEW_TASK = 100;
	/**完成任务**/
	public static final int TYPE_COMPLETE_TASK = 102;
	/**重做任务**/
	public static final int TYPE_REDO_TASK = 103;
	/**处理中任务**/
	public static final int TYPE_IN_PROGRESS_TASK = 104;
	/**已解决任务**/
	public static final int TYPE_RESOLVED_TASK = 105;
	/**作废任务**/
	public static final int TYPE_REJECT_TASK = 106;
	
	public static Map<Integer,String> Type_Map ;
	
	static{
		Type_Map = new HashMap<Integer,String>();
		Type_Map.put(TYPE_NEW_TASK, "新建");
		Type_Map.put(TYPE_COMPLETE_TASK, "已完成");
		Type_Map.put(TYPE_REDO_TASK, "重做");
		Type_Map.put(TYPE_IN_PROGRESS_TASK, "处理中");
		Type_Map.put(TYPE_RESOLVED_TASK, "已解决");
		Type_Map.put(TYPE_REJECT_TASK, "作废");
	}
	
	
	public Activity(){
		super();
	}
	
	public Activity(String taskId,int operationType, WebUser user){
		this.taskId = taskId;
		this.userId = user.getId();
		this.userName = user.getName();
		this.operationType = operationType;
		this.operationDate = new Date();
		this.domainid = user.getDomainid();
	}

	/**
	 * 任务主键
	 */
	private String taskId;
	
	/**
	 * 任务名称
	 */
	private String taskName;
	
	private String summary;
	 
	/**
	 * 用户Id
	 */
	private String userId;
	 
	/**
	 * 用户名称
	 */
	private String userName;
	 
	/**
	 * 操作类型
	 */
	private int operationType;
	 
	/**
	 * 发生日期
	 */
	private Date operationDate;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
