package cn.myapps.webservice.model;

import java.util.Date;

/**
 * 传阅文档对象(待阅、已阅)
 * @author Administrator
 *
 */
public class SimpleCirculator implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -108474646612557727L;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 用户
	 */
	private String userId;
	/**
	 * 文档
	 */
	private String docId;
	
	/**
	 * 节点
	 */
	private String nodertId;
	/**
	 * 流程实例
	 */
	private String flowstatertId;
	/**
	 * 抄送时间
	 */
	private Date ccTime;
	/**
	 * 阅读时间
	 */
	private Date readTime;
	/**
	 * 阅读期限
	 */
	private Date deadline;
	/**
	 * 是否已阅
	 */
	private boolean read = false;
	
	/**
	 * 摘要
	 */
	private String summary;
	
	private String formId;
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	/**
	 * 获取名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取用户Id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置用户Id
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取文档Id
	 * @return
	 */
	public String getDocId() {
		return docId;
	}
	/**
	 * 设置文档ID
	 * @param docId
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}
	/**
	 * 获取节点Id
	 * @return
	 */
	public String getNodertId() {
		return nodertId;
	}
	/**
	 * 设置节点Id
	 * @param nodertId
	 */
	public void setNodertId(String nodertId) {
		this.nodertId = nodertId;
	}
	/**
	 * 获取流程实例Id
	 * @return
	 */
	public String getFlowstatertId() {
		return flowstatertId;
	}
	/**
	 * 设置流程实例Id
	 * @param flowstatertId
	 */
	public void setFlowstatertId(String flowstatertId) {
		this.flowstatertId = flowstatertId;
	}
	/**
	 * 获取抄送时间
	 * @return
	 */
	public Date getCcTime() {
		return ccTime;
	}
	/**
	 * 设置抄送时间
	 * @param ccTime
	 */
	public void setCcTime(Date ccTime) {
		this.ccTime = ccTime;
	}
	/**
	 * 获取阅读时间
	 * @return
	 */
	public Date getReadTime() {
		return readTime;
	}
	/**
	 * 设置阅读时间
	 * @param readTime
	 */
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	/**
	 * 获取阅读期限
	 * @return
	 */
	public Date getDeadline() {
		return deadline;
	}
	/**
	 * 设置阅读期限
	 * @param deadline
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	/**
	 * 是否已读
	 * @return
	 */
	public boolean isRead() {
		return read;
	}
	/**
	 * 设置是否已读
	 * @param read
	 */
	public void setRead(boolean read) {
		this.read = read;
	}
}
