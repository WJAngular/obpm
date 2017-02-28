package com.teemlink.saas.weioa.suite.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;

public interface SuiteProcess {
	
	public Collection<Suite> getMySuiteApps(ParamsTable params,String domainId) throws Exception;

	public Collection<Suite> getAllSuiteApps(ParamsTable params,String domainId) throws Exception;
	
	
}
