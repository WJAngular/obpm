package cn.myapps.core.dynaform.activity.ejb.type;

import javax.servlet.http.HttpServletRequest;

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
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.cache.MemoryCacheUtil;

/**
 * 跳转类型操作对象
 *
 */
public class Jump extends ActivityType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4026601896630074752L;
	


	public Jump(Activity act) {
		super(act);
	}

	public String getAfterAction() {
		return BASE_ACTION;
	}

	public String getBackAction() {
		return BASE_ACTION;
	}

	public String getBeforeAction() {
		return BASE_ACTION;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}

	public String getOnClickFunction() {
		return "ev_Jump('" + act.getId() + "',"+this.act.getJumpType()+",'"+this.act.getTargetList()+"')";
	}
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			HttpServletRequest request = action.getRequest();
			
			FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
			String formid = this.getTargetFormId();
			Form form = (Form) formPross.doView(formid);
			if (form != null) {
				Document newDoc = proxy.doNew(form, user, params);
				action.setContent(newDoc);
				// 放入Session中
				request.setAttribute("content.id", newDoc.getId());
				((ActivityRunTimeAction)action).set_formid(formid);
				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
			}
			return AbstractRunTimeAction.FORM;
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			e.printStackTrace();
			return AbstractRunTimeAction.FORM;
		}
		catch (Exception e) {
			String message = e.getMessage();
			action.setRuntimeException(new OBPMRuntimeException(message,e));
			e.printStackTrace();
			return AbstractRunTimeAction.FORM;
		}
	}
	
	private String getTargetFormId(){
		String formid = null;
		String [] formids = this.act.getTargetList().split(";");
		for (int i = 0; i < formids.length; i++) {
			formid = formids[0].split("\\|")[0];
		}
		return formid;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
