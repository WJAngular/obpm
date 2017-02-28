package cn.myapps.rm.resource.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import cn.myapps.rm.base.ejb.BaseObject;
import cn.myapps.rm.resource.ejb.Resource;
import cn.myapps.rm.util.PersistenceUtils;



public abstract class AbstractResourceDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractResourceDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(BaseObject vo) throws Exception {
		Resource resource = (Resource) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("RM_RESOURCE")
				+ " (ID,NAME,SERIAL,DESCRIPTION,R_TYPE,CREATOR,CREATOR_ID,CREATE_DATE,DOMAIN_ID) values (?,?,?,?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, resource.getId());
			stmt.setString(2, resource.getName());
			stmt.setString(3, resource.getSerial());
			stmt.setString(4, resource.getType());
			stmt.setString(5, resource.getDescription());
			stmt.setString(6, resource.getCreator());
			stmt.setString(7, resource.getCreatorId());
			if (resource.getCreateDate() == null) {
				stmt.setNull(8, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resource.getCreateDate().getTime());
				stmt.setTimestamp(8, ts);
			}
			stmt.setString(9, resource.getDomainId());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(BaseObject vo) throws Exception {
		Resource resource = (Resource) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("RM_RESOURCE")
				+ " SET NAME=?,SERIAL=?,DESCRIPTION=?,R_TYPE=?,CREATOR=?,CREATOR_ID=?,CREATE_DATE=?,DOMAIN_ID=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, resource.getName());
			stmt.setString(2, resource.getDescription());
			stmt.setString(3, resource.getSerial());
			stmt.setString(4, resource.getType());
			stmt.setString(5, resource.getCreator());
			stmt.setString(6, resource.getCreatorId());
			if (resource.getCreateDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(resource.getCreateDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setString(8, resource.getDomainId());
			stmt.setString(9, resource.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public Resource find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("RM_RESOURCE") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			Resource resource = null;
			if (rs.next()) {
				resource = new Resource();
				setProperties(resource, rs);
			}
			rs.close();
			return resource;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(Resource resource, ResultSet rs) throws Exception {
		try {
			resource.setId(rs.getString("ID"));
			resource.setName(rs.getString("NAME"));
			resource.setSerial(rs.getString("SERIAL"));
			resource.setDescription(rs.getString("DESCRIPTION"));
			resource.setType(rs.getString("R_TYPE"));
			resource.setCreator(rs.getString("CREATOR"));
			resource.setCreatorId(rs.getString("CREATOR_ID"));
			resource.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			resource.setDomainId(rs.getString("DOMAIN_ID"));
		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("RM_RESOURCE")
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
