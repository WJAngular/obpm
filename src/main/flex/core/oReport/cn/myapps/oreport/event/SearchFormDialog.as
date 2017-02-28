
import cn.myapps.oreport.ExplorerCanvas;

import com.google.code.flexiframe.IFrame;
import com.adobe.serialization.json.JSON;

import mx.managers.PopUpManager;
import mx.core.Application;

//图表
public var iframe:IFrame;
public var explorerCanvas:ExplorerCanvas;


//初始化
protected function init():void{
	searchFormIFrame.source="displaySearchForm.action?_viewid="+explorerCanvas.viewid;
}

//查询事件
public function searchEvent(map:Object):void{
	iframe.source="switch.jsp?openType=ExplorerCanvas&type1=ExplorerCanvasFilter&chartType="+explorerCanvas.chartType+"&domainid="+Application.application.domainid;
	cancel();
}

//关闭
public function cancel():void{
	searchFormIFrame.removeIFrame();
	PopUpManager.removePopUp(this);
	iframe.visible = true;
}