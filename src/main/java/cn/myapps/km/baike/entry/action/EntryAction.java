package cn.myapps.km.baike.entry.action;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.category.ejb.Category;
import cn.myapps.km.baike.category.ejb.CategoryProcessBean;
import cn.myapps.km.baike.content.ejb.EntryContentProcessBean;
import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.entry.ejb.EntryProcess;
import cn.myapps.km.baike.entry.ejb.EntryProcessBean;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestion;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestionProcessBean;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.baike.user.ejb.BUserEntrySetProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author allen 词条的基本操作
 * 
 */
public class EntryAction extends AbstractRunTimeAction<Entry> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -77043527630120999L;

	/**
	 * 页码
	 */
	private int page;
	/**
	 * 页数
	 */
	private int lines;
	
	/**
	 * 用户Id
	 */
	private String userId;
	
	/**
	 * 词条
	 */
	private Entry entry;
	
	/**
	 * 贡献人
	 */
	private Collection<BUser> contributer;
	
	/**
	 * 词条id
	 */
	private String entryId;	

	/**
	 * 词条类型ID
	 */
	private String categoryId;

	/**
	 * 词条类型
	 */
	private Category category;
	
	/**
	 * 词条内容id
	 */
	private String contentId;
	
	/**
	 * 词条名称
	 */
	private String entryName;
	
	/**
	 * 最新词条
	 */
	private DataPackage<Entry> topNewest;
	
	/**
	 * 最热词条
	 */
	private DataPackage<Entry> topHot;
	
	/**
	 * 词条集合
	 */
	private Collection<Entry>  entries;
	
	/**
	 * 个人积分排行
	 */
	private DataPackage<BUser> topPoint;
	
	/**
	 * 问题集合
	 */
	private DataPackage<KnowledgeQuestion> question;

	/**
	 * 通过词条
	 */
	private DataPackage<Entry> entrys;
	
	/**
	 * 部门积分榜
	 */
	private DataPackage<Map> topDepartmentPoint;
	
	/**
	 * 我的贡献
	 */
	private DataPackage<EntryContent> topConttribute;
	
	/**
	 * 词条贡献者
	 */
	private DataPackage<Entry> conttribute;

	/**
	 * 查询字符串
	 */
	private String searchString;
	
	/**
	 * 当前点击菜单
	 */
	private String selectedMenu;
	
	/**
	 * 返回stream
	 */
	private InputStream inputStream;
	
	/**
	 * 收藏个数
	 */
	private int totalCount;

	/**
	 * 个人积分
	 */
	private DataPackage<Map> centerPoints;
	
	/**
	 * 用户属性
	 */
	private BUserAttribute attributer;
	
	/**
	 * 词条个数
	 */
	private int entryCounts;

	/**
	 * 浏览个数
	 */
	private int readCounts;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public int getReadCounts() {
		return readCounts;
	}

	public void setReadCounts(int readCounts) {
		this.readCounts = readCounts;
	}

	public int getEntryCounts() {
		return entryCounts;
	}

	public void setEntryCounts(int entryCounts) {
		this.entryCounts = entryCounts;
	}

	public BUserAttribute getAttributer() {
		return attributer;
	}

	public void setAttributer(BUserAttribute attributer) {
		this.attributer = attributer;
	}

	public DataPackage<Map> getCenterPoints() {
		return centerPoints;
	}

	public void setCenterPoints(DataPackage<Map> centerPoints) {
		this.centerPoints = centerPoints;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public DataPackage<Entry> getEntrys() {
		return entrys;
	}

	public void setEntrys(DataPackage<Entry> entrys) {
		this.entrys = entrys;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getEntryId() {
		return entryId;
	}

	public DataPackage<Entry> getConttribute() {
		return conttribute;
	}

	public DataPackage<KnowledgeQuestion> getQuestion() {
		return question;
	}

	public void setQuestion(DataPackage<KnowledgeQuestion> question) {
		this.question = question;
	}

	public void setConttribute(DataPackage<Entry> conttribute) {
		this.conttribute = conttribute;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
 
	public Collection<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Collection<Entry> entries) {
		this.entries = entries;
	}

	public Collection<BUser> getContributer() {
		return contributer;
	}

	public void setContributer(Collection<BUser> contributer) {
		this.contributer = contributer;
	}

	public DataPackage<Entry> getTopNewest() {
		return topNewest;
	}

	public void setTopNewest(DataPackage<Entry> topNewest) {
		this.topNewest = topNewest;
	}

	public DataPackage<Entry> getTopHot() {
		return topHot;
	}

	public void setTopHot(DataPackage<Entry> topHot) {
		this.topHot = topHot;
	}

	public DataPackage<BUser> getTopPoint() {
		return topPoint;
	}

	public void setTopPoint(DataPackage<BUser> topPoint) {
		this.topPoint = topPoint;
	}

	public DataPackage<EntryContent> getTopConttribute() {
		return topConttribute;
	}

	public void setTopConttribute(DataPackage<EntryContent> topConttribute) {
		this.topConttribute = topConttribute;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public DataPackage<Map> getTopDepartmentPoint() {
		return topDepartmentPoint;
	}

	public void setTopDepartmentPoint(DataPackage<Map> topDepartmentPoint) {
		this.topDepartmentPoint = topDepartmentPoint;
	}
	
	public String getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(String selectedMenu) {
		this.selectedMenu = selectedMenu;
	}
	
	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public int getPage() {
		if (getParams().getParameter("page") == null)
			getParams().setParameter("page", 1);
		return (Integer)getParams().getParameter("_pagelines");
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public int getLines() {
		if(lines==0)
			lines=10;
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getResult() {
		return inputStream;
	}
		
	/**
	 * 创建词条
	 * 
	 * @return
	 */
	public String doSaveAndAddPoint() {
		try {
			this.setContent(entry);
			Entry newEntry = ((EntryProcess)this.getProcess()).doFindByName(entry.getName());
			if (newEntry != null) {
				this.addFieldError("1", "该词条已存在！");
				return INPUT;
			}
			((EntryProcess)this.getProcess()).doCreateAndAddPoint(entry, 0,getUser());
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 删除词条action层实现
	 * 
	 * @return
	 */
	public String doRemove() {
		try {
			this.getProcess().doRemove(entryId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 修改词条action层实现
	 * @return
	 */
	public String doUpdate() {
		try {
			this.getProcess().doUpdate(this.content);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 修改词条名称
	 * @return
	 */
	public String doModifyEntryName(){
		try {
			Entry newEntry = ((EntryProcess)this.getProcess()).doFindByName(entryName);
			if (newEntry == null){
				((EntryProcess) this.getProcess()).doModifyEntryName(entryId,entryName,categoryId);
				
				inputStream = new ByteArrayInputStream("词条名称修改成功!".getBytes("UTF-8"));
				return SUCCESS;
			} else {
				inputStream = new ByteArrayInputStream("该词条名称已存在!".getBytes("UTF-8"));
				return INPUT;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	/**
	 * 修改词条名称和类型
	 * @return
	 */
	public String doModifyEntryNameAndCategory(){
		try {
			Entry newEntry = ((EntryProcess)this.getProcess()).doFindByName(entryName);
				((EntryProcess) this.getProcess()).doModifyEntryNameAndCategory(entryId,entryName,categoryId);
				
				inputStream = new ByteArrayInputStream("词条名称/类型修改成功!".getBytes("UTF-8"));
				return SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查找类型名称
	 * @return
	 */
	public String doFindByEntryId(){
		try {
			category = new CategoryProcessBean().doFindByEntryId(entryId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 根据编号查询词条
	 * 
	 * @return
	 */
	public String doView() {
		try {
			NObject content = ((EntryProcess)getProcess()).doView(entryId, null,getUser().getId());
			this.setContent(content);
			contributer=new BUserAttributeProcessBean().doFindContributer(entryId);
			totalCount=new BUserEntrySetProcessBean().doFavoriteCount(entryId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
		
	/**
	 * 词条预览
	 * @return
	 */
	public String doPreview() {
		try {
			NObject content = ((EntryProcess)getProcess()).doView(entryId, contentId,getUser().getId());
			contributer=new BUserAttributeProcessBean().doFindContributer(entryId);
		  //this.getRequest().put("entry", ent);
			this.setContent(content);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询热点词条
	 */
	public String doHot() {
		try {
			 page =getPage();
			 lines=getLines();
			 DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).doQueryHotEntry(page, lines);
			//this.getRequest().put("entry", hotEntry);
			 this.setDatas(entries);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询最新词条
	 */
	public String findRecentEntry() {
		try {
			 page =getPage();
			 lines=getLines();
			 DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).doQueryRecent(page, lines);
			//this.getRequest().put("entry", recentEntry);
			 this.setDatas(entries);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 返回最新词条,最近词条,积分排行
	 */
	public String doInit() {
		try {
			
			 page =1;   //页码
			 
			 lines=8;   //初始化页面最多显示8条记录
			 
			 //selectedMenu为页面菜单id,亦为词条类别ID
			 String categoryId = selectedMenu;
			 
			 //最热词条
			 topHot = ((EntryProcessBean)getProcess()).doQueryHotEntry(categoryId, page, lines);
			 
			 //最新词条
			 topNewest=((EntryProcessBean)getProcess()).doQueryRecent(categoryId, page, lines);
			 
			 //积分排行榜
		     topPoint=new BUserAttributeProcessBean().getScoreboard(page, lines);
		     
		     //部门积分榜
		     topDepartmentPoint = ((EntryProcess)getProcess()).getScoreboardByDepartment(page, lines);
			 
			 //贡献者
			 conttribute =((EntryProcessBean)getProcess()).queryContributorByCreated(categoryId, getUser().getId(), page, lines);
			 
			 //问题
			 question = new KnowledgeQuestionProcessBean().doQueryAllQuestion(page, lines);
			 
			 entryCounts = ((EntryProcessBean)getProcess()).getEntryCounts();
			 
			 readCounts = ((EntryProcessBean)getProcess()).getReadCounts();
			 
			 return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询部门积分
	 * @return
	 */
	public String doFindDepartmentPoint(){
	try {
			if(page==0){
				page =1;
			}//页码	 
			lines=50;   //初始化页面最多显示50条记录 
			topDepartmentPoint = ((EntryProcess)getProcess()).getScoreboardByDepartment(page, lines);
			return SUCCESS;
	} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
		
	/**
	 * 查询所有问题
	 * @return
	 */
	public String doqueryAllQuestion(){
	try {
			if(page==0){
				page =1;
			}//页码	 
			lines=50;   //初始化页面最多显示50条记录 
			 question = new KnowledgeQuestionProcessBean().doQueryAllQuestion(page, lines);
		
			return SUCCESS;
	} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询部门积分
	 * @return
	 */
	public String doFindContribute(){
	try {
			if(page==0){
				page =1;
			}//页码	 
			lines=50;   //初始化页面最多显示50条记录 
			String categoryId = selectedMenu;//selectedMenu为页面菜单id,亦为词条类别ID 
			conttribute =((EntryProcessBean)getProcess()).queryContributorByCreated(categoryId, getUser().getId(), page, lines);
			return SUCCESS;
	} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/** 
	 * 查询所有的最新词条
	 * @return
	 */
	public String doFindRecentEntry(){
	try {
			if(page==0){
				page =1;
			}//页码	 
			lines=50;   //初始化页面最多显示50条记录 
			String categoryId = selectedMenu;//selectedMenu为页面菜单id,亦为词条类别ID 
			DataPackage<Entry>	topNewest=((EntryProcessBean)getProcess()).doQueryRecent(categoryId, page, lines);
			 this.setDatas(topNewest);
			return SUCCESS;
	} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询所有的最热词条
	 * @return
	 */
	public String doFindHotEntry(){
	try {
		if(page==0){
			page =1;
		} //页码	 
			lines=50;   //初始化页面最多显示50条记录  
			String categoryId = selectedMenu;//selectedMenu为页面菜单id,亦为词条类别ID 
			DataPackage<Entry> topHot = ((EntryProcessBean)getProcess()).doQueryHotEntry(categoryId, page, lines);
			this.setDatas(topHot);
			return SUCCESS;
	} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询所有的积分排行名单
	 * @return
	 */
	public String doFindPointEntry(){
	try {
			if(page==0){
				page =1;
			}	 
			lines=50;   //初始化页面最多显示50条记录 
			topPoint=new BUserAttributeProcessBean().getScoreboard(page, lines);
			return SUCCESS;
	} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询所有贡献名单
	 * @return
	 */
	public String doFindContributor(){
	try {
		if(page==0){
			page =1;
		}  //页码	 
			lines=50;   //初始化页面最多显示50条记录 
			String categoryId = selectedMenu;//selectedMenu为页面菜单id,亦为词条类别ID 
			topConttribute = (new EntryContentProcessBean()).getUserPassedContent(categoryId,getUser().getId(), page, lines);
			return SUCCESS;
	} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 进入词条
	 * @return
	 */
	public String doAccess() {
		try {
			Entry entry = ((EntryProcess)getProcess()).doFindByName(searchString.trim());
			if (entry != null) {
				this.setEntryId(entry.getId());
				this.setContent(entry);
				return SUCCESS;
			} else {
				return "searching";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
  	
	/**
	 * 根据名字查询
	 */
	public String doFindEntryByName() {
		try {
			 page =getPage();
			 lines=getLines();
			String entryName=(String) getParams().getParameter("name");
			 DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).doQueryEntryByName(page, lines, entryName);
			 this.setDatas(entries);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 根据种类CategoryId查询
	 */
	public String doFindByCategoryId() {
		try {
			page =getPage();
			lines=getLines();
			String CategoryId=(String) getParams().getParameter("CategoryId");
			 DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).doQueryByCategoryId(page, lines, CategoryId);
			 this.setDatas(entries);
			//this.getRequest().put("entry", Entry);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	/**
	 * 根据关键字查询
	 */
	public String doFindByKeyWord() {
		try {
			page =getPage();
			lines=getLines();
			String CategoryId=(String) getParams().getParameter("CategoryId");
			 DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).doQueryByKeyWord(page, lines, CategoryId);
			 this.setDatas(entries);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	/**
	 * 根据当前登录用户查询词条
	 * @return
	 */
	public String doGetMyEntry() throws Exception{
		
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		//参数3:当前登录用户(创建者)
		System.out.println("根据创建者查询词条");
		//System.out.println("作者:"+entry.getAuthor());
		if(userId==null||userId.trim().length()<=0){
		DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).dofindEntryByUserId(page, lines, this.getUser());
		this.setDatas(entries);
		return SUCCESS;
		}else{
			DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).dofindEntryByUserId(page, lines, userId);
			this.setDatas(entries);
			return SUCCESS;
		}
	}
	
	/**
	 * 获取草稿箱
	 * @return
	 * @throws Exception
	 */
	public String doGetMyDraft() throws Exception {
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).queryDraftEntry(page, lines, this.getUser().getId());
		this.setDatas(entries);
		return SUCCESS;
	}
	
	/**
	 * 获取通过词条
	 * @return
	 * @throws Exception
	 */
	public String doGetAllEntry() throws Exception {
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		entrys = ((EntryProcessBean)getProcess()).queryPasseedEntry(page, lines);
		return SUCCESS;
	}
	
	/**
	 * 为词条投票，增加词条积分
	 * @return
	 * @throws Exception
	 */
	public String doVote() throws Exception {
		try {
			((EntryProcess)getProcess()).doVote(entryId);
			return SUCCESS;
		} catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}	
	}
	
	/**
	 * 通过用户ID进入用户中心
	 * @param userId
	 * @throws Exception
	 */
	public String doFindUserCenterByUserId() throws Exception{
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=3;
		}
		
		this.setDatas(((EntryProcessBean)getProcess()).doFindUserCenterByUserId(userId, page, lines));
		return SUCCESS;
	}
	
	
	@Override
	public NRunTimeProcess<Entry> getProcess() {
		process =new EntryProcessBean();
		return process;
	}
	
	/**
	 * 根据   用户   编号查询用户
	 * @param bUserId
	 * @return
	 * @throws Exception
	 */
	public String doFindByBUserId() throws Exception{
		
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		DataPackage<Entry> entries = ((EntryProcessBean)getProcess()).findPassedByUserId(page, lines, getUser().getId());
		this.setDatas(entries);
		attributer = new BUserAttributeProcessBean().findByUserId(this.getUser().getId());
		centerPoints = ((EntryProcessBean)getProcess()).getScoreboardByEntry(page, lines, getUser().getId());
		
		return SUCCESS;
	}
}
