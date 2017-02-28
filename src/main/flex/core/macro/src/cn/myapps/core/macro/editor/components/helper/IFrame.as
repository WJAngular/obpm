package cn.myapps.core.macro.editor.components.helper
{
	import flash.events.Event;
	import flash.external.ExternalInterface;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.controls.Alert;
	import mx.events.MoveEvent;
	import mx.events.ResizeEvent;

	/**
	 *帮助面板 
	 * @author Happy
	 * 
	 */
	public class IFrame extends Canvas
	{
		public function IFrame()
		{
			super();
			this.percentHeight =100;
			this.percentWidth =100;
			this.addEventListener(ResizeEvent.RESIZE,onMoveIFrame);
			this.addEventListener(MoveEvent.MOVE,onMoveIFrame);
		}
		
		
		 private var _source: String;
		 
		  public function onMoveIFrame(evnet:Event): void{
		  	this.percentHeight =100;
		  	this.percentWidth = 100;
		  	trace('IFrame------w'+this.width);
		  	trace('IFrame------h'+this.height);
		  	callLater(moveIFrame);
		  }
		 

        /**
         * Move iframe through ExternalInterface.  The location is determined using localToGlobal()
         * on a Point in the Canvas.
         **/
        private function moveIFrame(): void
        {

            var localPt:Point = new Point(0, 0);
            var globalPt:Point = this.localToGlobal(localPt);

            ExternalInterface.call("moveIFrame", globalPt.x, globalPt.y, this.width, this.height);
        }

        /**
         * The source URL for the IFrame.  When set, the URL is loaded through ExternalInterface.
         **/
        public function set source(source: String): void
        {
            if (source)
            {
                if (! ExternalInterface.available)
                {
                    throw new Error("ExternalInterface is not available in this container. Internet Explorer ActiveX, Firefox, Mozilla 1.7.5 and greater, or other browsers that support NPRuntime are required.");
                }
                _source = source;
                ExternalInterface.call("loadIFrame", source);
                moveIFrame();
            }
        }

        public function get source(): String
        {
            return _source;
        }

        /**
         * Whether the IFrame is visible.  
         **/
        override public function set visible(visible: Boolean): void
        {
            super.visible=visible;

            if (visible)
            {
                ExternalInterface.call("showIFrame");
            }
            else 
            {
                ExternalInterface.call("hideIFrame");
            }
        }
		
	}
}