(function($){
	$.fn.obpmTextareaField =function(){
		return this.each(function(){
			var $field = jQuery(this);
			var id =$field.attr("_id");
			var value =$field.val();
			var name =$field.attr("_name");
			var cssClass =$field.attr("_cssClass");
			var classname =$field.attr("classname");
			var isRefreshOnChanged =$field.attr("_isRefreshOnChanged");
			var fieldType =$field.attr("_fieldType");
			var isBorderType =$field.attr("_isBorderType");
			var style=$field.attr("style");
			var displayType = $field.attr("_displayType");
			var hiddenValue = $field.attr("_hiddenValue");
			var subGridView = $field.attr("_subGridView");
			var discript = $field.attr("_discript");
			
			value = value ? value : "";
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			discript = discript? discript : name;
			
			var html="";
			var height = "";
			var width = "";
			if(displayType == PermissionType_HIDDEN){
				html += "<textarea style='display:none' name='" + name + "'>" + value + "</textarea>";
			}else if(displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED) {
				html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";	//文本类型
				if(isBorderType == "true"){
					html += "<div id='"+ name +"_show' class='showItem'>" + value + "</div>";
				}
				
				html+= "<textarea rows='5' class='contactField requiredField' readonly='readonly' style='";
				if(isBorderType == "true"){
					html += ";display:none;"; 
				}
				html += "background-color:#fdfdfd;color: #333;' name='" + name + "' id='" + name + "' data-enhance='false'>" + value + "</textarea></div>";
			}else{
				html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";	//文本类型
				html+="<textarea rows='5' class='contactField requiredField' ";
				html+="id=\"" + name + "\" name=\""+name+"\" ";
				if(isRefreshOnChanged){
					if(subGridView){
						html += " onchange=\"dy_view_refresh(this.id)\"";
					}else{
						html += " onchange=\"dy_refresh(this.id)\"";
					}
				}
				html+=" data-enhance='false'>";
				html+=value;
				html+="</textarea></div>";
			}
			$field.after(html);
			$field.remove();
		});
	};
})(jQuery);