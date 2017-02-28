package com.filemanager
{
	import com.datagrid.FileNameItemRenderer;
	import com.datagrid.OperateItemRenderer;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.controls.DataGrid;
	import mx.controls.Label;
	import mx.controls.LinkButton;
	import mx.controls.Spacer;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.ClassFactory;

	public class ShowFileCanvas extends Canvas
	{
		public var hbox:HBox;
		public var path:Label;//路径
		public var spacer:Spacer;
		public var addSelectFileButton:LinkButton;//添加选择文件
		public var fileDataGrid:DataGrid;
		
		public function ShowFileCanvas()
		{
			super();
			horizontalScrollPolicy = "off";
			verticalScrollPolicy = "off";
		}
		
		//创建孩子
		override protected function createChildren():void{
			super.createChildren();
			hbox = new HBox();
			hbox.percentHeight = 100;
			hbox.percentWidth = 100;
			addChild(hbox);
			
			path = new Label();
			path.text = "路径：";
			hbox.addChild(path);
			
			spacer = new Spacer();
			spacer.percentWidth = 100;
			hbox.addChild(spacer);
			
			addSelectFileButton = new LinkButton();
			addSelectFileButton.label = "添加选择文件";
			hbox.addChild(addSelectFileButton);
			
			fileDataGrid = new DataGrid();
			fileDataGrid.percentHeight = 100;
			fileDataGrid.percentWidth = 100;
			fileDataGrid.y =25;
			addChild(fileDataGrid);
			
			var fileNameColumn:DataGridColumn = new DataGridColumn();
			 fileNameColumn.headerText = "文件名";
			 fileNameColumn.sortable = false;
			 fileNameColumn.itemRenderer = new ClassFactory(FileNameItemRenderer);
			 fileDataGrid.columns = fileDataGrid.columns.concat(fileNameColumn);
			 
			 var fileSizeColumn:DataGridColumn = new DataGridColumn();
			 fileSizeColumn.headerText = "大小";
			 fileSizeColumn.dataField = "fileSize";
			 fileSizeColumn.width = 160;
			 fileSizeColumn.sortable = false;
			 fileDataGrid.columns = fileDataGrid.columns.concat(fileSizeColumn);
			 
			 var fileTypeColumn:DataGridColumn = new DataGridColumn();
			 fileTypeColumn.headerText = "类型";
			 fileTypeColumn.dataField = "fileType";
			 fileTypeColumn.width = 60;
			 fileTypeColumn.sortable = false;
			 fileDataGrid.columns = fileDataGrid.columns.concat(fileTypeColumn);
			 
			 var operateColumn:DataGridColumn = new DataGridColumn();
			 operateColumn.headerText = "操作";
			 operateColumn.itemRenderer = new ClassFactory(OperateItemRenderer);
			 operateColumn.width = 300;
			 operateColumn.sortable = false;
			 fileDataGrid.columns = fileDataGrid.columns.concat(operateColumn);
			
		}
		
	}
}