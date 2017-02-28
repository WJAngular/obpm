package cn.myapps.core.dynaform.activity.ejb.type;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.WorkflowException;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.engine.ValidationException;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.property.PropertyUtil;

public class WorkFlowProcess extends ActivityType {

	public WorkFlowProcess(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 468670635828212529L;

	public String toHtml(int permissionType) {
		htmlBuilder = new StringBuffer();
		addDefaultButton(permissionType);
		return htmlBuilder.toString();
	}
	
	public String getOnClickFunction() {
		boolean isUsbKeyVerify = false;
		try {
			PropertyUtil.reload("usbkey");
			String usbkeyAuthenticate = PropertyUtil.get("usbkey.enable");
			if("true".equals(usbkeyAuthenticate)){
				NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user,null);
				if(nodert != null){
					FlowDiagram fd = doc.getState().getFlowVO().toFlowDiagram();
					Node node = (Node) fd.getElementByID(nodert.getNodeid());
					if(node instanceof ManualNode){ 
						if(((ManualNode)node).isUsbKeyVerify) isUsbKeyVerify = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String flowShowType = act.getFlowShowType();
		if (flowShowType != null && flowShowType.equals("ST02")){
			return "showFlowSelect(this, '" + act.getId() + "','{*[cn.myapps.core.workflow.submit_flow]*}',"+isUsbKeyVerify+")";
		}
		return "ev_validation(this, '" + act.getId() + "', "+isUsbKeyVerify+")";
	}

	public String getDefaultClass() {
		return "button-document";
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return DOCUMENT_SHARE_JSP_NAMESPACE + "/success.jsp";
	}

	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}
	
	public String getFieldErrorsAction(){
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getDefaultOnClass() {

		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		FlowStateRTProcess fsProcess = null;
		DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,doc.getApplicationid());
		// synchronized (user) {
		if (user.getStatus() == 1) {
			try {
				doc = DocumentHelper.rebuildDocument(doc, params,getUser());
				fsProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, doc.getApplicationid());
				doc = ((DocumentProcess) proxy).doFlow(doc, params, params.getParameterAsString("_currid"), params.getParameterAsArray("_nextids"), params.getParameterAsString("_flowType"),
						params.getParameterAsString("_attitude"), user);
				action.setContent(doc);
				MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
				//((ActivityRunTimeAction)action).set_attitude("");// 将remarks清空
				if(FlowType.RUNNING2RUNNING_BACK.equals(params.getParameterAsString("_flowType"))){
					action.addActionMessage("{*[cn.myapps.core.deploy.module.commit_back]*}");
				}else{
					action.addActionMessage("{*[cn.myapps.core.deploy.module.commit_succeed]*}");
				}
			} catch(OBPMValidateException e){
				if((e.getCause() instanceof ImpropriateException)){
					//加载数据库中最新的文档到上下文环境
					action.setContent(proxy.doView(action.getContent().getId()));
					MemoryCacheUtil.putToPrivateSpace(action.getContent().getId(), action.getContent(), getUser());
				}
				action.addFieldError("1", e.getValidateMessage());
				return AbstractRunTimeAction.FORM;
			} catch (Exception e) {
				action.addFieldError("System Error", e.getMessage());
				action.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
				if((e instanceof ImpropriateException)){
					//加载数据库中最新的文档到上下文环境
					action.setContent(proxy.doView(action.getContent().getId()));
					MemoryCacheUtil.putToPrivateSpace(action.getContent().getId(), action.getContent(), getUser());
				}else {
					doc.setState((FlowStateRT) fsProcess.doView(doc.getStateid()));
					action.setContent(doc);
				}
				if(!(e instanceof WorkflowException) && !(e instanceof ImpropriateException)  && !(e instanceof ValidationException)){
					e.printStackTrace();
				}
				
				return AbstractRunTimeAction.FORM;
			}
			return action.SUCCESS;
		} else {
			action.addFieldError("System Error",
					"{*[core.flow.intervention.table.notexist]*}  please update the application");
			return AbstractRunTimeAction.FORM;
		}
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		// synchronized (user) {
		Document doc = null;
		if (user.getStatus() == 1) {
			try {
				String formid = params.getParameterAsString("_formid");
				DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, act.getApplicationid());
				FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				Form form = (Form) formProcess.doView(formid);
				String docid = params.getParameterAsString("_docid");
				Document olddoc = (Document) proxy.doView(docid);
				
				if(olddoc == null){
					olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
				}
				
				if (olddoc != null) {
					doc = form.createDocument(olddoc, params, user);
				} else {
					doc = form.createDocument(params, user);
				}
				doc = ((DocumentProcess) proxy).doFlow(doc, params, params.getParameterAsString("_currid"), params.getParameterAsArray("_nextids"), params.getParameterAsString("_flowType"),
						params.getParameterAsString("_attitude"), user);
				
				MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
				return doc;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return null;
	}

}
