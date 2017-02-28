var PermissionType_READONLY = 1;
var PermissionType_MODIFY = 2;
var PermissionType_HIDDEN = 3;
var PermissionType_DISABLED = 4;
var PermissionType_PRINT = 5;
/**
 * 表单控件重构时调用
 * 通过id获取非后台显式添加（后台开发时在表单源码添加）的其他属性
 * objId:moduleOtherAttrs属性值
 * return:所有元素属性组成的html
 **/
function getOtherProps(objId) {
	var el = jQuery("[moduleOtherAttrs='"+objId+"']")[0], atts = el.attributes, att, html="";
	for (var i=0; i < atts.length; i++) {
		att = atts[i];
		if (att.specified 
				&& att.name.toLowerCase() != "moduleotherattrs" 
				&& att.name != "type" && att.value != "" 
				&& att.name.toLowerCase().indexOf("jquery") == -1) {
			html += " " +att.name+ "='" +att.value+ "'";
		}
	}
	jQuery(el).remove("input");
	return html;
}

/**
 * 表单控件重构时调用
 * 通过对象获取非后台显式添加（后台开发时在表单源码添加）的其他属性
 * obj:获取属性的对象
 * @return:所有元素属性组成的html
 */
function getOtherAttrs(obj) {
	var atts = obj.attributes, att, html="";
	for (var i=0; i < atts.length; i++) {
		att = atts[i];
		if (att.specified 
				&& att.name.toLowerCase() != "moduletype" 
				&& att.name.toLowerCase() != "classname" 
				&& att.name.toLowerCase() != "discript" 
				&& att.name.toLowerCase() != "style" 
				&& att.name.toLowerCase() != "class" 
				&& att.name.toLowerCase() != "value"
				&& att.name != "type" && att.value != "" 
				&& att.name.substr(0,1) != "_"
				&& att.name.toLowerCase().indexOf("jquery") == -1) {
			html += " " +att.name+ "='" +att.value+ "'";
		}
	}
	return html;
}

//当表单配置有待办控件时，把待办load进页面
function loadPedding(){
	jQuery(".ElementDiv").each(
			function(index){
				jQuery(this).load(jQuery(this).attr("src"),{'index':index},function(){
					jQuery("[moduleType='pending']").obpmPending();  //待办元素
				});
			}
		);
}


//渲染按钮栏常用搜索
function renderSearchActivePanel(){
	if($(".search-active-panel").size() > 0){
		
		if($("#activityTable").find("[onclick='modifyActionBack();']").size() <= 0){
			//按钮栏搜索按钮
			var _btnHtml = "";
			if($("[isCommonFilter='true']").size() > 0){
				_btnHtml = '<button type="button" class="btn btn-info " onclick="modifyActionBack();">'
						+ '<i class="fa fa-search"></i></button>'
						+ '<button type="button" class="btn btn-link search-senior" onclick="isSearch();">高级</button>';
				
				$("#searchFormTable").find("[isCommonFilter='true']").each(function(){
					var $this = $(this);
					var $span = $this.parents("span[id$='_divid']");
					var id = $this.attr("id");
					var name = $this.attr("name");
					if($this.attr("type") == "text"){
						$span.find("[isCommonFilter='true']").attr("placeholder",name);
					}
					$span.before("<span _isCommonFilterID='"+id+"' style='display:none'></span>");
					$span.addClass("form-group");
					$span.prepend('<span data-title style="vertical-align: middle;">'+ name +': </span>')
					$("#activityTable").find(".search-btns").before($span);
				}); 
			}else{
				_btnHtml = '<button type="button" class="btn btn-info " onclick="isSearch();">'
						+ '<i class="fa fa-search"></i>'
						+ '</button>';
			}
			$("#activityTable").find(".search-btns").html(_btnHtml);
		}
		
	}
}

//--表单控件jquery重构--start--//
/**
 * jquery重构按钮和控件
 * for:表单
 */
function jqRefactor(){

	try{
		var $actButtion = jQuery("input[moduleType='activityButton']");
		if($actButtion.size() != 0)
			$actButtion.obpmButton(); //操作按钮
		
		var $activityTable = $('#activityTable');
		if($activityTable.find('a.btn').length > 0 
				|| $activityTable.find('button').length>0
				|| $actButtion.size()>0
				|| $("#current-processing-node-td").size()>0
				|| $('#activityTable').find('.searchBtn').length > 0){
			if($activityTable.find('a.btn').length == 0 && $activityTable.find('button').length>0){
				$activityTable.find(".searchDiv").eq(0).css("height","55px");
			}
			$('#mt').css('marginTop',$('#activityTable_box').outerHeight());
		}else{
			$activityTable.css('display','none');
			$('#mt').css('display','none');
		}

		//必须放在选项卡重构前
		jQuery("input[moduleType='IncludedView']").filter(function(){
			return (jQuery(this).parents("div[moduleType='TabNormal']").size() == 0);
		}).obpmIncludedView();  		//包含元素
		jQuery("div[moduleType='TabNormal']").filter(function(){
			return (jQuery(this).parents("div[moduleType='TabNormal']").size() == 0);
		}).obpmTabNormalField();  		//页签选项卡
		
		jQuery("ul[moduleType='TabCollapse']").filter(function(){
			return (jQuery(this).parents("ul[moduleType='TabCollapse']").size() == 0);
		}).obpmTabCollapseField();  		//页签选项卡
		
		//以下控件必须在选项卡后重构
		jQuery("input[moduleType='formActivityButton']").obpmButtonField();		//按钮控件
		jQuery("span[moduleType='select']").obpmSelectField();		//下拉选择框
		jQuery("input[moduleType='input']").obpmInputField();		//单行文本框
		jQuery("textarea[moduleType='textarea']").obpmTextareaField();	//多行文本框
		jQuery("span[moduleType='radio']").obpmRadioField();		//单选框
		jQuery("span[moduleType='radiogrid']").obpmRadioGridField();//单选框网格视图
		jQuery("span[moduleType='checkbox']").obpmCheckbox();		//复选框
		jQuery("span[moduleType='gridcheckbox']").obpmGridCheckbox();//复选框网格视图
		jQuery("select[moduleType='department']").obpmDepartmentField();			//部门选择框
		jQuery("input[moduleType='viewDialog']").obpmViewDialog();				//视图选择框
		jQuery("input[moduleType='treeDepartment']").obpmTreeDepartmentField();	//树形部门选择框
		jQuery("input[moduleType='takePhoto']").obpmTakePhoto();					//在线拍照
		jQuery("select[moduleType='selectAbout']").obpmSelectAboutField();  		//左右选择框
		jQuery("input[moduleType='dateinput']").obpmDateField();		//日期控件
		jQuery("input[moduleType='userfield']").obpmUserfield();  				//用户选择框
		jQuery("input[moduleType='suggest']").obpmSuggestField();  				//下拉提示框
		jQuery("span[moduleType='uploadFile']").obpmUploadField();  			//文件（图片）上传功能
		jQuery("textarea[moduleType='htmlEditor']").obpmHtmlEditorField();  	//HTML编辑器
		jQuery("input[moduleType='fileManager']").obpmFileManager();  		//文件管理功能
		jQuery("input[moduleType='mapField']").obpmMapField();  				//地图功能
		jQuery("input[moduleType='wordField']").obpmWordField();  			//Word编辑器
		jQuery("div[moduleType='flowHistoryField']").obpmFlowHistoryField();  			//流程历史控件
		jQuery("input[moduleType='weixingpsfield']").obpmWeixinGpsField();  			//微信gps控件
		jQuery("input[moduleType='surveyField']").obpmSurveyField();//问卷调查控件
		jQuery("input[moduleType='qrcodefield']").obpmQRCodeField();//二维码控件
		jQuery("input[moduleType='weixinrecordfield']").obpmWeixinRecord();//录音
		
		loadPedding();	//待办
		renderSearchActivePanel();	//常用搜索
		
		
	}catch(ex){
		console.error("==========表单重构ex:"+ex);
	}
}
//--表单控件jquery重构--end--//



//--表单刷新重计算--start--//
var isRefreshLoading = false;	//是否正在执行刷新重计算，表单保存时使用
var dy_token = true;

//loading show
function dy_lock() {
	jQuery("#loadingDivBack").fadeTo(300,0.4);
}

//loading hide
function dy_unlock() {
	jQuery("#loadingDivBack").fadeOut(200);
}

function dy_getFormid() {
	return formid;
}

function dy_getValuesMap(withParentid) {
	jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
	var valuesMap = {};
	var mapVal = "";
	var mapVals = new Array();
	if(jQuery("#_formHtml > #dy_refreshObj").size() > 0){
		mapVal = jQuery("#_formHtml > #dy_refreshObj").attr("mapVal");
	}
	if(mapVal)
		mapVals = mapVal.split(";");
	if (document.getElementsByName('_selects')) {
		var selects = getCheckedListStr('_selects');
		valuesMap['_selectsText'] = (selects && selects != null) ? selects : '';
	}
	for(var i=0; i<mapVals.length; i++){
		valuesMap[mapVals[i]] = ev_getValue(mapVals[i]);
	}
	
	if ($("input[name='parentid']").length > 0
			&& withParentid) {
		valuesMap['parentid'] = $("input[name='parentid']").val();
	}
	if ($("input[name='application']").length > 0) {
		valuesMap['application'] = $("input[name='application']").val();
	}
	return valuesMap;
}

function dy_refresh(actfield) {
	var $dy_refreshObj = jQuery("#_formHtml > #dy_refreshObj");
	var formid = $dy_refreshObj.attr("formid");
	var docid = $dy_refreshObj.attr("docid");
	var userid = $dy_refreshObj.attr("userid");
	var flowid = $dy_refreshObj.attr("flowid");
	
	try {
		if (dy_token) {
			if(typeof FormHelper !="undefined"){
				dy_token = false;
				dy_lock();
				isRefreshLoading = true;
				if (document.getElementById('tabid')) {
					var _tabid = document.getElementById('tabid').value;
				}
				FormHelper.refresh(_tabid,
						formid, actfield,
						docid,
						userid,
						dy_getValuesMap(true), flowid, function(str) {

							try {
								eval(str);
								jQuery("#flowprocessDiv").hide();//隐藏流程面板，使得下次点击展开时能够进行重新加载。
								jqRefactor();
								if($(".refreshItem").find("iframe").size()>0){
									$(".refreshItem").find("iframe").each(function(){
										$(this).height($(this).parent("span").height());
									})
								}
								$(".refreshItem").removeAttr("style").removeClass("refreshItem");
							} catch (ex) {
								alert('form: ' + ex.message);
							}
							;
							dy_unlock();
							isRefreshLoading = false;
							dy_token = true;
						});
			}
		}
	} catch (ex) {
		dy_unlock();
		isRefreshLoading = false;
		alert('form: ' + ex.message);
	}
}
//--表单刷新重计算--end--//