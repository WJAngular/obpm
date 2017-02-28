package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public class ScriptProcess extends ActivityType {

	public ScriptProcess(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3911583010697127049L;

	public String getOnClickFunction() {
		return "ev_action('" + act.getType() + "', '" + act.getId() + "')";
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return DOCUMENT_SHARE_JSP_NAMESPACE + "/success.jsp";
	}

	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBeforeAction() {
		return DOCUMENT_SHARE_JSP_NAMESPACE + "/success.jsp";
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		return null;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}
}
