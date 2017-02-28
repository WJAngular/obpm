<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/tags.jsp"%>
<%@include file="/common/taglibs.jsp"%>
<% 
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>
<html>
<o:MultiLanguage>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>{*[cn.myapps.core.main.domain_info]*}</title>

<link rel="stylesheet" href="<s:url value='/resource/css/main.css'/>"
	type="text/css" />
<link rel="stylesheet" href="<s:url value='/resource/css/style.css'/>" type="text/css" />
<script type="">
	var contextPath='<%=contextPath%>';
</script>
<script>
	function doSave(){
		jQuery("#domianform").attr('action','<s:url action="step2_save" />')
		jQuery("#domianform").submit();
	}

	function changeSkinType(value){
		document.getElementById("skinTypePerview").innerHTML="<img height='100px' width='200px' src='../../resource/images/preview/preview-" + value + ".png' />";
	}
	
	jQuery(document).ready(function(){
		var thisSkinType=document.getElementById("skinType").value;
		changeSkinType(thisSkinType);
		window.top.toThisHelpPage("domain_info");
	});
	function doExit() {
		window.location = '<%=contextPath%>/admin/detail.jsp';
	}
</script>
</head>
<body id="domain_info" class="contentBody">
<div class="ui-layout-center">
<s:form name="domainInfo" id="domianform" action="save" method="post" theme="simple">
<div id="contentActDiv">
	<table class="table_noborder">
			<tr><td >
				<div class="domaintitlediv"><img src="<s:url value="/resource/image/email2.jpg"/>" />{*[cn.myapps.core.main.domain_info]*}</div>
			</td>
			<td>
				<div class="actbtndiv">
					<button type="button" id="save_btn" title="{*[Save]*}{*[Form]*}" class="justForHelp button-image" onClick="doSave();"><img src="<s:url value="/resource/imgnew/act/act_4.gif"/>">{*[Save]*}</button>
					<button type="button" id="exit_btn" title="{*[Exit]*}{*[Form]*}" class="justForHelp button-image" onclick="doExit();"><img src="<s:url value="/resource/imgnew/act/act_10.gif"/>" />{*[Exit]*}</button>
				</div>
			</td></tr>
	</table>
</div>
<div id="contentMainDiv" class="contentMainDiv">
	<%@include file="/common/msg.jsp"%>
	<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
		<%@include file="/common/basic.jsp" %>
	<s:hidden name="content.id" />
	<s:hidden name="content.sortId" />
	<table class="table_noborder id1" border="0">
		<tr>
			<td class="commFont">{*[Domain]*}{*[Name]*}:</td>
			<td class="commFont">{*[Skin]*}:</td>
		</tr>
		<tr>
			<td class="justForHelp" id="name_td" title="{*[Domain]*}{*[Name]*}" pid="main">
				<s:textfield cssClass="input-cmd" id="name" name="content.name" />
			</td>
			<td>
			<s:bean name="cn.myapps.core.domain.action.DomainHelper" id="dh" />
				<s:select cssClass="input-cmd" id="skinType" list="#dh.querySkinTypes()" onchange="changeSkinType(this.value)" name="content.skinType" />
			</td>
		</tr>

		<tr>
			<td class="commFont">{*[Status]*}:</td>
			<td class="commFont">{*[Skin]*}{*[Preview]*}：</td>
		</tr>
		<tr>
			<td id="_strstatus" class="justForHelp" title="{*[Status]*}" pid="main">
				<s:radio name="_strstatus" theme="simple" label="{*[Status]*}" list="#{'true':'{*[Enable]*}','false':'{*[Disable]*}'}" />
			</td>
			<td rowspan="5">
					<div id="skinTypePerview" title="{*[Preview]*}{*[Skin]*}"></div>
			</td>
		</tr>
		<tr>
			<td class="commFont">{*[cn.myapps.core.domain.SMS_member_code]*}:</td>
		</tr>
		<tr>
			<td id="smsMemberCode" class="justForHelp" title="{*[cn.myapps.core.domain.SMS_member_code]*}" pid="main">
				<s:textfield cssClass="input-cmd" name="content.smsMemberCode" />
			</td>
		</tr>
		<tr>
			<td class="commFont">{*[cn.myapps.core.domain.SMS_member_pwd]*}:</td>
		</tr>
		<tr>
			<td id="_password" class="justForHelp" title="{*[cn.myapps.core.domain.SMS_member_pwd]*}" pid="main">
				<s:password cssClass="input-cmd" name="_password" show="true" />
			</td>
		</tr>
		<tr>
			<td class="commFont">{*[Description]*}:</td>
		</tr>
		<tr>
			<td id="description" class="justForHelp" title="{*[Description]*}" pid="main">
				<s:textarea cssClass="input-cmd" cols="43" rows="2" label="{*[Description]*}" name="content.description" />
			</td>
		</tr>
	</table>
</div>
</s:form>
</div>
</body>
</o:MultiLanguage></html>
