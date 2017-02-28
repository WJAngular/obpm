package cn.myapps.km.baike.entry.ejb;


import java.util.Collection;
import java.util.Map;


import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;

/**
 * 
 * @author Allen
 *	entry 的process层的接口
 */
public interface EntryProcess extends  NRunTimeProcess<Entry>{
	
	/**
	 * 当前用户创建词条
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception ;
	
	/**
	 * 通过词条Id和名称修改 
	 * @param entryId
	 * @param name
	 * @throws Exception
	 */
	public void doModifyEntryName(String entryId,String name, String categoryId) throws Exception;
	
	/**
	 * 词条名称类型修改 
	 * @param entryId
	 * @param name
	 * @throws Exception
	 */
	public void doModifyEntryNameAndCategory(String entryId, String name, String categoryId) throws Exception;
	/**
	 * 当前用户创建词条并增加积分
	 * @param vo
	 * @param point
	 * @param user
	 * @throws Exception
	 */
	public void doCreateAndAddPoint(NObject vo, int point, NUser user) throws Exception ;
	
	/**
	 * 通过词条名称查找
	 * @param name
	 * 			词条名称
	 * @return
	 * @throws Exception
	 */
	public Entry doFindByName(String name) throws Exception;
	
	/**
	 * 通过词条id以及对应版本内容id查找词条
	 * @param entryId
	 * @param contentId
	 * @param userId 
	 * @return
	 * @throws Exception
	 */
	public Entry doView(String entryId, String contentId, String userId) throws Exception;
	
	
	/**
	 * 根据词条名称查询
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Collection<Entry> doQueryEntryByName(String name) throws Exception;
	
	/**
	 * 根据词条名称分页查询
	 * @param page
	 * @param lines
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doQueryEntryByName(int page,int lines,  String name) throws Exception;
	
	/**
	 * 根据关键字查询
	 * @param lable
	 * @return
	 * @throws Exception
	 */
	public Collection<Entry> doQueryByKeyWord(String keyword) throws Exception;
	
	/**
	 * 通过创建时间查询贡献者
	 * @param categoryId
	 * @param userId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryContributorByCreated(String categoryId, String userId,int page,int lines) throws Exception;
	
	/**
	 * 根据关键字分页查询
	 */
	public DataPackage<Entry> doQueryByKeyWord(int page,int lines,String lable) throws Exception;
	
	/**
	 * 查询热点词条
	 */
	public DataPackage<Entry> doQueryHotEntry(int page,int lines) throws Exception; 
	
	/**
	 * 根据词条类别查找该分类下的热点词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doQueryHotEntry(String categoryId, int page,int lines) throws Exception; 
	
	/**
	 * 给词条排序
	 */
	public DataPackage<Entry> doOrderEntryByPoints(int page,int lines) throws Exception;
	/**
	 * 查询最近词条
	 */
	public DataPackage<Entry> doQueryRecent(int page,int lines) throws Exception;
	
	/**
	 * 查找指定分类下的最新词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doQueryRecent(String categoryId, int page,int lines) throws Exception;
	
	/**
	 * 增加浏览次数
	 * @param entryId
	 * 				词条ID
	 * @throws Exception
	 */
	public void doAddBrowserCount(String entryId) throws Exception;
	
	/**
	 * 根据CategoryId查询词条,即根据分类查询
	 */
	public DataPackage<Entry> doQueryByCategoryId(int page,int lines,String CategoryId) throws Exception;
	
	/**
	 * 查找所有修改者id
	 */
	//public Collection<String> doQueryAttributerByEntryId(String entryId) throws Exception;
	
	/**
	 * 查找该词条下的版本
	 */
	public DataPackage<EntryContent> doQueryAttributerByEntryId(int page,int lines,String entryId) throws Exception;
	
	/**
	 * 根据创建者查找词条(我的词条)
	 * @param page
	 * @param lines
	 * @param user
	 * 			当前用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> dofindEntryByUserId(int page, int lines ,NUser user) throws Exception;
	
	/**
	 * 查询我的草稿
	 * @param page
	 * @param lines
	 * @param user
	 * 			当前用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doGetMyDraft(int page, int lines ,NUser user) throws Exception;
	
	/**
	 * 查询所有词条
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doQueryAllEntry(int page, int lines) throws Exception;
	
	/**
	 * 为词条投票，增加词条积分
	 * @param entryId
	 * 			词条ID
	 * @throws Exception
	 */
	public void doVote(String entryId) throws Exception ;
	
	/**
	 * 查找贡献者
	 * @param entryId
	 * @return 
	 * @throws Exception
	 */
	public  Collection<Entry> doQueryContributer(String entryId) throws Exception;
	
	/**
	 * 通过用户ID查询用户信息
	 * @param userId
	 * @param page
	 * @param lines
	 * @throws Exception
	 */
	public DataPackage<Entry> doFindUserCenterByUserId(String userId,int page,int lines) throws Exception;
	
	/**
	 * 分页查询部门积分榜
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Map> getScoreboardByDepartment(int page, int lines) throws Exception;
	
	/**
	 * 查询已通过词条
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryPasseedEntry(int page, int lines) throws Exception;
	
	/**
	 * 查询草稿词条
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryDraftEntry(int page, int lines,String userId) throws Exception;

	/**
	 * 获取修改积分
	 * @param userId
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public int getEditPoints(String userId,String entryId) throws Exception;
	
	/**
	 * 根据用户及状态查找词条.
	 */
	public DataPackage<Entry> findPassedByUserId(int page, int lines,String userId) throws Exception;
	
	/**
	 * 分页查询词条积分
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Map> getScoreboardByEntry(int page, int lines,String userId) throws Exception ;
	
	/**
	 * 查询所有词条个数
	 * @return
	 * @throws Exception
	 */
	public int getEntryCounts() throws Exception;
	
	/**
	 * 浏览次数
	 * @return
	 * @throws Exception
	 */
	public int getReadCounts() throws Exception;

}
