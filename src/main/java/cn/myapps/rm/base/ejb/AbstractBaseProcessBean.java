package cn.myapps.rm.base.ejb;

import java.sql.Connection;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.rm.base.dao.DataPackage;
import cn.myapps.rm.base.dao.RuntimeDAO;
import cn.myapps.rm.util.IDataSource;
import cn.myapps.rm.util.PersistenceUtils;


public abstract class AbstractBaseProcessBean<E> {
	
	public static final ThreadLocal<Integer> transactionSignal = new ThreadLocal<Integer>();
	
	public abstract RuntimeDAO getDAO() throws Exception;
		
	protected Connection getConnection() throws Exception {
	//	return NDataSource.getConnection();
		
		Connection threadLocal = PersistenceUtils.runtimeDBConn.get();
		Connection conn = null;
		if (threadLocal != null) {
			conn = threadLocal;
		}
		if (conn == null || conn.isClosed()) {
			conn = IDataSource.getConnection();
			if(conn !=null)
				PersistenceUtils.runtimeDBConn.set(conn);
		}
		
		return conn;

	}

	public abstract void doCreate(BaseObject no) throws Exception;

	public abstract void doUpdate(BaseObject no) throws Exception;

	public abstract void doRemove(String pk) throws Exception;

	public abstract BaseObject doView(String id) throws Exception;

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

	public DataPackage<E> doQuery(ParamsTable params, WebUser user)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
