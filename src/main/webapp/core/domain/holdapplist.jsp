<%@include file="/common/taglibs.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>
<html><o:MultiLanguage>
<head>
<title>{*[cn.myapps.core.domain.holdApp.application_list]*}</title>
</head>
<link rel="stylesheet" href="<s:url value='/resource/css/main.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/resource/css/style.css'/>" type="text/css" />
<script src="<s:url value="/script/list.js"/>"></script>
<script>

function ev_submit() {
	var url='<s:url value="/core/domain/addApp.action"></s:url>?domain=<%=request.getParameter("domain")%>';
	OBPM.dialog.show({
				opener:window.parent,
				width: 800,
				height: 500,
				url: url,
				args: {},
				title: '{*[cn.myapps.core.domain.holdApp.title.add_application]*}',
				close: function(rtn) {
					if(rtn=="success"){
						document.forms[0].submit();
					}
					window.top.toThisHelpPage("domain_application_list");
				}				
		});
}

function doDelete(){
	//var listform = document.getElementById("formList");
	var listform = document.forms["formList"];
    if(isSelectedOne("_selects","{*[please.choose.one]*}")){
    	listform.action='<s:url action="removeApp"></s:url>';
    	listform.submit();
    }
}

jQuery(document).ready(function(){
	cssListTable();
	window.top.toThisHelpPage("domain_application_list");
});

var target = null;
var weixinAgentId = "";
function editWeixinAgentId(obj){
	target = obj;
	weixinAgentId = "";
	var domainId = '<s:property value="#parameters.domain"/>';
	var applicationId = jQuery(obj).data("id");
	var value =  jQuery(obj).data("value");
	weixinAgentId = prompt("请输入微信应用ID","");
	if(weixinAgentId == null) return;
	if(weixinAgentId.length==0 || isNaN(weixinAgentId)){
		alert("微信应用id必须为数字格式，请重试！");
		return;
	}
	var url = '<s:url value="/core/domain/updateWeixinAgentId.action"/>';
	jQuery.ajax({
		url: url,
		type: 'post',
		data: {"domainId":domainId,"applicationId":applicationId,"weixinAgentId":weixinAgentId},
		success: function(result){
			if(result=="success"){
				jQuery(target).parent().parent().find(".weixinAgentId").text(weixinAgentId);
			}else{
				alert("服务器发生异常:"+result);
			}
			
		}
	});
	
}

</script>

<body id="domain_application_list" class="listbody">
<div>
<s:form name="formList" theme="simple" action="holdApp" method="post">
	<%@include file="/common/basic.jsp" %>
	<table class="table_noborder">
			<tr><td >
				<div class="domaintitlediv"><img src="<s:url value="/resource/image/email2.jpg"/>" />{*[cn.myapps.core.domain.holdApp.application_list]*}</div>
			</td>
			<td>
			<% 
				WebUser user = (WebUser) session
				.getAttribute(Web.SESSION_ATTRIBUTE_USER);
				boolean isSuperAdmin = user.isSuperAdmin();
				boolean isDomainAdmin = user.isDomainAdmin();
				int domainPermission = user.getDomainPermission();
					if (isSuperAdmin || (isDomainAdmin && domainPermission>=WebUser.NORMAL_DOMAIN)){
			%>
				<div class="actbtndiv">
					<button type="button" id="Add_Application" title="{*[cn.myapps.core.domain.holdApp.title.add_application]*}" class="justForHelp button-image" onClick="ev_submit()">	<img src="<s:url value='/resource/imgnew/add.gif'/>" />{*[Add]*}</button>
					<button type="button" id="Remove_Application" title="{*[cn.myapps.core.domain.holdApp.title.remove_application]*}" class="justForHelp button-image" onClick="doDelete()"><img src="<s:url value='/resource/imgnew/remove.gif'/>">{*[Remove]*}</button>
				</div>
			<%}else{ %>
				&nbsp;
			<%} %>
			</td></tr>
	</table>
<div id="main">
	<%@include file="/common/msg.jsp"%>
	<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
	<div id="searchFormTable" class="justForHelp" title="{*[cn.myapps.core.domain.holdApp.title.search_application]*}">
			<table class="table_noborder" >
				<tr><td class="head-text">
					{*[cn.myapps.core.domain.holdApp.application_name]*}:	<input pid="searchFormTable" title="{*[cn.myapps.core.domain.holdApp.title.by_application_name]*}" class="justForHelp input-cmd" type="text" name="sm_name" id="sm_name" 
						value='<s:property value="#parameters['sm_name']" />' size="10" />
					{*[Description]*}:	<input pid="searchFormTable" title="{*[cn.myapps.core.domain.holdApp.title.by_application_description]*}" class="justForHelp input-cmd" type="text" name="sm_description" id="sm_description"
						value='<s:property value="#parameters['sm_description']" />' size="10" />
					<input id="search_btn" pid="searchFormTable" title="{*[cn.myapps.core.domain.holdApp.title.search_application]*}" class="justForHelp button-cmd" type="submit" value="{*[Query]*}" />
					<input id="reset_btn" pid="searchFormTable" title="{*[cn.myapps.core.domain.title.reset_search_form]*}" class="justForHelp button-cmd" type="button" value="{*[Reset]*}"	onclick="resetAll();" />
				<tr><td>
			</table>
		</div>
		<div id="contentTable">
			<table class="table_noborder">
				<tr>
					<td class="column-head2" scope="col"><input type="checkbox"
						onclick="selectAll(this.checked)"></td>
					<td class="column-head" scope="col"><o:OrderTag field="name"
						css="ordertag">{*[cn.myapps.core.domain.holdApp.application_name]*}</o:OrderTag></td>
					<td class="column-head" scope="col"><o:OrderTag
						field="description" css="ordertag">{*[Description]*}</o:OrderTag></td>
					<td class="column-head" scope="col">{*[微信应用ID]*}</td>
					<td class="column-head" scope="col"><o:OrderTag
							field="activated" css="ordertag">{*[State]*}</o:OrderTag></td>
					<td class="column-head" scope="col">{*[操作]*}</td>
				</tr>
				<s:iterator value="datas.datas" status="index">
					<tr>
					<td class="table-td"><input type="checkbox" name="_selects"
						value="<s:property value="id" />"></td>
					<td><s:property value="name" /></td>
					<td><s:property value="description.length()>40?description.substring(0,40)+'...':description" /></td>
					<td class="weixinAgentId"><s:property value="weixinAgentId" /></td>
					<td>
						<s:if test="activated">
							{*[cn.myapps.core.domain.holdApp.activation]*}
						</s:if>
						<s:elseif test="!activated">
							{*[cn.myapps.core.domain.holdApp.disable]*}
						</s:elseif>
					</td>
					<td><a href="#" data-id="<s:property value='id' />" data-name="<s:property value='name' />" data-value="<s:property value='weixinAgentId' />" onclick="editWeixinAgentId(this)" >编辑微信应用ID</a></td>
					</tr>
				</s:iterator>
			</table>
		</div>
		<table class="table_noborder">
			<tr>
				<td align="right" class="pagenav"><o:PageNavigation
					dpName="datas" css="linktag" /></td>
			</tr>
		</table>
	</div>
</s:form>
</div>
</body>
</o:MultiLanguage></html>
