<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src='<s:url value="/dwr/interface/ApplicationHelper.js"/>'></script>
<title>Insert title here</title>
</head>
<script type="text/javascript">
function ev_dbtypechange(type) {
	var dbdriver = document.getElementById("driver");
	var dburl = document.getElementById("url");
	
	if (type=="ORACLE"){
		dbdriver.value = "oracle.jdbc.driver.OracleDriver";
		dburl.value = "jdbc:oracle:thin:@<server>:1521:<database_name>";
	}
	else if (type=="MSSQL"){
		dbdriver.value = "net.sourceforge.jtds.jdbc.Driver";
		dburl.value = "jdbc:jtds:sqlserver://<hostname>:1433/<dbname>";
	}
	else if (type=="DB2"){
		dbdriver.value = "com.ibm.db2.jcc.DB2Driver";
		dburl.value = "jdbc:db2://<hostname>:50000/<dbname>";
	}
	else if (type=="MYSQL"){
		dbdriver.value = "com.mysql.jdbc.Driver";
		dburl.value = "jdbc:mysql://<hostname>:3307/<dbname>?useUnicode=true&characterEncoding=utf8";
	}
	else if (type=="HSQLDB"){
		dbdriver.value = "org.hsqldb.jdbcDriver";
		dburl.value = "jdbc:hsqldb:hsql://<hostname>:9001/<dbname>";
	}
}
function IsDigit(){
	return ((event.keyCode>=48)&&(event.keyCode<=57));
}
function testDB() {
	var dbdriver = document.getElementsByName("kmConfig.driverClass")[0].value;
	var dburl = document.getElementsByName("kmConfig.url")[0].value;
	var dbusername = document.getElementsByName("kmConfig.username")[0].value;
	var dbpassword = document.getElementsByName("kmConfig.password")[0].value;
	
	ApplicationHelper.testDB(dbusername,dbpassword,dbdriver,dburl, function(msg){
		alert(msg);
	});
	
}

function disabledJudgment(){
	if(jQuery("#kmConfig").attr("checked") != "checked"){
		jQuery("#kmRoles").attr("disabled", true); 
		var elements = jQuery("#datasource *");
		makeFieldAbleOrNot(elements, true);
	}else if(jQuery("#kmConfig").attr("checked") == "checked"){
		jQuery("#kmRoles").attr("disabled", false); 
		var elements = jQuery("#datasource *");
		makeFieldAbleOrNot(elements, false);
	}
	jQuery("#kmConfig").click(function(){
		disabledJudgment();
	});
}

function makeFieldAbleOrNot(elements, able) {
	if (elements) {
		for (var i = 0; i < elements.length; i++) {
			var element = elements[i];
			if (element.disabled == !able) {
				element.disabled = able;
			}
		}
	}
}

function configureRoles(){
	var url = contextPath + '/core/sysconfig/configure.action';
	OBPM.dialog.show({
			opener:window.parent,
			width: 1000,
			height: 520,
			url: url,
			args: {},
			title: '{*[cn.myapps.core.sysconfig.km.role_manage]*}',
			close: function(rtn) {
			}
	});
}

jQuery(document).ready(function(){
	disabledJudgment();
});

</script>
<body>
<fieldset >
<legend>{*[cn.myapps.core.sysconfig.km.legendlabel_enable]*}</legend>
<table>
	<tr>
		<td style="display: none;">
		<s:if test="@cn.myapps.km.util.Config@previewEnabled()">
			<span style="color:blue;">已开启预览功能</span>
		</s:if>
		<s:else><span style="color:red;">未开启预览功能</span></s:else>
		</td>
		<td><s:checkbox id="kmConfig" name="kmConfig.enable" style="display:none;" theme="simple"></s:checkbox></td>
		<s:if test="kmConfig.enable">
		<td><input id="kmRoles" style="margin-left: 80px;" type="button" value="{*[cn.myapps.core.sysconfig.km.role_manage]*}" onclick="configureRoles()" /></td>
		</s:if>
	</tr>
</table>
</fieldset>
<fieldset id="datasource">
<legend>{*[cn.myapps.core.sysconfig.km.legendlabel_datasource]*}</legend>
			<table class="id1" width="100%">
			<tr>
				<td class="commFont">{*[DB_Type]*}:</td>
				<td></td>
			</tr>
			<tr>		
				<td><s:select name="kmConfig.dbType" cssClass="input-cmd"
					theme="simple"  list="_dblist" onchange="ev_dbtypechange(this.value)"></s:select>
				</td>
				<td></td>
			</tr>
			<tr class="seperate">
				<td class="commFont">{*[DriverClass]*}:</td>
				<td class="commFont">{*[URL]*}:</td>
			</tr>
			<tr>
				<td><s:textfield cssClass="input-cmd" theme="simple" name="kmConfig.driverClass" size="50" id="driver" /></td>
				<td><s:textfield cssClass="input-cmd" theme="simple" name="kmConfig.url" size="50" id="url" /></td>
			</tr>
		
			<tr class="seperate">
				<td class="commFont">{*[User_Name]*}:</td>
				<td class="commFont">{*[Password]*}:</td>
			</tr>
			<tr>		
				<td><s:textfield cssClass="input-cmd" theme="simple"
					name="kmConfig.username" size="50" /></td>
				<td><s:textfield cssClass="input-cmd" theme="simple"
					name="kmConfig.password" size="50" /></td>
			</tr>
			<tr class="seperate">
					<td class="commFont">{*[DB_PoolSize]*}:</td>
					<td class="commFont">{*[DB_Timeout]*}:</td>
			</tr>
			<tr>
				<td align="left"><s:textfield  cssClass="input-cmd" theme="simple" 
					name="kmConfig.poolsize" onkeypress="event.returnValue=IsDigit();"/>
				</td>
				<td align="left" ><s:textfield  cssClass="input-cmd" theme="simple" 
					name="kmConfig.timeout" onkeypress="event.returnValue=IsDigit();"/>
				</td>
			</tr>
			<tr>
				<td align="left">
				</td>
				<td align="left" >
					<input class="kmConfigButton" style="margin-left: 200px;" type="button" value="{*[cn.myapps.core.sysconfig.km.test_link]*}" onclick="testDB()" />
				</td>
			</tr>
			</table>
</fieldset>
</body>
</html>