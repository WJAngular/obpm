package cn.myapps.km.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.util.PersistenceUtils;


public class MySqlNUserRoleSetDAO extends AbstractNUserRoleSetDAO implements NUserRoleSetDAO {

	public MySqlNUserRoleSetDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MY SQL: ";
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getURL().trim().toUpperCase();
				if (this.schema.indexOf("?USE") > 0) {
					this.schema = this.schema.substring(this.schema
							.lastIndexOf("/") + 1, this.schema.indexOf("?USE"));
				} else {
					this.schema = this.schema.substring(this.schema
							.lastIndexOf("/") + 1);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
   
}
