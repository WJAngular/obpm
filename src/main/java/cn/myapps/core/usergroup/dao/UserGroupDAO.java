package cn.myapps.core.usergroup.dao;

import java.util.Collection;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.usergroup.ejb.UserGroupVO;

public interface UserGroupDAO extends IDesignTimeDAO<UserGroupVO> {
	/**
	 * 根据用户返回用户的组信息
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<UserGroupVO> getUserGroupsByUser(String userid) throws Exception;
}
