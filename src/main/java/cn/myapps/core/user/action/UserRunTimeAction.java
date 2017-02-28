package cn.myapps.core.user.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.xvolks.jnative.logging.JNativeLogger.SEVERITY;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.email.email.action.EmailHelper;
import cn.myapps.core.email.email.ejb.EmailUser;
import cn.myapps.core.email.email.ejb.EmailUserProcess;
import cn.myapps.core.email.util.EmailConfig;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.usergroup.ejb.UserGroupSet;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;


import flex.messaging.util.URLDecoder;

public class UserRunTimeAction extends UserAction {

	public UserRunTimeAction() throws Exception {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EmailUser emailUser;
	
	private String selectMode;

	public String getSelectMode() {
		return selectMode;
	}

	public void setSelectMode(String selectMode) {
		this.selectMode = selectMode;
	}

	/**
	 * 保存个人信息
	 * 
	 * @SuppressWarnings webwork不支持泛型
	 * @return 成功处理返回"SUCCESS",否则提示失败
	 * @throws Exception
	 */
	public String doSavePersonal() throws Exception {
		try {
			UserVO user = (UserVO) getContent();
			try {
				user = this.setPasswordArray(user);
			} catch (Exception e) {
				if (e.getMessage().equals("e1")) {
					return INPUT;
				}
			}

			if (user.getProxyUser() != null && user.getStartProxyTime() != null
					&& user.getEndProxyTime() == null) {
				this
						.addFieldError("1",
								"{*[Please]*}{*[Input]*}{*[Proxy]*}{*[End]*}{*[Date]*}");
				return INPUT;
			}

			if (user.getProxyUser() != null && user.getStartProxyTime() == null
					&& user.getEndProxyTime() != null) {
				this
						.addFieldError("1",
								"{*[Please]*}{*[Input]*}{*[Proxy]*}{*[Start]*}{*[Date]*}");
				return INPUT;
			}

			if (user.getProxyUser() != null
					&& startProxyTime != null
					&& !startProxyTime.equals("")
					&& user.getStartProxyTime().getTime() > user
							.getEndProxyTime().getTime()) {
				this.addFieldError("1", "{*[page.core.calendar.overoftime]*}");
				return INPUT;
			}

			if (user.getProxyUser() != null
					&& endProxyTime != null
					&& !endProxyTime.equals("")
					&& user.getEndProxyTime().getTime() < (new Date())
							.getTime()) {
				this
						.addFieldError("1",
								"{*[Proxy]*}{*[End]*}{*[Date]*}不得晚于{*[Current]*}{*[Time]*}");
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
			if (legal.equals("1") || legal == "1") {
				Pattern p = Pattern
						.compile("[a-zA-Z0-9]*[a-zA-Z]+[0-9]+[a-zA-Z0-9]*");
				Pattern p1 = Pattern
						.compile("[a-zA-Z0-9]*[0-9]+[a-zA-Z]+[a-zA-Z0-9]*");
				Matcher matcher = p.matcher(user.getLoginpwd());
				Matcher matcher1 = p1.matcher(user.getLoginpwd());
				boolean result = matcher.matches();
				boolean result1 = matcher1.matches();
				if ((!result) && (!result1)) {
					this.addFieldError("1", "{*[PasswordLegal]*}");
					return INPUT;
				}

			}

			WebUser webUser = getUser();

			// Email信息处理
			EmailUserProcess euserpro = (EmailUserProcess) ProcessFactory
					.createProcess(EmailUserProcess.class);
			EmailUser emailUser = (EmailUser) euserpro.doView(getEmailUser()
					.getId());
			if (emailUser == null) {
				EmailUser emailUser1 = new EmailUser();
				emailUser1.setOwnerid(webUser.getId());
				emailUser1.setDomainid(webUser.getDomainid());
				emailUser1.setName(webUser.getName());
				emailUser1.setAccount(getEmailUser().getAccount());
				emailUser1.setPassword(getEmailUser().getPassword());
				euserpro.doCreateEmailUser(emailUser1);
			} else {
				emailUser.setOwnerid(webUser.getId());
				emailUser.setDomainid(webUser.getDomainid());
				emailUser.setName(webUser.getName());
				emailUser.setAccount(getEmailUser().getAccount());
				emailUser.setPassword(getEmailUser().getPassword());
				euserpro.doUpdateEmailUser(emailUser);
			}

			// 待办信息设置
			UserDefined userDefined = getUserDefined();
			userDefined.setApplicationid(getApplication());
			userDefined.setUserId(webUser.getId());

			UserDefinedProcess userDefinedProcess = (UserDefinedProcess) ProcessFactory
					.createProcess(UserDefinedProcess.class);

			// 判断是否为后台默认首页
			if ("1".equals(userDefined.getType())) {
				userDefined.setType("0");
				userDefined.setCreator(user.getName());
				userDefinedProcess.doCreate(userDefined);
			} else {
				// 判断是否己有自定义首页
				if (userDefinedProcess.doView(userDefined.getId()) == null) {
					userDefinedProcess.doCreate(userDefined);
				} else {
					userDefinedProcess.doUpdate(userDefined);
				}
			}

			UserVO user1 = ((UserProcess) process).getUserByLoginno(webUser
					.getLoginno(), webUser.getDomainid());
			user.setDefaultDepartment(user1.getDefaultDepartment());
			user.setDefaultApplication(user1.getDefaultApplication());

			((UserProcess) process).doPersonalUpdate(user);
			setContent(user);
			webUser.setName(user.getName());
			webUser.setLoginno(user.getLoginno());
			webUser.setLoginpwd(user.getLoginpwd());
			webUser.setEmail(user.getEmail());
			webUser.setTelephone(user.getTelephone());
			webUser.setCalendarType(user.getCalendarType());
			webUser.setStartProxyTime(user.getStartProxyTime());
			webUser.setEndProxyTime(user.getEndProxyTime());
			webUser.setDefaultDepartment(user.getDefaultDepartment());

			// 把用户设置赋给webuser
			webUser.setUserSetup(user.getUserSetup());
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			// getContext().getSession().put(getWebUserSessionKey(), webUser);

			if (user.getUserSetup() != null) {
				String uskin = user.getUserSetup().getUserSkin();
				if (!StringUtil.isBlank(uskin)) {
					String oldSkin = (String) session
							.getAttribute(Web.SKIN_TYPE);
					if (!StringUtil.isBlank(oldSkin)) {
						if (!oldSkin.equals(uskin)) {
							session.setAttribute(Web.SKIN_TYPE, uskin);
							return "switchskin";
						}
					} else {
						session.setAttribute(Web.SKIN_TYPE, uskin);
						return "switchskin";
					}
				}
			}
			session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
			this.addActionMessage("{*[Save_Success]*}");
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * set_departmentids 用户选择
	 */
	public String doSelectUser() throws Exception {
		return SUCCESS;
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	public String listUserExcept() throws Exception {

		ParamsTable params = getParams();

		WebUser user = getUser();

		params.setParameter("t_domainid", getDomain());
		DataPackage<UserVO> pack = ((UserProcess) process).queryUsersExcept(
				params, user);

		setDatas(pack);

		return SUCCESS;
	}

	/**
	 * 点击树节点对應部门的用户列表。
	 * 
	 * @return
	 */
	public String doTreeList() {
		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String departid = params.getParameterAsString("departid");

			// DepartmentProcess departmentProcess = (DepartmentProcess)
			// ProcessFactory.createProcess(DepartmentProcess.class);
			if (domain != null && domain.trim().length() > 0) {
				if (departid == null || "".equals(departid)) {
					params.setParameter("t_domainid", domain);
					WebUser user = getUser();
					setDatas(process.doQuery(params, user));
				}
				// 列出選擇部門的所有用戶
				else {
					params.setParameter("t_domainid", domain);
					params.setParameter("sm_userDepartmentSets.departmentId",
							departid);
					WebUser user = getUser();
					setDatas(process.doQuery(params, user));
				}
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

	/**
	 * 根据部门获取用户列表
	 * 
	 * @return
	 */
	public String doUserListByDept() {
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String departid = params.getParameterAsString("departid");

			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));
			int total = 0;

			DataPackage<UserVO> users = ((UserProcess)process).queryByDeptForUserDialog(params, getUser(), ServletActionContext
					.getRequest(), null);
			total = users.rowCount;

			for (Iterator<UserVO> iter = users.datas.iterator(); iter.hasNext();) {
				UserVO tempUser = iter.next();
				String email = this.getUserEmail(tempUser);
				String avatar = "";
//				if(!StringUtil.isBlank(tempUser.getAvatar())){
//					JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
//					avatar = json.getString("url");
//				}
				html.append("<div class='list_div' title='"
						+ tempUser.getName() + "'>");
				if("selectOne".equals(getSelectMode())){
					html
							.append("<input class='list_div_click' type='radio' name='"
							+ tempUser.getName()
							+ "' id='"
							+ tempUser.getId()
							+ "' email='"
							+ email
							+ "' avatar='"
							+ avatar
							+ "' emailAccount='"
							+ getUserEmailAccount(email)
							+ "' telephone='"
							+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append("<span onclick='" +
							"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
					html.append("</div>");
				}else{
					html
							.append("<input class='list_div_click' type='checkbox' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ email
									+ "' avatar='"
									+ avatar
									+ "' emailAccount='"
									+ getUserEmailAccount(email)
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append("<span onclick='" +
							"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
					html.append("</div>");
				}
			}

			if (total > 0) {
				String url = "/portal/user/treelist.action?departid="
						+ departid;
				url += "&domain=" + domain + "&selectMode=" + getSelectMode();
				getPageDiv(html, currentPage, total, url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
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
			sm_name = URLDecoder.decode(sm_name);
			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			int total = 0;
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));

			if (getDomain() != null && domain.trim().length() > 0) {
				DataPackage<UserVO> users = ((UserProcess)process).queryForUserDialog(params, getUser(), ServletActionContext
						.getRequest(), null);
				total = users.rowCount;
				for (Iterator<UserVO> iter = users.datas.iterator(); iter.hasNext();) {
					UserVO tempUser = (UserVO) iter.next();
					String email = this.getUserEmail(tempUser);
					String avatar = "";
					if(!StringUtil.isBlank(tempUser.getAvatar())){
						JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
						avatar = json.getString("url");
					}
					html.append("<div class='list_div' title='"
							+ tempUser.getName() + "'>");
					if("selectOne".equals(getSelectMode())){
						html
								.append("<input class='list_div_click' type='radio' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' avatar='"
									+ avatar
									+ "' email='"
									+ email
									+ "' emailAccount='"
									+ getUserEmailAccount(email)
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
						html.append("<span onclick='" +
								"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
						html.append("</div>");
					}else {
						html
								.append("<input class='list_div_click' type='checkbox' name='"
										+ tempUser.getName()
										+ "' id='"
										+ tempUser.getId()
										+ "' avatar='"
										+ avatar
										+ "' email='"
										+ email
										+ "' emailAccount='"
										+ getUserEmailAccount(email)
										+ "' telephone='"
										+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
						html.append("<span onclick='" +
								"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
						html.append("</div>");
					}
				}
			}
			if (total > 0) {
				String url = "/portal/user/getAllUser.action?domain=" + domain
						+ "&sm_name=" + sm_name + "&selectMode=" + getSelectMode();
				getPageDiv(html, currentPage, total, url);
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 获取所有在线用户列表
	 * 
	 */
	// @SuppressWarnings("unchecked")
	public String doOnLineUserList() {
		StringBuffer html = new StringBuffer();
		try {
			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			this.getParams().setParameter("_pagelines", String.valueOf(pagelines));
			String domain = getParams().getParameterAsString("domain");

			DataPackage<WebUser> datas = OnlineUsers.doQueryByDomain(this.getParams(), domain);
			
			String useridStr = biuldUserIdStr();
			int rowCount = 0;
			for (Iterator<WebUser> iter = datas.datas.iterator(); iter.hasNext();) {
				WebUser tempUser = (WebUser) iter.next();
				if(!StringUtil.isBlank(useridStr) && useridStr.indexOf(tempUser.getId()) == -1){
					continue;
				}
				rowCount ++;
				String avatar = "";
				if(!StringUtil.isBlank(tempUser.getAvatar())){
					JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
					avatar = json.getString("url");
				}
				String email = this.getUserEmail(tempUser);
				html.append("<div class='list_div' title='" + tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"") + "'>");
				if("selectOne".equals(getSelectMode())){
					html.append("<input class='list_div_click' type='radio' name='" + tempUser.getName() + "' id='"
							+ tempUser.getId() 
							+ "' email='"
							+ email
							+ "' avatar='"
							+ avatar
							+ "' emailAccount='"
							+ getUserEmailAccount(email) + "' telephone='"
							+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
				}else{
					html.append("<input class='list_div_click' type='checkbox' name='" + tempUser.getName() + "' id='"
							+ tempUser.getId() 
							+ "' email='"
							+ email
							+ "' avatar='"
							+ avatar
							+ "' emailAccount='"
							+ getUserEmailAccount(email)+ "' telephone='"
							+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
				}
				html.append("<span onclick='" +
						"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
				html.append("</div>");
			}
			html.append("<input type='hidden' value='" + rowCount
					+ "' id='onLineUsersCount' name='onLineUsersCount'>");
			int pageCount = (int) Math.ceil((double) rowCount / (double) datas.linesPerPage);
			if (pageCount > 0) {
				String url = "/portal/user/getOnLineUserList.action?_pagelines="+this.getParams().getParameterAsString("_pagelines")+"&domain="+domain;
				getLeftPageDiv(html, currentPage, rowCount, url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
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
	
	/**
	 * 根据角色查找用户列表
	 * 
	 * @return
	 */
	// @SuppressWarnings("unchecked")
	public String doUserListByRole() {
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String rolesid = params.getParameterAsString("rolesid");
			String applicationid = params.getParameterAsString("applicationid");

			Long page = params.getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			int total = 0;
			params.setParameter("_pagelines", String.valueOf(pagelines));

			// params.setParameter("sm_userRoleSets.roleId", rolesid);
			// params.setParameter("t_domainid", getUser().getDomainid());
			if (applicationid != null && applicationid.trim().length() > 0) {
				if (rolesid != null && !"".equals(rolesid)) {

					DataPackage<UserVO> users = ((UserProcess)process).queryByRoleForUserDialog(params, getUser(), ServletActionContext
							.getRequest(), null);
					total = users.rowCount;
					for (Iterator<UserVO> iter = users.datas.iterator(); iter
							.hasNext();) {
						UserVO tempUser = iter.next();
						String avatar = "";
						if(!StringUtil.isBlank(tempUser.getAvatar())){
							JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
							avatar = json.getString("url");
						}
						String email = this.getUserEmail(tempUser);
						html.append("<div class='list_div' title='"
								+ tempUser.getName() + "'>");
						if("selectOne".equals(getSelectMode())){
							html
									.append("<input class='list_div_click' type='radio' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ email
									+ "' avatar='"
									+ avatar
									+ "' emailAccount='"
									+ getUserEmailAccount(email)
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
							html.append("<span onclick='" +
									"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
							html.append("</div>");
						}else{
							html
									.append("<input class='list_div_click' type='checkbox' name='"
											+ tempUser.getName()
											+ "' id='"
											+ tempUser.getId()
											+ "' email='"
											+ email
											+ "' avatar='"
											+ avatar
											+ "' emailAccount='"
											+ getUserEmailAccount(email)
											+ "' telephone='"
											+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
							html.append("<span onclick='" +
									"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);'>"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
							html.append("</div>");
						}
					}
					String url = "/portal/user/getUserListByRole.action?applicationid="
							+ applicationid;
					url += "&rolesid=" + rolesid + "&selectMode=" + getSelectMode();
					getPageDiv(html, currentPage, total, url);
				}
			}
			
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		}catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doUserListBycontancts(){
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = this.getParams();
			Long page = params.getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			Long lines = this.getParams().getParameterAsLong("_pagelines");
			int pagelines = lines != null ? lines.intValue() : 10;
			
			int total = 0;
			params.setParameter("_pagelines", String.valueOf(pagelines));
			
			String hql = "";
			String domainid = params.getParameterAsString("domainid");
			String contanctsid = params.getParameterAsString("contanctsid");
			if (!StringUtil.isBlank(domainid)) {
				if("all".equals(contanctsid) || StringUtil.isBlank(contanctsid)){
					hql += "FROM " + UserVO.class.getName() + " vo WHERE vo.domainid='" + domainid + "' AND dimission=1";
					hql += biuldUserIdStr();
					hql += " ORDER BY vo.id,vo.orderByNo";
				}else {
					hql += "SELECT U FROM cn.myapps.core.user.ejb.UserVO U WHERE U.id in (SELECT G.userId FROM " 
						+ UserGroupSet.class.getName() + " G WHERE G.userGroupId='" + contanctsid + "') AND U.dimission=1";
					hql += biuldUserIdStr();
					hql += " ORDER BY U.id,U.orderByNo";
				}
			}
			Collection<UserVO> users = process.doQueryByHQL(hql,
					currentPage, pagelines);
			total = process.doGetTotalLines(hql);
			for (Iterator<UserVO> iter = users.iterator(); iter
					.hasNext();) {
				UserVO tempUser = iter.next();
				String avatar = "";
				if(!StringUtil.isBlank(tempUser.getAvatar())){
					JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
					avatar = json.getString("url");
				}
				String email = this.getUserEmail(tempUser);
				html.append("<div class='list_div' title='"
						+ tempUser.getName() + "'>");
				if("selectOne".equals(getSelectMode())){
					html
							.append("<input class='list_div_click' type='radio' name='"
							+ tempUser.getName()
							+ "' id='"
							+ tempUser.getId()
							+ "' email='"
							+ email
							+ "' avatar='"
							+ avatar
							+ "' emailAccount='"
							+ getUserEmailAccount(email)
							+ "' telephone='"
							+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append(tempUser.getName());
					html.append("</div>");
				}else{
					html
							.append("<input class='list_div_click' type='checkbox' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ email
									+ "' avatar='"
									+ avatar
									+ "' emailAccount='"
									+ getUserEmailAccount(email)
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append(tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":""));
					html.append("</div>");
				}
			}
			String url = "/portal/usergroup/getUserListBycontancts.action?domainid="
				+ domainid;
			url += "&contanctsid=" + contanctsid + "&selectMode=" + getSelectMode();
			getPageDiv(html, currentPage, total, url);
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
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
		
		html.append("<div class='user_run_page' style='padding:5px;border-bottom:1px solid gray;'>");
		
		if (currentPage > 1) {
			html
					.append("<a style='cursor: pointer;' onclick='doPageNav(\""
							+ url
							+ "&_currpage=1\")'>{*[FirstPage]*}</a>&nbsp;");
			html
					.append("<a style='cursor: pointer;' onclick='doPageNav(\""
							+ url
							+ "&_currpage="
							+ (currentPage - 1)
							+ "\")'>{*[PrevPage]*}</a>&nbsp;");
		}

		if (currentPage < pageCount) {
			html
					.append("<a style='cursor: pointer;' onclick='doPageNav(\""
							+ url
							+ "&_currpage="
							+ (currentPage + 1)
							+ "\")'>{*[NextPage]*}</a>&nbsp;");
			html
					.append("<a style='cursor: pointer;' onclick='doPageNav(\""
							+ url
							+ "&_currpage="
							+ pageCount
							+ "\")'>{*[EndPage]*}</a>&nbsp;");
		}
		
		if (pageCount > 1) {
			html.append("<a style='cursor: pointer;' onclick='doPageNav(\""
					+ url
					+ "&_currpage=1"
					+ "&_pagelines=" + String.valueOf(Integer.MAX_VALUE)
					+ "&_returnPage="
					+ currentPage
					+ "\")'>{*[All]*}</a>&nbsp;");
		}
		
		if (pageCount <= 1 && pagelines > 10) {
			Long rtnPage = this.getParams().getParameterAsLong("_returnPage");
			long returnPage = rtnPage != null ? rtnPage : currentPage;
			html.append("<a style='cursor: pointer;' onclick='doPageNav(\""
					+ url
					+ "&_currpage="
					+ returnPage
					+ "\")'>{*[Pagination]*}</a>&nbsp;");
		}
	
		html.append("<a>{*[InPage]*}").append(currentPage).append(
				"{*[Page]*}/{*[Total]*}").append(pageCount).append(
				"{*[Pages]*}</a>&nbsp;");

		html.append("</div>");
	}
	
	public EmailUser getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}

	public UserVO setPasswordArray(UserVO user) throws Exception {

		PropertyUtil.reload("passwordLegal");
		String passwordArrayLengthString = PropertyUtil
				.get(LoginConfig.LOGIN_PASSWORD_UPADTE_TIMES);
		int passwordArrayLength = Integer.parseInt(passwordArrayLengthString);

		UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO po = (UserVO) userProcess.doView(user.getId());
		String oldPasswordArray = "";
		oldPasswordArray = po.getPasswordArray();

		String pwda[] = null;
		int oldpasswordArrayLength = 0;
		if (oldPasswordArray != null) {
			pwda = oldPasswordArray.split(",");
			oldpasswordArrayLength = oldPasswordArray.split(",").length;
		}

		String passwordArray = null;
		passwordArray = user.getPasswordArray();
		String loginpwd = user.getLoginpwd();

		if (!loginpwd.trim().equals(decrypt(po.getLoginpwd()))
				&& !loginpwd.trim().equals(Web.DEFAULT_SHOWPASSWORD)) {
			if (oldPasswordArray != null) {
				for (int i = 0; i < oldpasswordArrayLength; i++) {
					if (loginpwd.equals(decrypt(pwda[i]))) {
						this.addFieldError("1", "{*[ModifyPasswordNotSame]*}"
								+ passwordArrayLengthString);
						throw new Exception("e1");
					}
				}
				passwordArray = oldPasswordArray + "," + encrypt(loginpwd);
			} else {
				passwordArray = po.getLoginpwd() + ","
						+ encrypt(user.getLoginpwd());
			}
			user.setLastModifyPasswordTime(new Date());

		} else {
			passwordArray = po.getPasswordArray();
			user.setLastModifyPasswordTime(po.getLastModifyPasswordTime());
		}
		if (oldpasswordArrayLength + 1 > passwordArrayLength) {
			int i1 = passwordArray.split(",").length;
			String passwordArraytmp = "";
			for (int i = i1 - passwordArrayLength - 1; i < i1; i++) {
				passwordArraytmp += "," + passwordArray.split(",")[i];
			}
			passwordArray = passwordArraytmp.substring(1);
		}
		user.setDefaultDepartment(po.getDefaultDepartment());
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
	private String decrypt(final String s) {
		return Security.decryptPassword(s);
	}

	/**
	 * 根据企业域和应用查找用户
	 * @return
	 */
	public String doAllUserByApp(){
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String applicationid = params.getParameterAsString("application");
			String sm_name = params.getParameterAsString("sm_name");

			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;

			int total = 0;

			if (getDomain() != null && domain.trim().length() > 0) {
				DataPackage<UserVO> datas = ((UserProcess)process).queryByDomainAndApplicationWithHQL(domain, applicationid, params);
				total = datas.getRowCount();
				for (Iterator<UserVO> iter = datas.datas.iterator(); iter.hasNext();) {
					UserVO tempUser = (UserVO) iter.next();
					String avatar = "";
					if(!StringUtil.isBlank(tempUser.getAvatar())){
						JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
						avatar = json.getString("url");
					}
					html.append("<div class='list_div' title='"
							+ tempUser.getName() + "'>");
					if("selectOne".equals(getSelectMode())){
						html
								.append("<input class='list_div_click' type='radio' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ tempUser.getEmail()
									+ "' avatar='"
									+ avatar
									+ "' emailAccount='"
									+ getUserEmailAccount(tempUser.getEmail())
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
						html.append(tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":""));
						html.append("</div>");
					}else {
						html
								.append("<input class='list_div_click' type='checkbox' name='"
										+ tempUser.getName()
										+ "' id='"
										+ tempUser.getId()
										+ "' email='"
										+ tempUser.getEmail()
										+ "' avatar='"
										+ avatar
										+ "' emailAccount='"
										+ getUserEmailAccount(tempUser.getEmail())
										+ "' telephone='"
										+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
						html.append(tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":""));
						html.append("</div>");
					}
				}
			}
			if (total > 0) {
				String url = "/portal/user/getAllUserByApp.action?domain=" + domain
						+ "&sm_name=" + sm_name + "&selectMode=" + getSelectMode()
						+ "&application=" + applicationid;
				getPageDiv(html, currentPage, total, url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 根据企业域、应用以及部门查找用户
	 * @return
	 */
	public String doUserByApp(){
		StringBuffer html = new StringBuffer();
		try {
			ParamsTable params = getParams();
			String domain = params.getParameterAsString("domain");
			String departid = params.getParameterAsString("departid");
			String applicationid = params.getParameterAsString("application");

			Long page = this.getParams().getParameterAsLong("_currpage");
			int currentPage = page != null ? page.intValue() : 1;
			
			int total = 0;

			Collection<UserVO> users = ((UserProcess)process).queryByDomainAndApplicationAndDeptWithHQL(domain, applicationid, departid, params);
			total = users.size();
			for (Iterator<UserVO> iter = users.iterator(); iter.hasNext();) {
				UserVO tempUser = iter.next();
				String avatar = "";
				if(!StringUtil.isBlank(tempUser.getAvatar())){
					JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
					avatar = json.getString("url");
				}
				html.append("<div class='list_div' title='"
						+ tempUser.getName() + "'>");
				if("selectOne".equals(getSelectMode())){
					html
							.append("<input class='list_div_click' type='radio' name='"
							+ tempUser.getName()
							+ "' id='"
							+ tempUser.getId()
							+ "' email='"
							+ tempUser.getEmail()
							+ "' avatar='"
							+ avatar
							+ "' emailAccount='"
							+ getUserEmailAccount(tempUser.getEmail())
							+ "' telephone='"
							+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append(tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":""));
					html.append("</div>");
				}else{
					html
							.append("<input class='list_div_click' type='checkbox' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ tempUser.getEmail()
									+ "' avatar='"
									+ avatar
									+ "' emailAccount='"
									+ getUserEmailAccount(tempUser.getEmail())
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append(tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":""));
					html.append("</div>");
				}
			}

			if (total > 0) {
				String url = "/portal/user/treelist.action?departid="
						+ departid;
				url += "&domain=" + domain + "&selectMode=" + getSelectMode();
				getPageDiv(html, currentPage, total, url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 根据企业域获取所有用户列表，用于选择代理人、或上级用户
	 * 
	 * @return
	 */
	public String doAllUser4ProxyOrSuperior() {
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
					String avatar = "";
					if(!StringUtil.isBlank(tempUser.getAvatar())){
						JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
						avatar = json.getString("url");
					}
					html.append("<div class='list_div' title='"
							+ tempUser.getName() + "'>");
						html
								.append("<input class='list_div_click' type='radio' name='"
									+ tempUser.getName()
									+ "' id='"
									+ tempUser.getId()
									+ "' email='"
									+ tempUser.getEmail()
									+ "' avatar='"
									+ avatar
									+ "' emailAccount='"
									+ getUserEmailAccount(tempUser.getEmail())
									+ "' telephone='"
									+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
						html.append("<span onclick='" +
								"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);' style='cursor:pointer'  >"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
						html.append("</div>");
				}
			}
			if (total > 0) {
				String url = "/portal/user/getAllUser4ProxyOrSuperior.action?domain=" + domain
						+ "&sm_name=" + sm_name + "&contentId=" + contentId;
				getPageDiv(html, currentPage, total, url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
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
				String avatar = "";
				if(!StringUtil.isBlank(tempUser.getAvatar())){
					JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
					avatar = json.getString("url");
				}
				html.append("<div class='list_div' title='"
						+ tempUser.getName() + "'>");
					html
							.append("<input class='list_div_click' type='radio' name='"
							+ tempUser.getName()
							+ "' id='"
							+ tempUser.getId()
							+ "' email='"
							+ tempUser.getEmail()
							+ "' avatar='"
							+ avatar
							+ "' emailAccount='"
							+ getUserEmailAccount(tempUser.getEmail())
							+ "' telephone='"
							+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
					html.append("<span onclick='" +
							"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);' style='cursor:pointer'  >"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
					html.append("</div>");
			}

			if (total > 0) {
				String url = "/portal/user/treelist4ProxyOrSuperior.action?departid="
						+ departid;
				url += "&domain=" + domain + "&contentId=" + contentId;
				getPageDiv(html, currentPage, total, url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		}catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
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
				String avatar = "";
				if(!StringUtil.isBlank(tempUser.getAvatar())){
					JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
					avatar = json.getString("url");
				}
				html.append("<div class='list_div' title='" + tempUser.getName() + "'>");
				html.append("<input class='list_div_click' type='radio' name='" + tempUser.getName() + "' id='"
						+ tempUser.getId() 
						+ "' email='"
						+ tempUser.getEmail()
						+ "' avatar='"
						+ avatar
						+ "' emailAccount='"
						+ getUserEmailAccount(tempUser.getEmail())+ "' telephone='"
						+ tempUser.getTelephone() + "' onclick='selectUser(jQuery(this),true)'>");
				html.append("<span onclick='" +
						"jQuery(this).prev().click();selectUser(jQuery(this).prev(),true);' style='cursor:pointer'  >"+tempUser.getName()+(tempUser.isLiaisonOfficer()?"-[{*[core.user.LiaisonOfficer]*}]":"")+"</span>");
				html.append("</div>");
			}
			int pageCount = (int) Math.ceil((double) users2.size() / (double) pagelines);
			if (pageCount > 0) {
				String url = "/portal/user/getOnLineUser4ProxyOrSuperior.action?_pagelines="+this.getParams().getParameterAsString("_pagelines")+"&domain="+domain + "&contentId=" + contentId;
				getLeftPageDiv(html, currentPage, users2.size(), url);
			}
			ServletActionContext.getRequest().setAttribute("HTML", html.toString());

		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取用户Email
	 * @param tempUser
	 * @return
	 * @throws Exception
	 */
	private String getUserEmail(BaseUser tempUser) throws Exception{
		String email = tempUser.getEmail();
		if(EmailConfig.isUserEmail()){
			EmailUserProcess euserpro = (EmailUserProcess) ProcessFactory
					.createProcess(EmailUserProcess.class);
			EmailUser emailUser = (EmailUser) euserpro.getEmailUserByOwner(tempUser.getId(), tempUser.getDomainid());
			email = new EmailHelper().showAccount(emailUser);
		}
		return email == null ? "" : email;
	}
	
	private String getUserEmailAccount(String email){
		if(!StringUtil.isBlank(email)&& email.indexOf("@") > 0){
			String ea=email.substring(email.indexOf("<")+1, email.indexOf("@"));
			return ea;
		}
		return "";
	}
}
