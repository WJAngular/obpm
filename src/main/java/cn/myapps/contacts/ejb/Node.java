package cn.myapps.contacts.ejb;

/**
 * 通讯录组织架构树的节点对象
 * @author Happy
 *
 */
public class Node {
	
	public static final int TYPE_USER = 1;
	public static final int TYPE_DEPT = 2;
	public static final int TYPE_APPLICATION =3 ;
	public static final int TYPE_ROLE = 4 ;
	
	private String id;

	private String name;
	
	private int type;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	} 
	
}
