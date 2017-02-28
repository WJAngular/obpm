package cn.myapps.mr.room.ejb;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.base.dao.BaseDAO;
import cn.myapps.mr.base.dao.DaoManager;
import cn.myapps.mr.base.ejb.AbstractBaseProcessBean;
import cn.myapps.mr.reservation.ejb.Reservation;
import cn.myapps.mr.room.dao.RoomDAO;
import cn.myapps.pm.base.ejb.BaseProcess;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class RoomProcessBean extends AbstractBaseProcessBean<Room> implements RoomProcess{

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getRoomDAO(getConnection());
	}
	
	public Collection<Room> getAllRooms() throws Exception {
		Collection<Room> rooms;
		try {
			beginTransaction();
			
			rooms = ((RoomDAO)getDAO()).findAllRooms();
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return rooms;
	}
	
	public Collection<Room> getRoomByArea(String areaid) throws Exception {
		Collection<Room> rooms;
		try {
			beginTransaction();
			
			rooms = ((RoomDAO)getDAO()).findRoomByArea(areaid);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return rooms;
	}
	
	public Collection<Room> getRoomById(String id) throws Exception {
		Collection<Room> rooms;
		try {
			beginTransaction();
			
			rooms = ((RoomDAO)getDAO()).findById(id);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return rooms;
	}
	
	public void delete(String roomid) throws Exception {
		Collection<Room> rooms;
		try {
			beginTransaction();
			
			((RoomDAO)getDAO()).remove(roomid);
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	public void query(String roomid, WebUser user) {
		
	}

	public void queryByArea(String areaid) {
		
	}

	public boolean save(ValueObject room) throws Exception {
		boolean b;
		try {
			beginTransaction();
			if(StringUtil.isBlank(room.getId())){
				room.setId(Sequence.getSequence());
				b = ((RoomDAO)getDAO()).create(room);
			}else {
				b =  getDAO().update(room);
			}
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return b;
	}

	public void update(String roomid, WebUser user) {
		
	}

	public ValueObject doCreate(ValueObject vo, WebUser user) throws Exception {
		return null;
	}



}
