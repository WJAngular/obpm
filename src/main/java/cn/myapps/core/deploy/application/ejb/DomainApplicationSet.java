package cn.myapps.core.deploy.application.ejb;

import java.io.Serializable;

public class DomainApplicationSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -318522358059856425L;
	
	private String id;
	
	private String domainId;
	
	private String applicationId;
	
	private String weixinAgentId;
	
	

	public DomainApplicationSet() {
		super();
	}

	public DomainApplicationSet(String applicationId, String domainId,
			String weixinAgentId) {
		super();
		this.domainId = domainId;
		this.applicationId = applicationId;
		this.weixinAgentId = weixinAgentId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getWeixinAgentId() {
		return weixinAgentId;
	}

	public void setWeixinAgentId(String weixinAgentId) {
		this.weixinAgentId = weixinAgentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}
