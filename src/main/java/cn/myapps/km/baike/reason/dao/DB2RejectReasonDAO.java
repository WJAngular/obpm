package cn.myapps.km.baike.reason.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import cn.myapps.km.baike.reason.ejb.RejectReason;
import cn.myapps.km.util.BaikeUtils;

/**
 * @author Able
 * SQLSERVER实现类
 * 
 */
public class DB2RejectReasonDAO extends AbstractRejectReasonDAO implements RejectReasonDao {

	public DB2RejectReasonDAO(Connection conn) throws Exception {
		super(conn);
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getUserName().trim()
						.toUpperCase();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取词条通过版本内容
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public Collection<RejectReason> queryAllReason(String contentId) throws Exception {
		PreparedStatement stmt = null;
		List<RejectReason> list=new ArrayList<RejectReason>();
		String sql="SELECT  * FROM "+getFullTableName("BAIKE_REJECT_REASON")+" WHERE CONTENTID =? ORDER BY REJECTTIME DESC";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, contentId);
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
}
