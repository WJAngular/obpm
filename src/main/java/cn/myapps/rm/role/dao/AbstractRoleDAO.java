package cn.myapps.rm.role.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.rm.base.ejb.BaseObject;
import cn.myapps.rm.role.ejb.Role;
import cn.myapps.rm.util.PersistenceUtils;
import cn.myapps.util.StringUtil;


/**
 * @author Happy
 *
 */
public abstract class AbstractRoleDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractRoleDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(BaseObject vo) throws Exception {
		Role role = (Role) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("RM_ROLE")
				+ " (ID,NAME,R_LEVEL) values (?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, role.getId());
			stmt.setString(2, role.getName());
			stmt.setInt(3, role.getLevel());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(BaseObject vo) throws Exception {
		Role role = (Role) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("RM_ROLE")
				+ " SET NAME=?,R_LEVEL=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, role.getName());
			stmt.setInt(2, role.getLevel());
			stmt.setString(3, role.getId());

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public Role find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("RM_ROLE") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			Role role = null;
			if (rs.next()) {
				role = new Role();
				setProperties(role, rs);
			}
			rs.close();
			return role;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(Role role, ResultSet rs) throws Exception {
		try {
			role.setId(rs.getString("ID"));
			role.setName(rs.getString("NAME"));
			role.setLevel(rs.getInt("R_LEVEL"));
		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("RM_ROLE")
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
	
	public Collection<Role> getRoles() throws Exception {
		Collection<Role> roles = new ArrayList<Role>();
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("RM_ROLE");

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Role role = new Role();
				setProperties(role, rs);
				roles.add(role);
			}
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
		
		return roles;
		
	}
	
	public Collection<Role> getRolesByName(String name) throws Exception {
		Collection<Role> roles = new ArrayList<Role>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM "+ getFullTableName("RM_ROLE");
		if(!StringUtil.isBlank(name)){
			sql += " WHERE NAME like '%"+name+"%'";
		}

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Role role = new Role();
				setProperties(role, rs);
				roles.add(role);
			}
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
		
		return roles;
		
	}
	
	public Collection<Role> queryRolesByUser(String userId) throws Exception {
		Collection<Role> roles = new ArrayList<Role>();
		PreparedStatement stmt = null;

		String sql = "SELECT vo.* FROM " + getFullTableName("RM_ROLE") + " vo";
		sql += " WHERE vo.ID in (select s.ROLEID from " + getFullTableName("KM_USER_ROLE_SET")+" s";
		sql += " WHERE s.USERID=?)";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Role role = new Role();
				setProperties(role, rs);
				roles.add(role);
			}
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
		return roles;
	}

	protected String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException {
		return null;
	}

}
