package cn.myapps.rm.base.dao;

import cn.myapps.rm.base.ejb.BaseObject;



public interface RuntimeDAO {
	public void create(BaseObject vo) throws Exception;

	public void remove(String pk) throws Exception;

	public void update(BaseObject vo) throws Exception;

	public BaseObject find(String id) throws Exception;
}
