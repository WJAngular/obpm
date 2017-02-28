package main.flex.cn.myapps.core.printer.components
{
	import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;
	
	import mx.controls.Image;

	public class PageNumber extends ItemDraw
	{
		private var img:Image = new Image();
		[Embed(source="../asserts/page.jpg")]
		private var ms:Class;
		public function PageNumber(startX:int=0, startY:int=0, endX:int=0, endY:int=0, lineColor:uint=0x000000, fillColor:uint=0xFFFFFF, thickness:Number=1)
		{
			super(startX, startY, endX, endY, lineColor, fillColor, thickness);
			img.x = startX;//(endX-startX-text.width)/2;
			img.y = startY;//(endY-startY)*dbHeight+dbAdd;
			img.width=17;
			img.height =16;
			img.source = ms;
			this.addChild(img);
			this.draw();
		}
		
		
		public function draw(): void{
		}
		private function onReDraw(event:DrawEvent): void{
			draw();
			this.draw_text(1,1);
		}
		
		public override function draw_text(dbHeight:Number,dbAdd:Number):void{
			
			
			img.x = startX;//(endX-startX-text.width)/2;
			img.y = startY;//(endY-startY)*dbHeight+dbAdd;
			if ((endX-startX)>20){
				img.width=(endX-startX);
			}
			if ((endY-startY)>20){
				img.height=(endY-startY);
			}
		}
		
	}
}