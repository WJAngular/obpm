package cn.myapps.mr.base.dao;

import java.sql.Connection;

import cn.myapps.mr.area.dao.MySqlAreaDAO;
import cn.myapps.mr.reservation.dao.MySqlReservationDAO;
import cn.myapps.mr.room.dao.MySqlRoomDAO;
import cn.myapps.mr.util.ConnectionManager;

public class DaoManager {
	
	public static BaseDAO getReservationDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MYSQL".equals(dbType)){
			return new MySqlReservationDAO(conn);
		}
		return null;
	}
	
	public static BaseDAO getRoomDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MYSQL".equals(dbType)){
			return new MySqlRoomDAO(conn);
		}
		return null;
	}
	
	public static BaseDAO getAreaDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MYSQL".equals(dbType)){
			return new MySqlAreaDAO(conn);
		}
		return null;
	}
}
