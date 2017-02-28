package cn.myapps.km.kmap.dao;

import java.sql.Connection;
import java.sql.SQLException;


public class MySqlKMapDAO extends AbstractKMapDAO implements KMapDAO {

	public MySqlKMapDAO(Connection conn) throws Exception {
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
