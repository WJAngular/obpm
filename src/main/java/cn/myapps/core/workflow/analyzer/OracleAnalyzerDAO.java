package cn.myapps.core.workflow.analyzer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.DbTypeUtil;

public class OracleAnalyzerDAO extends AbstractAnalyzerDAO{
	public OracleAnalyzerDAO(Connection conn) throws Exception {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
	}

	@Override
	public Collection<FlowAnalyzerVO> analyzerActorTimeConsumingTopX(
			ParamsTable params, String dateRangeType, int top, String showMode, WebUser user) throws Exception {
		String extCondition = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND AUDITOR='" + user.getId() + "'";

		String sql = "SELECT FLOWID,FLOWNAME,STARTNODENAME,ENDNODENAME,AUDITOR,DOCID,APPLICATIONID,(TO_DATE(TO_CHAR(PROCESSTIME,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') - TO_DATE(TO_CHAR(ACTIONTIME,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')) * 24 * 60 AS AMOUNT FROM "
				+ getFullTableName("T_RELATIONHIS")
				+ " WHERE ACTIONTIME >= ? AND ACTIONTIME <?"
				+ extCondition
				+ " ORDER BY  AMOUNT DESC ";

		sql = buildLimitString(sql, 1, top);
		Collection<FlowAnalyzerVO> rtn = new ArrayList<FlowAnalyzerVO>();
		PreparedStatement statement = null;

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);

			statement.setTimestamp(1, getDateRangeBegin(dateRangeType));
			statement.setTimestamp(2, getDateRangeEnd(dateRangeType));
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				FlowAnalyzerVO fa = new FlowAnalyzerVO();
				setProperties(rs, fa);
				rtn.add(fa);
			}

			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	@Override
	public Collection<FlowAnalyzerVO> analyzerFlowTimeConsumingAccounting(
			ParamsTable params, String dateRangeType, String showMode, WebUser user) throws Exception {

		String extCondition = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND AUDITOR='" + user.getId() + "'";
		String sql = "SELECT FLOWID,FLOWNAME,APPLICATIONID,SUM((TO_DATE(TO_CHAR(PROCESSTIME,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') - TO_DATE(TO_CHAR(ACTIONTIME,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')) * 24 * 60) AMOUNT FROM "
				+ getFullTableName("T_RELATIONHIS")
				+ " WHERE ACTIONTIME >= ? AND ACTIONTIME <?"
				+ extCondition
				+ " GROUP BY FLOWID,FLOWNAME,APPLICATIONID";

		Collection<FlowAnalyzerVO> rtn = new ArrayList<FlowAnalyzerVO>();
		PreparedStatement statement = null;

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);

			statement.setTimestamp(1, getDateRangeBegin(dateRangeType));
			statement.setTimestamp(2, getDateRangeEnd(dateRangeType));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				FlowAnalyzerVO vo = new FlowAnalyzerVO();
				// 流程ID
				vo.addGroupColumn("FLOWID", rs.getString("FLOWID"));
				// 流程名称
				vo.addGroupColumn("FLOWNAME", rs.getString("FLOWNAME"));
				// 统计值
				vo.addResultField("AMOUNT", rs.getDouble("AMOUNT"));
				vo.setApplicationid(rs.getString("APPLICATIONID"));
				rtn.add(vo);
			}

			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	@Override
	public Collection<FlowAnalyzerVO> analyzerFlowAndNodeTimeConsuming(
			ParamsTable params, String dateRangeType, String showMode, WebUser user) throws Exception {

		String extCondition = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND AUDITOR='" + user.getId() + "'";

		String sql = "SELECT FLOWID,FLOWNAME,STARTNODENAME,APPLICATIONID,SUM((TO_DATE(TO_CHAR(PROCESSTIME,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') - TO_DATE(TO_CHAR(ACTIONTIME,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')) * 24 * 60) AMOUNT FROM "
				+ getFullTableName("T_RELATIONHIS")
				+ " WHERE ACTIONTIME >= ? AND ACTIONTIME <?"
				+ extCondition
				+ " GROUP BY FLOWID,FLOWNAME,APPLICATIONID,STARTNODENAME";

		Collection<FlowAnalyzerVO> rtn = new ArrayList<FlowAnalyzerVO>();
		PreparedStatement statement = null;

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);

			statement.setTimestamp(1, getDateRangeBegin(dateRangeType));
			statement.setTimestamp(2, getDateRangeEnd(dateRangeType));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				FlowAnalyzerVO vo = new FlowAnalyzerVO();
				// 流程ID
				vo.addGroupColumn("FLOWID", rs.getString("FLOWID"));
				// 流程名称
				vo.addGroupColumn("FLOWNAME", rs.getString("FLOWNAME"));
				// 操作节点名称
				vo.addGroupColumn("STARTNODENAME",
						rs.getString("STARTNODENAME"));
				// 统计值
				vo.addResultField("AMOUNT", rs.getDouble("AMOUNT"));
				vo.setApplicationid(rs.getString("APPLICATIONID"));
				rtn.add(vo);
			}

			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
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

	@Override
	protected String buildQueryConsumingSql(ParamsTable params,
			String dateRangeType, String showMode, WebUser user)
			throws Exception {
		String statelabel = params.getParameterAsString("select");
		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");

		String extCondition = "1".equals(statelabel) ? "AND a.COMPLETE = 1"
				: "AND a.COMPLETE = 0";
		String extCondition1 = StringUtil.isBlank(startdate) ? "" : " AND b.ACTIONTIME>=? ";
		String extCondition2 = StringUtil.isBlank(enddate) ? "" : " AND b.PROCESSTIME<=? ";
		String extCondition3 = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND b.AUDITOR='" + user.getId() + "'";

		String time = "AVG(TRUNC((CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE)) * 24)) AS CONSUMING,";
		
		String orderBy = "AVG(TRUNC(CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE))) DESC";
		
		String sql = "select a.FLOWNAME,a.COMPLETE,a.FLOWID, " + time
				+ "COUNT(a.FLOWNAME) AS NUM FROM "
				+ getFullTableName("T_FLOWSTATERT") + " a,"
				+ getFullTableName("T_RELATIONHIS") + " b WHERE "
				+ "a.ID=b.FLOWSTATERT_ID " + extCondition + extCondition1
				+ extCondition2 + extCondition3
				+ " GROUP BY a.FLOWID,a.FLOWNAME,a.COMPLETE ORDER BY "
				+ orderBy;
		sql = buildLimitString(sql, 1, 15);
		return sql;
	}

	@Override
	protected String buildQueryNodeSql(ParamsTable params,
			String dateRangeType, String showMode, WebUser user)
			throws Exception {
		String flowname = params.getParameterAsString("flowname");
		String statelabel = params.getParameterAsString("statelabel");
		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");

		String extCondition = "1".equals(statelabel) ? "AND a.COMPLETE = 1"
				: "AND a.COMPLETE = 0";
		String extCondition1 = StringUtil.isBlank(startdate) ? "" : " AND b.ACTIONTIME>=? ";
		String extCondition2 = StringUtil.isBlank(enddate) ? "" : " AND b.PROCESSTIME<=? ";
		String extCondition3 = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND b.AUDITOR='" + user.getId() + "'";

		String time = "AVG(TRUNC((CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE)) * 24)) AS CONSUMING,";
		
		String orderBy = "AVG(TRUNC(CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE))) DESC";
		
		String sql = "select a.FLOWNAME,a.COMPLETE,b.ENDNODENAME," + time
				+ "COUNT(b.ENDNODENAME) AS NUM FROM "
				+ getFullTableName("T_FLOWSTATERT") + " a,"
				+ getFullTableName("T_RELATIONHIS") + " b WHERE "
				+ "a.ID=b.FLOWSTATERT_ID " + extCondition + extCondition1
				+ extCondition2 + extCondition3 + "AND a.FLOWNAME LIKE '"
				+ flowname + "' "
				+ " GROUP BY a.FLOWID,a.FLOWNAME,a.COMPLETE ORDER BY "
				+ orderBy;
		sql = buildLimitString(sql, 1, 15);
		return sql;
	}

	@Override
	protected String buildQueryNamesSql(ParamsTable params, String showMode,
			WebUser user) throws Exception {
		String flowname = params.getParameterAsString("flowname");
		String nodename = params.getParameterAsString("nodename");
		String statelabel = params.getParameterAsString("statelabel");
		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");

		String extCondition = "1".equals(statelabel) ? "AND a.COMPLETE = 1"
				: "AND a.COMPLETE = 0";
		String extCondition1 = StringUtil.isBlank(startdate) ? "" : " AND b.ACTIONTIME>=? ";
		String extCondition2 = StringUtil.isBlank(enddate) ? "" : " AND b.PROCESSTIME<=? ";
		String extCondition3 = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND b.AUDITOR='" + user.getId() + "'";

		String time = "AVG(TRUNC((CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE)) * 24)) AS CONSUMING,";
		
		String orderBy = "AVG(TRUNC(CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE))) DESC";
		
		String sql = "select b.ENDNODENAME,a.INITIATOR," + time
				+ "COUNT(b.ENDNODENAME) AS NUM,a.COMPLETE FROM "
				+ getFullTableName("T_FLOWSTATERT") + "" + " a,"
				+ getFullTableName("T_RELATIONHIS")
				+ " b WHERE a.ID=b.FLOWSTATERT_ID AND b.ENDNODENAME LIKE'"
				+ nodename + "' " + extCondition + extCondition1 + extCondition2
				+ extCondition3 + " AND a.FLOWNAME LIKE'" + flowname + "' "
				+ "GROUP BY b.ENDNODENAME,a.INITIATOR "
				+ orderBy;
		sql = buildLimitString(sql, 1, 15);
		return sql;
	}

	@Override
	protected String buildQueryDetailedSql(ParamsTable params, String showMode,
			WebUser user) throws Exception {
		String initiator = params.getParameterAsString("initiator");
		String nodename = params.getParameterAsString("nodename");
		String statelabel = params.getParameterAsString("statelabel");

		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");

		String extCondition = "1".equals(statelabel) ? "AND a.COMPLETE = 1"
				: "AND a.COMPLETE = 0";
		String extCondition1 = StringUtil.isBlank(startdate) ? "" : " AND b.ACTIONTIME>=? ";
		String extCondition2 = StringUtil.isBlank(enddate) ? "" : " AND b.PROCESSTIME<=? ";
		String extCondition3 = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND b.AUDITOR='" + user.getId() + "'";

		String time = "AVG(TRUNC((CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE)) * 24)) AS CONSUMING,";
		
		String orderBy = "AVG(TRUNC(CAST(b.ACTIONTIME AS DATE) - CAST(a.LASTMODIFIED AS DATE))) DESC";
		
		String sql = "select a.FLOWNAME,a.COMPLETE,b.ENDNODENAME,a.INITIATOR,b.ACTIONTIME,"
				+ "b.PROCESSTIME," + time + "c.FORMID FROM "
				+ getFullTableName("T_FLOWSTATERT") + " a,"
				+ getFullTableName("T_RELATIONHIS") + " b,"
				+ getFullTableName("T_DOCUMENT") + " c WHERE "
				+ "a.ID=b.FLOWSTATERT_ID AND a.DOCID=c.ID " + extCondition
				+ extCondition1 + extCondition2 + extCondition3 + " AND "
				+ "a.INITIATOR LIKE'" + initiator + "' AND b.ENDNODENAME LIKE'"
				+ nodename + "' GROUP BY a.FLOWNAME,a.AUDITORNAMES "
				+ " ORDER BY " + orderBy;
		sql = buildLimitString(sql, 1, 15);
		return sql;
	}

}
