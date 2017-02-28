<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" deferredSyntaxAllowedAsLiteral="true"%>
<%@page import="java.util.*"%>
<%@page	import="cn.myapps.core.deploy.application.action.ApplicationHelper"%>
<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationProcess"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingVO"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingProcessBean"%>
<%@page import="cn.myapps.base.dao.DataPackage"%>
<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="myapps" prefix="o"%>
<%@page import="cn.myapps.util.ProcessFactory"%>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="resource/css/bootstrap.min.css" />
<link rel="stylesheet" href="resource/css/myapp.css" />
<link rel="stylesheet" href="resource/css/other.css" />
<link rel="stylesheet" href="<o:Url value='/resource/script/jquery.pagination/jquery.pagination.css'/>" />

<script type="text/javascript">
var front_nocontent = '{*[front.workflowcenter.nocontent]*}';
</script>
<script src="resource/script/jquery.min.js"></script>
<script src="resource/script/bootstrap.min.js"></script>
<script src="resource/script/flowCenter.js"></script>
<script src="resource/script/common.js"></script>
<script src="./resource/script/template.js"></script>
<script src="<o:Url value='/resource/script/jquery.pagination/jquery.pagination.js'/>"></script>
<script id="appLabelTmpl" type="text/html">
	<!-- app label -->
	<li role="presentation" style="display:none;">
		<a href="#{{appId}}" aria-controls="{{appId}}" role="tab" data-toggle="tab">{{appName}}</a><span>{{_rowcount}}</span>
	</li>
</script>
<script id="appContentTmpl" type="text/html">
		<div class="con-tab swiper-container" role="tabpanel">
			<!-- flowname label -->
			<ul class="con-nav nav nav-tabs swiper-wrapper font12 flowLabel" role="tablist" name="flowLabel" id="flowLabel">
				<li class="swiper-slide" role="presentation">
					<a href="#li-{{appId}}" aria-controls="li-{{appId}}" role="tab" data-toggle="tab">{*[front.workflowcenter.All]*}</a>
				</li>
				{{each flowLists as flows}}
				<li class="swiper-slide" role="presentation">
					<a href="#{{flows.flowName}}" aria-controls="{{flows.flowName}}" role="tab" data-toggle="tab">{{flows.flowName}}<span>{{flows.flowcount}}</span></a>
				</li>
				{{/each}}
			</ul>
			<div class="tab-content" id="tab-content">
				<!-- all -->
				<div role="tabpanel" class="tab-pane" id="li-{{appId}}">
					<ul>
						{{each lis }}
						<li class="widgetItem list-con" id="{{$value.tabDocID}}" _url="{{$value.tabUrl}}" _isRead="{{$value.tabIsRead}}">
							<div class="tabLiFace">
								{{$value.tabAvatar}}
								<span class="{{$value.readCls}}"></span>
							</div>
							<div class="tabLiCon">
								<div class="tabLiConBox">
									<div class="tabLiConA text-left">
										<span class="tabLiCon-text">[{{$value.tabName}}] {{$value.tabCon}}</span>
									</div>
									<div class="tabLiConB">
										<div class="tabLiTagLeft">
											<span class="tabLiCon-auditornames" _initiator="{{$value.tabInitiator}}" _initiatorId="{{$value.tabInitiatorID}}">{{$value.tabDept}}{{$value.tabInitiator}}</span>
											<span class="tabLiCon-lastprocesstime timeago" title="{{$value.tabTime}}">{{$value.tabTime}}</span>
										</div>
										<div class="tabLiTagRight text-right">
											<span class="tabLiCon-status" title="{{$value.tabState}}">{{$value.tabState}}</span>
										</div>
									</div>
								</div>
							</div>
						</li>
						{{/each}}
					</ul>
					<div id="pagination-panel"></div>
				</div>
				<!-- flowname -->
				{{each flowLists as flows}}
				<div role="tabpanel" class="tab-pane" id="{{flows.flowName}}">
					<ul>
						{{each lis }}
						{{if flows.flowName==$value.tabName }}
						<li class="widgetItem list-con" id="{{$value.tabDocID}}" _url="{{$value.tabUrl}}" _isRead="{{$value.tabIsRead}}">
							<div class="tabLiFace">
								{{$value.tabAvatar}}
								<span class="{{$value.readCls}}"></span>
							</div>
							<div class="tabLiCon">
								<div class="tabLiConBox">
									<div class="tabLiConA text-left">
										<span class="tabLiCon-text">[{{$value.tabName}}] {{$value.tabCon}}</span>
									</div>
									<div class="tabLiConB">
										<div class="tabLiTagLeft">
											<span class="tabLiCon-auditornames" _initiator="{{$value.tabInitiator}}" _initiatorId="{{$value.tabInitiatorID}}">{{$value.tabDept}}{{$value.tabInitiator}}</span>
											<span class="tabLiCon-lastprocesstime timeago" title="{{$value.tabTime}}">{{$value.tabTime}}</span>
										</div>
										<div class="tabLiTagRight text-right">
											<span class="tabLiCon-status" title="{{$value.tabState}}">{{$value.tabState}}</span>
										</div>
									</div>
								</div>
							</div>
						</li>
						{{/if}}
						{{/each}}
					</ul>
				</div>
				{{/each}}
			</div>
		</div>
</script>
<script type="text/javascript">
template.config("escape",false);	//设置模板是否编码输出变量的 HTML 字符
var contextPath = '<%=request.getContextPath()%>';
var appId = '<%=request.getParameter("appId")%>';
var flowCenter = {
	multilingual : {}
};
flowCenter.multilingual["Subject"] = "{*[Subject]*}";
flowCenter.multilingual["State"] = "{*[State]*}";
flowCenter.multilingual["Current_Processor"] = "{*[Current_Processor]*}";
flowCenter.multilingual["flow.last_time"] = "{*[flow.last_time]*}";
flowCenter.multilingual["Activity"] = "{*[Activity]*}";
flowCenter.multilingual["total"] = "{*[front.total]*}";
$(function() {
	//关闭缓存
	$.ajaxSetup({cache:false});
	FC.init();
	$("#content-box div.appSource").on("refresh",function(){
		FC.init();
	});
});
</script>
</head>
<body>
<!-- 遮挡层 -->
<div id="loadingDivBack" style="display:none;position: fixed; z-index: 110; width: 100%; height: 100%; top: 0px; left: 0px; background-color:#ccc; filter: alpha(opacity = 0.1); opacity: 0.1;">
	<div style="position: absolute;top: 35%;left: 45%;width: 128px;height: 128px;z-index: 100;">
		<img src="<o:Url value='/resource/main/images/loading1.gif'/>"/>
	</div>
</div>
<!-- 输出软件ids，重构使用 -->
<div id="appList" style="display:none" _type="pending">
	<%
	WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	ApplicationHelper ah = new ApplicationHelper();
	Collection<ApplicationVO> apps = ah.getListByWebUser(user);
	String _currpage = request.getParameter("_currpage");
	
	for (ApplicationVO applicationVO : apps) {
		String applicationId = applicationVO.getId();

		out.println("<div _appId='" + applicationId + "' _appName='" + applicationVO.getName() + "'></div>");
	}
	%>
</div>
<!-- 重构后显示内容 -->
<div class="content-box" id="content-box" style="height:100%;">
	<div class="toCreate" role="tabpanel">
		<!-- app label -->
		<ul class="app-nav nav nav-tabs" role="tablist" id="appLabel" style="display:none;">
		</ul>
		<div class="app-content tab-content" id="appContent">
		</div>
	</div>
</div>
</body>
</html>
</o:MultiLanguage>