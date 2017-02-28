package cn.myapps.km.org.dao;

import java.util.Collection;

import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.org.ejb.NRole;

public interface NRoleDAO extends NRuntimeDAO {

	public Collection<NRole> getRoles() throws Exception;
	public Collection<NRole> getRolesByName(String name) throws Exception;
	public Collection<NRole> queryRolesByUser(String userId) throws Exception;
}
