var historyRecory = history.length;

/**
 * 流程处理
 * @author Happy
 */
function WorkflowProcess(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 5;
	
	
	if(typeof WorkflowProcess._initialized == "undefined"){
		
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		WorkflowProcess.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		WorkflowProcess.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		WorkflowProcess.prototype.doBefore = function(){
			showLoadingToast();
			setHTMLValue();
			var flag = true;
			return flag;
		}
		/**
		 * 按钮动作时执行的其他业务操作
		 */
		WorkflowProcess.prototype.doAction = function(){
			//doWordSave();
			return true;
		}
		
		/**
		 * 按钮动作执行后的其他业务操作
		 */
		WorkflowProcess.prototype.doAfter = function(result){
			hideLoadingToast();
			var backUrl = $("#_backURL").val();
			var openType = $("#openType").val();
			if($("#openType").val()=="from_weixin_message"){//从微信待办消息中打开
				if (typeof WeixinJSBridge == "undefined"){
				    if( document.addEventListener ){
				        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
				    }else if (document.attachEvent){
				        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
				        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
				    }
				}
				try {
					WeixinJSBridge.invoke('closeWindow',{},function(res){
						//alert('关闭微信页面');
					});
				} catch (e) {
				}
				
			}else if(backUrl && backUrl.length>0){
				setTimeout(function(){
					window.location.href = backUrl;
				}, 3000);
			}
			else if(GetReferrer()==""){//判断来路为空
				try {
					WeixinJSBridge.invoke('closeWindow',{},function(res){
						//alert('关闭微信页面');
					});
				} catch (e) {
					window.close();
				}
			}else if($("#myModalexample",parent.document).hasClass("active")){
				parent.MyPopup._modal.trigger("close");
				return;
			}
			else{
				var historyRecory2 = history.length;
				var backNum = -2;
				if(historyRecory2-historyRecory > 1){
					backNum = -(historyRecory2-historyRecory+1);
				}
				history.go(backNum);
			}
		}
	}
		
		WorkflowProcess._initialized = true;
	
	
	
	
	return this;
}

function GetReferrer() {//获取来路URL
	var ref = '';  
		if (document.referrer.length > 0) {  
			ref = document.referrer;  
		}  
	try {  
		if (ref.length == 0 && opener.location.href.length > 0) {  
			ref = opener.location.href;  
		}  
	} catch (e) {} 
	return ref;
}

