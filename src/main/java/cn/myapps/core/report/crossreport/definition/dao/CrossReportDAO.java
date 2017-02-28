package cn.myapps.core.report.crossreport.definition.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;

public interface CrossReportDAO extends IDesignTimeDAO<CrossReportVO>{

	public Collection<CrossReportVO> getCrossReportByModule(String application, String module)
		throws Exception;
	
	/**
	 * 获取软件下的自定义报表集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public Collection<CrossReportVO> getCustomizeReportsByApplication(String applicationId) throws Exception;
	
	public DataPackage<CrossReportVO> queryCrossReportByModule(String moduleId,ParamsTable params,int page,int pagelines) throws Exception;
}
