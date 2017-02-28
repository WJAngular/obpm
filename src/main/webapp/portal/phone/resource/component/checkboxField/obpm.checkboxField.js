(function($){
	$.fn.obpmCheckbox = function(){
			return this.each(function(){
				var $field = jQuery(this);
				var name = $field.attr('_name');
				var displayType = $field.attr('_displayType');
				var textType = $field.attr('_textType');
				var isRefreshOnChanged = $field.attr('_isRefreshOnChanged');
				var classname = $field.attr('classname');
				var cssClass = $field.attr('_class');
				var getLayout= $field.attr('_getLayout');
				var discript = $field.attr("_discript");
				var hiddenValue = $field.attr("_hiddenValue");
				var html="";
				
				discript = discript? discript : name;
				isRefreshOnChanged = (isRefreshOnChanged == "true");
				
				html += "<div class='formfield-wrap' ";
	
				if(displayType == PermissionType_HIDDEN){
					html += " style='display:none;'";
				}
				html += "><label class='field-title contact-name' for='" + name + "' >" + discript + "</label><div class='radio-box contactField requiredField'>";
				$field.find("span").each(function(i){
					var $div = jQuery(this);
					var value = $div.attr('value');
					var text = $div.attr('text');
					var checkedListSize = $div.attr('checkedListSize');
					var isDef = $div.attr('isDef');
					var checkedListContains = $div.attr('checkedListContains');
					var divHtml="";
					divHtml +="<div class='radio-list'><input data-enhance='false' type='checkbox' id='"+name+(i+1)+"' value=\"" + value + "\" text='" + text + "' name='" + name + "' ";
					
					if(textType && textType.toLowerCase() == "readonly" || displayType == PermissionType_DISABLED ||displayType == PermissionType_READONLY){
						divHtml +=" disabled='disabled'";
					}
					if(isRefreshOnChanged){
						divHtml +=" onclick='dy_refresh(this.id)'";
					}
					if(displayType == PermissionType_HIDDEN){
						divHtml += " style='display:none;'";
					}
					if(checkedListSize >0 && checkedListContains=='true' || isDef =='true'){
						divHtml +=" checked='true'";
					}
					divHtml +="/>";
					divHtml +="<label style='display:inline' for='"+name+(i+1)+"' ";
					if(displayType == PermissionType_HIDDEN){
						divHtml += " style='display:none;'";
					}
					divHtml += ">" + text + "</label>";
					if(getLayout !="" && getLayout.toLowerCase() == "vertical"){
						divHtml +="<br/>";
					}
					html += divHtml;
					html+="</div> ";
					
					
				});
				html+="</div></div>";
				
				if(displayType == PermissionType_HIDDEN){
					html += hiddenValue;
				}
				$field.after(html);
				$field.remove();
			});
	};
})(jQuery);