package cn.myapps.contacts.ejb;

import java.util.ArrayList;
import java.util.List;


/**
 * 部门节点
 * @author Happy
 *
 */
public class Department extends Node{
	
	/**
	 * 孩子节点
	 */
	private List<Node> children = new ArrayList<Node>();


	public Department() {
		super();
		setType(TYPE_DEPT);
	}

	public List<?> getChildren() {
		return children;
	}

	public void addChildrens(List<Node> childrens) {
		children.addAll(childrens);
	}
	
	public void addChildren(Node child) {
		if(children == null ) children = new ArrayList<Node>();
		children.add(child);
	}
}
