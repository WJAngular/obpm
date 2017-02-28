package cn.myapps.core.datamap.runtime.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class OracleDataMapTemplateDAO extends AbstractDataMapTemplateDAO
		implements DataMapTemplateDAO {

	public OracleDataMapTemplateDAO(Connection connection) {
		super(connection);
		this.schema = DbTypeUtil
		.getSchema(connection, DbTypeUtil.DBTYPE_ORACLE);
	}

}
