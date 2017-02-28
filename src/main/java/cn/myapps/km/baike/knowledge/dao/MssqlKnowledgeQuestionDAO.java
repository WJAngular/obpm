package cn.myapps.km.baike.knowledge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestion;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.util.BaikeUtils;


/**
 * 
 * @author abel
 */

public class MssqlKnowledgeQuestionDAO extends AbstractKnowledgeQuestionDAO implements KnowledgeQuestionDAO{
	
	
	public MssqlKnowledgeQuestionDAO(Connection conn) throws Exception {
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
	
	/**
	 * 查询所有问题
	 */
	public DataPackage<KnowledgeQuestion> queryAllQuestion(int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<KnowledgeQuestion> result = new DataPackage<KnowledgeQuestion>();
		result.getDatas().clear();
		
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION");
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION");
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setProperty(rs));
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
	 * 根据问题标题查找词条
	 * @param name
	 * 			词条名称
	 * @throws Expception
	 */
	public KnowledgeQuestion findByName(String title) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT TOP 1 * FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION") +" WHERE TITLE=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, title);
			ResultSet rs = stmt.executeQuery();
		
			if (rs.next()) {
				KnowledgeQuestion question = setProperty(rs);;
				rs.close();
				return question;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 *所有问题个数 
	 */
	public int getQuestionCounts() throws Exception{
		PreparedStatement stmt = null;
		int rowsCount=0;
		String sql="SELECT COUNT(*) TOTALCOUNT FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				rowsCount=rs .getInt("TOTALCOUNT");
			}
			rs.close();
	
		} catch (Exception e) {
			throw e;
			
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		return rowsCount;
	}	

	/**
	 *所有已解决问题个数 
	 */
	public int getAcceptAnswerCounts() throws Exception{
		PreparedStatement stmt = null;
		int rowsCount=0;
		String sql="SELECT COUNT(*) TOTALCOUNT FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEANSWER")+" ANSWER WHERE ANSWER.STATE = 'ACCEPT'";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				rowsCount=rs .getInt("TOTALCOUNT");
			}
			rs.close();
	
		} catch (Exception e) {
			throw e;
			
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		return rowsCount;
	}	

	public void queryQuestionById(String id) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}




