<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/common/taglibs.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>
<html><o:MultiLanguage>
<head>
<script src='<s:url value="/dwr/interface/UserUtil.js"/>'></script>
<script src='<s:url value="/dwr/engine.js"/>'></script>
<script src='<s:url value="/dwr/util.js"/>'></script>
<title>{*[cn.myapps.core.deploy.application.joined_developer_list]*}</title>
<link rel="stylesheet" href="<s:url value='/resource/css/main.css' />" type="text/css">
<script src="<s:url value="/script/list.js"/>"></script>
<script type="text/javascript">
function showDevelopers() {
	var url = '<s:url value="/core/deploy/application/listUnjoinedDeveloper.action"><s:param name="id" value="#parameters.id" /></s:url>';
	OBPM.dialog.show({
				opener:window.parent,
				width: 800,
				height: 500,
				url: url,
				args: {},
				title: '{*[cn.myapps.core.deploy.application.add_developer]*}',
				close: function(rtn) {
					//window.top.toThisHelpPage("application_info_advancedTools_developer_list");
					document.forms[0].action='<s:url value="/core/deploy/application/listJoinedDeveloper.action"></s:url>';
					document.forms[0].submit();
				}
		});
}

function removeDevelopers() {
	if(isSelectedOne("_selects","{*[please.choose.one]*}")){
		document.forms[0].action='<s:url action="removeDeveloper"></s:url>';
		document.forms[0].submit();
	}
}

jQuery(document).ready(function(){
	cssListTable();
	inittab();
	window.top.toThisHelpPage("application_info_advancedTools_developer_list");
});
</script>
</head>
<body id="application_info_advancedTools_developer_list" style="padding: 0;margin: 0;">
<s:form name="formList" theme="simple" action="listJoinedDeveloper" method="post">
	<s:hidden name="id" value="%{#parameters.id}"/>
	<s:textfield cssStyle="display:none;" name="tab" value="3" />
	<s:textfield cssStyle="display:none;" name="selected" value="%{'btnDeveloper'}" />
	<table cellpadding="0" cellspacing="0" width="100%">
		<tr class="nav-td" style="height:27px;">
			<td rowspan="2"><div style="width:500px"><%@include file="/common/commontab.jsp"%></div></td>
			<td class="nav-td" width="100%">&nbsp;</td>
		</tr>
		<tr>
			<td class="nav-s-td"  align="right">
				<table width="100%" border=0 cellpadding="0" cellspacing="0">
					<tr>
						<td align="right";valign="top">
							<img align="middle" style="height:23px;" src="<s:url value='/resource/imgv2/back/main/nav_sep.gif' />" />
							<button type="button" class="button-image" onClick="showDevelopers()"><img
								src="<s:url value="/resource/imgnew/add.gif"/>">{*[Add]*}</button>
						</td>
						<td width="70" valign="top">
							<button type="button" class="button-image"
							onClick="removeDevelopers()"><img
							src="<s:url value="/resource/imgnew/remove.gif"/>">{*[Remove]*}</button>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<%@include file="/common/msg.jsp"%>	
	<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
	<div class="navigation_title">{*[Developer]*}</div>
	<div id="searchFormTable" class="justForHelp" title="{*[cn.myapps.core.deploy.application.search_form]*}">
			<table class="table_noborder">
				<tr><td class="head-text">
					{*[Name]*}:<input class="input-cmd" type="text" name="sm_name" value='<s:property value="#parameters['sm_name']" />' size="10" />
					{*[Account]*}:<input class="input-cmd" type="text" name="sm_loginno" value='<s:property value="#parameters['sm_loginno']" />' size="10" />
					<input class="button-cmd" type="submit" value="{*[Query]*}" />
					<input class="button-cmd" type="button" value="{*[Reset]*}"	onclick="resetAll();" />
				<tr><td>
			</table>
	</div>
	<div id="contentTable">
	<table class="table_noborder">
		<tr>
			<td class="column-head2" scope="col"><input type="checkbox"
				onclick="selectAll(this.checked)"></td>
			<td class="column-head" scope="col"><o:OrderTag field="name"
				css="ordertag">{*[Name]*}</o:OrderTag></td>
			<td class="column-head" scope="col"><o:OrderTag field="loginno"
				css="ordertag">{*[Account]*}</o:OrderTag></td>
			<td class="column-head" scope="col"><o:OrderTag field="email"
				css="ordertag">{*[Email]*}</o:OrderTag></td>
		</tr>
		<s:iterator value="datas.datas" status="index">
			<tr>
			<td class="table-td"><input type="checkbox" name="_selects"
				value="<s:property value="id"/>"></td>
			<td>
				<s:property value="name" /></td>
			<td><s:property value="loginno" /></td>
			<td><s:property value="email" /></td>
			</tr>
		</s:iterator>
	</table>
	<table class="table_noborder">
		<tr>
			<td align="right" class="pagenav"><o:PageNavigation dpName="datas"
				css="linktag" /></td>
		</tr>
	</table>
	<%@include file="/common/basic.jsp" %>
	</div>
</s:form>
</body>
</o:MultiLanguage></html>
