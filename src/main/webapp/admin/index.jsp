<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%><html>
<o:MultiLanguage>
<head>
<title>{*[page.title]*}</title>
</head>
<body>
<%
WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_USER);
if (user == null) {
	request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
}else{
	response.sendRedirect(request.getContextPath() + "/admin/main.jsp");
}
%>
</body>
</o:MultiLanguage>
</html>
