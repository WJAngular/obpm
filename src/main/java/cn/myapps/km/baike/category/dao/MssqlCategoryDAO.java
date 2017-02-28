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

public class MssqlCategoryDAO extends AbstractCategoryDAO implements CategoryDAO {

	public MssqlCategoryDAO(Connection conn) throws Exception {
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
	 * 根据词条ID查询词条的分类
	 * @param entryId
	 * @throws Exception
	 */
	public Category findByEntryId(String entryId) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT TOP 1 CATEGORY.* FROM " 
				+ getFullTableName("BAIKE_CATEGORY") 
				+ " CATEGORY, "
				+ getFullTableName("BAIKE_ENTRY") 
				+ " ENTRY WHERE CATEGORY.ID = ENTRY.CATEGORYID AND ENTRY.ID=?";
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
