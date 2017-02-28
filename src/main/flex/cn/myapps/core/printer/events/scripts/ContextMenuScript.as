// ActionScript file
import main.flex.cn.myapps.core.printer.components.FooterCanvas;
import main.flex.cn.myapps.core.printer.components.FormCanvas;
import main.flex.cn.myapps.core.printer.components.HeaderCanvas;
import main.flex.cn.myapps.core.printer.components.ViewCanvas;
import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
import main.flex.cn.myapps.core.printer.components.base.MyCanvas;
import main.flex.cn.myapps.core.printer.data.C_K_Str;
import main.flex.cn.myapps.core.printer.data.Treap;

import mx.events.CloseEvent;

/**
 *初始化 右键菜单 
 * 
 */
private function initContextMenu():void
{
	initControlMenu();
	initReportMenu();
	initCanvasMenu();
}

/**
 *初始化 组件 右键菜单 
 * 
 */
private function initControlMenu():void
{
	controlMenu = new ContextMenu();
	controlMenu.hideBuiltInItems();
	var deleteItem: ContextMenuItem = new ContextMenuItem("删除");
	controlMenu.customItems.push(deleteItem);
	deleteItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, 
		function(e: ContextMenuEvent): void{
			var pBox:ItemDraw = ItemDraw(e.contextMenuOwner);
			removeItemBox(pBox,pBox.parentCanvas);
		});
}

/**
 *初始化 Canvas组件 右键菜单 
 * 
 */
private function initCanvasMenu():void
{
	canvasMenu = new ContextMenu();
	canvasMenu.hideBuiltInItems();
	var deleteItem: ContextMenuItem = new ContextMenuItem("删除");
	canvasMenu.customItems.push(deleteItem);
	deleteItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, 
		function(e: ContextMenuEvent): void{
			var pCanvas:MyCanvas = MyCanvas(e.contextMenuOwner);
			var p:TreapEnumerator =null;
			if(pCanvas is DetailCanvas){
				p=detailItemTreap.Elements(true);
			}else if(pCanvas is FormCanvas){
				p=formItemTreap.Elements(true);
				formCount =0;
			}else if(pCanvas is HeaderCanvas){
				p=headerItemTreap.Elements(true);
				headerCount =0;
			}else if(pCanvas is FooterCanvas){
				p=footerItemTreap.Elements(true);
				footerCount =0;
			}
			if(p !=null){
				while(p.HasMoreElements()){
					var pBox:ItemDraw=ItemDraw(p.NextElement());
					if(pBox.parentCanvas.name ==pCanvas.name ){
						removeItemBox(pBox,pBox.parentCanvas);
					}
					
				}
			}
			canvasTreap.remove(new C_K_Str(pCanvas.name));
			removeItemCanvas(pCanvas);
		});
}

/**
 *实例化 画布右键菜单 
 * 
 */
private function initReportMenu():void
{
		reportMenu = new ContextMenu();
		reportMenu.hideBuiltInItems();
		var pageProperty:ContextMenuItem = new ContextMenuItem("页面属性");
		var cleanCanvar:ContextMenuItem = new ContextMenuItem("清空画板");
		pageProperty.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT,
			function(e: ContextMenuEvent): void{

				
		});
		cleanCanvar.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT,cleanCanvas);
		reportMenu.customItems.push(pageProperty);
		reportMenu.customItems.push(cleanCanvar);
		report.contextMenu = reportMenu;
}

/**
 * 清空画板上的所有元素 
 * @param event
 * 
 */
public function cleanCanvas(event:Event):void
{
	var msg:String ="确定清空画板吗？";
	var title:String = "提示";
	var alert:Alert = Alert.show(msg,title,Alert.YES|Alert.NO,null,
	function(e: CloseEvent): void{
		 if (e.detail==Alert.YES){
		 	report.removeAllChildren();
		 	pTreapControl = new Treap();
			canvasTreap= new Treap();
			formItemTreap = new Treap();
			detailItemTreap = new Treap();
			headerItemTreap = new Treap();
			footerItemTreap = new Treap();
			
			formCount =0;
			headerCount =0;
			footerCount =0;
		 	/*
		 	var p:TreapEnumerator=formItemTreap.Elements(true);//get 储存在 平衡树 Treap 里的 组件(TreapNode)枚举对象
			while(p.HasMoreElements()){
				var pBox:ItemDraw=ItemDraw(p.NextElement());
				removeItemBox(pBox,pBox.parentCanvas);
			}
			*/
		 }
	});
}