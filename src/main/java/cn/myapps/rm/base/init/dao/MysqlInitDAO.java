package cn.myapps.rm.base.init.dao;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.mysql.MysqlTableDefinition;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.DbTypeUtil;

public class MysqlInitDAO extends AbstractInitDAO {

	public MysqlInitDAO(Connection conn) throws Exception {
		super(conn);
		this.dbType = "MYSQL: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MYSQL);
		this.definition = new MysqlTableDefinition(conn);
	}

	protected Table getDBTable(String tableName) {
		return (Table) DbTypeUtil.getTable(tableName, DbTypeUtil.DBTYPE_MYSQL, this.connection);
	}

}
