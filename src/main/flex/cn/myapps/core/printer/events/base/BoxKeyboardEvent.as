package main.flex.cn.myapps.core.printer.events.base
{
	import flash.events.KeyboardEvent;
	
	public class BoxKeyboardEvent extends KeyboardEvent
	{
		public static var KEY_DOWN:String = "box_key_down";
		public static var KEY_UP:String = "box_key_up";
		public var pObj: Object;
		public function BoxKeyboardEvent(type:String)
		{
			super(type);
		}
	}
}