; 
; 
/*!
 * =====================================================
 * Ratchet v2.0.2 (http://goratchet.com)
 * Copyright 2014 Connor Sears
 * Licensed under MIT (https://github.com/twbs/ratchet/blob/master/LICENSE)
 *
 * v2.0.2 designed by @connors.
 * =====================================================
 */
!function(){"use strict";var a=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},b=function(b){var c=a(b.target);return c&&c.hash?document.querySelector(c.hash):void 0};window.addEventListener("touchend",function(a){var c=b(a);c&&(c&&c.classList.contains("modal")&&c.classList.toggle("active"),a.preventDefault())})}(),!function(){"use strict";var a,b=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},c=function(){a.style.display="none",a.removeEventListener("webkitTransitionEnd",c)},d=function(){var b=document.createElement("div");return b.classList.add("backdrop"),b.addEventListener("touchend",function(){a.addEventListener("webkitTransitionEnd",c),a.classList.remove("visible"),a.parentNode.removeChild(d)}),b}(),e=function(c){var d=b(c.target);if(d&&d.hash&&!(d.hash.indexOf("/")>0)){try{a=document.querySelector(d.hash)}catch(e){a=null}if(null!==a&&a&&a.classList.contains("popover"))return a}},f=function(a){var b=e(a);b&&(b.style.display="block",b.offsetHeight,b.classList.add("visible"),b.parentNode.appendChild(d))};window.addEventListener("touchend",f)}(),!function(){"use strict";var a,b=function(){},c=20,d=sessionStorage,e={},f={slideIn:"slide-out",slideOut:"slide-in",fade:"fade"},g={bartab:".bar-tab",barnav:".bar-nav",barfooter:".bar-footer",barheadersecondary:".bar-header-secondary"},h=function(a,b){o.id=a.id,b&&(a=k(a.id)),d[a.id]=JSON.stringify(a),window.history.replaceState(a.id,a.title,a.url),e[a.id]=document.body.cloneNode(!0)},i=function(){var a=o.id,b=JSON.parse(d.cacheForwardStack||"[]"),e=JSON.parse(d.cacheBackStack||"[]");for(e.push(a);b.length;)delete d[b.shift()];for(;e.length>c;)delete d[e.shift()];window.history.pushState(null,"",d[o.id].url),d.cacheForwardStack=JSON.stringify(b),d.cacheBackStack=JSON.stringify(e)},j=function(a,b){var c="forward"===b,e=JSON.parse(d.cacheForwardStack||"[]"),f=JSON.parse(d.cacheBackStack||"[]"),g=c?f:e,h=c?e:f;o.id&&g.push(o.id),h.pop(),d.cacheForwardStack=JSON.stringify(e),d.cacheBackStack=JSON.stringify(f)},k=function(a){return JSON.parse(d[a]||null)||{}},l=function(b){var c=t(b.target);if(!(!c||b.which>1||b.metaKey||b.ctrlKey||a||location.protocol!==c.protocol||location.host!==c.host||!c.hash&&/#/.test(c.href)||c.hash&&c.href.replace(c.hash,"")===location.href.replace(location.hash,"")||"push"===c.getAttribute("data-ignore")))return c},m=function(a){var b=l(a);b&&(a.preventDefault(),o({url:b.href,hash:b.hash,timeout:b.getAttribute("data-timeout"),transition:b.getAttribute("data-transition")}))},n=function(a){var b,c,h,i,l,m,n,p,q=a.state;if(q&&d[q]){if(l=o.id<q?"forward":"back",j(q,l),h=k(q),i=e[q],h.title&&(document.title=h.title),"back"===l?(n=JSON.parse("back"===l?d.cacheForwardStack:d.cacheBackStack),p=k(n[n.length-1])):p=h,"back"===l&&!p.id)return o.id=q;if(m="back"===l?f[p.transition]:p.transition,!i)return o({id:h.id,url:h.url,title:h.title,timeout:h.timeout,transition:m,ignorePush:!0});if(p.transition){h=v(h,".content",i.cloneNode(!0));for(b in g)g.hasOwnProperty(b)&&(c=document.querySelector(g[b]),h[b]?r(h[b],c):c&&c.parentNode.removeChild(c))}r((h.contents||i).cloneNode(!0),document.querySelector(".content"),m),o.id=q,document.body.offsetHeight}},o=function(a){var c,d=o.xhr;a.container=a.container||a.transition?document.querySelector(".content"):document.body;for(c in g)g.hasOwnProperty(c)&&(a[c]=a[c]||document.querySelector(g[c]));d&&d.readyState<4&&(d.onreadystatechange=b,d.abort()),d=new XMLHttpRequest,d.open("GET",a.url,!0),d.setRequestHeader("X-PUSH","true"),d.onreadystatechange=function(){a._timeout&&clearTimeout(a._timeout),4===d.readyState&&(200===d.status?p(d,a):q(a.url))},o.id||h({id:+new Date,url:window.location.href,title:document.title,timeout:a.timeout,transition:null}),a.timeout&&(a._timeout=setTimeout(function(){d.abort("timeout")},a.timeout)),d.send(),d.readyState&&!a.ignorePush&&i()},p=function(a,b){var c,d,e=w(a,b);if(!e.contents)return u(b.url);if(e.title&&(document.title=e.title),b.transition)for(c in g)g.hasOwnProperty(c)&&(d=document.querySelector(g[c]),e[c]?r(e[c],d):d&&d.parentNode.removeChild(d));r(e.contents,b.container,b.transition,function(){h({id:b.id||+new Date,url:e.url,title:e.title,timeout:b.timeout,transition:b.transition},b.id),s()}),!b.ignorePush&&window._gaq&&_gaq.push(["_trackPageview"]),!b.hash},q=function(a){throw new Error("Could not get: "+a)},r=function(a,b,c,d){var e,f,g;if(c?(e=/in$/.test(c),"fade"===c&&(b.classList.add("in"),b.classList.add("fade"),a.classList.add("fade")),/slide/.test(c)&&(a.classList.add("sliding-in",e?"right":"left"),a.classList.add("sliding"),b.classList.add("sliding")),b.parentNode.insertBefore(a,b)):b?b.innerHTML=a.innerHTML:a.classList.contains("content")?document.body.appendChild(a):document.body.insertBefore(a,document.querySelector(".content")),c||d&&d(),"fade"===c){b.offsetWidth,b.classList.remove("in");var h=function(){b.removeEventListener("webkitTransitionEnd",h),a.classList.add("in"),a.addEventListener("webkitTransitionEnd",i)},i=function(){a.removeEventListener("webkitTransitionEnd",i),b.parentNode.removeChild(b),a.classList.remove("fade"),a.classList.remove("in"),d&&d()};b.addEventListener("webkitTransitionEnd",h)}if(/slide/.test(c)){var j=function(){a.removeEventListener("webkitTransitionEnd",j),a.classList.remove("sliding","sliding-in"),a.classList.remove(g),b.parentNode.removeChild(b),d&&d()};b.offsetWidth,g=e?"right":"left",f=e?"left":"right",b.classList.add(f),a.classList.remove(g),a.addEventListener("webkitTransitionEnd",j)}},s=function(){var a=new CustomEvent("push",{detail:{state:k(o.id)},bubbles:!0,cancelable:!0});window.dispatchEvent(a)},t=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},u=function(a){window.history.replaceState(null,"","#"),window.location.replace(a)},v=function(a,b,c){var d,e={};for(d in a)a.hasOwnProperty(d)&&(e[d]=a[d]);return Object.keys(g).forEach(function(a){var b=c.querySelector(g[a]);b&&b.parentNode.removeChild(b),e[a]=b}),e.contents=c.querySelector(b),e},w=function(a,b){var c,d,e={},f=a.responseText;if(e.url=b.url,!f)return e;/<html/i.test(f)?(c=document.createElement("div"),d=document.createElement("div"),c.innerHTML=f.match(/<head[^>]*>([\s\S.]*)<\/head>/i)[0],d.innerHTML=f.match(/<body[^>]*>([\s\S.]*)<\/body>/i)[0]):(c=d=document.createElement("div"),c.innerHTML=f),e.title=c.querySelector("title");var g="innerText"in e.title?"innerText":"textContent";return e.title=e.title&&e.title[g].trim(),b.transition?e=v(e,".content",d):e.contents=d,e};window.addEventListener("touchstart",function(){a=!1}),window.addEventListener("touchmove",function(){a=!0}),window.addEventListener("touchend",m),window.addEventListener("click",function(a){l(a)&&a.preventDefault()}),window.addEventListener("popstate",n),window.PUSH=o}(),!function(){"use strict";var a=function(a){for(var b,c=document.querySelectorAll(".segmented-control .control-item");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a};window.addEventListener("touchend",function(b){var c,d,e,f=a(b.target),g="active",h="."+g;if(f&&(c=f.parentNode.querySelector(h),c&&c.classList.remove(g),f.classList.add(g),f.hash&&(e=document.querySelector(f.hash)))){d=e.parentNode.querySelectorAll(h);for(var i=0;i<d.length;i++)d[i].classList.remove(g);e.classList.add(g)}}),window.addEventListener("click",function(b){a(b.target)&&b.preventDefault()})}(),!function(){"use strict";var a,b,c,d,e,f,g,h,i,j,k,l,m,n=function(a){for(var b,c=document.querySelectorAll(".slider > .slide-group");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},o=function(){if("webkitTransform"in c.style){var a=c.style.webkitTransform.match(/translate3d\(([^,]*)/),b=a?a[1]:0;return parseInt(b,10)}},p=function(a){var b=a?0>d?"ceil":"floor":"round";k=Math[b](o()/(m/c.children.length)),k+=a,k=Math.min(k,0),k=Math.max(-(c.children.length-1),k)},q=function(f){if(c=n(f.target)){var k=c.querySelector(".slide");m=k.offsetWidth*c.children.length,l=void 0,j=c.offsetWidth,i=1,g=-(c.children.length-1),h=+new Date,a=f.touches[0].pageX,b=f.touches[0].pageY,d=0,e=0,p(0),c.style["-webkit-transition-duration"]=0}},r=function(h){h.touches.length>1||!c||(d=h.touches[0].pageX-a,e=h.touches[0].pageY-b,a=h.touches[0].pageX,b=h.touches[0].pageY,"undefined"==typeof l&&(l=Math.abs(e)>Math.abs(d)),l||(f=d/i+o(),h.preventDefault(),i=0===k&&d>0?a/j+1.25:k===g&&0>d?Math.abs(a)/j+1.25:1,c.style.webkitTransform="translate3d("+f+"px,0,0)"))},s=function(a){c&&!l&&(p(+new Date-h<1e3&&Math.abs(d)>15?0>d?-1:1:0),f=k*j,c.style["-webkit-transition-duration"]=".2s",c.style.webkitTransform="translate3d("+f+"px,0,0)",a=new CustomEvent("slide",{detail:{slideNumber:Math.abs(k)},bubbles:!0,cancelable:!0}),c.parentNode.dispatchEvent(a))};window.addEventListener("touchstart",q),window.addEventListener("touchmove",r),window.addEventListener("touchend",s)}(),!function(){"use strict";var a={},b=!1,c=!1,d=!1,e=function(a){for(var b,c=document.querySelectorAll(".toggle");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a};window.addEventListener("touchstart",function(c){if(c=c.originalEvent||c,d=e(c.target)){var f=d.querySelector(".toggle-handle"),g=d.clientWidth,h=f.clientWidth,i=d.classList.contains("active")?g-h:0;a={pageX:c.touches[0].pageX-i,pageY:c.touches[0].pageY},b=!1}}),window.addEventListener("touchmove",function(e){if(e=e.originalEvent||e,!(e.touches.length>1)&&d){var f=d.querySelector(".toggle-handle"),g=e.touches[0],h=d.clientWidth,i=f.clientWidth,j=h-i;if(b=!0,c=g.pageX-a.pageX,!(Math.abs(c)<Math.abs(g.pageY-a.pageY))){if(e.preventDefault(),0>c)return f.style.webkitTransform="translate3d(0,0,0)";if(c>j)return f.style.webkitTransform="translate3d("+j+"px,0,0)";f.style.webkitTransform="translate3d("+c+"px,0,0)",d.classList[c>h/2-i/2?"add":"remove"]("active")}}}),window.addEventListener("touchend",function(a){if(d){var e=d.querySelector(".toggle-handle"),f=d.clientWidth,g=e.clientWidth,h=f-g,i=!b&&!d.classList.contains("active")||b&&c>f/2-g/2;e.style.webkitTransform=i?"translate3d("+h+"px,0,0)":"translate3d(0,0,0)",d.classList[i?"add":"remove"]("active"),a=new CustomEvent("toggle",{detail:{isActive:i},bubbles:!0,cancelable:!0}),d.dispatchEvent(a),b=!1,d=!1}})}();; 
var Common = {};
Common.Util = {
	cache : {
		
	},
	/**
	 * 获取用户头像图片url地址
	 * @param userId
	 */	
	getAvatar : function(userId){
		if(!this.cache[userId]){
			$.ajax({
				type: "GET",
				url: contextPath + "/contacts/getAvatar.action",
				data: {"id":userId},
				async: false,
				dataType: "json",
				success:function(result){
					if(1==result.status){
						Common.Util.cache[userId] = result.data;
					}
				}
			});
		}
		
		return this.cache[userId];;
	},
	/**
	 * 计算时间差
	 * @param date,date2
	 */
	daysCalc : function(date,date2){
		var startDateArr = date.split(/[- :]/); 
		var startDate = new Date(startDateArr[0], startDateArr[1]-1, startDateArr[2], startDateArr[3], startDateArr[4]);
		var nowDate = new Date();
		if(!date2 || date2 == ""){
			var nowDate = new Date();
		}else{
			var endDateArr = date2.split(/[- :]/); 
			nowDate = new Date(endDateArr[0], endDateArr[1]-1, endDateArr[2], endDateArr[3], endDateArr[4]);
		}
		var msDate = nowDate.getTime() - startDate.getTime();
		//计算出相差天数
		var days=Math.floor(msDate/(24*3600*1000));
		//计算出小时数
		var leave1 = msDate%(24*3600*1000);//计算天数后剩余的毫秒数
		var hours = Math.floor(leave1/(3600*1000));
		//计算相差分钟数
		var leave2 = leave1%(3600*1000);//计算小时数后剩余的毫秒数
		var minutes = Math.floor(leave2/(60*1000));
		//计算相差秒数
		var leave3=leave2%(60*1000);//计算分钟数后剩余的毫秒数
		var seconds=Math.round(leave3/1000);
		//alert(" 相差 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒");	
		var timeCalc = {
			    "days": days,
			    "hours": hours,
			    "minutes": minutes,
			    "seconds": seconds
			};
		return timeCalc;
	}
}; 
/*
 Jh.js
 Jquery Portal layout 
 可拖拽布局 
 Copyright (C) Jh 2012.4.11
 @author xiaofan
*/
var Jh = {	
	Config:{//CLASS样式配置
		tableCls:"form-list",
		tdCls:"form-text",
		tdCls2:"single",
		ulCls : "tag-list",
		layCls :"layout-list",
		min :"min",
		mintext:"\u6536\u8D77",
		max :"max",
		maxtext:"\u5C55\u5F00",
		close :"close",
		closetext :"\u5173\u95ED",
		refreshtext:"\u5237\u65B0",
		refresh :"refresh",
		_groupItemContent : "itemContent",
		_groupItemHead : "itemHeader",
		_groupWrapperClass  : "groupWrapper",
		_groupItemClass : "groupItem"
	}	
};

function FormBaiduMap(FieldID,applicationid,displayType){
	var oField = jQuery("#"+ FieldID);
	var url="../../portal/share/component/map/form/baiduMap.jsp?type=dialog&applicationid="+applicationid+"&displayType="+displayType;
	var title_map = '{*[map]*}';
	OBPM.dialog.show({
		title : title_map,
		url : url,
		args: {"fieldID":FieldID,"mapData":oField.val()},
		width : 1000,
		height : 600,
		close : function(result) {
		}
	});
}

Jh.Layout=function(me){
	var _winwidth = $(window).width();
	
	var _left = "portal_l"	,
		_center ="portal_m",
		_right ="portal_r",
		_icon ="portal_i";
	return me = {
		location:{//三列容器
			left:_left,
			center:_center,
			right:_right,
			icon:_icon
		},
		locationId : {
			left:"#"+_left,
			center:"#"+_center,
			right:"#"+_right,
			icon:"#"+_icon
		},
		layoutCss : {
			0:"1:3",
			1:"3:1",
			2:"1:2",
			3:"2:1",
			4:"1:2:1",
			5:"1:1:2",
			6:"2:1:1",
			7:"1:1:1",
			8:"1:1",
			9:"1"
		},
		layoutText : {
			0 :function(){return "w25 w75 wnone";}(),
			1 :function(){return "w75 w25 wnone";}(),
			2 :function(){return "w33 w66 wnone";}(),
			3 :function(){return "w66 w33 wnone";}(),
			4 :function(){return "w25 w50 w25";}(),
			5 :function(){return "w25 w25 w50";}(),
			6 :function(){return "w50 w25 w25";}(),
			7 :function(){return "w33 w33 w33";}(),
			8 :function(){return "w50 w50 wnone";}(),
			9 :function(){return "w00 wnone wnone";}()
		}
	};
}();

Jh.Util = {//工具类
	format : function (str, model) {//格式化指定键值的模板
		for (var k in model) {
			var re = new RegExp("{" + k + "}", "g");
			str = str.replace(re, model[k]);
		}
		return str;
	},
	toBody:function(o){//往Body添加对象
		$("body").prepend(o);
	},
	configToString:function(val,cfg){
		name = "'"+val+"':" + cfg + ",";
		return name;
	},
	//判断旧数据 利用json2.js将字符串转对象
	setValueJson:function(val){
		var oldVal = false;
		var iconJson
		try { 
			iconJson = JSON.parse(val);
		}catch(e){ 
			oldVal = true;
		} 
		if(oldVal == true){
			iconJson = {}
			iconJson.icon = val;
		}
		return iconJson
	},
	sysFlowTab:function(tabName){
		$(".widget-tab").find(".nav-tabs").find("li").removeClass("active");
		$(".widget-tab").find("a[aria-controls='"+tabName+"']").parent("li").addClass("active");
		$(".widget-tab").find(".tab-content").find(".tab-pane").removeClass("active");
		$(".widget-tab").find(".tab-content").find("#sysFlowTab_"+tabName).addClass("active");
	},
	setUserWidget:function(userCfg, defaultCfg){//往Body添加对象
		var _userCfg = "";
		var _appIcon = "";
		var _appL = "";
		var _appM = "";
		var _appR = "";
		
		$.each(userCfg, function(i, value) {
			if(value && value.length > 0){
				if(i=="appIcon"){
					$.each(value,function(j,val) {
						if(defaultCfg.appIcon[val] || defaultCfg.appIcon[val]!=undefined){
							_appIcon += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appIcon[val]));
						}else if(defaultCfg.appL[val] || defaultCfg.appL[val]!=undefined){
							_appIcon += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appL[val]));
						}else if(defaultCfg.appM[val] || defaultCfg.appM[val]!=undefined){
							_appIcon += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appM[val]));
						}else if(defaultCfg.appR[val] || defaultCfg.appR[val]!=undefined){
							_appIcon += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appR[val]));
						}
					 })
				}
				if(i=="appL"){
					$.each(value,function(j,val) { 
						if(defaultCfg.appIcon[val] || defaultCfg.appIcon[val]!=undefined){
							_appL += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appIcon[val]));
						}else if(defaultCfg.appL[val] || defaultCfg.appL[val]!=undefined){
							_appL += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appL[val]));
						}else if(defaultCfg.appM[val] || defaultCfg.appM[val]!=undefined){
							_appL += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appM[val]));
						}else if(defaultCfg.appR[val] || defaultCfg.appR[val]!=undefined){
							_appL += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appR[val]));
						}
					 })
				}
				if(i=="appM"){
					$.each(value,function(j,val) {
						if(defaultCfg.appIcon[val] || defaultCfg.appIcon[val]!=undefined){
							_appM += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appIcon[val]));
						}else if(defaultCfg.appL[val] || defaultCfg.appL[val]!=undefined){
							_appM += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appL[val]));
						}else if(defaultCfg.appM[val] || defaultCfg.appM[val]!=undefined){
							_appM += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appM[val]));
						}else if(defaultCfg.appR[val] || defaultCfg.appR[val]!=undefined){
							_appM += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appR[val]));
						}
					 })
				}
				if(i=="appR"){
					$.each(value,function(j,val) {
						if(defaultCfg.appIcon[val] || defaultCfg.appIcon[val]!=undefined){
							_appR += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appIcon[val]));
						}else if(defaultCfg.appL[val] || defaultCfg.appL[val]!=undefined){
							_appR += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appL[val]));
						}else if(defaultCfg.appM[val] || defaultCfg.appM[val]!=undefined){
							_appR += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appM[val]));
						}else if(defaultCfg.appR[val] || defaultCfg.appR[val]!=undefined){
							_appR += Jh.Util.configToString(val,JSON.stringify(defaultCfg.appR[val]));
						}
					 })
				}
				if(i=="layoutStyle"){
					_userCfg += "'layoutStyle':'";
					_userCfg += defaultCfg.layoutStyle;
					_userCfg += "'";
				}
			}
		});
		
		if (_appL.length > 0)
			_appL = _appL.substring(0,_appL.length - 1);
		if (_appM.length > 0)
			_appM = _appM.substring(0,_appM.length - 1);
		if (_appR.length > 0)
			_appR = _appR.substring(0,_appR.length - 1);
		if (_appIcon.length > 0)
			_appIcon = _appIcon.substring(0,_appIcon.length - 1);
		
		return ("{" + "'appIcon':{"+ _appIcon +"}," 
			+ "'appL':{"+ _appL +"}," 
			+ "'appM':{"+ _appM +"}," 
			+ "'appR':{"+ _appR +"}," 
			+ _userCfg
			+ "}");
	},
	setIcon:function(widgetType){
		var defaultIcon;
		var _defaultIcon = {
				iconCustomizeReport : '<i class=\"icon iconfont\">&#xe602;</i>',
				iconIscript : '<i class=\"icon iconfont\">&#xe602;</i>',
				iconLink : '<i class=\"icon iconfont\">&#xe603;</i>',
				iconPage : '<i class=\"icon iconfont\">&#xe604;</i>',
				iconReport : '<i class=\"icon iconfont\">&#xe605;</i>',
				iconRunquanReport : '<i class=\"icon iconfont\">&#xe602;</i>',
				iconSummary : '<i class=\"icon iconfont\">&#xe607;</i>',
				iconSystemAnnouncement : '<i class=\"icon iconfont\">&#xe602;</i>',
				iconSystemWorkflow : '<i class=\"icon iconfont\">&#xe608;</i>',
				iconSystemWeather : '/portal/H5/resource/images/icon-weather.png',
				iconView : '<i class=\"icon iconfont\">&#xe608;</i>',
				iconWorkflowAnalyzer : '<i class=\"icon iconfont\">&#xe602;</i>',
				iconOther : '<i class=\"icon iconfont\">&#xe602;</i>'
		};
		switch (widgetType) {
			case "customizeReport":
				defaultIcon = _defaultIcon.iconCustomizeReport;
				break;
			case "iscript":
				defaultIcon = _defaultIcon.iconIscript;
				break;
			case "link":
				defaultIcon = _defaultIcon.iconLink;
				break;
			case "page":
				defaultIcon = _defaultIcon.iconPage;
				break;
			case "report":
				defaultIcon = _defaultIcon.iconReport;
				break;
			case "summary":
				defaultIcon = _defaultIcon.iconSummary;
				break;
			case "system_announcement":
				defaultIcon = _defaultIcon.iconSystemAnnouncement;
				break;
			case "system_workflow":
				defaultIcon = _defaultIcon.iconSystemWorkflow;
				break;
			case "system_weather":
				defaultIcon = _defaultIcon.iconSystemWeather;
				break;
			case "view":
				defaultIcon = _defaultIcon.iconView;
				break;
			case "workflow_analyzer":
				defaultIcon = _defaultIcon.iconWorkflowAnalyzer;
				break;
			default:
				defaultIcon = _defaultIcon.iconOther;
				break;
		}
		return defaultIcon;
	}
};
Jh.fn = function(me){//功能区
	return me = {
		init:function(data){//初始化
			me._ele = {};
			me._create();
			me._createWrap(data);
			me._bindEvent();
			//设置默认layout
			$("#header>table a[rel='"+data.layoutStyle+"']").trigger("click");
		},
		
		_create:function(){//创建自己
			var _box = $("<div id='header'/>");
			$("<div id='configBtn'><span class='configImg'></span></div>")
			.hover(function(){
				//addClass("")
			},function(){

			})
			.click(function(){
				var $table = $("#header").find("table");
				if ($table.is(":hidden")) {
					$table.show();
				}
				else {
					$table.hide();
				}
			}).appendTo(_box);
			me.box = _box; 
			Jh.Util.toBody(_box);//往Body里添加自己
			
			
			
		},
		
		_createWrap:function(d){//创建外层容器
			var _table = me._createTable(Jh.Config.tableCls);
			me._ele.table = _table;
			me._createModuleList(d);
			
			me._addPanel(_table);
			_table.hide();//初始化隐藏
		},
		
		_createTable:function(clsName){	//创建表格		
			var _t = $("<table/>").addClass(clsName);
			$("<tbody/>")
								.append(me._createLayoutTr())
								.append(me._createBaseTr())					 
								.append(me._createActionTr())
								.appendTo(_t);	
			return _t; 	
		},
		
		_createBaseTr:function(){//创建功能模块tr
			var	_td = me._createTd(Jh.Config.tdCls2),
				_tr = $("<tr>").append(me._createTd(Jh.Config.tdCls,"\u529F\u80FD\u6A21\u5757\u8BBE\u7F6E:"))
							   .append(_td);
			me._ele.mtd = _td;				   
			return _tr;
		},
		
		_createActionTr:function(){//创建按钮tr
			var _td = me._createTd(Jh.Config.tdCls2),
				_tr = $("<tr>").append(me._createTd(Jh.Config.tdCls))
							.append(_td);
			me._ele.atd = _td;	
			return _tr;
		},
		
		_createLayoutTr:function(){//创建布局
			var _td = me._createTd(Jh.Config.tdCls2),
				_div = $("<div/>").addClass(Jh.Config.layCls)
				  				  .append(me._createA("1"))
								  .append(me._createA("1:1"))
								  .append(me._createA("1:3"))
								  .append(me._createA("3:1"))
								  .append(me._createA("1:2"))
								  .append(me._createA("2:1"))
							 	  .append(me._createA("1:1:2"))
								  .append(me._createA("1:2:1"))
								  .append(me._createA("2:1:1"))
								  .append(me._createA("1:1:1"))
								  .appendTo(_td),
				_tr = $("<tr>").append(me._createTd(Jh.Config.tdCls,"\u5E03\u5C40\u8BBE\u7F6E:")).append(_td);

			me._ele.layoutTd = _td;
			return _tr;
		},
		
		_createModuleList:function(data){//创建模块list
			var _ul = $("<ul/>").addClass(Jh.Config.ulCls);
//			me._createLis(data,_ul);
			//初始化icon
			me._createLis(data.appIcon,_ul);
			me._createHr(_ul);
			//初始化左中右
			me._createLis(data.appL,_ul);
			me._createLis(data.appM,_ul);
			me._createLis(data.appR,_ul);
			me._ele.ul = _ul;
			_ul.appendTo(me._ele.mtd);
		},
		
		
		
		_createLis:function(obj,_ul){//创建li列表
			$.each(obj,function(key,o){				
				_ul.append(me._createLi(key,o));
			});
		},
		
		_createHr:function(_ul){//创建分割线
			_ul.append("<hr style='clear:both'/>");
		},
		
		_createA:function(text){//创建A
			return $("<a href='javascript:void(0);' rel='"+text+"'>"+text+"</a>");
		},
		
		_createLi:function(key,o){//创建li
			var setIcon
			if(o.icon == "" || o.icon == "null"){
				setIcon = Jh.Util.setIcon(o.type);
			}else{
				setIcon = o.icon;
			}
			 
			var $li = $("<li/>").append("<a href='#' rel='"+key+"' titleColor='"+o.titleColor+"' icon='../.."+setIcon+"' type='"+o.type+"' titleBColor='"+o.titleBColor+"' iconShow='"+o.iconShow+"' >"+o.name+"</a>").append("<span class='ok'></span>");
			////--todo
			var	_m = $("#"+key); //模块div 
			if (_m.size()>0) {
				$li.find(".ok").show();
			}
			else {
				$li.find(".ok").hide();
			}
			
			return $li;
		},
		
		_createTd:function(clsName,text){//创建td
			var t = $("<td>").addClass(clsName);
			if(text!=undefined){
				t.text(text);
			}
			return  t; 
		},
		_addPanel:function(o){
			me.box.append(o);		
		},
		
		_bindCancel:function(obj){//添加模块
			obj.click(function(){
				location.reload();
			});
		
		},
		
		_bindEvent:function(){//事件绑定
			me._moduleLiClick();
			me._layoutAClick();
		},
		
		_moduleLiClick:function(){//绑定模块LI单击事件
			$("."+Jh.Config.ulCls+" li").on("click",function(){
				var _this = $(this),
					_mName = _this.find("a").attr("rel"),//获取模块名
					_m = $("#"+_mName), //模块div 
					_d = _this.find(".ok");//对号
				
				if (_m.size()<=0) {//判断是否已经存在，避免二次添加
					var	o = {},
						key  = _mName,	
						layout = "left",
						position ;
					
						o["name"] = _this.find("a").text();
						o["titleColor"] = _this.find("a").attr("titleColor");
						o["titleBColor"] = _this.find("a").attr("titleBColor");
						o["icon"] = _this.find("a").attr("icon");
						o["iconShow"] = _this.find("a").attr("iconShow");
						o["type"] = _this.find("a").attr("type");
						
					// me._ele.ul.append(me._createLi(key,name));//添加功能标签
					if (o["iconShow"]=="true"){
						Jh.Iconlink.createIconOne(key,o);
					}
					else {
						if(layout=='left'){
							position = $("#"+Jh.Layout.location.left);
						}else if(layout=='center'){
							position = $("#"+Jh.Layout.location.center);
						}else{
							position = $("#"+Jh.Layout.location.right);
						}
						position.append(Jh.Portal._createPortalOne(key,o));//添加portal							
						Jh.Portal._eventRefresh();//重新绑定刷新事件并触发
					}
				}
					
				if(_d.is(":visible")){//判断是否显示
					_d.hide();//隐藏对号
					//_m.hide();//隐藏模块
					_m.remove();
				}else{
					_d.show();//显示对号
					_m.show();//显示模块
				}
				//Jh.Util.refresh();
				
			});
		},
		
		_layoutAClick:function(){//绑定布局列表A 单击事件
			$("."+Jh.Config.layCls+" a").click(function(){
				var _this = $(this);
				var _v = _this.attr("rel");
				me._ToLayout(_v);
				_this.addClass("active").siblings().removeClass("active");
			});
		},
		
		_ToLayout:function(v){//刷新布局
			var CssMode= Jh.Layout.layoutCss, //布局模式  
				CssText= Jh.Layout.layoutText,//css 
				ModulesId= Jh.Layout.locationId, //模块id
				CssTextId=0,//默认css数组编号
				ModuleItems="";//模块数组
			$.each(CssMode, function(m, mn){
				if(v==mn) CssTextId=m;//css 赋值
			});	
	
			$.each(ModulesId, function(s, sn){	

				var currentModule = $(sn),				
					cssName = CssText[CssTextId],
					ary = cssName.split(/\s+/);//得到当前布局css数组
				switch(s){
					case "left": s =0;
					break;
					case "center": s =1;
					break;
					case "right":s = 2;
				}	
			});
	
		}
		
	};

}();

//swiper-carousel对象
Jh.Carousel = function(me){	
	var _template = {
			carousel : '<div class="swiper-container">'
				+ '<div class="swiper-wrapper"></div>'
				+ '<div class="swiper-pagination"></div>'
				+ '</div>',
			carouselItem : '<div class="swiper-slide"><img src="{swiperPic}" style="width:100%"></div>'	
	};
	return me = {
		init : function(){
			var swiperPic = {
				    "001": {
				        "pic": obpmPhone.bar1//"resource/images/banner01.jpg"
				    },
				    "002": {
				        "pic": obpmPhone.bar2
				    },
				    "003": {
				        "pic": obpmPhone.bar3
				    }
				};
			
			$("#homePage").prepend("<div id='carousel-box'></div>")
			me.create(swiperPic);
			
			if($("#carousel-box").find(".swiper-container").html()==""){
				$("#carousel-box").remove();
			}
			
			var carouselSwiper = new Swiper('#carousel-box>.swiper-container', {
				autoplay: 4000,
		        pagination: '.swiper-pagination',
		        paginationClickable: true
		    });
			
		},
		create : function(swiperPic){
			var $carouselPanel = $(_template.carousel);
			$("#carousel-box").append($carouselPanel)
			$.each(swiperPic,function(){
				me.createSwiperOne(this.pic)
			})
		},
		createSwiperOne : function(img){
			var swiperItem_html = Jh.Util.format(_template.carouselItem,{'swiperPic' : img});
			var $swiperItem_html = $(swiperItem_html);
			$("#carousel-box").find(".swiper-wrapper").append($swiperItem_html);
		}
	};
}();

//iconLink对象
Jh.Iconlink = function(me){	
	var _template = {

			iconLink : '<div class="iconLink" id="iconLink" style="margin-top:0px;"></div>',
			icon_p : '<div class="weui_grids icon_p" style="padding:0px;"></div>',
			icon_con : '<a class="weui_grid js_grid icon_con" data-id="button" id="{id}" _type="{type}">'
					+ '<div class="weui_grid_icon">{icon}</div>'
					+ '<p class="weui_grid_label" style="color:{titleColor};background-color:{titleBColor}">{name}</p>'
					+ '</a>',
			iconMorePanel : '<script type="text/html" id="tpl_iconMore">'
					+ '<div class="page"><div class="bd">'
					+ '<div class="weui_grids"></div>'
					+ '</div></div></script>',
			iconMoreBtn : '<a class="weui_grid js_grid" data-id="iconMore" id="icoMoreBtn">'
					+ '<div class="weui_grid_icon">...</div>'
					+ '<p class="weui_grid_label">更多</p>'
					+ '</a>',
	};
	return me = {
		init : function(iconLink){
			$("#homePage").prepend("<div id='iconLink-box'></div>")
			me.create(iconLink);
			me.loadUrl();
			if($("#iconLink-box").find(".icon_p").html()==""){
				$("#iconLink-box").remove();
			}
			me.iconMoreNum();
			var iconPicHeight = $("#iconLink-box").find("span.icon_con").find("img").width();
			$("#iconLink-box").find("span.icon_con").find("img").height(iconPicHeight);
			$("#iconLink-box").find("a.btn").height(iconPicHeight+5);
			
			//临时修复页面默认#iconMore开始			
			if(window.location.hash && window.location.hash.length > 1 && window.location.hash == "#iconMore"){
				var html = $("#tpl_iconMore").html();
	            var $html = $(html).addClass('slideIn').addClass("iconMore").css("z-index","1000");
	            $(".js_container").append($html);
	            location.hash = "#iconMore";
			}
            //临时修复页面默认#iconMore结束
			
		},
		iconMoreNum : function(){
			var $icoNumBox = $(".icon_p");
			var $icoNumList = $icoNumBox.find("a");
			var underIcoNum = '<a class="weui_grid js_grid">'
							+ '<div class="weui_grid_icon">&nbsp;</div>'
							+ '<p class="weui_grid_label">&nbsp;</p>'
							+ '</a>';
			//计算快速入口数量
			if($icoNumList.size() > 8){
				$("body").append(_template.iconMorePanel);
				$icoNumList.each(function(i){
					if(i == 7){
						$(this).before(_template.iconMoreBtn);
						$("#icoMoreBtn").on("click",function(){
							var id = $(this).data('id');
							var html = $("#tpl_"+id).html();
				            var $html = $(html).addClass('slideIn').addClass(id).css("z-index","1000");
				            $(".js_container").append($html);
				            location.hash = "#"+id;
				            //临时修复更多图标url开始
				            $(".js_container .page.iconMore").find(".icon_con").each(function(i){
								$con = $(this);
								switch ($con.attr("_type")) {
								case "link":
									var $temp = $("<div></div>");
									var _url = "../widget/displayWidget.action?id="+$con.attr("id");
									
									$temp.appendTo($con);
									$temp.load(_url,function(){
										$cur = $(this);
										var _href = $cur.find("a").attr("href");
										$cur.parent().attr("_href",_href);
										$cur.parent().click(function(){
											window.location.href = _href;
										});
										$cur.remove();						
									});
									break;
								case "view":
									var _href = "../../portal/dynaform/view/displayViewWithPermission.action?_viewid="
										+ $con.attr("_actionContent") + "&clearTemp=true&_backURL=../../../portal/cool/closeTab.jsp";
									
									$con.attr("_href",_href).click(function(){
										var _href = $(this).attr("_href");
										window.location.href = _href;
									});
									break;
								case "page":
									var _href = $con.attr("_actionContent");
									$con.attr("_href",_href).click(function(){
										window.location.href = _href;
									});
									break;

								default:
									break;
								}
							});	
				            //临时修复更多图标url结束
						});
						var tplIconMorePanel = $($("#tpl_iconMore").html());
						tplIconMorePanel.find(".weui_grids").append($(this));
						$("#tpl_iconMore").html(tplIconMorePanel);
					}else if(i >= 8){
						var tplIconMorePanel = $($("#tpl_iconMore").html());
						tplIconMorePanel.find(".weui_grids").append($(this));
						$("#tpl_iconMore").html(tplIconMorePanel);
					}
				});
				
				var tplIconMorePanel = $($("#tpl_iconMore").html());
				var $tplIconMore = tplIconMorePanel.find("a").size();	
				if($tplIconMore<4){
					for(var j=0; j<(4-$tplIconMore); j++){
						tplIconMorePanel.find(".weui_grids").append($(underIcoNum));
						$("#tpl_iconMore").html(tplIconMorePanel);
					}
				}else{
					if($tplIconMore%4 != 0){
						for(var j=0; j<$tplIconMore%4; j++){
							tplIconMorePanel.find(".weui_grids").append($(underIcoNum));
							$("#tpl_iconMore").html(tplIconMorePanel);
						}
					}
				}
			}else{
				if($icoNumList.size()<4){
					for(var j=0; j<(4-$icoNumList.size()); j++){
						$(".icon_p").append($(underIcoNum));
					}
				}else{
					if($icoNumList.size()%4 != 0){
						for(var j=0; j<(8-$icoNumList.size()); j++){
							$(".icon_p").append($(underIcoNum));
						}
					}
				}
			}
		},
		create : function(iconLink){	//插入body
			var $icon_p = $(_template.icon_p);
			var $iconLink = $(_template.iconLink).append($icon_p);
			$("#iconLink-box").append($iconLink);
			if(iconLink || iconLink!=undefined){
				$.each(iconLink,function(key,obj){
					me.createIconOne(key,obj);
				});
			}
		},
		
		createIconOne : function(key,obj){	//替换参数
			var setIcon
			if(obj.icon == "" || obj.icon == "null"){
				setIcon = Jh.Util.setIcon(obj.type);
			}else{
				var iconJson = Jh.Util.setValueJson(obj.icon);
				if(iconJson.icontype == "font"){
					setIcon = "<i class='"+ iconJson.icon +"' style='color:" + iconJson.iconFontColor + "'></i>";
				}else{
					setIcon = "<img src=\"" + contextPath + iconJson.icon +"\" />";
				}
			}
			var con_html = Jh.Util.format(_template.icon_con,{'name' : obj.name});
				con_html = Jh.Util.format(con_html,{'icon' : setIcon});
				con_html = Jh.Util.format(con_html,{'titleColor' : obj.titleColor});
				con_html = Jh.Util.format(con_html,{'titleBColor' : obj.titleBColor});
				con_html = Jh.Util.format(con_html,{'id' : key});
				con_html = Jh.Util.format(con_html,{'type' : obj.type});
				
			var $con_html = $(con_html);
				if(obj.actionContent){
					$con_html.attr("_actionContent",obj.actionContent);
				}
			$("#iconLink-box").find(".icon_p").append($con_html);
		},
		loadUrl : function(){	//加载url并设置
			$("#iconLink .icon_con").each(function(i){
				$con = $(this);
				switch ($con.attr("_type")) {
				case "link":
					var $temp = $("<div></div>");
					var _url = "../widget/displayWidget.action?id="+$con.attr("id");
					
					$temp.appendTo($con);
					$temp.load(_url,function(){
						$cur = $(this);
						var _href = $cur.find("a").attr("href");
						$cur.parent().attr("_href",_href);
						$cur.parent().on("click",function(){
							window.location.href = _href;
						});
						$cur.remove();						
					});
					break;
				case "view":
					var _url = "../../portal/dynaform/view/displayViewWithPermission.action?_viewid="
						+ $con.attr("_actionContent") + "&clearTemp=true&_backURL=../../../portal/cool/closeTab.jsp";
					
					$con.attr("_href",_url).on("click",function(){
						var _href = $(this).attr("_href");
						window.location.href = _href;
					});
					break;
				case "page":
					var _href = $con.attr("_actionContent");
					$con.attr("_href",_href).on("click",function(){
						window.location.href = _href;
					});
					break;

				default:
					break;
				}
			});	
		}
	};
}();

Jh.Portal = function(me){//Portal对象
	
	var portalDiv = "<div id='home_link'></div>",
		_template = {//模板
			l :"<div id='"+Jh.Layout.location.left+"' class='"+Jh.Config._groupWrapperClass+"'/>",
			m :"<div id='"+Jh.Layout.location.center+"' class='"+Jh.Config._groupWrapperClass+"'/>",
			r :"<div id='"+Jh.Layout.location.right+"' class='"+Jh.Config._groupWrapperClass+"'/>",
			portalWrapBox : "<div id='{key}' class='home-widget-box' _type='{type}'></div>",
			portalWrap : "<div class='card_app home_link'><div class='table' style='padding:0px'><ul class='home-list'></ul></div></div>",
			itemHeader : "<li id='{key}' class='home' _type='{type}'><i class='icon iconfont'>&#xe60a;</i><span>{name}</span>"
						+ "<span class='icon icon-right-nav imgS' style='color:#bcbcbc'></span>" 
						+ "<span class='pendingTotal'></span></li>"
	};

	var _defaultCfg;
	var _userCfg;
	return me={	
		init:function(userCfg, defaultCfg){//初始化
			if(userCfg && userCfg!=undefined){
				_userCfg = eval("("+Jh.Util.setUserWidget(userCfg, defaultCfg)+")");
			}else{
				_userCfg = defaultCfg
			}
			_defaultCfg = defaultCfg;
			//初始化Portal
			me.box = $(portalDiv);
			$("#homePage").append(me.box);	
			me._elements = {};		
			me._createModulesWrap();
			me._bindData(_userCfg);
			Jh.Iconlink.init(_userCfg.appIcon);	//初始化图标链接--必须在最后,不好看 要优化
			Jh.Carousel.init();
		},
		_isFnExist:function(widgetId){//判断是否WidgetKey存在
			var flag = false;
			$.each(_defaultCfg.appL, function(k, o){
				if (widgetId == k) {
					flag = true;
					return;
				}
			});
			$.each(_defaultCfg.appM, function(k, o){
				if (widgetId == k) {
					flag = true;
					return;
				}
			});
			$.each(_defaultCfg.appR, function(k, o){
				if (widgetId == k) {
					flag = true;
					return;
				}
			});
			return flag;
		},


		
		_bindData:function(op){//绑定数据
			$.each(op,function(key,item){				
				me._createPortal(key,item);
			});
		},
		
		_createModulesWrap:function(){//创建模块外层容器
			me._elements.m_l = $(_template.l);
			me._elements.m_m = $(_template.m);
			me._elements.m_r = $(_template.r);
			me._addPanel(me._elements.m_l);
			me._addPanel(me._elements.m_m);
			me._addPanel(me._elements.m_r);
		},
		
		_addPanel:function(o){//往容器里添加
			me.box.append(o);
		},
		
		_createPortal:function(key,item){//创建portal
			
			
			
			var mWrap ;
			switch(key){
			   case "appL":mWrap = me._elements.m_l;
				break;
			   case "appM":mWrap = me._elements.m_m;
				break;
			   case "appR":mWrap = me._elements.m_r;
				break;
			}
			
			if (key!="layoutStyle")//添加内容部分,即：非layoutStyle
				$.each(item,function(k,o){
					
					if (me._isFnExist(k)){
						mWrap.append(me._createPortalOne(k,o));
						me._createContent(k,o);
					}	
				});
		},
		
		_createPortalOne:function(key,o){//创建单个portal item
			
			portalWrapBox = Jh.Util.format(_template.portalWrapBox,{"key":key});
			portalWrapBox = $(Jh.Util.format(portalWrapBox,{"type":o.type}));

			return portalWrapBox;
		},
		
		
		
		_createDiv:function(classname){
			var _div = $("<div/>").addClass(classname);
			return _div;
		},
		
		_createContent:function(key,o){
			var $me = $("#"+key);
			var refreshUrl = "../widget/displayWidget.action?id="+$me.attr("id")+"&appid="+$("#applicationId").val();
			$me.load(refreshUrl, function(data){
				var $content = $(this);
				switch($content.attr("_type")) {
					//case "summary":
					//	me._renderSummary($content);
					//break;
					//case "view":
					//	me._renderView($content);
					//break;
					//case "link":
					//	me._renderLink($content);
					//break;
					//case "extlink":
					//	me._renderExtLink($content);
					//break;
					//case "announcement":
					//	me._renderAnnouncement($content);
					//break;
					case "system_workflow":
						me._renderWorkflow($content);
					break;
					//case "weather":
					//	me._renderWeather($content);
					//break;
					default:
						$content.remove();
					break;
				};
			});
		},
		
		_renderWorkflow:function($me){//流程处理
			$me.addClass("card_app widget-tab");
			var _template = {
					tabPanel : '<div class="itemContent"><div class="widget-workflow-container" role="tabpanel"></div></div>',
					tabUl : '<ul class="nav nav-tabs swiper-wrapper" role="tablist"></ul>',
					tabNav : '<li role="presentation" class="widget-workflow-slide">'
					+'<a onclick="Jh.Util.sysFlowTab(\'pending\');" aria-controls="pending" >我的待办 <span class="flowNum">(0)</span></a>'
					+'</li><li role="presentation" class="widget-workflow-slide">'
					+'<a onclick="Jh.Util.sysFlowTab(\'processing\');" aria-controls="processing" >经办跟踪 <span class="flowNum">(0)</span></a>'
					+'</li>',
					tabConBox : '<div class="tab-content" style="padding-bottom: 50px;">'
					+'<div role="tabpanel" class="tab-pane" id="sysFlowTab_pending">'
					+'<ul style="padding:10px;margin:0px;"></ul><div class="bottom-menu text-right"><a class="btn btn-default" _url="pending.jsp" role="button" style="display:none">更多&raquo;</a></div></div>'
					+'<div role="tabpanel" class="tab-pane" id="sysFlowTab_processing">'
					+'<ul style="padding:10px;margin:0px;"></ul><div class="bottom-menu text-right"><a class="btn btn-default" _url="processing.jsp" role="button" style="display:none">更多&raquo;</a></div></div></div>',
					tabLi : '<li class="widgetItem" id="{tabDocID}" _url="{tabUrl}" _isRead ="{tabIsRead}">'
						+ '<div class="tabLiFace">{tabAvatar}'
						+ '<span class="{tabIsReadClass}"></span>'
						+ '</div>'
						+ '<div class="tabLiCon">'
						+ '<div class="tabLiConBox">'
						+ '<div class="tabLiConA text-left">'
						+ '<span class="tabLiCon-text">[{tabName}] {tabCon}</span>'
						+ '</div>'
						+ '<div class="tabLiConB"><div class="tabLiTagLeft">'
						+ '<span class="tabLiCon-auditornames" _initiator="{tabInitiator}" _initiatorId="{tabInitiatorID}">{tabDept}{tabInitiator}</span>'
						+ '<span class="tabLiCon-lastprocesstime timeago" title="{tabTime}">{tabTime}</span>'
						+ '</div>'
						+ '<div class="tabLiTagRight text-right">'
						+ '<span class="tabLiCon-status" title="{tabState}">{tabState}</span>'
						+ '</div>'
						+ '</div>'
						+ '</li>',
					tabSpacePending : '<div id="content-load-panel"><div id="content-space" class="text-center">'
						+ '<div class="content-space-icon icon iconfont">&#xe61b;</div>'
						+ '<div class="content-space-h1">当前没有任何待办</div>'
						+ '<div class="content-space-h2">可发起的待办将显示在这里</div></div></div>',
					tabSpaceProcessing : '<div id="content-load-panel"><div id="content-space" class="text-center">'
						+ '<div class="content-space-icon icon iconfont">&#xe618;</div>'
						+ '<div class="content-space-h1">当前没有任何内容</div>'
						+ '<div class="content-space-h2">可发起的申请将显示在这里</div></div></div>'
			};
			$me.append(_template.tabPanel);
			$me.find(".widget-workflow-container").append(_template.tabUl);
			$me.find(".widget-workflow-container").append(_template.tabConBox);
			$me.find(".nav-tabs").append(_template.tabNav);
			
			
			var $pendingList = $me.find("input[name='pendingList']");
			var $processedList = $me.find("input[name='processedList']");
			var _pendappId = $pendingList.attr("_appId");
			var _processedappId = $processedList.attr("_appId");
			var $accordionPending = $("<div class='panel-group' id='accordion-pending'></div>");
			var $accordionProcessing = $("<div class='panel-group' id='accordion-processing'></div>");
			$me.append($accordionPending);
			$me.append($accordionProcessing);
			setTimeout(function(){
			$pendingList.each(function(){
				$accordionPending.load($pendingList.attr("_url"), function(response,status) {
					var $accorPend = $("#accordion-pending");
					var pendingLi = "";
					if(status=="success"){
						var $lis = $accorPend.find("li");
						var count= $lis.size();
						if(count != 0){
							if(count > 9){
								count = "9+";
							}
							$(".nav-tabs").find("a[aria-controls='pending']>.flowNum").text("("+ count +")");	//设置总数
						}
						$lis.each(function(i){
							var $li = $(this);
							var _flowname = $li.attr("_flowname");
							var _url = $li.attr("_url");
							var _tabDocID = $li.attr("_docid");
							var _initiator = $li.attr("_initiator");
							var _initiatorID = $li.attr("_initiatorId");
							var _initiatorDept = $li.attr("_initiatorDept");
							var _time = $li.attr("_lastprocesstime");
							var _isRead = $li.attr("_isRead");
							
							if( i < 5 ){
								var flowTime = new Date(_time);
								var timeFixArr = _time.split(/[- :]/); 
								var timeFixDate = new Date(timeFixArr[0], timeFixArr[1]-1, timeFixArr[2], timeFixArr[3], timeFixArr[4]);
								var _timeAgo,_avatar;
								var avatar = Common.Util.getAvatar(_initiatorID);
								var Month = timeFixDate.getMonth() + 1; 
								var Day = timeFixDate.getDate(); 
								var Hour = timeFixDate.getHours(); 
								var Minute = timeFixDate.getMinutes(); 
							
								if(avatar!="" && avatar!=undefined){
									_avatar = "<img src ="+avatar+">";
								}else{
									_avatar = "<div class='noAvatar'>"+ _initiator.substr(_initiator.length-2, 2) +"</div>";
								}
								
								//比较时间 serviceTime定义在main.jsp
				 				var comTime = Common.Util.daysCalc(_time,serviceTime);

								if(comTime.days > 2){
									if (Month >= 10){ 
										_timeAgo = Month + "-"; 
									}else{ 
										_timeAgo = "0" + Month + "-"; 
									} 
									if (Day >= 10) 
									{ 
										_timeAgo += Day + " "; 
									}else{ 
										_timeAgo += "0" + Day; 
									} 
								}else if(comTime.days == 2){ 
									_timeAgo = "前天 ";
									if (Hour >= 10) 
									{ 
										_timeAgo += Hour + ":" ; 
									}else{ 
										_timeAgo += "0" + Hour + ":" ; 
									} 
									if (Minute >= 10) 
									{ 
										_timeAgo += Minute ; 
									}else{ 
										_timeAgo += "0" + Minute ; 
									} 
								}else if(comTime.days == 1){
									_timeAgo = "昨天 ";
									if (Hour >= 10) 
									{ 
										_timeAgo += Hour + ":" ; 
									}else{ 
										_timeAgo += "0" + Hour + ":" ; 
									} 
									if (Minute >= 10) 
									{ 
										_timeAgo += Minute ; 
									}else{ 
										_timeAgo += "0" + Minute ; 
									} 
								}else if(comTime.days <= 0 && comTime.hours > 0){
									_timeAgo = comTime.hours + " 小时前 ";
								}else if(comTime.days <= 0 && comTime.hours <= 0){
									if(comTime.minutes < 5){
										_timeAgo = " 刚刚";
									}else{
										_timeAgo = comTime.minutes + " 分钟前 ";
									}
								}
								
								var _stateLabel = $li.attr("_stateLabel");
								var $pendingBox = $(".tab-content").find("#sysFlowTab_pending>ul");
								var tabLi = Jh.Util.format(_template.tabLi , {'tabID' : _flowname});
								tabLi = Jh.Util.format(tabLi , {'tabCon' : $li.text()});
								tabLi = Jh.Util.format(tabLi , {'tabUrl' : _url});
								tabLi = Jh.Util.format(tabLi , {'tabName' : _flowname});
								tabLi = Jh.Util.format(tabLi , {'tabTime' : _timeAgo});
								tabLi = Jh.Util.format(tabLi , {'tabDocID' : _tabDocID});
								tabLi = Jh.Util.format(tabLi , {'tabIsRead' : _isRead});
								if(_isRead == "false"){
									tabLi = Jh.Util.format(tabLi , {'tabIsReadClass' : 'noread'});
								}else{
									tabLi = Jh.Util.format(tabLi , {'tabIsReadClass' : 'isread'});
								}
								tabLi = Jh.Util.format(tabLi , {'tabIsReadClass' : _isRead});
								tabLi = Jh.Util.format(tabLi , {'tabIsReadText' : _isRead});
								tabLi = Jh.Util.format(tabLi , {'tabInitiator' : _initiator});
								tabLi = Jh.Util.format(tabLi , {'tabInitiatorID' : _initiatorID});
								tabLi = Jh.Util.format(tabLi , {'tabAvatar' : _avatar});
								tabLi = Jh.Util.format(tabLi , {'tabDept' : _initiatorDept});
								tabLi = Jh.Util.format(tabLi , {'tabState' : _stateLabel});
							//if( i < 5 ){
								pendingLi += tabLi;	
							}else{
								$("#sysFlowTab_pending").find(".btn").show();
							};
							
							$li.remove();
						});
						
						
						if($(".nav-tabs").find("a[aria-controls='pending']>.flowNum").text()=="(0)"){
							$(".widget-tab").find("#sysFlowTab_pending>ul").append(_template.tabSpacePending);
							$(".nav-tabs").find("a[aria-controls='pending']>.flowNum").remove();
						}
						
						$(".widget-tab").find("#sysFlowTab_pending>ul").append(pendingLi)
						$(".widget-tab").find("#sysFlowTab_pending li.widgetItem").unbind().bind("click",function(){
							var $this = $(this);
							var url = $this.attr("_url");
							window.location.href=url;
						});
						$(".widget-tab").find("#sysFlowTab_pending>.bottom-menu>a").click(function(){
							window.location.href="#flowCenter";
							$("#flowCenter").find("div.weui_navbar_item[_for='pending']").trigger("touchend");
						});
					}
					if(status=="error"){
						
					}
					$accordionPending.remove();
				});
			});
			}, 1);
			setTimeout(function(){
				$processedList.each(function(){
					$accordionProcessing.load($processedList.attr("_url"), function(response,status) {
						var $accorProcess = $("#accordion-processing");
						var processingLi = "";
						if(status=="success"){
							var $lis = $accorProcess.find("li");
							var count = $lis.size();
							if(count != 0){
								if(count > 9){
									count = "9+";
								}
								$(".nav-tabs").find("a[aria-controls='processing']>.flowNum").text("("+ count +")");	//设置总数
							}
							$lis.each(function(i){
								
								var $li = $(this);
								var _flowname = $li.attr("_flowname");
								var _url = $li.attr("_url");
								var _tabDocID = $li.attr("_docid");
								var _initiator = $li.attr("_initiator");
								var _initiatorID = $li.attr("_initiatorId");
								var _initiatorDept = $li.attr("_initiatorDept");
								var _time = $li.attr("_lastprocesstime");
								var _isRead = $li.attr("_isRead");
								
								if( i < 5 ){
									var flowTime = new Date(_time);
									var timeFixArr = _time.split(/[- :]/); 
									var timeFixDate = new Date(timeFixArr[0], timeFixArr[1]-1, timeFixArr[2], timeFixArr[3], timeFixArr[4]);
									var _timeAgo,_avatar;
									var avatar = Common.Util.getAvatar(_initiatorID);
									var Month = timeFixDate.getMonth() + 1; 
									var Day = timeFixDate.getDate(); 
									var Hour = timeFixDate.getHours(); 
									var Minute = timeFixDate.getMinutes(); 
									if(avatar!="" && avatar!=undefined){
										_avatar = "<img src ="+avatar+">";
									}else{
										_avatar = "<div class='noAvatar'>"+ _initiator.substr(_initiator.length-2, 2) +"</div>";
									}
									
									//比较时间 serviceTime定义在main.jsp
					 				var comTime = Common.Util.daysCalc(_time,serviceTime);

									if(comTime.days > 2){
										if (Month >= 10){ 
											_timeAgo = Month + "-"; 
										}else{ 
											_timeAgo = "0" + Month + "-"; 
										} 
										if (Day >= 10) 
										{ 
											_timeAgo += Day + " "; 
										}else{ 
											_timeAgo += "0" + Day; 
										} 
									}else if(comTime.days == 2){ 
										_timeAgo = "前天 ";
										if (Hour >= 10) 
										{ 
											_timeAgo += Hour + ":" ; 
										}else{ 
											_timeAgo += "0" + Hour + ":" ; 
										} 
										if (Minute >= 10) 
										{ 
											_timeAgo += Minute ; 
										}else{ 
											_timeAgo += "0" + Minute ; 
										} 
									}else if(comTime.days == 1){
										_timeAgo = "昨天 ";
										if (Hour >= 10) 
										{ 
											_timeAgo += Hour + ":" ; 
										}else{ 
											_timeAgo += "0" + Hour + ":" ; 
										} 
										if (Minute >= 10) 
										{ 
											_timeAgo += Minute ; 
										}else{ 
											_timeAgo += "0" + Minute ; 
										} 
									}else if(comTime.days <= 0 && comTime.hours > 0){
										_timeAgo = comTime.hours + " 小时前 ";
									}else if(comTime.days <= 0 && comTime.hours <= 0){
										if(comTime.minutes < 5){
											_timeAgo = " 刚刚";
										}else{
											_timeAgo = comTime.minutes + " 分钟前 ";
										}
									}
									
									var _stateLabel = $li.attr("_stateLabel");
									
									var $processingBox = $(".tab-content").find("#sysFlowTab_processing>ul");
									
									var tabLi = Jh.Util.format(_template.tabLi , {'tabID' : _flowname});
									tabLi = Jh.Util.format(tabLi , {'tabCon' : $li.text()});
									tabLi = Jh.Util.format(tabLi , {'tabUrl' : _url});
									tabLi = Jh.Util.format(tabLi , {'tabName' : _flowname});
									tabLi = Jh.Util.format(tabLi , {'tabTime' : _timeAgo});
									tabLi = Jh.Util.format(tabLi , {'tabDocID' : _tabDocID});
									tabLi = Jh.Util.format(tabLi , {'tabIsRead' : _isRead});
									if(_isRead == "true"){
										tabLi = Jh.Util.format(tabLi , {'tabIsReadClass' : 'tabLiCon-isread'});
										tabLi = Jh.Util.format(tabLi , {'tabIsReadText' : '已阅'});
									}else{
										tabLi = Jh.Util.format(tabLi , {'tabIsReadClass' : 'tabLiCon-noread'});
										tabLi = Jh.Util.format(tabLi , {'tabIsReadText' : '未阅'});
									}
									tabLi = Jh.Util.format(tabLi , {'tabInitiator' : _initiator});
									tabLi = Jh.Util.format(tabLi , {'tabInitiatorID' : _initiatorID});
									tabLi = Jh.Util.format(tabLi , {'tabAvatar' : _avatar});
									tabLi = Jh.Util.format(tabLi , {'tabDept' : _initiatorDept});
									tabLi = Jh.Util.format(tabLi , {'tabState' : _stateLabel});
					
									processingLi += tabLi;	
								}else{
									$("#sysFlowTab_processing").find(".btn").show();
								};
								$li.remove();
							});	
							
							if($(".nav-tabs").find("a[aria-controls='processing']>.flowNum").text()=="(0)"){
								$(".widget-tab").find("#sysFlowTab_processing>ul").append(_template.tabSpaceProcessing);
								$(".nav-tabs").find("a[aria-controls='processing']>.flowNum").remove();
							}
							
							$(".widget-tab").find("#sysFlowTab_processing>ul").append(processingLi)
							$(".widget-tab").find("#sysFlowTab_processing li.widgetItem").unbind().bind("click",function(){
								var $this = $(this);
								var url = $this.attr("_url");
								window.location.href=url;
							});
							$(".widget-tab").find("#sysFlowTab_processing>.bottom-menu>a").click(function(){
								window.location.href="#flowCenter";
								$("#flowCenter").find("div.weui_navbar_item[_for='processing']").trigger("touchend");
							});
						}
						if(status=="error"){
							
						}
						$accordionProcessing.remove();
					});
				});
			}, 1);
			

			$me.find("li.widget-workflow-slide:eq(0)").addClass("active");
			$me.find("div.tab-pane:eq(0)").addClass("active");
		},
		
		_eventRefresh:function(){	
			
			
			
			$("#home_link .groupWrapper").find("div.widget-tab").each(function(){
				var _me = $(this);
				var refreshUrl = "../widget/displayWidget.action?id="+_me.attr("id");
				_me.load(refreshUrl, function(data){
					var $content = $(this).find(".widgetContent");
					switch($content.attr("_type")) {
						case "summary":
							me._renderSummary($content);
						break;
						case "view":
							me._renderView($content);
						break;
						case "link":
							me._renderLink($content);
						break;
						case "extlink":
							me._renderExtLink($content);
						break;
						case "announcement":
							me._renderAnnouncement($content);
						break;
						case "workflow":
							me._renderWorkflow($content);
						break;
						case "weather":
							me._renderWeather($content);
						break;
					}
				});
			})
		}
	};
}();

function loadHome(){
	$.get("./homepage.jsp?application="+$("#applicationId").val(),function(text){
		$("#homePage").html("");
		var $homePage = $("<div>"+text+"</div>");
		var defaultCfg = eval("("+$homePage.find("#defaultCfg").val()+")");
		try {
			var userCfg = $.trim($homePage.find("#templateElement").val());
							
			//兼容平台旧数据，是旧数据时重置首页
			if(userCfg.indexOf(";")>0){
				userCfg="";
			}
			
			if (userCfg.length>0) {
				userCfg = $.parseJSON(userCfg);
				defaultCfg.layoutStyle = userCfg.layoutStyle;
			}
			Jh.Portal.init(userCfg, defaultCfg);

		} catch(e) {
			alert("JSON:"+e);
		}
	});
	
	
    //控制菜单
    $('.phone-main-nav-trigger').on('click', function() {
        triggerBouncyNav(true);
    });
    $('.phone-main-nav-close').on('click', function() {
        triggerBouncyNav(false);
    });
    $('.phone-main-nav-modal').on('click', function(event) {
        if ($(event.target).is('.phone-main-nav-modal')) {
        	triggerBouncyNav(false);
        }
    });
    
}

function triggerBouncyNav($bool) {
    if (!is_bouncy_nav_animating) {
        is_bouncy_nav_animating = true;
        $('.phone-main-nav-modal').toggleClass('fade-in', $bool).toggleClass('fade-out', !$bool).find('li:last-child').one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function() {
        	$('.phone-main-nav-modal').toggleClass('is-visible', $bool);
            if (!$bool)
                $('.phone-main-nav-modal').removeClass('fade-out');
            is_bouncy_nav_animating = false;
        });
        if ($('.phone-main-nav-trigger').parents('.no-csstransitions').length > 0) {
            $('.phone-main-nav-modal').toggleClass('is-visible', $bool);
            is_bouncy_nav_animating = false;
        }
        $('.phone-main-nav-trigger').toggle();
    	$('.phone-main-nav-close').toggle();
    }
}; 

/**
 * 加载菜单
 */
function loadMenu(){
	$.get("./menu.jsp?application="+$("#applicationId").val(),function(text){
		//$("#menu").html(text).enhanceWithin();
		var $menu = $("#menu");
		$menu.html(text);
		//单击部门名称展开|折叠下级
		$menu.find(".menulist").on("click",".menu",function(e){
//			e.preventDefault();
			e.stopPropagation();
			$(this).toggleClass("open");
			$(">ul",$(this)).toggle();
		});
		
		var $appTitle = $menu.find(".appTitle");
		var $topBox = $menu.find(".top_menu_box");
		var $secondBox = $menu.find(".second_menu_box");
		var $thirdBox = $menu.find(".third_menu_box");
		
		$menu.find(".app_Title").html("<div><i class='iconfont-h5'>&#xe038;</i>" + $appTitle.text() + "</div><div class='title_line'></div>");
		
		$menu.find("li.topMenuItem").each(function(){
			var $this = $(this);

			if(($this).find(">ul>li").size()<=0){
				var $topMenuItem = $("<span id='icon_" + $this.attr("id") + "' data-placement='bottom' title='" + $this.find(">.topMenu_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-00'></div><!--<img src='resource/images/icon-form.png'>--></i><i class='menuLiTxt'>" + $this.find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
			}else{
				var $topMenuItem = $("<span id='icon_" + $this.attr("id") + "' data-placement='bottom' title='" + $this.find(">.topMenu_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-01'></div><!--<img src='resource/images/icon-menu-01.png'>--></i><i class='menuLiTxt'>" + $this.find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
			}

			//menu1
			$topMenuItem.click(function(){
				var topMenuId = $(this).attr("id").substring(5);
				
				var $topMenuLi = $menu.find("#"+topMenuId).find(">ul>li");
				$topBox.find("span").removeClass("active");
				$(this).addClass("active");
				//$secondBox.slideUp();
				$secondBox.empty();
				$thirdBox.empty();
				
				
				if ($topMenuLi.size()<=0) {
					urlLink(topMenuId);
				}
				else {
					
					$topMenuLi.each(function(){
						var $this = $(this);
						//$this
						if($(this).find(">ul>li").size()<=0){
							var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "' data-placement='bottom' title='" + $this.find(">.second_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-00'></div><!--<img src='resource/images/icon-form.png'>--></i><i class='menuLiTxt'>" + $this.find(">.second_title").text() + "</i></span>").appendTo($secondBox);
						}else{
							var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "' data-placement='bottom' title='" + $this.find(">.second_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-02'></div><!--<img src='resource/images/icon-menu-02.png'>--></i><i class='menuLiTxt' >" + $this.find(">.second_title").text() + "</i></span>").appendTo($secondBox);
						}
						
						//menu2
						$secondMenuItem.click(function(){
							var secondMenuId = $(this).attr("id").substring(5);
							var $secondMenuLi = $menu.find("#"+secondMenuId).find(">ul>li");
							$secondBox.find("span").removeClass("active");
							$(this).addClass("active");
							//$thirdBox.slideUp();
							$thirdBox.empty();
							
							if ($secondMenuLi.size()<=0){
								urlLink(secondMenuId);
							}
							else {
								$secondMenuLi.each(function(){
									var $thirdMenuItem = $("<span id='icon_" + $(this).attr("id") +"' data-placement='bottom' title='" + $(this).find(">.third_title").text() +"'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-00'></div><!--<img src='resource/images/icon-form.png'>--></i>" + $(this).find(">.third_title").text() + "</span>").appendTo($thirdBox);
									//menu3
									$thirdMenuItem.click(function(){
										var thirdMenuId = $(this).attr("id").substring(5);
										urlLink(thirdMenuId);
									});
									$thirdBox.slideDown("fast");
								});
							}
						});
						
					});
					$secondBox.slideDown("fast");	
				}
			});
		});
		//隐藏无菜单的软件
		$menu.find(".menu_dl").each(function(){
			if($(this).find(".menu .topMenuItem").size()<=0){
				$(this).find(".app").css("display","none");
			}else{
				$menu.find(".noApp").css("display","none");	//隐藏无发起内容的提示
			}
		});

		function urlLink(liId){
			var $a = $menu.find("#"+liId);
			var url = $a.attr("_href");
			window.location.href=url;
		};

	});
}; 
/**待办
 * 原数据结构：
 * <ul>软件
 * 	<li>待办：属性_flowName为流程名，
 *		根据流程名创建li，多个待办共用相同流程名
 * 返回数据结构：
 * <ul>
 * 	<li>软件名
 *	<li>流程名
 *		<span>数量
 *		<ul>
 *			<li>待办
 */
FlowCenter = {
	multilingual : {},
	_click : function(scope, tabName){
		var $scope = $("#" + scope);
	 	$scope.find("a[aria-controls='"+tabName+"']").parent("li").siblings().removeClass("active");
	 	$scope.find("a[aria-controls='"+tabName+"']").parent("li").addClass("active");
	 	$scope.find(".tab-content").find("#"+tabName).siblings().removeClass("active");
	 	$scope.find(".tab-content").find("#"+tabName).addClass("active");
	},
	format : function(str, model) {//格式化指定键值的模板
	 	for (var k in model) {
	 		var re = new RegExp("{" + k + "}", "g");
	 		str = str.replace(re, model[k]);
	 	}
	 	return str;
	 },
	 render2P : function(scope){
	 	//构建内容
	 	$("#"+scope +"App ul[_appid='"+$("#applicationId").val()+"']").each(function(i){
	 		$("#pending, #processing").html("");
	 		var $scope = $("#"+scope);
	 		var $app = $(this);
	 		var _appID = $app.attr("_appid");
	 		var _appName = $app.attr("_appname");
	 		var _appUrl = $app.attr("_url");

	 		var _template = {
	 				appPanel : '<div class="pending-app-tab" role="tabpanel"></div>',
	 				appHeaderBox : '<ul class="pending-app-nav nav nav-tabs" role="tablist" style="display:none"></ul>',
	 				appHeaderNav :	'<li role="presentation">'
	 							+'<a onclick="FlowCenter._click(\''+scope +'\',\'{appID}\');" _href="#{appID}" aria-controls="{appID}" role="tab" data-toggle="tab">{appName}</a>'
	 							+'</li>',
	 				appBodyBox : '<div class="pending-app-content tab-content"></div>',
	 				appBodyCon : '<div role="tabpanel" class="tab-pane" id="{appID}">'
	 							+'<div class="pending-tab swiper-container" role="tabpanel"><ul class="pending-nav nav nav-tabs swiper-wrapper" role="tablist" style="display: none;">'
	 							+'<li class="swiper-slide" role="presentation"><a onclick="FlowCenter._click(\''+scope +'\',\''+scope +'-'+_appID+'\');" _href="#'+scope +'-'+_appID+'" aria-controls="'+scope +'-'+_appID+'" role="tab" data-toggle="tab">全部<span></span></a></li></ul></div></div>',
	 							
	 				tabHeaderNav : '<li class="swiper-slide" role="presentation">'
	 								+'<a onclick="FlowCenter._click(\''+scope +'\',\'{tabID}\');" _href="#{tabID}" aria-controls="{tabID}" role="tab" data-toggle="tab">{tabTitle}<span></span></a>'
	 								+'</li>',
	 				
	 				appFilterBox : '<div class="pending-app-filter text-center">'
				 					+'<div class="pending-app-filter-item" data-filter="all"><i class="iconfont if-phone-updown"></i>全部 <span class="pending-app-filter-arrow">▲</span></div>'
				 					+'<div class="pending-app-filter-item" data-filter="filter"><i class="iconfont if-phone-filter"></i>筛选 <span class="pending-app-filter-arrow">▲</span></div>'
				 					+'</div>',
	 				 								
	 				tabBodyBox : '<div class="pending-content tab-content"></div>',
	 				
	 				tabBodyAll : '<div role="tabpanel" class="tab-pane" id="'+scope +'-'+_appID+'" style="padding-bottom: 50px;"><ul></ul></div>',
	 				
	 				tabBodyCon : '<div role="tabpanel" class="tab-pane" id="{tabID}"><ul></ul></div>',
	 									
					tabBodyLi : '<li class="widgetItem pending-list-con" id="{tabDocID}" _url="{tabUrl}" _isRead="{tabIsRead}">'
								+ '<div class="tabLiFace">{tabAvatar}'
								+ '<span class="{tabIsReadClass}"></span>'
								+ '</div>'
								+ '<div class="tabLiCon">'
								+ '<div class="tabLiConBox">'
								+ '<div class="tabLiConA text-left">'
								+ '<span class="tabLiCon-text">[{tabName}] {tabCon}</span>'

								+ '</div>'
								+ '<div class="tabLiConB"><div class="tabLiTagLeft">'
								+ '<span class="tabLiCon-auditornames" _initiator="{tabInitiator}" _initiatorId="{tabInitiatorID}">{tabDept}{tabInitiator}</span>'
								+ '<span class="tabLiCon-lastprocesstime timeago" title="{tabTime}">{tabTime}</span>'
								+ '</div>'
								+ '<div class="tabLiTagRight text-right">'
								+ '<span class="tabLiCon-status" title="{tabState}">{tabState}</span>'
								+ '</div>'
								+ '</div>'
								+ '</li>',
	 				
	 				tabBtnLeft : '<div class="tabs-button-prev"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span></div>',
	 				tabBtnright : '<div class="tabs-button-next"><span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span></div>',
	 				tabSpace : '<div id="content-load-panel"><div id="content-space" class="text-center">'
	 							+ '<div class="content-space-icon icon iconfont">&#xe61b;</div>'
	 							+ '<div class="content-space-h1">当前没有任何信息</div>'
	 							+ '<div class="content-space-h2">需要处理的信息将显示在这里</div></div></div>'

	 		}
	 		
	 		if($scope.find(".pending-app-tab").size()==0){
	 			var $appPanel = $(_template.appPanel).append(_template.appHeaderBox);
	 			$appPanel.append(_template.appBodyBox);
	 			$appPanel.find(".pending-tab").append(_template.tabBodyBox);
	 			$scope.append($appPanel);
	 		}
	 		
	 		var appHeaderNav = FlowCenter.format(_template.appHeaderNav , {'appID' : _appID});
	 			appHeaderNav = FlowCenter.format(appHeaderNav , {'appName' : _appName});
	 		var appBodyCon = FlowCenter.format(_template.appBodyCon , {'appID' : _appID});
	 		
	 		$scope.find(".pending-app-tab").find(".pending-app-nav").append(appHeaderNav);
	 		$scope.find(".pending-app-tab").find(".pending-app-content").append(_template.appFilterBox);
	 		$scope.find(".pending-app-tab").find(".pending-app-content").append(appBodyCon);
	 		$scope.find(".pending-app-content").find(".pending-tab").each(function(){
	 			if($(this).find(".pending-content").size()==0){
	 				$(this).append(_template.tabBodyBox);
	 			}
	 		})

	 		var $tabList = $scope.find(".pending-app-content").find("div.tab-pane[id='"+_appID+"']");
	 		var $tabListBox = $("<div id='tabListBox-"+_appID+"'></div>");
	 		$tabList.append($tabListBox);
	 		$tabList.find(".pending-content").append(_template.tabBodyAll);
	 		
	 		$tabListBox.load(_appUrl, {"_pagelines" : 100}, function() {
	 			var $accor = $(this);
	 			var $lis = $accor.find("li");
	 			var count = $lis.size();
	 			
	 			if(count != 0){
	 				if(count > 99){
	 					count = "99+";
	 				}
	 				$("#flowCenter").find(".weui_navbar_item[_for='"+scope+"']").find(".navbar-item-box>span").text(count).show();	//设置总数
	 				$scope.find("div.tab-pane[id='"+_appID+"']").find(".nav-tabs").find("a[aria-controls='"+scope +"-"+_appID+"']>span").text(count);	//设置总数
	 			}else{
	 				$scope.html(_template.tabSpace);
	 				$("#"+scope).height($("#flow-center-panel").height());
	 			}
	 			
	 			var count = {};		//统计各流程数据
	 			$lis.each(function(i){
	 				var $li = $(this);
	 				var _flowname = $.trim($li.attr("_flowname"));
	 				var _url = $li.attr("_url");
	 				var _tabDocID = $li.attr("_docid");
	 				var _text = $li.text();
	 				var _stateLabel = $li.attr("_statelabel");
	 				var _name = $li.attr("_auditornames");
					var _initiator = $li.attr("_initiator");
					var _initiatorID = $li.attr("_initiatorId");
					var _initiatorDept = $li.attr("_initiatorDept");
					var _time = $li.attr("_lastprocesstime");
					var _isRead = $li.attr("_isRead");
	 				
					var flowTime = new Date(_time);
					var timeFixArr = _time.split(/[- :]/); 
					var timeFixDate = new Date(timeFixArr[0], timeFixArr[1]-1, timeFixArr[2], timeFixArr[3], timeFixArr[4]);
					var _timeAgo,_avatar;
					var avatar = Common.Util.getAvatar(_initiatorID);
					var Month = timeFixDate.getMonth() + 1; 
					var Day = timeFixDate.getDate(); 
					var Hour = timeFixDate.getHours(); 
					var Minute = timeFixDate.getMinutes(); 

	 				if(avatar!="" && avatar!=undefined){
						_avatar = "<img src ="+avatar+">";
					}else{
						_avatar = "<div class='noAvatar'>"+ _initiator.substr(_initiator.length-2, 2) +"</div>";
					}
	 				
	 				//比较时间 serviceTime定义在main.jsp
	 				var comTime = Common.Util.daysCalc(_time,serviceTime);

					if(comTime.days > 2){
						if (Month >= 10){ 
							_timeAgo = Month + "-"; 
						}else{ 
							_timeAgo = "0" + Month + "-"; 
						} 
						if (Day >= 10) 
						{ 
							_timeAgo += Day + " "; 
						}else{ 
							_timeAgo += "0" + Day; 
						} 
					}else if(comTime.days == 2){ 
						_timeAgo = "前天 ";
						if (Hour >= 10) 
						{ 
							_timeAgo += Hour + ":" ; 
						}else{ 
							_timeAgo += "0" + Hour + ":" ; 
						} 
						if (Minute >= 10) 
						{ 
							_timeAgo += Minute ; 
						}else{ 
							_timeAgo += "0" + Minute ; 
						} 
					}else if(comTime.days == 1){
						_timeAgo = "昨天 ";
						if (Hour >= 10) 
						{ 
							_timeAgo += Hour + ":" ; 
						}else{ 
							_timeAgo += "0" + Hour + ":" ; 
						} 
						if (Minute >= 10) 
						{ 
							_timeAgo += Minute ; 
						}else{ 
							_timeAgo += "0" + Minute ; 
						} 
					}else if(comTime.days <= 0 && comTime.hours > 0){
						_timeAgo = comTime.hours + " 小时前 ";
					}else if(comTime.days <= 0 && comTime.hours <= 0){
						if(comTime.minutes < 5){
							_timeAgo = " 刚刚";
						}else{
							_timeAgo = comTime.minutes + " 分钟前 ";
						}
					}
	 				var _lastprocesstime = $li.attr("_lastprocesstime");
	 				
	 				
	 				
	 				count[_flowname] = count[_flowname] ? (count[_flowname]+1) : 1;
	 				$panel = $("#"+_flowname);
	 				
	 				var tabHeaderNav = FlowCenter.format(_template.tabHeaderNav , {'tabID' : _flowname});
	 					tabHeaderNav = FlowCenter.format(tabHeaderNav , {'tabTitle' : _flowname});
	 				var tabBodyCon = FlowCenter.format(_template.tabBodyCon , {'tabID' : _flowname});
	 					tabBodyCon = FlowCenter.format(tabBodyCon , {'tabCon' : $li.text()});
	 					tabBodyCon = FlowCenter.format(tabBodyCon , {'tabUrl' : _url});
	 					tabBodyCon = FlowCenter.format(tabBodyCon , {'tabDocID' : _tabDocID});
	 					
	 					
	 				var tabBodyLi = FlowCenter.format(_template.tabBodyLi , {'tabID' : _flowname});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabCon' : $li.text()});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabIsRead' : _isRead});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabUrl' : _url});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabName' : _flowname});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabTime' : _timeAgo});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabDocID' : _tabDocID});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabIsRead' : _isRead});
						if(_isRead == "false"){
							tabBodyLi = FlowCenter.format(tabBodyLi , {'tabIsReadClass' : 'noread'});
						}else{
							tabBodyLi = FlowCenter.format(tabBodyLi , {'tabIsReadClass' : 'isread'});
						}
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabInitiator' : _initiator});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabInitiatorID' : _initiatorID});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabAvatar' : _avatar});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabDept' : _initiatorDept});
	 					tabBodyLi = FlowCenter.format(tabBodyLi , {'tabState' : _stateLabel});
	 					
	 
	 					
	 				$scope.find("div.tab-pane[id='"+_appID+"']").find(".pending-content").find("#"+scope +"-"+_appID).find("ul").append(tabBodyLi);
	 				if($panel.size()==0){	//没有创建相同流程名称内容时创建
	 					$scope.find("div.tab-pane[id='"+_appID+"']").find(".pending-nav").append(tabHeaderNav);
	 					$scope.find("div.tab-pane[id='"+_appID+"']").find(".pending-content").append(tabBodyCon);
	 					$scope.find("div.tab-pane[id='"+_appID+"']").find(".pending-content").find("#"+_flowname).find("ul").append(tabBodyLi);
	 				}else{
	 					$scope.find("div.tab-pane[id='"+_appID+"']").find(".pending-content").find("#"+_flowname).find("ul").append(tabBodyLi);
	 				}
	 				$scope.find("div.tab-pane[id='"+_appID+"']").find(".nav-tabs").find("a[aria-controls='"+_flowname+"']>span").text(count[_flowname]);	//设置总数
	 				$li.remove();
	 			})
	 			
	 			$scope.find("div.tab-pane[id='"+_appID+"']").find(".pending-content .pending-list-con").click(function(){
	 				var $this = $(this);
	 				var id = $this.attr("id");
	 				var title = $this.text();
	 				title = !title || title.trim().length == 0 ? "..." : title.trim();
	 				title = title.length > 5 ? title.substring(0, 5) + ".." : title;
	 				var url = $this.attr("_url");
	 				window.location.href =url;
	 			})
	 			$scope.find(".pending-app-nav").find("li:eq(0) a").trigger('click');
	 			$scope.find(".pending-nav").find("li:eq(0) a").trigger('click');
	 			
	 			$tabListBox.remove();
	 		})
	 	});
	 },
	/**
	 * 加载流程历史
	 */
	loadResult:function() {
		//添加软件名称
		var $app = $("#app"), url = "../dynaform/work/historyListNew.action";
		
	    var htmlobj=$.ajax({
	    	url:url,
	    	data:$("#searchForm").serializeArray(),
	    	dataType:"text", 
	    	async:false
	    });
		var $ul = $("<ul></ul>").append(htmlobj.responseText);
		FlowCenter.renderNew($ul);
	},
	/**
	 * 流程历史数据重构和渲染
	 */
	renderNew:function($ul) {
		
		if($ul.find("li").size()==0){
			$("#finished").find("#content-load-panel").show();
			if($("#finished").find("#content-load-panel").find(".content-space-h1").text()=="当前没有任何查询"){
				$("#finished").find("#content-load-panel").find(".icon-619").hide();
				$("#finished").find("#content-load-panel").find(".icon-61a").show();
				$("#finished").find("#content-load-panel").find(".content-space-h1").text("当前没有历史记录");
				$("#finished").find("#content-load-panel").find(".content-space-h2").text("请重新输入关键字查询");
			}
		}else{
			$("#finished").find("#content-load-panel").hide();
		}

		$finishedBox = $("#finished");
		$listViewBox = "<div id='ui-content' class='card_app'><ul data-role='listview' id='resultUl' class='table-view'></ul></div>"
		$finishedBox.find("#ui-content").remove();
		$finishedBox.append($listViewBox);
		
		$listView = $("#resultUl");
		$listView.html("");	//清空原有数据

		$ul.find("li").each(function(){
			var $li = $(this);
			var url = $li.attr("_url");
			var text = $li.text();
			var flowName = $li.attr("_flowName");
			var stateLabel = $li.attr("_stateLabel");
			var _time = $li.attr("_lastProcessTime");
			var _applicationId=$li.attr("_applicationId");
			var _initiator = $li.attr("_initiator");
			var _initiatorID = $li.attr("_initiatorId");
			var _initiatorDept = $li.attr("_initiatorDept");
			var _isRead = $li.attr("_isRead");
			
			var _timeAgo,_avatar,_tabIsReadClass;
			
			var proTime = new Date(_time);
			var timeFixArr = _time.split(/[- :]/); 
			var timeFixDate = new Date(timeFixArr[0], timeFixArr[1]-1, timeFixArr[2], timeFixArr[3], timeFixArr[4]);
			var Month = timeFixDate.getMonth() + 1; 
			var Day = timeFixDate.getDate(); 
			var Hour = timeFixDate.getHours(); 
			var Minute = timeFixDate.getMinutes();
			
			
			var avatar = Common.Util.getAvatar(_initiatorID);
			if(avatar!="" && avatar!=undefined){
				_avatar = "<img src ="+avatar+">";
			}else{
				_avatar = "<div class='noAvatar'>"+ _initiator.substr(_initiator.length-2, 2) +"</div>";
			}
			
			//比较时间 serviceTime定义在main.jsp
			var comTime = Common.Util.daysCalc(_time,serviceTime);

			if(comTime.days > 2){
				if (Month >= 10){ 
					_timeAgo = Month + "-"; 
				}else{ 
					_timeAgo = "0" + Month + "-"; 
				} 
				if (Day >= 10) 
				{ 
					_timeAgo += Day + " "; 
				}else{ 
					_timeAgo += "0" + Day; 
				} 
			}else if(comTime.days == 2){ 
				_timeAgo = "前天 ";
				if (Hour >= 10) 
				{ 
					_timeAgo += Hour + ":" ; 
				}else{ 
					_timeAgo += "0" + Hour + ":" ; 
				} 
				if (Minute >= 10) 
				{ 
					_timeAgo += Minute ; 
				}else{ 
					_timeAgo += "0" + Minute ; 
				} 
			}else if(comTime.days == 1){
				_timeAgo = "昨天 ";
				if (Hour >= 10) 
				{ 
					_timeAgo += Hour + ":" ; 
				}else{ 
					_timeAgo += "0" + Hour + ":" ; 
				} 
				if (Minute >= 10) 
				{ 
					_timeAgo += Minute ; 
				}else{ 
					_timeAgo += "0" + Minute ; 
				} 
			}else if(comTime.days <= 0 && comTime.hours > 0){
				_timeAgo = comTime.hours + " 小时前 ";
			}else if(comTime.days <= 0 && comTime.hours <= 0){
				if(comTime.minutes < 5){
					_timeAgo = " 刚刚";
				}else{
					_timeAgo = comTime.minutes + " 分钟前 ";
				}
			}
			
			if(_isRead == "false"){
				_tabIsReadClass = "noread";
			}else{
				_tabIsReadClass = "isread";
			}
			
			var li = '<li class="table-view-cell"><a data-ignore="push" class="navigate-right" href="'+url+'">'
					+ '<div class="tabLiFace">' + _avatar + '<span class="' + _tabIsReadClass + '"></span></div>'
					+ '<div class="tabLiCon"><div class="tabLiConBox"><div class="tabLiConA text-left">'
					+ '[' + flowName + ']&nbsp;' + text + '</div>'
					+ '<div class="tabLiConB"><div class="tabLiTagLeft"><span class="tabLiCon-auditornames" >' + _initiatorDept + _initiator + '</span>'
					+ '<span class="tabLiCon-lastprocesstime timeago" title="' + _timeAgo + '">' + _timeAgo + '</span></div><div class="tabLiTagRight text-right">'
					+ '<span class="tabLiCon-status" title="">' + stateLabel + '</span></div></div></div></div>'
					+'</a></li>';

			$(li).appendTo($listView);		
			
		});
		if($ul.find("li").size()==0){
			$finishedBox.find("#ui-content").remove();
		}
		$ul.remove();
		$("#ui-content").html($("#ui-content").html());	
	},

	/**
	 * 流程发起打开链接
	 * @param liId
	 */
	urlLink : function(liId){
		var $a = $("#startFlow").find("#"+liId);	
		var url = $a.attr("_href");
		window.location.href = url;
	},
		/**
		 *	加载发起菜单
		 * @param $startFlow
		 */
	initStartFlow : function(){
		var $startFlow = $("#startFlow");
		$startFlow.find("dl.menu_dl#appNum_"+$("#applicationId").val()).each(function(){
			
			var $appBoxId = $(this).attr("id");
			$startFlow.find(".launch-tab").append("<li>"
									+"<a id='"+ $appBoxId +"_title' class='app_Title' href='#"+ $appBoxId+"_List' role='tab' data-toggle='tab'></a></li>"
									);		
			$startFlow.find(".launch-tab-content").append("<div id='"+ $appBoxId +"_List' class='app_List tab-pane'>"
					+ "<div class='top_menu_box clearfix'></div>"
					+ "<div class='second_menu_box clearfix' style='display:none'></div>"
					+ "<div class='third_menu_box clearfix' style='display:none'></div></div>");		
					
			var $appTitle = $(this).find(".appTitle");
			var $topBox = $startFlow.find("#"+$appBoxId+"_List>.top_menu_box");
			var $secondBox = $startFlow.find("#"+$appBoxId+"_List>.second_menu_box");
			var $thirdBox = $startFlow.find("#"+$appBoxId+"_List>.third_menu_box");
			
			$startFlow.find("#"+ $appBoxId +"_title").html("<div><i class='iconfont-h5'>&#xe038;</i>" + $appTitle.text() + "</div><div class='title_line'></div>");
			
			$(this).find("li.topMenuItem").each(function(){
				var $this = $(this);
				
				if(($this).find(">ul>li").size()<=0){
					var $topMenuItem = $("<span id='icon_" + $this.attr("id") + "' data-placement='bottom' title='" + $this.find(">.topMenu_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-00'></div><!--<img src='resource/images/icon-form1.png'>--></i><i class='menuLiTxt'>" + $this.find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
				}else{
					var $topMenuItem = $("<span id='icon_" + $this.attr("id") + "' data-placement='bottom' title='" + $this.find(">.topMenu_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-01'></div><!--<img src='resource/images/icon-startflow-01.png'>--></i><i class='menuLiTxt'>" + $this.find(">.topMenu_title").text() + "</i></span>").appendTo($topBox);
				}

				//menu1
				$topMenuItem.click(function(){
					var topMenuId = $(this).attr("id").substring(5);
					var $topMenuLi = $startFlow.find("#"+topMenuId).find(">ul>li");
					$topBox.find("span").removeClass("active");
					$(this).addClass("active");
					//$secondBox.slideUp();
					$secondBox.empty();
					$thirdBox.empty();
					
					
					if ($topMenuLi.size()<=0) {
						FlowCenter.urlLink(topMenuId);
					}
					else {
						
						$topMenuLi.each(function(){
							if($(this).find(">ul>li").size()<=0){
								var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "' data-placement='bottom' title='" + $(this).find(">.second_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-00'></div><!--<img src='resource/images/icon-form1.png'>--></i><i class='menuLiTxt'>" + $(this).find(">.second_title").text() + "</i></span>").appendTo($secondBox);
							}else{
								var $secondMenuItem = $("<span id='icon_" + $(this).attr("id") + "' data-placement='bottom' title='" + $(this).find(">.second_title").text() + "'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-02'></div><!--<img src='resource/images/icon-startflow-02.png'>--></i><i class='menuLiTxt' >" + $(this).find(">.second_title").text() + "</i></span>").appendTo($secondBox);
							}
							
							//menu2
							$secondMenuItem.click(function(){
								var secondMenuId = $(this).attr("id").substring(5);
								var $secondMenuLi = $startFlow.find("#"+secondMenuId).find(">ul>li");
								$secondBox.find("span").removeClass("active");
								$(this).addClass("active");
								//$thirdBox.slideUp();
								$thirdBox.empty();
								if ($secondMenuLi.size()<=0){
									FlowCenter.urlLink(secondMenuId);
								}
								else {
									$secondMenuLi.each(function(){
										var $thirdMenuItem = $("<span id='icon_" + $(this).attr("id") +"' data-placement='bottom' title='" + $(this).find(">.third_title").text() +"'><i class='menuLiIcon weui_bar_item_on'><div class='icon iconfont iconfont-menu iconfont-menu-00'></div><!--<img src='resource/images/icon-form1.png'>--></i>" + $(this).find(">.third_title").text() + "</span>").appendTo($thirdBox);
										//menu3
										$thirdMenuItem.click(function(){
											var thirdMenuId = $(this).attr("id").substring(5);
											FlowCenter.urlLink(thirdMenuId);
										});
										$thirdBox.slideDown("fast");
									});
								}
							});
						});
						$secondBox.slideDown("fast");
					}
				});
			});
		});
		//tab第一个增加class类“active”
		$startFlow.find(".launch-tab li").eq(0).addClass("active");
		$startFlow.find(".launch-tab-content .tab-pane").eq(0).addClass("active");
		
		//隐藏无菜单的软件
		$startFlow.find(".menu_dl").each(function(){
			if($(this).find(".menu .topMenuItem").size()<=0){
				$(this).find(".app").css("display","none");
			}else{
				$startFlow.find(".noApp").css("display","none");	//隐藏无发起内容的提示
			}
		});
	},
	initPending : function(){
		var _applicationId = $("#applicationId").val();
		$("#pendingApp ul[_appid='"+_applicationId+"']").each(function(){
			$(this).bind("refresh", function(event) {
				FlowCenter.render2P("pending");
			}).trigger("refresh");
		});
	},
	initProcessing : function(){
		var _applicationId = $("#applicationId").val();
		$("#processingApp ul[_appid='"+_applicationId+"']").each(function(){
			$(this).bind("refresh", function(event) {
				FlowCenter.render2P("processing");
			}).trigger("refresh");
		});
	},
	initFinished : function(){

		$("#finished").find("#content-load-panel").height($("#flowCenter").height()-166);
		FlowCenter.loadResult();
		//绑定查询按钮
		$("#searchBtn").click(function() {
			FlowCenter.loadResult();
			return false;
		});
		//切换我发起的
		$("#finish-pend").on("click",function(){
			var $this = $(this);
			if($this.hasClass("check")){
				$this.removeClass("check");
				$this.find(".finish-pend-icon").html('<i class="fa fa-square-o" aria-hidden="true"></i>');
				$("#_isMyWorkFlow").val("");
			}else{
				$this.addClass("check");
				$this.find(".finish-pend-icon").html('<i class="fa fa-check-square-o" aria-hidden="true"></i>');
				$("#_isMyWorkFlow").val("1");
			}
			FlowCenter.loadResult();
			return false;
		});
	},
	addListen : function(){

		//流程中心页头切换操作
		$(".weui_navbar").find(".weui_navbar_item").on("touchend",function(){
			$(this).addClass("weui_bar_item_on");
			$(this).siblings(".weui_navbar_item").removeClass("weui_bar_item_on");
			$("div[name=flowCenterDiv]").css("display","none");
			var val = $(this).attr("_for");
			$("#"+val).css("display","");
			switch (val) {
			case "pending":
				$("#pendingApp ul").trigger("refresh");
				break;

			case "processing":
				$("#processingApp ul").trigger("refresh");
				break;

			default:
				break;
			}
		});

		$("#flowCenter").on("touchend",".pending-app-filter-item",function(){
			var $this = $(this);
			var filter = $this.data("filter");
			var $pendingNav = $this.parents(".pending-app-content").find(".tab-pane.active").find("ul.pending-nav");
			$(".pending-app-filter-item").removeClass("active");
			if($pendingNav.is(":visible")){
				
			}else{
				$this.addClass("active");
			}
			$pendingNav.slideToggle(function(){
				if($pendingNav.is(":visible")){
					$pendingNav.css("display","block");
				}
			});
		})
	},
	/**
	 * 流程中心初始化
	 */
	init:function() {
		//发起
		this.initStartFlow();

		//待办
		this.initPending();
		
		//经办
		this.initProcessing();
		
		//历史
		this.initFinished();
		
		//事件监听
		this.addListen();
	}
};
; 
/**
 * Swiper 3.3.1
 * Most modern mobile touch slider and framework with hardware accelerated transitions
 * 
 * http://www.idangero.us/swiper/
 * 
 * Copyright 2016, Vladimir Kharlampidi
 * The iDangero.us
 * http://www.idangero.us/
 * 
 * Licensed under MIT
 * 
 * Released on: February 7, 2016
 */
!function(){"use strict";function e(e){e.fn.swiper=function(a){var s;return e(this).each(function(){var e=new t(this,a);s||(s=e)}),s}}var a,t=function(e,s){function r(e){return Math.floor(e)}function i(){y.autoplayTimeoutId=setTimeout(function(){y.params.loop?(y.fixLoop(),y._slideNext(),y.emit("onAutoplay",y)):y.isEnd?s.autoplayStopOnLast?y.stopAutoplay():(y._slideTo(0),y.emit("onAutoplay",y)):(y._slideNext(),y.emit("onAutoplay",y))},y.params.autoplay)}function n(e,t){var s=a(e.target);if(!s.is(t))if("string"==typeof t)s=s.parents(t);else if(t.nodeType){var r;return s.parents().each(function(e,a){a===t&&(r=t)}),r?t:void 0}if(0!==s.length)return s[0]}function o(e,a){a=a||{};var t=window.MutationObserver||window.WebkitMutationObserver,s=new t(function(e){e.forEach(function(e){y.onResize(!0),y.emit("onObserverUpdate",y,e)})});s.observe(e,{attributes:"undefined"==typeof a.attributes?!0:a.attributes,childList:"undefined"==typeof a.childList?!0:a.childList,characterData:"undefined"==typeof a.characterData?!0:a.characterData}),y.observers.push(s)}function l(e){e.originalEvent&&(e=e.originalEvent);var a=e.keyCode||e.charCode;if(!y.params.allowSwipeToNext&&(y.isHorizontal()&&39===a||!y.isHorizontal()&&40===a))return!1;if(!y.params.allowSwipeToPrev&&(y.isHorizontal()&&37===a||!y.isHorizontal()&&38===a))return!1;if(!(e.shiftKey||e.altKey||e.ctrlKey||e.metaKey||document.activeElement&&document.activeElement.nodeName&&("input"===document.activeElement.nodeName.toLowerCase()||"textarea"===document.activeElement.nodeName.toLowerCase()))){if(37===a||39===a||38===a||40===a){var t=!1;if(y.container.parents(".swiper-slide").length>0&&0===y.container.parents(".swiper-slide-active").length)return;var s={left:window.pageXOffset,top:window.pageYOffset},r=window.innerWidth,i=window.innerHeight,n=y.container.offset();y.rtl&&(n.left=n.left-y.container[0].scrollLeft);for(var o=[[n.left,n.top],[n.left+y.width,n.top],[n.left,n.top+y.height],[n.left+y.width,n.top+y.height]],l=0;l<o.length;l++){var p=o[l];p[0]>=s.left&&p[0]<=s.left+r&&p[1]>=s.top&&p[1]<=s.top+i&&(t=!0)}if(!t)return}y.isHorizontal()?((37===a||39===a)&&(e.preventDefault?e.preventDefault():e.returnValue=!1),(39===a&&!y.rtl||37===a&&y.rtl)&&y.slideNext(),(37===a&&!y.rtl||39===a&&y.rtl)&&y.slidePrev()):((38===a||40===a)&&(e.preventDefault?e.preventDefault():e.returnValue=!1),40===a&&y.slideNext(),38===a&&y.slidePrev())}}function p(e){e.originalEvent&&(e=e.originalEvent);var a=y.mousewheel.event,t=0,s=y.rtl?-1:1;if("mousewheel"===a)if(y.params.mousewheelForceToAxis)if(y.isHorizontal()){if(!(Math.abs(e.wheelDeltaX)>Math.abs(e.wheelDeltaY)))return;t=e.wheelDeltaX*s}else{if(!(Math.abs(e.wheelDeltaY)>Math.abs(e.wheelDeltaX)))return;t=e.wheelDeltaY}else t=Math.abs(e.wheelDeltaX)>Math.abs(e.wheelDeltaY)?-e.wheelDeltaX*s:-e.wheelDeltaY;else if("DOMMouseScroll"===a)t=-e.detail;else if("wheel"===a)if(y.params.mousewheelForceToAxis)if(y.isHorizontal()){if(!(Math.abs(e.deltaX)>Math.abs(e.deltaY)))return;t=-e.deltaX*s}else{if(!(Math.abs(e.deltaY)>Math.abs(e.deltaX)))return;t=-e.deltaY}else t=Math.abs(e.deltaX)>Math.abs(e.deltaY)?-e.deltaX*s:-e.deltaY;if(0!==t){if(y.params.mousewheelInvert&&(t=-t),y.params.freeMode){var r=y.getWrapperTranslate()+t*y.params.mousewheelSensitivity,i=y.isBeginning,n=y.isEnd;if(r>=y.minTranslate()&&(r=y.minTranslate()),r<=y.maxTranslate()&&(r=y.maxTranslate()),y.setWrapperTransition(0),y.setWrapperTranslate(r),y.updateProgress(),y.updateActiveIndex(),(!i&&y.isBeginning||!n&&y.isEnd)&&y.updateClasses(),y.params.freeModeSticky?(clearTimeout(y.mousewheel.timeout),y.mousewheel.timeout=setTimeout(function(){y.slideReset()},300)):y.params.lazyLoading&&y.lazy&&y.lazy.load(),0===r||r===y.maxTranslate())return}else{if((new window.Date).getTime()-y.mousewheel.lastScrollTime>60)if(0>t)if(y.isEnd&&!y.params.loop||y.animating){if(y.params.mousewheelReleaseOnEdges)return!0}else y.slideNext();else if(y.isBeginning&&!y.params.loop||y.animating){if(y.params.mousewheelReleaseOnEdges)return!0}else y.slidePrev();y.mousewheel.lastScrollTime=(new window.Date).getTime()}return y.params.autoplay&&y.stopAutoplay(),e.preventDefault?e.preventDefault():e.returnValue=!1,!1}}function d(e,t){e=a(e);var s,r,i,n=y.rtl?-1:1;s=e.attr("data-swiper-parallax")||"0",r=e.attr("data-swiper-parallax-x"),i=e.attr("data-swiper-parallax-y"),r||i?(r=r||"0",i=i||"0"):y.isHorizontal()?(r=s,i="0"):(i=s,r="0"),r=r.indexOf("%")>=0?parseInt(r,10)*t*n+"%":r*t*n+"px",i=i.indexOf("%")>=0?parseInt(i,10)*t+"%":i*t+"px",e.transform("translate3d("+r+", "+i+",0px)")}function u(e){return 0!==e.indexOf("on")&&(e=e[0]!==e[0].toUpperCase()?"on"+e[0].toUpperCase()+e.substring(1):"on"+e),e}if(!(this instanceof t))return new t(e,s);var c={direction:"horizontal",touchEventsTarget:"container",initialSlide:0,speed:300,autoplay:!1,autoplayDisableOnInteraction:!0,autoplayStopOnLast:!1,iOSEdgeSwipeDetection:!1,iOSEdgeSwipeThreshold:20,freeMode:!1,freeModeMomentum:!0,freeModeMomentumRatio:1,freeModeMomentumBounce:!0,freeModeMomentumBounceRatio:1,freeModeSticky:!1,freeModeMinimumVelocity:.02,autoHeight:!1,setWrapperSize:!1,virtualTranslate:!1,effect:"slide",coverflow:{rotate:50,stretch:0,depth:100,modifier:1,slideShadows:!0},flip:{slideShadows:!0,limitRotation:!0},cube:{slideShadows:!0,shadow:!0,shadowOffset:20,shadowScale:.94},fade:{crossFade:!1},parallax:!1,scrollbar:null,scrollbarHide:!0,scrollbarDraggable:!1,scrollbarSnapOnRelease:!1,keyboardControl:!1,mousewheelControl:!1,mousewheelReleaseOnEdges:!1,mousewheelInvert:!1,mousewheelForceToAxis:!1,mousewheelSensitivity:1,hashnav:!1,breakpoints:void 0,spaceBetween:0,slidesPerView:1,slidesPerColumn:1,slidesPerColumnFill:"column",slidesPerGroup:1,centeredSlides:!1,slidesOffsetBefore:0,slidesOffsetAfter:0,roundLengths:!1,touchRatio:1,touchAngle:45,simulateTouch:!0,shortSwipes:!0,longSwipes:!0,longSwipesRatio:.5,longSwipesMs:300,followFinger:!0,onlyExternal:!1,threshold:0,touchMoveStopPropagation:!0,uniqueNavElements:!0,pagination:null,paginationElement:"span",paginationClickable:!1,paginationHide:!1,paginationBulletRender:null,paginationProgressRender:null,paginationFractionRender:null,paginationCustomRender:null,paginationType:"bullets",resistance:!0,resistanceRatio:.85,nextButton:null,prevButton:null,watchSlidesProgress:!1,watchSlidesVisibility:!1,grabCursor:!1,preventClicks:!0,preventClicksPropagation:!0,slideToClickedSlide:!1,lazyLoading:!1,lazyLoadingInPrevNext:!1,lazyLoadingInPrevNextAmount:1,lazyLoadingOnTransitionStart:!1,preloadImages:!0,updateOnImagesReady:!0,loop:!1,loopAdditionalSlides:0,loopedSlides:null,control:void 0,controlInverse:!1,controlBy:"slide",allowSwipeToPrev:!0,allowSwipeToNext:!0,swipeHandler:null,noSwiping:!0,noSwipingClass:"swiper-no-swiping",slideClass:"swiper-slide",slideActiveClass:"swiper-slide-active",slideVisibleClass:"swiper-slide-visible",slideDuplicateClass:"swiper-slide-duplicate",slideNextClass:"swiper-slide-next",slidePrevClass:"swiper-slide-prev",wrapperClass:"swiper-wrapper",bulletClass:"swiper-pagination-bullet",bulletActiveClass:"swiper-pagination-bullet-active",buttonDisabledClass:"swiper-button-disabled",paginationCurrentClass:"swiper-pagination-current",paginationTotalClass:"swiper-pagination-total",paginationHiddenClass:"swiper-pagination-hidden",paginationProgressbarClass:"swiper-pagination-progressbar",observer:!1,observeParents:!1,a11y:!1,prevSlideMessage:"Previous slide",nextSlideMessage:"Next slide",firstSlideMessage:"This is the first slide",lastSlideMessage:"This is the last slide",paginationBulletMessage:"Go to slide {{index}}",runCallbacksOnInit:!0},m=s&&s.virtualTranslate;s=s||{};var f={};for(var g in s)if("object"!=typeof s[g]||null===s[g]||(s[g].nodeType||s[g]===window||s[g]===document||"undefined"!=typeof Dom7&&s[g]instanceof Dom7||"undefined"!=typeof jQuery&&s[g]instanceof jQuery))f[g]=s[g];else{f[g]={};for(var h in s[g])f[g][h]=s[g][h]}for(var v in c)if("undefined"==typeof s[v])s[v]=c[v];else if("object"==typeof s[v])for(var w in c[v])"undefined"==typeof s[v][w]&&(s[v][w]=c[v][w]);var y=this;if(y.params=s,y.originalParams=f,y.classNames=[],"undefined"!=typeof a&&"undefined"!=typeof Dom7&&(a=Dom7),("undefined"!=typeof a||(a="undefined"==typeof Dom7?window.Dom7||window.Zepto||window.jQuery:Dom7))&&(y.$=a,y.currentBreakpoint=void 0,y.getActiveBreakpoint=function(){if(!y.params.breakpoints)return!1;var e,a=!1,t=[];for(e in y.params.breakpoints)y.params.breakpoints.hasOwnProperty(e)&&t.push(e);t.sort(function(e,a){return parseInt(e,10)>parseInt(a,10)});for(var s=0;s<t.length;s++)e=t[s],e>=window.innerWidth&&!a&&(a=e);return a||"max"},y.setBreakpoint=function(){var e=y.getActiveBreakpoint();if(e&&y.currentBreakpoint!==e){var a=e in y.params.breakpoints?y.params.breakpoints[e]:y.originalParams,t=y.params.loop&&a.slidesPerView!==y.params.slidesPerView;for(var s in a)y.params[s]=a[s];y.currentBreakpoint=e,t&&y.destroyLoop&&y.reLoop(!0)}},y.params.breakpoints&&y.setBreakpoint(),y.container=a(e),0!==y.container.length)){if(y.container.length>1){var b=[];return y.container.each(function(){b.push(new t(this,s))}),b}y.container[0].swiper=y,y.container.data("swiper",y),y.classNames.push("swiper-container-"+y.params.direction),y.params.freeMode&&y.classNames.push("swiper-container-free-mode"),y.support.flexbox||(y.classNames.push("swiper-container-no-flexbox"),y.params.slidesPerColumn=1),y.params.autoHeight&&y.classNames.push("swiper-container-autoheight"),(y.params.parallax||y.params.watchSlidesVisibility)&&(y.params.watchSlidesProgress=!0),["cube","coverflow","flip"].indexOf(y.params.effect)>=0&&(y.support.transforms3d?(y.params.watchSlidesProgress=!0,y.classNames.push("swiper-container-3d")):y.params.effect="slide"),"slide"!==y.params.effect&&y.classNames.push("swiper-container-"+y.params.effect),"cube"===y.params.effect&&(y.params.resistanceRatio=0,y.params.slidesPerView=1,y.params.slidesPerColumn=1,y.params.slidesPerGroup=1,y.params.centeredSlides=!1,y.params.spaceBetween=0,y.params.virtualTranslate=!0,y.params.setWrapperSize=!1),("fade"===y.params.effect||"flip"===y.params.effect)&&(y.params.slidesPerView=1,y.params.slidesPerColumn=1,y.params.slidesPerGroup=1,y.params.watchSlidesProgress=!0,y.params.spaceBetween=0,y.params.setWrapperSize=!1,"undefined"==typeof m&&(y.params.virtualTranslate=!0)),y.params.grabCursor&&y.support.touch&&(y.params.grabCursor=!1),y.wrapper=y.container.children("."+y.params.wrapperClass),y.params.pagination&&(y.paginationContainer=a(y.params.pagination),y.params.uniqueNavElements&&"string"==typeof y.params.pagination&&y.paginationContainer.length>1&&1===y.container.find(y.params.pagination).length&&(y.paginationContainer=y.container.find(y.params.pagination)),"bullets"===y.params.paginationType&&y.params.paginationClickable?y.paginationContainer.addClass("swiper-pagination-clickable"):y.params.paginationClickable=!1,y.paginationContainer.addClass("swiper-pagination-"+y.params.paginationType)),(y.params.nextButton||y.params.prevButton)&&(y.params.nextButton&&(y.nextButton=a(y.params.nextButton),y.params.uniqueNavElements&&"string"==typeof y.params.nextButton&&y.nextButton.length>1&&1===y.container.find(y.params.nextButton).length&&(y.nextButton=y.container.find(y.params.nextButton))),y.params.prevButton&&(y.prevButton=a(y.params.prevButton),y.params.uniqueNavElements&&"string"==typeof y.params.prevButton&&y.prevButton.length>1&&1===y.container.find(y.params.prevButton).length&&(y.prevButton=y.container.find(y.params.prevButton)))),y.isHorizontal=function(){return"horizontal"===y.params.direction},y.rtl=y.isHorizontal()&&("rtl"===y.container[0].dir.toLowerCase()||"rtl"===y.container.css("direction")),y.rtl&&y.classNames.push("swiper-container-rtl"),y.rtl&&(y.wrongRTL="-webkit-box"===y.wrapper.css("display")),y.params.slidesPerColumn>1&&y.classNames.push("swiper-container-multirow"),y.device.android&&y.classNames.push("swiper-container-android"),y.container.addClass(y.classNames.join(" ")),y.translate=0,y.progress=0,y.velocity=0,y.lockSwipeToNext=function(){y.params.allowSwipeToNext=!1},y.lockSwipeToPrev=function(){y.params.allowSwipeToPrev=!1},y.lockSwipes=function(){y.params.allowSwipeToNext=y.params.allowSwipeToPrev=!1},y.unlockSwipeToNext=function(){y.params.allowSwipeToNext=!0},y.unlockSwipeToPrev=function(){y.params.allowSwipeToPrev=!0},y.unlockSwipes=function(){y.params.allowSwipeToNext=y.params.allowSwipeToPrev=!0},y.params.grabCursor&&(y.container[0].style.cursor="move",y.container[0].style.cursor="-webkit-grab",y.container[0].style.cursor="-moz-grab",y.container[0].style.cursor="grab"),y.imagesToLoad=[],y.imagesLoaded=0,y.loadImage=function(e,a,t,s,r){function i(){r&&r()}var n;e.complete&&s?i():a?(n=new window.Image,n.onload=i,n.onerror=i,t&&(n.srcset=t),a&&(n.src=a)):i()},y.preloadImages=function(){function e(){"undefined"!=typeof y&&null!==y&&(void 0!==y.imagesLoaded&&y.imagesLoaded++,y.imagesLoaded===y.imagesToLoad.length&&(y.params.updateOnImagesReady&&y.update(),y.emit("onImagesReady",y)))}y.imagesToLoad=y.container.find("img");for(var a=0;a<y.imagesToLoad.length;a++)y.loadImage(y.imagesToLoad[a],y.imagesToLoad[a].currentSrc||y.imagesToLoad[a].getAttribute("src"),y.imagesToLoad[a].srcset||y.imagesToLoad[a].getAttribute("srcset"),!0,e)},y.autoplayTimeoutId=void 0,y.autoplaying=!1,y.autoplayPaused=!1,y.startAutoplay=function(){return"undefined"!=typeof y.autoplayTimeoutId?!1:y.params.autoplay?y.autoplaying?!1:(y.autoplaying=!0,y.emit("onAutoplayStart",y),void i()):!1},y.stopAutoplay=function(e){y.autoplayTimeoutId&&(y.autoplayTimeoutId&&clearTimeout(y.autoplayTimeoutId),y.autoplaying=!1,y.autoplayTimeoutId=void 0,y.emit("onAutoplayStop",y))},y.pauseAutoplay=function(e){y.autoplayPaused||(y.autoplayTimeoutId&&clearTimeout(y.autoplayTimeoutId),y.autoplayPaused=!0,0===e?(y.autoplayPaused=!1,i()):y.wrapper.transitionEnd(function(){y&&(y.autoplayPaused=!1,y.autoplaying?i():y.stopAutoplay())}))},y.minTranslate=function(){return-y.snapGrid[0]},y.maxTranslate=function(){return-y.snapGrid[y.snapGrid.length-1]},y.updateAutoHeight=function(){var e=y.slides.eq(y.activeIndex)[0];if("undefined"!=typeof e){var a=e.offsetHeight;a&&y.wrapper.css("height",a+"px")}},y.updateContainerSize=function(){var e,a;e="undefined"!=typeof y.params.width?y.params.width:y.container[0].clientWidth,a="undefined"!=typeof y.params.height?y.params.height:y.container[0].clientHeight,0===e&&y.isHorizontal()||0===a&&!y.isHorizontal()||(e=e-parseInt(y.container.css("padding-left"),10)-parseInt(y.container.css("padding-right"),10),a=a-parseInt(y.container.css("padding-top"),10)-parseInt(y.container.css("padding-bottom"),10),y.width=e,y.height=a,y.size=y.isHorizontal()?y.width:y.height)},y.updateSlidesSize=function(){y.slides=y.wrapper.children("."+y.params.slideClass),y.snapGrid=[],y.slidesGrid=[],y.slidesSizesGrid=[];var e,a=y.params.spaceBetween,t=-y.params.slidesOffsetBefore,s=0,i=0;if("undefined"!=typeof y.size){"string"==typeof a&&a.indexOf("%")>=0&&(a=parseFloat(a.replace("%",""))/100*y.size),y.virtualSize=-a,y.rtl?y.slides.css({marginLeft:"",marginTop:""}):y.slides.css({marginRight:"",marginBottom:""});var n;y.params.slidesPerColumn>1&&(n=Math.floor(y.slides.length/y.params.slidesPerColumn)===y.slides.length/y.params.slidesPerColumn?y.slides.length:Math.ceil(y.slides.length/y.params.slidesPerColumn)*y.params.slidesPerColumn,"auto"!==y.params.slidesPerView&&"row"===y.params.slidesPerColumnFill&&(n=Math.max(n,y.params.slidesPerView*y.params.slidesPerColumn)));var o,l=y.params.slidesPerColumn,p=n/l,d=p-(y.params.slidesPerColumn*p-y.slides.length);for(e=0;e<y.slides.length;e++){o=0;var u=y.slides.eq(e);if(y.params.slidesPerColumn>1){var c,m,f;"column"===y.params.slidesPerColumnFill?(m=Math.floor(e/l),f=e-m*l,(m>d||m===d&&f===l-1)&&++f>=l&&(f=0,m++),c=m+f*n/l,u.css({"-webkit-box-ordinal-group":c,"-moz-box-ordinal-group":c,"-ms-flex-order":c,"-webkit-order":c,order:c})):(f=Math.floor(e/p),m=e-f*p),u.css({"margin-top":0!==f&&y.params.spaceBetween&&y.params.spaceBetween+"px"}).attr("data-swiper-column",m).attr("data-swiper-row",f)}"none"!==u.css("display")&&("auto"===y.params.slidesPerView?(o=y.isHorizontal()?u.outerWidth(!0):u.outerHeight(!0),y.params.roundLengths&&(o=r(o))):(o=(y.size-(y.params.slidesPerView-1)*a)/y.params.slidesPerView,y.params.roundLengths&&(o=r(o)),y.isHorizontal()?y.slides[e].style.width=o+"px":y.slides[e].style.height=o+"px"),y.slides[e].swiperSlideSize=o,y.slidesSizesGrid.push(o),y.params.centeredSlides?(t=t+o/2+s/2+a,0===e&&(t=t-y.size/2-a),Math.abs(t)<.001&&(t=0),i%y.params.slidesPerGroup===0&&y.snapGrid.push(t),y.slidesGrid.push(t)):(i%y.params.slidesPerGroup===0&&y.snapGrid.push(t),y.slidesGrid.push(t),t=t+o+a),y.virtualSize+=o+a,s=o,i++)}y.virtualSize=Math.max(y.virtualSize,y.size)+y.params.slidesOffsetAfter;var g;if(y.rtl&&y.wrongRTL&&("slide"===y.params.effect||"coverflow"===y.params.effect)&&y.wrapper.css({width:y.virtualSize+y.params.spaceBetween+"px"}),(!y.support.flexbox||y.params.setWrapperSize)&&(y.isHorizontal()?y.wrapper.css({width:y.virtualSize+y.params.spaceBetween+"px"}):y.wrapper.css({height:y.virtualSize+y.params.spaceBetween+"px"})),y.params.slidesPerColumn>1&&(y.virtualSize=(o+y.params.spaceBetween)*n,y.virtualSize=Math.ceil(y.virtualSize/y.params.slidesPerColumn)-y.params.spaceBetween,y.wrapper.css({width:y.virtualSize+y.params.spaceBetween+"px"}),y.params.centeredSlides)){for(g=[],e=0;e<y.snapGrid.length;e++)y.snapGrid[e]<y.virtualSize+y.snapGrid[0]&&g.push(y.snapGrid[e]);y.snapGrid=g}if(!y.params.centeredSlides){for(g=[],e=0;e<y.snapGrid.length;e++)y.snapGrid[e]<=y.virtualSize-y.size&&g.push(y.snapGrid[e]);y.snapGrid=g,Math.floor(y.virtualSize-y.size)-Math.floor(y.snapGrid[y.snapGrid.length-1])>1&&y.snapGrid.push(y.virtualSize-y.size)}0===y.snapGrid.length&&(y.snapGrid=[0]),0!==y.params.spaceBetween&&(y.isHorizontal()?y.rtl?y.slides.css({marginLeft:a+"px"}):y.slides.css({marginRight:a+"px"}):y.slides.css({marginBottom:a+"px"})),y.params.watchSlidesProgress&&y.updateSlidesOffset()}},y.updateSlidesOffset=function(){for(var e=0;e<y.slides.length;e++)y.slides[e].swiperSlideOffset=y.isHorizontal()?y.slides[e].offsetLeft:y.slides[e].offsetTop},y.updateSlidesProgress=function(e){if("undefined"==typeof e&&(e=y.translate||0),0!==y.slides.length){"undefined"==typeof y.slides[0].swiperSlideOffset&&y.updateSlidesOffset();var a=-e;y.rtl&&(a=e),y.slides.removeClass(y.params.slideVisibleClass);for(var t=0;t<y.slides.length;t++){var s=y.slides[t],r=(a-s.swiperSlideOffset)/(s.swiperSlideSize+y.params.spaceBetween);if(y.params.watchSlidesVisibility){var i=-(a-s.swiperSlideOffset),n=i+y.slidesSizesGrid[t],o=i>=0&&i<y.size||n>0&&n<=y.size||0>=i&&n>=y.size;o&&y.slides.eq(t).addClass(y.params.slideVisibleClass)}s.progress=y.rtl?-r:r}}},y.updateProgress=function(e){"undefined"==typeof e&&(e=y.translate||0);var a=y.maxTranslate()-y.minTranslate(),t=y.isBeginning,s=y.isEnd;0===a?(y.progress=0,y.isBeginning=y.isEnd=!0):(y.progress=(e-y.minTranslate())/a,y.isBeginning=y.progress<=0,y.isEnd=y.progress>=1),y.isBeginning&&!t&&y.emit("onReachBeginning",y),y.isEnd&&!s&&y.emit("onReachEnd",y),y.params.watchSlidesProgress&&y.updateSlidesProgress(e),y.emit("onProgress",y,y.progress)},y.updateActiveIndex=function(){var e,a,t,s=y.rtl?y.translate:-y.translate;for(a=0;a<y.slidesGrid.length;a++)"undefined"!=typeof y.slidesGrid[a+1]?s>=y.slidesGrid[a]&&s<y.slidesGrid[a+1]-(y.slidesGrid[a+1]-y.slidesGrid[a])/2?e=a:s>=y.slidesGrid[a]&&s<y.slidesGrid[a+1]&&(e=a+1):s>=y.slidesGrid[a]&&(e=a);(0>e||"undefined"==typeof e)&&(e=0),t=Math.floor(e/y.params.slidesPerGroup),t>=y.snapGrid.length&&(t=y.snapGrid.length-1),e!==y.activeIndex&&(y.snapIndex=t,y.previousIndex=y.activeIndex,y.activeIndex=e,y.updateClasses())},y.updateClasses=function(){y.slides.removeClass(y.params.slideActiveClass+" "+y.params.slideNextClass+" "+y.params.slidePrevClass);var e=y.slides.eq(y.activeIndex);e.addClass(y.params.slideActiveClass);var t=e.next("."+y.params.slideClass).addClass(y.params.slideNextClass);y.params.loop&&0===t.length&&y.slides.eq(0).addClass(y.params.slideNextClass);var s=e.prev("."+y.params.slideClass).addClass(y.params.slidePrevClass);if(y.params.loop&&0===s.length&&y.slides.eq(-1).addClass(y.params.slidePrevClass),y.paginationContainer&&y.paginationContainer.length>0){var r,i=y.params.loop?Math.ceil((y.slides.length-2*y.loopedSlides)/y.params.slidesPerGroup):y.snapGrid.length;if(y.params.loop?(r=Math.ceil((y.activeIndex-y.loopedSlides)/y.params.slidesPerGroup),r>y.slides.length-1-2*y.loopedSlides&&(r-=y.slides.length-2*y.loopedSlides),r>i-1&&(r-=i),0>r&&"bullets"!==y.params.paginationType&&(r=i+r)):r="undefined"!=typeof y.snapIndex?y.snapIndex:y.activeIndex||0,"bullets"===y.params.paginationType&&y.bullets&&y.bullets.length>0&&(y.bullets.removeClass(y.params.bulletActiveClass),y.paginationContainer.length>1?y.bullets.each(function(){a(this).index()===r&&a(this).addClass(y.params.bulletActiveClass)}):y.bullets.eq(r).addClass(y.params.bulletActiveClass)),"fraction"===y.params.paginationType&&(y.paginationContainer.find("."+y.params.paginationCurrentClass).text(r+1),y.paginationContainer.find("."+y.params.paginationTotalClass).text(i)),"progress"===y.params.paginationType){var n=(r+1)/i,o=n,l=1;y.isHorizontal()||(l=n,o=1),y.paginationContainer.find("."+y.params.paginationProgressbarClass).transform("translate3d(0,0,0) scaleX("+o+") scaleY("+l+")").transition(y.params.speed)}"custom"===y.params.paginationType&&y.params.paginationCustomRender&&(y.paginationContainer.html(y.params.paginationCustomRender(y,r+1,i)),y.emit("onPaginationRendered",y,y.paginationContainer[0]))}y.params.loop||(y.params.prevButton&&y.prevButton&&y.prevButton.length>0&&(y.isBeginning?(y.prevButton.addClass(y.params.buttonDisabledClass),y.params.a11y&&y.a11y&&y.a11y.disable(y.prevButton)):(y.prevButton.removeClass(y.params.buttonDisabledClass),y.params.a11y&&y.a11y&&y.a11y.enable(y.prevButton))),y.params.nextButton&&y.nextButton&&y.nextButton.length>0&&(y.isEnd?(y.nextButton.addClass(y.params.buttonDisabledClass),y.params.a11y&&y.a11y&&y.a11y.disable(y.nextButton)):(y.nextButton.removeClass(y.params.buttonDisabledClass),y.params.a11y&&y.a11y&&y.a11y.enable(y.nextButton))))},y.updatePagination=function(){if(y.params.pagination&&y.paginationContainer&&y.paginationContainer.length>0){var e="";if("bullets"===y.params.paginationType){for(var a=y.params.loop?Math.ceil((y.slides.length-2*y.loopedSlides)/y.params.slidesPerGroup):y.snapGrid.length,t=0;a>t;t++)e+=y.params.paginationBulletRender?y.params.paginationBulletRender(t,y.params.bulletClass):"<"+y.params.paginationElement+' class="'+y.params.bulletClass+'"></'+y.params.paginationElement+">";y.paginationContainer.html(e),y.bullets=y.paginationContainer.find("."+y.params.bulletClass),y.params.paginationClickable&&y.params.a11y&&y.a11y&&y.a11y.initPagination()}"fraction"===y.params.paginationType&&(e=y.params.paginationFractionRender?y.params.paginationFractionRender(y,y.params.paginationCurrentClass,y.params.paginationTotalClass):'<span class="'+y.params.paginationCurrentClass+'"></span> / <span class="'+y.params.paginationTotalClass+'"></span>',y.paginationContainer.html(e)),"progress"===y.params.paginationType&&(e=y.params.paginationProgressRender?y.params.paginationProgressRender(y,y.params.paginationProgressbarClass):'<span class="'+y.params.paginationProgressbarClass+'"></span>',y.paginationContainer.html(e)),"custom"!==y.params.paginationType&&y.emit("onPaginationRendered",y,y.paginationContainer[0])}},y.update=function(e){function a(){s=Math.min(Math.max(y.translate,y.maxTranslate()),y.minTranslate()),y.setWrapperTranslate(s),y.updateActiveIndex(),y.updateClasses()}if(y.updateContainerSize(),y.updateSlidesSize(),y.updateProgress(),y.updatePagination(),y.updateClasses(),y.params.scrollbar&&y.scrollbar&&y.scrollbar.set(),e){var t,s;y.controller&&y.controller.spline&&(y.controller.spline=void 0),y.params.freeMode?(a(),y.params.autoHeight&&y.updateAutoHeight()):(t=("auto"===y.params.slidesPerView||y.params.slidesPerView>1)&&y.isEnd&&!y.params.centeredSlides?y.slideTo(y.slides.length-1,0,!1,!0):y.slideTo(y.activeIndex,0,!1,!0),t||a())}else y.params.autoHeight&&y.updateAutoHeight()},y.onResize=function(e){y.params.breakpoints&&y.setBreakpoint();var a=y.params.allowSwipeToPrev,t=y.params.allowSwipeToNext;y.params.allowSwipeToPrev=y.params.allowSwipeToNext=!0,y.updateContainerSize(),y.updateSlidesSize(),("auto"===y.params.slidesPerView||y.params.freeMode||e)&&y.updatePagination(),y.params.scrollbar&&y.scrollbar&&y.scrollbar.set(),y.controller&&y.controller.spline&&(y.controller.spline=void 0);var s=!1;if(y.params.freeMode){var r=Math.min(Math.max(y.translate,y.maxTranslate()),y.minTranslate());y.setWrapperTranslate(r),y.updateActiveIndex(),y.updateClasses(),y.params.autoHeight&&y.updateAutoHeight()}else y.updateClasses(),s=("auto"===y.params.slidesPerView||y.params.slidesPerView>1)&&y.isEnd&&!y.params.centeredSlides?y.slideTo(y.slides.length-1,0,!1,!0):y.slideTo(y.activeIndex,0,!1,!0);y.params.lazyLoading&&!s&&y.lazy&&y.lazy.load(),y.params.allowSwipeToPrev=a,y.params.allowSwipeToNext=t};var x=["mousedown","mousemove","mouseup"];window.navigator.pointerEnabled?x=["pointerdown","pointermove","pointerup"]:window.navigator.msPointerEnabled&&(x=["MSPointerDown","MSPointerMove","MSPointerUp"]),y.touchEvents={start:y.support.touch||!y.params.simulateTouch?"touchstart":x[0],move:y.support.touch||!y.params.simulateTouch?"touchmove":x[1],end:y.support.touch||!y.params.simulateTouch?"touchend":x[2]},(window.navigator.pointerEnabled||window.navigator.msPointerEnabled)&&("container"===y.params.touchEventsTarget?y.container:y.wrapper).addClass("swiper-wp8-"+y.params.direction),y.initEvents=function(e){var a=e?"off":"on",t=e?"removeEventListener":"addEventListener",r="container"===y.params.touchEventsTarget?y.container[0]:y.wrapper[0],i=y.support.touch?r:document,n=y.params.nested?!0:!1;y.browser.ie?(r[t](y.touchEvents.start,y.onTouchStart,!1),i[t](y.touchEvents.move,y.onTouchMove,n),i[t](y.touchEvents.end,y.onTouchEnd,!1)):(y.support.touch&&(r[t](y.touchEvents.start,y.onTouchStart,!1),r[t](y.touchEvents.move,y.onTouchMove,n),r[t](y.touchEvents.end,y.onTouchEnd,!1)),!s.simulateTouch||y.device.ios||y.device.android||(r[t]("mousedown",y.onTouchStart,!1),document[t]("mousemove",y.onTouchMove,n),document[t]("mouseup",y.onTouchEnd,!1))),window[t]("resize",y.onResize),y.params.nextButton&&y.nextButton&&y.nextButton.length>0&&(y.nextButton[a]("click",y.onClickNext),y.params.a11y&&y.a11y&&y.nextButton[a]("keydown",y.a11y.onEnterKey)),y.params.prevButton&&y.prevButton&&y.prevButton.length>0&&(y.prevButton[a]("click",y.onClickPrev),y.params.a11y&&y.a11y&&y.prevButton[a]("keydown",y.a11y.onEnterKey)),y.params.pagination&&y.params.paginationClickable&&(y.paginationContainer[a]("click","."+y.params.bulletClass,y.onClickIndex),y.params.a11y&&y.a11y&&y.paginationContainer[a]("keydown","."+y.params.bulletClass,y.a11y.onEnterKey)),(y.params.preventClicks||y.params.preventClicksPropagation)&&r[t]("click",y.preventClicks,!0)},y.attachEvents=function(){y.initEvents()},y.detachEvents=function(){y.initEvents(!0)},y.allowClick=!0,y.preventClicks=function(e){y.allowClick||(y.params.preventClicks&&e.preventDefault(),y.params.preventClicksPropagation&&y.animating&&(e.stopPropagation(),e.stopImmediatePropagation()))},y.onClickNext=function(e){e.preventDefault(),(!y.isEnd||y.params.loop)&&y.slideNext()},y.onClickPrev=function(e){e.preventDefault(),(!y.isBeginning||y.params.loop)&&y.slidePrev()},y.onClickIndex=function(e){e.preventDefault();var t=a(this).index()*y.params.slidesPerGroup;y.params.loop&&(t+=y.loopedSlides),y.slideTo(t)},y.updateClickedSlide=function(e){var t=n(e,"."+y.params.slideClass),s=!1;if(t)for(var r=0;r<y.slides.length;r++)y.slides[r]===t&&(s=!0);if(!t||!s)return y.clickedSlide=void 0,void(y.clickedIndex=void 0);if(y.clickedSlide=t,y.clickedIndex=a(t).index(),y.params.slideToClickedSlide&&void 0!==y.clickedIndex&&y.clickedIndex!==y.activeIndex){var i,o=y.clickedIndex;if(y.params.loop){if(y.animating)return;i=a(y.clickedSlide).attr("data-swiper-slide-index"),y.params.centeredSlides?o<y.loopedSlides-y.params.slidesPerView/2||o>y.slides.length-y.loopedSlides+y.params.slidesPerView/2?(y.fixLoop(),o=y.wrapper.children("."+y.params.slideClass+'[data-swiper-slide-index="'+i+'"]:not(.swiper-slide-duplicate)').eq(0).index(),setTimeout(function(){y.slideTo(o)},0)):y.slideTo(o):o>y.slides.length-y.params.slidesPerView?(y.fixLoop(),o=y.wrapper.children("."+y.params.slideClass+'[data-swiper-slide-index="'+i+'"]:not(.swiper-slide-duplicate)').eq(0).index(),setTimeout(function(){y.slideTo(o)},0)):y.slideTo(o)}else y.slideTo(o)}};var T,S,C,z,M,P,I,k,E,B,D="input, select, textarea, button",L=Date.now(),H=[];y.animating=!1,y.touches={startX:0,startY:0,currentX:0,currentY:0,diff:0};var G,A;if(y.onTouchStart=function(e){if(e.originalEvent&&(e=e.originalEvent),G="touchstart"===e.type,G||!("which"in e)||3!==e.which){if(y.params.noSwiping&&n(e,"."+y.params.noSwipingClass))return void(y.allowClick=!0);if(!y.params.swipeHandler||n(e,y.params.swipeHandler)){var t=y.touches.currentX="touchstart"===e.type?e.targetTouches[0].pageX:e.pageX,s=y.touches.currentY="touchstart"===e.type?e.targetTouches[0].pageY:e.pageY;if(!(y.device.ios&&y.params.iOSEdgeSwipeDetection&&t<=y.params.iOSEdgeSwipeThreshold)){if(T=!0,S=!1,C=!0,M=void 0,A=void 0,y.touches.startX=t,y.touches.startY=s,z=Date.now(),y.allowClick=!0,y.updateContainerSize(),y.swipeDirection=void 0,y.params.threshold>0&&(k=!1),"touchstart"!==e.type){var r=!0;a(e.target).is(D)&&(r=!1),document.activeElement&&a(document.activeElement).is(D)&&document.activeElement.blur(),r&&e.preventDefault()}y.emit("onTouchStart",y,e)}}}},y.onTouchMove=function(e){if(e.originalEvent&&(e=e.originalEvent),!G||"mousemove"!==e.type){if(e.preventedByNestedSwiper)return y.touches.startX="touchmove"===e.type?e.targetTouches[0].pageX:e.pageX,void(y.touches.startY="touchmove"===e.type?e.targetTouches[0].pageY:e.pageY);if(y.params.onlyExternal)return y.allowClick=!1,void(T&&(y.touches.startX=y.touches.currentX="touchmove"===e.type?e.targetTouches[0].pageX:e.pageX,y.touches.startY=y.touches.currentY="touchmove"===e.type?e.targetTouches[0].pageY:e.pageY,z=Date.now()));if(G&&document.activeElement&&e.target===document.activeElement&&a(e.target).is(D))return S=!0,void(y.allowClick=!1);if(C&&y.emit("onTouchMove",y,e),!(e.targetTouches&&e.targetTouches.length>1)){if(y.touches.currentX="touchmove"===e.type?e.targetTouches[0].pageX:e.pageX,y.touches.currentY="touchmove"===e.type?e.targetTouches[0].pageY:e.pageY,"undefined"==typeof M){var t=180*Math.atan2(Math.abs(y.touches.currentY-y.touches.startY),Math.abs(y.touches.currentX-y.touches.startX))/Math.PI;M=y.isHorizontal()?t>y.params.touchAngle:90-t>y.params.touchAngle}if(M&&y.emit("onTouchMoveOpposite",y,e),"undefined"==typeof A&&y.browser.ieTouch&&(y.touches.currentX!==y.touches.startX||y.touches.currentY!==y.touches.startY)&&(A=!0),T){if(M)return void(T=!1);if(A||!y.browser.ieTouch){y.allowClick=!1,y.emit("onSliderMove",y,e),e.preventDefault(),y.params.touchMoveStopPropagation&&!y.params.nested&&e.stopPropagation(),S||(s.loop&&y.fixLoop(),I=y.getWrapperTranslate(),y.setWrapperTransition(0),y.animating&&y.wrapper.trigger("webkitTransitionEnd transitionend oTransitionEnd MSTransitionEnd msTransitionEnd"),y.params.autoplay&&y.autoplaying&&(y.params.autoplayDisableOnInteraction?y.stopAutoplay():y.pauseAutoplay()),B=!1,y.params.grabCursor&&(y.container[0].style.cursor="move",y.container[0].style.cursor="-webkit-grabbing",y.container[0].style.cursor="-moz-grabbin",y.container[0].style.cursor="grabbing")),S=!0;var r=y.touches.diff=y.isHorizontal()?y.touches.currentX-y.touches.startX:y.touches.currentY-y.touches.startY;r*=y.params.touchRatio,y.rtl&&(r=-r),y.swipeDirection=r>0?"prev":"next",P=r+I;var i=!0;if(r>0&&P>y.minTranslate()?(i=!1,y.params.resistance&&(P=y.minTranslate()-1+Math.pow(-y.minTranslate()+I+r,y.params.resistanceRatio))):0>r&&P<y.maxTranslate()&&(i=!1,y.params.resistance&&(P=y.maxTranslate()+1-Math.pow(y.maxTranslate()-I-r,y.params.resistanceRatio))),
i&&(e.preventedByNestedSwiper=!0),!y.params.allowSwipeToNext&&"next"===y.swipeDirection&&I>P&&(P=I),!y.params.allowSwipeToPrev&&"prev"===y.swipeDirection&&P>I&&(P=I),y.params.followFinger){if(y.params.threshold>0){if(!(Math.abs(r)>y.params.threshold||k))return void(P=I);if(!k)return k=!0,y.touches.startX=y.touches.currentX,y.touches.startY=y.touches.currentY,P=I,void(y.touches.diff=y.isHorizontal()?y.touches.currentX-y.touches.startX:y.touches.currentY-y.touches.startY)}(y.params.freeMode||y.params.watchSlidesProgress)&&y.updateActiveIndex(),y.params.freeMode&&(0===H.length&&H.push({position:y.touches[y.isHorizontal()?"startX":"startY"],time:z}),H.push({position:y.touches[y.isHorizontal()?"currentX":"currentY"],time:(new window.Date).getTime()})),y.updateProgress(P),y.setWrapperTranslate(P)}}}}}},y.onTouchEnd=function(e){if(e.originalEvent&&(e=e.originalEvent),C&&y.emit("onTouchEnd",y,e),C=!1,T){y.params.grabCursor&&S&&T&&(y.container[0].style.cursor="move",y.container[0].style.cursor="-webkit-grab",y.container[0].style.cursor="-moz-grab",y.container[0].style.cursor="grab");var t=Date.now(),s=t-z;if(y.allowClick&&(y.updateClickedSlide(e),y.emit("onTap",y,e),300>s&&t-L>300&&(E&&clearTimeout(E),E=setTimeout(function(){y&&(y.params.paginationHide&&y.paginationContainer.length>0&&!a(e.target).hasClass(y.params.bulletClass)&&y.paginationContainer.toggleClass(y.params.paginationHiddenClass),y.emit("onClick",y,e))},300)),300>s&&300>t-L&&(E&&clearTimeout(E),y.emit("onDoubleTap",y,e))),L=Date.now(),setTimeout(function(){y&&(y.allowClick=!0)},0),!T||!S||!y.swipeDirection||0===y.touches.diff||P===I)return void(T=S=!1);T=S=!1;var r;if(r=y.params.followFinger?y.rtl?y.translate:-y.translate:-P,y.params.freeMode){if(r<-y.minTranslate())return void y.slideTo(y.activeIndex);if(r>-y.maxTranslate())return void(y.slides.length<y.snapGrid.length?y.slideTo(y.snapGrid.length-1):y.slideTo(y.slides.length-1));if(y.params.freeModeMomentum){if(H.length>1){var i=H.pop(),n=H.pop(),o=i.position-n.position,l=i.time-n.time;y.velocity=o/l,y.velocity=y.velocity/2,Math.abs(y.velocity)<y.params.freeModeMinimumVelocity&&(y.velocity=0),(l>150||(new window.Date).getTime()-i.time>300)&&(y.velocity=0)}else y.velocity=0;H.length=0;var p=1e3*y.params.freeModeMomentumRatio,d=y.velocity*p,u=y.translate+d;y.rtl&&(u=-u);var c,m=!1,f=20*Math.abs(y.velocity)*y.params.freeModeMomentumBounceRatio;if(u<y.maxTranslate())y.params.freeModeMomentumBounce?(u+y.maxTranslate()<-f&&(u=y.maxTranslate()-f),c=y.maxTranslate(),m=!0,B=!0):u=y.maxTranslate();else if(u>y.minTranslate())y.params.freeModeMomentumBounce?(u-y.minTranslate()>f&&(u=y.minTranslate()+f),c=y.minTranslate(),m=!0,B=!0):u=y.minTranslate();else if(y.params.freeModeSticky){var g,h=0;for(h=0;h<y.snapGrid.length;h+=1)if(y.snapGrid[h]>-u){g=h;break}u=Math.abs(y.snapGrid[g]-u)<Math.abs(y.snapGrid[g-1]-u)||"next"===y.swipeDirection?y.snapGrid[g]:y.snapGrid[g-1],y.rtl||(u=-u)}if(0!==y.velocity)p=y.rtl?Math.abs((-u-y.translate)/y.velocity):Math.abs((u-y.translate)/y.velocity);else if(y.params.freeModeSticky)return void y.slideReset();y.params.freeModeMomentumBounce&&m?(y.updateProgress(c),y.setWrapperTransition(p),y.setWrapperTranslate(u),y.onTransitionStart(),y.animating=!0,y.wrapper.transitionEnd(function(){y&&B&&(y.emit("onMomentumBounce",y),y.setWrapperTransition(y.params.speed),y.setWrapperTranslate(c),y.wrapper.transitionEnd(function(){y&&y.onTransitionEnd()}))})):y.velocity?(y.updateProgress(u),y.setWrapperTransition(p),y.setWrapperTranslate(u),y.onTransitionStart(),y.animating||(y.animating=!0,y.wrapper.transitionEnd(function(){y&&y.onTransitionEnd()}))):y.updateProgress(u),y.updateActiveIndex()}return void((!y.params.freeModeMomentum||s>=y.params.longSwipesMs)&&(y.updateProgress(),y.updateActiveIndex()))}var v,w=0,b=y.slidesSizesGrid[0];for(v=0;v<y.slidesGrid.length;v+=y.params.slidesPerGroup)"undefined"!=typeof y.slidesGrid[v+y.params.slidesPerGroup]?r>=y.slidesGrid[v]&&r<y.slidesGrid[v+y.params.slidesPerGroup]&&(w=v,b=y.slidesGrid[v+y.params.slidesPerGroup]-y.slidesGrid[v]):r>=y.slidesGrid[v]&&(w=v,b=y.slidesGrid[y.slidesGrid.length-1]-y.slidesGrid[y.slidesGrid.length-2]);var x=(r-y.slidesGrid[w])/b;if(s>y.params.longSwipesMs){if(!y.params.longSwipes)return void y.slideTo(y.activeIndex);"next"===y.swipeDirection&&(x>=y.params.longSwipesRatio?y.slideTo(w+y.params.slidesPerGroup):y.slideTo(w)),"prev"===y.swipeDirection&&(x>1-y.params.longSwipesRatio?y.slideTo(w+y.params.slidesPerGroup):y.slideTo(w))}else{if(!y.params.shortSwipes)return void y.slideTo(y.activeIndex);"next"===y.swipeDirection&&y.slideTo(w+y.params.slidesPerGroup),"prev"===y.swipeDirection&&y.slideTo(w)}}},y._slideTo=function(e,a){return y.slideTo(e,a,!0,!0)},y.slideTo=function(e,a,t,s){"undefined"==typeof t&&(t=!0),"undefined"==typeof e&&(e=0),0>e&&(e=0),y.snapIndex=Math.floor(e/y.params.slidesPerGroup),y.snapIndex>=y.snapGrid.length&&(y.snapIndex=y.snapGrid.length-1);var r=-y.snapGrid[y.snapIndex];y.params.autoplay&&y.autoplaying&&(s||!y.params.autoplayDisableOnInteraction?y.pauseAutoplay(a):y.stopAutoplay()),y.updateProgress(r);for(var i=0;i<y.slidesGrid.length;i++)-Math.floor(100*r)>=Math.floor(100*y.slidesGrid[i])&&(e=i);return!y.params.allowSwipeToNext&&r<y.translate&&r<y.minTranslate()?!1:!y.params.allowSwipeToPrev&&r>y.translate&&r>y.maxTranslate()&&(y.activeIndex||0)!==e?!1:("undefined"==typeof a&&(a=y.params.speed),y.previousIndex=y.activeIndex||0,y.activeIndex=e,y.rtl&&-r===y.translate||!y.rtl&&r===y.translate?(y.params.autoHeight&&y.updateAutoHeight(),y.updateClasses(),"slide"!==y.params.effect&&y.setWrapperTranslate(r),!1):(y.updateClasses(),y.onTransitionStart(t),0===a?(y.setWrapperTranslate(r),y.setWrapperTransition(0),y.onTransitionEnd(t)):(y.setWrapperTranslate(r),y.setWrapperTransition(a),y.animating||(y.animating=!0,y.wrapper.transitionEnd(function(){y&&y.onTransitionEnd(t)}))),!0))},y.onTransitionStart=function(e){"undefined"==typeof e&&(e=!0),y.params.autoHeight&&y.updateAutoHeight(),y.lazy&&y.lazy.onTransitionStart(),e&&(y.emit("onTransitionStart",y),y.activeIndex!==y.previousIndex&&(y.emit("onSlideChangeStart",y),y.activeIndex>y.previousIndex?y.emit("onSlideNextStart",y):y.emit("onSlidePrevStart",y)))},y.onTransitionEnd=function(e){y.animating=!1,y.setWrapperTransition(0),"undefined"==typeof e&&(e=!0),y.lazy&&y.lazy.onTransitionEnd(),e&&(y.emit("onTransitionEnd",y),y.activeIndex!==y.previousIndex&&(y.emit("onSlideChangeEnd",y),y.activeIndex>y.previousIndex?y.emit("onSlideNextEnd",y):y.emit("onSlidePrevEnd",y))),y.params.hashnav&&y.hashnav&&y.hashnav.setHash()},y.slideNext=function(e,a,t){if(y.params.loop){if(y.animating)return!1;y.fixLoop();y.container[0].clientLeft;return y.slideTo(y.activeIndex+y.params.slidesPerGroup,a,e,t)}return y.slideTo(y.activeIndex+y.params.slidesPerGroup,a,e,t)},y._slideNext=function(e){return y.slideNext(!0,e,!0)},y.slidePrev=function(e,a,t){if(y.params.loop){if(y.animating)return!1;y.fixLoop();y.container[0].clientLeft;return y.slideTo(y.activeIndex-1,a,e,t)}return y.slideTo(y.activeIndex-1,a,e,t)},y._slidePrev=function(e){return y.slidePrev(!0,e,!0)},y.slideReset=function(e,a,t){return y.slideTo(y.activeIndex,a,e)},y.setWrapperTransition=function(e,a){y.wrapper.transition(e),"slide"!==y.params.effect&&y.effects[y.params.effect]&&y.effects[y.params.effect].setTransition(e),y.params.parallax&&y.parallax&&y.parallax.setTransition(e),y.params.scrollbar&&y.scrollbar&&y.scrollbar.setTransition(e),y.params.control&&y.controller&&y.controller.setTransition(e,a),y.emit("onSetTransition",y,e)},y.setWrapperTranslate=function(e,a,t){var s=0,i=0,n=0;y.isHorizontal()?s=y.rtl?-e:e:i=e,y.params.roundLengths&&(s=r(s),i=r(i)),y.params.virtualTranslate||(y.support.transforms3d?y.wrapper.transform("translate3d("+s+"px, "+i+"px, "+n+"px)"):y.wrapper.transform("translate("+s+"px, "+i+"px)")),y.translate=y.isHorizontal()?s:i;var o,l=y.maxTranslate()-y.minTranslate();o=0===l?0:(e-y.minTranslate())/l,o!==y.progress&&y.updateProgress(e),a&&y.updateActiveIndex(),"slide"!==y.params.effect&&y.effects[y.params.effect]&&y.effects[y.params.effect].setTranslate(y.translate),y.params.parallax&&y.parallax&&y.parallax.setTranslate(y.translate),y.params.scrollbar&&y.scrollbar&&y.scrollbar.setTranslate(y.translate),y.params.control&&y.controller&&y.controller.setTranslate(y.translate,t),y.emit("onSetTranslate",y,y.translate)},y.getTranslate=function(e,a){var t,s,r,i;return"undefined"==typeof a&&(a="x"),y.params.virtualTranslate?y.rtl?-y.translate:y.translate:(r=window.getComputedStyle(e,null),window.WebKitCSSMatrix?(s=r.transform||r.webkitTransform,s.split(",").length>6&&(s=s.split(", ").map(function(e){return e.replace(",",".")}).join(", ")),i=new window.WebKitCSSMatrix("none"===s?"":s)):(i=r.MozTransform||r.OTransform||r.MsTransform||r.msTransform||r.transform||r.getPropertyValue("transform").replace("translate(","matrix(1, 0, 0, 1,"),t=i.toString().split(",")),"x"===a&&(s=window.WebKitCSSMatrix?i.m41:16===t.length?parseFloat(t[12]):parseFloat(t[4])),"y"===a&&(s=window.WebKitCSSMatrix?i.m42:16===t.length?parseFloat(t[13]):parseFloat(t[5])),y.rtl&&s&&(s=-s),s||0)},y.getWrapperTranslate=function(e){return"undefined"==typeof e&&(e=y.isHorizontal()?"x":"y"),y.getTranslate(y.wrapper[0],e)},y.observers=[],y.initObservers=function(){if(y.params.observeParents)for(var e=y.container.parents(),a=0;a<e.length;a++)o(e[a]);o(y.container[0],{childList:!1}),o(y.wrapper[0],{attributes:!1})},y.disconnectObservers=function(){for(var e=0;e<y.observers.length;e++)y.observers[e].disconnect();y.observers=[]},y.createLoop=function(){y.wrapper.children("."+y.params.slideClass+"."+y.params.slideDuplicateClass).remove();var e=y.wrapper.children("."+y.params.slideClass);"auto"!==y.params.slidesPerView||y.params.loopedSlides||(y.params.loopedSlides=e.length),y.loopedSlides=parseInt(y.params.loopedSlides||y.params.slidesPerView,10),y.loopedSlides=y.loopedSlides+y.params.loopAdditionalSlides,y.loopedSlides>e.length&&(y.loopedSlides=e.length);var t,s=[],r=[];for(e.each(function(t,i){var n=a(this);t<y.loopedSlides&&r.push(i),t<e.length&&t>=e.length-y.loopedSlides&&s.push(i),n.attr("data-swiper-slide-index",t)}),t=0;t<r.length;t++)y.wrapper.append(a(r[t].cloneNode(!0)).addClass(y.params.slideDuplicateClass));for(t=s.length-1;t>=0;t--)y.wrapper.prepend(a(s[t].cloneNode(!0)).addClass(y.params.slideDuplicateClass))},y.destroyLoop=function(){y.wrapper.children("."+y.params.slideClass+"."+y.params.slideDuplicateClass).remove(),y.slides.removeAttr("data-swiper-slide-index")},y.reLoop=function(e){var a=y.activeIndex-y.loopedSlides;y.destroyLoop(),y.createLoop(),y.updateSlidesSize(),e&&y.slideTo(a+y.loopedSlides,0,!1)},y.fixLoop=function(){var e;y.activeIndex<y.loopedSlides?(e=y.slides.length-3*y.loopedSlides+y.activeIndex,e+=y.loopedSlides,y.slideTo(e,0,!1,!0)):("auto"===y.params.slidesPerView&&y.activeIndex>=2*y.loopedSlides||y.activeIndex>y.slides.length-2*y.params.slidesPerView)&&(e=-y.slides.length+y.activeIndex+y.loopedSlides,e+=y.loopedSlides,y.slideTo(e,0,!1,!0))},y.appendSlide=function(e){if(y.params.loop&&y.destroyLoop(),"object"==typeof e&&e.length)for(var a=0;a<e.length;a++)e[a]&&y.wrapper.append(e[a]);else y.wrapper.append(e);y.params.loop&&y.createLoop(),y.params.observer&&y.support.observer||y.update(!0)},y.prependSlide=function(e){y.params.loop&&y.destroyLoop();var a=y.activeIndex+1;if("object"==typeof e&&e.length){for(var t=0;t<e.length;t++)e[t]&&y.wrapper.prepend(e[t]);a=y.activeIndex+e.length}else y.wrapper.prepend(e);y.params.loop&&y.createLoop(),y.params.observer&&y.support.observer||y.update(!0),y.slideTo(a,0,!1)},y.removeSlide=function(e){y.params.loop&&(y.destroyLoop(),y.slides=y.wrapper.children("."+y.params.slideClass));var a,t=y.activeIndex;if("object"==typeof e&&e.length){for(var s=0;s<e.length;s++)a=e[s],y.slides[a]&&y.slides.eq(a).remove(),t>a&&t--;t=Math.max(t,0)}else a=e,y.slides[a]&&y.slides.eq(a).remove(),t>a&&t--,t=Math.max(t,0);y.params.loop&&y.createLoop(),y.params.observer&&y.support.observer||y.update(!0),y.params.loop?y.slideTo(t+y.loopedSlides,0,!1):y.slideTo(t,0,!1)},y.removeAllSlides=function(){for(var e=[],a=0;a<y.slides.length;a++)e.push(a);y.removeSlide(e)},y.effects={fade:{setTranslate:function(){for(var e=0;e<y.slides.length;e++){var a=y.slides.eq(e),t=a[0].swiperSlideOffset,s=-t;y.params.virtualTranslate||(s-=y.translate);var r=0;y.isHorizontal()||(r=s,s=0);var i=y.params.fade.crossFade?Math.max(1-Math.abs(a[0].progress),0):1+Math.min(Math.max(a[0].progress,-1),0);a.css({opacity:i}).transform("translate3d("+s+"px, "+r+"px, 0px)")}},setTransition:function(e){if(y.slides.transition(e),y.params.virtualTranslate&&0!==e){var a=!1;y.slides.transitionEnd(function(){if(!a&&y){a=!0,y.animating=!1;for(var e=["webkitTransitionEnd","transitionend","oTransitionEnd","MSTransitionEnd","msTransitionEnd"],t=0;t<e.length;t++)y.wrapper.trigger(e[t])}})}}},flip:{setTranslate:function(){for(var e=0;e<y.slides.length;e++){var t=y.slides.eq(e),s=t[0].progress;y.params.flip.limitRotation&&(s=Math.max(Math.min(t[0].progress,1),-1));var r=t[0].swiperSlideOffset,i=-180*s,n=i,o=0,l=-r,p=0;if(y.isHorizontal()?y.rtl&&(n=-n):(p=l,l=0,o=-n,n=0),t[0].style.zIndex=-Math.abs(Math.round(s))+y.slides.length,y.params.flip.slideShadows){var d=y.isHorizontal()?t.find(".swiper-slide-shadow-left"):t.find(".swiper-slide-shadow-top"),u=y.isHorizontal()?t.find(".swiper-slide-shadow-right"):t.find(".swiper-slide-shadow-bottom");0===d.length&&(d=a('<div class="swiper-slide-shadow-'+(y.isHorizontal()?"left":"top")+'"></div>'),t.append(d)),0===u.length&&(u=a('<div class="swiper-slide-shadow-'+(y.isHorizontal()?"right":"bottom")+'"></div>'),t.append(u)),d.length&&(d[0].style.opacity=Math.max(-s,0)),u.length&&(u[0].style.opacity=Math.max(s,0))}t.transform("translate3d("+l+"px, "+p+"px, 0px) rotateX("+o+"deg) rotateY("+n+"deg)")}},setTransition:function(e){if(y.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e),y.params.virtualTranslate&&0!==e){var t=!1;y.slides.eq(y.activeIndex).transitionEnd(function(){if(!t&&y&&a(this).hasClass(y.params.slideActiveClass)){t=!0,y.animating=!1;for(var e=["webkitTransitionEnd","transitionend","oTransitionEnd","MSTransitionEnd","msTransitionEnd"],s=0;s<e.length;s++)y.wrapper.trigger(e[s])}})}}},cube:{setTranslate:function(){var e,t=0;y.params.cube.shadow&&(y.isHorizontal()?(e=y.wrapper.find(".swiper-cube-shadow"),0===e.length&&(e=a('<div class="swiper-cube-shadow"></div>'),y.wrapper.append(e)),e.css({height:y.width+"px"})):(e=y.container.find(".swiper-cube-shadow"),0===e.length&&(e=a('<div class="swiper-cube-shadow"></div>'),y.container.append(e))));for(var s=0;s<y.slides.length;s++){var r=y.slides.eq(s),i=90*s,n=Math.floor(i/360);y.rtl&&(i=-i,n=Math.floor(-i/360));var o=Math.max(Math.min(r[0].progress,1),-1),l=0,p=0,d=0;s%4===0?(l=4*-n*y.size,d=0):(s-1)%4===0?(l=0,d=4*-n*y.size):(s-2)%4===0?(l=y.size+4*n*y.size,d=y.size):(s-3)%4===0&&(l=-y.size,d=3*y.size+4*y.size*n),y.rtl&&(l=-l),y.isHorizontal()||(p=l,l=0);var u="rotateX("+(y.isHorizontal()?0:-i)+"deg) rotateY("+(y.isHorizontal()?i:0)+"deg) translate3d("+l+"px, "+p+"px, "+d+"px)";if(1>=o&&o>-1&&(t=90*s+90*o,y.rtl&&(t=90*-s-90*o)),r.transform(u),y.params.cube.slideShadows){var c=y.isHorizontal()?r.find(".swiper-slide-shadow-left"):r.find(".swiper-slide-shadow-top"),m=y.isHorizontal()?r.find(".swiper-slide-shadow-right"):r.find(".swiper-slide-shadow-bottom");0===c.length&&(c=a('<div class="swiper-slide-shadow-'+(y.isHorizontal()?"left":"top")+'"></div>'),r.append(c)),0===m.length&&(m=a('<div class="swiper-slide-shadow-'+(y.isHorizontal()?"right":"bottom")+'"></div>'),r.append(m)),c.length&&(c[0].style.opacity=Math.max(-o,0)),m.length&&(m[0].style.opacity=Math.max(o,0))}}if(y.wrapper.css({"-webkit-transform-origin":"50% 50% -"+y.size/2+"px","-moz-transform-origin":"50% 50% -"+y.size/2+"px","-ms-transform-origin":"50% 50% -"+y.size/2+"px","transform-origin":"50% 50% -"+y.size/2+"px"}),y.params.cube.shadow)if(y.isHorizontal())e.transform("translate3d(0px, "+(y.width/2+y.params.cube.shadowOffset)+"px, "+-y.width/2+"px) rotateX(90deg) rotateZ(0deg) scale("+y.params.cube.shadowScale+")");else{var f=Math.abs(t)-90*Math.floor(Math.abs(t)/90),g=1.5-(Math.sin(2*f*Math.PI/360)/2+Math.cos(2*f*Math.PI/360)/2),h=y.params.cube.shadowScale,v=y.params.cube.shadowScale/g,w=y.params.cube.shadowOffset;e.transform("scale3d("+h+", 1, "+v+") translate3d(0px, "+(y.height/2+w)+"px, "+-y.height/2/v+"px) rotateX(-90deg)")}var b=y.isSafari||y.isUiWebView?-y.size/2:0;y.wrapper.transform("translate3d(0px,0,"+b+"px) rotateX("+(y.isHorizontal()?0:t)+"deg) rotateY("+(y.isHorizontal()?-t:0)+"deg)")},setTransition:function(e){y.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e),y.params.cube.shadow&&!y.isHorizontal()&&y.container.find(".swiper-cube-shadow").transition(e)}},coverflow:{setTranslate:function(){for(var e=y.translate,t=y.isHorizontal()?-e+y.width/2:-e+y.height/2,s=y.isHorizontal()?y.params.coverflow.rotate:-y.params.coverflow.rotate,r=y.params.coverflow.depth,i=0,n=y.slides.length;n>i;i++){var o=y.slides.eq(i),l=y.slidesSizesGrid[i],p=o[0].swiperSlideOffset,d=(t-p-l/2)/l*y.params.coverflow.modifier,u=y.isHorizontal()?s*d:0,c=y.isHorizontal()?0:s*d,m=-r*Math.abs(d),f=y.isHorizontal()?0:y.params.coverflow.stretch*d,g=y.isHorizontal()?y.params.coverflow.stretch*d:0;Math.abs(g)<.001&&(g=0),Math.abs(f)<.001&&(f=0),Math.abs(m)<.001&&(m=0),Math.abs(u)<.001&&(u=0),Math.abs(c)<.001&&(c=0);var h="translate3d("+g+"px,"+f+"px,"+m+"px)  rotateX("+c+"deg) rotateY("+u+"deg)";if(o.transform(h),o[0].style.zIndex=-Math.abs(Math.round(d))+1,y.params.coverflow.slideShadows){var v=y.isHorizontal()?o.find(".swiper-slide-shadow-left"):o.find(".swiper-slide-shadow-top"),w=y.isHorizontal()?o.find(".swiper-slide-shadow-right"):o.find(".swiper-slide-shadow-bottom");0===v.length&&(v=a('<div class="swiper-slide-shadow-'+(y.isHorizontal()?"left":"top")+'"></div>'),o.append(v)),0===w.length&&(w=a('<div class="swiper-slide-shadow-'+(y.isHorizontal()?"right":"bottom")+'"></div>'),o.append(w)),v.length&&(v[0].style.opacity=d>0?d:0),w.length&&(w[0].style.opacity=-d>0?-d:0)}}if(y.browser.ie){var b=y.wrapper[0].style;b.perspectiveOrigin=t+"px 50%"}},setTransition:function(e){y.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e)}}},y.lazy={initialImageLoaded:!1,loadImageInSlide:function(e,t){if("undefined"!=typeof e&&("undefined"==typeof t&&(t=!0),0!==y.slides.length)){var s=y.slides.eq(e),r=s.find(".swiper-lazy:not(.swiper-lazy-loaded):not(.swiper-lazy-loading)");!s.hasClass("swiper-lazy")||s.hasClass("swiper-lazy-loaded")||s.hasClass("swiper-lazy-loading")||(r=r.add(s[0])),0!==r.length&&r.each(function(){var e=a(this);e.addClass("swiper-lazy-loading");var r=e.attr("data-background"),i=e.attr("data-src"),n=e.attr("data-srcset");y.loadImage(e[0],i||r,n,!1,function(){if(r?(e.css("background-image",'url("'+r+'")'),e.removeAttr("data-background")):(n&&(e.attr("srcset",n),e.removeAttr("data-srcset")),i&&(e.attr("src",i),e.removeAttr("data-src"))),e.addClass("swiper-lazy-loaded").removeClass("swiper-lazy-loading"),s.find(".swiper-lazy-preloader, .preloader").remove(),y.params.loop&&t){var a=s.attr("data-swiper-slide-index");if(s.hasClass(y.params.slideDuplicateClass)){var o=y.wrapper.children('[data-swiper-slide-index="'+a+'"]:not(.'+y.params.slideDuplicateClass+")");y.lazy.loadImageInSlide(o.index(),!1)}else{var l=y.wrapper.children("."+y.params.slideDuplicateClass+'[data-swiper-slide-index="'+a+'"]');y.lazy.loadImageInSlide(l.index(),!1)}}y.emit("onLazyImageReady",y,s[0],e[0])}),y.emit("onLazyImageLoad",y,s[0],e[0])})}},load:function(){var e;if(y.params.watchSlidesVisibility)y.wrapper.children("."+y.params.slideVisibleClass).each(function(){y.lazy.loadImageInSlide(a(this).index())});else if(y.params.slidesPerView>1)for(e=y.activeIndex;e<y.activeIndex+y.params.slidesPerView;e++)y.slides[e]&&y.lazy.loadImageInSlide(e);else y.lazy.loadImageInSlide(y.activeIndex);if(y.params.lazyLoadingInPrevNext)if(y.params.slidesPerView>1||y.params.lazyLoadingInPrevNextAmount&&y.params.lazyLoadingInPrevNextAmount>1){var t=y.params.lazyLoadingInPrevNextAmount,s=y.params.slidesPerView,r=Math.min(y.activeIndex+s+Math.max(t,s),y.slides.length),i=Math.max(y.activeIndex-Math.max(s,t),0);for(e=y.activeIndex+y.params.slidesPerView;r>e;e++)y.slides[e]&&y.lazy.loadImageInSlide(e);for(e=i;e<y.activeIndex;e++)y.slides[e]&&y.lazy.loadImageInSlide(e)}else{var n=y.wrapper.children("."+y.params.slideNextClass);n.length>0&&y.lazy.loadImageInSlide(n.index());var o=y.wrapper.children("."+y.params.slidePrevClass);o.length>0&&y.lazy.loadImageInSlide(o.index())}},onTransitionStart:function(){y.params.lazyLoading&&(y.params.lazyLoadingOnTransitionStart||!y.params.lazyLoadingOnTransitionStart&&!y.lazy.initialImageLoaded)&&y.lazy.load()},onTransitionEnd:function(){y.params.lazyLoading&&!y.params.lazyLoadingOnTransitionStart&&y.lazy.load()}},y.scrollbar={isTouched:!1,setDragPosition:function(e){var a=y.scrollbar,t=y.isHorizontal()?"touchstart"===e.type||"touchmove"===e.type?e.targetTouches[0].pageX:e.pageX||e.clientX:"touchstart"===e.type||"touchmove"===e.type?e.targetTouches[0].pageY:e.pageY||e.clientY,s=t-a.track.offset()[y.isHorizontal()?"left":"top"]-a.dragSize/2,r=-y.minTranslate()*a.moveDivider,i=-y.maxTranslate()*a.moveDivider;r>s?s=r:s>i&&(s=i),s=-s/a.moveDivider,y.updateProgress(s),y.setWrapperTranslate(s,!0)},dragStart:function(e){var a=y.scrollbar;a.isTouched=!0,e.preventDefault(),e.stopPropagation(),a.setDragPosition(e),clearTimeout(a.dragTimeout),a.track.transition(0),y.params.scrollbarHide&&a.track.css("opacity",1),y.wrapper.transition(100),a.drag.transition(100),y.emit("onScrollbarDragStart",y)},dragMove:function(e){var a=y.scrollbar;a.isTouched&&(e.preventDefault?e.preventDefault():e.returnValue=!1,a.setDragPosition(e),y.wrapper.transition(0),a.track.transition(0),a.drag.transition(0),y.emit("onScrollbarDragMove",y))},dragEnd:function(e){var a=y.scrollbar;a.isTouched&&(a.isTouched=!1,y.params.scrollbarHide&&(clearTimeout(a.dragTimeout),a.dragTimeout=setTimeout(function(){a.track.css("opacity",0),a.track.transition(400)},1e3)),y.emit("onScrollbarDragEnd",y),y.params.scrollbarSnapOnRelease&&y.slideReset())},enableDraggable:function(){var e=y.scrollbar,t=y.support.touch?e.track:document;a(e.track).on(y.touchEvents.start,e.dragStart),a(t).on(y.touchEvents.move,e.dragMove),a(t).on(y.touchEvents.end,e.dragEnd)},disableDraggable:function(){var e=y.scrollbar,t=y.support.touch?e.track:document;a(e.track).off(y.touchEvents.start,e.dragStart),a(t).off(y.touchEvents.move,e.dragMove),a(t).off(y.touchEvents.end,e.dragEnd)},set:function(){if(y.params.scrollbar){var e=y.scrollbar;e.track=a(y.params.scrollbar),y.params.uniqueNavElements&&"string"==typeof y.params.scrollbar&&e.track.length>1&&1===y.container.find(y.params.scrollbar).length&&(e.track=y.container.find(y.params.scrollbar)),e.drag=e.track.find(".swiper-scrollbar-drag"),0===e.drag.length&&(e.drag=a('<div class="swiper-scrollbar-drag"></div>'),e.track.append(e.drag)),e.drag[0].style.width="",e.drag[0].style.height="",e.trackSize=y.isHorizontal()?e.track[0].offsetWidth:e.track[0].offsetHeight,e.divider=y.size/y.virtualSize,e.moveDivider=e.divider*(e.trackSize/y.size),e.dragSize=e.trackSize*e.divider,y.isHorizontal()?e.drag[0].style.width=e.dragSize+"px":e.drag[0].style.height=e.dragSize+"px",e.divider>=1?e.track[0].style.display="none":e.track[0].style.display="",y.params.scrollbarHide&&(e.track[0].style.opacity=0)}},setTranslate:function(){if(y.params.scrollbar){var e,a=y.scrollbar,t=(y.translate||0,a.dragSize);e=(a.trackSize-a.dragSize)*y.progress,y.rtl&&y.isHorizontal()?(e=-e,e>0?(t=a.dragSize-e,e=0):-e+a.dragSize>a.trackSize&&(t=a.trackSize+e)):0>e?(t=a.dragSize+e,e=0):e+a.dragSize>a.trackSize&&(t=a.trackSize-e),y.isHorizontal()?(y.support.transforms3d?a.drag.transform("translate3d("+e+"px, 0, 0)"):a.drag.transform("translateX("+e+"px)"),a.drag[0].style.width=t+"px"):(y.support.transforms3d?a.drag.transform("translate3d(0px, "+e+"px, 0)"):a.drag.transform("translateY("+e+"px)"),a.drag[0].style.height=t+"px"),y.params.scrollbarHide&&(clearTimeout(a.timeout),a.track[0].style.opacity=1,a.timeout=setTimeout(function(){a.track[0].style.opacity=0,a.track.transition(400)},1e3))}},setTransition:function(e){y.params.scrollbar&&y.scrollbar.drag.transition(e)}},y.controller={LinearSpline:function(e,a){this.x=e,this.y=a,this.lastIndex=e.length-1;var t,s;this.x.length;this.interpolate=function(e){return e?(s=r(this.x,e),t=s-1,(e-this.x[t])*(this.y[s]-this.y[t])/(this.x[s]-this.x[t])+this.y[t]):0};var r=function(){var e,a,t;return function(s,r){for(a=-1,e=s.length;e-a>1;)s[t=e+a>>1]<=r?a=t:e=t;return e}}()},getInterpolateFunction:function(e){y.controller.spline||(y.controller.spline=y.params.loop?new y.controller.LinearSpline(y.slidesGrid,e.slidesGrid):new y.controller.LinearSpline(y.snapGrid,e.snapGrid))},setTranslate:function(e,a){function s(a){e=a.rtl&&"horizontal"===a.params.direction?-y.translate:y.translate,"slide"===y.params.controlBy&&(y.controller.getInterpolateFunction(a),i=-y.controller.spline.interpolate(-e)),i&&"container"!==y.params.controlBy||(r=(a.maxTranslate()-a.minTranslate())/(y.maxTranslate()-y.minTranslate()),i=(e-y.minTranslate())*r+a.minTranslate()),y.params.controlInverse&&(i=a.maxTranslate()-i),a.updateProgress(i),a.setWrapperTranslate(i,!1,y),a.updateActiveIndex()}var r,i,n=y.params.control;if(y.isArray(n))for(var o=0;o<n.length;o++)n[o]!==a&&n[o]instanceof t&&s(n[o]);else n instanceof t&&a!==n&&s(n)},setTransition:function(e,a){function s(a){a.setWrapperTransition(e,y),0!==e&&(a.onTransitionStart(),a.wrapper.transitionEnd(function(){i&&(a.params.loop&&"slide"===y.params.controlBy&&a.fixLoop(),a.onTransitionEnd())}))}var r,i=y.params.control;if(y.isArray(i))for(r=0;r<i.length;r++)i[r]!==a&&i[r]instanceof t&&s(i[r]);else i instanceof t&&a!==i&&s(i)}},y.hashnav={init:function(){if(y.params.hashnav){y.hashnav.initialized=!0;var e=document.location.hash.replace("#","");if(e)for(var a=0,t=0,s=y.slides.length;s>t;t++){var r=y.slides.eq(t),i=r.attr("data-hash");if(i===e&&!r.hasClass(y.params.slideDuplicateClass)){var n=r.index();y.slideTo(n,a,y.params.runCallbacksOnInit,!0)}}}},setHash:function(){y.hashnav.initialized&&y.params.hashnav&&(document.location.hash=y.slides.eq(y.activeIndex).attr("data-hash")||"")}},y.disableKeyboardControl=function(){y.params.keyboardControl=!1,a(document).off("keydown",l)},y.enableKeyboardControl=function(){y.params.keyboardControl=!0,a(document).on("keydown",l)},y.mousewheel={event:!1,lastScrollTime:(new window.Date).getTime()},y.params.mousewheelControl){try{new window.WheelEvent("wheel"),y.mousewheel.event="wheel"}catch(O){(window.WheelEvent||y.container[0]&&"wheel"in y.container[0])&&(y.mousewheel.event="wheel")}!y.mousewheel.event&&window.WheelEvent,y.mousewheel.event||void 0===document.onmousewheel||(y.mousewheel.event="mousewheel"),y.mousewheel.event||(y.mousewheel.event="DOMMouseScroll")}y.disableMousewheelControl=function(){return y.mousewheel.event?(y.container.off(y.mousewheel.event,p),!0):!1},y.enableMousewheelControl=function(){return y.mousewheel.event?(y.container.on(y.mousewheel.event,p),!0):!1},y.parallax={setTranslate:function(){y.container.children("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function(){d(this,y.progress)}),y.slides.each(function(){var e=a(this);e.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function(){var a=Math.min(Math.max(e[0].progress,-1),1);d(this,a)})})},setTransition:function(e){"undefined"==typeof e&&(e=y.params.speed),y.container.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function(){var t=a(this),s=parseInt(t.attr("data-swiper-parallax-duration"),10)||e;0===e&&(s=0),t.transition(s)})}},y._plugins=[];for(var N in y.plugins){var R=y.plugins[N](y,y.params[N]);R&&y._plugins.push(R)}return y.callPlugins=function(e){for(var a=0;a<y._plugins.length;a++)e in y._plugins[a]&&y._plugins[a][e](arguments[1],arguments[2],arguments[3],arguments[4],arguments[5])},y.emitterEventListeners={},y.emit=function(e){y.params[e]&&y.params[e](arguments[1],arguments[2],arguments[3],arguments[4],arguments[5]);var a;if(y.emitterEventListeners[e])for(a=0;a<y.emitterEventListeners[e].length;a++)y.emitterEventListeners[e][a](arguments[1],arguments[2],arguments[3],arguments[4],arguments[5]);y.callPlugins&&y.callPlugins(e,arguments[1],arguments[2],arguments[3],arguments[4],arguments[5])},y.on=function(e,a){return e=u(e),y.emitterEventListeners[e]||(y.emitterEventListeners[e]=[]),y.emitterEventListeners[e].push(a),y},y.off=function(e,a){var t;if(e=u(e),"undefined"==typeof a)return y.emitterEventListeners[e]=[],y;if(y.emitterEventListeners[e]&&0!==y.emitterEventListeners[e].length){for(t=0;t<y.emitterEventListeners[e].length;t++)y.emitterEventListeners[e][t]===a&&y.emitterEventListeners[e].splice(t,1);return y}},y.once=function(e,a){e=u(e);var t=function(){a(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]),y.off(e,t)};return y.on(e,t),y},y.a11y={makeFocusable:function(e){return e.attr("tabIndex","0"),e},addRole:function(e,a){return e.attr("role",a),e},addLabel:function(e,a){return e.attr("aria-label",a),e},disable:function(e){return e.attr("aria-disabled",!0),e},enable:function(e){return e.attr("aria-disabled",!1),e},onEnterKey:function(e){13===e.keyCode&&(a(e.target).is(y.params.nextButton)?(y.onClickNext(e),y.isEnd?y.a11y.notify(y.params.lastSlideMessage):y.a11y.notify(y.params.nextSlideMessage)):a(e.target).is(y.params.prevButton)&&(y.onClickPrev(e),y.isBeginning?y.a11y.notify(y.params.firstSlideMessage):y.a11y.notify(y.params.prevSlideMessage)),a(e.target).is("."+y.params.bulletClass)&&a(e.target)[0].click())},liveRegion:a('<span class="swiper-notification" aria-live="assertive" aria-atomic="true"></span>'),notify:function(e){var a=y.a11y.liveRegion;0!==a.length&&(a.html(""),a.html(e))},init:function(){y.params.nextButton&&y.nextButton&&y.nextButton.length>0&&(y.a11y.makeFocusable(y.nextButton),y.a11y.addRole(y.nextButton,"button"),y.a11y.addLabel(y.nextButton,y.params.nextSlideMessage)),y.params.prevButton&&y.prevButton&&y.prevButton.length>0&&(y.a11y.makeFocusable(y.prevButton),y.a11y.addRole(y.prevButton,"button"),y.a11y.addLabel(y.prevButton,y.params.prevSlideMessage)),a(y.container).append(y.a11y.liveRegion)},initPagination:function(){y.params.pagination&&y.params.paginationClickable&&y.bullets&&y.bullets.length&&y.bullets.each(function(){var e=a(this);y.a11y.makeFocusable(e),y.a11y.addRole(e,"button"),y.a11y.addLabel(e,y.params.paginationBulletMessage.replace(/{{index}}/,e.index()+1))})},destroy:function(){y.a11y.liveRegion&&y.a11y.liveRegion.length>0&&y.a11y.liveRegion.remove()}},y.init=function(){y.params.loop&&y.createLoop(),y.updateContainerSize(),y.updateSlidesSize(),y.updatePagination(),y.params.scrollbar&&y.scrollbar&&(y.scrollbar.set(),y.params.scrollbarDraggable&&y.scrollbar.enableDraggable()),"slide"!==y.params.effect&&y.effects[y.params.effect]&&(y.params.loop||y.updateProgress(),y.effects[y.params.effect].setTranslate()),y.params.loop?y.slideTo(y.params.initialSlide+y.loopedSlides,0,y.params.runCallbacksOnInit):(y.slideTo(y.params.initialSlide,0,y.params.runCallbacksOnInit),0===y.params.initialSlide&&(y.parallax&&y.params.parallax&&y.parallax.setTranslate(),y.lazy&&y.params.lazyLoading&&(y.lazy.load(),y.lazy.initialImageLoaded=!0))),y.attachEvents(),y.params.observer&&y.support.observer&&y.initObservers(),y.params.preloadImages&&!y.params.lazyLoading&&y.preloadImages(),y.params.autoplay&&y.startAutoplay(),y.params.keyboardControl&&y.enableKeyboardControl&&y.enableKeyboardControl(),y.params.mousewheelControl&&y.enableMousewheelControl&&y.enableMousewheelControl(),
y.params.hashnav&&y.hashnav&&y.hashnav.init(),y.params.a11y&&y.a11y&&y.a11y.init(),y.emit("onInit",y)},y.cleanupStyles=function(){y.container.removeClass(y.classNames.join(" ")).removeAttr("style"),y.wrapper.removeAttr("style"),y.slides&&y.slides.length&&y.slides.removeClass([y.params.slideVisibleClass,y.params.slideActiveClass,y.params.slideNextClass,y.params.slidePrevClass].join(" ")).removeAttr("style").removeAttr("data-swiper-column").removeAttr("data-swiper-row"),y.paginationContainer&&y.paginationContainer.length&&y.paginationContainer.removeClass(y.params.paginationHiddenClass),y.bullets&&y.bullets.length&&y.bullets.removeClass(y.params.bulletActiveClass),y.params.prevButton&&a(y.params.prevButton).removeClass(y.params.buttonDisabledClass),y.params.nextButton&&a(y.params.nextButton).removeClass(y.params.buttonDisabledClass),y.params.scrollbar&&y.scrollbar&&(y.scrollbar.track&&y.scrollbar.track.length&&y.scrollbar.track.removeAttr("style"),y.scrollbar.drag&&y.scrollbar.drag.length&&y.scrollbar.drag.removeAttr("style"))},y.destroy=function(e,a){y.detachEvents(),y.stopAutoplay(),y.params.scrollbar&&y.scrollbar&&y.params.scrollbarDraggable&&y.scrollbar.disableDraggable(),y.params.loop&&y.destroyLoop(),a&&y.cleanupStyles(),y.disconnectObservers(),y.params.keyboardControl&&y.disableKeyboardControl&&y.disableKeyboardControl(),y.params.mousewheelControl&&y.disableMousewheelControl&&y.disableMousewheelControl(),y.params.a11y&&y.a11y&&y.a11y.destroy(),y.emit("onDestroy"),e!==!1&&(y=null)},y.init(),y}};t.prototype={isSafari:function(){var e=navigator.userAgent.toLowerCase();return e.indexOf("safari")>=0&&e.indexOf("chrome")<0&&e.indexOf("android")<0}(),isUiWebView:/(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(navigator.userAgent),isArray:function(e){return"[object Array]"===Object.prototype.toString.apply(e)},browser:{ie:window.navigator.pointerEnabled||window.navigator.msPointerEnabled,ieTouch:window.navigator.msPointerEnabled&&window.navigator.msMaxTouchPoints>1||window.navigator.pointerEnabled&&window.navigator.maxTouchPoints>1},device:function(){var e=navigator.userAgent,a=e.match(/(Android);?[\s\/]+([\d.]+)?/),t=e.match(/(iPad).*OS\s([\d_]+)/),s=e.match(/(iPod)(.*OS\s([\d_]+))?/),r=!t&&e.match(/(iPhone\sOS)\s([\d_]+)/);return{ios:t||r||s,android:a}}(),support:{touch:window.Modernizr&&Modernizr.touch===!0||function(){return!!("ontouchstart"in window||window.DocumentTouch&&document instanceof DocumentTouch)}(),transforms3d:window.Modernizr&&Modernizr.csstransforms3d===!0||function(){var e=document.createElement("div").style;return"webkitPerspective"in e||"MozPerspective"in e||"OPerspective"in e||"MsPerspective"in e||"perspective"in e}(),flexbox:function(){for(var e=document.createElement("div").style,a="alignItems webkitAlignItems webkitBoxAlign msFlexAlign mozBoxAlign webkitFlexDirection msFlexDirection mozBoxDirection mozBoxOrient webkitBoxDirection webkitBoxOrient".split(" "),t=0;t<a.length;t++)if(a[t]in e)return!0}(),observer:function(){return"MutationObserver"in window||"WebkitMutationObserver"in window}()},plugins:{}};for(var s=["jQuery","Zepto","Dom7"],r=0;r<s.length;r++)window[s[r]]&&e(window[s[r]]);var i;i="undefined"==typeof Dom7?window.Dom7||window.Zepto||window.jQuery:Dom7,i&&("transitionEnd"in i.fn||(i.fn.transitionEnd=function(e){function a(i){if(i.target===this)for(e.call(this,i),t=0;t<s.length;t++)r.off(s[t],a)}var t,s=["webkitTransitionEnd","transitionend","oTransitionEnd","MSTransitionEnd","msTransitionEnd"],r=this;if(e)for(t=0;t<s.length;t++)r.on(s[t],a);return this}),"transform"in i.fn||(i.fn.transform=function(e){for(var a=0;a<this.length;a++){var t=this[a].style;t.webkitTransform=t.MsTransform=t.msTransform=t.MozTransform=t.OTransform=t.transform=e}return this}),"transition"in i.fn||(i.fn.transition=function(e){"string"!=typeof e&&(e+="ms");for(var a=0;a<this.length;a++){var t=this[a].style;t.webkitTransitionDuration=t.MsTransitionDuration=t.msTransitionDuration=t.MozTransitionDuration=t.OTransitionDuration=t.transitionDuration=e}return this})),window.Swiper=t}(),"undefined"!=typeof module?module.exports=window.Swiper:"function"==typeof define&&define.amd&&define([],function(){"use strict";return window.Swiper});
//# sourceMappingURL=maps/swiper.jquery.min.js.map
; 
