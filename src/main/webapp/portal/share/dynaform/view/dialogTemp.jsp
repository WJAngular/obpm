<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/portal/share/common/head.jsp"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%
	String contextPath = request.getContextPath();
	// 组装queryString，WebSphere不支持getQueryString
	String queryString = "";
	Map parameterMap = request.getParameterMap();
	String[] temp = (String [])parameterMap.get("selectOne");
	String[] mutil = (String []) parameterMap.get("mutil");
	System.out.println(mutil);
	for(Iterator it = parameterMap.entrySet().iterator(); it.hasNext();) {
		Map.Entry entry = (Map.Entry)it.next();
		String[] values = (String[])entry.getValue();
		queryString += entry.getKey() + "="+values[0]+"&";
	}
	
	if (!parameterMap.isEmpty()) {
		queryString = queryString.substring(0, queryString.length() - 1);
	}

%>
<html><o:MultiLanguage value="FRONTMULTILANGUAGETAG">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<style type="text/css">
@-moz-keyframes three-quarters-loader {
  0% {
    -moz-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -moz-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@-webkit-keyframes three-quarters-loader {
  0% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes three-quarters-loader {
  0% {
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
/* :not(:required) hides this rule from IE9 and below */
.three-quarters-loader:not(:required) {
  -moz-animation: three-quarters-loader 1250ms infinite linear;
  -webkit-animation: three-quarters-loader 1250ms infinite linear;
  animation: three-quarters-loader 1250ms infinite linear;
  border: 8px solid #38e;
  border-right-color: transparent;
  border-radius: 16px;
  box-sizing: border-box;
  display: inline-block;
  position: relative;
  overflow: hidden;
  text-indent: -9999px;
  width: 32px;
  height: 32px;
}

</style>
<script>
function ev_onload2(){
   temp.submit();
}
//window.attachEvent("onload", ev_onload2);
</script>
</head>
<body onload='ev_onload2()' align="center" style="overflow: hidden"> 
<div id="tips">
  <table width=100% height=100% align=center cellpadding=0 cellspacing=0 border=0 >
    <tr>
      <td align="center">
		<div class="cell">
	      <div class="card">
	        <span class="three-quarters-loader">Loading&#8230;</span>
	      </div>
	    </div>
	</td>
    </tr>
  </table>
</div>
<%if(mutil[0].equals("true")){ %>
	<form method='post' name='temp' action='<%= contextPath %>/portal/dynaform/view/mainDialogView.action?<%=queryString%>'>
<%} else{%>
	<form method='post' name='temp' action='<%= contextPath %>/portal/dynaform/view/dialogView.action?<%=queryString%>'>
<%} %>
<div id="strHeddin" style="display:none; height:100%">
<script>
	var args = OBPM.dialog.getArgs();
	document.write(args?args['html']:"");
</script>
</div>
</form>
</body>
</o:MultiLanguage></html>