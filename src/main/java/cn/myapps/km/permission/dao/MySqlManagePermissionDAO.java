package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.SQLException;


public class MySqlManagePermissionDAO extends AbstractManagePermissionDAO implements ManagePermissionDAO{

	public MySqlManagePermissionDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MY SQL: ";
		if (conn != null) {
			try {
				this.schema = conn.getCatalog();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
   
}
