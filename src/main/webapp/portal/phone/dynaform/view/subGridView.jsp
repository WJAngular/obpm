<%@ page contentType="text/html; charset=UTF-8" errorPage="/portal/share/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="cn.myapps.core.dynaform.view.html.ViewHtmlBean"%>
<%@include file="/portal/share/common/lib.jsp"%>

<%
   	// 初始化HtmlBean
	ViewHtmlBean htmlBean = new ViewHtmlBean();
    htmlBean.setHttpRequest(request);
    WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	if("true".equals(request.getAttribute("_isPreview"))){
		webUser = (WebUser)session.getAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER);
	}
    htmlBean.setWebUser(webUser);
    request.setAttribute("htmlBean", htmlBean);
%>


<%@page import="cn.myapps.core.dynaform.document.ejb.Document"%>
<!DOCTYPE html>
<html style="height:100%;">
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<head>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />

<script src='<s:url value="/dwr/interface/FormHelper.js"/>'></script>
<script src='<s:url value="/dwr/interface/ViewHelper.js"/>'></script>
<link rel="stylesheet" href="<o:Url value='/resource/document/main-front.css'/>" type="text/css" />
<%@include file="/portal/phone/resource/common/js_base.jsp" %>
<%@include file="/portal/phone/resource/common/js_component.jsp" %>
<script src='<o:Url value="/resource/document/obpm.ui.js"/>'></script>
<script src='<o:Url value='/resource/component/view/common.js' />'></script>
<script src='<o:Url value='/resource/component/view/view.js' />'></script>
<script src='<o:Url value='/resource/component/view/obpm.listView.js' />'></script>
<script src='<o:Url value='/resource/js/tableList.js' />'></script>
<script>
var contextPath = '<%= request.getContextPath()%>';
var operation = '<s:property value="%{#parameters.operation}" />';
var isOpenAbleScriptShow = '{*[page.core.dynaform.forddin]*}';	//judgeOperating()
var isedit = '';
var enbled='';
var typeName= '<s:property value="%{#request.message.typeName}" />';	//showPromptMsg()
var urlValue= '<s:url value="%{#request.ACTIVITY_INSTNACE.actionUrl}"><s:param name="_activityid" value="%{#request.ACTIVITY_INSTNACE.id}" /></s:url>';	//showPromptMsg()
var selectStr = '{*[Select]*}';	//createDoc(),viewDoc
var someInformation= '{*[cn.myapps.core.workflow.input_audit_remark]*}';	//on_doflow
var okMessage = '{*[OK]*}';	//on_doflow()
var cancelMessage = '{*[Cancel]*}';	//on_doflow()
var openDownWinStr = '<s:property value="%{#request.excelFileName}"/>';	//openDownloadWindow()
var totalRows = '<s:property value="totalRowText" />';  //refreshMenuTotalRows()

jQuery(document).ready(function(){
	initListComm();	//列表视图公用初始化方法
	tableListColumn();
});

$(function() {
	jQuery.expr[':'].Contains = function(a,i,m){
		return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase())>=0;
	};

	function filterList(header, list) { 
    
    $("input.search")
    	.change(function(){
        	var filter = $(this).val();
        	if(filter) {
				$matches = $(list).find('a:Contains(' + filter + ')').parent();
				$('li', list).not($matches).slideUp();
				$matches.slideDown();
		    } else {
				$(list).find("li").slideDown();
			}
        	return false;
      	})

		.keyup( function () {
			$(this).change();
		});
	}

	$(function () {
		filterList($("#form"), $("#list"));
	});
	
	
});
</script>
<title><%=htmlBean.getViewTitle()%></title>
</head>
<body>
<s:form id="formList" name="formList" action="displayView" method="post" theme="simple">
<div class="reimburse">
<div data-role="page" class="jqm-demos jqm-home" id="listView">
<!-- <div class="selectuser-search">
            <div class="search-box">
                <input class="search pull-left" type="text">
                <button class="btn search-btn pull-left">
                    <span class="icon icon-search"></span>
                </button>
                <div class="clearfix"></div>
            </div>
        </div> -->

<div class="card_app">
<%
	out.print(htmlBean.toHTMLText());
	%>
		
	<div class="ui-content dataTableDiv" name="dataTableDiv" style="overflow:auto">
	</div>
 

</div>



<nav id="footer" class="text-center">

	<!-- 分页导航(page navigate)1 -->


<s:if test="_isPagination == 'true' || _isShowTotalRow == 'true'">
	<ul class="pagination"  style="margin:0;">
		<s:if test="_isPagination == 'true'">
			<s:if test="datas.pageNo  > 1">
				<li><a href='javascript:showFirstPage(null, listAction)'><span title="{*[FirstPage]*}">&lt;&lt;</span></a></li>
				<li><a href='javascript:showPreviousPage(null, listAction)'><span title="{*[PrevPage]*}">&lt;</span></a></li>
			</s:if>
			<s:else>
				<li class="disabled"><a href='javascript:showFirstPage(null, listAction)'><span title="{*[FirstPage]*}">&lt;&lt;</span></a></li>
				<li class="disabled"><a href='javascript:showPreviousPage(null, listAction)'><span title="{*[PrevPage]*}">&lt;</span></a></li>
			</s:else>
			<li><a href='javascript:showFirstPage(null, listAction)'><s:property value='datas.pageNo' />&nbsp;/&nbsp;<s:property value='datas.pageCount' /></a></li>
			<s:if test="datas.pageNo < datas.pageCount">
				<li><a href='javascript:showNextPage(null, listAction)'><span title="{*[NextPage]*}">&gt;</span></a></li>
				<li><a href='javascript:showLastPage(null, listAction)'><span title="{*[EndPage]*}">&gt;&gt;</span></a></li>
			</s:if>
			<s:else>
				<li class="disabled"><a href='javascript:showNextPage(null, listAction)'><span title="{*[NextPage]*}">&gt;</span></a></li>
				<li class="disabled"><a href='javascript:showLastPage(null, listAction)'><span title="{*[EndPage]*}">&gt;&gt;</span></a></li>
			</s:else>
		</s:if>
		<s:if test="_isShowTotalRow == 'true'">
			<!-- <span>{*[TotalRows]*}:(<s:property value="totalRowText" />)</span> -->
		</s:if>
	</ul>
</s:if>

    </nav>
	<!-- 分页导航结束(end of page navigate) -->
 


<div style="height:66px"></div>
     <div class="card_space_fix zindex10">
       <table width="100%"  cellspacing="10">
         <tr>
          <s:property value="#request.htmlBean.toActHtml()" escape="false"/>
			
			<s:if test="#request.htmlBean.showSearchForm">
			<td><a href="#searchForm" class="btn btn-primary btn-block">查询</a></td>
			</s:if>
         </tr>
       </table>

  </div>

</div>



<div data-role="page" id="searchForm" class="modal modal-iframe">

<header class="bar bar-nav">
	<a class="icon icon-close pull-right" id="btn-modal-close" href="#searchForm"></a>
	<h1 class="title">查询</h1>
</header>
<div class="content" <s:if test="#request.htmlBean.showSearchFormButton">style="margin-bottom:57px;"</s:if>>

<div role="main" class="ui-content" id="searchFormTable">
	<s:if test="#request.htmlBean.showSearchForm">
	<!-- 要在BackURL传递的参数放在 searchFormTable-->
		<!-- 输出查询表单HTML -->
	  <div class="card_app">
    <div class="contact-form">	
		<s:property value="#request.htmlBean.toSearchFormHtml()" escape="false"/>
   </div>
  </div>
  <div style="height:57px"></div>
</div>
		</div>	
		<!-- 是否显示查询表单按钮 -->
		<s:if test="#request.htmlBean.showSearchFormButton">
		
		<div class="card_space_fix zindex10">
       <table width="100%"  cellspacing="10">
         <tr>	
         	<td><a onclick="modifyActionBack();" class="btn btn-primary btn-block">{*[Query]*}</a></td>
         	<td><a onclick="ev_resetAll();" class="btn btn-block">{*[Reset]*}</a></td></tr></table></div>
		</s:if>


	</s:if>


</div>
</div>
<div class="tab_parameter">
 <%@include file="/common/list.jsp"%>
	<s:url id="backURL" action="displayView" >
		<s:param name="_viewid" value="#parameters._viewid" />
		<s:param name="_currpage" value="datas.pageNo"/>
		<s:param name="parentid" value="#parameters.parentid" />
		<s:param name="treedocid" value="#parameters.treedocid" />
		<s:param name="isinner" value="#parameters.isinner" />
		<s:param name="_resourceid" value="#parameters._resourceid" />
		<s:param name="application" value="#parameters.application[0]" />
	</s:url>
	
	<!-- 一些供javascript使用的参数 document.getElementById来获取 -->
	<s:hidden name="isedit" value="%{#parameters.isedit}" />
	<s:hidden name="isenbled" value="%{#parameters.isenbled}" />
	
	<!-- 当前视图对应的菜单编号 -->
	<s:hidden id="resourceid" name="_resourceid" value="%{#parameters._resourceid}" />
	
	<!-- 电子签章参数 -->
	<s:hidden name="signatureExist" id="signatureExist"
	value="%{#request.htmlBean.isSignatureExist()}"></s:hidden>
	<s:set name="sinfo" value="#request.htmlBean.getSignatureInfo(datas)"/>
	<s:hidden name="FormID" id="FormID" value="%{#sinfo.FormID}" ></s:hidden>
	<s:hidden name="ApplicationID" id="ApplicationID" value="%{#sinfo.ApplicationID}" ></s:hidden>
	<s:hidden name="DocumentID" id="DocumentID" value="%{#sinfo.DocumentID}" ></s:hidden>
	<s:hidden name="mGetBatchDocumentUrl" id="mGetBatchDocumentUrl" value="%{#sinfo.mGetBatchDocumentUrl}" ></s:hidden>
	<s:hidden name="mLoginname" id="mLoginname" value="%{#session.FRONT_USER.loginno}"></s:hidden>
	
	<s:textarea name="message" value="%{#request.message.content}" cssStyle="display:none" />
	<input type="hidden" name="_backURL" value="<%=request.getAttribute("backURL") %>" />
	<!-- <s:hidden name="isedit" value="%{#parameters.isedit}" /> -->
	<input type="hidden" name="_pageCount" value='<s:property value="datas.pageCount"/>' />
	<s:hidden name="_isdiv" value="%{#parameters.isDiv}" />
	<input type="hidden" name="divid" value="{#parameters.divid}" />
	<s:hidden name="tabid" id="tabid" value=""/>
	<s:hidden name="currentDate" value="%{#parameters.currentDate}" />
	<s:hidden name="viewEvent" value="%{#parameters.viewEvent}" />
	<input type="hidden" name="_openType" value='<s:property value="content.openType"/>' />
	<s:hidden name="_fieldid" value="%{#parameters._fieldid}" />
	<!-- 父表单ID参数 -->
	<s:hidden name="parentid" value="%{#parameters.parentid}" />
	<!-- 树形视图参数 -->
	<s:hidden id="treedocid" name="treedocid" value="%{#parameters.treedocid}" />
	<!-- 内嵌视图参数 -->
	<s:hidden id="isinner" name="isinner" value="%{#parameters.isinner}" />
	
<%@include file="/common/msg.jsp"%>
		<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>

			<s:hidden id="viewid" name="_viewid" />
			<s:hidden name="_sortCol" />
			<s:hidden name="_orderby" />
			<s:hidden name="_sortStatus" />
			
				<!-- 数据表格 -->
		<textarea id='_remark' type='text' style='display:none;' name='_remark'></textarea>
		<div id='doFlowRemarkDiv' style='display:none;width:280px;' title='{*[cn.myapps.core.dynaform.view.input_audit_remark]*}'>
		<textarea id='temp_remark' rows='12' cols='35' name='temp_remark' style='width:97%;'></textarea></div>
			
</div>
</s:form>
</body>
</o:MultiLanguage>
</html>
