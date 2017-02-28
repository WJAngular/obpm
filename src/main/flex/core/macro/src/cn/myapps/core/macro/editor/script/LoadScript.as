// ActionScript file
		import cn.myapps.core.macro.editor.components.editor.Editor;
		import cn.myapps.core.macro.editor.components.editor.EditorCanvas;
		import cn.myapps.core.macro.editor.components.helper.IFrame;
		
		import flash.events.Event;
		import flash.events.KeyboardEvent;
		import flash.events.MouseEvent;
		import flash.external.ExternalInterface;
		import flash.text.Font;
		import flash.text.TextField;
		
		import mx.collections.ArrayCollection;
		import mx.containers.Panel;
		import mx.controls.Alert;
		import mx.controls.ComboBox;
		
	   	private function onInitialize():void{
	   		initEditor();
	   		initFontList()
	   		initFontSize();
	   		
	   	}
	   	
	   	private function onCreationComplete():void{
	   		this.iscriptContent = ExternalInterface.call("doGetIscriptContent");
	   		this.iscriptLabel = ExternalInterface.call("doGetIscriptLabel");
	   		this.helperLink = ExternalInterface.call("doGetHelperLink");
	   		//for test--------------------------
	   		
	   		
	   		//this.iscriptContent ="asdasdasda";
	   		
	   		
	   		
	   		//for test--------------------------
	   		iFrame.source = this.helperLink;
	   		var editorCanvas:EditorCanvas = EditorCanvas(editor.canvas.getItemAt(0));
	   		if(iscriptLabel) editorCanvas.label =iscriptLabel;
	   		var codeTxt:TextField = this.displayTxt;
	   		codeTxt.addEventListener(Event.CHANGE,onCodeChange);
	   		setText(iscriptContent);
	   		if(iscriptContent){
	   			editorCanvas.historyStack.push(iscriptContent);
	   		}else{
	   			editorCanvas.historyStack.push("");
	   		}
	   		if(iFrame){
	   			iFrame.visible = true;
	   		}
	   		resize();
	   	}
	   	
	   	private function initEditor():void {
	    	editor = new Editor();
	    	var currPanel:EditorCanvas = EditorCanvas(editor.addEditorCanvas(iscriptContent,"Iscript",0));
	    	currPanel.label =iscriptLabel;
	    	middleFrame.addChild(editor);
	    	editor.percentHeight = 100;
	    	editor.percentWidth = 100;
	    }
	    
	    private function initHelper():void {
	    	helper =  new Panel();
	    	helper.title = "帮助";
	    	rightFrame.addChild(helper);
	    	helper.width = rightFrame.width-60;
	    	helper.height = rightFrame.height-60;
	    	iframe = new IFrame();
	    	iframe.source ="http://www.baidu.com";
	    	helper.addChild(iframe);
	    }
	    
	   private function initFontList():void {
		var arr_font:Array = Font.enumerateFonts(true);
		var font_arr:ArrayCollection = new ArrayCollection();
		var i:int = 0;
		while (i < arr_font.length)
		{
	        var font:Font = arr_font[i];
	        font_arr.addItem(font.fontName);
	        i++;
		}
		font_list.dataProvider = font_arr;
		}
		
		private function initFontSize():void {
			var size_arr:ArrayCollection = new ArrayCollection();
			for(var i=12;i<40;i++){
				size_arr.addItem(i);
			}
			font_size.dataProvider = size_arr;
		}
		
	    
	    
	    internal function on_App_KeyDown(event:KeyboardEvent): void{
			var keyCode:uint = event.keyCode;
			var ctrl_down = event.ctrlKey;
			
			if(event.ctrlKey){
			switch (keyCode){
//				case 17 : //ctrl键
//					if(!ctrl_down){
//						if(bt_save.enabled){
//						}
//						ctrl_down = true;
//						trace("ctrl_down: "+ctrl_down.toString());
//					}
//					break;
				case 83 : //s键
					Alert.show('save success!');
					break;
				}
			}
//			if(ctrl_down && s_down && bt_save.enabled){
//				doSave(event);
//				//bt_save.enabled = false;
//				Alert.show('save success!');
//			}
	    }
	    
	    internal function on_App_KeyUp(event:KeyboardEvent): void{
			var keyCode:uint = event.keyCode;
			switch (keyCode){
				case 17 : //ctrl键
					if(ctrl_down){
						ctrl_down = false;
						trace("ctrl_down: "+ctrl_down.toString());
					}
					break;
				case 83 : //s键
				if(s_down){
					s_down = false;
					trace("s_down: "+s_down.toString());
				}
				break;
				}
	    }
	
		
		private function resize() : void {
			if (editor != null) {
				hdbox.width = this.width;
				hdbox.height = this.height  - 40;
				middleFrame.height = hdbox.height;
				middleFrame.width = this.width - leftFrame.width - rightFrame.width-40;
				editor.height = middleFrame.height;
				editor.width = middleFrame.width-16;
				//editor.reSize();
			}
		}
		
	private function onFontChange():void {
		globalFormat.font = font_list.selectedLabel;
		displayTxt.setTextFormat(globalFormat);
	}
	private function onFontSizeChange():void {
		globalFormat.size = font_size.selectedLabel;
		displayTxt.setTextFormat(globalFormat);
	}
	
	private function onControlPanelChange(event:Event):void {
		var label:String = ComboBox(event.target).selectedLabel;
		box_common.visible = false;
		
		switch(label){
			case '常用':
				box_common.visible = true;
				break;
			
		}
	}
	
	
		
	public function getText():String {
		return EditorCanvas(editor.canvas.getItemAt(0)).getText();
	}
	
	public function setText(value:String):void {
		EditorCanvas(editor.canvas.getItemAt(0)).setText(value);
	}
	
	public function on_editor_doubleClick(event:MouseEvent):void{
		if(event.target==event.currentTarget){
			Alert.show('on_editor_doubleClick');
		}
	}
	
	public function onCodeChange(event:Event):void{
		if(event.target==event.currentTarget){
			var editorCanvas:EditorCanvas = EditorCanvas(editor.canvas.getItemAt(0));
			if(editorCanvas.label.indexOf("*")==-1)
				editorCanvas.label = '*'+editorCanvas.label;
			bt_save.enabled = true;
			bt_save_exit.enabled = true;
			msg.text ='';
		}
	}
	
	public function get displayTxt():TextField {
		return EditorCanvas(editor.canvas.getItemAt(0)).displayTxt;
		
	}
	
	public function get globalFormat():TextFormat{
		return EditorCanvas(editor.canvas.getItemAt(0)).globalFormat;
		}