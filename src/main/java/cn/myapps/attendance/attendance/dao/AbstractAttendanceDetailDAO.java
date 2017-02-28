package cn.myapps.attendance.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserProcessBean;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.attendance.base.dao.AbstractBaseDAO;
import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.attendance.ejb.AttendanceDetail;
import cn.myapps.attendance.util.ConnectionManager;

public abstract class AbstractAttendanceDetailDAO extends AbstractBaseDAO implements AttendanceDetailDAO{

	private static final Logger log = Logger.getLogger(AbstractAttendanceDetailDAO.class);

	public AbstractAttendanceDetailDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "AM_ATTENDANCE_DETAIL";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		AttendanceDetail attendanceDetail = (AttendanceDetail) vo;

		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName(tableName)
				+ " (ID,USER_ID,USER_NAME,DEPT_ID,DEPT_NAME,SIGNIN_TIME,SIGNOUT_TIME,SIGNIN_LOCATION,SIGNOUT_LOCATION,WORKING_HOURS,STATUS,ATTENDANCE_DATE,DOMAIN_ID,ATTENDANCE_ID,TIMEREGION) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, attendanceDetail.getId());
			stmt.setString(2, attendanceDetail.getUserId());
			stmt.setString(3, attendanceDetail.getUserName());
			stmt.setString(4, attendanceDetail.getDeptId());
			stmt.setString(5, attendanceDetail.getDeptName());
			if (attendanceDetail.getSigninTime() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendanceDetail.getSigninTime().getTime());
				stmt.setTimestamp(6, ts);
			}
			if (attendanceDetail.getSignoutTime() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendanceDetail.getSignoutTime().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setString(8, attendanceDetail.getSigninLocation());
			stmt.setString(9, attendanceDetail.getSignoutLocation());
			stmt.setDouble(10, attendanceDetail.getWorkingHours());
			stmt.setInt(11, attendanceDetail.getStatus());
			if (attendanceDetail.getAttendanceDate() == null) {
				stmt.setNull(12, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendanceDetail.getAttendanceDate().getTime());
				stmt.setTimestamp(12, ts);
			}
			stmt.setString(13, attendanceDetail.getDomainid());
			stmt.setString(14, attendanceDetail.getAttendanceId());
			stmt.setString(15, attendanceDetail.getTimeRegion());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;

	}

	public ValueObject find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			AttendanceDetail attendanceDetail = null;
			if (rs.next()) {
				attendanceDetail = new AttendanceDetail();
				setProperties(attendanceDetail, rs);
			}
			rs.close();
			return attendanceDetail;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			log.debug(sql);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}

	}

	public ValueObject update(ValueObject vo) throws Exception {
		AttendanceDetail attendanceDetail = (AttendanceDetail) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE " + getFullTableName(tableName)
				+ " SET USER_ID=?,USER_NAME=?,DEPT_ID=?,DEPT_NAME=?,SIGNIN_TIME=?,SIGNOUT_TIME=?,SIGNIN_LOCATION=?,SIGNOUT_LOCATION=?,WORKING_HOURS=?,STATUS=?,ATTENDANCE_DATE=?,DOMAIN_ID=?,ATTENDANCE_ID=?,TIMEREGION=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, attendanceDetail.getUserId());
			stmt.setString(2, attendanceDetail.getUserName());
			stmt.setString(3, attendanceDetail.getDeptId());
			stmt.setString(4, attendanceDetail.getDeptName());
			if (attendanceDetail.getSigninTime() == null) {
				stmt.setNull(5, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendanceDetail.getSigninTime().getTime());
				stmt.setTimestamp(5, ts);
			}
			if (attendanceDetail.getSignoutTime() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendanceDetail.getSignoutTime().getTime());
				stmt.setTimestamp(6, ts);
			}
			stmt.setString(7, attendanceDetail.getSigninLocation());
			stmt.setString(8, attendanceDetail.getSignoutLocation());
			stmt.setDouble(9, attendanceDetail.getWorkingHours());
			stmt.setInt(10, attendanceDetail.getStatus());
			if (attendanceDetail.getAttendanceDate() == null) {
				stmt.setNull(11, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendanceDetail.getAttendanceDate().getTime());
				stmt.setTimestamp(11, ts);
			}
			stmt.setString(12, attendanceDetail.getDomainid());
			stmt.setString(13, attendanceDetail.getAttendanceId());
			stmt.setString(14, attendanceDetail.getTimeRegion());
			stmt.setString(15, attendanceDetail.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}

	void setProperties(AttendanceDetail attendanceDetail, ResultSet rs) throws Exception {
		try {
			attendanceDetail.setId(rs.getString("ID"));
			attendanceDetail.setUserId(rs.getString("USER_ID"));
			attendanceDetail.setUserName(rs.getString("USER_NAME"));
			attendanceDetail.setDeptId(rs.getString("DEPT_ID"));
			attendanceDetail.setDeptName(rs.getString("DEPT_NAME"));
			attendanceDetail.setSigninTime(rs.getTimestamp("SIGNIN_TIME"));
			attendanceDetail.setSignoutTime(rs.getTimestamp("SIGNOUT_TIME"));
			attendanceDetail.setSigninLocation(rs.getString("SIGNIN_LOCATION"));
			attendanceDetail.setSignoutLocation(rs.getString("SIGNOUT_LOCATION"));
			attendanceDetail.setWorkingHours(rs.getDouble("WORKING_HOURS"));
			attendanceDetail.setStatus(rs.getInt("STATUS"));
			attendanceDetail.setAttendanceDate(rs.getTimestamp("ATTENDANCE_DATE"));
			attendanceDetail.setDomainid(rs.getString("DOMAIN_ID"));
			attendanceDetail.setAttendanceId(rs.getString("ATTENDANCE_ID"));
			attendanceDetail.setTimeRegion(rs.getString("TIMEREGION"));
			
		} catch (SQLException e) {
			throw e;
		}

	}	
	
	public DataPackage<AttendanceDetail> getTodayAttendanceDetailByAttendanceId(String id)throws Exception{
		PreparedStatement stmt = null;
		
		DataPackage<AttendanceDetail> datas = new DataPackage<AttendanceDetail>();
		Collection<AttendanceDetail> list = new ArrayList<AttendanceDetail>();
		
		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE ATTENDANCE_ID=?  AND ATTENDANCE_DATE=? " ;
		
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			stmt.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
			
			ResultSet rs = stmt.executeQuery();
			AttendanceDetail attendanceDetail = null;
			while (rs.next()) {
				attendanceDetail = new AttendanceDetail();
				setProperties(attendanceDetail, rs);
				list.add(attendanceDetail);
			}
			
			datas.setDatas(list);
			rs.close();
			
			return datas;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public DataPackage<AttendanceDetail> getAttendanceDetailByAttendance (String id, Date attendanceDate ,String domainId)throws Exception{
		PreparedStatement stmt = null;
		
		DataPackage<AttendanceDetail> datas = new DataPackage<AttendanceDetail>();
		Collection<AttendanceDetail> list = new ArrayList<AttendanceDetail>();
		
		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE ATTENDANCE_ID=?  AND ATTENDANCE_DATE=? " ;
		
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(attendanceDate);
			stmt.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
			
			ResultSet rs = stmt.executeQuery();
			AttendanceDetail attendanceDetail = null;
			while (rs.next()) {
				attendanceDetail = new AttendanceDetail();
				setProperties(attendanceDetail, rs);
				list.add(attendanceDetail);
			}
			
			datas.setDatas(list);
			rs.close();
			
			return datas;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public Collection<?> simpleQuery(ParamsTable params, WebUser user)
			throws Exception {
		Collection<AttendanceDetail> rtn = new ArrayList<AttendanceDetail>();

		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ORDER BY ID DESC";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			ResultSet rs = stmt.executeQuery();
			AttendanceDetail attendanceDetail = null;
			while (rs.next()) {
				attendanceDetail = new AttendanceDetail();
				setProperties(attendanceDetail, rs);
				rtn.add(attendanceDetail);
			}
			
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;

	}
	
    public DataPackage<?> query(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<AttendanceDetail> datas = new DataPackage<AttendanceDetail>();
		Collection<AttendanceDetail> list = new ArrayList<AttendanceDetail>();
		
		int page = params.getParameterAsInteger("page");
		int lines = params.getParameterAsInteger("rows");
		
		String name = params.getParameterAsString("name");
		String startDate = params.getParameterAsString("startDate");
		String endDate = params.getParameterAsString("endDate");
		
		String attendanceId = params.getParameterAsString("name");

		PreparedStatement stmt = null;
		PreparedStatement countStmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ";
		
		String countSQL =  "SELECT count(*) FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=?";
		
		if(!StringUtil.isBlank(startDate)){
			sql +=" AND ATTENDANCE_DATE>=?";
			countSQL +=" AND ATTENDANCE_DATE>=?";
		}
		if(!StringUtil.isBlank(endDate)){
			sql +=" AND ATTENDANCE_DATE<=?";
			countSQL +=" AND ATTENDANCE_DATE<=?";
		}
		if(!StringUtil.isBlank(name)){
			sql +=" AND USER_NAME like '%"+name+"%'";
			countSQL +=" AND USER_NAME like '%"+name+"%'";
		}
		
		sql = buildLimitString(sql, page, lines, "ID", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, user.getDomainid());
			if(!StringUtil.isBlank(startDate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				stmt.setTimestamp(index++, new Timestamp(calendar.getTimeInMillis()));
			}
			if(!StringUtil.isBlank(endDate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				stmt.setTimestamp(index++, new Timestamp(calendar.getTimeInMillis()));
			}
			
			ResultSet rs = stmt.executeQuery();
			AttendanceDetail attendanceDetail = null;
			while (rs.next()) {
				attendanceDetail = new AttendanceDetail();
				setProperties(attendanceDetail, rs);
				list.add(attendanceDetail);
			}
			datas.setDatas(list);
			rs.close();
			
			countStmt = connection.prepareStatement(countSQL);
			int index2 = 1;
			countStmt.setString(index2++, user.getDomainid());
			if(!StringUtil.isBlank(startDate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				countStmt.setTimestamp(index2++, new Timestamp(calendar.getTimeInMillis()));
			}
			if(!StringUtil.isBlank(endDate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				countStmt.setTimestamp(index2++, new Timestamp(calendar.getTimeInMillis()));
			}
			ResultSet cr = countStmt.executeQuery();
			if(cr.next()){
				datas.setRowCount(cr.getInt(1));
			}
			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(countStmt);
		}
		return datas;
	} 
	
  //因思程科技考勤业务需要，故新增该接口，根据日期获取员工的考勤信息
  		public List<AttendanceDetail> findAttendanceDetailByDate(String date) throws Exception{
  			PreparedStatement stmt = null;
  			List<AttendanceDetail> listattendanceDetail = new ArrayList<AttendanceDetail>();
  			String sql = "SELECT * FROM " + getFullTableName(tableName)
  					+ " WHERE ATTENDANCE_DATE=?";
  			
  			log.info(sql);
  			try {
  				stmt = connection.prepareStatement(sql);
  				stmt.setString(1, date);

  				ResultSet rs = stmt.executeQuery();
  				AttendanceDetail attendanceDetail = null;
  				while(rs.next()) {
  					attendanceDetail = new AttendanceDetail();
  					setProperties(attendanceDetail, rs);
  					listattendanceDetail.add(attendanceDetail);
  				}
  				rs.close();
  				return listattendanceDetail;
  			} catch (Exception e) {
  				e.printStackTrace();
  				throw e;
  			} finally {
  				ConnectionManager.closeStatement(stmt);
  			}
  		}
    
    public DataPackage<AttendanceDetail> queryAttendanceDetailByCondition(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<AttendanceDetail> datas = new DataPackage<AttendanceDetail>();
		Collection<AttendanceDetail> list = new ArrayList<AttendanceDetail>();
		
		//int page = params.getParameterAsInteger("page");
		//int lines = params.getParameterAsInteger("rows");
		
		String userId = params.getParameterAsString("userId");
		String signdate = params.getParameterAsString("signdate");
		String signenddate = params.getParameterAsString("signenddate");
		String status = params.getParameterAsString("status");
		PreparedStatement stmt = null;
		PreparedStatement countStmt = null;
		
			if("正常".equals(status)){
				status = "1";
			}else if("迟到".equals(status)){
				status = "-1";
			}else if("早退".equals(status)){
				status = "-2";
			}else if("迟到且早退".equals(status)){
				status = "-3";
			}else if("地点异常".equals(status)){
				status = "-4";
			}

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ";
		
		String countSQL =  "SELECT count(*) FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=?";
		
		if(!StringUtil.isBlank(signdate)){
			sql +=" AND ATTENDANCE_DATE>=?";
			countSQL +=" AND ATTENDANCE_DATE>=?";
		}
		if(!StringUtil.isBlank(signenddate)){//alvin
			sql +=" AND ATTENDANCE_DATE<=?";
			countSQL +=" AND ATTENDANCE_DATE<=?";
		}
		if(!StringUtil.isBlank(status)){
			sql +=" AND STATUS=?";
			countSQL +=" AND STATUS=?";
		}
//		if(!StringUtil.isBlank(userName)){
//			sql +=" AND USER_NAME like '%"+userName+"%'";
//			countSQL +=" AND USER_NAME like '%"+userName+"%'";
//		}
		if(!StringUtil.isBlank(userId)){
			sql +=" AND USER_ID IN ('"+userId+"') ORDER BY SIGNIN_TIME DESC";
			countSQL +=" AND USER_ID IN ('"+userId+"') ORDER BY SIGNIN_TIME DESC";
		}
		
		
		//sql = buildLimitString(sql, page, lines, "ID", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, user.getDomainid());
			if(!StringUtil.isBlank(signdate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(signdate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				stmt.setTimestamp(index++, new Timestamp(calendar.getTimeInMillis()));
			}
			if(!StringUtil.isBlank(signenddate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(signenddate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				stmt.setTimestamp(index++, new Timestamp(calendar.getTimeInMillis()));
			}
			if(!StringUtil.isBlank(status)){
				stmt.setString(index++, status);
			}
			
			ResultSet rs = stmt.executeQuery();
			AttendanceDetail attendanceDetail = null;
			while (rs.next()) {
				attendanceDetail = new AttendanceDetail();
				setProperties(attendanceDetail, rs);
				list.add(attendanceDetail);
			}
			datas.setDatas(list);
			rs.close();
			
			countStmt = connection.prepareStatement(countSQL);
			int index2 = 1;
			countStmt.setString(index2++, user.getDomainid());
			if(!StringUtil.isBlank(signdate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(signdate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				countStmt.setTimestamp(index2++, new Timestamp(calendar.getTimeInMillis()));
			}
			if(!StringUtil.isBlank(signenddate)){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(signenddate));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				countStmt.setTimestamp(index2++, new Timestamp(calendar.getTimeInMillis()));
			}
			if(!StringUtil.isBlank(status)){
				countStmt.setString(index2++, status);
			}
			ResultSet cr = countStmt.executeQuery();
			if(cr.next()){
				datas.setRowCount(cr.getInt(1));
			}
			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(countStmt);
		}
		return datas;
	}

    /**
     * 考勤明细报表统计
     * @param user
     * @return
     * @throws Exception
     */
    public DataPackage<AttendanceDetail> queryChart(WebUser user) throws Exception {
	
	DataPackage<AttendanceDetail> datas = new DataPackage<AttendanceDetail>();
	Collection<AttendanceDetail> list = new ArrayList<AttendanceDetail>();
	
	PreparedStatement stmt = null;
	
	String sql = "SELECT COUNT(STATUS),STATUS FROM " + getFullTableName(tableName)
			+ " WHERE DOMAIN_ID=? AND DATE_FORMAT(ATTENDANCE_DATE,'%Y-%m')=DATE_FORMAT(now(),'%Y-%m') GROUP BY STATUS ";
	
	log.info(sql);
	try {
		stmt = connection.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, user.getDomainid());
		ResultSet rs = stmt.executeQuery();
		AttendanceDetail attendanceDetail = null;
		while (rs.next()) {
			
			attendanceDetail = new AttendanceDetail();
			attendanceDetail.setVersion(rs.getInt("COUNT(STATUS)"));
			attendanceDetail.setStatus(rs.getInt("STATUS"));
			list.add(attendanceDetail);
		}
		datas.setDatas(list);
		rs.close();
		
	} catch (Exception e) {
		e.printStackTrace();
		throw e;
	} finally {
		ConnectionManager.closeStatement(stmt);
	}
	
	return datas;
	
}

}

