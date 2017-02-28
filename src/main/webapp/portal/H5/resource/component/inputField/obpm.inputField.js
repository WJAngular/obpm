// 当为数字控件而输入的值不是数字时，重置为上一个值
var origValue = "";
function resetWhenNonNumeric(input){
	var re = /^([\-]{1}[0-9]*|[0-9]*)\.?[0-9]*$/;
	//var re = /[^\-\d\.]/g;
	//var re = /^[\-]?[0-9]?\.?[0-9]*$/;
	//input.value = input.value.replace(re, '');

	if (!re.test(input.value)) {
		for(var i =0;i<input.value.length;i++){
			var s = input.value.charAt(i);
			if(isNaN(s)){	
				break;
			}
		}
		input.value = input.value.substring(0,i);
		return false;
	} else {
		origValue = input.value;
	}
}
	
(function($){
	$.fn.obpmInputField =function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id  = $field.attr("_id");
			var name = $field.attr("_name");
			var textType = $field.attr("_textType");
			var isBorderType = $field.attr("_isBorderType");
			var fieldType = $field.attr("_fieldType");
			var fieldKeyEvent = $field.attr("_fieldKeyEvent");
			var displayType = $field.attr("_displayType");
			var hiddenValue = $field.attr("_hiddenValue");
			var cssClass = $field.attr("_cssClass");
			var title = $field.attr("_title");
			var classname = $field.attr("classname");
			var isCommonFilter = $field.attr("_isCommonFilter");
			isCommonFilter = (isCommonFilter == "true");
			var style = $field.attr("style");
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			var value = $field.attr("value");
			value = value ? value : "";
			var isBlank = $field.attr("_isBlank");
			isBlank = (isBlank == "true");
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			var subGridView = $field.attr("_subGridView");
			subGridView = (subGridView == "true");

			var html = "";
			if(displayType == PermissionType_HIDDEN || textType.toLowerCase() == "hidden"){
				html="<input type='hidden' name='" + name + "' id='" + id + "' value='" + value + "' /><span>"+ hiddenValue +"</span>";
				this.parentNode.innerHTML = html;
			}else{
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性

				if(value && value != ""){
					value = value.replace(/"/g, "&quot;") ;//替换空格符
				}else{
					if(fieldType == "VALUE_TYPE_NUMBER"){
						value = "0";
					}
				}
					
				if(!title){
					title = "";
				}else if(title != ""){
					title = title.replace(/"/g, "&quot;") ;//替换空格符
				}

				html += "<input class='form-control component-input";

				
				html += "' isCommonFilter='"+isCommonFilter+"'";//是否常用搜索
				
				html += "' type=";	//文本类型
				
				if(textType.toLowerCase() == "readonly" || displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED){
					if(isBorderType == "true"){
						html+="\"hidden\"";
					}else{
						html+="\"text\" readonly ";
					}
					//html += " component-input-readonly";
				}else{
					if(!isBlank){
						if(textType.toLowerCase() == "hidden"){
							html+="\"hidden\"";
						}else if(textType.toLowerCase() == "password"){
							html+="\"password\"";
						}else if(textType == "tel"){
							html+="\"text\"	style=\"background:url(../../phone/resource/main/images/tel.png) no-repeat;background-position:right center; background-origin: content-box;background-size:14px 14px;\"";
						}else{
							html+="\"text\"";
						}
					}else{
						html+="\"text\"";
					}
					html += " style=\"";	//style
					if(style){
						html += style + ";";
					} 
					
					html += "\"";
					
					if(fieldType == "VALUE_TYPE_NUMBER"){	// 类型为数字时
						html += " onkeyup=\"resetWhenNonNumeric(this)\"";
						html += " onkeypress=\"isNumeric(event)\""; //只能输入数字和",.-";
					}
					html += " class=\"" + cssClass + "\""; 
					if(fieldKeyEvent && fieldKeyEvent.toLowerCase() == "enterkey"){
						//html +=" enterIndex";
					}

				}

				html+=" id=\"" + id + "\" name=\"" + name + "\" classname=\"" + classname + "\" fieldType=\"" + fieldType 
					+  "\" fieldKeyEvent=\"" + fieldKeyEvent + "\"  value=\"" + value + "\" title=\"" + title + "\"";
				html+=" isRefreshOnChanged=\"" + isRefreshOnChanged + "\"" + otherAttrsHtml;

				
				html+=" />";
				
				if((textType.toLowerCase() == "readonly" || displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED) && isBorderType == "true"){
					html+="<span id='" + name + "_show' class='component-input-span'>" + value + "</span>";
				}
				
				var spanHtml = "";
				var $spanHtml = jQuery(spanHtml);
				jQuery(this).after($spanHtml);
				jQuery(html).bind("change",function(){
					if(isRefreshOnChanged){
						if(subGridView){
							dy_view_refresh(this.id);
						}else{
							dy_refresh(this.id);
						}
					}
				}).bind('keydown', function (e) {
					if(fieldKeyEvent && fieldKeyEvent.toLowerCase() == "enterkey"){
			                var key = e.which;
			                if (key == 13) {
			                	jQuery("input:text:enabled,textarea:enabled").eq((jQuery("input:text:enabled,textarea:enabled").index(this)+1)).focus(); 
							}
					}
				}).bind("keyup",function(){
					if(fieldType == "VALUE_TYPE_NUMBER"){
						resetWhenNonNumeric(this);
					}
				}).replaceAll($field);
			}
		});
	};
})(jQuery);