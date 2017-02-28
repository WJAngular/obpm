<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:bean name="cn.myapps.core.dynaform.form.action.FormHelper" id="fh">
	<s:param name="moduleid" value="#parameters.moduleid" />
</s:bean>

<s:bean name="cn.myapps.core.role.action.RoleHelper" id="rh">
	<s:param name="applicationid" value="#parameters.application" />
</s:bean>

<s:bean name="cn.myapps.core.role.ejb.RoleVO" id="vh"/>
<html><o:MultiLanguage>
<HEAD>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="0">
<link href="../css/dialog.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href='<s:url value="/resource/css/main.css" />' type="text/css">
<script src='<s:url value="/dwr/util.js"/>'></script>
<script src='<s:url value="/dwr/engine.js"/>'></script>
<script src='<s:url value="/dwr/interface/Sequence.js"/>'></script>
<script src='<s:url value="/dwr/interface/FormHelper.js"/>'></script>
<script language=JavaScript src="../../dialog/sequence.js"></script>
<script language=JavaScript src="../../dialog/dialog.js"></script>
<script language=JavaScript src="../../dialog/script.js"></script>
<script src="../../dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<script src='<s:url value="/script/jquery.placeholder.1.3.js"/>'></script>
<link href="../../css/dialog.css" rel="stylesheet" type="text/css" />
<%
    String formid = request.getParameter("formid");
 %>

<SCRIPT language=JavaScript>
var dialog	= window.parent ;
var oEditor = dialog.InnerDialogLoaded() ;
// Gets the document DOM
var oDOM = oEditor.FCK.EditorDocument ;
var oActiveEl = dialog.Selection.GetSelectedElement() ;
var tempfilterField;

// 选项卡点击事件
function cardClick(cardID){
	var obj;
	for (var i=1;i<7;i++){
		obj=document.all("card"+i);
		obj.style.backgroundColor="#3A6EA5";
		obj.style.color="#FFFFFF";
	}
	obj=document.all("card"+cardID);
	obj.style.backgroundColor="#FFFFFF";
	obj.style.color="#3A6EA5";

	for (var i=1;i<7;i++){
		obj=document.all("content"+i);
		obj.style.display="none";
	}
	obj=document.all("content"+cardID);
	obj.style.display="";
	resize();
}

//初始值
function InitDocument(){
	var editMode;
	oEditor.FCKLanguageManager.TranslatePage(document) ;
		if (oActiveEl){
			temp.name.value = HTMLDencode(oActiveEl.getAttribute('name'));
			temp.refreshOnChanged.checked = oActiveEl.getAttribute('refreshOnChanged') == "true";

			if(oActiveEl.getAttribute('isDefaultValue')!=null){
				temp.isDefaultValue.checked = oActiveEl.getAttribute('isDefaultValue') == "true";
			}else{
				temp.isDefaultValue.checked = false;
			}

			temp.calculateOnRefresh.checked = oActiveEl.getAttribute('calculateOnRefresh') == "true";
			temp.limitByUser.checked = oActiveEl.getAttribute('limitByUser') == "true";
			temp.discript.value = HTMLDencode(oActiveEl.getAttribute('discript'));
			temp.filterField.value = HTMLDencode(oActiveEl.getAttribute('filterField'));
			temp.valueScript.value = HTMLDencode(oActiveEl.getAttribute('valueScript'));
			temp.validateRule.value = HTMLDencode(oActiveEl.getAttribute('validateRule'));
			temp.limitSum.value = HTMLDencode(oActiveEl.getAttribute('limitSum'));
			if(oActiveEl.getAttribute('hiddenScript')!=null){
				temp.hiddenScript.value = HTMLDencode(oActiveEl.getAttribute('hiddenScript'));
			}

			if(oActiveEl.getAttribute('hiddenValue')!=null){
				temp.hiddenValue.value = HTMLDencode(oActiveEl.getAttribute('hiddenValue'));
			}

			if(oActiveEl.getAttribute('hiddenPrintScript')!=null){
				temp.hiddenPrintScript.value = HTMLDencode(oActiveEl.getAttribute('hiddenPrintScript'));
			}

			if(oActiveEl.getAttribute('printHiddenValue')!=null){
				temp.printHiddenValue.value = HTMLDencode(oActiveEl.getAttribute('printHiddenValue'));
			}
			temp.filtercondition.value =HTMLDencode(oActiveEl.getAttribute('filtercondition'));
			temp.readonlyScript.value = HTMLDencode(oActiveEl.getAttribute('readonlyScript'));
			temp.mobile.checked = oActiveEl.getAttribute('mobile') == "true";
			editMode=HTMLDencode(oActiveEl.getAttribute('editMode'));
			
		    var items=HTMLDencode(oActiveEl.getAttribute('processDescription'));
		    parseRelStr(items);
			if(oActiveEl.getAttribute('textType')==null)
			{
				oActiveEl.setAttribute('textType','text');
			  
			}
			temp.textType.value = HTMLDencode(oActiveEl.getAttribute('textType'));
			if(oActiveEl.getAttribute('selectMode')==null)
			{
				oActiveEl.setAttribute('selectMode','multiSelect');
			  
			}
			temp.selectMode.value = HTMLDencode(oActiveEl.getAttribute('selectMode'));
			var libstr=HTMLDencode(oActiveEl.getAttribute('validateLibs'));
			var lib=libstr.split(';');
			var validateLibs=document.getElementsByName('validateLibs');
		    for(var i=0; i< lib.length; i++){
		       for(var j=0; j< validateLibs.length; j++){
	 			if(validateLibs[j].value==lib[i]){
	 				validateLibs[j].checked=true;
	 					}
	 				}
	 			}
		}
		else
			oActiveEl = null ;
		dialog.SetOkButton( true ) ;
		dialog.SetAutoSize( true ) ;
		SelectField( 'name' ) ;
		initForm();
		modeChange(editMode);
		if(temp.limitByUser.checked==false){
			document.all("filterField").style.display="none";
			tempfilterField=temp.filterField.value;
			temp.filterField.value="";
		}else{
			document.all("filterField").style.display="";
		}
		window.top.toThisHelpPage("application_module_form_info_advance_user");
		jQuery.Placeholder.init(); //表单控件显示的Placeholder提示
		cleanPromptVal();
}

function cleanPromptVal(){
	jQuery("#validateRuleButton").click(function(){
		if(jQuery("#validateRule").val() == jQuery("#validateRule").attr("title"))
			jQuery("#validateRule").val("");
		openIscriptEditor('validateRule','{*[Script]*}{*[Editor]*}','{*[Validate_Script]*}','name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#hiddenScriptButton").click(function(){
		if(jQuery("#hiddenScript").val() == jQuery("#hiddenScript").attr("title"))
			jQuery("#hiddenScript").val("");
		openIscriptEditor('hiddenScript','{*[Script]*}{*[Editor]*}','{*[Hidden_Script]*}','name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#hiddenPrintScriptButton").click(function(){
		if(jQuery("#hiddenPrintScript").val() == jQuery("#hiddenPrintScript").attr("title"))
			jQuery("#hiddenPrintScript").val("");
		openIscriptEditor('hiddenPrintScript','{*[Script]*}{*[Editor]*}','{*[Hidden_Print_Script]*}','name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#readonlyScriptButton").click(function(){
		if(jQuery("#readonlyScript").val() == jQuery("#readonlyScript").attr("title"))
			jQuery("#readonlyScript").val("");
		openIscriptEditor('readonlyScript','{*[Script]*}{*[Editor]*}','{*[ReadOnly_Script]*}','name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#valueScriptButton").click(function(){
		if(jQuery("#valueScript").val() == jQuery("#valueScript").attr("title"))
			jQuery("#valueScript").val("");
		openIscriptEditor('valueScript','{*[Script]*}{*[Editor]*}','{*[Value_Script]*}','name','{*[Save]*}{*[Success]*}');
	});
	jQuery("#optionsScriptButton").click(function(){
		if(jQuery("#optionsScript").val() == jQuery("#optionsScript").attr("title"))
			jQuery("#optionsScript").val("");
		openIscriptEditor('optionsScript','{*[Script]*}{*[Editor]*}','{*[Option_Script]*}','name','{*[Save]*}{*[Success]*}');
	});
}

function Ok()
{
	jQuery.Placeholder.cleanBeforeSubmit(); //清除表单控件的Placeholder提示
	var isok=true;
	//检查值脚本设计模式内容是否完成正确
	for(var i=0;i<document.all("editMode").length;i++){
		   if(document.all("editMode")[i].checked){
		    if(document.all("editMode")[0].checked){
		     isok=createItems();
		     if(!isok){
			     alert("{*[Value_Script]*}{*[page.content.notexist]*}");
		         return;
		     }
		    }
		  
	      }
	 }

	 
	if(ev_check()){
	oEditor.FCKUndo.SaveUndoStep() ;
	var className="cn.myapps.core.dynaform.form.ejb.UserField";
	var id=getFieldId();
	var editMode=document.getElementsByName("editMode");
	var ed="";   
    for(var i=0;i<editMode.length;i++){
		  if(editMode[i].checked){
			   ed += editMode[i].value;
	      }
	    }	
	var validateLibs=document.getElementsByName('validateLibs');
	var libs='';
	for(var i=0; i< validateLibs.length; i++){
 		if(validateLibs[i].checked){
 			libs+=validateLibs[i].value+";";
 		}
 	}
 	libs=libs.substring(0,libs.length-1);
	oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'SELECT', {
									classname: className,
									id: id,
									type:"text",
									name:HTMLEncode(temp.name.value),
									refreshOnChanged:HTMLEncode(temp.refreshOnChanged.checked+""),
									isDefaultValue:HTMLEncode(temp.isDefaultValue.checked+""),
									calculateOnRefresh:HTMLEncode(temp.calculateOnRefresh.checked+""),
									mobile: HTMLEncode(temp.mobile.checked+""),
									valueScript:HTMLEncode(temp.valueScript.value),
									validateRule:HTMLEncode(temp.validateRule.value),
									hiddenScript:HTMLEncode(temp.hiddenScript.value),
									hiddenValue:HTMLEncode(temp.hiddenValue.value),//////
									hiddenPrintScript:HTMLEncode(temp.hiddenPrintScript.value),
									printHiddenValue:HTMLEncode(temp.printHiddenValue.value),
									readonlyScript:HTMLEncode(temp.readonlyScript.value),
									discript:HTMLEncode(temp.discript.value),
									
									limitSum:HTMLEncode(temp.limitSum.value),
									
									fieldtype:"VALUE_TYPE_TEXT",
									textType:HTMLEncode(temp.textType.value),
									limitByUser:HTMLEncode(temp.limitByUser.checked+""),
									isUserField:true,
									filterField:HTMLEncode(temp.filterField.value),
									filtercondition:HTMLEncode(temp.filtercondition.value),
									processDescription :HTMLEncode(createRelStr()),
								   	validateLibs:HTMLEncode(libs),
									editMode:HTMLEncode(ed),
									selectMode:HTMLEncode(temp.selectMode.value)
									} 
								   ) ;	
	return true ;
	}
	jQuery.Placeholder.init(); //表单控件显示的Placeholder提示
	return false;
}


//检查内容是否完成正确
function ev_check(){
   if(temp.name.value==''){
 	 alert("{*[page.name.notexist]*}");
	  return false;
   }
   return !checkStartChar(temp.name.value);
}

function changeLimit(){
	var s = document.getElementById("limitSum").value;
	var reg = /^[0-9]*[1-9][0-9]*$/;
	if(reg.test(s)){
		return true;
	}
	alert("{*[Select]*}{*[UserCount]*}{*[MAX]*}不是正整数");
	return false;
}

function checkStartChar(value){
	return IsDigit(value,"{*[page.name.check]*}");
}

function IsDigit(s,msg){
    var patrn = /^(?![0-9])[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
                 
	if(s=="action"){
		if (!patrn.exec(s)) alert(msg);
	   	alert("action为关键字，不能作为字段名！");
	   	return true;
   	}else if (!patrn.exec(s)){
		alert(msg);
		return true;
	}
	return false;
} 

function isShowSelect(){
	if(temp.limitByUser.checked){
		temp.filterField.value=tempfilterField;
		document.all("filterField").style.display="";
	}else{
		document.all("filterField").style.display="none";
		temp.filterField.value="";
	}
}

</script>

</HEAD>

<BODY bgcolor=menu onload="InitDocument()"">

<form name="temp">
<s:hidden name="formid" id="formid" value="%{#parameters.formid}" ></s:hidden>
		<table border="0" cellpadding="3" cellspacing="0" width="520px" >
			<tr>
				<td class="card" onclick="cardClick(1)" id="card1">{*[Basic]*}</td>
				<td width=2></td>
				<td class="card" onclick="cardClick(2)" id="card2">{*[Value_Script]*}</td>
				<td width=2></td>
				<td class="card" onclick="cardClick(3)" id="card3">{*[Validate_Script]*}</td>
				<td width=2></td>
				<td class="card" onclick="cardClick(4)" id="card4">{*[Hidden_Script]*}</td>
				<td width=2></td>
				<td class="card" onclick="cardClick(5)" id="card5">{*[Hidden_Print_Script]*}</td>
				<td width=2></td>
				<td class="card" onclick="cardClick(6)" id="card6">{*[ReadOnly_Script]*}</td>
				<td width=2></td>
			</tr>
			<tr>
				<td valign="middle" colspan="13" bgcolor="#ffffff" align="center"
					width="100%">

				<table border=1 cellpadding=3 cellspacing=0 class="content"
					width="100%" id="content1" width="520px">
					<tr>
						<td class="commFont commLabel">{*[Name]*}:</td>
						<td><input type=text name="name" onchange="checkStartChar(this.value);"/></td>
						<td class="commFont commLabel">{*[TextType]*}:</td>
						<td><select name="textType">
							<option value="text" selected>{*[Common]*}</option>
							<option value="readonly">{*[Readonly]*}</option>
							<option value="hidden">{*[Hidden]*}</option>
						</select></td>
					</tr>
					<tr>
						<td class="commFont commLabel">{*[cn.myapps.core.dynaform.form.select_mode]*}:</td>
						<td>
						<select name="selectMode">
							<option value="multiSelect" selected>{*[cn.myapps.core.dynaform.form.multiple_choice]*}</option>
							<option value="selectOne">{*[cn.myapps.core.dynaform.form.single_choice]*}</option>
						</select>
						</td>
					</tr>
					<tr>
						<!-- <td class="commFont commLabel">{*[Select]*}{*[UserCount]*}<br>{*[MAX]*}:</td> -->
						<input type="hidden" id="limitSum" name="limitSum" value="10"  onchange="changeLimit()" width="4"/>
						<td align="left" colspan="4" style="padding-left: 140px;">
							<input type=checkbox name="refreshOnChanged" value="true">{*[cn.myapps.core.dynaform.activity.reflash]*}<br/>
							<input type=checkbox name="calculateOnRefresh" value="true">{*[cn.myapps.core.dynaform.activity.recalculate]*}<br/>
							<input type="checkbox" name="mobile" value="true" checked />{*[cn.myapps.core.dynaform.activity.phone]*}<br/>
						</td>
					</tr>
					
					<tr style="display:none">
						<td class="commFont commLabel">{*[Filter]*}:</td>
						<td><input type="checkbox" name="limitByUser" id="limitByUser" value="true" onclick="isShowSelect()"/>&nbsp;{*[Role]*}</td>
						<td colspan="2"><s:select cssStyle="width: 240px;" theme="simple" name="filterField" id="filterField" list="#rh.getRoleList(#parameters.application)" listValue="name" listKey="id"  label="{*[Related Field]*}" /></td>

					</tr>
					<tr>
						<td class="commLabel commFont">{*[Description]*}:</td>
						<td colspan="4"><textarea name="discript" style="width:100%;" rows="4"></textarea></td>
					</tr>
				</table>

				<table border=1 cellpadding=3 cellspacing=0 class="content"
					id="content2" width="520px">
					<tr>
						<%@include file="../../dialog/commondialog.jsp"%>
					</tr>
					<table border=1 cellpadding=3 cellspacing=1 class="content"
						id="content3">
						<tr>
							<td width="25%">
								<%@include file="../../dialog/common_validateScript.jsp"%>
							</td>
							<td width="75%">
								<textarea id="validateRule" name="validateRule" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.button_validate_rule]*}*/" style="width:93%" rows="10"></textarea>
								<button type="button" id="validateRuleButton" style="border:0px;cursor: pointer;width:16px;padding:0px;" >
									<img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/>
								</button>
							</td>
						</tr>
					</table>

					<table border=1 cellpadding=3 cellspacing=0 class="content"
						id="content4" width="520px">
						<tr>
							<td>
								<textarea id="hiddenScript" name="hiddenScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.return_boolean_value]*}（true/false）*/" style="width:96%" rows="10"></textarea>
								<button type="button" id="hiddenScriptButton" style="border:0px;cursor: pointer;width:16px;padding:0px;" >
									<img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/>
								</button>
							</td>
						</tr>
						<tr>
							<td>{*[cn.myapps.core.dynaform.form.webeditor.label.hiddenValue]*}：<input id="hiddenValue" type="text" name="hiddenValue" /></td>
						</tr>
					</table>

					<table border=1 cellpadding=3 cellspacing=0 class="content"
						id="content5" width="520px">
						<tr>
							<td>
								<textarea id="hiddenPrintScript" name="hiddenPrintScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.return_boolean_value]*}（true/false）*/" style="width:96%" rows="10"></textarea>
								<button type="button" id="hiddenPrintScriptButton" style="border:0px;cursor: pointer;width:16px;padding:0px;" >
									<img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/>
								</button>
							</td>
						</tr>
						<tr>
							<td>{*[cn.myapps.core.dynaform.form.webeditor.label.printHiddenValue]*}：<input  type="text" name="printHiddenValue" /></td>
						</tr>
					</table>

					<table border=1 cellpadding=3 cellspacing=0 class="content"
						id="content6" width="520px">
						<tr>
							<td>
								<textarea id="readonlyScript" name="readonlyScript" title="/*{*[cn.myapps.core.dynaform.activity.content.tips.return_boolean_value]*}（true/false）*/" style="width:96%" rows="10"></textarea>
								<button type="button" id="readonlyScriptButton" style="border:0px;cursor: pointer;width:16px;padding:0px;" >
									<img alt="{*[page.Open_with_IscriptEditor]*}" src="<s:url value='/resource/image/editor.png' />"/>
								</button>
							</td>
						</tr>
					</table>

					</td>
					</tr>
				</table>
		</form>

		<script language=javascript>
	cardClick(1);
</script>
</BODY>
</o:MultiLanguage>
</HTML>
