$(function(){
	$this.attrItem("申请金额大", "placeholder", "输入数字即可转换");
	$this.attrItem("批准金额大", "placeholder", "输入数字即可转换");
	//默认隐藏字段的值
	$this.defaultHideValue();
	//设置金额大写
	$this.changeMoneyBig("申请金额大");
	$this.changeMoneyBig("批准金额大");
	//设置金额小写时，金额大写自动设置值
	$this.changeMoneyBigForSmall("申请金额小","申请金额大");
	$this.changeMoneyBigForSmall("批准金额小","批准金额大");
	
	//单个导出Excel模版
	$("a[title='导出Excel']").unbind('click').bind('click',function(){
		var primaryTable = "TLK_临时业务费";
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
			window.open("http://192.168.1.30:8080/obpm/ReportServer?reportlet=ywPrint.cpt&number="+docNo);
		}
	});
});

var $this = {
	'defaultHideValue':function(){
			attrItem("费用类型","value","业务费");
			attrItem("业务费申请","value","业务费申请");
		},
	'attrItem':function(str,attr,val){//设置某个字段的属性值
		attrItem(str,attr,val);
	},
	'showItem':function(str){//显示某个字段
		showItem(str);
	},
	'hideItem':function(str){//隐藏某个字段
		hideItem(str);
	},
	/**
	 * str : 需要失去焦点触发的字段name
	 * sum : 合同金额总数
	 * pay : 已支出金额数
	 * balance : 余额数
	 */
	'blurFun':function(str,sum,pay,balance){
		blurFun(str,sum,pay,balance);
	},
	/**
	 * sum : 合同金额总数
	 * pay : 已支出金额数
	 * balance : 余额数
	 */
	'moneySumAndPayAndBalance':function(sum,pay,balance){
		moneySumAndPayAndBalance(sum,pay,balance);
	},
	'bankInfo':function(str){
		//设置银行卡号至少10位帐号
		bankInfoMoreTen(str);
		//设置银行卡格式
		bankInfoStyle(str);
	},
	//金额格式设置，组建失去焦点时候触发
	'changeMoneyStyle':function(str){
		changeMoneyStyle(str);
	},
	//金额大写设置
	'changeMoneyBig':function(str){
		changeMoneyBig(str);
	},
	'changeMoneyBigForSmall':function(str1,str2){
		changeMoneyBigForSmall(str1,str2);
	}
	
}