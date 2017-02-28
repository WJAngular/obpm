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
}