import cn.myapps.chartview.ChartViewCanvas;

import com.adobe.serialization.json.JSON;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.core.Application;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

public var chartType:String="PlotChart";//图表类型
public var openType:String;//打开类型
public var oReportRO:RemoteObject;
public var domainid:String;
public var _chartViewCanvas:ChartViewCanvas;

public var flag:Boolean = true;

//初始化
protected function init():void{
	flag = true;
	if((_chartViewCanvas._yList.dataProvider as ArrayCollection).length>1 && (_chartViewCanvas._xList.dataProvider as ArrayCollection).length==1){
		flag = false;
	}
	oReportRO = Application.application.oReportRO;
	iframe.source="switch.jsp?openType="+openType+"&chartType="+chartType+"&domainid="+domainid;
}

//导出
protected function exportEvent(event:MouseEvent):void{
	if(chartType=="Table"){
		Application.application.showMessage("assets/warning.png","表格类型不支持导出为PDF");
	}else{
		iframe.visible = false;
		Alert.show("您确定将图表导出为PDF？","提示",3, Application.application as Sprite, function(event:CloseEvent):void {
		if (event.detail==Alert.YES){
			iframe.visible = true;
			oReportRO.addEventListener(ResultEvent.RESULT,exportEventResult);
			if(openType == "ChartViewCanvasCreate"){
				oReportRO.saveChartAsPDF(_chartViewCanvas.createChartJson(),chartType,"",domainid);
			}else{
				oReportRO.saveChartAsPDF(_chartViewCanvas.viewVOjson,chartType,"",domainid);
			}
		}else{
			iframe.visible = true;
		}
		});
	}
}

//导出结果
protected function exportEventResult(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,exportEventResult);
	if(event.result.toString()!=""){
		var obj:Object = JSON.decode(event.result.toString());
		if(obj.message!=null){
			Application.application.showMessage(obj.icon,obj.message);
		}
	}
}

//打印
protected function pirntEvent(event:MouseEvent):void{
	iframe.visible = false;
	Alert.show("您确定打印图表？","提示",3, Application.application as Sprite, function(event:CloseEvent):void {
	if (event.detail==Alert.YES){
		iframe.visible = true;
		iframe.printIFrame();
	}else{
		iframe.visible = true;
	}
	});
}


//改变图表类型
public function changeChartTypeEvent(event:MouseEvent):void{
   var item:LinkButton = event.target as LinkButton;
   if(!flag){
   	  if(item.id =="PieChart"){
   	  	Application.application.showMessage("assets/warning.png","馅饼图只支持单X轴、单Y轴、单XY轴组合");
   	  	return;
   	  }else if(item.id =="BarChart"){
   	  	Application.application.showMessage("assets/warning.png","条形图只支持单X轴、单Y轴、单XY轴组合");
   	  	return;
   	  }else if(item.id =="AreaChart"){
   	  	Application.application.showMessage("assets/warning.png","面积图只支持单X轴、单Y轴、单XY轴组合");
   	  	return;
   	  }else if(item.id =="ColumnChart"){
   	  	Application.application.showMessage("assets/warning.png","柱状图只支持单X轴、单Y轴、单XY轴组合");
   	  	return;
   	  }
   }
   chartType = item.id;
   init();
}

//关闭
public function cancel():void{
	iframe.removeIFrame();
	PopUpManager.removePopUp(this);
}