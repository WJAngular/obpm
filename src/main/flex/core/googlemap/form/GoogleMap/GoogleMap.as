	 import com.adobe.serialization.json.JSON;
	 import com.google.maps.*;
	 import com.google.maps.controls.*;
	 import com.google.maps.geom.*;
	 import com.google.maps.interfaces.*;
	 import com.google.maps.overlays.*;
	 import com.google.maps.services.*;
	 import com.google.maps.styles.*;
	 
	 import flash.events.Event;
	 
	 import mx.collections.ArrayCollection;
	 import mx.controls.Alert;
	 import mx.rpc.events.FaultEvent;
	 import mx.rpc.events.ResultEvent;
	 

    private var map:Map=null;
    private var theMarkers:Array=[];
    [Bindable]
    private var countryStr:String="";//国家
    [Bindable]
    private var provinceStr:String="";//省份
    [Bindable]
    private var cityStr:String="";//城市
    [Bindable]
    private var townStr:String="";//镇
    [Bindable]
    private var streetStr:String="";//街道
    [Bindable]
    private var hostAddres:String;//请求服务器地址
    private var level:int=2;//缩放等级
    private var mapData:String;//保存数据
    private var fieldID:String;
    private var applicationid:String;
    private var type:String;//表单打开类型
    private var marker:Marker=null;//地图上一个点
    private var placemarks:Array=null;//返回地址数据
    private var key:String="";//API钥匙
    
    
    public function onHolderCreated(event:Event):void
    {
    	loadinghbox.visible=true;
    	key = Application.application.parameters.key;
    	fieldID = Application.application.parameters.fieldID;
    	type = Application.application.parameters.type;
    	hostAddres = Application.application.parameters.hostAddres;
    	applicationid = Application.application.parameters.applicationid;
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
			//map.addControl( new OverviewMapControl( new OverviewMapControlOptions() ) );   
			//显示一个刻度条用来放大和缩小地图 
			//map.addControl( new ZoomControl( new  ZoomControlOptions() ) );   
			//在地图上显示地图的4种类型，可以随意切换 
			//map.addControl( new MapTypeControl( new MapTypeControlOptions() ) );
	        getCountryXML();
	        
	       GoogleMapRO.addEventListener(ResultEvent.RESULT,getMapDataResult);
	       GoogleMapRO.getMapData(fieldID,applicationid);
  		}
  		loadinghbox.visible=false;
    }
    
    public function getMapDataResult(event:ResultEvent):void{
    	GoogleMapRO.removeEventListener(ResultEvent.RESULT,getMapDataResult);
    	mapData = "";
    	mapData = event.result.toString();
    	if(mapData!=null&&mapData!=""&&mapData!="null"){//加载点到地图上
	        	var objdata:Object=JSON.decode(mapData);
	            address.text=objdata.address;
	            
	            titletextinput.text=objdata.title;
	            detailtextarea.text=objdata.detail;
	            var titleStr:String = "<b>"+objdata.title+"</b>";
	            var detailStr:String = "地址:"+objdata.address+"<br/><br/>内容:"+objdata.detail;
	            if(placemarks==null){
	            	placemarks = new Array();
	            	var obj:Object = new Object();
	            	obj.address = address.text;
	            	placemarks.push(obj);
	            }
	            level = new Number(objdata.level);
	            var latlng:LatLng = new LatLng(new Number(objdata.lat),new Number(objdata.lng));
	        	createMarker(latlng,titleStr,detailStr);
	        }
    }


   //创建地图上点
    public function createMarker(latlng:LatLng,titleStr:String,detailStr:String):void
    {
    	map.setCenter(latlng,level);
        marker=new Marker(latlng,new MarkerOptions({draggable: true}));
        marker.addEventListener(MapMouseEvent.CLICK, function(e:MapMouseEvent):void
            {
                marker.openInfoWindow(new InfoWindowOptions({titleHTML:titleStr,contentHTML:detailStr}));
            });
        map.clearOverlays();
	    map.addOverlay(marker);
    }


 	//查找并定位地址
    private function doGeocode(event:Event):void
    {
    	if(address.text!=""){
	    	loadinghbox.visible=true;
	        // Geocoding example
	        var geocoder:ClientGeocoder=new ClientGeocoder();
			    //定位地址
	        geocoder.geocode(address.text);
	        
	        //成功处理
	        geocoder.addEventListener(GeocodingEvent.GEOCODING_SUCCESS, function(event:GeocodingEvent):void
	            {
	                placemarks=event.response.placemarks;
	                if (placemarks.length > 0)
	                {
	                    map.setCenter(placemarks[0].point,level);
	                    marker = new Marker(placemarks[0].point,new MarkerOptions({draggable: true}));
	                    marker.addEventListener(MapMouseEvent.CLICK, function(event:MapMouseEvent):void
	                        {
	                   marker.openInfoWindow(new InfoWindowOptions({titleHTML:"<b>"+titletextinput.text+"</b>",contentHTML:"地址:"+placemarks[0].address+"<br/><br/>内容:"+detailtextarea.text}));
	                        });
	                    map.clearOverlays();
	                    map.addOverlay(marker);
	                    addresshbox.percentHeight=100;
	                    loadinghbox.visible = false;
	                }
	            });
	       //监听失败     
	        geocoder.addEventListener(GeocodingEvent.GEOCODING_FAILURE, function(event:GeocodingEvent):void
	            {
	            	addresshbox.percentHeight=100;
	                loadinghbox.visible = false;
	                Alert.show("地址："+address.text+" 在 google map 中定位坐标失败！");
	            });
     }else{
     	 map.clearOverlays();
     }
    }
    
    	
    //获得国家信息xml
    public function getCountryXML():void{
    	var xmlString:URLRequest=new URLRequest("country.xml");
        var xmlLoader:URLLoader=new URLLoader(xmlString);
        xmlLoader.addEventListener("complete", readCountryXml);
    }
    
    //将转化为xml加到国家combox中
    public function readCountryXml(event:Event):void{
    	//Alert.show(event.target.data);
    	var countrysXML:XML=new XML(event.target.data);
        var countrys:XMLList=countrysXML..country;
        countrycombox.dataProvider=countrys;
    }
    
    //国家改变事件
    public function countryChange():void{
    	var countrySelect:Object = countrycombox.selectedItem;
    	var countrysXML:XML=new XML(countrySelect);
        var provinces:XMLList=countrysXML.province;
    	provincecombox.dataProvider=provinces;
    	citycombox.dataProvider = new XMLList();
    	towncombox.dataProvider= new XMLList();
    	if(countrySelect.@label!="请选择"){
    		countryStr=countrySelect.@label;
    	}else{
    		address.text="";
    	}
    	provinceStr="";
    	cityStr="";
    	townStr="";
    	streetStr="";
    	level=4;
    }
    
    //省份改变事件
    public function provinceChange():void{
    	var provinceSelect:Object = provincecombox.selectedItem;
    	var provincesXML:XML=new XML(provinceSelect);
        var citys:XMLList=provincesXML.city;
    	citycombox.dataProvider=citys;
    	towncombox.dataProvider = new XMLList();
    	if(provinceSelect.@label!="请选择"){
    		provinceStr=provinceSelect.@label;
    	}else{
    		provinceStr="";
    	}
    	cityStr="";
    	townStr="";
    	streetStr="";
    	level=8;
    }
    
    //城市改变事件
    public function cityChange():void{
    	var citySelect:Object = citycombox.selectedItem;
    	var citysXML:XML=new XML(citySelect);
        var towns:XMLList=citysXML.town;
    	towncombox.dataProvider=towns;
    	if(citySelect.@label!="请选择"){
    		cityStr=citySelect.@label;
    	}else{
    		cityStr="";
    	}
    	townStr="";
    	streetStr="";
    	level=12;
    }
    
    //镇改变事件
    public function townChange():void{
    	if(towncombox.selectedItem.@label!="请选择"){
    		townStr=towncombox.selectedItem.@label;
    	}else{
    		townStr="";
    	}
    	streetStr="";
    	level=14;
    }
    
    
    //保存数据
    public function saveInformation():void{
    	if(marker!=null){
    		if(titletextinput.text!=""){
    			if(detailtextarea.text!=""){
    				mapData="";
    				mapData+='{';
    				mapData+='"fieldid":"'+fieldID+'",';
		    		mapData+='"lat":"'+marker.getLatLng().lat()+'",';
		    		mapData+='"lng":"'+marker.getLatLng().lng()+'",';
		    		mapData+='"level":"'+level+'",';
		    	    mapData+='"title":"'+filter(titletextinput.text)+'",';
		    	    mapData+='"address":"'+placemarks[0].address+'",';
		    	    
		    	    if(countryStr!=""){
		    	    	mapData+='"addressInfo":{"country":"'+countryStr+'"';
		    	    	if(provinceStr!=""){
		    	    		mapData+=',"province":"'+provinceStr+'"';
		    	    		if(cityStr!=""){
				    	    	mapData+=',"city":"'+cityStr+'"';
				    	    	if(townStr!=""){
					    	    	mapData+=',"town":"'+townStr+'"';
					    	    	if(streetStr!=""){
						    	    	mapData+=',"street":"'+streetStr+'"';
						    	    }else{
					    	    		mapData+='},';
					    	    	}
					    	    }else{
				    	    		mapData+='},';
				    	    	}
				    	    }else{
			    	    		mapData+='},';
			    	    	}
		    	    	}else{
		    	    		mapData+='},';
		    	    	}
		    	    }
		    	    
		    	    mapData+='"detail":"'+filter(detailtextarea.text)+'"}';
		    	    if(type=="dialog"){
		    	    	ExternalInterface.call("ev_doClose",mapData);
		    	    }else{
		    	    	ExternalInterface.call("doClose",mapData);
		    	    }
	    	 	}else{
	    	 		Alert.show("请输入内容！","提示:");
	    	 	}
    	 	}else{
    	 		Alert.show("请输入标题！","提示:");
    	 	}
    	}else{
    		Alert.show("地图上没有坐标信息可以保存！","提示:");
    	}
    }
    //清空该节点信息
    public function clearInformation(event:Event):void{
    	countrycombox.selectedIndex = 0;
    	countryChange();
    	countryStr = "";
    	address.text="";
    	titletextinput.text="";
    	detailtextarea.text="";
    	marker = null;
    	map.clearOverlays();
    	if(type=="dialog"){
    		ExternalInterface.call("ev_doEmpty");
    	}else{
    		ExternalInterface.call("doEmpty");
    	}
    }
    
    //过滤特殊字符
    public function filter(str:String):String{
    	while(str.indexOf(" ")!=-1||str.indexOf("+")!=-1||str.indexOf("/")!=-1||str.indexOf("?")!=-1||str.indexOf("%")!=-1||str.indexOf("#")!=-1||str.indexOf("=")!=-1){
    	str = str.replace(" ","");
    	str = str.replace("+","");
    	str = str.replace("/","");
    	str = str.replace("?","");
    	str = str.replace("%","");
    	str = str.replace("#","");
    	str = str.replace("&","");
    	str = str.replace("=","");
    	}
    	return str;
    }
    
    public function fault(event:FaultEvent):void{
    	
    }
    