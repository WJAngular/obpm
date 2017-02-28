<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/portal/share/common/head.jsp"%>
<%@page import="cn.myapps.core.user.action.WebUser"%>
<%@page import="cn.myapps.constans.Web"%>
<%
	WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	String domainid=webUser.getDomainid();
	String applicationid=request.getParameter("application");
	String path=request.getContextPath();
%>
<html><o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<head>
<title>{*[Select]*}{*[User]*}</title>
<link rel="stylesheet" href="<s:url value='/resource/css/main.css' />" type="text/css"/>
<script src="<s:url value="/script/list.js"/>"></script>
<!-- tree pugin -->
<script src='<s:url value="/pm/js/plugin/artDialog/jquery.artDialog.js?skin=default"/>'></script>
<script src='<s:url value="/pm/js/plugin/artDialog/jquery.artDialog.js"/>'></script>
<script src='<s:url value="/pm/js/plugin/artDialog/artDialog.js"/>'></script>
<script src='<s:url value="/pm/js/plugin/artDialog/plugins/iframeTools.source.js"/>'></script>
<script type="text/javascript" src="<s:url value='/script/jquery-ui/plugins/tree/_lib/jquery.cookie.js'/>"></script>
<script type="text/javascript" src="<s:url value='/script/jquery-ui/plugins/tree/_lib/jquery.hotkeys.js'/>"></script>
<script type="text/javascript" src="<s:url value='/script/jquery-ui/plugins/tree/jquery.jstree.js'/>"></script>
<script type="text/javascript" src="<s:url value='/script/jquery-ui/plugins/tree/plugins/jquery.jstree.checkbox.js'/>"></script>
<script type="text/javascript" src="<s:url value='/script/json/json2.js'/>"></script>
<script type="text/javascript" src="<s:url value='/portal/share/script/jquery.slimscroll.min.js' />"></script>
<script type="text/javascript">
	var domainid='<%=domainid%>'; 
	var applicationid='<%=applicationid%>';
	var depttree;
	var args = art.dialog.data("args");
	var parentObj = args['parentObj'];
	var _container = args['container'];
	var _callback = args['callback'];
	var _memberType = args['memberType'];
	/*暂时不需要
	用户设定最大选着数
	var maxUser=args['limitSum'];
	if(maxUser==null || maxUser=="" || maxUser=="null"){
		maxUser=10;
	}*/
	/*存放userid*/
	var targetid=args['targetid'];
	/*存放username*/
	var viewName=args['receivername'];
	// 默认值
	var defValue=args['defValue'];
	var rolelist;
	/*选择模式*/
	var selectMode = args['selectMode'];
	/*存放userEmail*/
	var viewEmail=args['receiverEmail'];

	function doReturn(rtn){
		//parentObj.PM.project.addMembers();
		if(_callback && typeof _callback == "function"){
			_callback(buildSelectUserJSON());
		}
        var Origins = artDialog.open.origin;//这里表示父窗体
	    art.dialog.close();//关闭弹出框
	}
	
	function getOnLineUserList(){
		jQuery("#lefthead").html("{*[cn.myapps.core.email.write.choose.attr.current_online_user]*}:");
		jQuery.ajax({
					url:contextPath+"/portal/user/getOnLineUserList.action"+"?selectMode="+selectMode,
					type:"post",
					datatype:"jason",
					data:{"domain":domainid},
					success:function(data){
							if(data){
								jQuery("#leftcontent").html(data);
								if(jQuery("#onLineUsersCount")){
									jQuery("#lefthead").html(jQuery("#lefthead").html()+"(<span style='color:red'>"+jQuery("#onLineUsersCount").attr("value")+"</span>)");
								}
							}else{
								jQuery("#leftcontent").html("<div class='list_div'>{*[core.dynaform.form.userfield.front.page.tip.can_not_found_user]*}</div>");
							}
						},
					error:function(data,status){
							alert("failling to visited...");
						}
		});	
	}

	/*根据分组id去那下面的users*/
	function getUserListByContancts(obj){
		jQuery(".selectImg_right").each(function(){
			jQuery(this).css("visibility","hidden");
		});
		jQuery("#img_"+obj.attr("id")).css("visibility","visible");
		getUserListByContanctsid(obj);
	}
	
	function getUserListByContanctsid(contanctsObj){
		var contanctsid=contanctsObj.attr("id");
		var contanctsname=contanctsObj.attr("title");
		if(contanctsid!=""){
			jQuery("#righttitle").html("分组："+contanctsname);
			jQuery.ajax({
					url:contextPath+"/portal/usergroup/getUserListBycontancts.action"+"?selectMode="+selectMode,
					type:"post",
					datatype:"jason",
					data:{"domainid":domainid,"contanctsid":contanctsid},
					success:function(data){
							if(data){
								//jQuery("#rightcontent").css("overflow","auto");
								jQuery("#rightcontent").html(data);
							}else{
								jQuery("#rightcontent").html("<div class='list_div'>{*[core.dynaform.form.userfield.front.page.tip.can_not_found_user]*}</div>");
							}
						},
					error:function(data,status){
							alert("failling to visited...");
						}
			});	
		}
	}

	//获取左侧通讯录分组列表
	function getContancts(){
		jQuery("#lefthead").html("{*[cn.myapps.core.usergroup.submit.contacts]*}:");
		jQuery.ajax({
			url:contextPath+"/portal/usergroup/getContancts.action"+"?selectMode="+selectMode,
			type:"post",
			datatype:"jason",
			data:{"application":applicationid},
			success:function(data){
					if(data){
						rolelist=data;
						jQuery("#leftcontent").html(rolelist);
					}else{
						jQuery("#leftcontent").html("<div>{*[CanNotFind]*}{*[Role]*}</div>");
					}
				},
			error:function(data,status){
					alert("failling to visited...");
				}
		});
	}
	
	/*查找所有的用户*/
	function getAllUser(param){
		jQuery("#righttitle").html("<span style='float:left;'>{*[cn.myapps.core.email.write.choose.attr.by_username]*}:</span><input type='text' placeholder='{*[cn.myapps.core.email.write.choose.attr.by_username]*}' style='float:left;width:280px;height: 36px;outline:medium;border:none;background-color: #fafafa;' id='SHvalue' name='SHvalue' value=''><input onclick='getOnChange2Search(jQuery(\"#SHvalue\").attr(\"value\"))' type='button'  class='searchPerson'>");
		if(domainid!=""){
			jQuery.ajax({
					url:contextPath+"/portal/user/getAllUser.action"+"?selectMode="+selectMode,
					type:"post",
					datatype:"jason",
					data:{"domain":domainid,"sm_name":param},
					success:function(data){
							if(data){
								jQuery("#SHvalue").attr("value",param);
								jQuery("#leftcontent").html("");
								jQuery("#rightcontent").html(data);
							}else{
								jQuery("#SHvalue").attr("value",param);
								jQuery("#rightcontent").html("<div class='list_div'>{*[core.dynaform.form.userfield.front.page.tip.can_not_found_user]*}</div>");
							}
						},
					error:function(data,status){
							alert("failling to visited...");
						}
			});	
		}
	}
	
	function getOnChange2Search(value){
		jQuery("#righttitle").html("{*[Search]*}{*[Result]*}:");
		//if(value!=""){
		getAllUser(value);
		//}
	}
	
	/*根据角色id去那下面的users*/
	function getUserListByRole(obj){
		jQuery(".selectImg_right").each(function(){
			jQuery(this).css("visibility","hidden");
		});
		jQuery("#img_"+obj.attr("id")).css("visibility","visible");
		getUserListByRoleid(obj);
	}
	
	function getUserListByRoleid(roleObj){
		var rolesid=roleObj.attr("id");
		var rolesname=roleObj.attr("title");
		if(rolesid!=""){
			jQuery("#righttitle").html("{*[Role]*}："+rolesname);
			jQuery.ajax({
					url:contextPath+"/portal/user/getUserListByRole.action"+"?selectMode="+selectMode,
					type:"post",
					datatype:"jason",
					data:{"applicationid":applicationid,"rolesid":rolesid},
					success:function(data){
							if(data){
								jQuery("#rightcontent").html(data);
							}else{
								jQuery("#rightcontent").html("<div class='list_div'>{*[core.dynaform.form.userfield.front.page.tip.can_not_found_user]*}</div>");
							}
						},
					error:function(data,status){
							alert("failling to visited...");
						}
			});	
		}
	}
	
	/*获取左侧的角色列表*/
	function getRoleslist(){
		jQuery("#lefthead").html("{*[Roles]*}{*[List]*}:");
		if(rolelist!=""){
			if(applicationid!=""){
				jQuery.ajax({
						url:contextPath+"/portal/role/getRolesList.action"+"?selectMode="+selectMode,
						type:"post",
						datatype:"jason",
						data:{"application":applicationid},
						success:function(data){
								if(data){
									rolelist=data;
									jQuery("#leftcontent").html(rolelist);
								}else{
									jQuery("#leftcontent").html("<div>{*[CanNotFind]*}{*[Role]*}</div>");
								}
							},
						error:function(data,status){
								alert("failling to visited...");
							}
				});	
			}
		}else{
			jQuery("#leftcontent").html(rolelist);
		}
	}
	
	function doLeftPageNav(url){
		jQuery.ajax({
					url:contextPath+url,
					type:"post",
					datatype:"jason",
					success:function(data){
							if(data){
								jQuery("#leftcontent").html(data);
							}else{
								jQuery("#leftcontent").html("<div class='list_div'>{*[core.dynaform.form.userfield.front.page.tip.can_not_found_user]*}</div>");
							}
						},
					error:function(data,status){
							alert("failling to visited...");
						}
		});	
	}
	
	function doPageNav(url){
		jQuery.ajax({
					url:contextPath+url,
					type:"post",
					datatype:"jason",
					success:function(data){
							if(data){
								jQuery("#rightcontent").html(data);
							}else{
								jQuery("#rightcontent").html("<div class='list_div'>{*[core.dynaform.form.userfield.front.page.tip.can_not_found_user]*}</div>");
							}
						},
					error:function(data,status){
							alert("failling to visited...");
						}
		});	
	}
	
	/*初始化部门树*/
	function getDeptTree() {  
		jQuery("#lefthead").html("{*[Department]*}{*[List]*}:");
		depttree = jQuery("#leftcontent").jstree({ 
			core:{
				initially_open: ['root']
			}, 
			"json_data" : {
				"ajax" : { 
					"url" : function (){
						return contextPath + "/portal/department/departTree.action?domain="+domainid+"&datetime=" + new Date().getTime();
					},
					"data" : function (node) { 
						// buildParams
						var params = {};
						if (node.attr) {
							params['parentid'] = node.attr("id"); // 上级部门ID
							params['data'] = node.attr("data");
						}
						return params;
					}
				}
			},
			"plugins" : [ "themes", "json_data","lang", "ui",]
		}).bind("select_node.jstree", function(e, data){
			var node = data.rslt.obj;
			if (node && node.attr) {
				if(node.attr("id")!=""){
					var deptid=node.attr("id");
					var deptname=node.attr("name");
					//alert("deptid----->"+deptid+"    deptname--->"+deptname);
					getUserListByDept(domainid,deptid,deptname);
				}
			}
		});
	}
	
	
	/*用户点击部门节点返回该部门下的用户*/
	function getUserListByDept(domainid,deptid,deptname){
		if(domainid!="" && deptid!=""){
			jQuery("#righttitle").html("{*[Department]*}:"+deptname);
			jQuery.ajax({
					url:contextPath+"/portal/user/treelist.action"+"?selectMode="+selectMode,
					type:"post",
					datatype:"jason",
					data:{"domain":domainid,"departid":deptid},
					success:function(data){
							if(data){
								jQuery("#rightcontent").html(data);
							}else{
								jQuery("#rightcontent").html("<div class='list_div'>{*[core.dynaform.form.userfield.front.page.tip.can_not_found_user]*}</div>");
							}
						},
					error:function(data,status){
							alert("failling to visited...");
						}
			});	
		}
	}
	
	/*初始化默认值*/
	function initDefValue(defValue){
		if (defValue) {
			var array = jQuery.isArray(defValue) ? defValue: defValue.split(",");
			var params = "";
			if (array && array.length > 0) {
				for (var i = 0; i < array.length; i++) {
					params += "&_selects=" + array[i];
				}
			}
			
			jQuery.ajax({
					url:contextPath+"/portal/user/doListBySelectToJSON.action",
					type:"post",
					datatype:"jason",
					data: "domain=" + domainid + params,
					success:function(userlist){
						if (userlist && userlist.length > 0) {
							for (var i = 0; i < userlist.length; i++) {
								addToUserSelectByMap(userlist[i]);
							}
						}
					},
					
					error:function(data,status){
						alert("failling to doListBySelectToJSON...");
					}
			});	
		}
	}
	
	/*初始化返回事件*/
	function initDoReturnFunction(){
		jQuery("#doReturn").click(function(){
			var rtn="";
			jQuery(".onSelect").each(function(){
				rtn+=jQuery(this).attr("id")+",";
			});
			if (rtn && rtn.length > 0) {
				rtn=rtn.substring(0,rtn.length-1);
			}
			doReturn(rtn);
		});
	}

	/*用户点击选择用户事件(返回错误信息)*/
	function selectUserWithReturnMsg(obj,isclickone){
		if(selectMode == 'selectOne'){
			if(obj.attr("checked")=="checked"){
				jQuery(".list_div_click").each(function(){
					if(jQuery(this).attr("id") != obj.attr("id")){
						jQuery(this).removeAttr("checked");
					}
				});
				jQuery("#selectedUserDiv").find("span").remove();
				return addToUserSelect(obj);
			}else{
				//jQuery(this).attr("checked",false);
				jQuery(".onSelect").each(function(){
					if(jQuery(this).attr("id")==obj.attr("id")){		
						jQuery("span").remove("#"+obj.attr("id"));
					}
				});	
			}
			
			resetTargetValue();
		}else{
			if(isclickone){
				if(obj.attr("checked")=="checked"){
					var flag=false;
					//jQuery(this).attr("checked",false);
					jQuery(".onSelect").each(function(){
						if(jQuery(this).attr("id")==obj.attr("id")){
							flag=true;
						}
					});
					return addToUserSelect(obj);
				}else{
					//jQuery(this).attr("checked",false);
					jQuery(".onSelect").each(function(){
						if(jQuery(this).attr("id")==obj.attr("id")){		
							jQuery("span").remove("#"+obj.attr("id"));
						}
					});	
				}
			}else{
				return addToUserSelect(obj);
			}
			
			resetTargetValue();
		}
	}
	
	/*用户点击选择用户事件*/
	function selectUser(obj,isclickone){
		if(selectMode == 'selectOne'){
			if(obj.attr("checked")=="checked"){
				jQuery(".list_div_click").each(function(){
					if(jQuery(this).attr("id") != obj.attr("id")){
						jQuery(this).removeAttr("checked");
					}
				});
				jQuery("#selectedUserDiv").find("span").remove();
				addToUserSelect(obj);
			}else{
				//jQuery(this).attr("checked",false);
				jQuery(".onSelect").each(function(){
					if(jQuery(this).attr("id")==obj.attr("id")){		
						jQuery("span").remove("#"+obj.attr("id"));
					}
				});	
			}
			
			resetTargetValue();
		}else{
			if(isclickone){
				if(obj.attr("checked")=="checked"){
					var flag=false;
					//jQuery(this).attr("checked",false);
					jQuery(".onSelect").each(function(){
						if(jQuery(this).attr("id")==obj.attr("id")){
							flag=true;
						}
					});
					var errorMsg = addToUserSelect(obj);
					if(errorMsg && errorMsg.length > 0){
						errorMsg = errorMsg.substring(0,errorMsg.length - 1);
						alert("[" + errorMsg + "] {*[userAlreadyBeAdd]*}");
					}
				}else{
					//jQuery(this).attr("checked",false);
					jQuery(".onSelect").each(function(){
						if(jQuery(this).attr("id")==obj.attr("id")){		
							jQuery("span").remove("#"+obj.attr("id"));
						}
					});	
				}
			}else{
				var errorMsg = addToUserSelect(obj);
				if(errorMsg && errorMsg.length > 0){
					errorMsg = errorMsg.substring(0,errorMsg.length - 1);
					alert("[" + errorMsg + "] {*[userAlreadyBeAdd]*}");
				}
			}
			
			resetTargetValue();
		}
		
	}
	
	function addToUserSelect(obj){
		var map = {id: obj.attr("id"), name: obj.attr("name"), email: obj.attr("email")};
		return addToUserSelectByMap(map);
	}
	
	function addToUserSelectByMap(map){
		var errorMsg = '';
		var id = map["id"];
		var name = map["name"];
		var email = map["email"];
		//var ids = document.getElementsByName(id).length;
		var ids = 1;
		jQuery("#selectedUserDiv").find("span").each(function(){
			if(jQuery(this).attr("id")==id){
				ids++;
			}
		});
	   if(ids>1){
		   errorMsg += name + ';';
		 //alert("{*[userAlreadyBeAdd]*}");
	   }else if (parentObj.document.getElementById(viewEmail) && email == '') {
			alert("[" + name + "] {*[userAlreadyNoEmail]*}");
	   }else{
		 jQuery("#selectedUserDiv").append("<span style='display:block;width:95%;' onclick='clickRemoveSelect(\""+id+"\",\""+name+"\")' title='{*[Click]*}{*[Delete]*}' class='onSelect' id="+id+" name="+id+" email=>"+name+"</span>");
	   }
		
	   return errorMsg;
   }
	
	function clickRemoveSelect(id,name){
		/*
		if(jQuery("#"+id)){
			jQuery("#"+id).attr("checked",false);
		}
		if(jQuery("#"+id)){
			jQuery("span").remove("#"+id);
		}
		*/
		jQuery(".list_div_click").each(function(){
			if(jQuery(this).attr("id")==id){
				jQuery(this).attr("checked",false);
				resetTargetValue();
			}
		});
		/*
		jQuery(".onSelect").each(function(){
			if(jQuery(this).attr("id")==id){
				jQuery("span").remove("#"+jQuery(this).attr("id"));
			}
		});
		*/
		
		jQuery('span[class=onSelect]').live('click',function(){
			jQuery(this).remove();
			resetTargetValue();
		});
		resetTargetValue();
		jQuery("#selectedUserDiv").focus();
	}


	function buildSelectUserJSON(){
		
		/*var json = "[";
		jQuery(".onSelect").each(function(){
			json+="{'userId':'"+jQuery(this).attr("id")+"',";
			json+="'memberType':'"+_memberType+"',";
			json+="'userName':'"+jQuery(this).html()+"'},";
		});
		json=json.substring(0,json.length-1);
		json+="]";*/
		
		

		var json = [];
		jQuery(".onSelect").each(function(){
			var obj = {};
			obj.userId = jQuery(this).attr("id");
			obj.memberType = _memberType;
			obj.userName = jQuery(this).html();
			json[json.length] = obj;
		});

		return json;
	}
	
	/* 设置值到目标文本框中 */
	function resetTargetValue(){
			var json = buildSelectUserJSON();
			if(_container){
				parentObj.document.getElementById(_container).value=json;
			}
	}
	
	function bulitTitle(names){
		if(names != null && names != ''){
			var array = names.split(";");
			var title = '';
			for(var i = 0; i < array.length; i++){
				if(i != 0 && i%10 == 0){
					title += "\n";
				}
				title += array[i] + ";";
			}
			return title == ''?title:title.substring(0,title.length-1);
		}
		return '';
	}
	
	//设置己选择的用户数据
	function initSetValue(){
		var targetidVal = "";
		var viewNameVal = "";
		var viewEmailVal = "";
		if (parentObj.document.getElementById(targetid)) {
			targetidVal = parentObj.document.getElementById(targetid).value;
		}
		if (parentObj.document.getElementById(viewName)) {
			viewNameVal = parentObj.document.getElementById(viewName).value;
		}
		if (parentObj.document.getElementById(viewEmail)) {
			viewEmailVal = parentObj.document.getElementById(viewEmail).value;
		}
		var userIds = targetidVal.split(";");
		var userNames = viewNameVal.split(";");
		var userEmails = viewEmailVal.split(";");
		for(var i=0; i<userIds.length; i++){
			if(userIds[i] != ""){
				var obj = {id:userIds[i],name:userNames[i],email:userEmails[i]};
				addToUserSelectByMap(obj);
			}
		}
	}
	//滚动条
	function rightContentScroll(){
		var rightscrollHeight = jQuery(".crossULdivright_lef").height()-jQuery(".crossULdivright_lef>#right_btn").height()-jQuery(".crossULdivright_lef>#righttitle").height()-2;
		var leftscrollHeight = jQuery(".crossULdivleft").height()-jQuery(".crossULdivleft>#lefthead").height()-2;
		var selectedscrollHeight = jQuery(".crossULdivright_rig").height()-jQuery(".crossULdivright_rig>.list_div_head").height()-2;
		$("#rightcontent").slimscroll({
			height: rightscrollHeight,
			color:'#333'
		});
		$("#leftcontent").slimscroll({
			height: leftscrollHeight,
			color:'#333'
		});
		$("#selectedUserDiv").slimscroll({
			height: selectedscrollHeight,
			color:'#333'
		});
	}
	jQuery(document).ready(function(){
		//getRoleslist();
		getDeptTree();
		initDoReturnFunction();
		initDefValue(defValue);
		initSetValue();
		//OBPM.dialog.resize(jQuery(window).width()+20, jQuery(window).height()+75);
		
		jQuery("#addAll").click(function(){
			jQuery(".list_div_click").each(function(){
				jQuery(this).attr("checked",true);
			});
			var errorMsg = '';
			jQuery("#rightcontent").find('input[class=list_div_click]').each(function(){
				var msg = selectUserWithReturnMsg(jQuery(this),false);
				if(msg != '')
					errorMsg += msg;
			});
			if(errorMsg && errorMsg.length > 0){
				errorMsg = errorMsg.substring(0,errorMsg.length - 1);
				alert("[" + errorMsg + "] {*[userAlreadyBeAdd]*}");
			}
			resetTargetValue();
		});
		
		jQuery("#deleteAll").click(function(){
			jQuery(".list_div_click").each(function(){
				jQuery(this).attr("checked",false);
			});
			jQuery("#selectedUserDiv").html("");
			resetTargetValue();
		});

		/*注册页签切换事件*/
		jQuery(".crossUL > li").click(function(){
			if(jQuery(this).index() == $(".crossUL li").length - 1){
				jQuery(".guideOne").css("display","none");
				jQuery("#left").css("display","none");
				jQuery("#right_btn").css("display","none");
				jQuery(".crossULdivright_lef").css({"width":"423px"});
				jQuery(".crossULdivright_lef>.slimScrollDiv").css({"height":"330px"});
				jQuery(".crossULdivright_lef>.slimScrollDiv>#rightcontent").css({"height":"330px"});
			}else{
				jQuery(".guideOne").css("display","block");
				jQuery(".crossULdivright_lef").css("display","block");
				jQuery("#righttitle").html('');
				jQuery("#left").css("display","block");
				jQuery("#right_btn").css("display","block");
				jQuery(".crossULdivright_lef").css({"width":"198px"});
				jQuery(".crossULdivright_lef>.slimScrollDiv").css({"height":"292px"});
				jQuery(".crossULdivright_lef>.slimScrollDiv>#rightcontent").css({"height":"292px"});
			}
			jQuery(".crossUL li.on").removeClass("on");
			jQuery(this).addClass("on");
			$("#rightcontent").html("");
			if(jQuery(this).attr("id")=="bydept"){
				getDeptTree();
			}
			if(jQuery(this).attr("id")=="byroles"){
				getRoleslist();
			}
			if(jQuery(this).attr("id")=="byonline"){
				getOnLineUserList();
			}
			if(jQuery(this).attr("id")=="bycontancts"){
				getContancts();
			}
			if(jQuery(this).attr("id")=="bysearch"){
				getAllUser("");
			}
		});

		if(selectMode == 'selectOne'){
			document.getElementById("addAll").style.display = "none";
		}
		//滚动条
		rightContentScroll();
	});
</script>
</head>
<body style="overflow-x:hidden;overflow-y:hidden;height:100%;margin: 0;">
	
	<div class="crossUL-top">
			<ul class="crossUL">
				<!-- <li id="byroles" class="on">{*[cn.myapps.core.email.write.choose.tab.title.by_role]*}</li> -->
				<li id="bydept" class="on">{*[cn.myapps.core.email.write.choose.tab.title.by_department]*}</li>
				<li id="byonline">{*[cn.myapps.core.email.write.choose.tab.title.online_user]*}</li>
				<li id="bycontancts">{*[cn.myapps.core.usergroup.submit.contacts]*}</li>
				<li id="bysearch">{*[Search]*}</li>
			</ul>
	</div>
	<div style="float:none;clear:both;"></div>
	<div class="contentDiv on">
		<div id="left" class="crossULdivleft">
			<div id="lefthead" class="list_div list_div_head"></div>
			<div id="leftcontent" class="leftContent" style="overflow-x:hidden;height:320px"></div>
		</div>
		
		<div id="right" class="crossULdivright">
			<div class="guide guideOne"></div>
			<div class="crossULdivright_lef">
				<div id="right_btn" class="list_div list_div_head" style="text-align:center;">
					<input id="addAll" type="button" class="btn btn-default" value="{*[core.user.addPageAll]*}">
				</div>
				<div id="righttitle" style="height: 38px;line-height:38px;padding-left:10px;color:#676a6c;background-color: #fafafa;border-bottom: 1px solid #d2d2d2;"></div>
				<div id="rightcontent" style="clear: all;"></div>
			</div>
			<div class="guide"></div>
			<div class="crossULdivright_rig">
				<div class="list_div list_div_head">
					{*[cn.myapps.core.email.write.choose.delete_all.allready_choose_user]*}：
				</div>
				
				<div id="selectedUserDiv" class="selectedUserDiv"></div>
				<div style="margin-right:10px;position: absolute;top: 2px;right: 0;"><input id="deleteAll" class="btn btn-danger" type="button" value="{*[Remove]*}{*[All]*}"></div>
			</div>
		</div>
		<div style="float:none;clear:both;"></div>
	</div>
	<div style="float:none;clear:both;"></div>
	<div style="float: right;padding: 0 20px;margin-top:17px;">
			<input id="doReturn" class="btn btn-success" type="button" value="{*[OK]*}">
	</div>
</body>
</o:MultiLanguage>
</html>
