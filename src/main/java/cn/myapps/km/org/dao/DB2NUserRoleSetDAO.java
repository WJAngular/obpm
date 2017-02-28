package cn.myapps.km.org.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DB2NUserRoleSetDAO extends AbstractNUserRoleSetDAO implements NUserRoleSetDAO {

	public DB2NUserRoleSetDAO(Connection conn) throws Exception {
		super(conn);
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getUserName().trim()
						.toUpperCase();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

}
