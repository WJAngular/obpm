package cn.myapps.mr.area.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.area.ejb.Area;
import cn.myapps.mr.room.ejb.Room;
import cn.myapps.pm.util.ConnectionManager;

public class MySqlAreaDAO implements AreaDAO{

	protected Connection connection;
	
	public MySqlAreaDAO(Connection conn){
		connection = conn;
	}

	public boolean create(ValueObject vo) throws Exception {
		PreparedStatement stmt = null;
		boolean b;
		
		String sql = "INSERT INTO MR_AREA (ID,NAME,DOMAIN_ID,CREATOR,CREATOR_ID) VALUES (?,?,?,?,?)";

		Area area = (Area)vo;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, area.getId());
			stmt.setString(2, area.getName());
			stmt.setString(3, area.getDomainid());
			stmt.setString(4, area.getCreator());
			stmt.setString(5, area.getCreatorId());

			b = stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return b;
	}

	public ValueObject find(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM MR_AREA WHERE id=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			stmt.execute();
			
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public Collection<?> simpleQuery(ParamsTable params, WebUser user)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean update(ValueObject vo) throws Exception {
		PreparedStatement stmt = null;
		boolean b;
		
		String sql = "UPDATE MR_AREA set NAME=? where id=?";

		Area area = (Area)vo;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, area.getName());
			stmt.setString(2, area.getId());
			b = stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return b;
	}

	public void update(Map<String, Object> items, String pk) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public Collection<Area> findAllAreas() throws Exception {
		PreparedStatement stmt = null;

		Collection<Area> areas = new ArrayList<Area>();
		String sql = "SELECT * FROM MR_AREA";

		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Area area = new Area();
				area.setId(rs.getString("ID"));
				area.setCreator(rs.getString("CREATOR"));
				area.setCreatorId(rs.getString("CREATOR_ID"));
				area.setDomainid(rs.getString("DOMAIN_ID"));
				area.setName(rs.getString("NAME"));
				areas.add(area);
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return areas;
	}
	
}
