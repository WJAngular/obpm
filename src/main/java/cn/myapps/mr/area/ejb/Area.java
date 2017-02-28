package cn.myapps.mr.area.ejb;

import cn.myapps.base.dao.ValueObject;

public class Area extends ValueObject{

	/**
	 * 区域名称
	 */
	private String name;
	/**
	 * 创建人ID
	 */
	private String creatorId;
	/**
	 * 创建人名称
	 */
	private String creator;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
}
