package cn.myapps.core.usergroup.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.usergroup.dao.UserGroupSetDAO;

public class UserGroupSetProcessBean extends AbstractDesignTimeProcessBean<UserGroupSet> implements UserGroupSetProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6113783641600930372L;

	@Override
	protected IDesignTimeDAO<UserGroupSet> getDAO() throws Exception {
		return (UserGroupSetDAO) DAOFactory.getDefaultDAO(UserGroupSet.class.getName());
	}

	public void addUserToGroup(String[] userids, String userGroupId)
			throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			for(int i=0; i<userids.length; i++){
				if(!isUserInThisGroup(userids[i], userGroupId)){
					UserGroupSet userGroupSet = new UserGroupSet(userids[i], userGroupId);
					this.doCreate(userGroupSet);
				}
			}
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
		}
	}

	public boolean isUserInThisGroup(String userid, String UserGroupId)
			throws Exception {
		return ((UserGroupSetDAO)getDAO()).isUserInThisGroup(userid, UserGroupId);
	}

	public DataPackage<UserVO> getUserByGroup(String userGroupId, ParamsTable params)
			throws Exception {
		return ((UserGroupSetDAO)getDAO()).getUserByGroup(userGroupId, params);
	}

	public void deleteByUserGroupId(String userGroupId) throws Exception {
		((UserGroupSetDAO)getDAO()).deleteByUserGroupId(userGroupId);
	}

	public void deleteByUser(String[] userid, String userGroupId)
			throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			((UserGroupSetDAO)getDAO()).deleteByUser(userid, userGroupId);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
		}
	}

}
