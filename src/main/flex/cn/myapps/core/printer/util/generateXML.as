// ActionScript file
import main.flex.cn.myapps.core.printer.components.DetailCanvas;
import main.flex.cn.myapps.core.printer.components.FooterCanvas;
import main.flex.cn.myapps.core.printer.components.FormCanvas;
import main.flex.cn.myapps.core.printer.components.HeaderCanvas;
import main.flex.cn.myapps.core.printer.components.ViewCanvas;
import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
import main.flex.cn.myapps.core.printer.components.base.MyCanvas;
import main.flex.cn.myapps.core.printer.data.TreapEnumerator;



/**
 *生成XML 
 * 
 */
public function generateXML():void
{
	var strXML:String=this.getXMLContent();
	showXMLSource(strXML);
}
/**
 *获取XML内容
 * 根据 当前 平衡树储存的控件 逐个调出解析成xml 
 * @return 
 * 
 */
private function getXMLContent():String{//属性和控件类型 有待完善
	var strXML: String = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
			"<report  ";
	strXML+="paperFormat=\""+this.paperFormat.label+"\"";		
	strXML+=">\r\n"
	
	//strXML+=generateFormNode();
	
	
	var canvases:TreapEnumerator=canvasTreap.Elements(true);
	while(canvases.HasMoreElements()){
		var canvas:MyCanvas=MyCanvas(canvases.NextElement());
		strXML+=generateLeaderNode(canvas);
	}
	
	strXML += "</report>";
	return strXML;
}

/**
 *生成 form元素XML 节点
 * @return 
 * 
 */
private function generateFormNode():String{
	var formNode:String ="<form>\n\r";
	var items:TreapEnumerator=formItemTreap.Elements(true);
	while(items.HasMoreElements()){
		var pBox:ItemDraw=ItemDraw(items.NextElement());
		var strTag:String=ItemDraw.getTag(pBox);
		switch (strTag){
			case "textbox":
				formNode+=generateFieldNode(pBox,strTag);
				break;
			case "staticLabel":
				formNode+=generateStaticLabelNode(pBox,strTag);
				break;
			case "line":
				formNode+=generateLineNode(pBox,strTag);
				break;
		}
		
	}
		
	return formNode+'\r</form>\r\n';
}



/**
 *生成 ROOT的子节点XML 
 * @param canvas
 * @return 
 * 
 */
private function generateLeaderNode(canvas:MyCanvas):String{
	var canvasNode:String = "";
	var tag:String ="";
	var items:TreapEnumerator =null;
	var cName:String =canvas.name;
	if(canvas is DetailCanvas){
		tag ="detail"
		canvasNode=generateCanvasNode(tag,canvas);
		items= detailItemTreap.Elements(true);
	}else if(canvas is FormCanvas){
		tag ="form"
		canvasNode=generateCanvasNode(tag,canvas);
		items= formItemTreap.Elements(true);
	}else if(canvas is ViewCanvas){
		tag ="view"
		canvasNode=generateCanvasNode(tag,canvas);
	}else if(canvas is HeaderCanvas){
		tag ="header"
		canvasNode=generateCanvasNode(tag,canvas);
		items= headerItemTreap.Elements(true);
	}
	else if(canvas is FooterCanvas){
		tag ="footer"
		canvasNode=generateCanvasNode(tag,canvas);
		items= footerItemTreap.Elements(true);
	}
	treeNodeStr+="<node name='"+canvas.name+"'>";
	if(items !=null){
		while(items.HasMoreElements()){
			var pBox:ItemDraw=ItemDraw(items.NextElement());
			if(cName !=pBox.parentCanvas.name)
				continue;
			var strTag:String=ItemDraw.getTag(pBox);
			treeNodeStr+="<node name='"+pBox.name+"'/>";
			switch (strTag){
				case "textbox":
					canvasNode+=generateFieldNode(pBox,strTag);
					break;
				case "staticLabel":
					canvasNode+=generateStaticLabelNode(pBox,strTag);
					break;
				case "pageNumber":
					canvasNode+=generatePageNumberNode(pBox,strTag);
					break;
				case "line":
					canvasNode+=generateLineNode(pBox,strTag);
					break;
				case "view":
					canvasNode+=generateViewNode(pBox,strTag);
					break;
			}
			
		}
	}
	treeNodeStr+="</node>";	
	return canvasNode+"\r</"+tag+">\r\n";
}

/**
 * 生成 重复区域XML 
 * @param tag
 * @param canvas
 * @return 
 * 
 */
private function generateCanvasNode(tag:String,canvas:MyCanvas):String{
	var canvasNode:String ="";
	if(tag=="detail"){
		canvasNode = "<"+tag+
				 " name=\""+canvas.name +
				 "\" repeatType=\""+canvas.repeatType +
				 "\" repeat=\""+canvas.repeat +
				 "\" startX=\"" + (canvas.startX+canvas.x) + 
				 "\" startY=\"" +  (canvas.startY+canvas.y) +
				 "\" endX=\"" + (canvas.endX+canvas.x) +
				 "\" endY=\"" + (canvas.endY+canvas.y) +
				 "\" >\n\r";
	}else if(tag=="header" ||tag=="footer"){
		canvasNode = "<"+tag+
				 " name=\""+canvas.name +
				 "\" viewStyle=\""+canvas.viewStyle +
				 "\" startX=\"" + (canvas.startX+canvas.x) + 
				 "\" startY=\"" +  (canvas.startY+canvas.y) +
				 "\" endX=\"" + (canvas.endX+canvas.x) +
				 "\" endY=\"" + (canvas.endY+canvas.y) +
				 "\" >\n\r";
	}else if(tag=="view"){
		canvasNode = "<"+tag+
				 " name=\""+canvas.name +
				 "\" bindingView=\""+canvas.bindingView +
				 "\" repeat=\""+canvas.repeat +
				 "\" startX=\"" + (canvas.startX+canvas.x) + 
				 "\" startY=\"" +  (canvas.startY+canvas.y) +
				 "\" endX=\"" + (canvas.endX+canvas.x) +
				 "\" endY=\"" + (canvas.endY+canvas.y) +
				 "\" >\n\r";
	}else{
		canvasNode = "<"+tag+
					 " name=\""+canvas.name +
					 "\" startX=\"" + (canvas.startX+canvas.x) + 
					 "\" startY=\"" +  (canvas.startY+canvas.y) +
					 "\" endX=\"" + (canvas.endX+canvas.x) +
					 "\" endY=\"" + (canvas.endY+canvas.y) +
					 "\" >\n\r";
	}
				 
	return canvasNode;
}

/**
 *生成 field 组件节点xml 
 * @param field
 * @param strTag
 * @return 
 * 
 */
private function generateFieldNode(field:ItemDraw,strTag:String):String{
	var fieldNode:String ="";
	fieldNode = "    <" +strTag + 
				" name=\"" +  field.name +
				"\" label=\"" + field.label +
				"\" bindingField=\"" + field.bindingField +
				"\" fillColor=\"" + field.fillColor.toString(16) +
				"\" startX=\"" + (field.startX+field.x) + 
				"\" startY=\"" +  (field.startY+field.y) +
				"\" endX=\"" + (field.endX+field.x) +
				"\" endY=\"" + (field.endY+field.y) +
				"\" strText=\"" + field.textarea.text+"\"></" +strTag+">\n";
	
	return fieldNode;
}

/**
 *生成 staticLabel组件节点xml 
 * @param label
 * @param strTag
 * @return 
 * 
 */
private function generateStaticLabelNode(label:ItemDraw,strTag:String):String{
	var staticLabelNode:String ="";
	staticLabelNode = "    <" +strTag + 
				" name=\"" +  label.name +
				"\" label=\"" + label.label +
				"\" text=\"" + label.textInput.text +
				"\" fillColor=\"" + label.fillColor.toString(16) +
				"\" startX=\"" + (label.startX+label.x) + 
				"\" startY=\"" +  (label.startY+label.y) +
				"\" endX=\"" + (label.endX+label.x) +
				"\" endY=\"" + (label.endY+label.y) +
				"\" strText=\"" + label.textInput.text+"\"></" +strTag+">\n";
	
	return staticLabelNode;
}

/**
 *生成 Line组件节点xml 
 * @param label
 * @param strTag
 * @return 
 * 
 */
private function generateLineNode(line:ItemDraw,strTag:String):String{
	var lineNode:String ="";
	lineNode = "    <" +strTag + 
				" name=\"" +  line.name +
				"\" thickness=\"" + line.thickness +
				"\" lineColor=\"" + line.lineColor +
				"\" startX=\"" + (line.startX+line.x) + 
				"\" startY=\"" +  (line.startY+line.y) +
				"\" endX=\"" + (line.endX+line.x) +
				"\" endY=\"" + (line.endY+line.y) +
				"\"></" +strTag+">\n";
	
	return lineNode;
}
/**
 * 生成 PageNumber组件节点xml 
 * @param line
 * @param strTag
 * @return 
 * 
 */
private function generatePageNumberNode(pageNumber:ItemDraw,strTag:String):String{
	var pageNumberNode:String ="";
	pageNumberNode = "    <" +strTag + 
				" name=\"" +  pageNumber.name +
				"\" startX=\"" + (pageNumber.startX+pageNumber.x) + 
				"\" startY=\"" +  (pageNumber.startY+pageNumber.y) +
				"\" endX=\"" + (pageNumber.endX+pageNumber.x) +
				"\" endY=\"" + (pageNumber.endY+pageNumber.y) +
				"\"></" +strTag+">\n";
	
	return pageNumberNode;
}

/**
 * 生成 View组件节点xml 
 * @param line
 * @param strTag
 * @return 
 * 
 */
private function generateViewNode(view:ItemDraw,strTag:String):String{
	var viewNode:String ="";
	viewNode = "    <" +strTag + 
				" name=\"" +  view.name +
				" bindView=\"" +  view.bindingView +
				"\" startX=\"" + (view.startX+view.x) + 
				"\" startY=\"" +  (view.startY+view.y) +
				"\" endX=\"" + (view.endX+view.x) +
				"\" endY=\"" + (view.endY+view.y) +
				"\"></" +strTag+">\n";
	
	return viewNode;
}