<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Map"%>
<%@page import="cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess"%>
<%@page import="cn.myapps.util.ProcessFactory"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="cn.myapps.extendedReport.Charts"%>
<%
	Charts chart = new Charts();
	/*  String chartType = request.getParameter("chartType");
	String _name = request.getParameter("_name"); */
 
    JSONArray json2 = chart.getDatas();
	out.println(json2);
%>
