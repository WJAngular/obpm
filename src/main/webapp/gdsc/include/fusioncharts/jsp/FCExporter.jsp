<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%@page import="com.fusioncharts.exporter.FusionChartsExportHelper"%>
<%@page import="com.fusioncharts.exporter.beans.ExportBean"%>
<%@page import="com.fusioncharts.exporter.ErrorHandler"%>
<%
	StringBuffer err_warn_Codes = new StringBuffer();
	String WEB_ROOT_PATH = application.getRealPath("/");
	String pathSeparator = File.separator;
	String validation_def_filepath = WEB_ROOT_PATH + pathSeparator + FusionChartsExportHelper.RESOURCEPATH + "validation_def.jsp";
	String relativePathToValidationDef = FusionChartsExportHelper.RESOURCEPATH + "validation_def.jsp";
	
	try{
		File f = new File(validation_def_filepath);
		if(f.exists()){
%>
<jsp:include page="<%=relativePathToValidationDef%>"></jsp:include>
<%			
		}
	}catch(Exception e){
		System.out.println(e.getMessage());
	}
	
	ExportBean localExportBean = FusionChartsExportHelper.parseExportRequestStream(request);
	
	String exportFormat = (String)localExportBean.getExportParameterValue("exportformat");
	String exporterFilePath = FusionChartsExportHelper.getExporterFilePath(exportFormat);
	
	if(request.getParameter("legend") != null && request.getParameter("legend").equals("true")){
		exporterFilePath = exporterFilePath.replace("FCExporter_IMG","FCExporter_IMG_LEGEND");
		System.out.println("here is : "+exporterFilePath);
	}
	
	String exportTargetWindow = (String)localExportBean.getExportParameterValue("exporttargetwindow");
	if(localExportBean.getMetadata().getWidth() == -1 || localExportBean.getMetadata().getHeight() == -1 || localExportBean.getMetadata().getWidth() == 0 || localExportBean.getMetadata().getHeight() == 0){
		err_warn_Codes.append("E101,");
	}
	
	if(localExportBean.getMetadata().getBgColor() == null){
		err_warn_Codes.append("W513,");
	}
	
	if(localExportBean.getStream() == null){
		err_warn_Codes.append("E100,");
	}
	
	String exportAction = (String)localExportBean.getExportParameterValue("exportaction");
	boolean isHTML = false;
	if(exportAction.equals("download")){
		String fileNameWithoutExt = (String)localExportBean.getExportParameterValue("exportfilename");
		String extension = FusionChartsExportHelper.getExtensionFor(exportFormat.toLowerCase());
		String fileName = fileNameWithoutExt + "." + extension;
		
		err_warn_Codes.append(ErrorHandler.checkServerSaveStatus(WEB_ROOT_PATH,fileName));
	}
	
	if(err_warn_Codes.indexOf("E") != -1){
		String meta_values = localExportBean.getMetadataAsQueryString(null,true,isHTML);
%>
		<jsp:forward page="FCExportError.jsp">
			<jsp:param name="erroeMessage" value="<%=err_warn_Codes.toString()%>"/>
			<jsp:param name="otherMessages" value="<%=meta_values%>"/>
			<jsp:param name="exportTargetWindow" value="<%=exportTargetWindow%>"/>
			<jsp:param name="isHTML" value="<%=isHTML%>"/>
		</jsp:forward>
<%
		System.out.println("error 1");
		return;
	}
%>

<jsp:useBean id="exportBean" scope="request" class="com.fusioncharts.exporter.beans.ExportBean">
	<jsp:setProperty name="exportBean" property="metadata" value="<%=localExportBean.getMetadata()%>"/> 
	<jsp:setProperty name="exportBean" property="stream" value="<%=localExportBean.getStream()%>"/>
	<jsp:setProperty name="exportBean" property="exportParameters" value="<%=localExportBean.getExportParameters()%>"/>
</jsp:useBean>

<%
	//System.out.println("aaaa:"+WEB_ROOT_PATH+pathSeparator+exporterFilePath);
	File exporter = new File(WEB_ROOT_PATH+pathSeparator+exporterFilePath);
	if(exporter.exists()){
%>
	<jsp:forward page="<%=exporterFilePath%>"></jsp:forward>
<%		
	}else{
		System.out.println("error 2");
	}
%>