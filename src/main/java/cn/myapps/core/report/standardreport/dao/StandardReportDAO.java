package cn.myapps.core.report.standardreport.dao;

import java.util.Collection;
import java.util.Map;

import cn.myapps.core.report.basereport.dao.ReportDAO;

public interface StandardReportDAO  extends ReportDAO{

	public Collection<Map<String, String>> getSummaryReport(String sql) throws Exception;

	public String getSql(String formId, String startdate, String enddate,
			String[] columnName, String application, String dbmethod) throws Exception;
	
	public String getCountSql(String formId, String startdate, String enddate,
			String[] columnName, String application, String dbmethod) throws Exception;

	public int getReportRowsNum(String sql) throws Exception;
}
