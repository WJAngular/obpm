package cn.myapps.attendance.location.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.attendance.base.dao.AbstractBaseDAO;
import cn.myapps.attendance.location.ejb.Location;
import cn.myapps.attendance.util.ConnectionManager;

public abstract class AbstractLocationDAO extends AbstractBaseDAO {

	private static final Logger log = Logger.getLogger(AbstractLocationDAO.class);

	public AbstractLocationDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "AM_LOCATION";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		Location location = (Location) vo;

		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName(tableName)
				+ " (ID,NAME,LONGITUDE,LATITUDE,DOMAIN_ID) values (?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, location.getId());
			stmt.setString(2, location.getName());
			stmt.setDouble(3, location.getLongitude());
			stmt.setDouble(4, location.getLatitude());
			stmt.setString(5, location.getDomainid());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}

		return vo;

	}

	public ValueObject find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			Location location = null;
			if (rs.next()) {
				location = new Location();
				setProperties(location, rs);
			}
			rs.close();
			return location;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			log.debug(sql);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}

	}

	public ValueObject update(ValueObject vo) throws Exception {
		Location location = (Location) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE " + getFullTableName(tableName)
				+ " SET NAME=?,LONGITUDE=?,LATITUDE=?,DOMAIN_ID=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, location.getName());
			stmt.setDouble(2, location.getLongitude());
			stmt.setDouble(3, location.getLatitude());
			stmt.setString(4, location.getDomainid());
			stmt.setString(5, location.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}

	public Collection<?> simpleQuery(ParamsTable params, WebUser user)
			throws Exception {
		Collection<Location> rtn = new ArrayList<Location>();

		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ORDER BY ID DESC";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			ResultSet rs = stmt.executeQuery();
			Location location = null;
			while (rs.next()) {
				location = new Location();
				setProperties(location, rs);
				rtn.add(location);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;

	}
	
	public DataPackage<?> query(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<Location> datas = new DataPackage<Location>();
		Collection<Location> list = new ArrayList<Location>();
		
		int page = params.getParameterAsInteger("page");
		int lines = params.getParameterAsInteger("rows");

		PreparedStatement stmt = null;
		PreparedStatement countStmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ";
		
		String countSQL =  "SELECT count(*) FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=?";

		sql = buildLimitString(sql, page, lines, "ID", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			ResultSet rs = stmt.executeQuery();
			Location location = null;
			while (rs.next()) {
				location = new Location();
				setProperties(location, rs);
				list.add(location);
			}
			datas.setDatas(list);
			rs.close();
			
			countStmt = connection.prepareStatement(countSQL);
			countStmt.setString(1, user.getDomainid());
			ResultSet cr = countStmt.executeQuery();
			if(cr.next()){
				datas.setRowCount(cr.getInt(1));
			}
			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(countStmt);
		}
		return datas;
	} 

	void setProperties(Location location, ResultSet rs) throws Exception {
		try {
			location.setId(rs.getString("ID"));
			location.setName(rs.getString("NAME"));
			location.setLongitude(rs.getDouble("LONGITUDE"));
			location.setLatitude(rs.getDouble("LATITUDE"));
			location.setDomainid(rs.getString("DOMAIN_ID"));
		} catch (SQLException e) {
			throw e;
		}

	}
	
	public List<Location> getLocationsByRule(String ruleId) throws Exception {
		List<Location> rtn = new ArrayList<Location>();

		PreparedStatement stmt = null;

		String sql = "SELECT location.* FROM " + getFullTableName(tableName)
				+ " location, "+getFullTableName("AM_RULE_LOCATION_SET")+" rlset WHERE location.ID=rlset.LOCATION_ID and rlset.RULE_ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, ruleId);
			ResultSet rs = stmt.executeQuery();
			Location location = null;
			while (rs.next()) {
				location = new Location();
				setProperties(location, rs);
				rtn.add(location);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;

	}

}
