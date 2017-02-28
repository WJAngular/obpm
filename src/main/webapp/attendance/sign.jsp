<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="true" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta content="application/xhtml+xml" charset="UTF-8" http-equiv="Content-Type">
    <meta content="telephone=no, address=no" name="format-detection">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
	<title></title>
	<link rel="stylesheet" type="text/css" href="css/sign.css">
</head>
<div data-role="container" class="body signIn  faild position">
    <header data-role="header" id="shake">
        <div >
        </div>
    </header>
    <section data-role="body" style="min-height: 623px;">
        <div class="box2" id="signSuccess" style="display:none;">
            <div class="content success">
                <span style="color: #06be04;"><span class="sign_text"></span>成功</span>
                <div><span class="sign_text"></span>时间：<span class="time"></span></div>
                 <div><span class="sign_status" style="color:#ed2d2d;"></span></span></div>
            </div>
            <div class="mooddiv" style="text-align: center;display: none;" >
                    <div class="des">今天工作了<span id="workingTime"></span>小时哦！</div>
             </div>
            <div class="btndiv">
                <a class="btn" href="javascript:SIGN.close();">知道了</a>
            </div>
        </div>
        <div class="box2" id="signFail" style="display: none;">
            <div class="content fail">
                <span><span class="sign_text"></span>失败</span>
                <label class="tip"></label>
            </div>
            <div class="btndiv">
                <a class="btn" href="javascript:SIGN.close();">知道了</a>
            </div>
        </div>
        <div class="box2" id="signFail_position" style="display: none;">
            <div id="showtips"></div>
            <div class="content fail">
                <span><span class="sign_text"></span>失败</span>
                <label>请确保考勤地理位置已开启，稍后<span class="sign_text"></span>！ </label>
            </div>
            <div class="btndiv blue">
                <a class="btn" onclick="SIGN.close()">知道了</a>
            </div>
        </div>
        <div class="box2" id="signing">
        	<div class="show_map">
        		<div id="show_map" style="width: 100%"></div>
        	</div>
            <div class="mooddiv" style="text-align: center;" >
                 <div class="des">当前地点：<span id="current_location_text"></span></div>
                 <div class="des">“摇一摇”可以重新定位哦！</div>
                 <div class="des" style="color: red">
					<script language=Javascript> 
					
					var nows= '<%=new SimpleDateFormat("HH:mm").format(new Date())%>';
					document.write("当前时间："+nows) 
					</script> 
				</div>
             </div>
            <div class="btndiv">
                <a class="btn" href="javascript:SIGN.doAction(action,SIGN.cache.position);">确定<span class="sign_text"></span></a>
            </div>
        </div>
    </section>
    <footer data-role="footer"><div class="copyright">©思程技术支持</div>
    </footer>
</div>
<audio id="musicBox" controls="true" src="" style="display: none;"/>
</body>
	<script type="text/javascript" src="js/lib/jquery.min.js"></script>
	<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=4b13e87bef9c0298377377db89487985&s=1"></script>
	<script type="text/javascript" src="js/attendance.sign.js?v=8"></script>
	<script type="text/javascript">
	var action = '<s:property value="#parameters.action" />';
	var _host ='<s:property value="#session.FRONT_USER.getDomain().getServerHost()" />';
	function onBridgeReady(){
		 WeixinJSBridge.call('hideOptionMenu');
		}

		if (typeof WeixinJSBridge == "undefined"){
		    if( document.addEventListener ){
		        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
		    }else if (document.attachEvent){
		        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
		        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
		    }
		}else{
		    onBridgeReady();
		}
		
		var USER = {
				id:'<s:property value="#session.FRONT_USER.getId()" />',
				name:'<s:property value="#session.FRONT_USER.getName()" />',
				domainId:'<s:property value="#session.FRONT_USER.getDomainid()" />'
				};

		$(document).ready(function(){
			$.ajaxSetup({
                error:function(x,e){
				//Utils.showMessage("请求服务端发生异常,请稍后尝试！","error");
                    return false;
                }
            });	
			var media = document.getElementById("musicBox");//获取音频控件 
	     	var href = window.location.href;
	     	_host = href.substring(0,href.indexOf("/attendance/sign.jsp"));
	     	media.setAttribute("src", _host+"/attendance/media/yaoyiyao.mp3");
			SIGN.init();
		});
		
		
	</script>
</html>