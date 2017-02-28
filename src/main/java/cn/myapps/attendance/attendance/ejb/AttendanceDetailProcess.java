package cn.myapps.attendance.attendance.ejb;

import java.util.Date;
import java.util.List;

import cn.myapps.attendance.base.ejb.BaseProcess;
import cn.myapps.base.dao.DataPackage;

public interface AttendanceDetailProcess extends BaseProcess<AttendanceDetail> {
	
	/**
	 * 获取对应考勤记录下的考勤明细
	 * @param id
	 * @param attendanceDate
	 * @return
	 */
	public DataPackage<AttendanceDetail> getAttendanceDetailByAttendance(String id, Date attendanceDate,String domainId)throws Exception;

	/**
	 * 获取当前天考勤记录下的考勤明细
	 * @param id
	 * @param attendanceDate
	 * @return
	 */
	public DataPackage<AttendanceDetail> getTodayAttendanceDetailByAttendanceId(String id) throws Exception;
	
	public List<AttendanceDetail> findAttendanceDetailByDate(String date) throws Exception;
	
}
