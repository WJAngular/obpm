package cn.myapps.core.multilanguage.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface MultiLanguageProcess extends IDesignTimeProcess<MultiLanguage> {
	/**
	 * 
	 * @param languageType 
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public MultiLanguage doView(int languageType, String label) throws Exception;
	/**
	 * 条件语言类型、标签、域
	 * @param languageType
	 * @param label
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	public MultiLanguage doView(int languageType, String label,String domain) throws Exception;
	/**
	 * @param languageType
	 * @param label
	 * @throws Exception
	 */
	public void doRemove(int languageType, String label) throws Exception;
}
