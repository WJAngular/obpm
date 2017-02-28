(function($){
	$.fn.obpmDateField = function(){
		return this.each(function(){
			var $field = jQuery(this);
			var id=$field.attr("_id");
			var name=$field.attr("_name");
			var fieldType=$field.attr("_fieldType");
			var classname=""; //$field.attr("classname");
			var cssclass=$field.attr("_class");
			var limit=$field.attr("limit");
			var value=$field.attr("value");
			var displayType=$field.attr("_displayType");
			var textType=$field.attr("_textType");
			var dateFmt=$field.attr("_dateFmt");
			var getPrevName=$field.attr("_getPrevName");
			var getPrevNameMinDate=$field.attr("_getPrevNameMinDate");
			var skin=$field.attr("_skin");
			var minDateBoot=$field.attr("_minDate");
			var maxDateBoot=$field.attr("_maxDate");
			var noPatternHms=$field.attr("_noPatternHms");
			var isRefreshOnChanged=$field.attr("_isRefreshOnChanged");
			var subGridView = $field.attr("_subGridView");
			var hiddenValue = $field.attr("_hiddenValue");
			var isCommonFilter = $field.attr("_isCommonFilter");
			isCommonFilter = (isCommonFilter == "true");
			//var style = $field.attr("style");
			var style;
			var _style;

			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			
			value = value ? value : "";
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			getPrevName = (getPrevName == "true");
			noPatternHms = (noPatternHms == "true");
			subGridView = (subGridView == "true");
			
			//调整时间格式
			if(dateFmt == "yyyy" && dateFmt.length == 4){
				_dateFmt = "YYYY";
				_style = "width: 150px;";
			}
			if(dateFmt == "yyyy-MM" && dateFmt.length == 7){
				_dateFmt = "YYYY-MM";
				_style = "width: 150px;";
			}
			if(dateFmt == "yyyy-MM-dd" && dateFmt.length == 10){
				_dateFmt = "YYYY-MM-DD";
				_style = "width: 150px;";
			}
			if(dateFmt == "yyyy-MM-dd HH:mm" && dateFmt.length == 16){
				_dateFmt = "YYYY-MM-DD HH:mm";
				_style = "width: 210px";
			}
			if(dateFmt == "yyyy-MM-dd HH:mm:ss" && dateFmt.length == 19){
				_dateFmt = "YYYY-MM-DD HH:mm:ss";
				_style = "width: 210px;";
			}
			if(dateFmt == "HH:mm:ss" && dateFmt.length == 8){
				_dateFmt = "HH:mm:ss";
				_style = "width: 150px;";
			}

			var html="";
			if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
				
				function Wdate($dataField){
					var dateFmt = $dataField.find("input").attr("dateFmt");
					//调整时间格式
					if(dateFmt == "yyyy" && dateFmt.length == 4){
						_dateFmt = "YYYY";
					}
					if(dateFmt == "yyyy-MM" && dateFmt.length == 7){
						_dateFmt = "YYYY-MM";
					}
					if(dateFmt == "yyyy-MM-dd" && dateFmt.length == 10){
						_dateFmt = "YYYY-MM-DD";
					}
					if(dateFmt == "yyyy-MM-dd HH:mm" && dateFmt.length == 16){
						_dateFmt = "YYYY-MM-DD HH:mm";
					}
					if(dateFmt == "yyyy-MM-dd HH:mm:ss" && dateFmt.length == 19){
						_dateFmt = "YYYY-MM-DD HH:mm:ss";
					}
					if(dateFmt == "HH:mm:ss" && dateFmt.length == 8){
						_dateFmt = "HH:mm:ss";
					}

					//计算最小值
					var $minDateObj = $("#"+getPrevNameMinDate);
					var _minDate = $minDateObj.val();
					var minDateFmt = $minDateObj.attr("dateFmt");

					if(_minDate && _minDate != ""){
						minDateBoot = _minDate;
					}
					if(_minDate == "" || _minDate == undefined) minDateBoot="1900-01-01 00:00:00";
					
					//传入的最小值统一格式
					if(minDateFmt == "yyyy" && trim(minDateBoot).length == 4){
						minDateBoot = trim(minDateBoot)+"-01-01 00:00:00";
					}
					if(minDateFmt == "yyyy-MM" && trim(minDateBoot).length == 7){
						minDateBoot = trim(minDateBoot)+"-01 00:00:00";
					}
					if(minDateFmt == "yyyy-MM-dd" && trim(minDateBoot).length == 10){
						minDateBoot = trim(minDateBoot)+" 00:00:00";
					}
					if(minDateFmt == "yyyy-MM-dd HH:mm" && trim(minDateBoot).length == 16){
						minDateBoot = trim(minDateBoot)+":00";
					}
					if(minDateFmt == "HH:mm:ss" && trim(minDateBoot).length == 8){
						minDateBoot = "1900-01-01 "+minDateBoot;
					}
					
					//计算最大值
					var $subInput = jQuery("input[min_name='" + id + "']");
					var maxDateFmt = $subInput.attr("dateFmt");
					if($subInput.size() > 0){
						var _maxDate = "";
						var _dateName = "";
						$subInput.each(function(){
							var curVal = jQuery(this).val();
							var curName = jQuery(this).attr("name");
							if(curVal != ""){
								var cur_Date = new Date(curVal.replace(/-/g,"\/"));
								var max_Date = new Date(_maxDate.replace(/-/g,"\/"));
								_maxDate = (cur_Date > max_Date)?_maxDate:curVal;
								_dateName = (cur_Date > max_Date)?_dateName:curName;
							}
						});
						maxDateBoot = _maxDate;
						dateName = _dateName;
					}
					
					var now_min_Date = new Date(minDateBoot.replace(/-/g,"\/"));
					var now_max_Date = new Date(maxDateBoot.replace(/-/g,"\/"));
					if(now_min_Date > now_max_Date){
						var starDate = jQuery("#"+getPrevNameMinDate).attr("name");
						alert(starDate +" 不能大于 " + dateName);
						return false;
					}
					if(maxDateBoot == ""){maxDateBoot = "2099-12-31 23:59:59";}
					
					//传入的最大值统一格式
					if(maxDateFmt == "yyyy" && trim(maxDateBoot).length == 4){
						maxDateBoot = trim(maxDateBoot)+"-01-01 00:00:00";
					}
					if(maxDateFmt == "yyyy-MM" && trim(maxDateBoot).length == 7){
						maxDateBoot = trim(maxDateBoot)+"-01 00:00:00";
					}
					if(maxDateFmt == "yyyy-MM-dd" && trim(maxDateBoot).length == 10){
						maxDateBoot = trim(maxDateBoot)+" 00:00:00";
					}
					if(maxDateFmt == "yyyy-MM-dd HH:mm" && trim(maxDateBoot).length == 16){
						maxDateBoot = trim(maxDateBoot)+":00";
					}
					if(maxDateFmt == "HH:mm:ss" && trim(maxDateBoot).length == 8){
						maxDateBoot = "0000-00-00 "+maxDateBoot;
					}
					if($dataField.data("DateTimePicker")){
						$dataField.data("DateTimePicker").destroy();
					}
					
					//解决网格视图保存后数据被overflow:hidden的问题 by tank
					$dataField.parent(".grid-column-edit").parent().css("overflow","");
					
					$dataField.datetimepicker({
						format:_dateFmt,
						minDate:noPatternHms?minDateBoot:'1900-01-01 00:00:00',
						maxDate:noPatternHms?maxDateBoot:'2099-12-31 23:59:59',
			            locale:'zh-cn',
			            showClose:true,
			            icons:{
			                previous: '',
			                next: '', 
			                up: 'timepicker-up',
			                down: 'timepicker-down',
			                time: 'selectTime',
			                close: 'btn-close'
			            },
			            tooltips: {
			                close: '确认'
			            }
			        }).on("dp.show", function (e) {
			        	var _time = $(e.target).find("input").val();
			        	var _minName = $(e.target).find("input").attr("min_name");
			        	var _showFmt = $(e.target).find("input").attr("datefmt");
			        	if(_time == ""){
			        		if(_minName == undefined){
			        			var _id = $(e.target).find("input").attr("id");
			        			var $_minInput = $("input[min_name='"+_id+"']");
			        			if($_minInput.size() > 0){
			        				_time = $_minInput.val();
			        			}
			        		}else{
			        			_time = $("input#"+_minName).val();
			        		}
			        	}
						if(_showFmt == "yyyy-MM-dd HH:mm" && _showFmt.length == 16){
							$(e.target).find(".bootstrap-datetimepicker-widget").find("li.picker-switch").find("span.selectTime").html("&nbsp;"+_time.substring(11,16));
						}
						if(_showFmt == "yyyy-MM-dd HH:mm:ss" && _showFmt.length == 19){
							$(e.target).find(".bootstrap-datetimepicker-widget").find("li.picker-switch").find("span.selectTime").html("&nbsp;"+_time.substring(11,19));
						}
						if(_showFmt == "HH:mm:ss" && _showFmt.length == 8){
							$(e.target).find(".bootstrap-datetimepicker-widget").find("li.picker-switch").find("span.selectTime").html("&nbsp;"+_time);
						}
			        }).on("dp.hide", function (e) {
			        	if (isRefreshOnChanged) {
			        		if(subGridView){
			        			var _id = $(this).find("input").attr("id");
			        			dy_view_refresh(_id);
			        		}else{
			        			dy_refresh(this.id);
			        		}
						}
			        });
					
					$dataField.data("DateTimePicker").show();
				};
				
				html+="<div class='input-group date' "
				
				if(style){
					html+=" style='vertical-align: middle;display: inline-table;" + style + "";
				}else{
					html+=" style='vertical-align: middle;display: inline-table;" + _style + "";
				}
				
				if(textType.toLowerCase()=="hidden" || displayType == PermissionType_READONLY || textType.toLowerCase()=="readonly" 
					|| displayType == PermissionType_DISABLED){
					html+=";display:none;";
				}
				
				html+=" '>"

				if(textType.toLowerCase()=="hidden" ){
					html+="<input type='hidden'";
				}else{
					html+="<input type='text'";
				}

				html+=" id='" + id + "' value='"+value+"' isCommonFilter='"+isCommonFilter+"' limit='"+limit+"' name='"+name+"' classname='"+classname+"' fieldType='"+fieldType+"' class='form-control "+cssclass+"'";
				html+=" isRefreshOnChanged='" + isRefreshOnChanged + "' dateFmt='" + dateFmt + "' " + otherAttrsHtml;
				
				if(getPrevNameMinDate) html += " min_name=\"" + getPrevNameMinDate +"\"";
				html+=" /><span class='input-group-addon'><span class='glyphicon glyphicon-calendar'></span></span></div>";
				var $html = $(html);
				if(!subGridView){
					$html.bind("blur",function(){
						if(typeof(dateCompareVal)=="function") dateCompareVal(this);	//普通表单时设置表单是否更改
					});
				}
				$html.bind("click",function(){
					Wdate(jQuery(this));
				}).replaceAll($field);
				
				if (displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly"
					|| displayType == PermissionType_DISABLED){
					$html.after("<span id='" + name + "_show' >" + value + "</span>");
				}
			}else{
				html += "<input type='hidden' name=\""+name+"\" value=\""+value+"\" /><span>"+ hiddenValue +"</span>" ;
				$(html).replaceAll($field);
			}
		});
	};
})(jQuery);