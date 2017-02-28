<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="myapps" prefix="o"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="cn.myapps.core.resource.action.ResourceAction"%>
<%@ page import="cn.myapps.core.resource.ejb.ResourceVO"%>
<%@ page import="cn.myapps.core.resource.action.ResourceHelper"%>
<%@ page import="cn.myapps.core.deploy.application.action.ApplicationHelper"%>
<%@ page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@ page import="cn.myapps.core.deploy.application.ejb.ApplicationProcess"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.myapps.core.permission.action.PermissionHelper"%>
<%@ page import="cn.myapps.util.OBPMDispatcher"%>
<%@ page import="cn.myapps.util.ProcessFactory"%>

<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<%
	String contextPath = request.getContextPath();
	String closeUrlStr = new OBPMDispatcher().getDispatchURL("../../../portal/dispatch/closeTab.jsp",request,response);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<link rel="stylesheet" href="resource/css/bootstrap.min.css" />
<link rel="stylesheet" href="resource/css/myapp.css" />
<link rel="stylesheet" href="resource/css/launch.css" />
<script src="resource/script/jquery.min.js"></script>
<script src="resource/script/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		var $apps = $("dl.menu_dl");
		var appSize = $apps.size();
		
		$apps.each(function(){
			
			var $appBoxId = $(this).attr("id");
			
			$(".launch-tab").append("<li>"
									+"<a id='"+ $appBoxId +"_title' class='app_Title' href='#"+ $appBoxId+"_List' role='tab' data-toggle='tab'></a></li>"
									);		
			$(".launch-tab-content").append("<div id='"+ $appBoxId +"_List' class='app_List tab-pane'>"
					+ "<div class='top_menu_box clearfix'></div>"
					+ "<div class='second_menu_box clearfix' style='display:none'></div>"
					+ "<div class='third_menu_box clearfix' style='display:none'></div></div>");		
					
			var $appTitle = $(this).find(".appTitle");
			var $topBox = $("#"+$appBoxId+"_List>.top_menu_box");
			var $secondBox = $("#"+$appBoxId+"_List>.second_menu_box");
			var $thirdBox = $("#"+$appBoxId+"_List>.third_menu_box");
			
			$("#"+ $appBoxId +"_title").html("<div class='app_Title_Div'>" + $appTitle.text() + "</div>");
			
			var $topLis = $(this).find("li.topMenuItem");
			if($topLis.size() == 0){
				appSize--;
				$("#" + $appBoxId + "_title").parent().remove();	//移除无菜单的软件title
				$("#" + $appBoxId + "_List").remove();		//移除无菜单的软件List
			}
			
			$topLis.each(function(){
				var $this = $(this);
				
				if(($this).find(">ul>li").size()<=0){
					//var $topMenuItem = $("<span id='icon_" + $(this).attr("id") + "'><i class='menuLiIcon'><img src='resource/images/Newly-build.png'></i><i class='menuLiTxt' data-toggle='tooltip' data-placement='bottom' title='" + $(this).find(">.topMenu_title").text() + "'>" + $(this).find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
					var $topMenuItem = $("<span id='icon_" + $this.attr("id") + "' data-toggle='tooltip' data-placement='bottom' title='" + $this.find(">.topMenu_title").text() + "'><i class='menuLiIcon'><img src='resource/images/Newly-build.png'></i><i class='menuLiTxt'>" + $this.find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
				}else{
					//var $topMenuItem = $("<span id='icon_" + $(this).attr("id") + "'><i class='menuLiIcon'><img src='resource/images/File.png'><img class='menuLiArrow' src='resource/images/Triangle.png'></i><i class='menuLiTxt' data-toggle='tooltip' data-placement='bottom' title='" + $(this).find(">.topMenu_title").text() + "'>" + $(this).find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
					var $topMenuItem = $("<span id='icon_" + $this.attr("id") + "' data-toggle='tooltip' data-placement='bottom' title='" + $this.find(">.topMenu_title").text() + "'><i class='menuLiIcon'><img src='resource/images/File.png'><img class='menuLiArrow' src='resource/images/Triangle.png'></i><i class='menuLiTxt'>" + $this.find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
				}

				//menu1
				$topMenuItem.click(function(){
					var topMenuId = $(this).attr("id").substring(5);
					var $topMenuLi = $("#"+topMenuId).find(">ul>li");
					$topBox.find("span").removeClass("active");
					$(this).addClass("active");
					//$secondBox.slideUp();
					$secondBox.empty();
					$thirdBox.empty();
					
					
					if ($topMenuLi.size()<=0) {
						urlLink(topMenuId);
					}
					else {
						
						$topMenuLi.each(function(){
							//var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "'><i class='menuLiIcon'></i>" + $(this).find(">.second_title").text() + "</span>").appendTo($secondBox);
							
							if($(this).find(">ul>li").size()<=0){
								//var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "'><i class='menuLiIcon'><img src='resource/images/Newly-build.png'></i><i class='menuLiTxt' data-toggle='tooltip' data-placement='bottom' title='" + $(this).find(">.second_title").text() + "'>" + $(this).find(">.second_title").text() + "</i></span>").appendTo($secondBox);
								var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "' data-toggle='tooltip' data-placement='bottom' title='" + $(this).find(">.second_title").text() + "'><i class='menuLiIcon'><img src='resource/images/Newly-build.png'></i><i class='menuLiTxt'>" + $(this).find(">.second_title").text() + "</i></span>").appendTo($secondBox);
							}else{
								//var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "'><i class='menuLiIcon'><img src='resource/images/File2.png'><img class='menuLiArrow' src='resource/images/Triangle.png'></i><i class='menuLiTxt' data-toggle='tooltip' data-placement='bottom' title='" + $(this).find(">.second_title").text() + "'>" + $(this).find(">.second_title").text() + "</i></span>").appendTo($secondBox);
								var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "' data-toggle='tooltip' data-placement='bottom' title='" + $(this).find(">.second_title").text() + "'><i class='menuLiIcon'><img src='resource/images/File2.png'><img class='menuLiArrow' src='resource/images/Triangle.png'></i><i class='menuLiTxt' >" + $(this).find(">.second_title").text() + "</i></span>").appendTo($secondBox);
							}
							
							//menu2
							$secondMenuItem.click(function(){
								var secondMenuId = $(this).attr("id").substring(5);
								var $secondMenuLi = $("#"+secondMenuId).find(">ul>li");
								$secondBox.find("span").removeClass("active");
								$(this).addClass("active");
								//$thirdBox.slideUp();
								$thirdBox.empty();
								if ($secondMenuLi.size()<=0){
									urlLink(secondMenuId);
								}
								else {
									$secondMenuLi.each(function(){
										//var $thirdMenuItem = $("<span id='icon_" + $(this).attr("id") + "'><i class='menuLiIcon'><img src='resource/images/Newly-build.png'></i>" + $(this).find(">.third_title").text() + "</span>").appendTo($thirdBox);
										var $thirdMenuItem = $("<span id='icon_" + $(this).attr("id") +"' data-toggle='tooltip' data-placement='bottom' title='" + $(this).find(">.third_title").text() +"'><i class='menuLiIcon'><img src='resource/images/Newly-build.png'></i>" + $(this).find(">.third_title").text() + "</span>").appendTo($thirdBox);
										//menu3
										$thirdMenuItem.click(function(){
											var thirdMenuId = $(this).attr("id").substring(5);
											urlLink(thirdMenuId);
										});
										$thirdBox.slideDown("fast");
									});
									$("[data-toggle='tooltip']").tooltip();
								}
							});
							
							$("[data-toggle='tooltip']").tooltip();
							
						});
						$secondBox.slideDown("fast");
					}
					$("[data-toggle='tooltip']").tooltip();
				});
			});
		});
		
		if(appSize != 0){
			$("#noApp").hide();	
		}else{
			$(".launch-tab").hide();
		}
		//tab第一个增加class类“active”
		$(".launch-tab li").eq(0).addClass("active");
		$(".launch-tab-content .tab-pane").eq(0).addClass("active");
		//点击之后的span右下角图标更改
		$(".top_menu_box span").click(function(){
			$(this).find(".menuLiArrow").attr("src","resource/images/Triangle02.png");
			$(this).siblings().find(".menuLiArrow").attr("src","resource/images/Triangle.png");
			$(".second_menu_box span").click(function(){
				$(this).find(".menuLiArrow").attr("src","resource/images/Triangle02.png");
				$(this).siblings().find(".menuLiArrow").attr("src","resource/images/Triangle.png");
			});
		});
		
		function urlLink(liId){
			var $a = $("#"+liId);	
			var url = $a.attr("_href");
			
			if (url.indexOf("_backURL=")<=0) {//添加returnURL，实现返回时关闭当前TAB
				url += url.indexOf("?")>0 ? "&_backURL=../../../portal/H5/closeTab.jsp" : "?_backURL=../../../portal/H5/closeTab.jsp";
			}
			if (top && top.addTab) {
				top.addTab($a.attr("id"),
						$a.text(),
						url);
			}
			else {
				window.open(url, $a.text());
			}
		};
		$("[data-toggle='tooltip']").tooltip();
	});
</script>
</head>
<body>
<div class="con_right">
<div class="launch">
		<div class="noApp" id="noApp">
			<table height="100%" width="100%" border="0">
				<tr>
					<td align="center" valign="middle">
						<div class="content-space-pic iconfont-h5">&#xe050;</div>
						<div class="content-space-txt text-center">没有发起菜单</div>
					</td>
				</tr>
			</table>
		</div>
		<%
		
		
		
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		ApplicationHelper ah = new ApplicationHelper();
		Collection<ApplicationVO> apps = ah.getListByWebUser(user);
		
		int appNum = 0;
		
		for (ApplicationVO applicationVO : apps) {

			appNum++;
			
			String applicationId = applicationVO.getId();

			out.print("<dl id='appNum_"+appNum+"' class='menu_dl'><dt class='app'>"
					+ "<span class='appTitle'>"
					+ applicationVO.getName() + "</span></dt>"
					+ "<dd class='menu'>\n");
			ResourceAction resource = new ResourceAction();

			ResourceHelper resourceHelper = new ResourceHelper();

			PermissionHelper permissionHelper = new PermissionHelper();

			Collection<ResourceVO> topMenus = resource.get_topmenus4FlowCenter(
					applicationId, user.getDomainid());

			StringBuffer topMenuHtml = new StringBuffer();

			for (ResourceVO topMenu : topMenus) {
				String topMenuUrl = topMenu.toUrlString(user, request);
				Collection<ResourceVO> secondMenus = resourceHelper
						.searchSubResource4flowCenter(topMenu.getId(), 1,
								user.getDomainid());
				if (permissionHelper.checkPermission(topMenu,
						applicationId, user)) {

					StringBuffer secondMenuHtml = new StringBuffer();
					for (ResourceVO secondMenu : secondMenus) {
						boolean isPermission = permissionHelper
								.checkPermission(secondMenu, applicationId,
										user);

						if (permissionHelper.checkPermission(secondMenu,
								applicationId, user)) {

							Collection<ResourceVO> thirdMenus = resourceHelper
									.searchSubResource4flowCenter(secondMenu.getId(),
											1, user.getDomainid());

							StringBuffer thirdMenuHtml = new StringBuffer();
							for (ResourceVO thirdMenu : thirdMenus) {
								String thirdMenuUrl = thirdMenu
										.toUrlString(user, request);
								if (permissionHelper.checkPermission(
										thirdMenu, applicationId, user)) {

									//组合三级菜单
									if (("00".equals(thirdMenu
											.getLinkType())
											&& thirdMenuUrl != null && thirdMenuUrl
											.trim().length() > 0)) {
										String desc3 = thirdMenu.getDescription();
										if (desc3 != null && desc3.length() > 5) {
											//desc3 = desc3.substring(0, 4) + ".."
											//		+ desc3.charAt(desc3.length() - 1);
										}
										thirdMenuHtml
												.append("<li id='"
														+ thirdMenu.getId()
														+ "' _href='"
														+ thirdMenuUrl
														+ "' class='thirdMenuItem'>")
												.append("<a class='third_title'>"+desc3+"</a>")
												.append("</li>\n");
									}
								}
							}

							//组合二级菜单，没有url，且没有子菜单，则不输出
							String secondMenuUrl = secondMenu.toUrlString(
									user, request);
							if (thirdMenuHtml.length() > 0
									|| ("00".equals(secondMenu
											.getLinkType())
											&& secondMenuUrl != null && secondMenuUrl
											.trim().length() > 0)) {
								String desc2 = secondMenu.getDescription();
								if (desc2 != null && desc2.length() > 5) {
									//desc2 = desc2.substring(0, 4) + ".."
									//		+ desc2.charAt(desc2.length() - 1);
								}
								secondMenuHtml.append(
										"<li id='" + secondMenu.getId()
												+ "' _href='"
												+ secondMenuUrl + "' class='secondMenuItem'>")
										.append("<a class='second_title'>"+desc2
												+ "</a>\n");
								if (thirdMenuHtml.length() > 0) {
									secondMenuHtml
											.append("<ul class='thirdMenu'>")
											.append(thirdMenuHtml)
											.append("</ul>\n");
								}
								secondMenuHtml.append("</li>\n");
							}
						}

					}

					//组合顶级菜单，没有url，且没有子菜单，则不输出
					if (secondMenuHtml.length() > 0
							|| ("00".equals(topMenu.getLinkType())
									&& topMenuUrl != null && topMenuUrl
									.trim().length() > 0)) {
						topMenuHtml.append(
								"<li id='" + topMenu.getId() + "'_href='"
										+ topMenuUrl + "' class='topMenuItem'>").append("<span class='topMenu_title'>"+
								topMenu.getDescription()+"</span>");
						if (secondMenuHtml.length() > 0) {
							topMenuHtml.append("<ul class='secondMenu'>")
									.append(secondMenuHtml)
									.append("</ul>\n");
						}
						topMenuHtml.append("</li>\n");
					}
				}
			}

			out.println("<ul class='topMenu'>" + topMenuHtml + "</ul>");
			out.println("</dd></dl>");
		}
		%>
		<ul class="nav nav-tabs launch-tab" role="tablist"></ul>
		<div class="tab-content launch-tab-content"></div>
	</div>
</div>
</body>
</html>
</o:MultiLanguage>