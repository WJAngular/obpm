<%@ page contentType="text/html;charset=GBK" %>
<html>
<title>�����쳣��Ϣ</title>
<body>

<%
	Exception e = ( Exception ) request.getAttribute( "exception" );
	out.println( "<div style='color:red'>" + e.getMessage() + "</div>" );
%>

</body>
</html>