package cn.myapps.km.org.ejb;

import java.util.Collection;

import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.dao.NRoleDAO;

public class NRoleProcessBean extends AbstractBaseProcessBean<NRole>
		implements NRoleProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161963512904713460L;

	@Override
	public NRuntimeDAO getDAO() throws Exception {
		
		return  DaoManager.getNRoleDAO(getConnection());
	}

	@Override
	public void doCreate(NObject no) throws Exception {
		NRole role = (NRole)no;
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
	public void doUpdate(NObject no) throws Exception {
		NRole role = (NRole)no;
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
	public NObject doView(String id) throws Exception {
		return getDAO().find(id);
	}

	public Collection<NRole> doGetRoles() throws Exception {
		return ((NRoleDAO)getDAO()).getRoles();
	}
	
	public Collection<NRole> doGetRolesByName(String name) throws Exception {
		return ((NRoleDAO)getDAO()).getRolesByName(name);
	}
	
	public Collection<NRole> doQueryRolesByUser(String userId) throws Exception {
		return ((NRoleDAO)getDAO()).queryRolesByUser(userId);
	}

}
