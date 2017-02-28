package cn.myapps.mr.area.dao;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.mr.area.ejb.Area;
import cn.myapps.mr.base.dao.BaseDAO;

public interface AreaDAO extends BaseDAO{
	public Collection<Area> findAllAreas() throws Exception;
	public boolean create(ValueObject vo) throws Exception;
	public void remove(String pk) throws Exception;
	public boolean update(ValueObject vo) throws Exception;
}
