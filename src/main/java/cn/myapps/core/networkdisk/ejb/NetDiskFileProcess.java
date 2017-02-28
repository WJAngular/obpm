package cn.myapps.core.networkdisk.ejb;

import java.io.File;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface NetDiskFileProcess  extends IDesignTimeProcess<NetDiskFile> {

	/**
	 * 获得总数
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public abstract int getTotalLines(String hql) throws Exception;
	
	/**
	 * 增加文档
	 * @param fileName 文件名
	 * @param filePath 文件路径
	 * @param folderId 文件夹id
	 * @param userId 用户id
	 */
	public void createFile(String filePath,String folderId,String userId) throws Exception;
	
	/**
	 * 查找文档
	 * @param fileName 文件名
	 * @param folderId 所属文件夹id
	 * @return netdiskFile 
	 */
	public NetDiskFile searchFile(String fileName,String folderId) throws Exception;
	
	/**
	 * 删除文档
	 * @param fileName 文件名
	 * @param folderId 所属文件夹id
	 */
	public void removeFile(String fileName,String folderId) throws Exception;
}
