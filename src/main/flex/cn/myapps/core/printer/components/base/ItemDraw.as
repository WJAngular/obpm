package main.flex.cn.myapps.core.printer.components.base
{
	
	import main.flex.cn.myapps.core.printer.components.ItemControl;
	import main.flex.cn.myapps.core.printer.components.ItemTextBox;
	import main.flex.cn.myapps.core.printer.components.Line;
	import main.flex.cn.myapps.core.printer.components.PageNumber;
	import main.flex.cn.myapps.core.printer.components.ReportCanvas;
	import main.flex.cn.myapps.core.printer.components.StaticLabel;
	import main.flex.cn.myapps.core.printer.components.View;
	import main.flex.cn.myapps.core.printer.data.Treap;
	import main.flex.cn.myapps.core.printer.events.base.BoxEvent;
	import main.flex.cn.myapps.core.printer.events.base.BoxKeyboardEvent;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	
	import mx.controls.TextArea;
	import mx.controls.TextInput;
	import mx.core.UIComponent;
	
	
	
	
	
	/**
	 * 画板上的控件基础对象
	 */
	public class ItemDraw extends UIComponent
	{
		
		public static var C_ItemTextBox: String = "ItemTextBox";
		public static var C_StaticLabel: String = "StaticLabel";
		public static var C_Line: String = "Line";
		public static var C_PageNumber: String = "PageNumber";
		public static var C_View: String = "View";
	
		
		public var label:String="label";
		public var text: TextField = new TextField();
		public var textarea:TextArea = new TextArea();
		public var textInput:TextInput = new TextInput();
		public var bindingField:String ="";
		public var bindingView:String ="";
		public var parentCanvas:UIComponent = null; //XML中其上级的节点标签
		//pControlPoints 控制点
		public var pControlPoints:Array=new Array();
		public var pTreapConnect:Treap = new Treap();
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
		
		
		public static function getType(pBox:Object):String
		{
			var strTag:String="";
	
			if (pBox is ItemTextBox){
				strTag=ItemDraw.C_ItemTextBox;
			}else if(pBox is StaticLabel){
				strTag=ItemDraw.C_StaticLabel;
			}else if(pBox is Line){
				strTag=ItemDraw.C_Line;
			}else if(pBox is View){
				strTag=ItemDraw.C_View;
			}else if(pBox is PageNumber){
				strTag=ItemDraw.C_PageNumber;
			}
			return strTag;
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
				case "textbox":
					return ItemDraw.C_ItemTextBox;
				case "staticLabel":
					return ItemDraw.C_StaticLabel;
				case "line":
					return ItemDraw.C_Line;
				case "pageNumber":
					return ItemDraw.C_PageNumber;
				case "view":
					return ItemDraw.C_View;
			}
			return "unknow";
		}
		/**
		 * 操作XML时使用 用于生产 节点标签
		 * @param pBox
		 * @return 
		 * 
		 */		
		public static function getTag(pBox:Object):String
		{
			var strTag:String="";		
			if (pBox is ItemTextBox){
				strTag="textbox";
			}else if(pBox is StaticLabel){
				strTag="staticLabel";
			}else if(pBox is Line){
				strTag="line";
			}else if(pBox is PageNumber){
				strTag="pageNumber";
			}else if(pBox is View){
				strTag="view";
			}
			return strTag;
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
		
		
		//根据名称获取控制点
		public function getControlBox(strName:String):ItemControl
		{
			for (var j:int=0;j<this.pControlPoints.length;j++){
				var pControl:ItemControl=this.pControlPoints[j];
				if (pControl.name==strName){
					return pControl;
				}
			}
			return null;
		}
		
		//hide ControlBox
		public function hideControlBox():void
		{
			for (var j:int=0;j<pControlPoints.length;j++){
				var pItem:ItemControl=pControlPoints[j];
				pItem.visible=false;
			}
		}
		//diaplay ControlBox
		public function displayeControlBox():void
		{
			for (var j:int=0;j<pControlPoints.length;j++){
				var pItem:ItemControl=pControlPoints[j];
				if(pItem.name=="1"){
					pItem.x = this.startX+this.x -10;
					pItem.y = this.startY+this.y -10;
				}else if(pItem.name=="2"){
					pItem.x = this.endX+this.x -10;
					pItem.y = this.endY+this.y -10;
				}
				pItem.visible=true;
			}
		}
		
		public function ItemDraw(startX: int = 0,startY: int = 0,endX: int = 0,endY: int = 0,
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
			text.autoSize = TextFieldAutoSize.CENTER;
			
			text.x = startX+(endX-startX)/2;
			text.y = startY+(endY-startY)/2+ 30;
			textarea.styleName="MyTextarea";
			textarea.editable=false;
			this.addChild(text);
			//this.addChild(textarea);
			
			
			this.addEventListeners();//绑定事件
		}
		
		
		
		
		public function draw_text(dbHeight:Number,dbAdd:Number):void{
			
		}
		
		//画控制方块
		public function drawControlBox(report:ReportCanvas):Array
		{
			if (pControlPoints.length==0){
				//默认2个
				var pICO1:ItemControl=report.addNewICO(null,"1",startX,startY);
				var pICO2:ItemControl=report.addNewICO(null,"2",endX,endY);
				pICO1.DrawPosType="From";
				pICO2.DrawPosType="To";
				pICO1.Parent=this;
				pICO2.Parent=this;
				pControlPoints.push(pICO1);
				pControlPoints.push(pICO2);
				
			}
			
			for (var i:int=0;i<pControlPoints.length;i++){
				pControlPoints[i].visible=true;
			}
			
			return pControlPoints;
		}
		
		public function redraw():void
		{
			var e2: DrawEvent= new DrawEvent(DrawEvent.ReDraw);
			e2.pObj = this;
			this.buttonMode=true;  
    		this.mouseChildren=false;
			this.dispatchEvent(e2);
		}
		
		//从控制盒中读取参数.
		public function readFromControlBox():void
		{
			var e:DrawEvent=new DrawEvent("readFromControlBox");
			e.pObj = this;
			this.dispatchEvent(e);
			
			e= new DrawEvent(DrawEvent.ReDraw);
			e.pObj = this;
			this.dispatchEvent(e);
		}
		public function moveControlBox(dx:int,dy:int):void
		{
			for(var i:int=0;i<pControlPoints.length;i++){
				var pBox:ItemControl=pControlPoints[i];
				pBox.x+=dx;
				pBox.y+=dy;
			}
		}
		
		
		public function OnReadFromControlBox(event:DrawEvent):void
		{
			for(var i:int=0;i<pControlPoints.length;i++){
				var pControl:ItemControl=pControlPoints[i];
				switch(pControl.name){
					case "1":
						this.startX=pControl.x+pControl.width/2-this.x;
						this.startY=pControl.y+pControl.height/2-this.y;
						break;
					case "2":
						this.endX=pControl.x+pControl.width/2-this.x;
						this.endY=pControl.y+pControl.height/2-this.y;
						break;
				}
			}
		}
		
		
		//事件绑定
		private function addEventListeners(): void{
			this.addEventListener(MouseEvent.MOUSE_DOWN, onBoxMouseDown);
			this.addEventListener(MouseEvent.MOUSE_UP, onBoxMouseUp);
			this.addEventListener(MouseEvent.MOUSE_MOVE, onBoxMouseMove);
			this.addEventListener(MouseEvent.DOUBLE_CLICK, onBoxDoubleClick);
			this.addEventListener(KeyboardEvent.KEY_DOWN, onBoxKeyDown);
		}
		
		//触发鼠标按下事件
		private function onBoxMouseDown(event: MouseEvent): void{
			var e: BoxEvent= new BoxEvent(BoxEvent.MOUSE_DOWN);
			e.pObj = this;
			this.dispatchEvent(e);
			//隐藏当前显示的控制方块
			//this.hideControlBox();
			//this.startDrag();
			
			this.oldX=this.x;
			this.oldY=this.y;
		}
		
		//触发鼠标弹起事件
		private function onBoxMouseUp(event: MouseEvent): void{
			var e: BoxEvent= new BoxEvent(BoxEvent.MOUSE_UP);
			e.pObj = this;
			this.dispatchEvent(e);
			
			redraw();
			
			//this.stopDrag();
//			this.moveControlBox(
//				this.x-this.oldX,
//				this.y-this.oldY);
		}
		
		
		//触发鼠标移动事件
		private function onBoxMouseMove(event: MouseEvent): void{
			var e: BoxEvent= new BoxEvent(BoxEvent.MOUSE_MOVE);
			e.pObj = this;
			this.dispatchEvent(e);
		}
		
		
		private function onBoxDoubleClick(event: MouseEvent): void{
			var e: BoxEvent= new BoxEvent(BoxEvent.DOUBLE_CLICK);
			e.pObj = this;
			this.dispatchEvent(e);
		}
		
		private function onBoxKeyDown(event: KeyboardEvent): void{
			var e: BoxKeyboardEvent= new BoxKeyboardEvent(BoxKeyboardEvent.KEY_DOWN);
			e.charCode =event.charCode;
			e.pObj = this;
			this.dispatchEvent(e);
		}
		
		
		
		
		public function addNewICO(
		icon:Object,strName:String,
		x:Number,y:Number):ItemControl{
		var newBox: ItemControl = new ItemControl(icon,0);
		newBox.label = strName;
		newBox.draw();
		newBox.type=-1;//-1代表是ControlBox
		newBox.name = strName;
		newBox.x = x-10;
		newBox.y = y-10;
		
		this.addChild(newBox);
		
		return newBox;
		}
		
		

	}
	
	
		
}