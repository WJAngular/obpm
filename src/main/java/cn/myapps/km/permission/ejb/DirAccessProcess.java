package cn.myapps.km.permission.ejb;

import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface DirAccessProcess extends NRunTimeProcess<DirAccess> {
	
	/**
	 * 根据文件id、作用目标id,作用范围获取FileAccess
	 * @param fileId
	 * 		文件id
	 * @param ownerId
	 * 		作用目标id
	 * @param scope
	 * 		作用范围
	 * @return
	 * 		FileAccess
	 * @throws Exception
	 */
	public IFileAccess findByOwner(String fileId,String ownerId,String scope) throws Exception;
	
	/**
	 * 检查文件操作权限
	 * @param fileId
	 * 		文件ID
	 * @param scope
	 * 		作用域（用户、角色、部门）
	 * @param ownerId
	 * 		作用ID（用户id，角色id，部门id）
	 * @param permissionType
	 * 		权限类型（阅读、下载、写入、打印）
	 * @return
	 * 		true|false
	 * @throws Exception
	 */
	public boolean checkPermission(String fileId, String scope,
			String ownerId, int permissionType) throws Exception;
	
	/**
	 * @param permission
	 * @throws Exception
	 */
	public void doCreateByPermission(Permission permission) throws Exception;
	
	/**
	 * @param permissionId
	 * @throws Exception
	 */
	public void doRemoveByPermission(String permissionId) throws Exception;

	
}
