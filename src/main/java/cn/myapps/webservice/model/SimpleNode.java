package cn.myapps.webservice.model;

public class SimpleNode implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845446256737917667L;

	private java.lang.String id;	//节点id
	
	private java.lang.String name;		//节点名称
	
	public SimpleNode(){
	}

	/**
	 * 获取节点主键id
	 * @return id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * 设置节点主键id
	 * @param id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 获取节点名称
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * 设置节点名称
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

}
