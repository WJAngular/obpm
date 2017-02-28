package cn.myapps.km.baike.history.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.myapps.km.baike.history.ejb.History;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * 
 * @author Abel
 *驳回原因DAO层的抽象类
 */
public abstract class AbstractHistoryDAO extends AbstractBaseDAO{
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractHistoryDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	public int setParameters(PreparedStatement statement, History history)
			throws SQLException {
		int index = 1;
		statement.setString(index++, history.getId());
		statement.setString(index++, history.getAuthor().getId());
		statement.setString(index++, history.getEntryId());
		Timestamp ts = new Timestamp(history.getReadTime().getTime());
		statement.setTimestamp(index++, ts);
		statement.setString(index++, history.getEntryName());
		return index;
	}
	
	/**
	 * 创建内容
	 */
	public void create(NObject vo) throws Exception {
		History history = (History) vo;
		PreparedStatement statement = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_READ_HISTORY") + " (ID,AUTHOR,ENTRYID,READTIME,ENTRYNAME) values (?,?,?,?,?)";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, history);
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
		History ec = (History) vo;
		PreparedStatement statement = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_READ_HISTORY") + " SET ID=?,AUTHOR=?,ENTRYID=?,READTIME=?,ENTRYNAME=? WHERE ID=?";	
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
	public History find(String id) throws Exception {
		PreparedStatement stmt = null;
		History ec = new History();
		ResultSet rs = null;		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_READ_HISTORY") + " WHERE ID=?";
		
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
	
	private void setProperty(History ec, ResultSet rs) throws Exception{
		ec.setId(rs.getString("ID"));
		ec.setAuthor(rs.getString("AUTHOR"));
		ec.setEntryId(rs.getString("ENTRYID"));
		ec.setReadTime(new Date(rs.getTimestamp("READTIME").getTime()));
		ec.setEntryName(rs.getString("ENTRYNAME"));
	}
	

	public History setProperty(ResultSet rs) throws Exception{
		History ec=new History();
		ec.setId(rs.getString("ID"));
		ec.setAuthor(rs.getString("AUTHOR"));
		ec.setEntryId(rs.getString("ENTRYID"));
		ec.setReadTime(new Date(rs.getTimestamp("READTIME").getTime()));
		ec.setEntryName(rs.getString("ENTRYNAME"));
		return ec;
	}
	
	/**
	 * 通过Id进行删除
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_READ_HISTORY") + " WHERE ID=?";

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
	
}
