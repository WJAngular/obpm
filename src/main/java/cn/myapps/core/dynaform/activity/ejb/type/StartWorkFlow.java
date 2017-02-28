package cn.myapps.core.dynaform.activity.ejb.type;

import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.activity.action.ActivityRunTimeAction;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;

public class StartWorkFlow extends ActivityType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7069431626851422819L;

	/**
	 * 
	 */

	public StartWorkFlow(Activity act) {
		super(act);
	}
	
	public String toHtml(int permissionType) {
		htmlBuilder = new StringBuffer();
		addDefaultButton(permissionType);
		return htmlBuilder.toString();
	}
	
	public String getOnClickFunction() {
		//return "startWorkFlow("+act.getType()+", '" + act.getId() + "','" + act.getEditMode() + "','{*[cn.myapps.core.workflow.start_workflow]*}')";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{editMode:" + act.getEditMode() + ",title:'{*[cn.myapps.core.workflow.start_workflow]*}'})";
	}

	public String getDefaultClass() {
		return "button-document";
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBackAction() {
		return DOCUMENT_JSP_NAMESPACE + "/content.jsp";
	}

	public String getBeforeAction() {
		return ACTIVITY_RUNTIME_NAMESPACE + "/handle.action";
	}

	public String getDefaultOnClass() {

		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
		try {
			BillDefiProcess flowProcss = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
			doc = DocumentHelper.rebuildDocument(doc, params,user);
			doc.setDomainid(action.getDomain());
			BillDefiVO flowVO = null;
			String selectFlow = "";
			if (params.getParameterAsDouble("_editMode") == null || params.getParameterAsDouble("_editMode") == 0) {
				selectFlow = params.getParameterAsString("selectFlow");
			} else if (params.getParameterAsDouble("_editMode") == 1) {
				selectFlow = runStartFlowScript(doc,params,user);
			}

			if (selectFlow != null && selectFlow != "") {
				doc.setFlowid(selectFlow);
				flowVO = (BillDefiVO) flowProcss.doView(selectFlow);
				doc.setFlowVO(flowVO);
				params.setParameter("_flowid", selectFlow);
			} else {
				flowVO = doc.getFlowVO();
			}
			cn.myapps.core.workflow.element.Node firstNode = StateMachine.getFirstNode(doc,flowVO, user,params);

			if (firstNode != null) {
				cn.myapps.core.workflow.element.Node startNode = StateMachine
						.getStartNodeByFirstNode(flowVO, firstNode);
				if (startNode != null) {
					doc = proxy.doStartFlowOrUpdate(doc, params, user);
					
					/**
					if(!StringUtil.isBlank(doc.getStateid())){
						//把当前审批节点设置到上下文
						NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user,null);
						if(nodert != null){
							ServletActionContext.getRequest().setAttribute("_targetNodeRT", nodert);
							((ActivityRunTimeAction)action).set_targetNode(nodert.getNodeid());
						}
					}
					**/
				}
			} else {
				action.setContent(doc);
				throw new OBPMValidateException("{*[core.document.cannotsave]*}");
			}
			action.setContent(doc);
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
			action.addActionMessage("{*[cn.myapps.core.dynaform.activity.type.successfully_saved_and_open_process]*}");
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			e.printStackTrace();
		}catch (Exception e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(), e));
			e.printStackTrace();
		}
		return AbstractRunTimeAction.FORM;
	}
	
	private String runStartFlowScript(Document doc,ParamsTable params,WebUser user) throws Exception{
		
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), doc.getApplicationid());
		runner.initBSFManager(doc, params, user,
				new ArrayList<ValidateMessage>());

		StringBuffer label = new StringBuffer();
		label.append("Activity(").append(this.act.getName())
				.append(")." + this.act.getId())
				.append(".StartFlowScript");

		Object result = runner.run(label.toString(),act.getStartFlowScript());
		if(result !=null) return (String) result;
		
		return null;
		
		
		
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
