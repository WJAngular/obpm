/*
 * Created on 2005-4-4
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.workflow.utility;

import java.io.Serializable;

public class ActivityPermission implements Serializable {

	private static final long serialVersionUID = -8919129021907863954L;
	
	private String permisstionType;
	private String activityId;

	/**
	 * 操作按钮显示
	 */
	public static final String ACTIVITY_PERMESSION_SHOW = "show";
	/**
	 * 操作按钮隐藏
	 */
	public static final String ACTIVITY_PERMESSION_HIDE = "hide";
	public ActivityPermission() {

	}

	public String getPermisstionType() {
		return permisstionType;
	}

	public void setPermisstionType(String permisstionType) {
		this.permisstionType = permisstionType;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	@Override
	public String toString() {
		return "ActivityPermission [permisstionType=" + permisstionType
				+ ", activityId=" + activityId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activityId == null) ? 0 : activityId.hashCode());
		result = prime * result
				+ ((permisstionType == null) ? 0 : permisstionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityPermission other = (ActivityPermission) obj;
		if (activityId == null) {
			if (other.activityId != null)
				return false;
		} else if (!activityId.equals(other.activityId))
			return false;
		if (permisstionType == null) {
			if (other.permisstionType != null)
				return false;
		} else if (!permisstionType.equals(other.permisstionType))
			return false;
		return true;
	}

    


	
}
