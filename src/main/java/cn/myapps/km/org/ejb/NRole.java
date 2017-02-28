package cn.myapps.km.org.ejb;

import cn.myapps.km.base.ejb.NObject;

/**
 * 网盘角色
 * @author xiuwei
 *
 */
public class NRole extends NObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6255585707772036964L;
	
	/**
	 * 员工
	 */
	public static final int LEVEL_EMPLOYEE = 1;
	
	/**
	 * 部门文档管理员
	 */
	public static final int LEVEL_DEPT_ADMIN =2;
	
	/**
	 * 企业文档管理员
	 */
	public static final int LEVEL_DOMAIN_ADMIN = 3;


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
	
	
	
}
