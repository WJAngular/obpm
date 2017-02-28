package cn.myapps.chartview.list
{
	import cn.myapps.chartview.ChartViewCanvas;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.ComboBox;
	import mx.controls.Label;
	import mx.controls.LinkButton;
	import mx.controls.List;
	import mx.events.ListEvent;
	
	public class XYListItemRenderer extends HBox
	{
		[Embed("../../assets/close.gif")]
		private var closeimg:Class;
		private var _data:Object;
		private var fieldNameLable:Label;//字段名称
		private var fxLabel:Label;//函数标签
		private var fxComboBox:ComboBox;//函数
		private var closeButton:LinkButton;//关闭
		
		public var _mxList:List;
		public var _xList:List;
		public var _yList:List;
		
		public function XYListItemRenderer()
		{
			super();
			this.height = 30;
			horizontalScrollPolicy = "off";
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
			this.removeAllChildren();
			if(_data){
				if(_data.label){
					fieldNameLable = new Label();
					fieldNameLable.width = 95;
					fieldNameLable.text = _data.label;
					addChild(fieldNameLable);
				
					fxLabel = new Label();
					fxLabel.text = "fx:";
					fxLabel.setStyle("color","blue");
					addChild(fxLabel);
					
					fxComboBox = new ComboBox();
					fxComboBox.width = 80;
					fxComboBox.dataProvider = _data.mfxarr;
					//回选值
					for each(var i:Object in _data.mfxarr){
						if(i.data == _data.mfx){
							fxComboBox.selectedItem = i;
							break;
						}
					}
					fxComboBox.addEventListener(ListEvent.CHANGE,changemfxEvent);
					addChild(fxComboBox);
					
					closeButton = new LinkButton;
					closeButton.addEventListener(MouseEvent.CLICK,remoteItemEvnt);
					closeButton.setStyle("icon",closeimg);
					addChild(closeButton);
				}
			}
		}
		//改变函数
		private function changemfxEvent(event:ListEvent):void{
			_data.mfx = ComboBox(event.target).selectedItem.data;
		}
		
		//移除项
		private function remoteItemEvnt(event:MouseEvent):void{
			var arrayCollection:ArrayCollection = (owner as List).dataProvider as ArrayCollection;
			arrayCollection.removeItemAt(arrayCollection.getItemIndex(_data));
			if((owner as List).id=="_xList"){
				(owner as List).dropEnabled = true;
			}
		}

	}
}