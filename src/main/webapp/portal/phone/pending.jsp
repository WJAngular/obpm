<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="cn.myapps.core.deploy.application.action.ApplicationHelper"%>
<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingVO"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingProcessBean"%>
<%@page import="cn.myapps.base.dao.DataPackage"%>
<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@include file="/portal/share/common/lib.jsp"%>

<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationProcess"%>
<%@page import="cn.myapps.util.ProcessFactory"%>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/portal/phone/resource/common/js_base.jsp" %>
<%@include file="/portal/phone/resource/common/js_component.jsp" %>
</head>
<body>
<div data-role="page" class="jqm-demos jqm-home">
	<div data-role="header" class="jqm-header">
        <h1>待办</h1>
	</div>
	<div role="main" class="ui-content" id="contentTest">
	<%
		WebUser user = (WebUser) session
				.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	
		ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
		Collection<ApplicationVO> appList = process.queryAppsByDomain(user.getDomainid(),1,Integer.MAX_VALUE);
		out.println("<ul data-role='listview'>");
		for (ApplicationVO applicationVO : appList) {
			if (!applicationVO.isActivated())
				continue;
			String applicationId = applicationVO.getId();
			out.println("<li refreshId='" + applicationId + "' _appId='" + applicationId + "' _appName='"
					+ applicationVO.getName() + "' data-role='list-divider'>");

			out.println("</li>");
		}
		out.println("</ul>");
	%>
	</div>
</div>
<script type="text/javascript">

function render($ul) {
	$ul.siblings("li.fold").remove();
	
	$ul.find(">li[data-role!='list-divider']").each(function() {
						var $li = $(this);
						var id = $li.attr("_flowName");
						var $p = $("#" + id);
						if ($p.size() <= 0) {
							$p = $("<li id='"+id+"' class='fold'>"+ id
								+ "<span class='ui-li-count'>1</span><ul data-role='listview' class='foldContent'></ul></li>");
						}

						var $row = $("<li data-icon='false'><a data-ajax='false' href='"+$li.attr("_url")+"'>"
								+ "<h2>" + $li.text() + "</h2>"
								+ "<p>" + $li.attr("_stateLabel") + "</p>"
								+ "<p>" + $li.attr("_auditorNames") +"</p>"
								+ "<p>" + $li.attr("_lastProcessTime") + "时"
								+ "</p>"
								+"</a><span class='ui-li-count'>打开</span></li>");
						
						
						$p.find("ul").append($row);
						$p.insertAfter($ul);
						$li.remove();
					});
	//添加数量
	$("#contentTest").find("li.fold").each(function(){
		$(this).find(">span.ui-li-count").text($(this).find("ul.foldContent > li").size());
	});
	//添加软件标题
	$ul.append("<h3>" + $ul.attr("_appname") + "</h3>");
}

$(function() {
	//关闭缓存
	$.ajaxSetup({cache:false});

	var count = $("li[data-role='list-divider']").size();
	$("li[data-role='list-divider']").each(function(){
		$(this).bind("refresh", function(event) {
			var $li = $(this);

			var url = "../dynaform/work/pendingList.action?application="
					+ $li.attr("_appId");
			window.location.href='main.jsp?application='+$li.attr("_appId")+'&returnUrl=#flowCenter';
			$li.load(url, function() {
				render($(this));
				count --;
				if (count <=0) {
					$("#contentTest").html($("#contentTest").html());
				}
			});
			
		}).trigger("refresh"); 
	});
});
</script>
</body>
</html>
</o:MultiLanguage>