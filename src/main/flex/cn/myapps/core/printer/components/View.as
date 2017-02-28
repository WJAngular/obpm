package main.flex.cn.myapps.core.printer.components
{
	
	import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.text.TextFormat;
	
	import mx.controls.Alert;
	import mx.controls.TextArea;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.managers.DragManager;
	/**
	 * 表单字段 组件
	 * @author Happy
	 * 
	 */
	public class View extends ItemDraw
	{
		public var Value:String="";
		private var C_Width:int=50;
		private var C_Height:int=40;
		private var format:TextFormat=new TextFormat();
		
		public function View(startX:int=0, startY:int=0, endX:int=0, endY:int=0, lineColor:uint=0x000000, fillColor:uint=0xFFFFFF, thickness:Number=1)
		{
			super(startX, startY, endX, endY, lineColor, fillColor, thickness);
			this.label ="View"
			format.size=12;
			textarea.setStyle("borderStyle","none");
			this.addChild(textarea);
			this.draw();
			this.doubleClickEnabled =true;
			
			//注册一系列事件
			this.addEventListener(DrawEvent.ReDraw, onPaint);
			this.addEventListener("readFromControlBox",OnReadFromControlBox);
			
			this.addEventListener(DragEvent.DRAG_ENTER, dragInHandler);  
			this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);

		}
		
		
		public function onTextAreaChange(event:Event): void
		{
			this.Value=event.target.text;
			var e: Event= new Event(Event.CHANGE);
			this.dispatchEvent(e);
		}
		
		private function dragInHandler(event:DragEvent):void  
		{  
			var dropTarget:UIComponent=event.currentTarget as UIComponent;
			DragManager.acceptDragDrop(dropTarget);  
		}  
		
		private function dragDropHandler(event:DragEvent):void 
		{
			var pContainer:UIComponent=event.currentTarget as UIComponent;
			var pItem:UIComponent=event.draggedItem as UIComponent;
			
			pContainer.addChild(pItem);
		}
		
		//触发Draw
		private function onPaint(event:DrawEvent): void{
			draw();
			this.draw_text(1,1);
		}
		
		public override function draw_text(dbHeight:Number,dbAdd:Number):void{
			text.text = this.label;
			text.setTextFormat(format);
			
			text.x = startX+(endX-startX-text.width)/2;
			text.y = startY+(endY-startY)*dbHeight+dbAdd;
			
			textarea.x = startX+2;//(endX-startX-text.width)/2;
			textarea.y = startY+2;//(endY-startY)*dbHeight+dbAdd;
			if ((endX-startX)>20){
				textarea.width=(endX-startX-3);
			}
			if ((endY-startY)>20){
				textarea.height=(endY-startY-3);
			}
		}
		
		
		/**
		 * 
		private function draw_param(pPoint:C_Point):void{
			var x0:Number = startX+(endX-startX)*pPoint.X;
			var y0:Number = startY+(endY-startY)*pPoint.Y;
			
			this.graphics.drawCircle(x0,y0,5);
		}
		*/
		//画矩形
		public function draw(): void{
			this.graphics.clear();//清除前面画的东东
			
			//画线
			this.graphics.lineStyle(thickness,lineColor);
			
			this.graphics.beginFill(0xFFFFFF,0.8);
			this.graphics.drawRect(startX,startY,endX-startX,endY-startY);
			this.graphics.endFill();
		}
		
		
		
		

	}
}