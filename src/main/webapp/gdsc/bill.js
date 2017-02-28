$(function(){
		//单个导出Excel模版
	$("a[title='导出Excel']").unbind('click').bind('click',function(){
		var primaryTable = "TLK_项目结算表";
		var proNo = $("input[name='事项编号']").val();
		if(proNo == "" || proNo==null){
			alert("请选择相应的项目,并保存!");
		}else{
			window.location.href = "/obpm/bill/billExcel.action?proNo="+encodeURI(encodeURI(proNo))+"&primaryTable="+encodeURI(encodeURI(primaryTable));
		}
	});
});
