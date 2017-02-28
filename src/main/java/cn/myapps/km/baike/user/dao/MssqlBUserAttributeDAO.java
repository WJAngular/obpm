package cn.myapps.km.baike.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.core.user.dao.UserDAO;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.util.BaikeUtils;

/**
 * 
 * @author dragon MS SQL的连接
 * 
 */
public class MssqlBUserAttributeDAO extends AbstractBUserAttributeDAO implements
		BUserAttributeDAO {
	
	public MssqlBUserAttributeDAO(Connection conn) throws Exception {
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
	 * 积分排行榜：根据积分排序分页
	 * 
	 * @param page  每页显示行数
	 * @param lines  每页显示行数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<BUser> getScoreboard(int page, int lines) throws Exception { 
		PreparedStatement stmt = null;
		DataPackage<BUser> result = new DataPackage<BUser>();
		
		//设置分页信息
				String lineshql="SELECT * FROM "+getFullTableName("BAIKE_USER_ATTRIBUTE")+" WHERE USERID <> 'DX0371' "; 
				result.rowCount = getTotalLines(lineshql);
				result.pageNo = page;
				result.linesPerPage = lines;
				
				//当页码大于总页数
				if (result.pageNo > result.getPageCount()) {
					result.pageNo = 1;
					page = 1;
				}
		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_USER_ATTRIBUTE")+" WHERE USERID <> 'DX0371' ";
		//调用方法生成分页sql语句
		sql = buildPagingString(sql, page, lines, "INTEGRAL", "DESC");
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String userId = rs.getString("USERID");
				BUser user = this.findBUserById(userId);
				result.getDatas().add(user);
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
	

//	/**
//	 * 分页查找所有用户
//	 * 
//	 * @param page
//	 *            当前页
//	 * @param lines
//	 *            每页显示行数
//	 * @return
//	 * @throws Exception
//	 */
//	public DataPackage<BUserAttribute> query(int page, int lines)
//			throws Exception {
//		PreparedStatement stmt = null;
//		DataPackage<BUserAttribute> result = new DataPackage<BUserAttribute>();
//		
//		//设置分页信息
//		String lineshql="SELECT * FROM "+getFullTableName("BAIKE_USER_ATTRIBUTE"); 
//		result.rowCount = getTotalLines(lineshql);
//		result.pageNo = page;
//		result.linesPerPage = lines;
//		
//		//当页码大于总页数
//		if (result.pageNo > result.getPageCount()) {
//			result.pageNo = 1;
//			page = 1;
//		}
//		
//		String sql = "SELECT TOP "+lines+" * FROM " + getFullTableName("BAIKE_USER_ATTRIBUTE")+ " WHERE ID NOT IN (SELECT TOP "+lines*(page-1)+" id FROM " + getFullTableName("BAIKE_USER_ATTRIBUTE")+ " )";
//		
//		try {
//			stmt = connection.prepareStatement(sql);
////			stmt.setString(1, lines + "");
////			stmt.setString(2, page * lines + "");
//			ResultSet rs = stmt.executeQuery();
//			while (rs.next()) {
//				result.getDatas().add(setPropety(rs));
//			}
//			rs.close();
//			return result;
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			log.info(sql);
//			BaikeUtils.closeStatement(stmt);
//		}
//	}

	/**
	 * 根据用户ID查找用户
	 * @param userId
	 * 			用户ID
	 * @return BUser
	 * @throws Exception
	 */
	public BUser findBUserById(String userId) throws Exception{
		if (userId == null) {
			return null;
		}
		BUser buser = null;
		try {
			UserDAO userDao = (UserDAO) DAOFactory.getDefaultDAO(UserVO.class.getName());
			buser = new BUser((UserVO) userDao.find(userId));
			BUserAttribute attribute = this.findByUserId(userId);
			buser.setAttribute(attribute);
			return buser;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * 根据用户ID查找用户属性
	 * @param userId
	 * 			用户ID
	 * @return BUser
	 * @throws Exception
	 */
	public BUserAttribute findByUserId(String userId) throws Exception{
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_USER_ATTRIBUTE") + " WHERE USERID = ?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				BUserAttribute bUserAttribute  = setPropety(rs);
				rs.close();
				return bUserAttribute;
			}else{
				return null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
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
	 * 根据用户名称查询用户创建词条数
	 */
	public long countCreateEntryContent(String author) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT COUNT(*) FROM " + getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE CONTENT.ENTRYID=ENTRY.ID AND CONTENT.STATE<>'DRAFT' AND CONTENT.AUTHOR = ?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
		return 0;
	}
	
	/**
	 * 根据用户名称查询用户创建词条数
	 */
	public long countCreateEntry(String author) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT COUNT(DISTINCT ENTRY.ID ) FROM " + getFullTableName("BAIKE_ENTRY")+" ENTRY,"+getFullTableName("BAIKE_ENTRY_CONTENT")+ " CONTENT WHERE CONTENT.ENTRYID=ENTRY.ID AND CONTENT.STATE='PASSED' AND ENTRY.AUTHOR = ?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
		return 0;
	}

	/**
	 * 根据用户名称查询用户创建词条数
	 */
	public long countPassedEntry(String author) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT COUNT(*) FROM " + getFullTableName("BAIKE_ENTRY")+" Entry,"+ getFullTableName("BAIKE_ENTRY_CONTENT") + " CONTENT WHERE CONTENT.STATE='PASSED' AND CONTENT.ENTRYID=ENTRY.ID AND CONTENT.AUTHOR = ?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, author);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
		return 0;
	}

	/**
	 * 增加用户积分
	 * @param pk
	 * 			词条ID
	 * @param point
	 * 			增加积分
	 * @throws Expception
	 */
	public void addPoint(String userId, int point) throws Exception {
			PreparedStatement stmt = null;
			String sql = "UPDATE " + getFullTableName("BAIKE_USER_ATTRIBUTE") + " SET INTEGRAL = INTEGRAL+? WHERE USERID=?";
			
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setInt(1, point);
				stmt.setString(2, userId);
				stmt.execute();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				log.info(sql);
				BaikeUtils.closeStatement(stmt);
			}
			
		}

		public Collection<BUser> doFindContributer(String entryId) throws Exception {
		PreparedStatement stmt = null;
		List<BUser> list=new ArrayList<BUser>();
		//String sql = "SELECT * FROM " + getFullTableName("BAIKE_USER_ATTRIBUTE");
		String sql = "SELECT DISTINCT TOP 5 USERID FROM " + getFullTableName("BAIKE_USER_ATTRIBUTE")+" USERS, "+getFullTableName("BAIKE_ENTRY_CONTENT")+" CONTENT WHERE USERS.USERID=CONTENT.AUTHOR AND CONTENT.STATE='PASSED' AND ENTRYID='"+entryId+"'";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String userId = rs.getString("USERID");
				BUser user = this.findBUserById(userId);
				list.add(user);
			}
			rs.close();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}

		public Collection<BUserAttribute> doFindAllUser() throws Exception {
			PreparedStatement stmt = null;
			Collection<BUserAttribute> result = new ArrayList<BUserAttribute>();
		
			String sql="SELECT * FROM " + getFullTableName("BAIKE_USER_ATTRIBUTE");
			
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
	}


