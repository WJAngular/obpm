package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;


/**
 * Flex 动态打印按钮类型
 * @author Happy
 *
 */
public class FlexPrint extends ActivityType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4429402952733648050L;

	public FlexPrint(Activity act) {
		super(act);
	}



	public String getOnClickFunction() {
		//return "ev_flexPrint('"+act.getId()+"','"+act.getOnActionPrint()+"',false)";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{printerid:'"+act.getOnActionPrint()+"',withFlowHis:false})";
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return  "/portal/share/dynaform/printer/flexprint.jsp";
	}

	public String getBackAction() {
		return  DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		return "none";
	}



	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}
}
