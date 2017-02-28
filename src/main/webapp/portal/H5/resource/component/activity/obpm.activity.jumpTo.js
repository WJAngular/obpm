/**
 * 跳转操作
 * 
 */
function JumpToBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 43;
	this.jumpType = params.jumpType;
	this.targetList = params.targetList;
	this.jumpMode = params.jumpMode;
	this.jumpActOpenType = params.jumpActOpenType;
	this.target = params.target;
	
	
	if(typeof JumpToBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		JumpToBtn.prototype.getBeforePostData = function(){
			if(jQuery("#formList").size()>0){
				return jQuery("#formList").serialize();
			}else{
				return jQuery("#document_content").serialize();
			}
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		JumpToBtn.prototype.getActionPostData = function(){
			if(jQuery("#formList").size()>0){
				return jQuery("#formList").serialize();
			}else{
				return jQuery("#document_content").serialize();
			}
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		JumpToBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		JumpToBtn.prototype.doAction = function(){
			switch (this.jumpMode) {
			case 0 :
				var olist = this.targetList.split(";");
				var formId;
				for (var i = 0; i < olist.length; i++) {
					formId = olist[0].split("|")[0];
				}
				var applicationid = document.getElementById("application").value;
				var docid = document.getElementsByName("content.id")[0].value;
				var view_id = document.getElementById("view_id").value;
				var signatureExist = document.getElementsByName("signatureExist")[0].value;
				var formid = document.getElementsByName("formid")[0].value;
				var backUrl = document.getElementsByName("_backURL")[0].value;
				var docviewAction = contextPath + '/portal/dynaform/document/view.action';
				var newAction = contextPath + '/portal/dynaform/activity/process.action?_activityid=' + this.actId;
				var url = newAction + "&content.id="+docid+"&applicationid=" + applicationid + "&application=" + applicationid + "&_jumpForm=" + formid + "&_formid=" + formId + "&view_id=" + view_id + "&_isJump=1&_backURL="
						+ encodeURIComponent(docviewAction + "?_docid="+docid+"&application="+applicationid+"&_formid="+formid+"&view_id="+
								view_id+"&signatureExist="+signatureExist+"&_backURL="+encodeURIComponent(backUrl));
				switch(this.jumpActOpenType){
				case 0://当前页
					window.location.href = url;
					break;
				case 1://弹出层
					var w = document.body.clientWidth * 80 / 100;
					var h = document.body.clientHeight * 90 / 100;
					showfrontframe({
						title : "",
						url : url,
						w : w,
						h : h,
						windowObj : window.parent,
						callback : function(result) {
						}
					}); 
					break;
				case 2://页签
					parent.addTab(docid,"...",url);
					break;
				case 3://新窗口
					window.open(url);
					break;
				default:
					return false;
				}
				break;
			case 1 :
				var view_id ="";
				var formid ="";
				if(document.getElementById("view_id")){    // 表单
					view_id = document.getElementById("view_id").value;
				}else if(document.getElementById("viewid")){    //视图
					view_id = document.getElementById("viewid").value
				}
				var applicationid = document.getElementById("application").value;
				var docid = document.getElementsByName("content.id")[0].value;
				if(document.getElementsByName("formid")[0]){ //表单
					formid = document.getElementsByName("formid")[0].value;
				}
				
				var url = contextPath + '/portal/dynaform/activity/process.action?_activityid=' + this.actId + "&_formid=" + formid + "&view_id=" + view_id + "&_viewid="+view_id+ "&content.id="+docid+"&applicationid=" + applicationid + "&application=" + applicationid;
				makeAllFieldAble();
				switch(this.jumpActOpenType){
				case 0://当前页
					window.location.href = url;
					break;
				case 1://弹出层
					var w = document.body.clientWidth * 80 / 100;
					var h = document.body.clientHeight * 90 / 100;
					showfrontframe({
						title : "",
						url : url,
						w : w,
						h : h,
						windowObj : window.parent,
						callback : function(result) {
						}
					}); 
					break;
				case 2://页签
					parent.addTab(docid,"...",url);
					break;
				case 3://新窗口
					window.open(url);
					break;
				default:
					break;
				}
				break;
			default :
				return false;
			}
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		JumpToBtn.prototype.doAfter = function(result){
			
		}
		
		JumpToBtn._initialized = true;
	
	}
	
	
	
	return this;
}