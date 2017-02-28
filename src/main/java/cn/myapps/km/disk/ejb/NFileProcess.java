package cn.myapps.km.disk.ejb;

import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;

public interface NFileProcess extends NRunTimeProcess<NFile> {
	
	
	public void doCopyFile(String pk,String userid,String ndirid,String savePath) throws Exception;
	
	public void doCreate(NObject vo,String userid,String ndirid,String savePath) throws Exception;
	
	public DataPackage<NFile> doQuery(String userid,String ndirid) throws Exception;
	
	public void doShareFile4Public(String pk, NUser user,ParamsTable params) throws Exception;
	
	public void doShareFile4Personal(String pk, NUser user,ParamsTable params, String sharedUserId) throws Exception;
	
	
	/**
	 * 根据目录ID删除目录里的文档文件
	 * @param ndirid
	 * @throws Exception
	 */
	public void deleteFileByNDirid(String ndirid) throws Exception;
	
	/**
	 * 创建文件（doc文件格式和pdf文件格式会自动创建）
	 * @param file
	 * @param params
	 * @param user
	 * @throws Exception
	 */
	public void createFile(NFile file,ParamsTable params,NUser user) throws Exception;
	
	/**
	 * 移动文件
	 * @param parentid
	 * @param fileids
	 * @throws Exception
	 */
	public void moveNFile(String parentid, String[] fileids) throws Exception;
	
	/**
	 * 收藏文件
	 * @param fileId
	 * 		文件id
	 * @param user
	 * 		用户
	 * @param params
	 * 		参数表
	 * @throws Exception
	 */
	public void doFavorite(String fileId,NUser user,ParamsTable params) throws Exception;
	
	/**
	 * 推荐文件
	 * @param fileId
	 * 		文件id
	 * @param user
	 * 		用户(推荐者)
	 * @param params
	 * 		参数表
	 * @param reconmendUserId
	 * 		被推荐用户
	 * @throws Exception
	 */
	public void doRecommend(String fileId,NUser user,ParamsTable params, String reconmendUserId) throws Exception;
	
	/**
	 * 用户是否已经收藏文件
	 * @param fileId
	 * 		文件id
	 * @param user
	 * 		用户
	 * @throws Exception
	 */
	public boolean isFavorited(String fileId,NUser user) throws Exception;
	
	/**
	 * 创建swf文件
	 * @param file
	 * @param type
	 * @param params
	 * @throws Exception
	 */
	public void createSWF(NFile file, String type,ParamsTable params) throws Exception;
	
	/**
	 * 判断在给定的文件夹下,该文件是否被分享过
	 * @param ndirid
	 * @param nfileid
	 * @return
	 * @throws Exception
	 */
	public boolean isSharedInThisNdir(String ndirid, String nfileid) throws Exception;
	
	/**
	 * 删除用户时,删除用户的文件
	 * @param userid
	 * @throws Exception
	 */
	public void doRemoveNFileByRemoveUser(String userid) throws Exception;
	
	/**
	 * 获取km系统上传的文件总数
	 * @return
	 * @throws Exception
	 */
	public long getUploadFilesTotal() throws Exception;
	
	/**
	 * 根据文件名查找文件
	 * @param name
	 * @throws Exception
	 */
	public DataPackage<NFile> doQueryByName(int page,int lines,String name,String dirid) throws Exception;
	
}
