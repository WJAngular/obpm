package cn.myapps.core.report.crossreport.runtime.dao;

import java.sql.Connection;

import cn.myapps.core.report.basereport.dao.AbstractReportDAO;

public class AbstractRuntimeDAO extends AbstractReportDAO{

	public AbstractRuntimeDAO(Connection conn) throws Exception {
		super(conn);
	}

}
