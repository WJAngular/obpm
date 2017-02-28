package cn.myapps.qm.answer.dao;

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
import cn.myapps.pm.util.ConnectionManager;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.dao.AbstractBaseDAO;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcessBean;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.util.StringUtil;

public abstract class AbstractAnswerDAO extends AbstractBaseDAO implements AnswerDAO {
private static final Logger log = Logger.getLogger(AbstractAnswerDAO.class);
	
	public AbstractAnswerDAO(Connection conn) {
		super(conn);
		this.tableName = "QM_ANSWER";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		AnswerVO answer = (AnswerVO)vo;
		PreparedStatement stmt = null;
		
		String sql = "INSERT INTO "+getFullTableName(tableName)+" (ID,USER_ID,ANSWER,ANSWER_DATE,STATUS,QUESTIONNAIRE_ID,USER_NAME,TOTAL,USER_DEPARTMENT) values (?,?,?,?,?,?,?,?,?)";
		
		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, answer.getId());
			stmt.setString(2, answer.getUserId());
			stmt.setString(3, answer.getAnswer());
			if (answer.getDate() == null) {
				stmt.setNull(4, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(answer.getDate().getTime());
				stmt.setTimestamp(4, ts);
			}
			
			stmt.setInt(5, answer.getStatus());
			stmt.setString(6, answer.getQuestionnaire_id());
			stmt.setString(7, answer.getUserName());
			stmt.setInt(8,answer.getAccount());
			stmt.setString(9,answer.getUserDepartment());
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

		String sql = "DELETE FROM " + getFullTableName(tableName) + " WHERE ID=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			
			stmt.execute();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public ValueObject find(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT A.*,B.TITLE,B.EXPLAINS,B.SCORE,B.* FROM " + getFullTableName(tableName) +" A,"+getFullTableName("QM_QUESTIONNAIRE")+" B where A.QUESTIONNAIRE_ID = B.ID AND A.ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);

			ResultSet rs = stmt.executeQuery();
			AnswerVO answer = null;
			if (rs.next()) {
				answer = new AnswerVO();
				setProperties(answer, rs);
			}
			rs.close();
			return answer;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public DataPackage<AnswerVO> query(WebUser user) throws Exception {
		DataPackage<AnswerVO> qs = new DataPackage<AnswerVO>();
		String sql = "SELECT A.*,B.TITLE,B.EXPLAINS,B.CREATORNAME,B.Q_CONTENT,B.SCORE FROM " + getFullTableName(tableName) +" A,"+getFullTableName("QM_QUESTIONNAIRE")+" B where A.QUESTIONNAIRE_ID = B.ID AND USER_ID=?";
		PreparedStatement stmt = null;
		sql = bulidOrderString(sql, "ANSWER_DATE");
		log.debug(sql);
		try{
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getId());
			ResultSet rs = stmt.executeQuery();
			ArrayList<AnswerVO> datas = new ArrayList<AnswerVO>();
			while (rs != null && rs.next()) {
				AnswerVO vo = new AnswerVO();
				setProperties(vo, rs);
				datas.add(vo);
			}
			
			qs.datas = datas;
			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
		return qs;
	}
	
	/**
	 * 
	 * 个人答卷查询
	 * @param user
	 * @param s_title
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AnswerVO> queryByFilter(String s_title,int page,int lines,WebUser user) throws Exception {
		DataPackage<AnswerVO> qs = new DataPackage<AnswerVO>();
		String	sql = "SELECT A.*,B.TITLE,B.EXPLAINS,B.CREATORNAME,B.Q_CONTENT,B.SCORE FROM " + getFullTableName(tableName) +" A,"+getFullTableName("QM_QUESTIONNAIRE")+" B where B.TITLE LIKE '%"+s_title+"%' AND A.QUESTIONNAIRE_ID = B.ID AND A.USER_ID='"+user.getId()+"'";
		
		PreparedStatement stmt = null;
		sql = bulidOrderString(sql, "ANSWER_DATE");
		String limitSql = buildLimitString(sql, page, lines,"ANSWER_DATE","DESC");
		log.debug(sql);
		try{
			stmt = connection.prepareStatement(limitSql);
			
			ResultSet rs = stmt.executeQuery();
			ArrayList<AnswerVO> datas = new ArrayList<AnswerVO>();
			while (rs != null && rs.next()) {
				AnswerVO vo = new AnswerVO();
				setProperties(vo, rs);
				datas.add(vo);
			}
			
			qs.datas = datas;
			Long rowCount = countBySQL(sql);
			qs.setRowCount(rowCount.intValue());
			qs.linesPerPage = lines;
			qs.pageNo = page;
			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
		return qs;
	}
	
	/**
	 * 根据问卷的ID查询答卷
	 */
	public DataPackage<AnswerVO> queryForQuestionnaire(String id) throws Exception{
		DataPackage<AnswerVO> qs = new DataPackage<AnswerVO>();
		String sql = "SELECT A.*,B.TITLE,B.EXPLAINS,B.CREATORNAME,B.Q_CONTENT,B.SCORE FROM " + getFullTableName(tableName) +" A,"+getFullTableName("QM_QUESTIONNAIRE")+" B where A.QUESTIONNAIRE_ID = B.ID AND A.QUESTIONNAIRE_ID=?";
		PreparedStatement stmt = null;
		sql = bulidOrderString(sql, "ANSWER_DATE");
		log.debug(sql);
		try{
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			ArrayList<AnswerVO> datas = new ArrayList<AnswerVO>();
			while (rs != null && rs.next()) {
				AnswerVO vo = new AnswerVO();
				setProperties(vo, rs);
				datas.add(vo);
			}
			
			qs.datas = datas;
			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
		return qs;
	}
	
	public ValueObject update(ValueObject vo) throws Exception {
		AnswerVO answer = (AnswerVO) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE " + getFullTableName(tableName)
				+ " SET ANSWER=?,TOTAL=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, answer.getAnswer());
			stmt.setInt(2, answer.getAccount());
			stmt.setString(3, answer.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}
	
	void setProperties(AnswerVO answer, ResultSet rs) throws Exception {
		try {
			answer.setId(rs.getString("ID"));
			answer.setUserId(rs.getString("USER_ID"));
			answer.setQuestionnaire_id(rs.getString("QUESTIONNAIRE_ID"));
			answer.setAnswer(rs.getString("ANSWER"));
			answer.setDate(rs.getTimestamp("ANSWER_DATE"));
			answer.setStatus(rs.getInt("STATUS"));
			answer.setTitle(rs.getString("TITLE"));
			answer.setUserName(rs.getString("USER_NAME"));
			answer.setUserDepartment(rs.getString("USER_DEPARTMENT"));
			answer.setContent(rs.getString("Q_CONTENT"));
			answer.setTotalScore(rs.getInt("SCORE"));
			answer.setAccount(rs.getInt("TOTAL"));
			answer.setExplains(rs.getString("EXPLAINS"));
		} catch (SQLException e) {
			throw e;
		}
	}
	
	/**
	 * 添加排序
	 * 
	 * @param sql
	 * @param field
	 * @return
	 */
	protected String bulidOrderString(String sql,String field) {
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
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}
	
	protected abstract String buildLimitString(String sql, int page, int lines,String orderbyField,String orderbyMode) throws SQLException;
	
	public static void main(String[] arg) throws Exception{
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
	 * 通过questionnaireID 找相对应的 answerID
	 */
	@Deprecated
	public String answerIDByQuestionnaireID(String questionnaireID) throws Exception{
		String sql="SELECT DISTINCT A.ID FROM OBPM.QM_ANSWER A,OBPM.QM_QUESTIONNAIRE B WHERE A.QUESTIONNAIRE_ID=?";
		PreparedStatement stmt = null;
		String answerID=null;
		log.debug(sql);
		try{
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, questionnaireID);
			ResultSet rs = stmt.executeQuery();
			while (rs != null && rs.next()) {
				answerID=rs.getString("ID");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
		return answerID;
	}

	/**
	 * 获取某用户的问卷答案
	 */
	public String answerIDByQuestionnaireID(String questionnaireID,WebUser user) throws Exception{
		String sql="SELECT DISTINCT A.ID FROM OBPM.QM_ANSWER A,OBPM.QM_QUESTIONNAIRE B WHERE A.USER_ID='"+user.getId()+"' AND A.QUESTIONNAIRE_ID=?";
		PreparedStatement stmt = null;
		String answerID=null;
		log.debug(sql);
		try{
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, questionnaireID);
			ResultSet rs = stmt.executeQuery();
			while (rs != null && rs.next()) {
				answerID=rs.getString("ID");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
		return answerID;
	}
	
	/**
	 * 获取某用户的问卷答案
	 * @param questionnaireId
	 * 问卷ID
	 * @param user
	 * 用户对象
	 * @return
	 * 返回问卷答案对象
	 * @throws Exception
	 */
	public AnswerVO findByQuestionnaireIdAndUserId(String questionnaireId, WebUser user) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT A.*,B.TITLE,B.EXPLAINS,B.SCORE,B.Q_CONTENT FROM " + getFullTableName(tableName) +" A,"+getFullTableName("QM_QUESTIONNAIRE")+" B where A.QUESTIONNAIRE_ID = B.ID AND B.ID=? AND A.USER_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, questionnaireId);
			stmt.setString(2, user.getId());

			ResultSet rs = stmt.executeQuery();
			AnswerVO answer = null;
			if (rs.next()) {
				answer = new AnswerVO();
				setProperties(answer, rs);
			}
			rs.close();
			return answer;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	/**
	 * 
	 * 个人答卷查询
	 * @param user
	 * @param s_title
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AnswerVO> findInputReport(String questionnaireId,int page,int lines) throws Exception{

		DataPackage<AnswerVO> answerVO = new DataPackage<AnswerVO>();
		String	sql = "SELECT A.*,B.TITLE,B.EXPLAINS,B.SCORE,B.Q_CONTENT FROM " + getFullTableName(tableName) +" A,"+getFullTableName("QM_QUESTIONNAIRE")+" B WHERE A.QUESTIONNAIRE_ID=B.ID AND A.QUESTIONNAIRE_ID='"+questionnaireId+"'";
		
		PreparedStatement stmt = null;
		sql = bulidOrderString(sql, "ANSWER_DATE");
		String limitSql = buildLimitString(sql, page, lines,"ANSWER_DATE","DESC");
		log.debug(sql);
		try{
			stmt = connection.prepareStatement(limitSql);
			ResultSet rs = stmt.executeQuery();
			ArrayList<AnswerVO> datas = new ArrayList<AnswerVO>();
			while (rs != null && rs.next()) {
				AnswerVO vo = new AnswerVO();
				setProperties(vo, rs);
				datas.add(vo);
			}
			
			answerVO.datas = datas;
			Long rowCount = countBySQL(sql);
			answerVO.setRowCount(rowCount.intValue());
			answerVO.linesPerPage = lines;
			answerVO.pageNo = page;
			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			ConnectionManager.closeStatement(stmt);
		}
		return answerVO;
	}
}
