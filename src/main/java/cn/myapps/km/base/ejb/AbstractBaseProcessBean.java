package cn.myapps.km.base.ejb;

import java.io.File;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.constans.Environment;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.PersistenceUtils;

public abstract class AbstractBaseProcessBean<E> {
	
	/**
	 * 单态时存在问题
	 */
	public static final ThreadLocal<Integer> transactionSignal = new ThreadLocal<Integer>();
	
	public abstract NRuntimeDAO getDAO() throws Exception;
		
	protected Connection getConnection() throws Exception {
	//	return NDataSource.getConnection();
		
		Connection threadLocal = PersistenceUtils.runtimeDBConn.get();
		Connection conn = null;
		if (threadLocal != null) {
			conn = threadLocal;
		}
		if (conn == null || conn.isClosed()) {
			conn = NDataSource.getConnection();
			if(conn !=null)
				PersistenceUtils.runtimeDBConn.set(conn);
		}
		
		return conn;

	}

	public abstract void doCreate(NObject no) throws Exception;

	public abstract void doUpdate(NObject no) throws Exception;

	public abstract void doRemove(String pk) throws Exception;

	public abstract NObject doView(String id) throws Exception;

	public void beginTransaction() throws Exception {

		int signal = getTransactionSignal();

		if (signal == 0){
			Connection conn = getConnection();
			conn.setAutoCommit(false);
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
			conn.commit();
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
			conn.rollback();
			conn.setAutoCommit(true);
			if(!conn.getMetaData().getDriverName().equals("Oracle JDBC driver")){
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			}
		}

		setTransactionSignal(signal);
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
	
	

	public void closeConnection() throws Exception {
		if (getConnection() != null) {
			if (!getConnection().isClosed())
				getConnection().close();
		}
		
	}

	public DataPackage<E> doQuery(ParamsTable params, NUser user)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	//服务器目录的绝对路径
	public String getRealPath(){
		Environment evt = Environment.getInstance();
//		HttpServletRequest request = ServletActionContext.getRequest();
		String realPath = evt.getApplicationRealPath()+File.separator+FileUtils.uploadFolder;
		return realPath;
	}
	
	//服务器目录的绝对路径
	public String getRealPath(ParamsTable params){
		HttpServletRequest request = params.getHttpRequest();
		String realPath = request.getSession().getServletContext().getRealPath("")+File.separator+FileUtils.uploadFolder;
		return realPath;
	}

}
