package cn.myapps.webservice.model;

/**
 * 我的工作(待办、已办)
 * @author Administrator
 *
 */
public class SimpleWork implements java.io.Serializable {

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
