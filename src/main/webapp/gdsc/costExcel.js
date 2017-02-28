$(function(){
	$this.hideItem("当前文档ID");
	//绑定点击事件
	$("a[title='导出excel']").unbind('click').bind('click',function(e){
		$this.excelAction();
	});
});
var $this = {
		'hideItem':function(str){
			hideItem(str);
		},
		'excelAction':function(){
			var id = $("input[name='当前文档ID']").val();
			var primaryTable = "TLK_项目公关费用";
			var childTable = "TLK_业务费用子表";
			var no = $("input[name='编号']").val();
//			alert("id"+id+";"+"no"+no);
			window.location.href = "/obpm/bill/billExcel.action?id="+id+"&primaryTable="+encodeURI(encodeURI(primaryTable))+"&childTable="+encodeURI(encodeURI(childTable))+"&No="+no;
		}
}