package cn.myapps.km.permission.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.permission.ejb.FileAccess;
import cn.myapps.km.permission.ejb.Permission;
import cn.myapps.km.util.PersistenceUtils;

public class AbstractFileAccessDAO {
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractFileAccessDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		FileAccess fileAccess = (FileAccess) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_FILEACCESS")
				+ " (ID,SCOPE,OWNERID,FILEID,PERMISSIONID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE) values (?,?,?,?,?,?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileAccess.getId());
			stmt.setString(2, fileAccess.getScope());
			stmt.setString(3, fileAccess.getOwnerId());
			stmt.setString(4, fileAccess.getFileId());
			stmt.setString(5, fileAccess.getPermissionId());
			if (fileAccess.getStartDate() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(fileAccess.getStartDate().getTime());
				stmt.setTimestamp(6, ts);
			}
			if (fileAccess.getEndDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(fileAccess.getEndDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setInt(8, fileAccess.getReadMode());
			stmt.setInt(9, fileAccess.getWriteMode());
			stmt.setInt(10, fileAccess.getDownloadMode());
			stmt.setInt(11, fileAccess.getPrintMode());

			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		FileAccess fileAccess = (FileAccess) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_FILEACCESS")
				+ " SET SCOPE=?,OWNERID=?,FILEID=?,PERMISSIONID=?,STARTDATE=?,ENDDATE=?,READMODE=?,WRITEMODE=?,DOWNLOADMODE=?,PRINTMODE=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileAccess.getScope());
			stmt.setString(2, fileAccess.getOwnerId());
			stmt.setString(3, fileAccess.getFileId());
			stmt.setString(4, fileAccess.getPermissionId());
			if (fileAccess.getStartDate() == null) {
				stmt.setNull(5, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(fileAccess.getStartDate().getTime());
				stmt.setTimestamp(5, ts);
			}
			if (fileAccess.getEndDate() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(fileAccess.getEndDate().getTime());
				stmt.setTimestamp(6, ts);
			}
			stmt.setInt(7, fileAccess.getReadMode());
			stmt.setInt(8, fileAccess.getWriteMode());
			stmt.setInt(9, fileAccess.getDownloadMode());
			stmt.setInt(10, fileAccess.getPrintMode());
			stmt.setString(11, fileAccess.getId());

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public FileAccess find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT ID,SCOPE,OWNERID,FILEID,PERMISSIONID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE FROM "
				+ getFullTableName("KM_FILEACCESS") + " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				FileAccess fileAccess = new FileAccess();
				setProperties(fileAccess, rs);

				return fileAccess;
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	void setProperties(FileAccess fileAccess, ResultSet rs) throws Exception {
		try {
			fileAccess.setId(rs.getString("ID"));
			fileAccess.setScope(rs.getString("SCOPE"));
			fileAccess.setOwnerId(rs.getString("OWNERID"));
			fileAccess.setFileId(rs.getString("FILEID"));
			fileAccess.setPermissionId(rs.getString("PERMISSIONID"));
			fileAccess.setStartDate(rs.getTimestamp("STARTDATE"));
			fileAccess.setEndDate(rs.getTimestamp("ENDDATE"));
			fileAccess.setReadMode(rs.getInt("READMODE"));
			fileAccess.setWriteMode(rs.getInt("WRITEMODE"));
			fileAccess.setDownloadMode(rs.getInt("DOWNLOADMODE"));
			fileAccess.setPrintMode(rs.getInt("PRINTMODE"));

			rs.close();
		} catch (SQLException e) {
			throw e;
		}

	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_FILEACCESS")
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

	public DataPackage<FileAccess> query() throws Exception {
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
	 * 根据文件id、作用目标id,作用范围获取FileAccess
	 * 
	 * @param fileId
	 *            文件id
	 * @param ownerId
	 *            作用目标id
	 * @param scope
	 *            作用范围
	 * @return FileAccess
	 * @throws Exception
	 */
	public FileAccess findByOwner(String fileId, String ownerId, String scope)
			throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT ID,SCOPE,OWNERID,FILEID,PERMISSIONID,STARTDATE,ENDDATE,READMODE,WRITEMODE,DOWNLOADMODE,PRINTMODE FROM "
				+ getFullTableName("KM_FILEACCESS")
				+ " WHERE FILEID=? AND OWNERID=? AND SCOPE=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileId);
			stmt.setString(2, ownerId);
			stmt.setString(3, scope);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				FileAccess fileAccess = new FileAccess();
				setProperties(fileAccess, rs);

				return fileAccess;
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

		String sql = "DELETE FROM " + getFullTableName("KM_FILEACCESS")
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
