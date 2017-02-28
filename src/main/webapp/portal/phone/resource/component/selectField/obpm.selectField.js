(function($) {
	$.fn.obpmSelectField = function() {
		return this.each(function() {
			var $field = jQuery(this);
			var id = $field.attr("_id");
			var name = $field.attr("_name");
			var innerhtml = $field.children().html();
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			var textType = $field.attr("_textType");
			var displayType = $field.attr("_displayType");
			var fieldType = $field.attr("_fieldType");
			var style =$field.attr("style");
			var subGridView = $field.attr("_subGridView");
			var discript = $field.attr("_discript");
			var hiddenValue = $field.attr("_hiddenValue");
			var onchange = $field.attr("onchange");

			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");
			discript = discript? discript : name;
			
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			var html = "";
			var htmlShow = "";
			if(onchange){
				$field.removeAttr("onchange");
				if(onchange.indexOf("'") >= 0 || onchange.indexOf('"') >= 0){
					alert("onchange值不能包含引号！");
					onchange = "";
				}
			}else{
				onchange = "";
			}
			if(subGridView){
				$field.removeAttr("subGridView");
			}

			html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "' >" + discript + "</label>";
			html += "<div class='select-box'";

			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				html += " style='display:none;'";
			}
			html += "><select class='contactField requiredField'";
			html += " id='" + name + "' data-enhance='false' name='" + name + "' fieldType='" + fieldType
					+ "' ";
			if (isRefreshOnChanged) {
				if(subGridView){
					onchange += ";dy_view_refresh(\"" + id + "\")";
				}else{
					onchange += ";dy_refresh(\"" + id + "\")";
				}
			}
			html += " onchange='" + onchange + "'";
			html += " style=\"";	//style
			if(style) html += style + ";";
			if (textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN) {
				html += " display:none;";
			}
			html += "\"";
			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				html += " disabled";
				htmlShow += "<div id='"+ name +"_show' class='showItem'>";
			}
			html += ">";
			//option
			$field.find("span").each(function(){
				$option = jQuery(this);
				var isSelected = $option.attr("isSelected");
				var val = $option.attr("value");
				var text = $option.html();
				var cssClass= $option.attr("cssClass");
				isSelected = isSelected == "true"?true:false;
				val = val?val:"";
				var optHtml = "<option value=\"" + val + "\" class=\"" + cssClass + "\"";
				if(isSelected){
					optHtml += " selected=\"true\"";
					htmlShow += val;
				}
				optHtml += " >" + text + "</option>";
				html += optHtml;
			});
			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				htmlShow += "</div>";
			}
			html += "</select><i class='icon icon-down'></i></div>";
			
			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				html += htmlShow;
			}
			
			html += "</div>";
			if(displayType == PermissionType_HIDDEN){
				html += "<span>"+ hiddenValue +"</span>";
			}
			if (textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN) {
				$field.parent().hide();
			}else{
				$field.parent().show();
			}
			$field.parent().html(html).find("select").css("width","100%");//.css("height","2em");
		});
	};
})(jQuery);