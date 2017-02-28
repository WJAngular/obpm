package cn.myapps.core.workflow.analyzer;

import java.sql.Connection;
import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.DbTypeUtil;

public class HsqldbAnalyzerDAO extends AbstractAnalyzerDAO{
	public HsqldbAnalyzerDAO(Connection conn) throws Exception {
		super(conn);
		this.DBType = DbTypeUtil.DBTYPE_HSQLDB;
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_HSQLDB);
	}

	@Override
	public Collection<FlowAnalyzerVO> analyzerActorTimeConsumingTopX(
			ParamsTable params, String dateRangeType, int top, String showMode, WebUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<FlowAnalyzerVO> analyzerFlowTimeConsumingAccounting(
			ParamsTable params, String dateRangeType, String showMode, WebUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<FlowAnalyzerVO> analyzerFlowAndNodeTimeConsuming(
			ParamsTable params, String dateRangeType, String showMode, WebUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String buildQueryConsumingSql(ParamsTable params,
			String dateRangeType, String showMode, WebUser user)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String buildQueryNodeSql(ParamsTable params, String dateRangeType,
			String showMode, WebUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String buildQueryNamesSql(ParamsTable params, String showMode,
			WebUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String buildQueryDetailedSql(ParamsTable params, String showMode,
			WebUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
