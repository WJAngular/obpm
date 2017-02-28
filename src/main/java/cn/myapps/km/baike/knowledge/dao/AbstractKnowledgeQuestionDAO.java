package cn.myapps.km.baike.knowledge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestion;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * 
 * @author Abel
 *内容DAO层的抽象类
 */
public abstract class AbstractKnowledgeQuestionDAO extends AbstractBaseDAO{
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractKnowledgeQuestionDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	public int setParameters(PreparedStatement statement, KnowledgeQuestion knowledgeQuestion)
			throws SQLException {
		int index = 1;
		statement.setString(index++, knowledgeQuestion.getId());
		statement.setString(index++, knowledgeQuestion.getCategoryId());
		statement.setString(index++, knowledgeQuestion.getAuthor().getId());
		statement.setString(index++, knowledgeQuestion.getTitle());	
		statement.setString(index++, knowledgeQuestion.getContent());
		statement.setTimestamp(index++, new Timestamp(knowledgeQuestion.getCreated().getTime()));
		statement.setInt(index++,knowledgeQuestion.getPoint());
		return index;
	}
	
	/**
	 * 创建问题
	 */
	public void create(NObject vo) throws Exception {
		KnowledgeQuestion knowledgeQuestion = (KnowledgeQuestion) vo;
		PreparedStatement statement = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION") + " (ID,CATEGORYID,AUTHOR,TITLE,CONTENT,CREATED,POINT) values (?,?,?,?,?,?,?)";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, knowledgeQuestion);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			closeStatement(statement);
		}
	}
	
	/**
	 * 修改内容
	 */
	public void update(NObject vo) throws Exception {
		KnowledgeQuestion knowledgeQuestion = (KnowledgeQuestion) vo;
		PreparedStatement statement = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_KNOWLEDGE") + " SET ID=?,AUTHOR=?,TITLE=?,CONTENT=?,CREATE=? WHERE ID=?";	
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			int index = setParameters(statement, knowledgeQuestion);
			statement.setString(index, knowledgeQuestion.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(statement);
		}
	}

	/**
	 * 通过Id查找内容
	 */
	public KnowledgeQuestion find(String id) throws Exception {
		PreparedStatement stmt = null;
		KnowledgeQuestion knowledgeQuestion = new KnowledgeQuestion();
		ResultSet rs = null;		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);		
			rs = stmt.executeQuery();
			if(rs.next()){
				this.setProperty(knowledgeQuestion, rs);
			} else {
				return null;
			}
			return knowledgeQuestion;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			BaikeUtils.closeStatement(stmt);
		}
	}
	

	/**
	 * 通过Id进行删除
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			BaikeUtils.closeStatement(stmt);
		}
	}

	public KnowledgeQuestion setProperty(ResultSet rs) throws Exception{
		KnowledgeQuestion knowledgeQuestion=new KnowledgeQuestion();
		knowledgeQuestion.setId(rs.getString("ID"));
		knowledgeQuestion.setAuthor(rs.getString("AUTHOR"));
		knowledgeQuestion.setContent(rs.getString("CONTENT"));
		knowledgeQuestion.setTitle(rs.getString("TITLE"));
		knowledgeQuestion.setCreated(new Date(rs.getTimestamp("CREATED").getTime()));
		knowledgeQuestion.setPoint(rs.getInt("POINT"));
		return knowledgeQuestion;
	}
	

	private void setProperty(KnowledgeQuestion knowledgeQuestion, ResultSet rs) throws Exception{
		knowledgeQuestion.setId(rs.getString("ID"));
		knowledgeQuestion.setAuthor(rs.getString("AUTHOR"));
		knowledgeQuestion.setContent(rs.getString("CONTENT"));
		knowledgeQuestion.setTitle(rs.getString("TITLE"));
		knowledgeQuestion.setCreated(new Date(rs.getTimestamp("CREATED").getTime()));
		knowledgeQuestion.setPoint(rs.getInt("POINT"));
	}
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}

	public int getTotalLines(String sql) throws Exception {
		PreparedStatement stmt = null;
		Long amount = Long.valueOf(0);
		int from = sql.toUpperCase().indexOf("FROM");
		int order = sql.toUpperCase().indexOf("ORDER BY");
		String newsql = (order > 0) ? "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from, order)
				: "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from);
		
		try{stmt=connection.prepareStatement(newsql);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()){
			amount=rs.getLong("ROW_COUNT");
			rs.close();
			return amount.intValue();
		}
		}catch(Exception e){
			
		}finally{
			
		}

		return 0;
	}
	
	private void closeStatement(PreparedStatement stmt) throws SQLException{
		if(stmt!=null){
				stmt.close();
		}
	} 

	
}
