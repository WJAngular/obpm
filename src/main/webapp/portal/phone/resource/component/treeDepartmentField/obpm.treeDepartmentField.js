(function($){
	$.fn.obpmTreeDepartmentField =function(){
		return this.each(function(){
			var $field =jQuery(this);
			var textType=$field.attr("_textType");
			var fieldId = $field.attr("_fieldId");
			var fieldType = $field.attr("_fieldType");
			var cssClass=$field.attr("_cssClass");
			var fieldText=$field.attr("_fieldText");
			var limit = $field.attr("_limit");
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			var name=$field.attr("_name");
			var fieldValue=$field.attr("_fieldValue");
			var title=$field.attr("_title");
			var style =$field.attr("style");
			var textFieldId = fieldId + "_text";
			var valueFieldId = fieldId;
			var displayType = $field.attr("_displayType");
			var treeTip = $field.attr("_treeTip");
			var subGridView = $field.attr("_subGridView");
			var discript = HTMLDencode($field.attr("_discript"));
			var hiddenValue = $field.attr("_hiddenValue");
			var discript = $field.attr("_discript");

			discript = discript? discript : name;
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");

			var html="";
			var htmlShow="";
			var readonly = false;
			//if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
				
				//文本框
				html += "<div class='formfield-wrap' style='position: relative;";
				if(textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN){
					html += ";display:none;";
				}
				html += "'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";
				html += "<input class='contactField requiredField' data-enhance='false' type='text' readonly ";
				html += " id='" + textFieldId + "'";
				html += " class='" + cssClass + "'";
				html += " fieldType='" + fieldType + "'";
				html += " value='" + fieldText + "'" + otherAttrsHtml;
				//html += " style=\"";	//style
				//if(style) html += style + ";";
				//html += "\"";
				if(displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED || textType.toLowerCase() == "readonly"){
					html += " style='display:none;'";
					htmlShow += "<div id='"+ name +"_show' class='showItem'>" + fieldText + "</div>";
				}
				html += " />";
				
				//隐藏域
				html += "<input type='hidden' id='" +valueFieldId + "'";
				html += " name='" + name + "'";
				html += " fieldType='" + fieldType + "'";
				html += " value='" + fieldValue + "'";
				html += " />";
				if(textType.toLowerCase() != "hidden" && displayType != PermissionType_HIDDEN){	
					//按钮
					if(displayType != PermissionType_READONLY && displayType != PermissionType_DISABLED && textType.toLowerCase()!="readonly"){
						html += "<span data-enhance='false' class='tree-department' ";
						var settings = "{textField:'" + textFieldId +
							"',valueField:'" + valueFieldId +
							"',limit:'" + limit +
							"',callback:" + (isRefreshOnChanged?(subGridView?"dy_view_refresh":"dy_refresh"):"''") + 
							",readonly:" + readonly + "}";
						html += " onclick=\"showDepartmentSelect('actionName'," + settings + ")\"";
						html += " title='" + treeTip + "'";
						html += "></span>";
					}
//					if(discript !=""){
//						html+="<span style='margin-left:10px;'>" + discript + "</span>";
//					}
				}
				if(displayType == PermissionType_HIDDEN){
					html += ""+ hiddenValue +"";
				}
				html += htmlShow + "</div>";
			//}
			this.parentNode.innerHTML = html;
		});
	};
})(jQuery);