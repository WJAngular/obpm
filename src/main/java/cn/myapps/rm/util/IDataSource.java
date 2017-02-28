package cn.myapps.rm.util;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import cn.myapps.util.property.PropertyUtil;

public class IDataSource {
	
	private String dbType;

	private String userName;

	private String password;

	private String poolSize;

	private String timeout;

	private String driverClass;

	private String url;
	
	

	public String getDbType() {
		return dbType;
	}

	private IDataSource(String userName, String password, String poolSize,
			String timeout, String driverClass, String url,String dbType) {
		this.userName = userName;
		this.password = password;
		this.poolSize = poolSize;
		this.timeout = timeout;
		this.driverClass = driverClass;
		this.url = url;
		this.dbType = dbType;
		_dbTye = dbType;
	}

	public DataSource getDataSource() throws Exception {

		// dataSource = PersistenceUtils.getC3P0DataSource(username,
		// password, driver, url, poolsize, timeout);
		DataSource dataSource = getDBCPDataSource(userName,
				password, driverClass, url, poolSize, timeout);

		return dataSource;
	}
	
	public static synchronized void reLoadDataSource() throws Exception{
		PropertyUtil.reload("proxool");
		String userName = PropertyUtil.get("jdbc-0.user");
		String password = PropertyUtil.get("jdbc-0.password");
		String poolSize = PropertyUtil.get("jdbc-0.proxool.maximum-connection-count");
		String timeout = PropertyUtil.get("jdbc-0.proxool.maximum-connection-lifetime");
		String driverClass = PropertyUtil.get("jdbc-0.proxool.driver-class");
		String url = PropertyUtil.get("jdbc-0.proxool.driver-url");
		String dbType = "MYSQL";
		
		if(driverClass.indexOf("oracle")>=0){
			 dbType = "Oracle";
		}else if(driverClass.indexOf("db2")>=0){
			dbType = "DB2";
		}else if(driverClass.indexOf("sqlserver")>=0){
			dbType = "SQLServer";
		}
		
		try {
			Class.forName(driverClass).newInstance();
			Connection conn = DriverManager.getConnection(url, userName,password);
			conn.close();
			_dataSource = new IDataSource(userName, password, poolSize,
					timeout, driverClass, url,dbType).getDataSource();
		} catch (Exception e) {
			_dataSource = null;
			throw new Exception("{*[cn.myapps.core.deploy.application.connect_error]*}! \n" + e.getMessage());
		}
		
	}
	
	public DataSource getDBCPDataSource(String username, String password, String driver, String url,
			String poolSize, String timeout) {

		BasicDataSource ds = new BasicDataSource();

		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		// ds.setPoolPreparedStatements(true);
		// ds.setMaxOpenPreparedStatements(10);
		// ds.setInitialSize(10);
		ds.setMaxIdle(5);
		ds.setMaxActive(Integer.parseInt(poolSize));

		ds.setMaxWait(Integer.parseInt(timeout));
		ds.setDefaultAutoCommit(true);

		return ds;
	}

	private static DataSource _dataSource;
	public static String _dbTye;
	
	/*public static Connection getConnection() throws Exception {
		return PersistenceUtils.getDBConnection();
	}*/

	public static Connection getConnection() throws Exception {
		if (_dataSource == null) {
			try {
				reLoadDataSource();
			} catch (Exception e) {
				throw e;
			}
			
		}
		if(_dataSource != null){
			return _dataSource.getConnection();
		}
		return null;
	}
	

}
