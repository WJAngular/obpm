package cn.myapps.km.baike.user.dao;
import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.user.ejb.BUserEntrySet;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;

	/**
	 * 用户词条相关连信息
	 * @author dragon
	 * 用户收藏词条  DAO层接口
	 *
	 */
public interface BUserEntrySetDAO extends NRuntimeDAO{
	
	/**
	 * 统计用户分享词条数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public long countSharedEntry(String userId) throws Exception;
	
	/**
	 * 统计用户收藏词条数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public long countFavoriteEntry(String userId) throws Exception;
	
	/**
	 * 统计用户相关联词条次数
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public long countEntryByUserId(String userId, String type) throws Exception;
	
	/**
	 * 统计词条相关联的用户信息
	 * @param entryId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public long countBeingFavorites(String entryId , String type) throws Exception;
	
	/**
	 * 获取词条被收藏次数
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public long countBeingFavorite(String entryId) throws Exception;
	
	/**
	 * 获取词条被收藏次数
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public long countBeingShare(String entryId) throws Exception;
	
	/**
	 * 分页查询用户个人收藏
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> getPersonalFavorites(int page,int lines,String userId) throws Exception;
	
	/**
	 * 根据修改人查询词条贡献
	 * @param page
	 * @param lines
	 * @param author
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByAuthor(int page , int lines ,String author,String state) throws Exception;
	
	
	/** 
	 * 分页查询用户个人分享 
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> getPersonalShares(int page,int lines,String userId) throws Exception;
	
	/**
	 * 查询用户词条相关联的信息
	 * @param page
	 * @param lines
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByUserIdAndType(int page,int lines,String userId,String type) throws Exception;
	
	/**
	 * 收费已收藏
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
	 * 判断是否管理员
	 * @param userId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public boolean isPublicDiskAdmin(String userId,int r_level) throws Exception ;

	public DataPackage<Entry> getNotThroungh(int page,int lines,String userId) throws Exception;
}
