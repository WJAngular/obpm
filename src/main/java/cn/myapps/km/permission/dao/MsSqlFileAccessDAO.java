package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MsSqlFileAccessDAO extends AbstractFileAccessDAO implements FileAccessDAO{

	public MsSqlFileAccessDAO(Connection conn) throws Exception {
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
