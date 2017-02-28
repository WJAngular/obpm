/**
 * Phone皮肤 表单流程历史
 */
(function($,document,undefined) {
	$(document).ready(function(){
		var _flowStateId = $("input[name='content.stateid']").val();//流程实例id
		var curNodeName = $("input[name='targetNodeRT_name']").val();//当前节点name
		var curNodeId = $("input[name='targetNodeRT_id']").val();//当前节点id
		var _HAS_FLOW = _flowStateId.length>0;//表单是否已启动流程
		if(!_HAS_FLOW) return;
		$.getJSON(contextPath+"/portal/document/flow/getHistory.action",{_flowStateId:_flowStateId,_applicationId:$("input[name='application']").val()},function(result){
			if(result.status==1){
				renderFlowHisModal(result.data);
			}
		});
		$.getJSON(contextPath+"/portal/document/flow/getFlowInstanceJson.action",{_flowStateId:_flowStateId,_applicationId:$("input[name='application']").val(),_curNodeName:curNodeName,_curNodeId:curNodeId},function(result){
			if(result.status==1){
				renderFlowHisPanel(result.data.historys);
			}
		});

		/**
		**渲染表单下方流程历史面板
		**/
		function renderFlowHisPanel(flowHistorys){
			/*JSON格式
			 * [{"nodeType":"hisNode","nodeJson":[{"auditorId":"11e5-975b-abcfab55-8df7-f9d0d3fc48f4","auditorName":"邢儿（行政专员）","startNodeName":"起草","processtime":"2016-07-06 11:50:49.0","currentNode":"0"}]},{"nodeType":"curNode","nodeJson":[{"auditorId":"11e4-7b56-045d6210-a888-6d6b162bf5de","auditorName":"邢主管（行政主管）","startNodeName":"审核","processtime":"","currentNode":"1"},{"auditorId":"11e6-292f-c98bc185-9a78-9b801f3f5251","auditorName":"周杰伦（项目专员）","startNodeName":"审核","processtime":"","currentNode":"1"},{"auditorId":"5e120165-cb23-4131-aade-123ad00893da","auditorName":"李四","startNodeName":"审核","processtime":"","currentNode":"1"},{"auditorId":"880832b0-b0a6-47c0-9bb6-1f8c2debcad8","auditorName":"peng","startNodeName":"审核","processtime":"","currentNode":"1"},{"auditorId":"b5a8cc3d-dc1c-4c56-b27b-8704f4f1689a","auditorName":"张三","startNodeName":"审核","processtime":"","currentNode":"1"}]}]
			 * */
			$("#flowhis_panel_content").append("<div class='flowhis_panel_hisnode'></div>")
			
			$.each(flowHistorys,function(i,node){   // 渲染逻辑需要改
				if(node.nodeType == "hisNode"){
					var hisNodeLength = node.nodeJson.length;
					$.each(node.nodeJson,function(i,his){
						if(i == "0" || i == hisNodeLength - 1){
							var nodeItem = renderFlowHisPanelItem(i,his,node.nodeType,hisNodeLength);
							$("#flowhis_panel_content").find(".flowhis_panel_hisnode").append(nodeItem.join(""));
						}else{
							if($("#flowhis_panel_content").find(".point-node").size()<=0){
								var nodePoint = '<span class="point-node">'
									+'<div class="icon-box"><i class="iconfont iconfont-e615"></i><div class="icon-line icon-line-left"></div><div class="icon-line icon-line-right"></div>'
									+'</div></span>';
								$("#flowhis_panel_content").find(".flowhis_panel_hisnode").append(nodePoint);
							}
						}
					});
				}else if(node.nodeType == "curNode"){
					var curNodeLength = node.nodeJson.length;
					var curNodeName = node.nodeJson[0].startNodeName;
					var curNodes = false;
					$.each(node.nodeJson,function(i,his){
						var nodeItem = renderFlowHisPanelItem(i,his,node.nodeType,curNodeLength);
						//是否多节点
						if(his.startNodeName != curNodeName){
							curNodes = true;
						}
						//是否完结
						if(his.isComplete && his.isComplete != "true"){
							//是否多审批人
							if(curNodeLength > 1){
								if($("#flowhis_panel_content").find(".curNodes").size()<=0){
									$("#flowhis_panel_content").find(".flowhis_panel_hisnode").append(nodeItem.join(""));
								}
								//头像
								var avatar = Common.Util.getAvatar(his.auditorId);
								var avatarHtml = "";
								if(avatar!="" && avatar!=undefined){
									avatarHtml = '<img class="avatar" src="'+avatar+'" >';
								}else{
									avatarHtml = '<div class="noAvatar">'+ his.auditorName.substr(his.auditorName.length-2, 2) +'</div>';
								}
						        $("#flowhis_panel_content").find(".curNodes").append(avatarHtml);
							}else{
								$("#flowhis_panel_content").find(".flowhis_panel_hisnode").append(nodeItem.join(""));
							}
							
						}else{
							$("#flowhis_panel_content").find(".flowhis_panel_hisnode").append(nodeItem.join(""));
						}
					});
					//是否多节点
					if(curNodes){
						var nodesShadow = "<div class='nodesShadow2'></div><div class='nodesShadow1'></div>"
						$("#flowhis_panel_content").find(".curNodes").parent().append(nodesShadow);
					}
					
				}
			});
			
			$("#flowhis_panel_content").append('<span class="td more"><a data-ignore="push" href="#viewHistory">更多<span class="icon icon-right"></span></a></span>');
			$("#flowhis_panel").show();
		}
		
		/**
		**渲染表单上方流程历史面板中一项数据
		**/
		function renderFlowHisPanelItem(i,his,type,length){
			var html = [];
			//头像
			var avatar = Common.Util.getAvatar(his.auditorId);
			var avatarHtml = "";
			if(avatar!="" && avatar!=undefined){
				avatarHtml = '<img class="avatar" src="'+avatar+'" >';
			}else{
				avatarHtml = '<div class="noAvatar">'+ his.auditorName.substr(his.auditorName.length-2, 2) +'</div>';
			}
			//动作图标
			var actionIcon = "";
			switch (his.flowOperation){
				case "80"://"流转"
					actionIcon = "<i class='iconfont iconfont-e631 icon-09bb07'></i>"
					break;
				case "81"://"回退"
					actionIcon = "<i class='iconfont iconfont-e630 icon-f76260'></i>"
					break;
				case "85"://"回撤"
					actionIcon = "<i class='iconfont iconfont-e633 icon-ffc107'></i>"
					break;
				case "88"://"挂起"
					actionIcon = "<i class='iconfont iconfont-e628 icon-ff6600'></i>"
					break;
				case "89"://"恢复"
					actionIcon = "<i class='iconfont iconfont-e629 icon-10aeff'></i>"
					break;
				default://"当前"
					actionIcon = "<i class='iconfont iconfont-e627 icon-8c8c8c'></i>";
					break;
			}	
			
			//是否当前节点
			if(type == "hisNode"){
				//判断流程历史第一和最后节点
				if(i==0){
					html.push('<span><div class="face-name-box"><div class="face"><span class="face-box">');
		      	}else{
		      		html.push('<span class="show-node"><div class="face-name-box"><div class="face"><span class="face-box">');
		      	}
				html.push(avatarHtml);
				html.push('</span></div>');
		      	html.push('<div class="start-node-name">'+his.startNodeName+'</div></div>');
		      	if(i==0){
		      		html.push('<div class="icon-box">'+actionIcon+'<div class="icon-line icon-line-right"></div></div>')
		      	}else{
		      		html.push('<div class="icon-box">'+actionIcon+'<div class="icon-line icon-line-left"></div><div class="icon-line icon-line-right"></div></div>')
		      	}
		        html.push('<div>'+his.processtime.substring(5,10)+'</div>');
		        html.push('</span>');
			}else if(type == "curNode"){
				html.push('<span><div class="face-name-box"><div class="face"><span class="face-box">');
				//是否完结
				if(his.isComplete && his.isComplete == "true"){
					actionIcon = "<i class='iconfont iconfont-e632 icon-364046'></i>";
				}else{
					//是否多审批人
					if(length > 1){
						html.push('<div class="curNodes"></div>');
					}else{
						html.push(avatarHtml);
					}
				}
				html.push('</span></div>');
		      	html.push('<div class="start-node-name">'+his.startNodeName+'</div></div>');
		        html.push('<div class="icon-box">'+actionIcon+'<div class="icon-line icon-line-left"></div></div>')
		        html.push('<div>'+his.processtime.substring(5,10)+'</div>');
		        html.push('</span>');
			}
	        return html;
		}
		
		/**
		**渲染弹出的流程历史模态框
		**/
		function renderFlowHisModal(data){
			var isComplate = data.isComplete;
			
			var prevNodeTime;
			
			$.each(data.historys,function(i,his){
				var html = [];
				
				//审批节点
				var _startNodeName = his.startNodeName;
				//目标节点
				var _targetNodeName =  his.targetNodeName;
				//办理人
				var _auditorName = his.auditorName;
				//办理人头像
				var avatar = Common.Util.getAvatar(his.auditorId,contextPath);
				var _avatar = "";
				if(avatar!="" && avatar!=undefined){
					_avatar = '<img class="avatar" src="'+avatar+'" >';
				}else{
					_avatar = '<div class="noAvatar">'+ his.auditorName.substr(his.auditorName.length-2, 2) +'</div>';
				}
				
				//签核动作
				var _flowOperation = "";
				switch (his.flowOperation){
					case "81":
						_flowOperation = "回退"
						break;
					case "85":
						_flowOperation = "回撤"
						break;
					case "88":
						_flowOperation = "挂起"
						break;
					case "89":
						_flowOperation = "恢复"
						break;
					default:
						_flowOperation = "流转"
						break;
				}
				
				
				//办理时间
				var _time = his.processtime.replace("T"," ");
				var _processtime = new Date(_time);
				var timeFixArr = _time.split(/[- :]/); 
				var timeFixDate = new Date(timeFixArr[0], timeFixArr[1]-1, timeFixArr[2], timeFixArr[3], timeFixArr[4]);
				var _timeAgo;
				var Month = timeFixDate.getMonth() + 1; 
				var Day = timeFixDate.getDate(); 
				var Hour = timeFixDate.getHours(); 
				var Minute = timeFixDate.getMinutes(); 
				
				var comTime = Common.Util.daysCalc(_time);
				if(comTime.days > 2){
					if (Month >= 10){ 
						_timeAgo = Month + "-"; 
					}else{ 
						_timeAgo = "0" + Month + "-"; 
					} 
					if (Day >= 10) 
					{ 
						_timeAgo += Day + " "; 
					}else{ 
						_timeAgo += "0" + Day; 
					} 
				}else if(comTime.days == 2){ 
					_timeAgo = "前天 ";
					if (Hour >= 10) 
					{ 
						_timeAgo += Hour + ":" ; 
					}else{ 
						_timeAgo += "0" + Hour + ":" ; 
					} 
					if (Minute >= 10) 
					{ 
						_timeAgo += Minute ; 
					}else{ 
						_timeAgo += "0" + Minute ; 
					} 
				}else if(comTime.days == 1){
					_timeAgo = "昨天 ";
					if (Hour >= 10) 
					{ 
						_timeAgo += Hour + ":" ; 
					}else{ 
						_timeAgo += "0" + Hour + ":" ; 
					} 
					if (Minute >= 10) 
					{ 
						_timeAgo += Minute ; 
					}else{ 
						_timeAgo += "0" + Minute ; 
					} 
				}else if(comTime.days <= 0 && comTime.hours > 0){
					_timeAgo = comTime.hours + " 小时前 ";
				}else if(comTime.days <= 0 && comTime.hours <= 0){
					if(comTime.minutes < 5){
						_timeAgo = " 刚刚";
					}else{
						_timeAgo = comTime.minutes + " 分钟前 ";
					}
				}
				
				//动作图标
				var actionIcon = "";
				if(_flowOperation == "回退"){
					actionIcon = '<span class="flowOperation back-f76260"><i class="fa fa-reply"></i>'+_flowOperation+'</span>'
				}else if(_flowOperation == "回撤"){
					actionIcon = '<span class="flowOperation back-ffc107"><i class="fa fa-share-square-o"></i>'+_flowOperation+'</span>'
				}else if(_flowOperation == "挂起"){
					actionIcon = '<span class="flowOperation back-ff6600"><i class="fa fa-coffee"></i>'+_flowOperation+'</span>'
				}else if(_flowOperation == "恢复"){
					actionIcon = '<span class="flowOperation back-10aeff"><i class="fa fa-undo"></i>'+_flowOperation+'</span>'
				}else if(_flowOperation == "流转"){
					actionIcon = '<span class="flowOperation back-09bb07"><i class="fa fa-sign-in"></i>'+_flowOperation+'</span>'
				}
				
				//节点间耗时
				if(i > 0){
					var _prevNodeTime = Common.Util.daysCalc(prevNodeTime,_time);
					var _prevNodeTimeContent = "耗时"
					if(_prevNodeTime.days > 0){
						_prevNodeTimeContent += _prevNodeTime.days + "天";
					}
					if(_prevNodeTime.hours > 0){
						_prevNodeTimeContent += _prevNodeTime.hours + "小时";	
					}
					if(_prevNodeTime.minutes > 0){
						_prevNodeTimeContent += _prevNodeTime.minutes + "分";
					}
					if(_prevNodeTime.seconds > 0){
						_prevNodeTimeContent += _prevNodeTime.seconds + "秒";
					}
				}
				if(!_prevNodeTimeContent){
					_prevNodeTimeContent = "";
				}else if(_prevNodeTimeContent=="耗时"){
					_prevNodeTimeContent = "最新";
				}
				prevNodeTime = _time;

				html.push('<div class="leave-list"><div class="leave-list-tr"><div class="leave-list-td w50"></div><div class="leave-list-td"><span style="padding-left:10px">');
				html.push(_timeAgo);
				html.push('</span><span class="pull-right" style="padding-right:10px">'+_prevNodeTimeContent+'</span></div></div>');
				html.push('<div class="leave-list-tr"><div class="leave-list-td w50"><div class="face text-center"><span class="face-box">');
				html.push(_avatar);
				html.push('<div class="arrow"><i class="arrow1"></i><i class="arrow-m"></i></div></span>');
				html.push('<div class="auditor-name">'+his.auditorName+'</div>');
				html.push('</div></div><div class="leave-list-td"><div class="talk-box">');
				html.push('<div class="state">'+ _startNodeName + actionIcon + _targetNodeName +'</div>');
				html.push('<div><br/>'+(his.attitude? his.attitude : '')+'<img onclick="showbig(this);" style="max-height:128px" src="'+his.signatureImageDate+'"></div>');
				html.push('<div class="talk-horn"></div></div></div></div> </div>');
				$("#flowhis_modal_content").prepend(html.join(""));
			});
			
			if(isComplate){
				var endHis = data.historys[data.historys.length-1];
				var html = [];
				html.push(' <div class="leave-list text-center"><div class="leave-list-tr"><div class="leave-list-td w50" style="line-height: 15px;"><img src="'+contextPath+'/portal/phone/resource/images/ico-03.png" style="margin-top:15px;width: 15px;height: 15px;"><br />');
				html.push(endHis.endNodeName);
				html.push('</div><div class="leave-list-td"><span></span></div></div></div></div>');
				$("#flowhis_modal_content").append(html.join(""));
			}
			
			var leave_list_num = $(".leave-list").length;				
			for(i=0;i<leave_list_num;i++){
				var $listArrow = $(".leave-list .arrow").eq(i);
				var $listArrowLine = $(".leave-list .arrow-m").eq(i);
				if(i==leave_list_num-1){
					$listArrow.remove();
				}else{
					var reverseFaceH = $listArrow.parents(".face").outerHeight();
					$listArrow.height($(".leave-list .talk-box").eq(i).height()-reverseFaceH + 40);
					$listArrowLine.height($(".leave-list .talk-box").eq(i).height()-reverseFaceH + 40);
				}
		    	
		    }
		}
		
		
	});
	

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
		    if(result != "null"){    	 
				var data = eval( '(' + result + ')' );
				var jsonLength = 0;
				var keys = [];
				for(var item in data){
					keys.push(item);
				}
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
	
})(jQuery,document);


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
							}
							
							//判断是否展开面板
							if(act){
								var isToPerson_Flag = FlowPanel._checkHideSubmitPanel();
								if(isToPerson_Flag){
									if(confirm("确认提交")){
									    FlowPanel.flowCommitTo();
									}
								}else{
									location.hash = "#flowPro";
									$("#flowView").addClass("active");
									$("body").addClass("test-body");
									FlowPanel.refreshFlowPanel("commitTo");
								}
							}
							
						} catch (ex) {
							alert('refreshFlowPanel.callback(): ' + ex.message);
						}
				    },
				    error:function(){ 
				        alert("请求失败");
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
		/*if(obj && _flowType != '81'){
			if(obj.value.length<=0){
				alert("请选择抄送人！");
				return false;
			}
		}*/
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
						alert ('请选择指定审批人');
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
	flowCommitTo : function() {		//提交
		
			var data = $sigdiv.jSignature('getData', "image");
			if(data && data.length==2){
				var signatureJson = '{"type":"'+data[0]+'","data":"'+data[1]+'"}';
				$("input[name='_signature']").val(signatureJson);
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
					Activity.doExecute(actid,5);
				}
				else {
					alert('请选择一项操作');
				}
			}
			else {
				alert ('没有操作能被执行');
			}
		},
		
		flowReturnTo : function() {		//回退
			if (jQuery("#back").val()=='') {
				alert('请选择需要回退的结点！');
				return;
			}

			//设置流程类型为【回退-81】
			if(document.getElementById("_flowType")){
				document.getElementById("_flowType").value = "81";
			}

			var actid = jQuery("input[activityType='5']").attr("actid");
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
		
		if($("#btn_flow_reminder").length > 0 && $("#act_flow_reminder").length == 0){//渲染催办按钮
			var html = "<td><a id='act_flow_reminder' name='button_act' class='btn btn-orange btn-block'>"
				+ "催办"
				+ "</a></td>";
			var $b = $(html);
			var $flowhis_panel_hisnode = $(".flowhis_panel_hisnode").children();
			var $start_node = $flowhis_panel_hisnode.first();
			var $current_node = $flowhis_panel_hisnode.last();
			var start_node_name = $start_node.find(".start-node-name").html();
			var current_node_name = $current_node.find(".start-node-name").html();
			$('#flowReminderDiv').find(".pull-left").html(start_node_name);
			var $t =  $("#btn_flow_reminder");
			var $noderts = $t.data("nodes");
			for(var i = 0; i < $noderts.length ; ++i){
				$('<lable><input type="checkbox" name="_nodertIds" value="'
						+ $noderts[i].nodertId
						+ '" checked="checked" />'
						+ $noderts[i].nodeName 
						+ '</lable></br>').appendTo($("#flowReminderDiv").find(".pull-right"));
			}

			$b.click(function(){
				$(".flowReminderDiv").show().on('click', '.flowReminder_submit', function () {
					var url = contextPath + "/portal/dynaform/document/flowReminder.action";
					var data = $("form").serialize();
					$.ajax({
						type : "post",
						url : url,
						data : data,
						success : function(result){
			                $('#toast').show();
			                setTimeout(function () {
			                    $('#toast').hide();
			                    window.location.reload();
			                }, 2000);
			                $('#flowReminderDiv').off('click').hide();
						},
						error:function(){
			                $('#error_message').show();
			                setTimeout(function () {
			                    $('#error_message').hide();
			                }, 2000);
			                $('#flowReminderDiv').off('click').hide();
						}
					});
                }).on('click', '.flowReminder_cancel', function () {
                    $('#flowReminderDiv').off('click').hide();
                });
			});
			$flowProcessBtn.after($b);
		}
		
		if(document.getElementById("_handup")){//流程挂起
			var buttonName = jQuery("input[moduleType='handup']").attr("buttonname");
			var nodertId = jQuery("input[moduleType='handup']").attr("nodertId");
			var html = "<td><a id='act_flow_retracement' name='button_act' title='回撤'  onclick='ev_flowHandup(\"" + nodertId + "\")' class='btn btn-orange btn-block'>"
				+ buttonName
				+ "</a></td>"
			var $b = $(html);
			$flowProcessBtn.after($b);
		}else if(document.getElementById("_recover")){//流程恢复
			var buttonName = jQuery("input[moduleType='recover']").attr("buttonname");
			var nodertId = jQuery("input[moduleType='recover']").attr("nodertId");
			var html = "<div class='actBtn'> <span class='button-document' > <a id='act_flow_retracement' name='button_act' title='回撤'  onclick='ev_flowRecover(\"" + nodertId + "\")' > <span> <img style='border:0px solid blue;vertical-align:middle;' src='"
			+ "../../../resource/imgv2/front/act/act_45.gif"
			+ "' />"
			+ buttonName
			+ " </span> </a> </span></div> ";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_retracement") && !document.getElementById('act_flow_retracement')){//渲染回撤按钮			
			var html = "<td><a id='act_flow_retracement' name='button_act' title='回撤' onclick='doRetracement()' class='btn btn-orange btn-block' >"
			+ "回撤"
			+ "</a></td>";
			var $b = $(html);
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_back") && !document.getElementById('act_flow_back')){//渲染回退流程按钮
			var html = "<td><a id='act_flow_back' name='button_act' class='btn btn-orange btn-block'>"
			+ "回退"
			+ "</a></td>";
			var $b = $(html);
			$b.click(function(){
				location.hash = "#flowPro";
				$("#flowView").addClass("active");
				$("body").addClass("test-body");
				var $fieldset = $("#fieldset_return_to");
				//显示流程操作面板
				//点击按钮时需要重新刷新
				FlowPanel.refreshFlowPanel("returnTo");

				var $btn = $("<td><a name='btn_act_returnto' class='btn btn-orange btn-block' data-transition='fade'>回退</a></td>").click(function(){
					//$(this).attr("disabled", "disabled");
					FlowPanel.flowReturnTo();
				});
				var $cancel = $("<td><a name='btn_act_committo' class='btn btn-block' data-transition='fade'>取消</a></td>").click(function(){
					FlowPanel.cancelAction();
				});
				$("#div_button_place").empty().append($btn).append($cancel);
					
			});
			$flowProcessBtn.after($b);
		}
		if(document.getElementById("btn_commit") && !document.getElementById('act_flow_submit')){//渲染提交流程按钮
			var title = $flowProcessBtn.val();
			var flowShowType = $flowProcessBtn.attr("flowShowType");
			var html = '<td><a class="btn btn-positive btn-block" data-transition="fade" >' + title + '</a></td>';
			var $b = $(html);
			$b.click(function(){
				//点击按钮时需要重新刷新
				/*location.hash = "#flowPro";
				$("#flowView").addClass("active");
				$("body").addClass("test-body");
				FlowPanel.refreshFlowPanel("commitTo");
				*/
				if(flowShowType == "ST03"){
					FlowPanel.refreshFlowPanel("commitTo",true);
				}else{
					//点击按钮时需要重新刷新
					location.hash = "#flowPro";
					$("#flowView").addClass("active");
					$("body").addClass("test-body");
					FlowPanel.refreshFlowPanel("commitTo");
				}
				
				var $fieldset = $("#fieldset_commit_to");
				//设置流程类型
//					$("[name='_nextids']:first").trigger("click");
				//显示流程操作面板
				var $btn = $("<td><a name='btn_act_committo' class='btn btn-positive btn-block' data-transition='fade'>提交</a></td>").click(function(){
					//$(this).attr("disabled", "disabled");//锁住当前操作，避免重复提交
					FlowPanel.flowCommitTo();
				});
				var $cancel = $("<td><a name='btn_act_committo' class='btn btn-block' data-transition='fade'>取消</a></td>").click(function(){
					FlowPanel.cancelAction();

					//
				});
				$("#div_button_place").empty().append($btn).append($cancel);
				var u = navigator.userAgent, app = navigator.appVersion;
				var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
				if(isiOS){
					
						$("#flowProContent .reimburse").css({"overflow":"hidden","bottom":"57px"});
						var flowScroll;	
						setTimeout(function(){
							flowScroll = new IScroll('#flowProContent .reimburse', { 
								preventDefault: false
								//preventDefaultException: { tagName: /^(INPUT|TEXTAREA|BUTTON|SELECT|A|SPAN)$/ }
							})
					},1000)
				
			    }else{
			    	$(".reimburse").css("overflow","auto");
			    }
			});
			$flowProcessBtn.after($b);
		}
	},
	/**
		取消流程提交或回退操作
	**/

	cancelAction : function(){
		history.go(-1);
	},
	
	/*
	*	子流程节点选择审批人	
	*/       
	showUserSelectOnSubFlow : function (actionName, settings){
		var appId =  jQuery("input[activityType='5']").attr("applicationid");
		var url = contextPath + '/portal/phone/user/selectApprover4Subflow.jsp?application='+appId;
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
			title: '用户选择',
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
		var url = contextPath + '/portal/phone/resource/component/dialog/selectUserByAll.jsp?application='+appId;
		//var url = contextPath + '/portal/phone/user/selectbyflow.jsp?application='+appId;
		url += "&docid=" + settings.docid + "&nodeid=" + settings.nextNodeId+"&flowid="+ settings.flowid +'&flow=true';
		var valueField = document.getElementById(settings.valueField);
		var value = "";
		if (valueField){
			value = valueField.value;
		}
		
		var ids = document.getElementById(settings.hiddenIds).value;
		OBPM.dialog.show({
			width: 640,
			height: 480,
			url: url,
			//args: {parentObj: window, idField: "submitTo", nameField: settings.textField, readonly: settings.readonly},
			args: {parentObj: window, value: value, readonly: settings.readonly,"applicationid":appId,"defValue":ids},
			title: '用户选择',
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

function BackAction(){
	if(location.hash != "#flowPro"){
		$("#flowView").removeClass("active");
		$("body").removeClass("test-body");
	}
}

window.onhashchange = BackAction;

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
 
function ev_validation(el, actid) {
	var nextids = document.getElementsByName('_nextids');
	var flag = false;
	
	makeAllFieldAble();
	
	if (nextids != null) {
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
			document.forms[0].action='<s:url action="action" namespace="/portal/dynaform/activity" />?_activityid=' + actid;
			document.forms[0].submit();
		}
		else {
			alert('{*[page.workflow.chooseaction]*}');
		}
	}
	else {
		alert ('{*[page.workflow.noaction]*}');
	}
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
 
var $sigdiv;

jQuery(document).ready(function(){
	if($("input[activityType='5']").size()==0) return;
	FlowPanel.refreshFlowPanel("init");
	$sigdiv = $("#signature").jSignature({'UndoButton':false,width:$(window).width()-26,height:$(window).height()-300,color:'#000',lineWidth:0});
	
	var obj = document.getElementById('signature'); 
	obj.addEventListener("touchstart",function(e){
		e.preventDefault();
		e.stopPropagation();
	},false);

});

var circulatorInfos =[];
/**
*指定抄送人
*
**/
function selectCirculator(actionName, settings) {
	var url = contextPath + '/portal/phone/user/selectCirculatorByFlow.jsp?application='+application;
	url += "&docid=" + settings.docid + "&nodeid=" + settings.nextNodeId+"&flowid="+ settings.flowid;
	var valueField = document.getElementById(settings.valueField);
	var value = "";
	if (valueField){
		value = valueField.value;
	}
	OBPM.dialog.show({
		width: 610,
		height: 450,
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
		title: '用户选择',
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
}

function checkIsSelectCirculator(){
	var obj = document.getElementById("_circulator");
	var _flowType = document.getElementById("_flowType").value;
	/*if(obj && _flowType != '81'){
		if(obj.value.length<=0){
			alert("请选择抄送人！");
			return false;
		}
	}*/
	return true;
}


//流程回撤
function doRetracement() {
	//ignoreFormCheck();
	var action = contextPath + '/portal/dynaform/document/retracement.action';
	
	document.getElementsByName("_flowType")[0].value = "retracement";
	document.forms[0].action = action;
	makeAllFieldAble();
	document.forms[0].submit();
}


function showbig(obj){
	var showPicPanel = "<div class='showPicPanel' style='text-align: center;padding-top: 50%;position: fixed;top: 0px;left: 0px;right: 0px;bottom: 0px;background-color: rgba(0, 0, 0, 0.48);z-index: 100;'><img src='' onclick='hideBig(this);' style='background:#fff'></div>";
	$("#viewHistory").append(showPicPanel);
	$(".showPicPanel").find("img").attr("src",$(obj).attr("src"));
	
}
function hideBig(obj){
	$("#viewHistory").find(".showPicPanel").remove();
}
