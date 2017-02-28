package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.permission.ejb.DirAccess;
import cn.myapps.km.permission.ejb.Permission;
import cn.myapps.km.util.PersistenceUtils;

public class AbstractDirAccessDAO {
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractDirAccessDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		DirAccess dirAccess = (DirAccess) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_DirAccess")
				+ " (ID,SCOPE,OWNERID,FILEID,PERMISSIONID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE) values (?,?,?,?,?,?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, dirAccess.getId());
			stmt.setString(2, dirAccess.getScope());
			stmt.setString(3, dirAccess.getOwnerId());
			stmt.setString(4, dirAccess.getFileId());
			stmt.setString(5, dirAccess.getPermissionId());
			if (dirAccess.getStartDate() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(dirAccess.getStartDate().getTime());
				stmt.setTimestamp(6, ts);
			}
			if (dirAccess.getEndDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(dirAccess.getEndDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setInt(8, dirAccess.getReadMode());
			stmt.setInt(9, dirAccess.getWriteMode());
			stmt.setInt(10, dirAccess.getDownloadMode());
			stmt.setInt(11, dirAccess.getPrintMode());

			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		DirAccess dirAccess = (DirAccess) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_DirAccess")
				+ " SET SCOPE=?,OWNERID=?,FILEID=?,PERMISSIONID=?,STARTDATE=?,ENDDATE=?,READMODE=?,WRITEMODE=?,DOWNLOADMODE=?,PRINTMODE=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, dirAccess.getScope());
			stmt.setString(2, dirAccess.getOwnerId());
			stmt.setString(3, dirAccess.getFileId());
			stmt.setString(4, dirAccess.getPermissionId());
			if (dirAccess.getStartDate() == null) {
				stmt.setNull(5, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(dirAccess.getStartDate().getTime());
				stmt.setTimestamp(5, ts);
			}
			if (dirAccess.getEndDate() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(dirAccess.getEndDate().getTime());
				stmt.setTimestamp(6, ts);
			}
			stmt.setInt(7, dirAccess.getReadMode());
			stmt.setInt(8, dirAccess.getWriteMode());
			stmt.setInt(9, dirAccess.getDownloadMode());
			stmt.setInt(10, dirAccess.getPrintMode());
			stmt.setString(11, dirAccess.getId());

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public DirAccess find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT ID,SCOPE,OWNERID,FILEID,PERMISSIONID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE FROM "
				+ getFullTableName("KM_DirAccess") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				DirAccess dirAccess = new DirAccess();
				setProperties(dirAccess, rs);

				return dirAccess;
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(DirAccess dirAccess, ResultSet rs) throws Exception {
		try {
			dirAccess.setId(rs.getString("ID"));
			dirAccess.setScope(rs.getString("SCOPE"));
			dirAccess.setOwnerId(rs.getString("OWNERID"));
			dirAccess.setFileId(rs.getString("FILEID"));
			dirAccess.setPermissionId(rs.getString("PERMISSIONID"));
			dirAccess.setStartDate(rs.getTimestamp("STARTDATE"));
			dirAccess.setEndDate(rs.getTimestamp("ENDDATE"));
			dirAccess.setReadMode(rs.getInt("READMODE"));
			dirAccess.setWriteMode(rs.getInt("WRITEMODE"));
			dirAccess.setDownloadMode(rs.getInt("DOWNLOADMODE"));
			dirAccess.setPrintMode(rs.getInt("PRINTMODE"));

			rs.close();
		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_DirAccess")
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

	public DataPackage<DirAccess> query() throws Exception {
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
	 * 根据文件id、作用目标id,作用范围获取DirAccess
	 * 
	 * @param fileId
	 *            文件id
	 * @param ownerId
	 *            作用目标id
	 * @param scope
	 *            作用范围
	 * @return DirAccess
	 * @throws Exception
	 */
	public DirAccess findByOwner(String fileId, String ownerId, String scope)
			throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT ID,SCOPE,OWNERID,FILEID,PERMISSIONID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE FROM "
				+ getFullTableName("KM_DirAccess")
				+ " WHERE FILEID=? AND OWNERID=? AND SCOPE=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileId);
			stmt.setString(2, ownerId);
			stmt.setString(3, scope);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				DirAccess dirAccess = new DirAccess();
				setProperties(dirAccess, rs);

				return dirAccess;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * @param permissionId
	 * @throws Exception
	 */
	public void removeByPermission(String permissionId) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_DirAccess")
				+ " WHERE PERMISSIONID=?";

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
}
