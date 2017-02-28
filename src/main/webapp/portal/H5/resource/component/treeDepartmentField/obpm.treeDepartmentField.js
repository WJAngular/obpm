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
			var isCommonFilter = $field.attr("_isCommonFilter");
			isCommonFilter = (isCommonFilter == "true");
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");
			var hiddenValue = $field.attr("_hiddenValue");

			fieldValue = fieldValue ? fieldValue : "";
			style = style?"style='"+style+";":"style='vertical-align: bottom;width: auto;height: 34px;display: inherit;margin-right:4px;";
			
			var html="";
			var readonly = false;
			//if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
				
				//文本框				
				html += "<textarea type=";
				if(textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN){
					html += "'hidden'";
				}else{
					html += "'text' disabled ";
				}
				html += " id='" + textFieldId + "'";
				html += " class='form-control " + cssClass + "'";
				html += " isCommonFilter='"+isCommonFilter+"'";//是否常用搜索
				html += " fieldType='" + fieldType + "'";
				html += otherAttrsHtml;
				if(style) html += " " + style + "";
				
				if(textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN 
						|| displayType == PermissionType_READONLY || textType.toLowerCase()=="readonly"
							|| displayType == PermissionType_DISABLED){
					html += ";display:none;";
				}

				html += "'";
				html += " >" + fieldText + "</textarea>";
				
				//隐藏域
				html += "<input type='hidden' id='" +valueFieldId + "'";
				html += " name='" + name + "'";
				html += " fieldType='" + fieldType + "'";
				html += " value='" + fieldValue + "'";
				html += " />";
				if(textType.toLowerCase() != "hidden" && displayType != PermissionType_HIDDEN){	
					//按钮
					if(displayType != PermissionType_READONLY && displayType != PermissionType_DISABLED && textType.toLowerCase()!="readonly"){
						html += "<span class='btn btn-default'";
						var settings = "{textField:'" + textFieldId +
							"',valueField:'" + valueFieldId +
							"',limit:'" + limit +
							"',callback:" + (isRefreshOnChanged?(subGridView?"dy_view_refresh":"dy_refresh"):"''") + 
							",readonly:" + readonly + "}";
						
						var clearStr = 'jQuery("#' + textFieldId + '").val("");'
							+ 'jQuery("#' + textFieldId + '").attr("title","");'
							+ 'jQuery("#' + valueFieldId + '").attr("value","");'
							+ (isRefreshOnChanged?(subGridView?'dy_view_refresh("'+ valueFieldId +'")':'dy_refresh("' + name + '")'):'');
	
						html += " onclick=\"showDepartmentSelect('actionName'," + settings + ")\"";
						html += " style='cursor: pointer;margin-right:4px;'";
						html += " title='" + treeTip + "'";
						html += " >添加</span>"
						html += "<span class='btn btn-default' onclick='" + clearStr + "'" +
							" title='清除'> 清除 </span>";
					}
				}
				if(displayType == PermissionType_READONLY || textType.toLowerCase()=="readonly"
					|| displayType == PermissionType_DISABLED){
					html += "<span id='" + name + "_show'>" + fieldText + "</span>";
				}
				if(displayType == PermissionType_HIDDEN){
					html += "<span>"+ hiddenValue +"</span>";
				}
			//}
			this.parentNode.innerHTML = html;
		});
	};
})(jQuery);