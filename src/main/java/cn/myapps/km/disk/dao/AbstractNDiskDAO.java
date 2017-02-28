package cn.myapps.km.disk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.util.StringUtil;

public abstract class AbstractNDiskDAO {
	Logger log = Logger.getLogger(getClass());
	
	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractNDiskDAO(Connection conn) throws Exception {
		this.connection = conn;
	}
	
	public void create(NObject vo) throws Exception {
		NDisk nDisk = (NDisk) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName("KM_NDISK") + " (ID,DOMAIN_ID,NAME,OWNER_ID,TYPE,DIR_ID) values (?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nDisk.getId());
			stmt.setString(2, nDisk.getDomainId());
			stmt.setString(3, nDisk.getName());
			stmt.setString(4, nDisk.getOwnerId());
			stmt.setInt(5, nDisk.getType());
			stmt.setString(6, nDisk.getnDirId());

			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public void update(NObject vo) throws Exception {
		NDisk nDisk = (NDisk) vo;
		PreparedStatement stmt = null;
		
		String sql = "UPDATE " + getFullTableName("KM_NDISK") + " SET ID=?,DOMAIN_ID=?,NAME=?,TYPE=?,OWNER_ID=?,DIR_ID=? WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nDisk.getId());
			stmt.setString(2, nDisk.getDomainId());
			stmt.setString(3, nDisk.getName());
			stmt.setInt(4, nDisk.getType());
			stmt.setString(5, nDisk.getOwnerId());
			stmt.setString(6, nDisk.getnDirId());
			stmt.setString(7, nDisk.getId());
			
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public NDisk find(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDISK") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				return setProperty(rs);
				
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			PersistenceUtils.closeStatement(stmt);
		}
		
	}
	
	NDisk setProperty(ResultSet rs) throws Exception{
		NDisk nDisk = new NDisk();
		try {
			nDisk.setId(rs.getString("id"));
			nDisk.setDomainId(rs.getString("domain_id"));
			nDisk.setName(rs.getString("name"));
			nDisk.setType(rs.getShort("type"));
			nDisk.setOwnerId(rs.getString("owner_id"));
			nDisk.setnDirId(rs.getString("dir_id"));
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return nDisk;
	}
	

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("KM_NDISK") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public DataPackage<NDisk> query() throws Exception {
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
	
	public NDisk getNDiskByUser(String userid) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDISK") + " WHERE OWNER_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userid);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				return setProperty(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			PersistenceUtils.closeStatement(stmt);
		}
		return null;
	}
	
	/**
	 * 获取公有网盘
	 * @return
	 * @throws Exception
	 */
	public NDisk getPublicDisk(String domainid) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDISK") + " WHERE TYPE=? and DOMAIN_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setString(2, domainid);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				return setProperty(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			PersistenceUtils.closeStatement(stmt);
		}
		return null;
	}
	
	public DataPackage<IFile> getHotestFiles(int page, int lines,
			String domainid, String categoryID) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<IFile> datas = new DataPackage<IFile>();
		Collection<IFile> iFiles = new ArrayList<IFile>();
		
		String sql = "SELECT f.creator ,f.id,f.name,f.createdate,f.filesize,'2' filetype, favorites, downloads, views FROM " + getFullTableName("KM_NFILE") + " f WHERE DOMAIN_ID=? AND ORIGIN='1' AND STATE='1'";
		
		if(!StringUtil.isBlank(categoryID)){
			sql = sql + " AND ROOT_CATEGORY_ID=?";
		}
		
		//拼接MsSql的分页语句
		String querySql = buildLimitString(sql, page, lines, "favorites,downloads,views", "DESC");
		
		log.info(querySql);
		
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, domainid);
			if(!StringUtil.isBlank(categoryID)){
				stmt.setString(2, categoryID);
			}
			rs = stmt.executeQuery();
			
			while(rs.next()){
				IFile iFile = new IFile();
				iFile.setId(rs.getString("id"));
				iFile.setName(rs.getString("name"));
				iFile.setFileType(rs.getString("filetype"));
				iFile.setCreateDate(rs.getTimestamp("createdate"));
				iFile.setSize(rs.getLong("filesize"));
				iFile.setCreator(rs.getString("creator"));
				
				iFiles.add(iFile);
			}
			
			datas.datas = iFiles;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, domainid, categoryID);
			datas.pageNo = page;
			return datas;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return null;
	}
	
	public DataPackage<IFile> doListNewestFile(int page, int lines, String domainid, String categoryID)
		throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<IFile> datas = new DataPackage<IFile>();
		Collection<IFile> iFiles = new ArrayList<IFile>();
		
//		String sql = "SELECT f.id,f.name,f.createdate,f.filesize,'2' filetype, favorites, downloads, views FROM " + getFullTableName("KM_NFILE") + " f WHERE DOMAIN_ID=? AND STATE='1' AND ORIGIN='0' ORDER BY createdate DESC";
		
		String sql = "SELECT f.creator ,f.id,f.name,f.createdate,f.filesize,'2' filetype, favorites, downloads, views FROM " + getFullTableName("KM_NFILE") + " f WHERE DOMAIN_ID=? AND STATE='1' AND ORIGIN='0'";
		
		if(!StringUtil.isBlank(categoryID)){
			sql = sql + " AND ROOT_CATEGORY_ID=?";
		}
		
		sql = sql + " ORDER BY createdate DESC";
		//拼接MsSql的分页语句
		String querySql = buildLimitString(sql, page, lines, "createdate", "DESC");
		
		log.info(querySql);
		
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, domainid);
			if(!StringUtil.isBlank(categoryID)){
				stmt.setString(2, categoryID);
			}
			rs = stmt.executeQuery();
			
			while(rs.next()){
				IFile iFile = new IFile();
				iFile.setId(rs.getString("id"));
				iFile.setName(rs.getString("name"));
				iFile.setFileType(rs.getString("filetype"));
				iFile.setCreateDate(rs.getTimestamp("createdate"));
				iFile.setSize(rs.getLong("filesize"));
				iFile.setCreator(rs.getString("creator"));
				
				iFiles.add(iFile);
			}
			
			datas.datas = iFiles;
			datas.linesPerPage = lines;
			datas.rowCount = (int) countBySQL(sql, domainid, categoryID);
			datas.pageNo = page;
			return datas;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return null;
	}
	
	protected abstract String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException ;

	public long countBySQL(String sql, String domainid, String categoryID) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ") T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, domainid);
			if(!StringUtil.isBlank(categoryID)){
				statement.setString(2, categoryID);
			}
			rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}

	/**
	 * 获取归档网盘
	 * @return
	 * @throws Exception
	 */
	public NDisk getArchiveDisk(String domainid) throws Exception{
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDISK") + " WHERE TYPE=? and DOMAIN_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, 5);
			stmt.setString(2, domainid);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				return setProperty(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			PersistenceUtils.closeStatement(stmt);
		}
		return null;
	}
}
