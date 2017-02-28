package cn.myapps.core.dynaform.activity.ejb.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.opensymphony.xwork2.Action;


import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class BatchApprove extends ActivityType {

	public BatchApprove(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5379627759279693896L;

	public String getOnClickFunction() {
		//return "doBatchApprove('" + act.getId() + "' , true" + ")";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{target:this})";
	}

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

	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}

	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		String limistStrList = params.getParameterAsString("_approveLimit");
		Collection<String> limistList = new ArrayList<String>();

		if (!StringUtil.isBlank(limistStrList)) {
			limistList = Arrays.asList(limistStrList.split(","));
		}
		try {
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());

			proxy.doBatchApprove(action.get_selects(), user, Environment.getInstance(), params, limistList);
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			e.printStackTrace();
		}catch (Exception e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
		}

		return Action.NONE;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
