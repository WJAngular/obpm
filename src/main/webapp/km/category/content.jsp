<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/km/disk/head.jsp"%>
<s:bean name="cn.myapps.km.category.ejb.CategoryHelper" id="categoryHelper"></s:bean>
<html><o:MultiLanguage>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href='<s:url value="/km/disk/css/km.css" />' rel="stylesheet" type="text/css"/>
<script type="text/javascript" src='<s:url value="/km/disk/script/share.js"/>'></script>
<script src='<s:url value="/portal/share/component/dateField/datePicker/WdatePicker.js"/>'></script>
<script type="text/javascript">
	jQuery(document).ready(function(){
		
	});

	function return_back(){
		var _fileId = '<s:property value="content.fileId" />';
		var _fileType = '<s:property value="content.fileType" />';
		var url = contextPath + "/km/permission/list.action?_fileId="+_fileId+"&_fileType="+_fileType;
		location.href = url;
	}
	
	function ev_save(){
		document.forms[0].submit();
	}
</script>
</head>
<body>
<s:form action="save" method="post" theme="simple">
	<div>
		<s:hidden name="content.id" id="_id"/>
		<s:hidden name="content.domainId" id="_domainId" />
		<s:hidden name="content.sort" id="_sort"/>
		<table align="right" style="display:none;">
		<tr align="right">
			<td align="right">
				<div align="right">
					<button type="button" class="button-image" onclick="ev_save()"><img src="<s:url value="/resource/imgnew/act/act_4.gif"/>">{*[cn.myapps.km.category.save]*}</button>
				</div>
			</td></tr>
		</table>
	</div>
	<div>
		<table width="98%" align="left" height="100%">
			<tr>
				<td class="commFont" align="left">{*[cn.myapps.km.category.name]*}:</td>
				<td class="commFont" align="left">{*[cn.myapps.km.category.super_type]*}:</td>
			</tr>
			<tr>
				<td><s:textfield cssClass="input-cmd" name="content.name" id="_name" /></td>
				<td><s:select cssClass="input-cmd" name="content.parentId" id="_parentId" list="#categoryHelper.getRootCategory(#session.FRONT_USER.domainid)" listKey="id" listValue="name" emptyOption="true"/></td>
			</tr>
			<tr>
				<td class="commFont" align="left">{*[cn.myapps.km.category.description]*}:</td>
				<td class="commFont" align="left"></td>
			</tr>
			<tr>
				<td colspan="2"><s:textarea cssClass="input-cmd" name="content.description" id="_description" /></td>
			</tr>
		</table>
	</div>
</s:form>
</body>
</o:MultiLanguage></html>