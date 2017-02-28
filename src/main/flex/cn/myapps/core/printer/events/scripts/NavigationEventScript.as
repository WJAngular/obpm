// ActionScript file
//封装Application 导航类控件 的事件处理方法
import main.flex.cn.myapps.core.printer.controls.PageProperty;
import main.flex.cn.myapps.core.printer.controls.SourceDialog;
import main.flex.cn.myapps.core.printer.util.MyPopUpManager;

import mx.controls.Alert;
import mx.events.MenuEvent;
import mx.managers.PopUpManager;

public var bDirty:Boolean;



/**
 *处理导航类按钮点击事件 
 * @param event
 * 
 */
public function navigationBt_itemClick(event:MenuEvent):void
{
	var pControl:Object=event.target;
	var pXML:XML=XML(event.item);
	var strName:String=pXML.@name;
	
	switch(strName){
		case "GenerateXML":
			//Win.visible=true;
			generateXML();
			break;
		case "PageProperty":
			//Win.visible=true;
			showPagePropertyDialog();
			break;
	}
}

/**
 *打开页面设置对话框 
 * 
 */
private function showPagePropertyDialog():void
{
	var page:PageProperty = new PageProperty();
	page.myFormat = this.paperFormat;
	page.mainApp = this; 
	PopUpManager.addPopUp(page,this,true);
	PopUpManager.centerPopUp(page);
	
}
/**
 *弹出 显示XML源码对话框 
 * @param strXML
 * 
 */
private function showXMLSource(strXML:String):void
{
	var dialog:SourceDialog = new SourceDialog();
	dialog.strXML =strXML;
	MyPopUpManager.addPopUp(dialog,this,true);
	//PopUpManager.addPopUp(dialog,this,true);
	//PopUpManager.centerPopUp(dialog);
}

/**
 * 保存打印配置 
 * @param event
 * 
 */
public function doSave(event:Event):void{
	var strXML:String=this.getXMLContent();
	var result:String = ExternalInterface.call("flexSave",strXML);
	if(result){
		Alert.show(result,"提示");
		//closeIE();
	}
	
	
//	var strXML:String=this.getXMLContent();
//	var myRequest:URLRequest = new URLRequest(saveUrl);
//	myRequest.method = URLRequestMethod.POST;
//	var params:URLVariables = new URLVariables();
//	params["content.formid"]=this.formid;//添加请求参数
//	params["content.template"]=strXML;
//	
//	myRequest.data = params;
//	
//	var pURL_ReadNode:URLLoader= new URLLoader();
//	pURL_ReadNode.load(myRequest);
//	pURL_ReadNode.addEventListener(Event.COMPLETE,doSave_CallBack);
}
private function doSave_CallBack(event:Event):void
{
	var loader:URLLoader = URLLoader(event.target);
	var result:String = loader.data.toString();
	if(result){
		Alert.show("保存成功");
		//closeIE();
	}
	
	
	
}
private function closeIE():void{
	var request:URLRequest = new URLRequest("javascript:window.close()");
	navigateToURL(request,"_self");
}


