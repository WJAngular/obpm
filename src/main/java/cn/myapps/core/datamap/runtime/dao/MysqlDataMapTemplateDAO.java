package cn.myapps.core.datamap.runtime.dao;

import java.sql.Connection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.util.DbTypeUtil;

public class MysqlDataMapTemplateDAO extends AbstractDataMapTemplateDAO
		implements DataMapTemplateDAO {

	public MysqlDataMapTemplateDAO(Connection connection) {
		super(connection);
		dbType = "MY SQL: ";
		this.schema = DbTypeUtil.getSchema(connection, DbTypeUtil.DBTYPE_MYSQL);
	}

}
