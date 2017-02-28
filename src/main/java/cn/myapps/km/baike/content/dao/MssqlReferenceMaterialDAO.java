package cn.myapps.km.baike.content.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.km.baike.content.ejb.ReferenceMaterial;
import cn.myapps.km.util.PersistenceUtils;


/**
 * 
 * @author abel
 */

public class MssqlReferenceMaterialDAO extends AbstractReferenceMaterialDAO implements ReferenceMaterialDAO{
	
	
	public MssqlReferenceMaterialDAO(Connection conn) throws Exception {
		super(conn);
		
		dbTag = "MS SQL SERVER: ";
		try {
			ResultSet rs = conn.getMetaData().getSchemas();
			if (rs != null) {
				if (rs.next())
					this.schema = rs.getString(1).trim().toUpperCase();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * 获取某版本内容所引用的参考资料
	 * @param contentId
	 * @return
	 * @throws Exception
	 */
	
	public Collection<ReferenceMaterial> getReferenceMaterials(String contentId) throws Exception {
		PreparedStatement stmt = null;
		Collection<ReferenceMaterial> rc = new ArrayList<ReferenceMaterial>();
		String sql="SELECT  * FROM "+getFullTableName("BAIKE_ENTRY_REFERENCEMATERIAL")+" WHERE ENTRYCONTENTID = ? ";
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, contentId);
			//stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				rc.add(setProperty(rs));
			}
			rs.close();
				return  rc;
		} catch (Exception e) {
			throw e;
			
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	
	}


	
}


