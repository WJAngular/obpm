package cn.myapps.km.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.util.property.PropertyUtil;

public class NDataSource {
	
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

	private NDataSource(String userName, String password, String poolSize,
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
		DataSource dataSource = getDruidDataSource(userName,
				password, driverClass, url, poolSize, timeout);

		return dataSource;
	}
	
	public static synchronized void reLoadDataSource() throws Exception{
		PropertyUtil.reload("km");
		if(!"true".equals(PropertyUtil.get("km-enable"))) return;
		String userName = PropertyUtil.get(KmConfig.USER);
		String password = PropertyUtil.get(KmConfig.PASSWORD);
		String poolSize = PropertyUtil.get(KmConfig.POOL_SIZE);
		String timeout = PropertyUtil.get(KmConfig.TIME_OUT);
		String driverClass = PropertyUtil.get(KmConfig.DERVER_CLASS);
		String url = PropertyUtil.get(KmConfig.DERVER_URL);
		String dbType = PropertyUtil.get(KmConfig.DB_TYPE);
		try {
			Class.forName(driverClass).newInstance();
			Connection conn = DriverManager.getConnection(url, userName,password);
			conn.close();
			_dataSource = new NDataSource(userName, password, poolSize,
					timeout, driverClass, url,dbType).getDataSource();
		} catch (Exception e) {
			_dataSource = null;
			throw new Exception("{*[cn.myapps.core.deploy.application.connect_error]*}! \n" + e.getMessage());
		}
		
	}
	
	public static DataSource getDruidDataSource(String username, String password, String driver, String url,
			String poolSize, String timeout) {
		Map<String,String> properties =  new HashMap<String, String>();
		properties.put("driverClassName", driver);
		properties.put("url", url);
		properties.put("username", username);
		properties.put("password", password);
		properties.put("maxActive", poolSize);
		properties.put("maxWait", timeout);
		properties.put("filters", "stat");
		properties.put("initialSize", "10");
		properties.put("timeBetweenEvictionRunsMillis", "60000");
		properties.put("minEvictableIdleTimeMillis", "300000");
		properties.put("testWhileIdle", "true");
		properties.put("testOnBorrow", "false");
		properties.put("testOnReturn", "false");
		if(driver.toLowerCase().indexOf("oracle")>=0){
			properties.put("validationQuery", "SELECT 'x' FROM DUAL ");
			properties.put("poolPreparedStatements", "true");
		}else if(driver.toLowerCase().indexOf("db2")>=0){
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
		
		try {
			return DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
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
	
//	public static Connection getConnection() throws Exception {
//		if (_dataSource == null) {
//			String userName = "root";
//			String password = "";
//			String poolSize = "10";
//			String timeout = "5000";
//			String driverClass = "com.mysql.jdbc.Driver";
//			String url = "jdbc:mysql://localhost:3306/cpkm";
//			_dataSource = new NDataSource(userName, password, poolSize,
//					timeout, driverClass, url).getDataSource();
//		}
//
//		return _dataSource.getConnection();
//	}

}
