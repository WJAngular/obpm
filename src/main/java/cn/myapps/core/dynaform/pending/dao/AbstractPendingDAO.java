package cn.myapps.core.dynaform.pending.dao;

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
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.dao.UserDAO;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.storage.runtime.dao.FlowStateRTDAO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public abstract class AbstractPendingDAO {

	Logger LOG = Logger.getLogger(AbstractPendingDAO.class);

	protected Connection connection;

	protected String schema;

	protected String dbType = "Oracle: ";

	protected UserDAO userDAO;

	public FlowStateRTDAO stateRTDAO;

	public AbstractPendingDAO(Connection connection) {
		this.connection = connection;
		try {
			userDAO = (UserDAO) DAOFactory
					.getDefaultDAO(UserVO.class.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create(ValueObject vo) throws Exception {
		PendingVO pendingVO = (PendingVO) vo;
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;

		String sql = "INSERT INTO "
				+ getFullTableName("T_PENDING")
				+ "(ID,FORMID,FORMNAME, FLOWID, CREATED, LASTMODIFIED, AUDITDATE, AUTHOR ,AUDITUSER, STATELABEL, STATE, AUDITORNAMES, LASTFLOWOPERATION, LASTMODIFIER, SUMMARY, APPLICATIONID,DOMAINID,DOCID)";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		LOG.debug(dbType + sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, pendingVO);

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
			PersistenceUtils.closeStatement(statement2);
		}
	}

	private int setParameters(PreparedStatement statement, PendingVO pendingVO)
			throws SQLException {
		int paramterIndex = 0;
		try {
			statement.setObject(++paramterIndex, Sequence.getSequence());
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		statement.setObject(++paramterIndex, pendingVO.getFormid());
		statement.setObject(++paramterIndex, pendingVO.getFormname());
		statement.setObject(++paramterIndex, pendingVO.getFlowid());

		if (pendingVO.getCreated() != null) {
			statement.setTimestamp(++paramterIndex, new Timestamp(pendingVO
					.getCreated().getTime()));
		} else {
			statement.setTimestamp(++paramterIndex, null);
		}
		if (pendingVO.getLastmodified() != null) {
			statement.setTimestamp(++paramterIndex, new Timestamp(pendingVO
					.getLastmodified().getTime()));
		} else {
			statement.setTimestamp(++paramterIndex, null);
		}
		if (pendingVO.getAuditdate() != null) {
			statement.setTimestamp(++paramterIndex, new Timestamp(pendingVO
					.getAuditdate().getTime()));
		} else {
			statement.setTimestamp(++paramterIndex, null);
		}

		if (pendingVO.getAuthor() != null) {
			statement.setObject(++paramterIndex, pendingVO.getAuthor().getId());
		} else {
			statement.setObject(++paramterIndex, null);
		}
		statement.setObject(++paramterIndex, pendingVO.getAudituser());
		statement.setObject(++paramterIndex, pendingVO.getStateLabel());

		if (pendingVO.getState() != null) {
			statement.setObject(++paramterIndex, pendingVO.getState().getId());
		} else {
			statement.setObject(++paramterIndex, null);
		}

		statement.setObject(++paramterIndex, pendingVO.getAuditorNames());
		statement.setObject(++paramterIndex, pendingVO.getLastFlowOperation());
		statement.setObject(++paramterIndex, pendingVO.getLastmodifier());
		statement.setObject(++paramterIndex, pendingVO.getSummary());
		statement.setObject(++paramterIndex, pendingVO.getApplicationid());
		statement.setObject(++paramterIndex, pendingVO.getDomainid());
		statement.setObject(++paramterIndex, pendingVO.getDocId());

		return paramterIndex;
	}

	public void remove(String pk) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_PENDING")
				+ " WHERE ID = '" + pk + "'";
		String sql2 = "DELETE FROM " + getFullTableName("T_PENDING_ACTOR_SET")
				+ " WHERE DOCID = '" + pk + "'";
		LOG.debug(dbType + sql);
		LOG.debug(dbType + sql2);
		Statement statement = connection.createStatement();
		try {
			statement.addBatch(sql);
			statement.addBatch(sql2);
			statement.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void removeByDocId(String pk) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_PENDING")
				+ " WHERE DOCID = '" + pk + "'";
		String sql2 = "DELETE FROM " + getFullTableName("T_PENDING_ACTOR_SET")
				+ " WHERE DOCID = '" + pk + "'";
		LOG.debug(dbType + sql);
		LOG.debug(dbType + sql2);
		Statement statement = connection.createStatement();
		try {
			statement.addBatch(sql);
			statement.addBatch(sql2);
			statement.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void update(ValueObject vo) throws Exception {
		PendingVO pendingVO = (PendingVO) vo;
		String sql = "UPDATE "
				+ getFullTableName("T_PENDING")
				+ " SET ID=?,FORMID=?,FORMNAME=?, FLOWID=?, CREATED=?, LASTMODIFIED=?, AUDITDATE=?, AUTHOR=? ,AUDITUSER=?, STATELABEL=?, STATE=?, AUDITORNAMES=?, LASTFLOWOPERATION=?, LASTMODIFIER=?, SUMMARY=?, APPLICATIONID=?,DOMAINID=?,DOCID=? WHERE ID=?";
		LOG.debug(dbType + sql);
		PreparedStatement statement = null;
		Statement statement2 = null;
		try {
			statement = connection.prepareStatement(sql);
			int parameterIndex = setParameters(statement, pendingVO);
			statement.setString(++parameterIndex, pendingVO.getId());

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
			PersistenceUtils.closeStatement(statement2);
		}
	}

	public void updateSummaryByDocument(String docId, String summary)
			throws Exception {
		String sql = "UPDATE " + getFullTableName("T_PENDING")
				+ " SET SUMMARY=? WHERE DOCID=?";
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, summary);
			statement.setString(2, docId);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public ValueObject find(String id) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_PENDING")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		LOG.debug(dbType + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				PendingVO pendingVO = new PendingVO();
				setProperties(rs, pendingVO);
				return pendingVO;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public ValueObject findByDocIdAndFlowStateRtId(String id, String state)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_PENDING")
				+ " WHERE DOCID=? AND STATE=?";
		PreparedStatement statement = null;
		LOG.debug(dbType + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			statement.setString(2, state);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				PendingVO pendingVO = new PendingVO();
				setProperties(rs, pendingVO);
				return pendingVO;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void setProperties(ResultSet rs, PendingVO pendingVO)
			throws Exception {
		pendingVO.setId(rs.getString("ID"));
		pendingVO.setFormid(rs.getString("FORMID"));
		pendingVO.setFormname(rs.getString("FORMNAME"));
		pendingVO.setFlowid(rs.getString("FLOWID"));
		pendingVO.setApplicationid(rs.getString("APPLICATIONID"));
		pendingVO.setAuditdate(rs.getDate("AUDITDATE"));
		pendingVO.setAuditorNames(rs.getString("AUDITORNAMES"));
		pendingVO.setAudituser(rs.getString("AUDITUSER"));
		String authorId = rs.getString("AUTHOR");
		if (!StringUtil.isBlank(authorId)) {
			UserVO author = (UserVO) userDAO.find(authorId);
			pendingVO.setAuthor(author);
		}
		pendingVO.setCreated(rs.getDate("CREATED"));
		pendingVO.setLastFlowOperation(rs.getString("LASTFLOWOPERATION"));
		pendingVO.setLastmodifier(rs.getString("LASTMODIFIER"));
		String stateId = rs.getString("STATE");
		if (!StringUtil.isBlank(stateId)) {
			FlowStateRT stateRT = (FlowStateRT) stateRTDAO.find(stateId);
			pendingVO.setState(stateRT);
		}
		pendingVO.setStateLabel(StringUtil.dencodeHTML(rs.getString("STATELABEL")));
		pendingVO.setSummary(rs.getString("SUMMARY"));
		pendingVO.setDomainid(rs.getString("DOMAINID"));
		pendingVO.setDocId(rs.getString("DOCID"));
	}

	public long countByFilter(ParamsTable params, WebUser user)
			throws Exception {
		return countBySQL(createPendingSQL(params, user), user.getDomainid());
	}

	public DataPackage<PendingVO> queryByFilter(ParamsTable params, WebUser user)
			throws Exception {
		Long _currpage = params.getParameterAsLong("_currpage");
		Long _pagelines = params.getParameterAsLong("_pagelines");
		int currpage = _currpage != null ? _currpage.intValue() : 1;
		int pagelines = _pagelines != null ? _pagelines.intValue() : 10;

		String _orderby = params.getParameterAsString("_orderby");

		StringBuffer sqlBuilder = new StringBuffer();
		String sql = createPendingSQL(params, user);

		sqlBuilder.append(sql);
		sqlBuilder.append(" ORDER BY ").append(
				StringUtil.isBlank(_orderby) ? "ID" : "PEN."+_orderby.toUpperCase()
						+ ",ACTOR.REMINDER_TIMES DESC ");

		return queryBySQLPage(sqlBuilder.toString(), currpage, pagelines,
				user.getDomainid());
	}

	public DataPackage<PendingVO> queryByUser(WebUser user) throws Exception {
		String sql = "SELECT * FROM T_PENDING WHERE AUDITUSER=?";
		DataPackage<PendingVO> dpg = new DataPackage<PendingVO>();

		/**
		 * 生成分页SQL
		 */
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, user.getId());

			ResultSet rs = statement.executeQuery();
			Collection<PendingVO> datas = new ArrayList<PendingVO>();

			while (rs.next()) {
				PendingVO pendingVO = new PendingVO();
				setProperties(rs, pendingVO);
				datas.add(pendingVO);
			}

			dpg.datas = datas;

			dpg.rowCount = dpg.datas.size();
			dpg.linesPerPage = dpg.datas.size();
			dpg.pageNo = 1;

			return dpg;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	protected String createPendingSQL(ParamsTable params, WebUser user)
			throws Exception {
		FormProcess process = (FormProcess) ProcessFactory
				.createProcess(FormProcess.class);

		StringBuffer sqlBuilder = new StringBuffer();
		String formId = params.getParameterAsString("formid");
		Form form = (Form) process.doView(formId);

		sqlBuilder.append("SELECT  PEN.*,ACTOR.REMINDER_TIMES FROM T_PENDING PEN INNER JOIN T_ACTORRT ACTOR ON PEN.STATE=ACTOR.FLOWSTATERT_ID WHERE ACTOR.ACTORID IN(");
		sqlBuilder
				.append(user.getActorListString(form.getApplicationid())
						+ ") AND ACTOR.PENDING=1");
		sqlBuilder.append(" AND PEN.FORMID='" + formId).append("'");
		sqlBuilder.append(" AND PEN.SUMMARY IS NOT NULL");

		return sqlBuilder.toString();
	}

	public DataPackage<PendingVO> queryBySQL(String sql, String domainid)
			throws Exception {
		return queryBySQLPage(sql, 1, Integer.MAX_VALUE, domainid);
	}

	public DataPackage<PendingVO> queryBySQLPage(String sql, int page,
			int lines, String domainid) throws Exception {

		HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
		//sql = sqlUtil.appendCondition(sql, "DOMAINID ='" + domainid + "'");

		DataPackage<PendingVO> dpg = new DataPackage<PendingVO>();
		dpg.rowCount = (int) countBySQL(sql, domainid);
		dpg.linesPerPage = lines;
		int pages = dpg.rowCount % lines == 0 ? dpg.rowCount / lines
				: dpg.rowCount / lines + 1;
		if (pages > 0 && page > pages)// 当前页大于页数，则显示最后一页
			page = pages;
		dpg.pageNo = page;

		/**
		 * 生成分页SQL
		 */
		String limitSQL = buildLimitString(sql, page, lines);
		LOG.debug(dbType + limitSQL);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(limitSQL);
			ResultSet rs = statement.executeQuery();
			Collection<PendingVO> datas = new ArrayList<PendingVO>();

			for (int i = 0; i < lines && rs.next(); i++) {
				PendingVO pendingVO = new PendingVO();
				setProperties(rs, pendingVO);
				datas.add(pendingVO);
			}

			dpg.datas = datas;

			return dpg;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public abstract String buildLimitString(String sql, int page, int lines);

	public long countBySQL(String sql, String domainid) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;

		HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
		//sql = sqlUtil.appendCondition(sql, "DOMAINID ='" + domainid + "'");
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

	public FlowStateRTDAO getStateRTDAO() {
		return stateRTDAO;
	}

	public void setStateRTDAO(FlowStateRTDAO stateRTDAO) {
		this.stateRTDAO = stateRTDAO;
	}

	public void remove(ValueObject vo) throws Exception {
		remove(vo.getId());
	}
}
