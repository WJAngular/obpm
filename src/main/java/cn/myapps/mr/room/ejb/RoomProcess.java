package cn.myapps.mr.room.ejb;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.base.ejb.BaseProcess;

public interface RoomProcess extends BaseProcess<Room>{
	public void delete(String roomid) throws Exception;
	public void update(String roomid,WebUser user);
	public void query(String roomid,WebUser user);
	public void queryByArea(String areaid);
	public Collection<Room> getAllRooms() throws Exception;
	public Collection<Room> getRoomById(String id) throws Exception;
	public boolean save(ValueObject room) throws Exception;
	public Collection<Room> getRoomByArea(String areaid) throws Exception;
}
