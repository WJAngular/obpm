package cn.myapps.attendance.base.dao;

import java.sql.Connection;

import cn.myapps.attendance.attendance.dao.DB2AttendanceDAO;
import cn.myapps.attendance.attendance.dao.DB2AttendanceDetailDAO;
import cn.myapps.attendance.attendance.dao.MsSqlAttendanceDAO;
import cn.myapps.attendance.attendance.dao.MsSqlAttendanceDetailDAO;
import cn.myapps.attendance.attendance.dao.MySqlAttendanceDAO;
import cn.myapps.attendance.attendance.dao.MySqlAttendanceDetailDAO;
import cn.myapps.attendance.attendance.dao.OracleAttendanceDAO;
import cn.myapps.attendance.attendance.dao.OracleAttendanceDetailDAO;
import cn.myapps.attendance.location.dao.DB2LocationDAO;
import cn.myapps.attendance.location.dao.MsSqlLocationDAO;
import cn.myapps.attendance.location.dao.MySqlLocationDAO;
import cn.myapps.attendance.location.dao.OracleLocationDAO;
import cn.myapps.attendance.rule.dao.DB2RuleDAO;
import cn.myapps.attendance.rule.dao.MsSqlRuleDAO;
import cn.myapps.attendance.rule.dao.MySqlRuleDAO;
import cn.myapps.attendance.rule.dao.OracleRuleDAO;
import cn.myapps.attendance.util.ConnectionManager;

public class DaoManager {
	

	public static BaseDAO getLocationDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlLocationDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleLocationDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2LocationDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlLocationDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getRuleDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlRuleDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleRuleDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2RuleDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlRuleDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getAttendanceDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlAttendanceDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleAttendanceDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2AttendanceDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlAttendanceDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getRecordDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlAttendanceDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleAttendanceDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2AttendanceDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlAttendanceDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getAttendanceDetailDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlAttendanceDetailDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleAttendanceDetailDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2AttendanceDetailDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlAttendanceDetailDAO(conn);
		}
		return null;
	}
}
