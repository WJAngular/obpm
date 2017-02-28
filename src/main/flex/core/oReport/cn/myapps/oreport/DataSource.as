package cn.myapps.oreport
{
	import cn.myapps.chartview.ChartViewCanvas;
	
	import com.adobe.serialization.json.JSON;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import flexlib.containers.SuperTabNavigator;
	
	import mx.containers.TitleWindow;
	import mx.controls.ComboBox;
	import mx.controls.Label;
	import mx.controls.LinkButton;
	import mx.core.Application;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	public class DataSource extends TitleWindow
	{
		[Embed("../assets/ok.png")]
		private var okimg:Class;
		[Embed("../assets/cancel.png")]
		private var cancelimg:Class;
		
		public var _superTabNavigator:SuperTabNavigator;//超级tabNavigator
		
		public var viewType:String;//视图类型
		public var _ViewType:ViewType;
		public var chartViewCanvas:ChartViewCanvas;//图表对象
		
		private var applicationid:String;//软件编号
		public var moduleid:String;
		private var oReportRO:RemoteObject;//请求服务器
		
		private var dataSourceTypeLabel:Label;//数据源类型标签
		private var dataSourceTypeComboBox:ComboBox;//数据源类型
		private var moduleLabel:Label;//模块标签
		private var moduleComboBox:ComboBox;//模块
		private var dataSourceLabel:Label;//数据源标签
		private var dataSourceCombobox:ComboBox;//数据源
		private var okButton:LinkButton;//确定
		private var cancelButton:LinkButton;//取消
		
		//构造函数
		public function DataSource()
		{
			super();
			moduleid = Application.application.moduleid;
			applicationid = Application.application.applicationid;
			oReportRO = Application.application.oReportRO;
			_superTabNavigator = Application.application.superTabNavigator;
			showCloseButton = true;//显示关闭按钮
			addEventListener(CloseEvent.CLOSE,cancelEvent);
			width = 376;
			height = 210;
			layout="absolute";
			title = "选择数据源";
			
		}
		
		
		//创建孩子
		override protected function createChildren():void{
			super.createChildren();
			
			//数据源类型标签
			dataSourceTypeLabel = new Label;
			dataSourceTypeLabel.y = 12;
			dataSourceTypeLabel.text = "数据源类型:";
			
			//数据源类型
			dataSourceTypeComboBox = new ComboBox();
			dataSourceTypeComboBox.x = 88;
			dataSourceTypeComboBox.y = 10;
			dataSourceTypeComboBox.width = 238;
			dataSourceTypeComboBox.dataProvider = JSON.decode('[{"label":"视图","data":"view"}]');
			dataSourceTypeComboBox.addEventListener(ListEvent.CHANGE,dataSourceTypeChangeEvent);
			
			
			//模块标签
			moduleLabel = new Label();
			moduleLabel.y = 40;
			moduleLabel.text = "模块:";
			
			//模块
			moduleComboBox = new ComboBox();
			moduleComboBox.x = 88;
			moduleComboBox.y = 40;
			moduleComboBox.width = 238;
			moduleComboBox.addEventListener(ListEvent.CHANGE,moduleChangeEvent);
			
			//数据源
			dataSourceLabel = new Label();
			dataSourceLabel.y = 70;
			dataSourceLabel.text = "数据源:";
			
			//数据源
			dataSourceCombobox = new ComboBox();
			dataSourceCombobox.x = 88;
			dataSourceCombobox.y = 70;
			dataSourceCombobox.width = 238;
			
			//确定按钮
			okButton = new LinkButton();
			okButton.x = 127;
			okButton.y = 120;
			okButton.label = "确定";
			okButton.setStyle("icon",okimg);
			okButton.setStyle("fontWeight","normal");
			okButton.addEventListener(MouseEvent.CLICK,okEvent);
			//取消按钮
			cancelButton = new LinkButton();
			cancelButton.x = 204;
			cancelButton.y = 120;
			cancelButton.label = "取消";
			cancelButton.setStyle("icon",cancelimg);
			cancelButton.setStyle("fontWeight","normal");
			cancelButton.addEventListener(MouseEvent.CLICK,cancelEvent);
			
			addChild(dataSourceTypeLabel);
			addChild(dataSourceTypeComboBox);
			addChild(moduleLabel);
			addChild(moduleComboBox);
			addChild(dataSourceLabel);
			addChild(dataSourceCombobox);
			addChild(okButton);
			addChild(cancelButton);
			
			
			oReportRO.addEventListener(ResultEvent.RESULT,getModulDataResult);
			oReportRO.getModuleByApplictionid(applicationid,moduleid);
		}
		
		//赋值给模块
		protected function getModulDataResult(event:ResultEvent):void{
			oReportRO.removeEventListener(ResultEvent.RESULT,getModulDataResult);
			if(event.result.toString()!=""){
				var obj:Object = JSON.decode(event.result.toString());
				if(obj.message==null){
					moduleComboBox.dataProvider = obj.module;
					dataSourceCombobox.dataProvider = obj.view
					dataSourceCombobox.selectedIndex = 0;
				}else{
					Application.application.showMessage(obj.icon,obj.message);
				}
			}
		}
		
		//赋值给视图
		protected function getViewDataResult(event:ResultEvent):void{
			oReportRO.removeEventListener(ResultEvent.RESULT,getViewDataResult);
			if(event.result.toString()!=""){
				var obj:Object = JSON.decode(event.result.toString());
				if(obj.message==null){
					dataSourceCombobox.dataProvider = obj.view;
					dataSourceCombobox.selectedIndex = 0;
				}else{
					Application.application.showMessage(obj.icon,obj.message);
				}
			}
		}
		
		
		//数据源类型改变事件
		protected function dataSourceTypeChangeEvent(event:ListEvent):void{
			
		}
		
		//模块改变事件
		protected function moduleChangeEvent(event:ListEvent):void{
			oReportRO.addEventListener(ResultEvent.RESULT,getViewDataResult);
			oReportRO.getViewByModuleid(moduleComboBox.selectedItem.id,applicationid);
		}
		
		
		//确定按钮
		protected function okEvent(event:MouseEvent):void{
			if(dataSourceCombobox.selectedItem.id!=""&&dataSourceCombobox.selectedItem.id!=null){
				if(viewType=="chartView"){
					oReportRO.addEventListener(ResultEvent.RESULT,getViewColumnsByViewArrayResult);
					oReportRO.getViewColumnsByViewArray(dataSourceCombobox.selectedItem.id);
				}else if(viewType=="tableView"){
					
				}
			}
		}
		
		//数据库中返回的字段  图表视图
		protected function getViewColumnsByViewArrayResult(event:ResultEvent):void{
			oReportRO.removeEventListener(ResultEvent.RESULT,getViewColumnsByViewArrayResult);
			var obj:Object;
			//提示信息
			if(event.result.toString()!=""){
				obj = JSON.decode(event.result.toString());
				if(obj.message!=null){
					Application.application.showMessage(obj.icon,obj.message);
					return;
				}
				var _chartViewCanvas:ChartViewCanvas = new ChartViewCanvas();
				_chartViewCanvas.label = "新建图表视图--"+dataSourceCombobox.selectedItem.label;
				_chartViewCanvas.openType = "ChartViewCanvasCreate";
				_chartViewCanvas.moduleid = moduleComboBox.selectedItem.id;
				_chartViewCanvas.viewid = dataSourceCombobox.selectedItem.id;
				_chartViewCanvas.viewLabel =dataSourceCombobox.selectedItem.label;
				_chartViewCanvas.objData = obj;
				if(_ViewType!=null){
					_superTabNavigator.removeChild(_ViewType);
				}
				_superTabNavigator.addChild(_chartViewCanvas);
				_superTabNavigator.selectedChild = _chartViewCanvas;
			}else{
				Application.application.showMessage("assets/warning.png","视图 '"+dataSourceCombobox.selectedItem.label+"' 没有列");
			}
			//关闭
			cancelEvent(event);
			
		}
		
		//关闭
		protected function cancelEvent(event:Event):void{
			PopUpManager.removePopUp(this);
		}
		
	}
}