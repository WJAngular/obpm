package cn.myapps.core.dynaform.summary.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;

/**
 * @author Happy
 *
 */
public interface SummaryCfgProcess extends
		IDesignTimeProcess<SummaryCfgVO> {
	
	public Collection<SummaryCfgVO> doQueryByFormId(String formId) throws Exception;
	
	public SummaryCfgVO doViewByFormIdAndScope(String formId,int scope) throws Exception;
	
	public DataPackage<SummaryCfgVO> doQueryHomePageSummaryCfgs(ParamsTable params) throws Exception;
	
	/**
	 * 在给定的软件ID判断下是否存在同名摘要
	 * @param title
	 * @param applicationId
	 * @return
	 * @throws Exception
	 */
	public boolean isExistWithSameTitle(String title,String applicationId) throws Exception;

	/**
	 * 批量删除SummaryCfgVO
	 * @param summarycfgList
	 * @throws Exception
	 */
	public abstract void doRemove(Collection<SummaryCfgVO> summarycfgList)throws Exception;
}
