package cn.myapps.rm.role.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.rm.base.ejb.BaseObject;
import cn.myapps.rm.role.ejb.UserRoleSet;
import cn.myapps.rm.util.PersistenceUtils;


/**
 * @author Happy
 *
 */
public abstract class AbstractUserRoleSetDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractUserRoleSetDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(BaseObject vo) throws Exception {
		UserRoleSet userRoleSet = (UserRoleSet) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("RM_USER_ROLE_SET")
				+ " (ID,USERID,ROLEID) values (?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userRoleSet.getId());
			stmt.setString(2, userRoleSet.getUserId());
			stmt.setString(3, userRoleSet.getRoleId());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(BaseObject vo) throws Exception {
		UserRoleSet userRoleSet = (UserRoleSet) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("RM_USER_ROLE_SET")
				+ " SET USERID=?,ROLEID=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userRoleSet.getUserId());
			stmt.setString(2, userRoleSet.getRoleId());
			stmt.setString(3, userRoleSet.getId());

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public UserRoleSet find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("RM_USER_ROLE_SET") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			UserRoleSet userRoleSet = null;
			if (rs.next()) {
				userRoleSet = new UserRoleSet();
				setProperties(userRoleSet, rs);
			}
			rs.close();
			return userRoleSet;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(UserRoleSet userRoleSet, ResultSet rs) throws Exception {
		try {
			userRoleSet.setId(rs.getString("ID"));
			userRoleSet.setUserId(rs.getString("USERID"));
			userRoleSet.setRoleId(rs.getString("ROLEID"));
		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("RM_USER_ROLE_SET")
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

		String sql = "DELETE FROM " + getFullTableName("RM_USER_ROLE_SET")
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
	
	public Collection<UserRoleSet> quertByUser(String userId) throws Exception {
		Collection<UserRoleSet> rtn = new ArrayList<UserRoleSet>();
		
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("RM_USER_ROLE_SET") + " WHERE USERID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);

			ResultSet rs = stmt.executeQuery();
			UserRoleSet userRoleSet = null;
			while(rs.next()){
				userRoleSet = new UserRoleSet();
				setProperties(userRoleSet, rs);
				rtn.add(userRoleSet);
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

	protected String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException {
		return null;
	}

}
