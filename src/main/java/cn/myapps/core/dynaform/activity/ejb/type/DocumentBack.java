package cn.myapps.core.dynaform.activity.ejb.type;

import com.opensymphony.xwork2.Action;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public class DocumentBack extends ActivityType {

	public DocumentBack(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8129799097627577004L;

	public String getOnClickFunction() {
		//return "ev_action('" + act.getType() + "', '" + act.getId() + "')";
		return "Activity.doExecute('" + act.getId() + "',"+act.getType()+")";
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return BASE_ACTION;
	}

	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBeforeAction() {
		return  ACTIVITY_RUNTIME_NAMESPACE+ "/handle.action";
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			// 清空对应的Document
			user.removeFromTmpspace(action.getContent().getId());
		} catch (Exception e) {
			String message = e.getMessage();
			action.setRuntimeException(new OBPMRuntimeException(message,e));
			action.addFieldError("", e.getMessage());
			return AbstractRunTimeAction.FORM;
		}
		return "back";
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		try {
			String docid = params.getParameterAsString("_docid");
			user.removeFromTmpspace(docid);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
