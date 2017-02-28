<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.core.email.util.EmailConfig"%>
<%@ page import="cn.myapps.core.sysconfig.ejb.KmConfig"%>
<%@ page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@ page import="cn.myapps.core.deploy.application.action.ApplicationHelper"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="myapps" prefix="o"%>
<%
String appId = request.getParameter("application");
String appName = "微OA365";
if (appId!=null && appId.trim().length()>0 && !appId.equals("null")) {
	ApplicationHelper ah = new ApplicationHelper();
	ApplicationVO appVO = ah.getApplicationById(appId);
	appName = appVO.getName();
}

SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
String _date=dateFormater.format(new Date());
%>
<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<!doctype html>
<html>
<head>
<title><%=appName%></title>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<script>
var obpmPhone = {
	bar1 : "<s:url value='/portal/phone/resource/images/banner01.png'/>",
	bar2 : "<s:url value='/portal/phone/resource/images/banner02.png'/>",
	bar3 : "<s:url value='/portal/phone/resource/images/banner03.png'/>",
	pendingNum : "",
	processingNum : ""
};
</script>
<script id="script" src="<s:url value='/portal/phone/resource/js/jquery-1.11.3.min.js'/>"></script>
<%
boolean debug = false; 
if(debug){ %>
<link href='<s:url value="/portal/phone/resource/css/ratchet.min.css"/>' rel="stylesheet">
<link href='<s:url value="/portal/phone/resource/css/weui.min.css"/>' rel="stylesheet">
<link href='<s:url value="/portal/phone/resource/css/swiper.min.css"/>' rel="stylesheet">
<link href='<s:url value="/portal/phone/resource/css/font-awesome.min.css"/>' rel="stylesheet" />
<link href='<s:url value="/portal/phone/resource/css/global.css"/>' rel="stylesheet">
<link href='<s:url value="/portal/phone/resource/css/animate.css"/>' rel="stylesheet">
<link href='<s:url value="/portal/phone/resource/css/css.css"/>' rel="stylesheet">
<link href='<s:url value="/portal/phone/resource/css/launch.css"/>' rel="stylesheet" />
<link href='<s:url value="/portal/phone/resource/css/other.css"/>' rel="stylesheet" />

<script type="text/javascript" src='<s:url value="/portal/phone/resource/js/ratchet.min.js"/>'></script>
<script type="text/javascript" src='<s:url value="/portal/phone/resource/js/jquery.swiper-3.3.1.min.js"/>'></script>
<script type="text/javascript" src='<s:url value="/portal/phone/resource/js/homepage.js"/>'></script>
<script type="text/javascript" src='<s:url value="/portal/phone/resource/js/menu.js"/>'></script>
<script type="text/javascript" src='<s:url value="/portal/phone/resource/js/flowCenter.js"/>'></script>
<script type="text/javascript" src='<s:url value="/portal/phone/resource/js/common.js"/>'></script>
<%
}else{
%>
<%@include file="./resource/common/include_main_all_css.jsp"%>
<%@include file="./resource/common/include_main_all_js.jsp"%>
<%} %>


<link href='<s:url value="/fonts/custom/widget_icon_lib.css"/>' rel="stylesheet" />
</head>
<body >
<input id='applicationId' type='hidden' name='applicationId' value='<%=request.getParameter("application")%>'/>
<div class="container js_container">
    <div class="page phone-main">
	    <div class="bd" style="height: 100%;">
			<div id="homePage" class="phone-main-panel"></div>
			<div id="flowCenter" class="phone-main-panel" style="display:none;"></div>
			<div id="menu" class="phone-main-panel" style="display: none;"></div>
	    </div>
	</div>
	<div class="phone-main-nav-panel">
        <section class="phone-main-nav-trigger">
			<i class="iconfont if-phone-plus"></i>
		</section> 
		<section class="phone-main-nav-close" style="display:none">
			<i class="iconfont if-phone-close"></i>
		</section> 
		<div class="phone-main-nav-modal">
			<nav>
				<ul class="phone-main-nav">
					<li title="home"><span>主页</span><i class="iconfont if-phone-home home"></i></li>
					<li title="flowCenter"><span>流程</span><i class="iconfont if-phone-workflow flowCenter"></i></li>
					<li title="menu"><span>菜单</span><i class="iconfont if-phone-sender menu"></i></li>
				</ul>
			</nav>
		</div>
    </div>
</div>
</body>
<script>
var contextPath = '<%=request.getContextPath()%>';
var serviceTime = '<%=_date%>';
var is_bouncy_nav_animating = false;

$( document ).on( "mobileinit", function() {
	$.mobile.ignoreContentEnabled = true;
});

function loadFlow(){
	FlowCenter.multilingual = {
			subject : "{*[Subject]*}",
			State : "{*[State]*}",
			Current_Processor : "{*[Current_Processor]*}",
			flow_last_time : "{*[flow.last_time]*}",
			Activity : "{*[Activity]*}"
	};
	$.get("./flowCenter.jsp?application="+$("#applicationId").val(),function(text){
		$("#flowCenter").html(text);
		FlowCenter.init();
	});
}

$(function(){	
	//加载首页
	loadHome();

	//加载流程中心
	setTimeout(function(){
		loadFlow();
	},15);

	//加载菜单
	setTimeout(function(){
		loadMenu();
	},30);
	
	$(".phone-main-nav-panel .phone-main-nav li").on("touchend",function(event){
		if($("#flowCenter").is(":visible")){
			$(".weui_navbar_item[_for='startFlow']").trigger("touchend");
		}
		var title = $(this).attr("title");
		triggerBouncyNav(false);
		window.location.hash = "#"+title;
		event.preventDefault();
	});

	window.onhashchange=function(){
		changeMenu();
	};
	
	var action = '<s:property value="#parameters.action" />';
	//首页home 流程flowCenter 菜单menu
	if(action.length>0 && action!="null" && !window.location.hash){
		mainAction(action);
	}else{
		changeMenu();
	}
	
	
	function changeMenu(target){
		var hash = target? "#"+target : window.location.hash;
		var title; 
		if (hash && hash.length > 1) {
			title = hash.substring(1);
		}
		else {
			title = "home";
		}
		var $tab = $("#navUl>li[title='"+title+"']");
		$("div.mainContent,#headTitle").css("display","none");
		
		$(".phone-main").find(".phone-main-panel").hide();
		
		$(".bar-web a.control-item").removeClass("active");
		$("#navUl a[title=home]").find(".iconfont").removeClass("iconfont-home-active").addClass("iconfont-home");
		$("#navUl a[title=menu]").find(".iconfont").removeClass("iconfont-menu-active").addClass("iconfont-menu");
		$(".control-item-label").removeClass("bar-active");
		mainAction(title)
		window.location.hash = "#"+title;
	}
	
	function mainAction(action){
		switch (action) {
			case "home":	
				$(".container .iconMore").addClass('slideOut');
				setTimeout(function(){
					$(".container .iconMore").remove();
				},300);
				$(".phone-main-panel").hide();
				$("#homePage").show();
				$(document).attr("title","<%=appName%>");
				break;
	
			case "flowCenter":
				$(".phone-main-panel").hide();
				$("#flowCenter").show();
				$("fieldset>label[_for=startFlow]").addClass("bar-active");
				$("fieldset>#startFlowRadio").trigger("click");
				$(document).attr("title","流程中心");
				break;
	
			case "menu":
				$(".phone-main-panel").hide();
				$("#menu").show();
				//$("#flowCenter").hide();
				$(document).attr("title","菜单");
				//window.location.hash = "#menu";
				break;
				
			default:
				break;
		}
	}
});
</script>
</html>
</o:MultiLanguage>