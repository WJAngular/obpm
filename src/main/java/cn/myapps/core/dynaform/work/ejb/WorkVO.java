package cn.myapps.core.dynaform.work.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.util.ProcessFactory;

/**
 * 
 * @author Happy
 * 
 */
public class WorkVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5273894768950344743L;

	private String applicationId;

	private String docId;

	private String formId;

	private String flowId;

	private String flowName;

	private String stateLabel;

	private String auditorNames;

	private String auditorList;

	private String subject;
	
	private String initiator;
	
	private String initiatorId;
	
	private String initiatorDept;
	
	private String initiatorDeptId;

	private Date firstProcessTime;
	
	private Date lastProcessTime;
	
	private String lastFlowOperation;
	
	/*标记待办事项已阅/未阅*/ 
	private Boolean read;  
	
	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Date getLastProcessTime() {
		return lastProcessTime;
	}

	public void setLastProcessTime(Date lastProcessTime) {
		this.lastProcessTime = lastProcessTime;
	}

	public Date getFirstProcessTime() {
		return firstProcessTime;
	}

	public void setFirstProcessTime(Date firstProcessTime) {
		this.firstProcessTime = firstProcessTime;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
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
	
	

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getInitiatorId() {
		return initiatorId;
	}

	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}
	
	public String getInitiatorDept() {
		return initiatorDept;
	}

	public void setInitiatorDept(String initiatorDept) {
		this.initiatorDept = initiatorDept;
	}

	public String getInitiatorDeptId() {
		return initiatorDeptId;
	}

	public void setInitiatorDeptId(String initiatorDeptId) {
		this.initiatorDeptId = initiatorDeptId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		if (subject == null) {
			this.subject = "";
		} else {
			this.subject = subject.replaceAll("&#160;", " ");
		}
	}
	
	public String getLastFlowOperation() {
		return this.lastFlowOperation;
	}
	
	public void setLastFlowOperation(String lastFlowOperation) {
		this.lastFlowOperation = lastFlowOperation;
	}
	
	public boolean isDeletable(WebUser user) throws Exception {
		boolean deletable = false;
		if (FlowType.START2RUNNING.equals(this.getLastFlowOperation())) {
			deletable = true;
			//还需要加入对于回退到草稿节点的的处理，暂时空着，以后完善
		}
		return deletable;
	}

	public static void main(String[] args) throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("_currpage", 1);
		params.setParameter("_pagelines", 1);
		// params.setParameter("_actorid", "");
		params.setParameter("_processType", "processing");
		WebUser user = new WebUser(new BaseUser());
		user.setId("11e0-463e-37537d91-8d25-d362e82b2291");
		user.setDomainid("11de-c138-782d2f26-9a62-8bacb70a86e1");
		DocumentProcess process = (DocumentProcess) ProcessFactory
				.createRuntimeProcess(DocumentProcess.class,
						"11e0-4638-a6848426-8d25-d362e82b2291");

		DataPackage<WorkVO> dp = process.queryWorks(params, user);
		System.out.println(dp.toString());
	}

}
