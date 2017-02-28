import main.flex.cn.myapps.core.printer.components.DetailCanvas;
import main.flex.cn.myapps.core.printer.components.FooterCanvas;
import main.flex.cn.myapps.core.printer.components.FormCanvas;
import main.flex.cn.myapps.core.printer.components.HeaderCanvas;
import main.flex.cn.myapps.core.printer.components.ItemTextBox;
import main.flex.cn.myapps.core.printer.components.Line;
import main.flex.cn.myapps.core.printer.components.StaticLabel;
import main.flex.cn.myapps.core.printer.components.ViewCanvas;

import flash.events.Event;

import mx.controls.Alert;

private function setProperties(comp:Object): void{
	properties.removeAll();
	if (comp is ItemTextBox){
		properties.addItem({name: "名称", value: comp.name});	
		properties.addItem({name: "标签", value: comp.label});
		properties.addItem({name: "绑定字段", value: comp.bindingField});
		properties.addItem({name: "颜色", value: comp.fillColor.toString(16)});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});		
	}else if(comp is StaticLabel){
		properties.addItem({name: "名称", value: comp.name});
		properties.addItem({name: "标签", value: comp.label});	
		properties.addItem({name: "值", value: comp.textarea.text});
		properties.addItem({name: "颜色", value: comp.fillColor.toString(16)});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});	
	}else if(comp is Line){
		properties.addItem({name: "名称", value: comp.name});
		properties.addItem({name: "Border", value: comp.thickness});
		properties.addItem({name: "颜色", value: comp.lineColor.toString(16)});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});	
	}else if(comp is FormCanvas){
		properties.addItem({name: "名称", value: comp.name});
		properties.addItem({name: "颜色", value: comp.lineColor.toString(16)});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});	
	}else if(comp is DetailCanvas){
		properties.addItem({name: "名称", value: comp.name});
		properties.addItem({name: "重复", value: comp.repeat});
		properties.addItem({name: "颜色", value: comp.lineColor.toString(16)});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});	
	}else if(comp is HeaderCanvas){
		properties.addItem({name: "名称", value: comp.name});
		properties.addItem({name: "显示方式", value: comp.viewStyle});
		properties.addItem({name: "颜色", value: comp.lineColor.toString(16)});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});	
	}else if(comp is FooterCanvas){
		properties.addItem({name: "名称", value: comp.name});
		properties.addItem({name: "显示方式", value: comp.viewStyle});
		properties.addItem({name: "颜色", value: comp.lineColor.toString(16)});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});	
	}else if(comp is ViewCanvas){
		properties.addItem({name: "名称", value: comp.name});
		properties.addItem({name: "绑定视图", value: comp.bindingView});
		properties.addItem({name: "startX坐标", value: comp.startX+comp.x});	
		properties.addItem({name: "startY坐标", value: comp.startY+comp.y});	
		properties.addItem({name: "endX坐标", value: comp.endX+comp.x});	
		properties.addItem({name: "endY坐标", value: comp.endY+comp.y});	
	}
}

private function onPropertyEditEnd(event:Event):void
{
	Alert.show('onPropertyEditEnd');
}
