package cn.myapps.core.macro.editor
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Shape;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextLineMetrics;
	import flash.xml.XMLDocument;
	
	import mx.controls.Alert;
	import mx.controls.HScrollBar;
	import mx.controls.VScrollBar;
	import mx.rpc.http.HTTPService;
	
	public class FlashTextFormatter
	{
		private var _displayTxt : TextField;
		private var _linesTxt : TextField;
		
		private var _shadowHolder:Bitmap;
		private var _bitmapData:BitmapData;
	
		private var _hScroller:HScrollBar;
		private var _vScroller:VScrollBar;
		
		private var _linesNo : Number = 1;
		private var _auto : Boolean;
		
		private var _x:Number,_y:Number,_width:Number,_height:Number;
		
		private var hl : Formatter;
		private var formatDuration : Number;
		
		private var lineMetrics:TextLineMetrics;
		
		var intID;
		// automatic formatting
		public function set auto (bool:Boolean)
		{
			if (bool)
			{
				_auto = true;
			} else 
			{
				_auto = false;
			}
		}
		public function get auto () : Boolean
		{
			return _auto;
		}
		
		function FlashTextFormatter (c:CodeEditor)	{
	
			_displayTxt = c._displayTxt;
	
			_linesTxt = c._linesTxt;
			_hScroller = c._hScrollBar;
			_vScroller = c._vScrollBar;
			_shadowHolder = c._shadowHolder;
			
			_x = c.x;
			_y = c.y;
			_width = c.width;
			_height = c.height;

			_displayTxt.background = true;
			_displayTxt.backgroundColor = 0xffffff;
			_linesTxt.background = true;
			_linesTxt.backgroundColor = 0xf4f4f4;
			_linesTxt.selectable = false;
			_linesTxt.doubleClickEnabled = true;
			_linesTxt.alwaysShowSelection = false;
			
			_displayTxt.addEventListener(Event.CHANGE, onChange);
			_displayTxt.addEventListener(Event.SCROLL, onChangeLine);
			_displayTxt.addEventListener(MouseEvent.CLICK, onChangeLine);
			_displayTxt.addEventListener(KeyboardEvent.KEY_UP, onChangeLine);
			_vScroller.addEventListener(Event.SCROLL, onScroller);
			_hScroller.addEventListener(Event.SCROLL, onScroller);
			_linesTxt.addEventListener(MouseEvent.DOUBLE_CLICK, onSetBreak);
		
			numberLines ();
			hl = new Formatter ();
			loadText(""); //Just for test
		}
		
		function onSetBreak(event:MouseEvent) {
			// 强行转换
			var field: TextField = event.target as TextField;
			field.backgroundColor = 0xACFA58;
			trace(event);
		}
		
		function onChange(event:Event) {
	        if(auto)highlight();
	        
	        onChangeLine(event);
		}
		
		public function loadDef (path : String)
		{
			var http:HTTPService;
			http = new HTTPService();
			
			http.url = path;
			
			http.xmlDecode = function (x:XMLDocument) {
				onDefLoad(x);
			}
			
			http.send();
	
		}
		
		
		
		function onDefLoad (x : XMLDocument)
		{
			x.ignoreWhite = true;
			
			var def = x.childNodes[0];
			var setup = def.childNodes[0];
			var delimiters = setup.childNodes[0].childNodes[0].nodeValue.split ("");
			hl.delimiters = {};
			for (var i in delimiters) hl.delimiters[delimiters[i]] = true; 
			hl.delimiters["\r"] = true;
			hl.delimiters["\t"] = true;
			hl.delimiters["\n"] = true;
			hl.commentSeq = setup.childNodes[1].childNodes[0].nodeValue;
			hl.blockCommentOn = setup.childNodes[2].childNodes[0].nodeValue;
			hl.blockCommentOff = setup.childNodes[3].childNodes[0].nodeValue;
			hl.commFormat = convertToTextFormat (setup.childNodes[4].attributes);
			hl.strFormat = convertToTextFormat (setup.childNodes[5].attributes);
			var t = def.childNodes;
			hl.wordlist = {};
			
			for (var i = 1; i < t.length; i ++)
			{
	//			var format:TextFormat = hl ["format" + i] = convertToTextFormat (t[i].attributes);
				var format:TextFormat = convertToTextFormat (t[i].attributes);
				var w = t[i].childNodes;
				for (var j = 0; j < w.length; j ++)
				{
					if (w[j].childNodes[0]) {
						hl.wordlist [w[j].childNodes[0].nodeValue] = format;	
					}
				}
			}
			if (auto) highlight ();
		}
		
		function onChangeLine (event:Event)
		{
	//		invalidateDisplayList();
			
			if (_displayTxt.bottomScrollV >= _linesNo) numberLines ();
			
			_linesTxt.scrollV = _displayTxt.scrollV;
	
			_vScroller.minScrollPosition = 0;
			_vScroller.maxScrollPosition = _displayTxt.maxScrollV;
	        _vScroller.lineScrollSize = 1; 
	        _vScroller.pageScrollSize = 5; 
	        _vScroller.scrollPosition = _displayTxt.scrollV;
	        _vScroller.pageSize = _vScroller.height / _displayTxt.numLines;
	        
			_hScroller.minScrollPosition = 0;	        
	        _hScroller.maxScrollPosition = _displayTxt.maxScrollH;
	        _hScroller.lineScrollSize = 1;
	        _hScroller.pageScrollSize = 5;
	        _hScroller.scrollPosition = _displayTxt.scrollH;
	        _hScroller.pageSize = _hScroller.width * _hScroller.width / _displayTxt.maxScrollH;
	        
	       	setSize(_width, _height);
	        
   			var line = _displayTxt.getLineIndexOfChar(_displayTxt.selectionBeginIndex);
	        drawHightlightLine(line, 0xffff00);
	        
		}
		
	
		/**
		 * 
		 * 通过行索引查找相对显示行的总高度
		 * @param lineIndex 行索引
		 * @return 相对高度
		 * 
		 */		
		private function getDisLineHeightByLine(line:Number):Number {
			var lineIndex = line;
			
			lineIndex = lineIndex - _displayTxt.scrollV + 1;
			
			return lineIndex * getLineMetrics().height + 2;
		}
		
		/////////////////
		public function goLine(line:Number):void {
//   			var line = _displayTxt.getLineIndexOfChar(_displayTxt.selectionBeginIndex);
			
			var t = _displayTxt.bottomScrollV - _displayTxt.scrollV - 1 ;
//			_displayTxt.scrollV = line - t;
			if (line > t) {
				_displayTxt.scrollV = line - t;
			}
//	       Alert.show("_displayTxt.scrollV-->"+_displayTxt.scrollV+"\n_displayTxt.bottomScrollV-->"+_displayTxt.bottomScrollV);
	       drawHightlightLine(line, 0xff0000);
	       
		}
		
		function onScroller(event:Event){
			_displayTxt.scrollV = _vScroller.scrollPosition;
			_displayTxt.scrollH = _hScroller.scrollPosition;
		}
		
		function numberLines ()
		{
			var no = _displayTxt.bottomScrollV;
			if (no < 40) no = 40
			while (_linesNo <= no)
			{
				_linesTxt.text += _linesNo + ".\r";
				_linesNo ++;
			}
		}
		
		public function setSize (w:Number, h:Number):void{
			
			if (w > 0 )
				_width = w;
			
			if (h > 0 )
				_height = h;
				
			if (w>0 && h > 0 ) {
				_displayTxt.x = 39;
				
				_vScroller.x = _width - 16;
		
				_vScroller.width = 16;
				_vScroller.height = _height - 16;
				
				_hScroller.y = _height - 16;
				_hScroller.width = _width - 16;
				_hScroller.height = 16;
	
				//同时显示纵滚动条和横滚动条
				if (_displayTxt.height < _displayTxt.textHeight 
				&& _displayTxt.width < _displayTxt.textWidth) {
				
					_vScroller.visible = true;
					_vScroller.enabled = true;
					_vScroller.height = _height - 16;
					_displayTxt.width = _width - 16 - 39;
					
					_hScroller.visible = true;
					_hScroller.enabled = true;
					_hScroller.width = _width - 16;
					_displayTxt.height = _height - 16;
					
				}
				//仅显示纵滚动条
				else if (_displayTxt.height < _displayTxt.textHeight 
				&& _displayTxt.width > _displayTxt.textWidth) {
					_vScroller.visible = true;
					_vScroller.enabled = true;
					_vScroller.height = _height;
					
					_hScroller.visible = false;
					_displayTxt.width = _width - 16 - 39;
					_displayTxt.height = _height;
				}
				//仅显示横滚动条
				else if (_displayTxt.height > _displayTxt.textHeight 
				&& _displayTxt.width < _displayTxt.textWidth) {
					_vScroller.visible = false;
					trace("_width-->sss"+_width);
					
					_displayTxt.width = _width - 39;
					_hScroller.visible = true;
					_hScroller.enabled = true;
					_hScroller.width = _width;
					_displayTxt.height = _height - 16;
					
				}
				//均不显示
				else if (_displayTxt.height > _displayTxt.textHeight 
				&& _displayTxt.width > _displayTxt.textWidth) {
					_vScroller.visible = false;
					_displayTxt.width = _width - 39;
					
					_hScroller.visible = false;
					_displayTxt.height = _height;
				}
	
				_linesTxt.height = _displayTxt.height;
				_linesTxt.width = 39;
				

	   			var line = _displayTxt.getLineIndexOfChar(_displayTxt.selectionBeginIndex);
		        drawHightlightLine(line, 0xffff00);
			}
			
		}
	
		function setText (str:String)
		{
			try{
				var pattern:RegExp = /\n\r/g;
				str = str.replace(pattern, "\n");
				var pattern:RegExp = /\r\n/g;
				str = str.replace(pattern, "\n");
				trace(str);
	//			Alert.show(str);
				_displayTxt.text = str;
		//		_visible = true;
				if (str) {
					if (auto) highlight ();
				}
			} catch(e:Error) {
				Alert.show(e.message);
			}
		}
		
		function getText():String {
			return _displayTxt.text;
		}
		
		function loadText (url)
		{
			/*
			var lv = new LoadVars ();
			var tl = this;
			lv.onData = function (str)
			{
				tl.setText (str);
			}
			lv.load (url);
			*/
			var str:String;
			str = "var function helloworld(){good}\n\rvar function helloworld(){good}\nvar function helloworld(){good}\nvar function helloworld(){good}\nvar function helloworld(){good}\n";
			
			this.setText(str);//Just for test....
		}
		function convertToTextFormat(o)
		{
			var myo:TextFormat = new TextFormat();
			for (var i in o) {
				switch (i){
					case "color":
						myo[i]=Number("0x"+o[i].substring(1));
						break;
					default:
						myo[i]=(o[i]=="true")? true:false;
						break;
				}
			}
			myo.bold = true;////
			return myo;
		}
		
		private function getLineMetrics():TextLineMetrics {
			if (lineMetrics == null) {
				lineMetrics = _displayTxt.getLineMetrics(_displayTxt.scrollV);
			}
			return lineMetrics;
		}
		
		//显示当前行高亮背景
		private function drawHightlightLine(line:Number, color:Number):void {

			var y = getDisLineHeightByLine(line);
			
			var h = getLineMetrics().height;
	
			_bitmapData = new BitmapData(_displayTxt.width,h,true);
			
			_shadowHolder.y = y;
			_shadowHolder.x = _linesTxt.width;
			
			var block:Shape = new Shape();
			block.graphics.beginFill(color);
			block.graphics.drawRect(0,0,_displayTxt.width,h);
			
			_bitmapData.draw(block);
			_shadowHolder.bitmapData = _bitmapData;
			_shadowHolder.alpha = 0.3;
			
		}
		
		function highlight (){
			hl.format (_displayTxt);
		}

	}
}