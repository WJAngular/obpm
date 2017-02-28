package cn.myapps.oreport.tree
{
	import flash.display.DisplayObjectContainer;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.controls.LinkButton;
	import mx.controls.Menu;
	import mx.controls.Tree;
	import mx.controls.treeClasses.ITreeDataDescriptor;
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.controls.treeClasses.TreeListData;
	import mx.core.Application;
	import mx.events.MenuEvent;
	import mx.rpc.remoting.RemoteObject;

	public class ParentTreeItemRender extends TreeItemRenderer
	{
		include "TreeEvent.as";
		[Embed("../../assets/operation.png")]
		private var operationimg:Class;
		private var operationButton:LinkButton;//操作
		private var topMenu:Menu;//弹出菜单
		private var applicationid:String;//软件编号
		private var moduleid:String;
		private var oReportRO:RemoteObject;
		private var application:Object;
		private var menu:Menu;
		private var userid:String;
		
		//构造函数
		public function ParentTreeItemRender()
		{
			super();
			applicationid = Application.application.applicationid;
			moduleid = Application.application.moduleid;
			oReportRO = Application.application.oReportRO;
			application = Application.application as Object;
			userid = Application.application.userId;
		}
		//赋值
		override public function set data(value:Object):void{
			super.data = value;
		}
		
		//创建自定义的孩子
		override protected function createChildren():void{
			super.createChildren();
			operationButton = new LinkButton();//实例化操作图片
			addChild(operationButton);
			operationButton.setStyle("fontWeight","normal");
			operationButton.setStyle("icon",operationimg);
			operationButton.addEventListener(MouseEvent.CLICK,showTopMenu);
		}
		
		//显示弹出菜单事件
		protected function showTopMenu(event:MouseEvent):void{
			var str:String = '<root>';
			str += '<menuitem label="新建报表"/>';
			str += '<menuitem type="separator"/>'; 
			if(userid != null && userid != ""){
				str += '<menuitem label="编辑"/>';
			}
			str += '<menuitem label="重命名"/><menuitem label="删除"/></root>';
			var xml:XML = new XML(str);
			if(menu==null){
				menu =  Menu.createMenu(label as DisplayObjectContainer,xml,false);
			}
			menu.labelField = "@label";
			var point:Point = new Point();
			point = label.localToGlobal(point);
			menu.show(point.x+10,point.y+label.height/2);
			menu.addEventListener(MenuEvent.ITEM_CLICK,topMenuItemClick);
			menu.addEventListener(MouseEvent.ROLL_OUT,hiddenMenu);
		}
		
		
		//隐藏菜单
		protected function hiddenMenu(event:MouseEvent):void{
			if(menu){
				if(menu.visible){
					menu.visible = false;
				}
			}
		}
		
		
		//更新显示list
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(data!=null){
				var tree:Tree = (owner as Tree);//转为数实例
				var desc:ITreeDataDescriptor = tree.dataDescriptor;
				var currentNode:Object = data;
				var parentNode:Object = tree.getParentItem(currentNode);
				
				var startx:Number = data ? TreeListData( listData ).indent : 0;   
				if (disclosureIcon){   
					disclosureIcon.x = startx;   
					startx = disclosureIcon.x + disclosureIcon.width;   
					disclosureIcon.setActualSize(disclosureIcon.width,disclosureIcon.height);   
					disclosureIcon.visible = data ?TreeListData( listData ).hasChildren:false;   
				}
				
				label.move(startx, ( unscaledHeight - label.height ) / 2 );
				startx =label.x+label.measuredWidth;
				operationButton.setActualSize(operationButton.measuredWidth,operationButton.measuredHeight);
				operationButton.move(startx+50, ( unscaledHeight - operationButton.height ) / 2 ); 
				operationButton.visible = false;
				label.addEventListener(MouseEvent.MOUSE_OVER,showImage);
				label.addEventListener(MouseEvent.CLICK,lookChartEvent);
			}
		}
		
		//显示操作图片
		private function showImage(event:MouseEvent):void{
			if(operationButton.visible==false){
				operationButton.visible = true;
			}
		}
	}
}