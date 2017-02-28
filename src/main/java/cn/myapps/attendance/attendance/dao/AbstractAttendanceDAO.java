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

public abstract class AbstractAttendanceDAO extends AbstractBaseDAO {

	private static final Logger log = Logger.getLogger(AbstractAttendanceDAO.class);

	public AbstractAttendanceDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "AM_ATTENDANCE";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		Attendance attendance = (Attendance) vo;

		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName(tableName)
				+ " (ID,USER_ID,USER_NAME,DEPT_ID,DEPT_NAME,STATUS,ATTENDANCE_DATE,MULTI_PERIOD,DOMAIN_ID,WORKING_HOURS) values (?,?,?,?,?,?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, attendance.getId());
			stmt.setString(2, attendance.getUserId());
			stmt.setString(3, attendance.getUserName());
			stmt.setString(4, attendance.getDeptId());
			stmt.setString(5, attendance.getDeptName());
			stmt.setInt(6, attendance.getStatus());
			if (attendance.getAttendanceDate() == null) {
				stmt.setNull(7, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendance.getAttendanceDate().getTime());
				stmt.setTimestamp(7, ts);
			}
			stmt.setBoolean(8, attendance.isMultiPeriod());
			stmt.setString(9, attendance.getDomainid());
			stmt.setDouble(10, attendance.getWorkingHours());
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
			Attendance attendance = null;
			if (rs.next()) {
				attendance = new Attendance();
				setProperties(attendance, rs);
			}
			rs.close();
			return attendance;
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
		Attendance attendance = (Attendance) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE " + getFullTableName(tableName)
				+ " SET USER_ID=?,USER_NAME=?,DEPT_ID=?,DEPT_NAME=?,STATUS=?,ATTENDANCE_DATE=?,MULTI_PERIOD=?,DOMAIN_ID=? ,WORKING_HOURS=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, attendance.getUserId());
			stmt.setString(2, attendance.getUserName());
			stmt.setString(3, attendance.getDeptId());
			stmt.setString(4, attendance.getDeptName());
			stmt.setInt(5, attendance.getStatus());
			if (attendance.getAttendanceDate() == null) {
				stmt.setNull(6, java.sql.Types.TIMESTAMP);
			} else {
				Timestamp ts = new Timestamp(attendance.getAttendanceDate().getTime());
				stmt.setTimestamp(6, ts);
			}
			stmt.setBoolean(7, attendance.isMultiPeriod());
			stmt.setString(8, attendance.getDomainid());
			stmt.setDouble(9, attendance.getWorkingHours());
			stmt.setString(10, attendance.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}

	public DataPackage<AttendanceDetail> findTodayAttendanceByAttendanceId(String id)throws Exception{
		
		DataPackage<AttendanceDetail> datas = new DataPackage<AttendanceDetail>();
		Collection<AttendanceDetail> list = new ArrayList<AttendanceDetail>();
		
		return datas;
	    
	};

	public Collection<?> simpleQuery(ParamsTable params, WebUser user)
			throws Exception {
		Collection<Attendance> rtn = new ArrayList<Attendance>();

		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ORDER BY ID DESC";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			ResultSet rs = stmt.executeQuery();
			Attendance attendance = null;
			while (rs.next()) {
				attendance = new Attendance();
				setProperties(attendance, rs);
				rtn.add(attendance);
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
	
	public DataPackage<?> recordquery(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<Attendance> datas = new DataPackage<Attendance>();
		Collection<Attendance> list = new ArrayList<Attendance>();
		
		//int page = params.getParameterAsInteger("page");
		//int lines = params.getParameterAsInteger("rows");
		
		String userid = user.getId();

		PreparedStatement stmt = null;
		PreparedStatement countStmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ";
		
		String countSQL =  "SELECT count(*) FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=?";
		
		if(!StringUtil.isBlank(userid)){
			sql +=" AND USER_ID like '%"+userid+"%' AND YEARWEEK(date_format(ATTENDANCE_DATE,"
					+ "'%Y-%m-%d')) = YEARWEEK(now()) ORDER BY ATTENDANCE_DATE DESC";
			countSQL +=" AND USER_ID like '%"+userid+"%' AND YEARWEEK(date_format(ATTENDANCE_DATE,"
					+ "'%Y-%m-%d')) = YEARWEEK(now()) ORDER BY ATTENDANCE_DATE DESC";
		}
		
		//sql = buildLimitString(sql, page, lines, "ID", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, user.getDomainid());
			
			ResultSet rs = stmt.executeQuery();
			Attendance attendance = null;
			while (rs.next()) {
				attendance = new Attendance();
				setProperties(attendance, rs);
				list.add(attendance);
			}
			datas.setDatas(list);
			rs.close();
			
			countStmt = connection.prepareStatement(countSQL);
			int index2 = 1;
			countStmt.setString(index2++, user.getDomainid());
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
	
public DataPackage<?> query(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<Attendance> datas = new DataPackage<Attendance>();
		Collection<Attendance> list = new ArrayList<Attendance>();
		
		int page = params.getParameterAsInteger("page");
		int lines = params.getParameterAsInteger("rows");
		
		String name = params.getParameterAsString("name");
		String startDate = params.getParameterAsString("startDate");
		String endDate = params.getParameterAsString("endDate");

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
			Attendance attendance = null;
			while (rs.next()) {
				attendance = new Attendance();
				setProperties(attendance, rs);
				list.add(attendance);
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
	
	public Attendance findTodayAttendanceByUser(WebUser user) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE USER_ID=? AND ATTENDANCE_DATE=?";
		
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getId());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			
			stmt.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));

			ResultSet rs = stmt.executeQuery();
			Attendance attendance = null;
			if (rs.next()) {
				attendance = new Attendance();
				setProperties(attendance, rs);
			}
			rs.close();
			return attendance;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	//因思程科技考勤业务需要，故新增该接口，根据日期获取员工的考勤信息
		public List<Attendance> findAttendanceByDate(String date) throws Exception{
			PreparedStatement stmt = null;
			List<Attendance> listAttendance = new ArrayList<Attendance>();
			String sql = "SELECT * FROM " + getFullTableName(tableName)
					+ " WHERE ATTENDANCE_DATE=?";
			
			log.info(sql);
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, date);

				ResultSet rs = stmt.executeQuery();
				Attendance attendance = null;
				while(rs.next()) {
					attendance = new Attendance();
					setProperties(attendance, rs);
					listAttendance.add(attendance);
				}
				rs.close();
				return listAttendance;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				ConnectionManager.closeStatement(stmt);
			}
		}
	
	void setProperties(Attendance attendance, ResultSet rs) throws Exception {
		try {
			attendance.setId(rs.getString("ID"));
			attendance.setUserId(rs.getString("USER_ID"));
			attendance.setUserName(rs.getString("USER_NAME"));
			attendance.setDeptId(rs.getString("DEPT_ID"));
			attendance.setDeptName(rs.getString("DEPT_NAME"));
			attendance.setStatus(rs.getInt("STATUS"));
			attendance.setMultiPeriod(rs.getBoolean("MULTI_PERIOD"));
			attendance.setAttendanceDate(rs.getTimestamp("ATTENDANCE_DATE"));
			attendance.setDomainid(rs.getString("DOMAIN_ID"));
			attendance.setWorkingHours(rs.getDouble("WORKING_HOURS"));
		} catch (SQLException e) {
			throw e;
		}

	}
	
public DataPackage<Attendance> queryby(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<Attendance> datas = new DataPackage<Attendance>();
		Collection<Attendance> list = new ArrayList<Attendance>();
		
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
			Attendance attendance = null;
			while (rs.next()) {
				attendance = new Attendance();
				setProperties(attendance, rs);
				list.add(attendance);
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

public DataPackage<Attendance> queryChart(WebUser user) throws Exception {
	
	DataPackage<Attendance> datas = new DataPackage<Attendance>();
	Collection<Attendance> list = new ArrayList<Attendance>();
	
	PreparedStatement stmt = null;
	
	String sql = "SELECT COUNT(STATUS),STATUS FROM " + getFullTableName(tableName)
			+ " WHERE DOMAIN_ID=? AND DATE_FORMAT(ATTENDANCE_DATE,'%Y-%m')=DATE_FORMAT(now(),'%Y-%m') GROUP BY STATUS ";
	
	log.info(sql);
	try {
		stmt = connection.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, user.getDomainid());
		ResultSet rs = stmt.executeQuery();
		Attendance attendance = null;
		while (rs.next()) {
			
			attendance = new Attendance();
			attendance.setVersion(rs.getInt("COUNT(STATUS)"));
			attendance.setStatus(rs.getInt("STATUS"));
			list.add(attendance);
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

