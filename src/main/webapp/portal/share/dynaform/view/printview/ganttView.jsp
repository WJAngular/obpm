<%@ page contentType="text/html; charset=UTF-8" errorPage="/portal/share/error.jsp"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@page import="cn.myapps.core.dynaform.view.html.GanttViewHtmlBean"%>
<%@include file="/portal/share/common/lib.jsp"%>
<%
	// 初始化HtmlBean
	GanttViewHtmlBean htmlBean = new GanttViewHtmlBean();
    htmlBean.setHttpRequest(request);
    WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	if("true".equals(request.getAttribute("_isPreview"))){
		webUser = (WebUser)session.getAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER);
	}
    htmlBean.setWebUser(webUser);
    request.setAttribute("htmlBean", htmlBean);
%>
<!DOCTYPE html>
<html style="height:100%;_overflow-x:hidden;">
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/portal/share/common/js_base.jsp"%>
<%@include file="/portal/share/common/js_component.jsp"%>
<link rel="stylesheet" href="<o:Url value='/resource/css/main-front.css'/>" type="text/css" />
<jsp:include page='/resource/css/style.jsp' flush="true">
	<jsp:param name="styleid" value="<%= htmlBean.getViewStyle()%>" />
</jsp:include>
<!-- 
<link rel="stylesheet" href="<o:Url value='/resource/css/style.jsp'/><%=htmlBean.getViewStyle()!=null? "?styleid="+htmlBean.getViewStyle():"" %>" />
 -->
 <!-- View页面常用function,注意先后顺序,先common后view -->
<script src='<s:url value="/portal/default/script/obpm.ui.js"/>'></script>
<script src='<s:url value='/portal/share/component/view/common.js' />'></script>
<script src='<s:url value='/portal/share/component/view/view.js' />'></script>
<script src='<s:url value="/dwr/interface/FormHelper.js"/>'></script>
<script src='<s:url value="/dwr/interface/ViewHelper.js"/>'></script>
<script src='<s:url value="/portal/share/component/dateField/datePicker/WdatePicker.js" />' ></script>
<script src='<s:url value="/portal/share/component/view/obpm.ganttView.js" />' ></script>
<!-- 甘特图控件 -->
<link rel='stylesheet' href='<s:url value="/portal/share/component/view/jQuery.Gantt/style.css" />' />
<script src='<s:url value="/portal/share/component/view/jQuery.Gantt/jquery.fn.gantt.js" />'></script>

<script type="text/javascript">
var contextPath = '<%= request.getContextPath()%>';
var operation = '<s:property value="%{#parameters.operation}" />';
var isedit = '';
var enbled='';
var selectStr = '{*[Select]*}';	//viewDoc() createDoc()
var openDownWinStr = '<s:property value="%{#request.excelFileName}"/>';
var typeName = '<s:property value="%{#request.message.typeName}" />';	//showPromptMsg()
var urlValue = '<s:url value="%{#request.ACTIVITY_INSTNACE.actionUrl}">'+
	'<s:param name="_activityid" value="%{#request.ACTIVITY_INSTNACE.id}" /></s:url>';	//showPromptMsg()
var ganttText = {
		day : '{*[Day]*}',
		week : '{*[Week]*}',
		month : '{*[Month]*}'
};		//obpmGanttView()

//回选列表数据
function selectData4Doc(){
	var checkboxs = document.getElementsByName("_selects");
	<s:iterator value="_selects">
		for (var i=0; i<checkboxs.length; i++) {
			var checkedId = '<s:property />';
			if (checkboxs[i].value == checkedId) {
				checkboxs[i].checked = true;
			}
		}
	</s:iterator>
}

function doPrint() {
	window.print();
}

jQuery(document).ready(function(){
	initGantComm();		//甘特视图公用初始化方法
});
</script>
<title>detail</title>
</head>
<body class="body-front front-scroll-hidden front-bgcolor1" style="height:100%;">
<s:form id="formList" name="formList" action="displayView" method="post" theme="simple">
	<table width="100%" border="1" cellspacing="0" cellpadding="0" id="oLayer">
		<tr valign='top'>
			<td>
				<table width="100%" border=0 cellpadding="0" cellspacing="5">
					<tr>
						<td class="line-position" width="100%">&nbsp;</td>
						
						<td align="right"><input id="button_act" type="button"
							class="button-document" alt="print" value="{*[Print]*}"
							onclick="doPrint()" /></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
<div id="install" style="display:none">
     <a id="hreftest2" href="<s:url value='/portal/share/component/signature/iSignatureHTML.zip'/>"><font color="red"><b>&nbsp;&nbsp;&nbsp;点击下载金格iSignature电子签章HTML版软件</b></font></a> 
</div>
<%
    if(htmlBean.isSignatureExist()) {
%>
	<%@include file="/portal/share/dynaform/view/batchSignatureObject.jsp"%>
<%
    }
%>
	<%@include file="/common/list.jsp"%>
	<s:url id="backURL" action="displayView" >
		<s:param name="_viewid" value="#parameters._viewid" />
		<s:param name="parentid" value="#parameters.parentid" />
		<s:param name="_currpage" value="datas.pageNo"/>
		<s:param name="application" value="#parameters.application[0]" />
		<s:param name="isRelate" value="#parameters.isRelate[0]"/>
		<s:param name="divid" value="%{#parameters.divid}" />
	</s:url>
	
	<!-- 一些供javascript使用的参数 document.getElementById来获取 -->
	<s:hidden name="isedit" id="isedit" value="%{#parameters.isedit}" />
	<s:hidden name="isenbled" id="isenbled" value="%{#parameters.isenbled}" />
	<!-- 当前视图对应的菜单编号 -->
	<s:hidden id="resourceid" name="_resourceid" value="%{#parameters._resourceid}" />
	
	<!-- 电子签章参数 -->
	<s:hidden name="signatureExist" id="signatureExist"
	value="%{#request.htmlBean.isSignatureExist()}"></s:hidden>
	<!-- 电子签章参数 -->
	<s:set name="sinfo" value="#request.htmlBean.getSignatureInfo(datas)"/>
	<s:hidden name="FormID" id="FormID" value="%{#sinfo.FormID}" ></s:hidden>
	<s:hidden name="ApplicationID" id="ApplicationID" value="%{#sinfo.ApplicationID}" ></s:hidden>
	<s:hidden name="DocumentID" id="DocumentID" value="%{#sinfo.DocumentID}" ></s:hidden>
	<s:hidden name="mGetBatchDocumentUrl" id="mGetBatchDocumentUrl" value="%{#sinfo.mGetBatchDocumentUrl}" ></s:hidden>
	<s:hidden name="mLoginname" id="mLoginname" value="%{#session.FRONT_USER.loginno}"></s:hidden>
	
	<s:textarea name="message" value="%{#request.message.content}" cssStyle="display:none" />
	<input type="hidden" name="_backURL" value="<%=request.getAttribute("backURL") %>" />
	<s:hidden name="isedit" value="%{#parameters.isedit}" />
	<input type="hidden" name="_pageCount" value='<s:property value="datas.pageCount"/>' />
	<s:hidden name="_isdiv" value="%{#parameters.isDiv}" />
	<s:hidden name="parentid" value="%{#parameters.parentid}" />
	<s:hidden name="isRelate" value="%{#parameters.isRelate}"/>
	<s:hidden name="currentDate" value="%{#parameters.currentDate}" />
	<s:hidden name="viewEvent" value="%{#parameters.viewEvent}" />
	<input type="hidden" name="_openType" value='<s:property value="content.openType"/>' />
	<s:hidden name="_fieldid" value="%{#parameters._fieldid}" />
	<s:hidden name="isRelate" value="%{#parameters.isRelate}" />
<div id="container" class="front-boder front-scroll-hidden"  style="width:99%;">
	<%@include file="/common/msg.jsp"%>
	<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
	<div id="activityTable" class="front-border-bottom">
		<table class="front-table-act front-table-full-width">
			<tr valign="middle">
			<td>
			<!-- 输出视图操作HTML -->
			<s:property value="#request.htmlBean.toActHtml()" escape="false"/>
			
			<s:hidden name="_viewid" />
			<s:hidden name="divid" value="%{#parameters.divid}" />
			<!--后台查询报错，暂时隐藏--jack ww:hidden name="_sortCol" /> -->
			<s:hidden name="_orderby" />
			<s:hidden name="_sortStatus" />
			</td>
			</tr>
			</table>	
	</div>
	
	<!-- 是否显示查询表单 -->
	<s:if test="#request.htmlBean.showSearchForm">
	<div id="searchFormTable" class="front-scroll-hidden front-bgcolor2" style="padding: 10px;">
		<table class="front-table-full-width">
			<tr>
				<td>
				<!-- 输出查询表单HTML -->
				<s:property value="#request.htmlBean.toSearchFormHtml()" escape="false"/>
				</td>
				
				<!-- 是否显示查询表单按钮 -->
				<s:if test="#request.htmlBean.showSearchFormButton">
				<td style="width:150px;">
					<table>
						<tr>
							<td>
								<span class="button-cmd"><a href="#" onclick="document.getElementsByName('_currpage')[0].value=1;document.forms[0].submit();"><span><img align="middle" src="<o:Url value='/resource/imgv2/front/main/query.gif' />">{*[Query]*}</span></a></span>
							</td>
						</tr>
						<tr>
							<td>
								<span class="button-cmd"><a href="#" onclick="ev_resetAll()"><span><img align="middle" src="<o:Url value='/resource/imgv2/front/main/reset.gif' />">{*[Reset]*}</span></a></span>
							</td>
						</tr>
					</table>
				</td>
				</s:if>
			</tr>
		</table>
	</div>
	</s:if>
		
	<div id="dataTable">
		<div class="gantt"></div>
	</div>
	<s:property value="#request.htmlBean.toHtml()" escape="false"/>	<!-- 生成表格数据  -->
</div>
</s:form>

</body>
</o:MultiLanguage>
</html>
