;
/**
 * 微信gps位置控件
 * @author Happy
 * @param $
 */
(function($){
	
	/**
	 * 获取位置
	 */
	function getLocation(t,callback){
		wx.getLocation({
		    //type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
		    success: function (res) {
		       var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
		       var longitude = res.longitude ; // 经度，浮点数，范围为180 ~ -180。
		       
		       $.post(contextPath+"/attendance/attendance/convertGps2Baidu.action",{"from":"0","x":longitude,"y":latitude},function(result){
					if(1==result.status){
						var bdPoint = new BMap.Point(result.data.x,result.data.y);
						var geoc = new BMap.Geocoder();
			   			//坐标逆解析为地址
			   			geoc.getLocation(bdPoint, function(rs){
			   				var addComp = rs.addressComponents;
			   				var locationName = addComp.province+ addComp.city + addComp.district + addComp.street + addComp.streetNumber;
			   				var location ={
				    			   latitude:latitude,
				    			   longitude:longitude,
				    			   address:locationName
				    	   };
				    	   var options = t.data("weixinGpsField").options;
				    	   $("input[name='"+options.name+"']").val(JSON.stringify(location));
				    	   t.data("weixinGpsField").panel.find(".address-text").text(location.address);
				    	   if(callback && typeof callback=="function"){
				    		   callback(result);
				    	   }
			   			}); 
					}else{
					}
				},"json");
		    }
		});
		
	}
	
	/**
	 * 打开位置
	 */
	function openLocation(t){
		var v = t.val();
		if(!v || v.length<=0)return;
		
		var location = $.parseJSON(v);
		wx.openLocation({
		    latitude: location.latitude, // 纬度，浮点数，范围为90 ~ -90
		    longitude: location.longitude, // 经度，浮点数，范围为180 ~ -180。
		    name: '', // 位置名
		    address: location.address, // 地址详情说明
		    scale: 25, // 地图缩放级别,整形值,范围从1~28。默认为最大
		    infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
		});
		
			
	}
	/**
	 * gps控件初始化
	 */
	function _init(t){
		// 微信
		$.cachedScript( "http://res.wx.qq.com/open/js/jweixin-1.0.0.js" ).done(function( script, textStatus ) {
			console.log( textStatus );
			$.cachedScript( "../../phone/resource/component/weixin/weixin.jsApi.js" ).done(function( script, textStatus ) {
			  console.log( textStatus );
			});
		});

		var options = _parseOptions(t);
		var html = [];
		html.push('<div class="card_app">');
		html.push('<ul class="table-view table-address">');
		html.push('<li class="table-view-cell">');
		html.push('<a class="navigate-right location" data-transition="slide-in">');
		html.push('<span class="badge">');
		if(options.displayType==2){//可编辑
			html.push('<p class="refresh">获取位置</p>');
		}
		html.push('</span>');
		html.push('<div class="text"><i class="icon icon-add iconfont">&#xe613;</i><p class="add address-text"> &nbsp;</p></div>');
		html.push('</a>');
		html.push('</li>');
		html.push('</ul></div>');
		
		
		var panel = $(html.join(""));
		panel.insertAfter(t);
		var v = t.val();
		if(v.length<=0 && (options.displayType==2 || options.displayType==3)){//值为空，且可编辑状态下
			setTimeout(function(){
				getLocation(t,function(){
					//刷新表单
					if(options.isRefreshOnChanged){
						dy_refresh(options.id);
					}
				});
			}, 3000);
			
		}else{
			if(v && v!=""){
				var location = $.parseJSON(v);
				panel.find(".address-text").text(location.address);
			}
		}
		if(options.displayType==3){//隐藏
			panel.hide();
		}
		return panel;
	}
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var panel = t.data("weixinGpsField").panel;
		var options = t.data("weixinGpsField").options;
		panel.find(".refresh").on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			panel.find(".address-text").html(" &nbsp;");
			getLocation(t,function(){
				//刷新表单
				if(options.isRefreshOnChanged){
					dy_refresh(options.id);
				}
			});
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
		options.isRefreshOnChanged = t.attr("isRefreshOnChanged");
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
			isRefreshOnChanged:false,
			name:undefined
	},
	
	$.fn.obpmWeixinGpsField.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
		}
	}
	
})(jQuery);
