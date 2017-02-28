package cn.myapps.km.org.ejb;

import java.io.Serializable;

import cn.myapps.km.base.ejb.NObject;

/**
 * 用户与角色映射对象
 * @author xiuwei
 *
 */
public class NUserRoleSet extends NObject {
	private static final long serialVersionUID = -2833496521401389663L;

	private String id;

	private String userId;

	private String roleId;

	public NUserRoleSet() {

	}

	public NUserRoleSet(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
