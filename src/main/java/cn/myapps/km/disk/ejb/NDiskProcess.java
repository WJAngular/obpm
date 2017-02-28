package cn.myapps.km.disk.ejb;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface NDiskProcess extends NRunTimeProcess<NDisk> {
	
	/**
	 * 根据用户查找网盘
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public NDisk getNDiskByUser(String userid) throws Exception;
	
	/**
	 * 获取公有网盘
	 * @return
	 * @throws Exception
	 */
	public NDisk getPublicDisk(String domainid) throws Exception;
	
	/**
	 * 根据给定的用户Id创建个人网盘
	 * @param userId
	 * @throws Exception
	 */
	public void createByUser(String userId,String domainId) throws Exception;
	
	/**
	 * 根据给定的用户ID删除网盘
	 * @param userId
	 * @throws Exception
	 */
	public void removeByUser(String userId) throws Exception;

	/**
	 * 获取热门分享
	 */
	public DataPackage<IFile> doListHotest(int page, int lines, String domainid, String categoryID) throws Exception;
	
	/**
	 * 获取最新上传
	 * @param page
	 * @param lines
	 * @param domainid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<IFile> doListNewestFile(int page, int lines, String domainid, String categoryID) throws Exception;
	
	/**
	 * 获取归档网盘
	 * @return
	 * @throws Exception
	 */
	public NDisk getArchiveDisk(String domainid) throws Exception;
}
