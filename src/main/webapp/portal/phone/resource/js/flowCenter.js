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
