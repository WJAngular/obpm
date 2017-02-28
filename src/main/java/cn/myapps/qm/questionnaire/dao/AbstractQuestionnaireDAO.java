package cn.myapps.qm.questionnaire.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.qm.util.ConnectionManager;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.dao.AbstractBaseDAO;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcessBean;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.util.StringUtil;

public abstract class AbstractQuestionnaireDAO extends AbstractBaseDAO
		implements QuestionnaireDAO {

	private static final Logger log = Logger
			.getLogger(AbstractQuestionnaireDAO.class);

	public AbstractQuestionnaireDAO(Connection conn) {
		super(conn);
		this.tableName = "QM_QUESTIONNAIRE";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		QuestionnaireVO questionnaire = (QuestionnaireVO) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName(tableName)
				+ " (ID,TITLE,EXPLAINS,Q_CONTENT,CREATOR,CREATORNAME,CREATEDATE,STATUS,ACTORIDS,ACTORNAMES,SCORE,SCOPE,OWNERIDS,OWNERNAMES,PUBLISHDATE,CREATOR_DEPT_ID,CREATOR_DEPT_NAME,PATICIPATE_TOTAL,ANSWER_TOTAL) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, questionnaire.getId());
			stmt.setString(2, questionnaire.getTitle());
			stmt.setString(3, questionnaire.getExplains());
			stmt.setString(4, questionnaire.getContent());
			stmt.setString(5, questionnaire.getCreator());
			stmt.setString(6, questionnaire.getCreatorName());
			if (questionnaire.getCreateDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(questionnaire.getCreateDate()
						.getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setInt(8, questionnaire.getStatus());
			stmt.setString(9, questionnaire.getActorIds());
			stmt.setString(10, questionnaire.getActorNames());
			stmt.setInt(11, questionnaire.getScore());
			stmt.setString(12, questionnaire.getScope());
			stmt.setString(13, questionnaire.getOwnerIds());
			stmt.setString(14, questionnaire.getOwnerNames());
			if (questionnaire.getPublishDate() == null) {
				stmt.setNull(15, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(questionnaire.getPublishDate()
						.getTime());
				stmt.setTimestamp(15, ts);
			}
			stmt.setString(16, questionnaire.getCreatorDeptId());
			stmt.setString(17, questionnaire.getCreatorDeptName());
			stmt.setInt(18, 0);
			stmt.setInt(19, questionnaire.getOwnerIds().split(";").length);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}

	public void delete(String pk) throws Exception {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";
		String sql2 = "DELETE FROM " + getFullTableName("QM_ANSWER")
				+ " WHERE QUESTIONNAIRE_ID=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			stmt.execute();

			stmt2 = connection.prepareStatement(sql2);
			stmt2.setString(1, pk);
			stmt2.execute();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(stmt2);
		}
	}

	public ValueObject find(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";
		ResultSet rs = null;
		QuestionnaireVO questionnaire = null;
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);

			rs = stmt.executeQuery();
			if (rs.next()) {
				questionnaire = new QuestionnaireVO();
				setProperties(questionnaire, rs);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("查询错误");
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return questionnaire;
	}

	public ValueObject update(ValueObject vo) throws Exception {
		QuestionnaireVO questionnaire = (QuestionnaireVO) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName(tableName)
				+ " SET ID=?,TITLE=?,EXPLAINS=?,Q_CONTENT=?,STATUS=?,ACTORIDS=?,ACTORNAMES=?,SCORE=?,SCOPE=?,OWNERIDS=?,OWNERNAMES=?,PUBLISHDATE=?,CREATOR_DEPT_ID=?,CREATOR_DEPT_NAME=?,PATICIPATE_TOTAL=?,ANSWER_TOTAL=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, questionnaire.getId());
			stmt.setString(2, questionnaire.getTitle());
			stmt.setString(3, questionnaire.getExplains());
			stmt.setString(4, questionnaire.getContent());
			stmt.setInt(5, questionnaire.getStatus());
			stmt.setString(6, questionnaire.getActorIds());
			stmt.setString(7, questionnaire.getActorNames());
			stmt.setInt(8, questionnaire.getScore());
			stmt.setString(9, questionnaire.getScope());
			stmt.setString(10, questionnaire.getOwnerIds());
			stmt.setString(11, questionnaire.getOwnerNames());
			if (questionnaire.getPublishDate() == null) {
				stmt.setNull(12, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(questionnaire.getPublishDate()
						.getTime());
				stmt.setTimestamp(12, ts);
			}
			stmt.setString(13, questionnaire.getCreatorDeptId());
			stmt.setString(14, questionnaire.getCreatorDeptName());
			stmt.setInt(15, 0);
			stmt.setInt(16, questionnaire.getOwnerIds().split(";").length);
			stmt.setString(17, questionnaire.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("更新数据失败");
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}

	void setProperties(QuestionnaireVO questionnaire, ResultSet rs)
			throws Exception {
		try {
			questionnaire.setId(rs.getString("ID"));
			questionnaire.setTitle(rs.getString("TITLE"));
			questionnaire.setExplains(rs.getString("EXPLAINS"));
			questionnaire.setCreator(rs.getString("CREATOR"));
			questionnaire.setCreatorName(rs.getString("CREATORNAME"));
			questionnaire.setCreateDate(rs.getTimestamp("CREATEDATE"));
			questionnaire.setStatus(rs.getInt("STATUS"));
			questionnaire.setContent(rs.getString("Q_CONTENT"));
			questionnaire.setActorIds(rs.getString("ACTORIDS"));
			questionnaire.setActorNames(rs.getString("ACTORNAMES"));
			questionnaire.setScore(rs.getInt("SCORE"));
			questionnaire.setScope(rs.getString("SCOPE")); // 发布分类
			questionnaire.setOwnerIds(rs.getString("OWNERIDS")); // 发布范围id
			questionnaire.setOwnerNames(rs.getString("OWNERNAMES")); // 发布范围名称
			questionnaire.setPublishDate(rs.getTimestamp("PUBLISHDATE"));
			questionnaire.setCreatorDeptId(rs.getString("CREATOR_DEPT_ID"));
			questionnaire.setCreatorDeptName(rs.getString("CREATOR_DEPT_NAME"));
			questionnaire.setParticipantTotal(rs.getInt("PATICIPATE_TOTAL"));
			questionnaire.setAnswerTotal(rs.getInt("ANSWER_TOTAL"));
		} catch (SQLException e) {
			throw e;
		}

	}

	/**
	 * 获取待填写问卷
	 */
	public DataPackage<QuestionnaireVO> queryByPublish(WebUser user)
			throws Exception {
		DataPackage<QuestionnaireVO> qs = new DataPackage<QuestionnaireVO>();

		String sql = "SELECT * FROM "
				+ getFullTableName(tableName)
				+ " WHERE STATUS=1 AND ID NOT IN (SELECT QUESTIONNAIRE_ID FROM QM_ANSWER WHERE USER_ID = '"
				+ user.getId() + "') AND ACTORIDS LIKE '%" + user.getId()
				+ "%'";

		PreparedStatement stmt = null;
		sql = bulidOrderString(sql, "PUBLISHDATE");
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ArrayList<QuestionnaireVO> datas = new ArrayList<QuestionnaireVO>();
			while (rs != null && rs.next()) {
				QuestionnaireVO vo = new QuestionnaireVO();
				setProperties(vo, rs);
				datas.add(vo);
			}

			qs.datas = datas;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return qs;
	}

	public DataPackage<QuestionnaireVO> queryByPublishDone(WebUser user)
			throws Exception {
		DataPackage<QuestionnaireVO> qs = new DataPackage<QuestionnaireVO>();
		String sql = "SELECT * FROM "
				+ getFullTableName(tableName)
				+ " WHERE STATUS=1 AND ID IN (SELECT QUESTIONNAIRE_ID FROM QM_ANSWER WHERE USER_ID = '"
				+ user.getId() + "') ";
		PreparedStatement stmt = null;
		sql = bulidOrderString(sql, "PUBLISHDATE");
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ArrayList<QuestionnaireVO> datas = new ArrayList<QuestionnaireVO>();
			while (rs != null && rs.next()) {
				QuestionnaireVO vo = new QuestionnaireVO();
				setProperties(vo, rs);
				datas.add(vo);
			}

			qs.datas = datas;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return qs;
	}

	/**
	 * 根据条件获取实例集合
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public DataPackage<QuestionnaireVO> queryByFilter(String s_title, int page,
			int lines, WebUser user) throws Exception {
		DataPackage<QuestionnaireVO> qs = new DataPackage<QuestionnaireVO>();
		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE TITLE LIKE '%" + s_title + "%'";

		if (!user.getDomainUser().equals("true")) {
			sql += " and CREATOR='" + user.getId() + "'";
		}
		PreparedStatement stmt = null;

		sql = bulidOrderString(sql, "CREATEDATE");

		String limitsql = buildLimitString(sql, page, lines, "CREATEDATE",
				"DESC");
		log.debug(sql);

		try {
			stmt = connection.prepareStatement(limitsql);
			ResultSet rs = stmt.executeQuery();
			ArrayList<QuestionnaireVO> datas = new ArrayList<QuestionnaireVO>();
			while (rs != null && rs.next()) {
				QuestionnaireVO vo = new QuestionnaireVO();
				setProperties(vo, rs);
				datas.add(vo);
			}

			qs.datas = datas;
			Long rowCount = countBySQL(sql);
			qs.setRowCount(rowCount.intValue());
			qs.linesPerPage = lines;
			qs.pageNo = page;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return qs;
	}

	/**
	 * 添加1位已参加答卷人数
	 * @param arg
	 * @throws Exception
	 */
	public boolean addPaticipate(String id) throws Exception{
		String sql = "UPDATE "+getFullTableName(tableName)+" SET PATICIPATE_TOTAL = PATICIPATE_TOTAL + 1 WHERE ID=?";

		PreparedStatement stmt = null;
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("更新数据失败");
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return true;
	}
	
	
	public static void main(String[] arg) throws Exception {
		QuestionnaireVO q = new QuestionnaireVO();
		q.setId("XXX");
		q.setTitle("测试");
		q.setExplains("说明");
		q.setCreator("肖一");
		q.setCreateDate(new Date());
		q.setStatus(1);
		QuestionnaireProcessBean bean = new QuestionnaireProcessBean();
		bean.getDAO().create(q);

		QuestionnaireVO q2 = (QuestionnaireVO) bean.getDAO().find("XXX");
		System.out.println(q2.getTitle());
	}

	/**
	 * 添加排序 降序排列
	 * 
	 * @param sql
	 * @param field
	 * @return
	 */
	protected String bulidOrderString(String sql, String field) {
		if (StringUtil.isBlank(sql)) {
			return sql;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM (");
		buffer.append(sql);
		buffer.append(") tb_orderby ORDER BY ");
		buffer.append(field);
		buffer.append(" DESC");
		return buffer.toString();
	}

	/**
	 * 添加总页数
	 */
	public long countBySQL(String sql) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ") AS T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new Exception("查询错误");
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}

	protected abstract String buildLimitString(String sql, int page, int lines,
			String orderbyField, String orderbyMode) throws SQLException;

	public DataPackage<QuestionnaireVO> query(WebUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询根据条件查找我发布的问卷实例集合
	 * @param title
	 *            需要查找的标题
	 * @param status
	 *            问卷的状态
	 * @param page
	 *            当前页数
	 * @param lines
	 *            每页的条数
	 * @param user
	 *            查询的用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<QuestionnaireVO> doQueryMyPublish(String title,
			int status, int page, int lines, WebUser user) throws Exception {
		if (status != QuestionnaireVO.STATUS_ALL && status != QuestionnaireVO.STATUS_NORMAL && status != QuestionnaireVO.STATUS_PUBLISH && status != QuestionnaireVO.STATUS_RECOVER)
			throw new Exception("错误的status参数");

		DataPackage<QuestionnaireVO> dataPackage = new DataPackage<QuestionnaireVO>();
		String sql=null;
		if(status != QuestionnaireVO.STATUS_ALL){
		 sql = "SELECT * FROM qm_questionnaire WHERE CREATOR='"
				+ user.getId() + "' AND STATUS=" + status
				+ " AND TITLE LIKE '%" + title + "%'";
		}else{
			sql = "SELECT * FROM qm_questionnaire WHERE CREATOR='"
					+ user.getId() 
					+ "' AND TITLE LIKE '%" + title + "%'";
		}
		PreparedStatement stmt = null;
		String limitSQL = null;
		sql = bulidOrderString(sql, "CREATEDATE");
		limitSQL = buildLimitString(sql, page, lines, "CREATEDATE", "DESC");
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(limitSQL);
			ResultSet rs = stmt.executeQuery();
			ArrayList<QuestionnaireVO> datas = new ArrayList<QuestionnaireVO>();
			while (rs != null && rs.next()) {
				QuestionnaireVO vo = new QuestionnaireVO();
				setProperties(vo, rs);
				datas.add(vo);
			}

			dataPackage.datas = datas;
			Long rowCount = countBySQL(sql);
			dataPackage.setRowCount(rowCount.intValue());
			dataPackage.linesPerPage = lines;
			dataPackage.pageNo = page;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return dataPackage;
	}

	/**
	 * 查询根据条件查找我参与的问卷实例集合
	 * @param title
	 *            需要查找的标题
	 * @param status
	 *            问卷的状态
	 * @param page
	 *            当前页数
	 * @param lines
	 *            每页的条数
	 * @param user
	 *            查询的用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<QuestionnaireVO> doQueryMyPartake(String title,
			int status, int page, int lines, WebUser user) throws Exception {
		if (status != -1 && status != 1 && status != 2)
			throw new Exception("请传入正确的status参数");
		DataPackage<QuestionnaireVO> dataPackage = new DataPackage<QuestionnaireVO>();
		String sql = null;
		if (AnswerVO.STATUS_FILLING == status) {
			sql = "SELECT * FROM "
					+ getFullTableName(tableName)
					+ " WHERE STATUS=1 AND ID NOT IN (SELECT QUESTIONNAIRE_ID FROM QM_ANSWER WHERE USER_ID = '"
					+ user.getId() + "') AND TITLE LIKE '%" + title
					+ "%' AND ACTORIDS LIKE '%" + user.getId() + "%'";
		} else if(AnswerVO.STATUS_FILLED == status){
			sql = "SELECT Q.* FROM " + getFullTableName(tableName)
					+ " Q, QM_ANSWER A WHERE A.USER_ID='" + user.getId()
					+ "' AND A.QUESTIONNAIRE_ID = Q.ID AND Q.TITLE LIKE '%"
					+ title + "%'";
		} else {
			throw new Exception("请输入正确的status参数");
		}

		PreparedStatement stmt = null;
		sql = bulidOrderString(sql, "PUBLISHDATE");
		String limitSql = buildLimitString(sql, page, lines, "PUBLISHDATE",
				"DESC");
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(limitSql);
			ResultSet rs = stmt.executeQuery();
			ArrayList<QuestionnaireVO> datas = new ArrayList<QuestionnaireVO>();
			while (rs != null && rs.next()) {
				QuestionnaireVO vo = new QuestionnaireVO();
				vo.setFillStatus(status);
				setProperties(vo, rs);
				datas.add(vo);
			}

			dataPackage.datas = datas;

			Long rowCount = countBySQL(sql);
			dataPackage.setRowCount(rowCount.intValue());
			dataPackage.linesPerPage = lines;
			dataPackage.pageNo = page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("查询错误");
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return dataPackage;
	}

	/**
	 * 查询根据条件查找我发布的问卷实例集合所有集合
	 * @param title
	 *            需要查找的标题
	 * @param status
	 *            问卷的状态
	 * @param page
	 *            当前页数
	 * @param lines
	 *            每页的条数
	 * @param user
	 *            查询的用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<QuestionnaireVO> doQueryMyPartakeAll(String title,
			int status, int page, int lines, WebUser user) throws Exception {
		DataPackage<QuestionnaireVO> dataPackage = new DataPackage<QuestionnaireVO>();
		String sqlFilling = null;
		String sqlFilled = null;
		sqlFilling = "SELECT Q.*,1 as FILL_STATUS FROM "
				+ getFullTableName(tableName)
				+ " Q WHERE STATUS=1 AND ID NOT IN (SELECT QUESTIONNAIRE_ID FROM QM_ANSWER WHERE USER_ID = '"
				+ user.getId() + "') AND TITLE LIKE '%" + title
				+ "%' AND ACTORIDS LIKE '%" + user.getId() + "%'";
		sqlFilled = "SELECT Q.*,2 as FILL_STATUS FROM " + getFullTableName(tableName)
				+ " Q, QM_ANSWER A WHERE A.USER_ID='" + user.getId()
				+ "' AND A.QUESTIONNAIRE_ID = Q.ID AND Q.TITLE LIKE '%" + title
				+ "%'";
		String sqlAll = sqlFilled + " UNION " + sqlFilling;
		
		PreparedStatement stmt = null;
		
		sqlAll = bulidOrderString(sqlAll, "PUBLISHDATE");
		String limitSql = buildLimitString(sqlAll, page, lines, "PUBLISHDATE",
				"DESC");
		log.debug(sqlAll);
		try {
			stmt = connection.prepareStatement(limitSql);
			ResultSet rs = stmt.executeQuery();
			ArrayList<QuestionnaireVO> datas = new ArrayList<QuestionnaireVO>();
			while (rs != null && rs.next()) {
				QuestionnaireVO vo = new QuestionnaireVO();
				setProperties(vo, rs);
				vo.setFillStatus(rs.getInt("FILL_STATUS"));
				datas.add(vo);
			}

			dataPackage.datas = datas;

			Long rowCount = countBySQL(sqlAll);
			dataPackage.setRowCount(rowCount.intValue());
			dataPackage.linesPerPage = lines;
			dataPackage.pageNo = page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("查询错误");
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return dataPackage;
	}

}
