<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
  
<script type="text/javascript">
  function init() {
  		setPie2D();
  		setPie3D();
  }
  
  function setPie2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Pie2D.swf?ChartNoDataText=暂无数据", "haha", "100%", "100%", "0", "1");
		xmlData = "<chart caption='负荷预测值' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'";
		
		xmlData += "useRoundEdges='1' bgColor='FFFFFF,FFFFFF' showBorder='0'  ";
		xmlData += "exportEnabled='1' exportAtClient='0' exportAction='save' ";
		xmlData += "exportDialogMessage='正在生成,请稍候...' exportFormats='JPG=生成JPG图片|PDF=生成PDF文件'";
		xmlData += "exportHandler='${ctx}/fusioncharts/jsp/FCExporter.jsp' exportFileName='bartest--yyyy-MM-dd HH-mm-ss'> ";
		
		xmlData += "<set label='1月' value='25'/>  ";
		xmlData += "<set label='2月' value='50'/>  ";
		xmlData += "<set label='3月' value='75'/>  ";
		xmlData += "<set label='4月' value='100'/> ";
		xmlData += "</chart> ";
		
		line1.setDataXML(xmlData);
		line1.render("Pie2D");
  }
  
  function setPie3D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Pie3D.swf?ChartNoDataText=暂无数据", "haha", "100%", "100%", "0", "3");
		xmlData = "<chart caption='负荷预测值' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'";
		
		xmlData += "useRoundEdges='1' bgColor='FFFFFF,FFFFFF' showBorder='0'  ";
		xmlData += "exportEnabled='1' exportAtClient='0' exportAction='save' ";
		xmlData += "exportDialogMessage='正在生成,请稍候...' exportFormats='JPG=生成JPG图片|PDF=生成PDF文件'";
		xmlData += "exportHandler='${ctx}/fusioncharts/jsp/FCExporter.jsp' exportFileName='bartest--yyyy-MM-dd HH-mm-ss'> ";
		
		xmlData += "<set label='1月' value='25'/>  ";
		xmlData += "<set label='2月' value='50'/>  ";
		xmlData += "<set label='3月' value='75'/>  ";
		xmlData += "<set label='4月' value='100'/> ";
		xmlData += "</chart> ";
		line1.setDataXML(xmlData);
		line1.render("Pie3D");
  }
  
  //导出图片调用的方法
	function startExport(){
		var chart = getChartFromId("haha");
		alert(chart[0].length);
			if(chart.hasRendered()){
				chart.exportChart();
			}else{
				alert("Please wait for the chart to finish rendering, before you can invoke exporting.");
			}
	}
</script>
</head>  
  
<body onload="init();">
<table>
	<tr>
   		<input type='button' value='导出FusionCharts图片' onClick="javascript:startExport();" />
	</tr>
	<tr>
		<td><div id="Pie2D" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
	<tr>
		<td><div id="Pie3D" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
</table>
</body>
</html>
