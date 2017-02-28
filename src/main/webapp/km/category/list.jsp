<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/km/disk/head.jsp"%>
<s:bean name="cn.myapps.km.category.ejb.CategoryHelper" id="categoryHelper"></s:bean>
<html><o:MultiLanguage>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href='<s:url value="/km/script/dtree/dtree.css" />' rel="StyleSheet" type="text/css" />
<script type="text/javascript">
function ev_edit(id){
	var url='<s:url action="view"/>?id='+id;
	jQuery("#contentFrame").attr("src",url);
	jQuery("#btn_save").attr("disabled",false);
	jQuery("#btn_delete").attr("disabled",false);
}
function ev_save(){
	var iframe = document.getElementById('contentFrame').contentWindow;
	var  _name= iframe.document.getElementById('_name').value;
	if(_name!=null&&_name!=""){
		if(iframe.document.getElementById("_id")){
			warpAttributes();
			document.forms[0].submit();
		}
		}else{
			alert("{*[cn.myapps.km.category.name_not_null]*}");
			}
	
}

function warpAttributes(){
	var iframe = document.getElementById('contentFrame').contentWindow;
	if(iframe.document.getElementById("_id")){
		jQuery("#_id").attr("value",iframe.document.getElementById("_id").value);
		jQuery("#_name").attr("value",iframe.document.getElementById("_name").value);
		jQuery("#_parentId").attr("value",iframe.document.getElementById("_parentId").value);
		jQuery("#_domainId").attr("value",iframe.document.getElementById("_domainId").value);
		jQuery("#_description").attr("value",iframe.document.getElementById("_description").value);
	}
}

jQuery(document).ready(function(){
	var targetId = '<s:property value="targetId" />';
	if(targetId.length>0){
		ev_edit(targetId);
	}
});

function ev_delete(){
	var url='<s:url action="delete"/>';
	if(confirm("{*[cn.myapps.km.category.delete_tip]*}")){
		warpAttributes();
		document.forms[0].action=url;
		document.forms[0].submit();
	}
}
function ev_new(){
	var url='<s:url action="new"/>';
	jQuery("#contentFrame").attr("src",url);
	jQuery("#btn_save").attr("disabled",false);
	jQuery("#btn_delete").attr("disabled",true);
}
</script>
<style type="text/css">
body,table,tr,td,div,a{
	margin:0;
	padding:0;
}
div.category_content{
	width: 99%;
	height: 100%;
	min-width: 500px;
}
#left{
	margin-right:20px;
	float:left;
	width:200px;
	height: 300px;
	border-right: 2px solid #cccccc;
}
#right{
	width:350px;
	height: 300px;
	float:left;
}

.category_content a {
	color: black;
	text-decoration: none;
}

.category_content ul {
	margin-top: 1px;
	margin-bottom: 3px
}
.category_content ul li{
	margin-left:10px;
	list-style: none;
	margin-bottom: 3px;
}
.category_content ul a{
	font-size:14px;
}
.category_content ul li a{
	font-size:12px;
}
#toolbar {
	height:35px;
	border-bottom: 1px dashed #cccccc;
	margin-top:5px;
	margin-bottom: 6px;
}

</style>
</head>
<body>
<s:form theme="simple" method="post" action="save">
<s:hidden name="content.id" id="_id"/>
<s:hidden name="content.domainId" id="_domainId" />
<s:hidden name="content.sort" id="_sort"/>
<s:hidden name="content.name" id="_name"/>
<s:hidden name="content.parentId" id="_parentId"/>
<s:hidden name="content.description" id="_description"/>
</s:form>

<div class="category_content">
	<%@include file="/common/msg.jsp"%>
	<div id="toolbar">
		<table width="98%" border=0 cellpadding="0" cellspacing="0">
			<tr>
				<td align="left"><span style="font-weight: bold">{*[cn.myapps.km.category.category_list]*}</span></td>
				<td align="right">
					<button type="button" class="button-image" onClick="ev_new()"><img src="<s:url value="/resource/imgnew/act/act_2.gif"></s:url>">{*[cn.myapps.km.category.new]*}</button>
					<button type="button" id="btn_save" disabled="disabled" class="button-image" onClick="ev_save()"><img src="<s:url value="/resource/imgnew/act/act_4.gif"/>">{*[cn.myapps.km.category.save]*}</button>
					<button type="button" id="btn_delete" disabled="disabled" class="button-image" onClick="ev_delete()"><img src="<s:url value="/resource/imgnew/act/act_3.gif"/>">{*[cn.myapps.km.category.delete]*}</button>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- LEFT SITE START -->
	<div id="left">
		<div id="categoryList">
		<s:iterator value="#categoryHelper.getRootCategory(#session.FRONT_USER.domainid)" status="rootList" id="root">
			<ul ><a href="javascript:ev_edit('<s:property value="#root.id"/>')"><s:property value="#root.name"/></a>
			<s:iterator value="#categoryHelper.getSubCategory(#root.id,#session.FRONT_USER.domainid)" status="subList" id="sub">
				<li><a href="javascript:ev_edit('<s:property value="#sub.id"/>')"><s:property value="#sub.name"/></a></li>
			</s:iterator>
		</ul>
		</s:iterator>
		</div>
	</div>
	<!-- LEFT SITE END -->
	
	<!-- RIGHT SITE START -->
	<div id="right">
		<iframe frameborder="0" id="contentFrame" width="100%" height="100%" name="contentFrame" src=""></iframe>
	</div>
	<!-- RIGHT SITE END -->
</div>
</body>
</o:MultiLanguage></html>