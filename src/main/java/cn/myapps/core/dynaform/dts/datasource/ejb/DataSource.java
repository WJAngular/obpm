package cn.myapps.core.dynaform.dts.datasource.ejb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.StringUtil;

/**
 * @hibernate.class table="T_DATASOURCE" lazy="false"
 * @author nicholas
 */
public class DataSource extends ValueObject {
	
	private static final long serialVersionUID = 7641787810321822255L;

	private String id;

	private String name;
	
	/**
	 * 数据源使用类型 jdbc/jndi
	 */
	private String useType = TYPE_JDBC;

	private String driverClass;

	private String url;

	private String username;

	private String password;

	private transient Connection connection;

	private int dbType;

	private String poolsize;

	private String timeout;
	
	/**
	 * jndi名称
	 */
	private String jndiName;
	
	/**
	 * jndi服务提供者URL
	 */
	private String providerUrl;
	
	/**
	 * 指定到jndi服务的上下文工厂类
	 */
	private String initialContextFactory;
	
	/**
	 * 用于查找与jndi名称中含有的schema类似的上下工厂类(在此参数设定的包名下)
	 */
	private String urlPkgPrefixes;
	
	/**
	 * 通过jndi访问指定resource的凭证名称
	 */
	private String securityPrincipal;
	
	/**
	 * 通过jndi访问指定resource时与凭证相对应的密码
	 */
	private String securityCredentials;

	public static final int DB_ORACLE = 1;

	public static final int DB_SQLSERVER = 2;

	public static final int DB_DB2 = 3;

	public static final int DB_MYSQL = 4;

	public static final int DB_HSQL = 5;
	
	public static final String TYPE_JDBC = "JDBC";
	
	public static final String TYPE_JNDI = "JNDI";

	public static Map<Integer, String> dbType2NameMap = new HashMap<Integer, String>();

	static {
		dbType2NameMap.put(DB_ORACLE, DbTypeUtil.DBTYPE_ORACLE);
		dbType2NameMap.put(DB_SQLSERVER, DbTypeUtil.DBTYPE_MSSQL);
		dbType2NameMap.put(DB_DB2, DbTypeUtil.DBTYPE_DB2);
		dbType2NameMap.put(DB_MYSQL, DbTypeUtil.DBTYPE_MYSQL);
		dbType2NameMap.put(DB_HSQL, DbTypeUtil.DBTYPE_HSQLDB);
	}

	public static Integer getDbTypeByName(String typeName) {
		for (Iterator<Entry<Integer, String>> iterator = dbType2NameMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<Integer, String> entry = iterator.next();
			if ((entry.getValue()).equalsIgnoreCase(typeName)) {
				return entry.getKey();
			}
		}
		return 0;
	}

	/**
	 * @hibernate.property column="DBTYPE"
	 * @return
	 */

	public int getDbType() {
		return dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 * @uml.property name="dbType"
	 */
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	/**
	 * @hibernate.property column="DRIVERCLASS"
	 * @return
	 * @uml.property name="driverClass"
	 */
	public String getDriverClass() {
		return driverClass;
	}

	/**
	 * @param driverClass
	 *            the driverClass to set
	 * @uml.property name="driverClass"
	 */
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	/**
	 * @hibernate.property column="NAME"
	 * @return
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getUseType() {
		if(useType==null){
			useType = TYPE_JDBC;
		}
		return useType;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}

	/**
	 * @hibernate.property column="URL"
	 * @return
	 * @uml.property name="url"
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 * @uml.property name="url"
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @hibernate.id column="ID" generator-class="assigned"
	 * @uml.property name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 * @uml.property name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @hibernate.property column="PASSWORD"
	 * @return
	 * @uml.property name="password"
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 * @uml.property name="password"
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @hibernate.property column="USERNAME"
	 * @return
	 * @uml.property name="username"
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 * @uml.property name="username"
	 */
	public void setUsername(String username) {
		this.username = username;
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
	
	

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	public String getInitialContextFactory() {
		return initialContextFactory;
	}

	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	public String getUrlPkgPrefixes() {
		return urlPkgPrefixes;
	}

	public void setUrlPkgPrefixes(String urlPkgPrefixes) {
		this.urlPkgPrefixes = urlPkgPrefixes;
	}

	public String getSecurityPrincipal() {
		return securityPrincipal;
	}

	public void setSecurityPrincipal(String securityPrincipal) {
		this.securityPrincipal = securityPrincipal;
	}

	public String getSecurityCredentials() {
		return securityCredentials;
	}

	public void setSecurityCredentials(String securityCredentials) {
		this.securityCredentials = securityCredentials;
	}

	public String getDbTypeName() {
		String typeName = (String) dbType2NameMap.get(getDbType());
		return typeName;
	}

	/**
	 * @return the connection
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 * @uml.property name="connection"
	 */
	public Connection getConnection() throws NamingException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (connection == null || connection.isClosed()) {
			if (DataSource.TYPE_JNDI.equals(this.getUseType())) {
				connection = this.getJNDIConnection();
			}
			else {
				Class.forName(getDriverClass()).newInstance();
				connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
			} 
		}
		return connection;
	}
	
	public Connection getJNDIConnection() throws NamingException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Context context = null;
		if (StringUtil.isBlank(this.initialContextFactory) && StringUtil.isBlank(this.urlPkgPrefixes) 
				&& StringUtil.isBlank(this.providerUrl) && StringUtil.isBlank(this.securityPrincipal) && StringUtil.isBlank(this.securityCredentials)) {
				context = new InitialContext();
		} else {
			Hashtable<String, String> env = new Hashtable<String, String>();
			if (this.initialContextFactory!=null && this.initialContextFactory.trim().length()>0) {
				env.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
			}
			if (this.urlPkgPrefixes!=null && this.urlPkgPrefixes.trim().length()>0) {
				env.put(Context.URL_PKG_PREFIXES, this.urlPkgPrefixes);
			}
			if (this.providerUrl!=null && this.providerUrl.trim().length()>0) {
				env.put(Context.PROVIDER_URL, this.providerUrl);
			}
			if (this.securityPrincipal!=null && this.securityPrincipal.trim().length()>0) {
				env.put(Context.SECURITY_PRINCIPAL, this.securityPrincipal);
			}
			if (this.securityCredentials!=null && this.securityCredentials.trim().length()>0) {
				env.put(Context.SECURITY_CREDENTIALS, this.securityCredentials);
			}
			context = new InitialContext(env);
		}
		javax.sql.DataSource ds = (javax.sql.DataSource) context.lookup(jndiName);
		return ds.getConnection();
	}
	

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("UseType: '" + getUseType() + "', ");
		buffer.append("DriverClass: '" + getDriverClass() + "', ");
		buffer.append("DBType: '" + dbType2NameMap.get(getDbType()) + "', ");
		buffer.append("DBurl: '" + getUrl() + "',");
		buffer.append("UserName: '" + getUsername() + "', ");
		buffer.append("Password: '" + getPassword() + "', ");
		buffer.append("Poolsize: '" + getPoolsize() + "', ");
		buffer.append("JNDIName: '" + getJndiName() + "', ");
		buffer.append("InitialContextFactory: '" + getInitialContextFactory() + "', ");
		buffer.append("UrlPkgPrefixes: '" + getUrlPkgPrefixes() + "', ");
		buffer.append("ProviderUrl: '" + getProviderUrl() + "', ");
		buffer.append("SecurityPrincipal: '" + getSecurityPrincipal() + "', ");
		buffer.append("SecurityCredentials: '" + getSecurityCredentials() + "'");
		buffer.append("}");

		return buffer.toString();
	}
}
