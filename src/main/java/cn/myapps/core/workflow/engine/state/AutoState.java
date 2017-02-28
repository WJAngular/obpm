package cn.myapps.core.workflow.engine.state;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.scheduler.ejb.AutoNodeTimingApprovalJob;
import cn.myapps.core.scheduler.ejb.SchedulerFactory;
import cn.myapps.core.scheduler.ejb.TriggerVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.element.AutoNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.State;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcessBean;
import cn.myapps.util.ProcessFactory;

public class AutoState extends AbstractState implements State {

	public AutoState(Node node) {
		super(node);
	}

	public NodeRT process(ParamsTable params, NodeRT origNodeRT, FlowStateRT instance, WebUser user,
			String flowOption) throws Exception {
		instance.setComplete(false);
		NodeRTProcess nodeRTProcess = new NodeRTProcessBean(instance.getApplicationid());
		SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
		
		WebUser admin = new WebUser(superUserProcess.getDefaultAdmin()); // 系统用户
		admin.setDomainid(instance.getDocument().getDomainid());
		admin.setName("system");
		
		NodeRT newNodeRT = nodeRTProcess.doCreate(params, origNodeRT, instance, node, flowOption, admin);
		
		//AutoAuditJobManager.addJob(new AutoNodeAutoAuditJob(instance,newNodeRT, node.id, admin));// 添加Job
		
		AutoNodeTimingApprovalJob job = new AutoNodeTimingApprovalJob(instance.getDocid(),instance.getId(),newNodeRT.getId(),instance.getApplicationid());
		
		if(AutoNode.AUTO_AUDIT_TYPE_IMMEDIATELY==((AutoNode)node).autoAuditType){
			job.setFlowTicket(instance.getDocument().getFlowTicket());
		}
		TriggerVO trigger = new TriggerVO(job, newNodeRT.getDeadline().getTime());
		SchedulerFactory.getScheduler().addTrigger(trigger);
		
		return newNodeRT;
	}

	public int toInt() {
		return FlowState.AUTO;
	}
}
