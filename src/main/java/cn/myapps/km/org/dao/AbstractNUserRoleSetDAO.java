package cn.myapps.km.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUserRoleSet;
import cn.myapps.km.util.PersistenceUtils;

public abstract class AbstractNUserRoleSetDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractNUserRoleSetDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		NUserRoleSet nUserRoleSet = (NUserRoleSet) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_USER_ROLE_SET")
				+ " (ID,USERID,ROLEID) values (?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nUserRoleSet.getId());
			stmt.setString(2, nUserRoleSet.getUserId());
			stmt.setString(3, nUserRoleSet.getRoleId());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		NUserRoleSet nUserRoleSet = (NUserRoleSet) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_USER_ROLE_SET")
				+ " SET USERID=?,ROLEID=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nUserRoleSet.getUserId());
			stmt.setString(2, nUserRoleSet.getRoleId());
			stmt.setString(3, nUserRoleSet.getId());

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public NUserRoleSet find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("KM_USER_ROLE_SET") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			NUserRoleSet nUserRoleSet = null;
			if (rs.next()) {
				nUserRoleSet = new NUserRoleSet();
				setProperties(nUserRoleSet, rs);
			}
			rs.close();
			return nUserRoleSet;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(NUserRoleSet nUserRoleSet, ResultSet rs) throws Exception {
		try {
			nUserRoleSet.setId(rs.getString("ID"));
			nUserRoleSet.setUserId(rs.getString("USERID"));
			nUserRoleSet.setRoleId(rs.getString("ROLEID"));
		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_USER_ROLE_SET")
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
	
	public void removeByUser(String userId) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_USER_ROLE_SET")
				+ " WHERE USERID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public Collection<NUserRoleSet> quertByUser(String userId) throws Exception {
		Collection<NUserRoleSet> rtn = new ArrayList<NUserRoleSet>();
		
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("KM_USER_ROLE_SET") + " WHERE USERID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);

			ResultSet rs = stmt.executeQuery();
			NUserRoleSet nUserRoleSet = null;
			while(rs.next()){
				nUserRoleSet = new NUserRoleSet();
				setProperties(nUserRoleSet, rs);
				rtn.add(nUserRoleSet);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
		
		return rtn;
	}

}
