package main.flex.cn.myapps.core.printer.components
{
	import main.flex.cn.myapps.core.printer.components.base.ItemDraw;
	import main.flex.cn.myapps.core.printer.data.C_K_Str;
	import main.flex.cn.myapps.core.printer.events.base.DrawEvent;

	/**
	 *线条 组件 
	 * @author Happy
	 * 
	 */
	public class Line extends ItemDraw
	{
		
		
		public function Line(startX: int = 0,startY: int = 0,endX: int = 0,endY: int = 0,
			lineColor: uint = 0x000000,thickness: Number = 1)
		{
			super(startX,startY,endX,endY,lineColor,lineColor,thickness);
			this.label="";
			this.draw();
			
			this.addEventListener("readFromControlBox", OnResizeByControlBox);
			this.addEventListener(DrawEvent.ReDraw, reDraw);
		}
		
		
		//触发Draw
		private function reDraw(event:DrawEvent): void{
			draw();
		}
		
		//画直线
		public function draw(): void{
			this.graphics.clear();//清除前面画的东东
			//画线
			this.graphics.lineStyle(thickness, lineColor);
			var x0:Number=Math.ceil(startX);
			var y0:Number=Math.ceil(startY);
			var x1:Number=Math.ceil(endX);
			var y1:Number=Math.ceil(endY);
			this.graphics.moveTo(Math.round(x0/10)*10,Math.round(y0/10)*10);
			this.graphics.lineTo(Math.round(x1/10)*10,Math.round(y1/10)*10);
		}
		
		public function OnResizeByControlBox(event:DrawEvent):void
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
		
	}
}