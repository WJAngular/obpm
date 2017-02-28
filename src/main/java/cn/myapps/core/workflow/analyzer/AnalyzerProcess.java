package cn.myapps.core.workflow.analyzer;

import java.util.Collection;
import java.util.HashMap;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface AnalyzerProcess extends IRunTimeProcess<FlowAnalyzerVO>{
	public Collection<HashMap> getConsuming(ParamsTable params,String dateRangeType,String showMode,
			WebUser user) throws Exception;
	public Collection<HashMap> getNode(ParamsTable params,String dateRangeType,String showMode,
			WebUser user) throws Exception;
	public Collection<HashMap> getNames(ParamsTable params,String showMode,
			WebUser user) throws Exception;
	public Collection<HashMap> getDetailed(ParamsTable params,String showMode,
			WebUser user) throws Exception;
}
