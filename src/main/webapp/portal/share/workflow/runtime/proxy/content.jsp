<%@ include file="/portal/share/common/head.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="cn.myapps.core.email.util.Constants"%>
<%@page import="cn.myapps.core.dynaform.work.action.WorkHtmlUtil"%>
<%
WorkHtmlUtil workHtmlUtil = new WorkHtmlUtil(request);
request.setAttribute("workHtmlUtil", workHtmlUtil);
%>
<html><o:MultiLanguage>
<head>
<title>{*[]*}</title>
<link rel="stylesheet" href="<s:url value='/portal/share/css/setting-up.css'/>" type="text/css" />
<style type="text/css">
	.flowname select,.miaoshu textarea,.agentsname input,.agentsname .orgAdd{
		padding: 6px 12px;
	    margin-bottom: 0;
	    font-size: 14px;
	    font-weight: 400;
	    line-height: 1.42857143;
	    white-space: nowrap;
	    vertical-align: middle;
	    -ms-touch-action: manipulation;
	    touch-action: manipulation;
	    cursor: pointer;
	    -webkit-user-select: none;
	    -moz-user-select: none;
	    -ms-user-select: none;
	    user-select: none;
	    background-image: none;
	    border: 1px solid #ccc;
	    border-radius: 4px
	}
	.agentsname .orgAdd{
		padding: 8px 12px;}
</style>
<script type="text/javascript" language="javascript">
var contextPath = '<%=request.getContextPath()%>';
function onFlowChange(obj) {
	var pindex  =  obj.selectedIndex;
	document.getElementsByName("content.flowName")[0].value = obj.options[pindex].text;
}

function doExit(){
	OBPM.dialog.doExit();
}

function doSave(){
	var flowname = document.getElementById("save_content_flowId").value;
	var agentsname = document.getElementById("content.agentsName").value;
	if(!flowname){
		alert("请选择流程名称");
		return;
	}
	if(!agentsname){
		alert("请选择授权人");
		return;
	}
	var uri = contextPath +'/portal/workflow/runtime/proxy/save.action';
	document.forms['workflowConent'].action = uri;
	document.forms['workflowConent'].submit();	
}
function init(){
	if(document.getElementsByName("content.id")[0].value.length>0){
		//document.getElementById("content.flowId").disabled =true;
	}
	if(document.getElementsByName("isRefresh")[0].value=='true'){
		OBPM.dialog.doReturn('success');
	}		
}
jQuery(window).load(function(){
	init();
});
</script>
</head>
<body id="application_info_generalTools_links_info" style="margin:0px;padding:0px;">
	<table class="act_table" cellspacing="0" cellpadding="0" style="width:100%;display:none;">
		<tr>								
			<td style="width:100%">
				<div class="exitbtn">
				<div class="button-cmd">
					<div class="btn_left"></div>
					<div class="btn_mid">
						<a class="applyicon" onClick="doSave();">
							{*[Save]*}
						</a>
					</div>
					<div class="btn_right"></div>
				</div>
				<div class="button-cmd">
					<div class="btn_left"></div>
					<div class="btn_mid">
						<a class="exiticon" onClick="doExit();">
							{*[Exit]*}
						</a>
					</div>
					<div class="btn_right"></div>
				</div>					
				</div>							
			</td>				
		</tr>
	</table>
	<%@include file="/common/msg.jsp"%>
	<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
	<div id="contentMainDiv" class="contentMainDiv" style="margin-top:25px;">
		<s:form action="save" method="post" validate="true"
			theme="simple" name="workflowConent" >
			<s:hidden name="content.id" />
			<s:hidden name="content.owner"/>
			<s:hidden name="content.applicationid" value="%{#parameters.application}" />
			<s:hidden name="content.domainid" value="%{#parameters.domain}" />
			<s:hidden name="isRefresh" />
			<s:hidden name="application" value="%{#parameters.application}" />			
			<table width="100%" align="center" border="0" cellspacing="0" cellpadding="4">
			  <tr>
			    <td width="20%" align="right">{*[cn.myapps.core.workflow.name]*}：</td>
			    <s:hidden name="content.flowName" />
			    <td class="flowname">&nbsp;<s:select name="content.flowId"  cssStyle="width:250px" list="#request.workHtmlUtil.getFlowMap()" onchange="onFlowChange(this);" /></td>
			  </tr>
			  <tr>
			    <td align="right">{*[Description]*}：</td>
			    <td class="miaoshu">&nbsp;<s:textarea name="content.description" cssStyle="width:250px" rows="3" wrap="true" theme="simple"/></td>
			  </tr>
			  <tr>
			    <td align="right">{*[core.workflow.storage.runtime.proxy.authorize]*}：</td>
			    <s:hidden name="content.agents" id="content.agents" />
			    <td class="agentsname">&nbsp;<s:textfield name="content.agentsName" id="content.agentsName" cssStyle="width:250px" size="10" readonly="true" theme="simple" /> &nbsp; <a href="javascript:;" class="orgAdd"
							onClick="showUserSelectNoFlow('actionName', {textField:'content.agentsName', valueField:'content.agents'},'')" title="{*[core.workflow.storage.runtime.proxy.chooseUser]*}">{*[Choose]*}</a></td>
			  </tr>
			  <tr>
			    <td align="right">{*[State]*}：</td>
			    <td>&nbsp; <s:radio name="content.state" label="{*[State]*}" list="#{'1':'{*[core.workflow.storage.runtime.proxy.activation]*}','0':'{*[core.workflow.storage.runtime.proxy.disable]*}'}" value="content.state" theme="simple" /></td>
			  </tr>
			</table>
		</s:form>
	</div>
	<div class="button-cmd" style="float:right;margin-right:20px;">
		<a class="applyicon yingyong" onClick="doSave();">{*[Save]*}</a>
	</div>
</body>
</o:MultiLanguage></html>
