package cn.myapps.core.scheduler.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.AutoNode;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class ManualNodeSkipApprovalJob implements Job {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7075302742019717112L;

	private String docId;
	
	private String flowStateId;
	
	private String nodeId;
	
	private String actorId;
	
	private String applicationId;
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFlowStateId() {
		return flowStateId;
	}

	public void setFlowStateId(String flowStateId) {
		this.flowStateId = flowStateId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public ManualNodeSkipApprovalJob(String docId, String flowStateId,
			String nodeId,String applicationId) {
		super();
		this.docId = docId;
		this.flowStateId = flowStateId;
		this.nodeId = nodeId;
		this.applicationId = applicationId;
		this.actorId = null;
	}

	public ManualNodeSkipApprovalJob(String docId, String flowStateId,
			String nodeId,String applicationId, String actorId ) {
		super();
		this.docId = docId;
		this.flowStateId = flowStateId;
		this.nodeId = nodeId;
		this.applicationId = applicationId;
		this.actorId = actorId;
	}

	public void execute() throws Exception {
		try {
			NodeRTProcess nodertProcess = (NodeRTProcess) ProcessFactory.createRuntimeProcess(NodeRTProcess.class, this.applicationId);
			DocumentProcess docProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, this.applicationId);
			
			NodeRT nodert = (NodeRT) nodertProcess.doView(this.nodeId);
			if(nodert !=null){
				Document doc = (Document) docProcess.doView(docId);
				if(doc ==null) return;
				doc.setState(flowStateId);
				FlowStateRT instance = doc.getState();
				BillDefiVO flowVO = instance.getFlowVO();
				
				WebUser user = null;
				if(!StringUtil.isBlank(actorId)){
					UserProcess userProcess =  (UserProcess)ProcessFactory.createProcess(UserProcess.class);
					user = new WebUser((BaseUser)userProcess.doView(actorId));
				}
				
				FlowStateRTProcess instanceProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, this.applicationId);

				IRunner runner = JavaScriptFactory.getInstance(null, this.applicationId);
				runner.initBSFManager(doc, new ParamsTable(), user, new ArrayList<ValidateMessage>());
				ManualNode currNode = (ManualNode) flowVO.findNodeById(nodert.getNodeid());
				Collection<Node> nextNodeList = flowVO.getNextNodeList(nodert.getNodeid(),doc,null,user);
				if (currNode != null && nextNodeList != null
						&& !nextNodeList.isEmpty()) {
					Collection<String> nextIdList = new ArrayList<String>();
					for (Iterator<Node> iterator = nextNodeList.iterator(); iterator
							.hasNext();) {
						Node node = (Node) iterator.next();
						nextIdList.add(node.id);
						if (!currNode.issplit) { // 非分散
							break;
						}
					}
					instance.setDocument(doc);
					String[] nextids = (String[]) nextIdList
							.toArray(new String[nextIdList.size()]);
					
					instanceProcess.doApprove(new ParamsTable(), instance, nodert.getNodeid(),
							nextids, FlowType.SKIP, "", user);
					
				}
			}
			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
				}
			}
	}
	
	

}
