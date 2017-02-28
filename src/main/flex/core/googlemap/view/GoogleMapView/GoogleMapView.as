	 import com.adobe.serialization.json.JSON;
	 import com.google.maps.*;
	 import com.google.maps.controls.*;
	 import com.google.maps.geom.*;
	 import com.google.maps.interfaces.*;
	 import com.google.maps.overlays.*;
	 import com.google.maps.services.*;
	 import com.google.maps.styles.*;
	 
	 import flash.events.Event;
	 
	 import mx.controls.Alert;
	 
	private var map:Map;
    private var theMarkers:Array=[];
    private var level:int=2;//缩放等级
    public var formid:String = "";//表单编号
    public var documentid:String="";//文档编号
    private var key:String = "";
    
    public function onHolderCreated(event:Event):void
    {
    	loadinghbox.visible = true;
    	formid = Application.application.parameters.formid;
    	key = Application.application.parameters.key;
    	if(key!=""){
	        map=new Map();
	        map.sensor="true";
	        //map.key="ABQIAAAAnqA-6Fc1LCSEFTRYWMrd7xRi_j0U6kJrkFvY4-OX2XYmEAa76BRM9Fv5bM-yN1bKEDrsEVq2N22XTg";
	        map.key=key;
	        map.addEventListener(MapEvent.MAP_READY, onMapReady);
	        mapHolder.addChild(map);
     	}else{
     		Alert.show("google map API密钥为空,请检查配置是否正确?","警告:");
     	}
    }

    public function onHolderResized(event:Event):void
    {
    	if(map!=null){
        	map.setSize(new Point(mapHolder.width, mapHolder.height));
     	}
    }

    private function onMapReady(event:Event):void
    {
        
		if(map!=null){
			//鼠标滚轮实现地图缩放
			//map.enableScrollWheelZoom(); 
			//map.enableContinuousZoom();
	        map.enableScrollWheelZoom();
	        map.enableContinuousZoom();
	        map.setCenter(new LatLng(49.26780, 15.46875),2,MapType.NORMAL_MAP_TYPE);
	        //显示一个方向的按钮,用来移动地图 
			map.addControl( new PositionControl( new PositionControlOptions()));   
			//显示一个地图的缩略图 
			map.addControl( new OverviewMapControl( new OverviewMapControlOptions() ) );   
			//显示一个刻度条用来放大和缩小地图 
			map.addControl( new ZoomControl( new  ZoomControlOptions() ) );   
			//在地图上显示地图的4种类型，可以随意切换 
			map.addControl( new MapTypeControl( new MapTypeControlOptions() ) );
			
			map.addEventListener(TextEvent.LINK, viewDoc, false, 0, true);
			
	  		loadinghbox.visible = false;
	  		mapHolder.y = 0;
	  		
	  		ExternalInterface.addCallback("getMapDataResult",getMapDataResult);
	  		ExternalInterface.call("getMapData",getMapDataResult); 
    }
    
    
    }
    
    public function getMapDataResult(mapData:String):void{
    	if(mapData!=null&&mapData!=""&&mapData!="null"){//加载点到地图上
    			var objdata:Object=JSON.decode(mapData);
    			for each(var i:Object in objdata.mapInfo){
    				var titleStr:String = "<b>"+i.title+"</b>";
		            var detailStr:String = "地址:"+i.address+"<br/><br/>内容:"+i.detail;
		            detailStr+="<br/><br/>";
		            //判断是否只读
		            if(!objdata.editReadOnly){
		            	detailStr+="&nbsp;&nbsp;<a href='event:edit'>编辑</a>";
		            }
		            if(!objdata.deleteReadOnly){
		            	detailStr+="&nbsp;&nbsp;&nbsp;&nbsp;<a href='event:delete'>删除</a>";
		            }
		            var latlng:LatLng = new LatLng(new Number(i.lat),new Number(i.lng));
		            level = new Number(i.level);
		        	map.addOverlay(createMarker(latlng,titleStr,detailStr,i.fieldid));
    			}
	        }
	       var latlngMap:LatLng = new LatLng(35.86166,104.195397);
	       map.setCenter(latlngMap,4);
  		}


   //创建地图上点
    public function createMarker(latlng:LatLng,titleStr:String,detailStr:String,docid:String):Marker
    {
        var marker:Marker=new Marker(latlng);
        marker.addEventListener(MapMouseEvent.CLICK, function(e:MapMouseEvent):void
            {
            	documentid = "";
            	documentid = docid.split("_")[0];
                marker.openInfoWindow(new InfoWindowOptions({titleHTML:titleStr,contentHTML:detailStr,hasShadow: true}));
            });
        marker.addEventListener(MapMouseEvent.DOUBLE_CLICK,function(e:MapMouseEvent):void{
        		ExternalInterface.call("viewDoc",docid);
        	});
	   return marker;
	    
    }
    
    //点击编辑触发事件
    private function viewDoc(event:TextEvent):void
	{
		if(event.text=="edit"){
	 		ExternalInterface.call("viewDoc",documentid);
	 	}else if(event.text=="delete"){
	 		ExternalInterface.call("deleteDoc",documentid);
	 	}
	}
	