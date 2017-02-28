package cn.myapps.core.deploy.application.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import cn.myapps.core.table.ddlutil.oracle.OracleTableDefinition;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.DbTypeUtil;

public class OracleApplicationInitDAO extends AbstractApplicationInitDAO {
	Logger log = Logger.getLogger(OracleApplicationInitDAO.class);

	// private Connection connection;

	public OracleApplicationInitDAO(Connection conn) throws Exception {
		super(conn);
		this.dbType = "ORACLE: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
		this.definition = new OracleTableDefinition(conn);
	}

	protected Table getDBTable(String tableName) {
		return (Table) DbTypeUtil.getTable(tableName, DbTypeUtil.DBTYPE_ORACLE, this.connection);
	}
}
