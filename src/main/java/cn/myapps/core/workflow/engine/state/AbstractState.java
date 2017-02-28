package cn.myapps.core.workflow.engine.state;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachineUtil;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcessBean;

public abstract class AbstractState {
	protected Node node;

	public AbstractState(Node node) {
		this.node = node;
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
		NodeRTProcess nodeRTProcess = new NodeRTProcessBean(instance
				.getApplicationid());
		NodeRT newNodeRT = nodeRTProcess.doCreate(params, origNodeRT, instance, node, flowOption,user);
		/*if(newNodeRT.isComplete()){
			nodeRTProcess.doRemoveByFlowStateRT(instance.getId());
			instance.getNoderts().clear();
		}*/
		return newNodeRT;
	}

	public Collection<String> getPrincipalIdList(Document doc,ParamsTable params, String domainId, String applicationid,BaseUser auditor)
			throws Exception {
		Collection<String> prinspalIdList = StateMachineUtil.getPrincipalIdList(doc,params, node, domainId, applicationid, auditor);
		return prinspalIdList;
	}
}
