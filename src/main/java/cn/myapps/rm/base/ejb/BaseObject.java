package cn.myapps.rm.base.ejb;

import java.io.Serializable;


public class BaseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1294611187794734169L;

	/**
	 * id
	 */
	private String id;
	
	/**
	 * 企业域ID
	 */
	private String domainId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

}
