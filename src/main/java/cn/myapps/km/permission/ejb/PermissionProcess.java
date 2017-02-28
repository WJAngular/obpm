package cn.myapps.km.permission.ejb;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface PermissionProcess extends NRunTimeProcess<Permission> {
	
	public DataPackage<Permission> doQueryByFile(String fileId) throws Exception;
	
	/**
	 * 
	 * @param fileId
	 * 			文件ID
	 * @param ownerId
	 * 			权限拥有者ID（用户ID 或 角色ID）
	 * @return
	 * @throws Exception
	 */
	public Permission doQueryByFileAndOwner(String fileId, String ownerId) throws Exception;
	
	
	public DataPackage<Permission> doQuery(String fileId, ParamsTable params) throws Exception;
}
