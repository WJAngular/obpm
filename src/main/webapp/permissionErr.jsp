<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<o:MultiLanguage>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Permission Error</title>
<link rel="stylesheet"
		href="<o:Url value='/resource/css/main-front.css'/>" type="text/css" />
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center"><table width="419" height="226" border="0" cellpadding="0" cellspacing="0"  style="margin-top: 0px;">
      <tr>
        <td align="center" background="<s:url value="/portal/default/resource/imgv2/error.jpg"/>"><table width="388" height="194" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" style="color: red; font-size: 14;"><img src="<s:url value="/portal/default/resource/imgv2/error_b.gif"/>" width="31" height="31">&nbsp;&nbsp;ERROR:You do not have permission to access this page!
              <br />
              <br />
              <a href="#" onClick="history.back(-1)">{*[Back]*}</a></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<center>
</center>
</body>
</o:MultiLanguage>
</html>