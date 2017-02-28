package cn.myapps.core.usergroup.ejb;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;

public interface UserGroupProcess extends IDesignTimeProcess<UserGroupVO> {
	
	/**
	 * 根据用户返回用户的组信息
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserGroupVO> getUserGroupsByUser(String userid) throws Exception;
	
	/**
	 * 删除用户分组
	 * @param userGroupId
	 * @throws Exception
	 */
	public void deleteGroup(String userGroupId) throws Exception;
}
