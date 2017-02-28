package cn.myapps.base.ejb;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jamonapi.proxy.MonProxyFactory;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.pending.dao.AbstractPendingDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

/**
 * The base abstract run time process bean.
 */
public abstract class AbstractRunTimeProcessBean<E> implements IRunTimeProcess<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7120284978893104541L;

	/**
	 * The application id
	 */
	private String applicationId;

	/**
	 * The data sources.
	 */
	private static HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();

	/**
	 * 单态时存在问题
	 */
	public static final ThreadLocal<Integer> transactionSignal = new ThreadLocal<Integer>();

	/**
	 * The constructor with application id.
	 * 
	 * @param applicationId
	 */
	public AbstractRunTimeProcessBean(String applicationId) {
		this.applicationId = applicationId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doView(java.lang.String)
	 */
	public ValueObject doView(String id) throws Exception {
		return getDAO().find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doCreate(cn.myapps.base.dao.ValueObject)
	 */
	public void doCreate(ValueObject vo) throws Exception {
		try {
			if (vo.getId() == null || vo.getId().trim().length() == 0) {
				vo.setId(Sequence.getSequence());
			}

			if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
				vo.setSortId(Sequence.getTimeSequence());
			}

			beginTransaction();
			getDAO().create(vo);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doRemove(java.lang.String)
	 */
	public void doRemove(String pk) throws Exception {
		try {
			beginTransaction();
			getDAO().remove(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	
	public void doRemoveByDocId(String pk) throws Exception {
		try {
			beginTransaction();
			((AbstractPendingDAO)getDAO()).removeByDocId(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doUpdate(cn.myapps.base.dao.ValueObject)
	 */
	public void doUpdate(ValueObject vo) throws Exception {
		try {
			beginTransaction();
			if (vo.getApplicationid()==null) {
				vo.setApplicationid(getApplicationId());
			}
			getDAO().update(vo);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doQuery(cn.myapps.base.action.ParamsTable,
	 *      cn.myapps.core.user.action.WebUser)
	 */
	public DataPackage<E> doQuery(ParamsTable params, WebUser user) throws Exception {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#beginTransaction()
	 */
	public void beginTransaction() throws Exception {

		int signal = getTransactionSignal();

		if (signal == 0){
			Connection conn = getConnection();
			if (conn.getAutoCommit()) {
				conn.setAutoCommit(false);
			}
//System.out.println("数据库驱动类："+conn.getMetaData().getDriverName());
//System.out.println("数据库产品名称："+conn.getMetaData().getDatabaseProductName());
			if(!conn.getMetaData().getDriverName().equals("Oracle JDBC driver")){
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}
		}

		signal++;

		setTransactionSignal(signal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#commitTransaction()
	 */
	public void commitTransaction() throws Exception {
		int signal = getTransactionSignal();
		signal--;

		if (signal == 0) {
			Connection conn = getConnection();
			try {
				conn.commit();
			} catch (Exception e) {
				setTransactionSignal(signal);
				return ;
			}
			conn.setAutoCommit(true);
			if(!conn.getMetaData().getDriverName().equals("Oracle JDBC driver")){
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			}
		}

		setTransactionSignal(signal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#rollbackTransaction()
	 */
	public void rollbackTransaction() throws Exception {
		int signal = getTransactionSignal();
		signal--;

		if (signal == 0) {
			Connection conn = getConnection();
			try {
				conn.rollback();
			} catch (Exception e){
				setTransactionSignal(signal);
				return ;
			}	
			conn.setAutoCommit(true);
			if(!conn.getMetaData().getDriverName().equals("Oracle JDBC driver")){
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			}
		}

		setTransactionSignal(signal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#closeConnection()
	 */
	public void closeConnection() throws Exception {
		if (getConnection() != null) {
			if (!getConnection().isClosed())
				getConnection().close();

			PersistenceUtils.runtimeDBConn.set(null);
		}
	}

	/**
	 * Get the relate Dao
	 * 
	 * @return The relate Dao.
	 * @throws Exception
	 */
	protected abstract IRuntimeDAO getDAO() throws Exception;

	/**
	 * Get the application id.
	 * 
	 * @return
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	protected void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * The data source.
	 * 
	 * @return The data source.
	 * @throws Exception
	 */
	protected DataSource getDataSource() throws Exception {

		DataSource dataSource = (DataSource) ((getApplicationId() != null) ? dataSources.get(getApplicationId()) : null);

		if (dataSource == null) {
			ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
			ApplicationVO appvo = (ApplicationVO) process.doView(getApplicationId());
			
			cn.myapps.core.dynaform.dts.datasource.ejb.DataSource ds = appvo.getDataSourceDefine();
			
			if (ds != null) {
				if (cn.myapps.core.dynaform.dts.datasource.ejb.DataSource.TYPE_JNDI.equals(ds.getUseType())) {
					String jndiName = ds.getJndiName();
					String contextFactory = ds.getInitialContextFactory();
					String urlPkg = ds.getUrlPkgPrefixes();
					String providerUrl = ds.getProviderUrl();
					String securityPrincipal = ds.getSecurityPrincipal();
					String securityCredentials = ds.getSecurityCredentials();
					dataSource = PersistenceUtils.getJNDIDataSource(jndiName, contextFactory, urlPkg, providerUrl, securityPrincipal, securityCredentials);
				}
				else {
					String username = ds.getUsername();
					String password = ds.getPassword();
					String driver = ds.getDriverClass();
					String url = ds.getUrl();
					String poolsize = !StringUtils.isBlank(ds.getPoolsize()) ? ds.getPoolsize() : "10";
					String timeout = !StringUtils.isBlank(ds.getTimeout()) ? ds.getTimeout() : "5000";
	
	//				dataSource = PersistenceUtils.getC3P0DataSource(username, password, driver, url, poolsize, timeout);
					dataSource = PersistenceUtils.getDruidDataSource(appvo.getName(),username, password, driver, url, poolsize, timeout);
	//				dataSource = PersistenceUtils.getProxoolDataSource(username, password, driver, url, poolsize, timeout);
				} 
			}
			dataSources.put(applicationId, dataSource);
		}

		return dataSource;
	}

	/**
	 * Remove the data source.
	 * 
	 * @param application
	 *            The application name
	 */
	public static synchronized void removeDataSource(String application) {
		dataSources.remove(application);
	}

	/**
	 * Get the connection
	 * 
	 * @return the connection
	 * @throws Exception
	 */
	protected Connection getConnection() throws Exception {
		Map<String, Connection> connMap = PersistenceUtils.runtimeDBConn.get();
		Connection conn = null;
		try {
			if (connMap != null) {
				conn = (Connection) connMap.get(getApplicationId());
			}
			if (conn == null || conn.isClosed()) {
				conn = getDataSource().getConnection();
				Map<String, Connection> map = new HashMap<String, Connection>();
				map.put(getApplicationId(), conn);
				PersistenceUtils.runtimeDBConn.set(map);
				if(conn.getMetaData().getDriverName().equals("jTDS Type 4 JDBC Driver for MS SQL Server and Sybase")){
					conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		

		return MonProxyFactory.monitor(conn);
	}

	/**
	 * Get the transaction singal.
	 * 
	 * @return The transcation singal.
	 */
	protected int getTransactionSignal() {
		Integer signal = (Integer) transactionSignal.get();
		if (signal != null) {
			return signal.intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Set the transaction signal
	 * 
	 * @param signal
	 *            The transaction signal to set.
	 */
	protected void setTransactionSignal(int signal) {
		transactionSignal.set(Integer.valueOf(signal));
	}

}
