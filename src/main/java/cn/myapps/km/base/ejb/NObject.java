package cn.myapps.km.base.ejb;

import java.io.Serializable;


public class NObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1294611187794734169L;

	/**
	 * id
	 */
	private String id;
	
	/**
	 * name
	 */
	private String name;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

}
