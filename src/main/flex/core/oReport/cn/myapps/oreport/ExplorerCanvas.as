package cn.myapps.oreport
{
	import cn.myapps.oreport.tree.TreeCheckboxImageItemRender;
	
	import com.google.code.flexiframe.IFrame;
	
	import flash.events.MouseEvent;
	
	import flexlib.containers.SuperTabNavigator;
	
	import mx.containers.ApplicationControlBar;
	import mx.containers.Canvas;
	import mx.controls.Label;
	import mx.controls.LinkButton;
	import mx.controls.Tree;
	import mx.controls.VRule;
	import mx.core.Application;
	import mx.core.ClassFactory;
	import mx.rpc.remoting.mxml.RemoteObject;

	public class ExplorerCanvas extends Canvas
	{
		[Embed("../assets/new.png")]
		private var newimg:Class;
		[Embed("../assets/delete.png")]
		private var deleteimg:Class;
		[Embed("../assets/rename.png")]
		private var renameimg:Class;
		
		[Embed("../assets/chartView/AreaChart.png")]
		private var areachartimg:Class;
		
		[Embed("../assets/chartView/BarChart.png")]
		private var barchartimg:Class;
		
		
		[Embed("../assets/chartView/ColumnChart.png")]
		private var columnchartimg:Class;
		
		[Embed("../assets/chartView/LineChart.png")]
		private var linechartimg:Class;
		
		[Embed("../assets/chartView/PieChart.png")]
		private var piechartimg:Class;
		
		[Embed("../assets/chartView/PlotChart.png")]
		private var plotchartimg:Class;
		
		[Embed("../assets/chartView/Table.png")]
		private var tableimg:Class;
		
		[Embed("../assets/search.png")]
		private var searchimg:Class;
		
		[Embed("../assets/report_go.png")]
		private var exportimg:Class;
		
		[Embed("../assets/printer.png")]
		private var printimg:Class;
		
		[Embed("../assets/reset.png")]
		private var resetimg:Class;
		
		include "event/ExplorerCanvasEvent.as";
		
		private var applicationid:String;//软件编号
		private var oReportRO:RemoteObject;//请求服务器
		private var superTabNavigator:SuperTabNavigator;//超级
		public var chartType:String="PlotChart";
		public var viewid:String;
		public var json:String="";
		public var map:Object;
		private var userid:String;
		
		public var explorerCanvas:Canvas;//资源管理器容器
		public var _ApplicationControlBar:ApplicationControlBar;//菜单栏
		public var newButton:LinkButton;//新建
		public var deleteButton:LinkButton;//删除
		public var renameButton:LinkButton;//重命名
		public var _label:Label;
		public var selectAllButton:LinkButton;//全选
		public var clearButton:LinkButton;//清空
		public var folderTree:Tree;//文件夹树
		
		public var viewChartCanvas:Canvas;//图表容器
		public var veiwChartApplicationControlBar:ApplicationControlBar;
		public var searchButton:LinkButton;//查询
		public var backButton:LinkButton;//返回
		public var exportButton:LinkButton;//导出
		public var printButton:LinkButton;//打印
		public var vrule:VRule;
		public var pieChartButton:LinkButton;//馅饼图按钮
		public var barChartButton:LinkButton;//条形图按钮
		public var areaChartButton:LinkButton;//面积图按钮
		public var columnChartButton:LinkButton;//柱状图按钮
		public var lineChartButton:LinkButton;//折线图按钮
		public var plotChartButton:LinkButton;//散点图按钮
		public var iframe:IFrame;//iframe窗口
		public var searchFormDialog:SearchFormDialog;//查询表单
		
		public function ExplorerCanvas()
		{
			super();
			percentWidth = 100;
			percentHeight = 100;
			setStyle("borderStyle","solid");
			horizontalScrollPolicy="off";
			verticalScrollPolicy="off";
			applicationid = Application.application.applicationid;
			oReportRO = Application.application.oReportRO;
			superTabNavigator = Application.application.superTabNavigator;
			userid = Application.application.userId;
		}
		
		//创建孩子
		override protected function createChildren():void{
			super.createChildren();
			
			//资源管理器容器
			explorerCanvas = new Canvas();
			explorerCanvas.percentWidth = 100;
			explorerCanvas.percentHeight = 100;
			explorerCanvas.setStyle("borderStyle","solid");
			addChild(explorerCanvas);
			
			_ApplicationControlBar = new ApplicationControlBar();
			_ApplicationControlBar.percentWidth = 100;
			explorerCanvas.addChild(_ApplicationControlBar);
			
			newButton = new LinkButton();
			newButton.label = "新建报表";
			newButton.setStyle("fontWeight","normal");
			newButton.setStyle("textDecoration","underline");
			newButton.setStyle("icon",newimg);
			newButton.addEventListener(MouseEvent.CLICK,newEvent);
			_ApplicationControlBar.addChild(newButton);
			
			deleteButton = new LinkButton();
			deleteButton.label = "删除";
			deleteButton.setStyle("fontWeight","normal");
			deleteButton.setStyle("textDecoration","underline");
			deleteButton.setStyle("icon",deleteimg);
			deleteButton.addEventListener(MouseEvent.CLICK,deleteEvent);
			_ApplicationControlBar.addChild(deleteButton);
			
			renameButton = new LinkButton();
			renameButton.label = "重命名";
			renameButton.setStyle("fontWeight","normal");
			renameButton.setStyle("textDecoration","underline");
			renameButton.setStyle("icon",renameimg);
			renameButton.addEventListener(MouseEvent.CLICK,renameEvent);
			_ApplicationControlBar.addChild(renameButton);
			
			
			_label = new Label();
			_label.text="选择：";
			_label.x = 10;
			_label.y = 40;
			explorerCanvas.addChild(_label);
			
			selectAllButton = new LinkButton();
			selectAllButton.y=70;
			selectAllButton.x = 10;
			selectAllButton.label = "全选";
			selectAllButton.setStyle("fontWeight","normal");
			selectAllButton.setStyle("textDecoration","underline");
			selectAllButton.addEventListener(MouseEvent.CLICK,selectAllEvent);
			explorerCanvas.addChild(selectAllButton);
			
			clearButton = new LinkButton();
			clearButton.y = 70;
			clearButton.x = 100;
			clearButton.label = "清空";
			clearButton.setStyle("fontWeight","normal");
			clearButton.setStyle("textDecoration","underline");
			clearButton.addEventListener(MouseEvent.CLICK,clearEvent);
			explorerCanvas.addChild(clearButton);
			
			folderTree = new Tree();//文件夹树
			folderTree.setStyle("borderStyle","none");
			folderTree.y = 90;
			folderTree.percentWidth = 100;
			folderTree.percentHeight = 100;
			folderTree.itemRenderer = new ClassFactory(TreeCheckboxImageItemRender);
			explorerCanvas.addChild(folderTree);
			
		}
		
		
		//创建图表容器
		public function createChartViewCanvas(viewVOid:String):void{
			explorerCanvas.visible = false;
			viewid = viewVOid;
			if(viewChartCanvas==null){
				viewChartCanvas = new Canvas();
				viewChartCanvas.percentWidth = 100;
				viewChartCanvas.percentHeight = 100;
				addChild(viewChartCanvas);
				
				veiwChartApplicationControlBar = new ApplicationControlBar();
				veiwChartApplicationControlBar.percentWidth = 100;
				viewChartCanvas.addChild(veiwChartApplicationControlBar);
				
				searchButton = new LinkButton();
				searchButton.label = "筛选";
				searchButton.toolTip = "筛选";
				searchButton.setStyle("icon",searchimg);
				searchButton.setStyle("textDecoration","underline");
				searchButton.setStyle("fontWeight","normal");
				searchButton.addEventListener(MouseEvent.CLICK,searchEvent);
				veiwChartApplicationControlBar.addChild(searchButton);
				
				//第一种情况前台初始有图表id
				if(Application.application.chartId != null && Application.application.chartId !="" && Application.application.chartId !="null"){
					
				}else{
					backButton = new LinkButton();
					backButton.label = "返回";
					backButton.toolTip = "返回";
					backButton.setStyle("icon",resetimg);
					backButton.setStyle("textDecoration","underline");
					backButton.setStyle("fontWeight","normal");
					backButton.addEventListener(MouseEvent.CLICK,backEvent);
					veiwChartApplicationControlBar.addChild(backButton);
				}
				
				exportButton = new LinkButton();//导出
				exportButton.label = "导出为PDF";
				exportButton.toolTip = "导出为PDF";
				exportButton.setStyle("icon",exportimg);
				exportButton.setStyle("textDecoration","underline");
				exportButton.setStyle("fontWeight","normal");
				exportButton.addEventListener(MouseEvent.CLICK,exportEvent);
				veiwChartApplicationControlBar.addChild(exportButton);
				
				printButton = new LinkButton();//导出
				printButton.label = "打印";
				printButton.toolTip = "打印";
				printButton.setStyle("icon",printimg);
				printButton.setStyle("textDecoration","underline");
				printButton.setStyle("fontWeight","normal");
				printButton.addEventListener(MouseEvent.CLICK,pirntEvent);
				veiwChartApplicationControlBar.addChild(printButton);
				
				vrule = new VRule();
				vrule.width = 1;
				vrule.height = 20;
				veiwChartApplicationControlBar.addChild(vrule);
				
				pieChartButton = new LinkButton();//馅饼图按钮
				pieChartButton.id = "PieChart";
				veiwChartApplicationControlBar.addChild(pieChartButton);
				pieChartButton.addEventListener(MouseEvent.CLICK,changeChartTypeEvent);
				pieChartButton.setStyle("icon",piechartimg);
				
				barChartButton = new LinkButton();//条形图按钮
				barChartButton.id = "BarChart";
				barChartButton.setStyle("icon",barchartimg);
				barChartButton.addEventListener(MouseEvent.CLICK,changeChartTypeEvent);
				veiwChartApplicationControlBar.addChild(barChartButton);
				
				areaChartButton = new LinkButton();//面积图按钮
				areaChartButton.id = "AreaChart";
				areaChartButton.setStyle("icon",areachartimg);
				areaChartButton.addEventListener(MouseEvent.CLICK,changeChartTypeEvent);
				veiwChartApplicationControlBar.addChild(areaChartButton);
				
				columnChartButton = new LinkButton();//柱状图按钮
				columnChartButton.id = "ColumnChart";
				columnChartButton.setStyle("icon",columnchartimg);
				columnChartButton.addEventListener(MouseEvent.CLICK,changeChartTypeEvent);
				veiwChartApplicationControlBar.addChild(columnChartButton);
				
				lineChartButton = new LinkButton();//折线图按钮
				lineChartButton.id = "LineChart";
				lineChartButton.setStyle("icon",linechartimg);
				lineChartButton.addEventListener(MouseEvent.CLICK,changeChartTypeEvent);
				veiwChartApplicationControlBar.addChild(lineChartButton);
				
				plotChartButton = new LinkButton();//散点图按钮
				plotChartButton.id = "PlotChart";
				plotChartButton.setStyle("icon",plotchartimg);
				plotChartButton.addEventListener(MouseEvent.CLICK,changeChartTypeEvent);
				veiwChartApplicationControlBar.addChild(plotChartButton);
				
				iframe	= new IFrame("aUniqueIdForThisIFrameInstance");
				iframe.y = 40;
				iframe.id = "chartIframe";
				iframe.percentHeight=100;
				iframe.percentWidth=100;
				viewChartCanvas.addChild(iframe);
				
			}
			viewChartCanvas.visible = true;
			iframe.visible = true;
			
			oReportRO.addEventListener(ResultEvent.RESULT,getCreateChartJsonResutl);
			oReportRO.getCreateCharJson(viewVOid);
		}
				
	}
}