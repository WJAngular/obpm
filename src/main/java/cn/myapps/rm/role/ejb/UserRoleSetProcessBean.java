package cn.myapps.rm.role.ejb;

import java.util.Collection;

import cn.myapps.rm.base.dao.DaoManager;
import cn.myapps.rm.base.dao.RuntimeDAO;
import cn.myapps.rm.base.ejb.AbstractBaseProcessBean;
import cn.myapps.rm.base.ejb.BaseObject;
import cn.myapps.rm.role.dao.UserRoleSetDAO;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;


public class UserRoleSetProcessBean extends AbstractBaseProcessBean<UserRoleSet>
		implements UserRoleSetProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161963512904713460L;

	@Override
	public RuntimeDAO getDAO() throws Exception {
		return DaoManager.getUserRoleSetDAO(getConnection());
	}

	@Override
	public void doCreate(BaseObject no) throws Exception {
		
		try {
			beginTransaction();
			getDAO().create(no);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void doUpdate(BaseObject no) throws Exception {
		
		try {
			beginTransaction();
			
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void doRemove(String pk) throws Exception {
		try {
			beginTransaction();
			getDAO().remove(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public BaseObject doView(String id) throws Exception {
		
		return getDAO().find(id);
	}
	
	public void doRemoveByUser(String userId) throws Exception {
		try {
			((UserRoleSetDAO)getDAO()).removeByUser(userId);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void doUpdateUserRoleSet(String userId, String[] roleIds)
			throws Exception {
		try {
			if(StringUtil.isBlank(userId)) return;
			beginTransaction();
			doRemoveByUser(userId);
			for (int i = 0; i < roleIds.length; i++) {
				if(StringUtil.isBlank(roleIds[i])) continue;
				UserRoleSet no = new UserRoleSet(userId,roleIds[i]);
				no.setId(Sequence.getSequence());
				doCreate(no);
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public Collection<UserRoleSet> doQuertByUser(String userId) throws Exception {
		try {
			return ((UserRoleSetDAO)getDAO()).quertByUser(userId);
		} catch (Exception e) {
			throw e;
		}
	}

}
