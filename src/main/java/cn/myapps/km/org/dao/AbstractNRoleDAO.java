package cn.myapps.km.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.km.util.StringUtil;

public abstract class AbstractNRoleDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractNRoleDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		NRole role = (NRole) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_ROLE")
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

	public void update(NObject vo) throws Exception {
		NRole role = (NRole) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_ROLE")
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

	public NRole find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("KM_ROLE") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			NRole role = null;
			if (rs.next()) {
				role = new NRole();
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

	void setProperties(NRole role, ResultSet rs) throws Exception {
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

		String sql = "DELETE FROM " + getFullTableName("KM_ROLE")
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
	
	public Collection<NRole> getRoles() throws Exception {
		Collection<NRole> roles = new ArrayList<NRole>();
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("KM_ROLE");

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				NRole role = new NRole();
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
	
	public Collection<NRole> getRolesByName(String name) throws Exception {
		Collection<NRole> roles = new ArrayList<NRole>();
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM "+ getFullTableName("KM_ROLE");
		if(!StringUtil.isBlank(name)){
			sql += " WHERE NAME like '%"+name+"%'";
		}

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				NRole role = new NRole();
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
	
	public Collection<NRole> queryRolesByUser(String userId) throws Exception {
		Collection<NRole> roles = new ArrayList<NRole>();
		PreparedStatement stmt = null;

		String sql = "SELECT vo.* FROM " + getFullTableName("KM_ROLE") + " vo";
		sql += " WHERE vo.ID in (select s.ROLEID from " + getFullTableName("KM_USER_ROLE_SET")+" s";
		sql += " WHERE s.USERID=?)";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				NRole role = new NRole();
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

}
