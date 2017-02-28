package cn.myapps.km.baike.content.ejb;



import java.util.Collection;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;


/**
 * @author Abel
  */
public interface EntryContentProcess  extends NRunTimeProcess<EntryContent>{
	
	/**
	 * 当前用户创建词条版本
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception;
	
	/**
	 * 获取内容
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public EntryContent getContent(String entryId) throws Exception;
	
	/**
	 * 当前用户修改词条内容
	 * @param vo
	 * 			词条
	 * @param user
	 * 			当前用户
	 * @throws Exception
	 */
	public void doUpdate(NObject vo, NUser user) throws Exception;
	
	/**
	 * 当前用户创建或修改词条内容
	 * @param vo
	 * 			词条
	 * @param user
	 * 			当前用户
	 * @throws Exception
	 */
	public void doCreateOrUpdate(NObject vo, NUser user) throws Exception;
	
	/**
	 * 提交词条版本
	 * @param contentId
	 * @throws Exception
	 */
	public void doSubmmit(NObject content) throws Exception;
	
	/**
	 * 当前用户提交词条版本
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doSubmmit(NObject vo,NUser user) throws Exception;
	
	/**
	 * 创建或提交词条版本
	 * @param vo
	 * 			词条内容
	 * @param user
	 * 			用户
	 * @throws Exception
	 */
	public void doCreateOrSubmmit(NObject vo, NUser user) throws Exception;
	
	/**
	 * 驳回词条修改
	 * @param contentId
	 * @throws Exception
	 */
	public void doReject(String contentId,String reason) throws Exception;
	
	/**
	 * 提交内容
	 * @param contentId
	 * @throws Exception
	 */
	public void doSubmit(String contentId) throws Exception;
	
	/**
	 * 审核通过词条版本修改
	 * @param contentId
	 * @throws Exception
	 */
	public void doApprove(String contentId, String indexRealPath) throws Exception;
	
	/**
	 * 获取词条待办列表
	 * @param page
	 * 			页码
	 * @param lines
	 * 			每页显示记录总数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> doPendingList(int page, int lines) throws Exception;
	
	/**
	 * 查询所有未审核版本
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> querySubmmittedContent(int page, int lines) throws Exception;
	
	/**
	 * 通过词条id删除词条内容
	 * @param entryId
	 * 			词条ID
	 * @throws Exception
	 */
	public void doRemoveByEntryId(String entryId) throws Exception;

	/**
	 * 通过EntryId查找内容接口
	 */
	public DataPackage<EntryContent> queryByEntryId(String entryId,int page, int rowCount) throws Exception;
	
	/**
	 * 通过当前用户和状态查找内容接口
	 */
	public DataPackage<EntryContent> queryByUserIdAndState( NUser user, String state, int page, int lines) throws Exception;

	/**
	 * 通过UserId和state查找内容接口
	 */
	public DataPackage<EntryContent> queryByUserIdAndState(String userId, String state, int page, int rowCount) throws Exception;
	
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
	 * 根据categoryId查询贡献
	 * @param categoryId
	 * @param userId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> getUserPassedContent(String categoryId, String userId, int page, int lines) throws Exception;

	/**
	 * 获取提交状态的内容
	 */
	public DataPackage<EntryContent> getUserSubmittedContent(String userId, int page, int lines) throws Exception;

	/**
	 * 查询用户未通过版本
	 * @param user
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> getUserRejectContent(String userId, int page, int lines) throws Exception;
	
	/**
	 * 通过Id获取通过状态的版本内容
	 */
	public Collection<EntryContent> queryByEntryIdAndPassedContent(String entryId) throws Exception;
	
	/**
	 * 查询词条
	 * @param queryString
	 * @param indexPath
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<EntryContent> doQuery(String queryString, String indexPath, int page, int lines) throws Exception;
	
	/**
	 * 获取内容历史版本
	 * @param entryId
	 * @param contentId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> queryContentHistory( String entryId, String contentId, int page, int lines) throws Exception;
	
	/**
	 * 删除草稿箱
	 * @param entryId
	 * @throws Exception
	 */
	public void removeDraft(String entryId) throws Exception;
	}