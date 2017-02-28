<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/portal/share/common/lib.jsp"%>
<%@page import="java.util.*"%>
<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingVO"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingProcessBean"%>
<%@page import="cn.myapps.base.dao.DataPackage"%>
<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>
<%@page import="java.text.SimpleDateFormat"%>

<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/portal/phone/resource/common/js_base.jsp" %>
<%@include file="/portal/phone/resource/common/js_component.jsp" %>
<link href="./resource/jquery/obpm.paging.css" rel="stylesheet">
<script type="text/javascript" src="./resource/jquery/obpm.paging.js"></script>
<script type="text/javascript">
	$(function() {
		//关闭缓存
		$.ajaxSetup({cache:false});
		
		//绑定查询按钮
		$("#searchBtn").click(function() {
			loadResult();
			return false;
		});
	});

	function loadResult() {
		//添加软件名称
		var $app = $("#app"), url = "../dynaform/work/historyList.action?application="
				+ $app.val();
		
		var htmlobj=$.ajax({url:url,data:$("#searchForm").serializeArray(),dataType:"text", async:false});
		var $ul = $("<ul></ul>").append(htmlobj.responseText);
		renderNew($ul);
	}
	
	function renderNew($ul) {
		$listView = $("#resultUl");
		$listView.html("");	//清空原有数据
		
		$ul.find("li").each(function(){
			var $li = $(this);
			var url = $li.attr("_url");
			var text = $li.text();
			var flowName = $li.attr("_flowName");
			var stateLabel = $li.attr("_stateLabel");
			var proTime = $li.attr("_lastProcessTime");
			var li = '<li data-icon="false"><a data-ajax="false" '
				+ ' href="' + url + '"><h2>' + text + '</h2>'
				+ '<p>' + stateLabel + '</p>'
				+ '<p>' + proTime + '</p>'
				+ '</a><span class="ui-li-count">打开</span>'
				+ '</li>';
			$(li).appendTo($listView);		
		});
		$ul.remove();
		
		$("#ui-content").html($("#ui-content").html()).enhanceWithin();	
	}
</script>
</head>
<body>
<s:bean name="cn.myapps.core.deploy.application.action.ApplicationHelper" id="applicationHelper" />
<div data-role="page">
	<div data-role="header">
        <h1>历史</h1>
	</div>
	<div role="main" class="ui-content">
		<form id="searchForm" name="formList">
			<span class="label" for="app">{*[Application]*}:</span>
			<s:select id="app" cssClass="app1" placeholder="软件" name="applicationid"	list="%{#applicationHelper.getListByWebUser(#session.FRONT_USER)}"
				listKey="id" listValue="name" />
			<input type="text" name="_subject" id="_subject" class="subject1" size="10" placeholder="主题" value='<s:property value="#parameters._subject"/>'></input>
			<button id="searchBtn" class="ui-btn ui-btn-b ui-corner-all">{*[Search]*}</button>
			<input id="_currpage" type="hidden" name="_currpage" value="1" />
			<input id="_pagelines" type="hidden" name="_pagelines" value="15" />
		</form>
	</div>
	<div role="main" class="ui-content" id="ui-content">
		<ul data-role='listview' id='resultUl'></ul>
	</div>
</div>
</body>
	</html>
</o:MultiLanguage>