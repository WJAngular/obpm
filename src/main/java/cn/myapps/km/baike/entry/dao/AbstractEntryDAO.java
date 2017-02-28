package cn.myapps.km.baike.entry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * @author Allen
 * 实现主要的DAO层功能
 *
 */
public abstract class AbstractEntryDAO extends AbstractBaseDAO{
	Logger log = Logger.getLogger(getClass());
	public AbstractEntryDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	/**
	 * 创建
	 * @param bobject
	 * @throws Exception
	 */
	public  void create(NObject bobject) throws Exception {
		Entry entry=(Entry)bobject;
		PreparedStatement stmt = null;
		String sql = "INSERT INTO "+getFullTableName("BAIKE_ENTRY") +" (ID,NAME,DOMAINID,AUTHOR,LATESTCONTENTID,CREATED,EDIT_COUNT,POINTS,BROWSE_COUNT,KEYWORD,CATEGORYID) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		log.info(sql);													
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entry.getId());
			stmt.setString(2, entry.getName());
			stmt.setString(3, entry.getDomainId());
			stmt.setString(4, entry.getAuthor().getId());
			stmt.setString(5, entry.getLatestContentId());
			if (entry.getCreated()== null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(entry.getCreated().getTime());
				stmt.setTimestamp(6, ts);
			}
			stmt.setInt(7, entry.getEditCount());
			stmt.setLong(8, entry.getPoints());
			stmt.setInt(9, entry.getBrowseCount());
			stmt.setString(10, entry.getKeyWord());
			stmt.setString(11, entry.getCategoryId());
			stmt.execute();
			} catch (Exception e) {
				throw e;
			} finally {
				closeStatement(stmt);
			}
		}
	
	/**
	 * 根据Id查找
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Entry find(String id) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM "+getFullTableName("BAIKE_ENTRY") +" WHERE ID=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
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
	 *更新词条 
	 * @param vo
	 * @throws Exception
	 */
	public void update(NObject vo) throws Exception {
		Entry entry = (Entry) vo;
		PreparedStatement stmt = null;
																									
		String sql = "UPDATE "+getFullTableName("BAIKE_ENTRY") +" SET ID=?,NAME=?,DOMAINID=?,AUTHOR=?,LATESTCONTENTID=?,CREATED=?,EDIT_COUNT=?,POINTS=?,BROWSE_COUNT=?,KEYWORD=? ,CATEGORYID=? WHERE ID=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, entry.getId());
			stmt.setString(2, entry.getName());
			stmt.setString(3, entry.getDomainId());
			stmt.setString(4, entry.getAuthor().getId());			
			stmt.setString(5,entry.getLatestContentId() );
			if (entry.getCreated()== null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(entry.getCreated().getTime());
				stmt.setTimestamp(6, ts);
			}
			stmt.setInt(7, entry.getEditCount());
			stmt.setLong(8, entry.getPoints());
			stmt.setInt(9, entry.getBrowseCount());
			stmt.setString(10, entry.getKeyWord());
			stmt.setString(11, entry.getCategoryId());
			
			stmt.setString(12, entry.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}

	/**
	 * 删除词条
	 * @param id
	 * @throws Exception
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY") + " WHERE ID=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}	
	}
	
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	
	public static Entry setPropety(ResultSet rs){
		Entry entry = new Entry();
		try{
			entry.setId(rs.getString("ID"));
			entry.setName(rs.getString("NAME"));
			entry.setDomainId(rs.getString("DOMAINID"));
			entry.setAuthor(rs.getString("AUTHOR"));
			entry.setCreated((Date)rs.getTimestamp("CREATED"));
			entry.setEditCount(rs.getInt("EDIT_COUNT"));
			entry.setLatestContentId(rs.getString("LATESTCONTENTID"));
			entry.setBrowseCount(rs.getInt("BROWSE_COUNT"));
			entry.setKeyWord(rs.getString("KEYWORD"));
			entry.setPoints(rs.getInt("POINTS"));
			entry.setCategoryId(rs.getString("CATEGORYID"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return entry;
	}
	
	private void closeStatement(PreparedStatement stmt) throws SQLException{
		if(stmt!=null){
				stmt.close();
		}
	} 
	
	
	/**
	 * 获取行数
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int getTotalLines(String sql) throws Exception {
		PreparedStatement stmt = null;
		Long amount = Long.valueOf(0);
		int from = sql.toUpperCase().indexOf("FROM");
		int order = sql.toUpperCase().indexOf("ORDER BY");

		String newsql = (order > 0) ? " SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from, order)
				: " SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from);
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
	
	/**
	 * 移除词条集合
	 * @param collections
	 * @throws Exception
	 */
	public void remove(Collection<Entry> collections) throws Exception {
		PreparedStatement stmt = null;
		StringBuffer delSql = new StringBuffer("");
		for (Iterator<Entry> iter=collections.iterator(); iter.hasNext();) {
			NObject object = iter.next();
			delSql.append("'").append(object.getId()).append("',");
		}
		delSql.deleteCharAt(delSql.lastIndexOf(","));
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY") + " WHERE ID IN (" + delSql.toString() + ")";
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
	
}
