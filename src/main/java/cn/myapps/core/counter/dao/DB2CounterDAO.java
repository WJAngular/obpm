package cn.myapps.core.counter.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class DB2CounterDAO extends AbstractCounterDAO implements CounterDAO {

	public DB2CounterDAO(Connection conn) throws Exception {
		super(conn);
		dbType = "DB2: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_DB2);
	}

}
