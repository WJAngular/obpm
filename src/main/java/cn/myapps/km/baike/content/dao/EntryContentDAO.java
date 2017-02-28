package cn.myapps.km.baike.content.dao;


import java.util.Collection;
import java.util.Map;

import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;


/**
 * 
 * @author Abel
 *词条内容DAO层接口
 */
public interface EntryContentDAO extends NRuntimeDAO{	
	
	/**
	 * 通过词条ID删除词条内容
	 * @param entryId
	 * 			词条ID
	 * @throws Exception
	 */
	public void removeByEntryId(String entryId) throws Exception;
	
	/**
	 * 提交词条版本
	 * @param contentId
	 * 				词条版本ID
	 * @throws Exception
	 */
	public void submmitVersion(NObject content) throws Exception;
	
	/**
	 * 驳回版本
	 * @param contentId
	 * 				词条版本ID
	 * @throws Exception
	 */
	public void rejectVersion(String contentId) throws Exception;
	
	/**
	 * 提交版本
	 * @param contentId
	 * @throws Exception
	 */
	public void submitVersion(String contentId) throws Exception;
	
	/**
	 * 通过entryId获取内容
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public EntryContent getContent(String entryId) throws Exception;
	
	/**
	 * 审核版本
	 * @param contentId
	 * 				词条版本ID
	 * @throws Exception
	 */
	public void approveVersion(String contentId) throws Exception;
	
	
	/**
	 * 通过EntryId查找内容接口
	 */
	public  DataPackage<EntryContent> queryByEntryId(String entryId,int page, int lines) throws Exception;
	
	/**
	 * 通过当前用户和状态查找内容接口
	 */
	public DataPackage<EntryContent> queryByUserIdAndState( NUser user, String state, int page, int lines) throws Exception;

	/**
	 * 通过UserId和state查找内容接口
	 */
	public DataPackage<EntryContent> queryByUserIdAndState(String userId, String state, int page, int lines) throws Exception;

	/**
	 * 通过categiryId,UserId和state查找内容接口
	 */
	public DataPackage<EntryContent> queryByUserIdAndState(String categoryId, String userId, String state, int page, int lines) throws Exception;
	
	/**
	 * 获取词条最新版本内容
	 */
	public EntryContent getLatestVersionContent(String entryId) throws Exception;
	
	/**
	 * 获取词条历史版本内容
	 */
	public DataPackage<EntryContent> getHisVersionContent(String entryId, int page, int lines) throws Exception;
	
	/**
	 * 获取草稿状态的内容
	 */
	public DataPackage<EntryContent> getUserDraftContent(String userId, int page, int lines) throws Exception;

	/**
	 * 获取通过状态的内容
	 */
	public DataPackage<EntryContent> getUserPassedContent(String userId, int page, int lines) throws Exception;
	
	/**
	 * 获取提交状态的内容
	 */
	public DataPackage<EntryContent> getUserSubmittedContent(String userId, int page, int lines) throws Exception;


	/**
	 * 获取驳回状态的内容
	 */
	public DataPackage<EntryContent> getUserRejectContent(String userId, int page, int lines) throws Exception;
	
	/**
	 * 通过Id获取通过状态的版本内容
	 */
	public Collection<EntryContent> queryByEntryIdAndPassedContent(String entryId) throws Exception;
	
	/**
	 * 通过词条状态查询词条版本
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> queryByState(String state, int page, int lines) throws Exception;

	/**
	 *查询词条内容 
	 */
	public DataPackage<Map> query(String sql, ParamsTable params, int page, int lines) throws Exception;
	
	/**
	 * 查询内容版本
	 */
	public DataPackage<EntryContent> queryContentHistory( String entryId, String contentId, int page, int lines) throws Exception;
	
	/**
	 * 删除草稿
	 */
	public void removeDraft(String entryId) throws Exception;
}
