package cn.myapps.km.baike.category.ejb;

import cn.myapps.km.base.ejb.NObject;

public class Category extends NObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8169298410747019602L;
	
	/**
	 * 对分类的描述
	 */
	private String description;
	
	/**
	 * 上级分类ID（暂为预留字段）
	 */
	private String parentId;
	
	/**
	 * 排序号
	 */
	private int orderby;

	/**
	 * 类型名称
	 */
	private String name;
	
	private Category category;
	
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Category) {
			Category category = (Category)obj;
			return this.getId().equals(category.getId()) 
					&& this.parentId.equals(category.getParentId())
					&& this.getName().equals(category.getName())
					&& this.getDomainId().equals(category.getDomainId())
					&& this.description.equals(category.description)
					&& this.orderby == category.orderby;
		}
		return false;
	}

	
}
