package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public class NullType extends ActivityType {

	public NullType(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6026837196484129755L;

	public String toHtml(int permissionType) {
		return "";
	}

	public String getOnClickFunction() {
		throw new UnsupportedOperationException();
	}

	public String getDefaultClass() {
		throw new UnsupportedOperationException();
	}

	public String getButtonId() {
		throw new UnsupportedOperationException();
	}

	public String getAfterAction() {
		return BASE_ACTION;
	}

	public String getBackAction() {
		return BASE_ACTION;
	}

	public String getBeforeAction() {
		return BASE_ACTION;
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
