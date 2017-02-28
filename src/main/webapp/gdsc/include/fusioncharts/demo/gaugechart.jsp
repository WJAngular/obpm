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
		var chart = new FusionCharts("${ctx}/fusioncharts/charts/AngularGauge.swf?ChartNoDataText=暂无数据", "chartdiv", "100%", "100%", "0", "0");
		
		xmlData += " <chart caption='仪表盘实例' bgColor='e8f0f2,ffffff' baseFont='Arial' baseFontSize='12' baseFontColor='60634E' showNames='1' numberSuffix='%' fillAngle='45' upperLimit='100' lowerLimit='0' "; 
		xmlData += " majorTMNumber='10' majorTMHeight='8' showGaugeBorder='0' gaugeOuterRadius='120' gaugeOriginX='273' gaugeOriginY='220' gaugeInnerRadius='1' formatNumberScale='1' displayValueDistance='25' decimalPrecision='2' tickMarkDecimalPrecision='1' pivotRadius='5' showPivotBorder='1' pivotBorderColor='000000' pivotBorderThickness='5' pivotFillMix='FFFFFF,000000'> ";
		xmlData += " <colorRange> ";
			xmlData += " <color minValue='80' maxValue='100' code='fb1535' /> ";
			xmlData += " <color minValue='60' maxValue='80' code='f9d301' /> ";
			xmlData += " <color minValue='0' maxValue='60' code='2ad302' /> ";
		xmlData += " </colorRange> ";
		xmlData += " <dials> ";
			xmlData += " <dial value='20' borderAlpha='0' bgColor='000000' baseWidth='10' topWidth='1' radius='122' /> ";
			xmlData += " <dial value='60' borderAlpha='0' bgColor='ffffff' baseWidth='10' topWidth='1' radius='122' /> ";
		xmlData += " </dials> ";
		xmlData += " <annotations> ";
			xmlData += " <annotationGroup xPos='253' yPos='220'> ";
				xmlData += " <annotation type='circle' xPos='20' yPos='2.5' radius='125' startAngle='0' endAngle='180' fillPattern='linear' fillAsGradient='1' fillColor='dddddd,666666' fillAlpha='100,100' fillRatio='50,50' fillDegree='0' showBorder='1' borderColor='444444' borderThickness='2' /> ";
				xmlData += " <annotation type='circle' xPos='20' yPos='2.5' radius='123' startAngle='0' endAngle='180' fillPattern='linear' fillAsGradient='1' fillColor='666666,ffffff' fillAlpha='100,100' fillRatio='50,50' fillDegree='0' /> ";
			xmlData += " </annotationGroup> ";
		xmlData += " </annotations> ";
		xmlData += " </chart> ";
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
