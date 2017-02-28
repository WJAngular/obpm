package cn.myapps.core.dynaform.activity.ejb.type;

import javax.servlet.http.HttpServletResponse;

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
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.mobile2.document.MbDocumentXMLBuilder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.cache.MemoryCacheUtil;

public class DocumentCreate extends ActivityType {

	public DocumentCreate(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4852627575510900302L;

	public String getOnClickFunction() {
		//return "doNew('" + act.getId() + "')";
		View view = null;
		try {
			view = (View) act.getParent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{viewType:"+(view!=null? view.getViewType() : 1)+",openType:"+(view!=null? view.getOpenType() : 1)+",formId:'"+act.getOnActionForm()+"',target:this})";
	}

	public String getDefaultClass() {
		return VIEW_BUTTON_CLASS;
	}

	public String getButtonId() {
		return VIEW_BUTTON_ID;
	}

	public String getAfterAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBackAction() {
//		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
		return VIEW_NAMESPACE + "/displayView.action?isCloseDialog=true";
	}

	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action?_formid="
				+ act.getOnActionForm();
	}

	public String getDefaultOnClass() {

		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
			Form form = (Form) formPross.doView(params.getParameterAsString("formId"));
			if (form != null) {
				doc = proxy.doNewWithFlowPermission(form, user, params);
				doc.set_new(true);
				action.setContent(doc);
				// 放入Session中
				ServletActionContext.getRequest().setAttribute("content.id", doc.getId());
				MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
			}
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			e.printStackTrace();
		}catch (Exception e) {
			String message = e.getMessage();
			action.setRuntimeException(new OBPMRuntimeException(message,e));
			e.printStackTrace();
		}
		return "setParameter";
	}
	
	public String doProcess(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		action.setContent(doc);
		// 放入Session中
		MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
		return "form";
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		try{
			FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, act.getApplicationid());
			
			Form form = (Form) formPross.doView(act.getOnActionForm());
			Document newDoc = null;
			if (form != null) {
				newDoc = proxy.doNew(form, user, params);
				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
			}
			return newDoc;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
