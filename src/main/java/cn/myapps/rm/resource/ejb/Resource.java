package cn.myapps.rm.resource.ejb;

import java.util.Date;

import cn.myapps.rm.base.ejb.BaseObject;

/**
 * 资源
 * @author Happy
 *
 */
public class Resource extends BaseObject {
	
	private static final long serialVersionUID = -4227350275530258402L;
	
	/*会议室类型*/
	public static final String TYPE_MEETING_ROOM = "meeting room";
	/*车辆类型*/
	public static final String TYPE_CAR = "car";
	/*办公用品类型*/
	public static final String TYPE_OFFICE_SUPPLIES = "office supplies";
	/*其他类型*/
	public static final String TYPE_OTHER = "other";
	
	/**
	 * 资源名称
	 */
	private String name;
	
	/**
	 * 序号（编号、流水号）
	 */
	private String serial;
	
	/**
	 * 资源描述
	 */
	private String description;
	
	/**
	 * 资源类型
	 */
	private String type;
	
	/**
	 *创建人
	 */
	private String creator;
	/**
	 *创建人id
	 */
	private String creatorId;
	/**
	 *创建时间
	 */
	private Date createDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
