<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/portal/share/common/head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%String contextPath = request.getContextPath();%>
<html><o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<head><title>{*[{Import result]]*}</title>
<link rel="stylesheet"
	href="<s:url value='/resource/css/main.css' />"
	type="text/css"></head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<s:url id="url" value='/resourse/main.css'/>"/>
<body>
<table>
<tr><td>
<s:if test="hasFieldErrors()">
	<span class="errorMessage"> <b>{*[Errors]*}:</b><br>
	<s:iterator value="fieldErrors">
		*<s:property value="value[0]" />;</br>
	</s:iterator> </span>
</s:if>
</td></tr>

<tr>
<td>
<s:property value="_msg" />
</td>
</tr>
</table>
	  	
</body>
</o:MultiLanguage></html>
