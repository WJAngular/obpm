package cn.myapps.core.page.ejb;

import java.util.Collection;

import org.python.modules.newmodule;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.form.ejb.BaseFormProcessBean;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.page.dao.PageDAO;

public class PageProcessBean extends BaseFormProcessBean<Page> implements PageProcess {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1504118574193272728L;

	protected IDesignTimeDAO<Page> getDAO() throws Exception {
		return (PageDAO) DAOFactory.getDefaultDAO(Page.class.getName());
	}

	public Page getDefaultPage(String application) throws Exception {
		return ((PageDAO) getDAO()).findDefaultPage(application);
	}

	public Page doViewByName(String name, String application) throws Exception {
		return ((PageDAO) getDAO()).findByName(name, application);
	}

	public DataPackage<Page> doListExcludeMod(ParamsTable params, String application)
			throws Exception {
		return ((PageDAO) getDAO()).getDatasExcludeMod(params, application);
	}

	public Collection<Page> getPagesByApplication(String application)
			throws Exception {
		return ((PageDAO) getDAO()).getPagesByApplication(application);
	}

	public Collection<Page> getTemplateFormsByModule(String moduleid,
			String application) throws Exception {
		return null;
	}

	public Collection<Form> getFormsByApplication(String applicationId)
			throws Exception {
		throw new UnsupportedOperationException();
	}
}
