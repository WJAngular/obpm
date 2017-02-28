package cn.myapps.core.usergroup.ejb;

import cn.myapps.base.dao.ValueObject;

public class UserGroupVO extends ValueObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2404428295585683376L;
	
	/**
	 * 通讯录用户组组名
	 */
	private String name;
	
	/**
	 * 所有者id
	 */
	private String ownerId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
