/**
 * 
 */
package net.gdsc.util;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import cn.myapps.util.property.PropertyUtil;

/**
 * 2014-12-9
 * @author zhangmz
 */
public class OADataSource {
	
	private String dbType;
	
	private String userName;
	
	private String password;
	
	private String poolSize;
	
	private String timeout;
	
	private String driverClass;
	
	private String url;
	
	private static DataSource _dataSource;
	public static String _dbTye;

	/**
	 * @return 数据源类型(MySQL/Oracle/MSSQL)
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * @param dbType
	 * @param userName
	 * @param password
	 * @param poolSize
	 * @param timeout
	 * @param driverClass
	 * @param url
	 */
	public OADataSource(String dbType, String userName, String password,
			String poolSize, String timeout, String driverClass, String url) {
		super();
		this.dbType = dbType;
		this.userName = userName;
		this.password = password;
		this.poolSize = poolSize;
		this.timeout = timeout;
		this.driverClass = driverClass;
		this.url = url;
		_dbTye = dbType;
	}
	
	public static Connection getConnection() throws Exception{
		if(_dataSource==null){
			try {
				reloadDataSource();
			} catch (Exception e) {
				throw e;
			}
		}else{
			return _dataSource.getConnection();
		}
		return null;
	}
	
	public DataSource getDataSource() throws Exception {
		DataSource dataSource = getDBCPDataSource(userName,
				password, driverClass, url, poolSize, timeout);

		return dataSource;
	}
	
	public static synchronized void reloadDataSource() throws Exception{
		PropertyUtil.reload("oa");
		String userName = PropertyUtil.get(OAConfig.USER);
		String password = PropertyUtil.get(OAConfig.PASSWORD);
		String poolSize = PropertyUtil.get(OAConfig.POOL_SIZE);
		String timeout = PropertyUtil.get(OAConfig.TIME_OUT);
		String driverClass = PropertyUtil.get(OAConfig.DERVER_CLASS);
		String url = PropertyUtil.get(OAConfig.DERVER_URL);
		String dbType = PropertyUtil.get(OAConfig.DB_TYPE);
		try {
			Class.forName(driverClass).newInstance();
			Connection conn = DriverManager.getConnection(url, userName,password);
			conn.close();
			_dataSource = new OADataSource(dbType,userName,password,poolSize,timeout,driverClass,url).getDataSource();
		} catch (Exception e) {
			_dataSource = null;
			throw new Exception("数据库连接错误！ \n" + e.getMessage());
		} 
	}
	
	public DataSource getDBCPDataSource(String username, String password, String driver, String url,
			String poolSize, String timeout) {
		BasicDataSource ds = new BasicDataSource();
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setMaxIdle(5);
		ds.setMaxActive(Integer.parseInt(poolSize));
		ds.setMaxWait(Integer.parseInt(timeout));
		ds.setDefaultAutoCommit(true);
		return ds;
	}
	
	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
	       //创建C3P0连接池
	      /* DataSource dataSource = new ComboPooledDataSource();
	       ConnectionPool pool = new ConnectionPool();*/
		/*ConnectionPool pool = ConnectionPool.getInstance();
	       try {
		       for(int i=1;i<=100;i++){
		            Connection conn;
					conn = pool.getConnection();
					if(conn!=null){
		              System.out.println(i+":取得连接");
		              conn.close();
		            }
		       }
	       } catch (SQLException e) {
				e.printStackTrace();
			}
	       long end = System.currentTimeMillis();
	       System.out.println("共用" + (end-begin)/1000+"秒");*/
	}
	
	

}
