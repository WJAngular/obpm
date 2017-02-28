package cn.myapps.km.baike.category.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.myapps.km.baike.category.ejb.Category;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.util.BaikeUtils;

public class DB2CategoryDAO extends AbstractCategoryDAO implements CategoryDAO {

	public DB2CategoryDAO(Connection conn) throws Exception {
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
	 * 根据词条ID查询词条的分类
	 * @param entryId
	 * @throws Exception
	 */
	public Category findByEntryId(String entryId) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT  CATEGORY.* FROM " 
				+ getFullTableName("BAIKE_CATEGORY") 
				+ " CATEGORY, "
				+ getFullTableName("BAIKE_ENTRY") 
				+ " ENTRY WHERE CATEGORY.ID = ENTRY.CATEGORYID AND ENTRY.ID=? FETCH FIRST 1 ROWS ONLY";
		log.info(sql);
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Category category = setPropety(rs);
				rs.close();
				return category;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 根据词条ID分页查询词条的分类
	 * @param entryId 
	 * 				词条ID
	 * @param page
	 * 				页码
	 * @param lines
	 * 				每页显示条数
	 * @throws Exception
	 */
	public DataPackage<Category> queryCategoryByEntryId(String entryId,
			int page, int lines) throws Exception {
		
		DataPackage<Category> result = new DataPackage<Category>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		//设置分页信息
		String rowCountsql = "SELECT CATEGORY.* FROM " 
				+ getFullTableName("BAIKE_CATEGORY") 
				+ " CATEGORY, "
				+ getFullTableName("BAIKE_ENTRY") 
				+ " ENTRY WHERE CATEGORY.ID = ENTRY.CATEGORYID AND ENTRY.ID='" + entryId + "'";
		 
		result.rowCount = getTotalLines(rowCountsql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}

		String sql = "SELECT CATEGORY.* FROM " 
				+ getFullTableName("BAIKE_CATEGORY") 
				+ " CATEGORY LEFT JOIN "
				+ getFullTableName("BAIKE_ENTRY") 
				+ " ENTRY ON CATEGORY.ID = ENTRY.CATEGORYID AND ENTRY.ID=?";
		sql = buildPagingString(sql, page, lines, "ORDERBY", "ASC");
		log.info(sql);
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			rs = stmt.executeQuery();
			
			while(rs != null && rs.next()){
				Category category = setPropety(rs);
				result.getDatas().add(category);
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
	 * 根据词条ID查询词条的分类集
	 * @param entryId
	 * 				词条ID
	 * @throws Exception
	 */
	public Collection<Category> queryCategoryByEntryId(String entryId)
			throws Exception {
		// TODO Auto-generated method stub
		return queryCategoryByEntryId(entryId, 1, Integer.MAX_VALUE).datas;
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

	public Collection<Category> query() throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		List<Category> list = new ArrayList<Category>();
		
		String sql = "SELECT * FROM "
				+ getFullTableName("BAIKE_CATEGORY") +" ORDER BY ORDERBY";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs != null && rs.next()){
				Category category = setPropety(rs);
				list.add(category);
			}
			rs.close();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}

}