package cn.myapps.core.dynaform.activity.ejb.type;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.Action;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public class Dispatcher extends ActivityType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Dispatcher(Activity act) {
		super(act);
	}

	public String getAfterAction() {
		return DISPATCHER_SHARE_JSP_NAMESPACE + "/dispatcher.jsp";
	}

	public String getBackAction() {
		return DISPATCHER_SHARE_JSP_NAMESPACE + "/fail.jsp";
	}

	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}

	public String getOnClickFunction() {
		return "ev_dispatcherPage('"+act.getId()+"')";
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		ServletActionContext.getRequest().getSession().setAttribute("_selects", action.get_selects());
		return Action.SUCCESS;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
