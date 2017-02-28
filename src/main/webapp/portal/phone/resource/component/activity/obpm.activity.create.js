/**
 * 新建操作
 * 
 */
function CreateBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 2;
	this.viewType = params.viewType;
	this.openType = params.openType;
	this.formId = params.formId;
	this.target = params.target;
	
	if(typeof CreateBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		CreateBtn.prototype.getBeforePostData = function(){
			if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
				//var application = $("input[name='application']").val();
				//return $(".control-content.active").find("input,textarea,select").serialize()+"&application="+application;
				return DisplayView.span2Params(this.target);
			}else{
				return jQuery(document.forms[0]).serialize();
			}
			
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		CreateBtn.prototype.getActionPostData = function(){
			if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
				var application = $(".control-content.active").find("span[name='application']").text();
				//return $(".control-content.active").find("input,textarea,select").serialize()+"&application="+application+"&formId="+this.formId;
				var view = DisplayView.getTheView(this.target);
				var json = DisplayView.span2Json(this.target);
				json['formId'] = this.formId;
				var elems = $(view).find("[name=_selects]:checked");
				json['_backURL'] = '';
				return DisplayView.addVal2Params(json,elems);
			}else{
				return jQuery(document.forms[0]).serialize()+"&formId="+this.formId;
			}
			
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		CreateBtn.prototype.doBefore = function(){
			//createDoc(this.actId);
			//if(this.openType==288 && toggleButton("button_act")) return false;
			/**
			if(this.viewType!=1){
				doNew(this.actId);
			}
			**/
			//doNew(this.actId);
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		CreateBtn.prototype.doAction = function(){
			if(this.openType==288){//网格显示
				jQuery("#VIEW_OPEN_TYPE_GRID_BTN").css("display","");
	        	DocumentUtil.doNew(this.actId, getGridParams(), function(rtn) {
	        				var oTR = eval(rtn);
	        				if (oTR) {
	        					insertRow(oTR[0], oTable.rows.length);
	        					newRows.push(oTR.attr("id"));
	        					doRowEdit(oTR.attr("id"));
	        				}

    						jQuery("#" + oTR.attr("id")).bind('change',function(){
    							setFieldValChanged(true);
    						}).bind("keydown",function(e){
    							 var key = e.which;
    							 if (this.type != "textarea" && key == 13) {
    							 	e.preventDefault(); //按enter键时阻止表单默认行为
    							 }
    						});
	        				
	        				//jquery重构按钮和控件
	        				jqRefactor();
	        				ev_resize4IncludeViewPar(); // 调整高度
	        			});
	        	return false;
			}
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		CreateBtn.prototype.doAfter = function(result){
			// View的openType(打开类型)
			var url = contextPath + "/portal/dynaform/activity/process.action?_activityid="+this.actId+"&_formid="+this.formId;
			if(this.params.parameter){
				url += "&content.id="+this.params.parameter;
			}
			if (this.openType == 17) {
				var isRelate = document.getElementsByName("isRelate");
				url += "&isSubDoc=true";
				if (isRelate){
		            if (isRelate.length>0){
		            	var relate = isRelate[0].value;
		            	url += "&isRelate="+relate;
		            }
				}
			}
		
			if (document.getElementsByName("_openType")[0]) {
				if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
					this.openType = document.getElementsByName("_openType")[0].innerText;
				}else{
					this.openType = document.getElementsByName("_openType")[0].value;
				}
			}
		
			var parameters = getQueryString(this.openType);
		
			resetBackURL(); // view.js
		
			if (this.openType == 1) {
				if (isHomePage()) { // 首页单独处理
					url += "&_backURL="
							+ encodeURIComponent(contextPath
									+ "/portal/dispatch/homepage.jsp");
					url += "&" + parameters;
					parent.location = appendApplicationidByView(url);
				} else {
					document.forms[0].action = url;
					document.forms[0].submit();
				}
			} else if (this.openType == OPEN_TYPE_POP || this.openType == OPEN_TYPE_DIV) {
				var oSelects = document.getElementsByName("_selects");
				var _selects="";
				if(oSelects){
					for(var i=0;i<oSelects.length;i++){
						if(oSelects[i].checked){
							_selects+="&_selects="+oSelects[i].value;
						}
					}
				}
				url += "&" + parameters + "&openType=" + this.openType+_selects;
				url = appendApplicationidByView(url);
				
				$("#document_content").hide();
				$("#myModalexample").find(".content").css({"position":"static","height":$(window).height()-44})
				$("#myModalexample").find(".content>iframe").css({"position":"absolute","z-index":"1","top":"44px","height":$(window).height()-44})
				
				// var w = document.body.clientWidth - 25;
				// showfrontframe({
				// 			title : "",
				// 			url : url,
				// 			w : w,
				// 			h : 30,
				// 			windowObj : window.parent,
				// 			callback : function(result) {
				// 				setTimeout(function(){//通过右上角的关闭图标关闭弹出层后会显示加载进度条，加延迟后没有这个问题
				// 					document.forms[0].submit();
				// 				},1);
				// 			}
				// 		});
				MyPopup.open({
					url:url,
					title:"新建",
					success:function(){
						$("[name='_fieldid']").each(function(){
							$("#document_content").show();
							$("#"+$(this).text()).trigger("refresh");
						})
					}
				});
				

			} else if (this.openType == OPEN_TYPE_OWN) {
				var id = document.getElementsByName("_viewid")[0].value;
				if (this.openType == VIEW_TYPE_SUB) {
					var sub_divid = parent.document.getElementById('sub_divid');
					var doc_obj = parent.document.getElementById(id);
					if (sub_divid) {
						sub_divid.value = doc_obj.src;
					}
		//			if (doc_obj) {
						//url += "&" + parameters;
						//doc_obj.src = url;
						document.forms[0].action = url;
						document.forms[0].submit();
		//			}
				} else {
					document.forms[0].action = url;
					document.forms[0].submit();
				}
		
			} else {
				if(toggleButton("button_act")) return false;
			}
		}
		
		CreateBtn._initialized = true;
	
	}
	
	
	
	return this;
}