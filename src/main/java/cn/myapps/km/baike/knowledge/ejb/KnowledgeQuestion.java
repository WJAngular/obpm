package cn.myapps.km.baike.knowledge.ejb;

import java.util.Date;

import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.base.ejb.NObject;



/**
 * @author abel
 * 知识悬赏问题
 */
public class KnowledgeQuestion extends NObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2499010630448372710L;
	
	/**
	 * 问题标题
	 */
	private String title;
	
	/**
	 * 问题补充内容
	 */
	private String content;

	/**
	 *创建人
	 */
	private BUser  author;
	
	/**
	 * 创建时间
	 */
	private Date created;
	
	/**
	 * 类型Id
	 */
	private String categoryId;
	
	/**
	 * 积分
	 */
	private int point;

	/**
	 * 答案
	 */
	private KnowledgeAnswer answer;
	
	public KnowledgeAnswer getAnswer() {
		return answer;
	}

	public void setAnswer(KnowledgeAnswer answer) {
		this.answer = answer;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public BUser getAuthor() {
		return author;
	}

	public void setAuthor(BUser author) {
		this.author = author;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
		}else if(obj instanceof KnowledgeQuestion){
			KnowledgeQuestion question=(KnowledgeQuestion)obj;
			return (this.getId().equals(question.getId()))
				 &&(this.getCategoryId().equals(question.getCategoryId()))
				  //&&(this.getAuthor().equals(content.getAuthor()))
				   &&(this.getTitle().equals(question.getTitle()))
				    &&(this.getContent().equals(question.getContent()));
		}
		return false;
	}

	@Override
	public String toString() {
		return "KnowledgeQuestion [getCategoryId()=" + getCategoryId() +"getTitle()="+getTitle()+"getContent()="
	    +getContent()+"]";
	}
	
	

}
