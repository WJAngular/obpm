package cn.myapps.attendance.base.ejb;

import java.sql.Connection;
import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

/**
 * @author Happy
 *
 * @param <E>
 */
public abstract class AbstractBaseProcessBean<E> implements BaseProcess<E> {
	
	public static final ThreadLocal<Integer> transactionSignal = new ThreadLocal<Integer>();
	
	/**
	 * 获取DAO实现
	 * @return
	 * @throws Exception
	 */
	public abstract BaseDAO getDAO() throws Exception;
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws Exception
	 */
	protected Connection getConnection() throws Exception {
		return ConnectionManager.getConnection();
	}
	
	/**
	 * 开启事务
	 * @throws Exception
	 */
	public void beginTransaction() throws Exception {

		int signal = getTransactionSignal();

		if (signal == 0){
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			if(!conn.getMetaData().getDriverName().equals("Oracle JDBC driver")){
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}
		}

		signal++;

		setTransactionSignal(signal);
	}

	/**
	 * 提交事务
	 * @throws Exception
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

	/**
	 * 回滚事务
	 * @throws Exception
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
	
	protected int getTransactionSignal() {
		Integer signal = (Integer) transactionSignal.get();
		if (signal != null) {
			return signal.intValue();
		} else {
			return 0;
		}
	}
	
	protected void setTransactionSignal(int signal) {
		transactionSignal.set(Integer.valueOf(signal));
	}
	

	public ValueObject doCreate(ValueObject vo) throws Exception {
		try {
			beginTransaction();
			if(StringUtil.isBlank(vo.getId())){
				vo.setId(Sequence.getSequence());
			}
			vo = getDAO().create(vo);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return vo;
		
	}

	public DataPackage<E> doQuery(ParamsTable params, WebUser user)
			throws Exception {
		return (DataPackage<E>) getDAO().query(params, user);
	}

	public void doRemove(String pk) throws Exception {
		try {
			beginTransaction();
			getDAO().remove(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	public void doRemove(String[] pks) throws Exception {
		try {
			beginTransaction();
			for(String pk : pks){
				doRemove(pk);
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<E> doSimpleQuery(ParamsTable params,WebUser user) throws Exception {
		return (Collection<E>) getDAO().simpleQuery(params, user);
	}

	public ValueObject doUpdate(ValueObject vo) throws Exception {
		try {
			beginTransaction();
			vo = getDAO().update(vo);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
		return vo;
	}
	
	public ValueObject doView(String pk) throws Exception {
		return getDAO().find(pk);
	}

}
