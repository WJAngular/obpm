package cn.myapps.km.disk.dao;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.org.ejb.NUser;

public interface NFileDAO extends NRuntimeDAO {
	
	public NFile find(String id) throws Exception;
	
	public void remove(String id) throws Exception;
	
	public DataPackage<NFile> query() throws Exception;
	
	public DataPackage<NFile> query(String userid,String ndirid) throws Exception;
	
	/**
	 * 根据用户id以及目录id查找全部文件
	 */
	public DataPackage<NFile> queryAllFile(String userid,String ndirid) throws Exception;
	
	/**
	 * 移除收藏
	 */
	public void removeShare(String id) throws Exception;
	
	/**
	 * 统计用户收藏文件次数
	 * @param fileId
	 * 		文件id
	 * @param user
	 * 		用户
	 * @throws Exception
	 */
	public long countFavorited(String fileId,NUser user) throws Exception;
	
	/**
	 * 根据目录ID删除目录里的文档文件
	 * @param ndirid
	 * @throws Exception
	 */
	public void deleteFileByNDirid(String ndirid) throws Exception;
	
	/**
	 * 移动文件
	 * @param parentid
	 * @param fileids
	 * @throws Exception
	 */
	public void moveNFile(String parentid, String[] fileids) throws Exception;
	
	/**
	 * 判断在给定的文件夹下,该文件是否被分享过
	 * @param ndirid
	 * @param nfileid
	 * @return
	 * @throws Exception
	 */
	public long isSharedInThisNdir(String ndirid, String nfileid) throws Exception;
	
	/**
	 * 删除用户时,删除用户的文件
	 * @param userid
	 * @throws Exception
	 */
	public void doRemoveNFileByRemoveUser(String userid) throws Exception;
	
	/**
	 * 根据传入的sql查询记录总数
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public long countBySql(String sql) throws Exception;
	
	/**
	 * 根据文件名查找文件
	 */
	public DataPackage<NFile> queryByName(int page, int lines, String name, String dirid) throws Exception;
}
