package cn.myapps.core.role.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.role.ejb.RoleVO;

public interface RoleDAO extends IDesignTimeDAO<RoleVO> {
	public Collection<RoleVO> getRoleByName(String byName, String application) throws Exception;

	public Collection<RoleVO> getRolesByDepartment(String deptid) throws Exception;

	public Collection<RoleVO> getRolesByDepartments(String[] deptids) throws Exception;

	public Collection<RoleVO> getRolesByApplication(String applicationId) throws Exception;

	/**
	 * 获取软件下默认角色
	 * @param applicationId
	 * @return
	 * @throws Exception
	 */
	public Collection<RoleVO> getDefaultRolesByApplication(String applicationId) throws Exception;

	public RoleVO findByName(String name, String applicationid) throws Exception;
	
	public RoleVO findByRoleNo(String roleNo, String applicationid) throws Exception;
	
	public Collection<RoleVO> getRolesByapplicationids(String applicationids)throws Exception;
}
