package main.flex.cn.myapps.core.printer.components
{
	import main.flex.cn.myapps.core.printer.events.base.IconEvent;
	
	import flash.events.MouseEvent;
	
	import mx.controls.Image;
	import mx.managers.CursorManager;

	[Event(name=IconEvent.ICON_MOUSE_DOWN, type="events.IconEvent")]
	[Event(name=IconEvent.ICON_MOUSE_UP, type="events.IconEvent")]
	[Event(name=IconEvent.ICON_MOVE, type="events.IconEvent")]
	/**
	 * 面板控件对象上的控制方块
	 * @author Happy
	 * 
	 */
	public class ItemControl extends Image
	{
		private var _type: int; //图标类型
		private var _label: String; //图标上的文字说明
		private var _memo: String; //备注
		private var _selected: Boolean; //图标是否被选中
		private var _identity: String; //图标唯一标识符
		private var _icon: Object; //图像数据
		
		public var pAttractObj:Object=null;
		public var pAttractKey:String="";
		
		public var DrawPosType:String=""; //用name即可.以后删除
		
		public var Parent: Object;//ItemBox; //当前是ControlBox，它所控制的物件。
		
		
		
		public function get icon(): Object{
			return _icon;
		}
		
		public function set icon(value: Object): void{
			this._icon = value;
			draw();
		}
		
		public function get memo(): String{
			return _memo;
		}
		
		public function set memo(value: String): void{
			this._memo = value;
			draw();
		}
		
		public function get identity(): String{
			return _identity;
		}
		
		public function set identity(value: String): void{
			this._identity = value;
			draw();
		}
		
		public function get selected(): Boolean{
			return _selected;
		}
		
		public function set selected(value: Boolean): void{
			this._selected = value;
			draw();
		}
		
		public function get label(): String{
			return _label;
		}
		
		public function set label(value: String): void{
			this._label = value;
			draw();
		}
		
		public function get type(): int{
			return _type;
		}
		
		public function set type(value: int): void{
			this._type = value;
			draw();
		}
		
		public function ItemControl(icon: Object,type: int = 0,label: String = "节点",
									selected : Boolean = false, name: String = null,
									memo: String = "")
		{
			super();
			this._icon = icon;
			this._label = label;
			this._identity = name;
			this._selected = selected;
			this._type = type;
			this._memo = memo;
			this.width=20;
			this.height=20;
			
			this.draw();
			this.events();
		}
		
		//画图标
		public function draw(): void{
			this.alpha=0.50;
			this.graphics.clear();//清除前面画的东东
			
			//画线
			this.graphics.lineStyle(1, 0x0000FF);
			this.graphics.beginFill(0xFFFFFF, 0.5);
			this.graphics.drawRect(this.x,this.y,20,20);
			this.graphics.endFill();
		}
		
		//事件绑定
		private function events(): void{
			this.addEventListener(MouseEvent.MOUSE_DOWN, onIconMouseDown);
			this.addEventListener(MouseEvent.MOUSE_UP, onIconMouseUp);
			this.addEventListener(MouseEvent.MOUSE_MOVE, onIconMove);
			this.addEventListener(MouseEvent.MOUSE_OUT, onIconMouseOut);
		}
		
		private function onIconMouseOut(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_OUT);
			e.icon = this;
			this.dispatchEvent(e);
			CursorManager.removeAllCursors();		
		}
		
		//触发鼠标按下事件
		private function onIconMouseDown(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_DOWN);
			e.icon = this;
			this.dispatchEvent(e);			
		}
		
		//触发鼠标弹起事件
		private function onIconMouseUp(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_UP);
			e.icon = this;
			this.dispatchEvent(e);			
		}
		
		//触发鼠标移动事件
		private function onIconMove(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOVE);
			e.icon = this;
			this.dispatchEvent(e);
		}
	}
}