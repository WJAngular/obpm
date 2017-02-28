package cn.myapps.pm.activity.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.activity.ejb.Activity;
import cn.myapps.pm.base.dao.BaseDAO;

public interface ActivityDAO extends BaseDAO {

	public void removeByTask(String taskId) throws Exception;
	
	public Collection<Activity> query(ParamsTable params, WebUser user)throws Exception;
}
