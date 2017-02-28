/**
 * 保存操作
 * 
 */
function SaveBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 34;
	
	
	if(typeof SaveBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveBtn.prototype.getBeforePostData = function(){
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
		SaveBtn.prototype.getActionPostData = function(){
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
		SaveBtn.prototype.doBefore = function(){
			var flag = false;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			var retvalue = doWordSave();
			if(!retvalue) {
				alert('Word文档已经被其他用户更新， 请刷新页面加载最新的Word文档！');
				return false;
			}
			var isword = false;

			if (!isword || isword == 'false') {
				flag = true;
			} else {
				if (retvalue >= 0) { // 有返回值才保存
					flag = true;
				}
			}
			
			if(flag){
				flag = ifSubSaveForm();
			}
			
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveBtn.prototype.doAfter = function(result){
			if(result.resultType === "message"){
				var versions = parseInt($("input[name='content.versions']").val());
				$("input[name='content.versions']").val((versions + 1) + "");
			}
		}
		
		SaveBtn._initialized = true;
	
	}
	
	
	
	return this;
}