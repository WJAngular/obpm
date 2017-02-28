var EDEN = {};

EDEN.Main = {
	Config : {
		tabStack : ["tabs_homepage"],//tab顺序 默认首页
		userId : "",
		contextPath : ""
	},
	Util : {
		//更新tab顺序
		tabOrder : function(tabStr,tabArray,active){
			var tabStackIndex = $.inArray(tabStr,tabArray);
			if(tabStackIndex >= 0){
				tabArray.splice(tabStackIndex,1);
			}
			if(active == "add"){
				tabArray.push(tabStr);
			}
		},
		
		//调整main各元素尺寸
		setMainElement : function(){
			var $sidebar = $("#wrapper>.sidebar");
			var $sideMenu = $("#tabs_menu");
			var $pageWrapper = $("#wrapper>.page-wrapper");			
			var $tabsPreview = $("#tabsPreview");
			var _winHeight = $(window).height();
			var _winWidth = $(window).width();
			var _scrollHeight = _winHeight - 60;
			var _navColNum = parseInt(_winWidth / 330);
			
			//初始化sidebar菜单滚动条
			$sidebar.find(".menu").slimscroll({
				  height: _scrollHeight,
				  position: 'left',
				  distance: '-10px'
			});
			//$sidebar.find(".menu").css("overflow","");
			$sidebar.find(".menu").parent().css("overflow","");
			
			//根据屏幕宽度调整sidebar菜单对应的class
			if(_winWidth <= 1024){
				$("body").addClass("zoom-s");
				$("#zoomBtn").html("<i class='fa fa-caret-right' aria-hidden='true'></i>");
				$sideMenu.find("li[menu='show']").find(".menu-second").addClass("menu-second-pop");
				$sideMenu.find("li[menu='show']").attr("menu","pop");
			}else{
				$("body").removeClass("zoom-s");
				$("#zoomBtn").html("<i class='fa fa-caret-left' aria-hidden='true'></i>");
				$sideMenu.find("li[menu='pop']").find(".menu-second").removeClass("menu-second-pop");
				$sideMenu.find("li[menu='pop']").attr("menu","show");
			}
			
			//调整右侧内容高度
			$pageWrapper.height(_scrollHeight);

			//初始化预览窗口滚动条并调整样式
			$tabsPreview.find("ul.tabs-preview-panel").slimscroll({
				  height: _winHeight,
				  width: _winWidth
			});
			$tabsPreview.find("ul.tabs-preview-panel").css({
				"padding-left":(_winWidth-330*_navColNum)/2,
				"padding-right":(_winWidth-330*_navColNum)/2,
				"padding-top":"65px",
				"padding-bottom":"65px"
			})		
			$tabsPreview.find(".slimScrollDiv").css({
				"overflow":"",
				"position": "fixed",
				"display": "none",
				"top": "0px",
				"left": "0px",
				"z-index": "1100"
			});
			$('#backBlur').removeClass().hide();
		},
		
		//调整 侧边 顶部 内容 激活窗口class
		activePanel : function(id){
			var $tabs = $("#tabs");
			var $tabsPreview = $("#tabsPreview");
			var $tabsNavBar = $("ul.navbar-tabs-ul");
			var $active = $tabsPreview.find("[href*='#" + id + "']");

			$(".sidebar").find("li").removeClass("active");
			
			var _src = $("#"+id).find("iframe").attr("src");
			var $actSideBar = $(".sidebar").find("a[data-url='"+_src+"']").parent("[tabid='"+id+"']");
			var $actSCollapse = $actSideBar.parent().parent();
				
			$actSideBar.addClass("active");
			$actSCollapse.addClass("active");

			if($actSCollapse.attr("menu")=="show"){
				$actSCollapse.find("ul").collapse('show');
				$(".sidebar").find("li[menu='show']").not($actSCollapse).find("ul").collapse('hide');
			}else{
				$(".sidebar").find("li[menu='show']").find("ul").collapse('hide');
			}

			if($actSCollapse.attr("menu")=="pop"){
				$actSCollapse.addClass("active");
			}

			$tabs.find(".tabs-item-wrapper").removeClass("active");
			$tabs.find("#"+id).addClass("active");
			
			$tabsPreview.find("li").removeClass("active");
			$tabsPreview.find("li>a[href*='#" + id + "']").parent().addClass("active");
			
			$tabsNavBar.find(".navbar-tabs-item").removeClass("selected");
			$tabsNavBar.find(".navbar-tabs-item[data-id='"+id+"']").addClass("selected");

			$("#navbar-tabs-preview>a").find("span.badge").text($tabsNavBar.find(".navbar-tabs-item").size());
		},
		
		//计算顶部tabs宽度
		navTabRefresh : function(){
			var $navTabs = $(".navbar-tabs");
			var $navTabPanel = $navTabs.find(".navbar-tabs-panel");
			var $navTabUl = $navTabPanel.find(".navbar-tabs-ul");
			var $navTabPreview = $("#navbar-tabs-preview");
			var navUlWidth = 0;
			var navPanelWidth = $navTabs.width()-46;
			$navTabUl.find("li").each(function(){
				var _width = $(this).outerWidth();
				navUlWidth += _width;
			})
			if(navPanelWidth < navUlWidth){
				$navTabPanel.width(navPanelWidth);
				$navTabUl.width(navUlWidth);
				var $navTabS = $navTabUl.find(".navbar-tabs-item.selected");
				var _width = $navTabS.outerWidth();
				var _left = $navTabS.position().left;
				var _right = navUlWidth - _left - _width;
				if(_left > 0){
					if(_left + _width <= navPanelWidth){
						$navTabUl.css({
							"position":"absolute",
							"left":"0px"
						})
					}else{
						if(_right + _width <= navPanelWidth){
							$navTabUl.css({
								"position":"absolute",
								"left":"-"+(navUlWidth-navPanelWidth)+"px"
							})
						}else{
							$navTabUl.css({
								"position":"absolute",
								"left":"-"+ parseInt(_left-navPanelWidth/2) +"px"
							})
						}
					}
				}else{
					$navTabUl.css({
						"position":"absolute",
						"left":"0px"
					})
				}
			}else{
				$navTabPanel.width("auto");
				$navTabUl.width("auto");
				$navTabUl.css({
					"position":"static",
					"left":"0px"
				})
			}
		}
	},
	init: function(){
		EDEN.Main.Config.userId = $("#userId").val();
		EDEN.Main.Config.contextPath = $("#contextPath").val();
		
		this.bindEven();
		this.renderTabs.init();
		this.renderHeader.init();
		this.renderSidebar.init();
		
		EDEN.Main.Util.setMainElement();
		
		if($(window).width() <= 1024){
			$("body").addClass("zoom-s");
		}
	},
	bindEven : function(){
		
		//窗口改变调整尺寸 
		$(window).resize(function() {
			EDEN.Main.Util.setMainElement();
		});
		
		//左侧菜单缩放按钮
		$("#zoomBtn").on("click",function(){
			var $this = $(this);
			var $sidebar = $("#wrapper>.sidebar");
			var $sideMenu = $("#tabs_menu");
			var _sideMenuWidth = $sideMenu.width();			
			
			if(_sideMenuWidth > 55){
				$this.html("<i class='fa fa-caret-right' aria-hidden='true'></i>");	
				$sideMenu.find("li[menu='show']").find(".menu-second").addClass("menu-second-pop");
				$sideMenu.find("li[menu='show']").attr("menu","pop");
				$("body").addClass("zoom-s");
			}else{
				$this.html("<i class='fa fa-caret-left' aria-hidden='true'></i>");
				$sideMenu.find("li[menu='pop']").find(".menu-second").removeClass("menu-second-pop");
				$sideMenu.find("li[menu='pop']").attr("menu","show");
				$("body").removeClass("zoom-s");
			}
		})
	},
	
	renderTabs: {
		init : function(){
			this.bindEven();	
		},
		bindEven : function(){
			var $tabsPreview = $("#tabsPreview");
			var $navTabs = $(".navbar-tabs");
			var $tabs = $("#tabs");
			$tabsPreview.click(function(event){
				if (!$(event.target).hasClass("ui-icon-close")){
					$('#backBlur').removeClass().addClass('animated fadeOut');
					$('#backBlur').hide();
					$tabsPreview.hide();
				}
			})
			
			//激活窗口
			$tabsPreview.on("click","a[data-url]",function(){
				var tabActHref = $(this).attr("href");
				var tabActID = tabActHref.substring(1);
				EDEN.Main.Util.tabOrder(tabActHref.substring(1),EDEN.Main.Config.tabStack,"add");
				EDEN.Main.Util.activePanel(tabActID);
			}),
			
			
			//关闭全部窗口
			$tabsPreview.on("click","a.nav-closeAll-btn",function(){
				$tabsPreview.find(".tabs-preview-panel").find("span.glyphicon-remove").each(function(){
					var id = $(this).parent("li").data("id");
					EDEN.Main.renderTabs.closeTab(id);
				})
			}),
			//关闭窗口
			$tabsPreview.on("click","span.glyphicon-remove",function() {
				var id = $(this).parent("li").data("id");
				EDEN.Main.renderTabs.closeTab(id);
			});
			//顶部nav关闭窗口
			$navTabs.on("click","a.tab-btn-close",function() {
				var id = $(this).parent("li").data("id");
				EDEN.Main.renderTabs.closeTab(id);
			});
			//顶部nav激活窗口
			$navTabs.on("click","a.tab-btn-title",function() {
				var $this = $(this);
				var panelId = $this.data("id");
				EDEN.Main.Util.activePanel(panelId);
			});
		},
		//新增或激活tab
		addTab : function(id, label, url){
			var template = {
					tab : "<li data-id='#"+"{id}'><a href='#"+"{href}' data-url='#"+"{url}'><div class='status-box'><span class='status'>" + current_page + "</span></div>" +
							"<div class='nav-title'>#" + "{label}</div></a>" +
							"<iframe data-render='false' frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe>" +
							"<span class='glyphicon glyphicon-remove'></span></li>",
					tabPic : "<li data-id='#"+"{id}'><a href='#"+"{href}' data-url='#"+"{url}'><div class='status-box'>"
							+"<span class='status'>" + current_page + "</span></div><div class='nav-title'>#" + "{label}</div></a>"
							+"<img src='#" + "{pic}' style='width: 100%;height: 100%;'>"
							+"<span class='glyphicon glyphicon-remove'></span></li>",
					nav : "<li class='navbar-tabs-item' data-id='#"+"{id}'>"
							+"<a class='tab-btn-title' data-id='#"+"{id}' data-url='#"+"{url}'><div class='nav-title'>#"+"{label}</div></a>"
							+"<a class='tab-btn-close'><i class='fa fa-times'></i></a></li>",	
				}
			
			var $tabs = $("#tabs");
			var $tabsPreview = $("#tabsPreview");
			var $tabsNavBar = $("ul.navbar-tabs-ul");
			var $active = $tabsPreview.find("[href*='#" + id + "']");

			//如果id已经添加，则选中
			if ($active.size() > 0) {
				$tabs.find("#"+ id + ">iframe").attr("src",url);
			} else {
				var $previewItem = "";
				var $navItem = "";
				if(id == "tabs_pm_task" || id == "tabs_pm_project" || id == "tabs_pm_label"){
					var pic = "../share/images/logo/tab-pm.jpg";
					$previewItem = $(template.tabPic.replace(/#\{href\}/g,"#" + id)
							.replace(/#\{label\}/g, label)
							.replace(/#\{url\}/g, url)
							.replace(/#\{id\}/g, id)
							.replace(/#\{pic\}/g, pic));
					$navItem = $(template.nav.replace(/#\{id\}/g, id).replace(/#\{label\}/g, label).replace(/#\{url\}/g, url));
				}else if(id == "tabs_qm_homepage" || id == "tabs_qm_center"){
					var pic = "../share/images/logo/tab-qm.jpg";
					$previewItem = $(template.tabPic.replace(/#\{href\}/g,"#" + id)
							.replace(/#\{label\}/g, label)
							.replace(/#\{url\}/g, url)
							.replace(/#\{id\}/g, id)
							.replace(/#\{pic\}/g, pic));
					$navItem = $(template.nav.replace(/#\{id\}/g, id).replace(/#\{label\}/g, label).replace(/#\{url\}/g, url));
				}else{
					$previewItem = $(template.tab.replace(/#\{href\}/g,"#" + id)
							.replace(/#\{label\}/g, label)
							.replace(/#\{id\}/g, id)
							.replace(/#\{url\}/g, url));
					$navItem = $(template.nav.replace(/#\{id\}/g, id).replace(/#\{label\}/g, label).replace(/#\{url\}/g, url));
				}
				var $contentIframe = $("<div class='tabs-item-wrapper fadeInRightMain animated tab-pane' "
									 +"style='z-index:2;position: relative;background: #f6f7fb;' id='" + id + "' >"
									 +"<iframe src='" + url + "' _tags='window' frameborder='no' "
									 +"border='0' marginwidth='0' marginheight='0' height='100%' width='100%'>" +
									 +"</iframe></div>");
				
				$tabsNavBar.append($navItem);
				$tabs.append($contentIframe);
				$tabsPreview.find(".tabs-preview-panel").append($previewItem);
			}
			
			EDEN.Main.Util.activePanel(id);
			EDEN.Main.Util.tabOrder(id,EDEN.Main.Config.tabStack,"add");
			EDEN.Main.Util.navTabRefresh();
		},
		//关闭tab
		closeTab : function(id) {
			var $tabs = $("#tabs");
			var $tabsPreview = $("#tabsPreview");
			var $tabsNavBar = $("ul.navbar-tabs-ul");

			if(!id || id == ""){
				id = $tabsNavBar.find(".selected").data("id");	
			}

			if(id != "tabs_homepage"){
				$tabs.find("#" + id).remove();
				$tabsPreview.find("li>a[href*='#" + id + "']").parent().remove();
				$tabsNavBar.find(".navbar-tabs-item[data-id='" + id + "']").remove();
				
				EDEN.Main.Util.tabOrder(id,EDEN.Main.Config.tabStack);
				var tabActiveID = EDEN.Main.Config.tabStack[EDEN.Main.Config.tabStack.length-1];

				EDEN.Main.Util.activePanel(tabActiveID);
				EDEN.Main.Util.navTabRefresh ();
			}
		}
	},
	
	renderHeader : {
		init : function(){
			this.bindEven();
			this.renderUserPic();
		},
		bindEven : function(){
			//用户
			$(".navbar-menu").find(".top-user").mouseover(function(){   
		    	$(this).find(".dropdown").addClass('open');    
		    }).mouseout(function(){
		    	$(this).find(".dropdown").removeClass('open');
		    });
			//管理
			$(".user-manageDomain").on("click",function(){
				var id = "manageDomain";
				var title = $(this).find("a#manageDomain").attr("title");
				var url = $(this).find("a#manageDomain").attr("_url");
				EDEN.Main.renderTabs.addTab("manageDomainTab",title,url);
			});
			//个人设置
			$(".user-person-setting").on("click",function(){
				var _userId = $("#userId").val();				
				var _url = "../user/editPersonal.action?editPersonalId=" + _userId;
				var _path = "../H5/resource/component/artDialog";
				OBPM.dialog.show({
					opener: window,
					width: 920,
					height: 550,
					url: _url,
					icon:"icons_3",
					path: _path,
					title: "个人设置",
					close: function(rtn) {
						//
					}
				});
			});	
			//预览
			$("#navbar-tabs-preview").on("click",function(){
				var $tabsPreview = $("#tabsPreview");
				var $backBlur = $("#backBlur");

				$backBlur.show();
				$tabsPreview.find(".slimScrollDiv").show();
				$tabsPreview.show();
				$tabsPreview.find('li').addClass('animatedFast zoomIn');
				$backBlur.removeClass().addClass('animated fadeIn');
				
				$tabsPreview.find("iframe[data-render='false']").each(function(){
					var $this = $(this);
					var $thisIframeCon = $this.contents();
					var _href = $this.prev().attr("href");
					var _id = _href.substring(1);
					var $_document = $("#"+_id).find("iframe").contents();
					var $_noJsDoc = $_document.find("html").clone()
					$_noJsDoc.find("script").remove();
					$thisIframeCon.find("html").html($_noJsDoc.html());
					$this.attr("data-render","true");
				})
			})
			//顶部按钮点击addTab
			$(".top-tool-bar li").click(function() {
				var $this = $(this);
				if($this.attr("menu")=="open"){
					$this.siblings("[menu='pop']").find("ul.menu-second-pop").removeClass("in");
					var url = $this.children("a").attr("_url");
					var tabid = $this.attr("tabid");
					var title = $this.find("h5").text();
					if(tabid==""||tabid==undefined||!tabid){
						tabid = "iframe_" + $this.attr("id");
						title = $this.text();
					}								
					EDEN.Main.renderTabs.addTab(tabid, title, url);
				}
				$this.siblings("[menu='pop']").find("ul.menu-second-pop").removeClass("in");
			});
		},
		renderUserPic : function(){
			var userPic = Common.Util.getAvatar(EDEN.Main.Config.userId,EDEN.Main.Config.contextPath)
			if(userPic!="" && userPic!=undefined){
				$(".user-box>img").attr("src",userPic);
			}
		}
	},
	renderSidebar : {
		init : function(){
			this.bindEven();
		},
		bindEven : function(){
			//左侧菜单点击addTab
			$("#tabs_menu li").hover(function() {
				var $this = $(this);
				
				if (!$this.hasClass("active")) {
					$this.addClass("hover");
				}
				if ($this.attr("menu")=="pop"){
					$this.parents(".menu").css("overflow","");//临时解决缩小后菜单不能弹出
					$this.addClass("hover");
					$this.find(".menu-second-pop").addClass('in'); 
					$this.find(".menu-second-pop").attr("style","");
				}
			}, function() {
				var $this = $(this);
				if($this.parents(".menu-second").size()<=0){
					$this.parents(".menu").css("overflow","hidden");//临时解决缩小后菜单不能弹出
				}
				$this.removeClass("hover");
				if (!$this.hasClass("active")) {
					
				}
				if ($this.attr("menu")=="pop"){
					$this.removeClass("hover");
					$this.find(".menu-second-pop").removeClass('in'); 
				}
			});
			
			$("#tabs_menu li").click(function() {
				var $this = $(this);
				//利用menu判断是展开二级菜单还是打开页面
				if($this.attr("menu")=="open"){
					if($this.parents("li[menu='show']").size()<=0){
						if($this.parents("li[menu='pop']").size()<=0){
							$("#tabs_menu li").find("ul").collapse('hide');
						}
					}
					var url = $this.children("a").data("url");
					var tabid = $this.attr("tabid");

					if($this.find("h5").size()<=0){
						var title = $this.text();
					}else{
						var title = $this.find("h5").text();
					}
					
					if(tabid==""||tabid==undefined||!tabid){
						tabid = "iframe_" + $this.attr("id");
						title = $this.text();
					}
					
					if(tabid == "iframe_tabs_pm"){
						if($("#iframe_tabs_pm").size()>0){
							$("#iframe_tabs_pm").find("iframe").attr("src",url);
						}
					}
					if(tabid == "iframe_tabs_qm"){
						if($("#iframe_tabs_qm").size()>0){
							$("#iframe_tabs_qm").find("iframe").attr("src",url);
						}
					}
					
					EDEN.Main.renderTabs.addTab(tabid, title, url);
				}else if($this.attr("menu")=="show"){
					$this.siblings("[menu='show']").find("ul").collapse('hide');
					$("#tabs_menu>li.active").removeClass("active");
					$this.addClass("active");
				}
				$this.siblings("[menu='pop']").find("ul").collapse('hide');
				
			});
		}
	}
}

