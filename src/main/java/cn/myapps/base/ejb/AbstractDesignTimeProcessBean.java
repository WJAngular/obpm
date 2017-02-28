package cn.myapps.base.ejb;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public abstract class AbstractDesignTimeProcessBean<E> implements IDesignTimeProcess<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 87703310202408385L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doCreate(cn.myapps.base.dao.ValueObject )
	 */
	public void doCreate(ValueObject vo) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			if (vo.getId() == null || vo.getId().trim().length() == 0) {
				vo.setId(Sequence.getSequence());
			}

			if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
				vo.setSortId(Sequence.getTimeSequence());
			}

			getDAO().create(vo);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	public boolean checkExitName(String name, String application) throws Exception {
		try {
			ValueObject po = getDAO().findByName(name, application);
			if (po != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doCreate(cn.myapps.base.dao.ValueObject
	 *      [])
	 */
	public void doCreate(ValueObject[] vos) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			if (vos != null)
				for (int i = 0; i < vos.length; i++) {
					ValueObject vo = vos[i];

					if (vo.getId() == null || vo.getId().trim().length() == 0) {
						vo.setId(Sequence.getSequence());
					}

					if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
						vo.setSortId(Sequence.getTimeSequence());
					}
					getDAO().create(vo);
				}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doCreate(java.util.Collection)
	 */
	public void doCreate(Collection<ValueObject> vos) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			if (vos != null)
				for (Iterator<ValueObject> iter = vos.iterator(); iter.hasNext();) {
					ValueObject vo = (ValueObject) iter.next();
					if (vo.getId() == null || vo.getId().trim().length() == 0) {
						vo.setId(Sequence.getSequence());
					}

					if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
						vo.setSortId(Sequence.getTimeSequence());
					}
					getDAO().create(vo);
				}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.BaseProcess#doCreate(cn.myapps.base.dao.ValueObject,
	 *      cn.myapps.core.user.action.WebUser)
	 */
	public void doCreate(ValueObject vo, WebUser user) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			if (vo.getId() == null || vo.getId().trim().length() == 0) {
				vo.setId(Sequence.getSequence());
			}

			if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
				vo.setSortId(Sequence.getTimeSequence());
			}
			getDAO().create(vo);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.BaseProcess#doRemove(java.lang.String)
	 */
	public void doRemove(String pk) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			getDAO().remove(pk);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doRemove(java.lang.String[])
	 */
	public void doRemove(String[] pks) throws Exception {
		try {
			StringBuffer errorMsg = new StringBuffer();
			if (pks != null && pks.length > 0) {
				for (int i = 0; i < pks.length; i++) {
					try {
						doRemove(pks[i]);
					}
					catch (OBPMValidateException e) {
						throw new OBPMValidateException(errorMsg.append(e.getValidateMessage() + ";").toString());
					}catch (Exception e) {
						errorMsg.append(e.getMessage() + ";");
					}
				}
				if (errorMsg.lastIndexOf(";") != -1) {
					errorMsg.deleteCharAt(errorMsg.lastIndexOf(";"));
				}
				if (errorMsg.length() > 0) {
					throw new Exception(errorMsg.toString());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void doRemove(Collection<E> list) throws Exception {
		getDAO().remove(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doUpdate(cn.myapps.base.dao.ValueObject )
	 */
	public void doUpdate(ValueObject vo) throws Exception {
		try {
			PersistenceUtils.beginTransaction();

			ValueObject po = getDAO().find(vo.getId());
			if (po != null) {
				PropertyUtils.copyProperties(po, vo);
				getDAO().update(po);
			} else {
				getDAO().update(vo);
			}

			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doUpdate(cn.myapps.base.dao.ValueObject
	 *      [])
	 */
	public void doUpdate(ValueObject[] vos) throws Exception {
		try {
			PersistenceUtils.beginTransaction();

			if (vos != null)
				for (int i = 0; i < vos.length; i++) {
					ValueObject vo = vos[i];
					ValueObject po = getDAO().find(vo.getId());
					if (po != null) {
						PropertyUtils.copyProperties(po, vo);
						getDAO().update(po);
					} else {
						getDAO().update(vo);
					}
				}

			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doUpdate(java.util.Collection)
	 */
	public void doUpdate(Collection<ValueObject> vos) throws Exception {
		try {
			PersistenceUtils.beginTransaction();

			if (vos != null)
				for (Iterator<ValueObject> iter = vos.iterator(); iter.hasNext();) {
					ValueObject vo = (ValueObject) iter.next();
					ValueObject po = getDAO().find(vo.getId());
					if (po != null) {
						PropertyUtils.copyProperties(po, vo);
						getDAO().update(po);
					} else {
						getDAO().update(vo);
					}
				}

			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.BaseProcess#doUpdate(cn.myapps.base.dao.ValueObject,
	 *      cn.myapps.core.user.action.WebUser)
	 */
	public void doUpdate(ValueObject vo, WebUser user) throws Exception {
		try {
			PersistenceUtils.beginTransaction();

			ValueObject po = getDAO().find(vo.getId());
			PropertyUtils.copyProperties(po, vo);

			getDAO().update(vo);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
		}
	}
	
	

	/* (non-Javadoc)
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doCreateOrUpdate(cn.myapps.base.dao.ValueObject)
	 */
	public void doCreateOrUpdate(ValueObject vo) throws Exception {
		if (StringUtil.isBlank(vo.getId()))
			doCreate(vo);
		else
			doUpdate(vo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.BaseProcess#doView(java.lang.String)
	 */
	public ValueObject doView(String pk) throws Exception {
		return getDAO().find(pk);
	}

	public ValueObject doViewByName(String name, String application) throws Exception {
		return getDAO().findByName(name, application);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecn.myapps.base.ejb.IDesignTimeProcess#doQuery(cn.myapps.base.action.
	 * ParamsTable, cn.myapps.core.user.action.WebUser)
	 */
	public DataPackage<E> doQuery(ParamsTable params, WebUser user) throws Exception {
		return getDAO().query(params, user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecn.myapps.base.ejb.IDesignTimeProcess#doQuery(cn.myapps.base.action.
	 * ParamsTable)
	 */
	public DataPackage<E> doQuery(ParamsTable params) throws Exception {
		return getDAO().query(params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecn.myapps.base.ejb.BaseProcess#doSimpleQuery(cn.myapps.base.action.
	 * ParamsTable)
	 */
	public Collection<E> doSimpleQuery(ParamsTable params) throws Exception {
		return getDAO().simpleQuery(params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doSimpleQuery(cn.myapps.base.action
	 *      .ParamsTable, java.lang.String)
	 */
	public Collection<E> doSimpleQuery(ParamsTable params, String application) throws Exception {
		if (application != null) {
			if (params == null)
				params = new ParamsTable();
			params.setParameter("application", application);
		}

		return getDAO().simpleQuery(params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IDesignTimeProcess#doRemove(cn.myapps.base.dao.ValueObject )
	 */
	public void doRemove(ValueObject obj) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			getDAO().remove(obj);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
	}

	/**
	 * Get the relate data access object.
	 * 
	 * @return The relate data access object.
	 * @throws Exception
	 */
	protected abstract IDesignTimeDAO<E> getDAO() throws Exception;
	
	/**
	 * 通过hql语句获得数据总数
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public int doGetTotalLines(String hql) throws Exception{
		return getDAO().getTotalLines(hql);
	}

	public Collection<E> doQueryByHQL(String hql,int pageNo,int pageSize) throws Exception {
		return this.getDAO().queryByHQL(hql,pageNo,pageSize);
	}
	
	public void doCheckout(String id, WebUser user) throws Exception{
		PersistenceUtils.beginTransaction();
		this.getDAO().checkout(id, user);
		PersistenceUtils.commitTransaction();
		
	}
	
	public void doCheckin(String id, WebUser user) throws Exception{
		PersistenceUtils.beginTransaction();
		this.getDAO().checkin(id, user);
		PersistenceUtils.commitTransaction();
	}
	
	

}
