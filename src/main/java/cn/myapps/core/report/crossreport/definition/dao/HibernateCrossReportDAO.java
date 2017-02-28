package cn.myapps.core.report.crossreport.definition.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;

public class HibernateCrossReportDAO  extends HibernateBaseDAO<CrossReportVO> implements CrossReportDAO{
	
	public HibernateCrossReportDAO(String voClassName) {
		super(voClassName);
	}

	public Collection<CrossReportVO> getCrossReportByModule(String application,
			String module) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.module='"
		+ module + "'";
		ParamsTable params = new ParamsTable();
		params.setParameter("application", application);
		return getDatas(hql, params);
	}
	
	/**
	 * 获取软件下的自定义报表集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public Collection<CrossReportVO> getCustomizeReportsByApplication(String applicationId) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.type='CustomizeReport' and vo.applicationid='"
		+ applicationId + "'";
		return getDatas(hql);
	}

	public DataPackage<CrossReportVO> queryCrossReportByModule(String moduleId,
			ParamsTable params,int page,int pagelines) throws Exception {
		String hql = "FROM " + _voClazzName + " vo WHERE vo.type='CrossReport' and vo.module='"
		+ moduleId + "'";
		return getDatapackage(hql, params, page, pagelines);
	}


}
