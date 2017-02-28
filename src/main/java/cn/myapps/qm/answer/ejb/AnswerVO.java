package cn.myapps.qm.answer.ejb;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.base.dao.ValueObject;

public class AnswerVO extends ValueObject {
	
	private static final long serialVersionUID = -8229556919684561981L;

	
	public static final int STATUS_ALL = -1;//所有
	public static final int STATUS_FILLING = 1;//待填写
	public static final int STATUS_FILLED = 2;//已填写
	
	/**
	 * 试卷答案
	 */
	private String answer;
	
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 问卷id
	 */
	private String questionnaire_id;
	
	/**
	 * 答卷时间
	 */
	private Date date;
	
	/**
	 * 状态
	 */
	private int status;
	
	/**
	 * 问卷标题
	 */
	private String title;
	
	/**
	 * 问卷内容
	 */
	private String content;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 用户默认部门
	 */
	private String userDepartment;
	
	/**
	 * 问卷总分
	 * @return
	 */
	private int totalScore;
	
	/**
	 * 个人得分
	 * @return
	 */
	private int account;
	/**
	 * 问卷说明
	 */
	private String explains;
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getQuestionnaire_id() {
		return questionnaire_id;
	}

	public void setQuestionnaire_id(String questionnaire_id) {
		this.questionnaire_id = questionnaire_id;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDepartment() {
		return userDepartment;
	}

	public void setUserDepartment(String userDepartment) {
		this.userDepartment = userDepartment;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public String getExplains() {
		return explains;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}
}
