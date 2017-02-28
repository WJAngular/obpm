package cn.myapps.km.baike.content.ejb;

import java.util.Date;

import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.entry.ejb.EntryProcess;
import cn.myapps.km.baike.entry.ejb.EntryProcessBean;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.base.ejb.NObject;



/**
 * @author abel
 * 词条内容
 */
public class EntryContent extends NObject{

	
	private static final long serialVersionUID = -6179754824037267812L;
	
	/**
	 * 草稿状态
	 */
	public static final String STATE_DRAFT = "DRAFT";
	
	/**
	 * 通过状态
	 */
	public static final String STATE_PASSED = "PASSED";
	
	/**
	 * 已提交状态
	 */
	public static final String STATE_SUBMITTED = "SUBMITTED";
	
	/**
	 * 驳回状态
	 */
	public static final String STATE_REJECT = "REJECT";
	
	/**
	 * 词条ID
	 */
	private String entryId;
	
	/**
	 * 词条
	 */
	private Entry entry;
	
	/**
	 * 版本号
	 */
	private int versionNum;

	/**
	 * 处理时间
	 */
	private Date handleTime; 
	
	/**
	 * 提交时间
	 */
	private Date submmitTime; 
	
	/*
	 * 保存时间
	 */
	private Date saveTime;
	
	/**
	 * 修改人
	 */
	private BUser  author;
	
	/**
	 * 修改原因
	 */
	private String  reason;
	
	/**
	 * 内容
	 */
	private String  content;
	
	/**
	 * 状态
	 */
	private String  state;

	/*
	 * 简介
	 */
	private String  summary;
	
	

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}


	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	public Date getSubmmitTime() {
		return submmitTime;
	}

	public void setSubmmitTime(Date submmitTime) {
		this.submmitTime = submmitTime;
	}

	public BUser getAuthor() {
		return author;
	}

	public void setAuthor(BUser author) {
		this.author = author;
	}
	
	/**
	 * 设置作者
	 * 
	 * @param authorId
	 * 				作者ID  
	 * 
	 *  @uml.property name="author"
	 */
	public void setAuthor(String authorId) {
		try {
			BUserAttributeProcessBean process = new BUserAttributeProcessBean();
			this.setAuthor(process.findBUserById(authorId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	

	/**
	 * 通过词条ID获取词条
	 * @return
	 * @throws Exception
	 */
	public Entry getEntry() {
		try {
			EntryProcess process = new EntryProcessBean();
			entry = (Entry) process.doView(entryId);
			return entry;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	} 
	
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println(obj);
		if(obj==null){
			return false;
		}else if(obj instanceof EntryContent){
			EntryContent content=(EntryContent)obj;
			return (this.getId().equals(content.getId()))
				 &&(this.getEntryId().equals(content.getEntryId()))
				  //&&(this.getAuthor().equals(content.getAuthor()))
				   &&(this.getReason().equals(content.getReason()))
				    &&(this.getContent().equals(content.getContent()))
				     // &&(this.getState()==(content.getState()))
				       &&(this.getVersionNum()==(content.getVersionNum()))
				       &&(this.getSummary().equals(content.getSummary()));
		}
		return false;
	}

	@Override
	public String toString() {
		return "EntryContent [getEntryId()=" + getEntryId() +"getReason()="+getReason()+"getContent()="
	    +getContent()+ "getVersionNum()="+getVersionNum()+"getState()="+getState()+"getAuthor()="+getAuthor()+"getSubmmitTime()="+getSubmmitTime()+"]";
	}
	
	
	
}
