package cn.myapps.km.baike.user.action;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.user.ejb.BUserEntrySet;
import cn.myapps.km.baike.user.ejb.BUserEntrySetProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author dragon
 *
 */
public class BUserEntrySetAction extends AbstractRunTimeAction<BUserEntrySet> {
	
	/**
	 * Entry集合
	 */
	private DataPackage<Entry> entrys;
	
	/**
	 * Entry集合
	 */
	private DataPackage<Entry> favorites;
	
	//long型变量,存储数量(分享词条数量,收藏词条数量)
	private long count;
	
	/**
	 * 当前点击菜单
	 */
	private String selectedMenu;
	
	/**
	 * 返回stream
	 */
	private InputStream inputStream;

	/**
	 * 词条ID
	 */
	private String entryId;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 个人中心
	 */
	private DataPackage<BUserEntrySet> center;
	
	public DataPackage<BUserEntrySet> getCenter() {
		return center;
	}

	public void setCenter(DataPackage<BUserEntrySet> center) {
		this.center = center;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public DataPackage<Entry> getEntrys() {
		return entrys;
	}

	public void setEntrys(DataPackage<Entry> entrys) {
		this.entrys = entrys;
	}
		
	public String getEntryId() {
		return entryId;
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
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public DataPackage<Entry> getFavorites() {
		return favorites;
	}
	
	public void setFavorites(DataPackage<Entry> favorites) {
		this.favorites = favorites;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public String getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(String selectedMenu) {
		this.selectedMenu = selectedMenu;
	}
	
	public BUserEntrySetAction() {
		super();
		this.process = getProcess();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2014700496839634066L;
	
	private BUserEntrySet bUserEntrySet;
	
	private int lines=0;
	private int page=0;
	
	
	public int getlines() {
		return lines;
	}

	public void setlines(int lines) {
		this.lines = lines;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public BUserEntrySet getbUserEntrySet() {
		return bUserEntrySet;
	}

	public void setbUserEntrySet(BUserEntrySet bUserEntrySet) {
		this.bUserEntrySet = bUserEntrySet;
	}

	/**
	 * 根据编号查询用户
	 * @return
	 */
	public String doView(){
		try {
			BUserEntrySet bUserEntrySetrs =(BUserEntrySet) ((BUserEntrySetProcessBean)getProcess()).doView(bUserEntrySet.getId());
			this.setContent(bUserEntrySetrs);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 增加用户词条关联关系
	 * @return
	 */
	public String doSave(){
		try {
			//当前登录用户
			bUserEntrySet.setUserId(this.getUser().getId());
			//先验证当前用户是否收藏这个词条
			boolean falg = ((BUserEntrySetProcessBean)getProcess()).isFavoritesEntry(bUserEntrySet);
			//如果返回
			if(falg==true){
				return "error";
			}else{
				((BUserEntrySetProcessBean)getProcess()).doCreate(bUserEntrySet);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	public String doFavorite(){
		
		try{
			BUserEntrySet bUserEntrySet=new BUserEntrySet();
			bUserEntrySet.setEntryId(entryId);
			bUserEntrySet.setUserId(this.getUser().getId());
			bUserEntrySet.setType(BUserEntrySet.TYPE_FAVORITE);
			boolean flag = ((BUserEntrySetProcessBean)getProcess()).isFavoritesEntry(bUserEntrySet);
		if(flag){
			inputStream = new ByteArrayInputStream("该词条已被收藏!".getBytes("UTF-8"));
			return INPUT;
		}else{
			((BUserEntrySetProcessBean)getProcess()).doCreate(bUserEntrySet);
			inputStream = new ByteArrayInputStream("收藏词条成功!".getBytes("UTF-8"));
			return SUCCESS;
		}
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 删除用户收藏词条 action层实现
	 * @return
	 */
	public String doRemove(){
		try {
			((BUserEntrySetProcessBean)getProcess()).doRemove(bUserEntrySet.getId());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	
	/**
	 * 统计用户分享词条数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String doCountShared() throws Exception{
		count=((BUserEntrySetProcessBean)getProcess()).countSharedEntry(bUserEntrySet.getUserId());
		return SUCCESS;
	}
	
	/**
	 * 统计用户收藏词条数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String doCountFavorite() throws Exception{
		count=((BUserEntrySetProcessBean)getProcess()).countFavoriteEntry(bUserEntrySet.getUserId());
		return SUCCESS;
	}
	
	
	/**
	 * 统计用户相关联词条次数
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String doCountUserEntry() throws Exception{
		count=((BUserEntrySetProcessBean)getProcess()).countEntryByUserId(bUserEntrySet.getUserId(), bUserEntrySet.getType());
		return SUCCESS;
	}
	
	/**
	 * 统计词条相关联的用户信息
	 * @param entryId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String doCountBeingFavorites() throws Exception{
		count=((BUserEntrySetProcessBean)getProcess()).countBeingFavorites(bUserEntrySet.getUserId(), bUserEntrySet.getType());
		return SUCCESS;
	}
	
	/**
	 * 获取词条被收藏次数
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public String doCountBeingFavorite() throws Exception{
		count=((BUserEntrySetProcessBean)getProcess()).countBeingFavorite(bUserEntrySet.getEntryId());
		return SUCCESS;
	}
	
	/**
	 * 获取词条被收藏次数
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public String doCcountBeingShare() throws Exception{
		count=((BUserEntrySetProcessBean)getProcess()).countBeingShare(bUserEntrySet.getEntryId());
		return SUCCESS;
	}
	
	/**
	 * 分页查询用户个人收藏
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String doGetPersonalFavorites() throws Exception{
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		if(userId == null||userId.trim().length()<=0){
			userId=getUser().getId();
		}
		favorites=((BUserEntrySetProcessBean)getProcess()).getPersonalFavorites(page, lines, userId);
		System.out.println("执行:查询用户个人收藏");
		return SUCCESS;
		
	}
	
	
	/** 
	 * 分页查询用户个人分享 (词条信息)
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String doGetPersonalShares() throws Exception{
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		favorites=((BUserEntrySetProcessBean)getProcess()).getPersonalShares(page, lines,getUser().getId());
		System.out.println("执行:查询用户个人分享");
		return SUCCESS;
	}
	
	
	/**
	 * 查询用户词条相关联的信息
	 * @param page
	 * @param lines
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String doQueryByUserIdAndType() throws Exception{
		favorites=((BUserEntrySetProcessBean)getProcess()).queryByUserIdAndType(page, lines,  bUserEntrySet.getUserId(),  bUserEntrySet.getType());
		return SUCCESS;
	}
	
	
	/**
	 * 根据贡献人查询词条贡献  已通过版本
	 * @param page
	 * @param lines
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String doQueryByAuthor() throws Exception{
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		if(userId == null||userId.trim().length()<=0||userId==""){
		userId=getUser().getId();
		}
		entrys=((BUserEntrySetProcessBean)getProcess()).queryByAuthor(page, lines, userId,EntryContent.STATE_PASSED);
		return SUCCESS;
	}
	

	
	/**
	 * 未通过版本
	 * @return
	 * @throws Exception
	 */
	public String doNotThroughVersion() throws Exception{
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		if(userId == null||userId.trim().length()<=0){
			userId=getUser().getId();
		}
		entrys=((BUserEntrySetProcessBean)getProcess()).getNotThroungh(page, lines,userId);
		return SUCCESS;
		
	}
	
	
	/**
	 * 已提交(待审核)
	 * @return
	 * @throws Exception
	 */
	public String doSubmitVersion() throws Exception{
		if(page==0 || lines==0){
			//当前页
			page=1;
			//每页显示行数
			lines=8;
		}
		entrys=((BUserEntrySetProcessBean)getProcess()).queryByAuthor(page, lines, getUser().getId(), EntryContent.STATE_SUBMITTED);
		return SUCCESS;
	}
	
	/*
	 * 判断是否为管理员
	 */
	public String isPublicDiskAdmin() throws Exception{
		boolean	flag = ((BUserEntrySetProcessBean)getProcess()).isPublicDiskAdmin(getUser().getId(),3);
		if(flag==true){
			return SUCCESS;
		}else{
			return INPUT;
		}
	}
	

	public NRunTimeProcess<BUserEntrySet> getProcess() {
		return new BUserEntrySetProcessBean();
	}
	
	
}
