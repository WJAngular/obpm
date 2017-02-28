package cn.myapps.core.macro.editor.components.editor
{
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.text.TextField;
	import flash.text.TextFormat;
	
	import mx.containers.Canvas;
	import mx.core.UIComponent;

	/**
	 *代码编辑容器 用于显示编辑面板的其中一个代码编辑器 
	 * @author Happy
	 * 
	 */
	public class EditorCanvas extends Canvas
	{
		 [Embed(source="../../assets/scripticon.gif")]
         [Bindable]
         public var imgCls:Class;
         
         private var _lableStr:String = "Iscript";
         
         private var _codeEditor:CodeEditor;
         
         public var historyStack:Array = new Array();
         
         public var stackSize:Number = 30;
         
         public var index:Number = 0;
         
		public function EditorCanvas(iscriptContent:String ,_lable:String,goLine:int)
		{
			this.codeEditor = new CodeEditor();
			if(iscriptContent)
				this.setText(iscriptContent);
			//this.goLine(goLine);
			this.setReadonly(false);
			this.lableStr =_lable;
			this.label = this.lableStr;
			this.icon = imgCls;
			this.percentWidth =100;
			this.percentHeight =100;
			this.addChild(codeEditor);
			super();
			//this.addEventListener(ResizeEvent.RESIZE,resize);
			
			displayTxt.addEventListener(KeyboardEvent.KEY_DOWN,onKeyDown);
			displayTxt.addEventListener(KeyboardEvent.KEY_UP,onKeyUp);
			displayTxt.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,onKeyFocusChange);
		}
		
		  public function get codeEditor():CodeEditor
		{
			return _codeEditor;
		}

		public function set codeEditor(value:CodeEditor):void
		{
			_codeEditor = value;
		}
		
		public function get lableStr():String
		{
			return _lableStr;
		}

		public function set lableStr(value:String):void
		{
			_lableStr = value;
		}
		
		
		public function getText():String {
			return codeEditor.getText();
		}
		
		public function setText(value:String):void {
			if(value!=null)
				codeEditor.setText(value);
		}
		
		public function setReadonly(value:Boolean):void {
			codeEditor.setReadonly(value);
		}
		
		public function goLine(line:Number):void {
			codeEditor.goLine(line);
		}
		
		private function resize(event:Event):void {
			this.width = this.parent.width;
			this.height = this.parent.height;
			codeEditor.height =this.height-35;
			codeEditor.width = this.width-2;
		}
		
		public function reSize(pWidth:Number,pHeight:Number):void {
			this.width = pWidth;
			this.height = pHeight;			
			codeEditor.height =this.height-35;
			codeEditor.width = this.width-2;
		}
		
		public function get displayTxt():TextField {
			return this.codeEditor._flashTextFormater.displayTxt;
		}
		
		public function get globalFormat():TextFormat{
			return this.codeEditor._flashTextFormater.globalFormat;
		}
		
		
				/**
		 *键盘 KeyDown 事件监听
		 * @param event
		 * 
		 */
		public function onKeyDown(event:KeyboardEvent):void{
			if(event.target == event.currentTarget){
				var keyCode:uint = event.keyCode;
				trace(keyCode.toString());
				
				switch (keyCode){
					case 9://tab
						insertString("\t");
						break;
					case 13://enter
						//this.codeEditor._flashTextFormater.maxLine = this.codeEditor._flashTextFormater.maxLine+1;
						break;
				}
				
			}
		}
		
			
		/**
		 *键盘 KeyUp 事件监听
		 * @param event
		 * 
		 */
		public function onKeyUp(event:KeyboardEvent):void{
			this.codeEditor._flashTextFormater.onChangeLine(event);
			if(event.target == event.currentTarget){
				var keyCode:uint = event.keyCode;
				trace(keyCode.toString());
				switch (keyCode){
					case 13://enter
						event.preventDefault();//取消默认事件处理
						enter_KeyDown_Handle();//处理按下回车键 
						break;
				}
				if(historyStack.length>0){
					if(displayTxt.text!=historyStack[historyStack.length-1].toString() ){
						updateHistory();
					}
				}else if(displayTxt.text.length>0){
					updateHistory();
				} 
			}
		}
		
		 /**
		  *在输入框光标处插入字符串 
		  * @param insertStr
		  * 
		  */
		 public function insertString(insertStr:String):void {
                if (displayTxt.selectionBeginIndex == displayTxt.selectionEndIndex) {
                    var startPart:String=displayTxt.text.substring(0, displayTxt.selectionBeginIndex);//当前光标前面部分的内容
                    var endPart:String=displayTxt.text.substring(displayTxt.selectionEndIndex, displayTxt.text.length);//当前光标后面部分的内容
                    startPart+=insertStr;
                    startPart+=endPart;
                    displayTxt.setSelection(displayTxt.selectionBeginIndex+insertStr.length,displayTxt.selectionBeginIndex+insertStr.length);//移动光标位置
                   displayTxt.text=startPart;
                   this.codeEditor._flashTextFormater.formatter.format(displayTxt);
                }
                else//否则
                {
                   //displayTxt.text=insertStr;
                }
            }
            
            
        /**
         *处理回车键按下事件
         * @return 
         * 
         */
        private function enter_KeyDown_Handle(){
        	var line = 0;
        	if(displayTxt.selectionEndIndex==displayTxt.text.length){
        		this.codeEditor._flashTextFormater.calculateMaxLine();
        		trace("maxLine================>>"+maxLine);
	        	line = displayTxt.getLineIndexOfChar(displayTxt.selectionEndIndex-1)+1;
	        	if(line>maxLine)
	        	{
	        		line = maxLine;
	        	}
	        }else{
	        	line = displayTxt.getLineIndexOfChar(displayTxt.selectionEndIndex);
	        }
        	if(line>=1){
				trace('line----->'+line);
				trace('getLineText------up------>'+displayTxt.getLineText(line));
				trace('textLength--------up---->'+displayTxt.getLineText(line-1).length.toString());
				trace('indexOf--------up---->'+displayTxt.getLineText(line-1).indexOf("	").toString());
				var lineText:String = displayTxt.getLineText(line-1);
				var aotoAppent:Boolean = false;
				var c:int=0;
				for(var i=0;i<lineText.length;i++){
					if(lineText.charAt(i)=='\t'){
						c++;
					}else{
						break;
					}
				}
				
				if(lineText.indexOf("{")>-1){
					aotoAppent =  true;
					c++;
				}
				
				
				if(c>0){
					for(var j=0;j<c;j++){
						insertString("\t");
					}
				}
				if(aotoAppent){//自动追加圆括号
					insertString("\n");
					if(c>0){
						for(var j=1;j<c;j++){
							insertString("\t");
						}
						
					}else{
						insertString("\t");
					}
					insertString("}");
					displayTxt.setSelection(displayTxt.selectionBeginIndex-c-1,displayTxt.selectionBeginIndex-c-1);//移动光标位置
				}
        	}
			
        }
            
		/**
		 *焦点改变事件处理 
		 * @param event
		 * 
		 */
		private function onKeyFocusChange(event:FocusEvent):void{
			var txt:TextField = event.target  as  TextField;
			if(true) {
		    	event.preventDefault();//不允许使用Tab键把焦点移除
		  	} 
		}
		
		
		public function get maxLine():Number{
			return this.codeEditor._flashTextFormater.maxLine;
		}
		
		public function updateHistory():void{
			if(historyStack.length>=stackSize){
				historyStack.splice(0,1);
			}
			historyStack.push(displayTxt.text);
			index = historyStack.length -2; 
		}
		
		public function redo(obj:UIComponent):void { 
			if(historyStack.length>0 && index<historyStack.length-1){
				setText(historyStack[index+2]); 
				index++;
			}
			
       } 
		public function undo(obj:UIComponent):void { 
        	if(historyStack.length>0 && index>=0){
	            setText(historyStack[index]);
	            if(index>0)index--;
	            //else obj.enabled = false;
	            
           } 
   		} 

		
		
		
		
		
	}
}