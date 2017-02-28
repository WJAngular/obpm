<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="myapps" prefix="o"%>
<%@page import="java.util.Calendar" %>
<!DOCTYPE html>
<%
Calendar cld = Calendar.getInstance();
int currentYear = cld.get(Calendar.YEAR);
Object obj = request.getAttribute("showCode");
boolean showCode = false;
if (obj != null) {
	showCode = ((Boolean)obj).booleanValue();
}
%>
<html>
<o:MultiLanguage>
<s:bean name="cn.myapps.core.multilanguage.action.MultiLanguageHelper" id="mh" />
<head>
	<meta charset="utf-8">
	<title>{*[page.title]*}</title>
	<link rel="stylesheet" href="css/style.css">
	<script type="text/javascript" src="<s:url value='/portal/share/script/base64.js'/>"></script>
	<script type="text/javascript">
	function ev_submit() {
		var b = new Base64();
		var pw = document.getElementsByName("password")[0];
		var str = b.encode(pw.value);
		if(str.length>2){
			var lp = str.substr(0,2);
			var rp = str.substr(2,str.length);
			pw.value = rp+lp;
		}
		document.getElementById("login").submit();
	}


	function getSecurityCode() {
		var numkey = Math.random();
		document.getElementById("checkCodeImg").src = '<s:url value="/checkCodeImg"/>?num=' + numkey;
	}
</script>
</head>
<body>
	<%-- <s:select cssClass="select" name = "language" list="#mh.getTypeList()" value="#mh.getType(#session.USERLANGUAGE)" theme="simple" onchange="changeLanguage()" /> --%>
	<s:form id="login" name="myform" action="/admin/login.action" method="POST" theme="simple">
		<h1><img src="images/logo.png"/></h1>
		<fieldset>
			<input id="username" name="username"  class="form-control" type="text" placeholder="{*[Account]*}" autofocus="" required="">   
			<input id="password" name="password"  class="form-control" type="password" placeholder="{*[Password]*}" required="">
			<!-- 验证码 -->
			<%if (showCode) { %>
			<div id="check_code">
				<input id="check"  name="checkcode" onblur="disableTips(this)" onfocus="showTips(this)" class="form-control" type="text" placeholder="{*[Characters]*}" style="background: #fff; width:100px">
				<img src="<s:url value="/checkCodeImg"/>" align="absmiddle" onclick="getSecurityCode();" width="70" height="26" id="checkCodeImg" />
			</div>
			<%} %>

			<div id="actions">
				<button type="submit" class="sure" onclick="ev_submit()">登陆</button>
			<!-- 表单提示 -->
			<span href="javascript:void(0)" id="mention">
			<div align="left">
				<s:if test="hasFieldErrors()">
					<span class="errorMessage"> 
					<table><tr>
					<td valign="top">
						<b>{*[Errors]*}:</b>
					</td>
					<td>
						<s:iterator value="fieldErrors">
							*<s:property value="value[0]" />;<br/>
						</s:iterator>
					</td>
					</tr></table>
					</span>
				</s:if>
				<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
					<%@include file="/portal/share/common/msgbox/msg.jsp"%>
				</s:if>
			</div>
			
			</span>
			</div>
		</fieldset>
	</s:form>
	<small id="back"><p id="copyright"> &copy; 2009-<%=currentYear %>. <a href="http://www.gd-sc.net" target="_blank" title="思程科技"><b>{*[page.website]*}</b></a> {*[myapps.All.rights.reserved]*}.</p></small>
	<canvas id="canv" width="1440" height="799"></canvas>
	<script src="js/particle.js"></script>
</body>
</o:MultiLanguage>
</html>
