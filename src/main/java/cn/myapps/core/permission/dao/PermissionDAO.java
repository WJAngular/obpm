package cn.myapps.core.permission.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.permission.ejb.PermissionVO;

public interface PermissionDAO extends IDesignTimeDAO<PermissionVO> {
	public PermissionVO findByResouceAndUser(String resourceId, String userId)
			throws Exception;

	public PermissionVO getPermissionByName(String name) throws Exception;

	public PermissionVO getPermissionByResourcesId(String resourcesId)
			throws Exception;

	public Collection<PermissionVO> queryByRoleIdAndResName(String roleId,
			String resName) throws Exception;

	public Collection<PermissionVO> queryByRole(String roleId, int resType)
			throws Exception;

	public Collection<PermissionVO> queryByRole(String roleId) throws Exception;

	public Collection<PermissionVO> queryByRoleAndResource(String roleId,
			String resourceId) throws Exception;

	public Collection<String> getResourceIdsByRole(String roleId)
			throws Exception;

	public Collection<String> getOperatonIdsByResourceAndRole(
			String resourceid, String roleId) throws Exception;

}
