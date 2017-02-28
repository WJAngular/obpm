package cn.myapps.core.workflow.storage.runtime.intervention.ejb;

import java.util.Date;

import cn.myapps.base.dao.ValueObject;

/**
 * 工作流干预对象
 * @author Happy
 *
 */
public class FlowInterventionVO extends ValueObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1869526076159582039L;
	/*待办状态*/
	public static final String STATUS_PENDING = "pending";
	/*完成状态*/
	public static final String STATUS_COMPLETED = "completed";
	
	/**
	 * 文档摘要
	 */
	private String summary;
	
	/**
	 * 流程名称
	 */
	private String flowName;
	
	/**
	 * 流程状态
	 */
	private String stateLabel;
	
	/**
	 * 发起人名字
	 */
	private String initiator;
	
	/**
	 * 发起人Id
	 */
	private String initiatorId;
	
	/**
	 *发起人部门
	 */
	private String initiatorDept;
	
	/**
	 *发起人部门Id
	 */
	private String initiatorDeptId;
	/**
	 * 最后处理人名字
	 */
	private String lastAuditor;
	
	/**
	 * 流程开始时间
	 */
	private Date firstProcessTime;
	
	/**
	 * 最后处理时间
	 */
	private Date lastProcessTime;
	
	private String flowId;
	
	private String formId;
	
	private String docId;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 当前审核人名称(以分","号隔开)
	 */
	private String auditorNames;
	
	/**
	 * 当前审批人列表（JSON格式描述）
	 */
	private String auditorList;
	
	/**
	 * 最后一次流程处理的代码
	 */
	private String lastFlowOperation;
	

	/**
	 * 获取文档摘要
	 * @return
	 * 		文档摘要
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * 设置文档摘要
	 * @param summary
	 * 		文档摘要
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 获取流程名称
	 * @return
	 * 		流程名称
	 */
	public String getFlowName() {
		return flowName;
	}

	/**
	 * 设置流程名称
	 * @param flowName
	 * 		流程名称
	 */
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/**
	 * 获取流程状态标签
	 * @return
	 * 		流程状态标签
	 */
	public String getStateLabel() {
		return stateLabel;
	}

	/**
	 * 设置流程状态标签
	 * @param stateLabel
	 * 		流程状态标签
	 */
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	/**
	 * 获取流程发起人姓名
	 * @return
	 * 		流程发起人姓名
	 */
	public String getInitiator() {
		return initiator;
	}

	/**
	 * 设置流程发起人姓名
	 * @param initiator
	 * 		流程发起人姓名
	 */
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	
	
	/**
	 * 获取流程发起人Id
	 * @return
	 * 		流程发起人Id
	 */
	public String getInitiatorId() {
		return initiatorId;
	}

	/**
	 * 设置流程发起人Id
	 * @param initiatorId
	 * 		流程发起人Id
	 */
	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}
	
	
	/**
	 * 获取发起人部门
	 * @return
	 *       发起人部门
	 */
	public String getInitiatorDept() {
		return initiatorDept;
	}

	/**
	 * 设置发起人部门
	 * @param initiatorDept
	 *          发起人部门
	 */         
	public void setInitiatorDept(String initiatorDept) {
		this.initiatorDept = initiatorDept;
	}
    /**
     * 获取发起人部门Id
     * @return
     *       发起人部门Id
     */   
	public String getInitiatorDeptId() {
		return initiatorDeptId;
	}

	/**
	 * 设置发起人部门Id
	 * @param initiatorDeptId
	 *            发起人部门Id
	 */
	public void setInitiatorDeptId(String initiatorDeptId) {
		this.initiatorDeptId = initiatorDeptId;
	}

	/**
	 * 获取流程最后处理人姓名
	 * @return
	 * 		流程最后处理人姓名
	 */
	public String getLastAuditor() {
		return lastAuditor;
	}

	/**
	 * 设置流程最后处理人姓名
	 * @param lastAuditor
	 * 		流程最后处理人姓名
	 */
	public void setLastAuditor(String lastAuditor) {
		this.lastAuditor = lastAuditor;
	}

	/**
	 * 获取流程开始时间
	 * @return
	 * 		流程开始时间
	 */
	public Date getFirstProcessTime() {
		return firstProcessTime;
	}

	/**
	 * 设置流程开始时间
	 * @param firstProcessTime
	 * 		流程开始时间
	 */
	public void setFirstProcessTime(Date firstProcessTime) {
		this.firstProcessTime = firstProcessTime;
	}

	/**
	 * 获取流程最后处理时间
	 * @return
	 * 		流程最后处理时间
	 */
	public Date getLastProcessTime() {
		return lastProcessTime;
	}

	/**
	 * 设置流程最后处理时间
	 * @param lastProcessTime
	 * 		流程最后处理时间
	 */
	public void setLastProcessTime(Date lastProcessTime) {
		this.lastProcessTime = lastProcessTime;
	}

	/**
	 * 获取关联的流程Id
	 * @return
	 */
	public String getFlowId() {
		return flowId;
	}

	/**
	 * 设置关联的流程ID
	 * @param flowId
	 */
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	/**
	 * 获取关联的文档ID
	 * @return
	 */
	public String getDocId() {
		return docId;
	}

	/**
	 * 设置关联的文档ID
	 * @param docId
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuditorNames() {
		return auditorNames;
	}

	public void setAuditorNames(String auditorNames) {
		this.auditorNames = auditorNames;
	}

	public String getAuditorList() {
		return auditorList;
	}

	public void setAuditorList(String auditorList) {
		this.auditorList = auditorList;
	}

	public String getLastFlowOperation() {
		return lastFlowOperation;
	}

	public void setLastFlowOperation(String lastFlowOperation) {
		this.lastFlowOperation = lastFlowOperation;
	}
	

}
