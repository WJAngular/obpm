/*
 * Created on 2005-4-4
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.workflow.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;









import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.workflow.utility.ActivityPermission;


public class ActivityPermissionList implements Serializable {

	private static final long serialVersionUID = -7343012361341645957L;
	
	
	
	private Collection<ActivityPermission> data = new ArrayList<ActivityPermission>();
	
	public ActivityPermissionList() {

	}

	public void add(ActivityPermission fp) {
		if (fp != null) {
			data.add(fp);
		}
	}

	/**
	 * 将获取到的JSONArray数据转化为ActivityPermissionList集合
	 * 
	 * 数据示例：
	 * [
	 *  {'id':'11e5-a794-850b0b94-a918-957e979aaddf','permission':'show'},
	 *  {'id':'11e5-8d98-8c413ffa-8773-6b4c04922663','permission':'show'},
	 *  {'id':'11e5-a7a1-e6cac6ae-ac64-21e7937ef61d','permission':'hide'}
	 * ]
	 * 
	 * @param permissionMap
	 * @return
	 */
	public static ActivityPermissionList parser(JSONArray permissionMap) {
		
		ActivityPermissionList permissionList = new ActivityPermissionList();
		
		if (permissionMap == null || permissionMap.isEmpty()) {
			return permissionList;
		}
		
		Iterator<JSONObject> iterator = permissionMap.iterator();
	
		ActivityPermission tmp ;
		
		while(iterator.hasNext()){
			JSONObject object = iterator.next();
			tmp = new ActivityPermission();
			tmp.setActivityId(object.getString("id"));
			tmp.setPermisstionType(object.getString("permission"));
			permissionList.add(tmp);
		}
		
		return permissionList;
	}

	public void remove(ActivityPermission activityPerm) {
		data.remove(activityPerm);
	}

	public void clear() {
		data.clear();
	}

	/**
	 * 判断操作按钮是否隐藏
	 * 
	 * @param activityId 按钮ID
	 * @return
	 */
	public String checkPermission(String activityId) {
		
		if(activityId == null){
			return ActivityPermission.ACTIVITY_PERMESSION_SHOW;
		}
		if (data == null || data.size() <= 0) {
			return ActivityPermission.ACTIVITY_PERMESSION_SHOW;
		}
		
		Iterator<ActivityPermission> iters = data.iterator();
		while (iters.hasNext()) {
			ActivityPermission activityPerm = (ActivityPermission) iters.next();
			if (activityId.equals(activityPerm.getActivityId())) {
				return ActivityPermission.ACTIVITY_PERMESSION_HIDE;
			}
		}
		return ActivityPermission.ACTIVITY_PERMESSION_SHOW;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<ActivityPermission> iter = data.iterator();
		while (iter.hasNext()) {
			ActivityPermission activityPerm = (ActivityPermission) iter.next();
			sb.append(activityPerm.toString());
			sb.append(";");
		}
		return sb.toString();
	}
}
