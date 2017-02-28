import cn.myapps.chartview.SaveDialog;
import cn.myapps.oreport.SearchFormDialog;

import com.adobe.serialization.json.JSON;

import flash.display.DisplayObject;
import flash.display.Sprite;
import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.Alert;
import mx.core.Application;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;

public var flag:Boolean = true;

//新建事件
protected function newEvent(event:MouseEvent):void{
	var _viewType:ViewType = new ViewType();
	_viewType.label = "新建报表";
	Application.application.superTabNavigator.addChild(_viewType);
	Application.application.superTabNavigator.selectedChild = _viewType;
}

//删除事件
protected function deleteEvent(event:MouseEvent):void{
	Alert.show("您确定删除？","提示",3, Application.application as Sprite, function(event:CloseEvent):void {
	if (event.detail==Alert.YES){
		var j:int=0;
		var array:Array = new Array();
		for each(var i:Object in folderTree.dataProvider){
			if(i.selected){
				array.push(i.id);
				j++;
			}
		}
		if(j==0){
			Application.application.showMessage("assets/warning.png","请选择");
			return;
		}
		oReportRO.addEventListener(ResultEvent.RESULT,deleteViewVOResult);
		oReportRO.deleteViewVO(array);
	}
	});
}

//删除文件夹结果
protected function deleteViewVOResult(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,deleteViewVOResult);
	if(event.result.toString()!=""){
		var obj:Object = JSON.decode(event.result.toString());
		if(obj.message!=null){
			Application.application.showMessage(obj.icon,obj.message);
		}
	}
	oReportRO.addEventListener(ResultEvent.RESULT,Application.application.getAllViewVOEvent);
	oReportRO.getAllViewVO(Application.application.applicationid,Application.application.moduleid,"false",Application.application.userId);
}

//重名事件
protected function renameEvent(event:MouseEvent):void{
	var j:int=0;
	var viewVOid:String="";
	for each(var i:Object in folderTree.dataProvider){
		if(i.selected){
			viewVOid = i.id;
			j++;
		}
	}
	if(j==0){
		Application.application.showMessage("assets/warning.png","请选择");
		return;
	}
	if(j>1){
		Application.application.showMessage("assets/warning.png","只能选择一个");
		return;
	}
	
	var saveDialog:SaveDialog = PopUpManager.createPopUp(Application.application as DisplayObject,SaveDialog,true) as SaveDialog;
	saveDialog.viewVOid = viewVOid;
	PopUpManager.centerPopUp(saveDialog);
	
}

//全选
protected function selectAllEvent(event:MouseEvent):void{
	oReportRO.addEventListener(ResultEvent.RESULT,Application.application.getAllViewVOEvent);
	oReportRO.getAllViewVO(Application.application.applicationid,Application.application.moduleid,"true",Application.application.userId);
}

//清空
protected function clearEvent(event:MouseEvent):void{
	oReportRO.addEventListener(ResultEvent.RESULT,Application.application.getAllViewVOEvent);
	oReportRO.getAllViewVO(Application.application.applicationid,Application.application.moduleid,"false",Application.application.userId);
}

//获得json
protected function getCreateChartJsonResutl(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,getCreateChartJsonResutl);
	if(event.result.toString()!=null&&event.result.toString()!=""){
		//生成图表
		json = event.result.toString();
		var obj:Object =  JSON.decode(json);
		viewid  = obj.viewid;
		flag = true;
		if((obj.yColumn as Array).length>1){
			flag = false;
			//第一种情况前台初始有图表id
			if(Application.application.chartId != null && Application.application.chartId !="" && Application.application.chartId !="null"){
				veiwChartApplicationControlBar.removeChild(pieChartButton);
				veiwChartApplicationControlBar.removeChild(barChartButton);
				veiwChartApplicationControlBar.removeChild(areaChartButton);
				veiwChartApplicationControlBar.removeChild(columnChartButton);
			}
		}
		createChartEvent(event);
	}
	
}

//改变图表类型
protected function changeChartTypeEvent(event:MouseEvent):void{
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
   createChartEvent(event);
}

//创建图表
protected function createChartEvent(event:Event):void{
	iframe.source="switch.jsp?openType=ExplorerCanvas&chartType="+chartType+"&domainid="+Application.application.domainid;
}


//导出
protected function exportEvent(event:MouseEvent):void{
	iframe.visible = false;
	Alert.show("您确定将图表导出为PDF？","提示",3, Application.application as Sprite, function(event:CloseEvent):void {
	if (event.detail==Alert.YES){
		iframe.visible = true;
		oReportRO.addEventListener(ResultEvent.RESULT,exportEventResult);
		oReportRO.saveChartAsPDF(json,chartType,JSON.encode(map),Application.application.domainid);
	}else{
		iframe.visible = true;
	}
	});
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

//返回
protected function backEvent(event:MouseEvent):void{
	explorerCanvas.visible = true;
	viewChartCanvas.visible = false;
	iframe.visible = false;
}

//筛选
protected function searchEvent(event:MouseEvent):void{
	searchFormDialog = PopUpManager.createPopUp(Application.application as DisplayObject,SearchFormDialog,true) as SearchFormDialog;
	iframe.visible = false;
	searchFormDialog.iframe = iframe;
	searchFormDialog.explorerCanvas = this;
	PopUpManager.centerPopUp(searchFormDialog);
}