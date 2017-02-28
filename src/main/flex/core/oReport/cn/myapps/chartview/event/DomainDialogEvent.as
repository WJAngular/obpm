import cn.myapps.chartview.ChartViewCanvas;

import com.adobe.serialization.json.JSON;

import mx.core.Application;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;

public var openType:String;//打开类型
public var _chartViewCanvas:ChartViewCanvas;

//初始化
protected function init():void{
	Application.application.oReportRO.addEventListener(ResultEvent.RESULT,getDomainResult);
	Application.application.oReportRO.getDomain(Application.application.applicationid);
}

//企业域结果
protected function getDomainResult(event:ResultEvent):void{
	Application.application.oReportRO.removeEventListener(ResultEvent.RESULT,getDomainResult);
	if(event.result.toString()!=""){
		var obj:Object = JSON.decode(event.result.toString());
		if(obj.message==null){
			domainCombobox.dataProvider = obj.domain;
		}else{
			Application.application.showMessage(obj.icon,obj.message);
		}
	}
}

//确定
protected function ok(event:MouseEvent):void{
	if(domainCombobox.selectedItem.id != null){
		var _previewDialog:PreviewDialog = PopUpManager.createPopUp(Application.application as DisplayObject,PreviewDialog,true) as PreviewDialog;
		_previewDialog.domainid = domainCombobox.selectedItem.id;
		_previewDialog.isPopUp = false;
		_previewDialog.openType = openType;
		_previewDialog._chartViewCanvas = _chartViewCanvas;
		PopUpManager.centerPopUp(_previewDialog);
		cancel();
	}else{
		Application.application.showMessage("assets/warning.png","请选择");
	}
}

//取消
protected function cancel():void{
	PopUpManager.removePopUp(this);
}