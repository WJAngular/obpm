<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/portal/share/common/head.jsp"%>
<%@ page import="cn.myapps.core.workflow.notification.dao.EmailTranspondHelper" %>
<%
	//由处理url过来的跳到需要处理的url地址
	EmailTranspondHelper.initJump(request, response);
%>
