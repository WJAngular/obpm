package cn.myapps.mobile.login;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.permission.action.PermissionHelper;
import cn.myapps.core.personalmessage.action.PersonalMessageHelper;
import cn.myapps.core.personalmessage.ejb.MessageBody;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.resource.action.ResourceHelper;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.WebCookies;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.property.MultiLanguageProperty;
import cn.myapps.util.property.PropertyUtil;

public class MbLoginAction extends ActionSupport {
	
	private String username;
	private String password;
	private String domainname;
	private String _resourceId;
	private String _deep;
	private String _currResourceId;
	private String application;
	private boolean isShowApps;
	private int language;
	private int _pagelines;
	private boolean pendingMore=false;
	private boolean personalMore=false;
	private WebCookies webCookies;

	private static final long serialVersionUID = -4277772173056045618L;
	private static final Logger LOG = Logger.getLogger(MbLoginAction.class);

	public MbLoginAction() throws Exception {
	}

	/**
	 * 登录
	 * 
	 * @return "SUCCESS","ERROR"
	 * @throws Exception
	 */
	public String doLogin() throws Exception {
		// file and to also use the specified CallbackHandler.
		try {
			PropertyUtil.reload("sso");
			String loginFailTimesString = PropertyUtil.get(LoginConfig.LOGIN_FAIL_TIMES);
			int loginPasswordErrortimes = Integer.parseInt(loginFailTimesString);
			HttpSession session = ServletActionContext.getRequest().getSession();
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);

			ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);

			UserVO user = process.login(username, password, domainname,loginPasswordErrortimes);
			if (user != null && user.getStatus() == 1) {
				WebUser webUser = new WebUser(user);
				// webUser.setDomainid(user.getDomainid());

				String application = appProcess.getDefaultApplication(webUser.getDefaultApplication(), webUser)
						.getId();
				//webUser.setApplicationid(application);
				webUser.setDefaultApplication(application);

				UserVO vo = (UserVO) user.clone();
				vo.setDefaultApplication(application);
				vo.setLoginpwd(null);
				process.doUpdateWithCache(vo);

				session.setAttribute(Web.SESSION_ATTRIBUTE_DOMAIN, webUser.getDomainid());
				// OnlineUserBindingListener oluser = new
				// OnlineUserBindingListener(webUser);
				// session.setAttribute(Web.SESSION_ATTRIBUTE_ONLINEUSER,
				// oluser);
				session.setAttribute(Web.SESSION_ATTRIBUTE_APPLICATION, application);

				String language = MultiLanguageProperty.getName(2);
				session.setAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE, language);
				// session.setMaxInactiveInterval(20 * 60); // 20 minutes

				session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);

				ApplicationHelper helper = new ApplicationHelper();
				// 查找当前域下的所有应用（应该改为查找域个数）
				Collection<ApplicationVO> cols = helper.getListByWebUser(webUser);
				if (cols.size() > 1) {
					isShowApps = true;
					setApplication(application);
				}
				// 在环境中设置context path
				// Environment.getInstance().setContextPath(ServletActionContext.getRequest().getContextPath());
				String toXml = toResourceXml(null, null, application, webUser, isShowApps, " ss ='" + session.getId() + "'");
				if (toXml != null) {
					session.setAttribute("toXml", toXml);
				}
			}

		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			LOG.warn(e);
			return ERROR;
		}
		return SUCCESS;
	}

	public String doSearchResource() throws Exception {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String toXml = toResourceXml(_resourceId, _deep, getApplication(), getUser(), isShowApps, "");
			if (toXml != null) {
				session.setAttribute("toXml", toXml);
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	public String doRefresh() throws Exception {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String toXml = toResourceXml(_resourceId, "" + (Integer.parseInt(_deep) + 1), getApplication(), getUser(),
					isShowApps, "");
			if (toXml != null) {
				session.setAttribute("toXml", toXml);
			}
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	private String toResourceXml(String resourceid, String deep, String applicationid, WebUser user, boolean isShow,
			String handler) throws Exception {
		PermissionHelper helper = new PermissionHelper();
		ResourceHelper resHelper = new ResourceHelper();
		resHelper.setApplicationid(applicationid);
		helper.setUser(user);
		if (deep == null || deep.equals("0"))
			deep = "1";
		ResourceVO res = null;
		if (resourceid == null || resourceid.trim().equals("")) {
			res = resHelper.getTopResourceByName("Mobile");
		} else {
			res = resHelper.getResourcById(resourceid);
		}
		//boolean topMenu = false;
		Collection<ResourceVO> col = resHelper.searchResourceForMb(res, user.getDomainid());
		StringBuffer buffer = new StringBuffer();
		String title = null;
		if (res != null)
			title = res.getDescription();
		if (title == null || title.trim().length() <= 0 || title.equals("Mobile"))

			if (res != null)
				title = res.getDescription();
		if (title == null || title.trim().length() <= 0 || title.trim().equals("Mobile"))
			title = "主菜单";
		buffer.append("<").append(MobileConstant.TAG_HOMEPAGE).append(" ").append(MobileConstant.ATT_TITLE).append(
				"='" + title + "' ").append(handler + ">");
		buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append(
				"='_backId'>");
		if (res != null && res.getSuperior() != null) {
			buffer.append(res.getSuperior().getId());
		} else {
			buffer.append("mobile");
			//topMenu = true;
		}
		buffer.append("</").append(MobileConstant.TAG_HIDDENFIELD).append(">");

		if (res != null && res.getId() != null) {
			buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append(
				"='_resourceId'>");
			buffer.append(res.getId());
			buffer.append("</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
		}
		buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append(
				"='isShowApps'>");
		buffer.append(isShowApps);
		buffer.append("</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
		buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append(
				"='application'>" + applicationid + "</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
		buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append(
				"='_deep'>" + (Integer.parseInt(deep) - 1) + "</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
		// if (isShowApps && topMenu) {
		
		//拼接Link queryString 中参数  kharry
		
		addAppsCommands(buffer);
		// }
		if (col != null) {
			sort(col, helper, buffer, deep, applicationid, user);
		}
		//}
		buffer.append("</").append(MobileConstant.TAG_HOMEPAGE).append(">");
		return buffer.toString();
	}
	
	/**
	 * 在手机端隐藏参数传递过去
	 * 
	 * @param link
	 * @return
	 */
	public String appendQueryString(ResourceVO link) {
		if(link != null){
			StringBuffer sb = new StringBuffer();
			Collection<Object> qs = JsonUtil.toCollection(link.getQueryString(), JSONObject.class);
			Iterator<Object> iterator = qs.iterator();
			while (iterator.hasNext()) {
				JSONObject object = JSONObject.fromObject(iterator.next());
				sb.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
						"='"+ object.get("paramKey") +"'>");
				sb.append(object.get("paramValue"));
				sb.append("</").append(MobileConstant.TAG_PARAMETER).append(">");
			}
			return sb.toString();
		}
		return "";
	}

	private void addMenu(StringBuffer buffer, ResourceVO vo, int index, String deep) {
		boolean flag = false;
		if (ResourceVO.LinkType.VIEW.getCode().equals(vo.getLinkType())
				|| ResourceVO.LinkType.FORM.getCode().equals(vo.getLinkType())) {
			flag = true;
		} else {
			if (ResourceVO.LinkType.MANUAL_EXTERNAL.getCode().equals(vo.getLinkType())) {
				return;
			}
		}
		buffer.append("<").append(MobileConstant.TAG_MENU).append(" ").append(MobileConstant.ATT_ORDER).append(
				"='" + index + "' ").append(MobileConstant.ATT_ID).append(" = '" + vo.getId() + "' ").append(
				MobileConstant.ATT_DEEP).append(" = '" + deep + "'>");

		buffer.append("<").append(MobileConstant.TAG_TEXT).append(">" + vo.getDescription() + "</").append(
				MobileConstant.TAG_TEXT).append(">");
		if (flag) {
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ");
			if (ResourceVO.LinkType.FORM.getCode().equals(vo.getLinkType())) {
				buffer.append(MobileConstant.ATT_TYPE).append("='" + vo.getLinkType() + "'>");
				buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
						"='_formid'>" + vo.getActionContent() + "</").append(MobileConstant.TAG_PARAMETER).append(">");
				buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
						"='application'>" + vo.getApplicationid() + "</").append(MobileConstant.TAG_PARAMETER).append(">");
				buffer.append(appendQueryString(vo));
			} else {
				buffer.append(MobileConstant.ATT_TYPE).append("='" + vo.getLinkType() + "'>");
				buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
						"='_viewid'>" + vo.getActionContent() + "</").append(MobileConstant.TAG_PARAMETER).append(">");
				buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
						"='application'>" + vo.getApplicationid() + "</").append(MobileConstant.TAG_PARAMETER).append(">");
				buffer.append(appendQueryString(vo));
			}
			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
		}
		buffer.append("<").append(MobileConstant.TAG_ICO).append(">" + vo.getMobileIco() + "</").append(MobileConstant.TAG_ICO)
				.append(">");
		buffer.append("</").append(MobileConstant.TAG_MENU).append(">");
	}

	private void sort(Collection<ResourceVO> col, PermissionHelper helper, StringBuffer buffer, String deep, String applicationid,
			WebUser user) throws Exception {
		ResourceVO[] tmp = new ResourceVO[col.size()];
		int j = 0;
		for (Iterator<ResourceVO> iterator = col.iterator(); iterator.hasNext();) {
			ResourceVO tmpRes = (ResourceVO) iterator.next();
			tmp[j] = tmpRes;
			j++;
		}
		for (int i = 0; i < tmp.length; i++) {
			ResourceVO tmpRes = tmp[i];
//			boolean flag = true;
//			if (!StringUtil.isBlank(tmpRes.getOrderno())) {
//				flag = false;
//			}
//			for (j = i + 1; j < tmp.length; j++) {
//				if (!flag && !StringUtil.isBlank(tmp[j].getOrderno())) {
//					if (tmp[j].getOrderno().compareTo(tmpRes.getOrderno()) <= 0) {
//						flag = true;
//					}
//				}
//				if (flag) {
//					tmp[i] = tmp[j];
//					tmp[j] = tmpRes;
//					tmpRes = tmp[i];
//				}
//			}
			if (helper.checkPermission(tmpRes, applicationid, user)) {
				addMenu(buffer, tmpRes, i, deep);
			}
		}
	}

	public String doChange() {
		//long start = System.currentTimeMillis();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			//session.setAttribute(Web.SESSION_ATTRIBUTE_APPLICATION, getApplication());
			WebUser webUser = getUser();
			String userid = webUser.getId();

			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO userPO = (UserVO) userProcess.doView(userid);
			UserVO userVO = (UserVO) userPO.clone();
			// 对于User的update进行特别处理
			userVO.setDefaultApplication(getApplication());
			userVO.setLoginpwd(null);
			userProcess.doUpdateWithCache(userVO);
			//webUser.setApplicationid(getApplication());
			MultiLanguageProperty.load(getApplication(), false);
			String toXml = toResourceXml(null, null, getApplication(), getUser(), isShowApps, "");
			if (toXml != null) {
				session.setAttribute("toXml", toXml);
			}

		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		//long end = System.currentTimeMillis();
		return SUCCESS;
	}

	private void addAppsCommands(StringBuffer buffer) throws Exception {
		ApplicationHelper helper = new ApplicationHelper();
		Collection<ApplicationVO> cols = helper.getListByWebUser(getUser());
		for (Iterator<ApplicationVO> iterator = cols.iterator(); iterator.hasNext();) {
			ApplicationVO appVO = (ApplicationVO) iterator.next();
			buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='900'");
			if (appVO.getId().equals(application)) {
				buffer.append(" ").append(MobileConstant.ATT_SELECTED).append("='true'");
			}
			buffer.append(" ").append(MobileConstant.ATT_NAME).append("='" + appVO.getName() + "'>");
			buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
					"='application'>" + appVO.getId() + "</").append(MobileConstant.TAG_PARAMETER).append(">");
			buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
					"='isShowApps'>" + isShowApps + "</").append(MobileConstant.TAG_PARAMETER).append(">");
			// /
			if (appVO.getLogourl() == null || appVO.getLogourl().equals(""))
				buffer.append("<").append(MobileConstant.TAG_ICO).append(">001</").append(MobileConstant.TAG_ICO).append(">");
			else
				buffer.append("<").append(MobileConstant.TAG_ICO).append(">").append(appVO.getLogourl()).append("</").append(
						MobileConstant.TAG_ICO).append(">");

			buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");

		}
	}

	public String doAcquireApps() throws Exception {
		try {
			StringBuffer buffer = new StringBuffer();
			ApplicationHelper helper = new ApplicationHelper();
			Collection<ApplicationVO> cols = helper.getListByWebUser(getUser());
			int i = 0;
			buffer.append("<").append(MobileConstant.TAG_HOMEPAGE).append(" ").append(MobileConstant.ATT_TITLE)
					.append("='软件列表'>");
			for (Iterator<ApplicationVO> iterator = cols.iterator(); iterator.hasNext();) {
				ApplicationVO appVO = (ApplicationVO) iterator.next();
				buffer.append("<").append(MobileConstant.TAG_MENU).append(" ").append(MobileConstant.ATT_ORDER).append(
						"='" + i + "' ").append(MobileConstant.ATT_ID).append(" = '" + appVO.getId() + "' ").append(
						MobileConstant.ATT_DEEP).append(" = '0'>");
				buffer.append("<").append(MobileConstant.TAG_TEXT).append(">" + appVO.getName() + "</").append(
						MobileConstant.TAG_TEXT).append(">");
				buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE)
						.append("='900'>");
				buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
						"='application'>" + appVO.getId() + "</").append(MobileConstant.TAG_PARAMETER).append(">");
				buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(
						"='isShowApps'>" + isShowApps + "</").append(MobileConstant.TAG_PARAMETER).append(">");

				buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
				buffer.append("<").append(MobileConstant.TAG_ICO).append(">001</").append(MobileConstant.TAG_ICO).append(">");
				buffer.append("</").append(MobileConstant.TAG_MENU).append(">");
			}
			buffer.append("</").append(MobileConstant.TAG_HOMEPAGE).append(">");
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", buffer.toString());
		} catch (Exception e) {
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 注销
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String doLogout() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.invalidate();
		return null;
	}

	/**
	 * 返回密码
	 * 
	 * @return 返回密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 返回用户帐号
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户帐号
	 * 
	 * @param username
	 *            用户帐号
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String get_resourceId() {
		return _resourceId;
	}

	public String get_deep() {
		return _deep;
	}

	public void set_resourceId(String id) {
		_resourceId = id;
	}

	public void set_deep(String _deep) {
		this._deep = _deep;
	}

	public int get_pagelines() {
		if (_pagelines <= 0) _pagelines = 5;
		return _pagelines;
	}

	public void set_pagelines(int _pagelines) {
		this._pagelines = _pagelines;
	}

	/**
	 * Get WebUser Object.
	 * 
	 * @return WebUser Object user
	 * @throws Exception
	 */
	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user == null) {
			UserVO vo = new UserVO();
			vo.getId();
			vo.setName("GUEST");
			vo.setLoginno("guest");
			vo.setLoginpwd("");
			vo.setRoles(null);
			vo.setEmail("");
			// vo.setLanguageType(1);
			user = new WebUser(vo);
		}
		return user;
	}

	public boolean getIsShowApps() {
		return isShowApps;
	}

	public void setIsShowApps(boolean isShowApps) {
		this.isShowApps = isShowApps;
	}

	public static ActionContext getContext() {
		return ServletActionContext.getContext();
	}

	public String get_currResourceId() {
		return _currResourceId;
	}

	public void set_currResourceId(String _currResourceId) {
		this._currResourceId = _currResourceId;
	}

	public String getDomainname() {
		return domainname;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}

	private Map<String, List<String>> fieldErrors;

	public void addFieldError(String fieldname, String message) {
		List<String> thisFieldErrors = getFieldErrors().get(fieldname);

		if (thisFieldErrors == null) {
			thisFieldErrors = new ArrayList<String>();
			this.fieldErrors.put(fieldname, thisFieldErrors);
		}
		thisFieldErrors.add(message);
	}

	public Map<String, List<String>> getFieldErrors() {
		if (fieldErrors == null)
			fieldErrors = new HashMap<String, List<String>>();
		return fieldErrors;
	}

	/**
	 * @SuppressWarnings API不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public void setFieldErrors(Map fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
	
	//////////////////////////////////////////////////////
	//////*******************************************/////
	//////////////////////////////////////////////////////
	
	public String doLogin2() {
		try {
			Cookie pwdErrorTimes = getErrorTimes();
			int loginPasswordErrortimes = 0;
			if(pwdErrorTimes != null){
				loginPasswordErrortimes  = Integer.parseInt(pwdErrorTimes.getValue());
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = process.login(username, password, domainname,loginPasswordErrortimes);
			if (user == null) {
				throw new Exception("用户名或密码不正确");
			}
			if (user.getStatus() != 1) {
				throw new Exception("当前用户：" + user.getLoginno() + "，尚未激活");
			}
			
			WebUser webUser = MbLoginHelper.initLogin(request, user);
			String language = MultiLanguageProperty.getName(this.language);
			session.setAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE, language);
			session.setAttribute("_pagelines", Integer.valueOf(get_pagelines()));
			
			StringBuffer xml = new StringBuffer();
			xml.append("<" + MobileConstant.TAG_HOMEPAGE + " " + MobileConstant.ATT_SESSIONID + "='" + session.getId() + "'>");
			xml.append(getApplicationXmlByDomain(webUser));
			xml.append("</" + MobileConstant.TAG_HOMEPAGE + ">");
			if (xml.length() > 5) {
				session.setAttribute("toXml", xml.toString());
			}
			return SUCCESS;
		} catch (InvocationTargetException e1) {
			addFieldError("SystemError", e1.getTargetException().getMessage());
			e1.printStackTrace();
			return ERROR;
		}catch (Exception e) {
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String doGetMenu() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			String xml = getMenuByApplication(application, _resourceId, getUser());
			//if (xml.length() > 5) {
				session.setAttribute("toXml", xml);
			//}
			return SUCCESS;
		} catch (Exception e) {
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String doHomePage() {
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			StringBuffer xml = new StringBuffer();
			xml.append("<" + MobileConstant.TAG_HOMEPAGE + " " + MobileConstant.ATT_SESSIONID + "='" + session.getId() + "'>");
			xml.append(getApplicationXmlByDomain(getUser()));
			xml.append("</" + MobileConstant.TAG_HOMEPAGE + ">");
			if (xml.length() > 5) {
				session.setAttribute("toXml", xml.toString());
			}
			return SUCCESS;
		} catch (Exception e) {
			LOG.warn(e);
			return ERROR;
		}
	}
	
	public String doChangeApplication() {
		try {
			WebUser user = getUser();
			if (!user.getDefaultApplication().equals(application)) {
				UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				process.doUpdateDefaultApplication(user.getId(), application);
				user.setDefaultApplication(application);
			}
		} catch (Exception e) {
			LOG.warn(e);
		}
		return SUCCESS;
	}
	
	private String getApplicationXmlByDomain(WebUser user) throws Exception {
		StringBuffer xml = new StringBuffer();
		DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
		DomainVO domain = (DomainVO) domainProcess.doView(user.getDomainid());
		
		//获取默认软件 软件被删除后选择第一个软件
		ApplicationProcess applicationProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		ApplicationVO defaultApplication = applicationProcess.getDefaultApplication(user.getDefaultApplication(), user);
		MbLoginHelper loghelper = new MbLoginHelper();
		if (domain == null) return xml.toString();
		for (ApplicationVO vo : domain.getApplications()) {
			if(vo.isActivated()){
				Collection<RoleVO> roles = loghelper.getRolesByApplication(user, vo.getId());
				if(roles != null && !roles.isEmpty()){
					xml.append("<" + MobileConstant.TAG_APPLICATION + " ");
					xml.append(MobileConstant.ATT_NAME + "='" + vo.getName() + "' ");
					xml.append(MobileConstant.ATT_ID + "='" + vo.getId() + "' ");
					if (vo.getId().equals(defaultApplication.getId())) {
						this.setApplication(vo.getId());
						xml.append(MobileConstant.ATT_SELECTED + "='true'>");
						xml.append(getMenuByApplication(application, null, user));
					} else {
						xml.append(MobileConstant.ATT_SELECTED + "='false'>");
					}
					xml.append("<").append(MobileConstant.TAG_ICO).append(">001</").append(MobileConstant.TAG_ICO).append(">");
					xml.append("</" + MobileConstant.TAG_APPLICATION + ">");
				}
			}
		}
		return xml.toString();
	}
	
	private String getMenuByApplication(String application, String superid, WebUser user) {
		StringBuffer xml = new StringBuffer();
		try {
			PermissionHelper helper = new PermissionHelper();
			ResourceHelper resHelper = new ResourceHelper();
			resHelper.setApplicationid(application);
			helper.setUser(user);
			ResourceVO res = null;
			if (StringUtil.isBlank(superid)) {
				res = resHelper.getTopResourceByName("Mobile");
				if (user!=null && user.getDefaultApplication()!=null && !user.getDefaultApplication().equals(application)) {
					UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
					process.doUpdateDefaultApplication(user.getId(), application);
					user.setDefaultApplication(application);
				}
			} else {
				res = resHelper.getResourcById(superid);
			}
			Collection<ResourceVO> resources = resHelper.searchResourceForMb(res, user.getDomainid());
			if (resources != null) {
				String deep = "1";
				sort(resources, helper, xml, deep, application, user);
			}
			xml.append(MbLoginHelper.pendingNumberXml(user));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml.toString();
	}
	
	/**
	 * 获取软件下得待办信息
	 * @param applicationid
	 * @return
	 */
	public String doGetPending(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", toPendingXML());
		}catch(Exception e){
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 将待办信息解析为XML格式
	 * @return
	 * @throws Exception
	 */
	public String toPendingXML() throws Exception{
		WebUser webUser = getUser();
		String userid = webUser.getId();
		UserDefinedProcess udprocss = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
					
		ParamsTable params1 = new ParamsTable();
		//获取当前用户自定义首页		
		params1.setParameter("t_applicationid", application);
		params1.setParameter("t_userId", userid);
		params1.setParameter("i_useddefined", UserDefined.IS_DEFINED);
		params1.setParameter("_orderby", "id");
		DataPackage<UserDefined> dataPackage=udprocss.doQuery(params1);
		if(dataPackage.rowCount > 0){
			UserDefined userDefined = new UserDefined();
			for(Iterator<UserDefined> ite1 = dataPackage.datas.iterator();ite1.hasNext();){
				userDefined = (UserDefined)ite1.next();
			}
			return createTemplateElement(application, userDefined,webUser);
		}else{
			//无自定义首页时,获取后台定制的默认首页
			Collection<RoleVO> userRoles = webUser.getRoles();
			RoleVO roleVO = new RoleVO();
			params1 = new ParamsTable();
			params1.setParameter("t_applicationid", application);
			params1.setParameter("n_published", true);
			params1.setParameter("_orderby", "id");
			DataPackage<UserDefined> dataPackage1=udprocss.doQuery(params1);
			if(dataPackage1.rowCount>0){
				for(Iterator<UserDefined> ite1 = dataPackage1.datas.iterator();ite1.hasNext();){
					UserDefined userDefined = (UserDefined)ite1.next();
					//判断是否适用于所有角色
					if("1".equals(userDefined.getDisplayTo())){
						return createTemplateElement(application, userDefined,webUser);
					}else{
						//获取某一首页的角色
						String roleIds = userDefined.getRoleIds();
						if(!StringUtil.isBlank(roleIds)){
							String[] userRoleIds = roleIds.split(",");
							for(int i=0;i<userRoleIds.length;i++){
								if(userRoles.size()>0){
									for(Iterator<RoleVO> ite2 = userRoles.iterator();ite2.hasNext();){
										roleVO = (RoleVO)ite2.next();
										if(userRoleIds[i].equals(roleVO.getId())){
											//当前角色与 后台首页待办设置的角色 相同时，返回此后台定制的首页待办信息
											return createTemplateElement(application, userDefined,webUser);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return "";
	}
	
	/**
	 * 拼装待办信息XML模板
	 * @param applicationid
	 * @param userDefined
	 * @param webUser
	 * @return
	 * @throws Exception
	 */
	public String createTemplateElement(String applicationid, UserDefined userDefined, WebUser webUser) throws Exception{
		StringBuffer xml = new StringBuffer();
		
		int page = 1; //总页数
		
		int tab1count = 0;
		int show1count = 0;
		int tab2count = 0;
		int show2count = 0;
		
		String templateElement = userDefined.getTemplateElement();
		if(!StringUtil.isBlank(templateElement) && templateElement.length() > 1){
			templateElement = templateElement.substring(1, templateElement.length() - 1);
			//获取各布局单元格和对应的元素
			String[] templateElements = (String[]) templateElement.split(",");
			for (int i = 0; i < templateElements.length; i++) {
				String[] templateElementSubs = (String[]) templateElements[i].split(":");
				if(!StringUtil.isBlank(templateElementSubs[0]) && templateElementSubs[0].length() > 1){
					//单元格摘要Id和title
					String[] summaryIds = templateElementSubs[1].split(";");
					String templateTdEle = summaryIds[0].substring(1, summaryIds[0].length() - 1);
					//摘要id数组
					summaryIds = templateTdEle.split("\\|");
					if(summaryIds.length == 1 && summaryIds[0].equals("")){
						continue;
					}
					for(int j = 0; j<summaryIds.length; j++){
						SummaryCfgVO summaryCfg = MbLoginHelper.summaryIdCheck(summaryIds[j]);
						if(summaryCfg == null){
							continue;
						}
						if(summaryCfg.getScope()==SummaryCfgVO.SCOPE_PENDING){//代办
							ParamsTable summaryCfgParams = new ParamsTable();
							summaryCfgParams.setParameter("formid", summaryCfg.getFormId());
							summaryCfgParams.setParameter("application", applicationid);
							summaryCfgParams.setParameter("_orderby", summaryCfg.getOrderby());
							PendingProcess pendingProcess = new PendingProcessBean(applicationid);
							long count = pendingProcess.conutByFilter(summaryCfgParams, webUser);
							tab1count += count;
							if(!pendingMore){
								page = ((int)(count / 10)) + 1;
							}
							for(long n=0;n<page;n++){
								summaryCfgParams.setParameter("_currpage",String.valueOf(n+1));
								DataPackage<PendingVO> pendings = pendingProcess.doQueryByFilter(summaryCfgParams, webUser);
								for (Iterator<PendingVO> iterator = pendings.datas.iterator(); iterator.hasNext();) {
									PendingVO pendingVO = (PendingVO) iterator.next();
									if( pendingMore && show1count==10){
										break;
									}
									if(pendingVO.getState() != null){
										xml.append(pendingVO.toMbXMLText(webUser,summaryCfg.getId(),summaryCfg.getTitle()));
										show1count++;
									}
								}
							}
								
						}else if(summaryCfg.getScope()==SummaryCfgVO.SCOPE_CIRCULATOR){//代阅
							ParamsTable circulatorParams = new ParamsTable();
							circulatorParams.setParameter("formid", summaryCfg.getFormId());
							circulatorParams.setParameter("application", applicationid);
							CirculatorProcess circulatorProcess = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class, applicationid);
							long count = circulatorProcess.conutByFilter(circulatorParams, webUser);
							tab1count += count;
							if(!pendingMore){
								page = (int) (count / 10) + 1;
							}
							for(int n=0;n<page;n++){
								circulatorParams.setParameter("_currpage", String.valueOf(n+1));
								DataPackage<Circulator> circulators = circulatorProcess.getPendingByUser(circulatorParams,webUser);
								tab2count += circulators.rowCount;
								for (Iterator<Circulator> iterator = circulators.datas.iterator(); iterator.hasNext();) {
									Circulator circulator = (Circulator) iterator.next();
									if(pendingMore && show2count==10){
										break;
									}
									xml.append(circulator.toMbXMLText(webUser, summaryCfg.getId(),summaryCfg.getTitle()));
									show2count++;
								}
							}
						}
					}
				}
			}
		}
		if((tab1count>10 || tab2count>10) && pendingMore){
			return "<"+MobileConstant.TAG_PENDINGS+" size='"+(show1count+show2count)+"'></"+MobileConstant.TAG_PENDINGS+">"+xml.toString()+"<"+MobileConstant.TAG_PENDING+" "+MobileConstant.ATT_ID+"='' "+MobileConstant.ATT_FORMID+"='' "+MobileConstant.ATT_SUMMARYCFGID+"=''>more...</"+MobileConstant.TAG_PENDING+">";
		}else{
			return "<"+MobileConstant.TAG_PENDINGS+" size='"+(show1count+show2count)+"'></"+MobileConstant.TAG_PENDINGS+">"+xml.toString();
		}
	}

	public boolean isPendingMore() {
		return pendingMore;
	}

	public void setPendingMore(boolean pendingMore) {
		this.pendingMore = pendingMore;
	}
	
	/**
	 * 获取用户站内短信
	 * @param applicationid
	 * @return
	 */
	public String doGetPersonalMessage(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("toXml", toPersonalMessageXML());
		}catch(Exception e){
			addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String toPersonalMessageXML() throws Exception{
		PersonalMessageProcess personalMensageProcess = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
		WebUser webUser = getUser();
		String userid = webUser.getId();
		ParamsTable params2 = new ParamsTable();
		params2.setParameter("_orderby", "sendDate");
		params2.setParameter("_desc", "desc");
		params2.removeParameter("domain");
		DataPackage<PersonalMessageVO> data2 = personalMensageProcess.doInbox(userid, params2);
		StringBuffer xml = new StringBuffer();
		int i = 0;
		for(Iterator<PersonalMessageVO> ite1 = data2.datas.iterator();ite1.hasNext();){
			if(personalMore && i==10){
				break;
			}
			PersonalMessageVO personal = ite1.next();
			xml.append(toPersonalXML(personal));
			i++;
		}
		if(personalMore && data2.datas.size() > 10){
			return "<"+MobileConstant.TAG_PERSONALMESSAGES + " size='"+data2.datas.size()+"'></"+MobileConstant.TAG_PERSONALMESSAGES+">"+xml.toString().replaceAll("<br>", "")+"<"+MobileConstant.TAG_PERSONALMESSAGE+" t='' content='' time='' sender='' receiver='' tp='' Read=''>more...</"+MobileConstant.TAG_PERSONALMESSAGE+">";
		}else{
			return "<"+MobileConstant.TAG_PERSONALMESSAGES + " size='"+data2.datas.size()+"'></"+MobileConstant.TAG_PERSONALMESSAGES+">"+xml.toString().replaceAll("<br>", "");
		}
	}
	
	public String toPersonalXML(PersonalMessageVO pm) throws Exception{
		StringBuffer sb = new StringBuffer();
		MessageBody mb = pm.getBody();
		MbLoginHelper mlh = new MbLoginHelper();
		PersonalMessageHelper pmh = new PersonalMessageHelper();
		//sb.append("<"+MobileConstant.TAG_PERSONALMESSAGE+" t='"+mb.getTitle()+"' content='"+MbLoginHelper.replaceHTML(mb.getContent())+"' time='"+pm.getSendDate()+"' sender='"+pmh.findUserName(pm.getSenderId())+"' receiver='"+pmh.findUserNamesByMsgIds(pm.getReceiverId())+"' tp='"+pmh.findDisplayText(pm.getType())+"' read='"+pm.isRead()+"'></"+MobileConstant.TAG_PERSONALMESSAGE+">"  );
		sb.append("<"+MobileConstant.TAG_PERSONALMESSAGE+" t='"+mb.getTitle()+"' content='"+mlh.replaceHTML(mb.getContent())+"' time='"+pm.getSendDate()+"' sender='"+pmh.findUserName(pm.getSenderId())+"' receiver='"+pmh.findUserNamesByMsgIds(pm.getReceiverId())+"' tp='"+pmh.findDisplayText(pm.getType())+"' read='"+pm.isRead()+"'></"+MobileConstant.TAG_PERSONALMESSAGE+">"  );
		return sb.toString();
	}
	
	public boolean isPersonalMore() {
		return personalMore;
	}

	public void setPersonalMore(boolean personalMore) {
		this.personalMore = personalMore;
	}
	
	private WebCookies getWebCooikes() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		if (webCookies == null) {
			webCookies = new WebCookies(request, response, WebCookies.ENCRYPTION_URL);
		}

		return webCookies;
	}
	
	private Cookie getErrorTimes() {
		WebCookies webCookies = getWebCooikes();
		Cookie pwdErrorTimes = webCookies.getCookie("pwdErrorTimes");

		return pwdErrorTimes;
	}
}
