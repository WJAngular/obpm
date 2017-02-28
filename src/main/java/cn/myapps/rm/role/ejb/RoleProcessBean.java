package cn.myapps.rm.role.ejb;

import java.util.Collection;

import cn.myapps.rm.base.dao.DaoManager;
import cn.myapps.rm.base.dao.RuntimeDAO;
import cn.myapps.rm.base.ejb.AbstractBaseProcessBean;
import cn.myapps.rm.base.ejb.BaseObject;
import cn.myapps.rm.role.dao.RoleDAO;


public class RoleProcessBean extends AbstractBaseProcessBean<Role>
		implements RoleProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161963512904713460L;

	@Override
	public RuntimeDAO getDAO() throws Exception {
		
		return  DaoManager.getRoleDAO(getConnection());
	}

	@Override
	public void doCreate(BaseObject no) throws Exception {
		Role role = (Role)no;
		try {
			beginTransaction();
			getDAO().create(role);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void doUpdate(BaseObject no) throws Exception {
		Role role = (Role)no;
		try {
			beginTransaction();
			getDAO().update(role);
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

	public Collection<Role> doGetRoles() throws Exception {
		return ((RoleDAO)getDAO()).getRoles();
	}
	
	public Collection<Role> doGetRolesByName(String name) throws Exception {
		return ((RoleDAO)getDAO()).getRolesByName(name);
	}
	
	public Collection<Role> doQueryRolesByUser(String userId) throws Exception {
		return ((RoleDAO)getDAO()).queryRolesByUser(userId);
	}

}
