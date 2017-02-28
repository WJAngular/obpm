package cn.myapps.core.workflow.storage.runtime.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public class ActorRT extends ValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8623514798117541224L;

	private String id;

	private String name;

	private String actorid;

	/**
	 * 是否已处理
	 */
	private boolean isprocessed;

	/**
	 * 是否待处理
	 */
	private boolean pending = false;

	private int type;

	private String nodertid;// fk

	private String flowstatertid;// fk
	
	private String docId;

	/**
	 * 审批期限
	 */
	private Date deadline;

	/**
	 * 是否已阅
	 */
	private boolean isread = false;
	
	/**
	 * 审批次序 
	 */
	private int position;
	
	/**
	 * 催单次数
	 */
	private int reminderTimes = 0;

	/**
	 * 获取流程状态标识
	 * 
	 * @return 状态标识
	 */
	public String getFlowstatertid() {
		return flowstatertid;
	}

	/**
	 * 设置状态标识
	 * 
	 * @param flowstatertid
	 *            状态标识
	 */
	public void setFlowstatertid(String flowstatertid) {
		this.flowstatertid = flowstatertid;
	}

	/**
	 * 获取节点标识
	 * 
	 * @return 节点标识
	 */
	public String getNodertid() {
		return nodertid;
	}

	/**
	 * 设置节点标识
	 * 
	 * @param nodertid
	 *            节点标识
	 */
	public void setNodertid(String nodertid) {
		this.nodertid = nodertid;
	}

	public ActorRT() {
	}

	/**
	 * 构造方法
	 * 
	 * @param user
	 *            用户
	 * @throws SequenceException
	 */
	public ActorRT(BaseUser user) throws SequenceException {
		id = Sequence.getSequence();
		name = user.getName();
		actorid = user.getId();
		type = Type.TYPE_USER;
		isprocessed = false;
		isread = false;
		domainid = user.getDomainid();
	}

	/**
	 * 获取角色标识
	 * 
	 * @return 角色标识
	 */
	public String getActorid() {
		return actorid;
	}

	/**
	 * 设置角色标识
	 * 
	 * @param 角色标识
	 */
	public void setActorid(String actorid) {
		this.actorid = actorid;
	}

	/**
	 * 获取标识
	 * 
	 * @return 标识
	 */
	public String getId() {
		return id;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	/**
	 * 设置标识
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取是否已处理
	 * 
	 * @return 是否已处理
	 */
	public boolean getIsprocessed() {
		return isprocessed;
	}

	/**
	 * 设置是否已处理
	 * 
	 * @param isprocessed
	 *            是否已处理
	 */
	public void setIsprocessed(boolean isprocessed) {
		this.isprocessed = isprocessed;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * 比较对象,当为部门,角色,用户的时候返回true
	 * 
	 * @param user
	 * @return boolean
	 */
	public boolean isEquals(WebUser user) {
		boolean flag = false;

		switch (this.getType()) {
		case Type.TYPE_DEPARTMENT:
			if (user.getDepartments() != null) {
				if (user.getDepartmentById(this.getActorid()) != null) {
					flag = true;
				}
			}
			break;
		case Type.TYPE_ROLE:
			if (user.getRoles() != null) {
				if (user.getRoleById(this.getActorid()) != null) {
					flag = true;
				}
			}
			break;
		case Type.TYPE_USER:
			if (user.getId() != null && user.getId().equals(this.getActorid())) {//happy mark -此处需要判断是否为某个用户的流程代理人
				flag = true;
			}
			break;
		}
		return flag;
	}

	/**
	 * 获取审批文档的审批人对象集合,集合内存放(UserVO,RoleVO)
	 * 
	 * @return 对象集合
	 * @throws Exception
	 */
	public List<BaseUser> getAllUser() throws Exception {
		List<BaseUser> userList = new ArrayList<BaseUser>();

		switch (getType()) {
		case Type.TYPE_USER:
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(getActorid());
			if (null != user && user.getStatus() == 1)
				userList.add(user);
			break;
		case Type.TYPE_ROLE:
			RoleProcess roleProcess = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			RoleVO role = (RoleVO) roleProcess.doView(getActorid());
			if(role != null && RoleVO.STATUS_VALID == role.getStatus()){
				for (Iterator<UserVO> iterator = role.getUsers().iterator(); iterator
						.hasNext();) {
					UserVO roleUser = (UserVO) iterator.next();
					if (roleUser.getDomainid().equals(getDomainid())
							&& roleUser.getStatus() == 1) {
						userList.add(roleUser);
					}
				}
			}
			break;
		default:
			break;
		}

		return userList;

	}

	/**
	 * 获取过期时间
	 * 
	 * @return 过期时间
	 */
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * 设置过期时间
	 * 
	 * @param deadline
	 *            过期时间
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * 获取是否待处理
	 * 
	 * @return 是否待处理
	 */
	public boolean isPending() {
		return pending;
	}

	/**
	 * 设置是否待处理
	 * 
	 * @param pending
	 *            是否待处理
	 */
	public void setPending(boolean pending) {
		this.pending = pending;
	}

	/**
	 * 获取是否阅读
	 * 
	 * @return 是否阅读
	 */
	public boolean getIsread() {
		return isread;
	}

	/**
	 * 设置是否阅读
	 * 
	 * @param isread
	 *            是否阅读
	 */
	public void setIsread(boolean isread) {
		this.isread = isread;
	}

	public int getReminderTimes() {
		return reminderTimes;
	}

	public void setReminderTimes(int reminderTimes) {
		this.reminderTimes = reminderTimes;
	}

}
