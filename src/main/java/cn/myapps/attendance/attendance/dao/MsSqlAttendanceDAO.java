package cn.myapps.attendance.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.km.util.StringUtil;


public class MsSqlAttendanceDAO extends AbstractAttendanceDAO implements AttendanceDAO{
	private static final Logger log = Logger.getLogger(MsSqlAttendanceDAO.class);
	public MsSqlAttendanceDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MS SQL SERVER: ";
		try {
			ResultSet rs = conn.getMetaData().getSchemas();
			if (rs != null) {
				if (rs.next())
					this.schema = rs.getString(1).trim().toUpperCase();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/**
	 * 生成限制条件sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @return 生成限制条件sql语句字符串
	 * @throws SQLException 
	 */
	protected String buildLimitString(String sql, int page, int lines, String orderbyField, String orderbyMode) throws SQLException {

		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		// int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		int databaseVersion = connection.getMetaData()
				.getDatabaseMajorVersion();
		
		if (9 <= databaseVersion) {// 2005 row_number() over () 分页
			pagingSelect.append("SELECT TOP " + lines + " * FROM (");
			pagingSelect
					.append("SELECT ROW_NUMBER() OVER (ORDER BY ").append(orderbyField).append(" ").append(orderbyMode).append(") AS ROWNUMBER, TABNIC.* FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC) TableNickname ");
			pagingSelect.append("WHERE ROWNUMBER>" + lines * (page - 1));

		} else {
			pagingSelect.append("SELECT TOP " + lines * page + " * FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC");
		}

		return pagingSelect.toString();
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
			sql +=" AND USER_ID = '"+userid+"' AND datediff(month,[ATTENDANCE_DATE],getdate())=0 ORDER BY ATTENDANCE_DATE DESC";
			countSQL +=" AND USER_ID ='"+userid+"' AND datediff(month,[ATTENDANCE_DATE],getdate())=0 ";
		}
		
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
	
	public DataPackage<Attendance> queryChart(WebUser user) throws Exception {
		
		DataPackage<Attendance> datas = new DataPackage<Attendance>();
		Collection<Attendance> list = new ArrayList<Attendance>();
		
		PreparedStatement stmt = null;
		
		String sql = "SELECT COUNT(STATUS),STATUS FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? AND datediff(month,[ATTENDANCE_DATE],getdate())=0 GROUP BY STATUS ";
		
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
