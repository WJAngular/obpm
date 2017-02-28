<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
<script type="text/javascript">
  function init() {
  		setArea2D();
  }
  
  function setArea2D(){
  		var maxdate = 0;
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Area2D.swf?ChartNoDataText=暂无数据", "Area2D", "100%", "100%", "0", "1");
		xmlData = "<chart caption='区域图实例' numberSuffix=''";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'>";
			xmlData += "<set label='1月' value='50'/> "; 
			xmlData += "<set label='2月' value='30'/> ";  
			xmlData += "<set label='3月' value='75'/> "; 
			xmlData += "<set label='4月' value='20'/> "; 
			
		xmlData += "</chart>";
		line1.setDataXML(xmlData);
		line1.render("Area2D");
  }
  
</script>
</head>  
  
<body onload="init();">
<table>
	<tr>
		<td><div id="Area2D" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div></td>
		
	</tr>
	<tr>
	</tr>
</table>
</body>
</html>
