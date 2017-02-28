package cn.myapps.webservice.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import cn.myapps.base.dao.ValueObject;

public class SimpleActorHIS extends ValueObject implements Serializable {
	
	public final static int TYPE_USER = 3;
	
	public final static int TYPE_ROLE = 2;

	/**
	 * 
	 */
	private static final long serialVersionUID = -511742656217844826L;

	private String name;

	private String actorid;
	
	private String agentid;
	
	private String agentname;

	private int type;

	private Date processtime; // 审批日期

	private String attitude; // 审批意见
	
	private Collection<String> alluserid;//所有的审批用户的集合

	public SimpleActorHIS() {

	}

	/**
	 * 构造方法
	 * 
	 * @param user
	 *            web用户
	 * @throws SequenceException
	 */
//	public SimpleActorHIS(WebUser user) throws SequenceException {
//		this.id = Sequence.getSequence();
//		this.name = user.getName();
//		this.actorid = user.getId();
//		this.type = TYPE_USER;
//	}

	/**
	 * 获取角色标识
	 * 
	 * @return 角色标识
	 * @hibernate.property column="ACTORID"
	 */
	public String getActorid() {
		return actorid;
	}

	/**
	 * 设置角色标识
	 * 
	 * @param actorid
	 *            角色标识
	 */
	public void setActorid(String actorid) {
		this.actorid = actorid;
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
	
	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	/**
	 * 获取处理日期
	 * 
	 * @return
	 */
	public Date getProcesstime() {
		return processtime;
	}

	/**
	 * 设置处理日期
	 * 
	 * @param processtime
	 */
	public void setProcesstime(Date processtime) {
		this.processtime = processtime;
	}

	/**
	 * 获取处理意见
	 * 
	 * @return
	 */
	public String getAttitude() {
		return attitude;
	}

	/**
	 * 设置处理意见
	 * 
	 * @param attitude
	 */
	public void setAttitude(String attitude) {
		this.attitude = attitude;
	}

	/**
	 * 获取所有的审批用户的集合,集合里存放(SimpleUser的对象)
	 * 
	 * @return 审批用户的集合
	 * @throws Exception
	 */
//	public List<SimpleUser> getAllUser() throws Exception {
//		List<SimpleUser> rtnList = new ArrayList<SimpleUser>();
//		List<UserVO> userList = new ArrayList<UserVO>();
//
//		switch (getType()) {
//		case TYPE_USER:
//			UserProcess userProcess = (UserProcess) ProcessFactory
//					.createProcess(UserProcess.class);
//			UserVO user = (UserVO) userProcess.doView(getActorid());
//			userList.add(user);
//			break;
//		case TYPE_ROLE:
//			RoleProcess roleProcess = (RoleProcess) ProcessFactory
//					.createProcess(RoleProcess.class);
//			RoleVO role = (RoleVO) roleProcess.doView(getActorid());
//			for (Iterator<UserVO> iterator = role.getUsers().iterator(); iterator
//					.hasNext();) {
//				UserVO roleUser = (UserVO) iterator.next();
//				if (roleUser.getDomainid().equals(getDomainid())) {
//					userList.add(roleUser);
//				}
//			}
//			break;
//		default:
//			break;
//		}
//		for (Iterator<UserVO> iterator = userList.iterator(); iterator.hasNext();) {
//			UserVO userVO = (UserVO) iterator.next();
//			SimpleUser sUser = new UserService().convertToSimple(userVO);
//			rtnList.add(sUser);
//		}
//		return rtnList;
//	}

	/**
	 * 设置所有的审批用户的集合
	 */
	public void setAlluserid(Collection<String> alluserid) {
		this.alluserid = alluserid;
	}

	/**
	 * 获取所有的审批用户的集合
	 * @return
	 */
	public Collection<String> getAlluserid() {
		return alluserid;
	}
}

