package cn.myapps.km.permission.ejb;

import cn.myapps.km.base.ejb.NObject;

/**
 * 部门目录管理授权
 * @author xiuwei
 *
 */
public class ManagePermission extends NObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2186002980269610652L;
	
	public static final String RESOURCE_TYPE_DISK = "disk";
	public static final String RESOURCE_TYPE_DIR = "directory";
	
	public static final String SCOPE_USER = "user";
	public static final String SCOPE_ROLE = "role";
	public static final String SCOPE_DEPT = "department";
	
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
	private String ownerIds;
	/**
	 * 名称
	 */
	private String ownerNames;
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
	public String getOwnerIds() {
		return ownerIds;
	}
	public void setOwnerIds(String ownerIds) {
		this.ownerIds = ownerIds;
	}
	public String getOwnerNames() {
		return ownerNames;
	}
	public void setOwnerNames(String ownerNames) {
		this.ownerNames = ownerNames;
	}
	
	

}
