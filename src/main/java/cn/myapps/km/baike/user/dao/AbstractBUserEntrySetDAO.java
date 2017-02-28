package cn.myapps.km.baike.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import cn.myapps.km.baike.user.ejb.BUserEntrySet;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * @author dragon
 * 用户收藏词条/注:无修改功能
 *
 */
public abstract class AbstractBUserEntrySetDAO extends AbstractBaseDAO{
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractBUserEntrySetDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	/**
	 * 收藏词条无修改操作，空实现
	 * @param vo
	 * @throws Exception
	 */
	public void update(NObject vo) throws Exception {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 根据编号查询
	 */
	public BUserEntrySet find(String id) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM "+getFullTableName("BAIKE_USER_ENTRY_SET") +" WHERE ID=?";
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				BUserEntrySet bUserEntrySet = setPropety(rs);
				rs.close();
				return bUserEntrySet;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	
	/**
	 * 增加收藏
	 * @param id
	 * @throws Exception
	 */
	public void create(NObject bObject) throws Exception {
		BUserEntrySet bUserEntrySet = (BUserEntrySet)bObject;
		PreparedStatement stmt = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_USER_ENTRY_SET") + "(ID, USERID, ENTRYID, TYPE) values(?,?,?,?);";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, bUserEntrySet.getId());
			stmt.setString(2, bUserEntrySet.getUserId());
			stmt.setString(3, bUserEntrySet.getEntryId());
			stmt.setString(4, bUserEntrySet.getType());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 移除收藏
	 * @param id
	 * @throws Exception
	 */
	public void remove(Collection<BUserEntrySet> collections) throws Exception {
		PreparedStatement stmt = null;
		StringBuffer delSql = new StringBuffer("");
		for (Iterator<BUserEntrySet> iter=collections.iterator(); iter.hasNext();) {
			NObject object = iter.next();
			delSql.append("'").append(object.getId()).append("',");
		}
		delSql.deleteCharAt(delSql.lastIndexOf(","));
		String sql = "DELETE FROM " + getFullTableName("BAIKE_USER_ENTRY_SET") + " WHERE ID IN (" + delSql.toString() + ")";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 移除用户词条关系集
	 * @param id
	 * @throws Exception
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM " + getFullTableName("BAIKE_USER_ENTRY_SET") + " WHERE ID=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	
	/**
	 * 获得表名
	 */
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public BUserEntrySet setPropety(ResultSet rs) throws Exception{
		BUserEntrySet bUserEntrySet = new BUserEntrySet();
		try{
			bUserEntrySet.setId(rs.getString("ID"));
			bUserEntrySet.setEntryId(rs.getString("ENTRYID"));
			bUserEntrySet.setUserId(rs.getString("USERID"));
			bUserEntrySet.setType(rs.getString("TYPE"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return bUserEntrySet;
	}
	
	
	
	public int getTotalLines(String sql) throws Exception {
		PreparedStatement stmt = null;
		Long amount = Long.valueOf(0);
		int from = sql.toUpperCase().indexOf("FROM");
		int order = sql.toUpperCase().indexOf("ORDER BY");
		
		String newsql = (order > 0) ? "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from, order)
				: "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from);
		try {
			stmt = connection.prepareStatement(newsql);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				amount = rs.getLong("ROW_COUNT");
				rs.close();
				return amount.intValue();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			log.info(newsql);
			BaikeUtils.closeStatement(stmt);
		}
		return 0;
	}
	
};



