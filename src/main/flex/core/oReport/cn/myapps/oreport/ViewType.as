package cn.myapps.oreport
{
	import flash.events.MouseEvent;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Image;
	import mx.controls.Label;
	import mx.controls.LinkButton;
	import mx.controls.Spacer;
	import mx.managers.PopUpManager;
	
	public class ViewType extends Canvas
	{
		public var dataSource:DataSource;
		
		//图表视图
		public var chartViewHBox:HBox;
		public var chartViewImage:Image;
		public var chartViewVBox:VBox;
		public var chartViewButton:LinkButton;
		public var chartViewLabel:Label;
	
		public function ViewType()
		{
			super();
			this.percentHeight = 100;
			this.percentWidth = 100;
		}
		
		//创建孩子
		override protected function createChildren():void{
			super.createChildren();
			
			//图表视图
			chartViewHBox = new HBox();
			chartViewHBox.y = 5;
			chartViewHBox.x = 20;
			chartViewHBox.percentWidth = 50;
			chartViewHBox.height = 100;
			
			chartViewImage = new Image();
			chartViewImage.percentHeight = 100;
			chartViewImage.source = "assets/ViewType/chartView.png";
			chartViewImage.addEventListener(MouseEvent.CLICK,chartViewEvent);
			
			chartViewVBox = new VBox();
			chartViewVBox.percentHeight = 100;
			chartViewButton = new LinkButton();
			chartViewButton.label = "图表视图";
			chartViewButton.setStyle("textDecoration","underline");
			chartViewButton.addEventListener(MouseEvent.CLICK,chartViewEvent);
			chartViewLabel = new Label();
			chartViewLabel.text = "图表视图可以让您用不同的图表类型创建图形视图。";
			chartViewLabel.addEventListener(MouseEvent.CLICK,chartViewEvent);
			chartViewVBox.addChild(chartViewButton);
			chartViewVBox.addChild(chartViewLabel);
			
			chartViewHBox.addChild(chartViewImage);
			chartViewHBox.addChild(chartViewVBox);
			
			addChild(chartViewHBox);

		}
		
		//表格视图点击事件
		protected function tableViewEvent(event:MouseEvent):void{
			
		}
		
		//图表视图点击事件
		protected function chartViewEvent(event:MouseEvent):void{
			dataSource = PopUpManager.createPopUp(this,DataSource ,true) as DataSource;
			dataSource.viewType = "chartView";
			dataSource._ViewType = this;
			PopUpManager.centerPopUp(dataSource);
		}
		
		//透视视图点击事件
		protected function perspectiveViewEvent(event:MouseEvent):void{
			
		}
		
		//汇总视图点击事件
		protected function summaryViewEvent(event:MouseEvent):void{
			
		}
		
		//仪表板点击事件
		protected function dashboardViewEvent(event:MouseEvent):void{
			
		}
		
	}
}