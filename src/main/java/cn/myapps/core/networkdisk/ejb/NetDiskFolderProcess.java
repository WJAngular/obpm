package cn.myapps.core.networkdisk.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface NetDiskFolderProcess  extends IDesignTimeProcess<NetDiskFolder> {
	/**
	 * 创建文件夹
	 * @param folderName 文件夹名称
	 * @param parentId 父文件夹id
	 * @param userid 用户id
	 * @param orderno 序列号
	 */
	public void createFolder(String folderName,String parentId,String userid,int orderno) throws Exception;
}
