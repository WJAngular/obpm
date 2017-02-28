package cn.myapps.attendance.attendance.ejb;

import java.util.Date;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;

/**
 * 考勤记录
 * @author Seven
 *
 */
/**
 * @author Seven
 *
 */
/**
 * @author Seven
 *
 */
public class AttendanceDetail extends ValueObject {
	
	private static final long serialVersionUID = 923637378821988095L;
	/**正常**/
	public static final int STATUS_NORMAL = 1;
	/**迟到**/
	public static final int STATUS_LATE = -1;
	/**早退**/
	public static final int STATUS_LEAVE_EARLY = -2;
	/**迟到且早退**/
	public static final int STATE_LATE_AND_LEAVE_EARLY = -3;
	/**地点异常**/
	public static final int STATUS_WRONG_LOCATION = -4;
	
	/**
	 * 考勤记录id
	 */
	private String attendanceId ;
	
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
	 * 签到时间
	 */
	private Date signinTime;
	
	/**
	 * 签退时间
	 */
	private Date signoutTime;
	
	/**
	 * 签到地点
	 */
	private String signinLocation;
	
	/**
	 * 签退地点
	 */
	private String signoutLocation;
	
	/**
	 * 工作时长
	 */
	private double workingHours;
	
	/**
	 * 考勤状态
	 */
	private int status;
	
	/**
	 * 考勤日期(yyyy-MM-dd)
	 */
	private Date attendanceDate;
	
	/**
	 * 时间范围
	 */
	private String timeRegion;

	
	public String getTimeRegion() {
		return timeRegion;
	}

	public void setTimeRegion(String timeRegion) {
		this.timeRegion = timeRegion;
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

	public Date getSigninTime() {
		return signinTime;
	}

	public void setSigninTime(Date signinTime) {
		this.signinTime = signinTime;
	}

	public Date getSignoutTime() {
		return signoutTime;
	}

	public void setSignoutTime(Date signoutTime) {
		this.signoutTime = signoutTime;
	}

	public String getSigninLocation() {
		return signinLocation;
	}

	public void setSigninLocation(String signinLocation) {
		this.signinLocation = signinLocation;
	}

	public String getSignoutLocation() {
		return signoutLocation;
	}

	public void setSignoutLocation(String signoutLocation) {
		this.signoutLocation = signoutLocation;
	}

	public double getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(double workingHours) {
		this.workingHours = workingHours;
	}

	public int getStatus() {
		return status;
	}

	public String getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(String attendanceId) {
		this.attendanceId = attendanceId;
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

}
