package cn.myapps.attendance.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.attendance.ejb.AttendanceDetail;
import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.km.util.StringUtil;

public class OracleAttendanceDetailDAO extends AbstractAttendanceDetailDAO implements AttendanceDetailDAO {
	public OracleAttendanceDetailDAO(Connection conn) throws Exception {
		super(conn);
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getUserName().trim()
						.toUpperCase();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
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
	@Override
	protected String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect
				.append("select *  FROM  ( select row_.*, rownum rownum_  FROM  ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		// if (orderby != null && !orderby.trim().equals(""))
		// pagingSelect.append(orderby);

		return pagingSelect.toString();
	}
	
	public DataPackage<?> recordquery(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<AttendanceDetail> datas = new DataPackage<AttendanceDetail>();
		Collection<AttendanceDetail> list = new ArrayList<AttendanceDetail>();
		
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
			sql +=" AND USER_ID = '"+userid+"' AND to_char(ATTENDANCE_DATE,'yyyy-mm') = to_char(sysdate,'yyyy-mm') ORDER BY ATTENDANCE_DATE DESC";
			countSQL +=" AND USER_ID like '%"+userid+"%' AND to_char(ATTENDANCE_DATE,'yyyy-mm') = to_char(sysdate,'yyyy-mm') ORDER BY ATTENDANCE_DATE DESC";
		}
		
		try {
			stmt = connection.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, user.getDomainid());
			
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



}
