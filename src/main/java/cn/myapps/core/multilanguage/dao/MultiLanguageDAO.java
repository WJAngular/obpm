package cn.myapps.core.multilanguage.dao;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.multilanguage.ejb.MultiLanguage;

public interface MultiLanguageDAO extends IDesignTimeDAO<MultiLanguage> {
	/**
	 * 通过语言类型、标签查找
	 */
	public MultiLanguage find(int languageType, String label) throws Exception;
	/**
	 * 通过语言类型、标签、域来查找
	 * @param languageType
	 * @param label
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	public MultiLanguage find(int languageType, String label,String domain) throws Exception;
	/**
	 * 
	 * @param languageType
	 * @param label
	 * @throws Exception
	 */
	public void remove(int languageType, String label) throws Exception;
}
