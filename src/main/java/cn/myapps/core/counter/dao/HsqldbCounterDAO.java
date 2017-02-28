package cn.myapps.core.counter.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;
/**
 * 
 * @author Chris
 *
 */
public class HsqldbCounterDAO extends AbstractCounterDAO implements CounterDAO {

	public HsqldbCounterDAO(Connection conn) throws Exception {
		super(conn);
		dbType="HypersonicSQL: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_HSQLDB);
	}

	
}
