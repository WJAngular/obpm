<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/portal/share/error.jsp"%>
<%@ page import="cn.myapps.core.dynaform.activity.ejb.*"%>
<%@ page import="cn.myapps.core.dynaform.document.ejb.Document"%>
<%@ page import="cn.myapps.core.dynaform.document.html.DocumentHtmlBean"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="cn.myapps.core.workflow.engine.StateMachineHelper"%>
<%@ page import="cn.myapps.core.dynaform.document.action.DocumentHelper"%>
<%@ page import="cn.myapps.core.workflow.storage.runtime.ejb.NodeRT" %>
<%@ page import="cn.myapps.core.workflow.engine.StateMachine" %>
<%@include file="/portal/share/common/lib.jsp"%>
<s:bean name="cn.myapps.core.privilege.operation.action.OperationHelper" id="oh" />
<s:url id="backURL" value="/portal/dispatch/closeTab.jsp" >
	<s:param name="application" value="#parameters.application" />
</s:url>
<s:url id="viewDocURL" action="view" namespace="/portal/dynaform/document"></s:url>
<s:url id="moreDocURL" action="moreDoc" namespace="/portal/dynaform/document">
	<s:param name="application" value="#parameters.application" />
	<s:param name="summaryCfgId" value="%{#parameters.summaryCfgId}" />
</s:url>
<%
	String contextPath = request.getContextPath();

	Document doc = (Document) request.getAttribute("content");
	String nodeValue = (String) request.getParameter("node");
	String superior_node_fieldName = (String) request
			.getParameter("super_node_fieldName");
	if (nodeValue != null && superior_node_fieldName != null) {
		doc.addStringItem(superior_node_fieldName, nodeValue);
	}
	WebUser webUser = (WebUser) session
			.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	
	if("true".equals(request.getAttribute("_isPreview"))){
		webUser = (WebUser)session.getAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER);
	}

	DocumentHtmlBean dochtmlBean = new DocumentHtmlBean();
	dochtmlBean.setHttpRequest(request);
	dochtmlBean.setHttpResponse(response);
	dochtmlBean.setWebUser(webUser);
	request.setAttribute("dochtmlBean", dochtmlBean);
	
	String nodeId = "";
	if (doc.getStateid() != null && doc.getStateid().length()>0) {
		String defaultNodeId = (String)request.getAttribute("_targetNode");
		if(defaultNodeId !=null && defaultNodeId.length()>0){ 
			nodeId = defaultNodeId;
		}else{
			NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, webUser,defaultNodeId);
			if(nodert != null){
				nodeId = nodert.getNodeid();
			}
		}
	}
	//use in signatureobject.jsp
	String view_id = request.getParameter("_viewid");
	String mDoCommandUrl = dochtmlBean.getMDoCommandUrl();
	
	dochtmlBean.isOpenAble(view_id, contextPath);
%>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<%
	String printerid = null;
	String printerWfid = null;
	Activity flexPrintAct = dochtmlBean.getFlexPrintAct();
	Activity flexPrintWFHAct = dochtmlBean.getFlexPrintWFHAct();
	if (flexPrintAct != null) {
		printerid = flexPrintAct.getOnActionPrint();
	}
	if (flexPrintWFHAct != null) {
		printerWfid = flexPrintWFHAct.getOnActionPrint();
	}
%>
<!DOCTYPE html>
<html>
<head>
<title><%=dochtmlBean.getForm().getDiscription()%></title>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<%@include file="../../resource/common/js_base.jsp"%>
<%@include file="../../resource/common/js_component.jsp"%>
<script src='<s:url value="/dwr/interface/FormHelper.js"/>'></script>
<script src='<s:url value="/dwr/interface/ViewHelper.js"/>'></script>
<script src='<s:url value="/dwr/interface/StateMachineUtil.js"/>'></script>
<script src='<s:url value="/dwr/interface/RoleUtil.js"/>'></script>
<script src='<s:url value="/portal/phone/resource/document/obpm.ui.js"/>'></script>
<script src='<s:url value='/portal/phone/resource/js/tableList.js' />'></script>
<script src='<s:url value='/portal/phone/resource/js/iscroll.js' />'></script>
<script type="text/javascript" src='<s:url value="/portal/phone/resource/document/document.js"/>'></script>
<script src='<s:url value='/portal/phone/resource/js/cookie.js' />'></script>
	
<script type="text/javascript">
//设置cookie，浏览器通过后退返回到视图时可正常刷新
if(typeof(cook) == "object" && typeof(cook.setCookie) == "function"){
	cook.setCookie("viewurl",document.referrer);
}

var contextPath = '<%=contextPath%>';
var queryString = "<%=request.getQueryString()%>";
var contentId = '<s:property value="content.id" />';	//Signatures4Judge()
var typeName = '<s:property value="%{#request.message.typeName}" />';	//showPromptMsg()
var urlValue = '<s:url value="%{#request.ACTIVITY_INSTNACE.actionUrl}">'+
	'<s:param name="_activityid" value="%{#request.ACTIVITY_INSTNACE.id}" /></s:url>';	//showPromptMsg()
var application = '<%=request.getParameter("application")%>';	//email_transpond(),viewDoc()
var docidR = '<%=request.getParameter("_docid") %>';	//email_transpond()
var formidR = '<%=request.getParameter("_formid") %>';	//email_transpond()
var super_node_fieldNameR = '<s:property value="#parameters.super_node_fieldName"/>';	//Initialization4Node()
var nodeR = '<s:property value="#parameters.node"/>';	//Initialization4Node()
var escapeR = '<s:property value="#moreDocURL" escape="false"/>';	//doMoreDocR()
var viewDocUrl = '<s:property value="#viewDocURL" escape="false"/>'; //viewDoc()
var backUrl = '<s:property value="#backURL" escape="false"/>';	//viewDoc()
var closeStr = '{*[Close]*}';	//showHistoryRecord
var HistoryRecord ='{*[History]*}{*[Record]*}';	//showHistoryRecord


$(document).ready(function() {
	dy_lock();
	removeTitle();
    if (self != top) {
    	$("#formContent").prepend("<div class='formContent-Box'><div class='formContent-is'></div></div>")
    	$("#formContent>.card_app").each(function(){
    		$(this).appendTo($(".formContent-is"))
    	});
    	$(".formContent-Box").next().appendTo($(".formContent-is"));
		var	screenHeight = window.screen.availHeight
    	var buttonHeight = $("#div_button_box").outerHeight();
    	
    	$("#formContent").css({"position":"absolute","width":"100%","top":"0px","bottom":"0px"});
    	$("#div_button_box").css({"position":"absolute"});
    	
    	var u = navigator.userAgent, app = navigator.appVersion;
		var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
		if(isiOS){
			var myScroll;				
	    	window.onload = function() {	
	    		setTimeout(
	    			function(){
	    				myScroll = new IScroll('.formContent-Box', { 
							preventDefault: false,
							preventDefaultException: { tagName: /^(INPUT|TEXTAREA|BUTTON|SELECT|A|SPAN|IMG)$/ }
						})
	    				dy_unlock();
	    			}
	    			,2000); 
	    	}; 
	    	document.addEventListener('touchmove', function (e) {
	    		e.preventDefault(); 
	    	}, false);

		}else{
			$(".formContent-Box").css("overflow","auto");
			dy_unlock();
		}
		setTimeout(function(){
			if($("#form_tab").find("a").size()>0){
				$(".formContent-Box").css("top","37px");
			}
		},2000); 
    }else{
    	dy_unlock();
    }
}); 
var removeTitle = function(){
	var title = $("td[title='SVNNO:']");
	title.html("<font color='red'>广东思程科技有限公司  | 管理平台</font>");
};
</script>
</head>
<body onload="dy_unlock()">
<!-- loading -->
<div id="loadingDivBack">
	<div class="spinner">
		<div class="bounce1"></div>
		<div class="bounce2"></div>
		<div class="bounce3"></div>
	</div>
</div>	

<!-- loadingtoast -->
<div id="loadingToast" class="weui_loading_toast" style="display: none;">
	<div class="weui_mask_transparent"></div>
	<div class="weui_toast">
		<div class="weui_loading">
			<div class="weui_loading_leaf weui_loading_leaf_0"></div>
			<div class="weui_loading_leaf weui_loading_leaf_1"></div>
			<div class="weui_loading_leaf weui_loading_leaf_2"></div>
			<div class="weui_loading_leaf weui_loading_leaf_3"></div>
			<div class="weui_loading_leaf weui_loading_leaf_4"></div>
			<div class="weui_loading_leaf weui_loading_leaf_5"></div>
			<div class="weui_loading_leaf weui_loading_leaf_6"></div>
			<div class="weui_loading_leaf weui_loading_leaf_7"></div>
			<div class="weui_loading_leaf weui_loading_leaf_8"></div>
			<div class="weui_loading_leaf weui_loading_leaf_9"></div>
			<div class="weui_loading_leaf weui_loading_leaf_10"></div>
			<div class="weui_loading_leaf weui_loading_leaf_11"></div>
		</div>
		<p class="weui_toast_content">数据加载中</p>
	</div>
</div>

<%@include file="../../resource/common/msg.jsp"%>
<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
	<%@include file="/portal/share/common/msgbox/msg.jsp"%>
</s:if>
	<div class="tab-box swiper-container" style="position: fixed;z-index: 5">
	<div class="segmented-control reimburse swiper-wrapper" id="form_tab" style="display: none;overflow: initial;"></div>
</div>
<div class="tab-box-height" style="background:#fff"></div>

<form id='document_content' name='document_content' action="save" method="post">
	<div class="reimburse" id="form_continer">
		<span id="item1mobile" class="control-content active">
			<div data-role="page" class="jqm-demos jqm-home" id="formContent">
			
				<!--流程历史 -->
				<div class="weui_panel" id="flowhis_panel" style="display: none;">
					<div class="table text-center" id="flowhis_panel_content"></div>
				</div>
				
				<div class="card_app">
					<div id="_formHtml" class="contact-form">
   					<%
						out.print(dochtmlBean.getFormHTML());
					%>
					</div>
				</div>

				<!--BEGIN 催办流程面板-->
				<div class="flowReminderDiv" id="flowReminderDiv"
					style="display: none;">
					<div class="weui_mask" style="z-index: 15"
						onclick="jQuery(this).parent().hide();")></div>
					<div class="weui_dialog" style="z-index: 25">
						<div class="weui_dialog_hd">
							<strong class="weui_dialog_title">{*[cn.myapps.core.dynaform.document.reminder]*}</strong>
						</div>
						<div class="weui_dialog_bd">
							<span class="pull-left"></span> <span class="pull-right"></span>
						</div>
						<div class="weui_dialog_bd">
							<input name="_reminderContent" class="flowReminder_content"
								type="text"></input>
						</div>
						<div class="weui_dialog_ft">
							<a class="weui_btn_dialog default flowReminder_cancel">{*[cn.myapps.core.dynaform.document.reminder.cancel]*}</a>
							<a class="weui_btn_dialog primary flowReminder_submit">{*[cn.myapps.core.dynaform.document.reminder]*}</a>
						</div>
					</div>
				</div>
				<!--END 催办流程面板-->
				<!--BEGIN toast-->
				<div id="toast" style="display: none;">
					<div class="weui_mask_transparent"></div>
					<div class="weui_toast">
						<i class="weui_icon_toast"></i>
						<p class="weui_toast_content">{*[cn.myapps.core.dynaform.document.reminder.sended]*}</p>
					</div>
				</div>
				<!--end toast-->

					<div style="height:57px"></div>
				<div id="div_button_box" class="card_space_fix zindex10">
					<table>
         				<tr class="formActBtn">
							<s:if test="#request.dochtmlBean.getActivitiesSize()>0">
								<s:property value="#request.dochtmlBean.getActBtnHTML()" escape="false" />
							</s:if>
						</tr>
					</table>
				</div>
				<div data-role="content" class="ui-content">
					<input type="hidden" id="dwzUnbind" value="" />
					<input type="hidden" id="printerid" value="<%=printerid%>" />
					<input type="hidden" id="printerWfid" value="<%=printerWfid%>" />
					<input type="hidden" id="handleUrl" name="handleUrl" value="" />
					<s:hidden name="_templateForm" value="%{#parameters._templateForm}" />
					<input type="hidden" id="_flowType" name="_flowType" value="80" />
					<input type="hidden" id="_currid" name="_currid" value="<%=nodeId %>" />
					<!-- 控件 -->
					
					
					<!-- 参数 -->
					<input type="hidden" name="_flowid" id="_flowid" value="<%=dochtmlBean.getFlowId()%>" />
						<%
							if (dochtmlBean.getDoc().getState() != null) {
											DocumentHelper dh = new DocumentHelper();
						%>
						<s:hidden id="auditorList" name="content.auditorList" />
						<s:hidden id="isSubDoc" name="isSubDoc" value="true" />
					<%
						}
					%>
					<%@include file="/common/page.jsp"%>
					<s:token name="document.token" /> <s:hidden name="content.applicationid" />
					<s:hidden name="content.stateid" /> <input type="hidden" name="flowid" id="flowid" value="<%=dochtmlBean.getFlowId()%>" />
					<s:if test="#request.dochtmlBean.getParams().getParameterAsArray('view_id')[0] !=null && #request.dochtmlBean.getParams().getParameterAsArray('view_id')[0] !=''">
						<input type="hidden" id="view_id" name="view_id" value='<s:property value="params.getParameterAsArray('view_id')[0]" />' />
					</s:if> 
					<s:else>
						<s:hidden id="_viewid" value="%{#parameters._viewid}" />
						<s:hidden id="view_id" name="view_id" value="%{#parameters._viewid}" />
					</s:else> 
					<s:textarea name="message" value="%{#request.message.content}" cssStyle="display:none" /> <!-- 隐藏属性 --> 
					<input type="hidden" id="sub_divid" />
					<s:hidden name="signatureExist" id="signatureExist" value="%{#request.dochtmlBean.isSignatureExist()}"></s:hidden>
					<s:hidden name="formid" id="formid" value="%{#parameters._formid}"></s:hidden>
					<input type="hidden" name="applicationid" id="applicationid" value="<%=doc.getApplicationid()%>" />
					<s:hidden name="mGetDocumentUrl" id="mGetDocumentUrl" value="%{#request.dochtmlBean.getMGetDocumentUrl()}"></s:hidden>
					<input type="hidden" name="mLoginname" id="mLoginname" value="<%=webUser.getLoginno()%>" />
					<s:hidden id="openType" name="openType" value="%{#parameters.openType}" />
					<s:hidden id="operation" name="operation" value="%{#parameters.operation}" /> 
					<s:if test="#parameters._docid!=null && #parameters._docid!=''">
						<s:hidden name="_docid" id="_docid" value="%{#parameters._docid}" />
					</s:if>
					<s:else>
						<input type="hidden" name="_docid" id="_docid" value="<%=doc.getId() %>" />
					</s:else>
					<s:hidden name="isRelate" value="%{#parameters.isRelate[0]}" />
					<s:hidden name="_formid" id="_formid" value="%{#parameters._formid}" />
					<s:hidden name="isStartFlow" value="true" />
					<s:hidden name="domainid" value="%{#parameters.domain}" /> <!-- 当前表单对应的菜单编号 -->
					<s:hidden id="resourceid" name="_resourceid" value="%{#parameters._resourceid}" />
					
					<!-- for calendar_view -->
					<s:hidden name="currentDate" value="%{#parameters.currentDate}" />
					<s:hidden name="content.versions" id="content.versions" />
					<s:hidden name="content.mappingId" />
					<s:hidden name="parentid" value="%{#parameters.parentid}" />
			 		<s:hidden id="_backURL" name="_backURL" value="%{#parameters._backURL[0]}" />
					<s:hidden name="divid" value="%{#parameters.divid}" />
					<s:hidden name="tabid" id="tabid" value="" />
					<input type="hidden" name="defVal" /> <!-- 树形视图参数 -->
					<s:hidden id="treedocid" name="treedocid" value="%{#parameters.treedocid}" /> <!-- 内嵌视图参数 -->
					<s:hidden id="isinner" name="isinner" value="%{#parameters.isinner}" />
					<s:hidden id="isedit" name="isedit" value="%{#parameters.isedit[0]}"/>
					
					<!-- begin system field -->
					<s:hidden name="content.authorDeptIndex" />
					<s:hidden name="content.stateInt" />
					<s:hidden name="content.istmp" />
					<s:hidden name="content.lastmodified" />
					<s:hidden name="content.auditdate" />
					<s:hidden name="content.author.id" />
					
					<s:hidden name="content.created">
						<s:param name="value">
							<s:date name="content.created"/>
						</s:param>
					</s:hidden>
					<s:hidden name="content.stateLabel" />
					<s:hidden name="content.initiator" />
					<s:hidden name="content.audituser" />
					<s:hidden name="content.authorId" />
					<s:hidden name="content.lastFlowOperation" />
					<s:if test="%{#request._targetNodeRT}"><s:hidden name="targetNodeRT_id" value="%{#request._targetNodeRT.id}"/></s:if>
					<s:if test="%{#request._targetNodeRT}"><s:hidden name="targetNodeRT_name" value="%{#request._targetNodeRT.name}"/></s:if>
					
					<!-- end system field  -->
			</div>
		</div>
		<div data-role="page" class="modal modal-iframe jqm-demos jqm-home" id="flowView" style="background-color:#ebebeb;">
			<div role="main" class="ui-content" id="flowProContent">
				<%@include file="../../resource/workflow/runtime/flowProcessPanel.jsp"%>
			</div>
		</div>
		<!--查看流程历史模态框  -->
		<div data-role="page" class="modal modal-iframe" id="viewHistory">
<header class="bar bar-nav"><a class="icon icon-close pull-right" id="btn-modal-close" href="#viewHistory"></a><h1 class="title">流程历史</h1></header>
			<div class="content" style="background-color:#ebebeb;">
<div class="card_space" id="flowhis_modal_content">
</div>
</div>
</div>
</span>

  </div>

</form>
	</body>
	<script src='<s:url value="/portal/phone/resource/component/weixin/weixin.jsApi.js"/>'></script>
	<script type="text/javascript">
	var HAS_SUBFORM = false;
	var visit_from_weixin = '<s:property value="#session.visit_from_weixin"/>';	
	if(visit_from_weixin !="true"){
		initFormCommon();	//表单公用的初始化方法
	}
	if(jQuery("input[name='content.stateid']").val().length>0){
		jQuery(".flowbtn").show();
	}
	</script>
	
</html>
</o:MultiLanguage>