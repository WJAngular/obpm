import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.Application;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

[Bindable]
public var hostAddres:String;//服务器地址
public var contextPath:String;//系统根目录
public var allowedTypes:String;//文件管理打开类型
public var maximumSize:int;//限制上传的大小
public var rolelist:String;//用户角色
public var rootName:String;//根目录
public var realPath:String;//根目录真实路径
[Bindable]
public var showPath:String;//根目录显示路径
public var fileSaveMode:String;//文件保存模式
private var createMenu: ContextMenuItem;//创建菜单    
private var deleteMenu: ContextMenuItem;//删除菜单
private var renameMenu: ContextMenuItem;//重命名菜单
public var MNum:int = 8;
public var ANum:int = 0;

public var selectFileAC:ArrayCollection;//已选择的文件
public var wordPath:String="";//用word打开路径

public function getWordPath():String{
	return wordPath;
}

//初始化
protected function init():void{
	//ExternalInterface.addCallback("getWordPath",getWordPath);
	
	hostAddres = Application.application.parameters.hostAddres;
	contextPath = Application.application.parameters.contextPath;
	allowedTypes = Application.application.parameters.allowedTypes;
	maximumSize = new int(Application.application.parameters.maximumSize);
	rolelist = Application.application.parameters.rolelist;
	rootName = Application.application.parameters.rootName;
	fileSaveMode = Application.application.parameters.fileSaveMode;
	realPath = Application.application.parameters.realPath;
	showPath = Application.application.parameters.showPath;
	
	selectFileAC = new ArrayCollection();
	
	FileManagerRO.addEventListener(ResultEvent.RESULT,loadFolderTreeResult);
	FileManagerRO.loadFolderTree(realPath,rolelist);
}

//获得文件夹树
public var folderTreeXML:XML;
protected function loadFolderTreeResult(event:ResultEvent):void{
	FileManagerRO.removeEventListener(ResultEvent.RESULT,loadFolderTreeResult);
	if(event.result.toString()!=null&&event.result.toString()!=""){
	         folderTreeXML = XML(event.result);
	         var results:XMLList = folderTreeXML.node;
	         folderTree.dataProvider = results;
	         folderTree.selectedIndex = 0;
	       	 folderTree.callLater(expandTree);   //展开树
	       	 //创建树的右键菜单
  		 	 treeMenu();
  		 	 
  	 	 	FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  	 	 	FileManagerRO.getFolderFile(realPath,rolelist);
      }
}

/**
  *展开树文件夹 
  * 
  */                      
 private function expandTree():void{   
 	folderTree.expandChildrenOf(folderTreeXML , true);   
 }
 
public function treeChanged():void {
        showPath=String((folderTree.selectedItem as XML).@path);
        //selectFileAC.removeAll();
        FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  	 	FileManagerRO.getFolderFile(String((folderTree.selectedItem as XML).@realPath),rolelist);
}  

//获得文件列表
public function getFolderFileResult(event:ResultEvent):void{
	FileManagerRO.removeEventListener(ResultEvent.RESULT,getFolderFileResult);
	if(event.result.toString()!=""){
		fileDataGrid.dataProvider = XML(event.result.toString()).file;
	}else{
		fileDataGrid.dataProvider = new ArrayCollection();
	}
	
}

//统一处理错误
protected function faultHandler(event:FaultEvent):void{
	
}



//创建树的右键菜单
private function treeMenu(): void{
    createTreeMenuItem();    
    folderTree.contextMenu = getTreeContxtMenu();
}
    
//创建了具体的三个菜单-新建、删除、重命名
public function createTreeMenuItem(): void{    
    createMenu = new ContextMenuItem("新建文件夹"); 
    deleteMenu = new ContextMenuItem("删除文件夹");  
    renameMenu = new ContextMenuItem("重命名文件夹");   
    
    createMenu.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, createNode);    
    deleteMenu.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, deleteNode);    
    renameMenu.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, renameNode);    
} 
    
//将右键菜单附到树上
private function getTreeContxtMenu(): ContextMenu{    
    var contextMenu:ContextMenu = new ContextMenu();    
    contextMenu.hideBuiltInItems();    
    contextMenu.customItems.push(createMenu);   
    contextMenu.customItems.push(deleteMenu);   
    contextMenu.customItems.push(renameMenu);    
    return contextMenu;    
}

//新建菜单
private function createNode(event:ContextMenuEvent):void{
   	 	var item:Object = onRightClicked(event);
        createNodeCommon(item);
    }
    
//新建文件夹公共部分
protected function createNodeCommon(item:Object):void{
	if (item != null) { 
         	if((item as XML).@Create=="true"){
           //关闭节点，如果不这样做，会有问题
           item = folderTree.selectedItem;
           if (folderTree.dataDescriptor.isBranch(item) && folderTree.isItemOpen(item)) {
             folderTree.expandItem(item, false);
           }
            //添加新节点
            var nodeString:String = "<node label=\""+"新建文件夹"+"\" ></node>";
            var _XML:XML = new XML(nodeString);
            _XML.@label = "新建文件夹("+ANum+")";
            _XML.@realPath = String((item as XML).@realPath)+"\\"+_XML.@label;
            _XML.@path = String((item as XML).@path)+"\\"+_XML.@label;
            _XML.@Create = "true";
            _XML.@Delete = "true";
            _XML.@Rename = "true";
            
            (item as XML).appendChild(_XML);
            //展开该节点
            item = folderTree.selectedItem;
                  if (folderTree.dataDescriptor.isBranch(item) && !folderTree.isItemOpen(item)) {
                     folderTree.expandItem(item, true);
                  }
            //定位到新节点      
                 var children:XMLList = XMLList(item as XML).children();
               for(var i:Number=0; i < children.length(); i++) {                   
                    if( children[i].@label == _XML.@label ) {                     
		                        var newItemRender:IListItemRenderer;
					            var newNodeIndex:int;
					            folderTree.visible = true;
					            folderTree.firstVisibleItem = children[i] as Object;
					            //这句定位要求，是树中可见的节点中定位
					            newItemRender = folderTree.itemToItemRenderer(children[i] as Object);
							            if(newItemRender != null) {               
							              newNodeIndex =  folderTree.itemRendererToIndex(newItemRender);               
									              if(folderTree.selectedIndex != newNodeIndex) {  
									                 folderTree.selectedIndex = newNodeIndex;
									                 //通过remoteObject创建新的文件夹
									                 FileManagerRO.addEventListener(ResultEvent.RESULT,newFolderResult);
									                 FileManagerRO.newFolder(String((item as XML).@realPath)+"\\"+_XML.@label);
									                 //addFolder_FileOperate.newFolder(envRealPath+treeNodePath);
									                 folderTree.editable = true;
									            	 folderTree.editedItemPosition = {rowIndex: folderTree.selectedIndex};   
									          			} 
							               }else{
							                    folderTree.selectedItem = children[i] as Object;
							                    //通过remoteObject创建新的文件夹
							                    FileManagerRO.addEventListener(ResultEvent.RESULT,newFolderResult);
									            FileManagerRO.newFolder(String((item as XML).@realPath)+"\\"+_XML.@label);
							                    folderTree.editable = true;
							            		folderTree.editedItemPosition = {rowIndex: folderTree.selectedIndex};   
							               }
					                
					        //如果不加这几句，有时候就不好使啊！被迫加上了，以后有改进的办法了再说吧                
					       folderTree.firstVisibleItem = children[i] as Object;
					       folderTree.selectedItem = children[i] as Object;
					       //通过remoteObject创建新的文件夹
					       FileManagerRO.addEventListener(ResultEvent.RESULT,newFolderResult);
						   FileManagerRO.newFolder(String((item as XML).@realPath)+"\\"+_XML.@label);
					       
					       folderTree.editable = true;
					       folderTree.editedItemPosition = {rowIndex: folderTree.selectedIndex}; 
					       break; 
                   }
              }                   
           ANum++;
           }else{
            	Alert.show("您没有权限在当前路径下新建文件夹！","警告:");
            } 
       }
}
    
//新建文件夹成功
private function newFolderResult(event:ResultEvent):void{
	FileManagerRO.removeEventListener(ResultEvent.RESULT,newFolderResult);
	FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  	FileManagerRO.getFolderFile(String((folderTree.selectedItem as XML).@realPath),rolelist);
	showPath=String((folderTree.selectedItem as XML).@path);
}
    
//树的右键删除文件夹
private function deleteNode(event:ContextMenuEvent):void{
	var item:Object = onRightClicked(event);
     if (item != null) { 
     	    if(folderTree.selectedIndex>0){
     	    		if((item as XML).@Delete=="true"){
         	    	   //通过remoteObject删除文件
         	    	    FileManagerRO.addEventListener(ResultEvent.RESULT,delFolderResult);
         	    	    FileManagerRO.delFolder(String((item as XML).@realPath));
         	    	    
         	    	    //在树中移除删除的节点
	            		folderTree.dataDescriptor.removeChildAt(folderTree.selectedItem.parent(),folderTree.selectedItem,folderTree.selectedItem.childIndex(),folderTree.dataProvider);
     	    		}else{
     	    			Alert.show("您没有权限删除该文件夹！","警告:");
     	    		}
     	    	}
     }
}

//删除结果
protected function delFolderResult(event:ResultEvent):void{
	FileManagerRO.removeEventListener(ResultEvent.RESULT,delFolderResult);
	folderTree.selectedIndex = 0;
	FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  	FileManagerRO.getFolderFile(String((folderTree.selectedItem as XML).@realPath),rolelist);
  	showPath=String((folderTree.selectedItem as XML).@path);
}

//树的右键重命名文件夹
private function renameNode(event:ContextMenuEvent):void{
	var item:Object = onRightClicked(event);
     if (item != null && folderTree.selectedIndex>0) { 
     	if((item as XML).@Rename=="true"){
            folderTree.editable = true;
     		folderTree.editedItemPosition = {rowIndex: folderTree.selectedIndex};
     	}else{
     		Alert.show("您没有权限重命名该文件夹！","警告:");
     	}
     }
}

 //树的节点编辑完成之后触发事件
   public function itemEditEndHandler(event:Event):void{
   	folderTree.editable = false;
	var newValue:String = event.currentTarget.itemEditorInstance.text;
	if(newValue==""|| newValue==selectPath.text.substr(selectPath.text.lastIndexOf("\\")+1)){
		event.preventDefault(); 
	}else{
		var newNameRealPath:String = String((folderTree.selectedItem as XML).@realPath).substr(0,String((folderTree.selectedItem as XML).@realPath).lastIndexOf("\\")+1)+newValue;//新文件路径
		var newNamePath:String = String((folderTree.selectedItem as XML).@path).substr(0,String((folderTree.selectedItem as XML).@path).lastIndexOf("\\")+1)+newValue;//新文件路径
		(folderTree.selectedItem as XML).@realPath = newNameRealPath;
		(folderTree.selectedItem as XML).@path = newNamePath;
		
		FileManagerRO.addEventListener(ResultEvent.RESULT,reNameFolderOrFileResult);
		FileManagerRO.reNameFolderOrFile(String((folderTree.selectedItem as XML).@realPath),newNameRealPath);
	}
   }
   
   //
   public function reNameFolderOrFileResult(event:ResultEvent):void{
   	 	FileManagerRO.removeEventListener(ResultEvent.RESULT,reNameFolderOrFileResult);
   	 	
   	 	FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  		FileManagerRO.getFolderFile(String((folderTree.selectedItem as XML).@realPath),rolelist);
  		showPath=String((folderTree.selectedItem as XML).@path);
   }
   
   //双击树的节点编辑状态事件
   public function doubleClickHandler(event:MouseEvent):void{
   	var item:Object = folderTree.selectedItem;
   	if(item!=null){
	   	if((item as XML).@Rename=="true"){
		   	if(folderTree.selectedIndex>0){
		   	folderTree.editable = true;
		    folderTree.editedItemPosition = {rowIndex: folderTree.selectedIndex};
		    }
	    }else{
	 		Alert.show("您没有权限重命名该文件夹！","警告:");
	 	}
   	}
   }

//右键定位节点
 private function onRightClicked(e:ContextMenuEvent):Object{
   var rightClickItemRender:IListItemRenderer;  
    var rightClickIndex:int;
	    if(e.mouseTarget is IListItemRenderer) {  
	        rightClickItemRender = IListItemRenderer(e.mouseTarget);  
	    }else if(e.mouseTarget.parent is IListItemRenderer) {  
	        rightClickItemRender = IListItemRenderer(e.mouseTarget.parent);  
	    	}  
	    if(rightClickItemRender != null) {  
	        rightClickIndex =  folderTree.itemRendererToIndex(rightClickItemRender);  
		        if(folderTree.selectedIndex != rightClickIndex) {  
		               folderTree.selectedIndex = rightClickIndex; 
		     				} 
	     	//Alert.show("通过右键单击获得选定的行: " + folderTree.selectedIndex);  
	    	}  
			  //树节点的xml    
			  var globalItem:Object = folderTree.selectedItem; 
			  if(globalItem != null){
				   var node:XML = (globalItem as XML)
					   if (node.@label != undefined)
					    return globalItem; 
					   else{
					    Alert.show("此节点不可操作","警告：");
					    return null;
					   }  
			  }
		 return null;

}   

//根目录
protected function rootEvent():void{
	folderTree.selectedIndex = 0;
	FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  	FileManagerRO.getFolderFile(String((folderTree.selectedItem as XML).@realPath),rolelist);
  	showPath=String((folderTree.selectedItem as XML).@path);
}

//刷新
protected function refreshEvent():void{
	if(folderTree.selectedIndex<0){
		folderTree.selectedIndex = 0;
	}
	FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  	FileManagerRO.getFolderFile(String((folderTree.selectedItem as XML).@realPath),rolelist);
  	showPath=String((folderTree.selectedItem as XML).@path);
}

//新建文件夹
protected function newFolderEvent():void{
	createNodeCommon(folderTree.selectedItem);
}

//删除多文件
protected function deleteEvent():void{
	if(selectFileAC.length==0){
		Alert.show("请选择要删除的文件！","提示");
		return;
	}
	var tempAC:ArrayCollection = new ArrayCollection();
	var flag:Boolean = false; 
	for each(var i:Object in selectFileAC){
		if(i.deletefile=="false" || i.deletefile==false){
			flag = true;
			break;
		}
		tempAC.addItem(String(i.realPath));
	}
	
	if(flag){
		Alert.show("您没有权限删除所选文件！","警告:");
	}else{
		Alert.show("是否删除选择文件？","提示",3, this, function(event:CloseEvent):void {
	        if (event.detail==Alert.YES){
	        	FileManagerRO.addEventListener(ResultEvent.RESULT,deleteFileResult);
				FileManagerRO.delMoreFile(tempAC);
        }});
	}
}

//删除文件结果
protected function deleteFileResult(event:ResultEvent):void{
	FileManagerRO.removeEventListener(ResultEvent.RESULT,deleteFileResult);
	selectFileAC.removeAll();
	FileManagerRO.addEventListener(ResultEvent.RESULT,getFolderFileResult);
  	FileManagerRO.getFolderFile(String((folderTree.selectedItem as XML).@realPath),rolelist);
}

//移动文件
protected function removeEvent():void{
	if(selectFileAC.length==0){
		Alert.show("请选择要移动的文件！","提示");
		return;
	}
	var tempAC:ArrayCollection = new ArrayCollection();
	var flag:Boolean = false; 
	for each(var i:Object in selectFileAC){
		if(i.remove=="false" || i.remove==false){
			flag = true;
			break;
		}
		tempAC.addItem(String(i.realPath));
	}
	
	if(flag){
		Alert.show("您没有权限移动所选文件！","警告:");
	}else{
		var _winRemoveFolderFile:winRemoveFolderFile = PopUpManager.createPopUp(this,winRemoveFolderFile,true) as winRemoveFolderFile;
		_winRemoveFolderFile.oldPath = String((folderTree.selectedItem as XML).@path);
		_winRemoveFolderFile.selectPath = String((folderTree.selectedItem as XML).@realPath);
		_winRemoveFolderFile.tempAC = tempAC;
		PopUpManager.centerPopUp(_winRemoveFolderFile);
	}
}

//复制
protected function copyEvent():void{
	if(selectFileAC.length==0){
		Alert.show("请选择要复制的文件！","提示");
		return;
	}
	var tempAC:ArrayCollection = new ArrayCollection();
	var flag:Boolean = false; 
	for each(var i:Object in selectFileAC){
		if(i.copy=="false" || i.copy==false){
			flag = true;
			break;
		}
		tempAC.addItem(String(i.realPath));
	}
	
	if(flag){
		Alert.show("您没有权限复制所选文件！","警告:");
	}else{
		var _winCopyFolderFile:winCopyFolderFile = PopUpManager.createPopUp(this,winCopyFolderFile,true) as winCopyFolderFile;
		_winCopyFolderFile.oldPath = String((folderTree.selectedItem as XML).@path);
		_winCopyFolderFile.selectPath = String((folderTree.selectedItem as XML).@realPath);
		_winCopyFolderFile.tempAC = tempAC;
		PopUpManager.centerPopUp(_winCopyFolderFile);
	}
}

//上传
protected function uploadEvent():void{
	var _winUpload:winUpload = PopUpManager.createPopUp(this,winUpload,true) as winUpload;
	_winUpload.treeNodePath = String((folderTree.selectedItem as XML).@path);
	_winUpload.realPath = String((folderTree.selectedItem as XML).@realPath);
	PopUpManager.centerPopUp(_winUpload);
}

//添加文件到JSP页面并关闭弹出窗口
  public function addFileAndClose():void{
  	if(selectFileAC.length==0){
		Alert.show("请选择要添加文件！","提示：");
		return;
	}
	var tempAC:String = "";
	var flag:Boolean = false; 
	for each(var ii:Object in selectFileAC){
		if(ii.addSelectFile=="false" || ii.addSelectFile==false){
			flag = true;
			break;
		}
		tempAC+=("/"+(String(ii.path))+";");
	}
	if(tempAC.lastIndexOf(";")!=-1){
		tempAC = tempAC.substring(0,tempAC.lastIndexOf(";"));
	}
	if(flag){
		Alert.show("您没有权限添加所选文件！","警告:");
	}else{
		var imageFileNumber:int = 0;
		if(allowedTypes=="image"){
		    for each(var i:Object in selectFileAC){
		    	if(i.type==".jpg"||i.type==".JPG" ||i.type==".png"||i.type==".PNG"||i.type==".gif"||i.type==".GIF"||i.type==".jpeg"||i.type==".JPEG"||i.type==".bmp"||i.type==".BMP"){
	                imageFileNumber++;					
				}
		    }
				    
		    if(imageFileNumber==selectFileAC.length){
	  				ExternalInterface.call("ev_doClose",tempAC);
  			}else{
  				Alert.show("只支持图片格式,请选择图片格式文件","提示：");
  			}
  		}else{
  			ExternalInterface.call("ev_doClose",tempAC);
  		}
	}
  }
  
//清空
protected function clearEvent():void{
	selectFileAC.removeAll();
	treeChanged();
}

//提示加载
public function loading():void{
	
}