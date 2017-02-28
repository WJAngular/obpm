/**
 * Contacts 核心类
 * 封装Contacts界面渲染与交互行为
 */
;
var Contacts = Contacts || {
	
	config : {
		//判断时候能选择
		select : false
	},
	
	/**
	 * 首页
	 */
	main : {
		init : function(showtype) {
			Contacts.Util.controlLoading("show");
			document.title='通讯录';
			Contacts.main.renderMain(showtype);
			Contacts.Util.controlLoading("hide");
		},
		
		//根据屏幕调整界面样式
		renderMain : function(showtype){
			var $mainPanel = $('#tpl_contacts_main');
        	var $mainHtml = $($('#tpl_contacts_main').html());
        	var $mainSearchShow = $mainHtml.find(".contacts-search-show");
        	if($mainHtml.find(".weui_bar_item_on").size()<=0){
        		if(showtype == "favorite"){
            		$mainHtml.find(".weui_navbar_item").removeClass("weui_bar_item_on");
            		$mainHtml.find(".weui_navbar_item[data-showtype='favorite']").addClass("weui_bar_item_on");
            	}else{
            		$mainHtml.find(".weui_navbar_item[data-showtype='all']").addClass("weui_bar_item_on");
            		showtype = "all";
            	}
        	}else{
        		showtype = $mainHtml.find(".weui_bar_item_on").data("showtype");
        	}
        	
        	var breadcrumb = {
					"name": "返回" + $mainHtml.find(".weui_bar_item_on").find("div").text(),
					"type": $mainHtml.find(".weui_bar_item_on").data("showtype")
				}
        	Contacts.Util.cache.currentId = "";//首页时清空缓存id
        	Contacts.Util.cache.breadcrumbItem = [];
        	Contacts.Util.cache.breadcrumbMain = [];
			Contacts.Util.cache.breadcrumbMain.push(breadcrumb);
        	
        	var winHeight = $(window).height();
        	
        	//调整列表面板高度
        	$("#contacts").height("auto"); 
        	if($("#contacts-select-panel").is(":visible")){
        		$mainSearchShow.height(winHeight-44-70-127); 
        	}else{
        		$mainSearchShow.height(winHeight-44-70);
        	}
        	
        	Contacts.main.renderList(showtype, $mainSearchShow.find(".weui_cells"), {});
        	$mainPanel.html($mainHtml);
		},
		
		//渲染首页列表
		renderList : function(showtype,searchShowPanel,params){
			switch (showtype){
				//全部
				case "all" :
					Contacts.Service.getAllUser(function(result){
						if(result == ""){
							Contacts.Util.controlPlaceholder("show");
						}else{
							Contacts.Util.controlPlaceholder("hide");
						}
						Contacts.main.renderListItem(showtype,searchShowPanel,result);
					});
					break;
				//部门
				case "dept" :
					Contacts.Service.getContactsTree(params,function(result){
						if(result == ""){
							Contacts.Util.controlPlaceholder("show");
						}else{
							Contacts.Util.controlPlaceholder("hide");
						}
						Contacts.main.renderListItem(showtype,searchShowPanel,result);
					});
					break;
				//角色
				case "role" :
					Contacts.Service.getRoleTree(params,function(result){
						if(result == ""){
							Contacts.Util.controlPlaceholder("show");
						}else{
							Contacts.Util.controlPlaceholder("hide");
						}
						Contacts.main.renderListItem(showtype,searchShowPanel,result);
					});
					break;
				//常用
				case "favorite" :
					Contacts.Service.getFavorite(params,function(result){
						if(result == ""){
							Contacts.Util.controlPlaceholder("show");
						}else{
							Contacts.Util.controlPlaceholder("hide");
						}
						Contacts.main.renderListItem(showtype,searchShowPanel,result);
					});
					break;
				//搜索
				case "search" :
					Contacts.Service.searchContacts(params,function(result){
						if(result == ""){
							Contacts.Util.controlPlaceholder("show");
						}else{
							Contacts.Util.controlPlaceholder("hide");
						}
						Contacts.main.renderListItem(showtype,searchShowPanel,result);
    				});	
					break;
			}
			
			
			
			
			Contacts.selectList.init();
		},
		renderListItem : function(showtype,searchShowPanel,result){

			var userCounts = [];
			if(showtype == "dept"){
				for(var i = 0;i < result.length;i++){
					var params = {"id":result[i].id,"type":showtype == "dept" ? "2" : "3" };
					Contacts.Service.getRoleOrDeptUserCounts(params,function(userCount){
						userCounts.push(userCount);
					});	
				}
			}

			var $showPanel = $("#contacts .contacts-search-show .weui_cells");
			var html = template('atp-contacts-list-item', {
					select:Contacts.config.select,
					showtype:showtype,
					list:result,
					userCounts:userCounts
				});
			
			if($showPanel.size() <= 0){
				searchShowPanel.html(html)
			}else{
				$showPanel.html(html)
			}
		},
		bindEven : function(){
			var $contacts = $("#contacts");

			//搜索
			$contacts.on('focus', '#search_input', function () {
                var $weuiSearchBar = $('#search_bar');
                $weuiSearchBar.addClass('weui_search_focusing');
            }).on('blur', '#search_input', function () {
                var $weuiSearchBar = $('#search_bar');
                $weuiSearchBar.removeClass('weui_search_focusing');
                if ($(this).val()) {
                    $('#search_text').hide();
                } else {
                    $('#search_text').show();
                }
            }).on('input', '#search_input', function () {
                var $searchShow = $("#search_show");
                if ($(this).val()) {
                    $searchShow.show();
                } else {
                    $searchShow.hide();
                }
            }).on('click', '#search_cancel', function () {
                $("#search_show").hide();
                $('#search_input').val('');
            }).on('click', '#search_clear', function () {
                $("#search_show").hide();
                $('#search_input').val('');
            }).on('keydown','#search_input',function(event){
            	var $this = $(this);
            	if(event.keyCode == "13"){
            		var params = {"keyWord":$this.val()}
            		Contacts.main.renderList("search", $(".contacts-main .contacts-search-show .weui_cells"),params);
            		return false
            	}
            	
            });

			//顶部页签切换		
			$contacts.on('click', '.weui_navbar_item', function () {
				Contacts.Util.controlLoading("show");

                $(this).addClass('weui_bar_item_on').siblings('.weui_bar_item_on').removeClass('weui_bar_item_on');
                var $mainHtml = $('.contacts-main').html();
	        	var showtype = $(this).data("showtype");
	        	
	        	var breadcrumb = {
						"name": "返回" + $(this).find("div").text(),
						"type": showtype
					}
	        	Contacts.Util.cache.breadcrumbItem = [];
	        	Contacts.Util.cache.breadcrumbMain = [];
				Contacts.Util.cache.breadcrumbMain.push(breadcrumb);
	        	
	        	
	        	$('#tpl_contacts_main').html($mainHtml);
	        	Contacts.main.renderList(showtype, $(".contacts-main .contacts-search-show .weui_cells"), {});
	        	
	        	Contacts.Util.controlLoading("hide");
            });
			
			//列表点击
			$contacts.on("click", "a.weui_cell[data-type]", function () {
				//Contacts.Util.controlLoading("show");

				var $this = $(this);
				var showTypeNum = $this.data("type")
				var href = $this.data("href");
				var $activeNav = $("#contacts").find(".weui_navbar_item.weui_bar_item_on");
				Contacts.Util.cache.showTypeNum = showTypeNum;

				var scrollTop = {
						id : Contacts.Util.cache.currentId,
						top : $("#contacts").find(".contacts-search-show").scrollTop()
					}
				if(scrollTop.top == null){
					scrollTop.top = $("body").scrollTop();
				}
				Contacts.Util.cache.scrollTop.push(scrollTop);

				if(showTypeNum == "1"){
					Contacts.show.renderShow($this);
				}else if(showTypeNum == "2" || showTypeNum == "3" || showTypeNum == "4"){
					var breadcrumb = {
							"id": $this.data("id"),
							"name": $this.data("name")
						}
					
					Contacts.Util.cache.breadcrumbItem.push(breadcrumb);

				}

				window.location.href = href;
				
				/*setTimeout(function(){
	        		Contacts.Util.controlLoading("hide");
	        	},1000)*/

            });
			
			//列表点击
			$contacts.on("click", "a.breadcrumb-item[data-showtype]", function () {
				var $this = $(this);
				var showtype = $this.data("showtype");
				window.location.href = '#/:main';
            });
			
			
			
			//选中 移除选中 操作 为 日后用户选择框做准备 暂时注释掉 
			/*$contacts.on('change', 'input.weui_check', function () {
				var $contacts = $("#contacts");
            	var $selectPanel = $("#contacts-select-panel");
            	var $searchShowPanel = $contacts.find(".contacts-search-show");
            	var $this = $(this);
            	var $label = $this.parents("label.weui_check_label");
            	var id = $label.data("id");
            	var name = $label.data("name");
            	var avatar = $label.data("avatar");
            	var searchShowH = $searchShowPanel.height();
				
                if($("#contacts").find("input.weui_check:checked").size() > 0){ 
                	if($this.is(":checked")){
                		var html = template('atp-contacts-select-item', {
                    		"id":id,
                    		"name":name,
                    		"avatar":avatar,
        	    		});
                		if($selectPanel.find(".select-item[data-id='"+id+"']").size()<=0){
                			$selectPanel.find("#contacts-select-list").append(html);
                		}
                	}else{
                		$selectPanel.find(".select-item[data-id='"+id+"']").remove();
                	}
                	if($selectPanel.is(":hidden")){
                		$selectPanel.show()
                    	$searchShowPanel.height(searchShowH - 127)
                	}
                }else{
                	$searchShowPanel.height(searchShowH + 127)
                	$selectPanel.find(".select-item[data-id='"+id+"']").remove();
                	$selectPanel.hide()
                }
            });
            
            $("#contacts-select-panel").on('click', '.select-item', function () {
				var $this = $(this);
				var $selectPanel = $("#contacts-select-panel");
            	var $searchShowPanel = $contacts.find(".contacts-search-show");
				var searchShowH = $searchShowPanel.height();
				var selectH = $selectPanel.outerHeight();
            	var id = $this.data("id");
            	
				$this.remove();
				$("#contacts").find("#item-"+id).prop("checked",false);
				if($selectPanel.find(".select-item").size()<=0){
					$selectPanel.hide();
					$searchShowPanel.height(searchShowH + 127)
				}	
				return false;
			})*/
		}
	},
	/**
	 * 渲染列表页面
	 */
	list : {

		init : function(id,showtype) {
			
			Contacts.Util.controlLoading("show");
			document.title='通讯录';
			
			Contacts.Util.cache.currentId = id;//缓存当前id
			
			for(var i = 0; i < Contacts.Util.cache.breadcrumbItem.length; i++){
				if(Contacts.Util.cache.breadcrumbItem[i].id == id){
					Contacts.Util.cache.breadcrumbItem[i].current = "true";
				}else{
					Contacts.Util.cache.breadcrumbItem[i].current = "false";
				}
			}

			if(showtype == "role-3"){
				var params = {"applictaionId":id};
				Contacts.Service.getRoleTree(params,function(result){
					Contacts.list.renderList(result,showtype);
				});	
			}else if(showtype == "role-3-4"){
				var params = {"roleId":id};
				Contacts.Service.getRoleTree(params,function(result){
					Contacts.list.renderList(result,showtype);
				});	
			}else{
				var params = {"parentId":id};
				Contacts.Service.getContactsTree(params,function(result){
					Contacts.list.renderList(result,showtype);
				});	
			}
			Contacts.Util.controlLoading("hide");
		},
		renderList : function(result,showtype){
			var $mainPanel = $('#tpl_contacts_list');
        	var $mainHtml = $($('#tpl_contacts_list').html());
        	var currentBreadcrumbItem = [];
        	for(var i = 0; i < Contacts.Util.cache.breadcrumbItem.length; i++){
				if(Contacts.Util.cache.breadcrumbItem[i].current == "true"){
					currentBreadcrumbItem.push(Contacts.Util.cache.breadcrumbItem[i]);
				}
			}

        	var selectAllHtml = template('atp-contacts-list-item-all', {
	    			select: Contacts.config.select,
	        		list: Contacts.Util.cache.breadcrumbMain,
	        		current: currentBreadcrumbItem
    			});

        	var userCounts = [];
			for(var i = 0;i < result.length;i++){
				if(result[i].type == "2" || result[i].type == "4"){
					var params = {"id":result[i].id,"type":result[i].type};
					Contacts.Service.getRoleOrDeptUserCounts(params,function(userCount){
						userCounts.push(userCount);
					});	
				}else{
					userCounts.push("0");
				}
			}
			
        	
        	var html = template('atp-contacts-list-item', {
        			select:Contacts.config.select,
        			showtype:showtype,
	        		list:result,
	        		userCounts:userCounts
	    		});
        	
        	if($("#contacts-select-panel").is(":visible")){
        		$("#contacts").height($(window).height()-127); 
        	}

        	if(result == ""){
				Contacts.Util.controlPlaceholder("show");
				$("#contacts").height($(window).height()); 
			}else{
				Contacts.Util.controlPlaceholder("hide");
			}
        	
        	$mainHtml.html(selectAllHtml + html);
        	$mainPanel.html($mainHtml);
		}
	},
	/**
	 * 详细页面
	 */
	show : {
		/**
		 * 渲染详细页面
		 */
		renderShow: function(obj){
			Contacts.Util.controlLoading("show");
			
			document.title='个人信息';
			
			var $mainPanel = $('#tpl_contacts_show');
        	var $mainHtml = $($('#tpl_contacts_show').html());	
        	var isFavorite
        	Contacts.Service.isFavoriteContact({userId:obj.data("id")},function(result){
        		isFavorite = result
			});
        	var data = {
        		id : obj.data("id"),
        		avatar : obj.data("avatar"),
        		name : obj.data("name"),
        		mobile : obj.data("mobile"),
        		mobile2 : obj.data("mobile2"),
        		tel : obj.data("mobile"),
        		sms : obj.data("mobile"),
        		email : obj.data("email"),
        		mailto : obj.data("email"),
        		dept : obj.data("dept"),
        		isFavorite : isFavorite
    		};
        	var html = template('atp-contacts-show', data);
        	$mainPanel.html(html);
        	Contacts.Util.controlLoading("hide");
		},
		/**
		 * 绑定事件
		 */
		bindEven : function(){
			var $contacts = $("#contacts");
			//加入移除常用联系人
			$contacts.on('touchend', '.favorite', function (){

				Contacts.Util.controlLoading("show");
				
				var $this = $(this);
				var id = $this.parents(".weui_cells").data("userid")
				if($this.attr("data-isfavorite") == "1"){
					Contacts.Service.removeFavoriteContact({userId:id},function(result){
						$this.attr("data-isfavorite","2");;
		        		$this.html("<i class='fa fa-star-o'></i>")
					});
				}else{
					Contacts.Service.addFavorite({userId:id},function(result){
		        		$this.attr("data-isfavorite","1");
						$this.html("<i class='fa fa-star'></i>")
					});
				}
				
				Contacts.Util.controlLoading("hide");
			})
		}
	},
	/**
	 * 渲染选中项
	 */
	selectList : {
		init : function(){
			Contacts.selectList.renderSelectList();
		},
		renderSelectList : function(){
			var $contacts = $("#contacts");
			var $selectPanel = $("#contacts-select-panel");

			$selectPanel.find(".select-item").each(function(){
				var id = $(this).data("id");
				$contacts.find("input[data-id='"+id+"']").prop("checked",true);
			})
		}
	},
	/**
	 * 设置页面滚动条位置
	 */
	setScrollTop : function(id,position){
		var _scrollTop = Contacts.Util.cache.scrollTop;
		if(position == "main"){
			if(_scrollTop[0] && _scrollTop[0] != undefined){
				$("#contacts").find(".contacts-search-show").scrollTop(_scrollTop[0].top);
				Contacts.Util.cache.scrollTop = [];
			}
		}else if(position == "list"){
			var top = 0;
			for(var i = 0; i < _scrollTop.length; i++){
				if(_scrollTop[i].id == id){
					top = _scrollTop[i].top;
					_scrollTop.length = i;
				}
			}
			$("body").scrollTop(top);
		}
	}
}