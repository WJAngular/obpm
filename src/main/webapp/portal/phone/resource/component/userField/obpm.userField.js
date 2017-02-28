(function($){
	$.fn.obpmUserfield = function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id=$field.attr("_id");
			var name=$field.attr("_name");
			var fieldType=$field.attr("_fieldType");
			var cssClass=$field.attr("class");
			var value=$field.attr("value");
			var getFieldValue=$field.attr("_getFieldValue");
			var limitSum=$field.attr("_limitSum");
			var isRefreshOnChanged=$field.attr("_isRefreshOnChanged");
			var roleid=$field.attr("_roleid");
			var selectUser=$field.attr("_selectUser");
			var add=$field.attr("_add");
			var clearlabel=$field.attr("_clearlabel");
			var subGridView=$field.attr("_subGridView");
			var selectMode=$field.attr("_selectMode");
			var displayType=$field.attr("_displayType");
			var textType = $field.attr("_textType");
			var hiddenValue = $field.attr("_hiddenValue");
			var style = $field.attr("style");
			var discript = $field.attr("_discript");
			
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			fieldType = fieldType?fieldType:'';
			cssClass = cssClass?cssClass:'';
			getFieldValue = getFieldValue?getFieldValue:'';
			limitSum = limitSum?limitSum:'';
			isRefreshOnChanged = (isRefreshOnChanged == 'true');
			subGridView = (subGridView == 'true');
			roleid = roleid?roleid:'';
			selectUser = selectUser?selectUser:'';
			add = add?add:'';
			clearlabel = clearlabel?clearlabel:'';
			selectMode = selectMode?selectMode:'';
			displayType = displayType?displayType:'';
			textType = textType?textType:'';
			hiddenValue = hiddenValue?hiddenValue:'';
			style = style?"style='"+style+";'":'';
			value = value?value:'';
			discript = discript? discript : name;
			
			var readonly = false;
			var html="";
			var htmlShow = "";
			var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
			
			if (displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" || displayType == PermissionType_DISABLED) {
				readonly= true;
				htmlShow += "<div id='"+ name +"_show' class='showItem'>" + value + "</div>";
			}
			html +="<div class='formfield-wrap'><input type='hidden' id='" + id + "' name='" + name + "' value='" + getFieldValue + "' fieldType='" + fieldType + "' />";
			if(textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN){
				html+="<input type='hidden'";
			}else{
				html += "<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";
				html+="<div class='userSelectBox'><input class='contactField requiredField userField' data-enhance='false' type='text' readonly ";
			}
			if(displayType == PermissionType_READONLY || displayType != PermissionType_MODIFY) html += " disabled='disabled'";
			//html+=style;
			html+=" isRefreshOnChanged='" + isRefreshOnChanged + "'";
			html+=" id='" + id + "_text' class='"+cssClass+"'  filetype='userfield' fieldtype='" + fieldType + "' name='" + name + "_text' " + otherAttrsHtml;
			if(value){
				html+=" value='" + value + "'";
				html+=" title='" + bulitTitle(value) + "'";
			}
			html+=" />";
			if(textType.toLowerCase() != "hidden" && displayType != PermissionType_HIDDEN){
				if(textType.toLowerCase() != "readonly" && displayType == PermissionType_MODIFY){
					var settings = "{textField:'" + id +
							"_text',valueField:'" + id +
							"',limitSum:'" + limitSum +
							"',selectMode:'" + selectMode +
							"',callback:'" + (isRefreshOnChanged?(subGridView?"dy_view_refresh(\"" + id + "\")":"dy_refresh(\"" + name + "\")"):"") + 
							"',readonly:" + readonly + "}";
					var clearStr = 'jQuery("#' + id + '_text").val("");'
							+ 'jQuery("#' + id + '_text").attr("title","");'
							+ 'jQuery("#' + id + '").val("");'
							+ (isRefreshOnChanged?(subGridView?'dy_view_refresh("'+ id +'")':'dy_refresh("' + name + '")'):'');
					html+="<span class='selectBtn icon icon-person' onclick=showUserSelectNoFlow('actionName'," + settings + ",'" + roleid + "')" +
							" title='"+selectUser+"'></span>";
					html+="<span class='selectBtn icon icon-trash' onclick='" + clearStr + "' title='"+ clearlabel + "'></span>";
				}
			}
//				if(discript !=""){
//					html+="<span style='margin-left:10px;'>" + discript + "</span>";
//				}
			if(displayType == PermissionType_HIDDEN){
				html += ""+ hiddenValue +"";
			}
			html+="</div>" + htmlShow + "</div>";

			var $html = $(html);
			if (displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" || displayType == PermissionType_DISABLED) {
				$html.find("input").css("display", "none");
			}
			//var $newField = $(html);
			//$newField.find("input").css("width","100%").css("height","2em");
			//$field.after($newField);
			$field.parent().html($html).find("input[type='text']").css("width","100%");
		});
	};
			
})(jQuery);

function bulitTitle(names){
	if(names != null && names != ''){
		var array = names.split(";");
		var title = '';
		for(var i = 0; i < array.length; i++){
			if(i != 0 && i%10 == 0){
				title += "\n";
			}
			title += array[i] + ";";
		}
		return title == ''?title:title.substring(0,title.length-1);
	}
	return '';
}