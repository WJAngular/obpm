<%@ page contentType="charset=UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="oracle.jdbc.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="cn.myapps.util.property.PropertyUtil"%>
<%@ include file="config.jsp" %>
<%! 
public static String formatExcelText(String type, String value) {
	if (type!=null && type.equals("excel")) {
		value = "=ASC(\""+value+"\")";
	}
	return value;
}
%>

<%
try{
DecimalFormat nf = new DecimalFormat("#.##");
String sshowtype =(String)request.getParameter("showtype");
if(sshowtype.equalsIgnoreCase("excel"))
{
response.reset();
response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
response.setContentType("application/vnd.ms-excel;charset=UTF-8");
}
else
{
response.reset();
response.setHeader("Content-Type", "text/html; charset=UTF-8");
response.setContentType("text/html;charset=UTF-8");

}
%>
<%
String contextPath = request.getContextPath();
String XmlPath =  request.getRealPath("");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
  var contextPath ='<%=contextPath%>';
 </script>
<script language="javascript"
	src="<%= contextPath %>/core/fordemoreport/reportcommon.js"></script>
<script language="javascript" src="<%= contextPath %>/js/util.js"></script>
<title>重开供应商</title>
</head>
<body bgcolor="#ffffff">
<%
String customer =(String)request.getParameter("customer");
String storename =(String)request.getParameter("storename");

String year =(String)request.getParameter("year");
String month =(String)request.getParameter("month");

OracleConnection connoracle = null;
connoracle = getConnection();
String sql = "select item_促销员名称,item_购买时间,item_卖场名称,item_顾客姓名,item_顾客联系电话,item_数量,item_送货日期, item_产品型号,item_总金额,item_备注 from tlk_fm_vw_xindian where istmp='0' ";

if (customer!=null && !customer.trim().equalsIgnoreCase("A") && customer.trim().length()>0) {
	sql += " and item_顾客姓名 like '%"+customer+"%'";
}
if (storename!=null && !storename.trim().equalsIgnoreCase("A") && storename.trim().length()>0) {
	sql += " and item_卖场名称 like '%"+storename+"'";
}
if (year!=null && !year.trim().equalsIgnoreCase("A") && year.trim().length()>0) {
	sql += " and to_char(ITEM_购买时间,'YYYY')='"+year+"'";
}
if (month!=null && !month.trim().equalsIgnoreCase("A") && month.trim().length()>0) {
	//sql += " and audituser  '%"+sgroup+";%')";
	sql += " and to_char(ITEM_购买时间,'MM')  = '"+month+"'";
}

PreparedStatement statement = connoracle.prepareStatement(sql);
ResultSet rsall = statement.executeQuery();

java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
%>
<form name="queryform" method="post"
	action="list.jsp?showtype=excel">
<table border="1" align="center" cellpadding="2" cellspacing="1"
	>
	<tr bgcolor="#B3F48B">
		<td nowrap>购卖时间</td>
		<td nowrap>卖场名称</td>
		<td nowrap>顾客姓名</td>
		<td nowrap>联系电话</td>
		<td nowrap>送货日期</td>
		<td nowrap>产品型号</td>
		<td nowrap>金额</td>
		<td nowrap>数量</td>
		<td nowrap>备注</td>
	</tr>

	<% while(rsall.next()) {%>
	<tr bgcolor="#FFFFFF">
		<td nowrap>
		<div align="left"><%=rsall.getDate("item_购买时间")==null?"-":rsall.getDate("item_购买时间") %></div>
		</td>
		<td nowrap>
		<div align="left"><%=rsall.getString("item_卖场名称")==null?"-":rsall.getString("item_卖场名称") %></div>
		</td>
		<td nowrap>
		<div align="left"><%=rsall.getString("item_顾客姓名")==null?"-":rsall.getString("item_顾客姓名") %></div>
		</td>
		<td nowrap>
		<div align="left"><%=rsall.getString("item_顾客联系电话")==null?"-":rsall.getString("item_顾客联系电话")%></div>
		</td>
		<td nowrap>
		<div align="left"><%=rsall.getDate("item_送货日期")==null?"-":rsall.getDate("item_送货日期")%></div>
		</td>
		<td nowrap>
		<div align="left"><%=rsall.getString("item_产品型号")==null?"-":rsall.getString("item_产品型号")%></div>
		</td>
		<td nowrap>
		<div align="left"><%=rsall.getDouble("item_总金额")%></div>
		</td>
		<td nowrap>
		<div align="left"><%=rsall.getDouble("item_数量")%></div>
		</td>
		
       	<td nowrap>
		<div align="left"><%=rsall.getString("item_备注")==null?"-":rsall.getString("item_备注")%></div>
		</td>
	</tr>
	<%}%>
</table>
</form>
<%
rsall.close();
statement.close();
connoracle.close();
}catch(Exception e)
{
	e.printStackTrace();
}
%>
</body>
</html>

