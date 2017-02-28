package com.datagrid
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.MouseEvent;
	import flash.net.FileReference;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.navigateToURL;
	
	import mx.containers.HBox;
	import mx.controls.Alert;
	import mx.controls.LinkButton;
	import mx.core.Application;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;

	public class OperateItemRenderer extends HBox
	{
		[Embed("../../assets/operation.png")]
		private var operationimg:Class;
		public var _data:Object;
		public var reviewFileButton:LinkButton;
		public var editFileButton:LinkButton;
		public var deleteFileButton:LinkButton;
		public var downLoadButton:LinkButton;
		
		public var contextPath:String;
		
		private var fileRef:FileReference = new FileReference();
 		private var urlReq:URLRequest;
		
		
		public function OperateItemRenderer()
		{
			super();
			horizontalScrollPolicy = "off";
			verticalScrollPolicy = "off";
			contextPath = Application.application.contextPath;
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
				reviewFileButton = new LinkButton();
				reviewFileButton.label = "查看";
				reviewFileButton.setStyle("fontWeight","normal");
				reviewFileButton.setStyle("textDecoration","underline");
				reviewFileButton.setStyle("icon",operationimg);
				reviewFileButton.addEventListener(MouseEvent.CLICK,reviewFileEvent);
				addChild(reviewFileButton);
				
				editFileButton = new LinkButton();
				editFileButton.setStyle("fontWeight","normal");
				editFileButton.setStyle("textDecoration","underline");
				editFileButton.setStyle("icon",operationimg);
				editFileButton.addEventListener(MouseEvent.CLICK,editFileEvent);
				editFileButton.label = "编辑";
				addChild(editFileButton);
				
				deleteFileButton = new LinkButton();
				deleteFileButton.label = "删除";
				deleteFileButton.setStyle("fontWeight","normal");
				deleteFileButton.setStyle("textDecoration","underline");
				deleteFileButton.setStyle("icon",operationimg);
				deleteFileButton.addEventListener(MouseEvent.CLICK,deleteFileEvent);
				addChild(deleteFileButton);
				
				downLoadButton = new LinkButton();
				downLoadButton.setStyle("fontWeight","normal");
				downLoadButton.setStyle("textDecoration","underline");
				downLoadButton.setStyle("icon",operationimg);
				downLoadButton.addEventListener(MouseEvent.CLICK,downLoadEvent);
				downLoadButton.label = "下载";
				addChild(downLoadButton);
			}
		}
		
		
//查看
protected function reviewFileEvent(event:MouseEvent):void{
	if(_data.review=="true"){
   		if(_data.type==".doc" ||_data.type==".docx" ||_data.type==".xlsx" ||_data.type==".xls"){
   			/*
   				 Application.application.wordPath = contextPath+"/"+_data.path;
   			    var _word:word = PopUpManager.createPopUp(Application.application as DisplayObject,word,true) as word;
   			    _word.name1 = _data.name;
   			    _word.type = _data.type;
   			    _word.isPopUp = false;
   			    PopUpManager.centerPopUp(_word);*/
				urlReq = new URLRequest(encodeURI(contextPath+"/portal/share/component/filemanager/ntkoofficecontrol.jsp?path=/"+_data.path+"&type="+_data.type));
			}else{
				urlReq = new URLRequest(encodeURI(contextPath+"/"+_data.path)); 
			} 
		urlReq.method=URLRequestMethod.POST;
    	navigateToURL(urlReq); 
	 }else{
	 	Alert.show("您没有权限查看该文件!","警告:");
	 }
}

//编辑
protected function editFileEvent(evet:MouseEvent):void{
	if(_data.edit=="true"){
		  if(_data.type==".jpg"||_data.type==".png"||_data.type==".gif"||_data.type==".jpeg"||_data.type==".bmp"){
	   		var win_EditPictures:winEditPictures = PopUpManager.createPopUp(Application.application as DisplayObject, winEditPictures, true) as winEditPictures;
		    win_EditPictures.IMAGE_URL=contextPath+"/"+_data.path+"?time="+(new Date()).toString(); 
		    win_EditPictures.realPath=_data.realPath;
		    PopUpManager.centerPopUp(win_EditPictures);
		  }else{
		  	Alert.show("只能支持编辑图片格式！","提示：");	
		  }
	 }else{
	 	Alert.show("您没有权限编辑该文件!","警告:");
	 }
}

//删除
protected function deleteFileEvent(event:MouseEvent):void{
	if(_data.deletefile=="true"){
		Alert.show("是否删除该文件？","提示",3, this, function(event:CloseEvent):void {
	    if (event.detail==Alert.YES){
	    	//通过调用remoteObject来参数为文件的真实路径，删除一个文件
	    	for each(var i:Object in Application.application.selectFileAC){
				if(String(i.path) == String(_data.path)){
					Application.application.selectFileAC.removeItemAt(Application.application.selectFileAC.getItemIndex(i));
				}
			}
	    	Application.application.FileManagerRO.addEventListener(ResultEvent.RESULT,delFileResult);
	    	Application.application.FileManagerRO.delFile(String(data.realPath));
	    }});
	 }else{
	 	Alert.show("您没有权限删除该文件!","警告:");
	 }
}

//删除的结果
protected function delFileResult(event:ResultEvent):void{
	Application.application.FileManagerRO.removeEventListener(ResultEvent.RESULT,delFileResult);
	Application.application.FileManagerRO.addEventListener(ResultEvent.RESULT,Application.application.getFolderFileResult);
  	Application.application.FileManagerRO.getFolderFile(String((Application.application.folderTree.selectedItem as XML).@realPath),Application.application.rolelist);
	
}
		
//下载
protected function downLoadEvent(event:MouseEvent):void{
	if(_data.down=="true"){
		  	urlReq= new URLRequest(encodeURI(contextPath+"/"+_data.path)); 
		  	configureListeners(fileRef);
			fileRef.download(urlReq,data.name);
	 	}else{
	 		Alert.show("您没有权限下载该文件!","警告:");
	 	}
}
   
 private function configureListeners(dispatcher:IEventDispatcher):void {
    dispatcher.addEventListener(Event.COMPLETE, completeHandler);
 }
 //监听下载完成后事件
 private function completeHandler(event:Event):void {
	Alert.show("文件下载成功","提示：");
 }

	}
}