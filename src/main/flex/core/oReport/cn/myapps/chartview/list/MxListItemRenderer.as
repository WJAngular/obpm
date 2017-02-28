package cn.myapps.chartview.list
{
	import cn.myapps.chartview.ChartViewCanvas;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.CheckBox;
	import mx.controls.Image;
	import mx.controls.Label;
	import mx.controls.List;
	import mx.core.Application;
	import mx.rpc.remoting.mxml.RemoteObject;

	public class MxListItemRenderer extends HBox
	{
		private var fieldTypeImage:Image;//字段类型的图片
		private var fieldNameLabel:Label;//字段标签
		public var _data:Object;
		
		public var oReportRO:RemoteObject;
		
		public var _xList:List;
		public var _yList:List;
		public var mxListSelectData:ArrayCollection;//mxlist中保存已选择数据
		public var chartViewCanvas:ChartViewCanvas;
		
		public function MxListItemRenderer()
		{
			super();
			horizontalScrollPolicy="off";
			this.height = 30;
			oReportRO = Application.application.oReportRO;
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
				if(_data.icon){
					fieldTypeImage = new Image();
					fieldTypeImage.source = _data.icon;
					addChild(fieldTypeImage);
				}
				if(_data.label){
					fieldNameLabel = new Label();
					fieldNameLabel.text = _data.label;
					addChild(fieldNameLabel);
				}
			}
		}
	}
}