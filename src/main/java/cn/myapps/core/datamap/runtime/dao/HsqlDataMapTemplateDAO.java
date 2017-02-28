package cn.myapps.core.datamap.runtime.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class HsqlDataMapTemplateDAO extends AbstractDataMapTemplateDAO
		implements DataMapTemplateDAO {

	public HsqlDataMapTemplateDAO(Connection connection) {
		super(connection);
		dbType = "HypersonicSQL: ";
		this.schema = DbTypeUtil
				.getSchema(connection, DbTypeUtil.DBTYPE_HSQLDB);
	}

}
