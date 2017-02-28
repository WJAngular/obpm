package cn.myapps.km.baike.user.ejb;
import cn.myapps.km.baike.entry.dao.EntryDao;
import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.user.dao.BUserEntrySetDAO;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
/**
 * 
 * @author dragon
 * BUserEntrySet  用户收藏词条
 * 掉用DAO层，实现业务
 */
public class BUserEntrySetProcessBean extends  AbstractRunTimeProcessBean<BUserEntrySet> implements BUserEntrySetProcess{
	private static final long serialVersionUID = -979509233585133828L;
	/**
	 * 得到DAO的依赖
	 */
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		NRuntimeDAO dao =  BDaoManager.getBUserEntrySetDAO(getConnection());
		return dao;
	}
	
	//统计我的收藏次数
	public long countFavoriteEntry(String userId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).countFavoriteEntry(userId);
	}
	
	//统计我的分享次数
	public long countSharedEntry(String userId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).countSharedEntry(userId);
	}
	
	//我的收藏
	public DataPackage<Entry> queryFavoriteAll(int page, int lines,
			String userId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).getPersonalFavorites(page, lines, userId);
				
	}
	
	//我的分享
	public DataPackage<Entry> getPersonalShares(int page, int lines,
			String userId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).getPersonalShares(page, lines, userId);
	}
	
	
	public DataPackage<Entry> queryByUserIdAndType(int page,
			int lines, String userId, String type) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).queryByUserIdAndType(page, lines, userId, type);
	}

	public long countEntryByUserId(String userId, String type) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).countEntryByUserId(userId, type);
	}

	public long countBeingFavorites(String entryId, String type)
			throws Exception {
		return ((BUserEntrySetDAO)getDAO()).countBeingFavorites(entryId, type);
	}

	public long countBeingFavorite(String entryId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).countBeingFavorite(entryId);
	}

	public long countBeingShare(String entryId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).countBeingShare(entryId);
	}
	
	public DataPackage<Entry> getPersonalFavorites(int page, int lines,
			String userId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).getPersonalFavorites(page, lines, userId);
	}
	
	public DataPackage<Entry> queryByAuthor(int page,int lines,String author,String state) throws Exception{
		return ((BUserEntrySetDAO)getDAO()).queryByAuthor(page, lines, author,state);
	}
	
	/**
	 * 
	 * 判断用户是否已经收藏这个词条
	 * @param entrySet
	 * @return
	 * @throws Exception
	 */
	 
	public boolean isFavoritesEntry(BUserEntrySet entrySet) throws Exception{
		return ((BUserEntrySetDAO)getDAO()).isFavoritesEntry(entrySet);
	}

	/**
	 * 词条收藏个数
	 */
	public int doFavoriteCount(String entryId) throws Exception {
		return ((BUserEntrySetDAO)getDAO()).doFavoriteCount(entryId);
	}
	
	public boolean isPublicDiskAdmin(String userId,int r_level) throws Exception{
		return ((BUserEntrySetDAO)getDAO()).isPublicDiskAdmin(userId, r_level);
	}
	
	public DataPackage<Entry> getNotThroungh(int page,int lines,String userId) throws Exception{
		return ((BUserEntrySetDAO)getDAO()).getNotThroungh(page, lines, userId);
	}
}
	
