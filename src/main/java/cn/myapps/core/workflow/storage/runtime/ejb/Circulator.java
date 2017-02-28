package cn.myapps.core.workflow.storage.runtime.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;

/**
 * 传阅者
 * @author happy
 *
 */
public class Circulator extends ValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -736001366916806969L;
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
	
	
	public Circulator() {
		super();
	}
	/**
	 * @param doc
	 * @param nodeRT
	 * @param userVO
	 * @param deadline
	 */
	public Circulator(Document doc, NodeRT nodeRT, BaseUser userVO,Date deadline) {
		this.setName(userVO.getName());
		this.setUserId(userVO.getId());
		this.setDocId(doc.getId());
		this.setNodertId(nodeRT.getId());
		this.setFlowstatertId(doc.getStateid());
		this.setCcTime(new Date());
		if (deadline != null) {
			this.setDeadline(new java.sql.Timestamp(deadline.getTime()));
		} else {
			this.setDeadline(null);
		}
		this.setDomainid(doc.getDomainid());
		this.setApplicationid(doc.getApplicationid());
	}
	
	public String toHtmlText(WebUser user, String summaryCfgId) throws Exception {
		StringBuffer html = new StringBuffer();

		html.append("<input moduleType='pending' type='hidden'");
		html.append(" id='" + this.getDocId() + "'");
		html.append(" formId='" + this.getFormId() + "'");
		html.append(" summaryCfgId='" + summaryCfgId + "'");
		html.append(" Summary='" + this.getSummary() + "'");
		if (this.isRead()) {
			html.append(" isread='true'");
		}
		html.append(" />");
		return html.toString();

	}
	
	/**
	 * 拼装手机端待办XML
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String toMbXMLText(WebUser user, String summaryCfgId,String title) throws Exception {
		StringBuffer html = new StringBuffer();
		if (this.isRead()) {
			html.append("<"+MobileConstant.TAG_PENDING+" "+MobileConstant.ATT_ID+"='"+this.getId()+"' "+MobileConstant.ATT_FORMID+"='"+this.getFormId()+"' "+MobileConstant.ATT_SUMMARYCFGID+"='"+summaryCfgId+"'>");
			html.append("["+title+"](已读)"+this.getSummary()+"</"+MobileConstant.TAG_PENDING+">");
		} else {
			html.append("<"+MobileConstant.TAG_PENDING+" "+MobileConstant.ATT_ID+"='"+this.getId()+"' "+MobileConstant.ATT_FORMID+"='"+this.getFormId()+"' "+MobileConstant.ATT_SUMMARYCFGID+"='"+summaryCfgId+"'>");
			html.append("["+title+"](未读)"+this.getSummary()+"</"+MobileConstant.TAG_PENDING+">");
		}
		return html.toString();
	}
	

}
