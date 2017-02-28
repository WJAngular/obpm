/**
 * FusionCharts: Flash Player detection and Chart embed 
 * 
 * Morphed from SWFObject (http://blog.deconcept.com/swfobject/) under MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 */
if(typeof infosoftglobal == "undefined") var infosoftglobal = new Object();
if(typeof infosoftglobal.FusionChartsUtil == "undefined") infosoftglobal.FusionChartsUtil = new Object();
infosoftglobal.FusionCharts = function(swf, id, w, h, debugMode, registerWithJS, c, scaleMode, lang){
	if (!document.getElementById) { return; }
	
	//Flag to see whether data has been set initially
	this.initialDataSet = false;
	
	//Create container objects
	this.params = new Object();
	this.variables = new Object();
	this.attributes = new Array();
	
	//Set attributes for the SWF
	if(swf) { this.setAttribute('swf', swf); }
	if(id) { this.setAttribute('id', id); }
	if(w) { this.setAttribute('width', w); }
	if(h) { this.setAttribute('height', h); }
	
	//Set background color
	if(c) { this.addParam('bgcolor', c); }
	
	//Set Quality	
	this.addParam('quality', 'high');
	
	//Add scripting access parameter
	this.addParam('allowScriptAccess', 'always');
    this.addParam('wmode', 'transparent');
	
	//Pass width and height to be appended as chartWidth and chartHeight
	this.addVariable('chartWidth', w);
	this.addVariable('chartHeight', h);

	//Whether in debug mode
	debugMode = debugMode ? debugMode : 0;
	this.addVariable('debugMode', debugMode);
	//Pass DOM ID to Chart
	this.addVariable('DOMId', id);
	//Whether to registed with JavaScript
	registerWithJS = registerWithJS ? registerWithJS : 0;
	this.addVariable('registerWithJS', registerWithJS);
	
	//Scale Mode of chart
	scaleMode = scaleMode ? scaleMode : 'noScale';
	this.addVariable('scaleMode', scaleMode);
	//Application Message Language
	lang = lang ? lang : 'EN';
	this.addVariable('lang', lang);
}

infosoftglobal.FusionCharts.prototype = {
	setAttribute: function(name, value){
		this.attributes[name] = value;
	},
	getAttribute: function(name){
		return this.attributes[name];
	},
	addParam: function(name, value){
		this.params[name] = value;
	},
	getParams: function(){
		return this.params;
	},
	addVariable: function(name, value){
		this.variables[name] = value;
	},
	getVariable: function(name){
		return this.variables[name];
	},
	getVariables: function(){
		return this.variables;
	},
	getVariablePairs: function(){
		var variablePairs = new Array();
		var key;
		var variables = this.getVariables();
		for(key in variables){
			variablePairs.push(key +"="+ variables[key]);
		}
		return variablePairs;
	},
	getSWFHTML: function() {
		var swfNode = "";
		if (navigator.plugins && navigator.mimeTypes && navigator.mimeTypes.length) { 
			// netscape plugin architecture			
			swfNode = '<embed type="application/x-shockwave-flash" src="'+ this.getAttribute('swf') +'" width="'+ this.getAttribute('width') +'" height="'+ this.getAttribute('height') +'"  ';
			swfNode += ' id="'+ this.getAttribute('id') +'" name="'+ this.getAttribute('id') +'" ';
			var params = this.getParams();
			 for(var key in params){ swfNode += [key] +'="'+ params[key] +'" '; }
			var pairs = this.getVariablePairs().join("&");
			 if (pairs.length > 0){ swfNode += 'flashvars="'+ pairs +'"'; }
			swfNode += '/>';
		} else { // PC IE			
			swfNode = '<object id="'+ this.getAttribute('id') +'" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://eip.gpgc.sz.local/szeip/download/swflash.cab#version=6,0,0,0" width="'+ this.getAttribute('width') +'" height="'+ this.getAttribute('height') +'">';
			swfNode += '<param name="movie" value="'+ this.getAttribute('swf') +'" />';
			var params = this.getParams();
			for(var key in params) {
			 swfNode += '<param name="'+ key +'" value="'+ params[key] +'" />';
			}
			var pairs = this.getVariablePairs().join("&");			
			if(pairs.length > 0) {swfNode += '<param name="flashvars" value="'+ pairs +'" />';}
			swfNode += "</object>";
		}
		return swfNode;
	},
	setDataURL: function(strDataURL){
		//This method sets the data URL for the chart.
		//If being set initially
		if (this.initialDataSet==false){
			this.addVariable('dataURL',strDataURL);
			//Update flag
			this.initialDataSet = true;
		}else{
			//Else, we update the chart data using External Interface
			//Get reference to chart object
			var chartObj = infosoftglobal.FusionChartsUtil.getChartObject(this.getAttribute('id'));
			chartObj.setDataURL(strDataURL);
		}
	},
	setDataXML: function(strDataXML){
		//If being set initially
		if (this.initialDataSet==false){
			//This method sets the data XML for the chart INITIALLY.
			this.addVariable('dataXML',strDataXML);
			//Update flag
			this.initialDataSet = true;
		}else{
			//Else, we update the chart data using External Interface
			//Get reference to chart object
			var chartObj = infosoftglobal.FusionChartsUtil.getChartObject(this.getAttribute('id'));
			chartObj.setDataXML(strDataXML);
		}
	},
	render: function(elementId){
		var n = (typeof elementId == 'string') ? document.getElementById(elementId) : elementId;
		n.innerHTML = this.getSWFHTML();
		return true;		
	}
}

// ------------ Fix for Out of Memory Bug in IE in FP9 ---------------//
/* Fix for video streaming bug */
infosoftglobal.FusionChartsUtil.cleanupSWFs = function() {
	if (window.opera || !document.all) return;
	var objects = document.getElementsByTagName("OBJECT");
	for (var i=0; i < objects.length; i++) {
		objects[i].style.display = 'none';
		for (var x in objects[i]) {
			if (typeof objects[i][x] == 'function') {
				objects[i][x] = function(){};
			}
		}
	}
}
// Fixes bug in fp9
infosoftglobal.FusionChartsUtil.prepUnload = function() {
	__flash_unloadHandler = function(){};
	__flash_savedUnloadHandler = function(){};
	if (typeof window.onunload == 'function') {
		var oldUnload = window.onunload;
		window.onunload = function() {
			infosoftglobal.FusionChartsUtil.cleanupSWFs();
			oldUnload();
		}
	} else {
		window.onunload = infosoftglobal.FusionChartsUtil.cleanupSWFs;
	}
}
if (typeof window.onbeforeunload == 'function') {
	var oldBeforeUnload = window.onbeforeunload;
	window.onbeforeunload = function() {
		infosoftglobal.FusionChartsUtil.prepUnload();
		oldBeforeUnload();
	}
} else {
	window.onbeforeunload = infosoftglobal.FusionChartsUtil.prepUnload;
}

/* Add Array.push if needed (ie5) */
if (Array.prototype.push == null) { Array.prototype.push = function(item) { this[this.length] = item; return this.length; }}

/* Function to return Flash Object from ID */
infosoftglobal.FusionChartsUtil.getChartObject = function(id)
{
  if (window.document[id]) {
      return window.document[id];
  }
  if (navigator.appName.indexOf("Microsoft Internet")==-1) {
    if (document.embeds && document.embeds[id])
      return document.embeds[id]; 
  } else {
    return document.getElementById(id);
  }
}
/* Aliases for easy usage */
var getChartFromId = infosoftglobal.FusionChartsUtil.getChartObject;
var FusionCharts = infosoftglobal.FusionCharts;



/*****************************************************************************************/
function FlashChart(){
     this.Title    = "";
     this.Width    = 300;
     this.Height    = 300;
     this.xAxisName = "";
     this.yAxisName = "";
     this.MinValue = 0;
     this.MaxValue = 100;
     this.ScaleAngle = 180;
     this.Container  = document.body;
     this.Legend    = "";
     this.ChildCode = "";
     this.unitX = 0;
     this.Margin = new Array(10,10,10,10);
}

/*****************************************************************************************
    Flash 仪表盘类
*****************************************************************************************/
function InstrumentChart(title,width,height,type,amount){
    FlashChart.call(this);
    this.Pointers = new Array();
    this.Ranges = new Array();
    this.Title = title;
    this.Width = width?width:this.Width;
    this.Height = height?height:this.Height;
    this.MajorTMNumber =  amount?amount:11;
    this.Type = type?type:0;    
    this.init();
};

var _p = InstrumentChart.prototype = new FlashChart;

_p.init = function(){
    if(!this.Title){
        this.Title = "";
    }
    this.unitX = parseInt(this.Width/20);
}

_p.setTitle = function(title){
    this.Title = title;
}

_p.setScope = function(minValue,maxValue){
    this.MinValue = minValue;
    this.MaxValue = maxValue; 
}

_p.addData = function(value,color){
    var pointer = "<dial value='" + value + "' " 
    if(color){
        pointer +="bgColor='" + color +"' "
    }
    pointer +="radius='" + (this.unitX*9) +"' "
    //pointer +=" link='JavaScript:window.open(\"" + link + "\"," + target + ")' " }
    pointer += " borderAlpha='0' />" 
    //alert(pointer);
    this.Pointers[this.Pointers.length] = pointer;
}

_p.addRange = function(minValue,maxValue,color){
    var text = "<color minValue='" + minValue + "' maxValue='" + maxValue + "' " 
    if(color){
        text +="code='" + color +"' alpha='60' borderColor='" + color + "' "
    }
        text += " />" 
    this.Ranges[this.Ranges.length] = text;
}

_p.addChildCode = function(code){
    var text = "";
}

_p.getDataXML = function(){
    var str = " <chart upperLimit='" + this.MaxValue + "' lowerLimit='" + this.MinValue + "' majorTMNumber='" + this.MajorTMNumber + "' majorTMHeight='7'" 
        str += " minorTMNumber='4' minorTMColor='000000' minorTMHeight='3' pivotRadius='6' majorTMThickness='2' showBorder='1' "
        str += " gaugeOuterRadius='"+ eval(this.Width/2-this.unitX*2) +"' gaugeInnerRadius='0' gaugeOriginX='" + this.Width/2 + "' gaugeOriginY='" + (this.Width/2+15) + "' gaugeScaleAngle='" + this.ScaleAngle + "' gaugeAlpha='50' "
        str += " placeValuesInside='0' decimalPrecision='2' tickMarkDecimalPrecision='0' baseFontSize='9' displayValueDistance='"+ (-this.unitX*2) + "' baseFontColor='000000' hoverCapBgColor='F2F2FF' hoverCapBorderColor='6A6FA6'>"
        str += " <colorRange>"        
        for(var i=0; i<this.Ranges.length; i++){
            str += this.Ranges[i];
        }
        str += " </colorRange> "
        str += " <dials> "
        for(var i=0; i<this.Pointers.length; i++){
            str += this.Pointers[i];
        }
        str += " </dials>"
        str += " <customObjects>"
        str += "	<objectGroup xPos='" + this.Width/2 + "' yPos='0' showBelowChart='0'> "
        str += "		<object type='text' xPos='0' ypos='7' label='" + this.Title + "' fontColor='000000' fontSize='12' isBold='0' />"
        str += "	</objectGroup>"
        str += "	<objectGroup xPos='" + this.Width/2 + "' yPos='" + (this.Width/2 +15)+ "' showBelowChart='1'> "
        str += "		 <object type='circle' xPos='0' yPos='0' radius='"+ this.Width/2 +"' startAngle='" + eval(this.ScaleAngle>200?0:(88-this.ScaleAngle/2)) + "' endAngle='" + eval(this.ScaleAngle>200?360:(92+this.ScaleAngle/2)) + "' fillAsGradient='1' fillColor='cccccc,111111' fillPattern='linear' fillAlpha='100,100' fillRatio='50,50' fillDegree='45'/>"
        str += "		 <object type='circle' xPos='0' yPos='0' radius='"+ eval(this.Width/2-this.unitX) +"' startAngle='" + eval(this.ScaleAngle>200?0:(88-this.ScaleAngle/2)) + "' endAngle='" + eval(this.ScaleAngle>200?360:(91+this.ScaleAngle/2)) + "' fillAsGradient='1' fillColor='333333,cccccc' fillPattern='linear' fillAlpha='100,100' fillRatio='50,50' fillDegree='45'/>"
        str += "	</objectGroup>"
        str += "	<objectGroup xPos='" + this.Width/2 + "' yPos='" + this.Width/2 + "' showBelowChart='0'> "
        str += "	 	<object type='circle' xPos='0' yPos='15' radius='"+this.unitX+"' startAngle='0' endAngle='360' borderColor= 'bebcb0' fillAsGradient='1' fillColor='cccccc,333333' fillAlpha='100,100' fillRatio='50,50' />"
        //str += "	 	<object type='rectangle' xPos='-50' yPos='20' toXPos='-35' toYPos='35' fillColor='cccccc,cccccc'  />"
        //str += "		<object type='text' xPos='0' ypos='"+ this.unitX*2 +"' label='Good' fontColor='000000' fontSize='12' isBold='1' />"
        //str += "	 	<object type='rectangle' xPos='0' yPos='20' toXPos='20' toYPos='40' fillColor='FF0000,FF0000' />"
        str += "	</objectGroup>"
        str += " </customObjects>"
        str += " </chart>"
   return str;     
} 

_p.getDataXML1 = function(){
    var str = " <chart upperLimit='" + this.MaxValue + "' lowerLimit='" + this.MinValue + "'  majorTMNumber='" + this.MajorTMNumber + "' majorTMHeight='7'" 
        str += " minorTMNumber='4' minorTMColor='000000' minorTMHeight='3' pivotRadius='6' majorTMThickness='2' showBorder='1' "
        str += " gaugeOuterRadius='"+ (this.Width/2-4) +"' gaugeInnerRadius='0' gaugeOriginX='" + this.Width/2 + "' gaugeOriginY='" + (this.Width/2+15) + "' gaugeScaleAngle='" + this.ScaleAngle + "' gaugeAlpha='50' "
        str += " placeValuesInside='0'  decimalPrecision='2' tickMarkDecimalPrecision='0' displayValueDistance='"+ (-this.unitX*2-2) + "' baseFontColor='000000' hoverCapBgColor='F2F2FF' hoverCapBorderColor='6A6FA6'>"
        
        str += " <colorRange>"        
        for(var i=0; i<this.Ranges.length; i++){
            str += this.Ranges[i];
        }
        str += " </colorRange> "
        
        str += " <dials> "
        for(var i=0; i<this.Pointers.length; i++){
            str += this.Pointers[i];
        }
        str += " </dials>"
        
        str += " <customObjects>"
        str += "	<objectGroup xPos='" + this.Width/2 + "' yPos='0' showBelowChart='0'> "
        str += "		<object type='text' xPos='0' ypos='7' label='" + this.Title + "' fontColor='000000' fontSize='12' isBold='0' />"
        str += "	</objectGroup>"
        str += "	<objectGroup xPos='" + this.Width/2 + "' yPos='" + this.Width/2 + "' showBelowChart='0'> "
        str += "	 	<object type='circle' xPos='0' yPos='15' radius='"+this.unitX+"' startAngle='0' endAngle='360' borderColor= 'bebcb0' fillAsGradient='1' fillColor='cccccc,333333' fillAlpha='100,100' fillRatio='50,50' />"
        str += "	</objectGroup>"
        str += " </customObjects>"
        str += " </chart>"
   return str;     
} 

_p.draw = function(){
    this.init();
    var chart = new FusionCharts("/szeip/resources/flash/charts/FI2_Angular.swf", "ChartId", this.Width, this.Width, "0", 0);
    chart.setDataXML(this.Type?this.getDataXML1():this.getDataXML());		   
    chart.render(this.Container);
}

/*****************************************************************************************
    Flash 横向柱状图类
*****************************************************************************************/
function BarChart(title,width,height){
    FlashChart.call(this);
    this.Title = title?title:""; 
    this.Width = width?width:this.Width; 
    this.Height = height?height:this.Height;
    this.Margin = new Array(2,2,30,5);
    this.Data = new Array();
    this.Label = new Array();
}
var _p = BarChart.prototype = new FlashChart;

_p.addData = function(caption,value){
    this.Label[this.Data.length] = caption?caption:"";
    this.Data[this.Data.length] = value?value:0;
}
_p.getDataXML = function(){
    var str = "<chart caption='" + this.Title + "' formatNumberScale='1' xAxisName='"+this.xAxisName+"'  yAxisName='"+this.yAxisName+"' showValues='0' decimals='100' formatNumberScale='0' baseFontSize='12'"
        str += "chartLeftMargin='" +this.Margin[0]+ "' chartTopMargin='" +this.Margin[1]+ "' chartRightMargin='" +this.Margin[2]+ "' chartBottomMargin='" +this.Margin[3]+ "'>"
        for(var i=0,j=this.Data.length; i<j; i++){
            str +="<set label='"+ this.Label[i] +"' value='"+ this.Data[i] +"' />";
        }    
        str += "</chart>";
        //alert(str)
    return str;
}

_p.draw = function(){
    var chart = new FusionCharts("/szeip/resources/flash/charts/Bar2D.swf", "BarChartId", this.Width, this.Height, "0", 0);
    chart.setDataXML(this.getDataXML());
    chart.render(this.Container);
    //alert(chart.getSWFHTML());
    return chart.getSWFHTML();
}

/*****************************************************************************************
    Flash 进度条类
*****************************************************************************************/
function ProcessChart(title,value,goal,width,height){
    FlashChart.call(this);
    this.Title = title?title:"";
    this.BgColor = "FF0000" 
    this.Value = value?value:0; 
    this.Width = width?width:this.Width; 
    this.Height = height?height:this.Height;
    this.Color = "00FF00"; 
    this.TitleWidth = 0;
    this.Goal = goal?goal:""; 
    this.GoalWidth = 0;
    this.RightMargin = 50;
}

var _p = ProcessChart.prototype = new FlashChart;

_p.init = function(){
    this.TitleWidth = this.Title?this.Title.length*12:this.TitleWidth;
    this.GoalWidth = this.Goal?this.Goal.length*12:this.GoalWidth;
}

_p.addData = function(value,color){
    this.Value = value;
}

_p.setColor = function(color){
    this.Color = color;
}

_p.getDataXML = function(){
    var str  = "<chart lowerLimit='0' upperLimit='100' lowerLimitDisplay='0' decimalPrecision='2'  numberSuffix='%25' showTickMarks='0' ledGap='0'  baseFontColor='333333'  bgColor='FFFFFF'"
		str += "ledBoxBgColor='FFFFFF' ledBorderColor='FFFFFF' BorderThickness='0' chartLeftMargin='"+ (this.TitleWidth +4) +"' chartRightMargin='" +this.RightMargin+ "' chartTopMargin='0' chartBottomMargin='-15' >"
		str += "<colorRange><color code='" + this.Color + "' minValue='0' maxValue='" + this.Value + "' /><color code='" + this.BgColor + "' minValue='" + this.Value + "' maxValue='100'/></colorRange>"
        str += "<value>" + this.Value + "</value>"
        str += "<customObjects>"
		str += "<objectGroup xPos='0' yPos='0' showBelowChart='0'>	"
		str += "<object xpos='2' ypos='" +(this.Height/2-10)+ "' type='text' label='" + this.Title + "' fontColor='000000' fontSize='12' align='left' />"
		str += "<object xpos='" + (this.Width-this.RightMargin) + "' ypos='" +(this.Height/2-10)+ "' type='text' label='" + this.Goal + "' fontColor='000000' fontSize='12' align='left' />"
        str += "</objectGroup>"
        str += "</customObjects>"
        str += "</chart>"
    return str;
}
_p.draw = function(){
    this.init();
    var chart = new FusionCharts("/szeip/resources/flash/charts/FI2_HorLED.swf", "ProgressChartId", this.Width, this.Height, "0", 0);
    chart.setDataXML(this.getDataXML());
    chart.render(this.Container);
}
/*****************************************************************************************
    Flash Multi-Series Line 2D Chart
*****************************************************************************************/
function MSLineChart(title,width,height){
    FlashChart.call(this);
    this.Title = title?title:""; 
    this.Width = width?width:this.Width; 
    this.Height = height?height:this.Height;
    this.Thickness="1";//new add
    this.ShowValues="0";//new add
    this.FormatNumberScale="0";//new add
    this.AnchorRadius="2";//new add
    this.LabelStep="5";//new add
    this.Data = new Array();
    this.Label = new Array();
    this.ColorSet=new Array();//color , anchorBorderColor , anchorBgColor
    this.SeriesNameSet=new Array();
    this.ValueSet=new Array();//new add
    this.Date=new Array();
}
var _p = MSLineChart.prototype = new FlashChart;

//添加x轴标签名
_p.addLabel=function(labelValue){
    this.Label[this.Label.length]=labelValue?labelValue:"";
}

_p.addValueSet=function(value){
    this.ValueSet[this.ValueSet.length]=value?value:0;
}

_p.addDataset = function(seriesName,colorSet,valueSet){
    this.SeriesNameSet[this.SeriesNameSet.length] = seriesName?seriesName:"";
    this.ColorSet = colorSet;
    this.ValueSet = valueSet;
}
_p.getDataXML = function(){
    var str = "<chart caption='" + this.Title + "' lineThickness='"+this.Thickness+"' showValues='"+this.ShowValues+"' formatNumberScale='"+this.FormatNumberScale+"' anchorRadius='"+this.AnchorRadius+"' divLineAlpha='20' divLineColor='CC3300' divLineIsDashed='1' showAlternateHGridColor='1' alterHGridColor='CC3300' showAlpha='40' labelStep='"+this.LabelStep+"' numvdivlines='5' +charRightMargin='35' bgColor='FFFFFF,CC3300' bgAngle='270' bgAlpha='10,10'>";        
        str=str+"<categories>";
        for(var i=0; i<this.Label.length; i++){
            str +="<category label='"+ this.Label[i] +"'/>";
        }
        str=str+"</categories>";
           
        for(var i=0,j=this.Data.length; i<j; i++){
            str +="<set label='"+ this.Label[i] +"' value='"+ this.Data[i] +"' />";
        }    
        str += "</chart>";
        //alert(str)
    return str;
}

_p.draw = function(){
    var chart = new FusionCharts("/szeip/resources/flash/charts/Bar2D.swf", "BarChartId", this.Width, this.Height, "0", 0);
    chart.setDataXML(this.getDataXML());
    chart.render(this.Container);
    //alert(chart.getSWFHTML());
    return chart.getSWFHTML();
}