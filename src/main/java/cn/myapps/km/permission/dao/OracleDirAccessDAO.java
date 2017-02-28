package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleDirAccessDAO extends AbstractDirAccessDAO implements DirAccessDAO {

	public OracleDirAccessDAO(Connection conn) throws Exception {
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
