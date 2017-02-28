package cn.myapps.base.ejb;

import java.io.Serializable;
import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;

/**
 * The base process interface.
 */
public interface IDesignTimeProcess<E> extends Serializable {
	/**
	 * 创建数据文档对象
	 * 
	 * @param object
	 *            数据文档对象.
	 * @throws Exception
	 */
	public abstract void doCreate(ValueObject object) throws Exception;

	/**
	 * 批量创建数据文档对象
	 * 
	 * @param vos
	 *            数据文档对象数组
	 * @throws Exception
	 */
	public abstract void doCreate(ValueObject[] vos) throws Exception;

	/**
	 * 批量创建数据文档对象
	 * 
	 * @param vos
	 *            数据文档对象集合
	 * @throws Exception
	 */
	public abstract void doCreate(Collection<ValueObject> vos) throws Exception;

	/**
	 * 创建数据文档对象
	 * 
	 * @param object
	 *            数据文档对象.
	 * @param user
	 *            当前登录用户.
	 * @throws Exception
	 */
	public abstract void doCreate(ValueObject object, WebUser user) throws Exception;

	/**
	 * 移除数据文档对象
	 * 
	 * @param pk
	 *            数据文档对象ID标识.
	 * @throws Exception
	 */
	public abstract void doRemove(String pk) throws Exception;

	/**
	 * 批量移除数据文档对象
	 * 
	 * @param pks
	 *            数据文档对象ID标识数组.
	 * @throws Exception
	 */
	public void doRemove(String[] pks) throws Exception;

	/**
	 * 更新数据文档对象.
	 * 
	 * @param object
	 *            数据文档对象.
	 * @throws Exception
	 */
	public abstract void doUpdate(ValueObject object) throws Exception;

	/**
	 * 批量更新数据文档对象.
	 * 
	 * @param vos
	 *            数据文档对象数组.
	 * @throws Exception
	 */
	public abstract void doUpdate(ValueObject[] vos) throws Exception;

	/**
	 * 批量更新数据文档对象.
	 * 
	 * @param vos
	 *            数据文档对象集合.
	 * @throws Exception
	 */

	public abstract void doUpdate(Collection<ValueObject> vos) throws Exception;

	/**
	 * 更新数据文档对象.
	 * 
	 * @param object
	 *            数据文档对象.
	 * @param user
	 *            当前登录用户.
	 * @throws Exception
	 */
	public abstract void doUpdate(ValueObject object, WebUser user) throws Exception;

	/**
	 * 根据数据文档对象的ID标识查找数据文档对象.
	 * 
	 * @param pk
	 *            数据文档对象ID标识.
	 * @return 数据文档对象.
	 * @throws Exception
	 */
	public abstract ValueObject doView(String pk) throws Exception;

	/**
	 * 根据名称查找值对象
	 * 
	 * @param name
	 *            值对象名称
	 * @param application
	 *            软件ID
	 * @return 值对象
	 * @throws Exception
	 */
	public abstract ValueObject doViewByName(String name, String application) throws Exception;

	/**
	 * 根据条件列表查询数据文档对象记录.
	 * 
	 * @param params
	 *            条件列表.
	 * @param user
	 *            当前登录用户.
	 * @return 数据文档记录包装对象.
	 * @throws Exception
	 */
	public abstract DataPackage<E> doQuery(ParamsTable params, WebUser user) throws Exception;

	/**
	 * 根据条件列表查询数据文档对象记录.
	 * 
	 * @param params
	 *            条件列表.
	 * @return 数据文档记录包装对象.
	 * @throws Exception
	 */
	public DataPackage<E> doQuery(ParamsTable params) throws Exception;

	/**
	 * 根据条件列表查询数据文档对象记录.
	 * 
	 * @param params
	 *            条件列表.
	 * @return 数据文档记录集合.
	 * @throws Exception
	 */
	public abstract Collection<E> doSimpleQuery(ParamsTable params) throws Exception;

	/**
	 * 根据条件列表查询数据文档对象记录.
	 * 
	 * @param params
	 *            条件列表.
	 * @param application
	 *            应用软件ID标识
	 * @return 数据文档记录集合.
	 * @throws Exception
	 */
	public abstract Collection<E> doSimpleQuery(ParamsTable params, String application) throws Exception;
	
	
	/**
	 * 根据编写的hql语句查询记录集合
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<E>doQueryByHQL(String hql,int pageNo,int pageSize) throws Exception;

	/**
	 * 移除数据文档对象
	 * 
	 * @param vo
	 *            数据文档对象.
	 * @throws Exception
	 */
	public void doRemove(ValueObject vo) throws Exception;
	
	
	/**
	 * 创建或更新对象
	 * @param vo
	 * @throws Exception
	 */
	public void doCreateOrUpdate(ValueObject vo) throws Exception;

	/**
	 * Ajax检查name是否已近存在 param name return Boolean
	 */
	public boolean checkExitName(String name, String application) throws Exception;
	/**
	 * 通过hql语句获得数据总数
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public int doGetTotalLines(String hql) throws Exception;
	
	/**
	 * Checkout
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	public void doCheckout(String id, WebUser user) throws Exception;
	
	/**
	 * Checkin
	 * @param id
	 * @param user
	 * @throws Exception
	 */
	public void doCheckin(String id, WebUser user) throws Exception;
}
