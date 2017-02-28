package cn.myapps.attendance.util.dataprovider.easyui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * EsayUI 部门数据模型
 * @author Happy
 *
 */
public class Department {

	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 名字
	 */
	private String text;
	
	/**
	 * 子部门
	 */
	private List<Department> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Department> getChildren() {
		return children;
	}

	public void setChildren(List<Department> children) {
		this.children = children;
	}
	
	public void addChildren(Department department) {
		if(children ==null ) children = new ArrayList<Department>();
		children.add(department);
	}
	
}
