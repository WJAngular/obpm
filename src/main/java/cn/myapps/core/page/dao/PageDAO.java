package cn.myapps.core.page.dao;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.form.dao.FormDAO;
import cn.myapps.core.page.ejb.Page;

public interface PageDAO extends FormDAO<Page> {
	public Page findDefaultPage(String application) throws Exception;

	public Page findByName(String name, String application) throws Exception;

	public DataPackage<Page> getDatasExcludeMod(ParamsTable params, String application) throws Exception;

	public Collection<Page> getPagesByApplication(String application) throws Exception;
}
