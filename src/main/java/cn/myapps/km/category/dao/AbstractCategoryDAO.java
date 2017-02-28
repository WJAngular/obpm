package cn.myapps.km.category.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.category.ejb.Category;
import cn.myapps.km.util.PersistenceUtils;

/**
 * @author xiuwei
 *
 */
public abstract class AbstractCategoryDAO {

	Logger log = Logger.getLogger(getClass());

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractCategoryDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		Category o = (Category) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("KM_CATEGORY")
				+ " (ID,NAME,DESCRIPTION,PARENT_ID,SORT,DOMAIN_ID) values (?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, o.getId());
			stmt.setString(2, o.getName());
			stmt.setString(3, o.getDescription());
			stmt.setString(4, o.getParentId());
			stmt.setInt(5, o.getSort());
			stmt.setString(6, o.getDomainId());

			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		Category o = (Category) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("KM_CATEGORY")
				+ " SET ID=?,NAME=?,DESCRIPTION=?,PARENT_ID=?,SORT=?,DOMAIN_ID=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, o.getId());
			stmt.setString(2, o.getName());
			stmt.setString(3, o.getDescription());
			stmt.setString(4, o.getParentId());
			stmt.setInt(5, o.getSort());
			stmt.setString(6, o.getDomainId());
			stmt.setString(7, o.getId());

			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public Category find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName("KM_CATEGORY")
				+ " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Category category = setPropety(rs);
				;
				rs.close();
				return category;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("KM_CATEGORY")
				+ " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public Collection<Category> querySubCategory(String parentId,String domainId) throws Exception {
		Collection<Category> list = new ArrayList<Category>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + getFullTableName("KM_CATEGORY") + " d WHERE DOMAIN_ID=? AND PARENT_ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, domainId);
			stmt.setString(2, parentId);
			rs = stmt.executeQuery();
			
			while(rs != null && rs.next()){
				Category category = setPropety(rs);
				list.add(category);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
		
		return list;
		
	}

	public Category setPropety(ResultSet rs) throws SQLException {
		Category o = new Category();
		o.setId(rs.getString("ID"));
		o.setName(rs.getString("NAME"));
		o.setDescription(rs.getString("DESCRIPTION"));
		o.setParentId(rs.getString("PARENT_ID"));
		o.setSort(rs.getInt("SORT"));
		o.setDomainId(rs.getString("DOMAIN_ID"));
		return o;
	}

	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
}
