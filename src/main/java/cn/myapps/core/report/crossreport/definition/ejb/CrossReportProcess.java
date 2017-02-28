package cn.myapps.core.report.crossreport.definition.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;

public interface CrossReportProcess  extends IDesignTimeProcess<CrossReportVO>{

	public String getAllCrossReportVO(String applicationid,String moduleid,String flag,String userid) throws Exception;
	
	public String getCrossReportVO(String id) throws Exception;
	
	/**
	 * 获取模块下的CrossReportVO
	 * 
	 * @param application
	 *            应用标识
	 * @param module
	 *            模块标识
	 * @return CrossReportVO的集合(java.util.Collection)
	 * @throws Exception
	 */
	public Collection<CrossReportVO> getCrossReportByModule(String application, String module) throws Exception;
	
	/**
	 * 删除多个报表对象
	 * 
	 * @param crossreportList
	 *            报表对象集合
	 * @throws Exception
	 */
	public abstract void doRemove(Collection<CrossReportVO> crossreportList)
			throws Exception;
	
	/**
	 * 获取软件下的自定义报表集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public Collection<CrossReportVO> getCustomizeReportsByApplication(String applicationId) throws Exception;
	
	/**
	 * 查询模块下的交叉报表
	 * @param moduleId
	 * @param params
	 * @param page
	 * @param pagelines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<CrossReportVO> doQueryCrossReportByModule(String moduleId,ParamsTable params,int page,int pagelines) throws Exception;
}
