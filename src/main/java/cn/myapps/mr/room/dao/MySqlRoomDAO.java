package cn.myapps.mr.room.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.area.ejb.Area;
import cn.myapps.mr.reservation.ejb.Reservation;
import cn.myapps.mr.room.ejb.Room;
import cn.myapps.pm.util.ConnectionManager;

public class MySqlRoomDAO implements RoomDAO{

	protected Connection connection;

	
	public MySqlRoomDAO(Connection conn){
		connection = conn;
	}
	
	public Collection<Room> findAllRooms() throws Exception {
		PreparedStatement stmt = null;

		Collection<Room> rooms = new ArrayList<Room>();
		String sql = "SELECT * FROM MR_ROOM";

		try {
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Room room = new Room();
				room.setId(rs.getString("ID"));
				room.setArea(rs.getString("AREA"));
				room.setAreaId(rs.getString("AREA_ID"));
				room.setCreator(rs.getString("CREATOR"));
				room.setCreatorId(rs.getString("CREATOR_ID"));
				room.setDomainid(rs.getString("DOMAIN_ID"));
				room.setName(rs.getString("NAME"));
				room.setNumber(rs.getString("NUMBER"));
				room.setNote(rs.getString("NOTE"));
				room.setEquipment(rs.getString("EQUIPMENT"));
				rooms.add(room);
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rooms;
	}
	
	public Collection<Room> findRoomByArea(String areaid) throws Exception {
		PreparedStatement stmt = null;

		Collection<Room> rooms = new ArrayList<Room>();
		String sql = "SELECT * FROM MR_ROOM WHERE area_id=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, areaid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Room room = new Room();
				room.setId(rs.getString("ID"));
				room.setArea(rs.getString("AREA"));
				room.setAreaId(rs.getString("AREA_ID"));
				room.setCreator(rs.getString("CREATOR"));
				room.setCreatorId(rs.getString("CREATOR_ID"));
				room.setDomainid(rs.getString("DOMAIN_ID"));
				room.setName(rs.getString("NAME"));
				room.setNumber(rs.getString("NUMBER"));
				room.setNote(rs.getString("NOTE"));
				room.setEquipment(rs.getString("EQUIPMENT"));
				rooms.add(room);
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rooms;
	}
	
	public boolean create(ValueObject vo) throws Exception {
		PreparedStatement stmt = null;
		boolean b;
		
		String sql = "INSERT INTO MR_ROOM (ID,NAME,AREA,AREA_ID,CREATOR,CREATOR_ID,DOMAIN_ID,NUMBER,NOTE,EQUIPMENT) VALUES (?,?,?,?,?,?,?,?,?,?)";

		Room room = (Room)vo;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, room.getId());
			stmt.setString(2, room.getName());
			stmt.setString(3, room.getArea());
			stmt.setString(4, room.getAreaId());
			stmt.setString(5, room.getCreator());
			stmt.setString(6, room.getCreatorId());
			stmt.setString(7, room.getDomainid());
			stmt.setString(8, room.getNumber());
			stmt.setString(9, room.getNote());
			stmt.setString(10, room.getEquipment());

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

	public Collection<Room> findById(String id) throws Exception {
		PreparedStatement stmt = null;

		Collection<Room> rooms = new ArrayList<Room>();
		String sql = "SELECT * FROM MR_ROOM WHERE id=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Room room = new Room();
				room.setId(rs.getString("ID"));
				room.setArea(rs.getString("AREA"));
				room.setAreaId(rs.getString("AREA_ID"));
				room.setCreator(rs.getString("CREATOR"));
				room.setCreatorId(rs.getString("CREATOR_ID"));
				room.setDomainid(rs.getString("DOMAIN_ID"));
				room.setName(rs.getString("NAME"));
				room.setNumber(rs.getString("NUMBER"));
				room.setNote(rs.getString("NOTE"));
				room.setEquipment(rs.getString("EQUIPMENT"));
				rooms.add(room);
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rooms;
	}
	
	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM MR_ROOM WHERE id=?";

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
		
		String sql = "UPDATE MR_ROOM set CREATOR=?,NUMBER=?,NOTE=?,EQUIPMENT=? where id=?";

		Room room = (Room)vo;
		try {
			stmt = connection.prepareStatement(sql);
			//stmt.setString(1, room.getName());
			//stmt.setString(2, room.getArea());
			stmt.setString(1, room.getCreator());
			stmt.setString(2, room.getNumber());
			stmt.setString(3, room.getNote());
			stmt.setString(4, room.getEquipment());
			stmt.setString(5, room.getId());

			b = stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return b;
	}

	public boolean updateAreaName(ValueObject vo) throws Exception {
		PreparedStatement stmt = null;
		boolean b;
		
		String sql = "UPDATE MR_ROOM set AREA=? where AREA_ID=?";

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
		
	}

}
