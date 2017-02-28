package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Date;

import cn.myapps.base.dao.ValueObject;

/**
 * 流程催单历史
 * @author Happy
 *
 */
public class FlowReminderHistory extends ValueObject {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2840039751910436327L;

	/**
	 * 催办内容
	 */
	private String content;
	
	/**
	 * 操作者id
	 */
	private String userId;
	
	/**
	 * 操作者名称
	 */
	private String userName;
	
	/**
	 * 节点名称
	 */
	private String nodeName;
	
	/**
	 * 文档id
	 */
	private String docId;
	
	/**
	 * 流程实例id
	 */
	private String flowInstanceId;
	
	private Date processTime;
	

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


	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

}
