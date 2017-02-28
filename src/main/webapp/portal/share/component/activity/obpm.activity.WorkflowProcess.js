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
			dy_lock();
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			jQuery("iframe[name='display_view']").trigger("save");
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
			var flag = true;
			
			return flag;
		}
		/**
		 * 按钮动作时执行的其他业务操作
		 */
		WorkflowProcess.prototype.doAction = function(){
			doWordSave();
			return true;
		}
		
		/**
		 * 按钮动作执行后的其他业务操作
		 */
		WorkflowProcess.prototype.doAfter = function(result){
			var backUrl = $("#_backURL").val();
			var opentype = $("input[name='openType']").val();
			if(backUrl && backUrl.length>0){
				setTimeout(function(){
					window.location.href = backUrl;
				}, 3000);
			}
			try {
				if("277"==opentype || "16"==opentype){//弹出层打开
					Activity.closeWindow();
				}else if(!backUrl || backUrl.length<=0){
					if(typeof(parent.closeActiveTab) == "function"){
						parent.closeActiveTab();
					}else{
						window.close();
					}
				}
			} catch (e) {
				console.log("WorkflowProcess.doAfter-->" + e);
			}
		}
		
		WorkflowProcess._initialized = true;
	
	}
	
	
	
	return this;
}