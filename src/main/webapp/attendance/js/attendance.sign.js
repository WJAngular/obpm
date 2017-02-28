/**
 * 签到签退服务接口
 * <p>封装应用层界面渲染与交互行为</p>
 * @author Happy
 */
var SIGN = {
	/**
	 * 初始化
	 */
	init : function() {
		this.bindEvent();
		this.standby();
	},
	/**
	 * 绑定事件
	 */
	bindEvent : function() {
		window.addEventListener("load", function(){
			var container_height=$("[data-role='container']")[0].offsetHeight;
			var header_height=$("[data-role='header']")[0].offsetHeight;
			//$("[data-role='footer']").prepend("<div class='copyright'>"+compay_name+"</div>");
			var footer_height=$("[data-role='footer']")[0].offsetHeight;
			var section_height = container_height -header_height-footer_height;	
			if(section_height>0){
				$("[data-role='body']").css("min-height",section_height);
			}
		}, false);
		//html5 设备移动事件 （实现摇一摇重新定位功能）  
		if (window.DeviceMotionEvent) {
			var SHAKE_THRESHOLD = 3000;  
	        var last_update = 0;  
	        var x = y = z = last_x = last_y = last_z = 0; 
            window.addEventListener('devicemotion', function deviceMotionHandler(eventData) {  
                var acceleration = eventData.accelerationIncludingGravity;  
                var curTime = new Date().getTime();  
                if ((curTime - last_update) > 100) {  
                    var diffTime = curTime - last_update;  
                    last_update = curTime;  
                    x = acceleration.x;  
                    y = acceleration.y;  
                    z = acceleration.z;  
                    var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;  
      
                    if (speed > SHAKE_THRESHOLD) {  
                        var media = document.getElementById("musicBox");//获取音频控件
                        if (media.paused) { //判读是否播放
                        	media.play(); //没有就播放
                        }   
                        SIGN.posit();
                    }  
                    last_x = x;  
                    last_y = y;  
                    last_z = z;  
                }  
            }, false); 
            document.addEventListener("WeixinJSBridgeReady", function () {
                var media = document.getElementById("musicBox");//获取音频控件
                media.load();
            }, false);
        } else {  
            //alert('not support mobile event');  
        }  
	},
	
	/**
	 * 执行签到或签退的动作指令
	 * @params action
	 * 		signin：签到动作；signout：签退动作
	 */
	doAction: function(action,position){
	
		$('#signing').hide();
		if('signin'==action){
			this.signin(position)
		}else if('signout'==action){
			this.signout(position);
		}else{
			alert('没有动作指令');
		}
	},
	
	/**
	 * 签到|签退准备阶段
	 */
	standby: function(){
		$('#show_map').height($('body').height()/5*3);
		$('#signing').show();
		SIGN.posit();
		SIGN.cache.map = new BMap.Map("show_map");
	},
	
	/**
	 * 地图定位
	 */
	posit: function(){
		try {
			navigator.geolocation.getCurrentPosition(function(position){
				SIGN.cache.position = position;
		       	 //GPS坐标
		       	var gpsPoint = new BMap.Point(position.coords.longitude,position.coords.latitude); 
		       	//GPS坐标转换为百度地图坐标 
		    	$.post("attendance/convertGps2Baidu.action",{"from":"0","x":gpsPoint.lng,"y":gpsPoint.lat},function(result){
					if(1==result.status){
						var bdPoint = new BMap.Point(result.data.x,result.data.y);
						var geoc = new BMap.Geocoder();
			   			//坐标逆解析为地址
			   			geoc.getLocation(bdPoint, function(rs){
			   				var addComp = rs.addressComponents;
			   				var locationName = addComp.province+ addComp.city + addComp.district + addComp.street + addComp.streetNumber;
			   				SIGN.cache.map.clearOverlays();
			   				SIGN.cache.map.centerAndZoom(bdPoint, 18);
			   				SIGN.cache.map.addOverlay(new BMap.Marker(bdPoint));
			    			
			    			$('.sign_text').text(action=='signin'? '签到':'签退');
			    			$('#current_location_text').text(locationName);
			   			}); 
					}else{
					}
				},"json");
		       	
	        },function(error){
	        	$('#signFail_position').show();
	        	$('.sign_text').text(action=='signin'? '签到':'签退');
	        },{ timeout: 5000, enableHighAccuracy: true });
		} catch (e) {
			$('#signFail_position').show();
        	$('.sign_text').text(action=='signin'? '签到':'签退');
        	alert(e.message);
		}
	
		
	},
	
	/**
	 * 执行签到事件
	 */
	signin: function(position){
		
		var rule = this.getRule();
		
		if(!rule){
			//没有当前用户的考勤规则
			$('#signFail').show();
			$('.sign_text').text('签到');
			$('.tip').text('未找到您的考勤规则，请联系系统管理员进行设置！');
			return;
		}
		
		var locations = [];
		if(rule){
			locations = rule.locations;
		}
    	 //GPS坐标
    	var gpsPoint = new BMap.Point(position.coords.longitude,position.coords.latitude); 
    	//GPS坐标转换为百度地图坐标 
    	$.post("attendance/convertGps2Baidu.action",{"from":"0","x":gpsPoint.lng,"y":gpsPoint.lat},function(result){
			if(1==result.status){
				var bdPoint = new BMap.Point(result.data.x,result.data.y);
				//var locations = rule.locations;
	 			if(locations.length==0){//没有考勤地点，则任何位置都可以签到
	 				var geoc = new BMap.Geocoder();
	    			//坐标逆解析为地址
	    			geoc.getLocation(bdPoint, function(rs){
	    				var addComp = rs.addressComponents;
	    				var locationName = addComp.province+ addComp.city + addComp.district + addComp.street + addComp.streetNumber;
	        			var signinLocation = '{"longitude":$longitude,"latitude":$latitude,"name":"$name"}'
	    					.replace('$longitude',bdPoint.lng)
	    					.replace('$latitude',bdPoint.lat)
	    					.replace('$name',locationName);
	    				SIGN.signinService(signinLocation,rule.multiPeriod);
	    			}); 
	 			}else{
	 				for(i in locations){
	 					var r = new BMap.Map("").getDistance(new BMap.Point(locations[i].longitude,locations[i].latitude),bdPoint).toFixed(0);
	         			if(r<=rule.range) {//在允许签到范围内
	         				var signinLocation = '{"longitude":$longitude,"latitude":$latitude,"name":"$name"}'
	         					.replace('$longitude',locations[i].longitude)
	         					.replace('$latitude',locations[i].latitude)
	         					.replace('$name',locations[i].name);
	         				SIGN.signinService(signinLocation,rule.multiPeriod);
	         				break;
	         			}else if(i==locations.length-1){
	         				$('#signFail').show();
	        				$('.sign_text').text('签到');
	         				$('.tip').text('在公司附近'+rule.range+'米范围内才可以签到哦!')
	         			}
	 				}
	 			} 
			}else{
			}
		},"json");
	},
	
	/**
	 * 执行签退事件
	 */
	signout: function(position){
		
		var rule = this.getRule();
		
		if(!rule){
			//没有当前用户的考勤规则
			$('#signFail').show();
			$('.sign_text').text('签退');
			$('.tip').text('未找到您的考勤规则，请联系系统管理员进行设置！');
			return;
		}
		
		var locations = [];
		if(rule){
			locations = rule.locations;
		}
    	 //GPS坐标
    	var gpsPoint = new BMap.Point(position.coords.longitude,position.coords.latitude); 
    	//GPS坐标转换为百度地图坐标 
    	$.post("attendance/convertGps2Baidu.action",{"from":"0","x":gpsPoint.lng,"y":gpsPoint.lat},function(result){
			if(1==result.status){
				var bdPoint = new BMap.Point(result.data.x,result.data.y);
				if(locations.length==0){//没有考勤地点，则任何位置都可以签退
	 				var geoc = new BMap.Geocoder();
	    			//坐标逆解析为地址
	    			geoc.getLocation(bdPoint, function(rs){
	    				var addComp = rs.addressComponents;
	    				var locationName = addComp.province+ addComp.city + addComp.district + addComp.street + addComp.streetNumber;
	        			var signoutLocation = '{"longitude":$longitude,"latitude":$latitude,"name":"$name"}'
	    					.replace('$longitude',bdPoint.lng)
	    					.replace('$latitude',bdPoint.lat)
	    					.replace('$name',locationName);
	    				SIGN.signoutService(signoutLocation,rule.multiPeriod);
	    			}); 
	 			}else{
	 				for(i in locations){
	 					var r = new BMap.Map("").getDistance(new BMap.Point(locations[i].longitude,locations[i].latitude),bdPoint).toFixed(0);
	         			if(r<=rule.range) {//在允许签退范围内
	         				var signoutLocation = '{"longitude":$longitude,"latitude":$latitude,"name":"$name"}'
	         					.replace('$longitude',locations[i].longitude)
	         					.replace('$latitude',locations[i].latitude)
	         					.replace('$name',locations[i].name);
	         				SIGN.signoutService(signoutLocation,rule.multiPeriod);
	         				break;
	         			}else if(i==locations.length-1){
	         				$('#signFail').show();
	        				$('.sign_text').text('签退');
	         				$('.tip').text('在公司附近'+rule.range+'米范围内才可以签退哦!')
	         				
	         			}
	 				}
	 			}
			}else{
			}
			
		},"json");
    	
    	//----------------
    	/**
 		BMap.Convertor.translate(gpsPoint, 0, function(bdPoint){
 			if(locations.length==0){//没有考勤地点，则任何位置都可以签退
 				var geoc = new BMap.Geocoder();
    			//坐标逆解析为地址
    			geoc.getLocation(bdPoint, function(rs){
    				var addComp = rs.addressComponents;
    				var locationName = addComp.province+ addComp.city + addComp.district + addComp.street + addComp.streetNumber;
        			var signoutLocation = '{"longitude":$longitude,"latitude":$latitude,"name":"$name"}'
    					.replace('$longitude',bdPoint.lng)
    					.replace('$latitude',bdPoint.lat)
    					.replace('$name',locationName);
    				SIGN.signoutService(signoutLocation);
    			}); 
 			}else{
 				for(i in locations){
 					var r = new BMap.Map("").getDistance(new BMap.Point(locations[i].longitude,locations[i].latitude),bdPoint).toFixed(0);
         			if(r<=rule.range) {//在允许签退范围内
         				var signoutLocation = '{"longitude":$longitude,"latitude":$latitude,"name":"$name"}'
         					.replace('$longitude',locations[i].longitude)
         					.replace('$latitude',locations[i].latitude)
         					.replace('$name',locations[i].name);
         				SIGN.signoutService(signoutLocation);
         				break;
         			}else if(i==locations.length-1){
         				$('#signFail').show();
        				$('.sign_text').text('签退');
         				$('.tip').text('在公司附近'+rule.range+'米范围内才可以签退哦!')
         				
         			}
 				}
 			}
 		}); 
 		**/
	},
	
	/**
	 * 获取当前用户的考勤规则对象
	 * @returns
	 */
	getRule:function(){
		var rule;
		$.ajax({
			type: 'get',
			async: false,
			url: 'rule/getRule.action',
			dataType: 'json',
			success: function(result){
				if(1==result.status){
					rule = result.data;
				}else{
					
				}
			}
		});
		
		return rule;
	},
	
	/**
	 * 请求服务器创建签到记录
	 */
	signinService:function(signinLocation,multiPeriod){
		$.post("attendance/signin.action",{'signinLocation':signinLocation,'multiPeriod':multiPeriod},function(result){
			if(1==result.status){
				//渲染签到成功页面
				$('#signSuccess').show();
				$('.sign_text').text('签到');
				$('.time').text(result.signin);
				var status = "";
				if (result.data.status != 1){
					switch(result.data.status){
						case 1:
							status='正常';
							break;
						case -1:
							status='迟到';
							break;
						case -2:
							status='早退';
							break;
						case -3:
							status='迟到且早退';
							break;
						case -4:
							status='地点异常';
							break;
						default :
							status='未知'
					}
					$('.sign_status').text("考勤状态："+status);
				} 
			}else{
				//渲染签到失败页面
				$('#signFail').show();
				$('.sign_text').text('签到');
				$('.tip').text(result.message);
			}
		},"json");
	},
	/**
	 * 请求服务器创建签退记录
	 */
	signoutService:function(signoutLocation,multiPeriod){
		$.post("attendance/signout.action",{'_signoutLocation':signoutLocation,'multiPeriod':multiPeriod},function(result){
			if(1==result.status){
				//渲染签退成功页面
				$('#signSuccess').show();
				$('.sign_text').text('签退');
				$('.time').text();
				$('.mooddiv').show();
				$('#workingTime').text(result.data.workingHours);
				$('.time').text(result.signout);
				var status = "";
				if (result.data.status != 1){
					switch(result.data.status){
						case 1:
							status='正常';
							break;
						case -1:
							status='迟到';
							break;
						case -2:
							status='早退';
							break;
						case -3:
							status='迟到且早退';
							break;
						case -4:
							status='地点异常';
							break;
						default :
							status='未知'
					}
					$('.sign_status').text("考勤状态："+status);
				} 
			}else{
				//渲染签退失败页面
				$('#signFail').show();
				$('.sign_text').text('签退');
				$('.tip').text(result.message);
			}
			
			
		},"json");
	},
	close: function(){
		WeixinJSBridge.invoke('closeWindow',{},function(res){
			//alert('关闭微信页面');
		});
	},
	
	/**
	 * 缓存对象
	 */
	cache : {
		
		target : null,//事件发起对象
		position:null,
		map:null
	}
	
};
