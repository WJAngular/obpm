package cn.myapps.core.workflow.storage.runtime.proxy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.util.DbTypeUtil;

public class HsqldbWorkflowProxyDAO extends AbstractWorkflowProxyDAO implements WorkflowProxyDAO {

	public HsqldbWorkflowProxyDAO(Connection connection) {
		super(connection);
		dbType = "HypersonicSQL: ";
		this.schema = DbTypeUtil
				.getSchema(connection, DbTypeUtil.DBTYPE_HSQLDB);
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

		int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);
		int ind = sql.indexOf("ORDER BY");
		String orderby = "";
		if (ind > 0) {
			orderby = sql.substring(ind);
			sql = sql.substring(0, ind);
		}
		pagingSelect.append("SELECT LIMIT " + to + " " + lines + " * FROM (");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS TTTB " + orderby);

		return pagingSelect.toString();
	}
	

	/**
	 * 判断用户是否代理流程
	 * @param userId
	 * 		当前用户id
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isFlowAgent(String userId) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT LIMIT 0 1 * FROM " + getFullTableName("T_FLOW_PROXY")
					+ " T WHERE T.AGENTS LIKE '%"+userId+"%'";
			
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
		return false;
	}
}
