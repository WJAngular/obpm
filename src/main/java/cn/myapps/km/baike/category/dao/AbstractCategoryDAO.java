package cn.myapps.km.baike.category.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.km.baike.category.ejb.Category;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * 百科分类对应抽象DAO
 * @author jodg
 *
 */
public class AbstractCategoryDAO extends AbstractBaseDAO {
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractCategoryDAO(Connection conn) throws Exception {
		super(conn);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 创建词条分类
	 * @param vo
	 * @throws Exception
	 */
	public void create(NObject vo) throws Exception {
		Category category = (Category) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "
				+ getFullTableName("BAIKE_CATEGORY")
				+ " (ID,NAME,DESCRIPTION,PARENTID,DOMAINID,ORDERBY) values (?,?,?,?,?,?)";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, category.getId());
			stmt.setString(2, category.getName());
			stmt.setString(3, category.getDescription());
			stmt.setString(4, category.getParentId());
			stmt.setString(5, category.getDomainId());
			stmt.setInt(6, category.getOrderby());
			
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 修改词条分类
	 * @param vo
	 * @throws Exception
	 */
	public void update(NObject vo) throws Exception {
		Category category = (Category) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE "
				+ getFullTableName("BAIKE_CATEGORY")
				+ " SET ID=?,NAME=?,DESCRIPTION=?,PARENTID=?,DOMAINID=?,ORDERBY=? WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, category.getId());
			stmt.setString(2, category.getName());
			stmt.setString(3, category.getDescription());
			stmt.setString(4, category.getParentId());
			stmt.setString(5, category.getDomainId());
			stmt.setInt(6, category.getOrderby());
			stmt.setString(7, category.getId());

			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 根据Id查找分类
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Category find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName("BAIKE_CATEGORY")
				+ " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Category category = setPropety(rs);
				rs.close();
				return category;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 根据ID删除分类
	 * @param pk
	 * @throws Exception
	 */
	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName("BAIKE_CATEGORY")
				+ " WHERE ID=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 根据父类ID查找子类
	 * @param parentId
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public Collection<Category> querySubCategory(String parentId,String domainId) throws Exception {
		Collection<Category> list = new ArrayList<Category>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + getFullTableName("BAIKE_CATEGORY") + " d WHERE DOMAINID=? AND PARENTID=?";
		
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
			BaikeUtils.closeStatement(stmt);
		}
		
		return list;
		
	}

	public Category setPropety(ResultSet rs) throws SQLException {
		Category category = new Category();
		category.setId(rs.getString("ID"));
		category.setName(rs.getString("NAME"));
		category.setDescription(rs.getString("DESCRIPTION"));
		category.setParentId(rs.getString("PARENTID"));
		category.setDomainId(rs.getString("DOMAINID"));
		category.setOrderby(rs.getInt("ORDERBY"));
		return category;
	}
	
	
	/**
	 * 获取行数
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int getTotalLines(String sql) throws Exception {
		PreparedStatement stmt = null;
		Long amount = Long.valueOf(0);
		int from = sql.toUpperCase().indexOf("FROM");
		int order = sql.toUpperCase().indexOf("ORDER BY");

		String newsql = (order > 0) ? " SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from, order)
				: " SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from);
		try {
			stmt = connection.prepareStatement(newsql);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				amount = rs.getLong("ROW_COUNT");
				rs.close();
				return amount.intValue();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
		return 0;
	}
	
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	

}
