package cn.myapps.km.baike.content.action;

import java.util.Collection;

import org.apache.struts2.ServletActionContext;

import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.content.ejb.EntryContentProcess;
import cn.myapps.km.baike.content.ejb.EntryContentProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;


/** 
 * @author abel
 * 用户表的基本操作
 *
 */
public class EntryContentAction extends AbstractRunTimeAction<EntryContent>{
	
	public EntryContentAction(){
		super();
		this.process=getProcess();
	}

	private static final long serialVersionUID = -2014700496839634066L;
	
	/**
	 * 页数
	 */
	private int page;
	 
	/**
	 * 行数
	 */
	private int lines;

	/**
	 *词条内容 
	 */
	private EntryContent entryContent;
	
	/**
	 * 词条版本ID
	 */
	private String contentId;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 词条ID
	 */
	private String entryId;
	
	/**
	 * 状态
	 */
	private String state;
	
	/**
	 * 查询字符串
	 */
	private String searchString;
	
	/**
	 * 内容集合
	 */
	private Collection<EntryContent> listContent;
	
	/**
	 * 驳回所有原因
	 */
	private String reason;
	
	private String[] alls;
	
	public String[] getAlls() {
		return alls;
	}
	public void setAlls(String[] alls) {
		this.alls = alls;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public EntryContent getEntryContent() {
		return entryContent;
	}
	
	public String getSearchString() {
		return searchString;
	}
	
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public void setEntryContent(EntryContent entryContent) {
		this.entryContent = entryContent;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
		
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Collection<EntryContent> getListContent() {
		return listContent;
	}

	public void setListContent(Collection<EntryContent> listContent) {
		this.listContent = listContent;
	}

	/**
	 * 根据编号查询用户
	 * @return
	 */
	public String doView(){
		try {
			EntryContent content=(EntryContent) ((EntryContentProcessBean)getProcess()).doView(contentId);
			this.setContent(content);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 创建词条内容
	 * @return
	 */
	public String doSave(){
		try {
			((EntryContentProcess)getProcess()).doCreate(entryContent, getUser());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 创建或提交词条版本
	 * @return
	 */
	public String doSubmmit() {
		try {
			((EntryContentProcess)getProcess()).doCreateOrSubmmit(entryContent, getUser());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	

	/**
	 * 驳回词条修改
	 * @return
	 */
	public String doReject() {
		try {
			((EntryContentProcess)getProcess()).doReject(contentId,reason);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 提交内容
	 * @return
	 */
	public String doSubmit() {
		try {
			((EntryContentProcess)getProcess()).doSubmit(contentId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	/**
	 * 用户提交未通过版本
	 * @return
	 */
	public String doSubmmitFromReject() {
		try {
			((EntryContentProcess)getProcess()).doSubmmit(entryContent);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 用户从草稿箱提交词条版本
	 * @return
	 */
	public String doSubmmitFromDraft() {
		try {
			((EntryContentProcess)getProcess()).doSubmmit(entryContent);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 获取用户草稿
	 * @return
	 */
	public String doGetMyDraft() {
		try {
			page = page==0 ? 1: page;
			lines= lines==0 ? 8 : lines;
			DataPackage<EntryContent> entryContents = ((EntryContentProcess)getProcess()).getUserDraftContent(getUser().getId(), page, lines);
			this.setDatas(entryContents);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 获取用户管理
	 * @return
	 */
	public String doGetMyManager() {
		try {
			page = page==0 ? 1: page;
			lines= lines==0 ? 8 : lines;
			DataPackage<EntryContent> entryContents = ((EntryContentProcess)getProcess()).getUserPassedContent(getUser().getId(), page, lines);
			this.setDatas(entryContents);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
		
	/**
	 * 审核通过词条的修改
	 * @return
	 */
	public String doApprove() {
		try {
			String indexRealPath = ServletActionContext.getRequest().getRealPath("/baike");
			((EntryContentProcess)getProcess()).doApprove(contentId, indexRealPath);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询词条内容
	 * @return
	 */
	public String doQuery() {
		try {
			String indexRealPath = ServletActionContext.getRequest().getRealPath("/baike");
			page = page<=0 ? 1 : page;
			listContent = ((EntryContentProcess)getProcess()).doQuery(searchString, indexRealPath, page, 10);
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 管理员获取词条待办
	 * @return
	 */
	public String doPendingList() {
		try {
			page = page==0 ? 1 : page;
			lines = lines==0 ? 8 : lines;
			DataPackage<EntryContent> entyContents = ((EntryContentProcess)getProcess()).doPendingList(page, lines);
			this.setDatas(entyContents);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 删除用户action层实现
	 * @return
	 */
	public String doRemove(){
		try {
			((EntryContentProcessBean)getProcess()).doRemove(contentId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 修改用户action层实现
	 * @return
	 */
	public String doUpdate(){
		try {
			((EntryContentProcess)getProcess()).doCreateOrUpdate(entryContent, getUser());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 获取词条历史版本内容
	 * @return
	 */
	public String doQueryHisVersionContent(){
		try {
			DataPackage<EntryContent> hisContents =((EntryContentProcessBean)getProcess()).getHisVersionContent(entryId, page, lines);
			this.setDatas(hisContents);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 *   获取词条最新版本内容
	 * @return
	 */
	public String doQueryLatestVersionContent(){
		try {
			EntryContent content =((EntryContentProcessBean)getProcess()).getLatestVersionContent(entryId);
			this.setContent(content);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * 通过词条ID查询已通过版本的词条内容
	 * @return
	 */
	public String doQueryContentByEntryIdAndPassed(){
		try {
			listContent = ((EntryContentProcessBean)getProcess()).queryByEntryIdAndPassedContent(entryId);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	@Override
	public NRunTimeProcess<EntryContent> getProcess() {
		// TODO Auto-generated method stub
		return new EntryContentProcessBean();
	}
	
	/**
	 * 根据词条id删除内容
	 * @return
	 */
	public String doRemoveByEntryId(){
		try {
			((EntryContentProcessBean)getProcess()).doRemoveByEntryId(entryId);
			System.out.println("调用根据词条id删除词条内容方法.");
			System.out.println(entryId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 批量删除草稿箱
	 * @return
	 */
	public String deleteMany(){
		try {
			if(alls!=null){
				for (int i = 0; i < alls.length; i++) {
					String id = alls[i];
					System.out.println("ididididididididid:"+id);
					((EntryContentProcessBean)getProcess()).doRemoveByEntryId(id.toString());
					System.out.println("删除的为:"+id);
				}
			}
				return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
}

