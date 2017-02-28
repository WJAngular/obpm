/**
 * 无类型操作
 * 
 */
function NoneBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 13;
	this.afterActionType = params.afterActionType;
	this.afterActionDispatcherUrlScript = params.afterActionDispatcherUrlScript;
	
	if(typeof NoneBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		NoneBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		NoneBtn.prototype.getActionPostData = function(){
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
		NoneBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		NoneBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		NoneBtn.prototype.doAfter = function(result){
			switch (this.afterActionType) {
			case 0:  //无
				break;
			case 1:  //返回
				Activity.back(this);
				break;
			case 2:   //关闭
				var opentype = $("input[name='openType']").val();
				if("277"==opentype || "16"==opentype){//弹出层打开
					Activity.closeWindow();
				}else{
					Activity.back(this);
				}
				break;
			case 3:   //跳转
				window.location.href = this.afterActionDispatcherUrlScript;
				break;
			default:
				break;
			}
		}
		
		NoneBtn._initialized = true;
	
	}
	
	
	
	return this;
}