$(function(){
	$this.attrItem("系统招待费申请单号", "placeholder", "申请单号");
	$("input[name='支付类型']").click(function(){
		var radioVal = $("input[name='支付类型']:checked").val();
		if(radioVal == "业务招待费"){
			$(".account").show();
		}else{
			$(".account").hide();
		}
	});
	
	//单个打印模版
	$("a[title='打印']").unbind('click').bind('click',function(){
		var docNo = $("input[name='报销编号']").val();
		if(docNo == ""){
			alert("请先保存该文档，再进行打印!")
		}else{
			$("a[title='打印']").attr("target","_blank");
			window.open("http://192.168.1.30:8080/obpm/ReportServer?reportlet=scBaoXiao.cpt&number="+docNo);
		}
	});
});
var $this = {
	'attrItem':function(str,attr,val){
		attrItem(str,attr,val);
	}
}