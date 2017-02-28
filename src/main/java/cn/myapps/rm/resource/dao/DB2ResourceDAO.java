package cn.myapps.rm.resource.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DB2ResourceDAO extends AbstractResourceDAO implements ResourceDAO {

	public DB2ResourceDAO(Connection conn) throws Exception {
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

	
	/**
	 * 生成限制条件sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @return 生成限制条件sql语句字符串
	 * @throws SQLException
	 */
	@Override
	protected String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException {
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
