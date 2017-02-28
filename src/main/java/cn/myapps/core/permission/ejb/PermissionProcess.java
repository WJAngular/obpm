package cn.myapps.core.permission.ejb;

import java.util.Collection;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.role.ejb.RoleVO;

public interface PermissionProcess extends IDesignTimeProcess<PermissionVO> {
	/**
	 * 空实现
	 * 
	 * @return
	 * @throws Exception
	 */
	public PermissionVO getAppDomain_Cache() throws Exception;

	/**
	 * 根据菜单和用户查询(PermissionVO)对象
	 * 
	 * @param resourceId
	 *            菜单标识
	 * @param userId
	 *            用户标识
	 * @return (PermissionVO)对象
	 * @throws Exception
	 */
	public PermissionVO findByResouceAndUser(String resourceId, String userId)
			throws Exception;

	public PermissionVO getPermissionByName(String name) throws Exception;

	/**
	 * 授权
	 * 
	 * @param _selects
	 * @param params1
	 * @param process
	 * @throws Exception
	 */
	public void grantAuth(String[] _selectsResources, ParamsTable params1)
			throws Exception;

	/**
	 * 取消授权
	 * 
	 * @param _selects
	 * @param params1
	 * @param process
	 * @throws Exception
	 */
	public void removeAuth(String[] _selectsResources, ParamsTable params1)
			throws Exception;

	/**
	 * 根据角色进行查询
	 * 
	 * @param roleId
	 *            角色ID
	 * @param resType
	 *            资源类型编号
	 * @return 角色集合
	 * @throws Exception
	 */
	public Collection<PermissionVO> doQueryByRole(String roleId, int resType)
			throws Exception;

	/**
	 * 根据角色进行查询
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 角色集合
	 * @throws Exception
	 */
	public Collection<PermissionVO> doQueryByRole(String roleId)
			throws Exception;
	
	
	public boolean check(Collection<RoleVO> roles, String resId,String operationId,int operationCode,int resType,String applicationId) throws Exception;
	
	public boolean check(String[] roles, String resId,String operationId,int operationCode,int resType,String applicationId) throws Exception;
	
	public boolean check(String[] roles, String resId,String operationId,int operationCode,int resType,String applicationId,boolean defalut) throws Exception;

	public Collection<PermissionVO> doQueryByRoleIdAndResName(String roleId,
			String resName) throws Exception;

	public void grantAuth(Map<String, Object> permissionMap, ParamsTable params1)
			throws Exception;

	public Collection<String> getOperatonIdsByResourceAndRole(
			String resourceid, String roleId) throws Exception;

	/**
	 * 获取角色权限配置, JSON格式：
	 * {resourceid:{resourcename:资源名称,resourcetype:资源类型,operations:[操作1,操作2]}}
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPermissionJSONByRole(String roleid) throws Exception;

	public Collection<String> getResourceIdsByRole(String roleId)
			throws Exception;
}
