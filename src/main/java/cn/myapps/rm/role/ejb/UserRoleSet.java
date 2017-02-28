package cn.myapps.rm.role.ejb;


import cn.myapps.rm.base.ejb.BaseObject;


/**
 * 用户与角色映射对象
 * @author Happy
 *
 */
public class UserRoleSet extends BaseObject {
	private static final long serialVersionUID = -2833496521401389663L;

	private String id;

	private String userId;

	private String roleId;

	public UserRoleSet() {

	}

	public UserRoleSet(String userId, String roleId) {
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
