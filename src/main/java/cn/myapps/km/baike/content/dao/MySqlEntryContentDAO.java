package cn.myapps.km.baike.content.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.BaikeUtils;


/**
 * 
 * @author abel
 */

public class MySqlEntryContentDAO extends AbstractEntryContentDAO implements EntryContentDAO{
	
	
	public MySqlEntryContentDAO(Connection conn) throws Exception {
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
	 * 获取词条版本内容
	 * @param entryId
	 * @param page
	 * @param rowCount
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> queryByEntryId(String entryId,int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<EntryContent> result = new DataPackage<EntryContent>();
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") +" WHERE ENTRYID= '" +entryId + "'";; 
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
			
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE ENTRYID = ?";
		sql = buildPagingString(sql, page, lines, "SAVETIME", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			//stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setProperty(rs));
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
	 * 获取词条最新版本内容
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public EntryContent getLatestVersionContent(String entryId) throws Exception {
		PreparedStatement stmt = null;
		EntryContent ec = new EntryContent();
		String sql="SELECT  * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE ENTRYID =? AND STATE ='" + EntryContent.STATE_PASSED + "' ORDER BY HANDLETIME DESC limit 0 , 1";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			//stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ec= setProperty(rs);
			}
			rs.close();
	
		} catch (Exception e) {
			throw e;
			
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		return ec;
	}
	
	/**
	 * 获取词条内容
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public EntryContent getContent(String entryId) throws Exception {
		PreparedStatement stmt = null;
		EntryContent ec = new EntryContent();
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE ENTRYID =?  ORDER BY HANDLETIME DESC limit 0 ,1";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			//stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ec= setProperty(rs);
			}
			rs.close();
	
		} catch (Exception e) {
			throw e;
			
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
		return ec;
	}
	
	/**
	 * 获取词条通过版本内容
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public Collection<EntryContent> queryByEntryIdAndPassedContent(String entryId) throws Exception {
		PreparedStatement stmt = null;
		List<EntryContent> list=new ArrayList<EntryContent>();
		String sql="SELECT  * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE ENTRYID =? AND STATE='PASSED' ORDER BY HANDLETIME DESC";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			//stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				
				list.add(setProperty(rs));
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
	 * 获取词条历史版本内容
	 * @param entryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> getHisVersionContent(String entryId, int page, int lines) throws Exception {
		return queryByEntryIdAndState(entryId, EntryContent.STATE_PASSED, page, lines);
	}
	
	/**
	 * 通过词条ID,词条状态分页查询词条所有版本内容
	 * @param entryId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> queryByEntryIdAndState(String entryId, String state, int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<EntryContent> result = new DataPackage<EntryContent>();
		
		//设置分页信息
				String rowCountsql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE ENTRYID= '" + entryId + "' AND STATE='" + state + "'";
				stmt = connection.prepareStatement(rowCountsql);
				result.rowCount = getTotalLines(rowCountsql);
				result.pageNo = page;
				result.linesPerPage = lines;
				if (result.pageNo > result.getPageCount()) {
					result.pageNo = 1;
					page = 1;
				}
				
		result.getDatas().clear();
		//String sql="SELECT * FROM (SELECT TOP "+lines+" * FROM(SELECT TOP "+page*lines+" * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") 
		//								+" WHERE ENTRYID = ? AND STATE = ? ORDER BY HANDLETIME DESC ) AS T ORDER BY HANDLETIME ASC )AS T1 ORDER BY HANDLETIME DESC";
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE ENTRYID = ?  AND STATE = ?";
		sql = buildPagingString(sql, page, lines, "HANDLETIME", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			stmt.setString(2, state);
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
	 * 获取当前百科用户草稿状态的词条内容
	 */
	public DataPackage<EntryContent> getUserDraftContent(NUser user, int page, int lines) throws Exception{
		return  queryByUserIdAndState(user.getId(), EntryContent.STATE_DRAFT, page, lines);
	}
	
	/**
	 * 获取草稿状态的内容
	 */
	public DataPackage<EntryContent> getUserDraftContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_DRAFT, page, lines);
	}
	
	/**
	 * 获取通过状态的内容
	 */
	public DataPackage<EntryContent> getUserPassedContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_PASSED, page, lines);
	}
	
	/**
	 * 获取通过状态的内容
	 */
	public DataPackage<EntryContent> getUserPassedContent(String categoryId, String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(categoryId, userId, EntryContent.STATE_PASSED, page, lines);
	}
	
	/**
	 * 获取提交状态的内容
	 */
	public DataPackage<EntryContent> getUserSubmittedContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_SUBMITTED, page, lines);
	}
	
	/**
	 * 获取驳回状态的内容
	 */
	public DataPackage<EntryContent> getUserRejectContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_REJECT, page, lines);
	}
	
	/**
	  * 通过当前用户和状态获取词条内容
	 * @param user
	 * @param state
	 * @return
	 * @throws Exception 
	 */
	public DataPackage<EntryContent> queryByUserIdAndState( NUser user, String state, int page, int lines) throws Exception {
		return  queryByUserIdAndState(user.getId(), state, page, lines);
	}
	
	/**
	 * 通过用户ID,词条状态分页查询用户所有编辑的版本内容
	 * @param userId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> queryByUserIdAndState( String userId, String state, int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<EntryContent> result = new DataPackage<EntryContent>();
		result.getDatas().clear();
		
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE AUTHOR= '" + userId + "' AND STATE='" + state + "'";
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE AUTHOR = ?  AND STATE = ?";
		sql = buildPagingString(sql, page, lines, "SAVETIME", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, state);
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
	 * 通过词条状态查询词条版本
	 * @param state
	 * 			版本状态
	 * @param page
	 * 			页码
	 * @param lines
	 * 			每页显示条数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> queryByState(String state, int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<EntryContent> result = new DataPackage<EntryContent>();
		result.getDatas().clear();
		
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" WHERE STATE='" + state + "'";
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		String sql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE  STATE = ?";
		sql = buildPagingString(sql, page, lines, "SUBMMITTIME", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, state);
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
	 * 通过词条ID，删除词条内容
	 * @param entryId
	 * 			词条ID
	 * @return
	 * @throws Exception
	 */
	public void removeByEntryId(String entryId) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE ENTRYID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 驳回版本
	 * @param contentId
	 * 				词条版本ID
	 * @throws Exception
	 */
	public void rejectVersion(String contentId) throws Exception {
		PreparedStatement stmt = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_CONTENT") + " SET STATE=?,HANDLETIME=? WHERE ID=?";	
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, EntryContent.STATE_DRAFT);
			stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
			stmt.setString(3, contentId);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}

	public void submitVersion(String contentId) throws Exception {
		PreparedStatement stmt = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_CONTENT") + " SET STATE=?,SUBMMITTIME=? WHERE ID=?";	
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, EntryContent.STATE_SUBMITTED);
			stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
			stmt.setString(3, contentId);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 删除草稿
	 */
	public void removeDraft(String entryId) throws Exception{
		PreparedStatement stmt = null;		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE STATE='DRAFT' AND VERSIONNUM=0 AND ENTRYID=?";	
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	/**
	 * 提交词条版本
	 * @param contentId
	 * 				词条版本ID
	 * @throws Exception
	 */
	public void submmitVersion(NObject vo) throws Exception {
		if (vo instanceof EntryContent) {
			EntryContent content = (EntryContent)vo;
			PreparedStatement stmt = null;		
			String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_CONTENT") + " SET STATE=?,SUBMMITTIME=?,SUMMARY=?,CONTENT=?,REASON=?,VERSIONNUM=?,SAVETIME=? WHERE ID=?";	
			log.info(sql);
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, EntryContent.STATE_SUBMITTED);
				stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
				stmt.setString(3, content.getSummary());
				stmt.setString(4, content.getContent());
				stmt.setString(5, content.getReason());
				stmt.setInt(6, content.getVersionNum());
				stmt.setTimestamp(7, new Timestamp(new Date().getTime()));
				stmt.setString(8, content.getId());
				stmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				BaikeUtils.closeStatement(stmt);
			}
		}
	}
	
	/**
	 * 审核版本
	 * @param contentId
	 * 				词条版本ID
	 * @throws Exception
	 */
	public void approveVersion(String contentId) throws Exception {
		PreparedStatement stmt = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_CONTENT") + " SET STATE=?,HANDLETIME=? WHERE ID=?";	
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, EntryContent.STATE_PASSED);
			stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
			stmt.setString(3, contentId);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}


	/**
	 * 通过userId和状态查询内容
	 */
	public DataPackage<EntryContent> queryByUserIdAndState(String categoryId,
			String userId, String state, int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<EntryContent> result = new DataPackage<EntryContent>();
		result.getDatas().clear();
		
		//设置分页信息
		String rowCounthql="SELECT * FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") +" CONTENT , "+getFullTableName("BAIKE_ENTRY")+" ENTRY WHERE CONTENT.AUTHOR= '" + userId + "' AND CONTENT.STATE='" + state + "' AND ENTRY.CATEGORYID='"+categoryId+"' AND CONTENT.ENTRYID=ENTRY.ID";
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		String sql="SELECT CONTENT.* FROM "+getFullTableName("BAIKE_ENTRY_CONTENT") +" CONTENT , "+getFullTableName("BAIKE_ENTRY")+" ENTRY WHERE CONTENT.AUTHOR = ?  AND CONTENT.STATE = ? AND ENTRY.CATEGORYID = ? AND CONTENT.ENTRYID=ENTRY.ID";
		sql = buildPagingString(sql, page, lines, "HANDLETIME", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, state);
			stmt.setString(3, categoryId);
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
	 * 查询内容版本
	 */
	public DataPackage<EntryContent> queryContentHistory( String entryId, String contentId, int page, int lines) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<EntryContent> result = new DataPackage<EntryContent>();
		result.getDatas().clear();
		
		//设置分页信息
		String rowCounthql="SELECT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT,"+getFullTableName("BAIKE_ENTRY")+" ENTRY WHERE ENTRY.ID= '" + entryId + "' AND STATE='PASSED' AND CONTENT.ENTRYID=ENTRY.ID AND CONTENT.ID='"+contentId+"'";
		result.rowCount = getTotalLines(rowCounthql);
		result.pageNo = page;
		result.linesPerPage = lines;
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		
		String sql="SELECT ENTRY.* FROM "+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT,"+getFullTableName("BAIKE_ENTRY")+ " ENTRY WHERE ENTRY.ID = ?  AND CONTENT.STATE ='PASSED' AND CONTENT.ENTRYID=ENTRY.ID AND CONTENT.ID=?";
		sql = buildPagingString(sql, page, lines, "CREATED", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entryId);
			stmt.setString(2, contentId);
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
	


}




