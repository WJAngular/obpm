package cn.myapps.km.comments.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.comments.ejb.Comments;
import cn.myapps.km.util.PersistenceUtils;

/**
 * @author Happy
 *
 */
public abstract class AbstractCommentsDAO {
	
	Logger log = Logger.getLogger(getClass());
	
	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractCommentsDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		Comments comments = (Comments) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "+ getFullTableName("KM_COMMENTS")+" (ID,FILE_ID,USER_ID,USER_NAME,ASSESSMENTDATE,GOOD,BAD,CONTENT) values (?,?,?,?,?,?,?,?)";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, comments.getId());
			stmt.setString(2, comments.getFileId());
			stmt.setString(3, comments.getUserId());
			stmt.setString(4, comments.getUserName());
			if (comments.getAssessmentDate() == null) {
				stmt.setNull(5, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(comments.getAssessmentDate().getTime());
				stmt.setTimestamp(5, ts);
			}
			stmt.setBoolean(6, comments.isGood());
			stmt.setBoolean(7, comments.isBad());
			stmt.setString(8, comments.getContent());
			
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		Comments comments = (Comments)vo;
		PreparedStatement stmt = null;
		
		String sql = "UPDATE " + getFullTableName("KM_COMMENTS")+" SET ID=?,FILE_ID=?,USER_ID=?,USER_NAME=?,ASSESSMENTDATE=?,GOOD=?,BAD=?,CONTENT=? WHERE id=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, comments.getId());
			stmt.setString(2, comments.getFileId());
			stmt.setString(3, comments.getUserId());
			stmt.setString(4, comments.getUserName());
			if (comments.getAssessmentDate() == null) {
				stmt.setNull(5, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(comments.getAssessmentDate().getTime());
				stmt.setTimestamp(5, ts);
			}
			stmt.setBoolean(6, comments.isGood());
			stmt.setBoolean(7, comments.isBad());
			stmt.setString(8, comments.getContent());
			stmt.setString(9, comments.getId());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}

	}

	public Comments find(String id) throws Exception {
		PreparedStatement stmt = null;
		Comments comments = new Comments();
		ResultSet rs = null;
		
		String sql = "SELECT * FROM "+getFullTableName("KM_COMMENTS") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			rs = stmt.executeQuery();
			
			if (rs.next()) {
				/*Comments comments = new Comments();
				comments.setId(rs.getString(1));
				comments.setContent(rs.getString(2));
				rs.close();*/
				
				this.setCommentsProperty(comments, rs);
				
				return comments;
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	private void setCommentsProperty(Comments comments, ResultSet rs) throws Exception{
		comments.setId(rs.getString("id"));
		comments.setFileId(rs.getString("file_id"));
		comments.setUserId(rs.getString("user_id"));
		comments.setUserName(rs.getString("user_name"));
		comments.setAssessmentDate(rs.getDate("assessmentdate"));
		comments.setGood(rs.getBoolean("good"));
		comments.setBad(rs.getBoolean("bad"));
		comments.setContent(rs.getString("content"));
	}
	
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM " + getFullTableName("KM_COMMENTS") + " WHERE ID = ?";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}	
	}

	public DataPackage<Comments> query() throws Exception {
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
	

	public long countByGood(String fileID) throws Exception {
		String sql="";
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		sql= "SELECT COUNT(*)  FROM " + getFullTableName("KM_COMMENTS") + " WHERE FILE_ID=? and GOOD=4 and ASSESSMENTDATE >= dateadd(d,-30,getdate()) and ASSESSMENTDATE <=getdate()";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, fileID);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}
	
	public long countByBad(String fileID) throws Exception {
		String sql="";
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		sql= "SELECT COUNT(*)  FROM " + getFullTableName("KM_COMMENTS") + " WHERE FILE_ID=? and BAD=0 and ASSESSMENTDATE >= dateadd(d,-30,getdate()) and ASSESSMENTDATE <=getdate() ";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, fileID);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}
	
	public long countBy(String fileId,String userId) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql= "SELECT COUNT(*)  FROM " + getFullTableName("KM_COMMENTS") + " WHERE FILE_ID=? and USER_ID=? ";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, fileId);
			statement.setString(2, userId);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
		}
	
	
	
}
