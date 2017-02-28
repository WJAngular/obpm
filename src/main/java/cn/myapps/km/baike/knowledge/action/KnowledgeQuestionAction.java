package cn.myapps.km.baike.knowledge.action;

import java.util.Collection;

import org.apache.struts2.ServletActionContext;

import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswer;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswerProcessBean;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestion;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestionProcess;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestionProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/** 
 * @author abel
 * 用户表的基本操作
 *
 */
public class KnowledgeQuestionAction extends AbstractRunTimeAction<KnowledgeQuestion>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 763979164701275600L;


	public KnowledgeQuestionAction(){
		super();
		this.process=getProcess();
	}

	/**
	 * 知识悬赏问题
	 */
	private KnowledgeQuestion knowledgeQuestion;
	
	/**
	 * 页数
	 */
	private int page;
	
	/**
	 * 行数
	 */
	private int lines;

	/**
	 * 问题ID
	 */
	private String questionId;
	
	/**
	 * 答案ID
	 */
	private String answerId;
	
	/**
	 * 答案集合
	 */
	private Collection<KnowledgeAnswer>  answers;
	
	/**
	 * 问题集合
	 */
	private Collection<KnowledgeQuestion> listQuestion;
	
	/**
	 * 采纳答案
	 */
	private KnowledgeAnswer acceptAnswer;
	
	/**
	 * 查询字符串
	 */
	private String searchString;
	
	private int allQuestion;
	
	private int allAcceptAnswer;
	
	
	public int getAllAcceptAnswer() {
		return allAcceptAnswer;
	}

	public void setAllAcceptAnswer(int allAcceptAnswer) {
		this.allAcceptAnswer = allAcceptAnswer;
	}

	public int getAllQuestion() {
		return allQuestion;
	}

	public void setAllQuestion(int allQuestion) {
		this.allQuestion = allQuestion;
	}

	public KnowledgeAnswer getAcceptAnswer() {
		return acceptAnswer;
	}

	public void setAcceptAnswer(KnowledgeAnswer acceptAnswer) {
		this.acceptAnswer = acceptAnswer;
	}

	public Collection<KnowledgeQuestion> getListQuestion() {
		return listQuestion;
	}

	public void setListQuestion(Collection<KnowledgeQuestion> listQuestion) {
		this.listQuestion = listQuestion;
	}

	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public Collection<KnowledgeAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Collection<KnowledgeAnswer> answers) {
		this.answers = answers;
	}

	public KnowledgeQuestion getKnowledgeQuestion() {
		return knowledgeQuestion;
	}

	public void setKnowledgeQuestion(KnowledgeQuestion knowledgeQuestion) {
		this.knowledgeQuestion = knowledgeQuestion;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}
	
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/**
	 * 创建问题
	 * @return
	 */
	public String doSave(){
		try {
			String indexRealPath = ServletActionContext.getRequest().getRealPath("/baike");
			this.setContent(knowledgeQuestion);
			((KnowledgeQuestionProcessBean)getProcess()).doCreate(knowledgeQuestion,getUser(),indexRealPath);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 查询问题
	 * @return
	 */
	public String doQuery() {
		try {
			String indexRealPath = ServletActionContext.getRequest().getRealPath("/baike");
			page = page<=0 ? 1 : page;
			listQuestion = ((KnowledgeQuestionProcess)getProcess()).doQuery(searchString, indexRealPath, page, 10);
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 进入问题
	 * @return
	 */
	public String doAccess() {
		try {
			KnowledgeQuestion question = ((KnowledgeQuestionProcess)getProcess()).doFindByName(searchString.trim());
			if (question != null) {
				this.setQuestionId(question.getId());
				this.setContent(question);
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
	 * 删除问题
	 * @return
	 */
	public String doRemove(){
		try {
			this.process.doRemove(questionId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	

	/**
	 * 根据编号查询用户
	 * @return
	 */
	public String doView(){
		try {
			KnowledgeQuestion question=(KnowledgeQuestion) ((KnowledgeQuestionProcessBean)getProcess()).doView(questionId);
			this.setContent(question);
			answers = new KnowledgeAnswerProcessBean().queryAnswerById(questionId);
			
			acceptAnswer = (KnowledgeAnswer) new KnowledgeAnswerProcessBean().queryAcceptAnswerById(questionId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询所有问题
	 */
	public String doQueryAllQuestion() {
		try {
			if(page==0){
				page =1;
			}//页码	 
			lines=50;  
			 DataPackage<KnowledgeQuestion> question = ((KnowledgeQuestionProcessBean)getProcess()).doQueryAllQuestion(page, lines);
			 this.setDatas(question);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public String doInit() throws Exception{
		
		allQuestion = ((KnowledgeQuestionProcessBean)getProcess()).getQuestionCounts();
		
		allAcceptAnswer =  ((KnowledgeQuestionProcessBean)getProcess()).getAcceptAnswerCounts();
		
		return SUCCESS;
	}
	
	@Override
	public NRunTimeProcess<KnowledgeQuestion> getProcess() {
		// TODO Auto-generated method stub
		return new KnowledgeQuestionProcessBean();
	}
	
	
}

