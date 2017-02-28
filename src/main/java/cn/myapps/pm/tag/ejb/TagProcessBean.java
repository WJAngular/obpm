package cn.myapps.pm.tag.ejb;

import cn.myapps.pm.base.dao.BaseDAO;
import cn.myapps.pm.base.dao.DaoManager;
import cn.myapps.pm.base.ejb.AbstractBaseProcessBean;


public class TagProcessBean extends AbstractBaseProcessBean<Tag>
		implements TagProcess {

	private static final long serialVersionUID = 3833895426776628591L;

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getTagDAO(getConnection());
	}


}
