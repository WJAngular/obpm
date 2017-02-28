package cn.myapps.core.usergroup.ejb;


import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.usergroup.dao.UserGroupDAO;
import cn.myapps.util.ProcessFactory;

public class UserGroupProcessBean extends AbstractDesignTimeProcessBean<UserGroupVO> implements UserGroupProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6926182739199943036L;

	@Override
	protected IDesignTimeDAO<UserGroupVO> getDAO() throws Exception {
		return (UserGroupDAO) DAOFactory.getDefaultDAO(UserGroupVO.class.getName());
	}

	public DataPackage<UserGroupVO> getUserGroupsByUser(String userid)
			throws Exception {
		return ((UserGroupDAO)getDAO()).getUserGroupsByUser(userid);
	}

	public void deleteGroup(String userGroupId) throws Exception {
		try {
			UserGroupSetProcess userGroupSetProcess = (UserGroupSetProcess) ProcessFactory.createProcess(UserGroupSetProcess.class);
			PersistenceUtils.beginTransaction();
			//删除分组的用户信息
			userGroupSetProcess.deleteByUserGroupId(userGroupId);
			this.doRemove(userGroupId);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
		}
	}
}
