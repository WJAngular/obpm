package cn.myapps.km.comments.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleCommentsDAO extends AbstractCommentsDAO implements CommentsDAO {

	public OracleCommentsDAO(Connection conn) throws Exception {
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
