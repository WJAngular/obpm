package cn.myapps.core.role.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface RoleProcess extends IDesignTimeProcess<RoleVO> {
	/**
	 * 移除记录
	 * 
	 * @param pk
	 *            标识
	 * @return
	 */
	public abstract void doRemove(String pk) throws Exception;

	/**
	 * 获得应用相应的角色
	 * 
	 * @param applicationId
	 *            应用标识
	 * @return 角色的集合
	 */
	public Collection<RoleVO> getRolesByApplication(String applicationId) throws Exception;
	
	/**
	 * 获得应用相应的角色
	 * 
	 * @param applicationId
	 *            应用标识
	 * @return 角色的集合
	 */
	public Collection<RoleVO> getDefaultRolesByApplication(String applicationId) throws Exception;

	/**
	 * 
	 * 根据参数查询角色
	 * 
	 * @param name
	 *            角色名
	 * @param applicationId
	 *            应用标识
	 * @return 返回角色集合
	 * @throws Exception
	 */
	public RoleVO doViewByName(String name, String applicationId) throws Exception;

	public Collection<RoleVO> queryByUser(String userId) throws Exception;

	public RoleVO findByRoleNo(String roleNo, String applicationid) throws Exception;
	
	public Collection<RoleVO> getRolesByapplicationids(String applicationids)throws Exception;
}
