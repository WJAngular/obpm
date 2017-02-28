package cn.myapps.pm.task.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DB2TaskDAO extends AbstractTaskDAO implements TaskDAO {

	public DB2TaskDAO(Connection conn) throws Exception {
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
			if (sql.toUpperCase().indexOf("WITH UR") > 0) {
				return sql;
			}
			return sql + " WITH UR";
		}
		int from = (page - 1) * lines + 1;

		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("Select * from (select row_.*, rownumber() over( ORDER BY ");
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
    	pagingSelect.append( " ) AS rown from ( ");
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
