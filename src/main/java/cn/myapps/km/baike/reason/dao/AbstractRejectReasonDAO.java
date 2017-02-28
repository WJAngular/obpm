package cn.myapps.km.baike.reason.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.log4j.Logger;
import cn.myapps.km.baike.reason.ejb.RejectReason;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * 
 * @author Abel
 *内容DAO层的抽象类
 */
public abstract class AbstractRejectReasonDAO extends AbstractBaseDAO{
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractRejectReasonDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	public int setParameters(PreparedStatement statement, RejectReason reason)
			throws SQLException {
		int index = 1;
		statement.setString(index++, reason.getId());
		statement.setString(index++, reason.getContentId());
		statement.setString(index++, reason.getReason());
		Timestamp ts = new Timestamp(reason.getRejectTime().getTime());
		statement.setTimestamp(index++, ts);
		return index;
	}
	
	/**
	 * 创建内容
	 */
	public void create(NObject vo) throws Exception {
		RejectReason ec = (RejectReason) vo;
		PreparedStatement statement = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_REJECT_REASON") + " (ID,CONTENTID,REASON,REJECTTIME) values (?,?,?,?)";
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
		RejectReason ec = (RejectReason) vo;
		PreparedStatement statement = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_REJECT_REASON") + " SET ID=?,CONTENTID=?,REASON=?,REJECTTIME=? WHERE ID=?";	
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
	public RejectReason find(String id) throws Exception {
		PreparedStatement stmt = null;
		RejectReason ec = new RejectReason();
		ResultSet rs = null;		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_REJECT_REASON") + " WHERE ID=?";
		
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
	
	private void setProperty(RejectReason ec, ResultSet rs) throws Exception{
		ec.setId(rs.getString("ID"));
		ec.setContentId(rs.getString("CONTENTID"));
		ec.setReason(rs.getString("REASON"));
		ec.setRejectTime(new Date(rs.getTimestamp("REJECTTIME").getTime()));
	}
	

	public RejectReason setProperty(ResultSet rs) throws Exception{
		RejectReason ec=new RejectReason();
		ec.setId(rs.getString("ID"));
		ec.setContentId(rs.getString("CONTENTID"));
		ec.setReason(rs.getString("REASON"));
		ec.setRejectTime(new Date(rs.getTimestamp("REJECTTIME").getTime()));
		return ec;
	}
	
	/**
	 * 通过Id进行删除
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_REJECT_REASON") + " WHERE ID=?";

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

	public DataPackage<RejectReason> query() throws Exception {
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
	
}
