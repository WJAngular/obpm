package cn.myapps.attendance.base.dao;

import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;


public interface BaseDAO {
	
	public ValueObject create(ValueObject vo) throws Exception;

	public void remove(String pk) throws Exception;

	public ValueObject update(ValueObject vo) throws Exception;

	public ValueObject find(String id) throws Exception;
	
	public Collection<?> simpleQuery(ParamsTable params,WebUser user) throws Exception;
	
	public DataPackage<?> query(ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 更新字段
	 * @param items
	 * 		更新的字段集合（key表示字段名，value表示字段值）
	 * @param pk
	 * 		主键ID的值
	 * @throws Exception
	 */
	public void update(Map<String, Object> items,String pk) throws Exception;
}
