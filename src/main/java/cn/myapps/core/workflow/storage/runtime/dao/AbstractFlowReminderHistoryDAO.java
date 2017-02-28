package cn.myapps.core.workflow.storage.runtime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowReminderHistory;

/**
 * @author happy
 * 
 */
public abstract class AbstractFlowReminderHistoryDAO {

	Logger log = Logger.getLogger(AbstractFlowReminderHistoryDAO.class);

	protected String dbTag = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractFlowReminderHistoryDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(ValueObject vo) throws Exception {
		FlowReminderHistory flowReminderHistory = (FlowReminderHistory) vo;
		PreparedStatement statement = null;

		String sql = "INSERT INTO "
				+ getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ "(ID,REMINDER_CONTENT,USER_ID,USER_NAME,NODE_NAME,DOC_ID,FLOW_INSTANCE_ID,DOMAINID,APPLICATIONID,PROCESS_TIME)";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?)";
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, flowReminderHistory);

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public ValueObject find(String id) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				FlowReminderHistory vo = new FlowReminderHistory();
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

	public int setParameters(PreparedStatement statement, FlowReminderHistory flowReminderHistory)
			throws SQLException {
		int index = 1;
		statement.setObject(index++, flowReminderHistory.getId());
		statement.setObject(index++, flowReminderHistory.getContent());
		statement.setObject(index++, flowReminderHistory.getUserId());
		statement.setObject(index++, flowReminderHistory.getUserName());
		statement.setObject(index++, flowReminderHistory.getNodeName());
		statement.setObject(index++, flowReminderHistory.getDocId());
		statement.setObject(index++, flowReminderHistory.getFlowInstanceId());
		statement.setObject(index++, flowReminderHistory.getDomainid());
		statement.setObject(index++, flowReminderHistory.getApplicationid());
		Timestamp processtime = null;
		if (flowReminderHistory.getProcessTime() != null) {
			processtime = new Timestamp(flowReminderHistory.getProcessTime()
					.getTime());
		} else {
			processtime = new Timestamp(new Date().getTime());
		}
		statement.setObject(index++, processtime);
		
		return index;
	}

	public void setProperties(ResultSet rs, FlowReminderHistory vo) throws SQLException {
		vo.setId(rs.getString("ID"));
		vo.setContent(rs.getString("REMINDER_CONTENT"));
		vo.setUserId(rs.getString("USER_ID"));
		vo.setUserName(rs.getString("USER_NAME"));
		vo.setNodeName(rs.getString("NODE_NAME"));
		vo.setDocId(rs.getString("DOC_ID"));
		vo.setFlowInstanceId(rs.getString("FLOW_INSTANCE_ID"));
		vo.setDomainid(rs.getString("DOMAINID"));
		vo.setApplicationid(rs.getString("APPLICATIONID"));
		vo.setProcessTime(rs.getTimestamp("PROCESS_TIME"));

	}
	
	public Collection<FlowReminderHistory> queryByDocument(String docId) throws Exception{
		String sql = "SELECT * FROM " + getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ " WHERE  DOC_ID=? ORDER BY PROCESS_TIME ASC";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setObject(1, docId);
			ResultSet rs = statement.executeQuery();
			Collection<FlowReminderHistory> rtn = new ArrayList<FlowReminderHistory>();
			while (rs.next()) {
				FlowReminderHistory vo = new FlowReminderHistory();
				setProperties(rs, vo);
				rtn.add(vo);
			}
			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	/**
	 * 根据外键(DOCID、NODERT_ID、FLOWSTATERT_ID)级联查找Circulator
	 * 
	 * @param key
	 *            外键
	 * @param val
	 *            外键值
	 * @return 级联查找ActorRT
	 * @throws Exception
	 */
	public Collection<FlowReminderHistory> queryByForeignKey(String key, Object val)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ " WHERE " + key.toUpperCase() + "=? ORDER BY ID";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setObject(1, val);
			ResultSet rs = statement.executeQuery();
			Collection<FlowReminderHistory> rtn = new ArrayList<FlowReminderHistory>();
			while (rs.next()) {
				FlowReminderHistory vo = new FlowReminderHistory();
				setProperties(rs, vo);
				rtn.add(vo);
			}
			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	/**
	 * 根据外键(DOCID、NODERT_ID、FLOWSTATERT_ID)删除Circulator
	 * 
	 * @param key
	 *            外键字段名
	 * @param val
	 *            外键值
	 * @throws Exception
	 */
	public void removeByForeignKey(String key, Object val) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ " WHERE " + key.toUpperCase() + "=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setObject(1, val);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void update(ValueObject vo) throws Exception {
		FlowReminderHistory flowReminderHistory = (FlowReminderHistory) vo;

		PreparedStatement statement = null;

		String sql = "UPDATE "
				+ getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ " SET ID=?,REMINDER_CONTENT=?,USER_ID=?,USER_NAME=?,NODE_NAME=?,DOC_ID=?,FLOW_INSTANCE_ID=?,DOMAINID=?,APPLICATIONID=?,PROCESS_TIME=?";
		sql += " WHERE ID=?";
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			int index = setParameters(statement, flowReminderHistory);
			statement.setString(index, flowReminderHistory.getId());

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	/**
	 * 根据主键删除
	 * 
	 * @param pk
	 * @throws Exception
	 */
	public void remove(String pk) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, pk);

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	public void removeByDocument(String docId) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_WORKFLOW_REMINDER_HISTORY")
				+ " WHERE DOC_ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, docId);

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	/**
	 * 添加order by 语句
	 * @param sql
	 * @param orderName
	 * @return
	 */
	protected String addOrderBy(String sql, String orderName){
		StringBuffer sqlBuilder = new StringBuffer();
		sqlBuilder.append(sql);
		sqlBuilder.append(" ORDER BY c."+orderName+" DESC");
		return sqlBuilder.toString();
	}
	
	protected String getWorkSQLCondition(String _flowId, String _formId,String _subject4Circulator){
		String sql = "";
		if (_formId != null && _formId.trim().length() > 0) {
			sql += " and p.formid='" + _formId + "'";
		}
		
		if (_subject4Circulator != null && _subject4Circulator.trim().length() > 0) {
			sql += " and p.SUMMARY like '%" + _subject4Circulator + "%'";
		}
		
		return sql;
	}

	public DataPackage<FlowReminderHistory> queryBySQLPage(String sql, int page,
			int lines, String domainid) throws Exception {

		// HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
		/**
		 * 生成分页SQL
		 */
		String limitSQL = buildLimitString(sql, page, lines);
		
		
		log.info(dbTag + limitSQL);
		PreparedStatement statement = null;
		try {
			DataPackage<FlowReminderHistory> dpg = new DataPackage<FlowReminderHistory>();
			statement = connection.prepareStatement(limitSQL);
			ResultSet rs = statement.executeQuery();
			Collection<FlowReminderHistory> datas = new ArrayList<FlowReminderHistory>();
			
			int databaseVersion = connection.getMetaData().getDatabaseMajorVersion();
			if (9 <= databaseVersion || !dbTag.equals("MS SQL SERVER: ")) {
				//数据库版本>=9 或 数据库类型不为MSSQL
			} else {
				// JDBC1.0
				long emptylines = 1L * (page - 1) * lines;
				for (int i = 0; i < emptylines && rs.next(); i++) {
					// keep empty
				}
			}

			for (int i = 0; i < lines && rs.next(); i++) {
				FlowReminderHistory vo = new FlowReminderHistory();
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
	

	public long countBySQL(String sql, String domainid) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;

		String countSQL = "SELECT COUNT(*) FROM (" + sql + ") TB";
		log.info(dbTag + countSQL);
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
	
	
}
