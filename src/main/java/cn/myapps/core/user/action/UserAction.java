package cn.myapps.core.user.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.action.DomainHelper;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.email.email.action.EmailUserHelper;
import cn.myapps.core.email.email.ejb.EmailUser;
import cn.myapps.core.email.email.ejb.EmailUserProcess;
import cn.myapps.core.logger.ejb.LogProcess;
import cn.myapps.core.networkdisk.ejb.NetDiskProcess;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.usergroup.ejb.UserGroupSetProcess;
import cn.myapps.core.usersetup.ejb.UserSetupProcess;
import cn.myapps.core.usersetup.ejb.UserSetupVO;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.xmpp.XMPPSender;
import cn.myapps.core.xmpp.notification.ContactNotification;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.org.ejb.NRoleProcessBean;
import cn.myapps.km.org.ejb.NUserRoleSetProcess;
import cn.myapps.km.org.ejb.NUserRoleSetProcessBean;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;


import flex.messaging.util.URLDecoder;
public class UserAction extends BaseAction<UserVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4965166990637532872L;
	private String domain;
	private String sm_domainid;

	private String superiorid;
	private String _proxyUser;
	
	private UserDefined userDefined;
	private EmailUser emailUser;

	private Collection<RoleVO> _roleList;// 取得角色(Bluce)
	private Collection<DepartmentVO> _departmentlist;
	private Collection<ApplicationVO> _applicationlist;// 取得所有应用(Bluce)
	
	protected String startProxyTime;
	protected String endProxyTime;

	private DataPackage<WebUser> dpg = null;
	private Collection<NRole> _kmRoles;
	
	public DataPackage<WebUser> getDpg() {
		return dpg;
	}

	public void setDpg(DataPackage<WebUser> dpg) {
		this.dpg = dpg;
	}

	public String getSm_domainid() {
		return sm_domainid;
	}

	public void setSm_domainid(String smDomainid) {
		sm_domainid = smDomainid;
	}

	public Collection<NRole> get_kmRoles() {
		if(_kmRoles ==null){
			try {
				_kmRoles = new NRoleProcessBean().doGetRoles();
			} catch (OBPMValidateException e) {
				addFieldError("", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
		return _kmRoles;
	}

	public void set_kmRoles(Collection<NRole> _kmRoles) {
		this._kmRoles = _kmRoles;
	}

	public String doNew() {
		try {
			getContent().setId(Sequence.getSequence());
			//ParamsTable params = getParams();
			//String domain = params.getParameterAsString("domain");
			return super.doNew();
		} catch (SequenceException e) {
			addFieldError("", e.getMessage());
			return INPUT;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 设置 权限资源
	 * 
	 * @param _resourcelist
	 *            资源集合
	 * 
	 *            public void set_resourcelist(Collection _resourcelist) {
	 *            this._resourcelist = _resourcelist; }
	 */

	public String get_proxyUser() {
		UserVO content = (UserVO) this.getContent();
		if (content.getProxyUser() != null) {
			_proxyUser = content.getProxyUser().getId();
		}
		return _proxyUser;
	}

	public void set_proxyUser(String proxyUser) throws Exception {
		_proxyUser = proxyUser;
		if (!StringUtil.isBlank(proxyUser)) {
			UserVO content = (UserVO) this.getContent();
			UserVO proxyUserVO = (UserVO) process.doView(proxyUser);
			if (proxyUserVO != null) {
				content.setProxyUser(proxyUserVO);
			}
		}
	}

	/**
	 * 返回Department集合
	 * 
	 * @return Department集合
	 * @throws Exception
	 */
	public Collection<DepartmentVO> get_departmentlist() throws Exception {
		DepartmentProcess dp = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		_departmentlist = new ArrayList<DepartmentVO>();

//		DepartmentVO departmentVO = dp.getRootDepartmentByApplication(
//				getApplication(), getDomain());
//		Collection<DepartmentVO> subDeptList = dp.getUnderDeptList(departmentVO
//				.getId(), Integer.MAX_VALUE);
		List<DepartmentVO> subDeptList = (List<DepartmentVO>) dp.doQueryByHQL("from "+DepartmentVO.class.getName()+" vo where vo.domain.id='"+getDomain()+"' order by vo.level desc,vo.orderByNo", 1, Integer.MAX_VALUE);
		//subDeptList.remove(0);
//		_departmentlist.add(departmentVO);
		_departmentlist.addAll(subDeptList);

		return _departmentlist;
	}

	/**
	 * Set Department集合
	 * 
	 * @param _departmentlist
	 *            Department集合
	 */
	public void set_departmentlist(Collection<DepartmentVO> _departmentlist) {
		this._departmentlist = _departmentlist;
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public UserAction() throws Exception {
		super(ProcessFactory.createProcess(UserProcess.class), new UserVO());
	}

	/**
	 * 返回树形Department集合
	 * 
	 * @return Department集合
	 * @throws Exception
	 */
	public Map<String, String> get_departmentTree() throws Exception {
		DepartmentProcess da = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		Collection<DepartmentVO> dc = da.queryByDomain(getDomain());
		Map<String, String> dm = ((DepartmentProcess) da)
				.deepSearchDepartmentTree(dc, null, getContent().getId(), 0);

		return dm;
	}

	/**
	 * 修改获取企业域代码
	 * 
	 * @修改人：Bluce
	 * @return 角色集合
	 * @throws Exception
	 * @修改时间：2010－05－10
	 */
	public Collection<RoleVO> get_roleList() throws Exception {
		StringBuffer applicationids = new StringBuffer();
		RoleProcess rp = (RoleProcess) ProcessFactory
				.createProcess(RoleProcess.class);
		// DomainVO vo = DomainHelper.getDomainVO(getUser());//获取不到企业域(Bluce)
		DomainVO vo = DomainHelper.getDomainVO(domain);
		Collection<ApplicationVO> apps = vo.getApplications();
		Collection<RoleVO> rtn = new LinkedHashSet<RoleVO>();
		for (Iterator<ApplicationVO> iterator = apps.iterator(); iterator
				.hasNext();) {
			ApplicationVO applicationVO = (ApplicationVO) iterator.next();
			applicationids.append("'" + applicationVO.getId() + "',");
		}
		if(applicationids.length()>0){
			applicationids.setLength(applicationids.length()-1);
		}
		rtn = rp.getRolesByapplicationids(applicationids.toString());
		return rtn;
	}

	/**
	 * 返回Department主键集合
	 * 
	 * @return Department主键集合
	 */
	public Collection<String> get_departmentids() {
		LinkedHashSet<String> ids = new LinkedHashSet<String>();

		UserVO user = (UserVO) getContent();
		Collection<DepartmentVO> dptlist = user.getDepartments();
		if (dptlist != null) {
			Iterator<DepartmentVO> iter = dptlist.iterator();
			while (iter.hasNext()) {
				DepartmentVO dept = iter.next();
				ids.add(dept.getId());
			}
		}
		return ids;
	}

	/**
	 * Set Department集合
	 * 
	 * @param _departmentid
	 *            Department主键集合
	 * @throws Exception
	 */

	public void set_departmentids(Collection<String> _departmentids)
			throws Exception {
		Map<?, ?> m = getContext().getParameters();
		Object obj = m.get("_deptSelectItem");// 选中的部门
		String tmp[] = null;
		if (obj instanceof String[] && ((String[]) obj).length > 0) {
			tmp = (String[]) obj;
		}
		UserVO user = (UserVO) getContent();
		if (tmp != null) {
			for (int i = 0; i < tmp.length; i++) {
				DepartmentProcess da = (DepartmentProcess) ProcessFactory
						.createProcess(DepartmentProcess.class);
				DepartmentVO dpt = (DepartmentVO) da.doView(tmp[i]);

				UserDepartmentSet set = new UserDepartmentSet(user.getId(), dpt
						.getId());
				user.getUserDepartmentSets().add(set);
			}
		}

	}
	
	private void setDepartments() throws Exception{
		ParamsTable params = getParams();
		UserVO user = (UserVO) getContent();
		String _departmentids = params.getParameterAsString("_departmentids");
		if(StringUtil.isBlank(_departmentids)) return;
		String[] deptIds = _departmentids.split(";");
		Collection<DepartmentVO> depts = new ArrayList<DepartmentVO>();
		for (int i = 0; i < deptIds.length; i++) {
			DepartmentProcess da = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			DepartmentVO dpt = (DepartmentVO) da.doView(deptIds[i]);
			depts.add(dpt);
			UserDepartmentSet set = new UserDepartmentSet(user.getId(), dpt
					.getId());
			user.getUserDepartmentSets().add(set);
		}
		user.setDepartments(depts);
	}

	/**
	 * 保存用户信息
	 * 
	 * @return "SUCCESS"表示成功处理,否则返回错误提示
	 */
	public String doSave() {
		try {
			UserVO user = (UserVO) getContent();
			// user.setUserRoleSets(get_Roles());
			if(user.getLoginno().equals("admin")){
				this.addFieldError("1", "{*[NotCanUseadminAsLoginno]*}");
				return INPUT;
			}
			if(user.getProxyUser()!=null && user.getStartProxyTime()!=null && user.getEndProxyTime()==null){
				this.addFieldError("1", "{*[cn.myapps.core.user.tip.proxyenddate]*}");
				return INPUT;
			}
			
			if(user.getProxyUser()!=null && user.getStartProxyTime()==null && user.getEndProxyTime()!=null){
				this.addFieldError("1", "{*[cn.myapps.core.user.tip.proxystartdate]*}");
				return INPUT;
			}
			
			if(user.getProxyUser()!=null && startProxyTime!=null &&user.getStartProxyTime().getTime()>user.getEndProxyTime().getTime()){
				this.addFieldError("1", "{*[page.core.calendar.overoftime]*}");
				return INPUT;
			}
			
			if(user.getProxyUser()!=null && endProxyTime!=null &&user.getEndProxyTime().getTime()<(new Date()).getTime()){
				this.addFieldError("1", "{*[cn.myapps.core.user.tip.proxyenddate_currenttime]*}");
				return INPUT;
			}
			PropertyUtil.reload("passwordLegal");
			String passwordLength = PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LENGTH);
			
			if (passwordLength != null && !passwordLength.trim().equals("")) {
				int length = Integer.parseInt(passwordLength);
				if(user.getLoginpwd().length()<length){                         //判断密码长度
					this.addFieldError("1", "{*[PasswordLengthCanNotLow]*}"+length);
					return INPUT;
				}
			}
			
			String legal = PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LEGAL);
			if(legal.equals("1")||legal=="1"){                                //判断是否开启密码规则
				Pattern p = Pattern.compile("[a-zA-Z0-9]*[a-zA-Z]+[0-9]+[a-zA-Z0-9]*");
				Pattern p1 = Pattern.compile("[a-zA-Z0-9]*[0-9]+[a-zA-Z]+[a-zA-Z0-9]*");
				 Matcher matcher = p.matcher(user.getLoginpwd());
				 Matcher matcher1 = p1.matcher(user.getLoginpwd());
				 boolean result = matcher.matches();
				 boolean result1 = matcher1.matches();
				 if((!result)&&(!result1)){
				this.addFieldError("1", "{*[PasswordLegal]*}");
				return INPUT; 
				 }

				 
			}
			//set_departmentids(null);
			 setDepartments();

			// 保存角色
			set_rolesids(null);
			
			if(user.getSuperior()!=null){
				Collection<UserVO> underList=((UserProcess)process).getUnderList(user.getId(), Integer.MAX_VALUE);
				if(underList.contains(user.getSuperior())){
					throw new OBPMValidateException("用户上级设置会引起用户层级错乱，请重新设置！");
				}
			}
			
			if (process.doView(user.getId()) == null) {
				// 当角色为空时保存系统默认角色
				if(user.getUserRoleSets().size() == 0){
					set_DefaultRoles();
				}
				process.doCreate(user);
			} else {
				try{
				user = this.setPasswordArray(user);
				}catch(Exception e){
					if(e.getMessage().equals("e1")){
					return INPUT;
					}
				}
				process.doUpdate(user);
			}
			
			//角色添加或者清空
			if(user.getUserRoleSets()!=null && user.getUserRoleSets().size() == 0 ){
				user.setRoles(null);
			}
			
			setContent(user);
			
			this.addActionMessage("{*[Save_Success]*}");
			
			// 关联或创建邮件用户
			//HttpServletRequest request = ServletActionContext.getRequest();
			//EmailUserHelper.checkAndCreateEmailUser(user, request);
			
			//级联创建KM个人网盘
			PropertyUtil.reload("km");
			if(PropertyUtil.get(KmConfig.ENABLE).equals("true")){
				NDiskProcess ndiskProcess = new NDiskProcessBean();
				ndiskProcess.createByUser(user.getId(), user.getDomainid());
				
				//保存KM角色
				NUserRoleSetProcess nUserRoleSetProcess = new NUserRoleSetProcessBean();
				nUserRoleSetProcess.doUpdateUserRoleSet(user.getId(), getParams().getParameterAsArray("_kmRoleSelectItem"));
			}
			
			
			/**
			 * 增加了xmpp的消息发送,此消息将发送到obpm-spark的各个客户端
			 */
			sendNotification();
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	

	public String doList() {
		try {
			this.validateQueryParams();
			ParamsTable params = getParams();
			if (getDomain() != null && domain.trim().length() > 0) {
				params.setParameter("t_domainid", domain);
				params.setParameter("_orderby", "orderByNo;id");
				params.setParameter("t_roleid", params
						.getParameterAsString("sm_userRoleSets.roleId"));
				WebUser user = getUser();
				this.setDatas(process.doQuery(params, user));
			}

			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

	// department info page click button to show user UnjoinedDeptlist and add
	// user---- dolly 2011-1-9
	public String doUserListUnjoinedDept() {
		ParamsTable params = this.getParams();
		int lines = 10;
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		for(Cookie cookie : cookies){
			if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
				lines = Integer.parseInt(cookie.getValue());
			}
		    cookie.getName();
		    cookie.getValue();
		}
		params.removeParameter("_pagelines");
		params.setParameter("_pagelines", lines);
		try {
			String deptid = this.getParams().getParameterAsString("deptid");
			if (!deptid.equals("")) {
				UserProcess userProcess = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				setDatas(userProcess.queryOutOfDepartment(this.getParams(),
						deptid));
			}
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doUserListUnjoinedRole() {
		try {
			ParamsTable params = getParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			params.setParameter("sm_dimission", 1);
			String roleid = this.getParams().getParameterAsString("roleid");
			if (!roleid.equals("")) {
				UserProcess userProcess = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				setDatas(userProcess.queryOutOfRole(this.getParams(), roleid));
			}
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doAddUserToDept() {
		try {
			String[] selects = get_selects();
			String deptid = this.getParams().getParameterAsString("deptid");
			if (selects != null && selects.length > 0) {
				UserProcess userProcess = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				userProcess.addUserToDept(selects, deptid);
				this.addActionMessage("{*[Add]*}{*[Success]*}");
				return SUCCESS;
			} else {
				throw new OBPMValidateException("{*[core.domain.notChoose]*}");
			}

		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doAddUserToRole() {
		try {
			String[] selects = get_selects();
			String roleid = this.getParams().getParameterAsString("roleid");
			if (selects != null && selects.length > 0) {
				UserProcess userProcess = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				userProcess.addUserToRole(selects, roleid);
				this.addActionMessage("{*[Add]*}{*[Success]*}");
				return SUCCESS;
			} else {
				throw new OBPMValidateException("{*[core.domain.notChoose]*}");
			}

		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String listUser() throws Exception {
		ParamsTable params = getParams();
		String errorMsg = (String) this.getParams().getHttpRequest().getAttribute("errorMsg");
		int lines = 10;
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		for(Cookie cookie : cookies){
			if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
				lines = Integer.parseInt(cookie.getValue());
			}
		    cookie.getName();
		    cookie.getValue();
		}
		params.removeParameter("_pagelines");
		params.setParameter("_pagelines", lines);
		if(!StringUtil.isBlank(errorMsg)){
			this.addFieldError("1", errorMsg);
		}
		return super.doList();
	}

	public String doUserListByRole() {
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String rolesid = params.getParameterAsString("rolesid");
			String applicationid = params.getParameterAsString("applicationid");
			params.setParameter("sm_userRoleSets.roleId", rolesid);
			if (applicationid != null && applicationid.trim().length() > 0) {
				if (rolesid != null && !"".equals(rolesid)) {
					Collection<UserVO> users = this.process.doQuery(params,
							getUser()).getDatas();
					for (Iterator<UserVO> iter = users.iterator(); iter
							.hasNext();) {
						UserVO tempUser = iter.next();
						html.append("<div class='list_div_user' title='"
								+ tempUser.getName() + "'>");
						html
								.append("<input class='list_div_click' type='checkbox' name='"
										+ tempUser.getName()
										+ "' id='"
										+ tempUser.getId()
										+ "' onclick='selectUser(jQuery(this),true)'>");
						html.append(tempUser.getName());
						html.append("</div>");
					}

				}
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 返回角色集合
	 * 
	 * @return 角色集合
	 * @throws Exception
	 */
	public Collection<UserRoleSet> get_Roles() throws Exception {
		Map<?, ?> m = getContext().getParameters();
		Object obj = m.get("roleids");
		String[] rolesid = null;
		Collection<UserRoleSet> col = new HashSet<UserRoleSet>();
		RoleProcess rp = (RoleProcess) ProcessFactory
				.createProcess(RoleProcess.class);
		UserVO user = (UserVO) getContent();
		if (obj != null && obj instanceof String[]
				&& ((String[]) obj).length > 0) {
			rolesid = (String[]) obj;

			for (int i = 0; i < rolesid.length; i++) {
				RoleVO rv = (RoleVO) rp.doView(rolesid[i]);
				UserRoleSet userRoleSet = new UserRoleSet(user.getId(), rv
						.getId());

				col.add(userRoleSet);
			}
		}
		return col;
	}

	/**
	 * 返回用户密码
	 * 
	 * @return
	 */
	public String get_password() {
		UserVO user = (UserVO) getContent();
		if (user != null && user.getLoginpwd() != null)
			return Web.DEFAULT_SHOWPASSWORD;
		return null;
	}

	/**
	 * 设置用户密码
	 * 
	 * @param _password
	 */
	public void set_password(String _password) {
		UserVO user = (UserVO) getContent();
		user.setLoginpwd(_password);
	}

	/**
	 * 返回用户状态
	 * 
	 * @return "true"为可用，"false"为不可用
	 * @throws Exception
	 */
	public String get_strstatus() throws Exception {
		UserVO user = (UserVO) getContent();
		if (user.getStatus() == 1) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 设置 用户状态
	 * 
	 * @param strname
	 *            用户状态字符串true or false
	 * @throws Exception
	 */
	public void set_strstatus(String strname) throws Exception {
		UserVO user = (UserVO) getContent();
		if (strname != null) {
			if (strname.equalsIgnoreCase("true")) {
				user.setStatus(1);
			} else {
				user.setStatus(0);
			}
		}
	}

	/**
	 * 删除部门
	 * 
	 * @return 成功处理返回"SUCCESS",否则提示不能删除
	 * @throws Exception
	 */
	public String removeDepartment() throws Exception {
		String departmentid = getParams().getParameterAsString(
				"sm_userDepartmentSets.departmentId");
		StringBuffer errorMsg = new StringBuffer();
		if (_selects != null && departmentid != null
				&& departmentid.trim().length() > 0) {
			DepartmentProcess da = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			DepartmentVO dep = (DepartmentVO) da.doView(departmentid);

			for (int i = 0; i < _selects.length; i++) {
				String id = _selects[i];
				UserVO user = (UserVO) process.doView(id);
				if(departmentid.equals(user.getDefaultDepartment())){
					errorMsg.append(user.getName()).append(",");
				}else {
					Collection<UserDepartmentSet> userDepartmentSets = user
							.getUserDepartmentSets();
					Collection<UserDepartmentSet> newSets = new HashSet<UserDepartmentSet>();
	
					// 删除UserDepartmentSet
					for (Iterator<UserDepartmentSet> iterator = userDepartmentSets
							.iterator(); iterator.hasNext();) {
						UserDepartmentSet set = (UserDepartmentSet) iterator.next();
						if (!dep.getId().equals(set.getDepartmentId())) {
							newSets.add(set);
						}
					}
	
					user.setUserDepartmentSets(newSets);
					process.doUpdate(user);
				}
			}
		}
		if(errorMsg.length() > 0){
			errorMsg.setLength(errorMsg.length()-1);
			this.getParams().getHttpRequest().setAttribute("errorMsg", errorMsg + "的默认部门为该部门,不能移除");
		}
		return SUCCESS;
	}

	/**
	 * 删除角色
	 * 
	 * @return 成功处理返回"SUCCESS",否则提示失败
	 * @throws Exception
	 */
	public String removeRole() throws Exception {
		String roleid = getParams().getParameterAsString(
				"sm_userRoleSets.roleId");
		if (_selects != null && roleid != null && roleid.trim().length() > 0) {
			for (int i = 0; i < _selects.length; i++) {
				String id = _selects[i];
				UserVO user = (UserVO) process.doView(id);
				Collection<UserRoleSet> oldroles = user.getUserRoleSets();
				Collection<UserRoleSet> roleSets = new HashSet<UserRoleSet>();

				for (Iterator<UserRoleSet> it = oldroles.iterator(); oldroles != null
						&& it.hasNext();) {
					UserRoleSet set = (UserRoleSet) it.next();
					if (!roleid.equals(set.getRoleId()))
						roleSets.add(set);
				}
				user.setUserRoleSets(roleSets);
				process.doUpdate(user);
			}
		}
		return SUCCESS;
	}

	/**
	 * 修改用户信息
	 * 
	 * @return 成功处理返回"SUCCESS",否则提示失败
	 */
	public String doEdit() {
		try {

			String id = getParams().getParameterAsString("editPersonalId");
			if (id != null && !id.equals("")) {
				UserProcess process =  (UserProcess)ProcessFactory.createProcess(
						UserProcess.class);
				UserSetupProcess uSprocess = (UserSetupProcess) ProcessFactory
						.createProcess(UserSetupProcess.class);
				UserSetupVO userSetup = uSprocess.getUserSetupByUserId(id);
				UserVO user = (UserVO) process.doView(id);
				user.setUserSetup(userSetup);
				setContent(user);
				

				//获取EmailUser
				EmailUser emailUser1 = new EmailUser();
				ParamsTable params1 = new ParamsTable();
				params1.setParameter("t_name", user.getLoginno());
				EmailUserProcess euserpro = (EmailUserProcess) ProcessFactory.createProcess(EmailUserProcess.class);
				DataPackage<EmailUser> dataEmailUser = euserpro.doQuery(params1);
				if(dataEmailUser.rowCount >= 1){
					for(Iterator<EmailUser> ite1 = dataEmailUser.datas.iterator();ite1.hasNext();){
						 emailUser1 = ite1.next();
						if(emailUser1 != null){
							break;
						}
					}
				}
				setEmailUser(emailUser1);
				
				//获取当前用户的所有角色
				Collection<RoleVO> userRoles = user.getRoles();
				RoleVO roleVO = new RoleVO();
				
				UserDefinedProcess udprocss=(UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
				String applicationid = application;
				params = new ParamsTable();
				params.setParameter("t_applicationid", applicationid);
				params.setParameter("t_userId", id);
				params.setParameter("_orderby", "id");
				DataPackage<UserDefined> dataPackage=udprocss.doQuery(params);
				if(dataPackage.rowCount > 0){
					for(Iterator<UserDefined> ite1 = dataPackage.datas.iterator();ite1.hasNext();){
						userDefined = (UserDefined)ite1.next();
					}
				}else{
					params = new ParamsTable();
					params.setParameter("t_applicationid", applicationid);
					params.setParameter("n_published", true);
					params.setParameter("_orderby", "id");
					DataPackage<UserDefined> dataPackage1=udprocss.doQuery(params);
					if(dataPackage1.rowCount>0){
						//遍历相同软件下的不同首页
						for(Iterator<UserDefined> ite1 = dataPackage1.datas.iterator();ite1.hasNext();){
							userDefined = (UserDefined)ite1.next();
							//获取首页的角色
							String roleIds = userDefined.getRoleIds();
							if(!StringUtil.isBlank(roleIds)){
								String[] userRoleIds = roleIds.split(",");
								for(int i=0;i<userRoleIds.length;i++){
									if(userRoles.size()>0){
										for(Iterator<RoleVO> ite2 = userRoles.iterator();ite2.hasNext();){
											roleVO = (RoleVO)ite2.next();
											if(userRoleIds[i].equals(roleVO.getId())){
												// 前台用户设置信息回显
												setUserDefined(userDefined);
												return SUCCESS;
											}
										}
									}
								}
							}
							
						}
					}					
				}
			} else {
				// 根据ID获取当前用户对象,并返回跳转页面字符串
				String returnString = super.doEdit();
				return returnString;
			}
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * 保存个人信息
	 * 
	 * @return 成功处理返回"SUCCESS",否则提示失败
	 * @throws Exception
	 */
	public String doSavePersonal() throws Exception {
		try {
			UserVO user = (UserVO) getContent();
			((UserProcess) process).doPersonalUpdate(user);
			setContent(user);
			this.addActionMessage("{*[Save_Success]*}");
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 删除用户信息
	 * 
	 * @return 成功处理返回"SUCCESS",否则返回"ERROR"
	 */
	public String doDelete() {
		try {
			// 删除网盘信息
			NetDiskProcess netDiskProcess = (NetDiskProcess) ProcessFactory
					.createProcess(NetDiskProcess.class);
			netDiskProcess.doRemove(_selects);
			
			//级联删除KM个人网盘
			PropertyUtil.reload("km");
			if(PropertyUtil.get(KmConfig.ENABLE).equals("true")){
				NDiskProcess ndiskProcess = new NDiskProcessBean();
				for (int i = 0; i < _selects.length; i++) {
					if(StringUtil.isBlank(_selects[i])) continue;
					ndiskProcess.removeByUser(_selects[i]);
				}
			}
			
			// 删除邮件用户
			HttpServletRequest request = ServletActionContext.getRequest();
			EmailUserHelper.removeEmailUsers(_selects, request);

			//删除用户的操作日志
			LogProcess logProcess = (LogProcess) ProcessFactory.createProcess(LogProcess.class);
			for(int k=0;k<_selects.length; k++){
				logProcess.deleteLogsByUser(_selects[k]);
			}
			
			String rtn = super.doDelete();
			if(SUCCESS.equals(rtn)){
				/**
				 * 增加了xmpp的消息发送,此消息将发送到obpm-spark的各个客户端
				 */
				sendNotification();
			}
			return rtn;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			doList();
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			doList();
			return INPUT;
		}
	}

	/**
	 * 获取全部角色
	 * 
	 * @return 角色集合
	 * @throws Exception
	 */
	public Collection<RoleVO> get_allRoles() throws Exception {
		Collection<RoleVO> rtn = new ArrayList<RoleVO>();
		RoleProcess rp = (RoleProcess) ProcessFactory
				.createProcess(RoleProcess.class);
		Collection<RoleVO> roles = rp.doSimpleQuery(null, getApplication());
		if (roles != null) {
			rtn = roles;
		}
		return rtn;
	}

	/**
	 * 获取已加入软件的角色
	 * 
	 * @return 软件角色集合
	 * @throws Exception
	 */
	public Map<String, String> get_softRoles() throws Exception {
		// Collection rtn = new ArrayList();
		Map<String, String> map = new LinkedHashMap<String, String>();
		ParamsTable param = this.getParams();
		String domainid = param.getParameterAsString("domain");
		DomainProcess domainProcee = (DomainProcess) ProcessFactory
				.createProcess(DomainProcess.class);
		DomainVO domain = (DomainVO) domainProcee.doView(domainid);
		if (domain != null) {
			Collection<ApplicationVO> applications = domain.getApplications();
			if (applications != null) {
				Iterator<ApplicationVO> iter = applications.iterator();
				while (iter.hasNext()) {
					ApplicationVO application = (ApplicationVO) iter.next();
					String applicationid = application.getId();
					RoleProcess rp = (RoleProcess) ProcessFactory
							.createProcess(RoleProcess.class);
					Collection<RoleVO> roles = rp
							.getRolesByApplication(applicationid);
					// map.put(applicationid, roles);
					Iterator<RoleVO> iter_roles = roles.iterator();
					while (iter_roles.hasNext()) {
						RoleVO role = iter_roles.next();
						// rtn.add(application.getName() + "-" +
						// role.getName());
						map.put(role.getId(), application.getName() + "-"
								+ role.getName());
					}
				}
			}
		}
		return map;
	}

	/**
	 * 根据域获取域所属软件的角色
	 * 
	 * @return 软件角色集合
	 * @throws Exception
	 */
	public Collection<RoleVO> get_domainOfRoles() throws Exception {
		Collection<RoleVO> rtn = new ArrayList<RoleVO>();
		ParamsTable param = this.getParams();
		String domainid = getDomain();
		if (StringUtil.isBlank(domainid))
			domainid = param.getParameterAsString("domain");
		else if (StringUtil.isBlank(domainid))
			domainid = param.getParameterAsString("t_domainid");
		DomainProcess domainProcee = (DomainProcess) ProcessFactory
				.createProcess(DomainProcess.class);
		DomainVO domain = (DomainVO) domainProcee.doView(domainid);
		if (domain != null) {
			Collection<ApplicationVO> applications = domain.getApplications();
			if (applications != null) {
				Iterator<ApplicationVO> iter = applications.iterator();
				while (iter.hasNext()) {
					ApplicationVO application = (ApplicationVO) iter.next();
					String applicationid = application.getId();
					RoleProcess rp = (RoleProcess) ProcessFactory
							.createProcess(RoleProcess.class);
					Collection<RoleVO> roles = rp
							.getRolesByApplication(applicationid);
					rtn.addAll(roles);
				}
			}
		}
		return rtn;
	}

	public String getDomain() {
		if (domain != null && domain.trim().length() > 0) {
			return domain;
		} else {
			return (String) getContext().getSession().get(
					Web.SESSION_ATTRIBUTE_DOMAIN);
		}
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSuperiorid() {
		UserVO content = (UserVO) this.getContent();
		if (content.getSuperior() != null) {
			superiorid = content.getSuperior().getId();
		}

		return superiorid;
	}

	public void setSuperiorid(String superiorid) throws Exception {
		this.superiorid = superiorid;

		if (!StringUtil.isBlank(superiorid)) {
			UserVO content = (UserVO) this.getContent();
			UserVO superior = (UserVO) process.doView(superiorid);
			if (superior != null) {
				content.setSuperior(superior);
				content.setLevel(superior.getLevel() + 1);
			}
		}
	}
	
	/**
	 * 获取用户等级集合
	 * 
	 * @return 用户等级集合
	 * @throws Exception
	 */
	public Collection<BaseUser> getReportList(String contentId, String domain) throws Exception {

		UserProcess up = (UserProcess) process;

		ParamsTable params = new ParamsTable();
		params.setParameter("domain", domain);
		params.setParameter("sm_dimission", 1);

		Collection<?> userList = up.doSimpleQuery(params);
		Collection<BaseUser> userList2 = ((UserProcess) process).deepSearchTree2(userList, null,
				contentId, 0);
		return userList2;
	}

	public String doSelectUser() throws Exception {
		return SUCCESS;
	}

	public String linkmen() throws Exception {
		UserProcess up = (UserProcess) process;
		up.listLinkmen(getParams());
		return SUCCESS;
	}

	/**
	 * 保存并新建用户信息
	 * 
	 * @return "SUCCESS"表示成功处理,否则返回错误提示
	 * 
	 */
	public String doSaveAndNew() {
		try {
			UserVO user = (UserVO) getContent();
			 set_rolesids(null);
			//set_departmentids(null);
			 setDepartments();

			if (process.doView(user.getId()) == null) {
				
				// 当角色为空时保存系统默认角色
				if( user.getUserRoleSets().size() == 0){
					set_DefaultRoles();
				}
				
				process.doCreate(user);
			} else {
				process.doUpdate(user);
			}
			
			this.setContent(new UserVO());
			this.getContent().setId(Sequence.getSequence());

			this.addActionMessage("{*[Save_Success]*}");
			
			// 关联或创建邮件用户
			HttpServletRequest request = ServletActionContext.getRequest();
			EmailUserHelper.checkAndCreateEmailUser(user, request);
			/**
			 * 增加了xmpp的消息发送,此消息将发送到obpm-spark的各个客户端
			 */
			sendNotification();
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 获取一个企业域下的所有应用
	 * 
	 * @author Bluce
	 * @return 应用集合
	 * @throws Exception
	 * @date 2010-05-10
	 */
	public Collection<ApplicationVO> get_applicationlist() throws Exception {
		DomainVO vo = DomainHelper.getDomainVO(domain);
		_applicationlist = new ArrayList<ApplicationVO>();
		_applicationlist.addAll(vo.getApplications());
		return _applicationlist;
	}

	/**
	 * 设置角色
	 * 
	 * @author Bluce
	 * @param _roleList
	 */
	public void set_roleList(Collection<RoleVO> _roleList) {
		this._roleList = _roleList;
	}

	public Collection<RoleVO> getRoleList() {
		return this._roleList;
	}
	
	
	public String doTreeList() {
		try {
			//this.validateQueryParams();
			ParamsTable params = getParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			if(getParams().getParameterAsArray("sm_userRoleSets.roleId").length>0){
				params.setParameter("sm_userRoleSets.roleId", getParams().getParameterAsArray("sm_userRoleSets.roleId")[0]);
			}
			String domain = params.getParameterAsString("domain");
			String departid = params.getParameterAsString("departid");

			// DepartmentProcess departmentProcess = (DepartmentProcess)
			// ProcessFactory.createProcess(DepartmentProcess.class);
			if(StringUtil.isBlank( params.getParameterAsString("_orderby"))){
			   params.setParameter("_orderby", "orderByNo;id");
			}
			if (domain != null && domain.trim().length() > 0) {
				if (departid == null || "".equals(departid)) {
					params.setParameter("t_domainid", domain);
					WebUser user = getUser();
					setDatas(process.doQuery(params, user));
				} else {
					params.setParameter("t_domainid", domain);
					params.setParameter("sm_userDepartmentSets.departmentId",
							departid);
					WebUser user = getUser();
					setDatas(process.doQuery(params, user));
				}

			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

	/**
	 * 获取前台选择的用户角色ids,保存进数据库
	 * 
	 * @author Bluce
	 * @param _roleids
	 * @throws Exception
	 * @date 2010-05-10
	 */
	//@SuppressWarnings("unchecked")
	public void set_rolesids(Collection<String> _roleids) throws Exception {
		Map<?, ?> m = getContext().getParameters();
		Object obj = m.get("_roleSelectItem");// 选中的角色
		String tmp[] = null;
		if (obj instanceof String[] && ((String[]) obj).length > 0) {
			tmp = (String[]) obj;
		}
		UserVO user = (UserVO) getContent();
		//选中对应角色时，更新中间表数据
		Collection<RoleVO> roles = new ArrayList<RoleVO>();
		if (tmp != null) {
			user.getRoles().clear();
			for (int i = 0; i < tmp.length; i++) {
				RoleProcess rp = (RoleProcess) ProcessFactory
						.createProcess(RoleProcess.class);
				RoleVO role = (RoleVO) rp.doView(tmp[i]);
				// 去掉勾选应用时的干扰
				if (role != null) {
					roles.add(role);
					UserRoleSet set = new UserRoleSet(user.getId(), role.getId());
					user.getUserRoleSets().add(set);
				}
			}
		}
		user.setRoles(roles);
	}
	
	public void set_DefaultRoles() throws Exception {
		UserVO user = (UserVO) getContent();
		RoleProcess rp = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
		//获取软件
		Collection<ApplicationVO> apps = get_applicationlist();
		
		for(Iterator<ApplicationVO> app_its = apps.iterator(); app_its.hasNext();){
			ApplicationVO app = app_its.next();
			if(app.isActivated()){
				//获取软件下系统默认角色
				Collection<RoleVO> roles = rp.getDefaultRolesByApplication(app.getApplicationid());
				if(roles != null && roles.size() > 0 ){
					for(Iterator<RoleVO> it = roles.iterator(); it.hasNext();){
						RoleVO role = it.next();
						UserRoleSet set = new UserRoleSet(user.getId(), role.getId());
						user.getUserRoleSets().add(set);
					}
				}
			}
		}
	}

	public UserDefined getUserDefined() {
		return userDefined;
	}

	public void setUserDefined(UserDefined userDefined) {
		this.userDefined = userDefined;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void set_applicationlist(Collection<ApplicationVO> _applicationlist) {
		this._applicationlist = _applicationlist;
	}

	public String getStartProxyTime() {
		UserVO user = (UserVO) getContent();
		try {
			if (user.getStartProxyTime() != null && !user.getStartProxyTime().equals("")) {
				this.startProxyTime = DateUtil.format(user.getStartProxyTime(), "yyyy-MM-dd HH:mm:ss");
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return startProxyTime;
	}

	public void setStartProxyTime(String startProxyTime) {
		if(startProxyTime!=null && !startProxyTime.equals("")){
			try {
				UserVO user = (UserVO) getContent();
				Date date = DateUtil.parseDate(startProxyTime,
						"yyyy-MM-dd HH:mm:ss");
				user.setStartProxyTime(date);
			} catch (ParseException e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
			this.startProxyTime = startProxyTime;
		}
	}

	public String getEndProxyTime() {
		UserVO user = (UserVO) getContent();
		try {
			if (user.getEndProxyTime() != null && !user.getEndProxyTime().equals("")) {
				this.endProxyTime = DateUtil.format(user.getEndProxyTime(), "yyyy-MM-dd HH:mm:ss");
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return endProxyTime;
	}

	public void setEndProxyTime(String endProxyTime) {
		if(endProxyTime!=null && !endProxyTime.equals("")){
			try {
				UserVO user = (UserVO) getContent();
				Date date = DateUtil.parseDate(endProxyTime,
						"yyyy-MM-dd HH:mm:ss");
				user.setEndProxyTime(date);
			} catch (ParseException e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
			this.endProxyTime = endProxyTime;
		}
	}

	public EmailUser getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}
	
	/**
	 * xmpp消息发送,部门的增删改将触发此动作
	 * 
	 * 通知所有的obpm-spark客户端更新企业联系人列表
	 * 
	 * @author keezzm
	 * @date 2011-08-17
	 * @last modified by keezzm
	 */
	private void sendNotification() {
		try {
			// 发送XMPP信息
			ContactNotification notification = ContactNotification
					.newInstance(ContactNotification.ACTION_UPDATE);
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			/**
			 * 默认发送者为admin
			 */
			notification.setSender(superUserProcess.getDefaultAdmin());
			/**
			 * 添加接收者,为所有在线用户
			 */
			DataPackage<WebUser> dataPackage = OnlineUsers
					.doQuery(new ParamsTable());
			Collection<WebUser> users = dataPackage.getDatas();
			for (Iterator<WebUser> iterator = users.iterator(); iterator
					.hasNext();) {
				WebUser webUser = iterator.next();
				notification.addReceiver(webUser);
			}
			XMPPSender.getInstance().processNotification(notification);
		}catch (OBPMValidateException e) {
			LOG.warn("XMPP Notification Error", e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			LOG.warn("XMPP Notification Error", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}
	
	
	
	public UserVO setPasswordArray(UserVO user) throws Exception{
		
		PropertyUtil.reload("passwordLegal");
		String passwordArrayLengthString = PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_UPADTE_TIMES);
		int passwordArrayLength = Integer.parseInt(passwordArrayLengthString);
	
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO po = (UserVO)userProcess.doView(user.getId());
		String oldPasswordArray = "";	
		oldPasswordArray = po.getPasswordArray();
		
		String pwda[] = null;
		int oldpasswordArrayLength=0;
		if(oldPasswordArray!=null){
			pwda = oldPasswordArray.split(",");
			oldpasswordArrayLength = oldPasswordArray.split(",").length;
		}
	
		
		String passwordArray = null;
		passwordArray = user.getPasswordArray();
		String loginpwd = user.getLoginpwd();
		
		if(!loginpwd.trim().equals(decrypt(po.getLoginpwd()))&&!loginpwd.trim().equals(Web.DEFAULT_SHOWPASSWORD)){
			if(oldPasswordArray!=null){
				for(int i = 0 ;i<oldpasswordArrayLength;i++){
					if(loginpwd.equals(decrypt(pwda[i]))){
						this.addFieldError("1", "{*[ModifyPasswordNotSame]*}"+passwordArrayLengthString);
						throw new Exception("e1");
					}
				}
				passwordArray = oldPasswordArray+","+encrypt(loginpwd);
			}else{
				passwordArray = po.getLoginpwd()+","+encrypt(user.getLoginpwd());
			}
			user.setLastModifyPasswordTime(new Date());
		    
		}else{
			passwordArray=po.getPasswordArray();
			user.setLastModifyPasswordTime(po.getLastModifyPasswordTime());
		}
		if(oldpasswordArrayLength+1>passwordArrayLength){
			int i1=passwordArray.split(",").length;
			String passwordArraytmp="";
			for(int i =i1-passwordArrayLength-1;i<i1;i++){
				passwordArraytmp += ","+passwordArray.split(",")[i];
			}		
			passwordArray = passwordArraytmp.substring(1);
		}
		user.setPasswordArray(passwordArray);
		return user;
		
	} 
	/**
	 * 新的密码加密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private String encrypt(final String s) throws Exception {
		return Security.encryptPassword(s);
	}
	
	/**
	 * 新的密码解密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private String decrypt(final String s){
		return Security.decryptPassword(s);
	}
	
	/**
	 * 获取所有在线用户(后台系统监控)
	 * @return
	 */
	public String doOnlineUsersList(){
		try {
			ParamsTable params = getParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			Map<?, ?> session = ActionContext.getContext().getSession();
			WebUser user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_USER);
			if(user.isSuperAdmin()){
				this.setDpg(OnlineUsers.doQuery(getParams()));
			}else if(user.isDomainAdmin()){
				this.setDpg(OnlineUsers.doQueryForDomainAdmin(getParams()));
			}
			return SUCCESS;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 根据部门获取用户列表
	 * 
	 * @return
	 */
	public String doUserListByDept() {
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String departid = params.getParameterAsString("departid");
			String domain = getParams().getParameterAsString("domain");
			String contentId = params.getParameterAsString("contentId");

			int total = 0;

			Collection<BaseUser> users = getReportList(contentId,domain);
			
			for (Iterator<BaseUser> iter = users.iterator(); iter.hasNext();) {
				UserVO tempUser = (UserVO) iter.next();
				if(!tempUser.getDefaultDepartment().equals(departid)){
					boolean flag = false;
					Collection<DepartmentVO> deps = tempUser.getDepartments();
					for (Iterator<DepartmentVO> iterator = deps.iterator(); iterator
							.hasNext();) {
						DepartmentVO departmentVO = (DepartmentVO) iterator.next();
						if(departmentVO.getId().equals(departid)){
							flag = true;
							break;
						}
					}
					if(!flag){
						continue;
					}
				}
				html.append("<div class='list_div_user' title='"
						+ tempUser.getName() + "'>");
				html.append("<input class='list_div_click' type='radio' name='"
							+ tempUser.getName()
							+ "' id='"
							+ tempUser.getId()
							+ "' email='"
							+ tempUser.getEmail()
							+ "' onclick='selectUser(jQuery(this),true)'>");
				html.append("<span onclick='" +
						"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+"</span>");
				html.append("</div>");
				total++;
			}

			if (total > 0) {
				getPageDiv(html,total);
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取所有在线用户列表,用于选择代理人
	 * 
	 * @return
	 */
	// @SuppressWarnings("unchecked")
	public String doOnLineUserForSuperior() {
		StringBuffer html = new StringBuffer();
		try {
			String domain = getParams().getParameterAsString("domain");
			String contentId = getParams().getParameterAsString("contentId");

			DataPackage<WebUser> datas = OnlineUsers.doQueryByDomain(new ParamsTable(), domain);
			Collection<BaseUser> users = getReportList(contentId, domain);
			
			Collection<BaseUser> users2 = new ArrayList<BaseUser>();
			for (Iterator<BaseUser> iter = users.iterator(); iter.hasNext();) {
				if(users2.size() == datas.datas.size()){
					break;
				}
				BaseUser tempUser = (BaseUser) iter.next();
				for (Iterator<WebUser> iter2 = datas.datas.iterator(); iter2.hasNext();) {
					BaseUser webUser = (BaseUser) iter2.next();
					if(tempUser.getId().equals(webUser.getId())){
						users2.add(webUser);
					}
				}
			}
			
			html.append("<input type='hidden' value='" + users2.size()
					+ "' id='onLineUsersCount' name='onLineUsersCount'>");
			for (Iterator<BaseUser> iter = users2.iterator(); iter.hasNext();) {
				BaseUser tempUser = (BaseUser) iter.next();
				html.append("<div class='list_div_online' title='" + tempUser.getName() + "'>");
				html.append("<input class='list_div_click' type='radio' name='" + tempUser.getName() + "' id='"
							+ tempUser.getId() + "' email='"
							+ tempUser.getEmail() + "' onclick='selectUser(jQuery(this),true)'>");
				html.append("<span onclick='" +
						"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);' style='cursor:pointer'  >"+tempUser.getName()+"</span>");
				html.append("</div>");
			}
			if (users2.size() > 0) {
				getLeftPageDiv(html,users2.size());
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void getLeftPageDiv(StringBuffer html,int total) {
		int pageCount = (int) Math.ceil((double) total / 10.0);
		
		html.append("<div style='padding:5px;border-bottom:1px solid gray;'>");
		
		html.append("<a id='firstLeftPage' style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav("
					+ "1)'>{*[FirstPage]*}</a>&nbsp;");
		
		html.append("<a id='prevLeftPage' style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav("
					+ "1)'>{*[PrevPage]*}</a>&nbsp;");
		html.append("<a id='nextLeftPage' style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav("
					+ "2)'>{*[NextPage]*}</a>&nbsp;");
		
		html.append("<a id='endLeftPage' style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav("
					+ pageCount +")'>{*[EndPage]*}</a>&nbsp;");
		html.append("{*[InPage]*}<span id='inLeftPage'>1</span>{*[Page]*}/{*[Total]*}<span id='countLeftPage'>").append(pageCount).append(
				"</span>{*[Pages]*}&nbsp;");
		
		html.append("</div>");
	}
	
	/**
	 * 根据企业域获取所有用户列表
	 * 
	 * @return
	 */
	public String doAllUser() {
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String sm_name = params.getParameterAsString("sm_name");
			String contentId = params.getParameterAsString("contentId");
			
			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;

			int total = 0;

			if (getDomain() != null && domain.trim().length() > 0) {
				
				DataPackage<UserVO> users = ((UserProcess)process).queryForUserDialog(params, getUser(), ServletActionContext
						.getRequest(), contentId);
				total = users.rowCount;
				
				for (Iterator<UserVO> iter = users.datas.iterator(); iter.hasNext();) {
					UserVO tempUser = (UserVO) iter.next();
					html.append("<div class='list_div_user' title='"
							+ tempUser.getName() + "'>");
					html.append("<input class='list_div_click' type='radio' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ tempUser.getEmail()
									+ "' onclick='selectUser(jQuery(this),true)'>");
					html.append("<span onclick='" +
							"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+"</span>");
					html.append("</div>");
				}
			}
			if (total > 0) {
				String url = "/core/user/getAllUser.action?domain=" + domain
					+ "&sm_name=" + sm_name + "&contentId=" + contentId;
				getPageDiv(html, currentPage, total, url);
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String biuldUserIdStr() throws Exception {
		StringBuffer w = new StringBuffer();
		ParamsTable params = getParams();
		String getToPerson = params.getParameterAsString("_isGetToPerson");//指定审批人
		String getApprover2SubFlow = params.getParameterAsString("_isGetApprover2SubFlow");//子流程节点选择审批人	
		String getCirculator = params.getParameterAsString("_isGetCirculator");//抄送人
		if ((!StringUtil.isBlank(getToPerson) && "true".equals(getToPerson))
				||(!StringUtil.isBlank(getApprover2SubFlow) && "true".equals(getApprover2SubFlow))) {
			String _flowId = params.getParameterAsString("_flowId");
			String _docId = params.getParameterAsString("_docId");
			String _nodeId = params.getParameterAsString("_nodeId");
			Collection<BaseUser> users = StateMachineHelper.getPrincipalList(
					_docId, getUser(), _nodeId, ServletActionContext
							.getRequest(), _flowId);
			if (users != null && !users.isEmpty()) {
				w.append(" AND id in(");
				for (Iterator<BaseUser> iterator = users.iterator(); iterator
						.hasNext();) {
					BaseUser user = (BaseUser) iterator.next();
					w.append("'").append(user.getId()).append("',");
				}
				w.setLength(w.length() - 1);
				w.append(")");
			}
		}else if(!StringUtil.isBlank(getCirculator)
				&& "true".equals(getCirculator)){
			String _flowId = params.getParameterAsString("_flowId");
			String _docId = params.getParameterAsString("_docId");
			String _nodeId = params.getParameterAsString("_nodeId");
			Collection<BaseUser> users = StateMachineHelper.getCirculatorList(
					_docId, getUser(), _nodeId, ServletActionContext
							.getRequest(), _flowId);
			if (users != null && !users.isEmpty()) {
				w.append(" AND id in(");
				for (Iterator<BaseUser> iterator = users.iterator(); iterator
						.hasNext();) {
					BaseUser user = (BaseUser) iterator.next();
					w.append("'").append(user.getId()).append("',");
				}
				w.setLength(w.length() - 1);
				w.append(")");
			}
		}
		return w.toString();
	}
	
	public void getPageDiv(StringBuffer html, int total) {
		Long lines = this.getParams().getParameterAsLong("_pagelines");
		long pagelines = lines != null ? lines : 10;
		
		int pageCount = (int) Math.ceil((double) total / pagelines);
		
		html.append("<div class='user_page' style='padding:5px;border-bottom:1px solid gray;'>");
		
			html.append("<a id='first_page' style='cursor: pointer;' onclick='doPageNav("
							+ "1)'>{*[FirstPage]*}</a>&nbsp;");
			html.append("<a id='prev_page' style='cursor: pointer;' onclick='doPageNav("
							+ "1)'>{*[PrevPage]*}</a>&nbsp;");

			html.append("<a id='next_page' style='cursor: pointer;' onclick='doPageNav("
							+ "2)'>{*[NextPage]*}</a>&nbsp;");
			html.append("<a id='end_page' style='cursor: pointer;' onclick='doPageNav("
							+ pageCount
							+ ")'>{*[EndPage]*}</a>&nbsp;");
			
		if(pageCount != 1){
			html.append("<a id='all_page' style='cursor: pointer;' onclick='doAllPageNav("
					+ "0)'> {*[All]*}</a>&nbsp;");
			
			html.append("<span id='all_page_hide'>{*[InPage]*}1").append(
			"{*[Page]*}/{*[Total]*}1").append(
			"{*[Pages]*}</span>&nbsp;");
		}
		
		html.append("<span id='all_page_show'>{*[InPage]*}<span id='in_page'>1").append(
				"</span>{*[Page]*}/{*[Total]*}<span id='total_page'>").append(pageCount).append(
				"</span>{*[Pages]*}</span>&nbsp;");

		html.append("</div>");
	}
	
	/**
	 * 根据选中的ID获取用户JSON
	 * 
	 * @return
	 */
	public String doListBySelectToJSON() {
		String[] selects = get_selects();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			List<String> list = new ArrayList<String>();
			if (selects != null && selects.length > 0) {
				for (int i = 0; i < selects.length; i++) {
					UserVO userVO = (UserVO) userProcess.doView(selects[i]);
					if (userVO != null) {
						list.add(userVO.toJSON());
					}
				}
			}

			ResponseUtil.setJsonToResponse(ServletActionContext.getResponse(),
					list.toString());
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return NONE;
	}
	
	public String doList4Contacts(){
		try {
			ParamsTable params = this.getParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			//根据orderByNo排序
			params.setParameter("_orderby", "orderByNo");
			int currPage = params.getParameterAsInteger("_currpage") == null ? 1:params.getParameterAsInteger("_currpage");
			if (!StringUtil.isBlank(getDomain())) {
				if("all".equals(params.getParameterAsString("userGroupId")) || StringUtil.isBlank(params.getParameterAsString("userGroupId"))){
					params.setParameter("t_domainid", domain);
					params.setParameter("_currpage", currPage);
					params.setParameter("t_dimission", 1);
					this.setDatas(process.doQuery(params));
				}else {
					UserGroupSetProcess userGroupProcess = (UserGroupSetProcess) ProcessFactory.createProcess(UserGroupSetProcess.class);
					this.setDatas(userGroupProcess.getUserByGroup(params.getParameterAsString("userGroupId"), params));
				}
				
			}
			ServletActionContext.getRequest().setAttribute("userGroupId", params.getParameterAsString("userGroupId"));
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 根据企业域获取所有用户列表，用于选择代理人、或上级用户
	 * 
	 * @return
	 */
	public String doAllUser4ProxyOrSuperior(){
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String sm_name = params.getParameterAsString("sm_name");
			sm_name = URLDecoder.decode(sm_name);
			String contentId = params.getParameterAsString("contentId");
			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			int total = 0;
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));

			if (getDomain() != null && domain.trim().length() > 0) {
				DataPackage<UserVO> users = ((UserProcess)process).queryForUserDialog(params, getUser(), ServletActionContext
						.getRequest(), contentId);
				total = users.rowCount;
				for (Iterator<UserVO> iter = users.datas.iterator(); iter.hasNext();) {
					UserVO tempUser = (UserVO) iter.next();
					html.append("<div class='list_div' title='"
							+ tempUser.getName() + "'>");
						html
								.append("<input class='list_div_click' type='radio' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ tempUser.getEmail()
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
						html.append("<span onclick='" +
								"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);' style='cursor:pointer'  >"+tempUser.getName()+"</span>");
						html.append("</div>");
				}
			}
			if (total > 0) {
				String url = "/core/user/getAllUser4ProxyOrSuperior.action?domain=" + domain
						+ "&sm_name=" + sm_name + "&contentId=" + contentId;
				getPageDiv(html, currentPage, total, url);
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 根据部门获取用户列表，用于选择代理人、或上级用户
	 * 
	 * @return
	 */
	public String doUserListByDept4ProxyOrSuperior() {
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String departid = params.getParameterAsString("departid");
			String contentId = params.getParameterAsString("contentId");

			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));
			int total = 0;

			DataPackage<UserVO> users = ((UserProcess)process).queryByDeptForUserDialog(params, getUser(), ServletActionContext
					.getRequest(), contentId);
			total = users.rowCount;

			for (Iterator<UserVO> iter = users.datas.iterator(); iter.hasNext();) {
				UserVO tempUser = iter.next();
				html.append("<div class='list_div' title='"
						+ tempUser.getName() + "'>");
					html
							.append("<input class='list_div_click' type='radio' name='"
							+ tempUser.getName()
							+ "' id='"
							+ tempUser.getId()
							+ "' email='"
							+ tempUser.getEmail()
							+ "' telephone='"
							+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append("<span onclick='" +
							"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);' style='cursor:pointer'  >"+tempUser.getName()+"</span>");
					html.append("</div>");
			}

			if (total > 0) {
				String url = "/core/user/treelist4ProxyOrSuperior.action?departid="
						+ departid;
				url += "&domain=" + domain + "&contentId=" + contentId;
				getPageDiv(html, currentPage, total, url);
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取所有在线用户列表,用于选择代理人、或上级用户
	 * 
	 * @return
	 */
	public String doOnLineUser4ProxyOrSuperior() {
		StringBuffer html = new StringBuffer();
		try {
			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));
			String domain = getParams().getParameterAsString("domain");
			String contentId = params.getParameterAsString("contentId");

			DataPackage<WebUser> datas = OnlineUsers.doQueryByDomain(new ParamsTable(), domain);
			
			Collection<WebUser> users2 = new ArrayList<WebUser>();
			for (Iterator<WebUser> iter2 = datas.datas.iterator(); iter2.hasNext();) {
				WebUser webUser = (WebUser) iter2.next();
				if(!webUser.getId().endsWith(contentId)){
					users2.add(webUser);
				}
			}
			
			html.append("<input type='hidden' value='" + users2.size()
					+ "' id='onLineUsersCount' name='onLineUsersCount'>");
			for (Iterator<WebUser> iter = users2.iterator(); iter.hasNext();) {
				WebUser tempUser = (WebUser) iter.next();
				html.append("<div class='list_div' title='" + tempUser.getName() + "'>");
				html.append("<input class='list_div_click' type='radio' name='" + tempUser.getName() + "' id='"
						+ tempUser.getId() + "' email='"
						+ tempUser.getEmail() + "' telephone='"
						+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
				html.append("<span onclick='" +
						"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);' style='cursor:pointer'  >"+tempUser.getName()+"</span>");
				html.append("</div>");
			}
			int pageCount = (int) Math.ceil((double) users2.size() / (double) pagelines);
			if (pageCount > 0) {
				String url = "/core/user/getOnLineUser4ProxyOrSuperior.action?_pagelines="+this.getParams().getParameterAsString("_pagelines")+"&domain="+domain + "&contentId=" + contentId;
				getLeftPageDiv(html, currentPage, users2.size(), url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void getPageDiv(StringBuffer html, int currentPage, int total,
			String url) {
		Long lines = this.getParams().getParameterAsLong("_pagelines");
		long pagelines = lines != null ? lines : 10;
		
		int pageCount = (int) Math.ceil((double) total / pagelines);
		
		html.append("<div style='padding:5px;border-bottom:1px solid gray;'>");
		
		if (currentPage > 1) {
			html
					.append("<a style='cursor: pointer;color:#316AC5;' onclick='doPageNav(\""
							+ url
							+ "&_currpage=1\")'>{*[FirstPage]*}</a>&nbsp;");
			html
					.append("<a style='cursor: pointer;color:#316AC5;' onclick='doPageNav(\""
							+ url
							+ "&_currpage="
							+ (currentPage - 1)
							+ "\")'>{*[PrevPage]*}</a>&nbsp;");
		}

		if (currentPage < pageCount) {
			html
					.append("<a style='cursor: pointer;color:#316AC5;' onclick='doPageNav(\""
							+ url
							+ "&_currpage="
							+ (currentPage + 1)
							+ "\")'>{*[NextPage]*}</a>&nbsp;");
			html
					.append("<a style='cursor: pointer;color:#316AC5;' onclick='doPageNav(\""
							+ url
							+ "&_currpage="
							+ pageCount
							+ "\")'>{*[EndPage]*}</a>&nbsp;");
		}
		
		if (pageCount > 1) {
			html.append("<a style='cursor: pointer;color:#0000E3;' onclick='doPageNav(\""
					+ url
					+ "&_currpage=1"
					+ "&_pagelines=" + String.valueOf(Integer.MAX_VALUE)
					+ "&_returnPage="
					+ currentPage
					+ "\")'> {*[All]*}</a>&nbsp;");
		}
		
		if (pageCount <= 1 && pagelines > 10) {
			Long rtnPage = this.getParams().getParameterAsLong("_returnPage");
			long returnPage = rtnPage != null ? rtnPage : currentPage;
			html.append("<a style='cursor: pointer;color:#0000E3;' onclick='doPageNav(\""
					+ url
					+ "&_currpage="
					+ returnPage
					+ "\")'> {*[Pagination]*}</a>&nbsp;");
		}
	
		html.append("{*[InPage]*}").append(currentPage).append(
				"{*[Page]*}/{*[Total]*}").append(pageCount).append(
				"{*[Pages]*}&nbsp;");

		html.append("</div>");
	}
	
	public void getLeftPageDiv(StringBuffer html, int currentPage, int total, String url) {
		int pageCount = (int) Math.ceil((double) total / 10.0);
		
		html.append("<div style='padding:5px;border-bottom:1px solid gray;'>");
		if (currentPage > 1) {
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav(\"" + url
					+ "&_currpage=1\")'>{*[FirstPage]*}</a>&nbsp;");
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav(\"" + url + "&_currpage="
					+ (currentPage - 1) + "\")'>{*[PrevPage]*}</a>&nbsp;");
		}

		if (currentPage < pageCount) {
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav(\"" + url + "&_currpage="
					+ (currentPage + 1) + "\")'>{*[NextPage]*}</a>&nbsp;");
			html.append("<a style='cursor: pointer;color:#316AC5;' onclick='doLeftPageNav(\"" + url + "&_currpage="
					+ pageCount + "\")'>{*[EndPage]*}</a>&nbsp;");
		}

		html.append("{*[InPage]*}").append(currentPage).append("{*[Page]*}/{*[Total]*}").append(pageCount).append(
				"{*[Pages]*}&nbsp;");
		html.append("</div>");
	}
}
