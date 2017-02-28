package cn.myapps.km.org.ejb;

import java.util.Collection;

import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.dao.NUserRoleSetDAO;
import cn.myapps.km.util.Sequence;
import cn.myapps.km.util.StringUtil;

public class NUserRoleSetProcessBean extends AbstractBaseProcessBean<NUserRoleSet>
		implements NUserRoleSetProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4161963512904713460L;

	@Override
	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getNUserRoleSetDAO(getConnection());
	}

	@Override
	public void doCreate(NObject no) throws Exception {
		
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
	public void doUpdate(NObject no) throws Exception {
		
		try {
			beginTransaction();
			getDAO().update(no);
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
	
	public void doRemoveByUser(String userId) throws Exception {
		try {
			((NUserRoleSetDAO)getDAO()).removeByUser(userId);
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
				NUserRoleSet no = new NUserRoleSet(userId,roleIds[i]);
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
	
	public Collection<NUserRoleSet> doQuertByUser(String userId) throws Exception {
		try {
			return ((NUserRoleSetDAO)getDAO()).quertByUser(userId);
		} catch (Exception e) {
			throw e;
		}
	}

}
