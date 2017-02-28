package cn.myapps.core.workflow.storage.runtime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistoryVO;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.util.StringUtil;

/**
 * 
 * @author Chris
 * 
 */
public abstract class AbstractRelationHISDAO {
	Logger log = Logger.getLogger(AbstractRelationHISDAO.class);

	protected String dbTag = "Oracle: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractRelationHISDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	/**
	 * 获取一条历史记录
	 * 
	 * @param docid
	 *            Document id
	 * @param flowid
	 *            流程 id
	 * @return 一条历史记录
	 * @throws Exception
	 */
	public RelationHIS find(String docid, String flowid) throws Exception {
		return findLastRelationHIS(docid, flowid, null);
	}

	/**
	 * 根据流程操作,获取最后的审批者
	 * 
	 * @param docid
	 * @param flowid
	 * @param flowOperation
	 * @return
	 * @throws Exception
	 */
	public RelationHIS findLastRelationHIS(String docid, String flowid,
			String flowOperation) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo";
		sql += " WHERE vo.DOCID='" + docid + "'";
		sql += " AND vo.flowid='" + flowid + "' ";
		if (!StringUtil.isBlank(flowOperation)) {
			sql += " AND FLOWOPERATION = " + flowOperation;
		}
		sql += " ORDER BY actiontime DESC";

		Object data = this.getData(sql);
		if (data != null && data instanceof RelationHIS) {
			return (RelationHIS) data;
		}

		return null;
	}

	public RelationHIS findLastByEndNode(String docid, String flowStateId,
			String endNode) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo";
		sql += " WHERE vo.DOCID='" + docid + "'";
		sql += " AND vo.FLOWSTATERT_ID='" + flowStateId + "' ";
		sql += " AND ENDNODEID = '" + endNode + "'";
		sql += " ORDER BY actiontime DESC";

		Object data = this.getData(sql);
		if (data != null && data instanceof RelationHIS) {
			return (RelationHIS) data;
		}

		return null;
	}

	public RelationHIS findLastByStartNode(String docid, String flowid,String flowstatertId,
			String startNode) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo";
		sql += " WHERE vo.DOCID='" + docid + "'";
		sql += " AND vo.flowid='" + flowid + "' ";
		sql += " AND vo.flowstatert_id='" + flowstatertId + "' ";
		sql += " AND STARTNODEID = '" + startNode + "'";
		sql += " ORDER BY actiontime DESC";

		Object data = this.getData(sql);
		if (data != null && data instanceof RelationHIS) {
			return (RelationHIS) data;
		}

		return null;
	}

	public Collection<RelationHIS> queryRelationHIS(String docid,
			String flowStateId, String snodeid, String flowOperation)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo";
		sql += " WHERE vo.DOCID='" + docid + "'";
		sql += " AND vo.FLOWSTATERT_ID='" + flowStateId + "' ";
		if (!StringUtil.isBlank(flowOperation)) {
			sql += " AND vo.FLOWOPERATION IN (" + flowOperation + ")";
		}
		if (!StringUtil.isBlank(snodeid)) {
			sql += " AND vo.STARTNODEID = '" + snodeid + "'";
		}

		sql += " ORDER BY actiontime DESC";

		Collection<RelationHIS> datas = this.getDatas(sql);

		return datas;
	}

	public RelationHIS findRelHISByCondition(String docid, String startnodeid,
			String endnodeid, boolean ispassed) throws Exception {
		String passed = null;
		if (ispassed) {
			passed = "1";
		} else {
			passed = "0";
		}

		String hql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo WHERE " + "vo.DOCID='" + docid
				+ "' AND vo.startnodeid='" + startnodeid
				+ "' AND vo.endnodeid='" + endnodeid + "' AND vo.ispassed="
				+ passed;

		Object data = this.getData(hql);
		if (data != null && data instanceof RelationHIS) {
			return (RelationHIS) data;
		}
		return null;
	}

	public Collection<RelationHIS> query(String docid, String flowid)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo WHERE " + "vo.DOCID='" + docid + "' AND vo.flowid='"
				+ flowid + "'" + " ORDER BY vo.actiontime";
		return this.getDatas(sql);
	}

	public Collection<RelationHIS> queryRelationHIS(String docid,
			String flowid, String endnodeid) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo WHERE " + "vo.DOCID='" + docid + "' AND vo.flowid='"
				+ flowid + "' AND vo.endnodeid='" + endnodeid
				+ "' ORDER BY vo.actiontime DESC";

		return this.getDatas(sql);
	}

	public Collection<String> queryStartNodeHis(String docid, String flowid,
			String endnodeid) throws Exception {
		String sql = "SELECT DISTINCT vo.startnodeid FROM "
				+ getFullTableName("T_RELATIONHIS") + " vo WHERE "
				+ "vo.docid='" + docid + "' AND vo.flowid='" + flowid
				+ "' AND vo.endnodeid='" + endnodeid
				+ "' ORDER BY vo.actiontime DESC";

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			Collection<String> rtn = new ArrayList<String>();

			while (rs.next()) {
				rtn.add(rs.getString("STARTNODEID"));
			}
			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void create(ValueObject vo) throws Exception {
		RelationHIS relhis = (RelationHIS) vo;
		PreparedStatement statement = null;

		String sql = "INSERT INTO "
				+ getFullTableName("T_RELATIONHIS")
				+ "(ID,ACTIONTIME,FLOWID,FLOWNAME,DOCID,ATTITUDE,ENDNODEID,ENDNODENAME,STARTNODEID,STARTNODENAME,ISPASSED,PROCESSTIME,AUDITOR,FLOWOPERATION,REMINDERCOUNT,FLOWSTATERT_ID,APPLICATIONID )";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info(dbTag + sql);
		try {
			if (relhis != null) {
				statement = connection.prepareStatement(sql);

				setParameters(statement, vo);
				// setValues(statement, 11, relhis.getSortId());
				statement.executeUpdate();

				// 级联Create ActorHIS
				if (!relhis.getActorhiss().isEmpty()) {
					String sql2 = "INSERT INTO "
							+ getFullTableName("T_ACTORHIS")
							+ "(ID,NAME,ACTORID,AGENTID,AGENTNAME,TYPE,PROCESSTIME,ATTITUDE,NODEHIS_ID,FLOWSTATERT_ID,DOC_ID,SIGNATURE)";
					sql2 += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
					log.info(dbTag + sql2);
					statement = connection.prepareStatement(sql2);
					for (Iterator<ActorHIS> iter = relhis.getActorhiss()
							.iterator(); iter.hasNext();) {
						ActorHIS acthis = (ActorHIS) iter.next();

						setValues(statement, 1, acthis.getId());
						setValues(statement, 2, acthis.getName());
						setValues(statement, 3, acthis.getActorid());
						setValues(statement, 4, acthis.getAgentid());
						setValues(statement, 5, acthis.getAgentname());
						setValues(statement, 6,
								Integer.valueOf(acthis.getType()));
						Timestamp processtime = null;
						if (acthis.getProcesstime() != null) {
							processtime = new Timestamp(acthis.getProcesstime()
									.getTime());
						} else {
							processtime = new Timestamp(new Date().getTime());
						}
						setValues(statement, 7, processtime);
						setValues(statement, 8, acthis.getAttitude());
						setValues(statement, 9, relhis.getId());
						setValues(statement, 10, relhis.getFlowStateId());
						setValues(statement, 11, relhis.getDocid());
						setValues(statement, 12, acthis.getSignature());
						statement.addBatch();
					}
					statement.executeBatch();
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public ValueObject find(String id) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setValues(statement, 1, id);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				RelationHIS vo = createRelationHIS(rs);

				// 级联Query ActorHIS
				String sql2 = "SELECT * FROM " + getFullTableName("T_ACTORHIS")
						+ " WHERE NODEHIS_ID=?";
				log.info(dbTag + sql2);
				statement = connection.prepareStatement(sql2);
				setValues(statement, 1, id);
				ResultSet rs2 = statement.executeQuery();

				addActorHIS(vo, rs2);

				return vo;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public Object getData(String sql) throws Exception {
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				RelationHIS vo = createRelationHIS(rs);
				// 级联Query ActorHIS
				String sql2 = "SELECT * FROM " + getFullTableName("T_ACTORHIS")
						+ " WHERE NODEHIS_ID=?";
				log.debug("OracleSQL: " + sql2);
				statement = connection.prepareStatement(sql2);
				setValues(statement, 1, vo.getId());
				ResultSet rs2 = statement.executeQuery();
				addActorHIS(vo, rs2);
				return vo;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public Collection<RelationHIS> getDatas(String sql) throws Exception {
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			Collection<RelationHIS> rtn = new ArrayList<RelationHIS>();

			while (rs.next()) {
				RelationHIS vo = createRelationHIS(rs);
				// 级联Query ActorHIS
				String sql2 = "SELECT * FROM " + getFullTableName("T_ACTORHIS")
						+ " WHERE NODEHIS_ID=?";
				log.debug("OracleSQL: " + sql2);
				statement = connection.prepareStatement(sql2);
				setValues(statement, 1, vo.getId());
				ResultSet rs2 = statement.executeQuery();
				addActorHIS(vo, rs2);
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

	public void remove(String id) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_RELATIONHIS")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		log.info(dbTag + sql);
		try {
			statement = connection.prepareStatement(sql);
			setValues(statement, 1, id);

			// 级联Remove ActorHIS
			String sql2 = "DELETE FROM " + getFullTableName("T_ACTORHIS")
					+ " WHERE NODEHIS_ID=?";
			log.info(dbTag + sql2);
			PreparedStatement statement2 = connection.prepareStatement(sql2);
			setValues(statement2, 1, id);

			statement2.executeUpdate();

			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void update(ValueObject vo) throws Exception {
		RelationHIS relhis = (RelationHIS) vo;
		PreparedStatement statement = null;

		try {
			if (relhis != null) {
				String sql = "UPDATE "
						+ getFullTableName("T_RELATIONHIS")
						+ " SET ID=?,ACTIONTIME=?,FLOWID=?,FLOWNAME=?,DOCID=?,ATTITUDE=?,ENDNODEID=?,ENDNODENAME=?,STARTNODEID=?,STARTNODENAME=?,ISPASSED=?,PROCESSTIME=?,AUDITOR=?,FLOWOPERATION=?,REMINDERCOUNT=?,FLOWSTATERT_ID=?,APPLICATIONID=?";
				sql += " WHERE ID=?";
				log.info(dbTag + sql);

				statement = connection.prepareStatement(sql);
				int index = setParameters(statement, vo);
				setValues(statement, ++index, relhis.getId());
				statement.executeUpdate();

				// 删除所有ActorHIS
				String sql1 = "DELETE FROM " + getFullTableName("T_ACTORHIS")
						+ " WHERE NODEHIS_ID=?";
				log.info(dbTag + sql1);
				statement = connection.prepareStatement(sql1);
				statement.setString(1, relhis.getId());
				statement.executeUpdate();

				// 重新创建ActorHIS
				if (!relhis.getActorhiss().isEmpty()) {
					for (Iterator<ActorHIS> iter = relhis.getActorhiss()
							.iterator(); iter.hasNext();) {

						String sql2 = "INSERT INTO "
								+ getFullTableName("T_ACTORHIS")
								+ "(ID,NAME,ACTORID,AGENTID,AGENTNAME,TYPE,PROCESSTIME,ATTITUDE,NODEHIS_ID,FLOWSTATERT_ID,DOC_ID,SIGNATURE)";
						sql2 += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
						log.info(dbTag + sql2);
						statement = connection.prepareStatement(sql2);

						ActorHIS actorhis = (ActorHIS) iter.next();
						setValues(statement, 1, actorhis.getId());
						setValues(statement, 2, actorhis.getName());
						setValues(statement, 3, actorhis.getActorid());
						setValues(statement, 4, actorhis.getAgentid());
						setValues(statement, 5, actorhis.getAgentname());
						setValues(statement, 6,
								Integer.valueOf(actorhis.getType()));
						Timestamp processtime = null;
						if (actorhis.getProcesstime() != null) {
							processtime = new Timestamp(actorhis
									.getProcesstime().getTime());
						} else {
							processtime = new Timestamp(new Date().getTime());
						}
						setValues(statement, 7, processtime);
						setValues(statement, 8, actorhis.getAttitude());
						setValues(statement, 9, relhis.getId());
						setValues(statement, 10, relhis.getFlowStateId());
						setValues(statement, 11, relhis.getDocid());
						setValues(statement, 12, actorhis.getSignature());
						statement.addBatch();

						statement.executeBatch();
					}

				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	private RelationHIS createRelationHIS(ResultSet rs) throws SQLException {
		RelationHIS vo = new RelationHIS();
		vo.setId(rs.getString("ID"));
		vo.setActiontime(rs.getTimestamp("ACTIONTIME"));
		vo.setAttitude(rs.getString("ATTITUDE"));
		vo.setFlowid(rs.getString("FLOWID"));
		vo.setFlowname(rs.getString("FLOWNAME"));
		vo.setDocid(rs.getString("DOCID"));
		vo.setEndnodeid(rs.getString("ENDNODEID"));
		vo.setEndnodename(rs.getString("ENDNODENAME"));
		vo.setStartnodeid(rs.getString("STARTNODEID"));
		vo.setStartnodename(rs.getString("STARTNODENAME"));
		vo.setIspassed(rs.getInt("ISPASSED") == 1 ? true : false);
		vo.setProcesstime(rs.getTimestamp("PROCESSTIME"));
		vo.setAuditor(rs.getString("AUDITOR"));
		vo.setFlowOperation(rs.getString("FLOWOPERATION"));
		vo.setReminderCount(rs.getInt("REMINDERCOUNT"));
		vo.setFlowStateId(rs.getString("FLOWSTATERT_ID"));
		return vo;
	}

	private void addActorHIS(RelationHIS vo, ResultSet rs) throws SQLException {
		while (rs.next()) {
			ActorHIS acthis = new ActorHIS();
			acthis.setId(rs.getString("ID"));
			acthis.setActorid(rs.getString("ACTORID"));
			acthis.setAgentid(rs.getString("AGENTID"));
			acthis.setAgentname(rs.getString("AGENTNAME"));
			acthis.setName(rs.getString("NAME"));
			acthis.setType(rs.getInt("TYPE"));
			acthis.setProcesstime(rs.getTimestamp("PROCESSTIME"));
			acthis.setAttitude(rs.getString("ATTITUDE"));
			acthis.setSignature(rs.getString("SIGNATURE"));
			if (!vo.getActorhiss().contains(acthis))
				vo.getActorhiss().add(acthis);

		}
	}

	private int setParameters(PreparedStatement statement, ValueObject vo)
			throws SQLException {
		int index = 0;
		RelationHIS relhis = (RelationHIS) vo;

		setValues(statement, ++index, relhis.getId());
		Timestamp actiontime = null;
		if (relhis.getActiontime() != null) {
			actiontime = new Timestamp(relhis.getActiontime().getTime());
		}
		setValues(statement, ++index, actiontime);
		setValues(statement, ++index, relhis.getFlowid());
		setValues(statement, ++index, relhis.getFlowname());
		setValues(statement, ++index, relhis.getDocid());
		setValues(statement, ++index, relhis.getAttitude());
		setValues(statement, ++index, relhis.getEndnodeid());
		setValues(statement, ++index,
				StringUtil.dencodeHTML(relhis.getEndnodename()));
		setValues(statement, ++index, relhis.getStartnodeid());
		setValues(statement, ++index,
				StringUtil.dencodeHTML(relhis.getStartnodename()));
		setValues(statement, ++index,
				Integer.valueOf(relhis.getIspassed() ? 1 : 0));
		Timestamp processtime = null;
		if (relhis.getProcesstime() != null) {
			processtime = new Timestamp(relhis.getProcesstime().getTime());
		} else {
			processtime = new Timestamp(new Date().getTime());
		}
		setValues(statement, ++index, processtime);
		setValues(statement, ++index, relhis.getAuditor());
		setValues(statement, ++index, relhis.getFlowOperation());
		setValues(statement, ++index,
				Integer.valueOf(relhis.getReminderCount()));
		setValues(statement, ++index, relhis.getFlowStateId());
		setValues(statement, ++index, relhis.getApplicationid());

		return index;
	}

	private void setValues(PreparedStatement statement, int index, Object obj)
			throws SQLException {
		if (obj != null) {
			statement.setObject(index, obj);
		} else {
			statement.setNull(index, Types.NULL);
		}
	}

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}

	public Collection<RelationHIS> queryByDocIdAndFlowStateId(String docid,
			String flowStateId) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo WHERE " + "vo.DOCID='" + docid
				+ "' AND vo.FLOWSTATERT_ID='" + flowStateId
				+ "' AND vo.STARTNODEID IS NOT NULL "
				+ " ORDER BY vo.actiontime";
		return this.getDatas(sql);
	}

	public RelationHIS findByDocIdAndFlowStateId(String docId,
			String flowStateId) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo";
		sql += " WHERE vo.DOCID='" + docId + "'";
		sql += " AND vo.FLOWSTATERT_ID='" + flowStateId
				+ "' AND vo.STARTNODEID IS NOT NULL ";
		sql += " ORDER BY actiontime DESC";

		Object data = this.getData(sql);
		if (data != null && data instanceof RelationHIS) {
			return (RelationHIS) data;
		}
		return null;
	}

	public Collection<RelationHIS> queryAllByDocIdAndFlowStateId(String docid,
			String flowStateId) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo WHERE " + "vo.DOCID='" + docid
				+ "' AND vo.FLOWSTATERT_ID='" + flowStateId + "'"
				+ " ORDER BY vo.actiontime";
		return this.getDatas(sql);
	}

	public RelationHIS findCompleteRelationHIS(String docid, String flowStateId)
			throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo";
		sql += " WHERE vo.DOCID='" + docid + "'";
		sql += " AND vo.FLOWSTATERT_ID='" + flowStateId
				+ "' AND vo.FLOWOPERATION IN (";
		sql += "'" + cn.myapps.core.workflow.FlowType.RUNNING2COMPLETE + "',";
		sql += "'"
				+ cn.myapps.core.workflow.FlowType.RUNNING2RUNNING_INTERVENTION
				+ "')";
		sql += " ORDER BY actiontime DESC";

		Object data = this.getData(sql);
		if (data != null && data instanceof RelationHIS) {
			return (RelationHIS) data;
		}
		return null;
	}

	public void removeByDocument(String docId) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_RELATIONHIS")
				+ " vo WHERE " + "vo.DOCID='" + docId + "'";
		Collection<RelationHIS> datas = this.getDatas(sql);
		for (Iterator<RelationHIS> iterator = datas.iterator(); iterator
				.hasNext();) {
			RelationHIS relationHIS = iterator.next();
			remove(relationHIS.getId());
		}

	}
	
	/**
	 * 获取指定流程实例的流程历史集合
	 * @param folowStateId
	 * 		流程实例id
	 * @return
	 * 		流程历史的集合
	 * @throws Exception
	 */
	public Collection<FlowHistoryVO> queryFlowHistorysByFolowStateId(String folowStateId) throws Exception{
		Collection<FlowHistoryVO> historys = new ArrayList<FlowHistoryVO>();
		
		String sql = "select relhis.STARTNODENAME,relhis.ENDNODENAME,relhis.STARTNODEID,relhis.ENDNODEID,relhis.FLOWSTATERT_ID,acthis.NODEHIS_ID,acthis.NAME,acthis.ACTORID,acthis.AGENTNAME,acthis.ATTITUDE,acthis.SIGNATURE,acthis.PROCESSTIME, relhis.FLOWOPERATION " +
				"FROM " + getFullTableName("T_ACTORHIS")
				+ " acthis , " + getFullTableName("T_RELATIONHIS")
				+ " relhis  " +
				"where acthis.NODEHIS_ID=relhis.ID and acthis.FLOWSTATERT_ID = relhis.FLOWSTATERT_ID and acthis.FLOWSTATERT_ID =?" +
				"order by acthis.PROCESSTIME";
		
		log.info(dbTag + sql);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			setValues(statement, 1, folowStateId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				historys.add(createFlowHistory(rs));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
		
		return historys;
	}
	
	private FlowHistoryVO createFlowHistory(ResultSet rs) throws SQLException{
		FlowHistoryVO vo = new FlowHistoryVO();
		vo.setFolowStateId(rs.getString("FLOWSTATERT_ID"));
		vo.setHistoryId(rs.getString("NODEHIS_ID"));
		vo.setStartNodeName(rs.getString("STARTNODENAME"));
		vo.setTargetNodeName(rs.getString("ENDNODENAME"));
		vo.setAuditorName(rs.getString("NAME"));
		vo.setAttitude(rs.getString("ATTITUDE"));
		vo.setProcesstime(rs.getTimestamp("PROCESSTIME"));
		vo.setFlowOperation(rs.getString("FLOWOPERATION"));
		vo.setStartNodeId(rs.getString("STARTNODEID"));
		vo.setTargetNodeId(rs.getString("ENDNODEID"));
		vo.setAgentAuditorName(rs.getString("AGENTNAME"));
		vo.setSignature(rs.getString("SIGNATURE"));
		vo.setAuditorId(rs.getString("ACTORID"));
		return vo;
	}
}
