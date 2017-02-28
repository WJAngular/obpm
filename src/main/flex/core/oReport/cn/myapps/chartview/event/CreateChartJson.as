
//json基本信息
protected function baseInfo():String{
	var json:String = "";
	json += "{\"applicationid\":\""+applicationid+"\",";
	if(userid != null  && userid !=""){
		json += "\"userid\":\""+userid+"\",";
	}else{
		json += "\"userid\":\""+adminid+"\",";
	}
	json += "\"viewLabel\":\""+viewLabel+"\",";
	json += "\"viewid\":\""+viewid+"\"";
	return json;
}

//获得x轴json
public function getXJson():String{
	var json:String = "";
	if((_xList.dataProvider as ArrayCollection).length>0){
		json += ",\"xColumn\":";
		for each(var x:Object in (_xList.dataProvider as ArrayCollection)){
			json += "{\"label\":\""+x.label+"\",";
			json += "\"value\":\""+x.value+"\",";
			json += "\"fieldtype\":\""+x.fieldType+"\",";
			json += "\"fx\":\""+x.mfx+"\"}";
		}
	}
	return json;
}

public function getYJson():String{
	var json:String = "";
	if((_yList.dataProvider as ArrayCollection).length>0){
		json+=",\"yColumn\":[";
		for each(var y:Object in (_yList.dataProvider as ArrayCollection)){
			json+="{\"label\":\""+y.label+"\",";
			json+="\"value\":\""+y.value+"\",";
			json+="\"fieldtype\":\""+y.fieldType+"\",";
			json+="\"fx\":\""+y.mfx+"\"},";
		}
		if((_yList.dataProvider as ArrayCollection).length>0){
			json=json.substring(0,json.lastIndexOf(","));
		}
		json+="]}";
	}
	return json;
}

//获得创建报表的Json
public function createChartJson():String{
	var json:String = "";
	var j:int=0;
	json += baseInfo();
	if((_xList.dataProvider as ArrayCollection).length>0){
		json += ",\"xColumn\":";
		for each(var x:Object in (_xList.dataProvider as ArrayCollection)){
			json += "{\"label\":\""+x.label+"\",";
			json += "\"value\":\""+x.value+"\",";
			json += "\"fieldtype\":\""+x.fieldType+"\",";
			json += "\"fx\":\""+x.mfx+"\"}";
		}
	}
	if((_yList.dataProvider as ArrayCollection).length>0){
		json+=",\"yColumn\":[";
		for each(var y:Object in (_yList.dataProvider as ArrayCollection)){
			json+="{\"label\":\""+y.label+"\",";
			json+="\"value\":\""+y.value+"\",";
			json+="\"fieldtype\":\""+y.fieldType+"\",";
			json+="\"fx\":\""+y.mfx+"\"},";
		}
		if((_yList.dataProvider as ArrayCollection).length>0){
			json=json.substring(0,json.lastIndexOf(","));
		}
		json+="]";
	}
	json+="}";
	return json;
}

//判断x轴或y轴
public function checkXY():Boolean{
	if((_yList.dataProvider as ArrayCollection).length>1&&(_xList.dataProvider as ArrayCollection).length==0){
		Application.application.showMessage("assets/warning.png","X轴不能为空!");
		return false;
	}
	if((_yList.dataProvider as ArrayCollection).length==0&&(_xList.dataProvider as ArrayCollection).length==0){
		Application.application.showMessage("assets/warning.png","X轴或Y轴不能为空!");
		return false;
	}
	return true;
}