<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
	<script type="text/javascript">
		//导出图片后调用的回调函数
		function FC_Exported(objRtn){
			alert("");
			if(objRtn.statusCode == "1"){
				alert("The chart with DOM Id " + objRtn.DOMID + "was successfully save on server.The file can be accessed from " + objRtn.fileName);
			}else{
				alert("The chart with DOM Id " + objRtn.DOMID + "counid not be saved on server.There was an error.Description : " + objRtn.statusMessage);
			}
		}

		//导出图片调用的方法
		function startExport(){
			var chart = getChartFromId("myFusionExport");
			if(chart.hasRendered()){
				chart.exportChart();
			}else{
				alert("Please wait for the chart to finish rendering, before you can invoke exporting.");
			}
		}
	</script>
  <body>
    <input type='button' value='生成FusionCharts图表' onClick="showFusionCharts();" />   
  
    <input type='button' value='导出FusionCharts图片' onClick="javascript:startExport();" />
	
	<div id="myFusion"></div>
	
	<script type="text/javascript">
	function showFusionCharts(){
		var myChart = new FusionCharts("${ctx}/fusioncharts/charts/Column2D.swf", "myFusionExport", "500", "300", "0", "1");
		
		var charxml = "";
		charxml += "<chart yAxisName='Sales Figure' caption='Top 5 Sales Person' numberPrefix='$' ";
			charxml += "useRoundEdges='1' bgColor='FFFFFF,FFFFFF' showBorder='0'  ";
			charxml += "exportEnabled='1' exportAtClient='0' exportAction='save'  exportFormats='PNG=Export as High Quality Image' ";
			charxml += "exportHandler='${ctx}/fusioncharts/jsp/FCExporter.jsp' exportFileName='download'> ";
			charxml += "<set label='Alex' value='25000' />  ";
			charxml += "<set label='Mark' value='35000' />  ";
			charxml += "<set label='David' value='55000' />  ";
			charxml += "<set label='Graham' value='35300' />  ";
			charxml += "<set label='John' value='31300' /> ";
		charxml += "</chart> ";
		
		myChart.setDataXML(charxml);
		myChart.render("myFusion");
	}
	</script>
  </body>
</html>
