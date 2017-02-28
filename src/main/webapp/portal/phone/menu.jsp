<%@page import="cn.myapps.util.StringUtil"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>

<%@page import="cn.myapps.core.resource.action.ResourceAction"%>
<%@page import="cn.myapps.core.resource.ejb.ResourceVO"%>
<%@page import="cn.myapps.core.resource.action.ResourceHelper"%>
<%@page import="cn.myapps.core.deploy.application.action.ApplicationHelper"%>
<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@page import="java.util.*"%>

<%@page import="cn.myapps.core.permission.action.PermissionHelper"%>
<%@page import="cn.myapps.util.OBPMDispatcher"%>
<%@ taglib uri="myapps" prefix="o"%>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<%
	String contextPath = request.getContextPath();
	String closeUrlStr = new OBPMDispatcher().getDispatchURL("../../../portal/dispatch/closeTab.jsp",request,response);
%>

<div class="launch">
	<!-- <div class="app_Title"></div> -->
	<div class="app_List">
		<div class="top_menu_box clearfix"></div>
		<div class="second_menu_box clearfix" style="display:none"></div>
		<div class="third_menu_box clearfix" style="display:none"></div>
		<div class="noApp">无菜单内容</div>
	</div>
		<%
			ApplicationHelper ah = new ApplicationHelper();
			WebUser user = (WebUser) session.getAttribute("FRONT_USER");
			Collection<ApplicationVO> appList = ah.getListByDomainId(user.getDomainid());

			String applicationId = request.getParameter("application");
			if(StringUtil.isBlank(applicationId) || "null".equals(applicationId)){
				applicationId = user.getDefaultApplication();
			}
			for (ApplicationVO applicationVO : appList) {
                
				if (!applicationVO.isActivated() || !applicationVO.getId().equals(applicationId)){
					continue;
				}
					
				out.print("<dl class='menu_dl'><dt class='app'>"
						+ "<span class='appTitle'>"
						+ applicationVO.getName() + "</span></dt>"
						+ "<dd class='menu'>\n");
				ResourceAction resource = new ResourceAction();

				ResourceHelper resourceHelper = new ResourceHelper();

				PermissionHelper permissionHelper = new PermissionHelper();
				
				Collection<ResourceVO> topMenus = resource.get_topmenus4Mobile(
						applicationId, user.getDomainid());
				
				StringBuffer topMenuHtml = new StringBuffer();

				for (ResourceVO topMenu : topMenus) {
					Collection<ResourceVO> secondMenus = resourceHelper
							.searchMobileSubResource(topMenu.getId(), 1,
									user.getDomainid());
					if (permissionHelper.checkPermission(topMenu,
							applicationId, user)) {
						String desc1 = topMenu.getDescription();
						String mul1 = topMenu.getMultiLanguageLabel();
						if ((mul1== null || mul1.length()==0) && desc1 != null && desc1.length() > 8) {
							//desc1 = desc1.substring(0, 6) + ".."
							//		+ desc1.charAt(desc1.length() - 1);
						}
						topMenuHtml.append(
								"<li id='" + topMenu.getId() + "'_href='"
										+ topMenu.toUrlString(user, request)
										+ "' class='topMenuItem'>")
								.append("<span class='topMenu_title'>"+desc1+"</span>");

						StringBuffer secondMenuHtml = new StringBuffer();
						for (ResourceVO secondMenu : secondMenus) {
							boolean isPermission = permissionHelper
									.checkPermission(secondMenu, applicationId,
											user);

							if (permissionHelper.checkPermission(secondMenu,
									applicationId, user)) {
								String desc2 = secondMenu.getDescription();
								String mul2=secondMenu.getMultiLanguageLabel();
								if ((mul2== null || mul2.length()==0) && desc2 != null && desc2.length() > 8) {
									//desc2 = desc2.substring(0, 6) + ".."
									//		+ desc2.charAt(desc2.length() - 1);
								}
								secondMenuHtml.append(
										"<li id='"
												+ secondMenu.getId()
												+ "' _href='"
												+ secondMenu.toUrlString(user,
														request) + "' _type='"+ secondMenu.getLinkType() +"' class='secondMenuItem'>")
										.append("<a class='second_title'>"+desc2+"</a>"
												+ "\n");

								Collection<ResourceVO> thirdMenus = resourceHelper
										.searchSubResource(secondMenu.getId(),
												1, user.getDomainid());

								StringBuffer thirdMenuHtml = new StringBuffer();
								for (ResourceVO thirdMenu : thirdMenus) {
									if (permissionHelper.checkPermission(
											thirdMenu, applicationId, user)) {

										String desc3 = thirdMenu.getDescription();
										String mul3=thirdMenu.getMultiLanguageLabel();
										if ((mul3== null || mul3.length()==0) && desc3 != null && desc3.length() > 8) {
											//desc3 = desc3.substring(0, 6) + ".."
											//		+ desc3.charAt(desc3.length() - 1);
										}
										
										thirdMenuHtml
												.append("<li id='"
														+ thirdMenu.getId()
														+ "' _href='"
														+ thirdMenu
																.toUrlString(
																		user,
																		request)
														+ "' _type='"+ thirdMenu.getLinkType() +"' class='thirdMenuItem'>")
												.append("<a class='third_title'>"+desc3+"</a>")
												.append("</li>\n");
									}
								}

								if (thirdMenuHtml.length() > 0) {
									secondMenuHtml
											.append("<ul class='thirdMenu'>")
											.append(thirdMenuHtml)
											.append("</ul>\n");
								}

								secondMenuHtml.append("</li>\n");
							}

						}

						if (secondMenuHtml.length() > 0) {
							topMenuHtml.append("<ul class='secondMenu'>")
									.append(secondMenuHtml).append("</ul>\n");
						}

						topMenuHtml.append("</li>\n");
					}
				}

				out.println("<ul class='topMenu'>" + topMenuHtml + "</ul>");
				out.println("</dd></dl>");
			}
		%>
	</div>
</o:MultiLanguage>
