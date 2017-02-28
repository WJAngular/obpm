import main.flex.cn.myapps.core.printer.components.base.MyCanvas;
import main.flex.cn.myapps.core.printer.util.Utils;

import flash.events.Event;
import flash.external.ExternalInterface;
import flash.geom.Point;
import flash.net.URLLoader;
import flash.net.URLRequest;
import flash.net.URLRequestMethod;
import flash.net.URLVariables;
/**
 *application creationComplete 事件 
 * @param event
 * 
 */
public function onload(event:Event):void
{	
	
	//1.获取  Http 传递过来的参数(可以是XML：String，也可以是 applicationId domainId formName 等字符串 )
	getParamters();
	//2 根据HTTP传递过来的参数  作为变量 传递到 服务器 URL地址上 获取返回的值-XML
	//var strXML:String = getXMLByParam(formid,activityid);
	var strXML:String = getTemplate();
	//测试之用
/*	
	strXML ='<?xml version="1.0" encoding="utf-8"?>' + 
			'<report  paperFormat="A4">' + 
			'<footer name="Footer526276" startX="0" startY="320" endX="595" endY="470" >' + 
			'<line name="Line179221" thickness="1" lineColor="0" startX="100" startY="340" endX="200" endY="340"></line>' + 
			'</footer>' + 
			'<form name="Form876572" startX="0" startY="0" endX="595" endY="249" >' + 
			'</form>' + 
			'</report>';
*/
	//3.解析XML文件 根据 节点名称实例化 组件，根据节点属性值 设置组件属性值，调用 组件的  newaddNewItem(...args)方法 显示到画板
	if(strXML){
		parseXML(strXML);
	}
	//4 填充 表单字段List  和子视图List
	getFieldList();
	getSubViewList();
	
	//5 初始化右键菜单
	initContextMenu();
	onApplicationReSize(event);
//	if(!strXML){
//		initFormCanvas();
//	}
	
	initTree();
	
}


/**
 * application resize 事件 
 * @param event
 * 
 */
public function onApplicationReSize(event: Event): void{
	if(hdbox != null &&  canvas != null ){
		hdbox.width = this.width - 5;
		hdbox.height = this.height  - 50;
		canvas.height = hdbox.height;
		tree.height = hdbox.height;
		report.height = paperFormat.dimensions[1];
		report.width = paperFormat.dimensions[0];
		canvas.width =report.width+16;
		
	}
}
/**
 *获取参数 
 * 
 */
private function getParamters():void {
	this.formid = ExternalInterface.call("doGetFormid");
	this.printWithFlow = this.parameters.printWithFlow;

}


/**
 *调用js获取 Printer 的template 
 * @return 
 * 
 */
private function getTemplate():String {
	return ExternalInterface.call("flexGetTemplate");
}




/**
 * 根据HTTP传递过来的参数  作为变量 传递到 服务器 URL地址上并 获取返回的值-XML
 * @param application
 * @param domian
 * @return 
 * 
 */
private function getXMLByParam(formid:String,activityid:String):String
{
	var request:URLRequest = new URLRequest(getTemplateUrl);
	var variables:URLVariables = new URLVariables();
	variables._formid = formid;//。。。variables.参数名= 值   a
	//variables.activityid = activityid;
	
	request.data = variables;
	var loader:URLLoader = new URLLoader();
	loader.load(request);
	Alert.show(loader.data.toString());
	return loader.data.toString();
}

/**
 * 根据传递的formid从服务器取得表单的所有字段
 * 
 */
private function getFieldList():void {
	//this.enabled =false;
	this.formid = ExternalInterface.call("doGetFormid");
	var myRequest:URLRequest = new URLRequest(getFieldsUrl);
	myRequest.method = URLRequestMethod.POST;
	var params:URLVariables = new URLVariables();
	params._formid=this.formid;//添加请求参数
	
	myRequest.data = params;
	
	var pURL_ReadNode:URLLoader= new URLLoader();
	pURL_ReadNode.load(myRequest);
	pURL_ReadNode.addEventListener(Event.COMPLETE,getFieldList_CallBack);
	
	//pURL_ReadNode.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
}

private function getFieldList_CallBack(event:Event):void
{
	var loader:URLLoader = URLLoader(event.target);
	fieldList = new XML(new XML(loader.data).toString());
	//this.enabled =true;
	//Alert.show(fieldList.toString());
	
}


/**
 * 根据传递的formid从服务器取得表单的所有字段
 * 
 */
private function getSubViewList():void {
	this.formid = ExternalInterface.call("doGetFormid");
	var myRequest:URLRequest = new URLRequest(getSubViewsUrl);
	myRequest.method = URLRequestMethod.POST;
	var params:URLVariables = new URLVariables();
	params._formid=this.formid;//添加请求参数
	
	myRequest.data = params;
	
	var pURL_ReadNode:URLLoader= new URLLoader();
	pURL_ReadNode.load(myRequest);
	pURL_ReadNode.addEventListener(Event.COMPLETE,getSubViewList_CallBack);
	
}

private function getSubViewList_CallBack(event:Event):void
{
	var loader:URLLoader = URLLoader(event.target);
	subViewList = new XML(new XML(loader.data).toString());
	//this.enabled =true;
	//Alert.show(subViewList.toString());
	
}

private function initFormCanvas():void{
	var p: Point = new Point();
	p = report.localToContent(new Point(0, 0));//转换坐标
	pSelectedCanvas =addCanvas("Form" + Utils.randomString(6),MyCanvas.C_FormCanvas,p.x+1,p.y+1,p.x+1+report.width,p.y+1+350);
	formCount++;
}

public function canvasResize():void{
	var p:TreapEnumerator = canvasTreap.Elements(true);;
			while(p.HasMoreElements()){
				var canvas:MyCanvas=MyCanvas(p.NextElement());
				canvas.endX = canvas.endX+report.width-(canvas.endX-canvas.startX)-canvas.x;
				canvas.redraw();
				removeContorlLine(canvas);
				initControlLine(canvas);
			}
}	
private function removeContorlLine(canvas:MyCanvas):void
{
	var lines:Array=canvas.controlLine;
	for (var i:int=0;i<lines.length;i++){
		canvas.removeChild(lines[i]);
		canvas.controlLine =new Array();
		
	}
}