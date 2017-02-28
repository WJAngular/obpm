package cn.myapps.km.base.dao;

import java.sql.Connection;

import cn.myapps.km.category.dao.DB2CategoryDAO;
import cn.myapps.km.category.dao.MsSqlCategoryDAO;
import cn.myapps.km.category.dao.MySqlCategoryDAO;
import cn.myapps.km.category.dao.OracleCategoryDAO;
import cn.myapps.km.comments.dao.DB2CommentsDAO;
import cn.myapps.km.comments.dao.MsSqlCommentsDAO;
import cn.myapps.km.comments.dao.MySqlCommentsDAO;
import cn.myapps.km.comments.dao.OracleCommentsDAO;
import cn.myapps.km.disk.dao.DB2NDirDAO;
import cn.myapps.km.disk.dao.DB2NDiskDAO;
import cn.myapps.km.disk.dao.DB2NFileDAO;
import cn.myapps.km.disk.dao.MsSqlNDirDAO;
import cn.myapps.km.disk.dao.MsSqlNDiskDAO;
import cn.myapps.km.disk.dao.MsSqlNFileDAO;
import cn.myapps.km.disk.dao.MySqlNDirDAO;
import cn.myapps.km.disk.dao.MySqlNDiskDAO;
import cn.myapps.km.disk.dao.MySqlNFileDAO;
import cn.myapps.km.disk.dao.OracleNDirDAO;
import cn.myapps.km.disk.dao.OracleNDiskDAO;
import cn.myapps.km.disk.dao.OracleNFileDAO;
import cn.myapps.km.log.dao.DB2LogsDAO;
import cn.myapps.km.log.dao.MsSqlLogsDAO;
import cn.myapps.km.log.dao.MySqlLogsDAO;
import cn.myapps.km.log.dao.OracleLogsDAO;
import cn.myapps.km.org.dao.DB2NRoleDAO;
import cn.myapps.km.org.dao.DB2NUserRoleSetDAO;
import cn.myapps.km.org.dao.MsSqlNRoleDAO;
import cn.myapps.km.org.dao.MsSqlNUserRoleSetDAO;
import cn.myapps.km.org.dao.MySqlNRoleDAO;
import cn.myapps.km.org.dao.MySqlNUserRoleSetDAO;
import cn.myapps.km.org.dao.OracleNRoleDAO;
import cn.myapps.km.org.dao.OracleNUserRoleSetDAO;
import cn.myapps.km.permission.dao.DB2DirAccessDAO;
import cn.myapps.km.permission.dao.DB2FileAccessDAO;
import cn.myapps.km.permission.dao.DB2ManagePermissionDAO;
import cn.myapps.km.permission.dao.DB2PermissionDAO;
import cn.myapps.km.permission.dao.MsSqlDirAccessDAO;
import cn.myapps.km.permission.dao.MsSqlFileAccessDAO;
import cn.myapps.km.permission.dao.MsSqlManagePermissionDAO;
import cn.myapps.km.permission.dao.MsSqlPermissionDAO;
import cn.myapps.km.permission.dao.MySqlDirAccessDAO;
import cn.myapps.km.permission.dao.MySqlFileAccessDAO;
import cn.myapps.km.permission.dao.MySqlManagePermissionDAO;
import cn.myapps.km.permission.dao.MySqlPermissionDAO;
import cn.myapps.km.permission.dao.OracleDirAccessDAO;
import cn.myapps.km.permission.dao.OracleFileAccessDAO;
import cn.myapps.km.permission.dao.OracleManagePermissionDAO;
import cn.myapps.km.permission.dao.OraclePermissionDAO;
import cn.myapps.km.report.dao.DB2NDashBoardDAO;
import cn.myapps.km.report.dao.MsSqlNDashBoardDAO;
import cn.myapps.km.report.dao.MySqlNDashBoardDAO;
import cn.myapps.km.report.dao.OracleNDashBoardDAO;
import cn.myapps.km.util.NDataSource;

/**
 * @author xiuwei
 *
 */
public class DaoManager {
	

	public static NRuntimeDAO getNFileDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlNFileDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleNFileDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2NFileDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlNFileDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getNDiskDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlNDiskDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleNDiskDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2NDiskDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlNDiskDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getNDirDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlNDirDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleNDirDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2NDirDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlNDirDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getNRoleDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlNRoleDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleNRoleDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2NRoleDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlNRoleDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getNUserRoleSetDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlNUserRoleSetDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleNUserRoleSetDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2NUserRoleSetDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlNUserRoleSetDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getDirAccessDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlDirAccessDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleDirAccessDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2DirAccessDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlDirAccessDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getFileAccessDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlFileAccessDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleFileAccessDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2FileAccessDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlFileAccessDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getPermissionDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlPermissionDAO(conn); 
		}else if("ORACLE".equals(dbType)){
			return new OraclePermissionDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2PermissionDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlPermissionDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getManagePermissionDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlManagePermissionDAO(conn); 
		}else if("ORACLE".equals(dbType)){
			return new OracleManagePermissionDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2ManagePermissionDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlManagePermissionDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getCommentsDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlCommentsDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleCommentsDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2CommentsDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlCommentsDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getLogsDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlLogsDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleLogsDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2LogsDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlLogsDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getCategoryDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlCategoryDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleCategoryDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2CategoryDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlCategoryDAO(conn);
		}
		
		return null;
	}
	
	public static NRuntimeDAO getNDashBoardDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MsSqlNDashBoardDAO(conn); 
		}else if("ORACLE".equals(dbType)){
			return new OracleNDashBoardDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2NDashBoardDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlNDashBoardDAO(conn);
		}
		
		return null;
	}
	
}
