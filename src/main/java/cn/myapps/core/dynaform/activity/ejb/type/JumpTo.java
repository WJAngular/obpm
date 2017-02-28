package cn.myapps.core.dynaform.activity.ejb.type;



import javax.servlet.http.HttpServletRequest;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;

/**
 * 跳转类型操作对象
 *
 */
public class JumpTo extends ActivityType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4026601896630074752L;
	


	public JumpTo(Activity act) {
		super(act);
	}

	public String getAfterAction() {
		if(this.act.getJumpMode() == 1)
			return DISPATCHER_SHARE_JSP_NAMESPACE + "/dispatcher.jsp";
		return BASE_ACTION;
	}

	public String getBackAction() {
		if(this.act.getJumpMode() == 1)
			return DISPATCHER_SHARE_JSP_NAMESPACE + "/fail.jsp";
		return BASE_ACTION;
	}

	public String getBeforeAction() {
		if(this.act.getJumpMode() == 1)
			return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
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
		//return "ev_JumpTo('" + act.getId() + "',"+this.act.getJumpType()+",'"+this.act.getTargetList()+"',"+this.act.getJumpMode()+")";
		return "Activity.doExecute('" + act.getId() + "',"+act.getType()+",{jumpType:"+this.act.getJumpType()+",targetList:'"+this.act.getTargetList()+"',jumpMode:"+this.act.getJumpMode()+",jumpActOpenType:"+this.act.getJumpActOpenType()+"})";
	}
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		return null;
	}
	
	
	
	public String doProcess(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
			HttpServletRequest request = action.getRequest();
			
			if(this.act.getJumpMode() == Activity.JUMP_TO_URL){
				String docId = params.getParameterAsString("content.id");
				String formid = params.getParameterAsString("_formid");
				if(!StringUtil.isBlank(docId)){
					Document po = (Document) proxy.doView(docId);
					doc = po!=null? po : null;
					
					Form form = (Form) formPross.doView(formid);
					if(form !=null){
						if(doc ==null){
							doc = (Document) user.getFromTmpspace(docId);
						}
						
						if(doc ==null){
							doc = form.createDocument(params, user);
							doc.setId(docId);
							//doc = form.createDocumentWithSystemField(params, doc, user);
//							if(params.getParameter("content.versions") !=null){
//								doc.setVersions(params.getParameterAsInteger("content.versions"));
//							}
						}else{
							doc.setId(docId);
							doc = form.createDocumentWithSystemField(params, doc, user);
						}
						action.setContent(doc);
					}
				}
				request.getSession().setAttribute("_selects", action.get_selects());
				return "dispatcher.jsp";
			}
			String formid = this.getTargetFormId();
			Form form = (Form) formPross.doView(formid);
			if (form != null) {
				Document newDoc = proxy.doNew(form, user, params);
				action.setContent(newDoc);
				// 放入Session中
				request.setAttribute("content.id", newDoc.getId());
				//((ActivityAction)action).set_formid(formid);
				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
			}
			return AbstractRunTimeAction.FORM;
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			e.printStackTrace();
			return AbstractRunTimeAction.FORM;
		} catch (Exception e) {
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
