package cn.myapps.core.user.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.email.email.action.EmailUserHelper;
import cn.myapps.core.email.email.ejb.EmailUser;
import cn.myapps.core.email.runtime.mail.AuthProfile;
import cn.myapps.core.email.runtime.mail.ConnectionMetaHandler;
import cn.myapps.core.email.runtime.mail.ConnectionProfile;
import cn.myapps.core.email.runtime.mail.Protocol;
import cn.myapps.core.email.runtime.mail.ProtocolFactory;
import cn.myapps.core.email.util.Constants;
import cn.myapps.core.email.util.EmailConfig;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.usersetup.ejb.UserSetupVO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.proxy.ejb.WorkflowProxyProcessBean;
import cn.myapps.core.workflow.storage.runtime.proxy.ejb.WorkflowProxyVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class WebUser extends BaseUser {

	private static final long serialVersionUID = -1479514876856665353L;
	private static final Logger log = Logger.getLogger(WebUser.class);
	
	/**
	 * 来访设备-个人电脑
	 */
	public static final int EQUIPMENT_PC = 0;
	/**
	 * 来访设备-手机
	 */
	public static final int EQUIPMENT_PHONE = 1;
	/**
	 * 来访设备-平板
	 */
	public static final int EQUIPMENT_PAD = 2;

	private Date loginTime;

	private String managerdeptid;

	public static final String TYPE_BG_USER = "bg_User";// 后台用户

	public static final int DATA_AUTHORITY_POLICY_DEPARTMENT_UP = 1;

	public static final int DATA_AUTHORITY_POLICY_DEPARTMENT_DOWN = 2;

	public static final int DATA_AUTHORITY_POLICY_DEPARTMENT_SELF = 3;

	public static final int DATA_AUTHORITY_POLICY_GROUP_SELF = 4;

	public static final int DATA_AUTHORITY_POLICY_USER_SELF = 5;

	public static final String IS_DOMAIN_USER = "true";

	private HashMap<String, String> policy = new HashMap<String, String>(3);

	// 邮件session对象
	private EmailUser emailUser = null;
	// 邮件链接句柄
	private transient ConnectionMetaHandler connectionMetaHandler;

	private boolean recordLog = false;

	private String onlineUserid = null;

	private ConcurrentHashMap<Object, Object> tmpspace = new ConcurrentHashMap<Object, Object>();

	// private String lowerDepartmentList;

	private HashMap<String, String> lowerDepartments = new HashMap<String, String>(
			3);

	private String superiorDepartmentList;

	private String rolelist;

	private String deptlist;

	/* 用户设置信息 */
	private UserSetupVO userSetup;

	private String domainUser;

	/**
	 * 所属权限组
	 */
	private Collection<RoleVO> roles;

	/**
	 * 所属部门
	 */
	private Collection<DepartmentVO> departments;

	private String lowerdepcodelist;

	/**
	 * 工作日历种类
	 */
	private String calendarType;

	private String remarks;

	private String type;

	private Map<String,Collection<WorkflowProxyVO>> workflowProxysCache = new java.util.concurrent.ConcurrentHashMap<String, Collection<WorkflowProxyVO>>();

	private BaseUser employer;// 雇主
	
	/**
	 * 服务器地址
	 */
	private String serverAddr;
	
	/**
	 * 访问平台的设备类型
	 */
	private int equipment = EQUIPMENT_PC;
	
	/**
	 * 获取用户角色下的所有软件
	 */
	private Set<String>  applicationIds;
	
	public  Set<String>  getApplicationIds() {
		 
		if(applicationIds == null || applicationIds.size() == 0){
			Set<String> applicationSet = new HashSet<String>(); 
			try {
				 Collection<RoleVO> roles = getRoles();
				 Iterator<RoleVO> iterator = roles.iterator();
				 while(iterator.hasNext()){
					 RoleVO role= iterator.next();
				     String applicationid = role.getApplicationid();
				     if(!StringUtil.isBlank(applicationid) && !"NULL".equalsIgnoreCase(applicationid)){
				    	applicationSet.add(applicationid);
				     }
				 }
				 applicationIds = applicationSet;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return applicationIds;
	}

	public void setApplicationIds(Set<String>  applicationIds) {
		this.applicationIds = applicationIds;
	}


	public String getDomainUser() {
		return domainUser;
	}

	public void setDomainUser(String domainUser) {
		this.domainUser = domainUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取当前用户上级部门的标识列表，分号隔开的字符串
	 * 
	 * @return 部门的标识列表，分号隔开的字符串
	 */
	public String getSuperiorDepartmentList() {
		return superiorDepartmentList;
	}

	/**
	 * 生成部门的标识列表，分号隔开的字符串
	 * 
	 * @param superiorDepartmentList
	 *            部门的标识列表
	 * @throws Exception
	 */
	public void setSuperiorDepartmentList(String superiorDepartmentList)
			throws Exception {
		StringBuffer list = new StringBuffer();
		Collection<DepartmentVO> deplist = new HashSet<DepartmentVO>();
		Collection<DepartmentVO> col = getDepartments();
		if (col != null) {
			Iterator<DepartmentVO> it = col.iterator();

			DepartmentProcess dp = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			while (it.hasNext()) {
				DepartmentVO dep = (DepartmentVO) it.next();
				if (dep != null) {
					deplist.addAll(dp.getSuperiorDeptListExcludeCurrent(dep
							.getId()));
				}
			}
			it = deplist.iterator();
			while (it.hasNext()) {
				DepartmentVO dep = (DepartmentVO) it.next();
				if (dep != null)
					list.append("'" + dep.getId() + "',");
			}
			if (list.length() > 0)
				list.deleteCharAt(list.length() - 1);
			this.superiorDepartmentList = list.toString();
		}
	}
	
	

	public WebUser() {
		super();
	}

	/**
	 * 构造方法,初始化web用户的对象,将参数所属为(SuperUserVO,UserVO)对象转化为web用户
	 * 
	 * @param vo
	 *            BaseUser对象
	 * @throws Exception
	 */
	public WebUser(BaseUser vo) throws Exception {
		if (vo instanceof SuperUserVO) {
			this.setSuperAdmin((vo).isSuperAdmin());
			this.setDomainAdmin((vo).isDomainAdmin());
			this.setDeveloper((vo).isDeveloper());
			// this.setApplicationid(vo.getApplicationid());
		} else if (vo instanceof UserVO) {
			this.setDepartments(((UserVO) vo).getDepartments());
			this.setRoles(((UserVO) vo).getRoles());
			this.setDeptlist(((UserVO) vo).getDepartments());
			this.setRolelist(((UserVO) vo).getRoles());
			this.setDomainid(vo.getDomainid());// 隶属域
			this.setCalendarType(((UserVO) vo).getCalendarType());
			this.setTelephone(vo.getTelephone());
			this.setSuperior(vo.getSuperior());
			this.setRemarks(((UserVO) vo).getRemarks());
			// this.setApplicationid(vo.getDefaultApplication());
			this.setDomainUser(((UserVO) vo).getDomainUser());

			this.setUserSetup(((UserVO) vo).getUserSetup());
			this.setPublicKey(vo.getPublicKey());
		}else if(vo instanceof WebUser){
			this.calendarType = ((WebUser)vo).calendarType;
			this.connectionMetaHandler = ((WebUser)vo).connectionMetaHandler;
			this.departments = ((WebUser)vo).departments;
			this.deptlist = ((WebUser)vo).deptlist;
			this.domainUser = ((WebUser)vo).domainUser;
			this.emailUser = ((WebUser)vo).emailUser;
			this.employer = ((WebUser)vo).employer;
			this.lowerDepartments = ((WebUser)vo).lowerDepartments;
			this.lowerdepcodelist = ((WebUser)vo).lowerdepcodelist;
			this.managerdeptid = ((WebUser)vo).managerdeptid;
			this.onlineUserid = ((WebUser)vo).onlineUserid;
			this.policy = ((WebUser)vo).policy;
			this.recordLog = ((WebUser)vo).recordLog;
			this.remarks = ((WebUser)vo).remarks;
			this.rolelist = ((WebUser)vo).rolelist;
			this.roles = ((WebUser)vo).roles;
			this.serverAddr = ((WebUser)vo).serverAddr;
			this.superiorDepartmentList = ((WebUser)vo).superiorDepartmentList;
			this.tmpspace = ((WebUser)vo).tmpspace;
			this.type = ((WebUser)vo).type;
			this.userSetup = ((WebUser)vo).userSetup;
			this.workflowProxysCache = ((WebUser)vo).workflowProxysCache;
		}
		if (vo != null) {
			this.setId(vo.getId());
			this.setName(vo.getName());
			this.setLoginno(vo.getLoginno());
			this.setLoginpwd(vo.getLoginpwd());
			this.setEmail(vo.getEmail());
			this.loginTime = new Date();
			this.setStatus(vo.getStatus());
			this.setDomainid(vo.getDomainid());// 隶属域
			this.setDefaultApplication(vo.getDefaultApplication());
			this.setDefaultDepartment(vo.getDefaultDepartment());
			this.setDomainPermission(vo.getDomainPermission());
			this.setSuperiorDepartmentList("");
			this.setAvatar(vo.getAvatar());
		}
	}

	/**
	 * 获取当前用户下属部门的ID列表
	 * 
	 * @return ID组成的，分号隔开的字符串
	 * @throws Exception
	 */
	public String getLowerDepartmentList() throws Exception {
		return getLowerDepartmentList(Integer.MAX_VALUE);
	}

	/**
	 * 获取当前用户下属部门的ID列表
	 * 
	 * @param isExcludeSelf
	 *            是否排除同级（自身）部门
	 * @return ID组成的，分号隔开的字符串
	 * @throws Exception
	 */
	public String getLowerDepartmentList(boolean isExcludeSelf)
			throws Exception {
		return getLowerDepartmentList(Integer.MAX_VALUE, isExcludeSelf);
	}

	/**
	 * 获取当前用户下属部门的标识列表
	 * 
	 * @param maxDeep
	 *            最大获取的部门层数
	 * @return ID组成的，分号隔开的字符串
	 * @throws Exception
	 */
	public String getLowerDepartmentList(int maxDeep) throws Exception {
		return getLowerDepartmentList(maxDeep, false);
	}

	public String getLowerDepartmentList(int maxDeep, boolean isExcludeSelf)
			throws Exception {
		DepartmentProcess dp = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);

		String rtn = (String) lowerDepartments.get(maxDeep + "");
		if (rtn == null) {
			StringBuffer list = new StringBuffer();
			Collection<DepartmentVO> deplist = new HashSet<DepartmentVO>();
			Collection<DepartmentVO> col = getDepartments();
			if (col != null) {
				Iterator<DepartmentVO> it = col.iterator();
				String id = "";
				while (it.hasNext()) {
					DepartmentVO dep = it.next();
					if (dep != null) {
						if (dep.getSuperior() == null) {
							id = dep.getId();
							deplist = dp.queryByDomain(getDomainid());
						} else {
							deplist.addAll(dp.getUnderDeptList(dep.getId(),
									maxDeep, isExcludeSelf));
						}
					}
				}

				it = deplist.iterator();
				while (it.hasNext()) {
					DepartmentVO dep = (DepartmentVO) it.next();
					if (dep != null && !id.equals(dep.getId())) {
						list.append("'" + dep.getId() + "',");
					}
				}
				if (list.length() > 0)
					list.deleteCharAt(list.length() - 1);

			}
			rtn = list.toString();
			lowerDepartments.put(maxDeep + "", rtn);
		}
		return rtn;
	}

	/**
	 * 获取登录的时间
	 * 
	 * @return
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * 设置登录的时间
	 * 
	 * @param loginTime
	 *            时间
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * 获取下级部门的标识
	 * 
	 * @return 下级部门的标识
	 */
	public String getLowerdepcodelist() {
		return lowerdepcodelist;
	}

	public void setLowerdepcodelist(String lowerdepcodelist) {
		this.lowerdepcodelist = lowerdepcodelist;
	}

	/**
	 * 获取上级部门标识
	 * 
	 * @return 上级部门标识
	 */
	public String getManagerdeptid() {
		return managerdeptid;
	}

	/**
	 * 设置上级部门标识
	 * 
	 * @param managerdeptid
	 *            上级部门标识
	 */
	public void setManagerdeptid(String managerdeptid) {
		this.managerdeptid = managerdeptid;
	}

	public void setAuthorityString(int policy, String authroityStr) {
		String key = policy + "";
		this.policy.put(key, authroityStr);
	}

	public String getAuthorityString(int policy) throws Exception {
		if (policy == DATA_AUTHORITY_POLICY_DEPARTMENT_UP
				|| policy == DATA_AUTHORITY_POLICY_DEPARTMENT_DOWN
				|| policy == DATA_AUTHORITY_POLICY_DEPARTMENT_SELF
				|| policy == DATA_AUTHORITY_POLICY_GROUP_SELF
				|| policy == DATA_AUTHORITY_POLICY_USER_SELF) {
			String key = policy + "";
			String as = (String) this.policy.get(key);
			return as;
		} else {
			throw new OBPMValidateException("Can't use this type policy!");
		}
	}

	public static String deptList2AuthorityString(Collection<DepartmentVO> depts) {
		StringBuffer sb = new StringBuffer();
		Iterator<DepartmentVO> iter = depts.iterator();
		int count = 0;
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o != null && o instanceof DepartmentVO) {
				DepartmentVO d = (DepartmentVO) o;
				sb.append("'");
				sb.append(d.getId());
				sb.append("'");
				count++;
				if (count < depts.size()) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获取当前用户角色的ID列表
	 * 
	 * @return ID组成的，分号隔开的字符串
	 * @throws Exception
	 */
	public String getRolelist() throws Exception {
		return this.rolelist;
	}

	/**
	 * 获取当前用户角色的ID列表
	 * 
	 * @return ID组成的，分号隔开的字符串
	 * @throws Exception
	 */
	public String getRolelist(String applicationid) throws Exception {
		StringBuffer sb = new StringBuffer();
		// RoleProcess roleProcess = (RoleProcess)
		// ProcessFactory.createProcess(RoleProcess.class);
		if (this.getRoles().size() > 0) {
			for (Iterator<RoleVO> iterator = this.getRoles().iterator(); iterator
					.hasNext();) {
				RoleVO roleVO = (RoleVO) iterator.next();
				if (roleVO.getApplicationid().equals(applicationid)) {
					sb.append(roleVO.getId()).append(",");
				}
			}
		}
		if (sb.lastIndexOf(",") != -1) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}

	/**
	 * 获取当前用户部门的ID列表
	 * 
	 * @return ID组成的，分号隔开的字符串
	 * @throws Exception
	 */
	public String getDeptlist() throws Exception {
		return this.deptlist;
	}

	/**
	 * 设置用户部门
	 * 
	 * @param departments
	 *            (DepartmentVO)部门集合
	 */
	public void setDeptlist(Collection<DepartmentVO> departments) {
		StringBuffer list = new StringBuffer();
		Collection<DepartmentVO> col = departments;
		Iterator<DepartmentVO> it = col.iterator();
		while (it.hasNext()) {
			DepartmentVO dv = (DepartmentVO) it.next();
			if (dv != null)
				list.append("'" + dv.getId() + "',");
		}
		if (list.length() > 0)
			list.deleteCharAt(list.length() - 1);
		String deptlist = list.toString();
		this.deptlist = deptlist;
	}

	/**
	 * 设置用户角色
	 * 
	 * @param roles
	 *            (RoleVO)角色集合
	 */
	public void setRolelist(Collection<RoleVO> roles) {
		StringBuffer list = new StringBuffer();
		Collection<RoleVO> col = roles;
		Iterator<RoleVO> it = col.iterator();
		while (it.hasNext()) {
			RoleVO rv = (RoleVO) it.next();
			if (rv != null)
				list.append("'" + rv.getId() + "',");
		}
		if (list.length() > 0)
			list.deleteCharAt(list.length() - 1);
		String rolelist = list.toString();
		this.rolelist = rolelist;
	}

	/**
	 * 清除临时空间
	 */
	public void clearTmpspace() {
		long currTime = new Date().getTime();
		for (Iterator<Entry<Object, Object>> iterator = tmpspace.entrySet().iterator(); iterator.hasNext();) {
			Entry<Object, Object> entry = (Entry<Object, Object>) iterator.next();
			Object o = entry.getValue();
			if (o instanceof EffectiveDocument) {
				long refreshTime = ((EffectiveDocument)o).getRefreshTime().getTime();
				if(currTime - refreshTime>=1*60*1000L){
					tmpspace.remove(entry.getKey());
				}
			}else{
				tmpspace.remove(entry.getKey());
			}
			
		}
	}

	/**
	 * 获取表单的临时空间
	 * 
	 * @return 表单的临时空间
	 */
	public Collection<Object> getFromKeys() {
		return tmpspace.keySet();
	}

	/**
	 * 设置临时空间
	 * 
	 * @param key
	 *            Object类型的对象
	 * @param o
	 *            Object类型的对象
	 */
	public void putToTmpspace(Object key, Object o) {
		if(o instanceof Document){
			o = new EffectiveDocument((Document) o);
		}
		if(key !=null && o != null){
			tmpspace.put(key, o);
		}
	}

	/**
	 * 根据参数key获取表单的临时空间
	 * 
	 * @param key
	 *            保存的key
	 * @return
	 */
	public Object getFromTmpspace(Object key) {
		if(key == null) return null;
		
		Object o = tmpspace.get(key);
		if(o instanceof EffectiveDocument){
			o = ((EffectiveDocument) o).getDoc();
		}
		return o;
	}
	
	public EffectiveDocument getEffectiveDocumentFromTmpspace(String docId){
		if(docId == null) return null;
		return (EffectiveDocument) tmpspace.get(docId);
	}

	/**
	 * 移除参数key中表单的临时空间
	 * 
	 * @param key
	 *            保存临时空间的KEY
	 */
	public void removeFromTmpspace(Object key) {
		if (key != null) {
			tmpspace.remove(key);
		}
	}

	/**
	 * 获取角色集合
	 * 
	 * @return 角色集合
	 */
	public Collection<RoleVO> getRoles() {
		if (roles == null) {
			roles = new HashSet<RoleVO>();
		}
		return roles;
	}

	public Collection<RoleVO> getRolesByApplication(String application) {
		Collection<RoleVO> roles = getRoles();
		Collection<RoleVO> rtn = new ArrayList<RoleVO>();
		for (Iterator<RoleVO> iterator = roles.iterator(); iterator.hasNext();) {
			RoleVO roleVO = (RoleVO) iterator.next();
			if (roleVO.getApplicationid().equals(application)) {
				rtn.add(roleVO);
			}
		}

		return rtn;
	}

	/**
	 * 获取web用户下的部门集合
	 * 
	 * @return 部门集合
	 */
	public Collection<DepartmentVO> getDepartments() {
		if (departments == null) {
			departments = new HashSet<DepartmentVO>();
		}
		return departments;
	}

	/**
	 * 设置角色集合
	 * 
	 * @param roles
	 *            角色集合
	 */
	public void setRoles(Collection<RoleVO> roles) {
		this.roles = roles;
	}

	/**
	 * 根据角色标识取角色对象
	 * 
	 * @param roleid
	 *            角色标识
	 * @return 角色对象
	 */
	public RoleVO getRoleById(String roleid) {
		Collection<RoleVO> roleList = this.getRoles();
		for (Iterator<RoleVO> iterator = roleList.iterator(); iterator
				.hasNext();) {
			RoleVO role = (RoleVO) iterator.next();
			if (role.getId() != null && role.getId().equals(roleid)) {
				return role;
			}
		}
		return null;
	}

	/**
	 * 设置部门集合到web用户
	 * 
	 * @param departments
	 *            部门集合
	 */
	public void setDepartments(Collection<DepartmentVO> departments) {
		this.departments = departments;
	}

	/**
	 * 根据部门标识获取部门对象
	 * 
	 * @param deptid
	 *            部门标识
	 * @return 部门对象
	 */
	public DepartmentVO getDepartmentById(String deptid) {
		if (departments != null) {
			for (Iterator<DepartmentVO> iter = departments.iterator(); iter
					.hasNext();) {
				DepartmentVO dept = (DepartmentVO) iter.next();
				if (dept.getId() != null && dept.getId().equals(deptid))
					return dept;
			}
		}else{
			try {
				DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
				return (DepartmentVO) process.doView(deptid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取审核文档的用户列表
	 * 
	 * @return 审核文档的用户列表
	 * @throws Exception
	 */
	public String getActorListString(String applicationid) throws Exception {
		StringBuffer builder = new StringBuffer();
		builder.append("'").append(this.getId()).append("'");
		if (this.getWorkflowProxys(applicationid) != null
				&& this.getWorkflowProxys(applicationid).size() > 0) {
			for (WorkflowProxyVO vo : this.getWorkflowProxys(applicationid)) {
				builder.append(",").append("'").append(vo.getOwner())
						.append("'");
			}
		}
		if (!this.getRoles().isEmpty()) {
			builder.append(",").append(getRolelist());
		}
		if (!this.getDepartments().isEmpty()) {
			builder.append(",").append(getDeptlist());
		}
		return builder.toString();
	}

	/**
	 * 获取日历类型
	 * 
	 * @hibernate.property column="CALENDAR"
	 * @return 日历类型
	 */
	public String getCalendarType() {
		return calendarType;
	}

	/**
	 * 设置日历
	 * 
	 * @param 日历类型
	 */
	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public UserSetupVO getUserSetup() {
		return userSetup;
	}

	public void setUserSetup(UserSetupVO userSetup) {
		this.userSetup = userSetup;
	}

	/**
	 * 获取邮件用户
	 * 
	 * @return the emailUser
	 * @author Tom
	 */
	public EmailUser getEmailUser() {
		return emailUser;
	}

	/**
	 * 设置邮件用户
	 * 
	 * @param emailUser
	 *            the emailUser to set
	 * @author Tom
	 */
	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}

	/**
	 * 获取邮件链接句柄
	 * 
	 * @return the connectionMetaHandler
	 * @author Tom
	 */
	public ConnectionMetaHandler getConnectionMetaHandler() throws Exception {
		if (connectionMetaHandler == null) {
			EmailUserHelper.initConnectionHandler(this);
		}
		return connectionMetaHandler;
	}

	/**
	 * 设置邮件链接句柄
	 * 
	 * @param connectionMetaHandler
	 *            the connectionMetaHandler to set
	 * @author Tom
	 */
	public void setConnectionMetaHandler(
			ConnectionMetaHandler connectionMetaHandler) {
		this.connectionMetaHandler = connectionMetaHandler;
	}

	/**
	 * 注销邮件链接
	 * 
	 * @author Tom
	 */
	public void disconnectOfEmail() {
		if (connectionMetaHandler != null) {
			try {
				if (emailUser != null) {
					ConnectionProfile profile = EmailConfig
							.getConnectionProfile();
					AuthProfile auth = new AuthProfile();
					auth.setUserName(emailUser.getAccount());
					auth.setPassword(emailUser.getPassword());
					ProtocolFactory factory = new ProtocolFactory(profile,
							auth, connectionMetaHandler);
					Protocol protocol = factory
							.getProtocol(Constants.DEFAULT_FOLDER_INBOX);
					if (protocol != null) {
						protocol.disconnect();
					}
				}
			} catch (Exception e) {
				log.warn(e);
			} finally {
				try {
					connectionMetaHandler.closeFolder(true);
					connectionMetaHandler.closeStore();
				} catch (MessagingException e) {
					log.warn(e);
				}
			}
		}
	}

	public boolean isRecordLog() {
		return this.recordLog;
	}

	/**
	 * recordLog关联域的log属性
	 * 
	 * @param recordLog
	 * @see cn.myapps.core.domain.ejb.DomainVO#getLog()
	 */
	public void setRecordLog(boolean recordLog) {
		this.recordLog = recordLog;
	}

	/**
	 * @return the onlineUserid
	 * @see cn.myapps.core.user.action.OnlineUserBindingListener
	 * @see OnlineUsers
	 */
	public String getOnlineUserid() {
		return onlineUserid;
	}

	/**
	 * @param onlineUserid
	 *            the onlineUserid to set
	 * @see cn.myapps.core.user.action.OnlineUserBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void setOnlineUserid(String onlineUserid) {
		this.onlineUserid = onlineUserid;
	}

	/**
	 * 获取用户的流程代理信息
	 * 
	 * @return
	 */
	public Collection<WorkflowProxyVO> getWorkflowProxys(String applicationid) {
		Collection<WorkflowProxyVO> proxys = this.workflowProxysCache.get(applicationid);
		if (proxys == null) {
			try {
				proxys = new WorkflowProxyProcessBean(
						applicationid).getDatasByAgent(this, applicationid);
				if(proxys != null){
					this.workflowProxysCache.put(applicationid, proxys);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return proxys;
	}


	/**
	 * 当前登录用户是否为某个用户的流程代理人
	 * 
	 * @param userId
	 * @param flowId
	 * @return
	 */
	public boolean isAgent(String userId, String flowId) throws Exception {
		if (userId == null || flowId == null)
			return false;

		if (!StringUtil.isBlank(flowId)) {
			BillDefiProcess process = (BillDefiProcess) ProcessFactory
					.createProcess(BillDefiProcess.class);
			BillDefiVO flowVO = (BillDefiVO) process.doView(flowId);

			if (this.getWorkflowProxys(flowVO.getApplicationid()) == null
					|| this.getWorkflowProxys(flowVO.getApplicationid()).size() == 0)
				return false;
			Map<String, String> map = new HashMap<String, String>();
			for (WorkflowProxyVO vo : getWorkflowProxys(flowVO
					.getApplicationid())) {
				map.put(vo.getOwner() + vo.getFlowId(), vo.getApplicationid());
			}
			if (map.get(userId + flowId) != null
					&& flowVO.getApplicationid().equals(
							map.get(userId + flowId))) {
				if (this.getEmployer() == null) {
					this.setEmployer((BaseUser) ProcessFactory.createProcess(
							UserProcess.class).doView(userId));
				}
				return true;
			}
		}

		return false;
	}

	@Deprecated
	public String getApplicationid() {
		throw new UnsupportedOperationException("WebUser不支持此方法");
	}

	@Deprecated
	public void setApplicationid(String applicationid) {
		throw new UnsupportedOperationException("WebUser不支持此方法");
	}

	public BaseUser getEmployer() {
		return employer;
	}

	public void setEmployer(BaseUser employer) {
		this.employer = employer;
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public int getEquipment() {
		return equipment;
	}

	public void setEquipment(int equipment) {
		this.equipment = equipment;
	}
}

class EffectiveDocument implements Serializable {
	private static final long serialVersionUID = 3392470518120522362L;
	private String id;
	private Document doc;
	private Date refreshTime;
	
	public EffectiveDocument(Document doc){
		this.id = doc.getId();
		this.doc = doc;
		this.refreshTime = new Date();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public Date getRefreshTime() {
		return refreshTime;
	}
	public void setRefreshTime(Date refreshTime) {
		this.refreshTime = refreshTime;
	}
	
	
}
