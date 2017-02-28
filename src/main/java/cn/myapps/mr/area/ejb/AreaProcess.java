package cn.myapps.mr.area.ejb;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.mr.base.ejb.BaseProcess;

public interface AreaProcess extends BaseProcess<Area>{
	public Collection<Area> getAllAreas() throws Exception;
	public void create(Area area) throws Exception ;
	public void delete(String id) throws Exception;
	public void update(Area area) throws Exception;
}
