package cn.myapps.core.workflow.storage.runtime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;

/**
 * 
 * @author Chris
 * 
 */
public abstract class AbstractActorRTDAO {
	Logger log = Logger.getLogger(AbstractActorRTDAO.class);

	protected String dbTag = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractActorRTDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(Collection<ActorRT> actorrts) throws Exception {
		PreparedStatement statement = null;

		try {
			String sql = "INSERT INTO "
					+ getFullTableName("T_ACTORRT")
					+ "(ID,NAME,ACTORID,TYPE,ISPROCESSED,NODERT_ID,FLOWSTATERT_ID,DEADLINE, PENDING,ISREAD,DOMAINID,APPLICATIONID,APPROVAL_POSITION,DOC_ID,REMINDER_TIMES)";
			sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			log.info(dbTag + sql);
			statement = connection.prepareStatement(sql);
			for (Iterator<ActorRT> iterator = actorrts.iterator(); iterator
					.hasNext();) {
				ActorRT actorRT2 = iterator.next();
				setParameters(statement, actorRT2);
				statement.addBatch();
			}
			statement.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void create(ValueObject vo) throws Exception {
		ActorRT actorrt = (ActorRT) vo;
		PreparedStatement statement = null;

		String sql = "INSERT INTO "
				+ getFullTableName("T_ACTORRT")
				+ "(ID,NAME,ACTORID,TYPE,ISPROCESSED,NODERT_ID,FLOWSTATERT_ID,DEADLINE,PENDING,ISREAD,DOMAINID,APPLICATIONID,APPROVAL_POSITION,DOC_ID,REMINDER_TIMES)";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, actorrt);

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public ValueObject find(String id) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_ACTORRT")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				ActorRT vo = new ActorRT();
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

	public int setParameters(PreparedStatement statement, ActorRT actorrt)
			throws SQLException {
		int index = 1;
		statement.setObject(index++, actorrt.getId());
		statement.setObject(index++, actorrt.getName());
		statement.setObject(index++, actorrt.getActorid());
		statement.setObject(index++, Integer.valueOf(actorrt.getType()));
		statement.setObject(index++, actorrt.getIsprocessed() ? Integer
				.valueOf(1) : Integer.valueOf(0));
		statement.setObject(index++, actorrt.getNodertid());
		statement.setObject(index++, actorrt.getFlowstatertid());
		Date deadline = actorrt.getDeadline() != null ? new Timestamp(actorrt
				.getDeadline().getTime()) : null;
		statement.setObject(index++, deadline);
		statement.setObject(index++, actorrt.isPending() ? Integer.valueOf(1)
				: Integer.valueOf(0));
		statement.setObject(index++, actorrt.getIsread() ? Integer.valueOf(1)
				: Integer.valueOf(0));// 是否已阅
		statement.setObject(index++, actorrt.getDomainid());
		statement.setObject(index++, actorrt.getApplicationid());
		statement.setObject(index++, actorrt.getPosition());
		statement.setObject(index++, actorrt.getDocId());
		statement.setObject(index++, actorrt.getReminderTimes());

		return index;
	}

	public void setProperties(ResultSet rs, ActorRT vo) throws SQLException {
		vo.setId(rs.getString("ID"));
		vo.setName(rs.getString("NAME"));
		vo.setType(rs.getInt("TYPE"));
		vo.setActorid(rs.getString("ACTORID"));
		vo.setIsprocessed(rs.getInt("ISPROCESSED") == 1 ? true : false);
		vo.setNodertid(rs.getString("NODERT_ID"));
		vo.setFlowstatertid(rs.getString("FLOWSTATERT_ID"));
		vo.setDeadline(rs.getTimestamp("DEADLINE"));
		vo.setDomainid(rs.getString("DOMAINID"));
		vo.setApplicationid(rs.getString("APPLICATIONID"));
		vo.setPending(rs.getInt("PENDING") == 1 ? true : false);
		vo.setIsread(rs.getInt("ISREAD") == 1 ? true : false);// 是否已阅
		vo.setPosition(rs.getInt("APPROVAL_POSITION"));
		vo.setDocId(rs.getString("DOC_ID"));
		vo.setReminderTimes(rs.getInt("REMINDER_TIMES"));
	}

	/**
	 * 根据外键(FLOWSTATERT_ID)级联查找ActorRT
	 * 
	 * @param key
	 *            外键
	 * @param val
	 *            外键值
	 * @return 级联查找ActorRT
	 * @throws Exception
	 */
	public Collection<ActorRT> queryByForeignKey(String key, Object val)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_ACTORRT")
				+ " WHERE " + key.toUpperCase() + "=? ORDER BY ID";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setObject(1, val);
			ResultSet rs = statement.executeQuery();
			Collection<ActorRT> rtn = new ArrayList<ActorRT>();
			while (rs.next()) {
				ActorRT vo = new ActorRT();
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

	public Collection<ActorRT> queryByNoderRT(String nodertId, int position)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_ACTORRT")
				+ " WHERE NODERT_ID=? AND APPROVAL_POSITION=? ORDER BY ID";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setObject(1, nodertId);
			statement.setObject(2, position);
			ResultSet rs = statement.executeQuery();
			Collection<ActorRT> rtn = new ArrayList<ActorRT>();
			while (rs.next()) {
				ActorRT vo = new ActorRT();
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

	public void removeByForeignKey(String key, Object val) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_ACTORRT") + " WHERE "
				+ key.toUpperCase() + "=?";
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
		ActorRT actorrt = (ActorRT) vo;

		PreparedStatement statement = null;

		String sql = "UPDATE "
				+ getFullTableName("T_ACTORRT")
				+ " SET ID=?,NAME=?,ACTORID=?,TYPE=?,ISPROCESSED=?,NODERT_ID=?,FLOWSTATERT_ID=?,DEADLINE=?,PENDING=?,ISREAD=?,DOMAINID=?,APPLICATIONID=?,APPROVAL_POSITION=?,DOC_ID=?,REMINDER_TIMES=?";
		sql += " WHERE ID=?";
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			int index = setParameters(statement, actorrt);
			statement.setString(index, actorrt.getId());

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void remove(String pk) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_ACTORRT")
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

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
}
