package cn.myapps.core.dynaform.activity.ejb.type;

import java.util.Collection;
import java.util.Iterator;


import com.opensymphony.xwork2.Action;
import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.action.ActivityRunTimeAction;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

public class ClearAll extends ActivityType {

	public ClearAll(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8325941963241466935L;

	public String getOnClickFunction() {
		//return "ev_submit('" + act.getId() + "','','',true,'{*[core.dynaform.view.activity.msg.cleanall]*}')";
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
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action?_formid="
				+ act.getOnActionForm();
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	/**
	 * 查出这个formid的所有document,然后一条条删除，如果是审批中或者是审批完成不能删除的，则放到errorfiel中显示出来
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
			FormProcess fb = (FormProcess) (ProcessFactory.createProcess(FormProcess.class));
			String formid = params.getParameterAsString("FormID");
			Form form = (Form) fb.doView(formid);
			String dql = "$formname = '" + form.getFullName() + "'";
			
			Collection<Document> docs = proxy.queryByDQL(dql, action.getDomain()).datas;

			for (Iterator<Document> iter = docs.iterator(); iter.hasNext();) {
				Document _doc = (Document) iter.next();
				proxy.doRemove(_doc.getId());

			}
			action.addActionMessage("{*[delete.successful]*}");
		} catch (OBPMValidateException e) {
			log.error("doDelete", e);
			action.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			log.error("doDelete", e);
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
		}

		return Action.NONE;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		try{
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, act.getApplicationid());
			FormProcess fb = (FormProcess) (ProcessFactory.createProcess(FormProcess.class));
			ViewProcess vb = (ViewProcess) (ProcessFactory.createProcess(ViewProcess.class));
			String viewid = params.getParameterAsString("_viewid");
			View view = (View) vb.doView(viewid);
			Form form = (Form) fb.doView(view.getRelatedForm());
			String dql = "$formname = '" + form.getFullName() + "'";
			
			Collection<Document> docs = proxy.queryByDQL(dql, user.getDomainid()).datas;
	
			for (Iterator<Document> iter = docs.iterator(); iter.hasNext();) {
				Document _doc = (Document) iter.next();
				proxy.doRemove(_doc.getId());
			}
			
			return view;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

}
