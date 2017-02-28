package cn.myapps.core.workflow.engine.state;

import java.util.ArrayList;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.element.CompleteNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.engine.State;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class CompleteState extends AbstractState implements State {

	public CompleteState(Node node) {
		super(node);
	}

	public NodeRT process(ParamsTable params, NodeRT origNodeRT, FlowStateRT instance, WebUser user,
			String flowOption) throws Exception {
		instance.setComplete(true);
		if(!instance.isSubFlowStete()){
			//自动归档
			if(this.node instanceof CompleteNode){
				if(((CompleteNode)node).isAutoArchive){
					DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, instance.getApplicationid());
					proxy.doArchive(instance.getDocument(), user, params);
				}
			}
			
			return super.process(params, origNodeRT, instance, user, flowOption);
		}else{//当前流程实例为子流程实例
			/*
			 * 1.判断同批次创建的所有子流程实例是否已经完成
			 * 2.第一步返回true 则父流程实例提交到下一结点 同时运行子流程的回调脚本
			 */
			FlowStateRTProcess process = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, instance.getApplicationid());
			
			if(instance.isCallback() && process.isAllSubFlowStateRTComplete(instance)){//同批次创建的所有子流程实例都已经完成
				process.runCallbackScript(params, instance, user);//启动回调脚本
				process.doParentFlow2Next(instance, params,user);
				//instance.getDocument().setVersions(instance.getDocument().getVersions() + 5); 
			}
			
			//自动归档
			if(this.node instanceof CompleteNode){
				if(((CompleteNode)node).isAutoArchive){
					DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, instance.getApplicationid());
					proxy.doArchive(instance.getDocument(), user, params);
				}
			}
			
			return super.process(params, origNodeRT, instance, user, flowOption);
		}
	}
	
	public void processCrossForm(ParamsTable params, NodeRT pfNodert, Document doc, BillDefiVO pflowVO, WebUser user,
			String flowOption) throws Exception {
		DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,pflowVO.getApplicationid());

		// 主流程提交
		if (pfNodert != null) {
			// pf=parentflow
			Document pdoc = (Document) docProcess.doView(pfNodert.getDocid());

			SubFlow subFlowNode = (SubFlow) pflowVO.findNodeById(pfNodert.getNodeid());
			// 执行callback脚本
			runScript(subFlowNode, params, doc, pdoc, pflowVO, user);

			String[] pfNextNodeids = pflowVO.getNextNodeids(pfNodert.getNodeid(),doc,params,user);
			// 从主流程提交到下一步
//			StateMachine.doFlow(params, pdoc, pflowVO, pfNodert.getNodeid(), pfNextNodeids, user, flowOption, "",
//					Environment.getInstance()); //暂时屏蔽
			docProcess.doCreateOrUpdate(pdoc, user);
		}
	}

	public void processSingleForm(ParamsTable params, NodeRT pfNodert, Document doc, BillDefiVO flowVO, WebUser user,
			String flowOption) throws Exception {
		// 主流程提交
		if (pfNodert != null) {
			// pf=parentflow
			SubFlow subFlowNode = (SubFlow) flowVO.findNodeById(pfNodert.getNodeid());
			// 执行callback脚本
			runScript(subFlowNode, params, doc, doc, flowVO, user);

			String[] pfNextNodeids = flowVO.getNextNodeids(pfNodert.getNodeid(),doc,params,user);
			// 从主流程提交到下一步
//			StateMachine.doFlow(params, doc, flowVO, pfNodert.getNodeid(), pfNextNodeids, user, flowOption, "",
//					Environment.getInstance());//暂时屏蔽
			// docProcess.doCreateOrUpdate(doc, user);
		}
	}

	/**
	 * 执行子流程callback脚本
	 * 
	 * @param node
	 * @param params
	 * @param doc
	 * @param flowVO
	 * @param user
	 * @throws Exception
	 */
	public void runScript(SubFlow node, ParamsTable params, Document doc, Document pdoc, BillDefiVO parentFlowVO,
			WebUser user) throws Exception {
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), parentFlowVO.getApplicationid());
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
		runner.declareBean("$CALLBACK_DOC", pdoc, Document.class);

		StringBuffer label = new StringBuffer();
		label.append("WorkFlow").append("." + parentFlowVO.getSubject()).append(".Node(").append(node.id).append(
				")." + node.name).append(".CallbackScript");

		String callbackScript = StringUtil.dencodeHTML(node.callbackScript);
		if (!StringUtil.isBlank(callbackScript)) {
			runner.run(label.toString(), callbackScript);
		}
	}

	public int toInt() {
		return FlowState.COMPLETE;
	}
}
