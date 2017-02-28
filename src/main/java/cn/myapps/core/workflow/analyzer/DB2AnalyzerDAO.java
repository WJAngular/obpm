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

public class DB2AnalyzerDAO extends AbstractAnalyzerDAO{
	public DB2AnalyzerDAO(Connection conn) throws Exception {
		super(conn);
		this.DBType = DbTypeUtil.DBTYPE_DB2;
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_DB2);
	}

	@Override
	public Collection<FlowAnalyzerVO> analyzerActorTimeConsumingTopX(
			ParamsTable params, String dateRangeType, int top, String showMode, WebUser user) throws Exception {
		String extCondition = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND AUDITOR='" + user.getId() + "'";

		String sql = "SELECT FLOWID,FLOWNAME,STARTNODENAME,ENDNODENAME,AUDITOR,DOCID,APPLICATIONID,FLOOR(PROCESSTIME-ACTIONTIME)/60 AS AMOUNT FROM "
				+ getFullTableName("T_RELATIONHIS")
				+ " WHERE ACTIONTIME >= ? AND ACTIONTIME <?"
				+ extCondition;

		sql = buildLimitString(sql," ORDER BY  AMOUNT DESC ", 1, top);
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
		String sql = "SELECT FLOWID,FLOWNAME,APPLICATIONID,SUM(FLOOR(PROCESSTIME-ACTIONTIME)/60) AMOUNT FROM "
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

		String sql = "SELECT FLOWID,FLOWNAME,STARTNODENAME,APPLICATIONID,SUM(FLOOR(PROCESSTIME-ACTIONTIME)/60) AMOUNT FROM "
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
	
	public String buildLimitString(String sql, String orderby, int page,
			int lines) {
		if (lines == Integer.MAX_VALUE) {
			if(sql.toUpperCase().indexOf("WITH UR")>0){
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
		if (orderby != null && !orderby.trim().equals(""))
			pagingSelect.append(orderby);
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

		String time = "AVG(TIMESTAMPDIFF(8,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) AS CONSUMING,";

		String orderBy = "AVG(TIMESTAMPDIFF(4,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) DESC";
		
		String sql = "select a.FLOWNAME,a.COMPLETE," + time
				+ "COUNT(a.FLOWNAME) AS NUM FROM "
				+ getFullTableName("T_FLOWSTATERT") + " a,"
				+ getFullTableName("T_RELATIONHIS") + " b WHERE "
				+ "a.ID=b.FLOWSTATERT_ID " + extCondition + extCondition1
				+ extCondition2 + extCondition3
				+ " GROUP BY a.FLOWID,a.FLOWNAME,a.COMPLETE ORDER BY "
				+ orderBy;
		sql = buildLimitString(sql,null,1,15);
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

		String time = "AVG(TIMESTAMPDIFF(8,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) AS CONSUMING,";

		String orderBy = "AVG(TIMESTAMPDIFF(4,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) DESC";
		
		String sql = "select a.FLOWNAME,a.COMPLETE,b.ENDNODENAME," + time
				+ "COUNT(b.ENDNODENAME) AS NUM FROM "
				+ getFullTableName("T_FLOWSTATERT") + " a,"
				+ getFullTableName("T_RELATIONHIS") + " b WHERE "
				+ "a.ID=b.FLOWSTATERT_ID " + extCondition + extCondition1
				+ extCondition2 + extCondition3 + "AND a.FLOWNAME LIKE '"
				+ flowname + "' "
				+ " GROUP BY a.FLOWID,a.FLOWNAME,a.COMPLETE ORDER BY "
				+ orderBy;
		sql = buildLimitString(sql,null,1,15);
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

		String extCondition = "1".equals(statelabel) ? "AND a.COMPLETE =1"
				: "AND a.COMPLETE =0";
		String extCondition1 = StringUtil.isBlank(startdate) ? "" : " AND b.ACTIONTIME>=? ";
		String extCondition2 = StringUtil.isBlank(enddate) ? "" : " AND b.PROCESSTIME<=? ";
		String extCondition3 = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND b.AUDITOR='" + user.getId() + "'";

		String time = "AVG(TIMESTAMPDIFF(8,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) AS CONSUMING,";

		String orderBy = "AVG(TIMESTAMPDIFF(4,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) DESC";
		
		String sql = "select b.ENDNODENAME,a.INITIATOR," + time
				+ "COUNT(b.ENDNODENAME) AS NUM,a.COMPLETE FROM "
				+ getFullTableName("T_FLOWSTATERT") + "" + " a,"
				+ getFullTableName("T_RELATIONHIS")
				+ " b WHERE a.ID=b.FLOWSTATERT_ID AND b.ENDNODENAME LIKE'"
				+ nodename + "' " + extCondition + extCondition1 + extCondition2
				+ extCondition3 + " AND a.FLOWNAME LIKE'" + flowname + "' "
				+ "GROUP BY b.ENDNODENAME,a.INITIATOR "
				+ orderBy;
		sql = buildLimitString(sql,null,1,15);
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

		String extCondition = "1".equals(statelabel) ? "AND a.COMPLETE =1"
				: "AND a.COMPLETE =0";
		String extCondition1 = StringUtil.isBlank(startdate) ? "" : " AND b.ACTIONTIME>=? ";
		String extCondition2 = StringUtil.isBlank(enddate) ? "" : " AND b.PROCESSTIME<=? ";
		String extCondition3 = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND b.AUDITOR='" + user.getId() + "'";

		String time = "AVG(TIMESTAMPDIFF(8,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) AS CONSUMING,";

		String orderBy = "AVG(TIMESTAMPDIFF(4,CHAR(a.LASTMODIFIED-b.ACTIONTIME))) DESC";
		
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
		sql = buildLimitString(sql,null,1,15);
		return sql;
	}

}
