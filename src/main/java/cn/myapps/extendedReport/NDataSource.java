package cn.myapps.extendedReport;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import cn.myapps.util.property.PropertyUtil;

public class NDataSource {
	
	private String dbType;

	private String userName;

	private String password;

	private String poolSize;

	private String timeout;

	private String driverClass;

	private String url;
	

	public static boolean isErpEnable(){
		return PropertyUtil.get("erp-enable").equals("true");
	}

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
		DataSource dataSource = getDBCPDataSource(userName,
				password, driverClass, url, poolSize, timeout);

		return dataSource;
	}
	
	public static synchronized void reLoadDataSource() throws Exception{
		String DERVER_CLASS = "erp-datasource-driver-class";
		String DERVER_URL = "erp-datasource-driver-url";
		String USER = "erp-datasource-user";
		String PASSWORD = "erp-datasource-password";
		String DB_TYPE = "erp-datasource-dbType";
		String POOL_SIZE = "erp-datasource-poolsize";
		String TIME_OUT = "erp-datasource-timeout";
		
		String userName = PropertyUtil.get(USER);
		String password = PropertyUtil.get(PASSWORD);
		String poolSize = PropertyUtil.get(POOL_SIZE);
		String timeout = PropertyUtil.get(TIME_OUT);
		String driverClass = PropertyUtil.get(DERVER_CLASS);
		String url = PropertyUtil.get(DERVER_URL);
		String dbType = PropertyUtil.get(DB_TYPE);
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
}
