package cn.myapps.core.deploy.application.dao;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.hsql.HsqldbTableDefinition;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.DbTypeUtil;

public class HsqldbApplicationInitDAO extends AbstractApplicationInitDAO {

	public HsqldbApplicationInitDAO(Connection conn) throws Exception {
		super(conn);
		this.dbType = "HypersonicSQL: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_HSQLDB);
		this.definition = new HsqldbTableDefinition(conn);
	}

	protected Table getDBTable(String tableName) {
		return (Table) DbTypeUtil.getTable(tableName, DbTypeUtil.DBTYPE_HSQLDB, this.connection);
	}

}
