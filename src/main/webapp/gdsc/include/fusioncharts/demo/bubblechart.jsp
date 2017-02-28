<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
  
<script type="text/javascript">
	function init() {
		setAngularGauge();
  	}
  
  	function setAngularGauge(){
  		var maxdate = 0;
		var xmlData;
		var chart = new FusionCharts("${ctx}/fusioncharts/charts/Bubble.swf?ChartNoDataText=暂无数据", "chartdiv", "100%", "100%", "0", "0");
		
		xmlData += " <chart caption='冒泡图实例' palette='3' numberPrefix='$' is3D='1' animation='1' clipBubbles='1' xAxisMaxValue='100' showPlotBorder='0' xAxisName='横向标题' yAxisName='纵向标题' chartRightMargin='30'> "; 
		xmlData += " <categories>";
			xmlData += " <category label='0%' x='0' />";
			xmlData += " <category label='20%' x='20' showVerticalLine='1'/>";
			xmlData += " <category label='40%' x='40' showVerticalLine='1'/>";
			xmlData += " <category label='60%' x='60' showVerticalLine='1'/>";
			xmlData += " <category label='80%' x='80' showVerticalLine='1'/>";
			xmlData += " <category label='100%' x='100' showVerticalLine='1'/>";
		xmlData += " </categories>";
		xmlData += " <dataSet showValues='0'>";
			xmlData += " <set x='30' y='1.3' z='116' name='Traders'/>";
			xmlData += " <set x='32' y='3.5' z='99' name='Farmers'/>";
			xmlData += " <set x='8' y='2.1' z='33' name='Individuals'/>";
			xmlData += " <set x='62' y='2.5' z='72' name='Medium Business Houses'/>";
			xmlData += " <set x='78' y='2.3' z='55' name='Corporate Group A'/>";
			xmlData += " <set x='75' y='1.4' z='58' name='Corporate Group C'/>";
			xmlData += " <set x='68' y='3.7' z='80' name='HNW Individuals'/>";
			xmlData += " <set x='50' y='2.1' z='105' name='Small Business Houses'/>";
		xmlData += " </dataSet>";
		xmlData += " <trendlines>";
			xmlData += " <line startValue='2.5' isTrendZone='0' displayValue='Median Cost' color='0372AB'/>";
			xmlData += " <line startValue='4.5' isTrendZone='0' displayValue='Haha Cost' color='0372AB'/>";
		xmlData += " </trendlines>";
		xmlData += " <vTrendlines>";
			xmlData += " <line startValue='0' endValue='60' isTrendZone='1' displayValue='Potential Wins' color='663333' alpha='10'/>";
			xmlData += " <line startValue='60' endValue='80' isTrendZone='1' displayValue='To Wins' color='000000' alpha='10'/>";
			xmlData += " <line startValue='80' endValue='100' isTrendZone='1' displayValue='Cash Cows' color='990099' alpha='5'/>";
		xmlData += " </vTrendlines>";
		xmlData += " </chart>";
		
		chart.setDataXML(xmlData);
		chart.render("chartdiv");
  	}
</script>
</head>  
  
<body onload="init();">
<table>
	<tr>
		<td><div id="chartdiv" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
</table>
</body>
</html>
