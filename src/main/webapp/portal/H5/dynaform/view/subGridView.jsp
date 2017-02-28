<%@ page contentType="text/html; charset=UTF-8" errorPage="/portal/share/error.jsp" %>
<%@ page import="cn.myapps.core.dynaform.view.ejb.View"%>
<%@ page import="cn.myapps.core.dynaform.activity.ejb.*"%>
<%@ page import="cn.myapps.core.dynaform.document.ejb.Document"%>
<%@ page import="cn.myapps.base.action.ParamsTable"%>
<%@ page import="cn.myapps.util.StringUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.core.macro.runner.*"%>
<%@page import="cn.myapps.core.dynaform.view.html.ViewHtmlBean"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@include file="/portal/share/common/lib.jsp"%>
<%
	ViewHtmlBean htmlBean = new ViewHtmlBean();
	htmlBean.setHttpRequest(request);
	WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	if("true".equals(request.getAttribute("_isPreview"))){
		user = (WebUser)session.getAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER);
	}
    htmlBean.setWebUser(user);
	request.setAttribute("htmlBean", htmlBean);
	View view = ((View) request.getAttribute("content"));
	
	ParamsTable params = ParamsTable.convertHTTP(request);

	IRunner jsrun = JavaScriptFactory.getInstance(
			params.getSessionid(), view.getApplicationid());

	Document parent = (Document) request.getAttribute("parent");

	String viewid = request.getParameter("_viewid");
	String imageid = "";
	boolean isHeddienPage = true;
	request.setAttribute("runner", jsrun);
	
	boolean isEdit = !StringUtil.isBlank(request.getParameter("isedit")) ? Boolean.parseBoolean(request.getParameter("isedit")) : true;
%>


<%@page import="cn.myapps.core.dynaform.form.ejb.ValidateMessage"%>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/portal/H5/resource/common/js_base.jsp" %>
<%@include file="/portal/H5/resource/common/js_component.jsp" %>
<link rel="stylesheet" href="<s:url value='/portal/H5/dynaform/view/css/view.css'/> " />
<!-- 视图样式 -->
<!-- 注释旧样式
<link rel="stylesheet" href='<o:Url value="/dynaform/view/css/subGridView.css"/>' type="text/css" />
 -->
<!-- 样式库样式 -->
<jsp:include page='../../resource/css/styleLib.jsp' flush="true">
	<jsp:param name="styleid" value="<%= htmlBean.getViewStyle()%>" />
</jsp:include>

<script src='<s:url value="/dwr/interface/DocumentUtil.js"/>'></script>
<script src='<s:url value="/dwr/interface/FormHelper.js"/>'></script>
<script src='<s:url value="/dwr/interface/ViewHelper.js"/>'></script>

<script src='<o:Url value="/resource/js/obpm.ui.js"/>'></script>
<script src='<o:Url value='/resource/component/view/common.js' />'></script>
<script src='<o:Url value='/resource/component/view/grid.js' />'></script>
<script src='<o:Url value='/resource/component/view/obpm.subGridView.js' />'></script>

<!-- 图片滑动控件样式 -->
<link rel="stylesheet" href="<s:url value='/portal/share/css/slider.css' />" type="text/css" />

<script>
var contextPath = '<%= request.getContextPath()%>' ;
var importURL = contextPath + '/portal/share/dynaform/dts/excelimport/importbyid.jsp'; //import Excel
var downloadURL = contextPath + '/portal/share/download.jsp'; // Excel下载URL
var isedit = '';
var enbled='';
var _viewid = '<%=viewid%>';
var isRelate = '<s:property value="#parameters.isRelate" />';
var parentid = '<s:property value="#parameters.parentid" />';
var openDownWinStr = '<s:property value="%{#request.excelFileName}"/>';	//openDownloadWindow()
var typeName = '<s:property value="%{#request.message.typeName}" />';	//showPromptMsg()
var urlValue = '<s:url value="%{#request.ACTIVITY_INSTNACE.actionUrl}">'+
	'<s:param name="_activityid" value="%{#request.ACTIVITY_INSTNACE.id}" /></s:url>';	//showPromptMsg()
var totalRows = '<s:property value="totalRowText" />';  //refreshMenuTotalRows()

jQuery(document).ready(function(){
	dy_lock();	//在方法加载完之前锁定操作
	initGridComm();		//网格视图公用初始化方法
	var checkboxs = document.getElementsByName("_selects");
	<s:iterator value="_selects">
		for (var i=0; i<checkboxs.length; i++) {
			var checkedId = '<s:property />';
			if (checkboxs[i].value == checkedId) {
				checkboxs[i].checked = true;
			}
		}
	</s:iterator>
	dy_unlock();	//方法加载完之后解锁操作
});
</script>
<title>list column by view</title>
</head>
<body>
<!-- 遮挡层 -->
<div id="loadingDivBack" style="position: absolute; z-index: 50; width: 100%; height: 100%; top: 0px; left: 0px; background-color:#ccc; filter: alpha(opacity = 0.1); opacity: 0.1;">
	<div style="position: absolute;top: 35%;left: 45%;width: 128px;height: 128px;z-index: 100;">
		<img src="<o:Url value='/resource/main/images/loading1.gif'/>"/>
	</div>
</div>
<input type="hidden" id="obpm_subGridView" />
<div id="right" style="background:none">
	<div class="crm_right">
    	<div class="searchDiv">
		<s:form id="formList" name="formList" action="displayView" method="post" theme="simple">
			<!-- 电子签章 -->
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
				<s:param name="divid" value="%{#parameters.divid}" />
				<s:param name="_currpage" value="datas.pageNo"/>
				<s:param name="isSubDoc" value="true"/>
				<s:param name="application" value="#parameters.application[0]" />
			</s:url>
			
			<%@include file="../../resource/common/msg.jsp"%>
			<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
				<%@include file="/portal/share/common/msgbox/msg.jsp"%>
			</s:if>
			
			<div id="acttable" style="z-index: 10;width: 100%;background-color: #fff;">
				<s:textarea name="message" value="%{#request.message.content}" cssStyle="display:none" />
				<input type="hidden" name="_backURL" value="<%=request.getAttribute("backURL") %>" />
				<!-- 翻页使用--start -->
				<input type="hidden" name="_pageCount" value='<s:property value="datas.pageCount"/>' />
				<!-- 翻页使用--end -->
				<s:hidden name="isedit" value="%{#parameters.isedit}" />
			
				<s:hidden name="parentid" value="%{#parameters.parentid}" />
				<s:hidden name="isRelate" value="%{#parameters.isRelate}" />
				<s:hidden name="_viewid" />
				<s:hidden name="divid" value="%{#parameters.divid}" />
				<s:hidden name="_sortCol" />
				<s:hidden name="_orderby" />
				<s:hidden name="_sortStatus" />
				<s:hidden name="_isSubDoc" value="true" />
				<s:hidden name="_fieldid" value="%{#parameters._fieldid}" />
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
	            <div class="nav nav-pills" style="float: left;">
					<!-- 输出视图操作HTML -->
					<s:property value="#request.htmlBean.toActHtml()" escape="false"/>
	            </div>
				<s:if test="_isPagination == 'true' || _isShowTotalRow == 'true'">
				<div style="float: right;">
					<s:if test="_isPagination == 'true'">
						<ul class="pagination" style="margin: 0px;vertical-align: bottom;">
						<s:if test="datas.pageNo  > 1">
							<li><a class="showFirstPage"><span aria-hidden="true" title="{*[FirstPage]*}">&lt;&lt;</span></a></li>
							<li><a class="showPreviousPage"><span aria-hidden="true" title="{*[PrevPage]*}">&lt;</span></a></li>
						</s:if>
						<s:else>
							<li class="disabled"><a class="showFirstPage"><span aria-hidden="true" title="{*[FirstPage]*}">&lt;&lt;</span></a></li>
							<li class="disabled"><a class="showPreviousPage"><span aria-hidden="true" title="{*[PrevPage]*}">&lt;</span></a></li>
						</s:else>
							<li><a><s:property value='datas.pageNo' />{*[Page]*}&nbsp;{*[Total]*}<s:property value='datas.pageCount' />{*[Page]*}</a></li>
						<s:if test="datas.pageNo < datas.pageCount">
							<li><a class="showNextPage"><span aria-hidden="true" title="{*[NextPage]*}">&gt;</span></a></li>
							<li><a class="showLastPage"><span aria-hidden="true" title="{*[EndPage]*}">&gt;&gt;</span></a></li>
						</s:if>
						<s:else>
							<li class="disabled"><a class="showNextPage"><span aria-hidden="true" title="{*[NextPage]*}">&gt;</span></a></li>
							<li class="disabled"><a class="showLastPage"><span aria-hidden="true" title="{*[EndPage]*}">&gt;&gt;</span></a></li>
						</s:else>
	 						</ul>
	
						<div class="input-group" style="display: inline-table;width: 120px;vertical-align: bottom;">
							<input type="text" name="_jumppage"  class="form-control" style="width:51px;">
							<span class="input-group-btn">
								<input type="button" class="btn btn-default btn-jumppage" value="跳转" />
							</span>
						</div>
					</s:if>
					<s:if test="_isShowTotalRow == 'true'">
						<span style="height: 34px;padding: 6px 12px;border: 1px solid #ccc;display: inline-block;border-radius: 4px;">{*[TotalRows]*}:(<s:property value="totalRowText" />)</span>
					</s:if>
				</div>
				</s:if>
				<div style="clear: both;margin-bottom:10px;"></div>
				<div>
				<!-- 是否显示查询表单 -->
				<s:if test="#request.htmlBean.showSearchForm">
				<!-- 要在BackURL传递的参数放在 searchFormTable-->
			            <div class="container-fluid">
							<!-- 输出查询表单HTML -->
							<s:property value="#request.htmlBean.toSearchFormHtml()" escape="false"/>
			                   <div class="row">
			                       <div class="col-xs-12 text-center">
			                           <button type="button" class="btn ico-search" onclick="modifyActionBack();">{*[Query]*}</button>
			                          	&nbsp;&nbsp;&nbsp;&nbsp;
			                           <button type="button" class="btn ico-reset btn-reset" onclick="ev_resetAll()">{*[Reset]*}</button>
			                       </div>
			                   </div>
			            </div>
				</s:if>
				</div>
			</div>
			<div id="table_gridView" class="dataTable" style="overflow:auto;">
			<!-- 后台java代码输出网格视图数据 -->
			<%
				out.print(htmlBean.toHTMLText4Grid());
			%>
			</div>
		</s:form>
    	</div>
	</div>
       
	<!-- page navigate -->
</div>
</body>
</html>
</o:MultiLanguage>
