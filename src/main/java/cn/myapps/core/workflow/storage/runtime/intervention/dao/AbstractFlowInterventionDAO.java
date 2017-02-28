package cn.myapps.core.workflow.storage.runtime.intervention.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.intervention.ejb.FlowInterventionVO;
import cn.myapps.util.StringUtil;

/**
 * @author Happy
 *
 */
public abstract class AbstractFlowInterventionDAO {

	Logger LOG = Logger.getLogger(AbstractFlowInterventionDAO.class);

	protected Connection connection;

	protected String schema;

	protected String dbType = "Oracle: ";


	public AbstractFlowInterventionDAO(Connection connection) {
		this.connection = connection;
	}

	public void create(ValueObject obj) throws Exception {
		FlowInterventionVO vo = (FlowInterventionVO) obj;
		PreparedStatement statement = null;

		String sql = "INSERT INTO "
				+ getFullTableName("T_FLOW_INTERVENTION")
				+ "(ID,SUMMARY,FLOWNAME, STATELABEL, INITIATOR,INITIATORID,INITIATOR_DEPT,INITIATOR_DEPT_ID,LASTAUDITOR, FIRSTPROCESSTIME, LASTPROCESSTIME ,FLOWID, FORMID, DOCID, APPLICATIONID, DOMAINID, VERSION,STATUS,AUDITORNAMES,AUDITORLIST,LASTFLOWOPERATION)";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		LOG.debug(dbType + sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, vo);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	private int setParameters(PreparedStatement statement, FlowInterventionVO vo)
			throws SQLException {
		int paramterIndex = 0;
		statement.setObject(++paramterIndex, vo.getId());
		statement.setObject(++paramterIndex, vo.getSummary());
		statement.setObject(++paramterIndex, vo.getFlowName());
		statement.setObject(++paramterIndex, vo.getStateLabel());
		statement.setObject(++paramterIndex, vo.getInitiator());
		statement.setObject(++paramterIndex, vo.getInitiatorId());
		statement.setObject(++paramterIndex, vo.getInitiatorDept());
		statement.setObject(++paramterIndex, vo.getInitiatorDeptId());
		statement.setObject(++paramterIndex, vo.getLastAuditor());
		//为了兼容sql server数据库
		if(vo.getFirstProcessTime()!=null){
			statement.setObject(++paramterIndex, new Timestamp(vo.getFirstProcessTime().getTime()));
		} else {
			statement.setObject(++paramterIndex,null);
		}
		if(vo.getLastProcessTime()!=null){
			statement.setObject(++paramterIndex, new Timestamp(vo.getLastProcessTime().getTime()));
		} else {
			statement.setObject(++paramterIndex,null);
		}
		statement.setObject(++paramterIndex, vo.getFlowId());
		statement.setObject(++paramterIndex, vo.getFormId());
		statement.setObject(++paramterIndex, vo.getDocId());
		statement.setObject(++paramterIndex, vo.getApplicationid());
		statement.setObject(++paramterIndex, vo.getDomainid());
		statement.setObject(++paramterIndex, vo.getVersion());
		statement.setObject(++paramterIndex, vo.getStatus());
		statement.setObject(++paramterIndex, vo.getAuditorNames());
		statement.setObject(++paramterIndex, vo.getAuditorList());
		statement.setObject(++paramterIndex, vo.getLastFlowOperation());

		return paramterIndex;
	}

	public void remove(String pk) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_FLOW_INTERVENTION")
				+ " WHERE ID = '" + pk + "'";
		LOG.debug(dbType + sql);
		Statement statement = connection.createStatement();
		try {
			statement.addBatch(sql);
			statement.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void update(ValueObject obj) throws Exception {
		FlowInterventionVO vo = (FlowInterventionVO) obj;
		String sql = "UPDATE "
				+ getFullTableName("T_FLOW_INTERVENTION")
				+ " SET ID=?,SUMMARY=?,FLOWNAME=?, STATELABEL=?, INITIATOR=? ,INITIATORID=?,INITIATOR_DEPT=?,INITIATOR_DEPT_ID=?, LASTAUDITOR=?, FIRSTPROCESSTIME=?, LASTPROCESSTIME=? ,FLOWID=?, FORMID=?, DOCID=?, APPLICATIONID=?, DOMAINID=?, VERSION=?,STATUS=?,AUDITORNAMES=?,AUDITORLIST=?,LASTFLOWOPERATION=? WHERE ID=?";
		LOG.debug(dbType + sql);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			int parameterIndex = setParameters(statement, vo);
			statement.setString(++parameterIndex, vo.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public ValueObject find(String id) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_FLOW_INTERVENTION")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		LOG.debug(dbType + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				FlowInterventionVO vo = new FlowInterventionVO();
				setProperties(rs, vo);
				return vo;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void setProperties(ResultSet rs, FlowInterventionVO vo)
			throws Exception {
		vo.setId(rs.getString("ID"));
		vo.setSummary(rs.getString("SUMMARY"));
		vo.setFlowName(rs.getString("FLOWNAME"));
		vo.setStateLabel(StringUtil.dencodeHTML(rs.getString("STATELABEL")));
		vo.setInitiator(rs.getString("INITIATOR"));
		vo.setInitiatorId(rs.getString("INITIATORID"));
		vo.setInitiatorDept(rs.getString("INITIATOR_DEPT"));
		vo.setInitiatorDeptId(rs.getString("INITIATOR_DEPT_ID"));
		vo.setLastAuditor(rs.getString("LASTAUDITOR"));
		vo.setFirstProcessTime(rs.getTimestamp("FIRSTPROCESSTIME"));
		vo.setLastProcessTime(rs.getTimestamp("LASTPROCESSTIME"));
		vo.setFlowId(rs.getString("FLOWID"));
		vo.setFormId(rs.getString("FORMID"));
		vo.setDocId(rs.getString("DOCID"));
		vo.setApplicationid(rs.getString("APPLICATIONID"));
		vo.setDomainid(rs.getString("DOMAINID"));
		vo.setVersion(rs.getInt("VERSION"));
		vo.setStatus(rs.getString("STATUS"));
		vo.setAuditorNames(rs.getString("AUDITORNAMES"));
		vo.setAuditorList(rs.getString("AUDITORLIST"));
		vo.setLastFlowOperation(rs.getString("LASTFLOWOPERATION"));
	}

	public long countByFilter(ParamsTable params, WebUser user)
			throws Exception {
		return countBySQL(createFlowInterventionSQL(params, user), params.getParameterAsString("domain"));
	}

	public DataPackage<FlowInterventionVO> queryByFilter(ParamsTable params, WebUser user)
			throws Exception {
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");
		int currpage = _currpage != null && !"0".equals(_currpage) ? Integer.parseInt(_currpage) : 1;
		int pagelines = _pagelines != null && !"0".equals(_pagelines) ? Integer.parseInt(_pagelines) : 10;

		String _orderby = params.getParameterAsString("_orderby");

		StringBuffer sqlBuilder = new StringBuffer();
		String sql = createFlowInterventionSQL(params, user);

		sqlBuilder.append(sql);
		sqlBuilder.append(" ORDER BY ").append(
				StringUtil.isBlank(_orderby) ? "ID" : _orderby.toUpperCase());

		return queryBySQLPage(sqlBuilder.toString(), currpage, pagelines, params.getParameterAsString("domain"));
	}

	protected String createFlowInterventionSQL(ParamsTable params, WebUser user)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String whereBlock = buildWhereBlock(params);
		
		sql.append("SELECT * FROM "+getFullTableName("T_FLOW_INTERVENTION")+" WHERE 1=1 ");
		sql.append(whereBlock);
		return sql.toString();
	}
	
	/**
	 * 拼接查询条件
	 * @param params
	 * @return
	 */
	public String buildWhereBlock(ParamsTable params) {
		StringBuffer whereBlock = new StringBuffer();
		
		String _flowName = params.getParameterAsString("_flowName");
		String _steteLabel = params.getParameterAsString("_stateLabel");
		String _inintator = params.getParameterAsString("_initiator");
		String _inintatorId = params.getParameterAsString("_initiatorId");
		String _inintatorDept = params.getParameterAsString("_initiatorDept");
		String _inintatorDeptId = params.getParameterAsString("_initiatorDeptId");
		String _lastAuditor = params.getParameterAsString("_lastAuditor");
		String _firstProcessTime = params.getParameterAsString("_firstProcessTime");
		String _lastProcessTime = params.getParameterAsString("_lastProcessTime");
		String _summary = params.getParameterAsString("_summary");
		
		
		if(_flowName !=null && _flowName.trim().length()>0){
			whereBlock.append(" AND FLOWNAME like '%"+_flowName+"%' ");
		}
		if(_steteLabel !=null && _steteLabel.trim().length()>0){
			whereBlock.append(" AND STATELABEL like '%"+_steteLabel+"%' ");
		}
		if(_inintator !=null && _inintator.trim().length()>0){
			whereBlock.append(" AND INITIATOR like '%"+_inintator+"%' ");
		}
		if(_inintatorId !=null && _inintatorId.trim().length()>0){
			whereBlock.append(" AND INITIATORID = '"+_inintator+"' ");
		}
		if(_inintatorDeptId !=null && _inintatorDeptId.trim().length()>0){
			whereBlock.append(" AND INITIATOR_DEPT_ID = '"+_inintatorDeptId+"' ");
		}
		if(_inintatorDept !=null && _inintatorDept.trim().length()>0){
			whereBlock.append(" AND INITIATOR_DEPT = '"+_inintatorDept+"' ");
		}
		if(_lastAuditor !=null && _lastAuditor.trim().length()>0){
			whereBlock.append(" AND LASTAUDITOR like '%"+_lastAuditor+"%' ");
		}
		if(_firstProcessTime !=null && _firstProcessTime.trim().length()>0){
			whereBlock.append(" AND FIRSTPROCESSTIME > "+assemblyTime(_firstProcessTime));
		}
		if(_lastProcessTime !=null && _lastProcessTime.trim().length()>0){
			whereBlock.append(" AND LASTPROCESSTIME > "+assemblyTime(_lastProcessTime));
		}
		if(_summary !=null && _summary.trim().length()>0){
			whereBlock.append(" AND SUMMARY like '%"+_summary+"%' ");
		}
		
		return whereBlock.toString();
	}

	public DataPackage<FlowInterventionVO> queryBySQL(String sql, String domainid) throws Exception {
		return queryBySQLPage(sql, 1, Integer.MAX_VALUE, domainid);
	}

	public DataPackage<FlowInterventionVO> queryBySQLPage(String sql, int page, int lines,
			String domainid) throws Exception {

		HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
		sql = sqlUtil.appendCondition(sql, "DOMAINID ='" + domainid + "'");
		/**
		 * 生成分页SQL
		 */
		String limitSQL = buildLimitString(sql, page, lines);
		LOG.debug(dbType + limitSQL);
		PreparedStatement statement = null;
		try {
			DataPackage<FlowInterventionVO> dpg = new DataPackage<FlowInterventionVO>();
			statement = connection.prepareStatement(limitSQL);
			ResultSet rs = statement.executeQuery();
			Collection<FlowInterventionVO> datas = new ArrayList<FlowInterventionVO>();
			
			for (int i = 0; i < lines && rs.next(); i++) {
				FlowInterventionVO vo = new FlowInterventionVO();
				setProperties(rs, vo);
				datas.add(vo);
			}

			dpg.datas = datas;
			dpg.rowCount = (int) countBySQL(sql, domainid);
			dpg.linesPerPage = lines;
			dpg.pageNo = page;

			return dpg;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public abstract String buildLimitString(String sql, int page, int lines);
	
	/**
	 * 拼装时间格式  用于处理不同数据库
	 * @param time
	 * 		默认时间格式是  yyyy-mm-dd
	 * @return
	 * @throws Exception
	 */
	public abstract String assemblyTime(String time);

	public long countBySQL(String sql, String domainid) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;

		HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
		sql = sqlUtil.appendCondition(sql, "DOMAINID ='" + domainid + "'");
		String countSQL = "SELECT COUNT(*) FROM (" + sql + ") TB";

		LOG.debug(dbType + countSQL);
		try {
			statement = connection.prepareStatement(countSQL);
			rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
			PersistenceUtils.closeResultSet(rs);
		}

		return 0;
	}

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}


	public void remove(ValueObject vo) throws Exception {
		remove(vo.getId());
	}
}
