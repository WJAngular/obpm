package cn.myapps.core.dynaform.activity.ejb.type;

import com.opensymphony.xwork2.Action;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.cache.MemoryCacheUtil;

public class PrintView extends ActivityType {

	public PrintView(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5425161592049326841L;

	public String getOnClickFunction() {
		//return "ev_printview('" + act.getId() + "')";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{target:this})";
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		//return BASE_ACTION;
		
		return VIEW_NAMESPACE+ "/printView.action";
	}

	public String getBackAction() {
		return BASE_ACTION;
	}

	public String getBeforeAction() {
		//return BASE_ACTION;
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		String _docid = params.getParameterAsString("_docid");
		doc = (Document) MemoryCacheUtil.getFromPrivateSpace(_docid, user);
		action.setContent(doc);
		return Action.SUCCESS;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}
}
