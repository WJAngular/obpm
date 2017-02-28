$(function(){
	$("#客户名称_show1").hide();
	$("input[name='show_客户名称']").focus(function(){
		$this.clientNameFun();
	});
	$("input[name='show_客户名称']").blur(function(){
		$this.clientNameFun();
	});
	$("input[title='新增']").click(function(){
		$this.addClientFun();
	});
	$("input[title='选择模版']").click(function(){
		$this.proTemplate();
	});
	$("input[name='当前文档ID']").hide();
	//设置操作时间
//	time();
	//设置控件的默认大小 
	$this.inputWidthHeight();
	//设置客户新增提示颜色
	$("input[title='新增']").mouseover(function(){
		layer.tips('如系统中不存在客户信息，请点击新增', "input[title='新增']");
	});
});
var $this = {
	'clientNameFun' : function(){
		var clientName_ = $("input[name='show_客户名称']").val();
		if(clientName_ != null && clientName_.indexOf(":") > -1){
			$("input[name='show_客户名称']").val(clientName_.split(":")[0]);
			$("input[name='客户名称']").val(clientName_.split(":")[0]);
			$("input[name='联系人']").val(clientName_.split(":")[2]);
			$("input[name='联系电话']").val(clientName_.split(":")[3]);
		}
	},
	'addClientFun' : function(){
		layer.open({
			  type: 2,
			  title:'新增用户信息',
			  skin: 'layui-layer-rim', //加上边框
			  shadeClose: true,
		      maxmin: true, //开启最大化最小化按钮
		      area: ['1050px', '500px'], //宽高
			  content: '/obpm/client/jumpClient.action'
			});
	},
	'proTemplate':function(){
		layer.open({
			  type: 2,
			  title:'项目阶段模版',
			  skin: 'layui-layer-rim', //加上边框
			  shadeClose: true,
		      maxmin: true, //开启最大化最小化按钮
		      area: ['830px', '500px'], //宽高
			  content: '/obpm/stage/prostageJsp.action'
			});
	},
	'inputWidthHeight':function(){
		$("input[name='项目阶段模版']").attr("style","width:80%");
		$("input[name='事项名称']").attr("style","width:90%");
		$("input[name='项目地点']").attr("style","width:90%");
		$("input[name='项目阶段模版']").attr("readonly","readonly");
	}
};
//新增客户信息时，将相应的值带过来本页面
var setDefaultValue = function(name,person,telephone){
	$("input[name='show_客户名称']").val(name);
	$("input[name='客户名称']").val(name);
	$("input[name='客户名称新']").val(name);
	$("input[name='联系人']").val(person);
	$("input[name='联系电话']").val(telephone);
};
//为子页面获取父页面的项目类型的值
var getProType = function(){
	var rtn = $("select[name='项目类型']").val();
	return rtn;
};
/** 获取系统当前时间  例如：2016-05-16 17:48:34*/
function currDateTime() {
    var d = new Date();
    var result = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    var hour = d.getHours();
    var minute = d.getMinutes();
    var second = d.getSeconds();
    if (month > 9) {
    	result = result + "-" + month ;
    } else {
    	result = result + "-0" + month ;
    }
    if (date > 9) {
    	result = result + "-" + date;
    } else {
    	result = result + "-0" + date;
    }
    if (hour > 9) {
    	result = result + " " + hour;
    } else {
    	result = result + " 0" + hour;
    }
    if (minute > 9) {
    	result = result + ":" + minute;
    } else {
    	result = result + ":0" + minute;
    }
    if (second > 9) {
    	result = result + ":" + second;
    } else {
    	result = result + ":0" + second;
    }
    $("input[name='操作时间']").val(result);
    return result;
}
var time = function(){
	if($("input[name='操作时间']").val() == ""){
		//然后定时 1秒刷新时间
		setInterval(function(){currDateTime();},1000);
    }
}