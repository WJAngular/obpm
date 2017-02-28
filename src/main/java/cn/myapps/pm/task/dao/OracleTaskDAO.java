package cn.myapps.pm.task.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class OracleTaskDAO extends AbstractTaskDAO implements TaskDAO {

	public OracleTaskDAO(Connection conn) throws Exception {
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
			return sql;
		}

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect
				.append("select *  FROM  ( select row_.*, rownum rownum_  FROM  ( ");
		pagingSelect.append(sql);
		
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		// if (orderby != null && !orderby.trim().equals(""))
		// pagingSelect.append(orderby);

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

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect
				.append("select *  FROM  ( select row_.*, rownum rownum_  FROM  ( ");
		pagingSelect.append(sql);
		//添加排序条件
        pagingSelect.append(" ORDER BY ");
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
    	pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		// if (orderby != null && !orderby.trim().equals(""))
		// pagingSelect.append(orderby);

		return pagingSelect.toString();
	}

}
