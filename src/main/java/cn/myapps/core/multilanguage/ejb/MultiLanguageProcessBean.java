package cn.myapps.core.multilanguage.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.multilanguage.dao.MultiLanguageDAO;

public class MultiLanguageProcessBean extends AbstractDesignTimeProcessBean<MultiLanguage>
		implements MultiLanguageProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -13699440223439268L;

	protected IDesignTimeDAO<MultiLanguage> getDAO() throws Exception {
		return (MultiLanguageDAO)DAOFactory.getDefaultDAO(MultiLanguage.class.getName());

	}

	/**
	 * 条件语言类型、标签查找
	 */
	public MultiLanguage doView(int languageType, String label)
			throws Exception {
		return ((MultiLanguageDAO) getDAO()).find(languageType, label);
	}
	
	/**
	 * 条件语言类型、标签、域
	 */
	public MultiLanguage doView(int languageType, String label,String application)
	throws Exception {
	return ((MultiLanguageDAO) getDAO()).find(languageType, label,application);
	}

	public Collection<MultiLanguage> doSimpleQuery(ParamsTable params, String application)
			throws Exception {
		if (params == null)
			params = new ParamsTable();
		params.setParameter("s_applicationid", application);

		return getDAO().simpleQuery(params);
	}

	public void doRemove(int languageType, String label) throws Exception {
		((MultiLanguageDAO) getDAO()).remove(languageType, label);
	}
}
