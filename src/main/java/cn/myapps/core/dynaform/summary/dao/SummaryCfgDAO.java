package cn.myapps.core.dynaform.summary.dao;


import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;

/**
 * @author Happy
 *
 */
public interface SummaryCfgDAO extends IDesignTimeDAO<SummaryCfgVO> {
	
	public Collection<SummaryCfgVO> queryByFormId(String formId) throws Exception;
	
	public SummaryCfgVO findByFormIdAndScope(String formId, int scope) throws Exception;
	public DataPackage<SummaryCfgVO> queryHomePageSummaryCfgs(ParamsTable params) throws Exception;
	public boolean isExistWithSameTitle(String title,String applicationId) throws Exception;
}
