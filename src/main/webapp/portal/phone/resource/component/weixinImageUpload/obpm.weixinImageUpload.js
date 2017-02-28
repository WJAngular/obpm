;
/**
 * 微信端图片上传控件
 * @author Happy
 * @param $
 */
(function($){
	
	
	/**
	 * 控件初始化
	 */
	function _init(t){
		
		var options = _parseOptions(t);
		
		t.after('<input type="hidden" name="' + options.name+ '" fieldtype="'+t.attr("fieldtype")+'" value=\'' + (options.value?options.value:'') + '\' id="' + options.id + '"/>');
		var panel= $('<div id="weixinImageUpload_container_'+options.id+'" class="formfield-wrap"><label class="field-title contact-name" for="'+options.discription+'">'+options.discription+'</label></div>');
		//render
		panel.append(_renderImages(options));
		
		//t.hide();
		//panel.insertAfter(t);
//		panel.find("input[name='"+options.name+"']").val(options.value?options.value:'');
		
		$("body").append('<div _pid="'+options.id+'" class="modal bigpic preview-panel"><div class="bigpicbox">'
						+'<div class="content">'
							+'<div class="lookImg"><img class="preview-item" src="" /></div>'
						+'</div>'
						+'<div class="bar bar-standard bar-footer">'
							+'<a class="btn pull-left btn-close">退出预览</a>'		  
								+'<a class="btn btn-negative pull-right btn-delete-popup" data-ignore="push">删除</a>'
						+'</div>'
					+'</div></div>');
		
		$("body").find(".bigpic[_pid='"+options.id+"']").find(".bigpicbox").append('<div class="tanchu">'
						+'<div class="header">提示</div>'
						+'<div class="contenter">'
							+'<p>确认删除当前？</p>'
						+'</div>'
						+'<div class="foot">'
							+'<a class="btn pull-right  btn-link red btn-delete" data-ignore="push">删除</a>'
							+'<a class="btn pull-right btn-link gay btn-cancel">取消</a>'
						+'</div>'
					+'</div>');
		
		if(options.readonly){
			panel.find(".btn-delete-popup").hide();
		}
		
		// 描述
		if(t.attr("moduleType") != "uploadImageRefresh"){
			t.attr("moduleType", "uploadImageRefresh").css("display","none");
		}
		
		panel.insertAfter(t);
		
		return panel;
	}
	
	function _renderImages(options){
		var imagesDiv = $('<div class="card_app smallpic image-list" style="margin-top:0px;"></div>');
		var images = options.value? JSON.parse(options.value):[];
		for(var i=0;i<images.length;i++){
			imagesDiv.append('<a data-ignore="push" class="image-item" data-name="'+images[i].name+'" data-path="'+images[i].path+'" ><img src="'+contextPath+images[i].path+'"/></a>');
		}
		if(!options.readonly){
			imagesDiv.append('<a data-ignore="push" class="btn-upload"><span class="icon icon-plus"></span></a>');
		}

		return imagesDiv;
	}
	
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var panel = t.data("weixinImageUpload").panel;
		var $bigView = $("body").find(".preview-panel[_pid='"+t.attr("id")+"']")
			
		panel.find(".btn-upload").on("click",function(){
			_chooseImage(t);
		});
		
		panel.find(".image-item").on("click",function(){
			var pid = $(this).parent().siblings("input").attr("id");
			var src = $(this).find("img").attr("src");
			var name = $(this).data("name");
			$bigView.find(".preview-item").attr("src",src);
			$bigView.find(".preview-item").data("name",name);
			$bigView.addClass("active");
			$("div.lookImg").each(function () {
  				new RTP.PinchZoom($(this), {});
  			});
		});
		
		$bigView.find(".btn-close").on("click",function(){
			$bigView.removeClass("active");
		});
		
		$bigView.find(".btn-delete-popup").on("click",function(){
			$bigView.find(".tanchu").addClass("block animated bounceIn");
		});
		
		$bigView.find(".btn-cancel").on("click",function(){
			$bigView.find(".tanchu").removeClass("block animated bounceIn");
		});
		$bigView.find(".btn-delete").on("click",function(){
			var name = $bigView.find(".preview-item").data("name");
			_removeImage(t,name);
			$bigView.find(".tanchu").removeClass("block animated bounceIn");
			$bigView.removeClass("active");
		});
	}
	
	function _onImageItemClick(item){
		var src = item.find("img").attr("src");
		var name = item.data("name");
		panel.find(".preview-item").attr("src",src);
		panel.find(".preview-item").data("name",name);
		panel.find(".preview-panel").addClass("active");
	}
	
	/**
	 * 解析控件参数
	 */
	function _parseOptions(t){
		var options = {};
		options.id = t.attr("id");
		options.name = t.attr("name");
		options.discription = HTMLDencode(t.attr("discript"))? HTMLDencode(t.attr("discript")):options.name;
		options.value = t.attr("value");
		options.limitnumber = t.attr("limitNumber");
		options.maxsize = t.attr("maxsize");
		options.refreshOnChanged = t.attr("refreshOnChanged");
		options.readonly = (t.attr("disabled") == 'disabled');
		return options;
	}
	
	
	function _chooseImage(t){
		var panel = t.data("weixinImageUpload").panel;
		var _wx = top.wx ? top.wx : wx;
		 _wx.chooseImage({
			 count: 9, 
			 sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			 sourceType: ['album','camera'], // 可以指定来源是相册还是相机，默认二者都有
		     success: function (res) {
		        var localIds = res.localIds;
		        setTimeout(function(){
		        	_uploadImage(t,panel,_wx,localIds);
		        }, 200);
		      }
		    });
	}
	
	function _uploadImage(t,panel,_wx,localIds){
		var localId = localIds.pop();
		 _wx.uploadImage({
		        localId: localId,
		        isShowProgressTips: 1,// 默认为1，显示进度提示
		        success: function (res) {
		          var serverId = res.serverId;
		          $.ajax({
		        	  url:contextPath+"/portal/weixin/jsapi/upload.action",
		        	  async:false,
		        	  type:"get",
		        	  data:{"serverId":serverId},
		        	  dataType:"json",
		        	  success:function(result){
				          if(result.status==1){
				        	  var path = result.data;
				        	  var name = result.data.substring(result.data.lastIndexOf("/")+1,result.data.length);
				        	  var imageItem = $('<a data-ignore="push" class="image-item" data-name="'+name+'" data-path="'+path+'" ><img src="'+localId+'"/></a>');
				        	  imageItem.on("click",function(){
				        		  	var src = $(this).find("img").attr("src");
				      				var name = $(this).data("name");
					      			panel.find(".preview-item").attr("src",src);
					      			panel.find(".preview-item").data("name",name);
					      			panel.find(".preview-panel").addClass("active");
				        	  });
				        	  panel.find(".btn-upload").before(imageItem);
				        	  try {
				        		  _addImage(t,{name:name,path:path});
				        	  } catch (e) {
				        		  alert(e.stack.toString());
				        	  }
				        	  if(localIds.length>0){
				        		  _uploadImage(t,panel,_wx,localIds);
				        	  }
						      _bindEvents(t);
				          }
				       }
		          });
		        },
		        fail: function (res) {
		          alert("网络异常，请再次尝试！");
		        }
		      });
	}
	
	function _addImage(t,data){
		var options = t.data("weixinImageUpload").options;
		var vf = $("#"+options.id);
		var json = vf.val();
		var images = json? JSON.parse(json):[];
		images.push(data);
		vf.val(JSON.stringify(images));
	}
	
	function _removeImage(t,name){
		var state = t.data("weixinImageUpload");
		var panel =state.panel;
		var options = state.options;
		panel.find(".image-item").each(function(){
			var $this = $(this);
			if($this.data("name")==name){
				$this.remove();
				var vf = $("#"+options.id);
				var images = JSON.parse(vf.val());
				for(var i=0;i<images.length;i++){
					if(images[i].name==name){
						images.splice(i,1);
						vf.val(JSON.stringify(images));
					}
					break;
				}
				return;
			}
		});
	}
	
	
	
	$.fn.obpmWeixinImageUpload = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmWeixinImageUpload.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("weixinImageUpload");
			if(state){
				$.extend(state.options,options);
			}else{
				var r =_init(t);
				state = t.data('weixinImageUpload', {
					options: $.extend({}, $.fn.obpmWeixinImageUpload.defaults, _parseOptions(t), options),
					panel: r
				});
				_bindEvents(t);
			}
		});
	},
	
	$.fn.obpmWeixinImageUpload.defaults = {
			id:'',
			name:'',
			value:null,
			discription:'',
			limitnumber:null,
			maxsize:10240,
			path:"ITEM_PATH",
			refreshOnChanged:null,
			readonly:false
			
	},
	
	$.fn.obpmWeixinImageUpload.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
			jq.each(function(){
				//nothing
			});
		}
	}
	
})(jQuery);
