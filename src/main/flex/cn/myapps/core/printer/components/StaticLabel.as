package main.flex.cn.myapps.core.printer.components
{
	import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;
	
	import flash.events.KeyboardEvent;
	import flash.text.TextFormat;
	
	import mx.managers.CursorManager;

	/**
	 * 静态文本标签 组件
	 * @author Happy
	 * 
	 */
	public class StaticLabel extends ItemDraw
	{
		private var format:TextFormat=new TextFormat();
		
		public function StaticLabel(startX:int=0, startY:int=0, endX:int=0, endY:int=0, lineColor:uint=0x000000, fillColor:uint=0xFFFFFF, thickness:Number=1)
		{
			super(startX, startY, endX, endY, lineColor, fillColor, thickness);
			this.label ="Label"
			this.textInput.text='label';
			this.textInput.setStyle("borderStyle","none");
			this.textInput.addEventListener(KeyboardEvent.KEY_DOWN,onTextFieldKeyDown);
			format.size=12;
			this.addChild(textInput);
			//this.useHandCursor = true;
			//this.mouseChildren = false;
			//this.useHandCursor=true;    
   	 		
			//CursorManager.removeAllCursors();
			this.draw();
			this.doubleClickEnabled =true;
			
			//注册一系列事件
			this.addEventListener(DrawEvent.ReDraw, onReDraw);
			this.addEventListener("readFromControlBox",OnReadFromControlBox);
		}
		
		
		
		/**
		 *画矩形 
		 * 
		 */
		public function draw(): void{
			this.graphics.clear();//清除前面画的东东
			//画线
			this.graphics.lineStyle(thickness,lineColor);
			
			this.graphics.beginFill(0xFFFFFF,0.8);
			this.graphics.drawRect(startX,startY,endX-startX,endY-startY);
			this.graphics.endFill();
		}
		
		/**
		 *触发 Draw 
		 * @param event
		 * 
		 */
		private function onReDraw(event:DrawEvent): void{
			this.textInput.editable = false;
			draw();
			this.draw_text(1,1);
		}
		
		
		public function onTextFieldKeyDown(event:KeyboardEvent):void
		{
			if(event.keyCode ==13){
				this.redraw();
				this.hideControlBox();
			}
		}
		
		
		
		public override function draw_text(dbHeight:Number,dbAdd:Number):void{
			text.text = this.label;
			text.setTextFormat(format);
			
			text.x = startX+(endX-startX-text.width)/2;
			text.y = startY+(endY-startY)*dbHeight+dbAdd;
			
			textInput.x = startX+2;//(endX-startX-text.width)/2;
			textInput.y = startY+2;//(endY-startY)*dbHeight+dbAdd;
			if ((endX-startX)>20){
				textInput.width=(endX-startX-3);
			}
			if ((endY-startY)>20){
				textInput.height=(endY-startY-3);
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}