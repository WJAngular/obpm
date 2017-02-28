package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.Date;

import net.sf.json.JSONObject;
import cn.myapps.km.util.StringUtil;

/**
 * 流程审批历史
 * @author Happy
 *
 */
public class FlowHistoryVO {
	

	/**
	 * 开始节点名称
	 */
	private String startNodeName;
	
	/**
	 * 到达节点名称
	 */
	private String targetNodeName;
	
	/**
	 * 审批人名称
	 */
	private String auditorName;
	
	/**
	 * 审批人id
	 */
	private String auditorId;
	
	/**
	 * 代理人名称
	 */
	private String agentAuditorName;
	
	/**
	 * 审批意见（备注）
	 */
	private String attitude;
	
	/**
	 * 签名
	 */
	private String signature;
	
	/**
	 * 审批日期
	 */
	private Date processtime;
	
	/**
	 * 审批动作代码
	 */
	private String flowOperation;
	
	/**
	 * 流程实例Id
	 */
	private String folowStateId;
	
	/**
	 * 流程路线历史id
	 */
	private String historyId;
	
	/**
	 * 开始节点id
	 */
	private String startNodeId;
	
	/**
	 * 目标节点id
	 */
	private String targetNodeId;
	
	public String getFolowStateId() {
		return folowStateId;
	}

	public void setFolowStateId(String folowStateId) {
		this.folowStateId = folowStateId;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getStartNodeName() {
		return startNodeName;
	}

	public void setStartNodeName(String startNodeName) {
		this.startNodeName = startNodeName;
	}

	public String getTargetNodeName() {
		return targetNodeName;
	}

	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAttitude() {
		return attitude;
	}

	public void setAttitude(String attitude) {
		this.attitude = attitude;
	}

	public Date getProcesstime() {
		return processtime;
	}

	public void setProcesstime(Date processtime) {
		this.processtime = processtime;
	}

	public String getFlowOperation() {
		return flowOperation;
	}

	public void setFlowOperation(String flowOperation) {
		this.flowOperation = flowOperation;
	}

	public String getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(String startNodeId) {
		this.startNodeId = startNodeId;
	}

	public String getTargetNodeId() {
		return targetNodeId;
	}

	public void setTargetNodeId(String targetNodeId) {
		this.targetNodeId = targetNodeId;
	}

	public String getAgentAuditorName() {
		return agentAuditorName;
	}

	public void setAgentAuditorName(String agentAuditorName) {
		this.agentAuditorName = agentAuditorName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getSignatureImageDate(){
		StringBuilder out = new StringBuilder();
		if(!StringUtil.isBlank(signature)){
			JSONObject json = JSONObject.fromObject(signature);
			out.append("data:").append(json.getString("type")).append(",").append(json.getString("data"));
		}
		return out.toString();
				
	}

	public String getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}
	
	
	
}
