package cn.myapps.core.usergroup.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.ejb.UserVO;

public interface UserGroupSetProcess extends IDesignTimeProcess<UserGroupSet>{
	
	/**
	 * 添加用户到目标分组
	 * @param userids
	 * @param UserGroupId
	 * @throws Exception
	 */
	public void addUserToGroup(String[] userids, String UserGroupId) throws Exception;
	
	/**
	 * 判断用户是否在此分组中
	 * @param userid
	 * @param UserGroupId
	 * @return
	 * @throws Exception
	 */
	public boolean isUserInThisGroup(String userid, String userGroupId) throws Exception;
	
	/**
	 * 返回分组的用户
	 * @param userGroupId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserVO> getUserByGroup(String userGroupId, ParamsTable params) throws Exception;
	
	/**
	 * 根据分组id删除分组信息
	 * @param userGroupId
	 * @throws Exception
	 */
	public void deleteByUserGroupId(String userGroupId) throws Exception;
	
	/**
	 * 删除分组中的用户信息
	 * @param userid
	 * @param userGroupId
	 * @throws Exception
	 */
	public void deleteByUser(String[] userid, String userGroupId) throws Exception;
}
