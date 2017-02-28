	import flash.net.FileReference;
	import flash.net.URLRequest;
	
	import mx.containers.TitleWindow;
	import mx.controls.Alert;
	import mx.controls.Image;
	import mx.core.Application;
	import mx.core.IFlexDisplayObject;
	import mx.events.CloseEvent;
	import mx.graphics.ImageSnapshot;
	import mx.graphics.codec.JPEGEncoder;
	import mx.managers.PopUpManager;
	import mx.utils.*;
	
	public static const LINE_WIDTH:Number = 1;//缩放边框宽度
	private var file:FileReference;
	public var IMAGE_URL:String;
	private var loader:Loader;
	private var bmp:Bitmap;
    private var stream:URLStream;
    public var contextPath:String;
    public var realPath:String;
	
	/**
      * 关闭窗口
      */
    private function titleWin_close(evt:CloseEvent):void { 
        PopUpManager.removePopUp(evt.target as IFlexDisplayObject); 
        //Application.application.callBackAddFolder("uploads");
        
      } 
	//初始化数据
	private function init():void{
		this.loader = new Loader();
        this.stream = new URLStream();
        this.loader.contentLoaderInfo.addEventListener(Event.COMPLETE,this.onComplete);
        var urlReq:URLRequest = new URLRequest(encodeURI(this.IMAGE_URL));
        urlReq.method=URLRequestMethod.POST;
        this.loader.load(urlReq);
        this.stream.load(urlReq);
        this.stream.addEventListener(Event.COMPLETE,this.onLoaded);
	}
	private function onLoaded(e:Event):void
    {                                
        var bytearray:ByteArray = new ByteArray();    
        this.stream.readBytes(bytearray);
        
        if(this.stream.connected)
            this.stream.close();
            
        this.loader.loadBytes(bytearray);
    }
    private function onComplete(e:Event):void
    {
        try
        {
            this.bmp = this.loader.content as Bitmap;
            var showImage:Image= new Image();
            showImage.source=this.loader.content;
            canvas.addChild(showImage);
            canvas.setChildIndex(box,1);
            canvas.setChildIndex(showImage,0);
        }
        catch(e:Error)
        {
            
        }
    }
	
	//截图，显示缩放选择框
	private function doCapture():void{
		box.x = 100;
		box.y = 100;
		box.visible = true;
	}
	
	//获取缩放选择框内的图像
	private function getImg():BitmapData{
		//截取整个区域
		box.scaleEnable = false;
		var bmp:BitmapData = ImageSnapshot.captureBitmapData(canvas);
		box.scaleEnable = true;
		
		//矩形为要截取区域                
        var re:Rectangle = new Rectangle(box.x+LINE_WIDTH,box.y+LINE_WIDTH,box.boxWidth-LINE_WIDTH,box.boxHeight-LINE_WIDTH); 
        var bytearray:ByteArray = new ByteArray();   
        //截取出所选区域的像素集合                        
        bytearray = bmp.getPixels(re); 
        
        
        var imgBD:BitmapData = new BitmapData(box.boxWidth-LINE_WIDTH,box.boxHeight-LINE_WIDTH);       
        //当前的bytearray.position为最大长度，要设为从0开始读取       
        bytearray.position=0;            
        var fillre:Rectangle = new Rectangle(0,0,box.boxWidth-LINE_WIDTH,box.boxHeight-LINE_WIDTH);
        //将截取出的像素集合存在新的bitmapdata里，大小和截取区域一样
        imgBD.setPixels(fillre,bytearray);
        
        return imgBD;
	}
	
	//预览图片
	private function doScan():void{
		var t:TitleWindow = new TitleWindow();
		t.showCloseButton=true;
		t.addEventListener(CloseEvent.CLOSE,closeWindow);
		t.width = box.boxWidth+t.getStyle("borderThickness");
		t.height =box.boxHeight+t.getStyle("borderThickness")+t.getStyle("headerHeight");
		var img:Image = new Image();
		img.width = box.boxWidth;
		img.height = box.boxHeight; 
		img.source = new Bitmap(getImg());
		t.addChild(img);
		PopUpManager.addPopUp(t,this,true);
		PopUpManager.centerPopUp(t);
	}
	
	private function closeWindow(e:CloseEvent):void{            
        var t:TitleWindow = e.currentTarget as TitleWindow;                    
        PopUpManager.removePopUp(t);                
    }
    
    //保存图片到本地
	private function downloadPicture():void{
		file=new FileReference();
		file.addEventListener(Event.COMPLETE,downloadComplete);
		file.save(new JPEGEncoder(80).encode(getImg()),"default.jpg");
	}
	
	private function downloadComplete(event:Event):void{
		Alert.show("成功保存图片到本地,是否关闭窗口？","提示",3, this, function(event:CloseEvent):void {
    	if (event.detail==Alert.YES){
    		PopUpManager.removePopUp(this as IFlexDisplayObject); 
    	}
  		});
	}
	
	//保存图片到服务器即覆盖原来的图片
	private function save():void{
		Alert.show("是否保存剪切图片？","提示",3, this, function(event:CloseEvent):void {
        if (event.detail==Alert.YES){
        	var request:URLRequest = new URLRequest(contextPath+"/portal/FileManagerSaveFileServlet?realPath="+encodeURIComponent(StringUtil.trim(realPath)));
			request.method=URLRequestMethod.POST;
			request.contentType = "application/octet-stream";
			request.data = new JPEGEncoder(80).encode(getImg());
			var loader:URLLoader = new URLLoader();
			loader.load(request);
			loader.addEventListener(Event.COMPLETE,saveResult);

        }});
	}
	
	private function saveResult(event:Event):void{
		Application.application.reLoadFolderFiles(realPath.substr(0,realPath.lastIndexOf("\\")));
		Alert.show("成功保存剪切图片到服务器,是否关闭窗口？","提示",3, this, function(event:CloseEvent):void {
			
    	if (event.detail==Alert.YES){
    		//Alert.show(event.detail.toString());
    		PopUpManager.removePopUp(this as IFlexDisplayObject); 
    	}
  		});
	}