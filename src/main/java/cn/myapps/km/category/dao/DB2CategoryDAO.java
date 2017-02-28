package cn.myapps.km.category.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DB2CategoryDAO extends AbstractCategoryDAO implements CategoryDAO {

	public DB2CategoryDAO(Connection conn) throws Exception {
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
