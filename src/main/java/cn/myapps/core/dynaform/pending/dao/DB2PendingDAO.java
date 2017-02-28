package cn.myapps.core.dynaform.pending.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class DB2PendingDAO extends AbstractPendingDAO implements PendingDAO {

	public DB2PendingDAO(Connection connection) {
		super(connection);
		dbType = "DB2: ";
		this.schema = DbTypeUtil.getSchema(connection, DbTypeUtil.DBTYPE_DB2);

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
	 */
	public String buildLimitString(String sql, int page, int lines) {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("Select * from (select row_.*, rownumber() over(");
		pagingSelect.append(" ) AS rown from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS row_) AS rows_ where rows_.rown BETWEEN ");
		pagingSelect.append(from);
		pagingSelect.append(" AND ");
		pagingSelect.append(to);
		if(pagingSelect.toString().toUpperCase().indexOf("WITH UR")==-1){
			 pagingSelect.append(" WITH UR");
		}
		return pagingSelect.toString();
	}
}
