package cn.myapps.km.category.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MsSqlCategoryDAO extends AbstractCategoryDAO implements
		CategoryDAO {

	public MsSqlCategoryDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MS SQL SERVER: ";
		try {
			ResultSet rs = conn.getMetaData().getSchemas();
			if (rs != null) {
				if (rs.next())
					this.schema = rs.getString(1).trim().toUpperCase();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
