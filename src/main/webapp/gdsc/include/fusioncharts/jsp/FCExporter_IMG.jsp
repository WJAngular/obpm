<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.fusioncharts.exporter.FusionChartsExportHelper"%>
<%@page import="com.fusioncharts.exporter.beans.ChartMetadata"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="com.fusioncharts.exporter.generators.ImageGenerator"%>
<%@page import="java.io.File"%>
<%@page import="com.fusioncharts.exporter.ErrorHandler"%>
<%@page import="javax.imageio.stream.FileImageOutputStream"%>
<%@page import="com.fusioncharts.exporter.encoders.JPEGEncoder"%>
<%@page import="com.fusioncharts.exporter.encoders.BasicEncoder"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="exportBean" scope="request" class="com.fusioncharts.exporter.beans.ExportBean"/>
<%
	String action = (String)exportBean.getExportParameterValue("exportaction");
	String exportFormat = (String)exportBean.getExportParameterValue("exportformat");
	String exportTargetWindow = (String)exportBean.getExportParameterValue("exporttargetwindow");
	
	String fileNameWithoutExt = (String)exportBean.getExportParameterValue("exportfilename");
	String extension = FusionChartsExportHelper.getExtensionFor(exportFormat.toLowerCase());
	String strFormatTime = "";
	if(fileNameWithoutExt != null && !"".equals(fileNameWithoutExt)){
		String[] names = fileNameWithoutExt.split("--");
		if(names.length > 1){
			SimpleDateFormat sdf = new SimpleDateFormat(names[1]);
			Date currentTime = new Date();
			strFormatTime = "-" + sdf.format(currentTime);
			fileNameWithoutExt = names[0];
		}
	}
	
	String fileName = fileNameWithoutExt + strFormatTime + "." + extension;
	String stream = exportBean.getStream();
	ChartMetadata metadata = exportBean.getMetadata();
	
	boolean isHTML = false;
	if(action.equals("download")){
		isHTML = true;
	}
		
	StringBuffer err_warn_Codes = new StringBuffer();
	BufferedImage chartImage = ImageGenerator.getChartImage(stream,metadata);
	String noticeMessage = "";
	String meta_values = exportBean.getMetadataAsQueryString(null,false,isHTML);
	if(!action.equals("download")){
		noticeMessage = "&notice=";
		String pathToWebAppRoot = getServletContext().getRealPath("/");
		String pathToSaveFolder = pathToWebAppRoot + File.separator + FusionChartsExportHelper.SAVEPATH;
		
		File saveFolder = new File(pathToSaveFolder);
		
		String completeFilePath = pathToSaveFolder + File.separator + fileName;
		String completeFilePathWithoutExt = pathToSaveFolder + File.separator + fileNameWithoutExt;
		File saveFile = new File(completeFilePath);
		if(saveFile.exists()){
			noticeMessage += ErrorHandler.getErrorForCode("W509");
			if(!FusionChartsExportHelper.OVERWRITEFILE){
				if(FusionChartsExportHelper.INTELLIGENTFILENAMING){
					noticeMessage += ErrorHandler.getErrorForCode("W514");
					completeFilePath = FusionChartsExportHelper.getUniqueFileName(completeFilePathWithoutExt,extension);
					File tempFile = new File(completeFilePath);
					fileName = tempFile.getName();
					noticeMessage += ErrorHandler.getErrorForCode("W515") + fileName;
					err_warn_Codes.append("W515,");
				}
			}
		}
		String pathToDisplay = FusionChartsExportHelper.HTTP_URI + "/" + fileName;
		if(FusionChartsExportHelper.HTTP_URI.endsWith("/")){
			pathToDisplay = FusionChartsExportHelper.HTTP_URI + fileName;
		}
		
		meta_values = exportBean.getMetadataAsQueryString(pathToDisplay,false,isHTML);
		
		FileImageOutputStream fios = new FileImageOutputStream(new File(completeFilePath));
		if(exportFormat.toLowerCase().equals("jpg") || exportFormat.toLowerCase().equals("jpeg")){
			JPEGEncoder jpegEncoder = new JPEGEncoder();
			try{
				jpegEncoder.encode(chartImage,fios);
			}catch(Throwable e){
				err_warn_Codes.append("E516,");
			}
			chartImage = null;
		}else{
			BasicEncoder basicEncoder = new BasicEncoder();
			try{
				basicEncoder.encode(chartImage,fios,1F,exportFormat.toLowerCase());
			}catch(Throwable e){
				System.out.println("Unable to encode the buffered image.");
				err_warn_Codes.append("E516,");
			}
			chartImage = null;
		}
		
		if(err_warn_Codes.indexOf("E") > 0){
			out.print(meta_values + noticeMessage + "&statusCode=1&statusMessage=successful");
		}
		
	}else{
		response.setContentType(FusionChartsExportHelper.getMimeTypeFor(exportFormat.toLowerCase()));
		
		OutputStream os = response.getOutputStream();
		if(exportTargetWindow.equalsIgnoreCase("_self")){
			response.addHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");
		}else{
			response.addHeader("Content-Disposition","inline;filename=\""+fileName+"\"");
		}
		
		if(exportFormat.toLowerCase().equalsIgnoreCase("jpg") || exportFormat.toLowerCase().equalsIgnoreCase("jpeg")){
			JPEGEncoder jpegEncoder = new JPEGEncoder();
			try{
				jpegEncoder.encode(chartImage,os);
				os.flush();
			}catch(Throwable e){
				System.out.println("Unable to (JPEG) encode the buffered image.");
				err_warn_Codes.append("E516,");
				meta_values = exportBean.getMetadataAsQueryString(null,true,isHTML);
				response.reset();
				response.setContentType("text/html");
				out.print(meta_values + noticeMessage + ErrorHandler.buildResponse(err_warn_Codes.toString(),isHTML));
				return;
			}
			chartImage = null;
		}else{
			BasicEncoder basicEncoder = new BasicEncoder();
			try{
				basicEncoder.encode(chartImage,os,1F,exportFormat.toLowerCase());
				os.flush();
			}catch(Throwable e){
				System.out.println("Unable to encode the buffered image.");
				err_warn_Codes.append("E516,");
				meta_values = exportBean.getMetadataAsQueryString(null,true,isHTML);
				response.reset();
				response.setContentType("text/html");
				out.print(meta_values + noticeMessage + ErrorHandler.buildResponse(err_warn_Codes.toString(),isHTML));
				return;
			}
			chartImage = null;
		}
	}
	
	if(err_warn_Codes.indexOf("E") != -1){
		meta_values = exportBean.getMetadataAsQueryString(null,true,isHTML);
%>
<jsp:forward page="FCExporterError.jsp">
	<jsp:param name="errorMessage" value="<%=err_warn_Codes.toString()%>" />
	<jsp:param name="otherMessages" value="<%=meta_values%>" />
	<jsp:param name="exportTargetWindow" value="<%=exportTargetWindow%>" />
	<jsp:param name="isHTML" value="<%=isHTML%>" />
</jsp:forward>
<%
	return;
	}
	
%>

