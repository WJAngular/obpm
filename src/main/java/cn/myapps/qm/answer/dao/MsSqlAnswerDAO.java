package cn.myapps.qm.answer.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.myapps.util.StringUtil;

public class MsSqlAnswerDAO  extends AbstractAnswerDAO implements AnswerDAO {
	
	public MsSqlAnswerDAO(Connection conn) throws Exception {
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
	public String buildLimitString(String sql, int page, int lines ,String orderbyField,String orderbyMode) throws SQLException {

		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		// int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		int databaseVersion = connection.getMetaData()
				.getDatabaseMajorVersion();
		
		if (9 <= databaseVersion) {// 2005 row_number() over () 分页
			pagingSelect.append("SELECT TOP " + lines + " * FROM (");
			pagingSelect
					.append("SELECT ROW_NUMBER() OVER (ORDER BY ").append(orderbyField).append(" ").append(orderbyMode).append(") AS ROWNUMBER, TABNIC.* FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC) TableNickname ");
			pagingSelect.append("WHERE ROWNUMBER>" + lines * (page - 1));

		} else {
			pagingSelect.append("SELECT TOP " + lines * page + " * FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC");
		}

		return pagingSelect.toString();
	}
	
	protected String bulidOrderString(String sql,String field){
		if (StringUtil.isBlank(sql)) {
			return sql;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT TOP 100 PERCENT * FROM (");
		buffer.append(sql);
		buffer.append(") tb_orderby ORDER BY ");
		buffer.append(field);
		buffer.append(" DESC");
		return buffer.toString();
	}
	
}
