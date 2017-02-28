package cn.myapps.core.macro.editor.components.editor
{
	import flash.display.Bitmap;
	import flash.text.TextField;
	import flash.text.TextFieldType;
	
	import mx.controls.Alert;
	import mx.controls.HScrollBar;
	import mx.controls.VScrollBar;
	import mx.core.UIComponent;

	public class CodeEditor extends UIComponent {
		var _displayTxt : TextField;
		var _linesTxt : TextField;
		var _stateTxt : TextField;
		
		var _vScrollBar : VScrollBar;
		var _hScrollBar : HScrollBar;
		
		var _shadowHolder:Bitmap;
		var _flashTextFormater:FlashTextFormatter;

		var _mode : String;
		
		var _showLine : Number;
		
		var readonly: Boolean;
		
		public function CodeEditor()
		{
			super();
		
			_displayTxt = new TextField();
			_displayTxt.type = TextFieldType.INPUT;
			_displayTxt.multiline = true;
			
			_linesTxt = new TextField();

			_vScrollBar = new VScrollBar();
			
			_hScrollBar = new HScrollBar();
			
			addChild(_displayTxt);
			addChild(_linesTxt);
			
			addChild(_vScrollBar);
			addChild(_hScrollBar);

			_shadowHolder = new Bitmap();
			addChild(_shadowHolder);
			
			doSth();
		}
		
		private function doSth():void {
			
			_flashTextFormater = new FlashTextFormatter (this);
				
			_flashTextFormater.auto = true;
			
			_flashTextFormater.loadDef("Iscript.xml");
		}

		public function resize():void {
			trace("screen-->"+screen);
			
			_flashTextFormater.setSize(width, height);
		}
		
		override public function set x(value:Number):void {
			super.x = value;
		}
			
		override public function get x():Number {
			return super.x;
		}
		
		override public function set width(value:Number):void {
			super.width = value;
			resize();
		}
		
		override public function get width():Number {
			return super.width;
		}
		
		override public function set height(value:Number):void {
			super.height = value;
			resize();
		}
		
		override public function get height():Number {
			return super.height;
		}
		
		/**
		 * 设置是否只读 
		 */
		public function setReadonly(readonly:Boolean):void {
			this.readonly = readonly;
			this._displayTxt.type = readonly ? TextFieldType.DYNAMIC: TextFieldType.INPUT;
		}
		
		public function isReadonly():Boolean {
			return this.readonly;
		}
		
		public function setText(value:String):void {
			_flashTextFormater.setText(value);
		}
		
		public function getText():String {
			return _flashTextFormater.getText();
		}
		
		public function goLine(line:Number):void {
			_flashTextFormater.goLine(line);
		}
		
		public function setMode(mode : String) : void {
			_mode = mode;			
		}
		
		override protected function	updateDisplayList(unscaledWidth:Number,	unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth,unscaledHeight);
		}	
	}
}