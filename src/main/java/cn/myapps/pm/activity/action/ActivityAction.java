package cn.myapps.pm.activity.action;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.activity.ejb.Activity;
import cn.myapps.pm.activity.ejb.ActivityProcess;
import cn.myapps.pm.activity.ejb.ActivityProcessBean;
import cn.myapps.pm.base.action.BaseAction;

public class ActivityAction extends BaseAction<Activity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6018526558471229631L;

	public ActivityAction() {
		super();
		content = new Activity();
		process = new ActivityProcessBean();
	}
	
	public String doQuery(){
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			Collection<Activity> datas = ((ActivityProcess)process).doQueryActivity(params, user);
			addActionResult(true, null, datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
		
	}
	

}
