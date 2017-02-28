package cn.myapps.rm.role.dao;

import java.util.Collection;

import cn.myapps.rm.base.dao.RuntimeDAO;
import cn.myapps.rm.role.ejb.Role;

public interface RoleDAO extends RuntimeDAO {

	public Collection<Role> getRoles() throws Exception;
	public Collection<Role> getRolesByName(String name) throws Exception;
	public Collection<Role> queryRolesByUser(String userId) throws Exception;
}
