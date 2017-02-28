package cn.myapps.km.baike.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.entry.dao.AbstractEntryDAO;
import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.user.ejb.BUserEntrySet;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.util.BaikeUtils;
import cn.myapps.km.util.PersistenceUtils;

/**
 * 
 * @author dragon
 *  MS SQL的连接
 * 
 */
public class MySqlBUserEntrySetDAO extends AbstractBUserEntrySetDAO implements
		BUserEntrySetDAO {
	public MySqlBUserEntrySetDAO(Connection conn) throws Exception {
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
	 * 统计用户分享词条数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public long countSharedEntry(String userId) throws Exception {
		return countEntryByUserId(userId, BUserEntrySet.TYPE_SHARE);
	}
	
	/**
	 * 统计用户收藏词条数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public long countFavoriteEntry(String userId) throws Exception {
		return countEntryByUserId(userId, BUserEntrySet.TYPE_FAVORITE);
	}
	
	/**
	 * 统计用户相关联词条次数
	 * @param bUser_Id
	 * @return
	 * @throws Exception
	 */
	public long countEntryByUserId(String userId, String type) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM " + getFullTableName("BAIKE_USER_ENTRY_SET") + " WHERE USERID=? AND TYPE=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, type);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
		return 0;
	}
	
	/**
	 * 分页查找当前登录用户收藏词条表(我的收藏)
	 * @param page  当前页
	 * @param lines  每页显示行数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> getPersonalFavorites(int page,int lines,String userId) throws Exception {
				
		return queryByUserIdAndType(page, lines, userId, BUserEntrySet.TYPE_FAVORITE);
	}
	
	/**
	 * 分页查找当前登录用户收藏词条表(我的分享)
	 * @param page  当前页
	 * @param lines  每页显示行数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> getPersonalShares(int page,int lines,String userId) throws Exception {
		return queryByUserIdAndType(page, lines, userId, BUserEntrySet.TYPE_SHARE);
	}
	
	/**
	 * 分页查找当前登录用户收藏词条(我的分享或者收藏)
	 * @param page  当前页
	 * @param lines  每页显示行数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByUserIdAndType(int page,int lines,String userId,String type) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		
		//设置分页信息(词条表总行数)
		String lineshql="SELECT * FROM (SELECT * FROM " +getFullTableName("BAIKE_ENTRY")+" WHERE ID IN (SELECT ENTRYID AS ROW_COUNT FROM " +getFullTableName("BAIKE_USER_ENTRY_SET")+" WHERE TYPE = '"+type+"' AND USERID =  '"+userId+"')) TT";
		result.rowCount = getTotalLines(lineshql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		//设置每页数据集
		String sql = "SELECT * FROM "+getFullTableName("BAIKE_ENTRY") +" WHERE ID IN (SELECT ENTRYID FROM "+getFullTableName("BAIKE_USER_ENTRY_SET") +" WHERE TYPE=? AND USERID=?)";
		
		sql = buildPagingString(sql, page, lines, null, null);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, type);
			stmt.setString(2, userId);
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
	 * 统计词条被收藏或者分享次数
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public long countBeingFavorites(String entryId, String type) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM " + getFullTableName("BAIKE_USER_ENTRY_SET") + " WHERE ENTRYID=? AND TYPE=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			stmt.setString(2, type);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
		return 0;
	}


	/**
	 * 词条被收藏次数
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public long countBeingFavorite(String entryId) throws Exception {
		return countBeingFavorites(entryId,BUserEntrySet.TYPE_FAVORITE);
	}
	
	/**
	 * 词条被分享次数
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public long countBeingShare(String entryId) throws Exception {
		return countBeingFavorites(entryId,BUserEntrySet.TYPE_SHARE);
	}

	/**
	 * 查询词条 被分享||收藏的用户
	 * @param page
	 * @param lines
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public DataPackage<String> queryByEntryIdAndType(int page, int lines,String entryId, String type) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<String> result = new DataPackage<String>();
		
				//设置分页信息
				String lineshql="SELECT * FROM "+getFullTableName("BAIKE_USER_ENTRY_SET") + " WHERE TYPE = '" + type + "' AND ENTRYID ='" + entryId + "'"; 
				result.rowCount = getTotalLines(lineshql);
				result.pageNo = page;
				result.linesPerPage = lines;
				
				//当页码大于总页数
				if (result.pageNo > result.getPageCount()) {
					result.pageNo = 1;
					page = 1;
				}
				
		String sql ="SELECT * FROM " + getFullTableName("BAIKE_USER_ENTRY_SET") + " WHERE TYPE=? AND ENTRYID=?";
		
		sql = buildPagingString(sql, page, lines, null, null);
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, type);
			stmt.setString(2, entryId);
			
			stmt.setString(3, type);
			stmt.setString(4, entryId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs).getUserId());
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
	 * 获取词条被分享次数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<String> getSharedUsers(int page, int lines,
			String entryId) throws Exception {
		return queryByEntryIdAndType(page, lines, entryId, BUserEntrySet.TYPE_SHARE);
	}
	
	/**
	 * 获取词条被收藏次数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<String> getFavoritedUsers(int page, int lines,
			String entryId) throws Exception {
		return queryByEntryIdAndType(page, lines, entryId, BUserEntrySet.TYPE_FAVORITE);
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
	 * 根据修改人查询我的贡献
	 */
	public DataPackage<Entry> queryByAuthor(int page, int lines, String author ,String state) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String lineshql="SELECT * FROM " +getFullTableName("BAIKE_ENTRY")+" WHERE ID IN (SELECT ENTRYID FROM " +getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE STATE ='"+state+"' AND AUTHOR = '"+author+"')"; 
		result.rowCount = getTotalLines(lineshql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql = "SELECT * FROM " +getFullTableName("BAIKE_ENTRY")+" ENTRY WHERE ID IN (SELECT ENTRYID FROM " +getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE STATE = ? AND AUTHOR = ?)";
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, state);
			stmt.setString(2, author);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(AbstractEntryDAO.setPropety(rs));
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
	 * 判断用户是否已经收藏这个词条
	 * @param entrySet
	 * @return
	 * @throws Exception
	 */
	public boolean isFavoritesEntry(BUserEntrySet bUserEntrySet) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<BUserEntrySet> result = new DataPackage<BUserEntrySet>();
		String sql ="SELECT * FROM " + getFullTableName("BAIKE_USER_ENTRY_SET") + " WHERE USERID= '"+ bUserEntrySet.getUserId()+"'AND TYPE='"+ BUserEntrySet.TYPE_FAVORITE+"' AND ENTRYID='"+bUserEntrySet.getEntryId()+"'";
		try {
			result.rowCount = getTotalLines(sql);
			if(result.rowCount>0){
				return true;	
			}else{
				return false;
			}
			
			
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	public int doFavoriteCount(String entryId) throws Exception {
		PreparedStatement stmt = null;
		int rowsCount=0;
		String sql="SELECT COUNT(*)  TOTALCOUNT FROM "+getFullTableName("BAIKE_USER_ENTRY_SET")+"  WHERE  ENTRYID=? AND TYPE='FAVORITE'";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
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
	 * 判断是否为管理员
	 */
	public boolean isPublicDiskAdmin(String userId,int r_level) throws Exception {
		PreparedStatement stmt = null;
		long rows = 0;
		boolean flag = false;
		String dabaSourceName = BaikeUtils.getKmDataSourceName();
		String sql = "SELECT COUNT(*) AS ROW_COUNT FROM " +dabaSourceName+".KM_USER_ROLE_SET A WHERE A.USERID=? AND A.ROLEID IN (SELECT ID FROM "+dabaSourceName+".KM_ROLE B WHERE B.R_LEVEL=?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setInt(2, r_level);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				rows=rs.getLong("ROW_COUNT");
			}
			if(rows>=1){
				flag = true;
			}
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
		return flag;
	}

	public DataPackage<Entry> getNotThroungh(int page,int lines,String userId) throws Exception{
		PreparedStatement stmt = null;
		DataPackage<Entry> result = new DataPackage<Entry>();
		//设置分页信息
		String rowCounthql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID = CONTENT.ENTRYID AND CONTENT.AUTHOR = '"+userId+"' AND CONTENT.STATE <> 'PASSED' "; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		//设置每页数据集     没有排序的时候参数设置两遍
		String sql="SELECT DISTINCT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE ENTRY.ID = CONTENT.ENTRYID AND CONTENT.AUTHOR = ? AND CONTENT.STATE <> 'PASSED' ";
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
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
}
