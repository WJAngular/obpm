package cn.myapps.rm.role.ejb;

import cn.myapps.rm.base.ejb.BaseObject;

/**
 * 角色
 * @author Happy
 *
 */
public class Role extends BaseObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6255585707772036964L;
	
	/**
	 * 员工
	 */
	public static final int LEVEL_EMPLOYEE = 1;
	
	/**
	 * 资源管理员
	 */
	public static final int LEVEL_ADMIN =2;
	
	/**
	 * 超级管理员
	 */
	public static final int LEVEL_SUPER_ADMIN = 3;
	
	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 等级
	 */
	private int level;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
