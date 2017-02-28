package cn.myapps.pm.project.ejb;

import java.util.Collection;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.base.dao.ValueObject;

/**
 * 项目
 * 
 * @author Happy
 *
 */
public class Project extends ValueObject {

	private static final long serialVersionUID = -5196359321049838793L;
	
	/**
	 * 项目名称
	 */
	private String name;
	 
	/**
	 * 创建人名称
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
	 * 项目经理名称
	 */
	private String manager;
	 
	/**
	 * 项目经理Id
	 */
	private String managerId;
	 
	/**
	 * 项目成员集合
	 */
	private Collection<Member> members;
	 
	/**
	 * 任务总数
	 */
	private int tasksTotal;
	 
	/**
	 * 已完成任务数
	 */
	private int finishedTasksNum;
	
	/**
	 * 是否开启消息通知服务
	 */
	private boolean notification;


	public boolean isNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public Collection<Member> getMembers() {
		if(members ==null){
			try {
				members = new ProjectProcessBean().getMembersByProject(id,"");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return members;
	}

	public void setMembers(Collection<Member> members) {
		this.members = members;
	}

	public int getTasksTotal() {
		return tasksTotal;
	}

	public void setTasksTotal(int tasksTotal) {
		this.tasksTotal = tasksTotal;
	}

	public int getFinishedTasksNum() {
		return finishedTasksNum;
	}

	public void setFinishedTasksNum(int finishedTasksNum) {
		this.finishedTasksNum = finishedTasksNum;
	}
	
	
	 
}
