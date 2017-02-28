package cn.myapps.core.datamap.definition.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.datamap.definition.ejb.DataMapCfgVO;

public class HibernateDataMapCfgDAO extends HibernateBaseDAO<DataMapCfgVO> implements DataMapCfgDAO{
	
	public HibernateDataMapCfgDAO(String valueObjectName) {
		super(valueObjectName);
	}

}
