package cn.myapps.core.user.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;

public interface UserDefinedDAO extends IDesignTimeDAO<UserDefined> {

	public Collection<UserDefined> findByApplication(String applicationId) throws Exception;

	public int queryCountByName(String name, String applicationid) throws Exception;
//	public abstract UserDefined login(String loginno, String domain)
//	throws Exception;

	public UserDefined login(String name) throws Exception;
	
	/**
	 * 获取用户自定义的首页
	 * @param user
	 * 		当前登录用户
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public UserDefined findMyCustomUserDefined(WebUser user) throws Exception;
	
	/**
	 * 根据传入的软件id获取软件下的所有已发布的首页集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public Collection<UserDefined> queryPublishedUserDefinedsByApplication(String applicationId) throws Exception;
}
