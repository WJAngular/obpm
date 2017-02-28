package cn.myapps.attendance.base.schema.dao;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.db2.DB2TableDefinition;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.DbTypeUtil;

public class DB2SchemaDAO extends AbstractSchemaDAO {

	public DB2SchemaDAO(Connection conn) throws Exception {
		super(conn);
		this.dbType = "DB2: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_DB2);
		this.definition = new DB2TableDefinition(conn);
	}

	protected Table getDBTable(String tableName) {
		return (Table) DbTypeUtil.getTable(tableName, DbTypeUtil.DBTYPE_DB2, this.connection);
	}

}
