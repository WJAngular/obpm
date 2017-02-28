package cn.myapps.core.workflow.notification.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.ValueObject;

public abstract class AbstractNotificationDAO {
	protected final static Logger log = Logger
			.getLogger(AbstractNotificationDAO.class);

	protected Connection connection;

	protected String DBType = "Oracle :";// 标识数据库类型

	protected String schema = "";

	public AbstractNotificationDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(ValueObject vo) throws Exception {
		throw new UnsupportedOperationException();
	}

	public ValueObject find(String id) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void remove(String pk) throws Exception {
		throw new UnsupportedOperationException();

	}

	public void update(ValueObject vo) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	public void updateLastOverDueReminder(String actorrtId) throws Exception{
		String sql = "UPDATE T_ACTORRT SET LASTOVERDUEREMINDER = ? WHERE ID = ?";

		PreparedStatement statement = null;
		try {
			log.info(sql);

			statement = connection.prepareStatement(sql);
			statement.setObject(1, new Timestamp(new Date().getTime()));
			statement.setString(2, actorrtId);
			int count = statement.executeUpdate();

			log.debug("execute:" + count);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			statement.close();
		}
	}

	public Collection<Map<String, Object>> queryOverDuePending(Date curDate,
			String applicationId) throws SQLException {
		String sql = "SELECT doc.DOCID, doc.FORMID, doc.AUDITDATE, doc.FLOWID, doc.DOMAINID, doc.SUMMARY, nrt.NODEID, art.ID ACTORRTID, art.ACTORID, art.DEADLINE";
		sql += " FROM t_pending doc INNER JOIN t_nodert nrt";
		sql += " ON doc.DOCID = nrt.DOCID INNER JOIN t_actorrt art";
		sql += " ON nrt.ID = art.NODERT_ID";
		// sql += " WHERE art.DEADLINE >= ?";
		sql += " AND doc.APPLICATIONID = '" + applicationId + "'";
		sql += " AND art.DEADLINE IS NOT NULL";
		sql += " AND art.LASTOVERDUEREMINDER IS NULL";
		sql += " AND art.DEADLINE <= ?";

		ResultSet rs = null;
		PreparedStatement statement = null;
		try {
			log.info(sql);

			statement = connection.prepareStatement(sql);
			statement.setObject(1, new Timestamp(curDate.getTime()));
			Calendar calendar = Calendar.getInstance();
			rs = statement.executeQuery();
			Collection<Map<String, Object>> pendingInfoList = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				ResultSetMetaData rsmd = rs.getMetaData();
				int cols = rsmd.getColumnCount();

				for (int i = 1; i <= cols; i++) {
					String columnName = rsmd.getColumnLabel(i).toLowerCase();
					
					try {
						if(columnName.equals("summary")){
							result.put(columnName, rs.getString(i)); // CLOB特殊处理
						}else if("auditate".equals(columnName) || "deadline".equals(columnName)){
							result.put(columnName,
									rs.getTimestamp(i, calendar) != null ? rs
											.getTimestamp(i, calendar) : null); // 日期
						}else {
							result.put(columnName, rs.getObject(i));
						}
					} catch (SQLException e0) {
						e0.printStackTrace();
					}
				}
				pendingInfoList.add(result);
			}
			// QueryRunner qRunner = new QueryRunner();
			// List pendingInfoList = (List) qRunner.query(connection, sql,
			// new Object[] { startTime }, new MapListHandler());
			return pendingInfoList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				rs.close();
			}
			statement.close();
		}
		return new ArrayList<Map<String, Object>>();
	}
}
