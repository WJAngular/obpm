package com.teemlink.saas.weioa.suite.ejb;

import java.io.Serializable;

/**
 * 微信套件应用
 * @author Happy
 *
 */
public class Suite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3500606164686248581L;
	
	/**
	 * 未授权
	 */
	public static final int NO_AUTH = 0;
	/**
	 * 已授权
	 */
	public static final int HAS_AUTH = 1;
	/**
	 * 取消授权
	 */
	public static final int CAHCEL_AUTH = 2;
	
	
	/**
	 * 授权方应用id
	 */
	private String agentid;
	
	/**
	 * 应用名称
	 */
	private String name;
	
	/**
	 * 应用描述
	 */
	private String description;
	
	/**
	 * logo图片链接
	 */
	private String logourl;
	/**
	 * 所属企业域
	 */
	private String domainid;
	
	/**
	 * 套件服务提供方应用id
	 */
	private String appid;
	
	/**
	 * 应用状态
	 */
	private int status;

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

}
