package cn.myapps.mr.area.ejb;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.area.dao.AreaDAO;
import cn.myapps.mr.base.dao.BaseDAO;
import cn.myapps.mr.base.dao.DaoManager;
import cn.myapps.mr.base.ejb.AbstractBaseProcessBean;
import cn.myapps.mr.reservation.dao.ReservationDAO;
import cn.myapps.mr.room.dao.RoomDAO;
import cn.myapps.mr.room.ejb.Room;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class AreaProcessBean extends AbstractBaseProcessBean<Area> implements AreaProcess{

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getAreaDAO(getConnection());
	}

	public Collection<Area> getAllAreas() throws Exception {
		Collection<Area> areas;
		try {
			beginTransaction();
			
			areas = ((AreaDAO)getDAO()).findAllAreas();
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return areas;
	}
	
	public void create(Area area) throws Exception {
		try {
			beginTransaction();
			if(StringUtil.isBlank(area.getId())){
				area.setId(Sequence.getSequence());
				((AreaDAO)getDAO()).create(area);
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	public void delete(String id) throws Exception{
		try {
			beginTransaction();
			((AreaDAO)getDAO()).remove(id);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	public void update(Area area) throws Exception{
		try {
			beginTransaction();
			((AreaDAO)getDAO()).update(area);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		//修改会议室中区域名称
		try {
			beginTransaction();
			((RoomDAO)DaoManager.getRoomDAO(getConnection())).updateAreaName(area);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		//修改预订中区域名称
		try {
			beginTransaction();
			((ReservationDAO)DaoManager.getReservationDAO(getConnection())).updateAreaName(area);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
}
