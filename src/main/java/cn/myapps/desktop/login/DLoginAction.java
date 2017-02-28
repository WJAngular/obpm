package cn.myapps.desktop.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.permission.action.PermissionHelper;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.security.action.LoginHelper;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.UserDefinedHelper;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.WebCookies;
import cn.myapps.util.property.MultiLanguageProperty;

/**
 * 桌面应用登录Action
 * 
 * @author Tom
 * 
 */
public class DLoginAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	private String domainname;
	
	private WebCookies webCookies;

	private int updateState;
	private HttpServletRequest request;

	private static final int PENDING_UPDATE = 1, MENU_UPDATE = 2,
			CONTACT_UPDATE = 3;

	public static final String PENGING_LIST = "pengingList";
	public static final String CHANGEPENGINGLIST = "changepengingList";

	private List<String> pengingList = new ArrayList<String>();

	private static final Logger LOG = Logger.getLogger(DLoginAction.class);

	private boolean validateLogin() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		Object user = session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (user != null) {
			return true;
		}
		return false;
	}
	
	public String doLogin() throws Exception {
		Cookie pwdErrorTimes = getErrorTimes();
		int loginPasswordErrortimes = 0;
		if(pwdErrorTimes != null){
			loginPasswordErrortimes  = Integer.parseInt(pwdErrorTimes.getValue());
		}
		long time = System.currentTimeMillis();
		try {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			UserProcess process = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);

			ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
			
			UserVO user = process.login(username, password, domainname,loginPasswordErrortimes);

			if (user != null && user.getStatus() == 1) {
				WebUser webUser = new WebUser(user);

				ApplicationVO appvo = appProcess.getDefaultApplication(webUser
						.getDefaultApplication(), webUser);
				if (appvo == null) {
					appvo = (ApplicationVO) appProcess.queryByDomain(
							user.getDomainid()).iterator().next();

				}
				String application = appvo == null ? "" : appvo.getId();
				// webUser.setApplicationid(application);
				webUser.setDefaultApplication(application);

				UserVO vo = (UserVO) user.clone();
				vo.setDefaultApplication(application);
//				vo.setLoginpwd(null);
//				process.doUpdateWithCache(vo);

				session.setAttribute(Web.SESSION_ATTRIBUTE_DOMAIN, webUser
						.getDomainid());
				session.setAttribute(Web.SESSION_ATTRIBUTE_APPLICATION,
						application);
				DomainProcess process2 = (DomainProcess) ProcessFactory
						.createProcess(DomainProcess.class);
				DomainVO domainvo = (DomainVO) process2.doView(user
						.getDomainid());
				if(user.getUserSetup()!=null){
					session.setAttribute(Web.SKIN_TYPE,user.getUserSetup().getUserSkin());
				}else{
					//此用户为新建用户，usersetup表中没有此用户信息。
					session.setAttribute(Web.SKIN_TYPE,"default");
				}

				if (MultiLanguageProperty.getType(language) == 0)
					setLanguage(MultiLanguageProperty.getName(2));
				session.setAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE,
						language);
				// session.setMaxInactiveInterval(20 * 60); // 20 minutes

				session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
				session.setAttribute(Web.SESSION_ATTRIBUTE_LOGINBYAPP, Boolean
						.valueOf(true));
				// 初始化webuser
				LoginHelper.initWebUser(ServletActionContext.getRequest(),
						user, application, domainname);

				// 在环境中设置context path
				Environment.getInstance().setContextPath(
						ServletActionContext.getRequest().getContextPath());
				String toXml = DLoginXMLBuilder.toDesktopXML(webUser, getParams())
						+ getMenuXmlByApplication(webUser)
						+ DLoginXMLBuilder.toDesktopContactXML(webUser) + getUserInfoXml();
				if (toXml != null) {
					toXml = "<" + MobileConstant.TAG_MAIN + " "
							+ MobileConstant.ATT_SESSIONID + "='"
							+ session.getId() + "' "
							+ MobileConstant.ATT_SESSION_LIFT_CYCLE + "='"
							+ session.getMaxInactiveInterval() + "'>" + toXml
							+ "</" + MobileConstant.TAG_MAIN + ">";
					ServletActionContext.getRequest().setAttribute("toXml",
							toXml);
				}
				// System.out.println("login->:" + toXml);
			}
			ServletActionContext.getRequest().getSession().setAttribute(
					PENGING_LIST, pengingList);
			long end = System.currentTimeMillis();
			System.out.println("doLogin Waste Time: " + (end - time) + "(ms)");
		} catch (Exception e) {
			LOG.warn(e);
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			// System.out.println(e.toString());
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doRefreshWidget() throws Exception{
		try{
			String toXml = DLoginXMLBuilder.toDesktopXML(getUser(), getParams());
			ServletActionContext.getRequest().setAttribute("toXml",
					toXml);
		}catch(Exception e){
			LOG.warn(e);
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			// System.out.println(e.toString());
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doRefreshMessage() throws Exception{
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			DLoginHelper helper = new DLoginHelper(request, response);
			String toXml = helper.doQueryNewMessage(getUser(), getParams());
			ServletActionContext.getRequest().setAttribute("toXml",
					toXml);
		}catch(Exception e){
			LOG.warn(e);
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			// System.out.println(e.toString());
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doDomainList() throws Exception{
		StringBuffer sb = new StringBuffer();
		DomainProcess process = (DomainProcess)ProcessFactory
				.createProcess(DomainProcess.class);
		Collection<DomainVO> col = process.getDomainByStatus(1);
		if(col.size()>0){
			
			Iterator<DomainVO> it = col.iterator();
			while(it.hasNext()){
				sb.append("<D>");
				DomainVO vo = it.next();
				sb.append(vo.getName());
				sb.append("</D>");
			}
		}
		ServletActionContext.getRequest().setAttribute("toXml",sb.toString());
		return SUCCESS;
		
	}
	
	public String doRefreshApplication(){
		long time = System.currentTimeMillis();
		try {
			
			UserProcess process = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			HttpSession session = ServletActionContext.getRequest()
			.getSession();
			UserVO user = process.login(username);
			
			WebUser webUser = process.getWebUserInstance(user.getId());
			session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
				// webUser.setApplicationid(application);
				if (MultiLanguageProperty.getType(language) == 0)
					setLanguage(MultiLanguageProperty.getName(2));
				session.setAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE,
						language);
				// session.setMaxInactiveInterval(20 * 60); // 20 minutes

				// 在环境中设置context path
				Environment.getInstance().setContextPath(
						ServletActionContext.getRequest().getContextPath());
				String toXml =getMenuXmlByApplication(webUser);
				if (toXml != null) {
					toXml = toXml.replaceAll("<" + MobileConstant.TAG_APPLICATION + ">","<" + MobileConstant.TAG_REFRESHAPPLICATION + ">")
					.replaceAll("</" + MobileConstant.TAG_APPLICATION + ">", "</" + MobileConstant.TAG_REFRESHAPPLICATION + ">");
					ServletActionContext.getRequest().setAttribute("toXml",
							toXml);
				}
				// System.out.println("login->:" + toXml);
			long end = System.currentTimeMillis();
			System.out.println("doRefreshApplication Waste Time: " + (end - time) + "(ms)");
		} catch (Exception e) {
			LOG.warn(e);
			this.addFieldError("SystemError", e.getMessage());
			e.printStackTrace();
			// System.out.println(e.toString());
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * @deprecated 旧版本方法，已掉弃
	 */
	public String doChange() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		if (session == null
				|| session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER) == null) {
			this.addFieldError("SystemError", "服务器出现错误，请尝试重新登录。nologin");
			return ERROR;
		}
		try {
			DLoginHelper helper = new DLoginHelper(request, response);
			String toXml = helper.getChangePendingXml();
			request.setAttribute("toXml", toXml);
		} catch (Exception e) {
			LOG.warn(e);
			request.setAttribute("toXml", "");
		}
		// System.gc();
		return SUCCESS;
	}
	
	/**
	 * @deprecated 旧版本方法，已掉弃
	 */
	public String doChange2() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		if (session == null
				|| session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER) == null) {
			this.addFieldError("SystemError", "服务器出现错误，请尝试重新登录。nologin");
			return ERROR;
		}
		try {
			DLoginHelper helper = new DLoginHelper(request, response);
			String toXml = helper.getChangePendingXml();
			request.setAttribute("toXml", toXml);
		} catch (Exception e) {
			LOG.warn(e);
			request.setAttribute("toXml", "");
		}
		// System.gc();
		return SUCCESS;
	}

	private String getMenuXmlByApplication(WebUser user) {
		StringBuffer xml = new StringBuffer();
		if (user == null)
			return xml.toString();
		try {
			// DomainProcess process = (DomainProcess) ProcessFactory
			// .createProcess(DomainProcess.class);
			// DomainVO vo = (DomainVO) process.doView(user.getDomainid());
			Collection<ApplicationVO> apps = new ApplicationHelper()
					.getListByWebUser(user);// vo.getApplications();
			if (apps == null || apps.isEmpty()) {
				return xml.toString();
			}
			PermissionHelper pHelper = new PermissionHelper();
			xml.append("<" + MobileConstant.TAG_APPLICATION + ">");
			for (Iterator<ApplicationVO> it = apps.iterator(); it.hasNext();) {
				ApplicationVO app = it.next();

				if (app != null && !StringUtil.isBlank(app.getId())) {
					xml.append("<" + MobileConstant.TAG_MENU_POP + " ");
					xml.append(MobileConstant.ATT_ID + "='" + app.getId()
							+ "' ");
					// xml.append(MobileConstant.ATT_SRC + "='" +
					// app.getLogourl() + "' ");
					xml.append(MobileConstant.ATT_NAME + "='" + app.getName()
							+ "'>");
					Collection<ResourceVO> topMenus = get_topmenus(app.getId(),
							user.getDomainid());
					Collection<ResourceVO> temp = topMenus;

					ResourceProcess process1 = (ResourceProcess) ProcessFactory
							.createProcess(ResourceProcess.class);

					for (Iterator<ResourceVO> it1 = topMenus.iterator(); it1.hasNext();) {
						ResourceVO resvo = (ResourceVO) it1.next();
						if (resvo == null
								|| !pHelper.checkPermission(resvo, app.getId(),
										getUser())) {
							continue;
						}
						ParamsTable params = new ParamsTable();
						params.setParameter("_orderby", "orderno");
						params.setParameter("t_superior", resvo.getId());
						temp = process1.doSimpleQuery(params,app.getId());
//						temp = process1.doGetDatasByParent(resvo.getId());
//						temp = getSubMenus(resvo.getId(), app.getId(), getUser().getDomainid());
						if (temp != null && temp.size() == 0) {

							String url = resvo.toUrlString(getUser(),
									ServletActionContext.getRequest());

							xml.append("<" + MobileConstant.TAG_MENU_ITEM + " "
									+ MobileConstant.ATT_NAME + "='"
									+ resvo.getDescription() + "' ");
							// xml.append(MobileConstant.ATT_APPLICATIONID +
							// "='" + app.getId() + "' ");
							xml.append(MobileConstant.ATT_ORDER + "='"
									+ resvo.getOrderno() + "' ");
							xml.append(MobileConstant.ATT_URL + "='"
									+ HtmlEncoder.encode(url) + "'>");
							xml.append("</" + MobileConstant.TAG_MENU_ITEM
									+ ">");
						} else if (app != null && user != null) {
							String nextXml = menuRecursive(resvo, temp, app
									.getId(), user.getDomainid(), pHelper);
							if (nextXml != null) {
								xml.append(nextXml);
							}
						}
					}

					xml.append("</" + MobileConstant.TAG_MENU_POP + ">");
					try {
						PersistenceUtils.closeSessionAndConnection();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			xml.append("</" + MobileConstant.TAG_APPLICATION + ">");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xml.toString();
	}

	private String menuRecursive(ResourceVO resvo, Collection<ResourceVO> coll,
			String appId, String domainId, PermissionHelper pHelper)
			throws Exception {
		StringBuffer xml = new StringBuffer();
		
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		xml.append("<" + MobileConstant.TAG_MENU + " "
				+ MobileConstant.ATT_NAME + "='" + resvo.getDescription()
				+ "' ");
		xml.append(MobileConstant.ATT_ORDER + "='" + resvo.getOrderno() + "'>");
		for (Iterator<ResourceVO> it1 = coll.iterator(); it1.hasNext();) {
			ResourceVO resvo1 = (ResourceVO) it1.next();
			if (resvo1 == null
					|| !pHelper.checkPermission(resvo1, appId, getUser())) {
				continue;
			}
			ParamsTable params = new ParamsTable();
			params.setParameter("_orderby", "orderno");
			params.setParameter("t_superior", resvo1.getId());
			Collection<ResourceVO> subMenus = process.doSimpleQuery(params);
			
//			Collection<ResourceVO> subMenus = process.doGetDatasByParent(resvo1
//					.getId());
			
			if (subMenus != null && subMenus.size() > 0) {
				xml.append(menuRecursive(resvo1, subMenus, appId, domainId,
						pHelper));
			} else {
				String url = resvo1.toUrlString(getUser(), ServletActionContext
						.getRequest());

				xml.append("<" + MobileConstant.TAG_MENU_ITEM + " "
						+ MobileConstant.ATT_NAME + "='"
						+ resvo1.getDescription() + "' ");
				// xml.append(MobileConstant.ATT_APPLICATIONID+ "='" + appId +
				// "' ");
				xml.append(MobileConstant.ATT_ORDER + "='"
						+ resvo1.getOrderno() + "' ");
				xml.append(MobileConstant.ATT_URL + "='"
						+ HtmlEncoder.encode(url) + "'>");
				xml.append("</" + MobileConstant.TAG_MENU_ITEM + ">");
			}
		}
		xml.append("</" + MobileConstant.TAG_MENU + ">");
		return xml.toString();
	}

	private String getUserInfoXml() {
		try {
			WebUser user = getUser();
			StringBuffer sb = new StringBuffer();
			sb.append("<" + MobileConstant.TAG_USERINFO).append(" ");
			sb.append(MobileConstant.ATT_ID + "='" + user.getId() + "'")
					.append(" ");
			sb.append(MobileConstant.ATT_NAME + "='" + user.getName() + "'")
					.append(" ");
			sb
					.append(
							MobileConstant.ATT_DOMAIN + "='"
									+ user.getDomainid() + "'").append(" ");
			StringBuffer dep = new StringBuffer();
			Collection<DepartmentVO> deps = user.getDepartments();
			for (Iterator<DepartmentVO> it = deps.iterator(); it.hasNext();) {
				DepartmentVO vo = (DepartmentVO) it.next();
				if (vo != null) {
					if (dep.length() > 0) {
						dep.append(",");
					}
					dep.append(vo.getName());
				}
			}
			sb.append(MobileConstant.ATT_DEP + "='" + dep.toString() + "'")
					.append("></" + MobileConstant.TAG_USERINFO + ">");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getPendingXmlByApplication(WebUser user) {
		StringBuffer xml = new StringBuffer();
		xml.append("<" + MobileConstant.TAG_PENDING_LIST + ">");
		try {
			// HttpServletRequest request = ServletActionContext.getRequest();
			Collection<ApplicationVO> apps = new ApplicationHelper()
					.getListByWebUser(user);
			for (Iterator<ApplicationVO> it = apps.iterator(); it.hasNext();) {
				ApplicationVO app = (ApplicationVO) it.next();

				xml.append("<" + MobileConstant.TAG_PENDING_GROUP + " ");
				xml.append(MobileConstant.ATT_ID + "='" + app.getId() + "' ");
				xml.append(MobileConstant.ATT_NAME + "='" + app.getName()
						+ "'>");

				try {
					UserDefinedHelper hph = new UserDefinedHelper();
					hph.setApplicationid(app.getId());
					Collection<SummaryCfgVO> cfgs = hph.getUserSummaryCfg(user);
					if (cfgs == null || cfgs.isEmpty()) {
						xml.append("</" + MobileConstant.TAG_PENDING_GROUP
								+ ">");
						continue;
					}

					PendingProcess pProcess = (PendingProcess) ProcessFactory
							.createRuntimeProcess(PendingProcess.class, app
									.getId());
					ParamsTable params = new ParamsTable();

					for (Iterator<SummaryCfgVO> it3 = cfgs.iterator(); it3
							.hasNext();) {
						SummaryCfgVO cfg = it3.next();
						params.setParameter("formid", cfg.getFormId());
						params.setParameter("_orderby", cfg.getOrderby());
						params.setParameter("_pagelines", Integer.MAX_VALUE
								+ "");
						DataPackage<PendingVO> datas = pProcess
								.doQueryByFilter(params, user);

						Collection<PendingVO> list = new ArrayList<PendingVO>();
						if (datas != null && datas.datas != null) {
							list = datas.datas;
						}
						for (Iterator<PendingVO> it2 = list.iterator(); it2
								.hasNext();) {
							PendingVO pending = (PendingVO) it2.next();
							xml.append("<" + MobileConstant.TAG_PENDING_ITEM
									+ " ");

							xml.append(MobileConstant.ATT_ID + "='"
									+ pending.getDocId() + "' ");
							// xml.append(MobileConstant.ATT_URL + "='" + url +
							// "' ");

							xml.append(MobileConstant.ATT_FORMID + "='"
									+ pending.getFormid() + "'>");
							pengingList.add(pending.getId());
							xml.append("[" + cfg.getTitle() + "] "
									+ pending.getSummary());
							xml.append("</" + MobileConstant.TAG_PENDING_ITEM
									+ ">");

						}
					}
				} catch (Exception e) {
					LOG.warn(e);
				}

				xml.append("</" + MobileConstant.TAG_PENDING_GROUP + ">");
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
					LOG.warn(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		xml.append("</" + MobileConstant.TAG_PENDING_LIST + ">");
		return xml.toString();
	}
	
	private String getPendingXmlByApplication2(WebUser user) throws Exception {
		StringBuffer xml = new StringBuffer();
		WebUser webUser = getUser();
		String userid = webUser.getId();
		xml.append("<" + MobileConstant.TAG_PENDING_LIST + ">");
		try {
			// HttpServletRequest request = ServletActionContext.getRequest();
			Collection<ApplicationVO> apps = new ApplicationHelper()
					.getListByWebUser(user);
			for (Iterator<ApplicationVO> it = apps.iterator(); it.hasNext();) {
				ApplicationVO app = (ApplicationVO) it.next();

				xml.append("<" + MobileConstant.TAG_PENDING_GROUP + " ");
				xml.append(MobileConstant.ATT_ID + "='" + app.getId() + "' ");
				xml.append(MobileConstant.ATT_NAME + "='" + app.getName()
						+ "'>");
				UserDefinedProcess udprocss = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
				try {
					ParamsTable params1 = new ParamsTable();
					//获取当前用户自定义首页		
					params1.setParameter("t_applicationid", app.getId());
					params1.setParameter("t_userId", userid);
					params1.setParameter("i_useddefined", UserDefined.IS_DEFINED);
					params1.setParameter("_orderby", "id");
					DataPackage<UserDefined> dataPackage=udprocss.doQuery(params1);
					if(dataPackage.rowCount >0){
						UserDefined userDefined = new UserDefined();
						for(Iterator<UserDefined> ite1 = dataPackage.datas.iterator();ite1.hasNext();){
							userDefined = (UserDefined)ite1.next();
						}
						xml.append(createTemplateElement(app.getId(), userDefined,webUser));
					}else{//无自定义首页时,获取后台定制的默认首页
						Collection<RoleVO> userRoles = user.getRoles();
						RoleVO roleVO = new RoleVO();
						params1 = new ParamsTable();
						params1.setParameter("t_applicationid", app.getId());
						params1.setParameter("n_published", true);
						params1.setParameter("_orderby", "id");
						DataPackage<UserDefined> dataPackage1=udprocss.doQuery(params1);
						if(dataPackage1.rowCount>0){
							for(Iterator<UserDefined> ite1 = dataPackage1.datas.iterator();ite1.hasNext();){
								UserDefined userDefined = (UserDefined)ite1.next();
								//判断是否适用于所有角色
								if("1".equals(userDefined.getDisplayTo())){
									xml.append(createTemplateElement(app.getId(), userDefined,webUser));
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
														xml.append(createTemplateElement(app.getId(), userDefined,webUser));
													}
												}
											}
										}
									}
								}
							}
						}
						
					}
				}catch (Exception e) {
					LOG.warn(e);
				}
				xml.append("</" + MobileConstant.TAG_PENDING_GROUP + ">");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			PersistenceUtils.closeSessionAndConnection();
		} catch (Exception e) {
			LOG.warn(e);
		}
		xml.append("</" + MobileConstant.TAG_PENDING_LIST + ">");
		return xml.toString();
	}

	

	private String getUserListXml(String domain) throws Exception {
		StringBuffer xml = new StringBuffer();
		DepartmentProcess dp = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		DepartmentVO root = dp.getRootDepartmentByApplication("", domain);
		if (root == null) {
			return xml.toString();
		}
		xml.append("<" + MobileConstant.TAG_CONTACT + ">");
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		ParamsTable table = new ParamsTable();
		table.setParameter("sm_userDepartmentSets.departmentId", root.getId());
		table.setParameter("_pagelines", Integer.MAX_VALUE);
		DataPackage<UserVO> users = up.doQuery(table);

		xml.append("<" + MobileConstant.TAG_DEP + " " + MobileConstant.ATT_NAME
				+ "='" + root.getName() + "' " + MobileConstant.ATT_CODE + "='"
				+ root.getCode() + "'>");
		if (users.datas != null && !users.datas.isEmpty()) {
			for (Iterator<UserVO> it_user = users.datas.iterator(); it_user
					.hasNext();) {
				UserVO uservo = (UserVO) it_user.next();
				xml.append("<" + MobileConstant.TAG_CONTACT_USER + " "
						+ MobileConstant.ATT_ID + "='" + uservo.getId() + "'>");
				xml.append(uservo.getName());
				xml.append("</" + MobileConstant.TAG_CONTACT_USER + ">");
			}
		}
		// 获取顶部门下的其他部门信息
		xml.append(getUserListByDep(root, domain));
		xml.append("</" + MobileConstant.TAG_DEP + ">");

		xml.append("</" + MobileConstant.TAG_CONTACT + ">");
		// System.out.println(xml.toString());
		return xml.toString();
	}

	private String getUserListByDep(DepartmentVO depvo, String domain)
			throws Exception {
		StringBuffer xml = new StringBuffer();
		DepartmentProcess dp = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
		Collection<DepartmentVO> deps = dp.getDatasByParent(depvo.getId());
		if (deps != null && !deps.isEmpty()) {
			for (Iterator<DepartmentVO> it = deps.iterator(); it.hasNext();) {
				DepartmentVO dep = (DepartmentVO) it.next();
				xml
						.append("<" + MobileConstant.TAG_DEP + " "
								+ MobileConstant.ATT_NAME + "='"
								+ dep.getName() + "' "
								+ MobileConstant.ATT_CODE + "='"
								+ dep.getCode() + "'>");
				xml.append(getUserListByDep(dep, domain));
				UserProcess up = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				ParamsTable table = new ParamsTable();
				table.setParameter("sm_userDepartmentSets.departmentId", dep
						.getId());
				table.setParameter("_pagelines", Integer.MAX_VALUE);
				DataPackage<UserVO> users = up.doQuery(table);
				for (Iterator<UserVO> it_user = users.datas.iterator(); it_user
						.hasNext();) {
					UserVO uservo = (UserVO) it_user.next();
					xml.append("<" + MobileConstant.TAG_CONTACT_USER + " "
							+ MobileConstant.ATT_ID + "='" + uservo.getId()
							+ "'>");
					xml.append(uservo.getName());
					xml.append("</" + MobileConstant.TAG_CONTACT_USER + ">");
				}
				xml.append("</" + MobileConstant.TAG_DEP + ">");
			}
		}
		return xml.toString();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomainname() {
		return domainname;
	}

	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}

	public int getUpdateState() {
		return updateState;
	}

	public void setUpdateState(int updateState) {
		this.updateState = updateState;
	}

	private String language = "CN";

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public WebUser getUser() throws Exception {
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = null;
		if (session == null
				|| session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER) == null) {
			UserVO vo = new UserVO();
			vo.getId();
			vo.setName("GUEST");
			vo.setLoginno("guest");
			vo.setLoginpwd("");
			vo.setRoles(null);
			vo.setEmail("");
			// vo.setLanguageType(1);
			user = new WebUser(vo);
		} else {
			user = (WebUser) session
					.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		}
		return user;
	}

	public String getApplicationId() {
		return (String) getContext().getSession().get("APPLICATION");
	}

	public static ActionContext getContext() {
		return ActionContext.getContext();
	}

	/**
	 * Retrieve the top menus.
	 * 
	 * @return Returns the top menus collection.
	 * @throws Exception
	 */
	public Collection<ResourceVO> get_topmenus(String application,
			String domain, ParamsTable params) throws Exception {
		return getSubMenus(null, application, domain, params);
	}

	public Collection<ResourceVO> get_topmenus(String application, String domain)
			throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_MOBILE);
		return get_topmenus(application, domain, params);
	}

	/**
	 * Retrieve the sub menus (get the parent menu id from the web paramenter).
	 * 
	 * @return The sub menus collection.
	 * @throws Exception
	 */
	public Collection<ResourceVO> get_submenus(String application,
			String domain, ParamsTable params) throws Exception {
		Collection<ResourceVO> menus = null;
		try {
			ResourceProcess process = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			String _pid = ServletActionContext.getRequest().getParameter(
					"_parent");
			ResourceVO parent = (ResourceVO) process.doView(_pid);

			// if (parent != null) {
			menus = getSubMenus(parent, application, domain, params);
			// parent.setSuperiorid("-1");
			// menus.add(parent);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return menus;
	}

	/**
	 * Retrieve the sub menus
	 * 
	 * @param startNode
	 *            The parent menu.
	 * @return The sub menus under the parment menu.
	 * @throws Exception
	 */
	public Collection<ResourceVO> getSubMenus(ResourceVO startNode,
			String application) throws Exception {
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		ArrayList<ResourceVO> list = new ArrayList<ResourceVO>();
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderby", "orderno");
		params.setParameter("application", application);
		Collection<ResourceVO> cols = process
				.doSimpleQuery(params, application);

		Iterator<ResourceVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ResourceVO vo = (ResourceVO) iter.next();
			if (startNode == null) {
				if (vo.getSuperior() == null) {
					list.add(vo);
				}
			} else {
				if (vo.getSuperior() != null) {
					ResourceVO superior = vo.getSuperior();
					while (superior != null) {
						if (superior.getId().equals(startNode.getId())) {
							list.add(vo);
							break;
						}
						superior = superior.getSuperior();
					}
				}
			}
		}

		return list;
	}

	public Collection<ResourceVO> getSubMenus(String startNodeid,
			String application, String domain) throws Exception {
		ArrayList<ResourceVO> list = new ArrayList<ResourceVO>();
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderby", "orderno");
		params.setParameter("application", application);
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		Collection<ResourceVO> cols = process
				.doSimpleQuery(params, application);

		Iterator<ResourceVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ResourceVO vo = (ResourceVO) iter.next();
			if (startNodeid == null) {
				if (vo.getSuperior() == null
						) {
					list.add(vo);
				}
			} else {
				if (vo.getSuperior() != null) {
					ResourceVO superior = vo.getSuperior();
					while (superior != null) {
						if (superior.getId().equals(startNodeid)) {
							list.add(vo);
							break;
						}
						superior = superior.getSuperior();
					}
				}
			}
		}

		return list;
	}

	public Collection<ResourceVO> getSubMenus(ResourceVO startNode,
			String application, String domain, ParamsTable params)
			throws Exception {
		ArrayList<ResourceVO> list = new ArrayList<ResourceVO>();

		params.setParameter("_orderby", "orderno");
		params.setParameter("application", application);
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		Collection<ResourceVO> cols = process
				.doSimpleQuery(params, application);

		Iterator<ResourceVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ResourceVO vo = (ResourceVO) iter.next();
			if (startNode == null) {
				if (vo.getSuperior() == null
						) {
					list.add(vo);
				}
			} else {
				if (vo.getSuperior() != null) {
					ResourceVO superior = vo.getSuperior();
					while (superior != null) {
						if (superior.getId().equals(startNode.getId())) {
							list.add(vo);
							break;
						}
						superior = superior.getSuperior();
					}
				}
			}
		}

		return list;
	}

	// private String getString(String str) {
	// if (StringUtil.isBlank(str))
	// return "";
	// return str;
	// }

	public String doLogout() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		Object object = session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (object != null) {
			session.removeAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		}
		session.invalidate();
		ServletActionContext.getRequest().setAttribute("toXml", "");
		return SUCCESS;
	}

	public String doRefreshUser() {
		try {
			WebUser user = getUser();
			if (user != null) {
				String domain = user.getDomainid();
				if (!StringUtil.isBlank(domain)) {
					String xml = getUserListXml(domain);
					ServletActionContext.getRequest().setAttribute("toXml",
							xml == null ? "" : xml);
				} else {
					LOG.warn("Can't find domain id! " + user.getName());
				}
			} else {
				LOG.warn("User can't login!");
			}
		} catch (Exception e) {
			LOG.warn(e);
		}
		return SUCCESS;
	}

	/**
	 * update request
	 * 
	 * 更新请求
	 * 
	 * @return
	 * @throws Exception
	 */
	public String update() {
		try {
			WebUser webUser = getUser();
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			StringBuffer xml = new StringBuffer();
			if (updateState == PENDING_UPDATE) {
				xml.append("<" + MobileConstant.TAG_MAIN + " "
						+ MobileConstant.ATT_SESSIONID + "='" + session.getId()
						//添加SESSION_LIFT_CYCLE的值
						+ "' " +MobileConstant.ATT_SESSION_LIFT_CYCLE + "='"
							+ session.getMaxInactiveInterval()
						+ "'>");
				xml.append(getPendingXmlByApplication2(webUser));
				xml.append("</" + MobileConstant.TAG_MAIN + ">");
			} else if (updateState == MENU_UPDATE) {
				xml.append("<" + MobileConstant.TAG_MAIN + " "
						+ MobileConstant.ATT_SESSIONID + "='" + session.getId()
						+ "'>");
				xml.append(getMenuXmlByApplication(webUser));
				xml.append("</" + MobileConstant.TAG_MAIN + ">");

			} else if (updateState == CONTACT_UPDATE) {
				xml.append("<" + MobileConstant.TAG_MAIN + " "
						+ MobileConstant.ATT_SESSIONID + "='" + session.getId()
						+ "'>");
				xml.append(getUserListXml(webUser.getDomainid()));
				xml.append("</" + MobileConstant.TAG_MAIN + ">");
			} else if (updateState == (PENDING_UPDATE + MENU_UPDATE)) {
				xml.append("<" + MobileConstant.TAG_MAIN + " "
						+ MobileConstant.ATT_SESSIONID + "='" + session.getId()
						+ "'>");
				xml.append(getPendingXmlByApplication2(webUser));
				xml.append(getMenuXmlByApplication(webUser));
				xml.append("</" + MobileConstant.TAG_MAIN + ">");
			} else if (updateState == (PENDING_UPDATE + CONTACT_UPDATE)) {
				xml.append("<" + MobileConstant.TAG_MAIN + " "
						+ MobileConstant.ATT_SESSIONID + "='" + session.getId()
						+ "'>");
				xml.append(getPendingXmlByApplication2(webUser));
				xml.append(getUserListXml(webUser.getDomainid()));
				xml.append("</" + MobileConstant.TAG_MAIN + ">");
			} else if (updateState == (CONTACT_UPDATE + MENU_UPDATE)) {
				xml.append("<" + MobileConstant.TAG_MAIN + " "
						+ MobileConstant.ATT_SESSIONID + "='" + session.getId()
						+ "'>");
				xml.append(getMenuXmlByApplication(webUser));
				xml.append(getUserListXml(webUser.getDomainid()));
				xml.append("</" + MobileConstant.TAG_MAIN + ">");
			} else if (updateState == (PENDING_UPDATE + MENU_UPDATE + CONTACT_UPDATE)) {
				xml.append("<" + MobileConstant.TAG_MAIN + " "
						+ MobileConstant.ATT_SESSIONID + "='" + session.getId()
						+ "'>");
				xml.append(getPendingXmlByApplication2(webUser));
				xml.append(getMenuXmlByApplication(webUser));
				xml.append(getUserListXml(webUser.getDomainid()));
				xml.append("</" + MobileConstant.TAG_MAIN + ">");
			}
			ServletActionContext.getRequest().setAttribute("toXml",
					xml.toString());
		} catch (Exception e) {
			LOG.warn(e);
			this.addFieldError("SystemError", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}


/**
 * 查询摘要
 * 有摘要则返回此摘要对象
 * 否则返回null
 * @param summaryid
 * @author jack
 * @return summaryCfg
 * @throws Exception
 */
	public static SummaryCfgVO summaryIdCheck(String summaryid) throws ClassNotFoundException{
		SummaryCfgVO summaryCfg = null;
		try {
			SummaryCfgProcess summaryCfgPro=(SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
			if(!StringUtil.isBlank(summaryid))
				summaryCfg = (SummaryCfgVO) summaryCfgPro.doView(summaryid);
			if(summaryCfg == null){//兼容旧数据
				PageWidgetProcess process = (PageWidgetProcess)ProcessFactory.createProcess(PageWidgetProcess.class);
				PageWidget widget = (PageWidget)process.doView(summaryid);
				if(widget !=null && PageWidget.TYPE_SUMMARY.equals(widget.getType())){
					summaryCfg = (SummaryCfgVO)summaryCfgPro.doView(widget.getActionContent());
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return summaryCfg;
	}

	private String createTemplateElement(String id, UserDefined userDefined,WebUser user) throws Exception {
		String templateElement = userDefined.getTemplateElement();
		StringBuffer xml = new StringBuffer();
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
						SummaryCfgVO summaryCfg = summaryIdCheck(summaryIds[j]);
						if(summaryCfg == null){
							continue;
						}
						if(summaryCfg.getScope()==SummaryCfgVO.SCOPE_PENDING){//代办
							ParamsTable summaryCfgParams = new ParamsTable();
							summaryCfgParams.setParameter("formid", summaryCfg.getFormId());
							summaryCfgParams.setParameter("application", id);
							summaryCfgParams.setParameter("_orderby", summaryCfg.getOrderby());
							PendingProcess pendingProcess = new PendingProcessBean(id);
							DataPackage<PendingVO> pendings = pendingProcess.doQueryByFilter(summaryCfgParams, user);
							for (Iterator<PendingVO> iterator = pendings.datas.iterator(); iterator.hasNext();) {
								PendingVO pendingVO = (PendingVO) iterator.next();
                                //判断代办实体有没有流程对象,如没有流程对象，则一定是垃圾数据
                                if (pendingVO.getState() == null) continue;
								xml.append("<" + MobileConstant.TAG_PENDING_ITEM
										+ " ");
	
								xml.append(MobileConstant.ATT_ID + "='"
										+ pendingVO.getDocId() + "' ");
								
								String isread = "false";
								Collection<ActorRT> actors = pendingVO.getState().getActors();
								for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
									ActorRT actor = (ActorRT) iter.next();
									if (user.getId().equals(actor.getActorid()) && actor.getIsread()) {
										isread = "true";
										break;
									}
								}
								
								xml.append("isRead='"
										+ isread + "' ");
	
								xml.append(MobileConstant.ATT_FORMID + "='"
										+ pendingVO.getFormid() + "'>");
								pengingList.add(pendingVO.getId());
								xml.append("[" + summaryCfg.getTitle() + "] "
										+ pendingVO.getSummary());
								xml.append("</" + MobileConstant.TAG_PENDING_ITEM
										+ ">");
							}
						}else if(summaryCfg.getScope()==SummaryCfgVO.SCOPE_CIRCULATOR){//代阅
							ParamsTable circulatorParams = new ParamsTable();
							circulatorParams.setParameter("formid", summaryCfg.getFormId());
							circulatorParams.setParameter("application", id);
							CirculatorProcess circulatorProcess = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class, id);
							DataPackage<Circulator> circulators = circulatorProcess.getPendingByUser(circulatorParams,user);
							for (Iterator<Circulator> iterator = circulators.datas.iterator(); iterator.hasNext();) {
								Circulator circulator = (Circulator) iterator.next();
								xml.append("<" + MobileConstant.TAG_PENDING_ITEM
										+ " ");
	
								xml.append(MobileConstant.ATT_ID + "='"
										+ circulator.getId() + "' ");
	
								xml.append(MobileConstant.ATT_FORMID + "='"
										+ circulator.getFormId() + "'>");
								pengingList.add(circulator.getId());
								xml.append("[" + summaryCfg.getTitle() + "] "
										+ circulator.getSummary());
								xml.append("</" + MobileConstant.TAG_PENDING_ITEM
										+ ">");
							}
						}
					}
				}
			}
		}
		return xml.toString();
	}
	
	private void sort(Collection<ResourceVO> temp){
		Collections.sort((ArrayList<ResourceVO>)temp, new Comparator<ResourceVO>() {
			public int compare(ResourceVO o1, ResourceVO o2) {
				String value1 = null;
				String value2 = null;
					value1 = String.valueOf(o1.getOrderno());
					value2 = String.valueOf(o2.getOrderno());
				if (value1 instanceof String && value2 instanceof String) {
					String s1 = (String) value1;
					String s2 = (String) value2;
					if (s1.compareTo(s2) > 1)
						return 1;
					if (s1.compareTo(s2) < 1)
						return -1;
				}
				return 0;
			}
		});
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
	
	/**
     * 获取摘要待办
     * 
     * @param widget 摘要工具
     * @param user  用户
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static Collection<PendingVO> getSummaryPendVO(PageWidget widget,WebUser user,ParamsTable params) throws Exception{
    	String applicationId = widget.getApplicationid();
    	SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
				.createProcess(SummaryCfgProcess.class);
		params.removeParameter("_pagelines");

		String summaryCfgId = widget.getActionContent();
		SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess
				.doView(summaryCfgId);

		if (summaryCfg != null) {
			params.setParameter("formid", summaryCfg.getFormId());
			params.setParameter("_orderby", summaryCfg.getOrderby());
			PendingProcess process = new PendingProcessBean(
					applicationId);
			DataPackage<PendingVO> result = process.doQueryByFilter(
					params, user);
			return result.getDatas();
		}
		return null;
    }
    
    /**
	 * Get the Parameters table
	 * 
	 * @return ParamsTable
	 */
	public ParamsTable getParams() {
		ParamsTable params = null;
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null)
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}

		return params;
	}
	
}
