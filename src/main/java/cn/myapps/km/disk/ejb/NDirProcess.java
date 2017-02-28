package cn.myapps.km.disk.ejb;

import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface NDirProcess extends NRunTimeProcess<NDir> {
	
	/**
	 * 根据父目录查找下级目录及文件
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public Collection<IFile> getUnderNDirAndNFile(String parentid) throws Exception;
	
	/**
	 * 查找下级目录
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public Collection<NDir> getUnderNDir(String parentid) throws Exception;
	
	/**
	 * 在指定网盘中查找下级目录
	 * @param ndiskid
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public Collection<NDir> getUnderNDirByDisk(String ndiskid, String parentid) throws Exception;
	
	/**
	 * 查找下级目录的数量
	 * @param ndiskid
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public long countUnderNDir(String ndiskid, String parentid) throws Exception;
	
	/**
	 * 分页查找下级目录及文件
	 * @param page
	 * @param lines
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<IFile> getUnderNDirAndNFile(int page, int lines, String parentid,String orderbyFile,String orderbyMode) throws Exception;
	
	/**
	 * 根据网盘ID获取网盘下所有的文件夹
	 * @param ndiskid
	 * @return
	 * @throws Exception
	 */
	public Collection<NDir> getNDirByNDisk(String ndiskid) throws Exception; 
	
	/**
	 * 移动目录
	 * @param parentid
	 * @param dirids
	 * @throws Exception
	 */
	public void moveNDir(String parentid, String[] dirids) throws Exception;
	
	/**
	 * 获取公共网盘下系统用户共享文件夹
	 * @return
	 * @throws Exception
	 */
	public NDir getPublicShareNDir(String ndiskid) throws Exception;
	
	/**
	 * 获取个人网盘下系统用户共享文件夹
	 * @return
	 * @throws Exception
	 */
	public NDir getPersonalShareNDir(String ownerid) throws Exception;
	
	/**
	 * 获取个人网盘下的收藏文件夹
	 * @param ownerid
	 * @return
	 * @throws Exception
	 */
	public NDir getPersonalFavoritesNDir(String ownerid) throws Exception;
	
	/**
	 * 获取个人网盘下的推荐文件夹
	 * @param ownerid
	 * @return
	 * @throws Exception
	 */
	public NDir getPersonalRecommendNDir(String ownerid) throws Exception;
	
	/**
	 * 删除用户时,删除用户的文件夹
	 * @param userid
	 * @throws Exception
	 */
	public void doRemoveNdirByRemoveUser(String userid) throws Exception;
	
	/**
	 * @param dirSelects 文件夹id数组
	 * @param fileSelects 文件id数组
	 * @param diskId 网盘id
	 * @param indexRealPath 索引文件物理路径
	 * @param type 网盘类型
	 * @throws Exception
	 */
	public void doRemoveNdirAndNFile(String[] dirSelects, String[] fileSelects, String diskId, String indexRealPath, int type) throws Exception;
	
	/**
	 * 获取文件的所有上级目录列表
	 * @param nFileid
	 * @return
	 * @throws Exception
	 */
	public Collection<NDir> getSuperiorNDirList(String nDirid) throws Exception;
}
