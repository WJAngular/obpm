// ActionScript file
import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
import main.flex.cn.myapps.core.printer.components.base.MyCanvas;

import mx.controls.Alert;
import mx.core.UIComponent;


/**
 *解析XML文件 根据 节点名称实例化 控件，根据节点属性值 设置控件属性值，调用 控件的 newaddNewItem(...args)方法 显示到画板
 * @param strXML
 * 
 */
private function parseXML(strXML:String):void
{

	var xml:XML = new XML(strXML);
	pTreapControl = new Treap();
	canvasTreap= new Treap();
	formItemTreap = new Treap();
	detailItemTreap = new Treap();
	parseReport(xml);
	var leaderTags:Array=['form','detail','view','header','footer'];
	for each(var leaderTag:String in leaderTags){
		var xmlList:XMLList = xml.child(leaderTag);
		if(xmlList.length()>0){
			parseLeaderNode(xmlList,leaderTag);
		}
	}
}


/**
 *解析 Report元素 
 * @param xml
 * 
 */
private function parseReport(xml:XML):void
{
	var paperFormatLabel:String = xml.@paperFormat;
	if(paperFormatLabel)
		this.paperFormat = PaperFormat.getPaperFormat(paperFormatLabel);
}


/**
 *解析 Form元素 
 * @param Pcontrol
 * 
 */
private function parseForm(xmlList:XMLList,parentTag:String):void
{
	var parent:UIComponent = report;
	parseLeaderChildsNode(xmlList,parentTag,parent);
		
}
/**
 *解析 Detail元素 
 * @param Pcontrol
 * 
 */
private function parseLeaderNode(xmlList:XMLList,leaderTag:String):void
{
	switch(leaderTag){
		case "form" :
			parseCanvasNode(xmlList,leaderTag);
			break;
		case "detail" :
			parseCanvasNode(xmlList,leaderTag);
			break;
		case "view" :
			parseCanvasNode(xmlList,leaderTag);
			break;
		case "header" :
			parseCanvasNode(xmlList,leaderTag);
			break;
		case "footer" :
			parseCanvasNode(xmlList,leaderTag);
			break;
	}
	
		
}

private function parseLeaderChildsNode(xmlList:XMLList,leaderTag:String,parent:UIComponent):void
{
	var cControls:Array =['textbox','staticLabel','line','pageNumber','view'];
	for each(var controlsTag:String in cControls){
		var nodes:XMLList = xmlList.child(controlsTag);
		for(var i:int=0;i<nodes.length();i++){
			var node:XML = nodes[i];
			parseOneNode(node,controlsTag,leaderTag,parent);
		}
	}
	
}


/**
 *解析XML节点 
 * @param node
 * @param controlsTag
 * 
 */
private function parseOneNode(node:XML,controlsTag:String,parentTag:String,parent:UIComponent):void {
	var coutrolType:String = ItemDraw.MapType(controlsTag);
	switch(coutrolType){
		case ItemDraw.C_ItemTextBox :
			parseFieldNode(node,coutrolType,parent);
			break;
		case ItemDraw.C_StaticLabel :
			parseStaticLabelNode(node,coutrolType,parent);
			break;
		case ItemDraw.C_Line :
			parseLineNode(node,coutrolType,parent);
			break;
		case ItemDraw.C_PageNumber :
			parsePageNumberNode(node,coutrolType,parent);
			break;
		case ItemDraw.C_View :
			parseViewNode(node,coutrolType,parent);
			break;
		
	}
	
	
}

/**
 *解析 field节点  
 * @param node
 * @param coutrolType
 * 
 */
private function parseFieldNode(node:XML,coutrolType:String,parent:UIComponent):void {
	var pBox:ItemDraw =this.addComponent(node.@name,coutrolType,parent,node.@startX,node.@startY,node.@endX,node.@endY);
	pBox.bindingField =node.@bindingField;
	pBox.textarea.text =node.@strText;
	pBox.parentCanvas =parent;
	pBox.redraw();
}

/**
 *解析 staticLabel节点  
 * @param node
 * @param coutrolType
 * 
 */
private function parseStaticLabelNode(node:XML,coutrolType:String,parent:UIComponent):void {
	var pBox:ItemDraw =this.addComponent(node.@name,coutrolType,parent,node.@startX,node.@startY,node.@endX,node.@endY);
	pBox.textInput.text =node.@strText;
	pBox.parentCanvas =parent;
	pBox.redraw();
}

/**
 *解析 Line节点  
 * @param node
 * @param coutrolType
 * 
 */
private function parseLineNode(node:XML,coutrolType:String,parent:UIComponent):void {
	var pBox:ItemDraw =this.addComponent(node.@name,coutrolType,parent,node.@startX,node.@startY,node.@endX,node.@endY);
	pBox.parentCanvas =parent;
	pBox.redraw();
}

/**
 *解析 PageNumber节点  
 * @param node
 * @param coutrolType
 * 
 */
private function parsePageNumberNode(node:XML,coutrolType:String,parent:UIComponent):void {
	var pBox:ItemDraw =this.addComponent(node.@name,coutrolType,parent,node.@startX,node.@startY,node.@endX,node.@endY);
	pBox.parentCanvas =parent;
	pBox.redraw();
}


/**
 *解析 Line节点  
 * @param node
 * @param coutrolType
 * 
 */
private function parseViewNode(node:XML,coutrolType:String,parent:UIComponent):void {
	var pBox:ItemDraw =this.addComponent(node.@name,coutrolType,parent,node.@startX,node.@startY,node.@endX,node.@endY);
	pBox.parentCanvas =parent;
	pBox.bindingView =node.@bindView;
	pBox.textarea.text =node.@strText;
	pBox.redraw();
}

/**
 *解析detial 节点 
 * @param node
 * @param coutrolType
 * @param parent
 * 
 */
private function parseCanvasNode(xmllist:XMLList,leaderTag:String):void {
	for(var i:int=0;i<xmllist.length();i++){
		var node:XML = xmllist[i];
		var xmlList:XMLList =new XMLList(node);
		var canvas:MyCanvas = this.addCanvas(node.@name,MyCanvas.MapType(leaderTag),node.@startX,node.@startY,node.@endX,node.@endY);
		switch(leaderTag){
			case 'detail' :
				canvas.repeat =node.@repeat;
				canvas.repeatType =node.@repeatType;
				break;
			case 'view' :
				canvas.repeat =node.@repeat;
				canvas.bindingView =node.@bindingView;
				break;
			case 'header' :
				canvas.viewStyle =node.@viewStyle;
				break;
			case 'footer' :
				canvas.viewStyle =node.@viewStyle;
				break;
		}
		parseLeaderChildsNode(xmlList,leaderTag,canvas);
		
	}

}
