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
<link rel="stylesheet" href="<s:url value='/resource/css/main.css' />" type="text/css">
<script src="<s:url value="/script/list.js"/>"></script>
<!-- tree pugin -->
<script type="text/javascript" src="<s:url value='/portal/share/script/jquery-ui/plugins/tree/_lib/jquery.cookie.js'/>"></script>
<script type="text/javascript" src="<s:url value='/portal/share/script/jquery-ui/plugins/tree/_lib/jquery.hotkeys.js'/>"></script>
<script type="text/javascript" src="<s:url value='/portal/share/script/jquery-ui/plugins/tree/jquery.jstree.js'/>"></script>
<script type="text/javascript" src="<s:url value='/portal/share/script/jquery-ui/plugins/tree/plugins/jquery.jstree.checkbox.js'/>"></script>
<script type="text/javascript" src="<s:url value='/portal/share/script/json/json2.js'/>"></script>
<script type="text/javascript">
	var contextPath='<%=path%>';
	var domainid='<%=domainid%>'; 
	var applicationid='<%=applicationid%>';
	var depttree;
	var args = OBPM.dialog.getArgs();
	var parentObj = args['parentObj'];
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
	
	jQuery(document).ready(function(){
		getRoleslist();
		initDoReturnFunction();
		initDefValue(defValue);
		//OBPM.dialog.resize(jQuery(window).width()+20, jQuery(window).height()+75);
	});
	
	jQuery(function(){
		jQuery("#addAll").click(function(){
			jQuery("#selectedUserDiv").html("");
			jQuery(".list_div_click").each(function(){
				jQuery(this).attr("checked",true);
			});
			jQuery(".list_div_click").each(function(){
				selectUser(jQuery(this),false);
			});
			resetTargetValue();
		});
		
		jQuery("#deleteAll").click(function(){
			jQuery(".list_div_click").each(function(){
				jQuery(this).attr("checked",false);
			});
			jQuery("#selectedUserDiv").html("");
			resetTargetValue();
		});
	});
	
	/*注册页签切换事件*/
	jQuery(function(){
		jQuery(".crossUL > li").click(function(){
			jQuery(".crossUL li.on").removeClass("on");
			jQuery(this).addClass("on");
			jQuery("#leftcontent").css("overflow","auto");
			if(jQuery(this).attr("id")=="bydept"){
				getDeptTree();
			}
			if(jQuery(this).attr("id")=="byroles"){
				getRoleslist();
			}
			if(jQuery(this).attr("id")=="byonline"){
				getOnLineUserList();
			}
			if(jQuery(this).attr("id")=="bysearch"){
				getAllUser("");
			}
		});
	});
	
	function getOnLineUserList(){
		jQuery("#lefthead").html("{*[cn.myapps.core.email.write.choose.attr.current_online_user]*}:");
		jQuery("#rightcontent").html("");
		jQuery.ajax({
					url:contextPath+"/portal/user/getOnLineUserList.action",
					type:"post",
					datatype:"jason",
					data:{"domain":domainid},
					success:function(data){
							if(data){
								jQuery("#leftcontent").css("overflow","auto");
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
	
	/*查找所有的用户*/
	function getAllUser(param){
		jQuery("#lefthead").html("{*[Search]*}:"+"<input type='text' style='height: 18px;width:100px;' id='SHvalue' name='SHvalue' value=''><input onclick='getOnChange2Search(jQuery(\"#SHvalue\").attr(\"value\"))' style='padding-left:2px;' type='button' value='{*[Search]*}'>");
		if(domainid!=""){
			jQuery.ajax({
					url:contextPath+"/portal/user/getAllUser.action",
					type:"post",
					datatype:"jason",
					data:{"domain":domainid,"sm_name":param},
					success:function(data){
							if(data){
								jQuery("#SHvalue").attr("value",param);
								jQuery("#leftcontent").html("");
								jQuery("#rightcontent").css("overflow","auto");
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
					url:contextPath+"/portal/user/getUserListByRole.action",
					type:"post",
					datatype:"jason",
					data:{"applicationid":applicationid,"rolesid":rolesid},
					success:function(data){
							if(data){
								jQuery("#rightcontent").css("overflow","auto");
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
						url:contextPath+"/portal/role/getRolesList.action",
						type:"post",
						datatype:"jason",
						data:{"application":applicationid},
						success:function(data){
								if(data){
									rolelist=data;
									jQuery("#leftcontent").css("overflow","auto").html(rolelist);
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
								jQuery("#leftcontent").css("overflow","auto").html(data);
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
								jQuery("#rightcontent").css("overflow","auto");
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
					url:contextPath+"/portal/user/treelist.action",
					type:"post",
					datatype:"jason",
					data:{"domain":domainid,"departid":deptid},
					success:function(data){
							if(data){
								jQuery("#rightcontent").css("overflow","auto");
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
			OBPM.dialog.doReturn(rtn);
		});
	}
	
	/*用户点击选择用户事件*/
	function selectUser(obj,isclickone){
		if(isclickone){
			if(obj.attr("checked")=="checked"){
				var flag=false;
				//jQuery(this).attr("checked",false);
				jQuery(".onSelect").each(function(){
					if(jQuery(this).attr("id")==obj.attr("id")){
						flag=true;
					}
				});
				addToUserSelect(obj);
			}else{
				//jQuery(this).attr("checked",false);
				jQuery(".onSelect").each(function(){
					if(jQuery(this).attr("id")==obj.attr("id")){
						jQuery("span").remove("#"+obj.attr("id"));
					}
				});	
			}
		}else{
			addToUserSelect(obj);
		}
		
		resetTargetValue();
	}
	
	function addToUserSelect(obj){
		var map = {id: obj.attr("id"), name: obj.attr("name")};
		addToUserSelectByMap(map);
	}
	
	function addToUserSelectByMap(map){
		var id = map["id"];
		var name = map["name"];
		var ids = document.getElementsByName(id).length;
		if (!!window.ActiveXObject){
		    if(ids>1){
			  alert("{*[userAlreadyBeAdd]*}");
		    }else{
			  jQuery("#selectedUserDiv").append("<span style='display:block;width:95%;' onclick='clickRemoveSelect(\""+id+"\",\""+name+"\")' title='{*[Click]*}{*[Delete]*}' class='onSelect' id="+id+" name="+id+">"+name+"</span>");
			}
		}else{
		    if(ids>0){
			  alert("{*[userAlreadyBeAdd]*}");
			}else{
			  jQuery("#selectedUserDiv").append("<span style='display:block;width:95%;' onclick='clickRemoveSelect(\""+id+"\",\""+name+"\")' title='{*[Click]*}{*[Delete]*}' class='onSelect' id="+id+" name="+id+">"+name+"</span>");
			}
		}
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
	
	/* 设置值到目标文本框中 */
	function resetTargetValue(){
			var targetValue="";
			var names="";
			jQuery(".onSelect").each(function(){
				targetValue+=jQuery(this).attr("id")+";";//多个用户用“;”分隔
				names+=jQuery(this).html()+";";
			});
			targetValue=targetValue.substring(0,targetValue.length-1);
			names=names.substring(0,names.length-1);
			//parentObj.document.getElementsByName(targetid)[0].value=targetValue;
			if (parentObj.document.getElementById(targetid)) {
				parentObj.document.getElementById(targetid).value=targetValue;
			}
			if (parentObj.document.getElementById(viewName)) {
				parentObj.document.getElementById(viewName).value=names;
			}
			//parentObj.findUserName(targetValue);
	}
</script>
</head>
<body>
	<div style="width: 100%;height:45px;">
		<div style="float: left;">
			<ul class="crossUL">
				<li id="byroles" class="on">{*[cn.myapps.core.user.by_role]*}</li>
				<li id="bydept">{*[cn.myapps.core.user.by_department]*}</li>
				<li id="byonline">{*[Online]*}{*[User]*}</li>
				<li id="bysearch">{*[Search]*}</li>
			</ul>
		</div>
		<div style="float: right;width: 100px;">
			<input id="doReturn" type="button" value="{*[OK]*}">
		</div>
	</div>
	<div id="left" style="height: 100%;width:48%;border:1px solid #888;float:left;">
		<div id="lefthead" class="list_div" style="height: 25px;padding-top: 2px;padding-left: 2px;background-color: #F6F7F9;"></div>
		<div id="leftcontent" class="leftContent"></div>
	</div>
	<div id="right" class="crossULdivright" style="width: 50%;float:right;">
		<div class="crossULdivright_lef">
			<div id="right_btn" class="list_div">
				<input id="addAll" type="button" value="{*[core.user.addPageAll]*}">
			</div>
			<div id="righttitle" style="background-color: #F6F7F9;padding-top: 5px;padding-left: 2px;text-align: left;vertical-align: middle;height: 22px;"></div>
			<div id="rightcontent" style="height: 100%;padding-left: 25px;padding-bottom: 5px;"></div>
		</div>
		<div class="crossULdivright_rig">
			<div class="list_div">
				<input id="deleteAll" type="button" value="{*[Remove]*}{*[All]*}">
			</div>
			<div style="background-color: #F6F7F9;padding-top: 5px;padding-left: 2px;text-align: left;vertical-align: middle;height: 22px;">{*[cn.myapps.core.user.already_choose_user]*}：</div>
			<div id="selectedUserDiv" class="selectedUserDiv"></div>
		</div>
	</div>
</body>
</o:MultiLanguage>
</html>
