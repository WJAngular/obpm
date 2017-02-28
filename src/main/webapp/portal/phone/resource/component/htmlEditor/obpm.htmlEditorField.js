(function($){
	$.fn.obpmHtmlEditorField=function(){
		return this.each(function() {
			var $field = jQuery(this);
			var displayType = $field.attr("displayType");
			var text = URLDecode($field.html());
			var style = $field.attr("style");
			var name = $field.attr("name");
			var discript = $field.attr("_discript");
			discript = discript? discript : name;
			var html = "";
			if (displayType == PermissionType_HIDDEN) {
				$(html).replaceAll($field);
			} else if (displayType != PermissionType_MODIFY) {
				html += "<label for='" + name + "'>" + discript + "</label>";
				html += "<div class='html-edit' readonly='readonly' name='" + name + "' >";
				if (text != "null")
					html += text;
				html += "</div>";
				$(html).replaceAll($field);
			} else {
				var $html = $("<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>");
				var $iframeDiv = $("<div class='html-edit' id='" + name + "' name='" + name + "'></div>");
				var iframe = document.createElement("iframe");
				iframe.style.cssText = "border: black thin;width:100%; height:200px";
				iframe.setAttribute("class", "html-edit-panel");
				$iframeDiv.append(iframe).appendTo($html);
				$field.after($html);
				var doc = iframe.contentDocument;
				doc.designMode = "On";
				if (text != "null" && text != ""){
					setTimeout(function(){
						doc.write(text);
					},15);
				}
				$field.attr("moduletype","htmlEditorSave");
			}
		});
	};
})(jQuery);
