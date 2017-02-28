package cn.myapps.km.org.ejb;

import java.util.Collection;

import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface NUserRoleSetProcess extends NRunTimeProcess<NUserRoleSet> {
	
	public void doUpdateUserRoleSet(String userId,String[]roleIds) throws Exception;
	public Collection<NUserRoleSet> doQuertByUser(String userId) throws Exception;
	
}
