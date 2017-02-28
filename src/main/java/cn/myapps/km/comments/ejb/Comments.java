package cn.myapps.km.comments.ejb;

import java.util.Date;

import cn.myapps.km.base.ejb.NObject;

/**
 * 评价
 * @author linda
 *
 */
public class Comments extends NObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8576665495732868654L;

	/**
	 * 评价文件的ID
	 */
	private String fileId;
	
	/**
	 * 评价的用户ID
	 */
	private String userId;
	
	/**
	 * 评价的用户的名字
	 */
	private String userName;
	
	/**
	 * 评价的日期
	 */
	private Date assessmentDate;
	
	
	public static final int STATE_GOOD = 4;
	public static final int STATE_BAD = 0;
	/**
	 * 评定的状态（好评-1/差评-0）
	 */
	private boolean good = false;
	
	private boolean bad = false;
	
	/**
	 * 评价内容
	 */
	private String content;

	
	
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public Date getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(Date assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public boolean isBad() {
		return bad;
	}

	public void setBad(boolean bad) {
		this.bad = bad;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
 
