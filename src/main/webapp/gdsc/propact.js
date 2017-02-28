//$(function(){
//	$this.defaultTime();
//	//将默认的导出Excel中的onclick属性去掉，另新建一个onclick属性
//	$("input[title='导出Excel']").attr("onclick","$this.importExcel();");
//});
//
//var $this = {
//	'importExcel':function(){
//		var proName = $("input[name='_事项名称']").val();
//		var proNo = $("input[name='_项目编号']").val();
//		var status = $("input[name='状态']:selected").val();
//		var startTime = $("input[name='_开始日期']").val();
//		var endTime = $("input[name='_结束日期']").val();
//		var primaryTable = "TLK_项目合同";
//		var childTable = "TLK_合同费用子表";
//		var excelName = "项目合同";
//		if(status == undefined){
//			status = "未选择流程状态";
//		}
////		if(startTime == "" || endTime == ""){
////			layer.msg('请在查询字段中选择开始日期与结束日期!');
////			return;
////		}
////		if(proName == ""){
////			layer.msg('请在查询字段中选择相应的项目名称!');
////			return ;
////		}else{
//			window.location.href = "/obpm/bill/billTemplate.action?proName="+encodeURI(encodeURI(proName))+"&proNo="+encodeURI(encodeURI(proNo))+"&primaryTable="+encodeURI(encodeURI(primaryTable))+"&childTable="+encodeURI(encodeURI(childTable))+"&excelName="+encodeURI(encodeURI(excelName))+"&status="+encodeURI(encodeURI(status))+"&startTime="+startTime+"&endTime="+endTime;
////		}
//	},
//	'defaultTime':function(){
//		var startTime = $("input[name='_开始日期']").val();
//		var endTime = $("input[name='_结束日期']").val();
//		if(startTime == "" || startTime == null || startTime == undefined){
//			$("input[name='_开始日期']").val(currDate());
//		}
//		if(endTime == "" || endTime == null || endTime == undefined){
//			$("input[name='_结束日期']").val(getCurrAddDate(-7));
//		}
//		
//		
//	}
//}