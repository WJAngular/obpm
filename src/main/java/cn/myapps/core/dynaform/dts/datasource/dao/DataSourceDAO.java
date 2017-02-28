package cn.myapps.core.dynaform.dts.datasource.dao;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;

public interface DataSourceDAO extends IDesignTimeDAO<DataSource> {

	public DataSource getDataSource(String dataSouceName, String application)
			throws Exception;
}
