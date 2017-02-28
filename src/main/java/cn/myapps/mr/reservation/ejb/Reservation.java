package cn.myapps.mr.reservation.ejb;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.base.dao.ValueObject;

public class Reservation extends ValueObject{

	/**
	 * 会议(预定)名
	 */
	private String name;
	/**
	 * 会议内容
	 */
	private String content;
	/**
	 * 区域ID
	 */
	private String areaId;
	
	/**
	 * 区域名称
	 */
	private String area;
	
	/**
	 * 会议室ID
	 */
	private String roomId;
	
	/**
	 * 会议室名称
	 */
	private String room;
	
	/**
	 * 预定人ID
	 */
	private String creatorId;
	
	/**
	 * 预定人名称
	 */
	private String creator;
	
	/**
	 * 预定人电话
	 */
	private String creatorTel;
	
	/**
	 * 会议开始时间
	 */
	private Date startTime;
	
	/**
	 * 会议结束时间
	 */
	private Date endTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
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

	public String getCreatorTel() {
		return creatorTel;
	}

	public void setCreatorTel(String creatorTel) {
		this.creatorTel = creatorTel;
	}

	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
