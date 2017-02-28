package cn.myapps.core.dynaform.view.ejb.editmode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.EditMode;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.condition.Condition;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.varible.IScriptVariableStore;
import cn.myapps.util.varible.VariableExpander;

/**
 * 
 * @author nicholas zhen
 * 
 */
public class DQLEditMode extends AbstractEditMode implements EditMode {
	private final static Map<String, String> DQL_NAME_MAP = new HashMap<String, String>();

	static {
		DQL_NAME_MAP.put("$PARENT", "$parent.$id");
	}

	public DQLEditMode(View view) {
		super(view);
	}

	public String getQueryString(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		StringBuffer label = new StringBuffer();
		label.append("VIEW(").append(view.getId()).append(")." + view.getName()).append(".FilterScript");

		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
		runner.initBSFManager(sDoc, params, user, new ArrayList<ValidateMessage>());

		String dql = runScript(runner, label.toString(), view.getFilterScript());

		IScriptVariableStore store = new IScriptVariableStore();
		VariableExpander expander = new VariableExpander(store, "${", "}");
		Collection<String> rtn = expander.getVariableNames(dql);
		store.parseVariableNames(rtn, sDoc, params);
		dql = expander.expandVariables(dql);

		return dql;
	}

	public DataPackage<Document> getDataPackage(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		setFieldtype(params);

		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, view
				.getApplicationid());
		String dql = appendCondition(getQueryString(params, user, sDoc));

		return dp.queryByDQL(dql, params, user.getDomainid());
	}

	public DataPackage<Document> getDataPackage(ParamsTable params, int page, int lines, WebUser user, Document sDoc)
			throws Exception {
		setFieldtype(params);

		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, view
				.getApplicationid());
		String dql = appendCondition(getQueryString(params, user, sDoc));

		return dp.queryByDQLPage(dql, params, page, lines, user.getDomainid());
	}

	public long count(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, view
				.getApplicationid());
		String dql = appendCondition(getQueryString(params, user, sDoc));

		return dp.countByDQL(dql, user.getDomainid());
	}

	private void setFieldtype(ParamsTable params) throws Exception {
		String colSort = params.getParameterAsString("_sortCol");

		if (!StringUtil.isBlank(colSort)) {
			String relatedFormid = view.getRelatedForm();
			if (!StringUtil.isBlank(relatedFormid)) {
				FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				Form relatedForm = (Form) formProcess.doView(relatedFormid);
				FormField field = relatedForm.findFieldByName(relatedForm.getTableMapping().getFieldName(colSort));
				if (field != null) {
					String fieldType = field.getFieldtype();
					params.setParameter("fieldType", fieldType);
				}
			}
		}
	}

	public void addCondition(String name, String val, String operator) {
		try {
			name = name.startsWith("$") ? name.substring(1) : name; // 处理$符号
			if (isSystemField(name)) {
				conditions.add(new Condition("$" + name, val, operator));
			} else {
				conditions.add(new Condition(name, val, operator));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String appendCondition(String queryString) {
		StringBuffer rtn = new StringBuffer(queryString);
		if (!conditions.isEmpty()) {
			for (Iterator<Condition> iterator = conditions.iterator(); iterator.hasNext();) {
				Condition cndt = iterator.next();

				// DQL条件转义，如$PARENT转为$parent.$id
				if (DQL_NAME_MAP.containsKey(cndt.getName())) {
					rtn.append(" and ").append(DQL_NAME_MAP.get(cndt.getName())).append(cndt.getOperator()).append("'")
							.append(cndt.getVal()).append("'");
				} else {
					if (StringUtil.isBlank((String) cndt.getVal())) {
						rtn.append(" and ").append(cndt.getName()).append("= '' or ").append(cndt.getName()).append(
								" is null");
					} else {
						rtn.append(" and ").append(cndt.getName()).append(" ").append(cndt.getOperator()).append(" '")
								.append(cndt.getVal()).append("'");
					}
				}
			}
		}

		return rtn.toString();
	}

	public double getSumTotal(ParamsTable params, WebUser user,
			String fieldName, String formid, String domainid) {
		return 0;
	}
}
