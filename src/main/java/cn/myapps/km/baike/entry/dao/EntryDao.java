package cn.myapps.km.baike.entry.dao;

import java.util.Collection;
import java.util.Map;

import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
/**
 * @author Allen
 * EntryDao 抽象接口
 * 
 */
public interface EntryDao extends NRuntimeDAO{
	
	/**
	 * 通过词条名称查找词条
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Entry findByName(String name) throws Exception;
	
	/**
	 * 增加词条浏览次数
	 * @param entryId
	 *			词条ID
	 * @throws Exception
	 */
	public void addBrowserCount(String entryId) throws Exception;
	
	/**
	 * 根据词条积分排行
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> orderByPoint(int page,int lines) throws Exception ;
	
	/**
	 * 查找最新词条
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByCreated(int page,int lines) throws Exception;
	
	/**
	 * 查询贡献者
	 * @param categoryId
	 * @param userId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryContributorByCreated(String categoryId, String userId,int page,int lines) throws Exception;
	
	/**
	 * 查找指定分类下的最新词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByCreated(String categoryId, int page,int lines) throws Exception;
	
	/**
	 * 分页查找热点词条
	 */
	public DataPackage<Entry> queryByBrowseCount(int page,int lines) throws Exception;
	
	/**
	 * 根据词条类别查找该类别下的热点词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByBrowseCount(String categoryId, int page,int lines) throws Exception;
	
	/**
	 * 根据名字查找词条
	 */
	public Collection<Entry> queryByName(String name) throws Exception;
	
	/**
	 * 根据名字分页查找词条
	 */
	public DataPackage<Entry> queryByName(int page,int lines,String name) throws Exception;
	
	/**
	 * 根据关键字查询词条
	 */
	public Collection<Entry> queryByKeyWord(String name) throws Exception;
	
	/**
	 * 根据关键字分页查询词条
	 */
	public DataPackage<Entry> queryByKeyWord(int page,int lines,String name) throws Exception;
		
	/**
	 * 根据分类查找词条
	 * @param page
	 * @param lines
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByCategoryId(int page,int lines,String CategoryId) throws Exception;

	/**
	 * 根据用户Id(创建者)查询词条
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> findByUserId(int page,int lines ,String userId)throws Exception;
	
	/**
	 * 根据用户ID查询与状态查询词条
	 * @param page
	 * @param lines
	 * @param userId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryByUserIdAndState(int page, int lines, String userId, String state) throws Exception;
	
	/**
	 * 增加词条积分，加一分
	 * @param pk
	 * @throws Exception
	 */
	public void addPoint(String pk) throws Exception;
	
	/**
	 * 增加词条积分
	 * @param pk
	 * 			词条ID
	 * @param point
	 * 			增加积分
	 * @throws Expception
	 */
	public void addPoint(String pk, int point) throws Exception;
	
	/**
	 * 查询所有词条
	 * @param page
	 * @param lines
	 * @param userId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryAllEntry(int page, int lines) throws Exception;
	
	/**
	 * 查找贡献者
	 * @param entryId
	 * @throws Exception
	 */
	public  Collection<Entry> doFindContributer(String entryId) throws Exception;
	
	/**
	 * 通过用户ID查找用户中心
	 * @param userId
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
	 * 查询年通过了的词条
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryPasseedEntry(int page, int lines) throws Exception;
	
	/**
	 * 查询草稿状态的词条
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> queryDraftEntry(int page, int lines,String userId) throws Exception;
	
	/**
	 * 索引词条
	 * @param sql
	 * @param params
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Map> query(String sql, ParamsTable params, int page, int lines) throws Exception;
	
	/**
	 * 获取修改积分
	 * @param userId
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
