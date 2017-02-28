<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:bean name="cn.myapps.core.security.action.LoginHelper" id="lh" />
<!DOCTYPE html>
<html>
<head>
<title>登陆</title>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link href="./resource/css/ratchet.min.css" rel="stylesheet">
<link href="./resource/css/global.css" rel="stylesheet">
<script src="./resource/js/jquery-1.11.3.min.js"></script>
<script src="./resource/js/ratchet.min.js"></script>
<script type="text/javascript">
function getBrowserInfo()
{
var agent = navigator.userAgent.toLowerCase() ;

var regStr_ie = /msie [\d.]+;/gi ;
var regStr_ff = /firefox\/[\d.]+/gi
var regStr_chrome = /chrome\/[\d.]+/gi ;
var regStr_saf = /safari\/[\d.]+/gi ;
//IE
if(agent.indexOf("msie") > 0)
{
return agent.match(regStr_ie) ;
}

//firefox
if(agent.indexOf("firefox") > 0)
{
return agent.match(regStr_ff) ;
}

//Chrome
if(agent.indexOf("chrome") > 0)
{
return agent.match(regStr_chrome) ;
}

//Safari
if(agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0)
{
return agent.match(regStr_saf) ;
}

}

$(function(){
	var browser = window.navigator.userAgent.toLowerCase();
	var verinfo = (browser+"").replace(/[^0-9.]/ig,""); 
});
</script>
<style type="text/css">
        body,html{
            background: url("./resource/images/login-bg.jpg");
            background-size: cover; 
            background-attachment: fixed; 
        }
    </style>
<script type="text/javascript">
function CheckForm()
{
   return true;
}

</script>
</head>
<body onload="javascript:document.form1.username.focus();" scroll="auto" class="login">


    <div class="box">
<div class="logo"><img src="../share/images/logo/logo-phone.png" /></div>
<div class="login-title">天翎办公系统</div>
<div class="login-content">


<s:form name="form1" method="post" action="/portal/login/login.action" autocomplete="off" onsubmit="return CheckForm();" theme="simple" role="form">
<input type="hidden" name="domainName"  value="<s:property value='#lh.getDomainNameList().get(0)'/>">

  <div class="form-group">
    <i class="icon iconfont">&#xe600;</i><input type="text" name="username" onmouseover="this.focus()" onfocus="this.select()" value="" class="form-control username" />
  </div>
  <div class="form-group">




    <i class="icon iconfont">&#xe601;</i><input type="password" class="form-control password" name="password" onmouseover="this.focus()" onfocus="this.select()" value="" />
  </div>
 <!--  <div class="form-group">
    <i class="vcode-icon">码</i><img src="./resource/images/t005.png"><input type="text" class="form-control vcode" >
  </div> -->

  <button type="submit" class="btn btn-login">登 陆</button>
<!--   <div class="checkbox text-center">
  <label>
    <input type="checkbox" value="">
    记住密码
  </label>
</div>
 -->
</s:form>
</div>
</div>
<div class="copyright">Copyright © 2013 TEEMLINK<br/>广州市天翎网络科技有限公司 版权所有</div>

</body>
</html>