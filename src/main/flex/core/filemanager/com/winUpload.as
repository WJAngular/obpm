      import flash.events.*;
      import flash.net.FileReference;
      
      import mx.collections.ArrayCollection;
      import mx.controls.Alert;
      import mx.core.Application;
      import mx.events.CloseEvent;
      import mx.managers.PopUpManager;
      import mx.utils.*;
        
      private var fileList:FileReferenceList = new FileReferenceList();  //多选文件的list 
      //private var urlRequest:URLRequest = new URLRequest("obpm2/UploadServlet");    //处理文件上传的servlet 
      [Bindable] 
      public var selectedFiles: ArrayCollection = new ArrayCollection([]);;    //多选的文件分离成文件数组 
      public var fileListSize:uint=1; 
      public var maximumSize:int;//限制上传文件大小
      public var maximumSizeMessage:String;
      public var contextPath:String;//系统根路径
      public var servletURL:String;//请求路径
      public var realPath:String;//真实路径
      public var envRealPath:String;//获得项目真实路径
      public var size:String;
      [Bindable]
      public var treeNodePath:String;//树显示的路径
      public var kk:int = 0;  
      public var callbackFunction:Function;
      /** 
        * 初始化 
        */ 
      public function init():void{ 
      	if(maximumSize<1024){
        	maximumSizeMessage = maximumSize+" B"
        }else if(maximumSize<(1024*1024)&&maximumSize>=1024){
        	maximumSizeMessage = new int(maximumSize/1024)+" KB"; 
        }else if(maximumSize>=(1024*1024)){
        	maximumSizeMessage = new int(maximumSize/(1024*1024))+" M"; 
        }
        fileList.addEventListener(Event.SELECT,fileSelectHandler); 
        
      } 
      /**
        * 关闭窗口
        */
      private function titleWin_close():void { 
        PopUpManager.removePopUp(this);
        if(realPath!=null&&realPath!=""){ 
        	Application.application.reLoadFolderFiles(realPath);   
        } 
      } 
      
      /** 
        *监听文件选择的处理函数 
        */ 
      private function fileSelectHandler(event: Event): void 
      { 
      		var flag:Boolean = false;
      	  if(fileList.fileList.length>10||selectedFiles.length+1>10){ 
          	Alert.show("一次最多只能上传10个文件","提示："); 
          }else{ 
		          for(var i:int=0;i<fileList.fileList.length;i++){ 
			            var f:FileReference = FileReference(fileList.fileList[i]);
			            //限制上传文件大小
			            if(new int(f.size)<maximumSize){
			            	for each(var k:Object in selectedFiles){
			            		if(k.fileName as String == f.name as String){
			            			flag = true;
			            			Alert.show(f.name+" 已存在列表中，是否继续添加？","提示：",3, this, function(event:CloseEvent):void {
										if (event.detail==Alert.YES){
											var obj:Object= new Object(); 
								            obj.fileName = f.name; 
								            if(f.size<1024){
								            	obj.fileSize = new int(f.size)+" B"
								            }else if(f.size<(1024*1024)&&f.size>=1024){
								            	obj.fileSize = new int(f.size/1024)+" KB"; 
								            }else if(f.size>=(1024*1024)){
								            	obj.fileSize = new int(f.size/(1024*1024))+" M"; 
								            }
								            obj.fileType = f.type; 
								            obj.fileRefrence = f; 
								            fileListSize+=f.size; 
								            selectedFiles.addItem(obj);
								            flag = false;
				      					}else{
				      						flag = true;
				      					}
			            			});
			            			break;
			            		}
			            	}
			            	if(!flag){
					            var obj:Object= new Object(); 
					            obj.fileName = f.name; 
					            if(f.size<1024){
					            	obj.fileSize = new int(f.size)+" B"
					            }else if(f.size<(1024*1024)&&f.size>=1024){
					            	obj.fileSize = new int(f.size/1024)+" KB"; 
					            }else if(f.size>=(1024*1024)){
					            	obj.fileSize = new int(f.size/(1024*1024))+" M"; 
					            }
					            obj.fileType = f.type; 
					            obj.fileRefrence = f; 
					            fileListSize+=f.size; 
					            selectedFiles.addItem(obj);
					            flag = false;
				            }
			            }else{
			            	 if(f.size<1024){
				            	Alert.show(f.name+" "+size+" : "+new int(f.size)+" B , 大于限制大小为 : "+maximumSizeMessage,"提示：");
				            }else if(f.size<(1024*1024)&&f.size>=1024){
				            	Alert.show(f.name+" "+size+" : "+new int(f.size/1024)+" KB , 大于限制大小为 : "+maximumSizeMessage,"提示：");
				            }else if(f.size>=(1024*1024)){
				            	Alert.show(f.name+" "+size+" : "+new int(f.size/(1024*1024))+" M , 大于限制大小为 : "+maximumSizeMessage,"提示：");
				            }
			            } 
		          }
               }
      } 
      /** 
        * 点击"浏览"按钮-->选择文件 
        */ 
      protected function selectFile():void 
      { 
      	realPath=envRealPath+treeNodePath;
      	servletURL=contextPath+"/portal/FileManagerUploadServlet?realPath="+encodeURIComponent(StringUtil.trim(realPath));
        //浏览文件,因为是FileReferenceList所以可以多选. 并用FileFilter过滤文件类型. 
        fileList.browse([new FileFilter("*","*.*")]); 
      } 
      /** 
        * 逐个上传文件 
        */ 
      private function uploadHandler(event:Event):void{
      	if(selectedFiles.length!=0){ 
	      	var urlRequest:URLRequest = new URLRequest(servletURL);
	        var file:FileReference; 
		        if(selectedFiles.length>10){ 
		          	Alert.show("一次最多只能上传10个文件","提示："); 
		        }else{
		        	clearUpload.visible=false;
			        if(kk<selectedFiles.length){ 
				          file = FileReference(selectedFiles[kk].fileRefrence); 
				          file.addEventListener(Event.COMPLETE, doSingleUploadFileComplete); 
				          file.upload(urlRequest); 
			        } 
			        if(kk==selectedFiles.length){
			        	titleWin_close();
			        }
	            }
       }
      } 
      
      //实现文件上传
      private function doSingleUploadFileComplete(event: Event):void{ 
        var file: FileReference = event.target as FileReference; 
        file.removeEventListener(Event.COMPLETE, doSingleUploadFileComplete); 
        kk++;
        uploadHandler(event); 
      } 
