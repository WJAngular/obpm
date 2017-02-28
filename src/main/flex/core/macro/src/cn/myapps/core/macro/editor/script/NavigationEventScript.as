// ActionScript file
import cn.myapps.core.macro.editor.components.editor.EditorCanvas;

import flash.events.MouseEvent;

import mx.events.MenuEvent;



/**
 *处理导航类按钮点击事件 
 * @param event
 * 
 */
public function navigationBt_itemClick(event:MenuEvent):void
{
}


/**
 * 保存
 * @param event
 * 
 */
public function doSave(event:MouseEvent):void{
	var code:String=this.getText();
	var result:String = ExternalInterface.call("doEditorSave",code,true);
	var editorCanvas:EditorCanvas = EditorCanvas(editor.canvas.getItemAt(0));
			if(editorCanvas.label.indexOf("*")>-1)
				editorCanvas.label = editorCanvas.label.replace('*','');
	bt_save.enabled = false;
	bt_save_exit.enabled = false;
	if(result){
		msg.text =result;
		
	}
	if(event.target.id=='bt_save_exit'){
		this.callLater(closeIE);
	}
}
private function closeIE():void{
	var request:URLRequest = new URLRequest("javascript:OBPM.dialog.doExit()");
	navigateToURL(request,"_self");
}
