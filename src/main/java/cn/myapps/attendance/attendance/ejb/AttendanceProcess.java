package cn.myapps.attendance.attendance.ejb;

import java.util.List;
import java.util.Map;

import cn.myapps.attendance.base.ejb.BaseProcess;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;

public interface AttendanceProcess extends BaseProcess<Attendance> {
	
	/**
	 * 签到
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> signin(Attendance attendance,ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 签退
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> signout(ParamsTable params,WebUser user) throws Exception;
	
	public Attendance findTodayAttendanceByUser(WebUser user) throws Exception;
	
	public List<Attendance> findAttendanceByDate(String date) throws Exception;
	/**
	 * 查询实例集合
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Attendance> recordQuery(ParamsTable params,WebUser user) throws Exception;

	public DataPackage<Attendance> queryBy(ParamsTable params, WebUser user) throws Exception;

	public DataPackage<Attendance> queryChart(WebUser user) throws Exception;
	
}
