package cn.myapps.core.counter.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class OracleCounterDAO extends AbstractCounterDAO implements CounterDAO {
	
	public OracleCounterDAO(Connection conn) throws Exception {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
	}

}
