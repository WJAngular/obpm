<%@ page pageEncoding="UTF-8"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="java.util.*"%>
<% 
	String skinType = (String)request.getSession().getAttribute("SKINTYPE");
%>
<%@ page import="cn.myapps.core.workflow.FlowState"%>
<%@ page import="cn.myapps.core.workflow.FlowType"%>
<%@ page import="cn.myapps.core.workflow.element.*"%>
<%@ page import="cn.myapps.core.workflow.storage.definition.ejb.*"%>
<%@ page import="cn.myapps.util.ProcessFactory" %>
<%@page import="cn.myapps.util.StringUtil"%>
<!--[if lt IE 9]>
<script type="text/javascript" src='<s:url value="/portal/share/script/jSignature/flashcanvas.js"/>'></script>
<![endif]-->
<script src='<s:url value="/portal/share/script/jSignature/jSignature.min.js"/>'></script>
<script src='<s:url value="/portal/share/script/jSignature/plugins/jSignature.CompressorBase30.js"/>'></script>
<script src='<s:url value="/portal/share/script/jSignature/plugins/jSignature.CompressorSVG.js"/>'></script>
<script src='<s:url value="/portal/share/script/jSignature/plugins/jSignature.UndoButton.js"/>'></script> 
<script src='<s:url value="/portal/share/script/jSignature/plugins/signhere/jSignature.SignHere.js"/>'></script> 
<script src='<s:url value="/portal/share/script/layer/layer.js"/>'></script>
<style>
.layui-layer .row{
	margin:0px;
}
.layui-layer #flowprocessDiv{
	margin:0px;
}
#fieldset_commit_to div,
#flow-reminder-text-up div{
	margin: 10px;
}

#flow-reminder-panel fieldset,
#flowprocessDiv fieldset {
	min-height: 100px;
}
#flow-reminder-panel #div_button_place,
#flowprocessDiv #div_button_place {
    margin-right: 15px;
}
</style>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
	<s:token name="token"/>
	<!-- 流程提交面板 -->
	<div id="flowprocessDiv" class="searchDiv" >
		<s:hidden name="_signature" />
		<s:hidden id="submitTo" name="submitTo" value="%{#parameters.submitTo}"></s:hidden>
		<s:hidden id="_subFlowApproverInfo" name="_subFlowApproverInfo"
			value="%{#parameters._subFlowApproverInfo}"></s:hidden>
		<s:hidden id="_circulatorInfo" name="_circulatorInfo" value=""></s:hidden>
		<s:hidden id="_subFlowApproverInfoAll" name="_subFlowApproverInfoAll"
			value=""></s:hidden>
		<input type="hidden" id="input_hidden_id"/>

          <div class="row">
              <div class="col-xs-6" id="flowHtmlText">
                  <fieldset>
                      <legend>{*[cn.myapps.core.dynaform.document.submit_to]*}</legend>
                      <!-- <div class="radio">
                          <label>
                              <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked>
                              部门主管审批 </label>
                      </div> -->
                  </fieldset>
              </div>
              <div class="col-xs-6" id="flowHtmlForm">
                  <fieldset>
                      	<legend>{*[cn.myapps.core.dynaform.document.opinion]*}</legend>
						<s:textarea name="_attitude"  placeholder="{*[cn.myapps.core.workflow.approve_remarks]*}"
							cssClass="form-control" rows="3" value="%{#parameters._attitude}"/>
						<select id="fieldset_remark_usual" name="selse" style="margin:10px"><option>{*[cn.myapps.core.dynaform.document.common_opinion]*}</option></select>


						<button id="btn_undo" type="button" class="btn btn-reset fr" onclick="$sigdiv.jSignature('clear');"></button>
                        <button type="button" class="btn btn-pen fr" onclick="jQuery('#signature_box,#btn_undo').toggle(200)"></button>
						<!-- <button type="button" onclick="jQuery('#signature_box,#btn_undo').toggle(200)">手签意见</button> -->
						<!-- <button id="btn_undo" type="button" style="display:none" onclick="$sigdiv.jSignature('clear');">重写</button> -->
						
                  </fieldset>
              </div>
			</div>
			<div id="signature_box" style="padding-top:10px;display:none;">
				<div id="signature"></div>
			</div>
			<div id="div_button_place" class="button-css text-center"></div>
          	<div class="clearfix"></div>
      </div>
      
      
    <!-- 流程催办面板 -->
	<div id="flow-reminder-panel" class="searchDiv" style="display:none;">
		<div class="row">
			<div class="col-xs-6 flowHtmlText">
				<fieldset id="flow-reminder-text-up">
					<legend>{*[cn.myapps.core.dynaform.document.reminder]*}</legend>
					<div>
						<div class="flow-reminder-panel-node-list"></div>
					</div>
				</fieldset>
			</div>
			<div class="col-xs-6 flowremark">
				<fieldset id="flow-reminder-remark-box">
					<legend>{*[cn.myapps.core.dynaform.document.remarks]*}</legend>
					<section class="flow-reminder-panel-attitude">
						<s:textarea name="_reminderContent" rows="3" placeholder="{*[cn.myapps.core.dynaform.document.say_something]*}"
								cssStyle="padding:10px;width:96%;*width:90%;height:100%;overflow:auto;border:none;resize:none;" value="%{#parameters._reminderContent}"/>
					</section>
				</fieldset>
			</div>
		</div>	
		<div id="div_button_place" class="button-css flow-reminder-panel-footer">
			<a href='##' id='btn-flow-reminder' class='btn btn-primary fr'><span>{*[cn.myapps.core.dynaform.document.reminder]*}</span></a>
		</div>
		<div class="clearfix"></div>
	</div>
<script type="">
	var skinType='<%=skinType%>';
</script>
<script type="text/javascript">

var $sigdiv = $("#signature").jSignature({'UndoButton':false,width:$("#flowprocessDiv").width(),height:'200px',color:'#000',lineWidth:0});

//alvin
$('#fieldset_remark_usual').on("change",function(){
	$("textarea[name='_attitude']")[0].value=this.value;
});
//获得json的key值
function leng(data){
	var jsonLength = 0;
	var a = [];
	for(var item in data){
		a.push(item);
	}
	return a;
}
$(document).ready(function(){
	if($("input[activityType='5']").size()==0) return;
	//渲染select选择框
	var flowid = $("#flowid")[0].value;
	var url = contextPath + "/portal/share/workflow/runtime/billflow/defi/initopinion.action";
	jQuery.ajax({
		    type:"POST",      
		    url:url,      
		    data:"flowid="+flowid,
		    dataType:"text",//预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断
		    async:true,   //true为异步请求，false为同步请求
		    success:function(result){
			    if(result != "null")
			    {    	 
					var data = eval( '(' + result + ')' );
					var keys = leng(data);
					var contents="<option value=''>请选择常用意见</option>";
					for(;keys.length>=1;){
						var key = keys.pop();
						contents= contents+"<option>"+data[key]+"</option>";
					}
					$("#fieldset_remark_usual").html(contents);
			    }
		    },
		    error:function(){     //请求失败后的回调函数。
		        alert("请求失败");
		    }
		});
	
});
</script>
<script id="mode_id" type="text/x-jquery-tmpl">
	<option id="${id}">${name}</option>
</script>

<script>

var subFlowApproverInfo =[];
var subFlowApproverInfoAll =[];

var FlowPanel = {
		/**刷新流程面板， e变量为场景，1、刷新：e=init；2、提交：e=commitTo；3、回退：e=returnTo*/
	refreshFlowPanel : function(actionType,act) {
		
		var $dy_refreshObj = jQuery("#dy_refreshObj");
		var formid = $dy_refreshObj.attr("formid");
		var docid = $dy_refreshObj.attr("docid");
		var userid = $dy_refreshObj.attr("userid");
		var flowid = $dy_refreshObj.attr("flowid");
		
		var $auditorList =jQuery("#auditorList");

		//当为新建文档（$auditorList.size()==0）、或当前用户在【编辑人】中，显示流程按钮
		if ($auditorList.size()==0 || ($auditorList.size()>0 && $auditorList.val().indexOf(userid)>0)){}
		else{//反向代码写法，方便理解
			//return;
		}
		
		try {
			var url = contextPath + "/portal/dynaform/document/refreshFlowPanelHTML.action";
			var datas = dy_getValuesMap(true);
			datas["formid"]=formid;
			datas["docid"]=docid;
			datas["stateid"]=$("input[name='content.stateid']").val();
			datas["userid"]=userid;
			datas["flowid"]=flowid;
			datas["actionType"]=actionType;
			jQuery.ajax({
				    type:"POST",      
				    url:url,      
				    data:datas,
				    cache:false,
				    dataType:"text",//预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断
				    async:true,   //true为异步请求，false为同步请求
				    success:function(str){
				    	try {
							var $flowHtmlText = jQuery("#flowHtmlText").html(str);
								
							$flowHtmlText.find("fieldset").height($("#flowHtmlForm").find("fieldset").height());
							
						
							var $currNodeId = jQuery("#_currid");
							if ($currNodeId.size()>0 && $currNodeId.val()=="") {
								$currNodeId.val(jQuery("#_currentNodeId").val());
							}
							switch(actionType) {
								case "commitTo":
									$flowHtmlText.find("#fieldset_return_to").remove();
									break;
								case "returnTo":
									$flowHtmlText.find("#fieldset_commit_to").remove();
									break;
								case "init":
								default:
									FlowPanel._renderButtons();
									$flowHtmlText.find("fieldset").hide();
							}
						
							//判断是否展开面板
							if(act){
								var isToPerson_Flag = FlowPanel._checkHideSubmitPanel();
								if(isToPerson_Flag){
									if(confirm("确认提交")){
									    FlowPanel.flowCommitTo();
									}
								}else{
									var $fieldset = $("#fieldset_commit_to");
									var $fpdiv = $("#flowprocessDiv");
									
									if ($fieldset.is(":hidden") || $fieldset.size()==0) {//当前没有显示
										$fpdiv.css({'opacity':'0.2'}).slideDown("fast").fadeTo(200, 1);
									}
									else {
										$fpdiv.slideUp("fast");
									}
								}
							}
							
						} catch (ex) {
							alert('refreshFlowPanel.callback(): ' + ex.message);
						}
				    },
				    error:function (XMLHttpRequest, textStatus, errorThrown){
				    	if("timeout"==textStatus){
				    		FlowPanel.refreshFlowPanel(actionType);
				    	}else{
				    		alert("刷新流程面板失败:"+textStatus);
				    	}
				        
				    }
				});
		} catch (ex) {
			alert('refreshFlowPanel: ' + ex.message);
		}
	},
	
	/**检查是否选择抄送人*/
	_checkIsSelectCirculator : function (){
		var obj = document.getElementById("_circulator");
		var _flowType = document.getElementById("_flowType").value;
		/* 
		if(obj && _flowType != '81'){
			if(obj.value.length<=0){
				alert("请选择抄送人！");
				return false;
			}
		}
		 */
		return true;
	},
	
	/**检验选择指定审批人的节点是否已选择审批人*/
	_checkToPerson : function (){
		var nextids = document.getElementsByName('_nextids');
		var _flowType = '';
		if(document.getElementById("_flowType") != null){
			_flowType = document.getElementById("_flowType").value;
		}
		for (var i=0; i<nextids.length; i++) {
			var nodeid = nextids[i].value;
			//是否指定审批人
			var isToPerson = document.getElementById("isToPerson");
			if((isToPerson && isToPerson.value == 'true') && nextids[i].checked){
				var isNullUser = false;
				if(document.getElementById("input_"+i) && document.getElementById("input_"+i).value==""){
					isNullUser=true;
				}else{
					isNullUser=false;
				}

				// 提交到下一步
				if(_flowType == '80'){
					if(isNullUser){
				        alert ('{*[cn.myapps.core.workflow.choose_specify_auditor]*}');
						return false;
					}
				}
			}
		}

		return true;
	},

	_checkHideSubmitPanel : function (){
		var nextids = document.getElementsByName('_nextids');
		var _flowType = '';
		if(document.getElementById("_flowType") != null){
			_flowType = document.getElementById("_flowType").value;
		}
		
		var isToPerson = document.getElementById("isToPerson");
		var multiNodes = nextids.length > 1 ? true : false ;
		var isToPerson = isToPerson && isToPerson.value == 'true' ? true : false ;

		if(_flowType == '80'){
			if(!isToPerson && !multiNodes){
				return true;
			}
		}
		return false;
	},
	/**流程处理*/
	flowCommitTo : function() {
		
		if(jQuery("#signature_box").is(":visible")){
			var data = $sigdiv.jSignature('getData', "image");
			if(data && data.length==2){
				var signatureJson = '{"type":"'+data[0]+'","data":"'+data[1]+'"}';
				$("input[name='_signature']").val(signatureJson);
			}
		}
		
			if(!FlowPanel._checkIsSelectCirculator()){
				return;
			}
			
			var nextids = document.getElementsByName('_nextids');
			
			var isToPersonStr = '';
			if(document.getElementById("isToPerson")!=null){
				isToPersonStr = document.getElementById("isToPerson").value;
			}
			var flag = false;
			var parameters ='';
			
			if (nextids != null) {
				if (!FlowPanel._checkToPerson()){
					return;
				}
				
				for (var i=0; i<nextids.length; i++) {
					if (nextids[i].type != 'select-one') {
						if (nextids[i].checked) {
							flag = true;
							break;
						}
					} else {
						if (nextids[i].value != null 
						&& nextids[i].value != '') {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					//设置流程类型为【提交-80】
					if(document.getElementById("_flowType")){
						document.getElementById("_flowType").value = "80";
					}
					var actid = jQuery("input[activityType='5']").attr("actid");
					//执行提交流程按钮操作
					FlowPanel._biuldsubFlowApproverInfoStr();
					Activity.doExecute(actid,5);
				}
				else {
					alert('{*[page.workflow.chooseaction]*}');
				}
			}
			else {
				alert ('{*[page.workflow.noaction]*}');
			}
		},
		
		flowReturnTo : function() {
//			if(isToPerson || isToPersonStr =='true'){
//				var input_back = 'null';
//				if(document.getElementById("input_back") != null){
//					input_back = document.getElementById("input_back").value;
//				}
//				if(input_back !='null' && input_back == ''){
//					alert ('{*[cn.myapps.core.workflow.choose_specify_auditor]*}');
//					return;
//				}
//			}
			
			if (jQuery("#back").val()=='') {
				alert('请选择需要回退的结点！');
				return;
			}

			//设置流程类型为【回退-81】
			if(document.getElementById("_flowType")){
				document.getElementById("_flowType").value = "81";
			}
			var actid = jQuery("input[activityType='5']").attr("actid");
			if(toggleButton("button_act") || toggleButton("btn_act_returnto")) return false;//提交前把按钮变成灰色
			//执行提交流程按钮操作
			Activity.doExecute(actid,5);
		},
	
		/**子流程审批信息文本JSON格式*/
		_biuldsubFlowApproverInfoStr : function (){
			var result ='';
			if(subFlowApproverInfo.length>0){
				result+='[';
			}
			for(var i=0;i<subFlowApproverInfo.length;i++){
				result+=subFlowApproverInfo[i]+',';
			}
			if(result.length>0){
				result = result.substring(0,result.length-1);
				result+=']';
				
				jQuery("input[name='_subFlowApproverInfo']").val(result);
			}
		},
		
		/**构建抄送信息文本JSON格式*/
		_biuldCirculatorInfoStr : function (){
			var result ='';
			if(circulatorInfos.length>0){
				result+='[';
			}
			for(var i=0;i<circulatorInfos.length;i++){
				result+=circulatorInfos[i]+',';
			}
			if(result.length>0){
				result = result.substring(0,result.length-1);
				result+=']';
				
				jQuery("input[name='_circulatorInfo']").val(result);
			}
		},
		
	_renderButtons : function() {
		
		var $ = jQuery;
		var $flowProcessBtn = $("input[activityType='5']");
		if(document.getElementById("_handup")){//流程挂起
			var buttonName = jQuery("input[moduleType='handup']").attr("buttonname");
			var nodertId = jQuery("input[moduleType='handup']").attr("nodertId");
			
			var html = "<a class='1 vbtn btn btn-warning' id='act_flow_retracement' name='button_act' title='{*[Retracement]*}'  onclick='ev_flowHandup(\"" + nodertId + "\")' >"
				+ "<i class='fa fa-pause'></i> "
				+ buttonName
				+ "</a>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}else if(document.getElementById("_recover")){//流程恢复
			var buttonName = jQuery("input[moduleType='recover']").attr("buttonname");
			var nodertId = jQuery("input[moduleType='recover']").attr("nodertId");
			
			var html = "<a class='2 vbtn btn btn-success' id='act_flow_retracement' name='button_act' title='{*[Retracement]*}'  onclick='ev_flowRecover(\"" + nodertId + "\")' >"
				+ "<i class='fa fa-play'></i> "
				+ buttonName
				+ "</a>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_retracement") && !document.getElementById('act_flow_retracement')){//渲染回撤按钮
			
			var html = "<a class='3 vbtn btn btn-danger' id='act_flow_retracement' name='button_act' title='{*[Retracement]*}' onclick='doRetracement()' >"

			+ "{*[Retracement]*}"
			+ "</a>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_addAuditor") && !document.getElementById('act_flow_addAuditor')){//渲染流程加签按钮
			var html = "<a class='4 vbtn btn btn-default' id='act_flow_addAuditor' name='button_act' title='{*[cn.myapps.core.workflow.add_auditor]*}' onclick='addAuditor()' ><img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + "/resource/imgv2/front/act/act_35.gif"
			+ "' />"
			+ "{*[cn.myapps.core.workflow.add_auditor]*}"
			+ "</a>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_editAuditor") && !document.getElementById('act_flow_editAuditor')){//渲染编辑审批人按钮

			var html = "<a class='5 vbtn btn btn-primary' id='act_flow_editAuditor' name='button_act' title='{*[Edit_Auditor]*}' onclick='editAuditor()' >"
			//+ "<img style='border:0px solid blue;vertical-align:middle;' src='" + contextPath + "/resource/imgv2/front/act/act_24.gif"
			//+ "' />"
			+ "{*[Edit_Auditor]*}"
			+ "</a>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_editFlow") && !document.getElementById('act_flow_editFlow')){//渲染调整流程按钮
			var html = "<a class='6 vbtn btn btn-warning' id='act_flow_editFlow' name='button_act' title='{*[WorkflowAdjustment]*}' onclick='editFlowByFrontUser()' >"
			//+ "<img style='border:0px solid blue;vertical-align:middle;' src='" + contextPath + "/resource/imgv2/front/act/act_38.gif"
			//+ "' />"
			+ "{*[WorkflowAdjustment]*}"
			+ " </a>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_termination") && !document.getElementById('act_flow_termination')){//渲染终止流程按钮
			var html = "<a class='7 vbtn btn btn-danger' id='act_flow_termination' name='button_act' title='{*[cn.myapps.core.dynaform.activity.type.flow_terminate]*}' onclick='terminateFlow()' >"
			//+ "<img style='border:0px solid blue;vertical-align:middle;' src='contextPath + "/resource/imgv2/front/act/act_44.gif"
			//+ "' />"
			+ "{*[cn.myapps.core.dynaform.activity.type.flow_terminate]*}"
			+ " </a>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_back") && !document.getElementById('act_flow_back')){//渲染回退流程按钮
			var html = "<a class='8 vbtn btn btn-warning' id='act_flow_back' href='##' name='button_act' title='回退'>"
			+ "<i class='fa fa-reply-all'></i> 回退"
			+ " </a>";
			var $b = $(html);
			$b.click(function(){
				var $fieldset = $("#fieldset_return_to");
				var $fpdiv = $("#flowprocessDiv");
				if ($fieldset.is(":hidden") || $fieldset.size()==0) {//当前没有显示
					//显示流程操作面板
					//点击按钮时需要重新刷新
					FlowPanel.refreshFlowPanel("returnTo");
					//<button type='button' class='btn btn-green fr' name='btn_act_committo'>{*[Commit]*}</button>
					var $btn = $("<button type='button' name='btn_act_returnto' class='btn btn-danger'><span>{*[cn.myapps.core.workflow.reject_confirm]*}</button>").click(function(){
						//$(this).attr("disabled", "disabled");
						FlowPanel.flowReturnTo();
					});
					$("#div_button_place").empty().append($btn);
					$fpdiv.css("opacity","0.2").slideDown("fast").fadeTo(200, 1);
				}
				else {
					$fpdiv.slideUp("fast");
				}
			});
			$flowProcessBtn.after($b);
		}
		
		if(document.getElementById("btn_flow_reminder") && !document.getElementById('act_flow_reminder')){//渲染催办按钮
			var html = "<a class='9 vbtn btn btn-primary' id='act_flow_reminder' name='button_act' title='{*[cn.myapps.core.dynaform.document.reminder]*}'>"
				+ "<i class='fa fa-fire'></i> "
				+ "{*[cn.myapps.core.dynaform.document.reminder]*}"
				+ " </a> ";
			var $b = $(html);
			
			var $t =  $("#btn_flow_reminder");
			var noderts = $t.data("nodes");
			for(var i=0;i<noderts.length;i++){
				$('<lable><input type="checkbox" name="_nodertIds" value="'+noderts[i].nodertId+'" checked="checked" />'+noderts[i].nodeName+'</lable>').appendTo($(".flow-reminder-panel-node-list"));
			}
			
			$("#btn-flow-reminder").on("click",function(e){
				if($("input[name='_nodertIds']:checked").length<=0){
					alert("{*[cn.myapps.core.dynaform.document.reminder.please_tick_the_process_node_need_reminders]*}");
					return;
				}
				if($("textarea[name='_reminderContent']").val().length<=0){
					alert("{*[cn.myapps.core.dynaform.document.reminder.please_fill_in_the_note_reminders]*}");
					return;
				} else if($("textarea[name='_reminderContent']").val().length>200) {
					alert("{*[cn.myapps.core.dynaform.document.reminder.reminders_note_too_long]*}");
					return;
				}
			   	unbindBeforeUnload();
				document.forms[0].action = contextPath + '/portal/dynaform/document/flowReminder.action';
				if(toggleButton("button_act")) return false;//提交前把按钮变成灰色
				makeAllFieldAble();
				document.forms[0].submit();
				dy_lock();
				layer.closeAll();
			});
			
			$b.click(function(){
				var w = ($("#act").width()/3)*2;
				layer.open({
				   type: 1,
				   title: false, 
				   content: $("#flow-reminder-panel"), //捕获的元素
				   area: w+'px',
				   offset:'27px',//设置弹出层的top
				   shade: .3,
				   shadeClose: true,
				   //btn: ['提交', '取消'],
				   yes: function(index, layero){
						
				   },
				   cancel: function(index){
				        layer.close(index);
				   }
				});
			});
			
			$flowProcessBtn.after($b);
		}

		//Alvin	2014-12-23 用each()遍历出流程处理		2014-12-25 用icon设置更改后图标
		$.each($flowProcessBtn , function(i,target){
			var showtype = $(target).attr("flowshowtype");
			var icon = "/resource/imgv2/front/act/act_5.gif";
			if($(target).attr("icon")!=null && isNaN($(target).attr("icon")))
				icon = "/lib/icon" + $(target).attr("icon");
				
			if(document.getElementById("btn_commit") && !document.getElementById('act_flow_submit'+i)){//渲染提交流程按钮
			var title = $(target).val();
			
			var html = "<a class='10 vbtn btn btn-primary' href='#' id='act_flow_submit"+i+"' name='button_act' title='"
			+title+"'><i class='fa fa-clock-o'></i> "
			+ title
			+ "</a>";
			var $b = $(html);
			$b.click(function(){
				var $fieldset = $("#fieldset_commit_to");
				var $fpdiv = $("#flowprocessDiv");
				
				//点击按钮时需要重新刷新
				FlowPanel.refreshFlowPanel("commitTo");
				var $btn = $("<button type='button' class='btn btn-primary' name='btn_act_committo'>{*[Confirm_To_Commit]*}</button>").click(function(){
					//$(this).attr("disabled", "disabled");//锁住当前操作，避免重复提交
					if(showtype=="ST02"){
						layer.closeAll();
					}
					FlowPanel.flowCommitTo();
					
				});
				$("#div_button_place").empty().append($btn);
				//$fpdiv.css("opacity","0.2").slideDown("fast").fadeTo(200, 1);
				
				if(showtype=="ST02"){
					//捕获页
					var w = $("#act").width()-30;

					layer.open({
					   type: 1,
					   title: false, 
					   content: $fpdiv, //捕获的元素
					   area: w+'px',
					   offset:'27px',//设置弹出层的top
					   shade: .3,
					   shadeClose: true,
					   cancel: function(index){
					        layer.close(index);
					   }
					});
				}else if(showtype == "ST03"){
						FlowPanel.refreshFlowPanel("commitTo",true); // 存在制定审批人和多节点的情况进行筛除
				}else{
					if ($fieldset.is(":hidden") || $fieldset.size()==0) {//当前没有显示
						$fpdiv.css({'opacity':'0.2'}).slideDown("fast").fadeTo(200, 1);
					}
					else {
						$fpdiv.slideUp("fast");
					}
				}
			});
			$(target).after($b);
		}})
		
	},
	
	
	/*
	*	子流程节点选择审批人	
	*/       
	showUserSelectOnSubFlow : function (actionName, settings){
		var appId =  jQuery("input[activityType='5']").attr("applicationid");
		var url = contextPath + '/portal/share/user/selectApprover4Subflow.jsp?application='+appId;
		url += "&docid=" + settings.docid + "&instanceId=" + settings.instanceId+"&numberSetingType=" + settings.numberSetingType+"&instanceTotal=" + settings.instanceTotal+"&nodeid=" + settings.nextNodeId+"&flowid="+ settings.flowid;
		//var jsonStr = jQuery("input[name='_subFlowApproverInfoAll']").val();
		var jsonStr = getSubFlowApproverInfo(settings.nextNodeId);
		//var nodeid = "123456789";
		var value = '';
		OBPM.dialog.show({
			width: 700,
			height:500,
			url: url,
			args: {value: value, readonly: settings.readonly,numberSetingType: settings.numberSetingType,instanceTotal: settings.instanceTotal,nodeid: settings.nextNodeId,jsonStr: jsonStr},
			//args: {"instanceTotal": 5,"nodeid": nodeid,"jsonStr": jsonStr},
			title: '{*[User]*}{*[Select]*}',
			close: function(jsonObj) {
				if(jsonObj != null){
					//alert(JSON.stringify(jsonObj));
					
					
					FlowPanel._addSubFlowApproverInfo(subFlowApproverInfoAll,settings.nextNodeId,JSON.stringify(jsonObj));
					
					//jQuery("input[name='_subFlowApproverInfoAll']").val(JSON.stringify(jsonObj));
					//去除names属性
					var approverObj = JSON.parse(jsonObj.approver);
					var nameStr = "";
					//alert(approverObj.length);
					for(var i = 0; i < approverObj.length; i++){
						if(i != 0)nameStr += ',';
						nameStr += approverObj[i].names;
						delete approverObj[i].names;
					}
					jQuery("input[name="+ settings.textField +"]").val(nameStr);
					jsonObj.approver = approverObj;
					//alert("after delete ==> " + JSON.stringify(jsonObj));
					//-----------------------------------------------------
					//alert(JSON.stringify(jsonObj));
					FlowPanel._addSubFlowApproverInfo(subFlowApproverInfo,settings.nextNodeId,JSON.stringify(jsonObj));
					
					//biuldsubFlowApproverInfoStr();
					
					
					
					
					//-----------------------------------------------------
					
					//jQuery("input[name='_subFlowApproverInfo']").val(JSON.stringify(jsonObj));
				}
				//组装 json 字符串 并赋值到 一个name为“_subFlowApproverInfo”隐藏域里 作为参数提交到后台处理
			}
		});
	},
	_addSubFlowApproverInfo : function(Obj,nodeId,info){
		var falg =true;
		for(var i=0;i<Obj.length;i++){
			var tmp =Obj[i];
			if(tmp.indexOf(nodeId)>0){
				Obj[i] =info;
				falg = false;
				break;
			}
		}
		if(falg){
			Obj.push(info);
		}
	},

	
	showUserSelect : function(actionName, settings) {
		var appId =  jQuery("input[activityType='5']").attr("applicationid");
		var url = contextPath + '/portal/share/user/selectbyflow.jsp?application='+appId;
		url += "&docid=" + settings.docid + "&nodeid=" + settings.nextNodeId+"&flowid="+ settings.flowid;
		var valueField = document.getElementById(settings.valueField);
		var value = "";
		if (valueField){
			value = valueField.value;
		}
		
		var ids = document.getElementById(settings.hiddenIds).value;
		var icons;
		var _path;
		if(skinType == "H5"){
			icons = "icons_4";
			_path = "../H5/resource/component/artDialog"
		}else{
			icons = "";
			_path = "";
		}
		OBPM.dialog.show({
			width: 682,
			height: 500,
			url: url,
			icon:icons,
			path: _path,
			//args: {parentObj: window, idField: "submitTo", nameField: settings.textField, readonly: settings.readonly},
			args: {value: value, readonly: settings.readonly,"applicationid":appId,"defValue":ids},
			title: '{*[cn.myapps.core.workflow.user_select]*}',
			close: function(result) {
				selectFlag = true;
				var rtn = result;
				var field = document.getElementById(settings.textField);
				if (field) {
					if (rtn) {
						isToPerson = true;
						if (rtn[0] && rtn.length > 0) {
							var rtnValue = '';
							var rtnText = '';
							//userid多个以","分隔
							var selectedNode=document.getElementById(settings.nextNodeId);
							//用户选择曾经选过的节点
							var submitTo = document.getElementById("submitTo").value;
							if(submitTo==null || submitTo==""){
                                 submitTo = "[";
							}else{
								if(submitTo.lastIndexOf("]")!=-1){
								 	submitTo = submitTo.substr(0,submitTo.lastIndexOf("]"));
								 	submitTo = submitTo +',';
								}
							}
							var start=submitTo.indexOf(settings.nextNodeId)+settings.nextNodeId.length+34;
							if(submitTo.indexOf(settings.nextNodeId)>0){
								var strfront=submitTo.substr(0,start);
								var strtemp=submitTo.substr(start+1,submitTo.length);
								if(strtemp.substr(strtemp.length-1,strtemp.length)==","){
									strtemp = strtemp.substr(0,strtemp.length-1);
								}
								var strfoot=strtemp.substr(strtemp.indexOf("]",0)-1,strtemp.length);
								submitTo=strfront;
								for (var i = 0; i < rtn.length; i++) {
									rtnValue += rtn[i].value + ";";
									rtnText += rtn[i].text + ";";
									submitTo+="'"+rtn[i].value+"',";
								}
								//submitTo= submitTo.replace('"userids":"','"userids":"[');
								submitTo=submitTo.substring(0,submitTo.length-2);
								submitTo+=strfoot;

								document.getElementById(settings.hiddenIds).value = rtnValue.substring(0, rtnValue.lastIndexOf(";"));
							}
							else{
								submitTo+="{\"nodeid\":'"+settings.nextNodeId+"',\"isToPerson\":'true',\"userids\":\"[";
								var userids="";
								for (var i = 0; i < rtn.length; i++) {
									rtnValue += rtn[i].value + ";";
									rtnText += rtn[i].text + ";";
									userids+="'"+rtn[i].value+"',";
								}
								userids=userids.substring(0,userids.length-1); 
								submitTo+=userids+"]\"}";

								document.getElementById(settings.hiddenIds).value = rtnValue.substring(0, rtnValue.lastIndexOf(";"));
							}
							document.getElementById("submitTo").value = submitTo+"]";
							valueField.value = rtnValue.substring(0, rtnValue.lastIndexOf(";"));
							var text = rtnText.substring(0, rtnText.lastIndexOf(";"));
							field.value = text;
							field.title = text;
						}else{
							valueField.value = '';
							field.value = '';
							field.title = '';
							document.getElementById("submitTo").value = '';
							document.getElementById(settings.hiddenIds).value = '';
							isToPerson = false;
						}
					} else {
						
					}
			
					if (settings.callback) {
						settings.callback(valueField.name);
					}
				}
			}
		});
	}
};

/*
*	子流程节点选择审批人	
*/       
function showUserSelectOnSubFlow(actionName, settings){
	FlowPanel.showUserSelectOnSubFlow(actionName, settings);
}

/**不想改变服务器端输出HTML文本结构（虽然并不喜欢），在此保留一个接口*/
function showUserSelect(actionName, settings) {
	FlowPanel.showUserSelect(actionName, settings);
} 
/**
 *设置流程类型 
 *服务器端输出的html中输出的组件会调用此JavaScript方法，
 * 不想改变服务器端输出，因此保留一个空的方法接口，避免浏览器错误
 */
 function ev_setFlowType(isOthers, element, flowOperation) {
	//Nothing...
}

 function getSubFlowApproverInfo(nodeId){
		for(var i=0;i<subFlowApproverInfoAll.length;i++){
			var tmp =subFlowApproverInfoAll[i];
			if(tmp.indexOf(nodeId)>0){
				return tmp;
			}
		}
		return '';
	}
jQuery(document).ready(function(){
	if($("input[activityType='5']")){
		FlowPanel.refreshFlowPanel("init");
	}
		//$("input[name='input1']").blur(function(){ alert("Custom event triggered."); });	
});

var circulatorInfos =[];
/**
*指定抄送人
*
**/
function selectCirculator(actionName, settings) {
	var url = contextPath + '/portal/share/user/selectCirculatorByFlow.jsp?application='+application;
	url += "&docid=" + settings.docid + "&nodeid=" + settings.nextNodeId+"&flowid="+ settings.flowid;
	var valueField = document.getElementById(settings.valueField);
	var value = "";
	if (valueField){
		value = valueField.value;
	}
	OBPM.dialog.show({
		width: 682,
		height: 500,
		url: url,
		args: {
			// p1:当前窗口对象
			"parentObj" : window,
			// p2:存放userid的容器id
			"targetid" : "_circulatorInfo",
			// p3:存放username的容器id
			"receivername" : settings.textField,
			// p4:默认选中值, 格式为[userid1,userid2]
			"defValue": settings.defValue,
			//选择用户数
			"limitSum": settings.limitSum,
			//选择模式
			"selectMode":settings.selectMode
		},
		title: '{*[cn.myapps.core.workflow.user_select]*}',
		close: function(result) {
			selectFlag = true;
			var rtn = result;
			var field = document.getElementById(settings.textField);
			if (field) {
				if (rtn) {
					var rtnValue = '';
					var rtnText = '';
					for (var i = 0; i < rtn.length; i++) {
						rtnValue += '"'+ rtn[i].value + '",';
						rtnText += rtn[i].text + ";";
					}
					if(rtnValue.length>0){
						rtnValue = rtnValue.substring(0,rtnValue.length-1);
						rtnValue = '['+rtnValue+']';
						var circulatorInfo ='{circulator:'+rtnValue+'}';
						circulatorInfos[0] = circulatorInfo;
						//addCirculatorInfo(circulatorInfos,settings.nextNodeId,circulatorInfo);
						biuldCirculatorInfoStr();
					}
					var text = (rtnText == ''? rtnText:rtnText.substring(0,rtnText.length-1));
					field.value = text;
					field.title = text;
				}else {
					if (rtn == '') { // 清空
						field.value = '';
						field.title = '';
						circulatorInfos[0] ='';
						document.getElementById("_circulatorInfo").value = '';
					}
				}
			}
		}
	});
}

function addCirculatorInfo(Obj,nodeId,info){
	var falg =true;
	for(var i=0;i<Obj.length;i++){
		var tmp =Obj[i];
		if(tmp.indexOf(nodeId)>0){
			Obj[i] =info;
			falg = false;
			break;
		}
	}
	if(falg){
		Obj.push(info);
	}
}

function biuldCirculatorInfoStr(){
	var result ='';
	if(circulatorInfos.length>0){
		result+='[';
	}
	for(var i=0;i<circulatorInfos.length;i++){
		result+=circulatorInfos[i]+',';
	}
	if(result.length>0){
		result = result.substring(0,result.length-1);
		result+=']';
		
		jQuery("input[name='_circulatorInfo']").val(result);
	}
	//alert("result==" +result);

	//alert(jQuery("input[name='_circulatorInfo']").val());
	
}

function checkIsSelectCirculator(){
	var obj = document.getElementById("_circulator");
	var _flowType = document.getElementById("_flowType").value;
	/* if(obj && _flowType != '81'){
		if(obj.value.length<=0){
			alert("请选择抄送人！");
			return false;
		}
	} */
	return true;
}
</script>

</o:MultiLanguage>
