package cn.myapps.core.dynaform.activity.ejb.type;

import com.opensymphony.xwork2.Action;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

public class DocumentQuery extends ActivityType {

	public DocumentQuery(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3304912397283938128L;

	public String getOnClickFunction() {
		//return "doQuery('" + act.getId() + "')";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{viewId:'"+act.getOnActionView()+"',target:this})";
	}

	public String getDefaultClass() {
		return VIEW_BUTTON_CLASS;
	}

	public String getButtonId() {
		return VIEW_BUTTON_ID;
	}

	public String getAfterAction() {
		return VIEW_NAMESPACE + "/displayView.action?_viewid=" + act.getOnActionView();
		//return VIEW_JSP_NAMESPACE + "/detail.jsp";
	}

	public String getBackAction() {
		return VIEW_NAMESPACE + "/displayView.action";
		//return VIEW_JSP_NAMESPACE + "/detail.jsp";
	}

	public String getBeforeAction() {
		return  ACTIVITY_RUNTIME_NAMESPACE+ "/handle.action";
		//return VIEW_NAMESPACE + "/displayView.action?_viewid=" + act.getOnActionView();
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		return Action.NONE;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		try{
			String viewid = act.getOnActionView();
			ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View view = (View) process.doView(viewid);
			return view;
		}catch(Exception e){
			e.printStackTrace();
			throw(e);
		}
	}
}
