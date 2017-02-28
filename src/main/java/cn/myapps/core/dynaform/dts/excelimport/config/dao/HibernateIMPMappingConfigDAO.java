package cn.myapps.core.dynaform.dts.excelimport.config.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;

public class HibernateIMPMappingConfigDAO extends HibernateBaseDAO<IMPMappingConfigVO> implements IMPMappingConfigDAO {
	public HibernateIMPMappingConfigDAO(String voClassName) {
		super(voClassName);
	}

	public Collection<IMPMappingConfigVO> getAllMappingConfig(String application) throws Exception {
		String hql = "from " + _voClazzName;
		ParamsTable params = new ParamsTable();
		params.setParameter("application", application);
		return getDatas(hql, params);
	}

	public Object getMappingExcel(String id) throws Exception {
		String hql = "from " + _voClazzName;
		hql += " where ID='" + id + "'";
		return getData(hql);
	}

}
