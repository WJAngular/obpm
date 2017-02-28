(function($){
	$.fn.obpmWordField=function(){
		return this.each(function(){
			var $field = jQuery(this);
			var name = $field.attr("name");
			var id= $field.attr("id");
			var getItemValue = $field.attr("getItemValue");
			var getId = $field.attr("getId");
			var wordid = $field.attr("wordid");
			var getOpenTypeEquals = $field.attr("getOpenTypeEquals");
			var showWord = $field.attr("showWord");
			var secondvalue = $field.attr("secondvalue");
			var docgetId = $field.attr("docgetId");
			var docgetFormname = $field.attr("docgetFormname");
			var openType = $field.attr("openType");
			var displayType = $field.attr("displayType");
			var saveable = $field.attr("saveable");
			var isSignature = $field.attr("isSignature");
			var filename = $field.attr("filename");
			var _docid = $field.attr("_docid");
			var _type = $field.attr("_type");
			var fieldname = $field.attr("fieldname");
			var formname = $field.attr("formname");
			var versions = $field.attr("content.versions");
			var application = $field.attr("application");
			var _isEdit = $field.attr("_isEdit");
			var signature = $field.attr("signature");
			var isOnlyRead = (displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED);
			
			saveable = (saveable == "true");
			isSignature = (isSignature == "true");
			var html="";
			if(displayType != PermissionType_HIDDEN){
				if (isOnlyRead) {
					_isEdit=1;
				} 
				if(openType && openType=="2" || openType=="3"){
					if(getItemValue && getItemValue !=""){
						secondvalue = getItemValue;
					}else{
						getItemValue = wordid;
					}
					html+="<input type='hidden' name='" + name + "' id='" + id + "' value='" + getItemValue + "' />";
					html+="<input type='hidden' id='" + getId + "' value='" + secondvalue + "' />";
					html+="<button class='button-class' type='button'";
					if(displayType == PermissionType_READONLY){
						html +=" readonly";
					}
					html+=">";
					html+=" <img src='../../share/images/view/word.gif'></img>";
					html+="</button>";
				}else{
					if(getItemValue && getItemValue !=""){
						filename = getItemValue;
					}
					html+="<input type='hidden'  name='" + name + "' id='" + getId + "' value='" + getItemValue + "' />";
					html+="<iframe fieldName='" + name + "' id='" + getId + "' ";
					html+=" name='word' frameborder='0' width='100%' height='645px' scrolling='no' style='overflow:visible;z-index:-1px;' _type='word' ";
					html+=" src='" + contextPath + "/portal/dynaform/document/newword.action?id=" + getId + "";
					html+="&filename=" + filename + "";
					html+="&_docid=" + _docid + "";
					html+="&_type=" + _type + "";
					html+="&fieldname=" + fieldname + "";
					html+="&formname=" + formname+"";
					html+="&content.versions=" + versions + "";
					html+="&application=" + application + "";
					html+="&_isEdit=" + _isEdit + "";
					html+="&_opentype=" + openType + "";
					html+="&saveable=" + saveable + "";
					html+="&isSignature=" + isSignature + "";
					html+="&isOnlyRead=" + isOnlyRead + "";
					html+="&signature=" + signature + "'";
					html+=" ></iframe>";
				}
				if(openType && openType=="2" || openType=="3"){
					$(html).bind("click",function(){
						
						var url = "/portal/dynaform/document/newword.action?id=" + getId;
						url+="&_docid=" + _docid;
						url+="&_type=" + _type;
						url+="&fieldname=" + fieldname;
						url+="&formname=" + formname;
						url+="&content.versions=" + versions;
						url+="&application=" + application;
						url+="&_isEdit=" + _isEdit;
						url+="&isSignature=" + isSignature;
						url+="&isOnlyRead=" + isOnlyRead;
						url+="&signature=" + signature;
						url+="&_opentype=" + openType;
						url+="&saveable=" + saveable;
						
						showWordDialog(showWord,
								url,
								'WordControl',
								docgetId,
								docgetFormname,
								secondvalue,
								id,
								'content.versions',
								openType,
								displayType,
								_isEdit,
								saveable,
								isOnlyRead,
								signature,
								(isSignature?true:false));
					}).replaceAll($field);
				}else{
					$(html).replaceAll($field);
				}
			}else{
				$(html).replaceAll($field);
			}
		});
	};
	
})(jQuery);