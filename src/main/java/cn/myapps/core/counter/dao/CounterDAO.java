package cn.myapps.core.counter.dao;

import java.util.Collection;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.counter.ejb.CounterVO;

public interface CounterDAO extends IRuntimeDAO{

	/**
	 * Remove the sequence counter according the tag name.
	 * @param name The sequence tag name
	 * @throws Exception
	 */
	public abstract void removeByName(String name, String application, String domainid) throws Exception;

	/**
	 * find the sequence according the tag name.
	 * @param name The sequence tag name.
	 * @return The sequence counter.
	 * @throws Exception
	 */
	public abstract CounterVO findByName(String name, String application, String domainid) throws Exception;

	public Collection<CounterVO> getDatas(String sql,String domainid) throws Exception;
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

}
