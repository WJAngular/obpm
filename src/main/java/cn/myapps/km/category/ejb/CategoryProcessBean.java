package cn.myapps.km.category.ejb;

import java.util.Collection;

import cn.myapps.km.base.dao.DaoManager;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractBaseProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.category.dao.CategoryDAO;
import cn.myapps.km.util.Sequence;
import cn.myapps.util.StringUtil;

public class CategoryProcessBean extends AbstractBaseProcessBean<Category> implements CategoryProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4147992315082381895L;

	public void doCreate(NObject no) throws Exception {
		Category category = (Category)no;
		if(StringUtil.isBlank(category.getId())){
			category.setId(Sequence.getSequence());
		}
		
		try {
			beginTransaction();
			getDAO().create(category);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
		
	}

	public void doRemove(String pk) throws Exception {
		getDAO().remove(pk);
	}

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

	public NObject doView(String id) throws Exception {
		return getDAO().find(id);
	}

	public NRuntimeDAO getDAO() throws Exception {
		return DaoManager.getCategoryDAO(getConnection());
	}

	public Collection<Category> doQuerySubCategory(String parentId,
			String domainId) throws Exception {
		return ((CategoryDAO)getDAO()).querySubCategory(parentId, domainId);
	}


}
