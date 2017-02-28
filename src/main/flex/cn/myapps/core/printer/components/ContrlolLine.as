package main.flex.cn.myapps.core.printer.components
{
	import main.flex.cn.myapps.core.printer.events.base.IconEvent;
	
	import flash.events.MouseEvent;
	
	import mx.controls.Alert;
	import mx.core.UIComponent;

	/**
	 *画板上的控制线条 
	 * @author Happy
	 * 
	 */
	public class ContrlolLine extends UIComponent
	{
		private var _thickness: Number; //线条粗细
		private var _lineColor: uint;//线条颜色
		private var _fillColor: uint;//
		private var _startX: int; //左上角X坐标
		private var _startY: int;//左上角Y坐标
		private var _endX: int; //右下角X坐标
		private var _endY: int; //右下角Y坐标
		public var startX_MouseDown: int; //鼠标按下时 左上角X坐标
		public var startY_MouseDown: int; //鼠标按下时 左上角Y坐标
		private var oldX:int=0;//移动的时候记录以前的x
		private var oldY:int=0;//移动的时候记录以前的y
		
		public var Parent: Object;
		
		
		
		
		public function get thickness(): Number{
			return _thickness;
		}
		
		public function set thickness(value: Number): void{
			this._thickness = value;
		}
		
		public function get lineColor(): uint{
			return _lineColor;
		}
		
		public function set lineColor(value: uint): void{
			this._lineColor = value;
		}
		
		public function get fillColor(): uint{
			return _fillColor;
		}
		
		public function set fillColor(value: uint): void{
			this._fillColor = value;
		}
		
		public function get startX(): int{
			return _startX;
		}
		
		public function set startX(value: int): void{
			this._startX = value;
		}
		
		public function get startY(): int{
			return _startY;
		}
		
		public function set startY(value: int): void{
			this._startY = value;
		}
		
		public function get endX(): int{
			return _endX;
		}
		
		public function set endX(value: int): void{
			this._endX = value;
		}
		
		public function get endY(): int{
			return _endY;
		}
		
		public function set endY(value: int): void{
			this._endY = value;
		}
		
		
		
		
		public function ContrlolLine(startX: int = 0,startY: int = 0,endX: int = 0,endY: int = 0,
			lineColor: uint = 0x000000,
			fillColor: uint = 0xFFFFFF,
			thickness: Number = 1)
		{
			super();
			this._endX = endX;
			this._endY = endY;
			this._startX = startX;
			this._startY = startY;
			this._lineColor = lineColor;
			this._fillColor = fillColor;
			this._thickness = thickness;
			this.buttonMode=true;  
    		this.mouseChildren=false;
			this.draw();
			this.addEventListeners();
		
	}
	
	
	
	public function draw(): void{
		//1.先画一个6像素的透明矩形 方便鼠标选中 进行拖动
		//2。在控件的边框上画 直线 用于显示一个无颜色边框的组件的大小范围
			this.alpha=0.50;
			this.graphics.clear();
			this.graphics.lineStyle(thickness,0xFFFFFF);
			this.graphics.beginFill(0xFFFFFF,0.1);
			this.graphics.drawRect(startX,startY-3,endX-startX,6);
			this.graphics.endFill();
			
			
			this.graphics.lineStyle(thickness, 0x000000);
			this.graphics.moveTo(startX,startY);
			this.graphics.lineTo(endX,endY);
		}
		
		//事件绑定
		private function addEventListeners(): void{
			this.addEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
			this.addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
			this.addEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
		}
		
		
		private function onMouseDown(event:MouseEvent):void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_DOWN);
			e.icon = this;
			this.dispatchEvent(e);		
		}
		
		private function onMouseMove(event:MouseEvent):void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOVE);
			e.icon = this;
			this.dispatchEvent(e);		
		}
		private function onMouseUp(event:MouseEvent):void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_UP);
			e.icon = this;
			this.dispatchEvent(e);		
		}
	}
		
		
}