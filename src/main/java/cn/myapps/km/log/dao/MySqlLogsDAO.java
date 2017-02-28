package cn.myapps.km.log.dao;

import java.sql.Connection;
import java.sql.SQLException;


public class MySqlLogsDAO extends AbstractLogsDAO implements LogsDAO {

	public MySqlLogsDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MY SQL: ";
		if (conn != null) {
			try {
				this.schema = conn.getCatalog();
//				this.schema = conn.getMetaData().getURL().trim().toUpperCase();
//				this.schema = this.schema.substring(this.schema
//							.lastIndexOf("/") + 1);
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	

}
