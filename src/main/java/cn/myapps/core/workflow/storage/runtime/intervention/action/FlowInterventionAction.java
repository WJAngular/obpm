package cn.myapps.core.workflow.storage.runtime.intervention.action;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;


import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.core.workflow.storage.runtime.intervention.ejb.FlowInterventionProcessBean;
import cn.myapps.core.workflow.storage.runtime.intervention.ejb.FlowInterventionVO;
import cn.myapps.util.ProcessFactory;

public class FlowInterventionAction extends AbstractRunTimeAction<FlowInterventionVO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4072644497765893046L;
	
	private String nextNodeId;
	
	
	
	public String getNextNodeId() {
		return nextNodeId;
	}


	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}


	@Override
	public IRunTimeProcess<FlowInterventionVO> getProcess() {
		return new FlowInterventionProcessBean(getApplication());
	}
	
	
	@SuppressWarnings("static-access")
	public String doFlow() throws Exception {
		try{
			
			ParamsTable params = getParams();
			String[] ids = (String[]) (params.getParameterAsArray("id"));
			String id = (ids != null && ids.length > 0) ? ids[0] : null;
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,getApplication());
			Document doc =  (Document) proxy.doView(id);
			WebUser user = (WebUser) this.getContext().getSession().get(Web.SESSION_ATTRIBUTE_USER);
			//user.setApplicationid(getApplication());
			if(doc !=null && doc.getState() !=null){

				if(doc.getState().isTerminated()){
					doc.getState().setTerminated(false);
					((DocumentProcess) proxy).doFlow(doc, params, "terminate_id",new String[]{getNextNodeId()}, FlowType.RUNNING2RUNNING_INTERVENTION, "",user );
				}else if(doc.getState().isComplete()){
					
					RelationHISProcess procss = (RelationHISProcess)ProcessFactory.createRuntimeProcess(RelationHISProcess.class, doc.getApplicationid());
					RelationHIS his = procss.getCompleteRelationHIS(doc.getId(), doc.getState().getId());
					if(his != null)
						if (getNextNodeId()!=null && !getNextNodeId().equals(his.getEndnodeid())) {
							((DocumentProcess) proxy).doFlow(doc, params, his.getEndnodeid(),new String[]{getNextNodeId()}, FlowType.RUNNING2RUNNING_INTERVENTION, "",user );
						}
				}else if(doc.getState().getNoderts()!=null && doc.getState().getNoderts().size()>0){
					NodeRTProcess process = new NodeRTProcessBean(doc.getApplicationid());
					Collection<NodeRT> noderts = process.queryByFlowStateRT(doc.getStateid());
					for(NodeRT nodert : noderts){
						((DocumentProcess) proxy).doFlow(doc, params, nodert.getNodeid(),new String[]{getNextNodeId()}, FlowType.RUNNING2RUNNING_INTERVENTION, "",user );
					}
				}
				
				doList();
			}else{
				throw new OBPMValidateException("Doucment should not be null");
			}
			
			
			
		}catch (OBPMValidateException e) {
			addFieldError("System Error", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	
	public String doList() throws Exception {
		try{
			ParamsTable params = getParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			params.removeParameter("_pagelines");
			params.setParameter("_pagelines", lines);
			this.setDatas(this.getProcess().doQuery(params, getUser()));
		}catch (OBPMValidateException e) {
			addFieldError("System Error", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	public String doView() throws Exception {
		Map<?, ?> params = getContext().getParameters();

		String[] ids = (String[]) (params.get("id"));
		String id = (ids != null && ids.length > 0) ? ids[0] : null;

		ValueObject contentVO = this.getProcess().doView(id);
		setContent(contentVO);

		return SUCCESS;
	}
	
	public String doDelete() {
		try {
			ParamsTable params = getParams();
			String[] _selects = (String[]) (params.getParameterAsArray("_selects"));
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,getApplication());
			proxy.doRemove(_selects);
			params.setParameter("_currpage", "1");
			this.setDatas(this.getProcess().doQuery(params, getUser()));
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}



	
	

}
