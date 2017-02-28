package cn.myapps.km.disk.dao;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.disk.ejb.IFile;
import cn.myapps.km.disk.ejb.NDisk;

public interface NDiskDAO extends NRuntimeDAO{
	
	public NDisk find(String id) throws Exception; 
	
	public void remove(String id) throws Exception;
	
	public DataPackage<NDisk> query() throws Exception;
	
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
	
	public DataPackage<IFile> getHotestFiles(int page, int lines,
			String domainid, String categoryID) throws Exception;
	
	public DataPackage<IFile> doListNewestFile(int page, int lines, String domainid, String categoryID) throws Exception;
	
	/**
	 * 获取归档网盘
	 * @return
	 * @throws Exception
	 */
	public NDisk getArchiveDisk(String domainid) throws Exception;
}
