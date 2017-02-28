package cn.myapps.km.permission.dao;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.permission.ejb.Permission;

public interface PermissionDAO extends NRuntimeDAO {
	
	/**
	 * 根据文件id、获取Permissions
	 * 
	 * @param fileId
	 *            文件id
	 * @param ownerId
	 * @return Permission
	 * @throws Exception
	 */
	public DataPackage<Permission> queryByFile(String fileId) throws Exception;
	
	/**
	 * 
	 * @param fileId
	 * 			文件ID
	 * @param ownerId
	 * 			权限拥有者ID（用户ID 或 角色ID）
	 * @return
	 * @throws Exception
	 */
	public Permission findByFileAndOwner(String fileId, String ownerId) throws Exception;
	
	public DataPackage<Permission> query(String fileId, String scope,String ownerName, int readOnly, int download) throws Exception;
	
}
