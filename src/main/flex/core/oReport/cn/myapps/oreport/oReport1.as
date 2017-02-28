import cn.myapps.chartview.ChartViewCanvas;
import cn.myapps.oreport.ExplorerCanvas;
import cn.myapps.oreport.ShowManager;
import cn.myapps.oreport.tree.TreeImageItemRender;

import com.adobe.serialization.json.JSON;

import flexlib.controls.tabBarClasses.SuperTab;
import flexlib.events.SuperTabEvent;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;
import mx.controls.Tree;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

[Bindable]
public var hostAddress:String;//请求服务器
public var applicationid:String;//软件编号
public var domainid:String;//域编号
public var userId:String;//用户编号
public var adminId:String;//管理员编号
public var chartId:String;//图表编号
public var moduleid:String;

public var _folderTreeCanvas:Canvas;
public var _explorerCanvas:ExplorerCanvas;
public var folderTree:Tree;
public var explorerTree:Tree;
public var _chartViewCanvas:ChartViewCanvas;


//初始化
protected function init():void{
	hostAddress = Application.application.parameters.hostAddress;
	applicationid = Application.application.parameters.applicationid;
	domainid = Application.application.parameters.domainid;
	userId = Application.application.parameters.userId;
	adminId = Application.application.parameters.adminId;
	chartId = Application.application.parameters.chartId;
	moduleid = Application.application.parameters.moduleid;
	
	//设置superTabNavigator第一个tab关闭按钮隐藏
	superTabNavigator.setClosePolicyForTab(0, SuperTab.CLOSE_NEVER);
	if(chartId != null && chartId !="" && chartId !="null"){
		_explorerCanvas = new ExplorerCanvas();
		_hdividedBox.addChild(_explorerCanvas);
		
		createChartViewCanvas(chartId);
	}else{
		_folderTreeCanvas = new Canvas();
		_folderTreeCanvas.width=200;
        _folderTreeCanvas.percentHeight = 100;
        _folderTreeCanvas.setStyle("borderStyle","solid");
        _folderTreeCanvas.horizontalScrollPolicy="off"; 
        _folderTreeCanvas.verticalScrollPolicy="off"
        _hdividedBox.addChild(_folderTreeCanvas);
        
        _explorerCanvas = new ExplorerCanvas();
		_hdividedBox.addChild(_explorerCanvas);
		
		folderTree = new Tree();//文件夹树
		folderTree.setStyle("borderStyle","none");
		folderTree.percentWidth = 100;
		folderTree.percentHeight = 100;
		folderTree.itemRenderer = new ClassFactory(TreeImageItemRender);
		_folderTreeCanvas.addChild(folderTree);
		
		oReportRO.addEventListener(ResultEvent.RESULT,getAllViewVOEvent);
		oReportRO.getAllViewVO(applicationid,moduleid,"false",userId);
	}
	explorerTree = _explorerCanvas.folderTree;
	
	ExternalInterface.addCallback("cancelSearchFormDialog",cancelSearchFormDialog);
	ExternalInterface.addCallback("querySearchFormDialog",querySearchFormDialog);
	ExternalInterface.addCallback("getJSON",getJSON);
	ExternalInterface.addCallback("getFilter",getFilter);
}

//获得所有文件夹
public function getAllViewVOEvent(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,getAllViewVOEvent);
	if(event.result.toString()!=""){
		var obj:Object = JSON.decode(event.result.toString());
		if(obj.message!=null){
			showMessage(obj.icon,obj.message);
		}else{
			folderTree.dataProvider = obj.views;
			folderTree.validateNow();
			explorerTree.dataProvider = obj.views;
			explorerTree.validateNow();
			for each(var k:Object in obj.folders){
				folderTree.expandChildrenOf(k,true);
				explorerTree.expandChildrenOf(k,true);
			}
		}
	}else{
		folderTree.dataProvider = new ArrayCollection();
		explorerTree.dataProvider = new ArrayCollection();
	}
}

//改变事件
protected function changeEvent():void{
	superTabNavigator.getTabAt(superTabNavigator.selectedIndex);
}


//请求失败的处理
protected function fault(event:FaultEvent):void{
	var _SuperTab:SuperTab = new SuperTab();
}

//弹出提示信息
public function showMessage(imgSources:String,message:String):void{
	var _showMessage:ShowManager = PopUpManager.createPopUp(this,ShowManager,false) as ShowManager;
	_showMessage.imgSources = imgSources;
	_showMessage.message = message;
	_showMessage.x=0;
	_showMessage.y=0;
}

//SuperTabNavigator关闭事件
public function closeCanvas(event:SuperTabEvent):void{
	var tabIndex:int=event.tabIndex;
	if(event.target.getChildAt(tabIndex) is ChartViewCanvas){
		//var iframe:IFrame = (event.target.getChildAt(tabIndex) as ChartViewCanvas).resultChartPanel;
		//iframe.visible = false;
       // iframe.removeIFrame();
	}
}


//查看图表
public function createChartViewCanvas(viewVOid:String):void{
	_explorerCanvas.createChartViewCanvas(viewVOid);
}

//调用关闭弹出查询表单窗口
public function cancelSearchFormDialog():void{
	_explorerCanvas.searchFormDialog.cancel();
}

//调用查询事件查询事件
public function querySearchFormDialog(map:Object):void{
	_explorerCanvas.searchFormDialog.searchEvent(map);
	_explorerCanvas.map = map;
}

//iframe页面调用该方法获得json
public function getJSON(str:String):String{
	if(str=="ChartViewCanvasLoad"){
		return _chartViewCanvas.viewVOjson;
	}else if(str=="ChartViewCanvasCreate"){
		return _chartViewCanvas.createChartJson();
	}else if(str =="ExplorerCanvas"){
		return _explorerCanvas.json;
	}
	return "";
}

//iframe页面调用该方法获得filter
public function getFilter():String{
	return JSON.encode(_explorerCanvas.map);
}