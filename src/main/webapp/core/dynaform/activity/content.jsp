<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cn.myapps.base.action.BaseAction" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="cn.myapps.constans.Web" %>
	
<%@include file="/common/tags.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:bean name="cn.myapps.core.dynaform.summary.action.SummaryCfgHelper" id="sh">
	<s:param name="moduleid" value="#parameters.s_module" />
</s:bean>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% 
	String applicationid=request.getParameter("application");
	String s_moduleid=request.getParameter("s_module");
	String index=request.getParameter("index");
%>
<%
	String contextPath = request.getContextPath();
%>

<%@page import="cn.myapps.core.dynaform.activity.ejb.ActivityType"%>
<html><o:MultiLanguage>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>{*[Activity]*}</title>
<link rel="stylesheet" href="<s:url value='/resource/css/main.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/fonts/awesome/font-awesome.min.css'/>" type="text/css" />
<%@include file="/common/head.jsp"%>
<script src='<s:url value="/dwr/engine.js"/>'></script>
<script src='<s:url value="/dwr/util.js"/>'></script>
<script src='<s:url value="/dwr/interface/DWRHtmlUtil.js"/>'></script>
<script src='<s:url value="/dwr/interface/ActivityUtil.js"/>'></script>
<script src='<s:url value="/dwr/interface/Sequence.js"/>'></script>
<script src='<s:url value="/script/generic.js"/>'></script>
<script src='<s:url value="/script/jquery.placeholder.1.3.js"/>'></script>
<script src="<s:url value='/script/json/json2.js'/>"></script>
<script language="JavaScript" type="text/javascript">
var iconImgValue = "";
var iconFontValue = "";
jQuery(function(){
	//-aimar-阻止<button type="button">的默认行为，因为在ff下点击<from>表单中的<button type="button">会默认提交表单
	jQuery("button").click(function(e){e.preventDefault();});

	jQuery("#icon_switch input").on("click",function(){
		var typeVal = jQuery(this).val();
		jQuery("#icon_tr").find("span").hide();
		jQuery("#icon-"+typeVal).show();
		if(typeVal == "img"){
			document.getElementById("icon").value=iconImgValue;
		}else if(typeVal == "font"){
			document.getElementById("icon").value=iconFontValue;
		}
	});
	
	controlButtonType();
});

function controlButtonType(){
	var index = '<%=index%>';
	if(index!='null'){
		document.getElementById("type").disabled = true;		
		}
}
var args = OBPM.dialog.getArgs();
var parentObj = args['parentObj'];
function ev_init(fn1,fn2,fn3,fn4,fn5) {
	var instance = '<%= request.getParameter("application")%>';
	var mdlid = '<%= request.getParameter("s_module")%>';
	
	var viewid = '<%= request.getParameter("_viewid")%>';
	var formid = '<%= request.getParameter("_formid")%>';
	if(formid!="" && formid!="null"){
		document.getElementById("stateToShow_tr").style.display="";
		window.top.toThisHelpPage("application_module_form_activity_button_info");
	}else if(viewid!="" && viewid!="null"){
		document.getElementById("stateToShow_tr").style.display="none";
		window.top.toThisHelpPage("application_module_view_activity_Button_info");
	}
	
	var def1 = document.getElementsByName(fn1)[0].value;
	var def2 =  document.getElementsByName(fn2)[0].value;
	var def3 =  document.getElementsByName(fn3)[0].value;
	var def4 =  document.getElementsByName(fn4)[0].value;
	//var def5 =  document.getElementsByName(fn5)[0].value;
	var afterScript = document.getElementById("afterScriptId");
	var excelSub_tr = document.getElementById("expSub_tr");
	var l_BeforeActionScript = document.getElementById("l_BeforeActionScript");
	var l_ActionScript = document.getElementById("l_ActionScript");
	if(activity.expSub){document.getElementById("expSub").value = activity.expSub};
	if (!document.getElementsByName(fn1)[0].value){def1 = activity.type}
	if (!document.getElementsByName(fn2)[0].value){def2 = activity.onActionView}
	if (!document.getElementsByName(fn3)[0].value){def3 = activity.onActionForm}
	if (!document.getElementsByName(fn4)[0].value){def4 = activity.onActionFlow}
	//if (!document.getElementsByName(fn5)[0].value){def5 = activity.onActionPrint}
	else if (document.getElementsByName(fn1)[0].value=='none'){
		def2 = 'none';
		def3 = 'none';
		def4 = 'none';
		def5 = 'none';
	}
	
	oview_tr.style.display = "none";
	oprint_tr.style.display = "none";
	oflow_tr.style.display = "none";
	fshowtype_tr.style.display="none";		
	oform_tr.style.display = "none";
	ofilename_tr.style.display = "none";
	approve_tr.style.display = "none";
	afterScript.style.display = "";
	l_BeforeActionScript.style.display = "";
	l_ActionScript.style.display = "none";
	oexcelconfig_tr.style.display = "none";
	jumpActProp_tr.style.display = "none";
	jumpMode_tr.style.display = "none";
	jumpActOpenType_tr.style.display = "none";
	expSub_tr.style.display = "none";
	adjustmentPermission_tr.style.display = "none";
	editMode_tr.style.display = "none";
	startFlowScript_tr.style.display = "none";
	dispatcherMode_tr.style.display = "none";
	dispatcherUrl_tr.style.display = "none";
	dispatcherParams_tr.style.display = "none";
	mobileremind.style.display = "none";
	summaryTips.style.display = "none";
	document.getElementById("afterScriptLableId2").style.display = "none";
	document.getElementById("afterScriptLableId").style.display = "";
	document.getElementById("beforeScriptId").style.display = "";
	transpond_tr.style.display = "none";
	contextMenu_tr.style.display = "none";
	withOld_tr.style.display = "none";
	document.getElementById("afterActionType_tr").style.display = "none";
	document.getElementById("resultTypeDispatcherUrl_tr").style.display = "none";
	
	
	var func = new Function("ev_init('"+fn1+"','"+fn2+"','"+fn3+"','"+fn4+"','"+fn5+"')");
	
	document.getElementsByName(fn1)[0].onchange = func;
	if (document.getElementById("parentView").value) {
		ActivityUtil.createViewType(fn1,def1,function(str) {var func=eval(str);func.call();removeDeprecatedActivityTypeOption();});
	} else if (document.getElementById("parentForm").value){
		ActivityUtil.createFormType(fn1,def1,function(str) {var func=eval(str);func.call();removeDeprecatedActivityTypeOption();});
	} else {
		ActivityUtil.createType(fn1,def1,function(str) {var func=eval(str);func.call();});
	}
	ActivityUtil.createOnActionView(fn2,def1,mdlid,def2,instance,function(str) {var func=eval(str);func.call()});
	ActivityUtil.createOnActionForm(fn3,def1,mdlid,def3,instance,function(str) {var func=eval(str);func.call()});
	ActivityUtil.crateOnActionFlow(fn4,def1,mdlid,def4,function(str) {var func=eval(str);func.call()});
	//ActivityUtil.createOnActionPrint(fn5,def1,mdlid,def5,function(str) {var func=eval(str);func.call()});
	switch (def1.toString()) {
		case '1':
			oview_tr.style.display = "";
			break;
		case '3':
			contextMenu_tr.style.display = "";
			break;
		case '5':
			oflow_tr.style.display = "";
			fshowtype_tr.style.display="";
			break;
		case '6':
			afterScript.style.display = "none";
			l_BeforeActionScript.style.display = "none";
			l_ActionScript.style.display = "";
			break;
		case '2':
			contextMenu_tr.style.display = "";
			oform_tr.style.display = "";
			break;
		case '13':
			document.getElementById("afterScriptLableId2").style.display = "";
			document.getElementById("afterScriptLableId").style.display = "none";
			document.getElementById("beforeScriptId").style.display = "none";
			document.getElementById("afterActionType_tr").style.display = "";
			initResultType();
	        break;
		case '14':
			mobileremind.style.display = "";
			break;
		case '15':
			mobileremind.style.display = "";
			break;
		case '16':
			mobileremind.style.display = "";
			expSub_tr.style.display = "";
			break;
		case '18':
			mobileremind.style.display = "";
			oform_tr.style.display = "";
			break;
		case '20':
			mobileremind.style.display = "";
			contextMenu_tr.style.display = "";
			approve_tr.style.display = "";
			break;
		case '24':
			mobileremind.style.display = "";
			break;
		case '25':
			mobileremind.style.display = "";
			document.getElementById("afterScriptId").style.display = "none"; // 隐藏执行后脚本
			break;
		case '26':
			mobileremind.style.display = "";
			ofilename_tr.style.display = "";
			document.getElementById("afterScriptId").style.display = "none"; // 隐藏执行后脚本
			break;
		case '27':
			mobileremind.style.display = "";
			oexcelconfig_tr.style.display = "";
			document.getElementById("afterScriptLableId").style.display = "";
			document.getElementById("beforeScriptId").style.display = "none";
			document.getElementById("afterScriptId").style.display = "";
			break;
		case '28':
			mobileremind.style.display = "";
			document.getElementById("beforeScriptId").style.display = "none";
			document.getElementById("afterScriptId").style.display = "none";
			break;
		case '29':
			mobileremind.style.display = "";
			document.getElementById("beforeScriptId").style.display = "none"; // 隐藏执行前脚本
			document.getElementById("afterScriptId").style.display = "none"; // 隐藏执行后脚本
			contextMenu_tr.style.display = "";
			break;
		case '30':
			mobileremind.style.display = "";
			oprint_tr.style.display = "";
			break;
		case '31':
			oprint_tr.style.display = "";
			break;
		case '32':
			jumpActProp_tr.style.display = "";
			document.getElementById("beforeScriptId").style.display = "none";
			document.getElementById("afterScriptId").style.display = "none";
			break;
		case '33':
			mobileremind.style.display = "";
			editMode_tr.style.display = "";
			if(document.getElementsByName("content.editMode")[1].checked){
				startFlowScript_tr.style.display = "";
			}
			break;
		case '36':
			mobileremind.style.display = "";
			break;
		case '37':
			document.getElementById("afterScriptId").style.display = "none"; // 隐藏执行后脚本
			transpond_tr.style.display = "";
			mobileremind.style.display = "";
			summaryTips.style.display = "";
			
			break;
		case '39':
			document.getElementById("afterScriptId").style.display = "none"; // 隐藏执行后脚本
			mobileremind.style.display = "";
			dispatcherMode_tr.style.display = "";
			dispatcherUrl_tr.style.display = "";
			dispatcherParams_tr.style.display = "";
			break;
		case '38':
			mobileremind.style.display = "";
			adjustmentPermission_tr.style.display = "";
			break;
		case '40':
			mobileremind.style.display = "";
			break;
		case '42':
			withOld_tr.style.display = "";
			break;
		case '43':
			jumpMode_tr.style.display = "";
			jumpActOpenType_tr.style.display = "";
			document.getElementById("afterScriptId").style.display = "none"; // 隐藏执行后脚本
			initJumpMode();
			initJumpActOpenType();
			break;
		case '45':
			mobileremind.style.display = "";
			break;
		default:
			break;
		}
}	

function ev_onsubmit() {
	var typeSelect = document.getElementsByName('content.type')[0];
	var vidvalue = document.getElementsByName('_onActionViewid')[0].value;
	var fidvalue = document.getElementsByName('_onActionFormid')[0].value;
	var flowid = document.getElementsByName('_onActionFlowid')[0].value;
	var printid = document.getElementsByName('_onActionPrintid')[0].value;
	var aprovalue = document.getElementById('approveLimit').value;
	var transpond = document.getElementsByName('content.transpond')[0].value;
	var url = save.action;
	var _jumpType = document.getElementsByName('_jumpType');
	var moduleSelect = document.getElementsByName('moduleSelect')[0].value;
	var formSelect = document.getElementById('formSelect').value;
	var fileNameScript = document.getElementById("fileNameScript").value;
	var toolbarChecked = document.getElementsByName("showInToolbar")[0];
	var contextMueuChecked = document.getElementsByName("contextMenu")[0];
	var startFlowScript = document.getElementById("startFlowScript").value;
	var jumpMode = document.getElementsByName('jumpMode');
	var dispatcherUrl = document.getElementById("dispatcherUrl").value;
	if(typeSelect.value == 39 || typeSelect.value == 43) {
		//保存前把参数(json格式)赋给隐藏域
		document.getElementsByName('content.dispatcherParams')[0].value = buildQueryString();
	}
	
	if(vidvalue == '' && (typeSelect.value == 1)) {
		showMessage("error", '{*[page.core.dynaform.activity.content.onactionview]*}');
		return false;
	}
	
	else if(fidvalue == '' && (typeSelect.value == 2 || typeSelect.value == 18)) {
		showMessage("error", '{*[page.core.dynaform.activity.content.onactionform]*}');
		return false;
	}else if(aprovalue == '' && typeSelect.value == 20) {
		showMessage("error", '{*[page.core.dynaform.activity.content.approveLimit]*}');
		return false;
	}else if(transpond == '' && typeSelect.value == 37){
		showMessage("error", '请选择转发模板');
		return false;
	}else if(flowid == '' && typeSelect.value == 5) {
		showMessage("error", '{*[page.core.dynaform.activity.content.onactionflow]*}');
		return false;
	}else if(startFlowScript == '' && typeSelect.value == 33 && document.getElementsByName("content.editMode")[1].checked) {
		showMessage("error", '{*[cn.myapps.core.dynaform.activity.content.tips.start_flow_script_required]*}');
		return false;
	}
	else if(typeSelect.value == 27 && document.getElementById("impmappingconfigid").value==""){
		showMessage("error", '{*[page.resource.Excel_Import_Configurations.notchoose]*}');
		return false;
	}else if(typeSelect.value == 32 && (!_jumpType[0].checked || !moduleSelect || !formSelect)){
		showMessage("error", "{*[page.core.dynaform.activity.content.jump.validate]*}");
		return false;
	}else if(typeSelect.value == 26 && fileNameScript==""){
		showMessage("error", "{*[cn.myapps.core.dynaform.activity.content.tips.file_name_must_be]*}");
		return false;
	}else if(!contextMueuChecked.checked && !toolbarChecked.checked){
		showMessage("error", "显示位置必须选择一个!");
		return false;
	}else if(typeSelect.value == 43){
		if(jumpMode[0].checked && (!moduleSelect || !formSelect)){
			showMessage("error", "{*[page.core.dynaform.activity.content.jump.validate]*}");
			return false;
		}else if(jumpMode[1].checked && dispatcherUrl.replace(/(^\s*)|(\s*$)/g, "")==""){
			showMessage("error", "{*[cn.myapps.core.dynaform.activity.content.tips.dispatcher_url_must_be]*}");
			return false;
		}
	}
	return true;
}

function resetScriptField(){
	var elements = document.forms[0].elements;
	for (var i = 0; i < elements.length; i++) {
		var el = elements[i];
		if(el.name == 'content.beforeActionScript' 
			|| el.name == 'content.hiddenScript') {
			el.value = '';
		}
	}
}

function ev_load(){
	useLoadingImage('<s:url value="/resource/images/ajax-loader.gif" />');
}

function selectField(actionName, field){
	var url = contextPath + '/core/workflow/statelabel/'+ actionName +'.action?application=' + '<s:property value="%{#parameters.application}" />';
	if (field != '' && field != null) {
		url = url + '&field=' + field;
	}
	
	var oField = document.getElementById(field);
	//var rtn = window.showModalDialog(url, oField, "dialogHeight:400px; dialogWidth:300px; status:no; scroll:no;");
	OBPM.dialog.show({
		opener:window.parent,
		width: 300,
		height: 400,
		url: url,
		args: {oField:oField},
		title: '{*[Select]*}{*[State]*}',
		close: function(rtn) {
			if(rtn){
				oField.value = rtn;
			} else if (rtn == '') {
				oField.value = rtn;
			}
		}
	});
	
}

// Class Activity
function Activity(){
	this.id = null;
	this.name = null;
	this.type = 0;
	this.onActionForm = null;
	this.onActionView = null;
	this.onActionFlow = null;
	this.onActionPrint = null;
	this.beforeActionScript = null;
	this.afterActionScript = null;
	this.hiddenScript = null;
	this.readonlyScript = null;
	this.startFlowScript = null;
	this.editMode =0;
	this.iconurl = null;
	this.approveLimit = null;
	this.orderno = 0;
	this.stateToShow = null;
	this.parentView = null;
	this.parentForm = null;
	this.fileNameScript = null;
	this.impmappingconfigid = null;
	this.jumpType = null;
	this.targetList = null;
	this.expSub = true;
	this.icon = null;
	this.contextMenu = false;
	this.showInToolbar = true;
	this.transpond = null;
	this.disableFlowNode = true;
	this.changeFlowOperator = true;
	this.changeFlowCc = true;
	this.multiLanguageLabel = null;
	this.dispatcherMode = 0;
	this.dispatcherUrl = null;
	this.dispatcherParams = null;
	this.withOld = false;
	this.jumpMode = 0;
	this.jumpActOpenType = 0;
	this.afterActionType = null;
	this.afterActionDispatcherUrlScript = null ;
}

// Parent window's activityCache
var activityCache = [];
// Current edit activity
var activity = null;

// 查看util.js
var validators = [{fieldName: "content.name", type: "required", msg:"{*[page.name.required]*}"},
					{fieldName: "content.type", type: "required", msg:"{*[page.type.required]*}"}]; 

function doSave(){
	
	if(jQuery("#icon").val()=="" && jQuery("input[name='icontype']").is(":checked")){
		var a = confirm("尚未选择图标,确定保存么?");
		if(!a){
			return false;	
		}
	}
	
	
	
	jQuery.Placeholder.cleanBeforeSubmit(); //清除表单控件的Placeholder提示
	if (ev_onsubmit() && doValidate(validators)) {
		initTargetList();
		initJumpMode();
		initResultType();
		initDispatcherMode();
		DWRUtil.getValues(activity);
		if (!activity.id) {
			doCreate(activity);
		}
		jQuery("#save_success").css("display","");
		parentObj.actProcess.refreshList(activityCache);
		OBPM.dialog.doReturn();
	}
	jQuery.Placeholder.init(); //表单控件显示的Placeholder提示
}
//初始化 TargetList
function initTargetList(){
	var selectList = document.getElementById("formSelect");
	var targetList = document.getElementById("targetList");
	setSelectedValuesToField(targetList, selectList);
}
//初始化DispatcherParams
function initDispatcherParams(){
	//回显已设置的参数
	var str = document.getElementsByName('content.dispatcherParams')[0].value;
	var datas = parseRelStr(str);
	addRows(datas);
}
//初始化AjustmentPermission
function initAjustmentPermission(){
	var disableFlowNode = document.getElementsByName('disableFlowNode');
	var changeFlowOperator = document.getElementsByName('changeFlowOperator');
	var changeFlowCc = document.getElementsByName('changeFlowCc');
	if(this.activity.disableFlowNode){
		disableFlowNode[0].checked = true;
	}
	if(this.activity.changeFlowOperator){
		changeFlowOperator[0].checked = true;
	}
	if(this.activity.changeFlowCc){
		changeFlowCc[0].checked = true;
	}
}

//初始化EditMode
function initEditMode(){
	var editMode = document.getElementsByName('content.editMode');
	if(this.activity.editMode=='0'){
		editMode[0].checked = true;
	}
	else {
		editMode[1].checked = true;
	}
}

//onclick JumpMode
function ev_jumpMode_click(object){
	if(object){
		if(object.value == 0){
			document.getElementById('jumpActProp_tr').style.display = "";
			document.getElementsByName("_jumpType")[0].checked = true;
			document.getElementById('_jumpType_ul').style.display = "none";
			document.getElementById('mobileremind').style.display = "";
			document.getElementById('dispatcherMode_tr').style.display = "none";
			document.getElementById('dispatcherUrl_tr').style.display = "none";
			document.getElementById('dispatcherParams_tr').style.display = "none";
		}else{
			document.getElementById('jumpActProp_tr').style.display = "none";
			document.getElementById('mobileremind').style.display = "";
			document.getElementById('dispatcherMode_tr').style.display = "none";
			document.getElementById('dispatcherUrl_tr').style.display = "";
			document.getElementById('dispatcherParams_tr').style.display = "";
		}
	}
}

function ev_ResultType_click(object){
	if(object){
		if(object.value == 3){
			document.getElementById('resultTypeDispatcherUrl_tr').style.display = "";
		}else{
			document.getElementById('resultTypeDispatcherUrl_tr').style.display = "none";
		}
	}
}
//初始化JumpMode
function initJumpMode(){
	var types = document.getElementsByName("jumpMode");
	var flag = true;
	for (var i=0;i < types.length;i++){  
		if (types[i].checked) {
			types[i].click(); // on type change;
			flag = false;
			break;
		}		
	}
	if (document.getElementById("parentView").value) {//视图按钮时
		types[1].click();
		document.getElementById('jumpMode_tr').style.display = "none";
	}else if (flag) {
		types[0].click();
	}
}

//初始化jumpActOpenType
function initJumpActOpenType(){
	var types = document.getElementsByName("jumpActOpenType");
	var flag = true;
	for (var i=0;i < types.length;i++){  
		if (types[i].checked) {
			types[i].click(); // on type change;
			flag = false;
			break;
		}		
	}
	if (flag) {
		types[0].click();
	}
}

//初始化DispatcherMode
function initDispatcherMode(){
	var types = document.getElementsByName("dispatcherMode");
	var flag = true;
	for (var i=0;i < types.length;i++){  
		if (types[i].checked) {
			types[i].click(); // on type change;
			flag = false;
			break;
		}		
	}
	if (flag) {
	  	types[0].click();
	}
}
//初始化afterActionScriptResultType
function initResultType(){
	var afterActionType = document.getElementsByName("afterActionType");
	var flag = true;
	
	for(var i=0;i<afterActionType.length;i++){
	   if(afterActionType[i].checked) 
	     { 
		   afterActionType[i].click();
		   flag = false;	   		
		   break; 
	   	 }
	 }
	
	if (flag) {
		afterActionType[0].click();
	}
}


function doSaveAndNew(){
	jQuery.Placeholder.cleanBeforeSubmit(); //清除表单控件的Placeholder提示
	if (ev_onsubmit() && doValidate(validators)) {
		initTargetList();
		initJumpMode();
		initResultType();
		initDispatcherMode();
		DWRUtil.getValues(activity);
		// 须保留的字段
		var tempView = activity.parentView;
		var tempForm = activity.parentForm;
		if (!activity.id) {
			doCreate(activity);
		}
		this.activity = new Activity();
		this.activity.parentView = tempView;
		this.activity.parentForm = tempForm;
		DWRUtil.setValues(activity);
		var def1 = document.getElementsByName("content.type")[0].disabled='';
		ev_init('content.type','_onActionViewid','_onActionFormid','_onActionFlowid','_onActionPrintid');
		parentObj.actProcess.refreshList(activityCache);
		cleanIcon();
		jQuery.Placeholder.init(); //表单控件显示的Placeholder提示
	}
	jQuery.Placeholder.init(); //表单控件显示的Placeholder提示
}

function doCreate(activity){
	DWREngine.setAsync(false);
	Sequence.getSequence(function(id){
		activity.id = id;
		activity.orderno = getMaxOrderNo() + 1;
		var jumpType = document.getElementsByName("_jumpType");
		for(var i=0;i<jumpType.length;i++) 
		   {
		   if(jumpType[i].checked) 
		     { 
			   activity.jumpType=jumpType[i].value; 
		   		break; 
		   		}
		   }
		   if(!activity.jumpType){
			   activity.jumpType =00;
		   }

	   var editMode = document.getElementsByName("content.editMode");
		for(var i=0;i<editMode.length;i++) 
		   {
		   if(editMode[i].checked) 
		     { 
			   activity.editMode=editMode[i].value; 
		   		break; 
		   		}
		   }
		
		var afterActionType = document.getElementsByName("afterActionType");
		for(var i=0;i<afterActionType.length;i++){
		   if(afterActionType[i].checked) 
		     { 
			   activity.afterActionType = afterActionType[i].value; 
		   		break; 
		   	 }
		 }
		activityCache.push(activity);
	});
}

function getMaxOrderNo(){
	var maxOrderNo = 0;
	for (var i = 0; i < activityCache.length; i++) {
		if (!isNaN(activityCache[i].orderno) && activityCache[i].orderno > maxOrderNo) {
			maxOrderNo = activityCache[i].orderno;
		}
	}
	return maxOrderNo;
}


function setSelectedValuesToField(oField, oSel, separator){
	if (!oField || oSel.value == "") return;
	oField.value = '';
	var moduleid = document.getElementById("moduleSelect").value;
	oField.value +=oSel.value +"|"+moduleid;
}

function getSelectedValuesByFiled(oField, separator) {
	if (!oField) return;
	separator = separator ? separator : ";";
	var valueList = oField.value.split(separator);
	return valueList;
}



window.onload = function(){
	this.activityCache = parentObj.activityCache;
	var index = '<%=index%>';
	//this.activityCache = dialogArguments['activityCache'];
	if (activityCache[index]) {
		this.activity = activityCache[index];
		DWRUtil.setValues(this.activity);
		var jumpType = document.getElementsByName("_jumpType");
		//var selectList = document.getElementById("selectedList");
		var targetList = document.getElementById("targetList");
		var afterActionType = document.getElementsByName("afterActionType");
		var options = getSelectedValuesByFiled(targetList);
		//initSelectList(selectList,options);
		
		for(var i=0;i<jumpType.length;i++) //循环
		   {
		   if(jumpType[i].value == activity.jumpType) //比较值
		     { 
			   jumpType[i].checked=true; //修改选中状态
		   	   break; //停止循环
		   		}
		   }
		for(var i=0;i<afterActionType.length;i++){
		   if(afterActionType[i].value ==  activity.afterActionType) 
		     { 
			    afterActionType[i].checked=true; //修改选中状态
		   		break; //停止循环
		   	 }
		 }
	} else {
		this.activity = new Activity();
	}
	//初始化initJumpMode
	initJumpMode();
	//初始化DispatcherMode
	initDispatcherMode();
	//初始化DispatcherParams
	initDispatcherParams();
	initAjustmentPermission();
	initEditMode();
	initResultType();
	
	ev_init('content.type','_onActionViewid','_onActionFormid','_onActionFlowid','_onActionPrintid');
//------------------------------------------------------------------------
	var moduleSelect = document.getElementById("moduleSelect");
	var formSelect = document.getElementById("formSelect");
	var targetList = document.getElementById("targetList").value;
	var formDefValue ='';
	if(targetList){
		formDefValue = targetList.split("|")[0];
		moduleSelect.value =  targetList.split("|")[1];
	}
	// before initialize
	moduleSelect.onchange = function(){
		addFormOptions(moduleSelect, formSelect.id, formDefValue);
		moduleSelect.onchange = function(){
			addFormOptions(moduleSelect, formSelect.id);
		};
	};

	moduleSelect.onchange();
	
	if(activity.icon != null && activity.icon != ''){
		try{
			var iconJson = JSON.parse(activity.icon);
			var type = iconJson.type;
			var icon = iconJson.icon;
			if(type == "img"){
				iconImgValue = activity.icon
				document.getElementById("_iconImg").src ='<s:url value="/lib/icon/" />'+icon;
				jQuery("#btn_icontypeimg").attr("checked","checked");
				jQuery("#icon_tr").find("span").hide();
				jQuery("#icon-img").show();
			}else if(type == "font"){
				iconFontValue = activity.icon
				jQuery("#icon-font>i").removeClass().addClass(icon);
				jQuery("#btn_icontypefont").attr("checked","checked");
				jQuery("#icon_tr").find("span").hide();
				jQuery("#icon-font").show();
			}
		}
		catch(err){
			document.getElementById("_iconImg").src ='<s:url value="/lib/icon/" />'+activity.icon;
		}
	}else{
		document.getElementById("_iconImg").style.display = 'none';
	}
	
	
	
	
	jQuery.Placeholder.init(); //表单控件显示的Placeholder提示
	var _onActionPrintid = document.getElementById("onActionPrint").value;
	DWREngine.setAsync(false);
	ActivityUtil.getPrinterName(_onActionPrintid, function(rtn){
		document.getElementsByName("_onActionPrintName")[0].value = rtn;
	});
	DWREngine.setAsync(true);
	cleanPromptVal();
};

function cleanPromptVal(){
	jQuery("#beforeScriptButton").click(function(){
		if(jQuery("#beforeActionScript").val() == jQuery("#beforeActionScript").attr("title"))
			jQuery("#beforeActionScript").val("");
		openIscriptEditor('content.beforeActionScript','{*[Script]*}{*[Editor]*}','{*[ActionScript]*}','content.name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#afterScriptButton").click(function(){
		if(jQuery("#afterActionScript").val() == jQuery("#afterActionScript").attr("title"))
			jQuery("#afterActionScript").val("");
		openIscriptEditor('content.afterActionScript','{*[Script]*}{*[Editor]*}','{*[AfterActionScript]*}','content.name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#readonlyScriptButton").click(function(){
		if(jQuery("#readonlyScript").val() == jQuery("#readonlyScript").attr("title"))
			jQuery("#readonlyScript").val("");
		openIscriptEditor('content.readonlyScript','{*[Script]*}{*[Editor]*}','{*[ReadOnly_Script]*}','content.name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#hiddenScriptButton").click(function(){
		if(jQuery("#hiddenScript").val() == jQuery("#hiddenScript").attr("title"))
			jQuery("#hiddenScript").val("");
		openIscriptEditor('content.hiddenScript','{*[Script]*}{*[Editor]*}','{*[Hidden_Script]*}','content.name','{*[Save]*}{*[Success]*}');
	});
}

function initSelectList(selectList,options){
	for (var i=0; i<options.length; i++) {
		var stropt = options[i];
		var opt =stropt.split("|");
		selectList.options.add(new Option(opt[0],opt[1]));
	}
}

function doCfgPrint(){
	var formid = '<s:property value="#parameters._formid" />';
	var def1 = document.getElementsByName("content.type")[0].value;
	var url = '<s:url value="/core/dynaform/activity/printer/dynaprinter.jsp"/>';
	url += '?formid='+formid;
	if(def1.toString()=='15'){
		//url += '&flowid='+flowid;
	}
	window.open(url,'DynamicPrint','newwindow');
}

function addFormOptions(oModule, relatedFormId, defValues) {
	parentObj.FormHelper.get_formListByModuleType(oModule.value, function(options) {
		var oFormSelect = document.getElementById(relatedFormId);
		//DWRUtil.removeAllOptions(oSelectedList.id);
		addOptions(relatedFormId, options, defValues);
	});
}

function addOptions(relatedFormId, options, defValues){
	var el = document.getElementById(relatedFormId);
	if(relatedFormId){
		DWRUtil.removeAllOptions(relatedFormId);
		DWRUtil.addOptions(relatedFormId, options);
		if (defValues) {
			DWRUtil.setValue(relatedFormId, defValues);
		}
		if (el.onchange && typeof(el.onchange) == "function") {
			el.onchange();
		}
	}
}


function ev_onEditMode_click(value){
	if(value==0){
		activity.editMode =0;
		startFlowScript_tr.style.display = "none";
	}
	else if(value==1){
		activity.editMode =1;
		startFlowScript_tr.style.display = "";
	}
}

function selectIcon(){
	  var url = contextPath + '/core/resource/iconLib.jsp' ;
	  OBPM.dialog.show({
			opener:window.parent.parent,
			width: 700,
			height: 500,
			url: url,
			args: {},
			title: '{*[Select]*}{*[Icon]*}',
			close: function(rtn) {
				if (rtn != null && rtn != '') {
					document.getElementById("_iconImg").style.display = '';
					document.getElementById("icon").value='{"icon":"'+rtn+'","type":"img"}';
					document.getElementById("_iconImg").src ='<s:url value="/lib/icon/" />'+rtn;
					iconImgValue = '{"icon":"'+rtn+'","type":"img"}';
				}
			 	
			}
		});
	}

function cleanIcon(){
	document.getElementById("_iconImg").style.display = 'none';
	document.getElementById("icon").value='';
	iconImgValue = '';
}

function selectIconFont(){
	  var url = contextPath + '/core/resource/iconFontLib_Awesome.jsp' ;
	  OBPM.dialog.show({
			opener:window.parent.parent,
			width: 700,
			height: 500,
			url: url,
			args: {},
			title: '{*[Select]*}{*[Icon]*}',
			close: function(rtn) {
				if (rtn != null && rtn != '') {
					document.getElementById("icon").value='{"icon":"'+rtn+'","type":"font"}';
					jQuery("#icon-font>i").removeClass().addClass(rtn);
					iconFontValue = '{"icon":"'+rtn+'","type":"font"}';
				}
			}
		});
	}

function cleanIconFont(){
	jQuery("#icon-font>i").removeClass();
	document.getElementById("icon").value='';
	iconFontValue = '';
}

function selectMultiLanguage(actionName, field){
	var url = contextPath + '/core/multilanguage/'+ actionName +'.action?application=' + '<s:property value="%{#parameters.application}" />';
	
	var oField = document.getElementsByName(field)[0];
	OBPM.dialog.show({
		opener:window.parent,
		width: 610,
		height: 440,
		url: url,
		args: {},
		title: '{*[Select]*}{*[MultiLanguage]*}',
		close: function(rtn) {
			if(rtn){
				oField.value = rtn;
			} else if (rtn == '') {
				oField.value = rtn;
			}
		}
	});
	
}

//生成请求参数
function buildQueryString(){
	var pkey = document.getElementsByName("paramKey");
	var pvalue = document.getElementsByName("paramValue");
	var str = '[';
	for (var i=0;i<pkey.length;i++) {
		if (pkey[i].value != '' && pvalue[i].value != '' ){
				str += '{';
				str += pkey[i].name +':\''+pkey[i].value+'\',';
				str += pvalue[i].name +':\''+pvalue[i].value+'\'';
				str += '},';
		}
	}
	str += ']';
	return  str;	

}

//根据mapping str获取data array
function parseRelStr(str) {
	var obj = eval(str);
	if (obj instanceof Array) {
		return obj;
	} else {
		return new Array();	
	}
}

var rowIndex = 0;
var getParamKey = function(data) {
		if (data != null && data != undefined) {
	  	var s =''; 
		s +='<input type="text" id="paramKey'+ rowIndex +'" name="paramKey" style="width:100" value="'+HTMLDencode(data.paramKey)+'" />';
		return s; 
	}
};

var getParamValue = function(data) {
	if (data != null && data != undefined) {
	  	var s =''; 
		s +='<input type="text" id="paramValue'+ rowIndex +'" name="paramValue" style="width:100" value="'+HTMLDencode(data.paramValue)+'" />';
		return s; 
	}
};

var getDelete = function(data) {
	if (data != null && data != undefined) {
		
	  	var s = '<input type="button" value="{*[Delete]*}" onclick="delRow(tb, this.parentNode.parentNode)"/>';
	  	rowIndex ++;
	  	return s;
	}
};

// 根据数据增加行
function addRows(datas) {
	var cellFuncs = [getParamKey, getParamValue, getDelete];

	var rowdatas = datas;
	if (!datas || datas.length == 0) {
		var data = {paramKey:'', paramValue:''};
		rowdatas = [data];
	}
	DWRUtil.addRows("tb", rowdatas, cellFuncs);
	
}

// 删除一行
function delRow(elem, row) {
	if (elem) {
		elem.deleteRow(row.rowIndex);
		//rowIndex --;
	}
}

function editPrintTemplate(){

	var url = contextPath + "/core/dynaform/printer/listforact.action?s_module=" + "<%=s_moduleid%>" + "&application=" + "<%=applicationid%>" + "&mode=module" + "&source=act";

	OBPM.dialog.show({
		width: 800,
		height: 500,
		url: url,
		title: '{*[cn.myapps.core.dynaform.activity.button.edittemplate]*}',
		close: function(rtn) {
			if(rtn){
				var obj = eval("(" + rtn + ")");
				document.getElementsByName("_onActionPrintid")[0].value = obj.value;
				document.getElementsByName("_onActionPrintName")[0].value = obj.name;
			}
		}
	});
}

/**
 * 删除过时的操作按钮动作类型选项
 */
function removeDeprecatedActivityTypeOption(){
	if( !activity.id || activity.id.length==0){
		var types = document.getElementById("type");
		for(var i=0;i<types.options.length;i++){
			var value = types.options[i].value;
			if(value==12 || value==17 || value==9 || value==32 || value==39 || value==41){//需要屏蔽的动作类型
				 types.options.remove(i);
				 removeDeprecatedActivityTypeOption();
			}
		}
	}
}

</script>
</head>
<body style="margin:0px;padding: 5px;overflow-x: hidden;overflow-y:auto">
<div id="div_act" style='overflow:auto' class="content_table">
<s:form name="actform" action="save" method="post" onsubmit="" validate="true" theme="simple">
	 <%@include file="/common/page.jsp"%>
	<input type="hidden" name="s_module" value="<s:property value='#parameters.s_module'/>">
	<input type="hidden" name="moduleid" value="<s:property value='#parameters.s_module'/>">
	<input type="hidden" id="targetList" name="targetList" value="">
	<s:bean name="cn.myapps.core.deploy.module.action.ModuleHelper"
		id="mh">
		<s:param name="applicationid" value="%{#parameters.application}" />
	</s:bean>
	<table width="100%">
		<tr valign="top">
			<td><div id="save_success" style="display:none">操作已经保存</div></td>
			<td align="right">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td  valign="top">
						<button type="button" class="button-class" onClick="doSaveAndNew()">
							<img src="<s:url value="/resource/imgnew/act/act_4.gif"/>">{*[Save&New]*}
						</button>
					</td>
					
					<td width="60" valign="top">
						<button type="button" id="btn_confirm" class="button-class" onClick="doSave()">
							<img src="<s:url value="/resource/imgnew/act/act_4.gif"/>">{*[OK]*}
						</button>
					</td>
					<td width="65" valign="top">
						<button type="button" class="button-class" onClick="OBPM.dialog.doReturn();">
							<img src="<s:url value="/resource/imgnew/act/act_10.gif"/>">{*[Cancel]*}
						</button>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan="4" style="border-top: dotted 1px  black;">
			&nbsp;
			</td>
		</tr>
</table>
	
<%@include file="/common/msg.jsp"%>
<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
<s:hidden name="_viewid" />
<s:hidden name="_formid" />
<table>
		<s:hidden id="orderno" name="content.orderno" />
		<s:hidden id="parentView" name="parentView" value="%{#parameters._viewid}"/>
		<s:hidden id="parentForm" name="parentForm" value="%{#parameters._formid}" />
		
		<tr>
		    <td class="commFont commLabel">{*[Name]*}:</td>
			<td align="left"><s:textfield id="name" name="content.name" cssStyle="width:350px;"/></td>
		</tr>
		<tr>
		    <td class="commFont commLabel">{*[MultiLanguage]*}{*[Label]*}:</td>
			<td align="left"><s:textfield id="multiLanguageLabel" readonly="true" name="multiLanguageLabel" cssStyle="width:350px;"/>
				<button type="button" class="button-image" onClick="selectMultiLanguage('selectlist','multiLanguageLabel');">
					<img src="<s:url value="/resource/image/search.gif"/>">
				</button>
			</td>
		</tr>
		<tr>
		     <td class="commFont commLabel">{*[cn.myapps.core.dynaform.form.action]*}:</td>
			<td align="left"><s:select id="type" cssStyle="width:350px;" emptyOption="true" name="content.type" list="{}" /></td>
		</tr>
		<tr id="withOld_tr">
		    <td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.withold]*}:</td>
			<td align="left"><s:checkbox id="withOld" name="withOld" theme="simple" value="false"/></td>
		</tr>
		<tr id="mobileremind" Style="display:none">
			<td></td>
			<td class="tipsStyle">*{*[cn.myapps.core.dynaform.activity.label.Mobile_client_does_not_support]*}</td>
		</tr>
		<tr id="transpond_tr" Style="display:none">
		     <td class="commFont commLabel">{*[core.dynaform.form.activity.transpond.template]*}:</td>
			<td align="left"><s:select id="transpond" cssStyle="width:350px;" emptyOption="false" name="content.transpond" 
				list="#sh.getSummaryByFormIdAndScope(#parameters._formid,1)" /></td>
		</tr>
		<tr id="summaryTips" Style="display:none">
			<td></td>
			<td class="tipsStyle">*定义分享的格式与内容</td>
		</tr>
		<tr id="icon_switch">
			<td class="commFont commLabel">{*[Icon]*}类型:</td>
			<s:hidden id="icon" name="content.icon"/>
			<td align="left" class="commFont">
				<input type="radio" name="icontype" id="btn_icontypeimg" value="img">
				<label for="btn_icontypeimg">图片</label>
				<input type="radio" name="icontype" id="btn_icontypefont" value="font">
				<label for="btn_icontypefont">字体图标</label>
			</td>
		</tr>
		<tr id="icon_tr">
			<td class="commFont commLabel">{*[Icon]*}:</td>
			<td align="left" class="commFont">
				<span id="icon-img">
					<img id="_iconImg" alt="" src=''>
					<input type="button" class="button-cmd" onclick="selectIcon()" value="{*[Select]*}"/>
					<input type="button" class="button-cmd" onclick="cleanIcon()" value="{*[Clear]*}"/>
				</span>
				<span id="icon-font" style="display:none">
					<i class="" style="font-size: 24px;"></i>
					<input type="button" class="button-cmd" onclick="selectIconFont()" value="{*[Select]*}"/>
					<input type="button" class="button-cmd" onclick="cleanIconFont()" value="{*[Clear]*}"/>
				</span>
				
			</td>
		</tr>
		
		<tr id="contextMenu_tr" Style="display:none">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.position]*}：</td>
			<td>
				<s:checkbox name="showInToolbar" theme="simple" value="true"/>{*[cn.myapps.core.dynaform.activity.toolbar]*}
				<div><s:checkbox name="contextMenu" theme="simple" value="false"/>{*[cn.myapps.core.dynaform.activity.contextMenu]*}<span class="tipsStyle">(*该功能不支持网格、地图、日历、甘特视图)</span></div>
			</td>
		</tr>

		<tr id="oview_tr">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.onActionView]*}:</td>
			<td align="left"><s:select id="onActionView" cssStyle="width:350px;" emptyOption="true" name="_onActionViewid" list="{}" /></td>
		</tr>

		<tr id="oform_tr">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.onActionForm]*}:</td>
			<td align="left"><s:select id="onActionForm" cssStyle="width:350px;" emptyOption="true"  name="_onActionFormid" list="{}" /></td>
		</tr>
		<tr id ="editMode_tr">
  			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.form.process_startup_mode]*}:</td>
			<td>
			<input type="radio" name="content.editMode" value="0" onClick="ev_onEditMode_click(this.value)" checked>{*[cn.myapps.core.dynaform.activity.attr.editMode.select_by_user]*}
			<input type="radio" name="content.editMode" value="1" onClick="ev_onEditMode_click(this.value)">{*[cn.myapps.core.dynaform.activity.attr.editMode.iscript]*}
			</td>
		</tr>
		<tr id="oflow_tr">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.onActionFlow]*}:</td>
			<td align="left"><s:select id="onActionFlow" cssStyle="width:350px;" emptyOption="true" name="_onActionFlowid" list="{}" /></td>
		</tr>
		
		<tr id ="adjustmentPermission_tr">
  			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.adjustmentPermission]*}:</td>
			<td>
			<div><s:checkbox  name="disableFlowNode" id="disableFlowNode"  theme="simple"/>{*[disableFlowNode]*}</div>
			<div><s:checkbox  name="changeFlowOperator" id="changeFlowOperator"  theme="simple"/>{*[changeFlowOperator]*}</div>
			<div><s:checkbox  name="changeFlowCc" id="changeFlowCc"  theme="simple"/>{*[changeFlowCc]*}</div>
			</td>
		</tr>
		
		<tr id ="startFlowScript_tr">
  			<td class="commFont commLabel">{*[StartFlowScript]*}:</td>
			<td><s:textarea id="startFlowScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.start_flow_script]*}*/" cssClass="input-cmd" cssStyle="width:350px;" name="content.startFlowScript" cols="50" rows="3" />
			<button type="button" class="button-image" onclick="openIscriptEditor('content.startFlowScript','{*[Script]*}{*[Editor]*}','{*[StartFlowScript]*}','content.name','{*[Save]*}{*[Success]*}');"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
			</td>
		</tr>
		
		<tr id="fshowtype_tr">
		    <td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.flowInfoShowType]*}:</td>
			<td align="left">
				<s:select id="flowShowType" cssStyle="width:350px;" emptyOption="false" name="_flowShowType" list="#{'ST01':'{*[cn.myapps.core.dynaform.activity.flowInfoShowType.default]*}','ST02':'{*[cn.myapps.core.dynaform.activity.flowInfoShowType.div]*}','ST03':'{*[cn.myapps.core.dynaform.activity.flowInfoShowType.hide]*}'}" />
			</td>
		</tr>
		
		<tr id="oprint_tr">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.onActionPrint]*}:</td>
			<td align="left">
			<s:hidden id="onActionPrint" name="_onActionPrintid" />
			<s:textfield id="onActionPrintName" cssStyle="width:234px;" name="_onActionPrintName" readonly="true"/><input type="button" style="width:114px;" class="button-cmd" onclick="editPrintTemplate()" value="{*[cn.myapps.core.dynaform.activity.button.edittemplate]*}"/>
			</td>
		</tr>
		
		<tr id="jumpMode_tr" style="display: none">
			<td class="commFont commLabel">{*[Type]*}:</td>
			<td>
				<div><input type="radio" name="jumpMode" value="0" onClick="ev_jumpMode_click(this)" >{*[cn.myapps.core.dynaform.activity.type.jump.form]*}
				<input type="radio" name="jumpMode" value="1" onClick="ev_jumpMode_click(this)">{*[cn.myapps.core.dynaform.activity.type.jump.url]*}</div>
				<!-- 
				<s:radio id="jumpMode" name="jumpMode"
						theme="simple" cssStyle="width:25px;" onclick="ev_jumpMode_click(this)"
						list="#{0:'{*[跳转到动态表单]*}',1:'{*[跳转到指定URL]*}'}"></s:radio>
				 -->
			</td>
		</tr>
		
		<tr id="jumpActProp_tr" style="display: none">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.jumpConfig]*}</td>
			<td align="left">
				<div id="jumpActProp">
					<ul id="_jumpType_ul">
						{*[cn.myapps.core.dynaform.activity.label.action]*}：<s:radio id="_jumpType" name="_jumpType"
							 theme="simple" cssStyle="width:25px;"
							list="#{0:'{*[New]*}'}"></s:radio>
					</ul>
				    <ul>
					 	{*[Module]*}： <s:select id="moduleSelect" name="moduleSelect"
						list="#mh.getModuleSel(#parameters.application)"
						cssClass="input-cmd" onchange="addFormOptions(this, 'formSelect')"  />
		 			</ul>
					 <ul>
					 	{*[cn.myapps.core.dynaform.activity.label.targetForm]*}：<s:select id="formSelect" cssClass="input-cmd" name="formName" theme="simple" list="{}"/>
					  </ul>
				</div>
				</td>
		</tr>
		<tr id="ofilename_tr">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.filePathScript]*}:</td>
			<td><s:textarea id="fileNameScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.new_file_and_return]*}*/" cssClass="input-cmd" cssStyle="width:350px;" name="content.fileNameScript" cols="50" rows="3" />
			<button type="button" class="button-image" onclick="openIscriptEditor('content.fileNameScript','{*[Script]*}{*[Editor]*}','{*[cn.myapps.core.dynaform.activity.label.filePathScript]*}','content.name','{*[Save]*}{*[Success]*}');"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
			</td>
		</tr>
		
		<tr id="oexcelconfig_tr">
			<s:bean name="cn.myapps.core.dynaform.dts.excelimport.config.action.ImpHelper" id="hepler" />
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.ImportMappingConfig]*}:</td>
			<td><s:select id="impmappingconfigid" name="_impmappingconfigid" cssStyle="width:350px;" list="#hepler.get_allMappingConfig(#parameters.application)" listKey="id" listValue="name" theme="simple" emptyOption="true" /></td>
		</tr>
	
		<tr id="dispatcherMode_tr">
			<td class="commFont commLabel">{*[DispatcherMode]*}:</td>
			<td><s:radio id="dispatcherMode" name="dispatcherMode"
						theme="simple" cssStyle="width:25px;"
						list="#{0:'{*[Forward]*}',1:'{*[Redirect]*}'}"></s:radio>
			</td>
		</tr>
		
		<tr id="dispatcherUrl_tr">
			<td class="commFont commLabel">{*[DispatcherUrl]*}{*[Script]*}:</td>
			<td><s:textarea id="dispatcherUrl" cssClass="input-cmd" cssStyle="width:350px;" name="content.dispatcherUrl" cols="50" rows="3" />
			<button type="button" class="button-image" onclick="openIscriptEditor('content.dispatcherUrl','{*[Script]*}{*[Editor]*}','{*[DispatcherUrl]*}{*[Script]*}','content.name','{*[Save]*}{*[Success]*}');"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
			</td>
		</tr>
		
		<tr id="dispatcherParams_tr">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.parameter]*}:</td>
			<td>
				<table class="" border=1 cellpadding="0"
					cellspacing="0" bordercolor="" width="350px">
					<tbody id="tb">
						<tr>
							<td align="left" class="commFont">{*[cn.myapps.core.dynaform.activity.label.parameterName]*}</td>
							<td align="left" class="commFont">{*[cn.myapps.core.dynaform.activity.label.parameterValue]*}</td>
							<td align="left"><input type="button" value="{*[Add]*}"
								onclick="addRows()" /></td>
						</tr>
					</tbody>
				</table>
				<s:hidden id="dispatcherParams" name="content.dispatcherParams" />
			</td>
		</tr>
	
		<tr id="jumpActOpenType_tr" style="display:none">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.jumpActOpenType]*}</td>
			<td align="left">
				<div>
					<input type="radio" name="jumpActOpenType" value="0" checked>{*[cn.myapps.core.dynaform.activity.label.opentype_currentpage]*}
					<input type="radio" name="jumpActOpenType" value="1">{*[cn.myapps.core.dynaform.activity.label.opentype_poplayer]*}
					<input type="radio" name="jumpActOpenType" value="2">{*[cn.myapps.core.dynaform.activity.label.opentype_tab]*}
					<input type="radio" name="jumpActOpenType" value="3">{*[cn.myapps.core.dynaform.activity.label.opentype_newpage]*}
				</div>
			</td>
		</tr>		
					
		<tr id="stateToShow_tr">
			<td class="commFont commLabel">{*[StateToShow]*}:</td>
			<td><s:textfield id="stateToShow" readonly="true" cssStyle="width:350px;" cssClass="input-cmd" name="content.stateToShow" />
			
			<button type="button" class="button-image" onClick="selectField('statelist','stateToShow');">
				<img src="<s:url value="/resource/image/search.gif"/>">
			</button>
			</td>
		</tr>
		
		<tr id="approve_tr">
			<td class="commFont commLabel">{*[cn.myapps.core.dynaform.activity.label.sumbitToNextNode]*}:</td>
			<td><s:textfield id="approveLimit" cssStyle="width:350px;" readonly="true" cssClass="input-cmd" name="content.approveLimit" />
			
			<button type="button" class="button-image" onClick="selectField('statelist','approveLimit');">
				<img src="<s:url value="/resource/image/search.gif"/>">
			</button>
			</td>
		</tr>
		 
		<tr id ="beforeScriptId">
  			<td  class="commFont commLabel"><div id="l_ActionScript">{*[ActionScript]*}:</div><div id="l_BeforeActionScript">{*[BeforeActionScript]*}:</div></td>
			<td><s:textarea id="beforeActionScript"  title="/*{*[cn.myapps.core.dynaform.activity.content.tips.button_action_script]*}*/" cssClass="input-cmd" cssStyle="width:350px;height: 100px;" name="content.beforeActionScript" cols="50" rows="3" />
			<button type="button" id="beforeScriptButton" class="button-image"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
			</td>
		</tr>
		
		<tr id ="afterScriptId">
  			<td class="commFont commLabel">
  			<div id="afterScriptLableId" >{*[AfterActionScript]*}:</div>
  			<div id="afterScriptLableId2">{*[ActionScript]*}:</div>
  			</td>
			<td><s:textarea id="afterActionScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.button_after_action_script]*}*/" cssClass="input-cmd" cssStyle="width:350px;height: 100px;" name="content.afterActionScript" cols="50" rows="3" />
			<button type="button" id="afterScriptButton" class="button-image"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
			</td>
		</tr>
		
		<tr id ="afterActionType_tr">
  			<td class="commFont commLabel">
  			<div id="afterActionTypeLableId">动作执行后操作类型:</div>
  			</td>
			<td>
			<s:radio  name="afterActionType" theme="simple" cssStyle="width:25px;"
				 list="#{0:'无',1:'返回',2:'关闭',3:'跳转'}" onclick="ev_ResultType_click(this)"></s:radio>
			</td>
		</tr>
		
		<tr id="resultTypeDispatcherUrl_tr" >
			<td class="commFont commLabel">{*[DispatcherUrl]*}{*[Script]*}:</td>
			<td><s:textarea id="afterActionDispatcherUrlScript" cssClass="input-cmd" cssStyle="width:350px;" name="content.afterActionDispatcherUrlScript" cols="50" rows="3" />
			<button type="button" class="button-image" onclick="openIscriptEditor('content.afterActionDispatcherUrlScript','{*[Script]*}{*[Editor]*}','{*[DispatcherUrl]*}{*[Script]*}','content.name','{*[Save]*}{*[Success]*}');"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
			</td>
		</tr>
		
		<tr>
  			<td class="commFont commLabel">{*[ReadOnly_Script]*}:</td>
 			<td><s:textarea id="readonlyScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.return_boolean_value]*}（true/false）*/" cssClass="input-cmd" cssStyle="width:350px;" name="content.readonlyScript" cols="50" rows="3" theme="simple"  />
 			<button type="button" id="readonlyScriptButton" class="button-image"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
 			</td>
		</tr>
		<tr>
  			<td class="commFont commLabel">{*[Hidden_Script]*}:</td>
 			<td><s:textarea id="hiddenScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.return_boolean_value]*}（true/false）*/" cssClass="input-cmd" cssStyle="width:350px;" name="content.hiddenScript" cols="50" rows="3" theme="simple"  />
 			<button type="button" id="hiddenScriptButton" class="button-image"><img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/></button>
 			</td>
		</tr>
		<tr id="expSub_tr">
  			<td class="commFont commLabel">{*[core.dynaform.view.activity.expSub]*}:</td>
 			<td><s:checkbox id="expSub" name="content.expSub" theme="simple" value="true"/></td>
		</tr>
	</table>
</s:form></div>
</body>
</o:MultiLanguage></html>
