package com.datagrid
{
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.CheckBox;
	import mx.core.Application;

	public class FileNameItemRenderer extends HBox
	{
		public var _data:Object;
		public var fileNameCheckBox:CheckBox;
		
		public function FileNameItemRenderer()
		{
			super();
			horizontalScrollPolicy = "off";
			verticalScrollPolicy = "off";
		}
		
		//获取值
		override public function get data():Object{
			if(_data!=null){
				return _data;	
			}
			return null;
		} 
		
		//赋值
		override public function set data(value:Object):void{
			_data = value;
			removeAllChildren();
			if(_data!=null){
				fileNameCheckBox = new CheckBox();
				for each(var i:Object in Application.application.selectFileAC){
					if(String(i.path) == String(data.path)){
						fileNameCheckBox.selected = true;
						break;
					}
				}
				fileNameCheckBox.label = _data.name;
				fileNameCheckBox.addEventListener(MouseEvent.CLICK,changeSelectEvent);
				addChild(fileNameCheckBox);
			}
		}
		
		//改变选择事件
		protected function changeSelectEvent(event:MouseEvent):void{
			if(fileNameCheckBox.selected){
				Application.application.selectFileAC.addItem(data);
			}else{
				for each(var i:Object in Application.application.selectFileAC){
					if(String(i.path) == String(data.path)){
						Application.application.selectFileAC.removeItemAt(Application.application.selectFileAC.getItemIndex(i));
					}
				}
			}
		}
	}
}