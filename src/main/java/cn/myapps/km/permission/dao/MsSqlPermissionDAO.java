package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MsSqlPermissionDAO extends AbstractPermissionDAO implements
		PermissionDAO {

	public MsSqlPermissionDAO(Connection conn) throws Exception {
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
