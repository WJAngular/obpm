package cn.myapps.km.baike.entry.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.baike.content.dao.EntryContentDAO;
import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.content.ejb.EntryContentProcessBean;
import cn.myapps.km.baike.entry.dao.EntryDao;
import cn.myapps.km.baike.history.dao.HistoryDao;
import cn.myapps.km.baike.history.ejb.History;
import cn.myapps.km.baike.user.dao.BUserAttributeDAO;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

/**
 * @author Allen
 * process层，实现业务
 */
public class EntryProcessBean extends AbstractRunTimeProcessBean<Entry> implements EntryProcess{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -160006938248239439L;
	
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		NRuntimeDAO dao =  BDaoManager.getEntryDAO(getConnection());
		return dao;
	}
	
	/**
	 * 当前用户创建词条
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception {
		if (vo instanceof Entry) {
			try {
				if (vo.getId() == null || vo.getId().trim().length() == 0) {
					vo.setId(Sequence.getSequence());
				}
				Entry entry = (Entry)vo;
				entry.setDomainId(user.getDomainid());
				entry.setAuthor(new BUser(user));
				entry.setCreated(new Date());
				beginTransaction();
				getDAO().create(entry);
				commitTransaction();
			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}
		}
	}
	
	/**
	 * 修改词条名称
	 */
	public void doModifyEntryName(String entryId, String name, String categoryId) throws Exception {
		try {
			Entry entry= (Entry) this.doView(entryId);
			//entry可能为空
			entry.setName(name);
			super.doUpdate(entry);
		} catch (Exception e) {
			throw e;
		}	
	}
	
	/**
	 * 修改词条名称和类型
	 */
	public void doModifyEntryNameAndCategory(String entryId, String name, String categoryId) throws Exception {
		try {
			Entry entry= (Entry) this.doView(entryId);
			//entry可能为空
			entry.setName(name);
			entry.setCategoryId(categoryId);
			super.doUpdate(entry);
		} catch (Exception e) {
			throw e;
		}	
	}
	
	/**
	 * 当前用户创建词条并增加积分
	 * @param vo
	 * @param point
	 * @param user
	 * @throws Exception
	 */
	public void doCreateAndAddPoint(NObject vo, int point, NUser user) throws Exception {
		if (vo instanceof Entry) {
			try {
				if (vo.getId() == null || vo.getId().trim().length() == 0) {
					vo.setId(Sequence.getSequence());
				}
				Entry entry = (Entry)vo;
				entry.setDomainId(user.getDomainid());
				entry.setAuthor(new BUser(user));
				entry.setCreated(new Date());
				BUserAttributeDAO attributeDao = (BUserAttributeDAO)BDaoManager.getBUserAttributeDAO(getConnection());
				BUserAttribute attribute = attributeDao.findByUserId(user.getId());
				
				beginTransaction();
				getDAO().create(entry);

				EntryContentDAO entryContentDao = (EntryContentDAO)BDaoManager.getEntryContentDAO(getConnection());
				EntryContent content = new EntryContent();
				content.setId(Sequence.getSequence());
				content.setEntryId(entry.getId());
				content.setState(content.STATE_DRAFT);
				content.setAuthor(entry.getAuthor().getId());
				content.setSaveTime(new Date());
				entryContentDao.create(content);
				if (attribute == null) {
					attribute = new BUserAttribute();
					attribute.setUserId(user.getId());
					attribute.setId(Sequence.getSequence());
					attribute.setIntegral(point);
					attributeDao.create(attribute);
				} else {
					attributeDao.addPoint(user.getId(), point);
				}
				commitTransaction();
			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}
		}
	}
	
	@Override
	public void doCreate(NObject vo) throws Exception {
		Entry entry=(Entry)vo;
		try {
			if (entry.getId() == null || entry.getId().trim().length() == 0) {
				entry.setId(Sequence.getSequence());
			}
			if(entry.getCreated()==null){
				entry.setCreated(new Date());
			}
			beginTransaction();
			getDAO().create(entry);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	
	
	/**
	 * 通过词条名称查找
	 * @param name
	 * 			词条名称
	 * @return
	 * @throws Exception
	 */
	public Entry doFindByName(String name) throws Exception {
		return ((EntryDao)getDAO()).findByName(name);
	}
	
	
	/**
	 * 通过词条id以及对应版本内容id查找词条
	 * @param entryId
	 * @param contentId
	 * @return
	 * @throws Exception
	 */
	public Entry doView(String entryId, String contentId,String userId) throws Exception {
		Entry entry = (Entry) getDAO().find(entryId);
		if (entry != null) {
			
			EntryContentDAO contentDao = (EntryContentDAO) BDaoManager.getEntryContentDAO(getConnection());
			if (contentId == null || contentId.trim().length()<=0) {
				entry.setContent(contentDao.getLatestVersionContent(entryId));
			} else {
				entry.setContent((EntryContent)contentDao.find(contentId));
			}
			History history = new History();
			HistoryDao historyDao = (HistoryDao)BDaoManager.getHistoryDAO(getConnection());
			History hi = historyDao.queryByEntryId(entryId,userId);
			if(hi == null){
				history.setId(Sequence.getSequence());
				history.setAuthor(userId);
				history.setEntryId(entryId);
				history.setReadTime(new Date());
				history.setEntryName(entry.getName());
				historyDao.create(history);
			}else{
				
			}
		}
		return entry;
	}

	
	
	/**
	 * 根据名字查询词条
	 */
	public DataPackage<Entry> doQueryEntryByName(int page, int lines, String name)throws Exception {
		
		return ((EntryDao)getDAO()).queryByName(page, lines, name);			
	}
	
	/**
	 * 查询热门词条
	 */
	public DataPackage<Entry> doQueryHotEntry(int page, int lines) throws Exception {
		
		return ((EntryDao)getDAO()).queryByBrowseCount(page, lines);
	}
	
	/**
	 * 根据词条类别查找该分类下的热点词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doQueryHotEntry(String categoryId, int page,int lines) throws Exception {
		return ((EntryDao)getDAO()).queryByBrowseCount(categoryId, page, lines);
	}
	
	/**
	 * 根据CategoryId查询,即根据类别查询
	 */
	public DataPackage<Entry> doQueryByCategoryId(int page, int lines,String CategoryId) throws Exception {
		
		return ((EntryDao)getDAO()).queryByCategoryId(page, lines, CategoryId);
	}
	/**
	 * 查询最近词条
	 */
	public DataPackage<Entry> doQueryRecent(int page, int lines) throws Exception {
		return ((EntryDao)getDAO()).queryByCreated(page, lines);
	}
	
	/**
	 * 查询贡献者
	 */
	public DataPackage<Entry> queryContributorByCreated(String categoryId,String userId,int page,int lines) throws Exception{
		return ((EntryDao)getDAO()).queryContributorByCreated(categoryId, userId, page, lines);
	}
	/**
	 * 查找指定分类下的最新词条
	 * @param categoryId
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doQueryRecent(String categoryId, int page,int lines) throws Exception {
		return ((EntryDao)getDAO()).queryByCreated(categoryId, page, lines);
	}
	
	/**
	 * 增加浏览次数
	 * @param entryId
	 * 				词条ID
	 * @throws Exception
	 */
	public void doAddBrowserCount(String entryId) throws Exception {
		try {
			beginTransaction();
			((EntryDao)getDAO()).addBrowserCount(entryId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	
	/**
	 *根据标签查询词条  
	 */
	public DataPackage<Entry> doQueryByKeyWord(int page, int lines, String lable)
			throws Exception {
		
		 return ((EntryDao)getDAO()).queryByName(page, lines, lable);
	}
	
	/**
	 *查询所有词条
	 */
	public DataPackage<Entry> doQueryAllEntry(int page, int lines)
			throws Exception {
		
		 return ((EntryDao)getDAO()).queryAllEntry(page, lines);
	}
	
	/**
	 * 根据名字查找
	 */
	public Collection<Entry> doQueryEntryByName(String name) throws Exception {
		
		 return ((EntryDao)getDAO()).queryByName(name);
	}
	
	/**
	 * 根据关键字查找
	 */
	public Collection<Entry> doQueryByKeyWord(String keyword) throws Exception {
		 return ((EntryDao)getDAO()).queryByKeyWord(keyword);
	}
	
	
	public DataPackage<EntryContent> doQueryAttributerByEntryId(int page,
			int lines, String entryId) throws Exception {
		EntryContentProcessBean process = new EntryContentProcessBean();
		DataPackage<EntryContent> contents = process.queryByEntryId(entryId, page, lines);
		return contents;
	}
	
	/**
	 * 根据用户Id查找词条
	 */
	public DataPackage<Entry> dofindEntryByUserId(int page, int lines, NUser user) throws Exception {
		return ((EntryDao)getDAO()).findByUserId(page, lines, user.getId());
	}
	
	/**
	 * 根据用户Id查找词条
	 */
	public DataPackage<Entry> dofindEntryByUserId(int page, int lines, String userId) throws Exception {
		return ((EntryDao)getDAO()).findByUserId(page, lines, userId);
	}
	
	/**
	 * 给词条按积分排序
	 */
	public DataPackage<Entry> doOrderEntryByPoints(int page, int lines)
			throws Exception {
		return  ((EntryDao)getDAO()).orderByPoint(page, lines);
	}
	
	/**
	 * 查询我的草稿
	 * @param page
	 * @param lines
	 * @param user
	 * 			当前用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Entry> doGetMyDraft(int page, int lines, NUser user)
			throws Exception {
		return ((EntryDao)getDAO()).queryByUserIdAndState(page, lines, user.getId(), EntryContent.STATE_DRAFT);
	}
	
	/**
	 * 为词条投票，增加词条积分
	 * @param entryId
	 * 			词条ID
	 * @throws Exception
	 */
	public void doVote(String entryId) throws Exception {
		try{
		Collection list = new ArrayList();
		((EntryDao)getDAO()).addPoint(entryId);
		EntryContentProcessBean process = new EntryContentProcessBean();
		Collection<EntryContent> contents=process.queryByEntryIdAndPassedContent(entryId);
		if(contents!=null){
			for(Iterator<EntryContent> it=contents.iterator();it.hasNext();){
				EntryContent content = it.next();
				String userId = content.getAuthor().getId();
				if(!list.contains(userId)){
					list.add(userId);
					BUserAttributeDAO attributeDao = (BUserAttributeDAO)BDaoManager.getBUserAttributeDAO(getConnection());
					BUserAttribute attribute = attributeDao.findByUserId(userId);
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
			}
	
		  }	
		}catch(Exception e){
			rollbackTransaction();
		}		
	}

	/**
	 * 查找贡献者
	 * @return 
	 */
	public  Collection<Entry> doQueryContributer(String entryId) throws Exception {
		return ((EntryDao)getDAO()).doFindContributer(entryId);
		
	}
	
	/**
	 * 通过userId查询用户个人积分
	 */
	public DataPackage<Entry> doFindUserCenterByUserId(String userId,int page,int lines) throws Exception{
		return ((EntryDao)getDAO()).doFindUserCenterByUserId(userId, page, lines);
	}

	/**
	 * 获取部门积分
	 */
	public DataPackage<Map> getScoreboardByDepartment(int page, int lines) throws Exception {
		
		Collection<BUserAttribute> buserAttribute = ((BUserAttributeDAO)BDaoManager.getBUserAttributeDAO(getConnection())).doFindAllUser();
		//Map<String,Object> map = new HashMap<String,Object>();
		List<Map> list = new ArrayList<Map>();
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		DepartmentProcess depProcess =  (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		for(Iterator<BUserAttribute> itr = buserAttribute.iterator(); itr.hasNext();){
			BUserAttribute ba = itr.next();
			String userid = ba.getUserId();
			UserVO user = (UserVO) userProcess.doView(userid);
			String departmentId = user.getDefaultDepartment();
			Integer integral = ba.getIntegral();
			boolean flag = true;
			for(int i=0;i<list.size();i++){
				Map<String, Object> depInfo = list.get(i);
				if (departmentId.equals(depInfo.get("ID"))) {
					depInfo.put("TOTALINTEGRAL", (Integer)depInfo.get("TOTALINTEGRAL") + integral);
					flag = false;
					break;
				}
			}
			if (flag) {
				Map<String, Object> depInfo = new HashMap<String, Object>();
				String depName = "";
				DepartmentVO dep = (DepartmentVO) depProcess.doView(departmentId);
				if (dep!=null) {
					depName = dep.getName();
				}
				
				depInfo.put("ID", departmentId);
				depInfo.put("NAME", depName);
				depInfo.put("TOTALINTEGRAL", integral);
				list.add(depInfo);
			}
			 
		}
        for(int i = 0;i<list.size()-1;i++){
            for(int j = i+1;j<list.size();j++){
            	 Map<String, Object> a;
            	 if (((Integer)list.get(i).get("TOTALINTEGRAL")) < ((Integer)list.get(j).get("TOTALINTEGRAL"))) {
                    a =   list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, a); 
                }
            }
		}
        DataPackage<Map> result = new DataPackage<Map>();
        result.rowCount = list.size();
        result.pageNo = page;
		result.linesPerPage = lines;
		//当页码大于总页数
		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}
		result.setDatas(list);
        return result;
	}
	
	/**
	 * 获取通过词条
	 */
	public DataPackage<Entry> queryPasseedEntry(int page, int lines) throws Exception{
		return ((EntryDao)getDAO()).queryPasseedEntry(page, lines);
	}
	
	/**
	 * 获取草稿箱
	 */
	public DataPackage<Entry> queryDraftEntry(int page, int lines,String userId) throws Exception{
		return ((EntryDao)getDAO()).queryDraftEntry(page, lines,userId);
	}
	
	/**
	 * 获取修改积分
	 */
	public int getEditPoints(String userId,String entryId) throws Exception{
		return ((EntryDao)getDAO()).getEditPoints(userId,entryId);
	}
	
	/**
	 * 根据用户及状态查找词条.
	 */
	public DataPackage<Entry> findPassedByUserId(int page, int lines,String userId) throws Exception{
		return ((EntryDao)getDAO()).findPassedByUserId(page, lines, userId);
	}
	
	/**
	 * 分页查询词条积分
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Map> getScoreboardByEntry(int page, int lines, String userId) throws Exception{
		return ((EntryDao)getDAO()).getScoreboardByEntry(page, lines, userId);
	}
	
	/**
	 * 查询所有词条个数
	 * @return
	 * @throws Exception
	 */
	public int getEntryCounts() throws Exception{
		return ((EntryDao)getDAO()).getEntryCounts();
	}
	
	/**
	 * 浏览次数
	 * @return
	 * @throws Exception
	 */
	public int getReadCounts() throws Exception{
		return ((EntryDao)getDAO()).getReadCounts();
	}
	

	
}