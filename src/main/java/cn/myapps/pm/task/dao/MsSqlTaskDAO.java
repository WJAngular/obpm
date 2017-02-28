package cn.myapps.pm.task.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class MsSqlTaskDAO extends AbstractTaskDAO implements TaskDAO{
	
	public MsSqlTaskDAO(Connection conn) throws Exception {
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
	protected String buildLimitString(String sql, int page, int lines, String orderbyField, String orderbyMode) throws SQLException {

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

	/**
	 * 生成限制条件sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @param taskOrderConditionSql
	 *            从AbstractTaskDAO中获取排序条件
	 *            
	 * @return 生成限制条件sql语句字符串
	 * @throws SQLException
	 */
	@Override
	public String buildLimitString(String sql, int page, int lines,
			LinkedHashMap<String, String> taskOrderConditionMap) throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}
		// int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		int databaseVersion = connection.getMetaData()
				.getDatabaseMajorVersion();
		if (9 <= databaseVersion) {// 2005 row_number() over () 分页
			pagingSelect.append("SELECT TOP " + lines + " * FROM (");
			pagingSelect.append("SELECT ROW_NUMBER() OVER ( ORDER BY  ");
			//添加排序条件
			Iterator<Map.Entry<String, String>> iter = taskOrderConditionMap.entrySet().iterator();
        	String orderbyFile ;
        	String orderbyMode ;
        	while(iter.hasNext()){
        		Entry<String, String> entry = iter.next();
        		orderbyFile = entry.getKey();
        		orderbyMode = entry.getValue();
        		pagingSelect.append(" "+orderbyFile+" "+ orderbyMode+" ,");
        	}
        	pagingSelect = new StringBuffer(pagingSelect.substring(0, pagingSelect.length()-1));
        	pagingSelect.append(" ) AS ROWNUMBER, TABNIC.* FROM (");
        	pagingSelect.append(sql);
        	pagingSelect.append(" ) TABNIC) TableNickname ");
        	pagingSelect.append(" WHERE ROWNUMBER>" + lines * (page - 1));

		} else { //2005版本以上
			pagingSelect.append("SELECT TOP " + lines * page + " * FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC");
			//添加排序条件
			pagingSelect.append("ORDER BY ");
			Iterator<Map.Entry<String, String>> iter = taskOrderConditionMap.entrySet().iterator();
        	String orderbyFile ;
        	String orderbyMode ;
        	while(iter.hasNext()){
        		Entry<String, String> entry = iter.next();
        		orderbyFile = entry.getKey();
        		orderbyMode = entry.getValue();
        		pagingSelect.append(" "+orderbyFile+" "+ orderbyMode+" ,");
        	}
        	pagingSelect = new StringBuffer(pagingSelect.substring(0, pagingSelect.length()-1));
		}

		return pagingSelect.toString();
	}

}
