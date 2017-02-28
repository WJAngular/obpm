package cn.myapps.core.homepage.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 首页HTML构建助手类
 * 
 * @author Happy
 * 
 */
public class HomePageHelper {
	/**
	 * 获取当前用户有权限使用的Widget集合
	 * 
	 * @param user
	 *            当前登录用户
	 * @return
	 */
	public Collection<PageWidget> getMyWidgets(WebUser user) {
		Collection<PageWidget> widgets = new ArrayList<PageWidget>();
		try {
			PageWidgetProcess process = (PageWidgetProcess) ProcessFactory
					.createProcess(PageWidgetProcess.class);
			// 根据传入的企业域id获取企业域下所有激活软件的已发布Widget集合
			Collection<PageWidget> list = process
					.doQueryPublishedWidgetsByDomainId(user.getDomainid());

			String[] userRoles = user.getRolelist().split(",");
			
			Collection<ApplicationVO> apps = new ApplicationHelper().getListByWebUser(user);
			String appStr = "";//用户有权限使用的软件id
			for(ApplicationVO app : apps){
				appStr+=app.getId()+";";
			}
			// 根据用户的角色筛选符合使用条件的Widget
			for (Iterator<PageWidget> iterator = list.iterator(); iterator
					.hasNext();) {
				PageWidget pageWidget = iterator.next();
				if (pageWidget.AUTH_MODE_ALL_ROLES == pageWidget.getAuthMode()) {// 授权给所有角色使用
					if(appStr.indexOf(pageWidget.getApplicationid())>=0){
						widgets.add(pageWidget);
					}
				} else {// 授权给选定角色使用
					String rolesId = pageWidget.getAuthRolesId();
					for (int i = 0; i < userRoles.length; i++) {
						String roleid = userRoles[i].replaceAll("'", "");
						if (rolesId != null && rolesId.indexOf(roleid) >= 0) {// 获取与当前用户角色匹配的Widget
							widgets.add(pageWidget);
							break;
						}
					}
				}
			}
			widgets.addAll(getSystemWidgets());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return widgets;
	}
	
	
	/**
	 * 添加系统Widget
	 * @return
	 */
	private Collection<PageWidget> getSystemWidgets() {
		Collection<PageWidget> widgets = new ArrayList<PageWidget>();
		widgets.add(PageWidget.newSystemWidget(PageWidget.TYPE_SYSTEM_ANNOUNCEMENT));
		widgets.add(PageWidget.newSystemWidget(PageWidget.TYPE_SYSTEM_WORKFLOW));
		widgets.add(PageWidget.newSystemWidget(PageWidget.TYPE_SYSTEM_WEATHER));
		return widgets;
	}

	public UserDefined getUserDefined(WebUser user) {
		UserDefinedProcess process;
		try {
			process = (UserDefinedProcess) ProcessFactory
					.createProcess(UserDefinedProcess.class);
			UserDefined vo = (UserDefined) process
					.doFindMyCustomUserDefined(user);
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建当前用户的首页，返回HTML字符串
	 * 
	 * @param request
	 * @param user
	 * @return
	 * @deprecated 目前除cool以为的皮肤都使用此方法显示首页。
	 */
	public String toHTML(HttpServletRequest request, WebUser user) {

		try {
			ParamsTable params = ParamsTable.convertHTTP(request);
			String applicationId = request
					.getParameter(Web.REQUEST_ATTRIBUTE_APPLICATION);

			UserDefined homePage = getMyHomePage(user, applicationId);

			if (homePage != null) {
				return homePage.toHtml(params, user);
			} else {
				return toNullHomePageHtml(request);
			}

		} catch (Exception e) {
			// TODO: 后续需添加处理逻辑 当发生运行时异常，需要展示什么样的页面！
			e.printStackTrace();
		}

		return "";

	}

	/**
	 * 获取当前用户适用的首页(UserDefined)对象
	 * 
	 * @param user
	 *            当前登录用户
	 * @param applicationId
	 *            软件id
	 * @return
	 * @throws Exception
	 */
	public UserDefined getMyHomePage(WebUser user, String applicationId)
			throws Exception {
		UserDefined myHomePage = null;

		UserDefinedProcess userDefinedProcess = (UserDefinedProcess) ProcessFactory
				.createProcess(UserDefinedProcess.class);
		// 1.首先获取当前用户自定义的首页
		myHomePage = userDefinedProcess.doFindMyCustomUserDefined(user);
		// 2.用户没有自定义的首页,就获取后台发布的首页
		if (myHomePage == null) {
			Collection<UserDefined> userDefineds = userDefinedProcess
					.doQueryPublishedUserDefinedsByApplication(applicationId);// 获取软件下后台发布的首页集合
			if (userDefineds == null)
				return null;
			// 获取当前用户可以使用的首页
			String[] userRoles = user.getRolelist().split(",");
			for (Iterator<UserDefined> iterator = userDefineds.iterator(); iterator
					.hasNext();) {
				UserDefined userDefined = (UserDefined) iterator.next();
				if ("1".equals(userDefined.getDisplayTo()))
					return userDefined;// 适用所有角色

				String userDefinedRoles = userDefined.getRoleIds();
				if (StringUtil.isBlank(userDefinedRoles)) {
					myHomePage = userDefined;
					break;
				}
				for (int i = 0; i < userRoles.length; i++) {
					if (userDefinedRoles.indexOf(userRoles[i]) >= 0) {// 获取与当前用户角色匹配的首页
						myHomePage = userDefined;
						break;
					}
				}
			}
		}

		return myHomePage;
	}

	/**
	 * 首页为空时，跳转到欢迎页面
	 * 
	 * @param request
	 * @return
	 * @deprecated 目前除cool以为的皮肤都使用此方法显示首页。
	 */
	private String toNullHomePageHtml(HttpServletRequest request) {
		StringBuffer html = new StringBuffer();
		html.append("<script language=\"javascript\">\n")
				.append("window.location='" + request.getContextPath()
						+ "/help/front/welcome/index.jsp'\n")
				.append("</script>");

		return html.toString();
	}

}
