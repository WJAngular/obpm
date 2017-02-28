<!DOCTYPE html>
<%@page import="cn.myapps.core.dynaform.form.ejb.TextareaField"%>
<%@page import="cn.myapps.core.dynaform.form.ejb.ImageUploadField"%>
<%@page import="cn.myapps.core.dynaform.form.ejb.AttachmentUploadField"%>
<html>
<%@ page import="net.sf.json.JSONObject"%>
<%@ page import="net.sf.json.JSONArray"%>
<%@ page import="net.sf.json.util.JSONStringer"%>
<%@ page import="net.sf.json.JSON"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cn.myapps.core.dynaform.view.ejb.View"%>
<%@ page import="cn.myapps.core.dynaform.document.ejb.*"%>
<%@ page import="cn.myapps.base.action.ParamsTable"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.core.macro.runner.*"%>
<%@ page import="cn.myapps.core.dynaform.view.ejb.Column"%>
<%@ page import="cn.myapps.core.dynaform.form.ejb.Form"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="cn.myapps.base.dao.DataPackage" %>
<%@ page import="cn.myapps.util.StringUtil"%>
<%@	include file="/portal/share/common/lib.jsp"%>
<%@	include file="/portal/phone/resource/common/js_base.jsp" %>
<%@	include file="/portal/phone/resource/common/js_component.jsp" %>
<%@ page import="cn.myapps.core.dynaform.view.action.ViewHelper"%>
<%@ page import="cn.myapps.core.dynaform.form.ejb.MapField"%>
<%@page import="cn.myapps.core.dynaform.form.ejb.UserField"%>
<%@page import="cn.myapps.core.dynaform.form.ejb.SuggestField"%>
<%@page import="cn.myapps.core.dynaform.form.ejb.TreeDepartmentField"%>
<%@page import="cn.myapps.core.dynaform.form.ejb.DepartmentField"%>
<%View view = ((View) request.getAttribute("content"));
	WebUser user =(WebUser)session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	ParamsTable params = ParamsTable.convertHTTP(request);
	IRunner jsrun = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
	Collection errors = new HashSet();
	Collection columns=view.getColumns();
	String viewid=request.getParameter("_viewid");
	String styleid=ViewHelper.get_Styleid(viewid);
	String type=request.getParameter("_isdiv");
	String defaultSize = request.getParameter("_defaultSize");
	request.setAttribute("runner", jsrun);
	String selectOne = request.getParameter("selectOne");
%>

<%@page import="cn.myapps.core.dynaform.form.ejb.ValidateMessage"%>
<%@page import="cn.myapps.core.dynaform.form.ejb.FormField"%><html>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<head>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>list column by view</title>

<script src='<s:url value="/dwr/interface/FormHelper.js"/>'></script>
<script src='<s:url value="/dwr/interface/ViewHelper.js"/>'></script>
<script src='<o:Url value="/resource/document/obpm.ui.js"/>'></script>
<script src='<o:Url value='/resource/component/view/common.js' />'></script>
<script src='<o:Url value='/resource/component/view/view.js' />'></script>
<script src='<o:Url value='/resource/js/tableList.js' />'></script>
<script src='<o:Url value='/resource/js/iscroll.js' />'></script>
<script>
var importURL = "<s:url value='/portal/share/dynaform/dts/excelimport/importbyid.jsp'/>";
	importURL +="&applicationid=<%=view.getApplicationid()%>";
var downloadURL = '<s:url value="/portal/share/download.jsp" />'; // Excel下载URL

var isEdit = '<%=request.getParameter("isEdit")%>';

//改为弹出层后，此属性将失效，不需要判断打开方式
var type='<%=request.getParameter("_isdiv")%>';

var rtn = {};
var selectOne = '<%=selectOne%>';
var selectString = "";
function ev_selectone(docid, value) {
	if(selectOne=='false'){
		var params = jQuery.par2Json(decodeURIComponent(jQuery("form").serialize()));
		params['_selects'] = docid;
		ev_return(params, "{id:"+value+"}");
	}else{
		selectString+=(value+";");
		document.getElementById("selectList").innerHTML=selectString.substring(0, selectString.lastIndexOf(";")); //innerHTML改为innerText,innerHTML会引起&符号丢失
		
	}
}

function ev_doClear(){
	var result = {
			params :"",
			selectedValue:""
	};
	if(selectOne=="false"){
		top.OBPM.dialog.doClear(result);
	}else{
		selectString = "";
		document.getElementById("selectList").innerHTML = "";
	}
}

function ev_ok() {
	var params =jQuery.par2Json(decodeURIComponent(jQuery("form").serialize()));
	
	if(selectOne=="false"){
		var selects = params['_selects'];
		if (selects && selects instanceof Array) {
			params['_selects'] = selects.join(";");		
		}
			
		ev_return(params, jQuery.json2Str(rtn));
	}else{
		selectString=selectString.substring(0, selectString.lastIndexOf(";"));
		ev_return(params, selectString);
	}
}

// 执行确定脚本(okscript)并返回
var args = top.OBPM.dialog.getArgs();
function ev_return(params, selectedValue) {
	DWREngine.setAsync(false); 
	var result = {
			params :params,
			selectedValue:selectedValue
	};
	ViewHelper.runScript(params, function(rtn) {
		if (rtn) {
			if(rtn.indexOf('doAlert')>-1 || rtn.indexOf('doConfirm')>-1){
				eval(rtn);
			}else{
				var regExp =/<script[^>]*>.*(?=<\/script>)<\/script>/gi;
				if (rtn.replace(regExp,'') == "true") { // 2.执行脚本
					eval(RegExp.$1);
					top.OBPM.dialog.doReturn(result);
				} else {
					alert("确定条件出错，请联系后台管理员");
				}
			}
		} else {
			top.OBPM.dialog.doReturn(result);		
		}
	});
}

function doAlert(msg) {
	if (msg) {
		alert(msg);
	}
}

function doConfirm(msg, result) {
	if (msg) {
		if (confirm(msg)) {
			top.OBPM.dialog.doReturn(result);
		}
	}
}

function getFormFieldByName(name){
	var els = args['parent'].document.getElementsByName(name);
	if (els[0]) {
		if (els[0].tagName == 'IFRAME') {
		//els[0].contentWindow.location.reload();
			return els[0].contentWindow;
		} else {
			return els[0];
		}
	}
	return null;
}

function ev_select(key,value,isChecked) {
	if (isChecked) {
		rtn[key] = value;
	} else {
		delete rtn[key];
	}
}

function ev_selectAll(b) {
	var c = document.getElementsByName('_selects');
    if(c==null)
    return;
    if (c.length!=null){
      for(var i = 0; i < c.length ;++i) {
        c[i].checked = b && !(c[i].disabled);
    	c[i].onclick();
      }
    }else{
      c.checked = b;
	}
	return b;

}

function ev_init() {
	var defalutSize = <%=defaultSize%>;
	//Resize dialog, set the dialog window size as the body size.
	if(defalutSize == "true"){//后台显示大小为默认时，允许页面根据内容设置弹出层大小
		top.OBPM.dialog.resize(600, document.body.scrollHeight+70);
	}

	var c = document.getElementsByName('_selects');
	if (c) {
		for (prop in rtn) {
			
			for(var i = 0; i < c.length ;++i) {
				if (prop == c[i].value) {
					c[i].checked = true;
				}
			}
		}
	}
	
	changeElState('btn',true); // change button state
}

function initElonclick(fieldName, func) {
	var els = document.getElementsByName(fieldName);
	for(var i=0; i<els.length; i++) {
		els[i].onclick = func;
	}
}

function changeElState(fieldName,state) {
	var els = document.getElementsByName(fieldName);
	for(var i=0; i<els.length; i++) {
		els[i].disabled = !state;
	}
}



$(function(){
	
	dy_lock();
	ev_init();
	//表单控件jquery重构
	jqRefactor();
	tableListColumn();
	dy_unlock();
	
	if($("div#searchForm").size()>0){
		var searchBtn = "<td><a id='searchBtn' class='btn btn-primary btn-block'>查询</a></td>"
		if($(this).find("#searchBtn").size()<=0){
			$(this).find(".dialog_Btn").append(searchBtn);
		}

		var searchScroll;
		searchScroll = new IScroll('#searchForm>.content', { 
			preventDefault: false,
			preventDefaultException: { tagName: /^(INPUT|TEXTAREA|BUTTON|SELECT|A|SPAN)$/ }
		})
	}
		
	var myScroll;				
    setTimeout(function(){
   		myScroll = new IScroll('.formContent-Box', { 
   			scrollX: true, 
   			freeScroll: true,
			preventDefault: false
		});
   	},100);
    	
    $(".tableList-screen").click(function(){
    	$(".formContent-is").width($("#dialongViewTable").width());
    	myScroll.refresh();
    });
    	
    document.addEventListener('touchmove', function (e) {
    	e.preventDefault(); 
    }, false);
	
	$("#searchBtn").click(function(){
		$("#searchForm").next(".formContent-Box").hide();
		$("#searchForm").next().next(".card_space_fix").hide();
		$("#searchForm").show();
		$("#searchForm").addClass("active");
	});
	
	$("#searchForm").find("#btn-modal-close").click(function(){
		$("#searchForm").removeClass("active");
		$("#searchForm").next(".formContent-Box").show();
		$("#searchForm").next().next(".card_space_fix").show();
		$("#searchForm").hide();
	})
});


function setFormListSize(){
	if(navigator.userAgent.indexOf("MSIE")>0 && document.documentMode != 10){
		var selectList = jQuery("#selectList").html();
		if(selectList == undefined)
			jQuery("#formList").height(jQuery("body").height()-60);
		else
			jQuery("#formList").height(jQuery("body").height()-115);
	}
	jQuery("#dspview_divid").height(jQuery("#view_iframe").height() - jQuery(".searchDiv").height()-jQuery(".viewselect").height()-30);
}
</script>

</head>
<body>
<div id="loadingDivBack" style="position: fixed; z-index: 500; width: 100%; height: 100%; top: 0px; left: 0px; background-color:#ccc; filter: alpha(opacity = 0.1); opacity: 0.1;">
		<div style="position: absolute;top: 35%;left: 45%;width: 128px;height: 128px;z-index: 100;">
			<img src="<o:Url value='/resource/main/images/loading1.gif'/>"/>
		</div>
	</div>
<s:form id="formList" name="formList" action="dialogView4Phone" method="post" theme="simple">
<div class="reimburse">

<%
				Form searchForm = view.getSearchForm();
				if (searchForm != null) {
			%>
				<div data-role="page" id="searchForm" class="modal modal-iframe">
					<header class="bar bar-nav">
						<a class="icon icon-close pull-right" id="btn-modal-close" _href="#searchForm"></a>
						<h1 class="title">查询</h1>
					</header>
					<div class="content" style="bottom:57px;overflow: hidden;background: #ebebeb;z-index:11">
						<div role="main" class="ui-content" id="searchFormTable">
							<div class="card_app">
								<div class="contact-form">	
									<%
										Collection<FormField> fields = searchForm.getAllFields();
										Iterator<FormField> it = fields.iterator();
										while(it.hasNext()){
											FormField field = it.next();
											if(Item.VALUE_TYPE_NUMBER.equals(field.getFieldtype())){
												String value = params.getParameterAsString(field.getName());
												if(StringUtil.isBlank(value)){
													params.removeParameter(field.getName());
												}
											}
										}
										Document searchDoc = searchForm.createDocument(params, user);
										String ehtml = searchForm.toHtml(searchDoc, params, user, new ArrayList<ValidateMessage>());
										out.print(ehtml);
						           	%>
								</div>
								<!--  <div style="height:57px"></div>-->
							</div>
						</div>
					</div>
					<div class="card_space_fix zindex10" style="position: absolute;z-index:100">
						<table width="100%"  cellspacing="10">
							<tr>	
								<td><a onclick="ev_search()" class="btn btn-primary btn-block">{*[Query]*}</a></td>
								<td><a onclick="ev_resetAll();" class="btn btn-block">{*[Reset]*}</a></td>
							</tr>
						</table>
					</div>
				</div>
				<%
					}
				%>

<div class='formContent-Box'><div class='formContent-is'>
<div id="view_iframe" class="card_app">
		<input type="hidden" name="_currpage" value='<s:property value="datas.pageNo"/>' />
		<input type="hidden" name="_pagelines" value='<s:property value="content.pagelines"/>' />
		<input type="hidden" name="_rowcount" value='<s:property value="datas.rowCount"/>' />
		<input type="hidden" name="_pageCount" value='<s:property value="datas.pageCount"/>' />
		<s:hidden name="mutil" value="%{#parameters.mutil}" />
		<s:hidden name="selectOne" value="%{#parameters.selectOne}" />
		<s:hidden name="allow" value="%{#parameters.allow}" />
		<s:hidden name="className" value="%{#parameters.className}" />
		<s:hidden name="_isdiv" value="%{#parameters._isdiv}" />
		<s:hidden name="isEdit" value="%{#parameters.isEdit}" />
		<s:hidden name="_sortCol" value="%{#parameters._sortCol}" />
		<s:hidden name="_sortStatus" value="%{#parameters._sortStatus}" />
		<s:hidden name="parentid" value="%{#parameters.parentid}" />
		<s:hidden name="content.openType" />
		<s:hidden id="viewid" name="_viewid" />
		<s:hidden id="formid" name="formid" value="%{#parameters.formid}"/>
		<s:hidden id="fieldid" name="fieldid" value="%{#parameters.fieldid}"/>
		<input type="hidden" name="application" id="application" value="<%=view.getApplicationid()%>" />
		<input type="hidden" name="divid" value="%{#parameters.divid}" />
		
		<!-- 确定脚本标签 -->
		<s:hidden name="okscriptlabel" value="%{#parameters.okscriptlabel}" />
			
		<div id="dspview_divid">
		<div id="viewDiv" class="dataTableDiv">
		<table id="dialongViewTable" data-mode="columntoggle" class="listDataTable table-column-toggle">
			<tr class="listDataTh">
				<s:if test="#parameters.mutil[0] == 'true'">
					<td class="listDataThFirstTd" scope="col"><input type="checkbox"
						onClick="ev_selectAll(this.checked)"></td>
				</s:if>
				<%
				Collection hiddns = new ArrayList();
				%>
				<s:iterator value="content.columns" status="colstatus">
				<s:set name="column" scope="page" />
				<%
					Boolean isHidden = new Boolean(false);
					Column column = (Column) pageContext.getAttribute("column");
					StringBuffer label = new StringBuffer();
					label.append("View").append("." + view.getName()).append(".Activity(").append(column.getId())
							.append(")." + column.getName()).append(".runHiddenScript");
					jsrun.initBSFManager(new Document(), params, user, errors);
					Object result = jsrun.run(label.toString(), column.getHiddenScript());//运行脚本
					if (result != null && result instanceof Boolean) {
						isHidden = ((Boolean) result).booleanValue();
					}
					hiddns.add(isHidden);
					if (false){
						
					}else{
						%>
					<s:if test="width != null && width != \"0\" && !#colstatus.last">
						<th class="listDataThTd" nowrap="nowrap" width='<s:property value="width"/>' isVisible='<s:property value="visible"/>' ishiddencolumn='<%=isHidden.toString()%>'>
						<a style='cursor: pointer' href="#"
							onclick="sortTable('<s:property value="fieldName" />')"> {*[<s:property value="name" />]*} 
							<s:if test="_sortCol!=null && _sortCol!='' && _sortCol.toUpperCase()==fieldName.toUpperCase()" >
							<s:if test="_sortStatus=='ASC'">
								<img border=0 src='<s:url value="/portal/share/images/view/up.gif"/>'>
							</s:if>
							<s:elseif test="_sortStatus=='DESC'">
								<img border=0 src='<s:url value="/portal/share/images/view/down.gif"/>'>
							</s:elseif>
						</s:if> </a></th>
					</s:if>
					<s:elseif test="width == \"0\"">
						<th class="listDataThTd" isVisible='<s:property value="visible"/>' ishiddencolumn='<%=isHidden.toString()%>' style="display: none;">
							{*[<s:property value="name" />]*}
						</th>
					</s:elseif>
					<s:else>
						<th class="listDataThTd" isVisible='<s:property value="visible"/>' ishiddencolumn='<%=isHidden.toString()%>'><a style='cursor: pointer' href="#"
							onClick="sortTable('<s:property value="fieldName" />')"> 
							{*[<s:property value="name" />]*}
							<s:if test="_sortCol!=null && _sortCol!=''">
							<s:if test="_sortCol.toUpperCase()==fieldName.toUpperCase()">
								<s:if test="_sortStatus=='ASC'">
									<img border=0 src='<s:url value="/portal/share/images/view/up.gif"/>'>
								</s:if>
								<s:elseif test="_sortStatus=='DESC'">
									<img border=0 src='<s:url value="/portal/share/images/view/down.gif"/>'>
								</s:elseif>
							</s:if>
						</s:if> </a></th>
					</s:else>
					<%} %>
				</s:iterator>

				<s:if test="#parameters.allow[0] == 'true'">
					<td class="listDataThTd">{*[View]*}</td>
				</s:if>
			</tr>

			<s:iterator value="datas.datas" status="docStatus">
				<tr class="listDataTr" >
					<s:set name="doc" id="doc" scope="page" />
					<%
						Document doc = (Document) pageContext.getAttribute("doc");
						jsrun.initBSFManager(doc, params, user, errors);

						String trueValueMap = "{";
						String valuesMap = "{";
						String valuesStateLabel = "";
						String valuesAuditNode = "";
						String valuesAuditUser = "";
						Iterator it = columns.iterator();
						while (it.hasNext()) {
							Column key = (Column) it.next();

							Object value = key.getText(doc, jsrun, user);
							String trueValue = "";
							FormField formField = key.getFormField();
							String formType = formField.getType();
							if("attachmentupload".equals(formType)
									||"imageupload".equals(formType)){
								trueValue = key.getValue4FileUplad(doc, jsrun, user,null);
							}
							 if(formField instanceof UserField 
									 || formField instanceof SuggestField
									 || formField instanceof DepartmentField
									 || formField instanceof TreeDepartmentField){
								trueValue = key.getValueAndText(doc, jsrun, user,null);
							}  
							if("$StateLabel".equals(key.getFieldName()) && value.toString().indexOf("[")==0){//视图列绑定流程状态字段类型
								//解析json数据生成html
								JSONArray instances = JSONArray.fromObject(value); 
								Long[] arr=new Long[instances.size()];
								 for(int i=0;i<instances.size();i++){
										JSONObject instance = instances.getJSONObject(i);
										String instanceId = instance.getString("instanceId");
										JSONArray nodes = instance.getJSONArray("nodes");
										for(int j=0;j<nodes.size();j++){
											JSONObject node = nodes.getJSONObject(j);
											String stateLable = node.getString("stateLabel");
											valuesStateLabel += stateLable+";";
										}
								 }
								 valuesMap += "'" + key.getId() + "':'" + valuesStateLabel + "',";
								 trueValueMap += "'" + key.getId() + "':'" + valuesStateLabel + "',";
							}else if("$PrevAuditNode".equals(key.getFieldName()) && value.toString().indexOf("[")==0){//视图列绑定上一环节流程处理节点名称字段
								//解析json数据生成html
								JSONArray instances = JSONArray.fromObject(value);  
							    Long[] arr=new Long[instances.size()];  
							    for(int i=0;i<instances.size();i++){  
									JSONObject instance = instances.getJSONObject(i);
									String instanceId = instance.getString("instanceId");
									String prevAuditNode = instance.getString("prevAuditNode");
									valuesAuditNode += prevAuditNode+";";
							    }
								 valuesMap += "'" + key.getId() + "':'" + valuesAuditNode + "',";
								 trueValueMap += "'" + key.getId() + "':'" + valuesAuditNode + "',";
							}else if("$PrevAuditUser".equals(key.getFieldName()) && value.toString().indexOf("[")==0){//视图列绑定上一环节流程处理节点人名称字段
								//解析json数据生成html
								JSONArray instances = JSONArray.fromObject(value);  
							    Long[] arr=new Long[instances.size()];  
							    for(int i=0;i<instances.size();i++){  
									JSONObject instance = instances.getJSONObject(i);
									String instanceId = instance.getString("instanceId");
									String prevAuditUser = instance.getString("prevAuditUser");
									valuesAuditUser += prevAuditUser+";";
							    }
								 valuesMap += "'" + key.getId() + "':'" + valuesAuditUser + "',";
								 trueValueMap += "'" + key.getId() + "':'" + valuesAuditUser + "',";
							}else{
								valuesMap += "'" + key.getId() + "':'" + StringUtil.encodeHTMLForDialog(value.toString()) + "',";
								if (formField instanceof TextareaField){
									trueValueMap += "'" + key.getId() + "':'" + StringUtil.encodeHTML(StringUtil.encodeHTML(value.toString())) + "',";
								}else if("attachmentupload".equals(formType)
										||"imageupload".equals(formType)){
									trueValueMap += "'" + key.getId() + "':'" + trueValue + "',";
								}else if(formField instanceof UserField 
										 || formField instanceof SuggestField
										 || formField instanceof DepartmentField
										 || formField instanceof TreeDepartmentField ){
									trueValueMap += "'" + key.getId() + "':'" + trueValue + "',";
								}else{
									trueValueMap += "'" + key.getId() + "':'" + StringUtil.encodeHTMLForDialog(value.toString()) + "',";
								}
							}
						}
						valuesMap = valuesMap.substring(0, valuesMap.length() - 1);
						trueValueMap = trueValueMap.substring(0, trueValueMap.length() - 1);
						valuesMap += "}";
						trueValueMap += "}";
					%>
					<s:if test="#parameters.mutil[0] == 'true'">
						<td class="listDataTrFirstTd"><input type="checkbox" name="_selects"
							value='<s:property value="id"/>'
							onclick="ev_select(this.value,<%=valuesMap%>,this.checked)" /></td>
					</s:if>

					<%
						Iterator iter = columns.iterator();
					    Iterator hd =hiddns.iterator();
								while (iter.hasNext()) {
									Column col = (Column) iter.next();

									Object result = col.getText(doc, jsrun, user);
									if(col.getFormField() instanceof MapField){
										if(!StringUtil.isBlank(result.toString())){
											String resHtml = "<TABLE style=\"width:280px;border:0;\">";
											String results="["+result+"]";
											JSONArray instances = JSONArray.fromObject(results); 
											for(int i=0;i<instances.size();i++){
												if(i+1==instances.size()){	//最后一行不显示下边框线
													resHtml += "<tr><td style=\"line-height:16px;border-right-width:0;border-bottom-width:0; border-right-style: none;\">";
												}else{
													resHtml += "<tr><td style=\"line-height:16px;border-right-width: 0px; border-right-style: none;\">";
												}
												JSONObject instance = instances.getJSONObject(i);
												String address=instance.getString("address");
												resHtml += address+"&nbsp;&nbsp;";
												resHtml += "</td></tr>";
											}
											resHtml += "</TABLE>";
											result = resHtml;
										}
									}
									if((col.getFormField() instanceof AttachmentUploadField) 
											|| (col.getFormField() instanceof ImageUploadField)){

										if(!StringUtil.isBlank(result.toString())){
											JSONArray instances = JSONArray.fromObject(result);
										    Long[] arr=new Long[instances.size()];
										    String resHtml = "";
										    for(int i=0;i<instances.size();i++){  
												JSONObject instance = instances.getJSONObject(i);
												String _url = request.getContextPath() + instance.getString("path");
												resHtml += "<div class='images-preview' data-src='"+_url
														+"' style='display: inline-block;margin:3px;'>" +
														"<img style='max-height:50px;max-width:50px;' src='"+_url+"' />" +
														"</div>";
										    }
											result = resHtml;
										}
									}
									/**多流程状态时数据处理--start**/
									if("$StateLabel".equals(col.getFieldName()) && result.toString().indexOf("[")==0){//视图列绑定流程状态字段类型

										if(!StringUtil.isBlank(result.toString())){
											String resHtml = "<TABLE style=\"width:100%;border:0;\">";
											JSONArray instances = JSONArray.fromObject(result);  
										    Long[] arr=new Long[instances.size()];  
										    for(int i=0;i<instances.size();i++){  
												if(i+1==instances.size()){	//最后一行不显示下边框线
													resHtml += "<tr><td style=\"line-height:16px;border-right-width:0;border-bottom-width:0; border-right-style: none;\">";
												}else{
													resHtml += "<tr><td style=\"line-height:16px;border-right-width: 0px; border-right-style: none;\">";
												}
												JSONObject instance = instances.getJSONObject(i);
												String instanceId = instance.getString("instanceId");
												JSONArray nodes = instance.getJSONArray("nodes");
												for(int j=0;j<nodes.size();j++){
													JSONObject node = nodes.getJSONObject(j);
													String stateLable = node.getString("stateLabel");
													resHtml += stateLable+"&nbsp;&nbsp;";
												}
												resHtml += "</td></tr>";
												
										    }
											resHtml += "</TABLE>";
											result = resHtml;
										}
									}else if("$PrevAuditNode".equals(col.getFieldName()) && result.toString().indexOf("[")==0){//视图列绑定上一环节流程处理节点名称字段
										//解析json数据生成html
										if(!StringUtil.isBlank(result.toString())){
											String resHtml = "<TABLE style=\"width:100%;border:0;\">";
											JSONArray instances = JSONArray.fromObject(result);  
										    Long[] arr=new Long[instances.size()];  
										    for(int i=0;i<instances.size();i++){  
												JSONObject instance = instances.getJSONObject(i);
												String instanceId = instance.getString("instanceId");
												String prevAuditNode = instance.getString("prevAuditNode");
												if(i+1==instances.size()){	//最后一行不显示下边框线
													resHtml += "<tr><td title=\""+prevAuditNode+"\" style=\"line-height:16px;border-right-width:0;border-bottom-width:0; border-right-style: none;\">";
												}else{
													resHtml += "<tr><td title=\""+prevAuditNode+"\" style=\"line-height:16px;border-right-width: 0px; border-right-style: none;\">";
												}
												resHtml += prevAuditNode+"&nbsp;&nbsp;";
												resHtml += "</td></tr>";
										    }
											resHtml += "</TABLE>";
											result = resHtml;
										}
									}else if("$PrevAuditUser".equals(col.getFieldName()) && result.toString().indexOf("[")==0){//视图列绑定上一环节流程处理节点人名称字段
										//解析json数据生成html
										if(!StringUtil.isBlank(result.toString())){
											String resHtml = "<TABLE style=\"width:100%;border:0;\">";
											JSONArray instances = JSONArray.fromObject(result);  
										    Long[] arr=new Long[instances.size()];  
										    for(int i=0;i<instances.size();i++){  
												JSONObject instance = instances.getJSONObject(i);
												String instanceId = instance.getString("instanceId");
												String prevAuditUser = instance.getString("prevAuditUser");
												if(i+1==instances.size()){	//最后一行不显示下边框线
													resHtml += "<tr><td title=\""+prevAuditUser+"\" style=\"line-height:16px;border-right-width:0;border-bottom-width:0; border-right-style: none;\">";
												}else{
													resHtml += "<tr><td title=\""+prevAuditUser+"\" style=\"line-height:16px;border-right-width: 0px; border-right-style: none;\">";
												}
												resHtml += prevAuditUser+"&nbsp;&nbsp;";
												resHtml += "</td></tr>";
										    }
											resHtml += "</TABLE>";
											result = resHtml;
										}
									}
									/**多流程状态时数据处理--end**/
									
									Boolean isHidden = (Boolean)hd.next();
									if (isHidden.booleanValue()){
										%>
										<td class="listDataTrTd" style="display: none;"></td>
										<%	
									}else {
									if ((col.getWidth() != null && col.getWidth().equals("0")) || !col.isVisible()) {
					%>
					<td class="listDataTrTd" style="display: none;"></td>
					<%
						} else {
					%>
					
					<td class="listDataTrTd">
					<s:if test="#parameters.className[0] == 'cn.myapps.core.dynaform.form.ejb.ViewDialogField'">
						<div id='valuemap<s:property value="#docStatus.index" />' style="display: none"><%=trueValueMap%></div>
						<s:if test="#parameters.isEdit[0]">
							<s:if test="#parameters.selectOne[0] == 'false'">
								<a onClick="ev_selectone('<%=doc.getId()%>', document.getElementById('valuemap<s:property value="#docStatus.index" />').innerText);">
								<%=result%> </a>
							</s:if>
							<s:else>
								<a onClick="ev_selectone('<%=doc.getId()%>', '<%=StringUtil.encodeHTML((String)result)%>');">
								<%=result%> </a>
							</s:else>
						</s:if>
					
						<s:else>
							<%=result%>
						</s:else>
					</s:if>
					</td>
					<%
						}
									}
								}
					%>
					<!-- 是否允许查看记录 -->
					<s:if test="#parameters.allow[0] == 'true'">
						<td class="listDataTrTd"><input type="button" name='btnView'
							style="background:url('<s:url value="/resource/imgnew/act/act_1.gif"/>');border:none;width:16px;height:16px;cursor: pointer;"
							onclick="viewDoc('<s:property value="id" />','<s:property value="formid" />','<%=type%>')" />
						</td>
					</s:if>
				</tr>
			</s:iterator>

			<!-- 字段值汇总 -->
			<s:if test="content.sum">
				<tr class="listDataTr">
					<!-- <td class="table-td">&nbsp;</td> -->
					<s:iterator value="content.columns" status="rowStatus">
						<td class="listDataTrTd"><s:property
							value="getSumByDatas(datas, #request.htmlBean.runner, #session.FRONT_USER)" />&nbsp;
						</td>
					</s:iterator>
				</tr>
			</s:if>
		</table>
		</div>
		</div>
	
	</div>


<s:if test="#parameters.selectOne[0] == 'true'">
<div class="selectListBox">
	<table width="100%">
		<tr>
			<td>{*[cn.myapps.core.dynaform.view.choose_value]*}:</td>
		</tr>
		<tr>
			<td><div id="selectList"></div></td>
		</tr>
	</table>
</div>
</s:if>	

<!-- 分页导航(page navigate)1 -->	
<nav id="footer" class="text-center">
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
<div style="height:57px;"></div>

</div></div>
	

<div class="card_space_fix zindex10">
	<table width="100%" cellspacing="10">
	    <tbody>
	    <tr class="dialog_Btn">
			<s:if test="(#parameters.mutil[0] == 'true'||#parameters.selectOne[0] == 'true')&&#parameters.isEdit[0] == 'true'">				
				<td><a class="btn btn-positive btn-block" data-transition="fade" 
				onClick="ev_ok()">{*[OK]*}</a></td>
			</s:if>
			<s:if test="#parameters.isEdit[0] == 'true'">
				<td><a class="btn btn-positive btn-block" data-transition="fade" 
				onClick="ev_doClear()">{*[Clear]*}</a></td>
			</s:if>	
		</tr>
		</tbody>
	</table>
</div>	
	
	
	</s:form>





	</body>
	<script lanaguage="javaScript">
	var contextPath = '<%= request.getContextPath()%>' ;
	function resetAll() {
		var elements = document.forms[0].elements;
		if(elements){
			for (var i = 0; i < elements.length; i++) {
				//alert(elements[i].type);
				if (elements[i].type == 'text') {
				   if(elements[i].id==elements[i].name){
				   }
					elements[i].value="";
				}
			}
		}
	}

	function showView(docid, formid) {
		// 查看common.js
		var url = docviewAction;
		if (docid != null && formid != null) {
			url += '?_docid=' + docid + '&_formid=' +  formid; 
		}
		var rtn = showView('{*[Select]*}', url);
		document.location.reload();
	}

	function viewDoc(docid, formid,isDiv) {
		// 查看common.js
		var url = docviewAction;
		url += '?_docid=' + docid;
		if (formid != null && formid != "") {
			url += '&_formid=' +  formid;
		}
		type = document.getElementsByName('_isdiv')[0].value;
		url += "&show_act=false&signatureExist=false";
		url = appendApplicationidByView(url);
		
		showfrontframe({
			title : '{*[Select]*}',
			url : url,
			w : 800,
			h : 600,
			callback : function(result) {
			}
		});
	}
	
</script>
</o:MultiLanguage>
</html>