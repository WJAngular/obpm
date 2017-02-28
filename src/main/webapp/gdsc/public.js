
$(function(){
	//改变页面右下角的文字，该文字由license key生成
	removeTitle();
});

//<script type="text/javascript" src="__JS__/jquery.bankInput.js"></script> 
//<script>$(".account").bankInput()$(".account").bankList() 
//</script> 
//
//1.默认使用方法： 
//$("#account").bankInput(); 
//2.设置参数 
//$("#account").bankInput({min:16,max:25,deimiter,' '}); 
//3.非文本框格式化显示 
//$(".account").bankList(); 
//代码如下:

/** 
× JQUERY 模拟淘宝控件银行帐号输入 
* @Author wujing 
**/ 
(function($){ 
// 输入框格式化 
$.fn.bankInput = function(options){ 
var defaults = { 
	min : 10, // 最少输入字数 
	max : 25, // 最多输入字数 
	deimiter : ' ', // 账号分隔符 
	onlyNumber : true, // 只能输入数字 
	copy : true // 允许复制 
}; 
var opts = $.extend({}, defaults, options); 
var obj = $(this); 
obj.css({imeMode:'Disabled',borderWidth:'1px',color:'#000',fontFamly:'Times New Roman'}).attr('maxlength', opts.max); 
if(obj.val() != '') obj.val( obj.val().replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1"+opts.deimiter) ); 
obj.bind('keyup',function(event){ 
	if(opts.onlyNumber){ 
		if(!(event.keyCode>=48 && event.keyCode<=57)){ 
			this.value=this.value.replace(/\D/g,''); 
		} 
	} 
	this.value = this.value.replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1"+opts.deimiter); 
}).bind('dragenter',function(){ 
	return false; 
}).bind('onpaste',function(){ 
	return !clipboardData.getData('text').match(/\D/); 
}).bind('blur',function(){ 
	this.value = this.value.replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1"+opts.deimiter); 
	if(this.value.length < opts.min){ 
		//alert('最少输入'+opts.min+'位账号信息！'); 
		//obj.focus(); 
	} 
}) 
} 
// 列表显示格式化 
$.fn.bankList = function(options){ 
	var defaults = { 
			deimiter : ' ' // 分隔符 
	}; 
	var opts = $.extend({}, defaults, options); 
	return this.each(function(){ 
		$(this).text($(this).text().replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1"+opts.deimiter)); 
	}) 
} 
})(jQuery); 

/* 
 * formatMoney(s,type) 
 * 功能：金额按千位逗号分割 
 * 参数：s，需要格式化的金额数值. 
 * 参数：type,判断格式化后的金额是否需要小数位. 
 * 返回：返回格式化后的数值字符串. 
 */  
function formatMoney(s, type) {  
    if (/[^0-9\.]/.test(s))  
        return "0";  
    if (s == null || s == "")  
        return "0";  
    s = s.toString().replace(/^(\d*)$/, "$1.");  
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");  
    s = s.replace(".", ",");  
    var re = /(\d)(\d{3},)/;  
    while (re.test(s))  
        s = s.replace(re, "$1,$2");  
    s = s.replace(/,(\d\d)$/, ".$1");  
    if (type == 0) {// 不带小数位(默认是有小数位)  
        var a = s.split(".");  
        if (a[1] == "00") {  
            s = a[0];  
        }  
    }  
    return s;  
} 

/**
 * 数字转中文
 * @param dValue
 * @returns
 */
function chineseNumber(dValue) {
    var maxDec = 2;
    // 验证输入金额数值或数值字符串：
    dValue = dValue.toString().replace(/,/g, "");
    dValue = dValue.replace(/^0+/, ""); // 金额数值转字符、移除逗号、移除前导零
    if (dValue == "") {
        return "零元整";
    } // （错误：金额为空！）
    else if (isNaN(dValue)) {
        return "错误：金额不是合法的数值！";
    }
    var minus = ""; // 负数的符号“-”的大写：“负”字。可自定义字符，如“（负）”。
    var CN_SYMBOL = ""; // 币种名称（如“人民币”，默认空）
    if (dValue.length > 1) {
        if (dValue.indexOf('-') == 0) {
            dValue = dValue.replace("-", "");
            minus = "负";
        } // 处理负数符号“-”
        if (dValue.indexOf('+') == 0) {
            dValue = dValue.replace("+", "");
        } // 处理前导正数符号“+”（无实际意义）
    }
    // 变量定义：
    var vInt = "";
    var vDec = ""; // 字符串：金额的整数部分、小数部分
    var resAIW; // 字符串：要输出的结果
    var parts; // 数组（整数部分.小数部分），length=1时则仅为整数。
    var digits, radices, bigRadices, decimals; // 数组：数字（0~9——零~玖）；基（十进制记数系统中每个数字位的基是10——拾,佰,仟）；大基（万,亿,兆,京,垓,杼,穰,沟,涧,正）；辅币（元以下，角/分/厘/毫/丝）。
    var zeroCount; // 零计数
    var i, p, d; // 循环因子；前一位数字；当前位数字。
    var quotient, modulus; // 整数部分计算用：商数、模数。
    // 金额数值转换为字符，分割整数部分和小数部分：整数、小数分开来搞（小数部分有可能四舍五入后对整数部分有进位）。
    var NoneDecLen = (typeof (maxDec) == "undefined" || maxDec == null || Number(maxDec) < 0 || Number(maxDec) > 5); // 是否未指定有效小数位（true/false）
    parts = dValue.split('.'); // 数组赋值：（整数部分.小数部分），Array的length=1则仅为整数。
    if (parts.length > 1) {
        vInt = parts[0];
        vDec = parts[1]; // 变量赋值：金额的整数部分、小数部分
        if (NoneDecLen) {
            maxDec = vDec.length > 5 ? 5 : vDec.length;
        } // 未指定有效小数位参数值时，自动取实际小数位长但不超5。
        var rDec = Number("0." + vDec);
        rDec *= Math.pow(10, maxDec);
        rDec = Math.round(Math.abs(rDec));
        rDec /= Math.pow(10, maxDec); // 小数四舍五入
        var aIntDec = rDec.toString().split('.');
        if (Number(aIntDec[0]) == 1) {
            vInt = (Number(vInt) + 1).toString();
        } // 小数部分四舍五入后有可能向整数部分的个位进位（值1）
        if (aIntDec.length > 1) {
            vDec = aIntDec[1];
        } else {
            vDec = "";
        }
    } else {
        vInt = dValue;
        vDec = "";
        if (NoneDecLen) {
            maxDec = 0;
        }
    }
    if (vInt.length > 44) {
        return "错误：金额值太大了！整数位长【" + vInt.length.toString() + "】超过了上限——44位/千正/10^43（注：1正=1万涧=1亿亿亿亿亿，10^40）！";
    }
    // 准备各字符数组 Prepare the characters corresponding to the digits:
    digits = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); // 零~玖
    radices = new Array("", "拾", "佰", "仟"); // 拾,佰,仟
    bigRadices = new Array("", "万", "亿", "兆", "京", "垓", "杼", "穰", "沟", "涧", "正"); // 万,亿,兆,京,垓,杼,穰,沟,涧,正
    decimals = new Array("角", "分", "厘", "毫", "丝"); // 角/分/厘/毫/丝
    resAIW = ""; // 开始处理
    // 处理整数部分（如果有）
    if (Number(vInt) > 0) {
        zeroCount = 0;
        for (i = 0; i < vInt.length; i++) {
            p = vInt.length - i - 1;
            d = vInt.substr(i, 1);
            quotient = p / 4;
            modulus = p % 4;
            if (d == "0") {
                zeroCount++;
            } else {
                if (zeroCount > 0) {
                    resAIW += digits[0];
                }
                zeroCount = 0;
                resAIW += digits[Number(d)] + radices[modulus];
            }
            if (modulus == 0 && zeroCount < 4) {
                resAIW += bigRadices[quotient];
            }
        }
        resAIW += "元";
    }
    // 处理小数部分（如果有）
    for (i = 0; i < vDec.length; i++) {
        d = vDec.substr(i, 1);
        if (d != "0") {
            resAIW += digits[Number(d)] + decimals[i];
        }
    }
    // 处理结果
    if (resAIW == "") {
        resAIW = "零" + "元";
    } // 零元
    if (vDec == "") {
        resAIW += "整";
    } // ...元整
    resAIW = CN_SYMBOL + minus + resAIW; // 人民币/负......元角分/整
    return resAIW;
}
 
/**
 * 中文转数字
 * @param num
 * @returns
 */
function aNumber(num) {
    var numArray = new Array();
    var unit = "亿万元$";
    for ( var i = 0; i < unit.length; i++) {
        var re = eval("/" + (numArray[i - 1] ? unit.charAt(i - 1) : "") + "(.*)" + unit.charAt(i) + "/");
        if (num.match(re)) {
            numArray[i] = num.match(re)[1].replace(/^拾/, "壹拾");
            numArray[i] = numArray[i].replace(/[零壹贰叁肆伍陆柒捌玖]/g, function($1) {
                return "零壹贰叁肆伍陆柒捌玖".indexOf($1);
            });
            numArray[i] = numArray[i].replace(/[分角拾佰仟]/g, function($1) {
                return "*" + Math.pow(10, "分角 拾佰仟 ".indexOf($1) - 2) + "+"
            }).replace(/^\*|\+$/g, "").replace(/整/, "0");
            numArray[i] = "(" + numArray[i] + ")*" + Math.ceil(Math.pow(10, (2 - i) * 4));
        } else
            numArray[i] = 0;
    }
    return eval(numArray.join("+"));
}

/** 获取系统当前时间  例如：2016-05-16 17:48:34*/
function currDateTime(str) {
    var d = new Date();
    var result = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    var hour = d.getHours();
    var minute = d.getMinutes();
    var second = d.getSeconds();
    if (month > 9) {
    	result = result + "-" + month ;
    } else {
    	result = result + "-0" + month ;
    }
    if (date > 9) {
    	result = result + "-" + date;
    } else {
    	result = result + "-0" + date;
    }
    if (hour > 9) {
    	result = result + " " + hour;
    } else {
    	result = result + " 0" + hour;
    }
    if (minute > 9) {
    	result = result + ":" + minute;
    } else {
    	result = result + ":0" + minute;
    }
    if (second > 9) {
    	result = result + ":" + second;
    } else {
    	result = result + ":0" + second;
    }
    $("input[name='"+str+"']").val(result);
    return result;
}
var timeRefresh = function(str){
	if($("input[name='"+str+"']").val() == ""){
		//然后定时 1秒刷新时间
		setInterval(function(){currDateTime(str);},1000);
    }
}

//金额大写设置
var changeMoneyBig = function(str){
	$("input[name='"+str+"']").change(function(){
		var money = $("input[name='"+str+"']").val();
		if( money != ""){
			 $("input[name='"+str+"']").val(chineseNumber(money));
		}
	});
};

//金额格式设置，字段值改变时候触发
var changeMoneyStyle = function(str){
	$("input[name='"+str+"']").change(function(){
		var money = $("input[name='"+str+"']").val();
		if( money != ""){
			if(money.indexOf(",") > -1){
				money = money.replace(",","");
			}
			$("input[name='"+str+"']").val(formatMoney(money,2));
		}
	});
};

//金额格式设置，字段值改变时候触发  金额小写时，金额大写设置
var changeMoneyBigForSmall = function(str1,str2){
	$("input[name='"+str1+"']").change(function(){
		var money = $("input[name='"+str1+"']").val();
		if( money != ""){
			if(money.indexOf(",") > -1){
				money = money.replace(",","");
			}
			$("input[name='"+str2+"']").val(chineseNumber(money));
		}
	});
};
//设置银行卡格式
var bankInfoStyle = function(str){
	//设置银行卡信息
	$("input[name='"+str+"']").focus(function(){
		$("input[name='"+str+"']").bankInput();
	});
}

//设置银行卡号至少10位帐号
var bankInfoMoreTen = function(str){
	$("input[name='"+str+"']").blur(function(){
		var bankAccount = $("input[name='"+str+"']").val();
		if(bankAccount != ""){
			bankAccount = bankAccount.replace(" ","");
			if(bankAccount.length < 10){
				alert("最少输入10位账号信息");
				$("input[name='"+str+"']").focus();
			}
		}
	});
}

//设置合同金额总数、已支出金额数、余额数
/**
 * sum : 合同金额总数
 * pay : 已支出金额数
 * balance : 余额数
 */
var moneySumAndPayAndBalance = function(sum,pay,balance){
	var sumFund = $("input[name='"+sum+"']").val();
	var payFund = $("input[name='"+pay+"']").val();
	var applyFund = $("input[name='本次申请金额']").val();
	if(sumFund != "" && payFund != ""){
		if(sumFund.indexOf(",")>-1){sumFund = sumFund.replace(",","");}
		if(payFund.indexOf(",")>-1){payFund = payFund.replace(",","");}
		$("input[name='"+sum+"']").val(formatMoney(sumFund,2));
		$("input[name='"+pay+"']").val(formatMoney(payFund,2));
		if(parseFloat(formatMoney(payFund,2).replace(",","")) > parseFloat(formatMoney(sumFund,2).replace(",",""))){
			alert(""+pay+"不能大于"+sum+"!");
			$("input[name='"+pay+"']").focus();
			return;
		}
		$("input[name='"+balance+"']").val(formatMoney((parseFloat(sumFund) - parseFloat(payFund)).toString()));
	}
	
}

//设置合同金额总数、已支出金额数、余额数某个字段值改变时触发方法
/**
 * str : 需要失去焦点触发的字段name
 * sum : 合同金额总数
 * pay : 已支出金额数
 * balance : 余额数
 */
var blurFun = function(str,sum,pay,balance){
	$("input[name='"+str+"']").change(function(){
		moneySumAndPayAndBalance(sum,pay,balance);
	});
}

//隐藏某个字段
var hideItem = function(str){
	$("input[name='"+str+"']").hide();
}

//显示某个字段
var showItem = function(str){
	$("input[name='"+str+"']").show();
}
//设置某个字段的属性值
var attrItem = function(str,attr,val){
	$("input[name='"+str+"']").attr(""+attr+"",""+val+"");
}

//设置控件默认大小 高度以及宽度
/**
 * width : 控件宽度
 * height : 控件高度
 * isWdith : 是否设置宽度
 * isHeight : 是否设置高度
 */
var inputWidthHeight = function(str,width,height,isWidth,isHeight){
	if(isWidth && isHeight){
		$("input[name='"+str+"']").attr("style","width:"+width+"height:"+height);
	}else if(isWidth && !isHeight){
		$("input[name='"+str+"']").attr("style","width:"+width);
	}else if(!isWidth && isHeight){
		$("input[name='"+str+"']").attr("style","height:"+height);
	}
};
//改变页面右下角的文字，该文字由license key生成
var removeTitle = function(){
	var title = $("td[title='SVNNO:']");
	title.html("<font color='red'>广东思程科技有限公司  | 管理平台</font>");
};

/** 获取系统当前日期 */
function currDate() {
    var d = new Date();
    var result = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    if (month > 9) {
	result = result + "-" + month;
    } else {
	result = result + "-0" + month;
    }
    if (date > 9) {
	result = result + "-" + date;
    } else {
	result = result + "-0" + date;
    }
    return result;
}
/** 
 * 获取本周、本季度、本月、上月的开始日期、结束日期 
 */  
var now = new Date();                    //当前日期     
var nowDayOfWeek = now.getDay();         //今天本周的第几天     
var nowDay = now.getDate();              //当前日     
var nowMonth = now.getMonth();           //当前月     
var nowYear = now.getYear();             //当前年   
var oneDay = 1000 * 60 * 60 * 24;
nowYear += (nowYear < 2000) ? 1900 : 0;  //    
  
var lastMonthDate = new Date();  //上月日期  
lastMonthDate.setDate(1);  
lastMonthDate.setMonth(lastMonthDate.getMonth()-1);  
var lastYear = lastMonthDate.getYear();  
var lastMonth = lastMonthDate.getMonth();  

//格式化日期：yyyy-MM-dd     
function formatDate(date) {      
    var myyear = date.getFullYear();     
    var mymonth = date.getMonth()+1;     
    var myweekday = date.getDate();      
         
    if(mymonth < 10){     
        mymonth = "0" + mymonth;     
    }      
    if(myweekday < 10){     
        myweekday = "0" + myweekday;     
    }     
    return (myyear+"-"+mymonth + "-" + myweekday);      
}  

// 获取条形码需要的日期
function getBarCodeDate(){
	var myyear = nowYear%10; 
	var mymonth = nowMonth+1;
	if(mymonth < 10){     
        mymonth = "0" + mymonth;     
    } 
    var mydate = nowDay;      
    if(mydate < 10){     
    	mydate = "0" + mydate;     
    }  
    return myyear+mymonth+mydate;
}
    
//获得某月的天数     
function getMonthDays(myMonth){     
    var monthStartDate = new Date(nowYear, myMonth, 1);      
    var monthEndDate = new Date(nowYear, myMonth + 1, 1);      
    var days = (monthEndDate - monthStartDate)/oneDay;      
    return days;      
}     
    
//获得本季度的开始月份     
function getQuarterStartMonth(){     
    var quarterStartMonth = 0;     
    if(nowMonth<3){     
       quarterStartMonth = 0;     
    }     
    if(2<nowMonth && nowMonth<6){     
       quarterStartMonth = 3;     
    }     
    if(5<nowMonth && nowMonth<9){     
       quarterStartMonth = 6;     
    }     
    if(nowMonth>8){     
       quarterStartMonth = 9;     
    }     
    return quarterStartMonth;     
}     
    
//获得本周的开始日期     
function getWeekStartDate() {      
    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek+1);      
    return formatDate(weekStartDate);     
} 

// 获取当前时间加减天数后的日期
function getCurrAddDate(days) {
	var paramDate = new Date(nowYear,nowMonth,nowDay + parseInt(days));  
	return formatDate(paramDate);     
} 

// 时间加法
function getAddDate(param,days) {
	var arr = param.split("-");  
    var paramDate = new Date(Number(arr[0]), Number(arr[1])-1, Number(arr[2]) + parseInt(days));  
    return formatDate(paramDate);     
} 
    
//获得本周的结束日期     
function getWeekEndDate() {      
    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek)+1);      
    return formatDate(weekEndDate);     
}      
    
//获得本月的开始日期     
function getMonthStartDate(){     
    var monthStartDate = new Date(nowYear, nowMonth, 1);      
    return formatDate(monthStartDate);     
}     
    
//获得本月的结束日期     
function getMonthEndDate(){     
    var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));      
    return formatDate(monthEndDate);     
}     
  
//获得前一月开始时间  
function getPreMonthStartDate(param){  
	var arr = param.split("-"); 
	var year = Number(arr[0]);
	var month = Number(arr[1])-1; 
	var day = Number(arr[2]);
	if(month == 0){
		year = year-1;
		month =1;
	}
    return formatDate(new Date(year, month, 1));    
} 
//获得前一月结束时间  
function getPreMonthEndDate(param){
	var lastMonthStartDate = new Date(nowYear, lastMonth, 1);  
	return formatDate(lastMonthStartDate);    
}  


//获得上月开始时间  
function getLastMonthStartDate(){  
	var lastMonthStartDate = new Date(nowYear, lastMonth, 1);  
	return formatDate(lastMonthStartDate);    
}  
  
//获得上月结束时间  
function getLastMonthEndDate(){  
    var lastMonthEndDate = new Date(nowYear, lastMonth, getMonthDays(lastMonth));  
    return formatDate(lastMonthEndDate);    
}  
    
//获得本季度的开始日期     
function getQuarterStartDate(){     
    var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);      
    return formatDate(quarterStartDate);     
}     
    
//或的本季度的结束日期     
function getQuarterEndDate(){     
    var quarterEndMonth = getQuarterStartMonth() + 2;     
    var quarterStartDate = new Date(nowYear, quarterEndMonth, getMonthDays(quarterEndMonth));      
    return formatDate(quarterStartDate);     
} 
