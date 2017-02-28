package cn.myapps.attendance.attendance.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;

/**
 * 考勤信息
 * @author Happy
 *
 */
public class Attendance extends ValueObject {
	
	private static final long serialVersionUID = 5379269621120117708L;
	
	/**正常**/
	public static final int STATUS_NORMAL = 1;
	/**异常**/
	public static final int STATUS_UNNORMAL = 2;
	/**迟到**/
	public static final int STATUS_LATE = -1;
	/**早退**/
	public static final int STATUS_LEAVE_EARLY = -2;
	/**迟到且早退**/
	public static final int STATE_LATE_AND_LEAVE_EARLY = -3;
	/**地点异常**/
	public static final int STATUS_WRONG_LOCATION = -4;
	
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 部门id
	 */
	private String deptId;
	
	/**
	 * 部门名称
	 */
	private String deptName;
	
	/**
	 * 考勤状态
	 */
	private int status;
	
	/**
	 * 考勤记录列表
	 */
	private List<AttendanceDetail> attendanceDetailList ;
	
	/**
	 * 考勤日期(yyyy-MM-dd)
	 */
	private Date attendanceDate;
	
	/**
	 * 工作时长
	 */
	private double workingHours;
	
	/**
	 * 多时段考勤
	 */
	private Boolean multiPeriod;
	
	
	public double getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(double workingHours) {
		this.workingHours = workingHours;
	}

	public Boolean isMultiPeriod() {
		return multiPeriod;
	}

	public void setMultiPeriod(Boolean multiPeriod) {
		this.multiPeriod = multiPeriod;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
    
	public List<AttendanceDetail> getAttendanceDetailList() {
		if(attendanceDetailList == null ){
			attendanceDetailList =  new ArrayList<AttendanceDetail>();
		}
		return attendanceDetailList;
	}
	
	public void setAttendanceDetailList(List<AttendanceDetail> attendanceDetailList) {
		this.attendanceDetailList = attendanceDetailList;
	}
}
