package cn.myapps.core.logger.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface LogProcess extends IDesignTimeProcess<LogVO> {

	public DataPackage<LogVO> getLogsByDomain(ParamsTable params, WebUser user) throws Exception;
	
	public void deleteLogsByUser(String userid) throws Exception;
}
