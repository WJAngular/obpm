package cn.myapps.attendance.attendance.ejb;

import java.util.Date;
import java.util.List;

import cn.myapps.attendance.attendance.dao.AttendanceDetailDAO;
import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.attendance.base.dao.DaoManager;
import cn.myapps.attendance.base.ejb.AbstractBaseProcessBean;
import cn.myapps.base.dao.DataPackage;


public class AttendanceDetailProcessBean extends AbstractBaseProcessBean<AttendanceDetail>
		implements AttendanceDetailProcess {


	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getAttendanceDetailDAO(getConnection());
	}

	@Override
	public DataPackage<AttendanceDetail> getTodayAttendanceDetailByAttendanceId(String id) throws Exception {
		return (DataPackage<AttendanceDetail>) ((AttendanceDetailDAO) getDAO()).getTodayAttendanceDetailByAttendanceId(id);
	}

	@Override
	public DataPackage<AttendanceDetail> getAttendanceDetailByAttendance(String id, Date attendanceDate,String domainId)throws Exception {
		return (DataPackage<AttendanceDetail>) ((AttendanceDetailDAO) getDAO()).getAttendanceDetailByAttendance(id,attendanceDate,domainId);
	}

	/** 
	* @Title: findAttendanceByDate 
	* @Description: TODO
	* @param: @param date
	* @param: @return
	* @param: @throws Exception 
	* @throws 
	*/
	@Override
	public List<AttendanceDetail> findAttendanceDetailByDate(String date)
			throws Exception {
		return ((AttendanceDetailDAO)getDAO()).findAttendanceDetailByDate(date);
	}



}
