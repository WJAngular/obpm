<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<o:MultiLanguage>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<s:url value='/resource/css/main.css' />" type="text/css">
<script src="<s:url value="/script/list.js"/>"></script>
<title>{*[Widget列表]*}</title>
</head>
<script type="text/javascript">
	var applicationId = '<s:property value="%{#parameters.application}" />';

	//删除Widget
	function doDelete(){
		var listform = document.forms["widgetList"];
		if(isSelectedOne("_selects","{*[please.choose.one]*}")){
			listform.action='<s:url action="delete"/>';
		   	listform.submit();
		}
	}

	//新建Widget
	function doNew(){
		var url = contextPath+ "/core/widget/new.action?application=" + applicationId;
		
		OBPM.dialog.show({
			opener:window.parent.parent,
			width: 500,
			height: 510,
			url: url,
			args: {},
			title: '{*[cn.myapps.core.widget.title.add]*}',
			close: function(rtn) {
				if(rtn=='success'){
					document.forms["widgetList"].submit();
				}
			}
		});
	}
	//编辑Widget
	function doEdit(id){
		var url = contextPath+ "/core/widget/edit.action?id=" + id+"&application=" + applicationId;;
		
		OBPM.dialog.show({
			opener:window.parent.parent,
			width: 500,
			height: 550,
			url: url,
			args: {},
			title: '{*[cn.myapps.core.widget.title.edit]*}',
			close: function(rtn) {
				if(rtn=='success'){
					document.forms["widgetList"].submit();
				}
			}
		});
	}
	
	function adjustDataIteratorSize() {
		var containerHeight = document.body.clientHeight-80;
		var container = document.getElementById("main");
		container.style.height = containerHeight + 'px';
	}
	jQuery(document).ready(function(){
		inittab();
		cssListTable();
		adjustDataIteratorSize();
		//window.top.toThisHelpPage("application_info_generalTools_widget_list");
	});
</script>
<body id="application_info_generalTools_widget_list" class="body-back" onload="">
<s:form name="widgetList" action="list" method="post" theme="simple">
	<s:hidden name="tab" value="1" />
	<s:hidden name="selected" value="%{'btnWidget'}" />
	<%@include file="/common/list.jsp"%>
	<table cellpadding="0" cellspacing="0" width="100%">
		<tr class="nav-td" style="height: 27px;">
			<td rowspan="2"><div class="appsUsualIncludeTab"><%@include file="/common/commontab.jsp"%></div></td>
			<td class="nav-td" width="100%">&nbsp;</td>
		</tr>
		<tr class="nav-s-td">
			<td class="nav-s-td" align="right">
			<table width="100%" border=0 cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top" align="right">
						<img align="middle" style="height:23px;" src="<s:url value='/resource/imgv2/back/main/nav_sep.gif' />" />
						<button type="button" onclick="doNew()" class="button-image"><img
							src="<s:url value="/resource/imgnew/act/act_2.gif"></s:url>">{*[New]*}</button>
						<button type="button" class="button-image" onClick="doDelete()"><img
							src="<s:url value="/resource/imgnew/act/act_3.gif"/>">{*[Delete]*}</button>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<%@include file="/common/msg.jsp"%>
	<s:if test="runtimeException.nativeMessage !=null && runtimeException.nativeMessage !=''">	
			<%@include file="/portal/share/common/msgbox/msg.jsp"%>
		</s:if>
	 <div class="navigation_title">{*[Widget]*}</div>
	 <div id="main" style="overflow-y:auto;overflow-x:hidden;"> 
	<div id="searchFormTable">
			<table class="table_noborder">
				<tr>
					<td class="head-text">
					{*[Name]*}:<input class="input-cmd" type="text" name="sm_name" value='<s:property value="#parameters['sm_name']"/>' size="30" />
					<input class="button-cmd" type="submit" value="{*[Query]*}" />
					<input class="button-cmd" type="button" value="{*[Reset]*}"	onclick="resetAll();" />
					</td>
				</tr>
			</table>
	</div>
	<div id="contentTable">
		<table class="table_noborder">
			<tr class="column-head">
				<td class="column-head2" width="30"><input type="checkbox"
					onclick="selectAll(this.checked)"></td>
				<td class="column-head">
					<o:OrderTag field="name" css="ordertag">{*[Name]*}</o:OrderTag>
				</td>
				<td class="column-head">
					<o:OrderTag field="name" css="ordertag">{*[Type]*}</o:OrderTag>
				</td>
				<td class="column-head">
					<o:OrderTag field="name" css="ordertag">{*[cn.myapps.core.user.label.publish_or_not]*}</o:OrderTag>
				</td>
				<td class="column-head">
					<o:OrderTag field="name" css="ordertag">{*[cn.myapps.core.widget.list.order_number]*}</o:OrderTag>
				</td>
			</tr>
			<s:iterator value="datas.datas" status="index">
				<tr>
				<td class="table-td"><input type="checkbox" name="_selects"	value="<s:property value="id" />"></td>
				<td>
					<a href="javascript:doEdit('<s:property value="id" />')">
					<s:property value="name" />
					</a>
				</td>
				<td>
				<a href="javascript:doEdit('<s:property value="id" />')">
					<s:if test="type=='summary'">{*[Summary]*}</s:if>
					<s:elseif test="type=='view'">{*[View]*}</s:elseif>
					<s:elseif test="type=='link'">{*[cn.myapps.core.widget.type.link]*}</s:elseif>
					<s:elseif test="type=='page'">{*[cn.myapps.core.widget.type.page]*}</s:elseif>
					<s:elseif test="type=='report'">{*[cn.myapps.core.dynaform.links.report]*}</s:elseif>
					<s:elseif test="type=='runquanReport'">{*[cn.myapps.core.dynaform.links.raq_report]*}</s:elseif>
					<s:elseif test="type=='customizeReport'">{*[cn.myapps.core.resource.customize_report]*}</s:elseif>
					<s:elseif test="type=='workflow_analyzer'">{*[cn.myapps.core.widget.type.workflow_analyzer]*}</s:elseif>
					<s:elseif test="type=='iscript'">{*[cn.myapps.core.widget.type.iscript]*}</s:elseif>
				</a>
				</td>
				<td>
				<a href="javascript:doEdit('<s:property value="id" />')">
					<s:if test="published">{*[Yes]*}</s:if>
					<s:else>{*[cn.myapps.core.user.no]*}</s:else>
				</a>
				</td>
				<td>
				<a href="javascript:doEdit('<s:property value="id" />')">
					<s:property value="orderno"/>
				</a>
				</td>
			</s:iterator>
		</table>
		<table class="table_noborder">
			<tr>
				<td align="right" class="pagenav"><o:PageNavigation
					dpName="datas" css="linktag" /></td>
			</tr>
		</table>
	</div>
	</div>
</s:form>
</body>
</o:MultiLanguage>
</html>
