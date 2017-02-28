
<%
	String ctxPath = request.getContextPath();
	response.sendRedirect((ctxPath != "/" ? ctxPath : "") + "/portal/share/welcome.jsp");
%>
