package cn.myapps.km.baike.knowledge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswer;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestion;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;


/**
 * 
 * @author abel
 */

public class DB2KnowledgeAnswerDAO extends AbstractKnowledgeAnswerDAO implements KnowledgeAnswerDAO{
	
	
	public DB2KnowledgeAnswerDAO(Connection conn) throws Exception {
		super(conn);
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getUserName().trim()
						.toUpperCase();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	/**
	 * 生成分页sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @return 生成限制条件sql语句字符串
	 * @throws SQLException
	 */
	public String buildPagingString(String sql, int page, int lines, String orderbyColumn, String orderbyMode)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			if(sql.toUpperCase().indexOf("WITH UR")>0){
				return sql;
			}
			return sql + " WITH UR";
		}
		// Modify by James:2010-01-03, fixed page divide error.
		// int from = (page - 1) * lines;
		int from = (page - 1) * lines + 1;

		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("Select * from (select row_.*, rownumber() over(");
		//if (orderby != null && !orderby.trim().equals(""))
			//pagingSelect.append(orderby);
		pagingSelect.append(" ) AS rown from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS row_) AS rows_ where rows_.rown BETWEEN ");
		pagingSelect.append(from);
		pagingSelect.append(" AND ");
		pagingSelect.append(to);
		if(pagingSelect.toString().toUpperCase().indexOf("WITH UR")==-1){
			 pagingSelect.append(" WITH UR");
		}
		return pagingSelect.toString();
	
	}

	public void update(NObject vo) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 通过ID查询答案
	 */
	public Collection<KnowledgeAnswer> queryAnswerById(String questionId) throws Exception {
		PreparedStatement stmt = null;
		List<KnowledgeAnswer> result = new ArrayList<KnowledgeAnswer>();
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEANSWER")+" WHERE QUESTIONID = ? AND STATE <> 'ACCEPT' ORDER BY SUBMITTIME DESC";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, questionId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.add(setProperty(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 采纳答案
	 */
	public void acceptAnswer(String id) throws Exception {
		PreparedStatement stmt = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_KNOWLEDGEANSWER") + " SET STATE=?,SUBMITTIME=? WHERE ID=?";	
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, KnowledgeAnswer.STATE_ACCEPT);
			stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
			stmt.setString(3, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		
	}

	/**
	 * 获取通过的答案
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public KnowledgeAnswer queryAcceptAnswerById(String id) throws Exception {
		PreparedStatement stmt = null;
		KnowledgeAnswer answer=null;
		String sql="SELECT   * FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEANSWER")+" WHERE QUESTIONID =? AND STATE='ACCEPT' ";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			//stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				answer= setProperty(rs);
			}
			rs.close();
	
		} catch (Exception e) {
			throw e;
			
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		return answer;
	}

	
}




