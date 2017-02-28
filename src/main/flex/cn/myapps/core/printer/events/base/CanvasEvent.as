package main.flex.cn.myapps.core.printer.events.base
{
	import flash.events.Event;

	/**
	 *MyCanvas 事件基类 
	 * @author Happy
	 * 
	 */
	public class CanvasEvent extends Event
	{
		public static var MOUSE_DOWN: String = "canvas_mouse_down";
		public static var MOUSE_UP: String = "canvas_mouse_up";
		public static var MOUSE_MOVE: String = "canvas_mouse_move";
		public static var DOUBLE_CLICK:String="canvas_double_click";
		public static var KEY_DOWN:String ="canvas_key_down";
		
		public var pObj: Object;
		public function CanvasEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}