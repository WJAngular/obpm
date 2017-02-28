package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.permission.ejb.ManagePermission;
import cn.myapps.km.permission.ejb.ManagePermissionItem;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.km.util.Sequence;

/**
 * @author xiuwei
 *
 */
public class AbstractManagePermissionDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractManagePermissionDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		ManagePermission permission = (ManagePermission) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_MANAGE_PERMISSION")
				+ " (ID,NAME,DOMAIN_ID,RESOURCE_TYPE,RESOURCE_ID,SCOPE,OWNERIDS,OWNERNAMES) values (?,?,?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, permission.getId());
			stmt.setString(2, permission.getName());
			stmt.setString(3, permission.getDomainId());
			stmt.setString(4, permission.getResourceType());
			stmt.setString(5, permission.getResource());
			stmt.setString(6, permission.getScope());
			stmt.setString(7, permission.getOwnerIds());
			stmt.setString(8, permission.getOwnerNames());

			stmt.execute();
			
			cascadeCreatePermissionItem(permission);
			
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	private void cascadeCreatePermissionItem(ManagePermission permission) throws Exception {
		
		PreparedStatement stmt = null;
		String sql = "INSERT INTO "
			+ getFullTableName("KM_MANAGE_PERMISSION_ITEM")
			+ " (ID,DOMAIN_ID,RESOURCE_TYPE,RESOURCE_ID,SCOPE,OWNER,PERMISSION) values (?,?,?,?,?,?,?)";
		
		try {
			String[] owners = permission.getOwnerIds().split(",");
			for (int i = 0; i < owners.length; i++) {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, Sequence.getSequence());
				stmt.setString(2, permission.getDomainId());
				stmt.setString(3, permission.getResourceType());
				stmt.setString(4, permission.getResource());
				stmt.setString(5, permission.getScope());
				stmt.setString(6, owners[i].trim());
				stmt.setString(7, permission.getId());

				stmt.execute();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
		
	}
	
	private void cascadeRemovePermissionItem(String permissionId) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_MANAGE_PERMISSION_ITEM")
				+ " WHERE PERMISSION=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, permissionId);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		ManagePermission permission = (ManagePermission) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_MANAGE_PERMISSION")
				+ " SET NAME=?,DOMAIN_ID=?,RESOURCE_TYPE=?,RESOURCE_ID=?,SCOPE=?,OWNERIDS=?,OWNERNAMES=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, permission.getName());
			stmt.setString(2, permission.getDomainId());
			stmt.setString(3, permission.getResourceType());
			stmt.setString(4, permission.getResource());
			stmt.setString(5, permission.getScope());
			stmt.setString(6, permission.getOwnerIds());
			stmt.setString(7, permission.getOwnerNames());
			stmt.setString(8, permission.getId());

			stmt.execute();
			
			cascadeRemovePermissionItem(permission.getId());
			cascadeCreatePermissionItem(permission);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public ManagePermission find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("KM_MANAGE_PERMISSION") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				ManagePermission permission = new ManagePermission();
				setProperties(permission, rs);
				return permission;
			}
			rs.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(ManagePermission permission, ResultSet rs) throws Exception {
		try {
			permission.setId(rs.getString("ID"));
			permission.setName(rs.getString("NAME"));
			permission.setDomainId(rs.getString("DOMAIN_ID"));
			permission.setResourceType(rs.getString("RESOURCE_TYPE"));
			permission.setResource(rs.getString("RESOURCE_ID"));
			permission.setScope(rs.getString("SCOPE"));
			permission.setOwnerIds(rs.getString("OWNERIDS"));
			permission.setOwnerNames(rs.getString("OWNERNAMES"));

		} catch (SQLException e) {
			throw e;
		}

	}
	
	void setProperties(ManagePermissionItem permission, ResultSet rs) throws Exception {
		try {
			permission.setId(rs.getString("ID"));
			permission.setDomainId(rs.getString("DOMAIN_ID"));
			permission.setResourceType(rs.getString("RESOURCE_TYPE"));
			permission.setResource(rs.getString("RESOURCE_ID"));
			permission.setScope(rs.getString("SCOPE"));
			permission.setOwner(rs.getString("OWNER"));
			permission.setPermission(rs.getString("PERMISSION"));

		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_MANAGE_PERMISSION")
				+ " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
			
			cascadeRemovePermissionItem(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public Collection<ManagePermissionItem> queryAllPermissionItems() throws Exception {
		Collection<ManagePermissionItem> rtn = new java.util.ArrayList<ManagePermissionItem>();
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM "
				+ getFullTableName("KM_MANAGE_PERMISSION_ITEM");

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ManagePermissionItem item = new ManagePermissionItem();
				setProperties(item, rs);
				rtn.add(item);
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
	
	public DataPackage<ManagePermission> queryByResource(String resource) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<ManagePermission> result = new DataPackage<ManagePermission>();

		String sql = "SELECT * FROM "
				+ getFullTableName("KM_MANAGE_PERMISSION")+ " WHERE RESOURCE_ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, resource);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ManagePermission permission = new ManagePermission();
				setProperties(permission, rs);
				result.getDatas().add(permission);
			}
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public DataPackage<ManagePermission> query() throws Exception {
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

}
