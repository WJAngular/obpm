<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
  
<script type="text/javascript">
  function init() {
  		setLine();
  		setArea2D();
  		setPie2D();
  		setPie3D();
  		setColumn2D();
  		setColumn3D();
  		setBar2D();
  }
  
  function setLine(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Line.swf?ChartNoDataText=暂无数据", "Line", "100%", "100%", "0", "1");
		xmlData = "<chart caption='负荷预测值' numberSuffix='' exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' showExportDataMenuItem='1'";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Line");
  }
  
  function setArea2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Area2D.swf?ChartNoDataText=暂无数据", "Area2D", "100%", "100%", "0", "1");
		xmlData = "<chart caption='负荷预测值' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Area2D");
  }
  
  function setPie2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Pie2D.swf?ChartNoDataText=暂无数据", "Pie2D", "100%", "100%", "0", "1");
		xmlData = "<chart caption='负荷预测值' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Pie2D");
  }
  
  function setPie3D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Pie3D.swf?ChartNoDataText=暂无数据", "Pie3D", "100%", "100%", "0", "3");
		xmlData = "<chart caption='负荷预测值' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Pie3D");
  }
  
  function setColumn2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Column2D.swf?ChartNoDataText=暂无数据", "Column2D", "100%", "100%", "0", "3");
		xmlData = "<chart caption='负荷预测值' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Column2D");
  }
  
  function setColumn3D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Column3D.swf?ChartNoDataText=暂无数据", "Column3D", "100%", "100%", "0", "3");
		xmlData = "<chart caption='负荷预测值' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Column3D");
  }
  
  function setBar2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Bar2D.swf?ChartNoDataText=暂无数据", "Bar2D", "100%", "100%", "0", "3");
		xmlData = "<chart caption='负荷预测值' numberSuffix='¥'";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Bar2D");
  }
</script>
</head>  
  
<body onload="init();">
<table>
	<tr>
		<td><div id="Line" style="width:300px;height:245px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		<td><div id="Area2D" style="width:300px;height:245px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		<td><div id="Pie2D" style="width:300px;height:245px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		<td><div id="Pie3D" style="width:300px;height:245px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		
	</tr>
	<tr>
		<td><div id="Column2D" style="width:300px;height:245px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		<td><div id="Column3D" style="width:300px;height:245px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		<td><div id="Bar2D" style="width:300px;height:245px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
</table>
</body>
</html>
