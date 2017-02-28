package cn.myapps.core.user.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface UserDefinedProcess extends IDesignTimeProcess<UserDefined> {
	public Collection<UserDefined> doViewByApplication(String applicationId)
	throws Exception;

public int doViewCountByName(String name, String applicationid) throws Exception;


public DataPackage<UserDefined> getDatapackage(String hql, ParamsTable params) throws Exception;

/**
 * 获取用户自定义的首页
 * @param user
 * 		当前登录用户
 * @param applicationId
 * 		软件id
 * @return
 * @throws Exception
 */
public UserDefined doFindMyCustomUserDefined(WebUser user) throws Exception;

/**
 * 根据传入的软件id获取软件下的所有已发布的首页集合
 * @param applicationId
 * 		软件id
 * @return
 * @throws Exception
 */
public Collection<UserDefined> doQueryPublishedUserDefinedsByApplication(String applicationId) throws Exception;

}
