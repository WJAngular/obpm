package cn.myapps.km.kmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.kmap.ejb.KMap;
import cn.myapps.km.util.PersistenceUtils;

/**
 * @author Happy
 *
 */
public abstract class AbstractKMapDAO {

	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;

	public AbstractKMapDAO(Connection conn) throws Exception {
		this.connection = conn;
	}

	public void create(NObject vo) throws Exception {
		KMap kMap = (KMap) vo;
		PreparedStatement stmt = null;

		String sql = "INSERT INTO "+ getFullTableName("KM_KMAP")+" (ID,FILEID) values (?,?)";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, kMap.getId());
			stmt.setString(2, kMap.getFileid());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}

	public void update(NObject vo) throws Exception {
		KMap kmap = (KMap)vo;
		PreparedStatement stmt = null;
		
		String sql = "UPDATE " + getFullTableName("KM_KMAP")+" SET ID=?,FILEID=? WHERE ID=?";
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, kmap.getId());
			stmt.setString(2, kmap.getFileid());
			stmt.setString(3, kmap.getId());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}

	}

	public KMap find(String id) throws Exception {
		PreparedStatement stmt = null;
		
		String sql = "SELECT * FROM "+getFullTableName("KM_KMAP") + " WHERE ID=?";
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				KMap kmap = new KMap();
				kmap.setId(rs.getString(1));
				kmap.setFileid(rs.getString(2));
				rs.close();

				return kmap;
			}

			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public void remove(String id) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		String sql = "DELETE " + getFullTableName("KM_KMAP") + " WHERE ID = ?";
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

	public DataPackage<KMap> query() throws Exception {
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

}
