package cn.myapps.km.org.ejb;

import java.util.Collection;

import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface NRoleProcess extends NRunTimeProcess<NRole> {
	
	public Collection<NRole> doGetRoles() throws Exception;
	public Collection<NRole> doGetRolesByName(String name) throws Exception;
	public Collection<NRole> doQueryRolesByUser(String userId) throws Exception;

}
