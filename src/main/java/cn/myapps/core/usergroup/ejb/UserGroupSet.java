package cn.myapps.core.usergroup.ejb;

import java.io.Serializable;

import cn.myapps.base.dao.ValueObject;

public class UserGroupSet extends ValueObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1724765050118014841L;
	
	private String id;
	
	private String userId;
	
	private String userGroupId;

	public UserGroupSet(){
	}
	
	public UserGroupSet(String userId, String userGroupId){
		this.userId = userId;
		this.userGroupId = userGroupId;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}
}
