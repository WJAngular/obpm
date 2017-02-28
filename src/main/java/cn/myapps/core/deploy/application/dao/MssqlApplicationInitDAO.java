package cn.myapps.core.deploy.application.dao;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.mssql.MssqlTableDefinition;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.DbTypeUtil;

public class MssqlApplicationInitDAO extends AbstractApplicationInitDAO {

	public MssqlApplicationInitDAO(Connection conn) throws Exception {
		super(conn);
		this.dbType = "MS SQL Server: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
		this.definition = new MssqlTableDefinition(conn);
	}

	protected Table getDBTable(String tableName) {
		return (Table) DbTypeUtil.getTable(tableName, DbTypeUtil.DBTYPE_MSSQL, this.connection);
	}
}
