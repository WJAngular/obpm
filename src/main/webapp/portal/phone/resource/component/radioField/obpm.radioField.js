(function($){
	$.fn.obpmRadioField= function(){
		return this.each(function(){
			var $field = jQuery(this);
			var id = $field.attr("_id");
			var name =$field.attr('_name');
			var isRefreshOnChanged=$field.attr('_isRefreshOnChanged');
			var classname=$field.attr('classname');
			var cssClass=$field.attr('_cssClass');
			var displayType = $field.attr("_displayType");
			var textType = $field.attr("_textType");
			var valueList = $field.attr("_valueList");
			var discript = $field.attr("_discript");
			var hiddenValue = $field.attr("_hiddenValue");
			var getLayout= $field.attr('_getLayout');
			
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			discript = discript? discript : name;
			
			var html="";
			//if(displayType != PermissionType_HIDDEN){
				var defvalue ="";
				html += "<div class='formfield-wrap'";
				if(displayType == PermissionType_HIDDEN){
					html += " style='display:none;'";
				}
				html += "><label class='field-title contact-name' for='" + name + "' >" + discript + "</label><div class='radio-box contactField requiredField'>";
				$field.find("span").each(function(i){
					var $div = jQuery(this);
					var value = $div.attr('value');
					var get0ption = $div.attr('get0ption');
					var getValue = $div.attr('getValue');
					var isDef = $div.attr('isDef');
					isDef = (isDef == "true");

//					if(textType && textType.toLowerCase()=="readonly" || displayType == PermissionType_DISABLED || displayType == PermissionType_READONLY){
//						name += name + "$forshow" ;
//					}
					html+="<div class='radio-list'><input data-enhance='false' type='radio' id='" + name+(i+1) + "' value=\"" + value + "\" name='" + name + "' class='" + cssClass + "' ";
					if(displayType == PermissionType_HIDDEN){
						html += " style='display:none;'";
					}
					if(isRefreshOnChanged){
						html+=" isRefreshOnChanged='true' onclick='dy_refresh(this.id)'";
					}
					if(textType && textType.toLowerCase()=="readonly" || displayType == PermissionType_DISABLED || displayType == PermissionType_READONLY){
						html+=" disabled='disabled'";
					}
					if(valueList != "null"){
						if(valueList.split(";")[0]==getValue){
							defvalue = getValue;
							html+=" checked ='checked' ";
						}
					}else if(isDef){
						defvalue = getValue;
						html+=" checked ='checked' ";
					}
					html+=" />";
					html+="<label style='display:inline' for='" + name+(i+1) + "' ";
					if(displayType == PermissionType_HIDDEN){
						html += " style='display:none;'";
					}
					html+=">" + get0ption + "";
					html+="</label></div> ";
				});
				html+="</div></div>";
				$field.after(html);
			//}
			$field.remove();
		});
	};
})(jQuery);