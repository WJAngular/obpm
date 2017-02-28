package cn.myapps.mr.reservation.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.base.dao.BaseDAO;
import cn.myapps.mr.base.dao.DaoManager;
import cn.myapps.mr.base.ejb.AbstractBaseProcessBean;
import cn.myapps.mr.reservation.dao.ReservationDAO;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class ReservationProcessBean extends AbstractBaseProcessBean<Reservation> implements ReservationProcess{

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getReservationDAO(getConnection());
	}
	
	public boolean doCreate(ValueObject vo,WebUser user) throws Exception {
		Reservation reservation = (Reservation)vo;
		boolean b;
		try {
			beginTransaction();
			if(StringUtil.isBlank(reservation.getId())){
				reservation.setId(Sequence.getSequence());
				b =  getDAO().create(reservation);
			}else {
				b =  getDAO().update(reservation);
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return b;
	}

	public Collection<Reservation> queryByTime(Date date, WebUser user)
			throws Exception {
		Collection<Reservation> collections;
		try {
			beginTransaction();
			collections = ((ReservationDAO)getDAO()).queryByTime(date,user);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return collections;
	}
	
	public ValueObject queryById(String id)throws Exception{
		return ((ReservationDAO)getDAO()).find(id);
	}

	/**
	 * pages页数 lines显示行数
	 */
	public Collection<Reservation> queryMyReservation(WebUser user, String area,
			String room, Date date1, Date date2,int pages,int lines) throws Exception {
		Collection<Reservation> collections;
		try {
			beginTransaction();
			collections = ((ReservationDAO)getDAO()).findMyReservation(user, area, room, date1, date2,pages,lines);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return collections;
	}
	
	public void delete(String id)throws Exception{
		try {
			beginTransaction();
			((ReservationDAO)getDAO()).remove(id);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public int countMyReservation(WebUser user, String area,
			String room, java.util.Date date1, java.util.Date date2) throws Exception{
		int count=0;
		try {
			beginTransaction();
			count = ((ReservationDAO)getDAO()).countMyReservation(user, area, room, date1, date2);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		return count;
	}
}
