package cn.myapps.rm.role.dao;

import java.util.Collection;

import cn.myapps.rm.base.dao.RuntimeDAO;
import cn.myapps.rm.role.ejb.UserRoleSet;

public interface UserRoleSetDAO extends RuntimeDAO {
	
	public void removeByUser(String userId) throws Exception;
	public Collection<UserRoleSet> quertByUser(String userId) throws Exception;

}
