package cn.myapps.attendance.attendance.dao;

import java.util.Date;
import java.util.List;

import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.attendance.ejb.AttendanceDetail;
import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;

public interface AttendanceDetailDAO extends BaseDAO {
	
	/**
	 * 获取某时间考勤记录下的考勤明细
	 * @param attendanceId 
	 *             考勤记录
	 * @param attendanceDate
	 *             时间
	 * @param domainId
	 *             企业域
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AttendanceDetail> getAttendanceDetailByAttendance(String attendanceId, Date attendanceDate , String domainId)throws Exception;

	/**
	 * 获取当前天考勤明细
	 * @param attendanceId 
	 *             考勤记录
	 * @return
	 * @throws Exception
	 */
    public DataPackage<AttendanceDetail> getTodayAttendanceDetailByAttendanceId(String attendanceId)throws Exception;

	/**
	 * 根据搜索条件查询相关考勤记录
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AttendanceDetail> queryAttendanceDetailByCondition(ParamsTable params, WebUser user) throws Exception;
	
	public List<AttendanceDetail> findAttendanceDetailByDate(String date) throws Exception;
}
