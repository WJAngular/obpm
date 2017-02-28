package cn.myapps.core.dynaform.dts.exp.mappingconfig.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;

public class HibernateMappingConfigDAO extends HibernateBaseDAO<MappingConfig> implements MappingConfigDAO{

	public HibernateMappingConfigDAO(String voClassName) {
		super(voClassName);
	}

	public Collection<MappingConfig> getMCByTableName(String tableName, String application) throws Exception {
	  String hql = "from  " + this._voClazzName +" where tablename = '"+tableName+"'" ;
	   ParamsTable params=new ParamsTable();
	   params.setParameter("application",application);
		return this.getDatas(hql, params);
	}
}
