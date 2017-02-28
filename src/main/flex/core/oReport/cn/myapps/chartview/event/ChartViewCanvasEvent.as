
import cn.myapps.chartview.DomainDialog;
import cn.myapps.chartview.PreviewDialog;
import cn.myapps.chartview.SaveDialog;

import com.adobe.serialization.json.JSON;

import flash.display.DisplayObject;

import flexlib.containers.SuperTabNavigator;

import mx.collections.ArrayCollection;
import mx.core.Application;
import mx.events.DragEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

public var viewid:String;//视图编号
public var viewLabel:String;//视图名称
public var viewVOid:String;//视图操作编号
public var viewVOlabel:String;//视图操作的名称
public var viewVOjson:String;//视图中获取的json
public var viewVOjsonObj:Object;//视图中获取的json中object对象
public var openType:String;//打开类型
public var applicationid:String;//软件编号
public var moduleid:String;//模块编号
public var domainid:String;//域编号
public var userid:String;//用户编号
public var adminid:String;//管理员编号
public var objData:Object;//列表对象
public var _superTabNavigator:SuperTabNavigator;
public var oReportRO:RemoteObject;

//初始化
protected function init():void{
	applicationid = Application.application.applicationid;
	domainid = Application.application.domainid;
	userid = Application.application.userId;
	adminid = Application.application.adminId;
	_superTabNavigator = Application.application.superTabNavigator;
	oReportRO = Application.application.oReportRO;
	
	_xList.dataProvider = new ArrayCollection();
	_yList.dataProvider = new ArrayCollection();
	
	if(objData!=null){
		_mxList.dataProvider = objData.node;
	}else{
		_mxList.dataProvider = new ArrayCollection();
	}
	
	if(viewVOid != "" && viewVOid !=null){
		Application.application._chartViewCanvas = this;
		oReportRO.addEventListener(ResultEvent.RESULT,getCreateChartJsonResutl);
		oReportRO.getCreateCharJson(viewVOid);
	}
}

//获得json
protected function getCreateChartJsonResutl(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,getCreateChartJsonResutl);
	if(event.result.toString()!=null&&event.result.toString()!=""){
		viewVOjson = event.result.toString();
		viewVOjsonObj = JSON.decode(event.result.toString());
		viewid = viewVOjsonObj.viewid;
		viewLabel = viewVOjsonObj.viewLabel;
	}
	oReportRO.addEventListener(ResultEvent.RESULT,getViewColumnsByViewArrayResult);
	oReportRO.getViewColumnsByViewArray(viewid);
}


//保存
protected function saveEvent():void{
	//检查xy轴
	if(!checkXY()){
		return;
	}
	if(viewVOid !=null && viewVOid !=""){
		oReportRO.addEventListener(ResultEvent.RESULT,saveResult);
		oReportRO.doSaveViewVO("",viewVOid,"","","",createChartJson(),"");
	}else{
		saveAsEvent();
	}
}

//另存为
protected function saveAsEvent():void{
	//检查xy轴
	if(!checkXY()){
		return;
	}
	var saveDialog:SaveDialog = PopUpManager.createPopUp(this,SaveDialog,true) as SaveDialog;
	saveDialog.chartViewCanvas = this;
	PopUpManager.centerPopUp(saveDialog);
}

//保存结果
protected function saveResult(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,saveResult);
	if(event.result.toString()!=""){
		var obj:Object = JSON.decode(event.result.toString());
		if(obj.message!=null){
			Application.application.showMessage(obj.icon,obj.message);
		}
	}
}

//预览
protected function PreviewEvent():void{
	Application.application._chartViewCanvas = this;
	if(domainid == null || domainid == ""){
		var _domainDialog:DomainDialog = PopUpManager.createPopUp(Application.application as DisplayObject,DomainDialog,true) as DomainDialog;
		_domainDialog.isPopUp = false;
		_domainDialog._chartViewCanvas = this;
		_domainDialog.openType = openType;
		PopUpManager.centerPopUp(_domainDialog);
	}else{
		var _previewDialog:PreviewDialog = PopUpManager.createPopUp(Application.application as DisplayObject,PreviewDialog,true) as PreviewDialog;
		_previewDialog.domainid = domainid;
		_previewDialog.isPopUp = false;
		_previewDialog._chartViewCanvas = this;
		_previewDialog.openType = openType;
		PopUpManager.centerPopUp(_previewDialog);
	}
}


//刷新事件
protected function refreshEvent():void{
	if(objData!=null){
		_mxList.dataProvider = objData.node;
	}else{
		_mxList.dataProvider = new ArrayCollection();
	}
}

//搜索
protected function searchEvent():void{
	if(searchName.text!=""&&searchName.text!=null){
		var tempArrayCollection:ArrayCollection = new ArrayCollection();
		for each(var i:Object in _mxList.dataProvider){
			if((i.label).toString().indexOf(searchName.text)>-1){
				tempArrayCollection.addItem(i);
			}
		}
		_mxList.dataProvider = tempArrayCollection;
	}
}


//数据库中返回的字段  图表视图
protected function getViewColumnsByViewArrayResult(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,getViewColumnsByViewArrayResult);
	var obj:Object;
	//提示信息
	if(event.result.toString()!=""){
		obj = JSON.decode(event.result.toString());
		if(obj.message!=null){
			Application.application.showMessage(obj.icon,obj.message);
		}else{
			objData = obj;
			if(objData!=null){
				_mxList.dataProvider = objData.node;
			}else{
				_mxList.dataProvider = new ArrayCollection();
			}
			//回选x轴值
		    if(viewVOjsonObj.xColumn!=null){
		    	_xList.dataProvider = new ArrayCollection();
		    	for each(var x:Object in objData.node){
		    		if(x.label==viewVOjsonObj.xColumn.label){
		    			x.mfx = viewVOjsonObj.xColumn.fx;
		    			(_xList.dataProvider as ArrayCollection).addItem(x);
		    			break;
		    		}
		    	}
		    }
		    //判断x轴是否可拖动
		    checkxlistlengthEvent();
		    
			//回选y轴值
			if(viewVOjsonObj.yColumn!=null){
				_yList.dataProvider = new ArrayCollection();
				for each(var y:Object in viewVOjsonObj.yColumn){
					for each(var n:Object in objData.node){
		    		if(n.label==y.label){
		    			n.mfx = y.fx;
		    			(_yList.dataProvider as ArrayCollection).addItem(n);
		    			break;
		    		}
		    	}
				}
			}
			_mxList.dataProvider = objData.node;
		}
	}
}




//拖拉事件
protected function checkxlistlengthEvent():void{
	if((_xList.dataProvider as ArrayCollection).length==1){
		_xList.dropEnabled = false;
	}
}

