package cn.myapps.attendance.attendance.dao;

import java.util.List;

import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;

public interface AttendanceDAO extends BaseDAO {
	
	public Attendance findTodayAttendanceByUser(WebUser user) throws Exception;
	
	public List<Attendance> findAttendanceByDate(String date) throws Exception;
	
	public DataPackage<?> recordquery(ParamsTable params,WebUser user) throws Exception;

	public DataPackage<Attendance> queryby(ParamsTable params, WebUser user) throws Exception;

	public DataPackage<Attendance> queryChart(WebUser user) throws Exception;
	
}
