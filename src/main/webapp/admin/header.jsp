<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/common/tags.jsp"%>
<s:bean name="cn.myapps.core.resource.action.ResourceAction"
	id="ResourceAction" />
<s:bean name="cn.myapps.core.permission.action.PermissionHelper"
	id="ph">
	<s:param name="user" value="#session['USER']" />
</s:bean>
<% 
	java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.util.Date today=new java.util.Date();	
	String systemtime = format.format(today);
%>
<o:MultiLanguage>
<html>
<head>
<title>{*[page.title]*}</title>

<META content="text/html; charset=UTF-8" http-equiv=Content-Type>
<style type="text/css">
<!--
body,td,th {
	font-family: Arial, Vendera;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

a {
	text-decoration: none;
	font-size: 12px;
	font-weight: bold;
	color: #636365;
}

a:active {
	color: #636365;
}

span{font-size:9pt;TEXT-ALIGN: center; cursor:hand; color:#000;background-image: url(<s:url value="/resource/imgnew/button_arrow.gif"/>);background-repeat: no-repeat; display:block;background-position: left center; padding:3px 0px 0px 0px; float:left; width:58px; height:20px} 
-->
</style>

</head>
<script type="text/javascript">
function init(){
parent.openHomePage();
}
</script>
<body onload ="init()">
<s:form>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:#c8d0d5;">
			<tr>
				<td width="455" height="41" background='<s:url value="/resource/imgnew/index_01.gif"/>' style="font-weight: bold;align:center;FILTER: glow(color=white,strength=3)dropshadow(color=white,offx=2,offy=1,positive=1)">
					<img style="margin-left:11px;" src='<s:url value="/resource/imgnew/logobanner.gif" />' alt="Teemlink Web Technology" />
				</td>
				<td style="padding-top: 1px;">&nbsp;</td>
				<td width="250" align="right" background='<s:url value="/resource/imgnew/index_03.gif"/>' />&nbsp;</td>
				<td align="right" width="236"  background='<s:url value="/resource/imgnew/index_04.gif"/>'>
					<table  width="100%" border="0" align="right" cellspacing="0">
						<tr>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td><span>&nbsp;<a style="font-weight: normal; color:black;" onclick="parent.openHomePage()">{*[Home]*}</span></td> 
							<td><span>&nbsp;<a style="font-weight: normal; color:black;" href="<c:out value='${pageContext.request.contextPath}'/>/admin/logout.jsp">{*[Logout]*}</a></span></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</td>
	</tr>
</table>
</s:form>
</body>
</html>
</o:MultiLanguage>
