package com.teemlink.saas.weioa.suite.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;

import com.teemlink.saas.weioa.base.action.BaseAction;
import com.teemlink.saas.weioa.suite.ejb.SuiteProcessBean;

public class SuiteAction extends BaseAction {
	
	private boolean install = false;
	
	
	
	public boolean getInstall() {
		
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement stmt = null;

			String sql = "select * from saas_suite_auth where domain_id=?";

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, getUser().getDomainid());

				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					install = true;
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(conn !=null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
		return install;
	}

	public void setInstall(boolean install) {
		this.install = install;
	}
	
	

	public String doList(){
		
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String domainId = params.getParameterAsString("domain");
			list = new SuiteProcessBean().getMySuiteApps(params, user.getDomainid());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return SUCCESS;
	}
	
	/**
	 * 微信套件应用列表
	 * @return
	 */
	public String doListWeixinSuite(){
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String domainId = params.getParameterAsString("domain");
			list = new SuiteProcessBean().getAllSuiteApps(params, user.getDomainid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	

}
