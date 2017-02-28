package cn.myapps.rm.base.init.dao;

import java.sql.Connection;

import cn.myapps.core.table.ddlutil.oracle.OracleTableDefinition;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.DbTypeUtil;

public class OraclelInitDAO extends AbstractInitDAO {

	public OraclelInitDAO(Connection conn) throws Exception {
		super(conn);
		this.dbType = "ORACLE: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
		this.definition = new OracleTableDefinition(conn);
	}

	protected Table getDBTable(String tableName) {
		return (Table) DbTypeUtil.getTable(tableName, DbTypeUtil.DBTYPE_ORACLE, this.connection);
	}

}
