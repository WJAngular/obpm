(function($){
	$.fn.obpmIncludedView = function(){
		return this.each(function(){
			if(!HAS_SUBFORM){
				HAS_SUBFORM=true;
				$("#form_tab").append('<a class="control-item swiper-slide active" href="#item1mobile" style="width:auto;">基本信息</a>')
				.show();
				$("#document_content").css("margin-top","36px");
			}
			var $field = jQuery(this);
			var id = $field.attr("id");
			var isRefreshOnChanged = $field.attr("isRefreshOnChanged");
			var getName = $field.attr("getName");
			var userType = $field.attr("userType");
			var action = $field.attr("action");
			var application = $field.attr("application");
			var _viewid = $field.attr("_viewid");
			var _fieldid = $field.attr("_fieldid");
			var _opentype = 227;//$field.attr("_opentype");
			var isRelate = $field.attr("isRelate");
			var parentid = $field.attr("parentid");
			var divid = $field.attr("divid");
			var getEditMode = $field.attr("getEditMode");
			var fixation = $field.attr("fixation");
			var fixationHeight = $field.attr("fixationHeight");
			if(fixation){
				height = "height:" + fixationHeight + ";";
			}else {
				height = "";
			}
			var $tabcontent = $field.parents(".tabcontent");
			if($tabcontent.html()){
				var istab = "true";
				var formid = $tabcontent.attr("formid");
				var tabVisibleStyle = "";
				if($tabcontent.is(":visible")){
					tabVisibleStyle = "display:block;";
				}else{
					tabVisibleStyle = "display:none;";
				}
			}else{
				var istab = "false";
			}		
			
			userType = (userType == "true");
			getEditMode = (getEditMode == "true");
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			var html="";
				html+="<div id='" + _fieldid + "' isRelate='" + isRelate + "' istab='"+ istab +"' getName='" + getName + "' fixation='" + fixation + "' fixationHeight='" + fixationHeight 
					+ "' name='display_view' width='100%' height='100%' frameborder='0' style='' ";
				if(userType){
					html+=" src='../view/preView.action";//改成相对路径 --Jarod
					html+="?application=" + application + "";
					html+="&_skinType=" + $field.attr("skinType") + "";
				}else{
					html+=" src='../view/";
					html+="" + action + "";
					html+="?application=" + application + "";//改成相对路径 --Jarod
				}
				html+="&_viewid=" + _viewid + "";
				html+="&_fieldid=" + _fieldid + "";
				html+="&_opentype=" + _opentype + "";
				html+="&parentid=" + parentid + "";
				html+="&isRelate=" + isRelate + "";
				html+="&divid=" + divid + "";
				html+="&_from=includedView";
				if(getEditMode){
					html+="&isedit=false";
				}
				if(isRefreshOnChanged){
					html+="&refreshparent=true";
				}
				html+="'";
				html+=" isRefreshOnChanged='" + isRefreshOnChanged + "'";
				html+=" style='" + height + "overflow:auto;'></div>";
				
			var $html = $(html);
			var $tab = $("#tab_"+_fieldid);
			if($tab.length==0){
				$tab = $('<span id="tab_'+_fieldid+'" class="control-content"></span>');
				var $idTabInclude = $field.parent();
				if($idTabInclude.parent(".tabcontent").size()<=0){
					$("#form_tab").append('<a _tid='+formid+' class="control-item swiper-slide" href="#tab_'+_fieldid+'" style="width:auto;'+tabVisibleStyle+'">'+getName+'</a>');	
				}else{
					if($idTabInclude.parent().attr("ishidden")=="false"){
						$("#form_tab").append('<a _tid='+formid+' class="control-item swiper-slide" href="#tab_'+_fieldid+'" style="width:auto;'+tabVisibleStyle+'">'+getName+'</a>');	
					}
				}
				$("#form_continer").append($tab);
			}
			$html.load($html.attr("src"), function(){
				var $tabParameter = $(".tab_parameter");
				var viewid = _fieldid + "_params";
				var $viewDiv = $("<div></div>").attr("id",viewid).attr("_action",$(this).attr("src")).css("display","none");
				//表单元素转成span标签存入dom中
				$.each($(this).find(".tab_parameter").find("input[type!=button],select,textarea").serializeArray(),function(){
					$viewDiv.append("<span name='" + this.name + "'>" + this.value + "</span>"); 
				});
				$(this).append($viewDiv);
				//去掉表单元素
				$tabParameter.find("input[type!=button],select,textarea").each(function(){
					if(!$(this).attr("moduleType")) $(this).remove();
				});
				jqRefactor();					
			});
			
			$html.bind("refresh", function(){
				$html.load($html.attr("src"), function(){
					//alert("view refresh complated");
					var $tabParameter = $(".tab_parameter");
					var viewid = _fieldid + "_params";
					var $viewDiv = $("<div></div>").attr("id",viewid).attr("_action",$(this).attr("src")).css("display","none");
					//表单元素转成span标签存入dom中
					$.each($(this).find(".tab_parameter").find("input[type!=button],select,textarea").serializeArray(),function(){
						$viewDiv.append("<span name='" + this.name + "'>" + this.value + "</span>"); 
					});
					$(this).append($viewDiv);
					//去掉表单元素
					$tabParameter.find("input[type!=button],select,textarea").each(function(){
						if(!$(this).attr("moduleType")) $(this).remove();
					});
					dy_refresh(name);		
				});
			});
			$tab.html($html);
			$field.remove();
			
			

			var tabBox_width = 0;
			var tab_num = $("#form_tab").find(".control-item:visible").size();
			
			
			
			for(var i=0;i < tab_num;i++){
				$(".segmented-control").removeAttr("style");
				$("#form_tab").find(".control-item:visible:eq("+i+")").css({"width":"auto","display":"block"});
				tabBox_width = $("#form_tab").find(".control-item:visible:eq("+i+")").outerWidth()+tabBox_width;
			}
			
			$(".tab-box").width($(window).width());
			$(".segmented-control").removeAttr("style");
			if(tabBox_width <= $(window).width()){
				$(".segmented-control").css("display","table");
				$("#form_tab .control-item:visible").css("display","table-cell");
			}else{				
				$(".segmented-control").width(tabBox_width);
				$("#form_tab .control-item:visible").css("display","block");
				var swiper = new Swiper('.swiper-container', {
			        slidesPerView: 'auto',
			        spaceBetween: 0,
			        freeMode: true
			    });	
			}
			if($("#form_tab .control-item:visible").size()<=1){
				$("#form_tab").find("[_tid]").hide();
				$("#form_tab").find("a[href='#item1mobile']").hide();
				$("#document_content").css("margin-top","0px")
			}
			
		});
		
	};
})(jQuery);