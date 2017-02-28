// ActionScript file

import main.flex.cn.myapps.core.printer.components.ContrlolLine;
import main.flex.cn.myapps.core.printer.components.DetailCanvas;
import main.flex.cn.myapps.core.printer.components.FooterCanvas;
import main.flex.cn.myapps.core.printer.components.FormCanvas;
import main.flex.cn.myapps.core.printer.components.HeaderCanvas;
import main.flex.cn.myapps.core.printer.components.ItemTextBox;
import main.flex.cn.myapps.core.printer.components.Line;
import main.flex.cn.myapps.core.printer.components.PageNumber;
import main.flex.cn.myapps.core.printer.components.StaticLabel;
import main.flex.cn.myapps.core.printer.components.View;
import main.flex.cn.myapps.core.printer.components.ViewCanvas;
import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
import main.flex.cn.myapps.core.printer.components.base.MyCanvas;
import main.flex.cn.myapps.core.printer.controls.ChooseSubViewDialog;
import main.flex.cn.myapps.core.printer.controls.DetailDialog;
import main.flex.cn.myapps.core.printer.controls.ViewStyleDialog;
import main.flex.cn.myapps.core.printer.data.C_K_Str;
import main.flex.cn.myapps.core.printer.events.base.BoxEvent;
import main.flex.cn.myapps.core.printer.events.base.BoxKeyboardEvent;
import main.flex.cn.myapps.core.printer.events.base.CanvasEvent;
import main.flex.cn.myapps.core.printer.events.base.IconEvent;

import mx.core.UIComponent;
import mx.managers.PopUpManager;




/**
 *实例化 组件 
 * @param strName
 * @param strType
 * @param x0
 * @param y0
 * @param x1
 * @param y1
 * @return 
 * 

public function addComponent(
		name:String,
		type:String,
		sx:Number,sy:Number,
		ex:Number,ey:Number):ItemDraw{

	var pBox:ItemDraw=null;
	
	switch(type){
		case ItemDraw.C_ItemTextBox:
			pBox= new ItemTextBox(sx,sy,ex,ey,0x000000);
			break;
		
		case ItemDraw.C_StaticLabel:
			pBox= new StaticLabel(sx,sy,ex,ey,0x000000);
			break;
	}
	
	pBox.name = name;
	pBox.contextMenu = controlMenu;//注册菜单
	pBox.parentCanvas =report;
	addEventListeners(pBox);
	//在实例化并添加到画板的组件添加 控制方块
	pBox.drawControlBox(report);
	initControlBox(pBox);//初始化ControlBox
	report.addChild(pBox);
	formItemTreap.insert(new C_K_Str(pBox.name),pBox);//将组件加入平衡树中  储存
	pBox.redraw();
	return pBox;
}
 */
public function addComponent(
		name:String,
		type:String,
		parent:UIComponent,
		sx:Number,sy:Number,
		ex:Number,ey:Number):ItemDraw{

	var pBox:ItemDraw=null;
	var lineColor:uint =0x000000;
	parent is MyCanvas ? lineColor=0x04F0DD : 0x000000;
	switch(type){
		case ItemDraw.C_ItemTextBox:
			pBox= new ItemTextBox(sx,sy,ex,ey,lineColor);
			break;
		
		case ItemDraw.C_StaticLabel:
			pBox= new StaticLabel(sx,sy,ex,ey,lineColor);
			break;
		case ItemDraw.C_Line:
			pBox= new Line(sx,sy,ex,ey,0x000000);
			break;
		case ItemDraw.C_PageNumber:
			pBox= new PageNumber(sx,sy,ex,ey,lineColor);
			break;
		case ItemDraw.C_View:
			pBox= new View(sx,sy,ex,ey,lineColor);
			break;
	}
	
	pBox.name = name;
	pBox.contextMenu = controlMenu;//注册菜单
	pBox.parentCanvas =parent;
	addEventListeners(pBox);
	
	parent.addChild(pBox);
	if(!(pBox is PageNumber))
		initControlBox(pBox);//初始化ControlBox
	if(parent is FormCanvas){
		formItemTreap.insert(new C_K_Str(pBox.name),pBox);//将组件加入平衡树中  储存
	}else if(parent is DetailCanvas){
		detailItemTreap.insert(new C_K_Str(pBox.name),pBox);//将组件加入平衡树中  储存
	}else if(parent is HeaderCanvas){
		headerItemTreap.insert(new C_K_Str(pBox.name),pBox);//将组件加入平衡树中  储存
	}else if(parent is FooterCanvas){
		footerItemTreap.insert(new C_K_Str(pBox.name),pBox);//将组件加入平衡树中  储存
	}
	pBox.redraw();
	return pBox;
}

/*
public function addChildComponent(
		name:String,
		type:String,
		parent:MyCanvas,
		sx:Number,sy:Number,
		ex:Number,ey:Number):ItemDraw{
	
	
	var pBox:ItemDraw=null;
	switch(type){
		case ItemDraw.C_ItemTextBox:
			pBox= new ItemTextBox(sx,sy,ex,ey,0x00FFDD);
			break;
		
		case ItemDraw.C_StaticLabel:
			pBox= new StaticLabel(sx,sy,ex,ey,0x00FFDD);
			break;
	}
	
	pBox.name = name;
	pBox.contextMenu = controlMenu;//注册菜单
	pBox.parentCanvas =parent;
	addEventListeners(pBox);
	//在实例化并添加到画板的组件添加 控制方块
	pBox.drawControlBox(report);
	initControlBox(pBox);//初始化ControlBox
	parent.addChild(pBox);
	if(parent is DetailCanvas){
		detailItemTreap.insert(new C_K_Str(pBox.name),pBox);//将组件加入平衡树中  储存
	}
	
	pBox.redraw();
	return pBox;
}
*/

public function addCanvas(
		name:String,
		type:String,
		sx:Number,sy:Number,
		ex:Number,ey:Number):MyCanvas{
			
	var pCanvas:MyCanvas=null;
	
	switch(type){
		case MyCanvas.C_DetailCanvas:
			pCanvas= new DetailCanvas(sx,sy,ex,ey,0x000000);
			break;
		case MyCanvas.C_FormCanvas:
			pCanvas= new FormCanvas(sx,sy,ex,ey,0x000000);
			//formCount++;
			break;
		case MyCanvas.C_ViewCanvas:
			pCanvas= new ViewCanvas(sx,sy,ex,ey,0x000000);
			break;
		case MyCanvas.C_HeaderCanvas:
			pCanvas= new HeaderCanvas(sx,sy,ex,ey,0x000000);
			headerCount++;
			break;
		case MyCanvas.C_FooterCanvas:
			pCanvas= new FooterCanvas(sx,sy,ex,ey,0x000000);
			footerCount++;
			break;
	}
	
	pCanvas.name = name;
	pCanvas.contextMenu = canvasMenu;
	pCanvas.addEventListener(CanvasEvent.MOUSE_DOWN, onMyCanvasMouseDown);
	pCanvas.addEventListener(CanvasEvent.DOUBLE_CLICK, onMyCanvasDoubleClick);
	initControlLine(pCanvas);
	report.addChild(pCanvas);
	canvasTreap.insert(new C_K_Str(pCanvas.name),pCanvas);//将组件加入平衡树中  储存
	pCanvas.redraw();
	return pCanvas;		
	
}


//初始化ControlBox
public function initControlBox(pBox:ItemDraw):void{
	var pArray:Array=pBox.drawControlBox(report);
	
	for (var i:int=0;i<pArray.length;i++){
		pArray[i].addEventListener(IconEvent.ICON_MOUSE_DOWN, onControlBoxMouseDown);
		pArray[i].addEventListener(IconEvent.ICON_MOUSE_UP, onControlBoxMouseUp);
		pArray[i].addEventListener(IconEvent.ICON_MOVE, onControlBoxMouseMove);
	}
	
}
public function initControlLine(pCanvas:MyCanvas):void{
	var lines:Array=pCanvas.drawControlLine(pCanvas);
	for (var i:int=0;i<lines.length;i++){
		lines[i].addEventListener(IconEvent.ICON_MOUSE_DOWN, onControlLineMouseDown);
		lines[i].addEventListener(IconEvent.ICON_MOUSE_UP, onControlLineMouseUp);
		lines[i].addEventListener(IconEvent.ICON_MOVE, onControlLineMouseMove);
	}
	
}

public function onControlLineMouseDown(event:IconEvent):void
{
	var controlLine: ContrlolLine = event.icon as ContrlolLine;
	var parent:MyCanvas = controlLine.Parent as MyCanvas;
	
	parent.startX_MouseDown=parent.startX;
	parent.startY_MouseDown=parent.startY;
	
	cElement.selectedIndex = -1;
	
	//添加选中效果(发光)
	glow1.target = controlLine;
	glow1.end();
	glow1.play();
	
	//this.clearBorders(); //删除所有图标的边框
	
	controlPressable = true;
	//实施拖动
	controlLine.startDrag();
	
	this.pDragControl=controlLine;
	
}

public function onControlLineMouseMove(event:IconEvent):void
{
	
	if(event.target is ContrlolLine && 
		event.target==this.pDragControl ){
		var controlLine: ContrlolLine = event.icon as ContrlolLine;
		var parent:MyCanvas = controlLine.Parent as MyCanvas;
		controlLine.x=0;
		parent.x=0;
		
		var i:int=0;
		var line1:ContrlolLine =parent.controlLine[0];
		var line2:ContrlolLine =parent.controlLine[1];
		if(controlLine.name=="1"){
			if(controlLine.y+controlLine.startY+30>=line2.y+line2.startY){
				controlLine.stopDrag();
				controlLine.y =line2.y+line2.startY-30-controlLine.startY;
			}
				
				
		}else if(controlLine.name=="2"){
			if(controlLine.y+controlLine.startY-30<=line1.y+line1.startY){
				controlLine.stopDrag();
				controlLine.y =line1.y+line1.startY+30-controlLine.startY;;
			}
		}
		
		
		if (parent!=null){
			parent.reSizeByControlLine();
		}
	}
	
	
	
	
	
	
}

public function onControlLineMouseUp(event:IconEvent):void
{
	var controlLine: ContrlolLine = event.icon as ContrlolLine;
	//var parent:MyCanvas = controlLine.Parent as MyCanvas;
	controlLine.stopDrag();
}
	
public function onMyCanvasDoubleClick(event:CanvasEvent):void
{
	if (!bdc){
	if(event.pObj is HeaderCanvas || event.pObj is FooterCanvas){
		var canvas:MyCanvas = MyCanvas(event.pObj);
		var viewStyleDialog:ViewStyleDialog = new ViewStyleDialog();
		viewStyleDialog.canvas =canvas;
		PopUpManager.addPopUp(viewStyleDialog,this,true);
		PopUpManager.centerPopUp(viewStyleDialog);
	}else if(event.pObj is DetailCanvas){
		var detail:DetailCanvas = DetailCanvas(event.pObj);
		var detailDialog:DetailDialog = new DetailDialog();
		detailDialog.canvas = detail;
		PopUpManager.addPopUp(detailDialog,this,true);
		PopUpManager.centerPopUp(detailDialog);
	}
	else if(event.pObj is ViewCanvas){
		var view:ViewCanvas = ViewCanvas(event.pObj);
		var chooseView:ChooseSubViewDialog = new ChooseSubViewDialog();
		var _formid:String =  ExternalInterface.call("doGetFormid");
		if(_formid !=this.formid){
			getSubViewList();
		}
		chooseView.views = subViewList;
		chooseView.viewCanvas = view;
		PopUpManager.addPopUp(chooseView,this,true);
		PopUpManager.centerPopUp(chooseView);
	}
	}
	bdc =false;
}
/**
 *取消 组件 的事件监听 
 * @param pBox
 * 
 */
public function addEventListeners(pBox:ItemDraw):void
{
	pBox.addEventListener(BoxEvent.MOUSE_DOWN, on_Box_MouseDown);
	pBox.addEventListener(BoxEvent.MOUSE_UP, on_Box_MouseUp);
	pBox.addEventListener(BoxEvent.MOUSE_MOVE, on_Box_MouseMove);
	pBox.addEventListener(BoxEvent.DOUBLE_CLICK, on_Box_DoubleClick);
	pBox.addEventListener(BoxKeyboardEvent.KEY_DOWN,on_Box_KeyDown);
}


/**
 *取消 组件 的事件监听 
 * @param pBox
 * 
 */
public function removeEventListeners(pBox:ItemDraw):void
{
	pBox.removeEventListener(BoxEvent.MOUSE_DOWN, on_Box_MouseDown);
	pBox.removeEventListener(BoxEvent.MOUSE_UP, on_Box_MouseUp);
	pBox.removeEventListener(BoxEvent.MOUSE_MOVE, on_Box_MouseMove);
	pBox.removeEventListener(BoxEvent.DOUBLE_CLICK, on_Box_DoubleClick);
	pBox.removeEventListener(BoxKeyboardEvent.KEY_DOWN,on_Box_KeyDown);
}
	

