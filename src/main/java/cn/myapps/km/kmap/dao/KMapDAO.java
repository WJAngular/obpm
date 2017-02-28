package cn.myapps.km.kmap.dao;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.kmap.ejb.KMap;

public interface KMapDAO extends NRuntimeDAO {
	
	public KMap find(String id) throws Exception;
	
	public void remove(String id) throws Exception;
	
	public DataPackage<KMap> query() throws Exception;
}
