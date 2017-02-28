<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%
  String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script >
//has parent window 
var object1 = window.parent;
var parenturl = object1.url;
//alert(object1.url);
// while(object1!=null)
 //{
  // parenturl = object1.url;
   //object1 = object1.parent;
   
 //}
 object1.url ='<%=contextPath%>/login_error.htm';
</script>
<body>

</body>
</html>