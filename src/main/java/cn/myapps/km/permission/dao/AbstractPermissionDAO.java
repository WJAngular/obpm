package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.permission.ejb.Permission;
import cn.myapps.km.util.PersistenceUtils;

/**
 * @author xiuwei
 *
 */
public class AbstractPermissionDAO {
	
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractPermissionDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		Permission permission = (Permission) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_PERMISSION")
				+ " (ID,NAME,SCOPE,OWNERIDS,OWNERNAMES,FILETYPE,FILEID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE,USERID,ROLEID,DEPTID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, permission.getId());
			stmt.setString(2, permission.getName());
			stmt.setString(3, permission.getScope());
			stmt.setString(4, permission.getOwnerIds());
			stmt.setString(5, permission.getOwnerNames());
			stmt.setInt(6, permission.getFileType());
			stmt.setString(7, permission.getFileId());
			if (permission.getStartDate() == null) {
				stmt.setNull(8, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(permission.getStartDate().getTime());
				stmt.setTimestamp(8, ts);
			}
			if (permission.getEndDate() == null) {
				stmt.setNull(9, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(permission.getEndDate().getTime());
				stmt.setTimestamp(9, ts);
			}
			stmt.setInt(10, permission.getReadMode());
			stmt.setInt(11, permission.getWriteMode());
			stmt.setInt(12, permission.getDownloadMode());
			stmt.setInt(13, permission.getPrintMode());
			stmt.setString(14, permission.getUserId());
			stmt.setString(15, permission.getRoleId());
			stmt.setString(16, permission.getDeptId());
			
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		Permission permission = (Permission) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_PERMISSION")
				+ " SET NAME=?, SCOPE=?,OWNERIDS=?,OWNERNAMES=?,FILETYPE=?,FILEID=?,STARTDATE=?,ENDDATE=?,READMODE=?,WRITEMODE=?,DOWNLOADMODE=?,PRINTMODE=?,USERID=?,ROLEID=?,DEPTID=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, permission.getName());
			stmt.setString(2, permission.getScope());
			stmt.setString(3, permission.getOwnerIds());
			stmt.setString(4, permission.getOwnerNames());
			stmt.setInt(5, permission.getFileType());
			stmt.setString(6, permission.getFileId());
			if (permission.getStartDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(permission.getStartDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			if (permission.getEndDate() == null) {
				stmt.setNull(8, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(permission.getEndDate().getTime());
				stmt.setTimestamp(8, ts);
			}
			stmt.setInt(9, permission.getReadMode());
			stmt.setInt(10, permission.getWriteMode());
			stmt.setInt(11, permission.getDownloadMode());
			stmt.setInt(12, permission.getPrintMode());
			stmt.setString(13, permission.getUserId());
			stmt.setString(14, permission.getRoleId());
			stmt.setString(15, permission.getDeptId());
			stmt.setString(16, permission.getId());

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public Permission find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT ID,NAME,SCOPE,OWNERIDS,OWNERNAMES,FILETYPE,FILEID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE,USERID,ROLEID,DEPTID FROM "
				+ getFullTableName("KM_PERMISSION") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Permission permission = new Permission();
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

	void setProperties(Permission permission, ResultSet rs) throws Exception {
		try {
			permission.setId(rs.getString("ID"));
			permission.setName(rs.getString("NAME"));
			permission.setScope(rs.getString("SCOPE"));
			permission.setOwnerIds(rs.getString("OWNERIDS"));
			permission.setOwnerNames(rs.getString("OWNERNAMES"));
			permission.setFileType(rs.getInt("FILETYPE"));
			permission.setFileId(rs.getString("FILEID"));
			permission.setStartDate(rs.getTimestamp("STARTDATE"));
			permission.setEndDate(rs.getTimestamp("ENDDATE"));
			permission.setReadMode(rs.getInt("READMODE"));
			permission.setWriteMode(rs.getInt("WRITEMODE"));
			permission.setDownloadMode(rs.getInt("DOWNLOADMODE"));
			permission.setPrintMode(rs.getInt("PRINTMODE"));
			permission.setUserId(rs.getString("USERID"));
			permission.setRoleId(rs.getString("ROLEID"));
			permission.setDeptId(rs.getString("DEPTID"));

		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_PERMISSION")
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

	public DataPackage<Permission> query() throws Exception {
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

	/**
	 * 根据文件id、获取Permissions
	 * 
	 * @param fileId
	 *            文件id
	 * @param ownerId
	 * @return Permission
	 * @throws Exception
	 */
	public DataPackage<Permission> queryByFile(String fileId)
			throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Permission> result = new DataPackage<Permission>();

		String sql = "SELECT ID,NAME,SCOPE,OWNERIDS,OWNERNAMES,FILETYPE,FILEID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE,USERID,ROLEID,DEPTID FROM "
				+ getFullTableName("KM_PERMISSION")
				+ " WHERE FILEID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileId);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Permission permission = new Permission();
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
	
	/**
	 * 
	 * @param fileId
	 * 			文件ID
	 * @param ownerId
	 * 			权限拥有者ID（用户ID 或 角色ID）
	 * @return
	 * @throws Exception
	 */
	public Permission findByFileAndOwner(String fileId, String ownerId)
			throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		String sql = "SELECT ID,NAME,SCOPE,OWNERIDS,OWNERNAMES,FILETYPE,FILEID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE,USERID,ROLEID,DEPTID FROM "
				+ getFullTableName("KM_PERMISSION")
				+ " WHERE FILEID = ? AND OWNERIDS = ?";
		log.info(sql);		
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileId);
			stmt.setString(2, ownerId);
			
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Permission permission = new Permission();
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
	
	/**
	 * 根据文件id、获取Permissions
	 * 
	 * @param fileId
	 *            文件id
	 * @param ownerId
	 * @return Permission
	 * @throws Exception
	 */
	public DataPackage<Permission> query(String fileId, String scope,String ownerName, int readOnly, int download)
			throws Exception {
		PreparedStatement stmt = null;
		DataPackage<Permission> result = new DataPackage<Permission>();
		
		//查询条件标记
		int mark = 1;
		
		//查询条件数量
		int conditionCount = 1;
		
		String sql = "SELECT ID,NAME,SCOPE,OWNERIDS,OWNERNAMES,FILETYPE,FILEID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE,USERID,ROLEID,DEPTID  FROM "
				+ getFullTableName("KM_PERMISSION")
				+ " WHERE FILEID = ?";
		
		if (scope!=null && scope.trim().length()>0) {
			sql += " AND SCOPE = ?";
			mark += 2;
			conditionCount++;
		}
		
		if (ownerName!=null && ownerName.trim().length()>0) {
			sql += " AND OWNERNAMES LIKE ?";
			mark += 4;
			conditionCount++;
		}
		
		if (readOnly!=0) {
			sql += " AND READMODE = ?";
			mark +=8;
			conditionCount++;
		}
		
		if (download!=0) {
			sql += " AND DOWNLOADMODE = ?";
			mark += 16;
			conditionCount++;
		}
		

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileId);
			
			switch(mark) { 
				case 1 :
					stmt.setString(1, fileId);
					break;
				case 3 :
					stmt.setString(1, fileId);
					stmt.setString(2, scope);
					break;
				case 5 :
					stmt.setString(1, fileId);
					stmt.setString(2, "%"+ownerName+"%");
					break;
				case 7:
					if (conditionCount==3) {
						stmt.setString(1, fileId);
						stmt.setString(2, scope);
						stmt.setString(3, "%"+ownerName+"%");
					}
					break;	
				case 9 :
					stmt.setString(1, fileId);
					stmt.setInt(2, readOnly);
					break;
				case 11:
					if (conditionCount==3) {
						stmt.setString(1, fileId);
						stmt.setString(2, scope);
						stmt.setInt(3, readOnly);
					}
					break;	
				case 13:
					if (conditionCount==3) {
						stmt.setString(1, fileId);
						stmt.setString(2, "%"+ownerName+"%");
						stmt.setInt(3, readOnly);
					}
					break;	
				case 15:
					if (conditionCount==4) {
						stmt.setString(1, fileId);
						stmt.setString(2, scope);
						stmt.setString(3, "%"+ownerName+"%");
						stmt.setInt(4, readOnly);
					}
					break;	
				case 17 :
					if (conditionCount==2) {
						stmt.setString(1, fileId);
						stmt.setInt(2, download);
					} 
					break;		
				case 19:
					if (conditionCount==3) {
						stmt.setString(1, fileId);
						stmt.setString(2, scope);
						stmt.setInt(3, download);
					}
					break;	
				case 21:
					if (conditionCount==3) {
						stmt.setString(1, fileId);
						stmt.setString(2, "%"+ownerName+"%");
						stmt.setInt(3, download);
					}
					break;
				case 23:
					if (conditionCount==4) {
						stmt.setString(1, fileId);
						stmt.setString(2, scope);
						stmt.setString(3, "%"+ownerName+"%");
						stmt.setInt(4, download);
					}
					break;	
				case 25:
					if (conditionCount==3) {
						stmt.setString(1, fileId);
						stmt.setInt(2, readOnly);
						stmt.setInt(3, download);
					}
					break;
				case 27:
					if (conditionCount==4) {
						stmt.setString(1, fileId);
						stmt.setString(2, scope);
						stmt.setInt(3, readOnly);
						stmt.setInt(4, download);
					}
					break;		
				case 29:
					if (conditionCount==4) {
						stmt.setString(1, fileId);
						stmt.setString(2, "%"+ownerName+"%");
						stmt.setInt(3, readOnly);
						stmt.setInt(4, download);
					}
					break;
				case 31:
					if (conditionCount==5) {
						stmt.setString(1, fileId);
						stmt.setString(2, scope);
						stmt.setString(3, "%"+ownerName+"%");
						stmt.setInt(4, readOnly);
						stmt.setInt(5, download);
					}
					break;	
				default:
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Permission permissions = new Permission();
				setProperties(permissions, rs);
				result.getDatas().add(permissions);
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
}
