package cn.myapps.km.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.myapps.km.util.PersistenceUtils;

public class OracleLogsDAO extends AbstractLogsDAO implements LogsDAO {

	public OracleLogsDAO(Connection conn) throws Exception {
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
	public String buildLimitString(String sql, int page, int lines)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("select *  FROM  ( select row_.*, rownum rownum_  FROM  ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" order by OPERATIONDATE desc");
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		
		//if (orderby != null && !orderby.trim().equals(""))
			//pagingSelect.append(orderby);

		return pagingSelect.toString();
	}
	
	public long countBySQL(String sql, String filename,String operationtype,String userid) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ") T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			if((filename==null||filename=="")&&(operationtype==null||operationtype=="")){
				statement.setString(1, userid);
			}else if(filename==null||filename==""){
				statement.setString(1, operationtype);
				statement.setString(2, userid);
			}else{
			statement.setString(1, filename);
			statement.setString(2, operationtype);
			statement.setString(3, userid);
			}
			rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}
	
	public long countBy(String sql) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ")  T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}
}
