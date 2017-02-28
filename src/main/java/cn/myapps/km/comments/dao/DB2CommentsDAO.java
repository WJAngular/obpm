package cn.myapps.km.comments.dao;

import java.sql.Connection;
import java.sql.SQLException;


public class DB2CommentsDAO extends AbstractCommentsDAO implements CommentsDAO {

	public DB2CommentsDAO(Connection conn) throws Exception {
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
