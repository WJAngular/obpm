package cn.myapps.pm.activity.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.ejb.BaseProcess;

public interface ActivityProcess extends BaseProcess<Activity> {
	
	public void removeByTask(String taskId) throws Exception;
	
	public Collection<Activity> doQueryActivity(ParamsTable params, WebUser user)throws Exception;

}
