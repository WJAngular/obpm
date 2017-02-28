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

public class OracleKnowledgeQuestionDAO extends AbstractKnowledgeQuestionDAO implements KnowledgeQuestionDAO{
	
	
	public OracleKnowledgeQuestionDAO(Connection conn) throws Exception {
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
			return sql;
		}
		//若排序规则为空， 按升序排序
		if (orderbyMode==null || orderbyMode.trim().length()<=0) {
			orderbyMode = "ASC";
		}
		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect
				.append("select *  FROM  ( select row_.*, rownum rownum_  FROM  ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		// if (orderby != null && !orderby.trim().equals(""))
		// pagingSelect.append(orderby);

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
		String sql = "SELECT * FROM "+getFullTableName("BAIKE_ENTRY_KNOWLEDGEQUESTION") +" WHERE TITLE=? ADN ROWNUM <= 1";
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




