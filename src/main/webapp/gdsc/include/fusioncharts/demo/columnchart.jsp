<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
  
<script type="text/javascript">
  function init() {
  		setColumn2D();
  		setColumn3D();
  		
  		setStCol2D();
  		setStCol3D();
  }
  
  function setColumn2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Column2D.swf?ChartNoDataText=暂无数据", "Column2D", "100%", "100%", "0", "3");
		xmlData = "<chart caption='2D柱状图实例' xAxisName='Month' yAxisName='Units' showValues='0' formatNumberScale='0' useRoundEdges='1' baseFont='Arial' baseFontSize='12' baseFontColor='60634E' showAlternateHGridColor='1' anchorRadius='1' alternateHGridColor='CC3300' alternateHGridAlpha='5' divLineColor='CC3300' divLineAlpha='20' shadowAlpha='40' bgColor='e8f0f2,ffffff'  showNames='1'>";
		xmlData += "<set label='1月' value='25'/> ";
		xmlData += "<set label='2月' value='50'/> ";
		xmlData += "<set label='3月' value='75'/> ";
		xmlData += "<set label='4月' value='100'/> ";
		xmlData += "</chart>";
		line1.setDataXML(xmlData);
		line1.render("Column2D");
  }
  
  function setColumn3D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Column3D.swf?ChartNoDataText=暂无数据", "Column3D", "100%", "100%", "0", "3");
		xmlData = "<chart caption='3D柱状图实例' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> ";
		xmlData += "<set label='2月' value='50'/> ";
		xmlData += "<set label='3月' value='75'/> ";
		xmlData += "<set label='4月' value='100'/> ";
		xmlData += "</chart>";
		line1.setDataXML(xmlData);
		line1.render("Column3D");
  }
  
  function setStCol2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/StackedColumn2D.swf?ChartNoDataText=暂无数据", "StCol2D", "100%", "100%", "0", "3");
		xmlData += "<chart caption='2D累加柱状图实例'  palette='2' caption='Product Comparison' shownames='1' showvalues='0'  numberPrefix='$' showSum='1' decimals='0' useRoundEdges='1'>";
		xmlData += "<categories>";
			xmlData += "<category label='Product A' />";
			xmlData += "<category label='Product B' />";
			xmlData += "<category label='Product C' />";
			xmlData += "<category label='Product D' />";
			xmlData += "<category label='Product E' />";
		xmlData += "</categories>";
		xmlData += "<dataset seriesName='2004' color='AFD8F8' showValues='0'>";
			xmlData += "<set value='25601.34' />";
			xmlData += "<set value='20148.82' />";
			xmlData += "<set value='17372.76' />";
			xmlData += "<set value='35407.15' />";
			xmlData += "<set value='38105.68' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='2005' color='F6BD0F' showValues='0'>";
			xmlData += "<set value='57401.85' />";
			xmlData += "<set value='41941.19' />";
			xmlData += "<set value='45263.37' />";
			xmlData += "<set value='117320.16' />";
			xmlData += "<set value='114845.27' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='2006' color='8BBA00' showValues='0'>";
			xmlData += "<set value='45000.65' />";
			xmlData += "<set value='44835.76' />";
			xmlData += "<set value='18722.18' />";
			xmlData += "<set value='77557.31' />";
			xmlData += "<set value='92633.68' />";
		xmlData += "</dataset>";
		xmlData += "</chart>";
		line1.setDataXML(xmlData);
		line1.render("StCol2D");
  }
  
  function setStCol3D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/StackedColumn3D.swf?ChartNoDataText=暂无数据", "StCol3D", "100%", "100%", "0", "3");
		xmlData += "<chart caption='2D累加柱状图实例'  palette='2' caption='Product Comparison' shownames='1' showvalues='0'  numberPrefix='$' showSum='1' decimals='0' useRoundEdges='1'>";
		xmlData += "<categories>";
			xmlData += "<category label='Product A' />";
			xmlData += "<category label='Product B' />";
			xmlData += "<category label='Product C' />";
			xmlData += "<category label='Product D' />";
			xmlData += "<category label='Product E' />";
		xmlData += "</categories>";
		xmlData += "<dataset seriesName='2004' showValues='0'>";
			xmlData += "<set value='25601.34' />";
			xmlData += "<set value='20148.82' />";
			xmlData += "<set value='17372.76' />";
			xmlData += "<set value='35407.15' />";
			xmlData += "<set value='38105.68' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='2005'  showValues='0'>";
			xmlData += "<set value='57401.85' />";
			xmlData += "<set value='41941.19' />";
			xmlData += "<set value='45263.37' />";
			xmlData += "<set value='117320.16' />";
			xmlData += "<set value='114845.27' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='2006' showValues='0'>";
			xmlData += "<set value='45000.65' />";
			xmlData += "<set value='44835.76' />";
			xmlData += "<set value='18722.18' />";
			xmlData += "<set value='77557.31' />";
			xmlData += "<set value='92633.68' />";
		xmlData += "</dataset>";
		xmlData += "</chart>";
		line1.setDataXML(xmlData);
		line1.render("StCol3D");
  }
</script>
</head>  
  
<body onload="init();">
<table>
	<tr>
		<td><div id="Column2D" style="width:500px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		<td><div id="Column3D" style="width:500px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
	<tr>
		<td><div id="StCol2D" style="width:500px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		<td><div id="StCol3D" style="width:500px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
</table>
</body>
</html>
