package cn.myapps.km.disk.dao;

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
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.util.StringUtil;

public class AbstractNDirDAO {
	
	Logger log = Logger.getLogger(getClass());
	
	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractNDirDAO(Connection conn) throws Exception {
		this.connection = conn;
	}
	
	public void create(NObject vo) throws Exception {
		NDir nDir = (NDir) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName("KM_NDIR") + " (ID,OWNER_ID,NAME,PARENT_ID,NDISK_ID,PATH,CREATEDATE,TYPE,DOMAIN_ID) values (?,?,?,?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nDir.getId());
			stmt.setString(2, nDir.getOwnerId());
			stmt.setString(3, nDir.getName());
			stmt.setString(4, nDir.getParentId());
			stmt.setString(5, nDir.getnDiskId());
			stmt.setString(6, nDir.getPath());
			if (nDir.getCreateDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(nDir.getCreateDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setInt(8, nDir.getType());
			stmt.setString(9, nDir.getDomainId());

			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public void update(NObject vo) throws Exception {
		NDir nDir = (NDir) vo;
		PreparedStatement stmt = null;
		
		String sql = "UPDATE " + getFullTableName("KM_NDIR") + " SET ID=?,OWNER_ID=?,NAME=?,PARENT_ID=?,NDISK_ID=?,PATH=?,CREATEDATE=?,DOMAIN_ID=? WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nDir.getId());
			stmt.setString(2, nDir.getOwnerId());
			stmt.setString(3, nDir.getName());
			stmt.setString(4, nDir.getParentId());
			stmt.setString(5, nDir.getnDiskId());
			stmt.setString(6, nDir.getPath());
			if (nDir.getCreateDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(nDir.getCreateDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setString(8, nDir.getDomainId());
			stmt.setString(9, nDir.getId());
			
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public NDir find(String id) throws Exception {
		PreparedStatement stmt = null;
		NDir dir = new NDir();
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			
			rs = stmt.executeQuery();
			if(rs.next()){
				this.setNdirProperty(dir, rs);
			}
			
			return dir;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	private void setNdirProperty(NDir dir, ResultSet rs) throws Exception{
		dir.setId(rs.getString("id"));
		dir.setOwnerId(rs.getString("owner_id"));
		dir.setName(rs.getString("name"));
		dir.setParentId(rs.getString("parent_id"));
		dir.setnDiskId(rs.getString("ndisk_id"));
		dir.setPath(rs.getString("path"));
		dir.setCreateDate(rs.getDate("createdate"));
		dir.setType(rs.getInt("type"));
		dir.setDomainId(rs.getString("DOMAIN_ID"));
	}

	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("KM_NDIR") + " WHERE ID=?";

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

	public DataPackage<NDir> query() throws Exception {
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
	 * 根据父目录查找下级目录及文件
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public Collection<IFile> getUnderNDirAndNFile(String parentid) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<IFile> iFiles = new ArrayList<IFile>();
		
		String sql = "SELECT d.id,d.name,d.owner_id as ownerid,d.createdate,filesize=null,filetype='1',creator=null,origin=0 FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID=?" +
					 " UNION " +
					 "SELECT f.id,f.name,f.ownerid,f.createdate,f.filesize,filetype='2',f.creator,f.origin FROM " + getFullTableName("KM_NFILE") + " f WHERE NDIRID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, parentid);
			stmt.setString(2, parentid);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				IFile iFile = new IFile();
				iFile.setId(rs.getString("id"));
				iFile.setName(rs.getString("name"));
				iFile.setFileType(rs.getString("filetype"));
				iFile.setCreateDate(rs.getTimestamp("createdate"));
				iFile.setSize(rs.getLong("filesize"));
				iFile.setCreator(rs.getString("creator"));
				iFile.setOwnerId(rs.getString("ownerid"));
				iFile.setOrigin(rs.getInt("origin"));
				iFiles.add(iFile);
			}
			
			return iFiles;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return null;
	}
	
	/**
	 * 查找下级目录
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public Collection<NDir> getUnderNDir(String parentid) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<NDir> dirs = new ArrayList<NDir>();
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, parentid);
			rs = stmt.executeQuery();
			
			while(rs != null && rs.next()){
				NDir dir = new NDir();
				setNdirProperty(dir, rs);
				dirs.add(dir);
			}
			
			return dirs;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return null;
	}
	
	public Collection<NDir> getUnderNDirByDisk(String ndiskid, String parentid) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<NDir> dirs = new ArrayList<NDir>();
		
		String sql = "";
		if(!StringUtil.isBlank(parentid)){
			sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID='" + parentid + "' AND NDISK_ID=?";
		}else {
			sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID IS NULL AND NDISK_ID=?";
		}
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, ndiskid);
			rs = stmt.executeQuery();
			
			while(rs != null && rs.next()){
				NDir dir = new NDir();
				setNdirProperty(dir, rs);
				dirs.add(dir);
			}
			
			return dirs;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return null;
	}
	
	public long countUnderNDir(String ndiskid, String parentid) throws Exception{
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID=? AND NDISK_ID=?";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, parentid);
			statement.setString(2, ndiskid);
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
	 * 分页查找下级目录及文件
	 * @param page
	 * @param lines
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<IFile> getUnderNDirAndNFile(int page, int lines, String parentid,String orderbyFile,String orderbyMode) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<IFile> datas = new DataPackage<IFile>();
		Collection<IFile> iFiles = new ArrayList<IFile>();
		
		String sql = "SELECT d.id,d.name,d.createdate,filesize=null,filetype='1',creator=null FROM " + getFullTableName("KM_NDIR") + " d WHERE PARENT_ID=?" +
					 " UNION " +
					 "SELECT f.id,f.name,f.createdate,f.filesize,filetype='2',creator FROM " + getFullTableName("KM_NFILE") + " f WHERE NDIRID=?";
		
		//拼接MsSql的分页语句
		String querySql = buildLimitString(sql, page, lines, orderbyFile, orderbyMode);
		
		log.info(querySql);
		
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, parentid);
			stmt.setString(2, parentid);
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
			datas.rowCount = (int) countBySQL(sql, parentid);
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
	
	/**
	 * 生成限制条件sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @return 生成限制条件sql语句字符串
	 * @throws SQLException
	 */
	public String buildLimitString(String sql, int page, int lines, String orderbyFile, String orderbyMode)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		// int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		int databaseVersion = connection.getMetaData()
				.getDatabaseMajorVersion();
		if (9 <= databaseVersion) {// 2005 row_number() over () 分页
			pagingSelect.append("SELECT TOP " + lines + " * FROM (");
			pagingSelect
					.append("SELECT ROW_NUMBER() OVER (ORDER BY ").append(buildOrderbyString(orderbyFile, orderbyMode)).append(") AS ROWNUMBER, TABNIC.* FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC) TableNickname ");
			pagingSelect.append("WHERE ROWNUMBER>" + lines * (page - 1));

		} else {
			pagingSelect.append("SELECT TOP " + lines * page + " * FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC");
		}

		return pagingSelect.toString();
	}
	
	String buildOrderbyString(String orderbyFile,String orderbyMode){
		StringBuffer rtn = new StringBuffer();
		rtn.append("filetype");//
		rtn.append(",");
		rtn.append(orderbyFile).append(" ").append(orderbyMode);
		return rtn.toString();
	}
	
	public Collection<NDir> getNDirByNDisk(String ndiskid) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<NDir> dirs = new ArrayList<NDir>();
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " d WHERE NDISK_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, ndiskid);
			rs = stmt.executeQuery();
			
			while(rs != null && rs.next()){
				NDir dir = new NDir();
				setNdirProperty(dir, rs);
				dirs.add(dir);
			}
			
			return dirs;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return null;
	}
	
	public long countBySQL(String sql, String parentid) throws Exception {
		if (sql == null || sql.trim().equals(""))
			return 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		sql = "SELECT COUNT(*)  FROM (" + sql + ") AS T";

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, parentid);
			statement.setString(2, parentid);
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
	 * 移动目录
	 * @param parentid
	 * @param dirids
	 * @throws Exception
	 */
	public void moveNDir(String parentid, String[] dirids) throws Exception{
		PreparedStatement stmt = null;
		
		StringBuffer ids = new StringBuffer();
		if(dirids.length > 0){
			for(int i=0; i<dirids.length; i++){
				ids.append("'").append(dirids[i]).append("',");
			}
		}
		
		if(ids.length() > 0)
			ids.setLength(ids.length()-1);
		
		String sql = "UPDATE " + getFullTableName("KM_NDIR") + " SET PARENT_ID=? WHERE ID IN (" + ids + ")";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, parentid);
			
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public NDir getPublicShareNDir(String ndiskid) throws Exception{
		PreparedStatement stmt = null;
		NDir dir = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " WHERE TYPE=? AND NAME=? AND NDISK_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, 2);
			stmt.setString(2, NDir.USER_SHARE);
			stmt.setString(3, ndiskid);
			
			rs = stmt.executeQuery();
			if(rs.next()){
				dir = new NDir();
				this.setNdirProperty(dir, rs);
			}
			
			return dir;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public NDir getPersonalShareNDir(String ownerid) throws Exception{
		PreparedStatement stmt = null;
		NDir dir = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " WHERE OWNER_ID=? AND TYPE=? AND NAME=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, ownerid);
			stmt.setInt(2, NDir.TYPE_SYSTEM);
			stmt.setString(3, NDir.USER_SHARE);
			
			rs = stmt.executeQuery();
			if(rs.next()){
				dir = new NDir();
				this.setNdirProperty(dir, rs);
			}
			
			return dir;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public NDir findPersonalFavoritesNDir(String ownerid) throws Exception {
		PreparedStatement stmt = null;
		NDir dir = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " WHERE OWNER_ID=? AND TYPE=? AND NAME=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, ownerid);
			stmt.setInt(2, NDir.TYPE_SYSTEM);
			stmt.setString(3, NDir.MY_COLLECTION);
			
			rs = stmt.executeQuery();
			if(rs.next()){
				 dir = new NDir();
				this.setNdirProperty(dir, rs);
			}
			
			return dir;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public NDir findPersonalRecommendNDir(String ownerid) throws Exception {
		PreparedStatement stmt = null;
		NDir dir = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_NDIR") + " WHERE OWNER_ID=? AND TYPE=? AND NAME=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, ownerid);
			stmt.setInt(2, NDir.TYPE_SYSTEM);
			stmt.setString(3, NDir.RECOMMEND_DOCUMENT);
			
			rs = stmt.executeQuery();
			if(rs.next()){
				 dir = new NDir();
				this.setNdirProperty(dir, rs);
			}
			
			return dir;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public void doRemoveNdirByRemoveUser(String userid) throws Exception{
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("KM_NDIR") + " WHERE OWNER_ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userid);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			PersistenceUtils.closeStatement(stmt);
		}
	}
}
