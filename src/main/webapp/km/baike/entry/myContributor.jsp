<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="cn.myapps.km.base.dao.DataPackage" %><%
	//获取参数：选中菜单
	String selectedMenu = request.getParameter("selectedMenu");
%>

<html>
<%
	String contextPath = request.getContextPath();  
	String basePath = request.getScheme() + "://"  
        + request.getServerName() + ":" + request.getServerPort()  
        + contextPath;  
%>
<script src="<%=basePath %>/km/baike/script/jquery-ui/jquery-1.9.1.js" type="text/javascript"></script>
<link type="text/css" href="css/style.css" rel="stylesheet" />
<head>
<title> </title>
    <style>


.title span {
font-size: 18px;
    color: #333333;
	 line-height: 50px;
    padding: 25px 0 0 8px;
}
.table-head .time {
    width: 35%;
    text-align:center;
}


.table-head .version {
    width: 40%;
     margin-left:15px;
}

.table-head .contributor {
    width: 40%;
    text-align:center;
}

.table-list .tr {
    border-bottom: 1px solid #ECECEC;
    line-height: 28px;
}
.table-head  {
    border-bottom: 1px solid #665666;
    line-height: 28px;
}

.table-list .tr li {
    float: left;
    line-height: 28px;
}

.table-list .tr .version a {
    vertical-align: middle;
}
.table-list .tr a {
    text-decoration: none;
}
a:hover, a:link, a:visited {
    color: #136EC2;
    text-decoration: none;
}
.table-list .tr .version {
    font-size: 14px;
    width: 40%;
     margin-left:15px;
}
.table-list .tr .contributor {
    font-size: 14px;
    overflow: hidden;
    width: 40%;
     text-align:center;
}


.table-head li {
    color: #333333;
    float: left;
    font-size: 14px;
    font-weight: bold;
}
.back {
    float: right;
    line-height: 28px;
}

.page {
    font-size: 14px;
    line-height: 128px;
    text-align: center;
}
td {
    border-bottom: 1px dotted #AEC9EA;
    font-size: 14px;
    margin-top: 3px;
}

	</style>

</head>

<body id="baike" >

	<div id="wrap">
		<%@ include file="/km/baike/entry/baikeHead.jsp"%>
		
		<div id="page">
			<div class="edithistory">
				<div id="navbar-collection" class="navbar" style="">
					<div class="line"></div>
				</div>
				<form action="/diff/" method="get" name="diff" target="_blank">
					<input type="hidden" name="vid1" value="">
					<input type="hidden" name="vid2" value="">
				</form>
				<div class="tab clearfix" style="width:100%; height:30px; background:#888888">
					
				</div>
					<div class="xian" style="border-bottom: 1px solid #AEC9EA; margin-top: 1px;">
					</div>
			<div>
				<div class="title clearfix">
					
				</div>
				<table  style="width:125%;">
					<tr class="table-head clearfix">
						<th class="version" width="40%" style="white-space:nowrap;padding-left:10px;font-size: 14px;font-weight: bold; text-align:left;">词条名称</th>
						<th class="contributor" width="40%" style="text-align:center;font-size: 14px;font-weight: bold;">创建时间</th>
					</tr>
					<s:iterator value="conttribute.datas" status="anyStatus">
						<div class="table-list">				
		   						<tr class="tr clearfix">
		   							<td class="version" width="40%" style="padding-left: 10px;"><a title='<s:property value="entry.name"/>' href='<s:url value="/km/baike/entry/doView.action"/>?entryId=<s:property value="id" />'><s:property value="name"/></a></td>
		
		   							<td class="contributor" width="40%" style="text-align:center;"><s:date name="created" format="yyyy-MM-dd HH:mm" /></td>
		  						</tr>
		  					</div>
		  			</s:iterator>	
			</table>
		</div>
		
	</div>

<div class="bgpagLn" align="center">
			<div class="bgpagLn" align="center">
				<div class="page">
				
			<a href="<s:url action='doFindContributor'> 
					<s:param name="page" value="1" />
					<s:param name="lines" value="datas.linesPerPage" />
					<s:param name="selectedMenu" ><s:property value="selectedMenu" /></s:param>
					</s:url>" >首页</a>
			<a href="<s:if test="datas.pageNo>1">
					<s:url action='doFindContributor'>
					<s:param name="page" value="datas.pageNo-1" />
					<s:param name="lines" value="datas.linesPerPage" />
					<s:param name="selectedMenu" ><s:property value="selectedMenu" /></s:param>
					
					</s:url></s:if><s:else>#</s:else>" >上一页</a> 
			<a href="<s:if test="datas.pageNo<datas.getPageCount()">
					<s:url action='doFindContributor'>
					<s:param name="page" value="datas.pageNo+1" />
					<s:param name="lines" value="datas.linesPerPage" />
					
					<s:param name="selectedMenu" ><s:property value="selectedMenu" /></s:param>
					</s:url></s:if><s:else>#</s:else>" >下一页</a>
			<a href="<s:url action='doFindContributor'>
					<s:param name="page" value="datas.getPageCount()" />
					<s:param name="lines" value="datas.linesPerPage" />
					<s:param name="selectedMenu" ><s:property value="selectedMenu" /></s:param>
					</s:url>" >尾页</a>
页次：<s:property value="conttribute.pageNo" />/<s:property value="conttribute.getPageCount()" />
页 共<s:property value="conttribute.rowCount" />条记录 <s:property value="conttribute.linesPerPage" />条记录/页</div>
			</div>
			</div>
		</div>
		<div id="footer" class="nslog-area" style="text-align:center;margin-top:115px;">
		© 2013 BaiKe版权所有@天翎网络
			</div>
		</div>
	</body>
</html>