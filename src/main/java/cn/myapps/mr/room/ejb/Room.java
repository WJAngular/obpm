package cn.myapps.mr.room.ejb;

import antlr.collections.List;
import cn.myapps.base.dao.ValueObject;

/**
 * 
 * @author Alvin
 *	会议室
 */
public class Room  extends ValueObject{
	/**
	 * 会议室名称
	 */
	private String name;
	/**
	 * 区域ID
	 */
	private String areaId;
	/**
	 * 区域名
	 */
	private String area;
	/**
	 * 会议室容纳人数
	 */
	private String number;
	/**
	 * 创建者ID
	 */
	private String creatorId;
	/**
	 * 创建者名称
	 */
	private String creator;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 设备
	 */
	private String equipment;
	
//	private List<Reservation> reservations;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
}
