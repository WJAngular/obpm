(function($){
	$.fn.obpmTabNormalField =function(){
		return this.each(function(){
			var $field = jQuery(this);
			$field.removeAttr("moduleType");
			
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabNormal']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabNormal']").size() == 0);
				//}).obpmTabNormalField();  		//页签选项卡
			}).obpmTabCollapseField(); 
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabCollapse']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabCollapse']").size() == 0);
			}).obpmTabCollapseField();  		//折叠选项卡
			
			var html = "";
			// title
			var $fieldTitle = $field.find("[id='title']");

			var tabId=$field.attr("tabId");
			var fieldId=$fieldTitle.attr("fieldId");
			var titleDiv = "<div id='" + fieldId + "' class='basictab' style='margin-bottom: 10px;border-bottom: 1px solid #CACACA;padding-bottom: 10px;overflow-x: auto;'>";
			var titleUl = "<table cellSpacing='0' cellPadding='0'><tr>";
			$fieldTitle.find("div").each(function(){
				var formId=$(this).attr("formId");
				var isHidden=$(this).attr("isHidden");
				var isRefreshOnChanged=$(this).attr("isRefreshOnChanged");
				var name=$(this).attr("name");
				isRefreshOnChanged = (isRefreshOnChanged == "true");
				isHidden = (isHidden == "true");
								
				// 判断选项卡内是否含有非包含元素的控件
				var $moduleType = $field.find(".tabcontainer>div[formId='"+formId+"']").find("[moduleType]");
				var $calctext =  $field.find(".tabcontainer>div[formId='"+formId+"']").find(".calctext-field");
				if($moduleType.not("[moduleType=IncludedView]").size()==0 && $calctext.size()==0){
					isHidden = true;
				}

				// <li>
				var liHtml = "";

				liHtml += "<td><div id='li_" + formId + "'";

				if(isHidden){
					liHtml += " style='display:none'";
				}
				liHtml += ">";
				
				// <a>
				var aHtml = "";
				aHtml += "<a class='btn btn-positive btn-outlined' style='margin-right:10px;cursor:pointer'";
				if(isRefreshOnChanged){
					//aHtml += " callback=dy_refresh('" + formId + "')";
				}
				aHtml += " id='" + formId + "'";
				aHtml += " rel='content_" + formId + "'";
				aHtml += " title='" + name + "'>";
				aHtml += name + "</a>";
				liHtml += aHtml;
				liHtml += "</div></td>";
				titleUl += liHtml;
			});
			titleUl += "</tr></table>";
			titleDiv += titleUl;
			titleDiv += "</div>";
			
			// content
			$field.find("#tabcontainer > div").each(function(i){
				var formId=$(this).attr("formId");
				var isHidden=$(this).attr("isHidden");
				isHidden = (isHidden == "true");
				jQuery(this).attr("id","content_" + formId + "").addClass("tabcontent");
				if(i>=0){
					jQuery(this).hide();
				}
				if(isHidden){
					jQuery(this).css("display","none");	
					var tabIncludedID = $(this).find("[moduletype='IncludedView']").attr("_fieldid");
					$("body").find("a.control-item[href='#tab_"+tabIncludedID+"']").hide();
				}				
			});
			jQuery(titleDiv).replaceAll($fieldTitle);
			
			//初始化
			ddtabmenu.definemenu(fieldId,tabId);
		});
	};
	
	$.fn.obpmTabCollapseField =function(){
		return this.each(function(){
			var $field = jQuery(this);
			$field.addClass("subNavBox");
			$field.removeAttr("moduleType");
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabNormal']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabNormal']").size() == 0);
			//}).obpmTabNormalField();  		//页签选项卡
			}).obpmTabCollapseField(); 
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabCollapse']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabCollapse']").size() == 0);
			}).obpmTabCollapseField();  		//折叠选项卡
			
			var fieldId = $field.attr("fieldId");
			var tabIdList = $field.find("#tabIdList").text();
			//字符串转数组
			tabIdList = eval(tabIdList);
			$field.find("#tabIdList").remove();
			var html = "";
			$field.find("li").each(function(){
				jQuery(this).css("listStyle","none");
				//title
				$(this).find("#title").each(function(){
					var formId = $(this).attr("formId");
					var isHidden = $(this).attr("isHidden");
					var fieldId = $(this).attr("fieldId");
					var tabName = $(this).attr("tabName");
					var isOpen = $(this).attr("isOpen");
					isHidden = (isHidden == "true");
					isOpen = (isOpen == "true");
					//div
					var titleDivHtml = "";
					titleDivHtml += "<div id='li_" + formId + "'";
					if(isHidden){
						titleDivHtml += " style='display:none'";
						this.parentNode.style.display = "none";	//ie下面当有选项卡隐藏后，中间还是留有间隔，所以隐藏。
					}
					titleDivHtml += ">";
					//table
					var tableHtml = "";
					tableHtml = "<div class='subNav' id='zlight-nav'>"
					tableHtml += "<span class='icon icon-down up-color'></span>"
					tableHtml += tabName
					tableHtml += "</div>"




					// var imgId = "img_" + formId;
					// tableHtml += "<table width='100%' height='22' align='center' cellPadding='0' cellSpacing='0' id='" + formId
					// 	+ "' class='margin' onclick=toggleCollapse('" + fieldId + "',this.id)>";
					// tableHtml += "<tr>";

					// var imagePath = "../../share/component/tabField/collapse/images";
					// tableHtml += "<td width='5' align='left'><img src='" + imagePath + "/left_l_bar.gif'/></td>";
					// tableHtml += "<td width='30' align='center' background='" + imagePath + "/left_bar.gif'>";
					// tableHtml += "<img id='" + imgId + "' src='" + imagePath + "/";
					// if(isOpen)
					// 	tableHtml += "left_open";
					// else
					// 	tableHtml += "left_dot";
					// tableHtml += ".gif' /></td>";
					// tableHtml += "<td class='white' background='" + imagePath + "/left_bar.gif'>" + tabName + "</td>";
					// tableHtml += "<td width='5' align='right'><img src='" + imagePath + "/left_r_bar.gif'/></td>";
					// tableHtml += "</tr>";
					// tableHtml += "</table>";
					
					titleDivHtml += tableHtml;
					titleDivHtml += "</div>";
					html += titleDivHtml;
					jQuery(titleDivHtml).replaceAll(jQuery(this));
				});
				//content
				$(this).find("#content").each(function(){
					var formId=$(this).attr("formId");
					var isHidden=$(this).attr("isHidden");
					isHidden = (isHidden == "true");
					jQuery(this).attr("id","content_" + formId + "");
					jQuery(this).addClass("navContent");
					if(isHidden){
						jQuery(this).css("display","none");	
					}


				});

				$(window).scroll(function(){
					var subNavTop = $(this).scrollTop();
					$(".subNavBox>li").each(function() {

						if($(this).offset().top <= subNavTop){

							$(this).children("div[id^='li_']").addClass("navTitle").css({'position':'fixed','top':'0px'});
							
							$(this).children("div[id^='content_']").css({'padding-top':$(this).children("div[id^='li_']").outerHeight()+10+'px'});
							if($(this).next().offset().top - subNavTop <= $(this).children("div[id^='li_']").outerHeight()){
								$(this).children("div[id^='li_']").css({'position':'absolute','top':$(this).next().offset().top-$(this).children("div[id^='li_']").outerHeight()});
			
							}
						}else{
							$(this).children("div[id^='li_']").removeClass("navTitle").css({'position':'static'});
							$(this).children("div[id^='content_']").css({'padding-top':'10px'});
						}

						//console.log("this-top:"+$(this).next().offset().top+",scrolltop:"+subNavTop+",childheight:"+$(this).children("div[id^='li_']").outerHeight());
        	
        			});
				});	
			});
			
			$(this).find(".navContent").each(function(){
				if($(this).text()==""){
					$(this).parent("li").hide();
				}
			})
			//初始化
			defineCollapse(fieldId,tabIdList);

			$(".subNav").click(function(){
				$(this).parent().toggleClass("icon-border-hide");
				$(this).find('span').toggleClass("icon-right").siblings(".icon").removeClass("icon-right");
				$(this).find('span').toggleClass("icon-down up-color").siblings(".icon").removeClass("icon-down up-color");
				$(this).parent().next(".navContent").slideToggle(500).siblings(".navContent").slideUp(500);
			});
		});			
	};
})(jQuery);

