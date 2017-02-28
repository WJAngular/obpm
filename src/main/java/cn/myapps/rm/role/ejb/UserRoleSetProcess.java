package cn.myapps.rm.role.ejb;

import java.util.Collection;

import cn.myapps.rm.base.ejb.RunTimeProcess;


public interface UserRoleSetProcess extends RunTimeProcess<UserRoleSet> {
	
	public void doUpdateUserRoleSet(String userId,String[]roleIds) throws Exception;
	public Collection<UserRoleSet> doQuertByUser(String userId) throws Exception;
	
}
