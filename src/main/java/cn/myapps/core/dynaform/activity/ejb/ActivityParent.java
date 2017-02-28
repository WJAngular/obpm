package cn.myapps.core.dynaform.activity.ejb;

import java.util.Set;

public interface ActivityParent {
	public String getId();
	
	public String getName();

	public void setActivitys(Set<Activity> activitys);

	public Set<Activity> getActivitys();

	public String getActivityXML();

	public void setActivityXML(String activityXML);
	
	public String getApplicationid();
	
	/**
	 * 根据ID获取按钮
	 * 
	 * @param id 按钮ID
	 *            
	 * @return 按钮
	 */
	public Activity findActivity(String id);

	/**
	 * 获取完整名称
	 * 
	 * @return 元素全名，包括
	 */
	public String getFullName();
	
	public String getSimpleClassName();
}
