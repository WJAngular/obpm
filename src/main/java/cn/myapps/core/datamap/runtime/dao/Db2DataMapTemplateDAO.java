package cn.myapps.core.datamap.runtime.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class Db2DataMapTemplateDAO extends AbstractDataMapTemplateDAO implements
		DataMapTemplateDAO {

	public Db2DataMapTemplateDAO(Connection connection) {
		super(connection);
		dbType = "DB2: ";
		this.schema = DbTypeUtil.getSchema(connection, DbTypeUtil.DBTYPE_DB2);
	}

}
