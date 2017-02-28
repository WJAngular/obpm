
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
}