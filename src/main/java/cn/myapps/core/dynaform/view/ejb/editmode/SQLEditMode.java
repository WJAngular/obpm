package cn.myapps.core.dynaform.view.ejb.editmode;

import java.util.ArrayList;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.EditMode;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;

/**
 * 
 * @author nicholas zhen
 * 
 */
public class SQLEditMode extends AbstractEditMode implements EditMode {

	public SQLEditMode(View view) {
		super(view);
	}

	public String getQueryString(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		StringBuffer label = new StringBuffer();
		label.append("VIEW(").append(view.getId()).append(")." + view.getName()).append(".SqlFilterScript");

		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
		runner.initBSFManager(sDoc, params, user, new ArrayList<ValidateMessage>());

		return runScript(runner, label.toString(), view.getSqlFilterScript());
	}

	public double getSumTotal(ParamsTable params, WebUser user,
			String fieldName, String formid, String domainid) {
		return 0;
	}

}
