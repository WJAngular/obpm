package cn.myapps.core.datamap.runtime.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class MssqlDataMapTemplateDAO extends AbstractDataMapTemplateDAO
		implements DataMapTemplateDAO {

	public MssqlDataMapTemplateDAO(Connection connection) {
		super(connection);
		dbType = "MS SQL Server: ";
		this.schema = DbTypeUtil.getSchema(connection, DbTypeUtil.DBTYPE_MSSQL);
	}

}
