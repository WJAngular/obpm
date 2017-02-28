package cn.myapps.km.base.dao;

import cn.myapps.km.base.ejb.NObject;


public interface NRuntimeDAO {
	public void create(NObject vo) throws Exception;

	public void remove(String pk) throws Exception;

	public void update(NObject vo) throws Exception;

	public NObject find(String id) throws Exception;
}
