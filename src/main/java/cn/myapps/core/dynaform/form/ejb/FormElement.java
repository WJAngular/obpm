/*
 * Created on 2005-1-22
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * @author ZhouTY
 * 
 *  To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface FormElement {
	public String toTemplate();

	/**
	 * Form模版内容结合Document中的ITEM存放的值,返回重定义后的html。
	 * 
	 * @param doc
	 *            Document对象
	 * @param runner
	 * @param webUser 
	 * @param permissionType TODO
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html
	 * @throws Exception
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception;

	/**
	 * Form模版内容结合Document中的ITEM存放的值,返回重定义后的xml。
	 * 
	 * @param doc
	 *            Document对象
	 * @param runner
	 * 
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的xml
	 * @throws Exception
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception;
	
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception;
	
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception;
}
