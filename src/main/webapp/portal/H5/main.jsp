<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.myapps.core.domain.ejb.DomainVO"%>
<%@ page import="cn.myapps.constans.Environment"%>
<%@ page import="cn.myapps.util.property.DefaultProperty"%>
<%@ page import="cn.myapps.core.deploy.application.action.ApplicationHelper"%>
<%@ page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@ page import="cn.myapps.core.deploy.application.ejb.ApplicationProcess"%>
<%@ page import="cn.myapps.util.ProcessFactory"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="cn.myapps.core.email.util.EmailConfig"%>
<%@ page import="cn.myapps.core.sysconfig.ejb.KmConfig"%>
<%@ page import="cn.myapps.extendedReport.NDataSource"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="myapps" prefix="o"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%
	WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	String userId = user.getId();
	String contextPath = request.getContextPath();
%>

<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<title>
	<s:if test="#session.FRONT_USER.getDomain().getSystemName().length()>0">
	<s:property value="#session.FRONT_USER.getDomain().getSystemName()"/></s:if>
	<s:else>{*[front.teemlink]*} OA</s:else>
</title>

<link rel="shortcut icon" type="images/x-icon" href="<s:url value='../share/images/logo/logo32x32.ico'/>" media="screen" />
<link rel="stylesheet" href="resource/css/bootstrap.min.css" />
<link href="<s:url value='/fonts/awesome/font-awesome.min.css'/>" rel="stylesheet" />
<link rel="stylesheet" href="resource/css/animate.css" />
<link rel="stylesheet" href="resource/css/main.css" />

<script src="resource/js/jquery-1.11.3.min.js"></script>
<script src="resource/js/bootstrap.min.js"></script>
<script src="resource/js/jquery.slimscroll.min.js"></script>
<script src="resource/js/obpm.common.js"></script>
<script src="resource/js/obpm.main.js"></script>

<!-- 弹出层插件--start -->
<script src="resource/component/artDialog/jquery.artDialog.source.js?skin=aries"></script>
<script src="resource/component/artDialog/plugins/iframeTools.source.js"></script>
<script src="resource/component/artDialog/obpm-jquery-bridge.js"></script>
<!-- 弹出层插件--end -->

<!--  -->
<script>
	var current_page = '{*[front.current_page]*}';
</script>
</head>
<body>
<input type="hidden" id="userId" name="userId" value="<%=userId%>">
<input type="hidden" id="contextPath" name="contextPath" value="<%=contextPath%>">
<div id="wrapper">
	<!-- top-navbar -->
	<nav class="header navbar">
		<div class="header-inner">
			<div class="navbar-item logo text-center">
				<!-- <a class="a-menu-hamburger"><span class="btn-zoom glyphicon glyphicon-menu-hamburger"></span></a> -->
				<img src='<s:url value="/portal/share/images/logo/logo-H5.png"/>' title="{*[page.title]*}" />
			</div>
			<div class="navbar-item navbar-tabs">
				<div class="navbar-tabs-panel">
					<ul class="navbar-tabs-ul">
						<li class="navbar-tabs-item selected" data-id="tabs_homepage">
							<a class="tab-btn-title" data-id="tabs_homepage" data-url="homepage.jsp">
								<div class="nav-title">{*[Home]*}</div>
							</a>
						</li>
					</ul>
				</div>
				
				<div id="navbar-tabs-preview">
					<a><div class="iconfont-h5" title="预览">&#xe048;</div><span class="badge">1</span></a>
				</div>
			</div>
			<div class="navbar-item navbar-menu text-center">
				<div class="top-user pull-right">
	    			<div class="user-box dropdown">
	    				<img src='<s:url value="/portal/H5/resource/images/t002.png"/>' class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true" />
						<ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="dropdownMenu1">
							<li class="user-manageDomain" role="presentation">
								<%
									if(user.getDomainUser()!=null && user.getDomainUser().equals(WebUser.IS_DOMAIN_USER)){
										if("true".equals(DefaultProperty.getProperty("saas"))){
											out.println("<a id='controlPanel' title='{*[cn.myapps.core.main.domain_management]*}' href='../../saas/weioa/controlPanel.jsp'><span class='iconfont-h5'>&#xe054;</span>{*[front.Management]*}</a>");
										}else{
											out.println("<a id='manageDomain' title='{*[cn.myapps.core.main.domain_management]*}' _url='../domain/edit.action?domain="+user.getDomainid()+"'><span class='iconfont-h5'>&#xe054;</span>{*[front.Management]*}</a>");
										}
									}
								%>
							</li>
							<li class="user-person-setting" role="presentation"><a role="menuitem" tabindex="-1" href="#"><span  class="iconfont-h5">&#xe04a;</span>{*[PersonalSettings]*}</a></li>
							<li class="user-logout" role="presentation">
								<a data-toggle="modal" data-target="#myModal"><span  class="iconfont-h5">&#xe049;</span>{*[Logout]*}</a>
							</li>  
						</ul>
					</div>
				</div>
				<div class="top-tool-bar pull-right">
					
					<%
					if(user.getDomainUser()!=null && user.getDomainUser().equals(WebUser.IS_DOMAIN_USER) && DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(user.getDomain().getWeixinProxyType())){
					%>
		 			<!-- <div class="b_setting fl"><a href="http://yun.weioa365.com/weixin/main?siteId=<%=Environment.getMACAddress() %>&domainId=<%=user.getDomainid()%>"><div class="iconfont-h5" title="微信设置">&#xe04a;</div></a></div> -->
					<%} %>
      				<!--<div class="b_exit fl"><a href="./logout.jsp"><div class="glyphicon"  title="注销">&#xe017;</div></a></div> -->   
    			</div>
			</div>
		</div>
	</nav>
	<!-- left-sidebar -->
	<div class="sidebar">
 		<div class="menu">
 			<ul id="tabs_menu" class="tabs_menu sidebar-menu">
 				<li tabid="tabs_homepage" menu="open" class="active">
 					<a href="#tabs_homepage" data-url="homepage.jsp">
 						<i class="iconfont-h5">&#xe030;</i><h5>{*[front.workbench]*}</h5>
 					</a>
 				</li>
		        <li tabid="tabs_flowcenter" menu="show">
		        	<a href="#flowMeun" class="nav-header menu-first collapsed" data-toggle="collapse" >
		        		<i class="iconfont-h5">&#xe02c;</i><h5>{*[front.workflowcenter]*}</h5>
		        	</a>
		        	<ul id="flowMeun" class="nav nav-list collapse menu-second">
				        <li tabid="launch" menu="open"><a data-url="startFlow.jsp">{*[front.workflowcenter.new]*}</a></li>
					    <li tabid="pending" menu="open"><a data-url="pending.jsp">{*[front.workflowcenter.pending]*}</a></li>
					    <li tabid="processed" menu="open"><a data-url="processing.jsp">{*[front.workflowcenter.tracking]*}</a></li>
					    <li tabid="finished" menu="open"><a data-url="finished.jsp">{*[front.workflowcenter.history]*}</a></li>
					    <li tabid="dashboard" menu="open"><a data-url="dashboard.jsp">{*[front.workflowcenter.analyze]*}</a></li>
			    	</ul>	    	
		        </li>
				<%
					Collection<String> applicationIds = user.getApplicationIds();
					for (String applicationId_ : applicationIds) {
						ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
						ApplicationVO applicationVO = (ApplicationVO) process.doView(applicationId_);
		
						if(applicationVO == null)
							continue;
						if (!applicationVO.isActivated())
							continue;
						
					    String applicationId = applicationVO.getId();							
					    String desc = applicationVO.getName().trim();
						String title = desc;
						out.print("<li tabid='tab_" + applicationId + "' menu='open' title='" + title + "'><a data-url='menu.jsp?application="
								+ applicationId
								+ "'><i class='iconfont-h5'>&#xe018;</i><h5>"
								+ desc + "</h5></a></li>");
					}
		
					out.println("<li tabid='tabs_pm' menu='show'>"
							+"<a href='#pmMeun' class='nav-header menu-first collapsed' data-toggle='collapse' >"
				        	+"<i class='iconfont-h5'>&#xe02d;</i><h5>{*[front.task]*}</h5></a>"
							+"<ul id='pmMeun' class='nav nav-list collapse menu-second'>"
							+"<li tabid='tabs_pm_task' menu='open'><a data-url='../../pm/h5-task.jsp#task'>{*[front.task.my_task]*}</a></li>"
							+"<li tabid='tabs_pm_project' menu='open'><a data-url='../../pm/h5-task.jsp#project'>{*[front.task.project]*}</a></li>"
							+"<li tabid='tabs_pm_label' menu='open'><a data-url='../../pm/h5-task.jsp#tag'>{*[front.task.label]*}</a></li>"
							//+"<li id='tabs_pm' menu='open'><a data-url='../../pm/h5-task.jsp#activity'>{*[front.dynamic]*}</a></li>"
							+"</ul>");	
		
					out.println("<li tabid='tabs_qm' menu='show'>"
							+"<a href='#qmMeun' class='nav-header menu-first collapsed' data-toggle='collapse' >"
				        	+"<i class='iconfont-h5'>&#xe046;</i><h5>{*[front.survey]*}</h5></a>"
							+"<ul id='qmMeun' class='nav nav-list collapse menu-second'>");
					
					out.println("<li tabid='tabs_qm_homepage' menu='open'><a data-url='../../qm/answer/index.jsp'>{*[front.survey.home_page]*}</a></li>");

					out.println("<li tabid='tabs_qm_center' menu='open'><a data-url='../../qm/questionnaire/list.action'>{*[front.survey.issued]*}</a></li>");
			
					out.println("<li tabid='tabs_qm_fill' menu='open' style='display:none;'><a data-url='../../qm/answer/list.action'>{*[front.survey.write]*}</a></li>"
							+"</ul>");
					
					//判断是否开启ERP报表				
					if(NDataSource.isErpEnable()){
						out.println("<li tabid='tabs_erp' menu='show'><a href='#erpMeun' class='nav-header menu-first collapsed' data-toggle='collapse' ><i class='iconfont-h5'>&#xe030;</i><h5>ERP</h5></a></li>"
									+"<ul id='erpMeun' class='nav nav-list collapse menu-second'>"
									+"<li id='tabs_erp_report' menu='open'><a data-url='../../extendedReport/erpReport/goodsonhand.jsp'>ERP报表</a></li>"
									+"<li id='tabs_erp_data' menu='open'><a data-url='../../extendedReport/erpSearch/searchDept.jsp'>ERP数据查询</a></li>"
									+"<li id='tabs_erp_add' menu='open'><a data-url='../../extendedReport/erpOrder/orderPurchase.jsp'>ERP订单</a></li>"
									+"</ul>");
					}

					//判断是否开启KM
					if (KmConfig.isKmEnable()) {
						out.println("<li tabid='tabs_knowledge' menu='show'>"
								+"<a href='#kmMeun' class='nav-header menu-first collapsed' data-toggle='collapse' >"
					        	+"<i class='iconfont-h5'>&#xe02f;</i><h5>{*[km.name]*}</h5></a>"
								+"<ul id='kmMeun' class='nav nav-list collapse menu-second'>"
								+"<li tabid='hotest' menu='open'><a data-url='../../km/disk/listHotest.action'>{*[cn.myapps.km.disk.popular_share]*}</a></li>"
								+"<li tabid='newest' menu='open'><a data-url='../../km/disk/listNewest.action'>{*[cn.myapps.km.latest_upload]*}</a></li>"
								+"<li tabid='publicDisk' menu='open'><a data-url='../../km/disk/view.action?_type=1'>{*[cn.myapps.km.disk.public_disk]*}</a></li>"
								+"<li tabid='personalDisk' menu='open'><a data-url='../../km/disk/view.action?_type=2'>{*[cn.myapps.km.disk.my_disk]*}</a></li>"
								+"<li tabid='archiveDisk' menu='open'><a data-url='../../km/disk/view.action?_type=5'>{*[cn.myapps.km.disk.archive_disk]*}</a></li>"
								+"<li tabid='baike' menu='open'><a data-url='../../km/baike/entry/doInit.action'>{*[km.encyclopedia]*}<font style='font-size:9pt;color:red;'>(beta)</font></a></li>"
								+"</ul>");	
					}
				%>
   			</ul>
 		</div>
 	</div>
 	<!-- right-page -->
  	<div id="tabs" class="page-wrapper tab-content">  	
		<div id="tabsLoading" class="tabs-loading" style="z-index:1;"></div>
		<div class="tabs-item-wrapper fadeInRightMain animated tab-pane active" id="tabs_homepage" style='z-index:2;position: relative;background: #f6f7fb;'>
			<iframe style='border-top: 0px #e7eef5 solid;width:100%;height:100%' id="iframe-homepage" _tags="window" src="homepage.jsp" frameborder='no' border='0' marginwidth='0' marginheight='0' width='' scrolling='auto' allowtransparency='yes'></iframe>
		</div>	
	</div>
</div>

<div id="tabsPreview">
	<ul class="tabs-preview-panel">
		<li data-id="tabs_homepage" class="animatedFast zoomIn active" >
			<a href="#tabs_homepage" data-url="homepage.jsp">
				<div class='status-box'><span class='status'>{*[front.current_page]*}</span></div>
				<div class="nav-title">{*[Home]*}</div>
			</a>
			<img src="../share/images/logo/tab-home.jpg" style="width: 100%;height: 100%;">
			<i class='ui-icon ui-icon-close glyphicon glyphicon-lock' role='presentation'></i>
		</li>
	</ul>
	<div class="nav-closeAll">
		<a class="nav-closeAll-btn">{*[front.close_all]*}</a>
	</div>
</div>

<!-- 缩放 -->
<div id="zoomBtn"><i class="fa fa-caret-left" aria-hidden="true"></i></div>

<!-- 遮挡层 -->
<div id="backBlur" style="display:none"></div>

<!-- 退出弹出层 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel"><div class="glyphicon"  title="{*[Logout]*}">&#xe017;</div></h4>
			</div>
			<div class="modal-body">{*[front.logout.logout_or_not]*}</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">{*[front.logout.cancel]*}</button>
				<a href="./logout.jsp" style="display:inline-block"><button type="button" class="btn btn-primary">{*[front.logout.exit]*}</button></a>
			</div>
		</div>
	</div>
</div>

</body>

<script>
EDEN.Main.init();
var addTab = EDEN.Main.renderTabs.addTab;
</script>

</html>
</o:MultiLanguage>