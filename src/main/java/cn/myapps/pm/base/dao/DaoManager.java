package cn.myapps.pm.base.dao;

import java.sql.Connection;

import cn.myapps.pm.activity.dao.DB2ActivityDAO;
import cn.myapps.pm.activity.dao.MsSqlActivityDAO;
import cn.myapps.pm.activity.dao.MySqlActivityDAO;
import cn.myapps.pm.activity.dao.OracleActivityDAO;
import cn.myapps.pm.project.dao.DB2ProjectDAO;
import cn.myapps.pm.project.dao.MsSqlProjectDAO;
import cn.myapps.pm.project.dao.MySqlProjectDAO;
import cn.myapps.pm.project.dao.OracleProjectDAO;
import cn.myapps.pm.tag.dao.DB2TagDAO;
import cn.myapps.pm.tag.dao.MsSqlTagDAO;
import cn.myapps.pm.tag.dao.MySqlTagDAO;
import cn.myapps.pm.tag.dao.OracleTagDAO;
import cn.myapps.pm.task.dao.DB2TaskDAO;
import cn.myapps.pm.task.dao.MsSqlTaskDAO;
import cn.myapps.pm.task.dao.MySqlTaskDAO;
import cn.myapps.pm.task.dao.OracleTaskDAO;
import cn.myapps.pm.util.ConnectionManager;

public class DaoManager {
	

	public static BaseDAO getProjectDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlProjectDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleProjectDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2ProjectDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlProjectDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getTaskDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlTaskDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleTaskDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2TaskDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlTaskDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getTagDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlTagDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleTagDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2TagDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlTagDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getActivityDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		if("MSSQL".equals(dbType)){  
			return new MsSqlActivityDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleActivityDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2ActivityDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlActivityDAO(conn);
		}
		
		return null;
	}

	
}
