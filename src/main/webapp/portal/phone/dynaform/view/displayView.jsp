<%@ page contentType="text/html; charset=UTF-8" errorPage="/portal/share/error.jsp" %>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@page import="cn.myapps.core.dynaform.view.html.ViewHtmlBean"%>
<%@include file="/portal/share/common/lib.jsp"%>
<%
	//初始化HtmlBean
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
	
<script type="text/javascript">
var contextPath = '<%= request.getContextPath()%>' ;
var isedit = '';
var enbled='';
var openDownWinStr = '<s:property value="%{#request.excelFileName}"/>';
var selectStr = '{*[Select]*}';	//viewDoc()
var newStr = '{*[New]*}';	//createDoc()
var typeName = '<s:property value="%{#request.message.typeName}" />';	//showPromptMsg()
var urlValue = '<s:url value="%{#request.ACTIVITY_INSTNACE.actionUrl}">'+
	'<s:param name="_activityid" value="%{#request.ACTIVITY_INSTNACE.id}" /></s:url>';	//showPromptMsg()

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

/**
 * 弹出方式打开视图明细内容
 * 重载uil.js中定义的方法，使得适应移动设备的工作情况。
 */
function openWindowByType(action, title, viewType, actid) {
	var flag = false;
	
	if(actid){
		jQuery.ajax({
			type: 'POST',
			async:false, 
			url: contextPath + '/portal/dynaform/activity/runbeforeactionscript.action?_actid=' + actid,
			dataType : 'text',
			timeout: 3000,
			data: jQuery("#formList").serialize(),
			success:function(result) {
				if(result != null && result != ""){
		        	var jsmessage = eval("(" + result + ")");
		        	var type = jsmessage.type;
		        	var content = jsmessage.content;
		        	
		        	if(type == '16'){
		        		alert(content);
		        		document.forms[0].submit();
		        	}
		        	
		        	if(type == '32'){
		        		var rtn = window.confirm(content);
		        		if(!rtn){
		        			document.forms[0].submit();
		        		}else {
		        			flag = true;
		        		}
		        	}
		        }else {
		        	flag = true;
		        }
			},
			error: function(result) {
				alert("运行脚本出错");
			}
		});
	} else {
		flag = true;
	}

	if(flag){
		// View的openType(打开类型)
		var openType = OPEN_TYPE_NORMAL;
		var url = action;

		if (viewType == VIEW_TYPE_SUB) {
			var isRelate = document.getElementsByName("isRelate");
			url += "&isSubDoc=true";
			if (isRelate){
	            if (isRelate.length>0){
	            	var relate = isRelate[0].value;
	            	url += "&isRelate="+relate;
	            }
			}
		}
		if (document.getElementsByName("_openType")[0]) {
			openType = document.getElementsByName("_openType")[0].innerText;
		}
	
		var parameters = getQueryString(openType);
	
		resetBackURL(); // view.js
		
		if (true || openType == OPEN_TYPE_POP || openType == OPEN_TYPE_DIV) {//所有类型都用弹出层方式
			var oSelects = document.getElementsByName("_selects");
			var _selects="";
			if(oSelects){
				for(var i=0;i<oSelects.length;i++){
					if(oSelects[i].checked){
						_selects+="&_selects="+oSelects[i].value;
					}
				}
			}
			url += "&" + parameters + "&openType=" + openType+_selects;
			url = appendApplicationidByView(url);
			var width = $(top.window).width()-25;
			var height = $(top.window).height()-50;
	
			$("#document_content").hide();
			$("#myModalexample").find(".content").css({"position":"static","height":$(window).height()-44})
			$("#myModalexample").find(".content>iframe").css({"position":"absolute","z-index":"1","top":"44px","height":$(window).height()-44})
			
			//showfrontframe({
			//			title : "",
			//			url : url,
			//			h : height,
			//			w : width,
			//			windowObj : window.parent,
			//			callback : function(result) {
			//				var _fieldid = $("[name='_fieldid']").val();
			//				$("#"+_fieldid+"_divid>div").trigger("refresh");
			//			}
			//});
			
			MyPopup.open({
				url:url,
				title:"编辑",
				success:function(){
					$("[name='_fieldid']").each(function(){
						$("#document_content").show();
						$("#"+$(this).text()).trigger("refresh");
						
					})
					dy_refresh(name);
					
					/*var _fieldid = $(".control-content.active").find("input[name='_fieldid']").val();
					$("#"+_fieldid).trigger("refresh");
					$("#document_content").show();
					dy_refresh(name);*/
				}
			});
		}
	}
}

	

jQuery(document).ready(function(){
	//dy_lock();	//在方法加载完之前锁定操作
	initDispComm();	//子视图公用初始化方法
	//dy_unlock();	//方法加载完之后解锁操作
});
</script>

<div id="content-load-panel" style="position: fixed;z-index: 1;left: 0px;right: 0px;top:0px;bottom:0px;display:none">
	<div id="content-space" class="text-center">
		<div class="content-space-icon icon iconfont">&#xe61a;</div>
		<div class="content-space-h1">当前没有任何内容</div>
		<div class="content-space-h2">添加的内容将显示在这里</div>
	</div>
</div>

<div class='card_app'><ul class='table-view'>	
	<%
	out.print(htmlBean.toHTMLText());
	%>
			

	<div class="ui-content dataTableDiv" id="dataTableDiv" name="dataTableDiv" style="overflow:auto">
	</div>


</ul></div>


<nav id="footer" class="text-center">

	<!-- 分页导航(page navigate)1 -->


<s:if test="_isPagination == 'true' || _isShowTotalRow == 'true'">
	<ul class="pagination"  style="margin:0;">
		<s:if test="_isPagination == 'true'">
			<s:if test="datas.pageNo  > 1">
				<li><a onclick="turnPage('showFirstPage',this)"><span title="{*[FirstPage]*}">&lt;&lt;</span></a></li>
				<li><a onclick="turnPage('showPreviousPage',this)"><span title="{*[PrevPage]*}">&lt;</span></a></li>
			</s:if>
			<s:else>
				<li class="disabled"><a><span title="{*[FirstPage]*}">&lt;&lt;</span></a></li>
				<li class="disabled"><a><span title="{*[PrevPage]*}">&lt;</span></a></li>
			</s:else>
			<li><a href='javascript:showFirstPage()'><s:property value='datas.pageNo' />&nbsp;/&nbsp;<s:property value='datas.pageCount' /></a></li>
			<s:if test="datas.pageNo < datas.pageCount">
				<li><a onclick="turnPage('showNextPage',this)"><span title="{*[NextPage]*}">&gt;</span></a></li>
				<li><a onclick="turnPage('showLastPage',this)"><span title="{*[EndPage]*}">&gt;&gt;</span></a></li>
			</s:if>
			<s:else>
				<li class="disabled"><a><span title="{*[NextPage]*}">&gt;</span></a></li>
				<li class="disabled"><a><span title="{*[EndPage]*}">&gt;&gt;</span></a></li>
			</s:else>
		</s:if>
		<s:if test="_isShowTotalRow == 'true'">
			<!-- <span>{*[TotalRows]*}:(<s:property value="totalRowText" />)</span> -->
		</s:if>
	</ul>
</s:if>

    </nav>
	<!-- 分页导航结束(end of page navigate) -->


<div class="activityDiv" id="activityDiv">
<div style="height:66px"></div>

     <div class="card_space_fix zindex10" data-role="controlgroup" data-type="horizontal" data-enhance="false">

	<table>
         <tr>
<!-- 输出视图操作HTML -->
			<s:property value="#request.htmlBean.toActHtml()" escape="false"/>
			
			<s:if test="#request.htmlBean.showSearchForm">



			<td><a href="#searchForm" class="btn btn-primary btn-block">查询</a></td>
			</s:if>

		</tr></table>
  </div>


			
	
	</div>

<!-- /div --><!-- /page -->

<div data-role="page" id="searchForm" class="modal modal-iframe">
	<header class="bar bar-nav">
	<a class="icon icon-close pull-right" id="btn-modal-close" href="#searchForm"></a>
	<h1 class="title">查询</h1>
</header><!-- /header -->
<div class="content" <s:if test="#request.htmlBean.showSearchFormButton">style="margin-bottom:57px;"</s:if>>

<div role="main" class="ui-content" id="searchFormTable">
	
	<!-- 要在BackURL传递的参数放在 searchFormTable-->
		<!-- 输出查询表单HTML -->
	  <div class="card_app">
    <div class="contact-form">	
		<s:if test="#request.htmlBean.showSearchForm">
	<!-- 要在BackURL传递的参数放在 searchFormTable-->
		<!-- 输出查询表单HTML -->
		<s:property value="#request.htmlBean.toSearchFormHtml()" escape="false"/>
		
	
	</s:if>
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

</div>
<!-- 数据表格 -->

<div class="tab_parameter">
	<%@include file="../../resource/common/list.jsp"%>
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
	<s:hidden name="signatureExist" id="signatureExist" value="%{#request.htmlBean.isSignatureExist()}"></s:hidden>
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
	<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
		<%@include file="/portal/share/common/msgbox/msg.jsp"%>
	</s:if>
	
	<s:hidden name="application" id="application" value="%{#parameters.application[0]}" ></s:hidden>
	<s:hidden id="viewid" name="_viewid" />
	<s:hidden name="_sortCol" />
	<s:hidden name="_orderby" />
	<s:hidden name="_sortStatus" />
	<s:hidden name="_isSubDoc" value="true" />
	<textarea id='_remark' type='text' style='display:none;' name='_remark'></textarea>
	<div id='doFlowRemarkDiv' style='display:none;width:280px;' title='{*[cn.myapps.core.dynaform.view.input_audit_remark]*}'>
		<textarea id='temp_remark' rows='12' cols='35' name='temp_remark' style='width:97%;'></textarea>
	</div>
</div>

</o:MultiLanguage>