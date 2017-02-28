package cn.myapps.attendance.base.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;

public interface BaseProcess<E> {
	
	/**
	 * 创建实例
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public ValueObject doCreate(ValueObject vo) throws Exception;
	
	/**
	 * 更新实例
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public ValueObject doUpdate(ValueObject vo) throws Exception;
	
	/**
	 * 根据主键获取实例
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public ValueObject doView(String pk) throws Exception;
	
	/**
	 * 删除实例
	 * @param pk
	 * @throws Exception
	 */
	public void doRemove(String pk) throws Exception;
	
	/**
	 * 批量删除实例
	 * @param pk
	 * @throws Exception
	 */
	public void doRemove(String[] pk) throws Exception;
	
	/**
	 * 查询实例集合
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<E> doQuery(ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 查询实例集合
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Collection<E> doSimpleQuery(ParamsTable params,WebUser user) throws Exception;
	

}
