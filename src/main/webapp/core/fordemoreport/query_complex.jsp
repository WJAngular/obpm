<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.myapps.testInvokeCmd"%>
<%
String contextPath = request.getContextPath();
String path =  request.getRealPath("");
%>
<html>
<head>
 <script language="javascript">
  var contextPath ='<%=contextPath%>';
  function getqueryurlSubmit(actionurl)
{
   document.queryform.action=actionurl;
   document.queryform.submit();
}
 </script>
 <%
	String script = path +"\\core\\fordemoreport\\"+"excel_sync.vbs";

   try {
			Process process = Runtime.getRuntime().exec(
					" cmd /c  cscript  " + script);
			process.waitFor();
			process.getErrorStream();
	
	} catch (Exception  e) {
		e.printStackTrace();
	}

 %>
<script language="javascript" src="<%= contextPath %>/core/fordemoreport/reportcommon.js"></script>
<script language="javascript" src="<%= contextPath %>/script/util.js"></script>
<title>
</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body bgcolor="#ffffff">
<form name="queryform" method="post" action="list_simple.jsp">
  <table border="0" align="center" cellpadding="2" cellspacing="1">
    <tr> 
      <td class="style1" colspan="4">
        <div align="center"> 
          <p>&nbsp;</p>
          <p>每日销售汇总表</p>
        </div>
      </td>
    </tr>
    <tr> 
      <td width="8%">&nbsp;</td>
      <td width="29%"> 
        
      </td>
    </tr>
 <tr>
 <td>
 <a href="report.xls">下载excel</a>
      </td></div></td>
    </tr>
  </table>
</form>

</body>
</html>