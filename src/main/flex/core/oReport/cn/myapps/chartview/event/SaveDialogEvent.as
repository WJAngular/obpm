import cn.myapps.chartview.ChartViewCanvas;

import com.adobe.serialization.json.JSON;

import mx.core.Application;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

public var oReportRO:RemoteObject;
public var applicationid:String;
public var moduleid:String;
public var viewVOid:String;
public var chartViewCanvas:ChartViewCanvas;
private var userid:String;
//初始化完成
protected function init():void{
	oReportRO = Application.application.oReportRO;
	applicationid = Application.application.applicationid;
	userid = Application.application.userId;
	moduleid = Application.application.moduleid;
	
	if(viewVOid!=null&&viewVOid!=""){
		this.title = "重命名";
		oReportRO.addEventListener(ResultEvent.RESULT,getViewVOResult);
		oReportRO.getViewVO(viewVOid);
	}else{
		this.title = "另存为";
	}
}

//根据id获得视图
protected function getViewVOResult(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,getViewVOResult);
	if(event.result.toString()!=""){
		var obj:Object = JSON.decode(event.result.toString());
		if(obj.message!=null){
			Application.application.showMessage(obj.icon,obj.message);
		}else{
			nameTextInput.text = obj.name;
			descriptionTextArea.text =  obj.description;
		}
	}
}

//保存事件
protected function saveEvent():void{
	if(chartViewCanvas!=null){
		//检查xy轴
		if(!chartViewCanvas.checkXY()){
			return;
		}
	}
	if(nameTextInput.text!=""){
		if(chartViewCanvas!=null){
			chartViewCanvas.viewLabel = nameTextInput.text;
		}
		if(descriptionTextArea.text!=""){
			oReportRO.addEventListener(ResultEvent.RESULT,saveResult);
			if(viewVOid!=null&&viewVOid!=""){//重命名视图
				oReportRO.doSaveViewVO("","",viewVOid,nameTextInput.text,descriptionTextArea.text,"","");
			}else{//另存为视图
				oReportRO.doSaveViewVO(applicationid,moduleid,"",nameTextInput.text,descriptionTextArea.text,chartViewCanvas.createChartJson(),userid);
			}
		}else{
			Application.application.showMessage("assets/warning.png","报表备注不能为空!");
		}
	}else{
		Application.application.showMessage("assets/warning.png","报表名称不能为空!");
	}
}

//保存结果
protected function saveResult(event:ResultEvent):void{
	oReportRO.removeEventListener(ResultEvent.RESULT,saveResult);
	if(event.result.toString()!=""){
		var obj:Object = JSON.decode(event.result.toString());
		if(obj.message!=null){
			Application.application.showMessage(obj.icon,obj.message);
		}
		//另存为图表赋值id给viewVOid，实现判断已保存了该视图
		if(chartViewCanvas!=null){
			chartViewCanvas.viewVOid = obj.viewVOid;
			chartViewCanvas.label = nameTextInput.text;
		}
	}
	
	oReportRO.addEventListener(ResultEvent.RESULT,Application.application.getAllViewVOEvent);
	oReportRO.getAllViewVO(applicationid,moduleid,"false",userid);
	
	closeEvent();
}

//关闭
protected function closeEvent():void{
	PopUpManager.removePopUp(this);
	if(Application.application._explorerCanvas.iframe!=null){
 		Application.application._explorerCanvas.iframe.visible = true;
 	}
}