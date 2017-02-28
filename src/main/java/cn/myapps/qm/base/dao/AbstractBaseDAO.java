package cn.myapps.qm.base.dao;

import java.sql.Connection;

public abstract class AbstractBaseDAO {
	
	protected String dbTag = "MYSQL: ";

	protected String schema = "";
	
	protected String tableName = "";
	
	protected Connection connection;
	
	public AbstractBaseDAO(Connection conn){
		this.connection = conn;
	}
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}

}
