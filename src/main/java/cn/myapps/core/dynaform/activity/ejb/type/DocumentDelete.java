package cn.myapps.core.dynaform.activity.ejb.type;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.Action;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mobile2.view.MbViewXMLBuilder;
import cn.myapps.util.ProcessFactory;

public class DocumentDelete extends ActivityType {

	public DocumentDelete(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3934714556361444621L;

	public String getOnClickFunction() {
		//return "doRemove('" + act.getId() + "')";
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
		try {
			if (action.get_selects() != null) {
				DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
				proxy.doRemove(action.get_selects());
			}
		} catch (OBPMValidateException e) {
			log.error("doDelete", e);
			action.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			String message = e.getMessage();
			action.setRuntimeException(new OBPMRuntimeException(message,e));
			log.error("doDelete", e);
		}

		//return "view";
		return "none";
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		try{
			String viewid = params.getParameterAsString("_viewid");
			String _selects = params.getParameterAsString("_selects");
			if (_selects != null) {
				DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, act.getApplicationid());
				if(_selects!= null){
					String[] selects = _selects.split(";");
					proxy.doRemove(selects);
				}
			}
			ViewProcess viewprocess = (ViewProcess)ProcessFactory.createProcess(ViewProcess.class);
			View view = null;
			if(viewid != null){
				view = (View) viewprocess.doView(viewid);
			}
			return view;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

}
