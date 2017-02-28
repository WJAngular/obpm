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
import cn.myapps.core.macro.runner.*;

public class Nothing extends ActivityType {

	public Nothing(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2189293333861764513L;

	

	public String getOnClickFunction() {
		//return "ev_action('" + act.getType() + "', '" + act.getId() + "')";
	
		String dispatcherUrlScript = new String() ;
		StringBuffer url = new StringBuffer();
	
		if(this.act.getAfterActionType() == Activity.AFTER_ACTION_TYPE_URL){
			if(this.act.getAfterActionDispatcherUrlScript() != null && this.act.getAfterActionDispatcherUrlScript().trim().length() > 0 ){
				try {
					ParamsTable params = this.doc.get_params();
					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),doc.getApplicationid());
					StringBuffer label = new StringBuffer();
					label.append(" Activity(").append(act.getId()).append(")." + act.getName()).append(".dispatcherUrlScript");
					Object result = runner.run(label.toString(), act.getAfterActionDispatcherUrlScript());
					if (result != null){
						url.append(result);
					}
					dispatcherUrlScript = url.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{afterActionType:"+this.act.getAfterActionType()+",afterActionDispatcherUrlScript:'"+dispatcherUrlScript+"'})";
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBeforeAction() {
		return   ACTIVITY_RUNTIME_NAMESPACE+ "/handle.action?isWithoutValidate=true";
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		action.addActionMessage(this.act.getName() + "{*[Success]*}");
		MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
		return "form";
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
