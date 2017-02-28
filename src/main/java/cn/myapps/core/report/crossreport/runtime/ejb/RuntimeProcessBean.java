package cn.myapps.core.report.crossreport.runtime.ejb;

import java.sql.Connection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.util.RuntimeDaoManager;

public class RuntimeProcessBean extends AbstractRunTimeProcessBean<Object> implements RuntimeProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313728753180088046L;

	public RuntimeProcessBean(String applicationId) {
		super(applicationId);
	}

	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getCrossReportDAO(getConnection(),getApplicationId()); 
	}

	public Connection getRuntimeConn()throws Exception
	{
		return this.getConnection();
	}
}
