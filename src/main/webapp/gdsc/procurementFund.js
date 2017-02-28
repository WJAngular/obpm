$(function(){
	$this.attrItem("申请金额大", "placeholder", "输入数字即可转换");
	$this.attrItem("批准金额大", "placeholder", "输入数字即可转换");
	//设置控件默认大小
	$this.inputWidthHeight("银行帐号", "80%", "", true, false);
	$this.inputWidthHeight("开户银行", "80%", "", true, false);
	//默认隐藏
	$this.defaultHide();
	
	if($("input[name='入库情况']:checked").val() == "已入库"){
		showItem("入库单号");
	}else{
		hideItem("入库单号");
	}
	if($("input[name='到货情况']:checked").val() == "已到"){
		showItem("到货比例");
	}else{
		hideItem("到货比例");
	}
	
	//隐藏字段的值
	$this.defaultHideValue();
	
	$("input[name='入库情况']").click(function(){
		var ware = $("input[name='入库情况']:checked").val();
		if(ware == "已入库"){
			attrItem("入库单号","placeholder","入库单号");
			showItem("入库单号");
		}else{
			hideItem("入库单号");
		}
	});
	$("input[name='到货情况']").click(function(){
		var ware = $("input[name='到货情况']:checked").val();
		if(ware == "已到"){
			attrItem("到货比例","placeholder","到货比例");
			showItem("到货比例");
		}else{
			hideItem("到货比例");
		}
	});
	
	//设置金额大写
	changeMoneyBig("申请金额大");
	changeMoneyBig("批准金额大");
	//设置金额小写时，金额大写自动设置值
	changeMoneyBigForSmall("申请金额小","申请金额大");
	changeMoneyBigForSmall("批准金额小","批准金额大");
	
	//单个导出Excel模版
	$("a[title='导出Excel']").unbind('click').bind('click',function(){
		var primaryTable = "TLK_临时采购货款";
		var docNo = $("input[name='编号']").val();
		window.location.href = "/obpm/fund/fundExcelTem.action?docNo="+encodeURI(encodeURI(docNo))+"&primaryTable="+encodeURI(encodeURI(primaryTable));
	});
	//单个打印模版
	$("a[title='打印']").unbind('click').bind('click',function(){
		var docNo = $("input[name='编号']").val();
		if(docNo == ""){
			alert("请先保存该文档，再进行打印!")
		}else{
			$("a[title='打印']").attr("target","_blank");
			window.open("http://192.168.1.30:8080/obpm/ReportServer?reportlet=cghkPrint.cpt&number="+docNo);
		}
	});
});

var $this = {
	'defaultHide':function(){
		hideItem("入库单号");
		hideItem("到货比例");
	},
	'defaultHideValue':function(){
		attrItem("费用类型","value","采购费");
		attrItem("采购货款申请","value","采购货款申请");
	},
	'inputWidthHeight':function(str,width,height,isWidth,isHeight){
		inputWidthHeight(str,width,height,isWidth,isHeight);
	},
	'attrItem':function(str,attr,val){
		attrItem(str,attr,val);
	}
};
