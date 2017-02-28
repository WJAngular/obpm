package cn.myapps.rm.usage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import cn.myapps.rm.base.ejb.BaseObject;
import cn.myapps.rm.usage.ejb.ResourceUsage;
import cn.myapps.rm.util.PersistenceUtils;



/**
 * @author Happy
 *
 */
public abstract class AbstractResourceUsageDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractResourceUsageDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(BaseObject vo) throws Exception {
		ResourceUsage resourceUsage = (ResourceUsage) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("RM_RESOURCE_USAGE")
				+ " (ID,RESOURCE_ID,RESOURCE_NAME,RESOURCE_SERIAL,USER,USER_ID,REMARK,START_DATE,END_DATE,CREATE_DATE,DOMAIN_ID,USAGE_MODE,STATUS,EFFECTIVE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, resourceUsage.getId());
			stmt.setString(2, resourceUsage.getResourceId());
			stmt.setString(3, resourceUsage.getResourceName());
			stmt.setString(4, resourceUsage.getResourceSerial());
			stmt.setString(5, resourceUsage.getUser());
			stmt.setString(6, resourceUsage.getUserId());
			stmt.setString(7, resourceUsage.getRemark());
			if (resourceUsage.getStartDate() == null) {
				stmt.setNull(8, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resourceUsage.getStartDate().getTime());
				stmt.setTimestamp(8, ts);
			}
			if (resourceUsage.getEndDate() == null) {
				stmt.setNull(9, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resourceUsage.getEndDate().getTime());
				stmt.setTimestamp(9, ts);
			}
			
			if (resourceUsage.getCreateDate() == null) {
				stmt.setNull(10, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resourceUsage.getCreateDate().getTime());
				stmt.setTimestamp(10, ts);
			}
			stmt.setString(11, resourceUsage.getDomainId());
			stmt.setInt(12, resourceUsage.getUsageMode());
			stmt.setString(13, resourceUsage.getStatus());
			stmt.setInt(14, resourceUsage.isEffective()? 1: 0);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(BaseObject vo) throws Exception {
		ResourceUsage resourceUsage = (ResourceUsage) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("RM_RESOURCE_USAGE")
				+ " SET RESOURCE_ID=?,RESOURCE_NAME=?,RESOURCE_SERIAL=?,USER=?,USER_ID=?,REMARK=?,START_DATE=?,END_DATE=?,CREATE_DATE=?,DOMAIN_ID=?,USAGE_MODE=?,STATUS=?,EFFECTIVE=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, resourceUsage.getResourceId());
			stmt.setString(2, resourceUsage.getResourceName());
			stmt.setString(3, resourceUsage.getResourceSerial());
			stmt.setString(4, resourceUsage.getUser());
			stmt.setString(5, resourceUsage.getUserId());
			stmt.setString(6, resourceUsage.getRemark());
			if (resourceUsage.getStartDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resourceUsage.getStartDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			if (resourceUsage.getEndDate() == null) {
				stmt.setNull(8, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resourceUsage.getEndDate().getTime());
				stmt.setTimestamp(8, ts);
			}
			
			if (resourceUsage.getCreateDate() == null) {
				stmt.setNull(9, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resourceUsage.getCreateDate().getTime());
				stmt.setTimestamp(9, ts);
			}
			stmt.setString(10, resourceUsage.getDomainId());
			stmt.setInt(11, resourceUsage.getUsageMode());
			stmt.setString(12, resourceUsage.getStatus());
			stmt.setInt(13, resourceUsage.isEffective()? 1: 0);
			stmt.setString(14, resourceUsage.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public ResourceUsage find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("RM_RESOURCE_USAGE") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			ResourceUsage resourceUsage = null;
			if (rs.next()) {
				resourceUsage = new ResourceUsage();
				setProperties(resourceUsage, rs);
			}
			rs.close();
			return resourceUsage;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(ResourceUsage resourceUsage, ResultSet rs) throws Exception {
		try {
			resourceUsage.setId(rs.getString("ID"));
			resourceUsage.setResourceId(rs.getString("RESOURCE_ID"));
			resourceUsage.setResourceName(rs.getString("RESOURCE_NAME"));
			resourceUsage.setResourceSerial(rs.getString("RESOURCE_SERIAL"));
			resourceUsage.setUser(rs.getString("USER"));
			resourceUsage.setUserId(rs.getString("USER_ID"));
			resourceUsage.setRemark(rs.getString("REMARK"));
			resourceUsage.setStartDate(rs.getTimestamp("START_DATE"));
			resourceUsage.setEndDate(rs.getTimestamp("END_DATE"));
			resourceUsage.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			resourceUsage.setDomainId(rs.getString("DOMAIN_ID"));
			resourceUsage.setUsageMode(rs.getInt("USAGE_MODE"));
			resourceUsage.setStatus(rs.getString("STATUS"));
			resourceUsage.setEffective(rs.getInt("EFFECTIVE")==1? true:false );
		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("RM_RESOURCE_USAGE")
				+ " WHERE ID=?";

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

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	protected abstract String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException ;
	

}
