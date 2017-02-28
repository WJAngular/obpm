package cn.myapps.base.dao;

import java.util.Collection;

import org.hibernate.SessionFactory;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;

/**
 * @author Jarod
 */
public interface IDesignTimeDAO<E> extends IBaseDAO {

	/**
	 * Get data Object
	 * 
	 * @param hql
	 * @return object
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getData(java.lang.String)
	 */
	public abstract Object getData(String hql) throws Exception;

	/**
	 * Get datas collection
	 * 
	 * @param hql
	 * @return collection
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatas(java.lang.String)
	 */
	public abstract Collection<E> getDatas(String hql) throws Exception;

	/**
	 * Get datas collection .
	 * 
	 * @param hql
	 * @param params
	 * @return collection
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatas(java.lang.String,
	 *      java.lang.Object)
	 */
	public abstract Collection<E> getDatas(String hql, ParamsTable params) throws Exception;

	/**
	 * Get datas collection.
	 * 
	 * @param hql
	 * @param params
	 * @param page
	 * @param lines
	 * @return collection Get datas collection
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatas(java.lang.String,
	 *      java.lang.Object, int, int)
	 */
	public abstract Collection<E> getDatas(String hql, ParamsTable params, int page, int lines) throws Exception;

	/**
	 * Get TotalLines
	 * 
	 * @param hql
	 * @return int
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getTotalLines(java.lang.String)
	 */
	public abstract int getTotalLines(String hql) throws Exception;

	/**
	 * @param hql
	 * @param page
	 * @param lines
	 * @return Collection Get datas collection.
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatas(java.lang.String, int,
	 *      int)
	 */
	public abstract Collection<E> getDatas(String hql, int page, int lines) throws Exception;

	/**
	 * @param hql
	 * @return DataPackage Get the datapackage.
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatapackage(java.lang.String)
	 */
	public abstract DataPackage<E> getDatapackage(String hql) throws Exception;

	/**
	 * @param hql
	 * @param page
	 * @param lines
	 * @return dataPackape
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatapackage(java.lang.String,
	 *      int, int)
	 */
	public abstract DataPackage<E> getDatapackage(String hql, int page, int lines) throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatapackage(java.lang.String,
	 *      java.lang.Object)
	 */
	public abstract DataPackage<E> getDatapackage(String hql, ParamsTable params) throws Exception;

	/**
	 * Get the datapackage
	 * 
	 * @param hql
	 * @param params
	 *            Object
	 * @param page
	 *            int
	 * @param lines
	 *            int
	 * @return datapackage
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#getDatapackage(java.lang.String,
	 *      java.lang.Object, int, int)
	 */
	public abstract DataPackage<E> getDatapackage(String hql, ParamsTable params, int page, int lines) throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#create(cn.myapps.base.dao.ValueObject,
	 *      cn.myapps.core.user.action.WebUser)
	 */
	public abstract void create(ValueObject vo, WebUser user) throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#create(java.lang.Object)
	 */
	public abstract void create(Object po) throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#update(cn.myapps.base.dao.ValueObject,
	 *      cn.myapps.core.user.action.WebUser)
	 */
	public abstract void update(ValueObject vo, WebUser user) throws Exception;

	/**
	 * @see cn.myapps.base.dao.IDesignTimeDAO#update(java.lang.Object)
	 */
	public abstract void update(Object po) throws Exception;

	/**
	 * 
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#query(cn.myapps.base.action.ParamsTable)
	 */
	public abstract DataPackage<E> query(ParamsTable params) throws Exception;

	public abstract SessionFactory buildSessionFactory() throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#find(java.lang.String)
	 */
	public abstract ValueObject find(String id) throws Exception;

	public abstract ValueObject findByName(String name, String application) throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#create(cn.myapps.base.dao.ValueObject)
	 */
	public abstract void create(ValueObject vo) throws Exception;

	/**
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.dao.IBaseDAO#update(cn.myapps.base.dao.ValueObject)
	 */
	public abstract void update(ValueObject vo) throws Exception;

	/**
	 * 
	 * @see cn.myapps.base.dao.IDesignTimeDAO#remove(java.lang.String)
	 */
	public abstract void remove(String id) throws Exception;

	public abstract void remove(ValueObject obj) throws Exception;

	public abstract void remove(String ids[]) throws Exception;

	public abstract void remove(Collection<E> vos) throws Exception;

	/**
	 * query datapackage
	 * 
	 * @param params
	 *            ParamsTable
	 * @param user
	 *            WebUser
	 * @return datapackage
	 */
	public abstract DataPackage<E> query(ParamsTable params, WebUser user) throws Exception;

	/**
	 * Get datas collection by the Parameters
	 * 
	 * @param params
	 *            ParamsTable
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.base.dao.IDesignTimeDAO#simpleQuery(cn.myapps.base.action.ParamsTable)
	 */
	public abstract Collection<E> simpleQuery(ParamsTable params) throws Exception;

	/**
	 * 根据SQL查询
	 * 
	 * @param sql
	 * @param params
	 * @param params
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<E> getDatapackageBySQL(String sql, ParamsTable params, int page, int lines) throws Exception;

	/**
	 * 获取hibernate配置文件的default_schema
	 * 
	 * @return
	 */
	public String getSchema();

	public Collection<E> getDatasBySQL(String sql, int page, int lines) throws Exception;

	public Collection<E> getDatasBySQL(String sql) throws Exception;
	
	public Collection<E> queryByHQL(String hql,int pageNo,int pageSize) throws Exception;

	public int executeUpdate(String hql) throws Exception;
	
	/**
	 * chekcout
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	public void checkout(String id, WebUser user) throws Exception;
	
	/**
	 * checkin
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	public void checkin(String id, WebUser user) throws Exception;
}
