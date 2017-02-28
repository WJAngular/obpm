package main.flex.cn.myapps.core.printer.components
{
	import main.flex.cn.myapps.core.printer.components.base.MyCanvas;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;

	/**
	 *重复区域画板 
	 * @author Happy
	 * 
	 */
	public class DetailCanvas extends MyCanvas
	{
		
		public function DetailCanvas(startX:int=0, startY:int=0, endX:int=0, endY:int=0, lineColor:uint=0xFFFFFF, fillColor:uint=0xFFFFFF, thickness:Number=1)
		{
			super(startX, startY, endX, endY, lineColor, fillColor, thickness);
			
			this.label.text ="Detail";
			draw();
			this.addEventListener(DrawEvent.ReDraw, onReDraw);
			this.addEventListener("reSizeByControlLine",onReSizeByControlLine);
		}
		
		
		public function onReDraw(event:DrawEvent): void{
			draw();
		}
		
	}
}