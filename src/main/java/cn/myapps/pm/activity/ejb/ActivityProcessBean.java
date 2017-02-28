package cn.myapps.pm.activity.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.activity.dao.ActivityDAO;
import cn.myapps.pm.base.dao.BaseDAO;
import cn.myapps.pm.base.dao.DaoManager;
import cn.myapps.pm.base.ejb.AbstractBaseProcessBean;


public class ActivityProcessBean extends AbstractBaseProcessBean<Activity>
		implements ActivityProcess {

	private static final long serialVersionUID = 3833895426776628591L;

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getActivityDAO(getConnection());
	}

	public void removeByTask(String taskId) throws Exception {
		try {
			beginTransaction();
			((ActivityDAO)getDAO()).removeByTask(taskId);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	public Collection<Activity> doQueryActivity(ParamsTable params, WebUser user)
			throws Exception {
		return ((ActivityDAO)getDAO()).query(params, user);
	}


}
