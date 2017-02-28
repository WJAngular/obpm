package cn.myapps.km.baike.user.ejb;

import java.util.Collection;

import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author dragon
 * 用户收藏词条表：BUserEntrySet 
 *  process层接口
 */
public interface BUserEntrySetProcess extends NRunTimeProcess<BUserEntrySet>{
	
	public long countSharedEntry(String userId) throws Exception;
	
	public long countFavoriteEntry(String userId) throws Exception;
	
	public long countEntryByUserId(String userId, String type) throws Exception;
	

	/**
	 * 统计词条被收藏或者分享次数
	 * @param entryId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public long countBeingFavorites(String entryId , String type) throws Exception;
	/**
	 * 
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public long countBeingFavorite(String entryId) throws Exception;
	
	/**
	 * 
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public long countBeingShare(String entryId) throws Exception;
	
	
	public DataPackage<Entry> queryFavoriteAll(int page,int lines,String userId) throws Exception;
	
	public DataPackage<Entry> getPersonalShares(int page,int lines,String userId) throws Exception;
	
	public DataPackage<Entry> queryByUserIdAndType(int page,int lines,String userId,String type) throws Exception;
	
	/**
	 * 根据修改人查询我的贡献
	 * @param page
	 * @param lines
	 * @param author
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByAuthor(int page,int lines,String author,String state) throws Exception;
	
	/**
	 * 判断用户是否已经收藏这个词条
	 * @param entrySet
	 * @return
	 * @throws Exception
	 */
	public boolean isFavoritesEntry(BUserEntrySet entrySet) throws Exception;
	
	/**
	 * 收藏的个数
	 */
	public int doFavoriteCount(String entryId) throws Exception;
	
	/**
	 * 判断是否为管理员
	 * @param userId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public boolean isPublicDiskAdmin(String userId,int r_level) throws Exception;
	public DataPackage<Entry> getNotThroungh(int page,int lines,String userId) throws Exception;
}
