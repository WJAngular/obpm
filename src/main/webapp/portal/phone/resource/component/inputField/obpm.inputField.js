(function($){
	$.fn.obpmInputField =function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id  = $field.attr("_id");
			var name = $field.attr("_name");
			var textType = $field.attr("_textType");
			var isBlank = $field.attr("_isBlank");
			var isBorderType = $field.attr("_isBorderType");
			var fieldType = $field.attr("_fieldType");
			var fieldKeyEvent = $field.attr("_fieldKeyEvent");
			var displayType = $field.attr("_displayType");
			var hiddenValue = $field.attr("_hiddenValue");
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			var cssClass = $field.attr("_cssClass");
			var title = $field.attr("_title");
			var style = $field.attr("style");
			var subGridView = $field.attr("_subGridView");
			var discript = $field.attr("_discript");
			
			isBlank = (isBlank == "true");
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			var value = $field.attr("value");
			value = value ? value : "";
			discript = discript? discript : name;
			
			var html = "";
			if(displayType == PermissionType_HIDDEN || textType.toLowerCase() == "hidden"){
				html="<input type='hidden' name='" + name + "' id='" + id + "' value='" + value + "' />";
				this.parentNode.innerHTML = html;
			}else{
				if(value && value != ""){
					value = value.replace(/"/g, "&quot;") ;//替换空格符
				}else{
					if(fieldType == "VALUE_TYPE_NUMBER"){
						value = "0";
					}
				}
				if(!title){
					title = "";
				}else if(title != "")
					title = title.replace(/"/g, "&quot;") ;//替换空格符
				



				html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";	//文本类型
				
				if(textType == "tel" && value.length>0){
					html +="<table width=\"100%\"><tr><td width=\"95%\">";
				}
				html += "<input class='contactField requiredField' type=";	//文本类型
				if(!isBlank){
					if(textType.toLowerCase() == "password"){
						html+="\"password\"";
					}else if(fieldType == "VALUE_TYPE_NUMBER"){	// 类型为数字时
						html+="\"number\"";
					}else if(textType == "tel"){
						html+="\"text\"";
					}else{
						html+="\"text\"";
					}
				}else{
					html+="\"text\"";
				}
				
				
				
				if(textType == "tel" && value.length>0){
					html+=" id=\"" + name + "\" name=\"" + name + "\" value=\"" + value + "\" style=\"width: 100%; height: 32px; border: 1px solid rgba(0,0,0,.2); border-radius:3px;\" data-enhance=\"false\"";
				}else if(textType == "tel"){
					html+=" id=\"" + name + "\" name=\"" + name + "\" value=\"" + value + "\" style=\"background:url(../../phone/resource/main/images/tel.png) no-repeat;background-position:right center; background-size:28px 28px;\" data-enhance=\"false\"";
				}else{
					html+=" id=\"" + name + "\" name=\"" + name + "\" value=\"" + value + "\" data-enhance=\"false\"";
				}
				
				if(isRefreshOnChanged){
					if(subGridView){
						html += " onchange=\"dy_view_refresh(this.id)\"";
					}else{
						html += " onchange=\"dy_refresh(this.id)\"";
					}
				}
				
				html+=" /></div>";
				if(textType == "tel" && value.length>0){
					html+="</td><td width=\"5%\"><a href=\"tel:" + value + "\"><span style=\"width:32px; height:32px; margin:5px; display:block;background:url(../../phone/resource/main/images/tel.png) no-repeat;  background-size:32px 32px;\"></span></a></td></tr></table>";
				}
				
				var $html = $(html);
				
				if(textType.toLowerCase() == "readonly" || displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED){
					if(isBorderType == "true"){
						$html.find("input").attr("type","hidden").css("display", "none");
						$html.append("<div id='"+ name +"_show' class='showItem'>" + value + "</div>");
					}else{
						$html.find("input").attr("readonly","readonly").css({"background-color":"rgb(239, 239, 239)","color":"#333"})//只读
					}
				}
				$html.bind("change",function(){
					if(isRefreshOnChanged){
						if(subGridView){
							dy_view_refresh(this.id);
						}else{
							dy_refresh(this.id);
						}
					}
				}).replaceAll($field);//.textinput();
			}
		});
	};
})(jQuery);