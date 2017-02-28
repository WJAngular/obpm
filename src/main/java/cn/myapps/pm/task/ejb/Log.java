package cn.myapps.pm.task.ejb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.core.user.action.WebUser;
import cn.myapps.km.util.StringUtil;

/**
 * 任务日志
 * @author Happy
 *
 */
public class Log {
	
	/**创建任务**/
	public static final int TYPE_CREATE_TASK = 100;
	/**完成任务**/
	public static final int TYPE_COMPLETE_TASK =101;
	/**重做任务**/
	public static final int TYPE_REDO_TASK =102;
	/**更新任务名称**/
	public static final int TYPE_UPDATE_NAME = 103;
	/**更新任务优先级**/
	public static final int TYPE_UPDATE_LEVEL = 104;
	/**更新任务提醒方式**/
	public static final int TYPE_UPDATE_REMIND_MODE = 105;
	/**更新任务描述**/
	public static final int TYPE_UPDATE_DESC = 106;
	/**更新任务开始日期**/
	public static final int TYPE_UPDATE_START_DATE = 109;
	/**更新任务结束日期**/
	public static final int TYPE_UPDATE_END_DATE = 110;
	/**更新任务执行人**/
	public static final int TYPE_UPDATE_EXECUTOR = 111;
	
	/**创建子任务**/
	public static final int TYPE_CREATE_SUB_TASK =200;
	/**完成子任务**/
	public static final int TYPE_COMPLETE_SUB_TASK =201;
	/**重做子任务**/
	public static final int TYPE_REDO_SUB_TASK =202;
	/**删除子任务**/
	public static final int TYPE_DELETE_SUB_TASK =203;
	/**更新子任务**/
	public static final int TYPE_UPDATE_SUB_TASK =204;
	
	/**创建备注**/
	public static final int TYPE_CREATE_REMAEK =300;
	/**删除备注**/
	public static final int TYPE_DELETE_REMAEK =301;
	/**更新备注**/
	public static final int TYPE_UPDATE_REMAEK =302;
	
	/**添加关注人**/
	public static final int TYPE_ADD_FOLLOWER =400;
	/**删除关注人**/
	public static final int TYPE_DELETE_FOLLOWER =401;
	/**关注任务**/
	public static final int TYPE_FOLLOW_TASK =402;
	/**取消关注任务**/
	public static final int TYPE_UNFOLLOW_TASK =403;
	
	public static final int TYPE_SET_PROJECT =500;
	public static final int TYPE_DELETE_PROJECT =501;
	
	public static final int TYPE_ADD_TAG =600;
	public static final int TYPE_DELETE_TAG =601;
	

	private String userId;
	 
	private String userName;
	 
	private int operationType;
	 
	private Date operationDate;
	 
	private String summary;
	
	
	
	public Log() {
		super();
		this.operationDate = new Date();
	}
	
	public Log(WebUser user) {
		super();
		this.operationDate = new Date();
		this.setUserId(user.getId());
		this.setUserName(user.getName());
	}


	public Log(Task task,int operationType,WebUser user){
		this.setUserId(user.getId());
		this.setUserName(user.getName());
		this.setOperationDate(new Date());
		this.setOperationType(operationType);
		this.setSummary(task.getName());
	}
	
	public Log(Task task,String updateField,String updateValue,WebUser user){
		if("name".equals(updateField)){
			task.setName(updateValue);
			operationType = Log.TYPE_UPDATE_NAME;
		}else if("description".equals(updateField)){
			task.setDescription(updateValue);
			operationType = Log.TYPE_UPDATE_DESC;
		}else if("level".equals(updateField)){
			task.setLevel(Integer.parseInt(updateValue));
			operationType = Log.TYPE_UPDATE_LEVEL;
		}else if("remindMode".equals(updateField)){
			task.setRemindMode(Integer.parseInt(updateValue));
			operationType = Log.TYPE_UPDATE_REMIND_MODE;
		}else if("startDate".equals(updateField)){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				if(!StringUtil.isBlank(updateValue)){
					task.setStartDate(dateFormat.parse(updateValue));
				}else{
					task.setStartDate(null);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			operationType = Log.TYPE_UPDATE_START_DATE;
		}else if("endDate".equals(updateField)){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				if(!StringUtil.isBlank(updateValue)){
					task.setEndDate(dateFormat.parse(updateValue));
				}else{
					task.setEndDate(null);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			operationType = Log.TYPE_UPDATE_END_DATE;
		}else if("executor".equals(updateField)){
			String[] _executor = updateValue.split(",");
			task.setExecutorId(_executor[0]);
			task.setExecutor(_executor[1]);
			operationType = Log.TYPE_UPDATE_EXECUTOR;
		}
		operationDate = new Date();
		
		userId = user.getId();
		userName = user.getName();
		if("executor".equals(updateField)){
			summary = task.getExecutor();
		}else{
			summary = updateValue;
		}
		
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
	
}
