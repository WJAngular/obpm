// ActionScript file

import flash.events.MouseEvent;
import flash.geom.Point;

import main.flex.cn.myapps.core.printer.components.DetailCanvas;
import main.flex.cn.myapps.core.printer.components.FooterCanvas;
import main.flex.cn.myapps.core.printer.components.FormCanvas;
import main.flex.cn.myapps.core.printer.components.HeaderCanvas;
import main.flex.cn.myapps.core.printer.components.ItemControl;
import main.flex.cn.myapps.core.printer.components.ItemTextBox;
import main.flex.cn.myapps.core.printer.components.Line;
import main.flex.cn.myapps.core.printer.components.ReportCanvas;
import main.flex.cn.myapps.core.printer.components.StaticLabel;
import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
import main.flex.cn.myapps.core.printer.components.base.MyCanvas;
import main.flex.cn.myapps.core.printer.controls.ChooseFieldDialog;
import main.flex.cn.myapps.core.printer.data.C_K_Str;
import main.flex.cn.myapps.core.printer.data.Treap;
import main.flex.cn.myapps.core.printer.data.TreapEnumerator;
import main.flex.cn.myapps.core.printer.events.base.BoxEvent;
import main.flex.cn.myapps.core.printer.events.base.BoxKeyboardEvent;
import main.flex.cn.myapps.core.printer.events.base.CanvasEvent;
import main.flex.cn.myapps.core.printer.events.base.IconEvent;

import mx.core.UIComponent;
import mx.managers.PopUpManager;


/**
 * 鼠标点击画布时,画出一个图标,如果工具栏没有选中任何工具，则清除选择
 * @param event
 * 
 */
internal function onReportCanvasMouseDown(event: MouseEvent): void{
	//如果用户点击的是画布,而不是按钮冒泡过来的事件
	
	if(event.target is ReportCanvas && event.target ==event.currentTarget){
		//1，清除所有组件控制方块
		this.hideControlBox(formItemTreap);
		//2,实例化所选组件
		//3.将实例化的组件加入画板   see  addNewItem.as 的 addNewItem(...args)方法
		//4.在实例化的组件上加控制方块
		if(cCanvas !=null && cCanvas.selectedItem != null){
			//定位图标
			var p: Point = new Point();
			p = report.globalToLocal(new Point(mouseX, mouseY));//转换坐标
			p.x=Math.round(p.x/10)*10;
			p.y=Math.round(p.y/10)*10;
			
			switch(cCanvas.selectedItem.label){
//				case '选择':
//					//清除所有图标的边框
//					//this.clearBorders();
//					break;
//				case 'Field':
//					pSelectedItem=addComponent(
//						"Field" + Utils.randomString(6),
//						ItemDraw.C_ItemTextBox,
//						report,
//						p.x,p.y,p.x+80,p.y+25);
//					break;
//				
//				case 'Label':
//					pSelectedItem=addComponent(
//						"Label" + Utils.randomString(6),
//						ItemDraw.C_StaticLabel,
//						report,
//						p.x,p.y,p.x+80,p.y+25);
//					break;
//				case 'Line':
//					pSelectedItem=addComponent(
//						"Line" + Utils.randomString(6),
//						ItemDraw.C_Line,
//						report,
//						p.x,p.y,p.x+100,p.y);
//					break;
				case 'Detail':
					pSelectedCanvas=addCanvas(
						"Detail" + Utils.randomString(6),
						MyCanvas.C_DetailCanvas,
						0,p.y,report.width,p.y+80);
					break;
				case 'Form':
					//if(formCount<1){
						pSelectedCanvas=addCanvas(
							"Form" + Utils.randomString(6),
							MyCanvas.C_FormCanvas,
							0,p.y,report.width,p.y+80);
					//}
					break;
					case 'View':
						pSelectedCanvas=addCanvas(
							"View" + Utils.randomString(6),
							MyCanvas.C_ViewCanvas,
							0,p.y,report.width,p.y+50);
					break;
				case 'Header':
					if(headerCount<1){
						pSelectedCanvas=addCanvas(
							"Header" + Utils.randomString(6),
							MyCanvas.C_HeaderCanvas,
							0,p.y,report.width,p.y+80);
					}
					break;
				case 'Footer':
					if(footerCount<1){
						pSelectedCanvas=addCanvas(
							"Footer" + Utils.randomString(6),
							MyCanvas.C_FooterCanvas,
							0,p.y,report.width,p.y+80);
					}
					break;
				
			}
			reSetControlsPanel(); //重新选择第一个,不然老是添加了.
			if(pSelectedCanvas !=null){
				this.setProperties(pSelectedCanvas);
			}
			initTree();
		}
	}
 }                 
     

internal function onMyCanvasMouseDown(event: CanvasEvent): void{
	var parent:MyCanvas; 
	if(event.target is MyCanvas && event.target ==event.currentTarget){
		this.hideControlBox(formItemTreap);
		hideControlBox(detailItemTreap);
		hideControlBox(headerItemTreap);
		hideControlBox(footerItemTreap);
		pSelectedCanvas =event.target as MyCanvas;
		parent= MyCanvas(event.pObj);
		//定位图标
		var p: Point = new Point();
		p = parent.globalToLocal(new Point(mouseX, mouseY));//转换坐标
		p.x=Math.round(p.x/10)*10;
		p.y=Math.round(p.y/10)*10;
		if(cElement !=null && cElement.selectedItem != null){
		
		//if(parent is FormCanvas || parent is DetailCanvas){
			switch(cElement.selectedItem.label){
				case '选择':
					break;
				case 'Field':
					pSelectedItem=addComponent(
						"Field" + Utils.randomString(6),
						ItemDraw.C_ItemTextBox,
						parent,
						p.x,p.y,p.x+80,p.y+25);
					break;
				
				case 'Label':
					pSelectedItem=addComponent(
						"Label" + Utils.randomString(6),
						ItemDraw.C_StaticLabel,
						parent,
						p.x,p.y,p.x+80,p.y+25);
					break;
				case 'Line':
					pSelectedItem=addComponent(
						"Line" + Utils.randomString(6),
						ItemDraw.C_Line,
						parent,
						p.x,p.y,p.x+100,p.y);
					break;
	//			case 'View':
	//				pSelectedItem=addComponent(
	//					"View" + Utils.randomString(6),
	//					ItemDraw.C_View,
	//					parent,
	//					p.x,p.y,p.x+180,p.y+25);
	//				break;
					
				
			}
			initTree();
			this.setProperties(pSelectedItem);
		//}else
		
		}else if(cTools !=null && cTools.selectedItem != null){
			switch(cTools.selectedItem.label){
				case 'PageNumber':
					if(parent is FooterCanvas){
						pSelectedItem=addComponent(
						"PageNumber" + Utils.randomString(6),
						ItemDraw.C_PageNumber,
						parent,
						p.x,p.y,p.x+17,p.y+16);
					}
					
					break;
			}
			initTree();
			this.setProperties(pSelectedItem);
		}else if(!bc){
				this.setProperties(pSelectedCanvas);
			}
		
		reSetControlsPanel();
		bc =false;
	}
}

/**
 * 画板上的组件 的鼠标按下事件函数
 */
internal function on_Box_MouseDown(event:BoxEvent): void{
	//隐藏当前显示的控制方块
	bc =true;
	var pBox:ItemDraw = ItemDraw(event.pObj);
	var parent:UIComponent = pBox.parentCanvas;
	if(parent is FormCanvas){
		hideControlBox(formItemTreap);
	}else if(parent is DetailCanvas){
		hideControlBox(detailItemTreap);
	}else if(parent is HeaderCanvas){
		hideControlBox(headerItemTreap);
	}else if(parent is FooterCanvas){
		hideControlBox(footerItemTreap);
	}
	
	pOldSelectedItem=pSelectedItem;
	pSelectedItem=pBox;//代表选择这个Item
	pSelectedCanvas==null;
	reSetControlsPanel();
	
	//添加选中效果(发光)
	glow1.target = pBox;//将效果指定到图标上(编程时不是为图标设置效果)
	glow1.end();
	glow1.play();
	
	//this.clearBorders(); //删除所有图标的边框
	
	controlPressable = true;
	//实施拖动
	pBox.startDrag();
	//var w:uint = pBox.endX-pBox.startX;
	//var h:uint = pBox.endY-pBox.startY;
	//pBox.startDrag(false,new Rectangle(pBox.x-pBox.startX,pBox.y-pBox.startY,report.width-w,report.height-h-30));
	//设置属性对话框
	this.setProperties(pBox);
}

/**
 * 画板上的组件 的鼠标移动事件函数
 */
internal function on_Box_MouseMove(event: BoxEvent): void{
	var pBox:ItemDraw = ItemDraw(event.pObj);
	var parent:UIComponent = pBox.parentCanvas;
	var x:uint = pBox.startX+pBox.x;
	var y:uint = pBox.startY+pBox.y;
	var w:uint = pBox.endX-pBox.startX;
	var h:uint = pBox.endY-pBox.startY;
	if(pSelectedItem==pBox){
		if(parent is ReportCanvas){
			
			if(x>40000000){
				pBox.stopDrag();
				pBox.x = 0-pBox.startX;
			}else if(x+w>report.width){
				pBox.stopDrag();
				pBox.x = report.width-w-pBox.startX;
			}
			if(y>4000000){
				pBox.stopDrag();
				//pBox.y = 0+pBox.startY;
			}else if(y+h>report.height){
				pBox.stopDrag();
				pBox.y = report.height-h-pBox.startY;
			}
			this.bDirty=true;
		}else if(parent is MyCanvas){
			//var pw:Number = 
			var ph:Number =(parent as MyCanvas).endY-(parent as MyCanvas).startY;
			if(x>40000000){
				pBox.stopDrag();
				pBox.x = 0-pBox.startX;
			}else if(x+w>report.width){
				pBox.stopDrag();
				pBox.x = report.width-w-pBox.startX;
			}
			if(y<(parent as MyCanvas).startY+parent.y){
				pBox.stopDrag();
				pBox.y = (parent as MyCanvas).startY+parent.y-pBox.startY;
			}else if(y+h>(parent as MyCanvas).startY+parent.y+ph){
				pBox.stopDrag();
				pBox.y = (parent as MyCanvas).startY+parent.y+ph-h-pBox.startY;
			}
		}
	
	}
}




/**
 * 画板上的组件 的双击事件函数
 */
internal function on_Box_DoubleClick(event: BoxEvent): void{
	bdc = true;
	if(event.target is ItemDraw){
	if(event.pObj is ItemTextBox){
		var pBox:ItemTextBox = ItemTextBox(event.pObj);
		
		var _formid:String =  ExternalInterface.call("doGetFormid");
		if(_formid !=this.formid){
			getFieldList();
		}
			
		var chooseFiled:ChooseFieldDialog = new ChooseFieldDialog();
		chooseFiled.pBox =pBox;
		//Alert.show(fieldList.toString());
		chooseFiled.fields = fieldList;
		PopUpManager.addPopUp(chooseFiled,this,true);
		PopUpManager.centerPopUp(chooseFiled);
	}else if(event.pObj is StaticLabel){
		var pLabel:StaticLabel = StaticLabel(event.pObj);
		pLabel.mouseChildren=true;
		pLabel.textInput.editable =true;
    	pLabel.textInput.setFocus();
    	//this.removeEventListeners(pLabel);
		//pLabel.textarea.text.
	}
	}
	/*
	else if(event.pObj is View){
		var pView:View = View(event.pObj);
		var _formid:String =  ExternalInterface.call("doGetFormid");
		if(_formid !=this.formid){
			getSubViewList();
		}
			
		var chooseSubView:ChooseSubViewDialog = new ChooseSubViewDialog();
		chooseSubView.pBox =pView;
		chooseSubView.views = subViewList;
		PopUpManager.addPopUp(chooseSubView,this,true);
		PopUpManager.centerPopUp(chooseSubView);
	}
	*/
	
	
}


/**
 * 画板上的组件 的鼠标弹起事件函数
 */
internal function on_Box_MouseUp(event: BoxEvent): void{
	var pBox:ItemDraw = ItemDraw(event.pObj);
	glow2.target = pBox;
	glow2.end();
	glow2.play();
	
	controlPressable = false;
	
	//停止拖动
	pBox.stopDrag();
	//将隐藏的控制方块显示
	pBox.displayeControlBox();
	
	var x0:Number=Math.round(pBox.x/10)*10;
	var y0:Number=Math.round(pBox.y/10)*10;
	var dx:Number=x0-pBox.startX;
	var dy:Number=y0-pBox.startY;
	pBox.x=x0;
	pBox.y=y0;
	pSelectedItem = null;
}
/**
 * 画板上的组件 的Key_down事件函数
 * @param event
 * 
 */
internal function on_Box_KeyDown(event:BoxKeyboardEvent): void{
	var pBox:ItemDraw = ItemDraw(event.pObj);
	if(event.charCode==127){
		removeItemBox(pBox,pBox.parentCanvas);//删除 组件
//		pBox.hideControlBox();
//		report.removeChild(pBox);
//		pTreapItem.remove(new C_K_Str(pBox.name));
//		properties.removeAll();
	}
}



/**
 *从画板删除 指定组件 
 * @param pBox
 * 
 */
public function removeItemBox(pBox:ItemDraw,parent:UIComponent):void
{
	//删除 控制方块
	this.removeControlBox(pBox.pControlPoints);
	//删除事件
	pBox.removeEventListener(BoxEvent.MOUSE_DOWN, on_Box_MouseDown);
	pBox.removeEventListener(BoxEvent.MOUSE_UP, on_Box_MouseUp);
	pBox.removeEventListener(BoxEvent.MOUSE_MOVE, on_Box_MouseMove);
	pBox.removeEventListener(BoxEvent.DOUBLE_CLICK, on_Box_DoubleClick);
	pBox.removeEventListener(BoxKeyboardEvent.KEY_DOWN,on_Box_KeyDown);
	parent.removeChild(pBox); //删除Item
	//删除Treap中的元素
	if(parent is FormCanvas){
	formItemTreap.remove(new C_K_Str(pBox.name));
	}else if(parent is DetailCanvas){
		detailItemTreap.remove(new C_K_Str(pBox.name));
	}
	
	//清空属性对话框
	properties.removeAll();
	pSelectedItem =null;
	initTree();
}

/**
 *删除画布的 Canvas 组件 
 * @param pCanvas
 * 
 */
public function removeItemCanvas(pCanvas:MyCanvas):void
{
	
	report.removeChild(pCanvas); //删除Item
	//删除Treap中的元素
	//formItemTreap.remove(new C_K_Str(pBox.name));
	properties.removeAll();
	pSelectedItem =null;
	initTree();
	
}

/**
 *删除 组件上的 控制方块 
 * @param controlBoxs
 * 
 */
private function removeControlBox(controlBoxs:Array): void{
	var controlBox: ItemControl;
	
	for(var i:int=0;i<controlBoxs.length;i++){
		controlBox = ItemControl(controlBoxs[i]);
		report.removeChild(controlBox); 
		pTreapControl.remove(new C_K_Str(controlBox.name));
	}
}




/**
 * 组件上的控制方块 MouseDown 事件 函数体
 * @param event
 * 
 */
internal function onControlBoxMouseDown(event: IconEvent): void{
	var pControl: ItemControl = ItemControl(event.icon);
	var pItemParent:ItemDraw = ItemDraw(pControl.Parent);
	
	pItemParent.startX_MouseDown=pItemParent.startX;
	pItemParent.startY_MouseDown=pItemParent.startY;
	
	reSetControlsPanel();
	
	//添加选中效果(发光)
	glow1.target = pControl;//将效果指定到图标上(编程时不是为图标设置效果)
	glow1.end();
	glow1.play();
	
	//this.clearBorders(); //删除所有图标的边框
	
	controlPressable = true;
	//实施拖动
	pControl.startDrag();
	
	this.pDragControl=pControl;
	this.bDirty=true;
}


/**
 *组件上的控制方块 MouseUp 事件 函数体 
 * @param event
 * 
 */
internal function onControlBoxMouseUp(event: IconEvent): void{
	var pControl: ItemControl = ItemControl(event.icon);
	glow2.target = pControl;
	glow2.end();
	glow2.play();
	
	controlPressable = false;
	
	//停止拖动
	pControl.stopDrag();
	if(!(pItemParent is Line)){
	//网格对齐功能。
	if (pControl.DrawPosType=="From"){
		var x0:Number=Math.round(pControl.x/10)*10;
		var y0:Number=Math.round(pControl.y/10)*10;
		var dx:Number=x0-pControl.x;
		var dy:Number=y0-pControl.y;
		pControl.x=x0;
		pControl.y=y0;
		var pItemParent:ItemDraw=ItemDraw(pControl.Parent);
		for (var i:int=1;i<pItemParent.pControlPoints.length;i++)
		{
			var pControl2:ItemControl=ItemControl(pItemParent.pControlPoints[i]);
			pControl2.x=pControl2.x+dx;
			pControl2.y=pControl2.y+dy;
		}
	}else{
		x0=Math.round(pControl.x/10)*10;
		pControl.x=Math.round(pControl.x/10)*10;
		pControl.y=Math.round(pControl.y/10)*10;
	}
	}
}


/**
 *组件上的控制方块 MouseMove 事件 函数体  
 * @param event
 * 
 */
internal function onControlBoxMouseMove(event: IconEvent): void{
	
	if(event.target is ItemControl && 
		event.target==this.pDragControl ){
		
		var pControl:ItemControl=ItemControl(event.icon);
		var dw:Number =40;
		var dh:Number = 25;
		var i:int=0;
		var pItemParent:ItemDraw=ItemDraw(pControl.Parent);
		var pc1:ItemControl =pItemParent.pControlPoints[0];
		var pc2:ItemControl =pItemParent.pControlPoints[1];
		if(pItemParent is Line){
			dw=0;
			dh=0;
		}
		if(pControl.name=="1"){
			if(pControl.x+dw>pc2.x){
				pControl.stopDrag();
				pControl.x =pc2.x-dw;
				
			}if(pControl.y+dw>pc2.y){
				pControl.stopDrag();
				pControl.y =pc2.y-dw;
			}
				
				
		}else if(pControl.name=="2"){
			if(pControl.x-dh<pc1.x){
				pControl.stopDrag();
				pControl.x =pc1.x+dh;
				
			}if(pControl.y-dh<pc1.y){
				pControl.stopDrag();
				pControl.y =pc1.y+dh;
			}
		}
		
		if (pItemParent!=null){
			pItemParent.readFromControlBox();
		}
	}
}


//隐藏 面板所有 组件 的 控制方块(ControlBox)
private function hideControlBox(itemTreap:Treap):void
{
	var i:int=0;
	var pBox:ItemDraw;
	var p:TreapEnumerator=itemTreap.Elements(true);//get 储存在 平衡树 Treap 里的 组件(TreapNode)枚举对象
	while(p.HasMoreElements()){
		pBox=ItemDraw(p.NextElement());
		pBox.hideControlBox();
	}
}

private function reSetControlsPanel():void{
	if(cElement !=null)
		cElement.selectedIndex = -1; //重新选择第一个,不然老是添加了.
	if(cCanvas !=null)
		cCanvas.selectedIndex = -1
	if(cTools !=null)
		cTools.selectedIndex = -1
}


private function initTree():void
{
	treeNodeStr="";
	getXMLContent();
	tree1.dataProvider=XML("<node name='root'>"+treeNodeStr+"</node>").node;
	tree1.validateNow();
	expandTree(tree1.dataProvider);   //展开树
	
}

private function expandTree(tData:Object):void
{   
    for each (var item:Object in tData) { 
    	tree1.expandChildrenOf(item,true); 
    } 
     
}  
	