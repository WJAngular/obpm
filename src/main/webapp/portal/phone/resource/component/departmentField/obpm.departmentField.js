(function($){
	$.fn.obpmDepartmentField = function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id=$field.attr("_id");
			var name=$field.attr("_name");
			var fieldType=$field.attr("_fieldType");
			var classname=$field.attr("classname");
			var cssclass=$field.attr("class");
			var isRefreshOnChanged=$field.attr("_isRefreshOnChanged");
			var innerhtml = $field.html();
			var style=$field.attr("style");
			var displayType=$field.attr("_displayType");
			var textType=$field.attr("_textType");
			var subGridView = $field.attr("_subGridView");
			var hiddenValue = $field.attr("_hiddenValue");
			var discript = $field.attr("_discript");

			discript = discript ? discript : name;
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");
			
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			var html = "<div class='formfield-wrap' ";

			if(displayType == PermissionType_HIDDEN || textType.toLowerCase() == "hidden"){
				html += " style='display:none;' ";
			}
			html += ">";
			var htmlShow = "";
			//if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性			
				html += "<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";
				html += "<div class='select-box' ";

				if(textType.toLowerCase() == "readonly" || displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED){
					html += " style='display:none'";
					htmlShow += "<div id='"+ name +"_show' class='showItem'>" + $field.find("option:selected").text() + "</div>";
				}
				html += "><select class='contactField requiredField' id ='"+id+"' fieldType='"+fieldType+"'";
				
				html += " style=\"";	//style
				if(style) html += style + ";";
				html += "\"";
				
				html += "' classname='"+classname+"' class='contactField requiredField " + cssclass + "' name='"+name+"' ";
				html+=" isRefreshOnChanged='" + isRefreshOnChanged + "'" + otherAttrsHtml;
				
				if (isRefreshOnChanged){
					if(subGridView){
						html += " onchange='dy_view_refresh(this.id)'";
					}else{
						html += " onchange='dy_refresh(this.id)'";
					}
				}
				html+=">";
				html += "" + innerhtml;
				html += "</select><i class='icon icon-down'></i></div>" + htmlShow + "</div>";
				if(displayType == PermissionType_HIDDEN){
					html += "<span>"+ hiddenValue +"</span>";
				}
			//}
			this.parentNode.innerHTML = html;	
		});
	};
})(jQuery);