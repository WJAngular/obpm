package cn.myapps.km.comments.dao;

import java.sql.Connection;
import java.sql.SQLException;


public class MySqlCommentsDAO extends AbstractCommentsDAO implements CommentsDAO {

	public MySqlCommentsDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MY SQL: ";
		if (conn != null) {
			try {
				this.schema = conn.getCatalog();
//				this.schema = this.schema.substr?ing(this.schema
//							.lastIndexOf("/") + 1);
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	

}
