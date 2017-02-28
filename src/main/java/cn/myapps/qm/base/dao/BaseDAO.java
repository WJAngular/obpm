package cn.myapps.qm.base.dao;

import cn.myapps.base.dao.ValueObject;

public interface BaseDAO {
	public ValueObject create(ValueObject vo) throws Exception;
	
	public void delete(String pk) throws Exception;
	
	public ValueObject find(String pk) throws Exception;
	
	public ValueObject update(ValueObject vo) throws Exception;
}
