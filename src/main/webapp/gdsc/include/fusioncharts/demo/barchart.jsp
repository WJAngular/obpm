<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html>
<head>
	<%@ include file="/include/head.jsp" %>
	<%@include file="/include/fusionchars.jsp" %>
</head>
  
<script type="text/javascript">
  function init() {
  		setBar2D();
  }
  
  
  function setBar2D(){
		var xmlData;
		var line1 = new FusionCharts("${ctx}/fusioncharts/charts/Bar2D.swf?ChartNoDataText=��������", "haha", "500", "300", "0", "1");
		xmlData = "<chart caption='����ͼʵ��' numberSuffix='�0�6'";
		xmlData += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'"; 
		
		
		xmlData += "useRoundEdges='1' bgColor='FFFFFF,FFFFFF' showBorder='0'  ";
		xmlData += "exportEnabled='1' exportAtClient='0' exportAction='save' ";
		xmlData += "exportDialogMessage='��������,���Ժ�...' exportFormats='JPG=����JPGͼƬ|PDF=����PDF�ļ�'";
		xmlData += "exportHandler='${ctx}/fusioncharts/jsp/FCExporter.jsp' exportFileName='bartest--yyyy-MM-dd HH-mm-ss'> ";
		
		xmlData += "<set label='1��' value='25'/> <set label='2��' value='50'/> <set label='3��' value='75'/> <set label='4��' value='100'/> </chart>";
		line1.setDataXML(xmlData);
		line1.render("Bar2D");
  }
  
  function showFusionCharts(){
		var myChart = new FusionCharts("${ctx}/fusioncharts/charts/Bar2D.swf", "haha", "500", "300", "0", "1");
		
		var charxml = "";
		charxml +="<chart caption='����ͼʵ��' numberSuffix='�0�6'";
		charxml += "useRoundEdges='1' bgColor='FFFFFF,FFFFFF' showBorder='0'  ";
		charxml += "showValues='1' decimalPrecision='2' decimals='2' formatNumberScale='0' baseFont='Arial' baseFontSize ='12'"; 
		
		charxml += "exportEnabled='1' exportAtClient='0' exportAction='save' ";
		charxml += "exportDialogMessage='��������,���Ժ�...' exportFormats='JPG=����JPGͼƬ|PDF=����PDF�ļ�'";
		charxml += "exportHandler='${ctx}/fusioncharts/jsp/FCExporter.jsp' exportFileName='download'> ";
		
		charxml += "<set label='1��' value='25'/>  ";
		charxml += "<set label='2��' value='50'/>  ";
		charxml += "<set label='3��' value='75'/>  ";
		charxml += "<set label='4��' value='100'/> ";
		charxml += "</chart> ";
		
		myChart.setDataXML(charxml);
		myChart.render("Bar2D");
	}
  

	//����ͼƬ���õķ���
	function startExport(){
		var chart = getChartFromId("haha");
		alert(chart);
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
   		<input type='button' value='����FusionChartsͼƬ' onClick="javascript:startExport();" />
	</tr>
	<tr>
		<td>
			<div id="Bar2D" style="width:600px;height:350px;border-collapse: collapse;overflow-y:hidden;"></div>
		</td>
	</tr>
</table>
</body>
</html>
