package cn.myapps.oreport.tree
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.CheckBox;
	import mx.controls.Image;
	import mx.controls.treeClasses.TreeListData;

	public class TreeCheckboxImageItemRender extends ParentTreeItemRender
	{
		private var iconImage:Image;//图标图片
		private var checkbox:CheckBox;//复选框
		private var _data:Object;
		
		//构造函数
		public function TreeCheckboxImageItemRender()
		{
			super();
		}
		
		//赋值
		override public function set data(value:Object):void{
			super.data = value;
		}
		
		//创建自定义的孩子
		override protected function createChildren():void{
			super.createChildren();
			iconImage = new Image();//实例化图标图片
			addChild(iconImage);
			
			checkbox = new CheckBox();
			checkbox.addEventListener(Event.CHANGE,changeSelectEvent);
			addChild(checkbox);
		}
		
		//选择事件
		protected function changeSelectEvent(event:Event):void{
			if(checkbox.selected){
				data.selected = true;
			}else{
				data.selected = false;
			}
		}
		
		//更新显示list
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(data!=null){
				var startx:Number = data ? TreeListData( listData ).indent : 0;   
				if (disclosureIcon){   
					disclosureIcon.x = startx;   
					startx = disclosureIcon.x + disclosureIcon.width;   
					disclosureIcon.setActualSize(disclosureIcon.width,disclosureIcon.height);   
					disclosureIcon.visible = data ?TreeListData( listData ).hasChildren:false;   
				}
				iconImage.source = "assets/type/chartView.png";//赋值图片的路径
				checkbox.selected = data.selected;
				checkbox.move(startx, ( unscaledHeight - checkbox.height ) / 2 );
				startx =checkbox.x+checkbox.measuredWidth;
				iconImage.setActualSize(iconImage.measuredWidth,iconImage.measuredHeight);
				iconImage.move(startx, ( unscaledHeight - iconImage.height ) / 2 );
				startx =iconImage.x+iconImage.measuredWidth;
				label.move(startx , ( unscaledHeight - label.height ) / 2 ); 
				icon.visible = false;
			}
		}
		
	}
}