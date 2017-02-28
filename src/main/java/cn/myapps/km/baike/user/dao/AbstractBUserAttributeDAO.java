package cn.myapps.km.baike.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.base.dao.AbstractBaseDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.util.BaikeUtils;

/**
 * @author dragon
 *
 */
public abstract class AbstractBUserAttributeDAO extends AbstractBaseDAO{
	
	Logger log = Logger.getLogger(getClass());
	
	public AbstractBUserAttributeDAO(Connection conn) throws Exception {
		super(conn);
	}
	
	
	public void update(NObject bObject) throws Exception {
		BUserAttribute bUserAttribute = (BUserAttribute)bObject;
		PreparedStatement stmt = null;
		String sql = "update "+getFullTableName("BAIKE_USER_ATTRIBUTE")+" set INTEGRAL=?,THROUGHPUTRATE=? where ID=?";
		try {
			stmt = connection.prepareStatement(sql);
			//积分
			stmt.setInt(1, bUserAttribute.getIntegral());
			//通过率
			stmt.setInt(2, bUserAttribute.getThroughputRate());
			stmt.setString(3, bUserAttribute.getId());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 根据编号查询
	 */
	public BUserAttribute find(String id) throws Exception {
		PreparedStatement stmt = null;
		String sql = "SELECT * FROM "+getFullTableName("BAIKE_USER_ATTRIBUTE") +" WHERE ID=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				BUserAttribute bUserAttribute = setPropety(rs);
				rs.close();
				return bUserAttribute;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 增加
	 * @param id
	 * @throws Exception
	 */
	public void create(NObject bObject) throws Exception {
		BUserAttribute bUserAttribute = (BUserAttribute)bObject;
		PreparedStatement stmt = null;
		String sql = "INSERT INTO " + getFullTableName("BAIKE_USER_ATTRIBUTE") + "(ID,USERID,INTEGRAL,THROUGHPUTRATE,FIELD1,FIELD2,FIELD3,FIELD4,FIELD5) values(?,?,?,?,?,?,?,?,?)";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, bUserAttribute.getId());
			stmt.setString(2, bUserAttribute.getUserId());
			stmt.setInt(3, bUserAttribute.getIntegral());
			stmt.setInt(4, bUserAttribute.getThroughputRate());
			
			stmt.setString(5, bUserAttribute.getField1());
			stmt.setString(6, bUserAttribute.getField2());
			stmt.setString(7, bUserAttribute.getField3());
			stmt.setString(8, bUserAttribute.getField4());
			stmt.setString(9, bUserAttribute.getField5());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @throws Exception
	 */
	public void remove(String id) throws Exception {
		PreparedStatement stmt = null;
		String sql = "delete from " + getFullTableName("BAIKE_USER_ATTRIBUTE") + " where ID=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info(sql);
			BaikeUtils.closeStatement(stmt);
		}
	}
	
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	
	public static BUserAttribute setPropety(ResultSet rs) throws Exception{
		BUserAttribute bUserAttribute = new BUserAttribute();
		try{
			bUserAttribute.setId(rs.getString("ID"));
			bUserAttribute.setUserId(rs.getString("USERID"));
			bUserAttribute.setIntegral(rs.getInt("INTEGRAL"));
			bUserAttribute.setThroughputRate(rs.getInt("THROUGHPUTRATE"));
			
			bUserAttribute.setField1(rs.getString("FIELD1"));
			bUserAttribute.setField2(rs.getString("FIELD2"));
			bUserAttribute.setField3(rs.getString("FIELD3"));
			bUserAttribute.setField4(rs.getString("FIELD4"));
			bUserAttribute.setField5(rs.getString("FIELD5"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return bUserAttribute;
	}
	

	public int getTotalLines(String sql) throws Exception {
		PreparedStatement stmt = null;
		Long amount = Long.valueOf(0);
		int from = sql.toUpperCase().indexOf("FROM");
		int order = sql.toUpperCase().indexOf("ORDER BY");
		
		String newsql = (order > 0) ? "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from, order)
				: "SELECT COUNT(*) AS ROW_COUNT " + sql.substring(from);
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
	
}
