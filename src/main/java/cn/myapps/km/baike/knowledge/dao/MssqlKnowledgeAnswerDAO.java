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

public class MssqlKnowledgeAnswerDAO extends AbstractKnowledgeAnswerDAO implements KnowledgeAnswerDAO{
	
	
	public MssqlKnowledgeAnswerDAO(Connection conn) throws Exception {
		super(conn);
		
		dbTag = "MS SQL SERVER: ";
		try {
			ResultSet rs = conn.getMetaData().getSchemas();
			if (rs != null) {
				if (rs.next())
					this.schema = rs.getString(1).trim().toUpperCase();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
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
			return sql;
		}
		
		//若排序规则为空， 按升序排序
		if (orderbyMode==null || orderbyMode.trim().length()<=0) {
			orderbyMode = "ASC";
		}

		// int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);
		
		int databaseVersion = connection.getMetaData()
				.getDatabaseMajorVersion();
				
		if (orderbyColumn!=null && orderbyColumn.trim().length()>0) {   //若存在orderby 字段
			if (9 <= databaseVersion) {// 2005 row_number() over () 分页
				pagingSelect.append("SELECT TOP " + lines + " * FROM (");
				pagingSelect.append("SELECT ROW_NUMBER() OVER (ORDER BY ").append(orderbyColumn).append(" ").append(orderbyMode).append(") AS ROWNUMBER, TABNIC.* FROM (");
				pagingSelect.append(sql);
				pagingSelect.append(") TABNIC) TableNickname ");
				pagingSelect.append("WHERE ROWNUMBER>" + lines * (page - 1));

			} else {
				pagingSelect.append("SELECT TOP " + lines + " * FROM (");
				pagingSelect.append(sql).append(") TAB1 WHERE ID NOT IN (SELECT TOP " + (page-1)*lines + " ID FROM (");
				pagingSelect.append(sql).append(") TAB2 ORDER BY ").append(orderbyColumn).append(" ").append(orderbyMode);
				pagingSelect.append(") ORDER BY ").append(orderbyColumn).append(" ").append(orderbyMode);
			}
		}	else {
			pagingSelect.append("SELECT TOP " + lines + " * FROM (");
			pagingSelect.append(sql).append(") TAB1 WHERE ID NOT IN (SELECT TOP " + (page-1)*lines + " ID FROM (");
			pagingSelect.append(sql).append(") TAB2)");
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




