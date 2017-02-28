package cn.myapps.km.baike.entry.ejb;

import java.util.Date;

import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.content.ejb.EntryContentProcess;
import cn.myapps.km.baike.content.ejb.EntryContentProcessBean;
import cn.myapps.km.baike.history.ejb.History;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.base.ejb.NObject;

	/**
	 * 词条
	 * @author Allen
	 * 
	 */
public class Entry extends NObject {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5875321631793852775L;
	
	/**
	 * 作者
	 */
	private  BUser author;
	 
	/**
	 *词条类别 
	 */
	private String categoryId;
	
	/**
	 * 词条贡献人
	 */
	//private Collection<BUser> contributorys;
	
	/**
	 * 创建时间
	 */
	private Date created;

	/**
	 * 词条最新内容 
	 */
	@Deprecated
	private String latestContentId;
	
	/**
	 * 最新词条内容
	 */
	private EntryContent latestContent;
	
	/**
	 * 词条内容
	 */
	private EntryContent entryContent;
	
	/**
	 * 历史记录
	 */
	private History history;
	/**
	 * 词条显示内容
	 */
	private EntryContent content;

	/**
	 * 关键字
	 */
	private String keyWord;
	
	/**
	 * 积分
	 */
	private long points = 0;
	
	/**
	 * 编辑次数
	 */
	private int editCount = 0;

	/**
	 * 浏览次数
	 */
	private int browseCount=0;
	
	
	
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

	public EntryContent getContent() {
		return content;
	}

	public void setContent(EntryContent content) {
		this.content = content;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public int getEditCount() {
		return editCount;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public long getPoints() {
		return points;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date createdTime) {
		this.created = createdTime;
	}

	public void setEditCount(int editCount) {
		this.editCount = editCount;
	}

	public void setKeyWord(String lable) {
		this.keyWord = lable;
		
	}
	
	public void setPoints(long points) {
		this.points = points;
	}
	
	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public String getLatestContentId() {
		return latestContentId;
	}

	public void setLatestContentId(String latestContentId) {
		this.latestContentId = latestContentId;
	}

	public EntryContent getLatestContent() {
		try {
			EntryContentProcess process = new EntryContentProcessBean();
			latestContent = process.getLatestVersionContent(this.getId());
			return latestContent;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setLatestContent(EntryContent latestContent) {
		this.latestContent = latestContent;
	}

	public EntryContent getEntryContent() {
		try {
			EntryContentProcess process = new EntryContentProcessBean();
			entryContent = process.getContent(this.getId());
			return entryContent;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setEntryContent(EntryContent entryContent) {
		this.entryContent = entryContent;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Entry&&obj!=null){
			Entry entry=(Entry)obj;
			boolean b=this.getId().equals(entry.getId())
					&&this.getBrowseCount()==entry.getBrowseCount()
					&&this.getEditCount()==entry.getEditCount()
					&&this.getPoints()==entry.getPoints()
					&&(this.getCreated()==null&&entry.getCreated()==null)?true:((this.getCreated()==null&&entry.getCreated()!=null)?false :this.getCreated().equals(entry.getCreated()))
					&&(this.getName()==null&&entry.getName()==null)?true:((this.getName()==null&&entry.getName()!=null)?false :this.getName().equals(entry.getName()))
					&&(this.getKeyWord()==null&&entry.getKeyWord()==null)?true:((this.getKeyWord()==null&&entry.getKeyWord()!=null)?false :this.getKeyWord().equals(entry.getKeyWord()))
					&&(this.getCategoryId()==null&&entry.getCategoryId()==null)?true:((this.getCategoryId()==null&&entry.getCategoryId()!=null)?false :this.getCategoryId().equals(entry.getCategoryId()))
					&&(this.getAuthor()==null&&entry.getAuthor()==null)?true:((this.getAuthor()==null&&entry.getAuthor()!=null)?false :this.getAuthor().equals(entry.getAuthor()))
					&&(this.getDomainId()==null&&entry.getDomainId()==null)?true:this.getDomainId().equals(entry.getDomainId())
					&&this.getCategoryId()==entry.getCategoryId()
					&&(this.getLatestContentId()==null&&entry.getLatestContentId()==null)?true:this.getLatestContentId().equals(entry.getLatestContentId())
					;
			return  b;
		}
		return false;
	}
	
}
