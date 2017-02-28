package cn.myapps.core.workflow.storage.runtime.proxy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.util.DbTypeUtil;

public class OracleWorkflowProxyDAO extends AbstractWorkflowProxyDAO implements WorkflowProxyDAO {

	public OracleWorkflowProxyDAO(Connection connection) {
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
	 * 判断用户是否代理流程
	 * @param userId
	 * 		当前用户id
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isFlowAgent(String userId) throws Exception {
		StringBuffer pagingSelect = new StringBuffer(100);
		pagingSelect.append("select row_.*, rownum rownum_ from ");
		pagingSelect.append(getFullTableName("T_FLOW_PROXY"));
		pagingSelect.append(" row_ where row_.agents like '%"+userId+"%' and rownum <= ");
		pagingSelect.append(1);
		
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(pagingSelect.toString());
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
