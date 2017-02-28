<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<% 
	String contextPath = request.getContextPath();
%>
<html><o:MultiLanguage>
<head>
<s:bean name="cn.myapps.core.domain.action.DomainHelper" id="dh" />
<script src='<s:url value="/dwr/interface/UserUtil.js"/>'></script>
<script src='<s:url value="/dwr/engine.js"/>'></script>
<script src='<s:url value="/dwr/util.js"/>'></script>
<script src='<s:url value="/script/list.js"/>'></script>
<title>{*[Users]*}</title>
<link rel="stylesheet" href="<s:url value='/resource/css/main.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/resource/css/style.css'/>" type="text/css" />
<script type="">
	var contextPath='<%=contextPath%>';
</script>
<script src="<s:url value="/script/list.js"/>"></script>
<script>
	function addUserToDept(btn){
		if(checkSelect()){
			jQuery(btn).attr("disabled",true);
			document.forms[0].action='<s:url value="/core/user/addUserToRole.action"/>';
			document.forms[0].submit();
		}else{
			alert("{*[core.domain.notChoose]*}");
			return;
		}
	}
	
	function checkSelect(){
		var rtn=false;
		var _selectsAtrr=document.getElementsByName("_selects");
		for(var i=0;i<_selectsAtrr.length;i++){
			if(_selectsAtrr[i].checked){
				rtn=true;
				break;
			}
		}
		return rtn;
	}
	
	jQuery(document).ready(function(){
		cssListTable();
		window.top.toThisHelpPage("application_info_generalTools_role_info_addUserToRole");
	});
</script>
</head>
<body style="margin:0px;overflow: auto;" align="left">
<s:form name="formList" action="userListUnjoinedRole" method="post">
	<%@include file="/common/basic.jsp" %>
	<input type="hidden" id='roleid' name='roleid' value='<s:property value="#parameters['roleid']"/>' />
	<input type="hidden" name="sm_userRoleSets.roleId" value='<s:property value="#parameters['sm_userRoleSets.roleId']"/>' />
   	<input type="hidden" name="sm_userDepartmentSets.departmentId" value='<s:property value="#parameters['sm_userDepartmentSets.departmentId']"/>' />
	<table class="table_noborder">
			<tr><td >
				<div class="domaintitlediv"><img src="<s:url value="/resource/image/email2.jpg"/>" />{*[cn.myapps.core.role.belong_user]*}</div>
			</td>
			<td>
				<div class="actbtndiv">
					<button type="button" class="button-image" name="addButton" id="addButton" onClick="addUserToDept(this)"><img src="<s:url value="/resource/imgnew/add.gif"/>">{*[Add]*}</button>
					<button type="button" class="button-image" name="removeButton" id="removeButton" onClick="OBPM.dialog.doReturn();"><img src="<s:url value="/resource/imgnew/remove.gif"/>">{*[Exit]*}</button>
				</div>
			</td></tr>
	</table> 
	<div id="main">  
	<div id="searchFormTable">
		<table class="table_noborder">
			<tr><td>
				{*[Name]*}:
				<input class="input-cmd" type="text" name="sm_vo.name"	id="sm_vo.name" value='<s:property value="params.getParameterAsString('sm_vo.name')" />' size="10" />
				{*[Account]*}:
				<input class="input-cmd" type="text" name="sm_loginno" id="sm_loginno" value='<s:property value="params.getParameterAsString('sm_loginno')" />' size="10" />
				{*[Department]*}:
				<input class="input-cmd" type="text" name="sm_de.name" id="sm_de.name" value='<s:property value="params.getParameterAsString('sm_de.name')" />' size="10" />
				{*[Domain]*}:
				<s:select name="sm_vo.domainid" list="#dh.getDomainByStatus()" listKey="id" listValue="name" theme="simple" emptyOption="true" />
 
				<input id="search_btn" class="button-cmd" type="button" value="{*[Search]*}" onclick="submit()" />
				<input id="reset_btn" class="button-cmd" type="button" value="{*[Reset]*}"	onclick="resetAll();" />
				</td>
			</tr>
		</table>
	</div>
	<div id="contentTable">
		<table class="table_noborder">
			<tr> 
				<td class="column-head2" scope="col"><input type="checkbox"
					onclick="selectAll(this.checked)"></td>
				<td class="column-head" scope="col">{*[Name]*}</td>
				<td class="column-head" scope="col">{*[Account]*}</td>
				<td class="column-head" scope="col">{*[Department]*}</td>
				<td class="column-head" scope="col">{*[Email]*}</td>
				<td class="column-head" scope="col">{*[Mobile]*}</td>
			</tr>
			<s:iterator value="datas.datas" status="index">
				<tr>
				<td class="table-td"><input type="checkbox" name="_selects"
					value="<s:property value="id"/>"></td>
				<td>
					<s:property value="name" />
				</td>
				<td><s:property value="loginno" /></td>
				<td><s:iterator value="departments" var="dept" status="de">
					<s:property value="#dept.name" />
					<s:if test="!#de.last"> | </s:if>
				</s:iterator>
				<td><s:property value="email" /></td>
				<td><s:property value="telephone" /></td>
				</tr>
			</s:iterator>
		</table>
		<table class="table_noborder">
			<tr>
				<td align="right" class="pagenav">
					<o:PageNavigation dpName="datas" css="linktag" />
				</td>
			</tr>
		</table>
	</div>
	</div>
</s:form>
</body>
</o:MultiLanguage></html>