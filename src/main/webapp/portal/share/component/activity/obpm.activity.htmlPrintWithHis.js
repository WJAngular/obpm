/**
 * 网页打印(带流程历史)操作
 * 
 */
function HtmlPrintWithHisBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 14;
	
	
	if(typeof HtmlPrintWithHisBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		HtmlPrintWithHisBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		HtmlPrintWithHisBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		HtmlPrintWithHisBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		HtmlPrintWithHisBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		HtmlPrintWithHisBtn.prototype.doAfter = function(result){
			var withFlowHis = true;
			var id = document.getElementsByName("content.id")[0].value;
			var applicationid = document.getElementsByName("content.applicationid")[0].value;
			var formid = document.getElementsByName("_formid")[0].value;
			var flowid = document.getElementsByName("_flowid")[0].value;
			var _templateForm = document.getElementsByName("_templateForm")[0].value;
			var signatureExist = document.getElementById("signatureExist").value;
			
			var url = contextPath + '/portal/dynaform/activity/print.action?_docid=' + id;
			url += '&_formid=' + formid;
			url += '&_signatureExist=' + signatureExist;
			url += '&_activityid=' + this.actId;
			url += '&application=' + applicationid;
			if (withFlowHis && flowid) {
				url += '&_flowid=' + flowid;
			}
			if(_templateForm){
				url += '&_templateForm=' + _templateForm;
			}
			if (parent != top) {
				parent.open(url);
			} else {
				window.open(url);
			}
		}
		
		HtmlPrintWithHisBtn._initialized = true;
	
	}
	
	
	
	return this;
}