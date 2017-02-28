package cn.myapps.km.baike.content.ejb;




import java.util.Collection;
import java.util.Date;

import cn.myapps.km.baike.content.dao.AbstractEntryContentDAO;
import cn.myapps.km.baike.content.dao.EntryContentDAO;
import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.reason.dao.RejectReasonDao;
import cn.myapps.km.baike.reason.ejb.RejectReason;
import cn.myapps.km.baike.search.BKSearchEngine;
import cn.myapps.km.baike.user.dao.BUserAttributeDAO;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.Sequence;



/**
 * @author Abel
 * 词条内容ejb层的实现类
 */
public class EntryContentProcessBean extends AbstractRunTimeProcessBean<EntryContent> implements EntryContentProcess{

	
	private static final long serialVersionUID = 2788287724926400503L;
	
	

	@Override
	public void doCreate(NObject vo) throws Exception {
		EntryContent content = (EntryContent)vo;
		if (content.getId() == null || content.getId().trim().length() == 0) {
			content.setId(Sequence.getSequence());
		}
		content.setSaveTime(new Date());
		content.setState(EntryContent.STATE_DRAFT);
		content.setVersionNum(((AbstractEntryContentDAO)getDAO()).getVersionNum(content.getEntryId()));
		
		super.doCreate(content);
	}
	
	/**
	 * 当前用户创建词条版本
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception {
		EntryContent content = (EntryContent)vo;
		if (content.getId() == null || content.getId().trim().length() == 0) {
			content.setId(Sequence.getSequence());
		}
		content.setSaveTime(new Date());
		content.setAuthor(new BUser(user));
		content.setState(EntryContent.STATE_DRAFT);
		content.setVersionNum(((AbstractEntryContentDAO)getDAO()).getVersionNum(content.getEntryId()));
		((EntryContentDAO)getDAO()).removeDraft(content.getEntryId());
		super.doCreate(content);
		
	}
		
	/**
	 * 提交词条版本
	 * @param contentId
	 * @throws Exception
	 */
	public void doSubmmit(NObject content) throws Exception {
		try	{
			beginTransaction();
			((EntryContentDAO)getDAO()).submmitVersion(content);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	
	/**
	 * 提交词条版本
	 * @param contentId
	 * @throws Exception
	 */
	public void doSubmmit(NObject vo,NUser user) throws Exception {
		try	{
			beginTransaction();
			EntryContent newContent = (EntryContent)vo;
			newContent.setAuthor(new BUser(user));
			if (newContent.getId() == null || newContent.getId().trim().length() == 0) {
				newContent.setId(Sequence.getSequence());
			}
			newContent.setVersionNum(((AbstractEntryContentDAO)getDAO()).getVersionNum(newContent.getEntryId()));
			((EntryContentDAO)getDAO()).submmitVersion(newContent);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	
	/**
	 * 创建或提交词条版本
	 * @param vo
	 * 			词条内容
	 * @param user
	 * 			用户
	 * @throws Exception
	 */
	public void doCreateOrSubmmit(NObject vo, NUser user) throws Exception {
		EntryContent content = (EntryContent) this.doView(vo.getId());
		if (content != null) {
			doSubmmit(vo,user);
			//super.doUpdate((EntryContent) vo);
		} else {
			EntryContent newContent = (EntryContent)vo;
			newContent.setAuthor(new BUser(user));
			if (newContent.getId() == null || newContent.getId().trim().length() == 0) {
				newContent.setId(Sequence.getSequence());
			}
			newContent.setSaveTime(new Date());
			newContent.setSubmmitTime(new Date());
			newContent.setState(EntryContent.STATE_SUBMITTED);
			newContent.setVersionNum(((AbstractEntryContentDAO)getDAO()).getVersionNum(newContent.getEntryId()));
			((EntryContentDAO)getDAO()).removeDraft(newContent.getEntryId());
			super.doCreate(newContent);

			
		}
	}
	
	/**
	 * 当前用户创建或修改词条内容
	 * @param vo
	 * 			词条
	 * @param user
	 * 			当前用户
	 * @throws Exception
	 */
	public void doCreateOrUpdate(NObject vo, NUser user) throws Exception {
		EntryContent content = (EntryContent) this.doView(vo.getId());
		if (content != null) {
			doUpdate(vo, user);
		} else {
			EntryContent newContent = (EntryContent)vo;
			newContent.setAuthor(new BUser(user));
			if (newContent.getId() == null || newContent.getId().trim().length() == 0) {
				newContent.setId(Sequence.getSequence());
			}
			newContent.setSaveTime(new Date());
			newContent.setState(EntryContent.STATE_DRAFT);
			newContent.setVersionNum(((AbstractEntryContentDAO)getDAO()).getVersionNum(newContent.getEntryId()));
			super.doCreate(newContent);
		}
	}
	
	/**
	 * 驳回词条修改
	 * @param contentId
	 * @throws Exception
	 */
	public void doReject(String contentId,String reason) throws Exception {
		try	{
			beginTransaction();
				((EntryContentDAO)getDAO()).rejectVersion(contentId);
				RejectReasonDao rejectReasonDao = (RejectReasonDao)BDaoManager.getReasonDAO(getConnection());
				RejectReason rejectReason = new RejectReason();
				rejectReason.setId(Sequence.getSequence());
				rejectReason.setContentId(contentId);
				rejectReason.setReason(reason);
				rejectReason.setRejectTime(new Date());
				rejectReasonDao.create(rejectReason);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	

	/**
	 * 提交词条版本
	 * @param contentId
	 * @throws Exception
	 */
	public void doSubmit(String contentId) throws Exception {
		try	{
			beginTransaction();
			((EntryContentDAO)getDAO()).submitVersion(contentId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	
	/**
	 * 审核通过词条版本修改
	 * @param contentId
	 * @throws Exception
	 */
	public void doApprove(String contentId, String indexRealPath) throws Exception {
		try	{
			beginTransaction();
			EntryContentDAO contentDAO = (EntryContentDAO) getDAO();
			contentDAO.approveVersion(contentId);
			EntryContent content = (EntryContent) contentDAO.find(contentId);
			
			String userId = content.getAuthor().getId();
		
			Integer versionNum = content.getVersionNum();
			BUserAttributeDAO attributeDao = (BUserAttributeDAO)BDaoManager.getBUserAttributeDAO(getConnection());
			BUserAttribute attribute = attributeDao.findByUserId(userId);
		
		if(versionNum <= 1){
		
			if (attribute == null) {
				attribute = new BUserAttribute();
				attribute.setUserId(userId);
				attribute.setId(Sequence.getSequence());
				attribute.setIntegral(5);
				attributeDao.create(attribute);
			} else {
				attributeDao.addPoint(userId,5);
			}
		}else{
			if (attribute == null) {
				attribute = new BUserAttribute();
				attribute.setUserId(userId);
				attribute.setId(Sequence.getSequence());
				attribute.setIntegral(1);
				attributeDao.create(attribute);
			} else {
				attributeDao.addPoint(userId,1);
			}
			
		}	
			Entry entry = content.getEntry();
			BKSearchEngine.createIndex(entry.getName(), entry.getKeyWord(), content, indexRealPath, true);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * 获取词条待办列表
	 * @param page
	 * 			页码
	 * @param lines
	 * 			每页显示记录总数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> doPendingList(int page, int lines) throws Exception {
		return ((EntryContentDAO)getDAO()).queryByState(EntryContent.STATE_SUBMITTED, page, lines);	
	}
	
	@Override
	public void doUpdate(NObject vo) throws Exception {
		EntryContent content = (EntryContent)vo;
		content.setSaveTime(new Date());
		content.setVersionNum(((AbstractEntryContentDAO)getDAO()).getVersionNum(content.getEntryId()));
		super.doUpdate(vo);
	}
	
	/**
	 * 修改词条
	 * @param contentId
	 * @throws Exception
	 */
	public void doUpdate(NObject vo , NUser user) throws Exception {
		EntryContent content = (EntryContent)vo;
		content.setSaveTime(new Date());
		content.setAuthor(new BUser(user));
		content.setState(EntryContent.STATE_DRAFT);
		content.setVersionNum(((AbstractEntryContentDAO)getDAO()).getVersionNum(content.getEntryId()));
		super.doUpdate(vo);
	}

	/**
	 * 查询所有未审核版本
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<EntryContent> querySubmmittedContent(int page, int lines) throws Exception {
		return ((EntryContentDAO)getDAO()).queryByState(EntryContent.STATE_SUBMITTED, page, lines);
		
	}
	
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		// TODO Auto-generated method stub
		return BDaoManager.getEntryContentDAO(getConnection());
	}

	/**
	 * 通过entryId查询内容
	 */
	public DataPackage<EntryContent> queryByEntryId(String entryId,int page, int lines
			) throws Exception {
		return (DataPackage<EntryContent>) ((EntryContentDAO)getDAO()).queryByEntryId(entryId,page,lines);
	}
	
	/**
	 * 获取草稿状态的内容
	 */
	public DataPackage<EntryContent> getUserDraftContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_DRAFT, page, lines);
	}
	
	/**
	 * 获取通过状态的内容
	 */
	public DataPackage<EntryContent> getUserPassedContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_PASSED, page, lines);
	}
	
	/**
	 * 根据categoryId查询已通过内容
	 */
	public DataPackage<EntryContent> getUserPassedContent(String categoryId, String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(categoryId, userId, EntryContent.STATE_PASSED, page, lines);
	}
	
	/**
	 * 获取提交状态的内容
	 */
	public DataPackage<EntryContent> getUserSubmittedContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_SUBMITTED, page, lines);
	}
	
	/**
	 * 获取驳回状态的内容
	 */
	public DataPackage<EntryContent> getUserRejectContent(String userId, int page, int lines) throws Exception{
		return  queryByUserIdAndState(userId, EntryContent.STATE_REJECT, page, lines);
	}
	
	/**
	 * 通过当前用户和状态查找内容
	 */
	public DataPackage<EntryContent> queryByUserIdAndState( NUser user, String state, int page, int lines) throws Exception {
		return  queryByUserIdAndState(user.getId(), state, page, lines);
	}
	
	/**
	 * 通过 UserId和State查询内容
	 */
	public DataPackage<EntryContent> queryByUserIdAndState(String userId, String state, int page, int lines) throws Exception{
		return (DataPackage<EntryContent>) ((EntryContentDAO)getDAO()).queryByUserIdAndState(userId, state, page, lines);
	}

	/**
	 * 通过categiryId, UserId和State查询内容
	 */
	public DataPackage<EntryContent> queryByUserIdAndState(String categoryId,String userId, String state, int page, int lines) throws Exception{
		return (DataPackage<EntryContent>) ((EntryContentDAO)getDAO()).queryByUserIdAndState(categoryId,userId, state, page, lines);
	}
		
	/**
	 * 获取词条最新版本内容
	 */
	public EntryContent getLatestVersionContent(String entryId)
			throws Exception {
		return (EntryContent) ((EntryContentDAO)getDAO()).getLatestVersionContent(entryId);
	}
	public EntryContent getContent(String entryId) throws Exception{
		return (EntryContent) ((EntryContentDAO)getDAO()).getContent(entryId);
	}
	
	/**
	 * 获取词条历史版本内容
	 */
	public DataPackage<EntryContent> getHisVersionContent(String entryId, int page, int lines) throws Exception{
		return (DataPackage<EntryContent>) ((EntryContentDAO)getDAO()).getHisVersionContent(entryId, page, lines);
	}

	/**
	 * 通过词条ID查找已通过内容
	 */
	public Collection<EntryContent> queryByEntryIdAndPassedContent(String entryId)
			throws Exception {
		return (Collection<EntryContent>) ((EntryContentDAO)getDAO()).queryByEntryIdAndPassedContent(entryId);
	}

	/**
	 * 通过词条id删除词条内容
	 * @param entryId
	 * 			词条ID
	 * @throws Exception
	 */
	public void doRemoveByEntryId(String entryId) throws Exception {
		try {
			beginTransaction();
			((EntryContentDAO)getDAO()).removeByEntryId(entryId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
		
	}
	
	/**
	 * 查询词条
	 * @param queryString
	 * @param indexPath
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<EntryContent> doQuery(String queryString, String indexPath, int page, int lines) throws Exception {
		return BKSearchEngine.search(queryString, indexPath,  lines, page);
	}


	/**
	 * 查询词条版本
	 * @param contentId
	 * @throws Exception
	 */
	public DataPackage<EntryContent> queryContentHistory( String entryId, String contentId, int page, int lines) throws Exception{
		return (DataPackage<EntryContent>)  ((EntryContentDAO)getDAO()).queryContentHistory(entryId, contentId, page, lines);
	}
	

	/**
	 * 删除草稿箱
	 * @param contentId
	 * @throws Exception
	 */
	public void removeDraft(String entryId) throws Exception{
		try	{
			beginTransaction();
			((EntryContentDAO)getDAO()).removeDraft(entryId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
}
