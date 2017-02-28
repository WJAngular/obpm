package cn.myapps.core.domain.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.action.CalendarType;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.util.ProcessFactory;

/**
 * 
 * @hibernate.class table="T_DOMAIN" batch-size="10" lazy="false"
 */
public class DomainVO extends ValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4259448158835058227L;
	
	/**
	 * 非激活
	 */
	public static final int STATE_INACTIVE = 0;
	/**
	 * 激活
	 */
	public static final int STATE_ACTIVATED = 1;
	
	public static final String WEIXIN_PROXY_TYPE_NONE = "none";
	
	public static final String WEIXIN_PROXY_TYPE_CLOUD = "cloud";
	
	public static final String WEIXIN_PROXY_TYPE_LOCAL = "local";

	/**
	 * 域主键
	 * 
	 * @uml.property name="id"
	 */
	private String id;

	/**
	 * 域名称
	 * 
	 * @uml.property name="name"
	 */
	private String name;

	/**
	 * 部门(department)集合
	 * 
	 * @uml.property name="roles"
	 */
	private Collection<DepartmentVO> departments;
	/**
	 * 应用(Application)集合
	 */
	private Collection<ApplicationVO> applications;
	/**
	 * 域管理员(SuperUserVO)集合
	 */
	private Collection<SuperUserVO> users;
	
	/**
	 * 皮肤(Skin)类型
	 */
	private String skinType;

	/**
	 * 1:effective ;0:invalid
	 */
	private int status = 1;
	/**
	 * 默认工作日历种类
	 */
	private String defaultCalendar;
	/**
	 * 描述
	 * 
	 * @uml.property name="description"
	 */
	private String description;
	
	/**短信平台会员编码*/
	private String smsMemberCode;
	/**短信平台会员密码*/
	private String smsMemberPwd;
	
	private Boolean log;
	
	/**
	 * 系统名称
	 */
	private String systemName;
	
	/**
	 * Logo地址
	 */
	private String logoUrl;
	
	/**
	 * 微信企业号id
	 */
	private String weixinCorpID;
	
	/**
	 * 微信企业号管理组的凭证密钥
	 */
	private String weixinCorpSecret;
	
	/**
	 * 微信企业号的企业应用ID
	 */
	private String weixinAgentId;
	
	/**
	 * 服务器域名(公网域名)
	 */
	private String serverHost;
	
	/**
	 * 企业微信号代理方式
	 */
	private String weixinProxyType = WEIXIN_PROXY_TYPE_NONE;
	
	
	public String getWeixinProxyType() {
		return weixinProxyType;
	}
	public void setWeixinProxyType(String weixinProxyType) {
		this.weixinProxyType = weixinProxyType;
	}
	/**
	 * 微信企业号Token
	 */
	private String weixinToken;
	
	/**
	 * 微信企业号EncodingAESKey
	 */
	private String weixinEncodingAESKey;
	

	/**
	 * 获取域标识
	 * 
	 * @return java.lang.String
	 * @hibernate.id column="ID" generator-class="assigned"
	 * @roseuid 44C5FCE0027C
	 * @uml.property name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取域名
	 * 
	 * @return 域名<java.lang.String>
	 * @hibernate.property column="NAME"
	 * @roseuid 44C5FCE002C2
	 * @uml.property name="name"
	 */
	public String getName() {
		if (name != null)
			this.name = name.toLowerCase();
		return name;
	}

	/**
	 * 所属域的部门集合
	 * 
	 * @return 所属域的部门集合<java.util.Collection>
	 * @hibernate.set cascade = "none" order-by = "ID" name="departments"
	 *                table="T_DEPARTMENT"
	 * @hibernate.collection-one-to-many class="cn.myapps.core.department.ejb.DepartmentVO"
	 * @hibernate.collection-key column = "DOMAIN_ID"
	 * @roseuid 44C5FCE10007
	 * @uml.property name="departments"
	 */
	public Collection<DepartmentVO> getDepartments() {
		if (isLazyLoad && departments == null) {
			try {
				DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
				departments = process.queryByDomain(id);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(departments == null){
				return new HashSet<DepartmentVO>();
			}else{
				return departments;
			}
			//return new HashSet<DepartmentVO>();
		}
		return departments;
	}
	
	/**
	 * 所属域的前台用户集合
	 * 
	 * @return 所属域的部门集合<java.util.Collection>
	 * @hibernate.set cascade = "none" order-by = "ID" name="departments"
	 *                table="T_DEPARTMENT"
	 * @hibernate.collection-one-to-many class="cn.myapps.core.department.ejb.DepartmentVO"
	 * @hibernate.collection-key column = "DOMAIN_ID"
	 * @roseuid 44C5FCE10007
	 * @uml.property name="departments"
	 */
	public Collection<UserVO> getUserVOs() {
		Collection<UserVO> users = new HashSet<UserVO>();
		if(departments!=null){
			for (Iterator<DepartmentVO> iterator = departments.iterator(); iterator.hasNext();) {
				DepartmentVO departmentVO = (DepartmentVO) iterator.next();
				users.addAll(departmentVO.getUsers());
			}
		}
		return users;
	}

	/**
	 * 获取域用户
	 * 
	 * @return 域用户集合<java.util.Collection>
	 */
	public Collection<SuperUserVO> getUsers() {
		if (users == null)
			users = new HashSet<SuperUserVO>();
		return users;
	}

	/**
	 * 设置标识
	 * 
	 * @param aId
	 * @roseuid 44C5FCE00290
	 * @uml.property name="id"
	 */
	public void setId(String aId) {
		id = aId;
	}

	/**
	 * 设置域名
	 * 
	 * @param aName
	 * @roseuid 44C5FCE002D6
	 * @uml.property name="name"
	 */
	public void setName(String aName) {
		name = aName;
		if (name != null)
			this.name = name.toLowerCase();
	}

	/**
	 * 设置域部门
	 * 
	 * @param aDepartments
	 *            部门集合
	 * @roseuid 44C5FCE10025
	 * @uml.property name="departments"
	 */
	public void setDepartments(Collection<DepartmentVO> aDepartments) {
		departments = aDepartments;
	}

	/**
	 * 设置用户到域用户
	 * 
	 * @param aUsers
	 *            用户对象
	 * @roseuid 44C5FCE1007F
	 * @uml.property name="users"
	 */
	public void setUsers(Collection<SuperUserVO> aUsers) {
		users = aUsers;
	}

	/**
	 * 获取域的描述
	 * 
	 * @return 域的描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置域的描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取所属域下的应用集合
	 * 
	 * @return 应用的集合
	 */
	public Collection<ApplicationVO> getApplications() {
		if (applications == null){
			try {
				ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
				applications = process.queryByDomain(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return applications;
	}

	/**
	 * 设置应用为当前域管理
	 * 
	 * @param applications
	 */
	public void setApplications(Collection<ApplicationVO> applications) {
		this.applications = applications;
	}

	/**
	 * 获取第一个软件
	 * 
	 * @return 软件对象
	 */
	public ApplicationVO getFirstApplication() {
		if (getApplications() != null && !getApplications().isEmpty()) {
			return (ApplicationVO) getApplications().iterator().next();
		}

		return null;

	}

	/**
	 * 域状态(激活,未激活)
	 * 
	 * @hibernate column="STATUS"
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置域状态
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取域默认的工作日历种类
	 * 
	 * @return (java.lang.String) 日历标识
	 */
	public String getDefaultCalendar() {
		if (this.defaultCalendar == null) {
			try {
				CalendarVO calendar = (CalendarVO) ((CalendarProcess) ProcessFactory
						.createProcess(CalendarProcess.class)).doViewByName(CalendarType.getName(1), getId());
				defaultCalendar = calendar.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultCalendar;
	}

	/**
	 * 设置工作日历种类
	 * 
	 * @param defaultCalendar
	 */
	public void setDefaultCalendar(String defaultCalendar) {
		this.defaultCalendar = defaultCalendar;
	}

	/**
	 * 设置皮肤类型
	 * 
	 * @param skinType
	 * @uml.property name="skintype"
	 */
	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}

	/**
	 * 获取皮肤类型
	 * @return
	 */
	public String getSkinType() {
		return skinType;
	}

	/**获取短信平台会员编码*/
	public String getSmsMemberCode() {
		return smsMemberCode;
	}

	/**设置短信平台会员编码*/
	public void setSmsMemberCode(String smsMemberCode) {
		this.smsMemberCode = smsMemberCode;
	}

	/**获取短信平台会员密码*/
	public String getSmsMemberPwd() {
		return smsMemberPwd;
	}

	/**设置短信平台会员密码*/
	public void setSmsMemberPwd(String smsMemberPwd) {
		this.smsMemberPwd = smsMemberPwd;
	}

	/**
	 * @return the log
	 */
	public Boolean getLog() {
		if (log == null) {
			log = Boolean.valueOf(false);
		}
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(Boolean log) {
		this.log = log;
	}

	public String getSystemName() {
		if(systemName == null){
			systemName = "";
		}
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getLogoUrl() {
		if(logoUrl == null){
			logoUrl = "";
		}
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getWeixinCorpID() {
		return weixinCorpID;
	}

	public void setWeixinCorpID(String weixinCorpID) {
		this.weixinCorpID = weixinCorpID;
	}

	public String getWeixinCorpSecret() {
		return weixinCorpSecret;
	}

	public void setWeixinCorpSecret(String weixinCorpSecret) {
		this.weixinCorpSecret = weixinCorpSecret;
	}

	public String getWeixinAgentId() {
		return weixinAgentId;
	}

	public void setWeixinAgentId(String weixinAgentId) {
		this.weixinAgentId = weixinAgentId;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public String getWeixinToken() {
		return weixinToken;
	}

	public void setWeixinToken(String weixinToken) {
		this.weixinToken = weixinToken;
	}

	public String getWeixinEncodingAESKey() {
		return weixinEncodingAESKey;
	}

	public void setWeixinEncodingAESKey(String weixinEncodingAESKey) {
		this.weixinEncodingAESKey = weixinEncodingAESKey;
	}

}
