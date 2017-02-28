package cn.myapps.core.dynaform.activity.ejb.type;


import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.action.ActivityAction;
import cn.myapps.core.dynaform.activity.action.ActivityRunTimeAction;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.cache.MemoryCacheUtil;

public class Copy extends ActivityType {

	public Copy(Activity atc) {
		super(atc);
	}

	private static final long serialVersionUID = 1899989109269523704L;

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;

	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getOnClickFunction() {
		//return "ev_action('" + act.getType() + "', '" + act.getId() + "')";
		return "Activity.doExecute('" + act.getId() + "', " + act.getType()+")";

	}

	public String getAfterAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBackAction() {
		return getAfterAction();
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
			new DocumentUpdate(act).doExecute(action, doc, user, params);
			if (!action.hasFieldErrors()) {
				Document oldDoc = (Document) action.getContent();

				FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
				Form form = (Form) formPross.doView(params.getParameterAsString("_formid"));
				Document newDoc = proxy.doNewWithChildren(form, user, params, oldDoc.getChilds());
				//action.setContent(newDoc);
				//ServletActionContext.getRequest().setAttribute("content.id", newDoc.getId());
				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
				
				((ActivityAction)action).result.setResultData(newDoc.getId());
			}
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			e.printStackTrace();
		}catch (Exception e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
		}
		return "none";
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		Document newDoc = null;
		try{
            //new DocumnetUpdate(act).doMbExecte(user,params);
		 doc = (Document)new DocumentUpdate(act).doMbExecte( user, params);
			if(doc != null && doc.getId() != null){
				FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, act.getApplicationid());
				Form form = (Form) formPross.doView(((Document)doc).getFormid());
				 newDoc = proxy.doNewWithChildren(form, user, params, ((Document)doc).getChilds());
				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
				return newDoc;
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

}
