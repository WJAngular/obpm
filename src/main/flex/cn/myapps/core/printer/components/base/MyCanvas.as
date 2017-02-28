package main.flex.cn.myapps.core.printer.components.base
{
	import main.flex.cn.myapps.core.printer.components.ContrlolLine;
	import main.flex.cn.myapps.core.printer.events.base.CanvasEvent;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;
	
	import flash.events.MouseEvent;
	
	import mx.controls.Label;
	import mx.core.UIComponent;

	/**
	 *画板 基类 
	 * @author Happy
	 * 
	 */
	public class MyCanvas extends UIComponent
	{
		
		public static var C_ReportCanvas :String = "ReportCanvas";
		public static var C_FormCanvas :String = "FormCanvas";
		public static var C_DetailCanvas :String = "DetailCanvas";
		public static var C_ViewCanvas :String = "ViewCanvas";
		public static var C_HeaderCanvas :String = "HeaderCanvas";
		public static var C_FooterCanvas :String = "FooterCanvas";
		
		
		public var label:Label= new Label();
		public var controlLine:Array=new Array();
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
		private var _repeat:uint =1;
		private var _repeatType:String ="static";
		private var _viewStyle:String ="showInFirstPage";
		private var _bindingView:String;
		
		
		public function set repeatType(value:String):void
		{
			_repeatType=value;
		}
		public function get repeatType():String
		{
			return _repeatType;
		}
		public function set repeat(value:uint):void
		{
			_repeat=value;
		}
		public function get repeat():uint
		{
			return _repeat;
		}
		
		public function set viewStyle(value:String):void
		{
			_viewStyle=value;
		}
		public function get viewStyle():String
		{
			return _viewStyle;
		}
		
		public function set bindingView(value:String):void
		{
			_bindingView=value;
		}
		public function get bindingView():String
		{
			return _bindingView;
		}
		
		
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
		
		/**
		 * 操作XML时使用   根据传递的xml节点标签 返回控件类型字符串
		 * @param strName
		 * @return 
		 * 
		 */		
		public static function MapType(strName:String):String
		{
			switch(strName){
				case "detail":
					return MyCanvas.C_DetailCanvas;
				case "form":
					return MyCanvas.C_FormCanvas;
				case "view":
					return MyCanvas.C_ViewCanvas;
				case "header":
					return MyCanvas.C_HeaderCanvas;
				case "footer":
					return MyCanvas.C_FooterCanvas;
			}
			return "unknow";
		}
		
		
		
		public function MyCanvas(startX: int = 0,startY: int = 0,endX: int = 0,endY: int = 0,
			lineColor: uint = 0xFFFFFF,fillColor: uint = 0xFFFFFF,thickness: Number = 1)
		{
			super();
			this._endX = endX;
			this._endY = endY;
			this._startX = startX;
			this._startY = startY;
			
			this._lineColor = lineColor;
			this._fillColor = fillColor;
			
			this._thickness = thickness;
			this.doubleClickEnabled =true;
			label.width =120;
			label.height =40;
			label.setStyle("fontSize",30);
			label.text ="canvas";
			label.setStyle("color","#C5CED3");
			label.alpha =0.20;
			
			this.addChild(label);
			
			
			this.addEventListeners();//绑定事件
		}
		
		/**
		 *画图 
		 * 
		 */
		public function draw(): void{
			this.graphics.clear();//清除前面画的东东
			//画线
			this.graphics.lineStyle(thickness,0xFFFFFF);
			
			this.graphics.beginFill(0xFFFFF8,0.3);
			this.graphics.drawRect(startX,startY,endX-startX,endY-startY);
			this.graphics.endFill();
			drawLabel();
			trace(startY);
		}
		
		public function drawLabel(): void{
			var sx:uint = startX+x;
			var sy:uint = startY+y;
			var ex:uint = endX+x;
			var ey:uint = endY+y;
			var h:uint = ey -sy;
			
			label.x = (ex-label.width)/2;
			label.y = (ey-h/2-label.height/2);
		}
		
		
		public function redraw():void
		{
			var e2: DrawEvent= new DrawEvent(DrawEvent.ReDraw);
			e2.pObj = this;
			this.dispatchEvent(e2);
		}
		
		
		/**
		 *添加事件监听 
		 * 
		 */
		private function addEventListeners(): void{
			this.addEventListener(MouseEvent.MOUSE_DOWN, onCanvasMouseDown);
			this.addEventListener(MouseEvent.DOUBLE_CLICK, onCanvasMouseDoubleClick);
		}
		
		private function onCanvasMouseDown(event: MouseEvent): void{
			var e: CanvasEvent= new CanvasEvent(CanvasEvent.MOUSE_DOWN);
			e.pObj = this;
			this.dispatchEvent(e);
		}
		private function onCanvasMouseDoubleClick(event: MouseEvent): void{
			var e: CanvasEvent= new CanvasEvent(CanvasEvent.DOUBLE_CLICK);
			e.pObj = this;
			this.dispatchEvent(e);
		}
		
		
		
		
		
		/**
		 *画 控制线条 
		 * @param report
		 * @return 
		 * 
		 */
		public function drawControlLine(canvas:MyCanvas):Array
		{
			if (controlLine.length==0){
				var sx:uint = startX+x;
				var sy:uint = startY+y;
				var ex:uint = endX+x;
				var ey:uint = endY+y;
				var h:uint = ey -sy;
				var line1:ContrlolLine=this.addControlLine("1",sx,sy,ex,sy);
				var line2:ContrlolLine=this.addControlLine("2",sx,sy+h,ex,sy+h);
				line1.Parent=this;
				line2.Parent=this;
				controlLine.push(line1);
				controlLine.push(line2);
				
			}
			
			for (var i:int=0;i<controlLine.length;i++){
				controlLine[i].visible=true;
			}
			
			return controlLine;
		}
		
		public function addControlLine(name:String,sx:int,sy:int,ex:int,ey:int):ContrlolLine
		{
			var line:ContrlolLine = new ContrlolLine(sx,sy,ex,ey);
			line.name =name;
			this.addChild(line);
			return line;
		}
		
		
		public function reSizeByControlLine():void
		{
			var e:DrawEvent=new DrawEvent("reSizeByControlLine");
			e.pObj = this;
			this.dispatchEvent(e);
			
			e= new DrawEvent(DrawEvent.ReDraw);
			e.pObj = this;
			this.dispatchEvent(e);
		}
		
		
		public function onReSizeByControlLine(event:DrawEvent):void
		{
			for(var i:int=0;i<controlLine.length;i++){
				var cLine:ContrlolLine=controlLine[i];
				switch(cLine.name){
					case "1":
						//this.startX=0-this.x;
						this.startY=cLine.startY+cLine.y;
						break;
					case "2":
						//this.endX=;
						this.endY=cLine.endY+cLine.y;
						break;
				}
			}
		}
		
		
		
		
		
		
		
		
		
		
	}
}