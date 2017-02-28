(function($) {
	var ALBUM = false;
	
	function takePhoto(el){
		var $this = $(el);
		if($this.find(".pan").length>0) return;
		var id = $this.data("id");
		var oField = jQuery("#"+ id);
		var img = jQuery("#"+ id+"_img");
		var _wx = top.wx ? top.wx : wx;
		 _wx.chooseImage({
			 count: 1, // 默认9
			 sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			 sourceType: ALBUM? ['album','camera']:['camera'], // 可以指定来源是相册还是相机，默认二者都有
		     success: function (res) {
		        var localIds = res.localIds;
		        setTimeout(function(){
		            _wx.uploadImage({
				        localId: localIds[0],
				        success: function (res) {
				          var serverId = res.serverId;
				          $.get(contextPath+"/portal/weixin/jsapi/upload.action",{"serverId":serverId},function(result){
					          if(result.status==1){
					        	  //todo 构建json
					        	oField.val(result.data);
								img.attr("src",localIds[0]).show();
					          }
					       });
				        },
				        fail: function (res) {
				          alert("网络异常，请再次尝试！");
				        }
				      });
		        }, 100)
			
		        //---
		        
		        
		      }
		    });
	}
	
	function previewImage(el){
		var $this = $(el);
		var current = $this.attr("src");
		if(current && current.length>0){
			var _wx = top.wx ? top.wx : wx;
			_wx.previewImage({
			    current: current, // 当前显示图片的http链接
			    urls: [] // 需要预览的图片http链接列表
			});
		}
	}
	$.fn.obpmTakePhoto = function() {
		return this.each(function() {
			// 微信
			$.cachedScript( "http://res.wx.qq.com/open/js/jweixin-1.0.0.js" ).done(function( script, textStatus ) {
				console.log( textStatus );
				$.cachedScript( "../../phone/resource/component/weixin/weixin.jsApi.js" ).done(function( script, textStatus ) {
				  console.log( textStatus );
				});
			});
			
			var $field = jQuery(this);
			var contextPath = $field.attr("contextPath");
			var value = $field.attr("value");
			var id = $field.attr("id");
			var imgw = $field.attr("imgw");
			var imgh = $field.attr("imgh");
			var name = $field.attr("name");
			var tagName = $field.attr("tagName");
			var discript = HTMLDencode($field.attr("discript"));
			var disabled = $field.attr("disabled");
			var displayType = $field.attr("displayType");
			ALBUM = $field.attr("album")=="true"; 
			
			var html = [];
			var $html = "";
			
			discript = discript? discript : name;
			
			if(displayType!=PermissionType_HIDDEN){
				var disabledClazz = "";
				var PhotoIcon = "&#xe60f;";
				if(displayType == PermissionType_DISABLED){
					disabledClazz = "ban";
					var PhotoIcon = "&#xe610;";
				}
				html.push('<div class="formfield-wrap"><label class="field-title contact-name" for="' + name + '">' + discript + '</label>');
				html.push('');
				html.push('<a data-id="'+id+'" class="btn btn-gray btn-block btn-photo"><i class="icon iconfont '+disabledClazz+'">'+ PhotoIcon +'</i></a>');
				html.push('<input type="hidden" id="' + id + '" name="' + name+ '" fieldType="' + tagName + '" value = "' +value+'"/>');
				html.push('<div class="btn-box-pic">');
				html.push('<img id="'+id+'_img" src="../../..' + value+'"');
				html.push(value.indexOf("photo.png")>=0? ' style="display:none;" >':' >');
				html.push('</div></div>');
				
				$html = $(html.join(""));
				$html.find(".btn-photo").bind("click",function(){
					takePhoto(this);
				}).end().replaceAll($field);
			}else{
				html.push('<input type="hidden" id="' + id + '" name="' + name+ '" fieldType="' + tagName + '" value = "' +value+'"/>');
				$html = $(html.join(""));
				$html.replaceAll($field);
			}
			$html.find("div.btn-box-pic").each(function () {  				
  				new RTP.PinchZoom($(this));
  			});
		});
	};
	$.fn.obpmViewTakePhoto = function(){
		return this.each(function(){
			var $field = jQuery(this);
			var imgw = $field.attr("imgw");
			var imgh = $field.attr("imgh");
			var viewReadOnly = $field.attr("viewReadOnly");
			var docId = $field.attr("docId");
			var docFormid = $field.attr("docFormid");
			var url = $field.attr("url");
			var fileName = $field.attr("fileName");
			
			viewReadOnly = (viewReadOnly=="true")?true:false;
			var imgwHalf = imgh/2;
			var isSubGridView = jQuery("#obpm_subGridView").size()>0?true:false;
			
			var html = "<div  class='takePhotoImg' style='position:relative;width:" + imgw + "; height:" + imgh + "'>";
			if(viewReadOnly){
				html += "<img alt='" + fileName + "' border='0' src='" + url + "' width='" + imgw + "' height='" + imgh + "' />";
			}else{
				html += "<a ";
				if(!isSubGridView){
					html +=" href=\"javaScript:viewDoc('" + docId + "','" + docFormid + "')\"";
				}
				html +=" title='" + fileName + "'>";
				html += "<img alt='" + fileName + "' border='0' src='" + url + "' width='" + imgw + "' height='" + imgh + "' />";
				html += "</a>";
			}
			html += "<div  class='takePhotoIcon' style='display:none;position:absolute;right:0px;top:"+imgwHalf+"px;z-index:100;'><a class='imgClick' href='" + url + "' target='blank'>";
			html += "<img alt='" + fileName + "' border='0' src='../../../resource/images/picture_go.png' title='点击查看原图' /></a><div>";
			html += "</div>";
			var $html=$(html);
			$html.mouseover(function(event) {
				event.stopPropagation();
				jQuery(this).find(".takePhotoIcon").show();
			}).mouseout(function(event){
				event.stopPropagation();
				jQuery(this).find(".takePhotoIcon").hide();
			});
			$field.replaceWith($html);
		});
	};
})(jQuery);