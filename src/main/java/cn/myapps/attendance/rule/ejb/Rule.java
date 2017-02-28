package cn.myapps.attendance.rule.ejb;

import java.util.List;

import cn.myapps.attendance.location.ejb.Location;
import cn.myapps.attendance.location.ejb.LocationProcess;
import cn.myapps.attendance.location.ejb.LocationProcessBean;
import cn.myapps.base.dao.ValueObject;

/**
 * 考勤规则
 * @author Happy
 *
 */
public class Rule extends ValueObject {

	private static final long serialVersionUID = -7202006054691399356L;
	
	public static final int TYPE_COMPANY = 0;
	public static final int TYPE_DEPT = 1;
	public static final int TYPE_USER = 2;
	
	/**
	 * 规则名称
	 */
	private String name;
	
	/**
	 * 适用范围(组织机构类型：部门|用户)
	 */
	private int organizationType;
	
	/**
	 * 组织机构(部门id集合|用户id集合)
	 */
	private String organizations;
	
	/**
	 * 组织机构(部门名称集合|用户名称集合)
	 */
	private String organizationsText;
	
	/**
	 * 考勤范围(该范围内员工可以签到/签退)
	 */
	private int range;
	
	/**
	 * 考勤地点
	 */
	private List<Location> locations;
	
	/**
	 * 考勤地点(文本)
	 */
	private String locationsText;
	
	/**
	 * 多时段考勤
	 */
	private Boolean multiPeriod;
	

	public Boolean getMultiPeriod() {
		return multiPeriod;
	}

	public void setMultiPeriod(Boolean multiPeriod) {
		this.multiPeriod = multiPeriod;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}

	public String getOrganizations() {
		return organizations;
	}

	public void setOrganizations(String organizations) {
		this.organizations = organizations;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public List<Location> getLocations() {
		if(locations ==null){
			try {
				LocationProcess process = new LocationProcessBean();
				locations = process.getLocationsByRule(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getOrganizationsText() {
		return organizationsText;
	}

	public void setOrganizationsText(String organizationsText) {
		this.organizationsText = organizationsText;
	}

	public String getLocationsText() {
		return locationsText;
	}

	public void setLocationsText(String locationsText) {
		this.locationsText = locationsText;
	}
	
}
