
var gSetColorType = "";
var gIsIE = document.all; // document.all是ie浏览器特有的属性 gIsIE用于判断当前浏览器是否是IE
var gIEVer = fGetIEVer(); // 获取当前使用的IE的version
var gLoaded = false; // 页面是否全部加载完毕，包括所有图片和文字。 gLoaded的属性在window.onload =
						// function(){……}执行后变为true
var ev = null;

function fGetEv(e) {
	ev = e;
}

/**
 * 重新set域名信息
 */
function ResetDomain() {
	/*
	 * 将页面上的id或是name为HtmlEditor的iframe元素给f，
	 * （这里需要说明的是，163edit.html中是没有显式的HtmlEditor，在editor.html中才有，因为editor.html会对163edit.html中的ifarme重写）
	 */
	var f = window.frames["HtmlEditor"];
	var ss = document.domain; // 获取域名信息 ss == "www.126.com";
	var ii = ss.lastIndexOf('.');
	if (ii > 0) {
		if (!isNaN(ss.substr(ii + 1) * 1))
			return;
		ii = ss.lastIndexOf('.', ii - 1);
		if (ii > 0) {
			f.document.domain = ss.substr(ii + 1);
			document.domain = ss.substr(ii + 1);
		}
	} // document.domain == "126.com";
}

/**
 * 获取所使用的IE的version
 */
function fGetIEVer() {
	var iVerNo = 0;
	var sVer = navigator.userAgent;
	if (sVer.indexOf("MSIE") > -1) {
		var sVerNo = sVer.split(";")[1];
		sVerNo = sVerNo.replace("MSIE", "");
		iVerNo = parseFloat(sVerNo);
	}
	return iVerNo;
}

/**
 * 把页面上的指定iframe成为可编辑模式 这个方法在window.onload后被调用
 */
function fSetEditable() {
	var f = window.frames["HtmlEditor"];
	f.document.designMode = "on";
	if (!gIsIE) // 非IE的处理
		f.document.execCommand("useCSS", false, true);
}

/**
 * 设置事件
 */
function fSetFrmClick() {
	var f = window.frames["HtmlEditor"];

	// 鼠标指针在iframe中移动时,执行window.onblur() 方法
	f.document.onmousemove = function() {
		window.onblur();
	};

	f.document.onclick = function() {
		fHideMenu();
	};
	f.document.onkeydown = function() {
		top.frames["jsFrame"].gIsEdited = true;
	};
}

function fSetHtmlContent() {
	try {
		var f = window.frames["HtmlEditor"];
		var CM = window.parent.parent.parent.frames["jsFrame"].CM;
		var GE = window.parent.parent.parent.frames["jsFrame"].GE;
		win = window.parent.parent.parent.frames["jsFrame"];
		if (!GE.IsIE) {
			try {
				if (!CM["compose"].htext) {
					return;
				}
				if (CM["compose"].htext.trim() != "") {
					f.document.getElementsByTagName("BODY")[0].innerHTML = CM["compose"].htext;
					window.parent.parent.parent.status = "完成.";
				}
			} catch (e) {
				window.setTimeout("fSetHtmlContent()", 1500);
			}
		} else {
			if (GE.composeType == "reply"
					|| GE.composeType == "forwardByCompose") {
				if (win.gReplyContent) {
					window.setTimeout('fSetReplyContent()', 500);
				} else {
					return;
				}
			} else if (GE.composeType == "draft" || GE.composeType == "photo"
					|| GE.composeType == "netfolder") {

			} else {
				window.setTimeout('fSetSign()', 500);
			}
			// f.focus();
		}
	} catch (exp) {

	}
}
function fSetReplyContent() {
	try {
		win.fSetComposeContent(win.gReplyContent);
		window.frames["HtmlEditor"].focus();
	} catch (exp) {
		window.setTimeout('fSetReplyContent()', 1000);
	}
	win.gReplyContent = null;
}
function fSetSign() {
	var f = window.frames["HtmlEditor"];
	var CM = window.parent.parent.parent.frames["jsFrame"].CM;
	var GE = window.parent.parent.parent.frames["jsFrame"].GE;
	win = window.parent.parent.parent.frames["jsFrame"];
	f.document.body.innerHTML += "<div>&nbsp;</div><div>&nbsp;</div><div>&nbsp;</div><div>&nbsp;</div><div ID=\"spnSign\"></div>";
	spnSign = f.document.getElementById("spnSign");
	if (win.gDefaultSignMode == "0") {
		spnSign.innerText = win.gDefaultSign;
	} else {
		spnSign.innerHTML = win.gDefaultSign;
	}
	// window.frames["HtmlEditor"].focus();
}
function Request(name) {// 获取页面ID参数
	var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
	if (reg.test(location.href))
		return unescape(RegExp.$2.replace(/\+/g, " "));
	return "";
}

function LoadContent(ContentID) {
	if (typeof (window.frames["HtmlEditor"]) == "object") {
		eval("window.frames['HtmlEditor'].document.getElementsByTagName('BODY')[0].innerHTML=window.parent.document.getElementById('"
				+ ContentID + "').value;");
		eval("setInterval(\"SaveContent('" + ContentID + "')\",500)");
	} else {
		eval("setTimeout(\"LoadContent('" + ContentID + "')\",200)");
	}
}

function SaveContent(ContentID) {
	var sourceEditor = $("sourceEditor");
	if (sourceEditor.value != window.frames['HtmlEditor'].document
			.getElementsByTagName('BODY')[0].innerHTML) {
		if (sourceEditor.style.display == "none") {
			sourceEditor.value = window.frames['HtmlEditor'].document
					.getElementsByTagName('BODY')[0].innerHTML;
		} else {
			window.frames['HtmlEditor'].document.getElementsByTagName('BODY')[0].innerHTML = sourceEditor.value;
		}
	}
	if (eval("window.parent.document.getElementById('"
			+ ContentID
			+ "').value!=window.frames['HtmlEditor'].document.getElementsByTagName('BODY')[0].innerHTML")) {
		eval("window.parent.document.getElementById('"
				+ ContentID
				+ "').value=window.frames['HtmlEditor'].document.getElementsByTagName('BODY')[0].innerHTML;");
	}
}

/**
 * 当前页面中的全部内容都加载完毕后，执行这个函数
 */
window.onload = function() {
	try {
		gLoaded = true; // 把这个量的值改成true，表示页面已经加载完毕了
		fSetEditable(); // 把页面上的指定的iframe变成编辑模式
		fSetFrmClick();
		ResetDomain();
		fSetHtmlContent();
		top.frames["jsFrame"].fHideWaiting();
	} catch (e) {
		// window.location.reload();
	}
};

function fSetColor() {
	var dvForeColor = $("dvForeColor");
	if (dvForeColor.getElementsByTagName("TABLE").length == 1) {
		dvForeColor.innerHTML = drawCube() + dvForeColor.innerHTML;
	}
}

/**
 * 鼠标指针在iframe中移动时,执行这个方法
 */
window.onblur = function() {
	//return;
	var dvForeColor = $("dvForeColor");
	var dvPortrait = $("dvPortrait");
	dvForeColor.style.display = "none";
	dvPortrait.style.display = "none";

	if (!gIsIE || 1 == 1) {
		fHideMenu();
	}
};

document.onmousemove = function(e) {
	if (gIsIE)
		var el = event.srcElement; // srcElement 对于生成事件的 Window 对象、Document 对象或
									// Element 对象的引用。
	else
		var el = e.target;
	var tdView = $("tdView"); // js中 $ 的作用相当于document.getElementById
	var tdColorCode = $("tdColorCode");
	var dvForeColor = $("dvForeColor");
	var dvPortrait = $("dvPortrait");
	var fontsize = $("fontsize");
	var fontface = $("fontface");

	if (el.tagName == "IMG") {
		try {
			if (fCheckIfColorBoard(el)) {
				tdView.bgColor = el.parentNode.bgColor;
				tdColorCode.innerHTML = el.parentNode.bgColor;
			}
		} catch (e) {
		}
	} else {
		return;
		dvForeColor.style.display = "none";
		if (!fCheckIfPortraitBoard(el))
			dvPortrait.style.display = "none";
		if (!fCheckIfFontFace(el))
			fontface.style.display = "none";
		if (!fCheckIfFontSize(el))
			fontsize.style.display = "none";
	}
};

document.onclick = function(e) {
	if (gIsIE)
		var el = event.srcElement;
	else
		var el = e.target;
	var dvForeColor = $("dvForeColor");
	var dvPortrait = $("dvPortrait");

	if (el.tagName == "IMG") {
		try {
			if (fCheckIfColorBoard(el)) {
				format(gSetColorType, el.parentNode.bgColor);
				dvForeColor.style.display = "none";
				return;
			}
		} catch (e) {
		}
		try {
			if (fCheckIfPortraitBoard(el)) {
				format("InsertImage", el.src);
				dvPortrait.style.display = "none";
				return;
			}
		} catch (e) {
		}
	}
	fHideMenu();
	switch (el.id) {
	case "imgFontface":
		var fontface = $("fontface");
		if (fontface)
			fontface.style.display = "";
		break;
	case "imgFontsize":
		var fontsize = $("fontsize");
		if (fontsize)
			fontsize.style.display = "";
		break;
	case "imgFontColor":
		var dvForeColor = $("dvForeColor");
		if (dvForeColor)
			dvForeColor.style.display = "";
		break;
	case "imgBackColor":
		var dvForeColor = $("dvForeColor");
		if (dvForeColor)
			dvForeColor.style.display = "";
		break;
	case "imgFace":
		var dvPortrait = $("dvPortrait");
		if (dvPortrait)
			dvPortrait.style.display = "";
		break;
	case "imgAlign":
		var divAlign = $("divAlign");
		if (divAlign)
			divAlign.style.display = "";
		break;
	case "imgList":
		var divList = $("divList");
		if (divList)
			divList.style.display = "";
		break;
	}
};
function format(type, para) {
	var f = window.frames["HtmlEditor"];
	var sAlert = "";
	if (!gIsIE) {
		switch (type) {
		case "Cut":
			sAlert = "你的浏览器安全设置不允许编辑器自动执行剪切操作,请使用键盘快捷键(Ctrl+X)来完成";
			break;
		case "Copy":
			sAlert = "你的浏览器安全设置不允许编辑器自动执行拷贝操作,请使用键盘快捷键(Ctrl+C)来完成";
			break;
		case "Paste":
			sAlert = "你的浏览器安全设置不允许编辑器自动执行粘贴操作,请使用键盘快捷键(Ctrl+V)来完成";
			break;
		}
	}
	if (sAlert != "") {
		alert(sAlert);
		return;
	}
	f.focus();
	if (!para)
		if (gIsIE)
			f.document.execCommand(type);
		else
			f.document.execCommand(type, false, false);
	else
		f.document.execCommand(type, false, para);
	f.focus();
}
function setMode(bStatus) {
	var sourceEditor = $("sourceEditor");
	var HtmlEditor = $("HtmlEditor");
	var divEditor = $("divEditor");
	var f = window.frames["HtmlEditor"];
	var body = f.document.getElementsByTagName("BODY")[0];
	if (bStatus) {
		sourceEditor.style.display = "";
		HtmlEditor.style.height = "0px";
		divEditor.style.height = "0px";
		sourceEditor.value = body.innerHTML;
	} else {
		sourceEditor.style.display = "none";
		if (gIsIE) {
			HtmlEditor.style.height = "286px";
			divEditor.style.height = "286px";
		} else {
			HtmlEditor.style.height = "283px";
			divEditor.style.height = "283px";
		}
		body.innerHTML = sourceEditor.value;
		// fSetEditable();
	}
}
function foreColor(e) {
	fDisplayColorBoard(e);
	gSetColorType = "foreColor";
	/*
	 * var sColor = fDisplayColorBoard(e); gSetColorType = "foreColor";
	 * if(gIsIE) format(gSetColorType, sColor);
	 */
}
function backColor(e) {
	var sColor = fDisplayColorBoard(e);
	if (gIsIE)
		gSetColorType = "backcolor";
	else
		gSetColorType = "backcolor";
	// if(gIsIE) format(gSetColorType, sColor);
}
function fDisplayColorBoard(e) {
	if (gIsIE) {
		var e = window.event;
	}
	if (gIEVer <= 5.01 && gIsIE) {
		var arr = showModalDialog(
				"ColorSelect.htm",
				"",
				"font-family:Verdana; font-size:12; status:no; dialogWidth:21em; dialogHeight:21em");
		if (arr != null)
			return arr;
		return;
	}
	var dvForeColor = $("dvForeColor");
	fSetColor();
	var iX = e.clientX;
	var iY = e.clientY;
	dvForeColor.style.display = "";
	dvForeColor.style.left = (iX - 60) + "px";
	dvForeColor.style.top = 33 + "px";
	return true;
}
function createLink() {
	var sURL = window.prompt("Enter link location (e.g. http://www.abc.com/):",
			"http://");
	if ((sURL != null) && (sURL != "http://")) {
		format("CreateLink", sURL);
	}
}
function createImg() {
	var sPhoto = prompt("请输入图片位置:", "http://");
	if ((sPhoto != null) && (sPhoto != "http://")) {
		format("InsertImage", sPhoto);
	}
}
function addPortrait(e) {
	if (gIEVer <= 5.01 && gIsIE || 1 == 1) {
		// var imgurl = showModalDialog("portraitSelect.htm","",
		// "font-family:Verdana; font-size:12; status:no; unadorned:yes;
		// scroll:no; resizable:yes;dialogWidth:358px; dialogHeight:232px");
		// if (imgurl != null) format("InsertImage", imgurl);
		var dvPortrait = $("dvPortrait");
		if (dvPortrait) {
			dvPortrait.parentNode.removeChild(dvPortrait);
		}
		var div = document.createElement("DIV");
		div.style.position = "absolute";
		div.style.zIndex = "9";
		div.id = "dvPortrait";
		var iX = e.clientX;
		div.style.top = 33 + "px";
		div.style.left = (iX - 380) + "px";
		div.innerHTML = '<iframe border=0 marginWidth=0 marginHeight=0 frameBorder=no style="width:358px;height:232px" src="portraitSelect.jsp">';
		document.body.appendChild(div);
		var dvPortrait = $("dvPortrait");
		dvPortrait.style.display = "";
		return;
	}
	var dvPortrait = $("dvPortrait");
	var tbPortrait = $("tbPortrait");
	var iX = e.clientX;
	var iY = e.clientY;
	dvPortrait.style.display = "";
	if (window.screen.width == 1024) {
		dvPortrait.style.left = (iX - 380) + "px";
	} else {
		if (gIsIE)
			dvPortrait.style.left = (iX - 380) + "px";
		else
			dvPortrait.style.left = (iX - 380) + "px";
	}
	dvPortrait.style.top = 33 + "px";
	dvPortrait.innerHTML = '<table width="100%" border="0" cellpadding="5" cellspacing="1" style="cursor:hand" bgcolor="black" ID="tbPortrait"><tr align="left" bgcolor="#f8f8f8" class="unnamed1" align="center" ID="trContent">' + drawPortrats() + '</tr>	</table>';
}

// 检测是否有父节点 并且父节点的id是否是dvForeColor
function fCheckIfColorBoard(obj) {
	if (obj.parentNode) {
		if (obj.parentNode.id == "dvForeColor")
			return true;
		else
			return fCheckIfColorBoard(obj.parentNode);
	} else {
		return false;
	}
}

function fCheckIfPortraitBoard(obj) {
	if (obj.parentNode) {
		if (obj.parentNode.id == "dvPortrait")
			return true;
		else
			return fCheckIfPortraitBoard(obj.parentNode);
	} else {
		return false;
	}
}
function fCheckIfFontFace(obj) {
	if (obj.parentNode) {
		if (obj.parentNode.id == "fontface")
			return true;
		else
			return fCheckIfFontFace(obj.parentNode);
	} else {
		return false;
	}
}
function fCheckIfFontSize(obj) {
	if (obj.parentNode) {
		if (obj.parentNode.id == "fontsize")
			return true;
		else
			return fCheckIfFontSize(obj.parentNode);
	} else {
		return false;
	}
}
function fImgOver(el) {
	if (el.tagName == "IMG") {
		el.style.borderRight = "1px #cccccc solid";
		el.style.borderBottom = "1px #cccccc solid";
	}
}
function fImgMoveOut(el) {
	if (el.tagName == "IMG") {
		el.style.borderRight = "1px #F3F8FC solid";
		el.style.borderBottom = "1px #F3F8FC solid";
	}
}
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
function fSetBorderMouseOver(obj) {
	obj.style.borderRight = "1px solid #aaa";
	obj.style.borderBottom = "1px solid #aaa";
	obj.style.borderTop = "1px solid #fff";
	obj.style.borderLeft = "1px solid #fff";
	/*
	 * var sd = document.getElementsByTagName("div"); for(i=0;i<sd.length;i++) {
	 * sd[i].style.display = "none"; }
	 */
}

function fSetBorderMouseOut(obj) {
	obj.style.border = "none";
}

function fSetBorderMouseDown(obj) {
	obj.style.borderRight = "1px #F3F8FC solid";
	obj.style.borderBottom = "1px #F3F8FC solid";
	obj.style.borderTop = "1px #cccccc solid";
	obj.style.borderLeft = "1px #cccccc solid";
}

function fDisplayElement(element, displayValue) {
	if (gIEVer <= 5.01 && gIsIE) {
		if (element == "fontface") {
			var sReturnValue = showModalDialog(
					"FontFaceSelect.html",
					"",
					"font-family:Verdana; font-size:12; status:no; unadorned:yes; scroll:no; resizable:yes;dialogWidth:112px; dialogHeight:271px");
			;
			format("fontname", sReturnValue);
		} else if (element == "fontsize") {
			var sReturnValue = showModalDialog(
					"FontSizeSelect.html",
					"",
					"font-family:Verdana; font-size:12; status:no; unadorned:yes; scroll:no; resizable:yes;dialogWidth:130px; dialogHeight:250px");
			;
			format("fontsize", sReturnValue);
		} else if (element == "divAlign") {
			var sReturnValue = showModalDialog(
					"AlignSelect.html",
					"",
					"font-family:Verdana; font-size:12; status:no; unadorned:yes; scroll:no; resizable:yes;dialogWidth:40px; dialogHeight:45px");
			;
			format(sReturnValue);
		} else if (element == "divList") {
			var sReturnValue = showModalDialog(
					"ListSelect.html",
					"",
					"font-family:Verdana; font-size:12; status:no; unadorned:yes; scroll:no; resizable:yes;dialogWidth:60px; dialogHeight:45px");
			;
			format(sReturnValue);
		}
		return;
	}
	fHideMenu();
	if (typeof element == "string")
		element = $(element);
	if (element == null)
		return;
	element.style.display = displayValue;
	if (gIsIE) {
		var e = event;
	} else {
		var e = ev;
	}
	var iX = e.clientX;
	var iY = e.clientY;
	element.style.display = "";
	element.style.left = (iX - 30) + "px";
	element.style.top = 33 + "px";
	return true;
}
function fSetModeTip(obj) {
	var x = f_GetX(obj);
	var y = f_GetY(obj);
	var dvModeTip = $("dvModeTip");
	if (!dvModeTip) {
		var dv = document.createElement("DIV");
		dv.style.position = "absolute";
		dv.style.top = 33 + "px";
		dv.style.left = (x - 40) + "px";
		dv.style.zIndex = "999";
		dv.style.fontSize = "12px";
		dv.id = "dvModeTip";
		dv.style.padding = "2 2 0 2px";
		dv.style.border = "1px #000000 solid";
		dv.style.backgroundColor = "#FFFFCC";
		dv.style.height = "12px";
		dv.innerHTML = "编辑源码";
		document.body.appendChild(dv);
	} else {
		dvModeTip.style.display = "";
	}
}
function fHideTip() {
	$("dvModeTip").style.display = "none";
}
function f_GetX(e) {
	var l = e.offsetLeft;
	while (e = e.offsetParent) {
		l += e.offsetLeft;
	}
	return l;
}
function f_GetY(e) {
	var t = e.offsetTop;
	while (e = e.offsetParent) {
		t += e.offsetTop;
	}
	return t;
}
function fHideMenu() {
	var fontface = $("fontface");
	var fontsize = $("fontsize");
	var dvForeColor = $("dvForeColor");
	var dvPortrait = $("dvPortrait");
	var divAlign = $("divAlign");
	var divList = $("divList");
	if (dvForeColor)
		dvForeColor.style.display = "none";
	if (dvPortrait)
		dvPortrait.style.display = "none";
	if (fontface)
		fontface.style.display = "none";
	if (fontsize)
		fontsize.style.display = "none";
	if (divAlign)
		divAlign.style.display = "none";
	if (divList)
		divList.style.display = "none";
}
function $(id) {
	return document.getElementById(id);
}
window.onerror = function() {
	return true;
};
function fGetList() {

}
function fGetAlign() {

}
function fHide(obj) {
	obj.style.display = "none";
}
