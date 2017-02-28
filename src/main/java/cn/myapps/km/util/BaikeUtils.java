package cn.myapps.km.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.myapps.util.property.PropertyUtil;

public class BaikeUtils {
	
	public static final ThreadLocal<Connection> runtimeDBConn = new ThreadLocal<Connection>();
	
	public static void closeStatement(Statement stmt) throws Exception {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException se) {
			throw new Exception("SQL Exception while closing " + "Statement : \n" + se);
		}
	}
	
	public static void closeConnection() throws Exception {
		try {
			Connection threadLocal = BaikeUtils.runtimeDBConn.get();
			if (threadLocal != null) {
					if (!threadLocal.isClosed()) {
						threadLocal.close();
					}
				threadLocal = null;
				
				BaikeUtils.runtimeDBConn.set(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private static Map<String, Properties> settings = new HashMap<String, Properties>();
	public static String getSysDataSourceName() throws Exception{	
		String dataSourceName = null;
		if (!(settings.keySet().contains("proxool"))){
			PropertyUtil.reload("proxool");
		}
		String driverClass = PropertyUtil.get("jdbc-0.proxool.driver-class");
		String url = PropertyUtil.get("jdbc-0.proxool.driver-url");
		//当为SqlServer数据库时
		if(driverClass.equals("net.sourceforge.jtds.jdbc.Driver")){
			int	firstIndex = url.lastIndexOf("/");
			int	endIndex = url.lastIndexOf("?")<=0 ? url.length() : url.lastIndexOf("?");
			dataSourceName = url.substring(firstIndex+1, endIndex);	
			//当为DB2数据库时
		}else if(driverClass.equals("com.ibm.db2.jcc.DB2Driver")){
			int firstIndex = url.lastIndexOf("/");
			int	endIndex = url.lastIndexOf("?")<=0 ? url.length() : url.lastIndexOf("?");
			dataSourceName = url.substring(firstIndex+1, endIndex);	
			//当为Oracle数据库时
		}else if(driverClass.equals("oracle.jdbc.driver.OracleDriver")){
			int firstIndex = url.lastIndexOf(":");
			int	endIndex = url.lastIndexOf("?")<=0 ? url.length() : url.lastIndexOf("?");
			dataSourceName = url.substring(firstIndex+1, endIndex);
			//当为mysql数据库时
		}else if(driverClass.equals("com.mysql.jdbc.Driver")){
			int firstIndex = url.lastIndexOf("/");
			int	endIndex = url.lastIndexOf("?")<=0 ? url.length() : url.lastIndexOf("?");
			dataSourceName = url.substring(firstIndex+1,endIndex);
		}
		return dataSourceName;
	}
	
	public static String getKmDataSourceName() throws Exception{	
		String dataSourceName = null;
		if (!(settings.keySet().contains("km"))){
			PropertyUtil.reload("km");
		}
		String driverClass = PropertyUtil.get("km-datasource-driver-class");
		String url = PropertyUtil.get("km-datasource-driver-url");
		//当为SqlServer数据库时
		if(driverClass.equals("net.sourceforge.jtds.jdbc.Driver")){
			int	firstIndex = url.lastIndexOf("/");
			int	endIndex = url.lastIndexOf("?")<=0 ? url.length() : url.lastIndexOf("?");
			dataSourceName = url.substring(firstIndex+1, endIndex);	
			//当为mysql数据库时
		} else if(driverClass.equals("com.mysql.jdbc.Driver")){
			int	firstIndex = url.lastIndexOf("/");
			int	endIndex = url.lastIndexOf("?")<=0 ? url.length() : url.lastIndexOf("?");
			dataSourceName = url.substring(firstIndex+1, endIndex);	
			//当为DB2数据库时
		}else if(driverClass.equals("com.ibm.db2.jcc.DB2Driver")){
			int	firstIndex = url.lastIndexOf("/");
			dataSourceName = url.substring(firstIndex+1);	
		}
		return dataSourceName;
	}
	
	public static void mian(String[] args) {
		try {
			BaikeUtils.getSysDataSourceName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
