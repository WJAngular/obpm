package cn.myapps.rm.base.dao;

import java.sql.Connection;

public class AbstractBaseDAO {
	
	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractBaseDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

}
