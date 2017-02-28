package cn.myapps.contacts.ejb;

/**
 * 用户节点
 * @author Happy
 *
 */
public class User extends Node{
	
	/**
	 * 手机号码。
	 */
	private String mobile;
	
	/**
	 * 手机号码。
	 */
	private String mobile2;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 头像(url)
	 */
	private String avatar;
	
	/**
	 * 部门名称
	 */
	private String dept;
	
	private String deptId;
	
	private String loginNo;
	
	private String domainId;

	public User() {
		super();
		setType(Node.TYPE_USER);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getLoginNo() {
		return loginNo;
	}

	public void setLoginNo(String loginNo) {
		this.loginNo = loginNo;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	
	
}
