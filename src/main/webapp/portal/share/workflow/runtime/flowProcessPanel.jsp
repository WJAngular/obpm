<%@ page pageEncoding="UTF-8"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.myapps.core.workflow.FlowState"%>
<%@ page import="cn.myapps.core.workflow.FlowType"%>
<%@ page import="cn.myapps.core.workflow.element.*"%>
<%@ page import="cn.myapps.core.workflow.storage.definition.ejb.*"%>
<%@ page import="cn.myapps.util.ProcessFactory" %>
<%@page import="cn.myapps.util.StringUtil"%>
<script type="text/javascript" src='<s:url value="/portal/share/script/jquery.placeholder.1.3.js"/>'></script>
<!--[if lt IE 9]>
<script type="text/javascript" src='<s:url value="/portal/share/script/jSignature/flashcanvas.js"/>'></script>
<![endif]-->
<script src='<s:url value="/portal/share/script/jSignature/jSignature.min.js"/>'></script>
<script src='<s:url value="/portal/share/script/jSignature/plugins/jSignature.CompressorBase30.js"/>'></script>
<script src='<s:url value="/portal/share/script/jSignature/plugins/jSignature.CompressorSVG.js"/>'></script>
<script src='<s:url value="/portal/share/script/jSignature/plugins/jSignature.UndoButton.js"/>'></script> 
<script src='<s:url value="/portal/share/script/jSignature/plugins/signhere/jSignature.SignHere.js"/>'></script>
<script src='<s:url value="/portal/share/script/layer/layer.js"/>'></script> 

<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
	<s:token name="token"/>
<style>
.btn-ul {list-style-image: none; margin-left: 0; padding-left: 0;}
.btn-ul li {float:left;display: inline;word-break:keep-all;	margin-right:5px;margin-bottom:3px;}
.flowHtmlText,.flowremark{float:left;}
.flowHtmlText{padding:7px;}	
.flowremark{padding:7px 10px 0 0;}
#div_button_place {padding:10px;}
fieldset{min-height:100px;border:solid 1px #d9d9d9;}

/**提交按钮风格 begin**/
        .button
        {
            margin: 3px;
            text-decoration: none;
            font: bold 1.5em 'Trebuchet MS',Arial, Helvetica; /*Change the em value to scale the button*/
            display: inline-block;
            text-align: center;
            color: #fff;
            
            border: 1px solid #9c9c9c; /* Fallback style */
            border: 1px solid rgba(0, 0, 0, 0.3);            
            
            text-shadow: 0 1px 0 rgba(0,0,0,0.4);
            
            box-shadow: 0 0 .05em rgba(0,0,0,0.4);
            -moz-box-shadow: 0 0 .05em rgba(0,0,0,0.4);
            -webkit-box-shadow: 0 0 .05em rgba(0,0,0,0.4);
            
        }
        
        .button, .button span
        {
            -moz-border-radius: .3em;
            border-radius: .3em;
        }
        
        .button span
        {
           /* border-top: 1px solid #fff;*/ /* Fallback style */
            /*border-top: 1px solid rgba(255, 255, 255, 0.5);*/
            display: block;
            padding: 0.5em 2.5em;
            
            /* The background pattern */
            
            /*background-image: -webkit-gradient(linear, 0 0, 100% 100%, color-stop(.25, rgba(0, 0, 0, 0.05)), color-stop(.25, transparent), to(transparent)),
                              -webkit-gradient(linear, 0 100%, 100% 0, color-stop(.25, rgba(0, 0, 0, 0.05)), color-stop(.25, transparent), to(transparent)),
                              -webkit-gradient(linear, 0 0, 100% 100%, color-stop(.75, transparent), color-stop(.75, rgba(0, 0, 0, 0.05))),
                              -webkit-gradient(linear, 0 100%, 100% 0, color-stop(.75, transparent), color-stop(.75, rgba(0, 0, 0, 0.05)));
            background-image: -moz-linear-gradient(45deg, rgba(0, 0, 0, 0.05) 25%, transparent 25%, transparent),
                              -moz-linear-gradient(-45deg, rgba(0, 0, 0, 0.05) 25%, transparent 25%, transparent),
                              -moz-linear-gradient(45deg, transparent 75%, rgba(0, 0, 0, 0.05) 75%),
                              -moz-linear-gradient(-45deg, transparent 75%, rgba(0, 0, 0, 0.05) 75%);*/

            /* Pattern settings */
            
           /* -moz-background-size: 3px 3px;
            -webkit-background-size: 3px 3px;    */        
        }

        .button:hover
        {
            box-shadow: 0 0 .1em rgba(0,0,0,0.4);
            -moz-box-shadow: 0 0 .1em rgba(0,0,0,0.4);
            -webkit-box-shadow: 0 0 .1em rgba(0,0,0,0.4);
        }
        
        .button:active
        {
            /* When pressed, move it down 1px */
            position: relative;
            top: 1px;
        }
        
        
        /*----------------------------*/
        
        .button-green
        {
        	background: #5cb85c;
            /*background: #428739;
            background: -webkit-gradient(linear, left top, left bottom, from(#c8dd95), to(#428739) );
            background: -moz-linear-gradient(-90deg, #c8dd95, #428739);
            filter: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr='#c8dd95', EndColorStr='#428739');*/
        }
        
        .button-green:hover
        {
        	background: #419641;
            /*background: #c8dd95;
            background: -webkit-gradient(linear, left top, left bottom, from(#428739), to(#c8dd95) );
            background: -moz-linear-gradient(-90deg, #428739, #c8dd95);
            filter: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr='#428739', EndColorStr='#c8dd95');*/
        }
        
        .button-green:active
        {
            background: #428739;
        }
        
        /*----------------------------*/
        
        .button-red
        {
            background: #D82741;
            background: -webkit-gradient(linear, left top, left bottom, from(#E84B6E), to(#D82741) );
            background: -moz-linear-gradient(-90deg, #E84B6E, #D82741);
            filter: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr='#E84B6E', EndColorStr='#D82741');
        }

        .button-red:hover
        {
            background: #E84B6E;
            background: -webkit-gradient(linear, left top, left bottom, from(#D82741), to(#E84B6E) );
            background: -moz-linear-gradient(-90deg, #D82741, #E84B6E);
            filter: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr='#D82741', EndColorStr='#E84B6E');
        }
        
        .button-red:active
        {
            background: #D82741;
        }
       
		/*This is the div within which the signature canvas is fitted*/
		#signature {
			background-color:#ffffe6;
			/*float:left;*/
		}
        /*----------------------------*/
        
/****/
#fieldset_commit_to,#flow-reminder-text-up {/*margin-left:80px;*/margin:10px 15px 0 15px;padding:6px 12px;border-radius:4px; /*background: url("../../share/workflow/runtime/bg_flowto_fieldset.png") no-repeat ; background-position: 5px 10px ; */ } 
#fieldset_return_to {margin-left:80px;padding:0 10px 10px 25px; background: url("../../share/workflow/runtime/bg_flowto_fieldset.png") no-repeat ; background-position: 5px 5px ;  } 
#fieldset_remark,#flow-reminder-remark-box {/*margin-left:20px;*/margin:10px 15px 0 15px;padding:6px 12px;border-radius:4px;/* background: url("../../share/workflow/runtime/bg_flowto_fieldset.png") no-repeat ; background-position: 5px 10px ;*/ } 
#fieldset_commit_to legend,#fieldset_remark legend{ border: none; width: 80px; margin-left: 10px; margin-bottom: 0px; font-size: 16px; color: #33333f; padding-left: 15px; }
</style>

<div id="flowprocessDiv" style="border-top: 0px solid #fafafa;float:left;width:100%;background-color:#ffffe6;display:none;">
		<div id="flowHtmlText" class="flowHtmlText" style="width:50%;padding:0;">
		</div>
		<div class="flowremark" style="width:50%;padding:0;">
		<fieldset id="fieldset_remark" ><legend>备注</legend>
			<s:textarea name="_attitude" rows="3" placeholder="说点什么吧..."
				cssStyle="width:100%;*width:90%;height:100%;overflow:auto;border:none;resize:none;" value="%{#parameters._attitude}"/>
			<select id="fieldset_remark_usual" style="float:left;margin-top:10px;padding-top:1px;padding-bottom:1px;" name="selse"><option>常用意见</option></select>
			<button type="button"  style="float:right;width:90px;height:22px;margin-top:10px;border:none;cursor:pointer;background:url('../../share/images/btn_pen.png');" onclick="jQuery('#signature_box,#btn_undo').toggle(200)"></button>
		</fieldset>
		</div>
		<s:hidden name="_signature" />
		<s:hidden id="submitTo" name="submitTo" value="%{#parameters.submitTo}"></s:hidden>
		<s:hidden id="_subFlowApproverInfo" name="_subFlowApproverInfo"
			value="%{#parameters._subFlowApproverInfo}"></s:hidden>
		<s:hidden id="_circulatorInfo" name="_circulatorInfo" value=""></s:hidden>
		<s:hidden id="_subFlowApproverInfoAll" name="_subFlowApproverInfoAll"
			value=""></s:hidden>
		<input type="hidden" id="input_hidden_id"/>

		
		<!-- 
		<button id="btn_undo" type="button" style="display:none" onclick="$sigdiv.jSignature('clear');">重写</button>
		 -->
		<div id="signature_box" style="width:100%;margin-top:30px;border-bottom:1px solid #efefef;display:none;clear:both">
			<div id="signature"></div>
		</div>
		<div style="clear:both;"></div>
		<div id="div_button_place" class="button-css" style="float:right;"></div>
		<button id="btn_undo" type="button" style="display:none;float:right;line-height:49px;margin:10px;font-size:1.5em; background:none;border:none;cursor:pointer;" onclick="$sigdiv.jSignature('clear');">重写</button>
		
</div>

<!-- 流程催办面板 -->
<div id="flow-reminder-panel" style="border-top: 0px solid #fafafa;float:left;width:100%;background-color:#ffffe6;display:none;">
	<div class="flowHtmlText" style="width:50%;padding:0;">
		<fieldset id="flow-reminder-text-up">
			<legend>提交至</legend>
			<div>
				<div class="flow-reminder-panel-node-list"></div>
			</div>
		</fieldset>
	</div>
	<div class="flowremark" style="width:50%;padding:0;">
		<fieldset id="flow-reminder-remark-box">
			<legend>备注</legend>
			<section class="flow-reminder-panel-attitude">
				<s:textarea name="_reminderContent" rows="3" placeholder="说点什么吧..."
						cssStyle="width:96%;*width:90%;height:100%;overflow:auto;border:none;resize:none;" value="%{#parameters._reminderContent}"/>
			</section>
		</fieldset>
	</div>
	
	<div id="div_button_place" class="button-css" style="float:right;">
		<footer class="flow-reminder-panel-footer">
			<a href='##' id='btn-flow-reminder' class='button button-green'><span>{*[催办]*}</span></a>
		</footer>
	</div>
</div>

<script type="text/javascript">
var $sigdiv = $("#signature").jSignature({'UndoButton':false,width:$("#flowprocessDiv").width()-300,height:'180px',color:'#000',lineWidth:0});

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
	//渲染select选择框
	var flowid = $("#flowid")[0].value;
	var url = contextPath +"/portal/share/workflow/runtime/billflow/defi/initopinion.action";
	jQuery.ajax({
		    type:"GET",      
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
									break;
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
		/* if(obj && _flowType != '81'){
			if(obj.value.length<=0){
				alert("请选择抄送人！");
				return false;
			}
		} */
		
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
					//if(toggleButton("button_act") || toggleButton("btn_act_returnto")) return false;//提交前把按钮变成灰色
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
			
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_retracement' name='button_act' title='{*[Retracement]*}'  onclick='ev_flowHandup(\"" + nodertId + "\")' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
				+ "../../.."+"/resource/imgv2/front/act/act_13.gif"
				+ "' />"
				+ buttonName
				+ " </span> </a> </span></div> ";
				var $b = $(html);
				$flowProcessBtn.after($b);
		}else if(document.getElementById("_recover")){//流程恢复
			var buttonName = jQuery("input[moduleType='recover']").attr("buttonname");
			var nodertId = jQuery("input[moduleType='recover']").attr("nodertId");
			
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_retracement' name='button_act' title='{*[Retracement]*}'  onclick='ev_flowRecover(\"" + nodertId + "\")' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ "../../../resource/imgv2/front/act/act_45.gif"
			+ "' />"
			+ buttonName
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_retracement") && !document.getElementById('act_flow_retracement')){//渲染回撤按钮
			
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_retracement' name='button_act' title='{*[Retracement]*}' onclick='doRetracement()' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ "../../../resource/imgv2/front/act/act_35.gif"
			+ "' />"
			+ "{*[Retracement]*}"
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_addAuditor") && !document.getElementById('act_flow_addAuditor')){//渲染流程加签按钮
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_addAuditor' name='button_act' title='{*[cn.myapps.core.workflow.add_auditor]*}' onclick='addAuditor()' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + "/resource/imgv2/front/act/act_35.gif"
			+ "' />"
			+ "{*[cn.myapps.core.workflow.add_auditor]*}"
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_editAuditor") && !document.getElementById('act_flow_editAuditor')){//渲染编辑审批人按钮

			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_editAuditor' name='button_act' title='{*[Edit_Auditor]*}' onclick='editAuditor()' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + "/resource/imgv2/front/act/act_24.gif"
			+ "' />"
			+ "{*[Edit_Auditor]*}"
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_editFlow") && !document.getElementById('act_flow_editFlow')){//渲染调整流程按钮
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_editFlow' name='button_act' title='{*[WorkflowAdjustment]*}' onclick='editFlowByFrontUser()' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + "/resource/imgv2/front/act/act_38.gif"
			+ "' />"
			+ "{*[WorkflowAdjustment]*}"
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_termination") && !document.getElementById('act_flow_termination')){//渲染终止流程按钮
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_termination' name='button_act' title='{*[cn.myapps.core.dynaform.activity.type.flow_terminate]*}' onclick='terminateFlow()' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + "/resource/imgv2/front/act/act_44.gif"
			+ "' />"
			+ "{*[cn.myapps.core.dynaform.activity.type.flow_terminate]*}"
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_back") && !document.getElementById('act_flow_back')){//渲染回退流程按钮
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_back' href='##' name='button_act' title='回退'> <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + "/resource/imgv2/front/act/act_44.gif"
			+ "' />"
			+ "回退"
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$b.find("a").click(function(){
				var $fieldset = $("#fieldset_return_to");
				var $fpdiv = $("#flowprocessDiv");
				if ($fieldset.is(":hidden") || $fieldset.size()==0) {//当前没有显示
					//显示流程操作面板
					//点击按钮时需要重新刷新
					FlowPanel.refreshFlowPanel("returnTo");

					var $btn = $("<a href='##' name='btn_act_returnto' class='button button-red'><span>{*[cn.myapps.core.workflow.reject]*}</span></a>").click(function(){
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
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_reminder' name='button_act' title='{*[催办]*}' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + "/resource/imgv2/front/act/act_2.gif"
			+ "' />"
			+ "{*[催办]*}"
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			
			var $t =  $("#btn_flow_reminder");
			var noderts = $t.data("nodes");
			for(var i=0;i<noderts.length;i++){
				$('<lable><input type="checkbox" name="_nodertIds" value="'+noderts[i].nodertId+'" checked="checked" />'+noderts[i].nodeName+'</lable>').appendTo($(".flow-reminder-panel-node-list"));
			}
			
			$("#btn-flow-reminder").on("click",function(e){
				if($("input[name='_nodertIds']:checked").length<=0){
					alert("请勾选需要催办的流程节点！");
					return;
				}
				if($("textarea[name='_reminderContent']").val().length<=0){
					alert("请填写催办备注！");
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
			
			$b.find("a").click(function(){
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
		

		$.each($flowProcessBtn , function(i,target){
			var showtype = $(target).attr("flowshowtype");
			var icon = "/resource/imgv2/front/act/act_5.gif";
			if($(target).attr("icon")!=null && isNaN($(target).attr("icon")))
				icon = "/lib/icon" + $(target).attr("icon");
				
			if(document.getElementById("btn_commit") && !document.getElementById('act_flow_submit'+i)){//渲染提交流程按钮
			var title = $(target).val();
			
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_submit"+i+"' name='button_act' title='"+title+"'> <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ contextPath + icon
			+ "' />"
			+ title
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$b.find("a").click(function(){
				var $fieldset = $("#fieldset_commit_to");
				var $fpdiv = $("#flowprocessDiv");
				
				//点击按钮时需要重新刷新
				FlowPanel.refreshFlowPanel("commitTo");
				var $btn = $("<a href='##' name='btn_act_committo' class='button button-green'><span>{*[Commit]*}</span></a>").click(function(){
					if(showtype=="ST02"){
						layer.closeAll();
					}
					FlowPanel.flowCommitTo();
					
				});
				$("#div_button_place").empty().append($btn);
				
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
		OBPM.dialog.show({
			width: 682,
			height: 500,
			url: url,
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
	//jQuery.Placeholder.init();
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
	/* 
	if(obj && _flowType != '81'){
		if(obj.value.length<=0){
			alert("请选择抄送人！");
			return false;
		}
	} */
	return true;
}
</script>

</o:MultiLanguage>
