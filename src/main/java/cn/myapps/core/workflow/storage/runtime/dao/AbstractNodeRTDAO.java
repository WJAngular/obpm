package cn.myapps.core.workflow.storage.runtime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workflow.element.StartNode;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.StringUtil;

/**
 * 
 * @author Chris
 * 
 */
public abstract class AbstractNodeRTDAO {
	Logger log = Logger.getLogger(AbstractNodeRTDAO.class);

	protected String dbTag = "Oracle: ";

	protected String schema = null;

	protected Connection connection;

	public AbstractNodeRTDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public abstract void create(ValueObject vo) throws Exception;

	public void create(ValueObject vo, ActorRTDAO actorRTDAO) throws Exception {
		NodeRT nodert = (NodeRT) vo;
		if(nodert.getNode()!=null && nodert.getNode() instanceof StartNode) return;//开始节点不持久化
		PreparedStatement statement = null;

		String sql = "INSERT INTO "
				+ getFullTableName("T_NODERT")
				+ "(ID,NAME,FLOWID,DOCID,FLOWOPTION,STATELABEL,NODEID,FLOWSTATERT_ID, NOTIFIABLE, PASSCONDITION, PARENTNODERTID, SPLITTOKEN, DOMAINID, APPLICATIONID,DEADLINE,ORDERLY,APPROVAL_POSITION,STATE,LASTPROCESSTIME,REMINDER_TIMES)";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info(dbTag + sql);
		try {
			if (nodert != null) {
				statement = connection.prepareStatement(sql);
				setValues(statement, nodert);

				
				Collection<ActorRT> actorrts = new ArrayList<ActorRT>();
				if (!nodert.getActorrts().isEmpty()) { // 级联create ActorRT
					for (Iterator<ActorRT> iter = nodert.getActorrts()
							.iterator(); iter.hasNext();) {
						ActorRT element = (ActorRT) iter.next();
						element.setNodertid(nodert.getId());
						actorrts.add(element);
					}
				}
				actorRTDAO.create(actorrts);
				statement.executeUpdate();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public ValueObject find(String id) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_NODERT")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setValue(statement, 1, id);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				NodeRT vo = new NodeRT();
				setBaseProperties(vo, rs);

				return vo;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public NodeRT getData(String sql) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				NodeRT vo = new NodeRT();
				setBaseProperties(vo, rs);

				return vo;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeResultSet(rs);
			PersistenceUtils.closeStatement(statement);
		}
	}

	public Collection<NodeRT> getDatas(String sql) throws Exception {
		Collection<NodeRT> rtn = new ArrayList<NodeRT>();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				NodeRT vo = new NodeRT();
				setBaseProperties(vo, rs);

				rtn.add(vo);
			}
			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void remove(ValueObject obj) throws Exception {
		if (obj != null) {
			remove(obj.getId());
		}
	}

	public abstract void remove(String id) throws Exception;

	public void remove(String id, AbstractActorRTDAO actorRTDAO)
			throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_NODERT")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setValue(statement, 1, id);
			// 级联删除ActorRT
			actorRTDAO.removeByForeignKey("NODERT_ID", id);

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void removeByForeignKey(String key, Object val) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_NODERT") + " WHERE "
				+ key.toUpperCase() + "=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setValue(statement, 1, val);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public abstract void update(ValueObject vo) throws Exception;
	
	public void doUpdateReminderTimes(NodeRT vo) throws Exception{
		PreparedStatement statement = null;

		String sql = "UPDATE "
				+ getFullTableName("T_NODERT")
				+ " SET REMINDER_TIMES=?";
		sql += " WHERE ID=?";
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setObject(1, vo.getReminderTimes());
			statement.setObject(2, vo.getId());
			statement.executeUpdate();
			doUpdateReminderTimes4ActorRT(vo);
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	private void doUpdateReminderTimes4ActorRT(NodeRT vo) throws Exception{
		PreparedStatement statement = null;

		String sql = "UPDATE "
				+ getFullTableName("T_ACTORRT")
				+ " SET REMINDER_TIMES=?";
		sql += " WHERE NODERT_ID=?";
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setObject(1, vo.getReminderTimes());
			statement.setObject(2, vo.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void update(ValueObject vo, AbstractActorRTDAO actorRTDAO)
			throws Exception {
		NodeRT nodert = (NodeRT) vo;
		PreparedStatement statement = null;

		String sql = "UPDATE "
				+ getFullTableName("T_NODERT")
				+ " SET ID=?,NAME=?,FLOWID=?,DOCID=?,FLOWOPTION=?,STATELABEL=?,NODEID=?,FLOWSTATERT_ID=?, NOTIFIABLE=?, PASSCONDITION=?, PARENTNODERTID=?, SPLITTOKEN=?, DOMAINID=?, APPLICATIONID=?,DEADLINE=?,ORDERLY=?,APPROVAL_POSITION=?,STATE=?,LASTPROCESSTIME=?,REMINDER_TIMES=?";
		sql += " WHERE ID=?";
		log.info(dbTag + sql);
		try {
			if (nodert != null) {
				statement = connection.prepareStatement(sql);
				int index = setValues(statement, nodert);
				setValue(statement, ++index, nodert.getId());

				actorRTDAO.removeByForeignKey("NODERT_ID", vo.getId());
				// 级联update ActorRT
				if (!nodert.getActorrts().isEmpty()) {
					for (Iterator<ActorRT> iter = nodert.getActorrts()
							.iterator(); iter.hasNext();) {
						ActorRT element = (ActorRT) iter.next();
						element.setNodertid(nodert.getId());
						actorRTDAO.create(element);
					}
				}
				statement.executeUpdate();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void updatePosition(ValueObject vo)
			throws Exception {
		NodeRT nodert = (NodeRT) vo;
		PreparedStatement statement = null;

		String sql = "UPDATE " + getFullTableName("T_NODERT")
				+ " SET APPROVAL_POSITION=?";
		sql += " WHERE ID=?";
		log.info(dbTag + sql);
		try {
			if (nodert != null) {
				statement = connection.prepareStatement(sql);
				setValue(statement, 1, nodert.getPosition());
				setValue(statement, 2, nodert.getId());
				statement.executeUpdate();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	/**
	 * 根据文档，文档相应流程查询，获取文档的所有运行时节点
	 * 
	 * @param docid
	 *            document id
	 * @param flowid
	 *            流程实例 id
	 * @return 文档的所有运行时节点
	 * @throws Exception
	 */
	public Collection<NodeRT> query(String docid, String flowStateId)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_NODERT")
				+ " vo WHERE vo.DOCID='" + docid + "' AND vo.FLOWSTATERT_ID='"
				+ flowStateId + "'";

		return this.getDatas(sql);
	}

	/**
	 * 根据文档和流程实例，获取运行时的所有限时审批节点的集合
	 * 
	 * @param docid
	 *            文档ID
	 * @param flowStateId
	 *            流程实例ID
	 * @return 运行时的所有限时审批节点的集合
	 * @throws Exception
	 */
	public Collection<NodeRT> queryDeadlineNodes(String docid,
			String flowStateId) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_NODERT")
				+ " vo WHERE vo.DOCID='" + docid + "' AND vo.FLOWSTATERT_ID='"
				+ flowStateId + "' AND vo.DEADLINE IS NOT NULL";

		return this.getDatas(sql);
	}

	/**
	 * 根据外键(FLOWSTATERT_ID)级联查找NodeRT
	 * 
	 * @param key
	 *            外键
	 * @param val
	 *            外键值
	 * @return NodeRT Collection
	 * @throws Exception
	 */
	public Collection<NodeRT> queryByForeignKey(String key, Object val)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_NODERT")
				+ " WHERE " + key.toUpperCase() + "=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setValue(statement, 1, val);
			ResultSet rs = statement.executeQuery();
			Set<NodeRT> rtn = new HashSet<NodeRT>();
			while (rs.next()) {
				NodeRT vo = new NodeRT();
				setBaseProperties(vo, rs);
				rtn.add(vo);
			}
			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public Collection<NodeRT> queryNodeRTByDocidAndFlowid(String docid,
			String flowid) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_NODERT")
				+ " vo WHERE vo.DOCID='" + docid + "' AND vo.FLOWID='" + flowid
				+ "'";

		return this.getDatas(sql);
	}

	public Collection<NodeRT> queryNodeRTByFlowStateIdAndDocId(
			String instanceId, String docId) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_NODERT")
				+ " vo WHERE vo.DOCID='" + docId + "' AND vo.FLOWSTATERT_ID='"
				+ instanceId + "'";

		return this.getDatas(sql);
	}

	public NodeRT findByNodeid(String docid, String flowStateId, String nodeid)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_NODERT") + " vo";
		sql += " WHERE vo.DOCID='" + docid + "'";
		sql += " AND vo.NODEID='" + nodeid + "'";
		sql += " AND vo.FLOWSTATERT_ID='" + flowStateId + "'";

		return this.getData(sql);
	}

	/**
	 * 设置参数值
	 * 
	 * @param statement
	 *            PreparedStatement
	 * @param index
	 *            索引
	 * @param obj
	 *            参数值
	 * @throws SQLException
	 */
	private void setValue(PreparedStatement statement, int index, Object obj)
			throws SQLException {
		if (obj != null) {
			statement.setObject(index, obj);
		} else {
			statement.setNull(index, Types.NULL);
		}
	}

	private void setBaseProperties(NodeRT vo, ResultSet rs) throws SQLException {
		vo.setId(rs.getString("ID"));
		vo.setName(rs.getString("NAME"));
		vo.setNodeid(rs.getString("NODEID"));
		vo.setFlowid(rs.getString("FLOWID"));
		vo.setDocid(rs.getString("DOCID"));
		vo.setFlowoption(rs.getString("FLOWOPTION"));
		vo.setStatelabel(StringUtil.dencodeHTML(rs.getString("STATELABEL")));
		vo.setDomainid(rs.getString("DOMAINID"));
		vo.setNotifiable(rs.getInt("NOTIFIABLE") == 1 ? true : false);
		vo.setPassCondition(rs.getInt("PASSCONDITION"));
		vo.setParentNodertid(rs.getString("PARENTNODERTID"));
		vo.setSplitToken(rs.getString("SPLITTOKEN"));
		vo.setFlowstatertid(rs.getString("FLOWSTATERT_ID"));
		vo.setApplicationid(rs.getString("APPLICATIONID"));
		vo.setDeadline(rs.getTimestamp("DEADLINE"));
		vo.setOrderly(rs.getInt("ORDERLY") == 1 ? true : false);
		vo.setPosition(rs.getInt("APPROVAL_POSITION"));
		vo.setState(rs.getInt("STATE"));
		vo.setLastProcessTime(rs.getTimestamp("LASTPROCESSTIME"));
		vo.setReminderTimes(rs.getInt("REMINDER_TIMES"));

	}

	private int setValues(PreparedStatement statement, NodeRT nodert)
			throws SQLException {
		int i = 0;
		setValue(statement, ++i, nodert.getId());
		setValue(statement, ++i, nodert.getName());
		setValue(statement, ++i, nodert.getFlowid());
		setValue(statement, ++i, nodert.getDocid());
		setValue(statement, ++i, nodert.getFlowoption());
		setValue(statement, ++i, nodert.getStatelabel());
		setValue(statement, ++i, nodert.getNodeid());
		setValue(statement, ++i, nodert.getFlowstatertid());
		setValue(statement, ++i, nodert.isNotifiable() ? Integer.valueOf(1)
				: Integer.valueOf(0));
		setValue(statement, ++i, Integer.valueOf(nodert.getPassCondition()));
		setValue(statement, ++i, nodert.getParentNodertid());
		setValue(statement, ++i, nodert.getSplitToken());
		setValue(statement, ++i, nodert.getDomainid());
		setValue(statement, ++i, nodert.getApplicationid());
		Timestamp time = null;
		if (nodert.getDeadline() != null) {
			time = new Timestamp(nodert.getDeadline().getTime());
		}
		setValue(statement, ++i, time);
		setValue(statement, ++i, nodert.isOrderly() ? Integer.valueOf(1)
				: Integer.valueOf(0));
		setValue(statement, ++i, nodert.getPosition());
		setValue(statement, ++i, nodert.getState());
		Timestamp lastTime = null;
		if(nodert.getLastProcessTime() != null){
			lastTime = new Timestamp(nodert.getLastProcessTime().getTime());
		}
		setValue(statement, ++i, lastTime);
		setValue(statement, ++i, nodert.getReminderTimes());

		return i;
	}

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
}
