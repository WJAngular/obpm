/**
 * 
 */
package net.gdsc.util;

import java.io.Serializable;

import cn.myapps.util.property.PropertyUtil;

/**
 * OA配置信息
 * @author zhangmz
 */
public class OAConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否启用
	 */
	public static final String ENABLE = "oa-enable";
	public static final String DERVER_CLASS = "oa-datasource-driver-class";
	public static final String DERVER_URL = "oa-datasource-driver-url";
	public static final String USER = "oa-datasource-user";
	public static final String PASSWORD = "oa-datasource-password";
	public static final String DB_TYPE = "oa-datasource-dbType";
	public static final String POOL_SIZE = "oa-datasource-poolsize";
	public static final String TIME_OUT = "oa-datasource-timeout";
	
	private boolean enable;
	
	private String driverClass;

	private String url;

	private String username;

	private String password;
	private String dbType;

	private String poolsize;

	private String timeout;

	public static boolean isoaEnable(){
		return PropertyUtil.get(OAConfig.ENABLE).equals("true");
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public boolean getEnable(){
		return enable;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getPoolsize() {
		return poolsize;
	}

	public void setPoolsize(String poolsize) {
		this.poolsize = poolsize;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
	
	
	
	
}
