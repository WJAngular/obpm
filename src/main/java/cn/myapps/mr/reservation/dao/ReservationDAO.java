package cn.myapps.mr.reservation.dao;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.base.dao.BaseDAO;
import cn.myapps.mr.reservation.ejb.Reservation;

public interface ReservationDAO extends BaseDAO{

	public Collection<Reservation> queryByTime(java.util.Date date ,WebUser user) throws Exception;
	public ValueObject find(String id) throws Exception ;
	public boolean create(ValueObject vo) throws Exception;
	public Collection<Reservation> findMyReservation(WebUser user, String area,
			String room, Date date1, Date date2, int pages, int lines) throws Exception;
	public boolean updateAreaName(ValueObject vo) throws Exception;
	public int countMyReservation(WebUser user, String area,
			String room, java.util.Date date1, java.util.Date date2) throws Exception;
}
