package cn.myapps.km.permission.dao;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.permission.ejb.FileAccess;
import cn.myapps.km.permission.ejb.Permission;

public interface FileAccessDAO extends NRuntimeDAO{

	public FileAccess find(String id) throws Exception; 
	
	public void remove(String id) throws Exception;
	
	public DataPackage<FileAccess> query() throws Exception;
	
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
	public FileAccess findByOwner(String fileId,String ownerId,String scope) throws Exception;
	
	/**
	 * @param permissionId
	 * @throws Exception
	 */
	public void removeByPermission(String permissionId) throws Exception;
}
