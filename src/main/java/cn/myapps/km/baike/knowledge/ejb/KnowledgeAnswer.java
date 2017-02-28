package cn.myapps.km.baike.knowledge.ejb;

import java.util.Date;

import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.base.ejb.NObject;



/**
 * @author abel
 * 词条内容
 */
public class KnowledgeAnswer extends NObject{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1365793707048887545L;

	/**
	 * 问题标题
	 */
	private String questionId;
	
	/**
	 * 答案内容
	 */
	private String content;

	/**
	 *提交人
	 */
	private BUser  author;
	
	/**
	 * 提交时间
	 */
	private Date submitTime;
	
	/**
	 * 采纳状态
	 */
	public static final String STATE_ACCEPT = "ACCEPT";
	
	/**
	 * 状态
	 */
	public String state;
	
	/**
	 * 未采纳状态
	 * @return
	 */
	public static final String STATE_UNACCEPT = "UNACCEPT";
	
	/**
	 * 知识悬赏答案
	 */
	private KnowledgeAnswer knowledgeAnswer;
	
	public KnowledgeAnswer getKnowledgeAnswer() {
		return knowledgeAnswer;
	}

	public void setKnowledgeAnswer(KnowledgeAnswer knowledgeAnswer) {
		this.knowledgeAnswer = knowledgeAnswer;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
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

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
		}else if(obj instanceof KnowledgeAnswer){
			KnowledgeAnswer question=(KnowledgeAnswer)obj;
			return (this.getId().equals(question.getId()))
				 &&(this.getQuestionId().equals(question.getQuestionId()))
				  //&&(this.getAuthor().equals(content.getAuthor()))
				    &&(this.getContent().equals(question.getContent()));
		}
		return false;
	}

	@Override
	public String toString() {
		return "KnowledgeQuestion [getQuestionId()=" + getQuestionId() +"getContent()="
	    +getContent()+"]";
	}
	
	

}
