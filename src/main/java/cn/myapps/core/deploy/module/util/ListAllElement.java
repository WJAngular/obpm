package cn.myapps.core.deploy.module.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleProcessBean;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcessBean;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcessBean;
import cn.myapps.core.style.repository.ejb.StyleRepositoryProcessBean;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryProcessBean;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcessBean;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;

public class ListAllElement {

	static int counter = 0;

	public String listAndGetStr(ModuleVO module, String applicationid) throws Exception {

		ModuleProcess process = new ModuleProcessBean();

		ModuleVO temp = (ModuleVO) process.doView(module.getId());
		Map<String, Date> elements;
		ParamsTable params = new ParamsTable();
		params.setParameter("s_module", module.getId());
		params.setParameter("s_applicationid", applicationid);

		StringBuffer rtnStr = new StringBuffer();

		rtnStr.append("<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" width=\"100%\">");

		rtnStr
				.append("<tr><td class=\"column-head2\" scope=\"col\">{*[Action]*}</td><td width=35% class=\"column-head2\" scope=\"col\">{*[Type]*}</td><td width=35% class=\"column-head2\" scope=\"col\"> {*[Name]*}</td><td width=35% class=\"column-head2\" scope=\"col\">{*[LastModifyDate]*}</td></tr>");

		// 查找属于这个module的form
		elements = listForm(params);
		rtnStr.append(listElements(elements, temp, "Form", counter));

		elements = listView(params);
		rtnStr.append(listElements(elements, temp, "View", counter));

		elements = listWorkFlow(params);
		rtnStr.append(listElements(elements, temp, "Workflow", counter));

		elements = listStyle(params);
		rtnStr.append(listElements(elements, temp, "Style", counter));

		elements = listValidateRepository(params);
		rtnStr.append(listElements(elements, temp, "Validate", counter));

		rtnStr.append("</table>");

		return rtnStr.toString();
	}

	/**
	 * 
	 * @param mp
	 * @return 返回包含html的字符串
	 * @throws Exception
	 */
	public String listElements(Map<String, Date> mp, ModuleVO module, String className, int counter) throws Exception {
		StringBuffer rtnStr = new StringBuffer();
		String trclass = "table-text";

		for (Iterator<Entry<String, Date>> iter = mp.entrySet().iterator(); iter.hasNext();) {

			if (counter % 2 == 0) {
				trclass = "table-text2";
			}
			Entry<String, Date> entry = iter.next();
			Date lastModify = entry.getValue();

			rtnStr.append("<tr class=" + trclass
				+ "><td class=\"table-td\">{*[Normal]*}</td><td class=\"table-td\">" + className
				+ "</td><td class=\"table-td\">" + entry.getKey() + "</td><td class=\"table-td\">" + lastModify
				+ "</td></tr>");
			counter++;

		}

		return rtnStr.toString();
	}

	/**
	 * 
	 * @param params
	 * @return 属于这个moduleid的form
	 * @throws Exception
	 */
	private Map<String, Date> listForm(ParamsTable params) throws Exception {
		Collection<? extends ValueObject> forms = list(params, new FormProcessBean());
		Map<String, Date> rtns = new HashMap<String, Date>();

		for (Iterator<? extends ValueObject> iter = forms.iterator(); iter.hasNext();) {
			Form form = (Form) iter.next();
			rtns.put(form.getName(), form.getLastmodifytime());
		}

		return rtns;
	}

	/**
	 * 
	 * @param params
	 * @return 属于这个moduleid的view
	 * @throws Exception
	 */
	private Map<String, Date> listView(ParamsTable params) throws Exception {
		Collection<? extends ValueObject> views = list(params, new ViewProcessBean());
		Map<String, Date> rtns = new HashMap<String, Date>();

		for (Iterator<? extends ValueObject> iter = views.iterator(); iter.hasNext();) {
			View view = (View) iter.next();
			rtns.put(view.getName(), view.getLastmodifytime());
		}

		return rtns;
	}

	/**
	 * 
	 * @param params
	 * @return 属于这个moduleid的style
	 * @throws Exception
	 */
	private Map<String, Date> listStyle(ParamsTable params) throws Exception {
		Collection<? extends ValueObject> styles = list(params, new StyleRepositoryProcessBean());
		Map<String, Date> rtns = new HashMap<String, Date>();

		for (Iterator<? extends ValueObject> iter = styles.iterator(); iter.hasNext();) {
			StyleRepositoryVO style = (StyleRepositoryVO) iter.next();
			rtns.put(style.getName(), style.getLastmodifytime());
		}

		return rtns;
	}

	/**
	 * 
	 * @param params
	 * @return 属于这个moduleid的ValidateRepository
	 * @throws Exception
	 */
	private Map<String, Date> listValidateRepository(ParamsTable params) throws Exception {
		Collection<? extends ValueObject> validates = list(params, new ValidateRepositoryProcessBean());
		Map<String, Date> rtns = new HashMap<String, Date>();

		for (Iterator<? extends ValueObject> iter = validates.iterator(); iter.hasNext();) {
			ValidateRepositoryVO validate = (ValidateRepositoryVO) iter.next();
			rtns.put(validate.getName(), validate.getLastmodifytime());
		}

		return rtns;
	}

	/**
	 * 
	 * @param params
	 * @return 属于这个moduleid的WorkFlow
	 * @throws Exception
	 */
	private Map<String, Date> listWorkFlow(ParamsTable params) throws Exception {

		Collection<? extends ValueObject> billdefis = list(params, new BillDefiProcessBean());
		Map<String, Date> rtns = new HashMap<String, Date>();

		for (Iterator<? extends ValueObject> iter = billdefis.iterator(); iter.hasNext();) {
			BillDefiVO billdefi = (BillDefiVO) iter.next();
			rtns.put(billdefi.getSubject(), billdefi.getLastmodify());
		}

		return rtns;
	}
	
	private Collection<? extends ValueObject> list(ParamsTable params, IDesignTimeProcess<? extends ValueObject> process) throws Exception {
		return process.doSimpleQuery(params);

	}
}
