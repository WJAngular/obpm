package cn.myapps.km.baike.knowledge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswer;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.BaikeUtils;

/**
 * 
 * @author Abel
 *内容DAO层的抽象类
 */
public abstract class AbstractKnowledgeAnswerDAO extends AbstractBaseDAO{
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractKnowledgeAnswerDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	public int setParameters(PreparedStatement statement, KnowledgeAnswer knowledgeAnswer)
			throws SQLException {
		int index = 1;
		statement.setString(index++, knowledgeAnswer.getId());
		statement.setString(index++, knowledgeAnswer.getQuestionId());	
		statement.setString(index++, knowledgeAnswer.getAuthor().getId());
		statement.setString(index++, knowledgeAnswer.getContent());
		statement.setTimestamp(index++, new Timestamp(knowledgeAnswer.getSubmitTime().getTime()));
		statement.setString(index++, knowledgeAnswer.getState());
		return index;
	}
	
	/**
	 * 创建答案内容
	 */
	public void create(NObject vo) throws Exception {
		KnowledgeAnswer knowledgeAnswer = (KnowledgeAnswer) vo;
		PreparedStatement statement = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_ENTRY_KNOWLEDGEANSWER") + " (ID,QUESTIONID,AUTHOR,CONTENT,SUBMITTIME,STATE) values (?,?,?,?,?,?)";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, knowledgeAnswer);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			closeStatement(statement);
		}
	}
	
	
	/**
	 * 通过Id查找内容
	 */
	public KnowledgeAnswer find(String id) throws Exception {
		PreparedStatement stmt = null;
		KnowledgeAnswer knowledgeAnswer = new KnowledgeAnswer();
		ResultSet rs = null;		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_ENTRY_KNOWLEDGEANSWER") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);		
			rs = stmt.executeQuery();
			if(rs.next()){
				this.setProperty(knowledgeAnswer, rs);
			} else {
				return null;
			}
			return knowledgeAnswer;
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
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_KNOWLEDGEANSWER") + " WHERE ID=?";

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

	public KnowledgeAnswer setProperty(ResultSet rs) throws Exception{
		KnowledgeAnswer knowledgeAnswer=new KnowledgeAnswer();
		knowledgeAnswer.setId(rs.getString("ID"));
		knowledgeAnswer.setAuthor(rs.getString("AUTHOR"));
		knowledgeAnswer.setContent(rs.getString("CONTENT"));
		knowledgeAnswer.setSubmitTime(new Date(rs.getTimestamp("SUBMITTIME").getTime()));
		knowledgeAnswer.setState(rs.getString("STATE"));
		return knowledgeAnswer;
	}
	

	private void setProperty(KnowledgeAnswer knowledgeAnswer, ResultSet rs) throws Exception{
		knowledgeAnswer.setId(rs.getString("ID"));
		knowledgeAnswer.setQuestionId(rs.getString("QUESTIONID"));
		knowledgeAnswer.setAuthor(rs.getString("AUTHOR"));
		knowledgeAnswer.setContent(rs.getString("CONTENT"));
		knowledgeAnswer.setSubmitTime(new Date(rs.getTimestamp("SUBMITTIME").getTime()));
		knowledgeAnswer.setState(rs.getString("STATE"));
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
