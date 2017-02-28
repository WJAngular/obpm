$(function(){
	//	//将默认的导出Excel中的onclick属性去掉，另新建一个onclick属性
	$("input[title='导入考勤数据']").attr("onclick","importOnclick();");
//	本地电脑/obpm/portal/share/dynaform/dts/excelimport/importbyid.jsp?parentid=&id=11e6-61b1-a03f2fb1-9f50-43e7915f8047&application=11e6-3d0f-5d78a4dd-b6a6-2fcfcefd00c4&isRelate=&_activityid=11e6-61b1-b8f4b7ff-9f50-43e7915f8047&_viewid=11e6-6126-f342a04d-9305-c9c685791f52&_=1471133367563
//	服务器/obpm/portal/share/dynaform/dts/excelimport/importbyid.jsp?parentid=&id=11e6-6287-674523ac-946d-21a59102eafe&application=11e6-3d0f-5d78a4dd-b6a6-2fcfcefd00c4&isRelate=&_activityid=11e6-628a-96100556-b937-e7ed460763e0&_viewid=11e6-6126-f342a04d-9305-c9c685791f52&_=1471225815059
	//单个打印模版
	$("input[title='导出微信考勤数据']").attr("onclick","importWechat();");
});
var importWechat = function(){
	$("input[title='导出微信考勤数据']").attr("target","_blank");
	window.open("http://192.168.1.30:8080/obpm/ReportServer?reportlet=weChatReport.cpt");
}
var importOnclick = function(){
	var url = "/obpm/gdsc/importExcel.jsp?parentid=&id=11e6-6287-674523ac-946d-21a59102eafe&application=11e6-3d0f-5d78a4dd-b6a6-2fcfcefd00c4&isRelate=&_activityid=11e6-628a-96100556-b937-e7ed460763e0&_viewid=11e6-6126-f342a04d-9305-c9c685791f52&_=1471225815059";
	OBPM.dialog.show( {
		opener : window.parent.parent,
		width : 700,
		height : 450,
		url : url,
		args : {},
		title : '文件导入',
		close : function(result) {
			parent.location.reload();
		}
	});
}