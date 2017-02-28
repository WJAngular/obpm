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
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/portal/H5/resource/common/js_base.jsp" %>
<%@include file="/portal/H5/resource/common/js_component.jsp" %>
<!-- 视图样式 -->

<!-- 屏蔽旧样式-->
<link rel="stylesheet" href='<o:Url value="/dynaform/view/css/collapsibleView.css"/>' type="text/css" />
 <link rel="stylesheet" href='<o:Url value="/resource/css/view.css"/>' type="text/css" />
<!-- 样式库样式 -->
<jsp:include page='../../resource/css/styleLib.jsp' flush="true">
	<jsp:param name="styleid" value="<%= htmlBean.getViewStyle()%>" />
</jsp:include>
	<!-- View页面常用function,注意先后顺序,先common后view 22-->
<script src='<o:Url value="/resource/js/obpm.ui.js"/>'></script>
<script src='<o:Url value='/resource/component/view/common.js' />'></script>
<script src='<o:Url value='/resource/component/view/view.js' />'></script>
<script src='<o:Url value='/resource/component/view/obpm.collapsibleView.js' />'></script>
<script src='<s:url value="/dwr/interface/FormHelper.js"/>'></script>
<script src='<s:url value="/dwr/interface/ViewHelper.js"/>'></script>
<script src='<s:url value="/portal/share/script/cookie.js" />' ></script>
<!-- 图片滑动控件样式 -->
	<link rel="stylesheet" href="<s:url value='/portal/share/css/slider.css' />" type="text/css" />
<script>
//设置cookie，浏览器通过后退返回到视图时可正常刷新
if(typeof(cook) == "object" && typeof(cook.reloadByCookie) == "function"){
	cook.reloadByCookie();
}

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
	
	jQuery(window).resize(function(){
		listViewAdjustLayout();
	});

	jQuery(document).ready(function(){
		dy_lock();	//在方法加载完之前锁定操作
		initListComm();	//列表视图公用初始化方法
		/**
		adjustDataIteratorSize();
		setTimeout(function(){
			listViewAdjustLayout();
		},10);	//调整当前窗口布局
		**/
		dy_unlock();	//方法加载完之后解锁操作
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
		
		//把表头克隆后插入到fixed中
		
		
		var td_width = $("#listDataTh_b tr:first td").length;
		for(i=0;i<td_width;i++)
		{
			$("#listDataTh_a tr:first td").eq(i).width($("#listDataTh_b tr:first td").eq(i).width());
		}
		
		var top = $('#tableFix').offset().top - parseFloat($('#tableFix').css('marginTop').replace(/auto/, 0)) -115;
	    var footTop = $('#footer').offset().top - parseFloat($('#footer').css('marginTop').replace(/auto/, 0));
	    var maxY = footTop - $('#tableFix').outerHeight();
	    $(window).scroll(function(evt) {
	        var y = $(this).scrollTop();  
	        if (y > top) {
	            if (y < maxY) {
	            	if($("#tableFix tbody").html() == "" || $("#tableFix tbody").html().trim() == ""){
		            	$("#tableFix tbody").append($("#dataTable .listDataTh").clone(true));
	            	}
	                $('#tableFix').addClass('fixed').removeAttr('style');
	            } else {
	                $('#tableFix').removeClass('fixed').css({
	                    position: 'absolute',
	                    top: (maxY - top) + 'px'
	                });
	            }
	        } else {
	            $('#tableFix').removeClass('fixed');
	        }
	    });
	});
</script>
<title>{*[cn.myapps.core.dynaform.view.collapsible_view]*}</title>
</head>
<body>
<!-- 遮挡层 -->
<div id="loadingDivBack" style="position: absolute; z-index: 50; width: 100%; height: 100%; top: 0px; left: 0px; background-color:#ccc; filter: alpha(opacity = 0.1); opacity: 0.1;">
	<div style="position: absolute;top: 35%;left: 45%;width: 128px;height: 128px;z-index: 100;">
		<img src="<o:Url value='/resource/main/images/loading1.gif'/>"/>
	</div>
</div>
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
<div id="right" style="background:none">
	<div class="crm_right">
		<%@include file="../../resource/common/msg.jsp"%>
		<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
        <div id="activityTable" style="position: fixed;z-index: 10;top: 0px;left: 15px;right:15px;background-color: #fff;">
            <div class="searchDiv">
                <ul class=" nav-pills">
					<!-- 输出视图操作HTML -->
					<s:property value="#request.htmlBean.toActHtml()" escape="false"/>
					<s:hidden id="viewid" name="_viewid" />
					<s:hidden name="_sortCol" />
					<s:hidden name="_orderby" />
					<s:hidden name="_sortStatus" />
					 <!-- 查询按钮 -->
	                <s:if test="#request.htmlBean.showSearchForm">
	                <button type="button" class="btn btn-info " style="float:right;margin-right:15px;" onclick="isSearch();"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
	            	</s:if>
                </ul>
            </div>
        </div>
        <div id="mt" style="padding-top:10px;margin-top:56px;"></div>
	<div class="searchDiv" id="searchFormTable" style="margin-bottom:10px;display:none;">
	<!-- 是否显示查询表单 -->
	<s:if test="#request.htmlBean.showSearchForm">
	<!-- 要在BackURL传递的参数放在 searchFormTable-->
            <div class="container-fluid">
                <form>
					<!-- 输出查询表单HTML -->
					<s:property value="#request.htmlBean.toSearchFormHtml()" escape="false"/>
					<!-- 
                    <div class="row">
                        <div class="col-xs-2 text-right">线索状态：</div>
                        <div class="col-xs-2">
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-xs-2 text-right">公司：</div>
                        <div class="col-xs-2">
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-xs-2 text-right">填单日期：</div>
                        <div class="col-xs-2">
                            <div class="input-group date form_date fl" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                                <input class="form-control" size="16" type="text" value="" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> </div>
                            <input type="hidden" id="dtp_input2" value="" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2 text-right">电话：</div>
                        <div class="col-xs-2">
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-xs-2 text-right">邮件：</div>
                        <div class="col-xs-2">
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-xs-2 text-right">最后修改日期：</div>
                        <div class="col-xs-2">
                            <div class="input-group date form_date fl" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                                <input class="form-control" size="16" type="text" value="" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> </div>
                            <input type="hidden" id="dtp_input2" value="" />
                        </div>
                    </div>
                     -->
                    <div class="row">
                        <div class="col-xs-12 text-center">
                            <button type="button" class="btn btn-primary" onclick="modifyActionBack();"><span class="glyphicon glyphicon-search" aria-hidden="true"></span>{*[Query]*}</button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="button" class="btn btn-default btn-reset" onclick="ev_resetAll()">{*[Reset]*}</button>
                        </div>
                    </div>
                </form>
            </div>
	</s:if>
    </div>
	<!-- 数据表格 -->
	<!-- fixed表头 -->
	<div id="tableFix" style="display:none;">
        <div class="dataTable">
            <table id="listDataTh_a" class="listDataTable text-center">
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
	<div class="dataTable">
		<textarea id='_remark' type='text' style='display:none;' name='_remark'></textarea>
		<div id='doFlowRemarkDiv' style='display:none;width:280px;' title='{*[cn.myapps.core.dynaform.view.input_audit_remark]*}'>
		<textarea id='temp_remark' rows='12' cols='35' name='temp_remark' style='width:97%;'></textarea></div>
		<%
		out.print(htmlBean.toHTMLText());
		%>
    </div>
    <nav id="footer" class="text-center">
	<!-- 分页导航(page navigate) -->
	<s:if test="_isPagination == 'true' || _isShowTotalRow == 'true'">
			<s:if test="_isPagination == 'true'">
	        <ul class="pagination">
				<s:if test="datas.pageNo  > 1">
					<!-- 
					<a href='javascript:showFirstPage(null, listAction)'><img src="<o:Url value='/resource/document/pg_first.gif' />" alt="{*[FirstPage]*}"></a>&nbsp;
					<a href='javascript:showPreviousPage(null, listAction)'><img src="<o:Url value='/resource/document/pg_previous.gif' />" alt="{*[PrevPage]*}"></a>&nbsp;
					 -->
	            	<li><a href="javascript:showPreviousPage(null, listAction)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
	            	<li><a href="javascript:showFirstPage(null, listAction)">1</a></li>
				</s:if>
				<s:else>
					<!-- 
					<img src="<o:Url value='/resource/document/pg_first_d.gif' />" alt="{*[FirstPage]*}">&nbsp;
					<img src="<o:Url value='/resource/document/pg_previous_d.gif' />" alt="{*[PrevPage]*}">&nbsp;
					-->
				</s:else>
					<!-- 
	            <li><a href="#">2</a></li>
	            <li><a href="#">3</a></li>
	            <li><a href="#">4</a></li>
	            <li><a href="#">5</a></li>
					-->
				<s:if test="datas.pageNo < datas.pageCount">
					<!-- 
					<a href='javascript:showNextPage(null, listAction)'><img src="<o:Url value='/resource/document/pg_next.gif' />" alt="{*[NextPage]*}"></a>&nbsp;
					<a href='javascript:showLastPage(null, listAction)'><img src="<o:Url value='/resource/document/pg_last.gif' />" alt="{*[EndPage]*}"></a>&nbsp;
	            	 -->
	            	<li><a href="javascript:showNextPage(null, listAction)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
				</s:if>
				<s:else>
					<!-- 
					<img src="<o:Url value='/resource/document/pg_next_d.gif' />" alt="{*[NextPage]*}">&nbsp;
					<img src="<o:Url value='/resource/document/pg_last_d.gif' />" alt="{*[EndPage]*}">&nbsp;
					-->
				</s:else>
				<s:property value='datas.pageNo' />{*[Page]*}&nbsp;{*[Total]*}<s:property value='datas.pageCount' />{*[Page]*}
				<s:if test="datas.pageCount > 1">
				{*[cn.myapps.core.dynaform.activity.type.jump]*}&nbsp;<input type="text" style="width:25px;height:19px;margin:1px" name="_jumppage" />
				<button type="button" onclick='javascript:jumpPage(listAction);' style="height:20px;line-height:17px;margin:3px">{*[cn.myapps.core.dynaform.activity.type.jump]*}</button>&nbsp;
				</s:if>
	        </ul>
			</s:if>
			<s:if test="_isShowTotalRow == 'true'">
				<span>{*[TotalRows]*}:(<s:property value="totalRowText" />)</span>
			</s:if>
	</s:if>
    </nav>
	<!-- 分页导航结束(end of page navigate) -->
	</div>
    <div class="clearfix"></div>
</div>
</s:form>
	<%
	//out.println(htmlBean.toContextMenuHtml());
	%>
</body>
</html>
</o:MultiLanguage>
