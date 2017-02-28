/**
 * 保存并返回操作
 * 
 */
function SaveNewBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 10;
	this.withOld = params.withOld;//是否带旧数据
	
	
	if(typeof SaveNewBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveNewBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveNewBtn.prototype.getActionPostData = function(){
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
		SaveNewBtn.prototype.doBefore = function(){
			var flag = true;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			//flag = ifSubSaveForm();
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveNewBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveNewBtn.prototype.doAfter = function(result){
			
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/newDocument.action?_withOld='+this.withOld,
				dataType : 'json',
				timeout: 3000,
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
						dy_lock();//显示loading层
						//渲染流程提交按钮
						if($("input[activityType='5']")){
							FlowPanel.refreshFlowPanel("init");
						}
						//填充系统字段
						if(datas["systemFields"]){
							for(var n in datas["systemFields"]){
								$("input[name='"+n+"']").val(datas["systemFields"][n]);
							}
						}
						//渲染流程处理人列表、流程状态标签、流程历史按钮
						$("#processorHtml").html('');
						$("#flowStateHtml").html('');
						jQuery(".flowbtn").hide();
						initFormCommon();//表单公用的初始化方法
//						adjustDocumentLayout4form();//调整相关文档布局
						dy_unlock();//隐藏loading层
					}else{
						Activity.showMessage(result.message,"error");
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.readyState == 0){
						Activity.showMessage("网络已断开！","error");
					}else{
						Activity.showMessage(errorThrown.message,"error");
					}
				}
			});
			
		}
		
		SaveNewBtn._initialized = true;
	
	}
	
	
	
	return this;
}