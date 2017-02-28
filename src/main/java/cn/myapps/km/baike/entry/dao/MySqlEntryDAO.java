package cn.myapps.km.baike.entry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.util.BaikeUtils;

/**CT
 * @author Allen
 * SQLSERVER实现类
 * 
 */
public class MySqlEntryDAO extends AbstractEntryDAO implements EntryDao {

	public MySqlEntryDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MY SQL: ";
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
	 * 增加词条浏览次数
	 * @param entryId
	 *			词条ID
	 * @throws Exception
	 */
	public void addBrowserCount(String entryId) throws Exception {
		PreparedStatement stmt = null;
																									
		String sql = "UPDATE "+getFullTableName("BAIKE_ENTRY") +" SET BROWSE_COUNT=BROWSE_COUNT+1 WHERE ID=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		
	}
	
	/**
	 * 积分排行
	 */
	public DataPackage<Entry> orderByPoint(int page,int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY")+" ORDER BY POINTS DESC "; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
		
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//				+" ORDER BY CREATED DESC ) AS T ORDER BY CREATED ASC ) AS T1 ORDER BY CREATED DESC";
//		
//		log.info(sql);
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") ;
		sql = buildPagingString(sql, page, lines, "POINTS", "DESC");
		log.info(sql);
		
		try {
			
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 查找最新词条
	 */
	public DataPackage<Entry> queryByCreated(int page,int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY")+" ORDER BY CREATED DESC "; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
		
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//				+" ORDER BY CREATED DESC ) AS T ORDER BY CREATED ASC ) AS T1 ORDER BY CREATED DESC";
//		
//		log.info(sql);
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") ;
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		log.info(sql);
		
		try {
			
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 查找指定分类下的最新词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByCreated(String categoryId, int page,int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM (SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.CATEGORYID='"+categoryId+"' AND CONTENT.ENTRYID=ENTRY.ID AND STATE='PASSED'  ) TB" ; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
		
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//				+" ORDER BY CREATED DESC ) AS T ORDER BY CREATED ASC ) AS T1 ORDER BY CREATED DESC";
//		
//		log.info(sql);
		
		String sql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.CATEGORYID=? AND CONTENT.STATE='PASSED' AND ENTRY.ID=CONTENT.ENTRYID" ;
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		log.info(sql);
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, categoryId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 分页查找热点词条
	 */
	public DataPackage<Entry> queryByBrowseCount(int page,int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY")+" ORDER BY BROWSE_COUNT DESC "; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//										+" ORDER BY BROWSE_COUNT DESC ) AS T ORDER BY BROWSE_COUNT ASC )AS T1 ORDER BY BROWSE_COUNT DESC";

		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") ;
		sql = buildPagingString(sql, page, lines, "BROWSE_COUNT", "DESC");
		log.info(sql);
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 根据词条类别查找该类别下的热点词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByBrowseCount(String categoryId, int page,int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM (SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.CATEGORYID='"+categoryId+"' AND CONTENT.ENTRYID=ENTRY.ID AND STATE='PASSED'  ) TB" ; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//										+" ORDER BY BROWSE_COUNT DESC ) AS T ORDER BY BROWSE_COUNT ASC )AS T1 ORDER BY BROWSE_COUNT DESC";

		
		String sql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY") +" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.CATEGORYID=? AND ENTRY.ID=CONTENT.ENTRYID AND STATE='PASSED'" ;
		sql = buildPagingString(sql, page, lines, "BROWSE_COUNT", "DESC");
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, categoryId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 查询贡献者
	 */
	public DataPackage<Entry> queryContributorByCreated(String categoryId, String userId,int page,int lines) throws Exception{
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM (SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.CATEGORYID='"+categoryId+"' AND ENTRY.AUTHOR='"+userId+"'  AND CONTENT.ENTRYID=ENTRY.ID AND STATE='PASSED'  ) TB" ; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//										+" ORDER BY BROWSE_COUNT DESC ) AS T ORDER BY BROWSE_COUNT ASC )AS T1 ORDER BY BROWSE_COUNT DESC";

		
		String sql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY") +" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.CATEGORYID=? AND ENTRY.AUTHOR=? AND ENTRY.ID=CONTENT.ENTRYID AND STATE='PASSED'" ;
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, categoryId);
			stmt.setString(2, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 根据名字分页查找
	 */
	public DataPackage<Entry> queryByName(int page,int lines,String name) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE NAME LIKE '%"+name+"%' ORDER BY BROWSE_COUNT DESC"; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
		result.getDatas().clear();
	//	int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY")
//				+"  WHERE NAME LIKE  '%"+name+"%' ORDER BY BROWSE_COUNT DESC ) AS T ORDER BY BROWSE_COUNT ASC )AS T1 ORDER BY BROWSE_COUNT DESC";
//		log.info(sql);
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE NAME LIKE '%"+name+"%'";
		sql = buildPagingString(sql, page, lines, "BROWSE_COUNT", "DESC");
		log.info(sql);
		
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 根据名字查找
	 */
	public Collection<Entry> queryByName(String name) throws Exception {
		PreparedStatement stmt = null;
		Collection<Entry> result = new ArrayList<Entry>();
		result.clear();
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY")+" WHERE NAME LIKE '%"+name+"%' ORDER BY BROWSE_COUNT DESC ";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.add(setPropety(rs));
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
	 * 根据关键字查找相关词条
	 */
	public DataPackage<Entry> queryByKeyWord(int page, int lines, String keyWord)
			throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE KEYWORD LIKE '%"+keyWord+"%' ORDER BY BROWSE_COUNT DESC"; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
		//String sql = "SELECT TOP ? * FROM "+getFullTableName("BAIKE_ENTRY") +" WHERE ID IN (SELECT TOP ? ID FROM "+getFullTableName("BAIKE_Entry") +"WHERE LABLE LIKE '%"+name+"%'ORDER BY BROWSECOUNT DESC)";
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//				+" WHERE KEYWORD LIKE '%"+keyWord+"%' ORDER BY BROWSE_COUNT DESC )AS T ORDER BY BROWSE_COUNT ASC )AS T1 ORDER BY BROWSE_COUNT DESC";
//		log.info(sql);
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE KEYWORD LIKE '%"+keyWord+"%'";
		sql = buildPagingString(sql, page, lines, "BROWSE_COUNT", "DESC");
		log.info(sql);
		
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
	 * 根据关键字查找相关词条
	 */
	public Collection<Entry> queryByKeyWord(String keyWord)
			throws Exception {
		PreparedStatement stmt = null;
		Collection<Entry> result = new ArrayList<Entry>();

		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") 
				+" WHERE KEYWORD LIKE '%"+keyWord+"%' ORDER BY BROWSE_COUNT DESC";
		log.info(sql);
		
		
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.add(setPropety(rs));
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
	 * 根据分类查找相关词条
	 */
	public DataPackage<Entry> queryByCategoryId(int page, int lines,String categoryId) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE CATEGORYID LIKE '%"+categoryId+"%' ORDER BY BROWSE_COUNT DESC"; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE CATEGORYID LIKE '%"+categoryId+"%'";
		sql = buildPagingString(sql, page, lines, "BROWSE_COUNT", "DESC");
		log.info(sql);
		
		
//		int count=page*lines;
//		String sql="SELECT * FROM ( SELECT TOP "+lines+" * FROM ( SELECT TOP "+count+" * FROM "+getFullTableName("BAIKE_ENTRY") 
//										+" WHERE CATEGORYID LIKE '"+categoryId+"' ORDER BY BROWSE_COUNT DESC )AS T ORDER BY BROWSE_COUNT ASC )AS T1 ORDER BY BROWSE_COUNT DESC";
//
//		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
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
		/*
		if (orderbyColumn!=null && orderbyColumn.trim().length()>0) {   //若存在orderby 字段
			
		}	else {
			
		}
		*/
		int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("SELECT * FROM (");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS TB LIMIT " + to + "," + lines);

		return pagingSelect.toString();
				
		
	}
	
	/**
	 * 根据用户查找词条.
	 */
	public DataPackage<Entry> findByUserId(int page, int lines,String userId) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE AUTHOR = '"+userId+"' "; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " WHERE AUTHOR = ? ";
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 根据用户及状态查找词条.
	 */
	public DataPackage<Entry> findPassedByUserId(int page, int lines,String userId) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID = CONTENT.ENTRYID AND CONTENT.AUTHOR = '"+userId+"' AND CONTENT.STATE = 'PASSED' "; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID = CONTENT.ENTRYID AND CONTENT.AUTHOR = ? AND CONTENT.STATE = 'PASSED' ";
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 通过用户ID和状态查询词条
	 */
	public DataPackage<Entry> queryByUserIdAndState(int page, int lines,
			String userId, String state) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") + " ENTRY," + getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID=CONTENT.ENTRYID AND ENTRY.AUTHOR = '"+userId+"' AND CONTENT.STATE='" + state + "'"; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql="SELECT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY") + " ENTRY," + getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID=CONTENT.ENTRYID AND ENTRY.AUTHOR = ? AND CONTENT.STATE= ?";
		sql = buildPagingString(sql, page, lines, null, null);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, state);
			stmt.setString(3, userId);
			stmt.setString(4, state);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 查询所有词条
	 */
	public DataPackage<Entry> queryAllEntry(int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY") ; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY");
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 查询已通过的词条
	 */
	public DataPackage<Entry> queryPasseedEntry(int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY ,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.ID=CONTENT.ENTRYID AND CONTENT.STATE = 'PASSED'" ; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY ,"+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE ENTRY.ID=CONTENT.ENTRYID AND CONTENT.STATE = 'PASSED'" ; 
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	

	
	/**
	 * 根据用户及状态查找词条.
	 */
	public DataPackage<Entry> queryDraftEntry(int page, int lines,String userId) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID = CONTENT.ENTRYID AND CONTENT.AUTHOR = '"+userId+"' AND CONTENT.STATE = 'DRAFT' "; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID = CONTENT.ENTRYID AND CONTENT.AUTHOR = ? AND CONTENT.STATE = 'DRAFT' ";
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 增加词条积分，加一分
	 * @param pk
	 * @throws Exception
	 */
	public void addPoint(String pk) throws Exception {
		this.addPoint(pk, 1);
	}

	/**
	 * 增加词条积分
	 * @param pk
	 * 			词条ID
	 * @param point
	 * 			增加积分
	 * @throws Expception
	 */
	public void addPoint(String pk, int point) throws Exception {
		PreparedStatement stmt = null;
																									
		String sql = "UPDATE "+getFullTableName("BAIKE_ENTRY") +" SET POINTS=POINTS+? WHERE ID=?";
		log.info(sql);
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, point);
			stmt.setString(2, pk);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		
	}

	/**
	 * 根据词条名称查找词条
	 * @param name
	 * 			词条名称
	 * @throws Expception
	 */
	public Entry findByName(String name) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM "+getFullTableName("BAIKE_ENTRY") +" WHERE NAME=? limit 0, 1";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
		
			if (rs.next()) {
				Entry entry = setPropety(rs);;
				rs.close();
				return entry;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 获取词条通过版本内容
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public Collection<Entry> doFindContributer(String entryId) throws Exception {
		PreparedStatement stmt = null;
		List<Entry> list=new ArrayList<Entry>();
		String sql="SELECT  * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT,"+getFullTableName("BAIKE_ENTRY")+" ENTRY WHERE ENTRY.ID =? AND ENTRY.ID=CONTENT.ENTRYID AND CONTENT.STATE='PASSED' ORDER BY HANDLETIME DESC limit 0, 5";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			//stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				
				list.add(setPropety(rs));
			}
			rs.close();
	
		} catch (Exception e) {
			throw e;
			
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		return list;
	}

	/**
	 * 通过用户ID查找用户中心
	 * @param userId
	 * @throws Exception
	 */
	public DataPackage<Entry> doFindUserCenterByUserId(String userId,int page,int lines) throws Exception{
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		String lineshql="SELECT ENTRY.* FROM " +getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+
																		" CONTENT,"+getFullTableName("BAIKE_USER_ATTRIBUTE")+
		" ATTRIBUTE WHERE ENTRY.ID=CONTENT.ENTRYID AND ATTRIBUTE.USERID=CONTENT.AUTHOR AND ATTRIBUTE.USERID=ENTRY.AUTHOR AND CONTENT.STATE='PASSED' AND ATTRIBUTE.USERID='"+userId+"'";
		result.rowCount = getTotalLines(lineshql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集
		String sql="SELECT  ENTRY.* FROM " +getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+
				" CONTENT,"+getFullTableName("BAIKE_USER_ATTRIBUTE")+
" ATTRIBUTE WHERE ENTRY.ID=CONTENT.ENTRYID AND ATTRIBUTE.USERID=CONTENT.AUTHOR AND ATTRIBUTE.USERID=ENTRY.AUTHOR AND CONTENT.STATE='PASSED' AND ATTRIBUTE.USERID=?";
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(AbstractEntryDAO.setPropety(rs));
			}
		
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(lineshql);
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	
	/**
	 * 分页查询部门积分榜
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Map> getScoreboardByDepartment(int page, int lines) throws Exception {
		String dabaSourceName = BaikeUtils.getSysDataSourceName();
		String sql = "SELECT TD.ID, TD.NAME, SUM(TD.INTEGRAL) AS TOTALINTEGRAL FROM  " 
					+ "("
					+ "SELECT BAIKE.USERID,T_DEPARTMENT.ID, T_DEPARTMENT.NAME, BAIKE.INTEGRAL "
					+ "FROM BAIKE_USER_ATTRIBUTE BAIKE, "+dabaSourceName+".T_USER,"+dabaSourceName+".T_DEPARTMENT" 
					+ " WHERE BAIKE.USERID=T_USER.ID AND T_USER.DEFAULTDEPARTMENT = T_DEPARTMENT.ID AND BAIKE.USERID <> 'DX0371' "
					+ ") TD " 
					+ " GROUP BY TD.ID, TD.NAME";
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderbyColumn", "TOTALINTEGRAL");
		params.setParameter("_orderbyMode", "DESC");
		return query(sql, params, page, lines);
	}
	
	/**
	 * 分页查询词条积分
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Map> getScoreboardByEntry(int page, int lines, String userId) throws Exception {
		String sql = "SELECT MAX(CREATED) AS CREATED, NAME, SUM(DEPLOY_POINT) AS DEPLOY_POINTS, SUM(MODIFY_POINT) AS MODIFY_POINTS, SUM(VOTE_POINT) AS VOTE_POINTS ,SUM(DEPLOY_POINT)+SUM(MODIFY_POINT)+SUM(VOTE_POINT) AS TOTAL FROM " 
					+ "("
					+ "SELECT A.*, B.NAME,B.CREATED, B.AUTHOR AS ENTRY_AUTHOR, (CASE WHEN A.AUTHOR=B.AUTHOR AND A.VERSIONNUM =1 THEN 5 ELSE 0 END) AS DEPLOY_POINT ,(CASE WHEN (A.AUTHOR<>B.AUTHOR OR (A.AUTHOR = B.AUTHOR AND A.VERSIONNUM>1)) THEN 1 ELSE 0 END) AS MODIFY_POINT, B.POINTS AS VOTE_POINT  "
					+ "FROM BAIKE_ENTRY_CONTENT A LEFT JOIN BAIKE_ENTRY  B ON A.ENTRYID = B.ID " 
					+ ") " 
					+" C WHERE C.AUTHOR = '"+userId+"' AND STATE = 'PASSED' GROUP BY NAME";
		ParamsTable params = new ParamsTable();
		/*params.setParameter("_orderbyColumn", "name");
		params.setParameter("_orderbyColumn", "deploy_points");
		params.setParameter("_orderbyColumn", "vote_points");*/
		params.setParameter("_orderbyColumn", "CREATED");
		return query(sql, params, page, lines);
	}
	
	public DataPackage<Map> query(String sql, ParamsTable params, int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Map> result = new DataPackage<Map>();
		result.getDatas().clear();
		
		//设置分页信息
		String rowCounthql="SELECT * FROM (" + sql + ") TB" ;
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		String _orderbyColumn = params.getParameterAsString("_orderbyColumn");
		String _orderbyMode = params.getParameterAsString("_orderbyMode");
		
		String newSql = buildPagingString(sql, page, lines, _orderbyColumn, _orderbyMode);
		log.info(newSql);
		try {
			stmt = connection.prepareStatement(newSql);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i=1; i<=metaData.getColumnCount(); i++) {
					int columnType = metaData.getColumnType(i);
					String columnName = metaData.getColumnName(i);
					Object columnValue = null;
					switch (columnType) {	
						case java.sql.Types.NCHAR:
						case java.sql.Types.VARCHAR:
							columnValue = rs.getString(i);
							break;
						case java.sql.Types.DATE:
							columnValue = rs.getDate(i);
							break;
						case java.sql.Types.DOUBLE:
							columnValue = rs.getDouble(i);
							break;	
						case java.sql.Types.INTEGER:
						case java.sql.Types.BIT:
							columnValue = rs.getInt(i);
							break;	
						default:
							columnValue = rs.getString(i);
							break;
					}
					map.put(columnName, columnValue);
				}
				result.getDatas().add(map);
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
	 * 获取修改积分
	 */
	public int getEditPoints(String userId,String entryId) throws Exception{
		PreparedStatement stmt = null;
		int rowsCount=0;
		String sql="SELECT COUNT(*) TOTALCOUNT FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE STATE = 'PASSED' AND VERSIONNUM >= 2 AND AUTHOR = ? AND ENTRYID = ? ";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, entryId);
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
	 *所有词条个数 
	 */
	public int getEntryCounts() throws Exception{
		PreparedStatement stmt = null;
		int rowsCount=0;
		String sql="SELECT COUNT(*) TOTALCOUNT FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT,"+getFullTableName("BAIKE_ENTRY")+" ENTRY WHERE CONTENT.STATE = 'PASSED' AND ENTRY.ID = CONTENT.ENTRYID";
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
	 * 浏览次数
	 * @return
	 * @throws Exception
	 */
	public int getReadCounts() throws Exception{
		PreparedStatement stmt = null;
		int rowsCount=0;
		String sql="SELECT SUM(BROWSE_COUNT) TOTALCOUNT FROM "+getFullTableName("BAIKE_ENTRY");
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
}