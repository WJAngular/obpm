package cn.myapps.km.disk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.PersistenceUtils;

/**
 * @author Happy
 *
 */
public abstract class AbstractNFileDAO {
	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractNFileDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		NFile nFile = (NFile) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "+getFullTableName("KM_NFILE") +" (ID,NAME,URL,CREATEDATE,STATE,OWNERID,VERSION,TYPE,MEMO,FILESIZE,LASTMODIFY,CREATORID,SHAREID,NDIRID,TITLE,ORIGIN,CREATOR,CLASSIFICATION,VIEWS,DOWNLOADS,FAVORITES,DOMAIN_ID,ROOT_CATEGORY_ID,SUB_CATEGORY_ID,DEPARTMENT,DEPARTMENT_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nFile.getId());
			stmt.setString(2, nFile.getName());
			stmt.setString(3, nFile.getUrl());
			if (nFile.getCreateDate() == null) {
				stmt.setNull(4, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(nFile.getCreateDate().getTime());
				stmt.setTimestamp(4, ts);
			}
			stmt.setInt(5, nFile.getState());
			stmt.setString(6, nFile.getOwnerId());
			stmt.setInt(7, nFile.getVersion());
			stmt.setString(8, nFile.getType());

			String text = nFile.getMemo();

			stmt.setString(9, text);

			stmt.setLong(10, nFile.getSize());

			if (nFile.getLastmodify() == null) {

				stmt.setNull(11, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(nFile.getLastmodify().getTime());

				stmt.setTimestamp(11, ts);
			}

			stmt.setString(12, nFile.getCreatorId());
			stmt.setString(13, nFile.getShareId());
			stmt.setString(14, nFile.getNDirId());
			stmt.setString(15, nFile.getTitle());
			stmt.setInt(16, nFile.getOrigin());
			stmt.setString(17, nFile.getCreator());
			stmt.setString(18, nFile.getClassification());
			stmt.setInt(19, nFile.getViews());
			stmt.setInt(20, nFile.getDownloads());
			stmt.setInt(21, nFile.getFavorites());
			stmt.setString(22, nFile.getDomainId());
			stmt.setString(23, nFile.getRootCategoryId());
			stmt.setString(24, nFile.getSubCategoryId());
			stmt.setString(25, nFile.getDepartment());
			stmt.setString(26, nFile.getDepartmentId());
			
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	
	public void update(NObject vo) throws Exception {
		NFile nFile = (NFile) vo;
		PreparedStatement stmt = null;
		
		String sql = "UPDATE "+getFullTableName("KM_NFILE") +" SET ID=?,NAME=?,URL=?,CREATEDATE=?,STATE=?,OWNERID=?,VERSION=?,TYPE=?,MEMO=?,FILESIZE=?,LASTMODIFY=?,CREATORID=?,SHAREID=?,NDIRID=?,TITLE=?,ORIGIN=?,CREATOR=?,CLASSIFICATION=?,VIEWS=?,DOWNLOADS=?,FAVORITES=?,DOMAIN_ID=?,GOOD=?,BAD=?,ROOT_CATEGORY_ID=?,SUB_CATEGORY_ID=?,DEPARTMENT=?,DEPARTMENT_ID=? WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nFile.getId());
			stmt.setString(2, nFile.getName());
			stmt.setString(3, nFile.getUrl());
			if (nFile.getCreateDate() == null) {
				stmt.setNull(4, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(nFile.getCreateDate().getTime());
				stmt.setTimestamp(4, ts);
			}
			stmt.setInt(5, nFile.getState());
			stmt.setString(6, nFile.getOwnerId());
			stmt.setInt(7, nFile.getVersion());
			stmt.setString(8, nFile.getType());

			String text = nFile.getMemo();

			stmt.setString(9, text);

			stmt.setLong(10, nFile.getSize());

			if (nFile.getLastmodify() == null) {

				stmt.setNull(11, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(nFile.getLastmodify().getTime());

				stmt.setTimestamp(11, ts);
			}

			stmt.setString(12, nFile.getCreatorId());
			stmt.setString(13, nFile.getShareId());
			stmt.setString(14, nFile.getNDirId());
			stmt.setString(15, nFile.getTitle());
			stmt.setInt(16, nFile.getOrigin());
			stmt.setString(17, nFile.getCreator());
			stmt.setString(18, nFile.getClassification());
			stmt.setInt(19, nFile.getViews());
			stmt.setInt(20, nFile.getDownloads());
			stmt.setInt(21, nFile.getFavorites());
			stmt.setString(22, nFile.getDomainId());
			stmt.setInt(23, nFile.getGood());
			stmt.setInt(24, nFile.getBad());
			stmt.setString(25, nFile.getRootCategoryId());
			stmt.setString(26, nFile.getSubCategoryId());
			stmt.setString(27, nFile.getDepartment());
			stmt.setString(28, nFile.getDepartmentId());
			stmt.setString(29, nFile.getId());

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}

	}
	
	public NFile find(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM "+getFullTableName("KM_NFILE") +" WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				NFile file = setPropety(rs);;
				rs.close();
				return file;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 查询多条数据
	 * @param userid 创建者id 
	 * @return NFile集合
	 * @throws Exception
	 */
	public DataPackage<NFile> query(String userid,String ndirid) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<NFile> result = new DataPackage<NFile>();
		String sql = "SELECT * FROM "+getFullTableName("KM_NFILE") +" WHERE CREATORID=? AND NDIRID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userid);
			stmt.setString(2, ndirid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 遍历改目录下全部文件
	 * @param userid 用户id
	 * @param ndirid 目录id
	 * @return
	 * @throws Exception
	 */
	public DataPackage<NFile> queryAllFile(String userid,String ndirid) throws Exception{
		PreparedStatement stmt = null;
		DataPackage<NFile> result = new DataPackage<NFile>();
		String sql = "SELECT * FROM "+getFullTableName("KM_NFILE") +" WHERE CREATORID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userid);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 查找收藏文件
	 * @param id 源文件id
	 * @return
	 */
	public DataPackage<NFile> queryShareFile(String id) throws Exception{
		PreparedStatement stmt = null;
		DataPackage<NFile> result = new DataPackage<NFile>();
		
		String sql = "SELECT * FROM "+getFullTableName("KM_NFILE") +" WHERE SHAREID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "delete from " + getFullTableName("KM_NFILE") + " where id=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}	
	}
	
	/**
	 * 移除收藏
	 * @param id 源文件id
	 * @throws Exception
	 */
	public void removeShare(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("KM_NFILE") + " WHERE SHAREID=?";
		
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
	
	/**
	 * 统计用户收藏文件次数
	 * @param fileId
	 * 		文件id
	 * @param user
	 * 		用户
	 * @throws Exception
	 */
	public long countFavorited(String fileId,NUser user) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM " + getFullTableName("KM_NFILE") + " WHERE SHAREID=? AND OWNERID=? AND ORIGIN=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, fileId);
			stmt.setString(2, user.getId());
			stmt.setInt(3, NFile.ORIGN_FAVORITE);
			rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			PersistenceUtils.closeStatement(stmt);
		}
		return 0;
	}

	public DataPackage<NFile> query() throws Exception {
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
	
	private NFile setPropety(ResultSet rs){
		NFile nfile = new NFile();
		try{
			nfile.setId(rs.getString("ID"));
			nfile.setName(rs.getString("NAME"));
			nfile.setUrl(rs.getString("URL"));
			nfile.setCreateDate(rs.getTimestamp("CREATEDATE"));
			nfile.setState(rs.getInt("STATE"));
			nfile.setOwnerId(rs.getString("OWNERID"));
			nfile.setVersion(rs.getInt("VERSION"));
			nfile.setType(rs.getString("TYPE"));
			nfile.setMemo(rs.getString("MEMO"));
			nfile.setSize(rs.getLong("FILESIZE"));
			nfile.setLastmodify(rs.getTimestamp("LASTMODIFY"));
			nfile.setCreatorId(rs.getString("CREATORID"));
			nfile.setShareId(rs.getString("SHAREID"));
			nfile.setNDirId(rs.getString("NDIRID"));
			nfile.setTitle(rs.getString("TITLE"));
			nfile.setOrigin(rs.getInt("ORIGIN"));
			nfile.setCreator(rs.getString("CREATOR"));
			nfile.setClassification(rs.getString("CLASSIFICATION"));
			nfile.setViews(rs.getInt("VIEWS"));
			nfile.setDownloads(rs.getInt("DOWNLOADS"));
			nfile.setFavorites(rs.getInt("FAVORITES"));
			nfile.setDomainId(rs.getString("DOMAIN_ID"));
			nfile.setGood(rs.getInt("GOOD"));
			nfile.setBad(rs.getInt("BAD"));
			nfile.setRootCategoryId(rs.getString("ROOT_CATEGORY_ID"));
			nfile.setSubCategoryId(rs.getString("SUB_CATEGORY_ID"));
			nfile.setDepartment(rs.getString("DEPARTMENT"));
			nfile.setDepartmentId(rs.getString("DEPARTMENT_ID"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return nfile;
	}

	/**
	 * 删除目录下的文件
	 * @param ndirid
	 * @throws Exception
	 */
	public void deleteFileByNDirid(String ndirid) throws Exception{
		PreparedStatement stmt = null;
		String sql = "DELETE FROM " + getFullTableName("KM_NFILE") + " WHERE NDIRID = ?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, ndirid);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 移动文件
	 * @param parentid
	 * @param fileids
	 * @throws Exception
	 */
	public void moveNFile(String parentid, String[] fileids) throws Exception{
		PreparedStatement stmt = null;
		
		StringBuffer ids = new StringBuffer();
		if(fileids.length > 0){
			for(int i=0; i<fileids.length; i++){
				ids.append("'").append(fileids[i]).append("',");
			}
		}
		
		if(ids.length() > 0)
			ids.setLength(ids.length()-1);
		
		String sql = "UPDATE " + getFullTableName("KM_NFILE") + " SET NDIRID=? WHERE ID IN (" + ids + ")";
		
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
	
	public long isSharedInThisNdir(String ndirid, String nfileid) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM " + getFullTableName("KM_NFILE") + " WHERE SHAREID=? AND NDIRID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, nfileid);
			stmt.setString(2, ndirid);
			rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			PersistenceUtils.closeStatement(stmt);
		}
		return 0;
	}
	
	public void doRemoveNFileByRemoveUser(String userid) throws Exception{
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("KM_NFILE") + " WHERE OWNERID=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, userid);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public long countBySql(String sql) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			PersistenceUtils.closeStatement(stmt);
		}
		return 0;
	}
	
	/**
	 * 
	 * 根据文件名查找文件
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public DataPackage<NFile> queryByName(int page, int lines, String name, String dirid) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<NFile> result = new DataPackage<NFile>();
		String sql = "SELECT * FROM "+getFullTableName("KM_NFILE") +" WHERE NAME LIKE '%"+name+"%' AND NDIRID='"+dirid+"'";
		
		//拼接MsSql的分页语句
		String querySql = buildLimitString(sql, page, lines, "createdate", "DESC");
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(querySql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.getDatas().add(setPropety(rs));
			}
			rs.close();
			
			Long rowCount = countBySql("SELECT COUNT(*)  FROM (" + sql + ") T");
			result.setRowCount(rowCount.intValue());
			
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	protected abstract String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException ;
}
