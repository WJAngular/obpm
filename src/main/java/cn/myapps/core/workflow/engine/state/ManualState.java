package cn.myapps.core.workflow.engine.state;

import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.dts.excelimport.FlowType;
import cn.myapps.core.scheduler.ejb.Job;
import cn.myapps.core.scheduler.ejb.ManualNodeSkipApprovalJob;
import cn.myapps.core.scheduler.ejb.ManualNodeTimingApprovalJob;
import cn.myapps.core.scheduler.ejb.SchedulerFactory;
import cn.myapps.core.scheduler.ejb.TriggerVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.State;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcessBean;

public class ManualState extends AbstractState implements State {

	public ManualState(Node node) {
		super(node);
	}
	
	/**
	 * 当前节点状态切换
	 * 
	 * @param params
	 *            参数
	 * @param doc
	 *            文档
	 * @param flowVO
	 *            流程
	 * @param user
	 *            当前用户
	 * 
	 * @param flowOption
	 *            流程操作
	 * @return
	 * @throws Exception
	 */
	public NodeRT process(ParamsTable params, NodeRT origNodeRT,FlowStateRT instance, WebUser user, String flowOption)
			throws Exception {
		instance.setComplete(false);
		NodeRTProcess nodeRTProcess = new NodeRTProcessBean(instance
				.getApplicationid());
		
		
		NodeRT newNodeRT = nodeRTProcess.doCreate(params, origNodeRT, instance, node, flowOption,user);
		
		if(FlowType.RUNNING2RUNNING_NEXT.equals(flowOption) && ((ManualNode)node).isAllowSkip 
				&& ManualNode.PASS_CONDITION_OR == ((ManualNode)node).getPassCondition() 
				&& newNodeRT.getActorIdList().contains(user.getId())){
			Job job = new ManualNodeSkipApprovalJob(instance.getDocid(),instance.getId(),newNodeRT.getId(),instance.getApplicationid(),user.getId());
			TriggerVO trigger = new TriggerVO(job, new Date().getTime());
			SchedulerFactory.getScheduler().addTrigger(trigger);
			
		}else if(((ManualNode)node).isLimited && newNodeRT.getDeadline() !=null){
			Job job = new ManualNodeTimingApprovalJob(instance.getDocid(),instance.getId(),newNodeRT.getId(),instance.getApplicationid());
			TriggerVO trigger = new TriggerVO(job, newNodeRT.getDeadline().getTime());
			SchedulerFactory.getScheduler().addTrigger(trigger);
		}
		
		return newNodeRT;
	}

	public int toInt() {
		return FlowState.RUNNING;
	}

}
