package cn.myapps.attendance.location.ejb;

import cn.myapps.base.dao.ValueObject;

/**
 * 地点
 * @author Happy
 *
 */
public class Location extends ValueObject {

	private static final long serialVersionUID = 951210112464572381L;

	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 经度
	 */
	private double longitude;
	
	/**
	 * 纬度
	 */
	private double latitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
}
