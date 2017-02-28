package cn.myapps.mr.room.dao;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.mr.base.dao.BaseDAO;
import cn.myapps.mr.room.ejb.Room;

public interface RoomDAO extends BaseDAO{
	public Collection<Room> findAllRooms() throws Exception;
	public Collection<Room> findById(String id) throws Exception;
	public Collection<Room> findRoomByArea(String areaid) throws Exception;
	public boolean updateAreaName(ValueObject vo) throws Exception;
}
