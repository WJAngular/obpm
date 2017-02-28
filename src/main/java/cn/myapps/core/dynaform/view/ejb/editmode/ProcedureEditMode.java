package cn.myapps.core.dynaform.view.ejb.editmode;

import java.util.ArrayList;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.EditMode;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

public class ProcedureEditMode extends AbstractEditMode implements EditMode {

	public ProcedureEditMode(View view) {
		super(view);
	}

	public DataPackage<Document> getDataPackage(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		return getDataPackage(params, 1, Integer.MAX_VALUE, user, sDoc);
	}

	public DataPackage<Document> getDataPackage(ParamsTable params, int page, int lines, WebUser user, Document sDoc)
			throws Exception {
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, view
				.getApplicationid());
		String procedure = appendCondition(getQueryString(params, user, sDoc));

		return dp.queryByProcedure(procedure, params, page, lines, user.getDomainid());
	}

	public String getQueryString(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		StringBuffer label = new StringBuffer();
		label.append("VIEW(").append(view.getId()).append(")." + view.getName()).append(".ProcedureFilterScript");

		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
		runner.initBSFManager(sDoc, params, user, new ArrayList<ValidateMessage>());

		return runScript(runner, label.toString(), view.getProcedureFilterScript());
	}

	/**
	 * 获取文档总行数(默认为SQL查询)
	 * 
	 * @param params
	 *            参数
	 * @param user
	 *            当前用户
	 * @param sDoc
	 *            查询文档
	 * @return 文档总行数
	 * @throws Exception
	 */
	public long count(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, view
				.getApplicationid());
		String sql = appendCondition(getQueryString(params, user, sDoc));

		return dp.countByProcedure(sql, user.getDomainid());
	}

	public double getSumTotal(ParamsTable params, WebUser user,
			String fieldName, String formid, String domainid) {
		return 0;
	}
}
