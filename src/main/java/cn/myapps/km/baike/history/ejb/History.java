package cn.myapps.km.baike.history.ejb;

import java.util.Date;

import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.base.ejb.NObject;

	/**
	 * 浏览历史
	 * @author Able
	 * 
	 */
public class History extends NObject {

	private static final long serialVersionUID = -1507133446792476558L;
	
	/**
	 * 词条Id
	 */
	private String entryId;
	
	/**
	 * 浏览时间
	 */
	private Date readTime;
	
	/**
	 * 用户
	 */
	private BUser author;

	/**
	 * 词条名称
	 */
	private String entryName;
	

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
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
	

	@Override
	public boolean equals(Object obj) {
		System.out.println(obj);
		if(obj==null){
			return false;
		}else if(obj instanceof History){
			History history=(History)obj;
			return (this.getId().equals(history.getId()))
				 &&(this.getEntryId().equals(history.getEntryId()))
				 &&(this.getEntryName().equals(history.getEntryName()));
		}
		return false;
	}


	
}
