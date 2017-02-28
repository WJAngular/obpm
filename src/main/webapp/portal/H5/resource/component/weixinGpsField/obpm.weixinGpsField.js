;
/**
 * 微信gps位置控件
 * @author Happy
 * @param $
 */
(function($){
	
	
	function markerLocation(t){
		var options = _parseOptions(t);
		var location = $.parseJSON(t.val());
		
		var geocoder,map, marker = null;
		var center = new qq.maps.LatLng(location.latitude,location.longitude);
	    map = new qq.maps.Map(document.getElementById('weixingpsfield_map_container_'+options.id),{
	        center: center,
	        zoom: 25
	    });
	    var info = new qq.maps.InfoWindow({map: map});
	    geocoder = new qq.maps.Geocoder({
	        complete : function(result){
	            map.setCenter(result.detail.location);
	            var marker = new qq.maps.Marker({
	                map:map,
	                position: result.detail.location
	            });
	            qq.maps.event.addListener(marker, 'click', function() {
	                info.open();
	                info.setContent('<div style="width:200px;height:50px;">'+
	                		location.address+'</div>');
	                info.setPosition(result.detail.location);
	            });
	            setTimeout(function(){
	            	qq.maps.event.trigger(marker,'click');
	            }, 1000);
	          
	        }
	    });
	    geocoder.getAddress(center);
	}
	
	
	/**
	 * gps控件初始化
	 */
	function _init(t){
		var options = _parseOptions(t);
		
		var v = t.val();
		var panle;
		if(v.length<=0 && options.displayType==2){//值为空，且可编辑状态下
			panel = $('<div style="height: 128px;width: 128px;background:url(../../share/component/weixinGpsField/gps.png) 0 0 no-repeat;"></div>');
			panel.insertAfter(t);
		}else{
			var html = [];
			html.push('<div style="width:400px;height:220px" id="weixingpsfield_map_container_'+options.id+'"></div>');
			panel = $(html.join(""));
			panel.insertAfter(t);
			markerLocation(t);
		}
		return panel;
	}
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var panel = t.data("weixinGpsField").panel;
		panel.find(".refresh").on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			panel.find(".address-text").html(" &nbsp;");
			getLocation(t);
		});
		panel.find(".location").on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			openLocation(t);
		});
	}
	
	/**
	 * 解析gps控件设置参数
	 */
	function _parseOptions(t){
		var options = {}
		options.id = t.attr("id");
		options.name = t.attr("name");
		options.displayType = t.attr("displayType");
		return options;
	}
	
	$.fn.obpmWeixinGpsField = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmWeixinGpsField.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("weixinGpsField");
			if(state){
				$.extend(state.options,options);
			}else{
				var r =_init(t);
				state = t.data('weixinGpsField', {
					options: $.extend({}, $.fn.obpmWeixinGpsField.defaults, _parseOptions(t), options),
					panel: r
				});
				
				_bindEvents(t);
			}
		});
	},
	
	$.fn.obpmWeixinGpsField.defaults = {
			id:'',
			name:undefined
	},
	
	$.fn.obpmWeixinGpsField.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
		}
	}
	
})(jQuery);
