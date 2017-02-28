package cn.myapps.km.permission.ejb;

import cn.myapps.km.base.ejb.NObject;

public class ManagePermissionItem extends NObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7660734019762746118L;
	/**
	 * 资源类型
	 */
	private String resourceType;
	/**
	 * 资源ID
	 */
	private String resource;
	/**
	 * 权限范围（用户|角色|部门）
	 */
	private String scope;
	/**
	 * 
	 */
	private String owner;
	
	private String permission;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	
	
}
