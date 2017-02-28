<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.net.InetAddress"%>
<%@page import="cn.myapps.core.user.action.OnlineUsers"%>
<%
	String obpmVersion = "2.5sp8";
	String hostName = InetAddress.getLocalHost().getHostName();
	String appVersion = "2.5sp8";
	int onlineUserCount = OnlineUsers.getUsersCount();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>System Info</title>
</head>
<body>
<table>
	<tr>
		<td>OBPM Version:</td>
		<td><%=obpmVersion%></td>
	</tr>
	<tr>
		<td>Host Name:</td>
		<td><%=hostName%></td>
	</tr>
	<tr>
		<td>App Version:</td>
		<td><%=appVersion%></td>
	</tr>
	<tr>
		<td>Current Online User:</td>
		<td><%=onlineUserCount%></td>
	</tr>
</body>
</html>