package cn.myapps.km.baike.content.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * 
 * @author Abel
 *内容DAO层的抽象类
 */
public abstract class AbstractEntryContentDAO extends AbstractBaseDAO{
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractEntryContentDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	public int setParameters(PreparedStatement statement, EntryContent content)
			throws SQLException {
		int index = 1;
		statement.setString(index++, content.getId());
		statement.setString(index++, content.getEntryId());
		if (content.getAuthor() == null) {
			statement.setNull(index++,java.sql.Types.VARBINARY);
		} else {
			statement.setString(index++, content.getAuthor().getId());
		}
		
		if (content.getHandleTime() == null) {
			statement.setNull(index++, java.sql.Types.TIMESTAMP);
		} else {
			Timestamp ts = new Timestamp(content.getHandleTime().getTime());
			statement.setTimestamp(index++, ts);
		}	
		
		if (content.getSubmmitTime() == null) {
			statement.setNull(index++, java.sql.Types.TIMESTAMP);
		} else {
			Timestamp ts = new Timestamp(content.getSubmmitTime().getTime());
			statement.setTimestamp(index++, ts);
		}
		
		if (content.getSaveTime() == null) {
			statement.setNull(index++, java.sql.Types.TIMESTAMP);
		} else {
			Timestamp ts = new Timestamp(content.getSaveTime().getTime());
			statement.setTimestamp(index++, ts);
		}	
		
		statement.setString(index++, content.getContent());	
		statement.setInt(index++, content.getVersionNum());
		statement.setString(index++, content.getReason());
		statement.setString(index++, content.getState());
		statement.setString(index++, content.getSummary());
		return index;
	}
	
	/**
	 * 创建内容
	 */
	public void create(NObject vo) throws Exception {
		EntryContent ec = (EntryContent) vo;
		PreparedStatement statement = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_ENTRY_CONTENT") + " (ID,ENTRYID,AUTHOR,HANDLETIME,SUBMMITTIME,SAVETIME,CONTENT,VERSIONNUM,REASON,STATE,SUMMARY) values (?,?,?,?,?,?,?,?,?,?,?)";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, ec);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(statement);
		}
	}
	
	/**
	 * 修改内容
	 */
	public void update(NObject vo) throws Exception {
		EntryContent ec = (EntryContent) vo;
		PreparedStatement statement = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_CONTENT") + " SET ID=?,ENTRYID=?,AUTHOR=?,HANDLETIME=?,SUBMMITTIME=?,SAVETIME=?,CONTENT=?,VERSIONNUM=?,REASON=?,STATE=?,SUMMARY=? WHERE ID=?";	
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			int index = setParameters(statement, ec);
			statement.setString(index, ec.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			BaikeUtils.closeStatement(statement);
		}
	}
	
	/**
	 * 通过Id查找内容
	 */
	public EntryContent find(String id) throws Exception {
		PreparedStatement stmt = null;
		EntryContent ec = new EntryContent();
		ResultSet rs = null;		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);		
			rs = stmt.executeQuery();
			if(rs.next()){
				this.setProperty(ec, rs);
			} else {
				return null;
			}
			return ec;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	private void setProperty(EntryContent ec, ResultSet rs) throws Exception{
		ec.setId(rs.getString("ID"));
		ec.setEntryId(rs.getString("ENTRYID"));
		ec.setAuthor(rs.getString("AUTHOR"));
		if (rs.getTimestamp("HANDLETIME") == null) {
			ec.setHandleTime(null);
		} else {
			ec.setHandleTime(new Date(rs.getTimestamp("HANDLETIME").getTime()));
		}
		if (rs.getTimestamp("SUBMMITTIME") == null) {
			ec.setSubmmitTime(null);
		} else {
			ec.setSubmmitTime(new Date(rs.getTimestamp("SUBMMITTIME").getTime()));
		}
		if (rs.getTimestamp("SAVETIME") == null) {
			ec.setSaveTime(null);
		} else {
			ec.setSaveTime(new Date(rs.getTimestamp("SAVETIME").getTime()));
		}
		ec.setContent(rs.getString("CONTENT"));
	    ec.setVersionNum(rs.getInt("VERSIONNUM"));
	    ec.setReason(rs.getString("REASON"));
		ec.setState(rs.getString("STATE"));
		ec.setSummary(rs.getString("SUMMARY"));
	}
	

	public EntryContent setProperty(ResultSet rs) throws Exception{
		EntryContent ec=new EntryContent();
		ec.setId(rs.getString("ID"));
		ec.setEntryId(rs.getString("ENTRYID"));
		ec.setAuthor(rs.getString("AUTHOR"));
		if (rs.getTimestamp("HANDLETIME") == null) {
			ec.setHandleTime(null);
		} else {
			ec.setHandleTime(new Date(rs.getTimestamp("HANDLETIME").getTime()));
		}
		if (rs.getTimestamp("SUBMMITTIME") == null) {
			ec.setSubmmitTime(null);
		} else {
			ec.setSubmmitTime(new Date(rs.getTimestamp("SUBMMITTIME").getTime()));
		}
		if (rs.getTimestamp("SAVETIME") == null) {
			ec.setSaveTime(null);
		} else {
			ec.setSaveTime(new Date(rs.getTimestamp("SAVETIME").getTime()));
		}
		ec.setContent(rs.getString("CONTENT"));
	    ec.setVersionNum(rs.getInt("VERSIONNUM"));
	    ec.setReason(rs.getString("REASON"));
		ec.setState(rs.getString("STATE"));
		ec.setSummary(rs.getString("SUMMARY"));
		return ec;
	}
	
	/**
	 * 通过Id进行删除
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			BaikeUtils.closeStatement(stmt);
		}
	}

	public DataPackage<EntryContent> query() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}

	public int getTotalLines(String sql) throws Exception {
		PreparedStatement stmt = null;
		Long amount = Long.valueOf(0);
		int from = sql.toUpperCase().indexOf("FROM");
		int order = sql.toUpperCase().indexOf("ORDER BY");
		String newsql = (order > 0) ? "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from, order)
				: "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from);
		
		try{stmt=connection.prepareStatement(newsql);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()){
			amount=rs.getLong("ROW_COUNT");
			rs.close();
			return amount.intValue();
		}
		}catch(Exception e){
			
		}finally{
			
		}

		return 0;
	}
	
	
	/**
	 * 移除内容
	 * @param id
	 * @throws Exception
	 */
	public void remove(Collection<EntryContent> collections) throws Exception {
		PreparedStatement stmt = null;
		StringBuffer delSql = new StringBuffer("");
		for (Iterator<EntryContent> iter=collections.iterator(); iter.hasNext();) {
			NObject object = iter.next();
			delSql.append("'").append(object.getId()).append("',");
		}
		delSql.deleteCharAt(delSql.lastIndexOf(","));
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE ID IN (" + delSql.toString() + ")";
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
	 * 获取自动生成的verisonNum
	 */
	public  synchronized int getVersionNum(String entryId) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int versionNum = 0;
		String sql = "SELECT MAX(VERSIONNUM) AS VERSIONNUM FROM " + getFullTableName("BAIKE_ENTRY_CONTENT") + " WHERE ENTRYID=?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);	
			stmt.setString(1, entryId);
			rs = stmt.executeQuery();
			if(rs.next()){
				Integer num = rs.getInt("VERSIONNUM");
				rs.close();
				if (num!=null) {
					versionNum = num+1;
				}
				return versionNum;
			}
			return versionNum;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	public DataPackage<Map> query(String sql, int page, int lines) throws Exception {
		return null;
	}
}
