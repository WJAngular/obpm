package cn.myapps.km.category.ejb;

import cn.myapps.km.base.ejb.NObject;

/**
 * 类别
 * @author xiuwei
 *
 */
public class Category extends NObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2196207973814086405L;
	
	private String description;
	
	private String parentId;
	
	private int sort;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
