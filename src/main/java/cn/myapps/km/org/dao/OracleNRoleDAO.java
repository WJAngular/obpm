package cn.myapps.km.org.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleNRoleDAO extends AbstractNRoleDAO implements NRoleDAO {

	public OracleNRoleDAO(Connection conn) throws Exception {
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
