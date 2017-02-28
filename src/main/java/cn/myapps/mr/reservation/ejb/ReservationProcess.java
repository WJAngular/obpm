package cn.myapps.mr.reservation.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mr.base.ejb.BaseProcess;

public interface ReservationProcess extends BaseProcess<Reservation>{

	public boolean doCreate(ValueObject vo,WebUser user) throws Exception;
	
	public Collection<Reservation> queryByTime(Date date,WebUser user) throws Exception;
	
	public ValueObject queryById(String id)throws Exception;
	
	public Collection<Reservation> queryMyReservation(WebUser user,String area,String room,Date date1,Date date2,int pages,int lines)throws Exception;

	public void delete(String id)throws Exception;
	
	public int countMyReservation(WebUser user, String area,
			String room, java.util.Date date1, java.util.Date date2) throws Exception;
}
