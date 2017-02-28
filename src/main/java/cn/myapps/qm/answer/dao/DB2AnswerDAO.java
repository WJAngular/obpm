package cn.myapps.qm.answer.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DB2AnswerDAO extends AbstractAnswerDAO implements AnswerDAO {

	public DB2AnswerDAO(Connection conn) {
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

	@Override
	protected String buildLimitString(String sql, int page, int lines, String orderbyField, String orderbyMode)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			if (sql.toUpperCase().indexOf("WITH UR") > 0) {
				return sql;
			}
			return sql + " WITH UR";
		}
		// Modify by James:2010-01-03, fixed page divide error.
		// int from = (page - 1) * lines;
		int from = (page - 1) * lines + 1;

		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("Select * from (select row_.*, rownumber() over(");
		// if (orderby != null && !orderby.trim().equals(""))
		// pagingSelect.append(orderby);
		pagingSelect.append(" ) AS rown from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS row_) AS rows_ where rows_.rown BETWEEN ");
		pagingSelect.append(from);
		pagingSelect.append(" AND ");
		pagingSelect.append(to);
		if (pagingSelect.toString().toUpperCase().indexOf("WITH UR") == -1) {
			pagingSelect.append(" WITH UR");
		}
		return pagingSelect.toString();
	}

}
