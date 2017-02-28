package cn.myapps.pm.task.ejb;

import java.io.Serializable;

import org.apache.struts2.json.annotations.JSON;

/**
 * 关注人
 * 
 * @author Happy
 *
 */
public class Follower implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2131584485058420050L;

	private String userId;
	
	private String userName;
	
	private String domainId;

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

	@JSON(serialize=false)
	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	
	
	
}
