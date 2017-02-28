package cn.myapps.km.baike.knowledge.action;

import java.util.Collection;

import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswer;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswerProcess;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswerProcessBean;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestionProcessBean;
import cn.myapps.km.baike.user.dao.BUserAttributeDAO;
import cn.myapps.km.baike.user.dao.MssqlBUserAttributeDAO;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/** 
 * @author abel
 * 用户表的基本操作
 *
 */
public class KnowledgeAnswerAction extends AbstractRunTimeAction<KnowledgeAnswer>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3252465709321106960L;

	public KnowledgeAnswerAction(){
		super();
		this.process=getProcess();
	}

	/**
	 * 知识悬赏答案
	 */
	private KnowledgeAnswer knowledgeAnswer;
	
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
	 * 积分
	 */
	private int point;

	/**
	 * 答案集合
	 */
	private Collection<KnowledgeAnswer>  answers;
	
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
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

	public KnowledgeAnswer getKnowledgeAnswer() {
		return knowledgeAnswer;
	}

	public void setKnowledgeAnswer(KnowledgeAnswer knowledgeAnswer) {
		this.knowledgeAnswer = knowledgeAnswer;
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

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	/**
	 * 创建答案
	 * @return
	 */
	public String doSave(){
		try {
			((KnowledgeAnswerProcess)getProcess()).doCreate(knowledgeAnswer,getUser());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 通过ID查询答案
	 */
	public String doQueryAnswerById(){
		try {
			answers = ((KnowledgeAnswerProcess)getProcess()).queryAnswerById(questionId);
			this.getDatas().setDatas(answers);
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 删除答案
	 * @return
	 */
	public String doRemove(){
		try {
			((KnowledgeAnswerProcess)getProcess()).doRemove(questionId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	@Override
	public NRunTimeProcess<KnowledgeAnswer> getProcess() {
		// TODO Auto-generated method stub
		return new KnowledgeAnswerProcessBean();
	}
		
	/**
	 * 采纳答案
	 */
	public String doAcceptAnswer() {
		try {
			if(page==0){
				page =1;
			}//页码	 
			lines=50;  
			((KnowledgeAnswerProcessBean)getProcess()).doAcceptAnswer(answerId, getUser());
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
}

