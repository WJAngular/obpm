import cn.myapps.chartview.ChartViewCanvas;
import cn.myapps.chartview.SaveDialog;
import cn.myapps.oreport.ViewType;

import com.adobe.serialization.json.JSON;

import flash.display.DisplayObject;
import flash.display.Sprite;
import flash.events.MouseEvent;

import mx.controls.Alert;
import mx.controls.Tree;
import mx.core.Application;
import mx.core.Container;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;

//菜单点击事件
protected function topMenuItemClick(event:MenuEvent):void{
	var chartViewCanvas:ChartViewCanvas = (owner as Tree).parent.parent.parent.parent as ChartViewCanvas;
		if(event.item.@label=="新建报表"){
			for each(var m:Container in Application.application.superTabNavigator.getChildren()){
				if((m.label as String) == "新建报表"){
					Application.application.superTabNavigator.selectedChild = m;
					return;
				}
			}
			var _viewType:ViewType = new ViewType();
			_viewType.label = "新建报表";
			Application.application.superTabNavigator.addChild(_viewType);
			Application.application.superTabNavigator.selectedChild = _viewType;
		}else if(event.item.@label=="重命名"){
			//隐藏iframe
	 		if(Application.application._explorerCanvas.iframe!=null){
	 			Application.application._explorerCanvas.iframe.visible = false;
	 		}
			var saveDialog:SaveDialog = PopUpManager.createPopUp(application as DisplayObject,SaveDialog,true) as SaveDialog;
			saveDialog.viewVOid = data.id;
			saveDialog.chartViewCanvas = chartViewCanvas;
			PopUpManager.centerPopUp(saveDialog);
		}else if(event.item.@label=="删除"){
			//隐藏iframe
	 		if(Application.application._explorerCanvas.iframe!=null){
	 			Application.application._explorerCanvas.iframe.visible = false;
	 		}
			Alert.show("你确定删除 '"+data.label+"'？","提示",3, application as Sprite, function(event:CloseEvent):void {
			if (event.detail==Alert.YES){
				oReportRO.addEventListener(ResultEvent.RESULT,deleteViewVOResult);
				oReportRO.deleteViewVO((data.id as String));
				if(Application.application._explorerCanvas.iframe!=null){
		 			Application.application._explorerCanvas.explorerCanvas.visible = true;
		 			Application.application._explorerCanvas.viewChartCanvas.visible = false;
		 			Application.application._explorerCanvas.iframe.visible = false;
		 		}
			}else{
				if(Application.application._explorerCanvas.iframe!=null){
		 			Application.application._explorerCanvas.iframe.visible = true;
		 		}
			}
			});
		}else if(event.item.@label=="编辑"){
			//判断是否已存在tab
			for each(var sh:Container in Application.application.superTabNavigator.getChildren()){
				if((sh.label as String) == (data.label as String)){
					Application.application.superTabNavigator.selectedChild = sh;
					return;
				}
			}
			var _chartViewCanvas:ChartViewCanvas = new ChartViewCanvas();
			_chartViewCanvas.label = data.label;//canvas容器的标题
			_chartViewCanvas.viewVOid = data.id;//视图操作编号
			_chartViewCanvas.viewLabel =data.label;//视图操作名称
			_chartViewCanvas.openType = "ChartViewCanvasLoad";
			Application.application.superTabNavigator.addChild(_chartViewCanvas);
			Application.application.superTabNavigator.selectedChild = _chartViewCanvas;
		}
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
	oReportRO.getAllViewVO(applicationid,moduleid,"false",userid);
}

//查看图表
protected function lookChartEvent(event:MouseEvent):void{
	//前台没有初始化id
	if(userid !=null && userid !=""){
		Application.application.createChartViewCanvas(data.id);
	}else{
		//判断是否已有tab
		for each(var i:Container in Application.application.superTabNavigator.getChildren()){
			if((i.label as String) == (data.label as String)){
				Application.application.superTabNavigator.selectedChild = i;
				return;
			}
		}
		//没有情况下
		var _chartViewCanvas:ChartViewCanvas = new ChartViewCanvas();
		_chartViewCanvas.label = data.label;//canvas容器的标题
		_chartViewCanvas.viewVOid = data.id;//视图操作编号
		_chartViewCanvas.viewLabel =data.label;//视图操作名称
		_chartViewCanvas.openType = "ChartViewCanvasLoad";
		Application.application.superTabNavigator.addChild(_chartViewCanvas);
		Application.application.superTabNavigator.selectedChild = _chartViewCanvas;
	}
}



