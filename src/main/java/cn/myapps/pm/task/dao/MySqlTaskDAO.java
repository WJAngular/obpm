package cn.myapps.pm.task.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ibm.db2.jcc.uw.classloader.e;

import edu.emory.mathcs.backport.java.util.TreeMap;

public class MySqlTaskDAO extends AbstractTaskDAO implements TaskDAO{

	public MySqlTaskDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MY SQL: ";
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getURL().trim().toUpperCase();
				if (this.schema.indexOf("?USE") > 0) {
					this.schema = this.schema.substring(this.schema
							.lastIndexOf("/") + 1, this.schema.indexOf("?USE"));
				} else {
					this.schema = this.schema.substring(this.schema
							.lastIndexOf("/") + 1);
				}
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
	public String buildLimitString(String sql, int page, int lines, String orderbyFile, String orderbyMode)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);
		
		pagingSelect.append("SELECT * FROM (");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS TB ORDER BY TB." + orderbyFile + " " + orderbyMode +" LIMIT " + to + "," + lines);

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

		int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);
		
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
    	pagingSelect.append(" LIMIT " + to + "," + lines);
		return pagingSelect.toString();
	}


}
