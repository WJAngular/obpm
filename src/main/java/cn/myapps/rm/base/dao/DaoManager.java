package cn.myapps.rm.base.dao;

import java.sql.Connection;

import cn.myapps.rm.resource.dao.DB2ResourceDAO;
import cn.myapps.rm.resource.dao.MsSqlResourceDAO;
import cn.myapps.rm.resource.dao.MySqlResourceDAO;
import cn.myapps.rm.resource.dao.OracleResourceDAO;
import cn.myapps.rm.role.dao.DB2RoleDAO;
import cn.myapps.rm.role.dao.DB2UserRoleSetDAO;
import cn.myapps.rm.role.dao.MsSqlRoleDAO;
import cn.myapps.rm.role.dao.MsSqlUserRoleSetDAO;
import cn.myapps.rm.role.dao.MySqlRoleDAO;
import cn.myapps.rm.role.dao.MySqlUserRoleSetDAO;
import cn.myapps.rm.role.dao.OracleRoleDAO;
import cn.myapps.rm.role.dao.OracleUserRoleSetDAO;
import cn.myapps.rm.usage.dao.DB2ResourceUsageDAO;
import cn.myapps.rm.usage.dao.MsSqlResourceUsageDAO;
import cn.myapps.rm.usage.dao.MySqlResourceUsageDAO;
import cn.myapps.rm.usage.dao.OracleResourceUsageDAO;
import cn.myapps.rm.util.IDataSource;

/**
 * @author Happy
 *
 */
public class DaoManager {
	

	public static RuntimeDAO getResourceDAO(Connection conn) throws Exception{
		String dbType = IDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlResourceDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleResourceDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2ResourceDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlResourceDAO(conn);
		}
		
		return null;
	}
	
	public static RuntimeDAO getResourceUsageDAO(Connection conn) throws Exception{
		String dbType = IDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlResourceUsageDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleResourceUsageDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2ResourceUsageDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlResourceUsageDAO(conn);
		}
		
		return null;
	}
	
	public static RuntimeDAO getRoleDAO(Connection conn) throws Exception{
		String dbType = IDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlRoleDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleRoleDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2RoleDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlRoleDAO(conn);
		}
		
		return null;
	}
	
	public static RuntimeDAO getUserRoleSetDAO(Connection conn) throws Exception{
		String dbType = IDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlUserRoleSetDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleUserRoleSetDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2UserRoleSetDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlUserRoleSetDAO(conn);
		}
		
		return null;
	}
	
	
}
