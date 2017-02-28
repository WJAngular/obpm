package main.flex.cn.myapps.core.printer.components
{
	import main.flex.cn.myapps.core.printer.components.base.MyCanvas;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;

	public class ViewCanvas extends MyCanvas
	{
		public function ViewCanvas(startX:int=0, startY:int=0, endX:int=0, endY:int=0, lineColor:uint=0xFFFFFF, fillColor:uint=0xFFFFFF, thickness:Number=1)
		{
			super(startX, startY, endX, endY, lineColor, fillColor, thickness);
			this.label.text ="View";
			label.setStyle("fontSize",25);
			//this.buttonMode=true;  
    		//this.mouseChildren=false;
			draw();
			this.addEventListener(DrawEvent.ReDraw, onReDraw);
			this.addEventListener("reSizeByControlLine",onReSizeByControlLine);
		}
		
		/*
		public override function draw(): void{
			
		}
		*/
		
		public function onReDraw(event:DrawEvent): void{
			draw();
		}
		
	}
}