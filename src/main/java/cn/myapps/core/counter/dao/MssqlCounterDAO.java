package cn.myapps.core.counter.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;
/**
 * 
 * @author Chris
 *
 */
public class MssqlCounterDAO extends AbstractCounterDAO implements CounterDAO {

	public MssqlCounterDAO(Connection conn) throws Exception {
		super(conn);
		dbType="MS SQL Server: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
	}

	
}
