package cn.myapps.core.counter.ejb;

import java.util.Collection;
import java.util.HashMap;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.counter.dao.CounterDAO;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.sequence.Sequence;

/**
 * @author nicholas
 */
public class CounterProcessBean extends AbstractRunTimeProcessBean<CounterVO>
		implements CounterProcess {

	private static final Object _lock = new Object();

	private static HashMap<CounterKey, CounterVO> _counters = new HashMap<CounterKey, CounterVO>();

	public CounterProcessBean(String applicationId) {
		super(applicationId);
	}

	private static final long serialVersionUID = -3768074774695007780L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.core.counter.ejb.CounterProcess#doRemoveByName(java.lang.String)
	 */
	public void doRemoveByName(String name, String application, String domainid)
			throws Exception {
		synchronized (_lock) {
			try {
				PersistenceUtils.beginTransaction();

				((CounterDAO) getDAO()).removeByName(name, application,
						domainid);
				PersistenceUtils.commitTransaction();
				// 从Cache中清除
				_counters.remove(new CounterKey(name, application, domainid));
			} catch (Exception ex) {
				PersistenceUtils.rollbackTransaction();
				ex.printStackTrace();
				throw ex;
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.core.counter.ejb.CounterProcess#getNextValue(java.lang.String)
	 */
	public int getNextValue(String name, String application, String domainid)
			throws Exception {
		synchronized (_lock) {
			try {
				// Session s =
				// PersistenceUtils.getSessionSignal().currentSession;
				PersistenceUtils.beginTransaction();
				// PersistenceUtils.getSessionSignal().currentSession.clear();
				CounterDAO dao = (CounterDAO) getDAO();
				if (name != null) {
					
CounterKey key = new CounterKey(name, application, domainid);

					CounterVO vo = findByName(name, application, domainid);
					if (vo == null) {
						vo = new CounterVO();
						vo.setId(Sequence.getSequence());
						vo.setName(name);
						vo.setCounter(1);
						vo.setApplicationid(application);
						vo.setDomainid(domainid);
						
						dao.create(vo);
					} else {
						vo.setCounter(vo.getCounter() + 1);
						dao.update(vo);
					}
					
					PersistenceUtils.commitTransaction();
					
					//更新Cache
					_counters.put(key, vo);
					
					return vo.getCounter();
				}
			} catch (Exception e) {
				e.printStackTrace();
				PersistenceUtils.rollbackTransaction();
				throw e;
			}
			return 0;
		}
	}

	public int getShortSquence(String name, String application,
			String domainid, int maxValue) throws Exception {
		return getShortSquence(name, application, domainid, maxValue, 1);
	}

	public int getShortSquence(String name, String application,
			String domainid, int maxValue, int defaultValue) throws Exception {

		synchronized (_lock) {
			try {
				PersistenceUtils.beginTransaction();
				int value = getNextValue(name, application, domainid);
				if (value < defaultValue || value > maxValue) {
					CounterVO vo = findByName(name,
							application, domainid);
					vo.setCounter(defaultValue);
					((CounterDAO) getDAO()).update(vo);
					
					PersistenceUtils.commitTransaction();
					
					//更新Cache
					_counters.put(new CounterKey(name, application, domainid), vo);
					
					value = vo.getCounter();
					return value;
				}
			} catch (Exception e) {
				PersistenceUtils.rollbackTransaction();
				e.printStackTrace();
				throw e;
			}
			
			return 0;
			
		}
	}

	public int getLastValue(String name, String application, String domainid)
			throws Exception {
		if (name != null) {
			CounterVO vo = findByName(name, application, domainid);
			if (vo == null) {
				return 0;
			} else {
				return vo.getCounter();
			}
		}
		return 0;
	}

	public Collection<CounterVO> getDatas(String sql, String domainid)
			throws Exception {
		CounterDAO dao = (CounterDAO) getDAO();
		return dao.getDatas(sql, domainid);

	}

	public CounterVO findByName(String name, String application, String domainid)
			throws Exception {

		CounterKey key = new CounterKey(name, application, domainid);
		CounterVO vo = _counters.get(key);
		if (vo == null) {
			vo = ((CounterDAO) getDAO())
					.findByName(name, application, domainid);
			if (vo != null) {
				_counters.put(key, vo);
			}
		}

		return vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.BaseProcessBean#getDAO()
	 */
	protected IRuntimeDAO getDAO() throws Exception {
		// ApplicationVO app=getApplicationVO(getApplicationId());
		RuntimeDaoManager runtimeDao = new RuntimeDaoManager();
		return runtimeDao.getCounterDAO(getConnection(), getApplicationId());
	}
}

class CounterKey {
	private String name;
	private String applicationid;
	private String domainid;

	CounterKey(String name, String application, String domainid) {
		this.name = name;
		this.applicationid = application;
		this.domainid = domainid;
	}

	public boolean equals(Object object) {
		if (object != null && object instanceof CounterKey) {
			CounterKey ck = (CounterKey) object;
			return ((ck.name == null && this.name == null) || (ck.name != null && ck.name
					.equals(name)))
					&& ((ck.applicationid == null && this.applicationid == null) || (ck.applicationid != null && ck.applicationid
							.equals(applicationid)))
					&& ((ck.domainid == null && this.domainid == null) || (ck.domainid != null && ck.domainid
							.equals(domainid)));
		}
		return false;
	}
}