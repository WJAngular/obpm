package cn.myapps.rm.role.ejb;

import java.util.Collection;

import cn.myapps.rm.base.ejb.RunTimeProcess;


public interface RoleProcess extends RunTimeProcess<Role> {
	
	public Collection<Role> doGetRoles() throws Exception;
	public Collection<Role> doGetRolesByName(String name) throws Exception;
	public Collection<Role> doQueryRolesByUser(String userId) throws Exception;

}
