package cn.myapps.core.workflow.storage.runtime.intervention.dao;

import java.sql.Connection;

import cn.myapps.util.DbTypeUtil;

public class OracleFlowInterventionDAO extends AbstractFlowInterventionDAO implements FlowInterventionDAO {

	public OracleFlowInterventionDAO(Connection connection) {
		super(connection);
		this.schema = DbTypeUtil
				.getSchema(connection, DbTypeUtil.DBTYPE_ORACLE);
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

		pagingSelect
				.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);

		return pagingSelect.toString();
	}
	
	/**
	 * 拼装时间格式  用于处理不同数据库
	 * @param time
	 * 		默认时间格式是  yyyy-mm-dd
	 * @return
	 */
	public String assemblyTime(String time) {
		return "to_date('"+time+"','yyyy-mm-dd')";
	}
}
