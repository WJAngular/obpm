package cn.myapps.mr.reservation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.area.ejb.Area;
import cn.myapps.mr.reservation.ejb.Reservation;
import cn.myapps.pm.util.ConnectionManager;


public class MySqlReservationDAO implements ReservationDAO{

	protected Connection connection;
	
	public MySqlReservationDAO(Connection conn){
		connection = conn;
	}
	
	public Boolean validate(ValueObject vo) throws Exception {
		PreparedStatement stmt = null;
		Reservation reservation = (Reservation)vo;
		
		String sql = "SELECT * FROM MR_RESERVATION"
				+ " WHERE area_id=? AND room_id=? AND start_time >= ? AND end_time <=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, reservation.getAreaId());
			stmt.setString(2, reservation.getRoomId());
			java.util.Date date = reservation.getStartTime();
			java.util.Date D = new java.util.Date(date.getYear(),date.getMonth(),date.getDate());
			Timestamp ts = new Timestamp(D.getTime());
			java.util.Date D2 = new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+1);
			Timestamp ts2 = new Timestamp(D2.getTime());
			stmt.setTimestamp(3, ts);
			stmt.setTimestamp(4, ts2);
			//stmt.setDate(2, new java.sql.Date(date.getTime()));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("ID").equals(reservation.getId()))
					continue;
				java.util.Date date1= (java.util.Date)rs.getTimestamp("START_TIME");
				java.util.Date date2= (java.util.Date)rs.getTimestamp("END_TIME");
				if(!(reservation.getStartTime().compareTo(date2)>=0 || reservation.getEndTime().compareTo(date1)<=0))
					return false;
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return true;
	}
	
	public boolean create(ValueObject vo) throws Exception {
		Reservation reservation = (Reservation)vo;
		PreparedStatement stmt = null;
		boolean b;
		String sql = "INSERT INTO MR_RESERVATION"
				+ " (ID,NAME,CONTENT,CREATOR,CREATOR_ID,CREATOR_TEL,AREA_ID,AREA,START_TIME,END_TIME,ROOM_ID,ROOM,DOMAIN_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if(reservation!=null&&reservation.getStartTime().compareTo(reservation.getEndTime())>0){
			java.util.Date date= reservation.getStartTime();
			reservation.setStartTime(reservation.getEndTime());
			reservation.setEndTime(date);
		}
		try {
			
			//先判断是否有时间冲突
			if(!validate(vo))
				throw new Exception("时间冲突!");
			
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, reservation.getId());
			stmt.setString(2, reservation.getName());
			stmt.setString(3, reservation.getContent());
			stmt.setString(4, reservation.getCreator());
			stmt.setString(5, reservation.getCreatorId());
			stmt.setString(6, reservation.getCreatorTel());
			stmt.setString(7, reservation.getAreaId());
			stmt.setString(8, reservation.getArea());
			Timestamp ts1 = new Timestamp(reservation.getStartTime().getTime());
			Timestamp ts2 = new Timestamp(reservation.getEndTime().getTime());
			stmt.setTimestamp(9, ts1);
			stmt.setTimestamp(10, ts2);
			stmt.setString(11, reservation.getRoomId());
			stmt.setString(12, reservation.getRoom());
			stmt.setString(13, reservation.getDomainid());
			b = stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return b;
	}

	public Collection<Reservation> queryByTime(java.util.Date date ,WebUser user) throws Exception {
		PreparedStatement stmt = null;

		Collection<Reservation> reservations = new ArrayList<Reservation>();
		String sql = "SELECT * FROM MR_RESERVATION"
				+ " WHERE creator_id=? AND start_time >= ? AND end_time <=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getId());
			java.util.Date D = new java.util.Date(date.getYear(),date.getMonth(),date.getDate());
			Timestamp ts = new Timestamp(D.getTime());
			java.util.Date D2 = new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+1);
			Timestamp ts2 = new Timestamp(D2.getTime());
			stmt.setTimestamp(2, ts);
			stmt.setTimestamp(3, ts2);
			//stmt.setDate(2, new java.sql.Date(date.getTime()));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Reservation reservation = new Reservation();
				setProperties(reservation, rs);
				reservations.add(reservation);
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return reservations;
	}
	
	public void setProperties(Reservation reservation,ResultSet rs) throws SQLException{
		reservation.setId(rs.getString("ID"));
		reservation.setName(rs.getString("NAME"));
		reservation.setAreaId(rs.getString("AREA_ID"));
		reservation.setArea(rs.getString("AREA"));
		reservation.setCreatorId(rs.getString("CREATOR_ID"));
		reservation.setCreator(rs.getString("CREATOR"));
		reservation.setCreatorTel(rs.getString("CREATOR_TEL"));
		reservation.setDomainid(rs.getString("DOMAIN_ID"));
		reservation.setStartTime(rs.getTimestamp("START_TIME"));
		reservation.setEndTime(rs.getTimestamp("END_TIME"));
		reservation.setContent(rs.getString("CONTENT"));
		reservation.setRoomId(rs.getString("ROOM_ID"));
		reservation.setRoom(rs.getString("ROOM"));
	}
	
	public ValueObject find(String id) throws Exception {
		PreparedStatement stmt = null;
		Reservation reservation = new Reservation();
		String sql = "SELECT * FROM MR_RESERVATION WHERE id=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				reservation = new Reservation();
				setProperties(reservation, rs);
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return reservation;
	}

	public Collection<?> simpleQuery(ParamsTable params, WebUser user)
			throws Exception {
		return null;
	}

	public boolean update(ValueObject vo) throws Exception {
		Reservation reservation = (Reservation)vo;
		PreparedStatement stmt = null;
		boolean b;
		String sql = "update MR_RESERVATION set NAME=?,CONTENT=?,CREATOR_TEL=?,AREA_ID=?,AREA=?,START_TIME=?,END_TIME=?,ROOM_ID=?,ROOM=? where id=?";

		if(reservation!=null&&reservation.getStartTime().compareTo(reservation.getEndTime())>0){
			java.util.Date date= reservation.getStartTime();
			reservation.setStartTime(reservation.getEndTime());
			reservation.setEndTime(date);
		}
		try {
			//先判断是否有时间冲突
			if(!validate(vo))
				throw new Exception("时间冲突");
			
			stmt = connection.prepareStatement(sql);
			Timestamp ts1 = new Timestamp(reservation.getStartTime().getTime());
			Timestamp ts2 = new Timestamp(reservation.getEndTime().getTime());
			stmt.setString(1, reservation.getName());
			stmt.setString(2, reservation.getContent());
			stmt.setString(3, reservation.getCreatorTel());
			stmt.setString(4, reservation.getAreaId());
			stmt.setString(5, reservation.getArea());
			stmt.setTimestamp(6, ts1);
			stmt.setTimestamp(7, ts2);
			stmt.setString(8, reservation.getRoomId());
			stmt.setString(9, reservation.getRoom());
			stmt.setString(10, reservation.getId());
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

	public Collection<Reservation> findMyReservation(WebUser user, String area,
			String room, java.util.Date date1, java.util.Date date2, int pages, int lines)
			throws Exception {
		int index = (pages-1) * lines; //pages页数 lines显示行数 index开始数
		PreparedStatement stmt = null;
		Timestamp ts=null,ts2=null;
		Collection<Reservation> reservations = new ArrayList<Reservation>();
		String sql = "SELECT * FROM MR_RESERVATION"
				+ " WHERE creator_id =?";
		int i=1;
		if(date1!=null){
			sql = sql + " AND start_time >=?";
		}
		if(date2!=null){
			sql = sql + " AND end_time <=?";
		}
		if(area!=null){
			sql = sql + " AND area_id=?";
		}
		if(room!=null){
			sql = sql + " AND room_id=?";
		}
		sql = sql +" Order By start_time Desc limit "+index+","+lines ;
		try {
			stmt = connection.prepareStatement(sql);
			if(date1!=null){
				java.util.Date D = new java.util.Date(date1.getYear(),date1.getMonth(),date1.getDate());
				ts = new Timestamp(D.getTime());
			}
			if(date2!=null){
				java.util.Date D2 = new java.util.Date(date2.getYear(),date2.getMonth(),date2.getDate()+1);
				ts2 = new Timestamp(D2.getTime());
			}
			
			stmt.setString(1, user.getId());
			if(date1!=null){
				i++;
				stmt.setTimestamp(i, ts);
			}
			if(date2!=null){
				i++;
				stmt.setTimestamp(i, ts2);
			}
			if(area!=null){
				i++;
				stmt.setString(i, area);
			}
			if(room!=null){
				i++;
				stmt.setString(i, room);
			}
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Reservation reservation = new Reservation();
				setProperties(reservation, rs);
				reservations.add(reservation);
			}
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return reservations;
	}

	public int countMyReservation(WebUser user, String area,
			String room, java.util.Date date1, java.util.Date date2) throws Exception{
		PreparedStatement stmt = null;
		Timestamp ts=null,ts2=null;
		int length;
		Collection<Reservation> reservations = new ArrayList<Reservation>();
		String sql = "SELECT count(*) FROM MR_RESERVATION"
				+ " WHERE creator_id =?";
		int i=1;
		if(date1!=null){
			sql = sql + " AND start_time >=?";
		}
		if(date2!=null){
			sql = sql + " AND end_time <=?";
		}
		if(area!=null){
			sql = sql + " AND area_id=?";
		}
		if(room!=null){
			sql = sql + " AND room_id=?";
		}
		try {
			stmt = connection.prepareStatement(sql);
			if(date1!=null){
				java.util.Date D = new java.util.Date(date1.getYear(),date1.getMonth(),date1.getDate());
				ts = new Timestamp(D.getTime());
			}
			if(date2!=null){
				java.util.Date D2 = new java.util.Date(date2.getYear(),date2.getMonth(),date2.getDate()+1);
				ts2 = new Timestamp(D2.getTime());
			}
			stmt.setString(1, user.getId());
			if(date1!=null){
				i++;
				stmt.setTimestamp(i, ts);
			}
			if(date2!=null){
				i++;
				stmt.setTimestamp(i, ts2);
			}
			if(area!=null){
				i++;
				stmt.setString(i, area);
			}
			if(room!=null){
				i++;
				stmt.setString(i, room);
			}
			ResultSet rs = stmt.executeQuery();
			rs.next();
			length = rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return length;
	}
	
	public void remove(String pk) throws Exception{
		PreparedStatement stmt = null;
		String sql = "DELETE FROM MR_RESERVATION WHERE id=?";
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
	
	public boolean updateAreaName(ValueObject vo) throws Exception {
		PreparedStatement stmt = null;
		boolean b;
		String sql = "UPDATE MR_RESERVATION set AREA=? where AREA_ID=?";
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
}
