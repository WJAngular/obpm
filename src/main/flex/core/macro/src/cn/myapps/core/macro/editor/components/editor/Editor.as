package cn.myapps.core.macro.editor.components.editor
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.containers.TabNavigator;
	import mx.events.ResizeEvent;

	/**
	 *代码编辑面板 
	 * @author Happy
	 * 
	 */
	public class Editor extends TabNavigator
	{
		private var _canvas:ArrayCollection = new ArrayCollection();
		
		public function Editor()
		{
			this.percentHeight = 100;
			this.percentWidth = 100;
			super();
			this.addEventListener(ResizeEvent.RESIZE,resize);
		}
		
		public function addEditorCanvas(iscriptContent:String ,lable:String,goLine:int):EditorCanvas{
			var editorCanvas = new EditorCanvas(iscriptContent,lable,goLine);
			canvas.addItem(editorCanvas);
			this.addChild(editorCanvas);
			editorCanvas.reSize(this.width,this.height);
			this.validateNow();
			return editorCanvas;
			
		}
		
		public function get canvas():ArrayCollection
		{
			return _canvas;
		}

		public function set canvas(value:ArrayCollection):void
		{
			_canvas = value;
		}
		
		private function resize(event:Event):void {
			this.percentHeight = 100;
			this.percentWidth  = 100;
			for each(var ec:EditorCanvas in canvas){
				ec.reSize(this.width,this.height);
			}
		}
		
		public function reSize():void {
			for each(var ec:EditorCanvas in canvas){
				ec.reSize(this.width,this.height);
			}
		}
	}
}