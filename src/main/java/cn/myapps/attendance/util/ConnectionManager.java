package cn.myapps.attendance.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.property.PropertyUtil;


/**
 * 数据库连接管理器
 * <p>管理Connection 对象的创建、缓存、关闭</p>
 * 
 * @author Happy
 *
 */
public class ConnectionManager {
	
	private final static Logger log = Logger.getLogger(ConnectionManager.class);

	public static final ThreadLocal<Connection> runtimeDBConn = new ThreadLocal<Connection>();
	
	private static DataSource ds = null;
	
	public static String dbType = "MYSQL";
	
	/**
	 * 获取数据库连接对象
	 * @return
	 * 		java.sql.Connection
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		Connection threadLocal = runtimeDBConn.get();
		Connection conn = null;
		if (threadLocal != null) {
			conn = threadLocal;
		}
		if (conn == null || conn.isClosed()) {
			conn = newConnection();
			if(conn !=null)
				runtimeDBConn.set(conn);
		}
		
		return conn;
	}
	
	/**
	 * 实例化数据库连接对象
	 * <p>读取“proxool.properties”文件获取obpm系统库的数据源信息，创建数据库连接对象。</p>
	 * @return
	 * 		java.sql.Connection
	 * @throws Exception
	 */
	private static Connection newConnection() throws Exception {
		if (ds == null) {
			try {
				synchronized (ConnectionManager.class) {
					PropertyUtil.reload("proxool");
					String userName = PropertyUtil.get("jdbc-0.user");
					String password = PropertyUtil.get("jdbc-0.password");
					String poolSize = PropertyUtil.get("jdbc-0.proxool.maximum-connection-count");
					String timeout = PropertyUtil.get("jdbc-0.proxool.maximum-connection-lifetime");
					String driverClass = PropertyUtil.get("jdbc-0.proxool.driver-class");
					String url = PropertyUtil.get("jdbc-0.proxool.driver-url");
					if(url.toLowerCase().indexOf("oracle")>=0){
						 dbType = "ORACLE";
					}else if(url.toLowerCase().indexOf("db2")>=0){
						dbType = "DB2";
					}else if(url.toLowerCase().indexOf("sqlserver")>=0){
						dbType = "MSSQL";
					}
					Map<String,String> properties =  new HashMap<String, String>();
					properties.put("name", "微信考勤");
					properties.put("driverClassName", driverClass);
					properties.put("url", url);
					properties.put("username", userName);
					properties.put("password", password);
					properties.put("maxActive", poolSize);
					properties.put("maxWait", timeout);
					properties.put("initialSize", "2");
					properties.put("timeBetweenEvictionRunsMillis", "60000");
					properties.put("minEvictableIdleTimeMillis", "300000");
					properties.put("testWhileIdle", "true");
					properties.put("testOnBorrow", "false");
					properties.put("testOnReturn", "false");
					if(driverClass.toLowerCase().indexOf("oracle")>=0){
						properties.put("validationQuery", "SELECT 'x' FROM DUAL ");
						properties.put("poolPreparedStatements", "true");
					}else if(driverClass.toLowerCase().indexOf("db2")>=0){
						properties.put("validationQuery", "SELECT 'x'  FROM SYSDUMMY ");
						properties.put("poolPreparedStatements", "false");
					}else{
						properties.put("validationQuery", "SELECT 'x' ");
						properties.put("poolPreparedStatements", "false");
					}
					properties.put("maxPoolPreparedStatementPerConnectionSize", "200");
					//https://github.com/alibaba/druid/wiki/%E8%BF%9E%E6%8E%A5%E6%B3%84%E6%BC%8F%E7%9B%91%E6%B5%8B
					properties.put("removeAbandoned", "true");
					properties.put("removeAbandonedTimeout", "1800");
					if("true".equals(DefaultProperty.getProperty("debug"))){
						properties.put("filters", "stat");
					}
					ds = DruidDataSourceFactory.createDataSource(properties);
				}
			} catch (Exception e) {
				log.error("读取proxool.propertise文件失败", e);
				throw e;
			}
			
		}
		if(ds != null){
			return ds.getConnection();
		}
		return null;
	}
	
	/**
	 * 关闭Statement
	 * @param stmt
	 * @throws Exception
	 */
	public static void closeStatement(Statement stmt) throws Exception {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException se) {
			throw new Exception("SQL Exception while closing " + "Statement : \n" + se);
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @throws Exception
	 */
	public static void closeConnection() throws Exception {
		try {
			Connection threadLocal = runtimeDBConn.get();
			if (threadLocal != null) {
					if (!threadLocal.isClosed()) {
						threadLocal.close();
					}
				threadLocal = null;
				
				runtimeDBConn.set(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
