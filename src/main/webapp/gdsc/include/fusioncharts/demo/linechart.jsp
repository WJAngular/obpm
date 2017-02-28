<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
  
<script type="text/javascript">
  function init() {
  		setLine();
  		setMSLine();
  		
  		setMSChixujichen();
  }
  
  function setLine(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Line.swf?ChartNoDataText=暂无数据", "Line", "100%", "100%", "0", "1");
		xmlData = "<chart caption='单线图实例' numberSuffix='' exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' showExportDataMenuItem='1'";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
		xmlData += "<set label='1月' value='25'/> <set label='2月' value='50'/> <set label='3月' value='75'/> <set label='4月' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Line");
  }
  
  function setMSLine(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/MSLine.swf?ChartNoDataText=暂无数据", "MSLine", "100%", "100%", "0", "1");
		
		xmlData += "<chart caption='多线图实例' lineThickness='1' showValues='0' formatNumberScale='0' anchorRadius='2'   divLineAlpha='20' divLineColor='CC3300' divLineIsDashed='1' showAlternateHGridColor='1' alternateHGridAlpha='5' alternateHGridColor='CC3300' shadowAlpha='40' labelStep='2' numvdivlines='5' chartRightMargin='35' bgColor='FFFFFF,CC3300' bgAngle='270' bgAlpha='10,10'>";
		xmlData += "<categories >";
			xmlData += "<category label='8/6' />";
			xmlData += "<category label='8/7' />";
			xmlData += "<category label='8/8' />";
			xmlData += "<category label='8/9' />";
			xmlData += "<category label='8/10' />";
			xmlData += "<category label='8/11' />";
			xmlData += "<category label='8/12' />";
		xmlData += "</categories>";
		xmlData += "<dataset seriesName='Offline Marketing' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
			xmlData += "<set value='1327' />";
			xmlData += "<set value='1826' />";
			xmlData += "<set value='1699' />";
			xmlData += "<set value='1511' />";
			xmlData += "<set value='1904' />";
			xmlData += "<set value='1957' />";
			xmlData += "<set value='1296' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='Search' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C'>";
			xmlData += "<set value='2042' />";
			xmlData += "<set value='3210' />";
			xmlData += "<set value='2994' />";
			xmlData += "<set value='3115' />";
			xmlData += "<set value='2844' />";
			xmlData += "<set value='3576' />";
			xmlData += "<set value='1862' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='Paid Search' color='2AD62A' anchorBorderColor='2AD62A' anchorBgColor='2AD62A'>";
			xmlData += "<set value='850' />";
			xmlData += "<set value='1010' />";
			xmlData += "<set value='1116' />";
			xmlData += "<set value='1234' />";
			xmlData += "<set value='1210' />";
			xmlData += "<set value='1054' />";
			xmlData += "<set value='802' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='From Mail' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>";
			xmlData += "<set value='541' />";
			xmlData += "<set value='781' />";
			xmlData += "<set value='920' />";
			xmlData += "<set value='754' />";
			xmlData += "<set value='840' />";
			xmlData += "<set value='893' />";
			xmlData += "<set value='451' />";
		xmlData += "</dataset>";
		
		xmlData += "</chart>";
		line1.setDataXML(xmlData);
		line1.render("MSLine");
  }
  
  function setMSChixujichen(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/MSLine.swf?ChartNoDataText=暂无数据", "MSChixujichen", "100%", "100%", "0", "1");
		
		xmlData += "<chart baseFont='Arial' baseFontSize ='12' caption='知识库系统持续集成跟踪图' lineThickness='1' showValues='0' formatNumberScale='0' anchorRadius='1'   divLineAlpha='20' divLineColor='CC3300' divLineIsDashed='1' showAlternateHGridColor='1' alternateHGridAlpha='5' alternateHGridColor='CC3300' shadowAlpha='40' labelStep='1' numvdivlines='5' chartRightMargin='35' bgColor='FFFFFF,CC3300' bgAngle='270' bgAlpha='10,10'>";
		xmlData += "<categories >";
			xmlData += "<category label='12-03' />";
			xmlData += "<category label='12-05' />";
			xmlData += "<category label='12-07' />";
			xmlData += "<category label='12-09' />";
			xmlData += "<category label='12-19' />";
			xmlData += "<category label='12-24' />";
			xmlData += "<category label='12-29' />";
			xmlData += "<category label='01-02' />";
			xmlData += "<category label='01-06' />";
			xmlData += "<category label='01-09' />";
			xmlData += "<category label='01-14' />";
			xmlData += "<category label='01-16' />";
		xmlData += "</categories>";
		xmlData += "<dataset seriesName='checkstyle' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C'>";
			xmlData += "<set value='2' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='2' />";
			xmlData += "<set value='30' />";
			xmlData += "<set value='1' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='pmd' color='2AD62A' anchorBorderColor='2AD62A' anchorBgColor='2AD62A'>";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='1' />";
			xmlData += "<set value='1' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='3' />";
			xmlData += "<set value='1' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
		xmlData += "</dataset>";
		xmlData += "<dataset seriesName='FindBugs' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>";
			xmlData += "<set value='1' />";
			xmlData += "<set value='2' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='2' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
			xmlData += "<set value='0' />";
		xmlData += "</dataset>";
		xmlData += "</chart>";
		line1.setDataXML(xmlData);
		line1.render("MSChixujichen");
  }
</script>
</head>  
  
<body onload="init();">
<table>
	<tr>
		<td><div id="MSChixujichen" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
	<tr>
		<td><div id="Line" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
	<tr>
		<td><div id="MSLine" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
	</tr>
</table>
</body>
</html>
