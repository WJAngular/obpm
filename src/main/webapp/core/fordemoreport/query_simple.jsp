<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%
String contextPath = request.getContextPath();
String XmlPath =  request.getRealPath("");
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
          <p>促销员销量表</p>
        </div>
      </td>
    </tr>
    <tr> 
      <td width="8%">&nbsp;</td>
      <td width="29%"> 
        
      </td>
    </tr>
    <tr> 
      <td width="8%">顾客姓名:</td>
      <td width="29%"> 
        <input name="customer" type="text" class="formcss">
      </td>
      <td width="12%">卖场名称:</td>
      <td width="32%"> 
       <input name="storename" type="text" class="formcss">
      </td>
    </tr>
    <tr> 
      <td width="8%">年份:</td>
      <td width="29%"> 
        <select name="year" >
          <option value="A">[全部]</option>
          <option value="2008">2008</option>
          <option value="2009">2009</option>
        </select>
      </td>
      <td width="12%">月份: </td>
      <td width="32%">
	     <select name="month" >
          <option value="A">[全部</option>
          <option value="01">01</option>
          <option value="02">02</option>
          <option value="03">03</option>
          <option value="04">04</option>
          <option value="05">05</option>
          <option value="06">06</option>
          <option value="07">07</option>
          <option value="08">08</option>
          <option value="09">09</option>
          <option value="10">10</option>
          <option value="11">11</option>
          <option value="11">12</option>
        </select>
	  </td>
    </tr>
      <td colspan="4"><div align="center"><input name="Submit" type="button" class="bt" value="查询" onclick='getqueryurlSubmit("list_simple.jsp?showtype=html")'>
         <input name="button" type="button" class="bt" value="导出到excel" onclick='getqueryurlSubmit("list_simple.jsp?showtype=excel")'>
      </td></div></td>
    </tr>
  </table>
</form>

</body>
</html>