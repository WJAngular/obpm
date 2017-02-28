package cn.myapps.km.baike.content.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import cn.myapps.km.baike.content.ejb.ReferenceMaterial;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.PersistenceUtils;

/**
 * 
 * @author Abel
 *参考资料DAO层的抽象类
 */
public class AbstractReferenceMaterialDAO extends AbstractBaseDAO{


	Logger log = Logger.getLogger(getClass());
	
	public AbstractReferenceMaterialDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	public int setParameters(PreparedStatement statement, ReferenceMaterial rm)
			throws SQLException {
		int index = 1;
		statement.setString(index++, rm.getId());
		statement.setString(index++, rm.getEntryContentId());
		statement.setString(index++, rm.getArticleName());	
		statement.setString(index++, rm.getUrl());
		statement.setString(index++, rm.getWebName());
		if (rm.getPublishDate() == null) {
			statement.setNull(index++, java.sql.Types.TIMESTAMP);
		} else {
			Timestamp ts = new Timestamp(rm.getPublishDate().getTime());
			statement.setTimestamp(index++, ts);
		}
		if (rm.getReferenceDate() == null) {
			statement.setNull(index++, java.sql.Types.TIMESTAMP);
		} else {
			Timestamp ts = new Timestamp(rm.getReferenceDate().getTime());
			statement.setTimestamp(index++, ts);
		}
		
		return index;
	}
	
	/**
	 * 创建内容
	 */
	public void create(NObject vo) throws Exception {
		ReferenceMaterial rm = (ReferenceMaterial) vo;
		PreparedStatement statement = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_ENTRY_REFERENCEMATERIAL") + " (ID,ENTRYCONTENTID,ARTICLENAME,URL,WEBNAME,PUBLISHDATE,REFERENCEDATE) values (?,?,?,?,?,?,?)";
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, rm);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	/**
	 * 修改内容
	 */
	public void update(NObject vo) throws Exception {
		ReferenceMaterial rm = (ReferenceMaterial) vo;
		PreparedStatement statement = null;		
		String sql = "UPDATE " + getFullTableName("BAIKE_ENTRY_REFERENCEMATERIAL") + " SET ID=?,ENTRYCONTENTID=?,ARTICLENAME=?,URL=?,WEBNAME=?,PUBLISHDATE=?,REFERENCEDATE=? WHERE ID=?";	
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			int index = setParameters(statement, rm);
			statement.setString(index, rm.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	/**
	 * 通过Id查找内容
	 */
	public ReferenceMaterial find(String id) throws Exception {
		PreparedStatement stmt = null;
		ReferenceMaterial ec = new ReferenceMaterial();
		ResultSet rs = null;		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_ENTRY_REFERENCEMATERIAL") + " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);		
			rs = stmt.executeQuery();
			if(rs.next()){
				this.setProperty(ec, rs);
			} else {
				return null;
			}
			return ec;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	
	private void setProperty(ReferenceMaterial rm, ResultSet rs) throws Exception{
		rm.setId(rs.getString("ID"));
		rm.setEntryContentId(rs.getString("ENTRYCONTENTID"));
		rm.setArticleName(rs.getString("ARTICLENAME"));		
		rm.setUrl(rs.getString("URL"));
		rm.setWebName(rs.getString("WEBNAME"));
		rm.setPublishDate(new Date(rs.getTimestamp("PUBLISHDATE").getTime()));
		rm.setReferenceDate(new Date(rs.getTimestamp("REFERENCEDATE").getTime()));
	}
	

	public ReferenceMaterial setProperty(ResultSet rs) throws Exception{
		ReferenceMaterial rm = new ReferenceMaterial();
		rm.setId(rs.getString("ID"));
		rm.setEntryContentId(rs.getString("ENTRYCONTENTID"));
		rm.setArticleName(rs.getString("ARTICLENAME"));		
		rm.setUrl(rs.getString("URL"));
		rm.setWebName(rs.getString("WEBNAME"));
		rm.setPublishDate(new Date(rs.getTimestamp("PUBLISHDATE").getTime()));
		rm.setReferenceDate(new Date(rs.getTimestamp("REFERENCEDATE").getTime()));
		return rm;
	}
	
	/**
	 * 通过Id进行删除
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_REFERENCEMATERIAL") + " WHERE ID=?";

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

	public DataPackage<ReferenceMaterial> query() throws Exception {
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
	 * 移除内容
	 * @param id
	 * @throws Exception
	 */
	public void remove(Collection<ReferenceMaterial> collections) throws Exception {
		PreparedStatement stmt = null;
		StringBuffer delSql = new StringBuffer("");
		for (Iterator<ReferenceMaterial> iter=collections.iterator(); iter.hasNext();) {
			NObject object = iter.next();
			delSql.append("'").append(object.getId()).append("',");
		}
		delSql.deleteCharAt(delSql.lastIndexOf(","));
		String sql = "DELETE FROM " + getFullTableName("BAIKE_ENTRY_REFERENCEMATERIAL") + " WHERE ID IN (" + delSql.toString() + ")";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			PersistenceUtils.closeStatement(stmt);
		}
	}
}
