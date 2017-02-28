package cn.myapps.webservice.model;

public class SimpleDepartment {

	/**
	 * 主键
	 * 
	 * @uml.property name="id"
	 */
	private String id;

	/**
	 * 部门名称
	 * 
	 * @uml.property name="name"
	 */
	private String name;

	/**
	 * @uml.property name="code"
	 */
	private String code;

	/**
	 * @uml.property name="level"
	 */
	private int level;

	/**
	 * 企业域名称
	 */
	private String domainName;
	
	/**
	 * 上级部门名称
	 */
	private String superiorName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public void setSuperiorName(String superiorName) {
		this.superiorName = superiorName;
	}

	public String getSuperiorName() {
		return superiorName;
	}

}
