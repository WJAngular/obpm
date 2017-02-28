package cn.myapps.core.workflow.analyzer;

import java.util.Collection;
import java.util.HashMap;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.user.action.WebUser;

public interface AnalyzerDAO extends IRuntimeDAO {
	public Collection<HashMap> queryConsuming(ParamsTable params,String dateRangeType,String showMode,
			WebUser user) throws Exception;
	public Collection<HashMap> queryNode(ParamsTable params,String dateRangeType,String showMode,
			WebUser user) throws Exception;
	public Collection<HashMap> queryNames(ParamsTable params,String showMode,
			WebUser user) throws Exception;
	public Collection<HashMap> queryDetailed(ParamsTable params,String showMode,
			WebUser user) throws Exception;
}
