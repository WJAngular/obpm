package cn.myapps.core.workflow.engine.state;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.State;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcessBean;

public class SuspendState extends AbstractState implements State {

	public SuspendState(Node node) {
		super(node);
	}

	public NodeRT process(ParamsTable params, NodeRT origNodeRT,FlowStateRT instance, WebUser user,
			String flowOption) throws Exception {
		instance.setComplete(false);
		NodeRTProcess nodeRTProcess = new NodeRTProcessBean(instance.getApplicationid());
		NodeRT newNodeRT = nodeRTProcess.doCreate(params, origNodeRT, instance, node, flowOption, user);

		return newNodeRT;
	}

	public int toInt() {
		return FlowState.SUSPEND;
	}
}
