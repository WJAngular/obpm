package cn.myapps.rm.usage.ejb;

import cn.myapps.rm.base.dao.DaoManager;
import cn.myapps.rm.base.dao.RuntimeDAO;
import cn.myapps.rm.base.ejb.AbstractBaseProcessBean;
import cn.myapps.rm.base.ejb.BaseObject;

public class ResourceUsageProcessBean extends AbstractBaseProcessBean<ResourceUsage>
		implements ResourceUsageProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3833895426776628591L;

	@Override
	public void doCreate(BaseObject no) throws Exception {
		getDAO().create(no);

	}

	@Override
	public void doRemove(String pk) throws Exception {
		getDAO().remove(pk);
	}

	@Override
	public void doUpdate(BaseObject no) throws Exception {
		getDAO().update(no);

	}

	@Override
	public BaseObject doView(String id) throws Exception {
		return getDAO().find(id);
	}

	@Override
	public RuntimeDAO getDAO() throws Exception {
		return DaoManager.getResourceUsageDAO(getConnection());
	}

}
