package cn.myapps.core.dynaform.dts.datasource.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;

public class HibernateDataSourceDAO extends HibernateBaseDAO<DataSource> implements DataSourceDAO {

	public HibernateDataSourceDAO(String voClassName) {
		super(voClassName);
	}

	public DataSource getDataSource(String dataSouceName, String application)
			throws Exception {
		String hql = "FROM " + _voClazzName + " vo where vo.applicationid='"
				+ application + "' ";
		hql += " AND vo.name='" + dataSouceName + "'";
		return (DataSource) getData(hql);
	}
}
