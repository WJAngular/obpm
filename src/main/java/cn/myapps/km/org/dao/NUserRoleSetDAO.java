package cn.myapps.km.org.dao;

import java.util.Collection;

import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.org.ejb.NUserRoleSet;

public interface NUserRoleSetDAO extends NRuntimeDAO {
	
	public void removeByUser(String userId) throws Exception;
	public Collection<NUserRoleSet> quertByUser(String userId) throws Exception;

}
