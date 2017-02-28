package cn.myapps.km.base.ejb;

import java.sql.Connection;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.BaikeUtils;
import cn.myapps.km.util.Sequence;





/**
 * The base abstract run time process bean.
 */
public abstract class AbstractRunTimeProcessBean<E> implements NRunTimeProcess<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4600206742931383225L;

	/**
	 * 单态时存在问题
	 */
	public static final ThreadLocal<Integer> transactionSignal = new ThreadLocal<Integer>();


	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doView(java.lang.String)
	 */
	public NObject doView(String id) throws Exception {
		return getDAO().find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doCreate(cn.myapps.base.dao.ValueObject)
	 */
	public void doCreate(NObject vo) throws Exception {
		try {
			if (vo.getId() == null || vo.getId().trim().length() == 0 || vo.getId() == "") {
				vo.setId(Sequence.getSequence());
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
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doUpdate(cn.myapps.base.dao.ValueObject)
	 */
	public void doUpdate(NObject vo) throws Exception {
		try {
			beginTransaction();
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
	public DataPackage<E> doQuery(ParamsTable params, NUser user) throws Exception {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#closeConnection()
	 */
	public void closeConnection() throws Exception {
		if (getConnection() != null) {
			if (!getConnection().isClosed())
				getConnection().close();

			BaikeUtils.runtimeDBConn.set(null);
		}
	}

	/**
	 * Get the relate Dao
	 * 
	 * @return The relate Dao.
	 * @throws Exception
	 */
	protected abstract NRuntimeDAO getDAO() throws Exception;


	/**
	 * Get the connection
	 * 
	 * @return the connection
	 * @throws Exception
	 */
	protected Connection getConnection() throws Exception {
	//	return NDataSource.getConnection();
		
		Connection threadLocal = BaikeUtils.runtimeDBConn.get();
		Connection conn = null;
		if (threadLocal != null) {
			conn = threadLocal;
		}
		if (conn == null || conn.isClosed()) {
			conn = NDataSource.getConnection();
			if(conn !=null)
				BaikeUtils.runtimeDBConn.set(conn);
		}
		
		return conn;

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
