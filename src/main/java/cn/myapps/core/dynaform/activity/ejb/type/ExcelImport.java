package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

public class ExcelImport extends ActivityType {

	public ExcelImport(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6664581076332960524L;

	public String getDefaultClass() {
		return VIEW_BUTTON_CLASS;
	}

	public String getButtonId() {
		return VIEW_BUTTON_ID;
	}

	public String getAfterAction() {
		return VIEW_NAMESPACE + "/displayView.action";
	}

	public String getBackAction() {
		return VIEW_NAMESPACE + "/displayView.action";
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}

	public String getOnClickFunction() {
		//return "ev_excelImport('" + act.getId() + "', '" + act.getImpmappingconfigid() + "')";
		return "Activity.doExecute('" + act.getId() + "',"+act.getType()+",{impmappingconfigid:'"+act.getImpmappingconfigid()+"',target:this})";
		
	}

	public String getBeforeAction() {
		return "";
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
