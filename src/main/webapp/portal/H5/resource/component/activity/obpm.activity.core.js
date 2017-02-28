/**
 * 操作按钮执行框架
 * <p>框架主要封装操作按钮的执行流程，和一些通用操作接口，在执行过程中，具体的行为实现由具体的按钮类型实例完成</p>
 * @author Happy
 */
var Activity ={
		
		_LOCK:false,//操作按钮触发锁，用于避免多次触发执行
		/**
		 * 触发按钮点击动作
		 * @param actId
		 * 		操作id
		 * @param actType
		 * 		操作类型代码
		 * @param params
		 * 		额外参数设置
		 */
		doExecute:function(actId,actType,params){
			var activityType = null;//操作类型实例
			switch (actType) {
			case 1://载入视图
				activityType = new QueryBtn(actId,params);
				break;
			case 2://新建
				activityType = new CreateBtn(actId,params);
				break;
			case 3://删除
				activityType = new DeleteBtn(actId,params);
				break;
			case 4://保存并启动流程
				activityType = new SaveStartWorkflow(actId,params);
				break;
			case 5://流程处理
				activityType = new WorkflowProcess(actId,params);
				break;
			case 8://关闭窗口
				activityType = new CloseWindowBtn(actId,params);
				break;
			case 9://保存并关闭窗口
				activityType = new SaveCloseWindowBtn(actId,params);
				break;
			case 10://返回
				activityType = new BackBtn(actId,params);
				break;
			case 11://保存并返回
				activityType = new SaveBackBtn(actId,params);
				break;
			case 13://无类型
				activityType = new NoneBtn(actId,params);
				break;
			case 14://html打印
				activityType = new HtmlPrintBtn(actId,params);
				break;
			case 16://导出excel
				activityType = new ExportToExcelBtn(actId,params);
				break;
			case 15://html打印(带流程历史)
				activityType = new HtmlPrintWithHisBtn(actId,params);
				break;
			case 18://清空数据
				activityType = new ClearAllBtn(actId,params);
				break;
			case 19://保存草稿（不校验）
				activityType = new SaveWithoutValidateBtn(actId,params);
				break;
			case 20://批量提交
				activityType = new BatchApproveBtn(actId,params);
				break;
			case 21://保存并复制
				activityType = new SaveCopyBtn(actId,params);
				break;
			case 25://导出pdf
				activityType = new ExportToPdfBtn(actId,params);
				break;
			case 26://文件下载
				activityType = new FileDownloadBtn(actId,params);
				break;
			case 27://文件下载
				activityType = new ExcelImportBtn(actId,params);
				break;
			case 28://电子签章
				activityType = new SignatureBtn(actId,params);
				break;
			case 29://批量电子签章
				activityType = new BatchSignatureBtn(actId,params);
				break;
			case 30://自定义打印（套打）
				activityType = new FlexPrintBtn(actId,params);
				break;
			case 33://启动流程
				activityType = new StartWorkflow(actId,params);
				break;
			case 34://保存
				activityType = new SaveBtn(actId,params);
				break;
			case 36://打印视图
				activityType = new PrintViewBtn(actId,params);
				break;
			case 37://通过手机邮件转发
				activityType = new TranspondBtn(actId,params);
				break;	
			case 42://保存并新建
				activityType = new SaveNewBtn(actId,params);
				break;
			case 43://跳转
				activityType = new JumpToBtn(actId,params);
				break;
			case 45://归档
				activityType = new ArchiveBtn(actId,params);
				break;
			default:
				
				break;
			}
			if(!activityType) return;
			Activity.doBefore(activityType);
			
		},
		
		/**
		 * 执行前操作
		 * @param activityType
		 * 		操作类型实例
		 * @returns {Boolean}
		 */
		doBefore : function(activityType){
			if(Activity._LOCK) return;
			
			Activity._LOCK = true;
			setTimeout(function(){
				//一分钟后自动释放锁
				Activity._LOCK = false;
			}, 60*1000);
			if(activityType.doBefore){//按钮动作执行前的准备与校验操作
				if(!activityType.doBefore()) {
					Activity._LOCK = false;
					return;
				}
			}
			
			var flag = false;//执行完脚本后,是否进行下一步提交
			jQuery.ajax({
				type: 'POST',
				async:false, 
				url: contextPath + '/portal/dynaform/activity/runbeforeactionscript.action?_actid=' + activityType.actId,
				dataType : 'text',
				data: activityType.getBeforePostData(),
				success:function(result) {
					if(result != null && result != ""){
						result = result.replace(/\n/g,"<br/>");
						result = result.replace(/\r/g,"<br/>");
			        	var jsmessage = eval("(" + result + ")");
			        	var type = jsmessage.type;
			        	var content = jsmessage.content;
			        	
			        	if(type){
			        		if(type == '16'){
			        			Activity.showMessage(content,"error");
				        		flag = false;
				        	}
				        	
				        	if(type == '32'){
				        		var rtn = window.confirm(content);
				        		if(!rtn){
				        			flag = false;
				        		}else {
				        			flag = true;
				        		}
				        	}
			        	}else {
			        		flag = true;
			        	}
			        	if(flag){
			        		var changedField = jsmessage.changedField;
			        		if( typeof(changedField) != "undefined" && changedField != null && changedField.length > 0){
			        			for(var i=0; i<changedField.length; i++){
			        				var field = changedField[i];
				        			for(var key in field)	{
				        				if(document.getElementsByName(key).length>0){
				        					document.getElementsByName(key)[0].value = field[key];
				        				}
				        			}
			        			}
			        		}
			        	}
					}else {
			        	flag = true;
			        }
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(errorThrown.name == "NetworkError"){
						Activity.showMessage("网络异常","error");
					}else{
						Activity.showMessage(errorThrown.message,"error");
					}
					Activity._LOCK = false;
				}
			});
			
			//return flag;
			if(!flag){
				Activity._LOCK = false;
				dy_unlock();
			}else{
				Activity.doAction(activityType);
			}
			//if(flag) Activity.doAction(activityType);
		},
		/**
		 * 执行按钮操作
		 * @param activityType
		 * 		操作类型实例
		 */
		doAction:function(activityType){
			
			if(activityType.doAction){
				//按钮动作时执行的其他业务操作
				if(!activityType.doAction()) {
					Activity._LOCK = false;
					return;
				}
			}
			
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/execute.action?_activityid=' + activityType.actId,
				dataType : 'json',
				data: activityType.getActionPostData(),
				success:function(result) {
					if(result.status==1){
						if(result.message && result.message.length>3){
							Activity.showMessage(result.message,"info");
						}
						switch (result.data.resultType) {//结果处理类型
						case "form":
							Activity.refreshForm(activityType);
							break;
						case "view":
							Activity.refreshView(activityType);
							break;
						case "back":
							Activity.back(activityType);
							break;
						case "close":
							Activity.closeWindow();
							break;
						case "message":
							if(result.data.resultData.type==16){//alert
								Activity.showMessage(result.data.resultData.content,"error");
							}else if(result.data.resultData.type==32){//confirm
								
							}else if(result.data.resultData.type==1){//success
								Activity.showMessage(result.data.resultData.content,"success");
							}else if(result.data.resultData.type==2){//info
								Activity.showMessage(result.data.resultData.content,"info");
							}else if(result.data.resultData.type==3){//warning
								Activity.showMessage(result.data.resultData.content,"warning");
							}else if(result.data.resultData.type==4){//danger
								Activity.showMessage(result.data.resultData.content,"danger");
							}
							Activity._LOCK = false;
							break;
						case "setParameter"://给activityType传参
							activityType.params.parameter = result.data.resultData;
							break;
						case "validate":
							var fieldErrors = result.data.resultData;
							var content = "";
							for(var name in fieldErrors){
								var fr = fieldErrors[name];
								for(var i in fr){
									var m = fr[i];
									content+=m+";";
								}
							}
							Activity.showMessage(content,"error");
							Activity._LOCK = false;
							return;
							break;
						case "exception":
							Activity.showMessage(result.data.resultData,"error",60*1000);
							Activity._LOCK = false;
							return;
							break;
						default:
							break;
						}
						//按钮动作执行后的其他业务操作
						if(activityType.doAfter && typeof activityType.doAfter == "function"){
							activityType.doAfter(result.data);
						}
					}else{
						Activity.showMessage(result.message,"error");
					}
					setTimeout(function(){
						Activity._LOCK = false;
					},1000);
					
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					Activity.showMessage(errorThrown.message,"error");
					Activity._LOCK = false;
				}
			});
			
		},
		
		/**
		 * 刷新表单
		 * @param activityType
		 * 		操作类型实例
		 */
		refreshForm:function(activityType){
			fieldValChanged = false;	//保存表单后重置表单修改的状态为未修改
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/refreshForm.action',
				dataType : 'json',
				data: jQuery("#document_content").serialize(),
				success:function(result) {
					if(result.status==1){
						var datas = result.data;
						var activityHtml = datas["activityHtml"];
						if(activityHtml){
							$(".formActBtn").html(activityHtml);
						}
						var formHtml = datas["formHtml"];
						if(formHtml){
							$("#_formHtml").html(formHtml)
						}
						//---
						dy_lock();//显示loading层
						try {
							//填充系统字段
							if(datas["systemFields"]){
								for(var n in datas["systemFields"]){
									$("input[name='"+n+"']").val(datas["systemFields"][n]);
								}
							}
							
							//渲染流程处理人列表、流程状态标签、流程历史按钮
							if(datas["processorHtml"]){
								$("#flow-json").html(datas["processorHtml"]);
							}else{
								$("#flow-json").html("[]");
							}
							if(datas["flowStateHtml"]){
								$("#flowStateHtml").html('<div class="flowstate" onmouseover="showFlowState(\'flowstate\');"><b>'+datas["flowStateHtml"]+'</b></div>');
							}
							if(jQuery("input[name='content.stateid']").val().length>0){
								jQuery(".flowbtn").show();
							}
							initFormCommon();//表单公用的初始化方法
							//渲染流程提交按钮
							if($("input[activityType='5']")){
								FlowPanel.refreshFlowPanel("init");
							}
							adjustDocumentLayout4form();//调整相关文档布局
						} catch (e) {
							console.log(e.message);
						}
						
						dy_unlock();//隐藏loading层
						
						//---
						/**
						if(callback && typeof callback == "function"){
							callback(result.data);
						}
						**/
					}else{
						Activity.showMessage(result.message,"error");
						Activity._LOCK = false;
					}
					Activity._LOCK = false;
					
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					Activity.showMessage(errorThrown.message,"error");
					Activity._LOCK = false;
				}
			});
		},
		
		/**
		 * 刷新视图
		 * @param activityType
		 * 		操作类型实例
		 */
		refreshView:function(activityType){
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/refreshView.action',
				dataType : 'json',
				data: jQuery(document.forms[0]).serialize(),
				success:function(result) {
					if(result.status==1){
						var datas = result.data;
						var activityHtml = datas["activityHtml"];
						if(activityHtml){
							//$(".formActBtn").html(activityHtml);
						}
						var viewHtml = datas["viewHtml"];
						if(viewHtml){
							$("#viewHtml").html(viewHtml)
						}
						dy_lock();//显示loading层
						try {
							initListComm();	//列表视图公用初始化方法
							//adjustDataIteratorSize();
							setTimeout(function(){
								listViewAdjustLayout();
							},10);	//调整当前窗口布局
						} catch (e) {
							console.log(e.message);
						}
						dy_unlock();//隐藏loading层
						
						//---
						/**
						if(callback && typeof callback == "function"){
							callback(result.data);
						}
						**/
					}else{
						Activity.showMessage(result.message,"error");
						Activity._LOCK = false;
					}
					Activity._LOCK = false;
					
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					Activity.showMessage(errorThrown.message,"error");
					Activity._LOCK = false;
				}
			});
		},
		/**
		 * 返回
		 * @param activityType
		 * 		操作类型实例
		 */
		back:function(activityType){
			var _time = 0;
			if(activityType.actType == 11){
				_time = 3000;
			}		
			setTimeout(function(){
				var backUrl = $("#_backURL").val();
				if(backUrl && backUrl.indexOf("isSubDoc=true")>0){
					backUrl = undefined;
				}
				if(backUrl && backUrl.length>0){
					window.location.href = backUrl;
				}else{
					var opentype = $("input[name='openType']").val();
					if("277"==opentype || "16"==opentype){//弹出层打开
						Activity.closeWindow();
					}else{
						parent.EDEN.Main.renderTabs.closeTab();
					}
				}
			},_time);
		},
		/**
		 * 关闭窗口
		 */
		closeWindow:function() {
			var backUrl = $("#_backURL").val();
			var opentype = $("input[name='openType']").val();
			if(!opentype || ""==opentype){
				if(backUrl && backUrl.length>0){
					window.location.href = backUrl;
				}
				return;
			}
			var  view_id = $("input[name='view_id']").val();
			if(parent){
			    var sub_divid  =parent.document.getElementById('sub_divid');
				var doc_obj = parent.document.getElementById(view_id);
				if (doc_obj) { // 本区域返回
					doc_obj.src= sub_divid.value;
					parent.ev_reloadParent();
					return;
				}
				
				if(parent.frames['main_iframe']){
					var viewiFrame = parent.frames['main_iframe'].frames['detail'].frames[view_id];
					if(viewiFrame){
						try{
						 	viewiFrame.ev_reload()
						 	hidden();
						}catch(ex){}
					}else{
					  parent.close();
					  parent.parentWindow.ev_reload();
					}
				}else{
				  OBPM.dialog.doExit();
				}
			} else {
				OBPM.dialog.doExit();
			}
		},
		
	    /**
		 * 显示消息提示
		 * @param msg
		 * 		消息内容
		 * @param type
		 * 		消息类型（'info','error'）
		 * @param hideAfter
		 * 		延时几秒后关闭消息窗体
		 */
		showMessage : function(msg, type,hideAfter) {
			
	    	if(!msg) return;
	    	//数据兼容 后面修改消息样式要改  	
	    	if(type =="success" || type=="Confirm"){
	    		type="success";
	    	}else if(type =="Danger" || type=="Alert"){
	    		type="danger";
	    	}
	    	window.showMessage(type,msg);
	    	
	    	/**
		    var type = "undefined" == typeof type ? "info": type,
		    		hideAfter = "undefined" == typeof hideAfter ? 3 : hideAfter;//默认3秒停留时间
		    $._messengerDefaults = {
		        extraClasses: "messenger-fixed messenger-on-top",
		        theme: 'flat',
		        messageDefaults: {
		            showCloseButton: true,
		            hideAfter: hideAfter
		        }
		    },
		    $.globalMessenger().post({
		        message: msg,
		        type: type
		    });
		    **/
		},
		
		makeAllFieldAble :function(elements) {
			var fields = [];
			if (!elements) {
				elements = document.forms[0].elements;
			}
			for (var i = 0; i < elements.length; i++) {
				var element = elements[i];
				if (element.disabled == true) {
					element.disabled = false;
					fields.push(element);
				}
			}
			
			return fields;
		},
		
		setFieldDisabled : function(fields){
			for (var i = 0; i < fields.length; i++) {
				var element = fields[i];
				if (element.disabled == false) {
					element.disabled = true;
				}
			}
			
		},
		
		cache:{
			
		}
		
		
}