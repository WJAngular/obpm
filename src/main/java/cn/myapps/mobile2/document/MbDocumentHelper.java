package cn.myapps.mobile2.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRTProcessBean;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class MbDocumentHelper {

	public static String refreshFlowXML(Document olddoc, WebUser user, ParamsTable params,String type,String actid) throws Exception {
		Document doc = (Document) olddoc.clone();
		NodeRT nodert = null;
		String flowid = doc.getFlowid();
		HttpServletRequest request = params.getHttpRequest();
		if (doc.getIstmp() || StringUtil.isBlank(doc.getStateid())) {
			if (!StringUtil.isBlank(flowid) && !"null".equals(flowid)) {
				// doc = (Document) doc.clone();
				FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(
						FlowStateRTProcess.class, doc.getApplicationid());

				BillDefiProcess billDefiProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);

				BillDefiVO flowVO = (BillDefiVO) billDefiProcess.doView(flowid);
				Node firstNode = StateMachine.getFirstNode(doc, flowVO, user, params);

				if (firstNode != null) {
					FlowStateRT state = stateProcess.createTransientFlowStateRT(doc, flowid, user);
					ArrayList<NodeRT> noderts = new ArrayList<NodeRT>();
					nodert = new NodeRT(state, firstNode, FlowType.START2RUNNING);
					noderts.add(nodert);
					state.setNoderts(noderts);

					doc.setState(state);// 创建瞬态流程实例
					doc.setInitiator(user.getId());// 设置流程发起人
					Node startNode = StateMachine.getStartNodeByFirstNode(flowVO, firstNode);
					if (startNode != null) {
						// 设置上下文当前处理节点对象
						request.setAttribute("_targetNode", firstNode.id);
						request.setAttribute("_targetNodeRT", nodert);
						
					}
				}
			}
		}
		StateMachineHelper helper = new StateMachineHelper(doc);
		if("init".equals(type)){
			return helper.toFlowXMLTextForInit(doc, user, request,actid);
		}else if ("commitTo".equals(type)) {
			return helper.toFlowXMLTextForCommitTo(doc, user, "", request);
		}else if ("returnTo".equals(type)) {
			return helper.toFlowXMLTextForReturnTo(doc, user,
					"",request);
		}
		
		return "";
	}

	public static ArrayList<Activity> doGetActivity(Form form, ParamsTable params, Document doc, WebUser user)
			throws Exception {
		try {
			ArrayList<Activity> actList = new ArrayList<Activity>();
			Collection<ValidateMessage> errors = new ArrayList<ValidateMessage>();
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
			runner.initBSFManager(doc, params, user, errors);

			// add activity xml text.
			Collection<Activity> acts = form.getActivitys();
			if (acts != null) {
				for (Iterator<Activity> iter = acts.iterator(); iter.hasNext();) {
					Activity act = (Activity) iter.next();
					if (act.isHidden(runner, form, doc, user, ResVO.FORM_TYPE)) {
						continue;
					}
					if (act.getType() == ActivityType.START_WORKFLOW) { // 启动流程
						if (!StringUtil.isBlank(doc.getParentid())
								|| !(doc.getStateid() == null || doc.getStateid() == "" || doc.getStateLabel() == null || doc
										.getStateLabel().equals("")))
							continue;
					}
					// 打印Activity不生成XML
					if (act.getType() == ActivityType.PRINT || act.getType() == ActivityType.PRINT_WITHFLOWHIS
							|| act.getType() == ActivityType.EXPTOPDF || act.getType() == ActivityType.EMAIL_TRANSPOND) {
						continue;
					}
					actList.add(act);
				}
			}
			return actList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static ArrayList<String> doGetFormField(Document doc, Form form, ParamsTable params, WebUser user)
			throws Exception {
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), doc.getApplicationid());
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
		ArrayList<String> fields = new ArrayList<String>();
		Iterator<FormField> iter = form.getFields().iterator();
		while (iter.hasNext()) {
			try {
				FormField fld = (FormField) iter.next();
				if (fld.isMobile()) {
					String content = fld.toMbXMLText2(doc, runner, user);
					if (content != null && content.trim().length() > 0) {
						fields.add(content);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return fields;
	}

	/**
	 * 更新表单状态
	 * 
	 * @param doc
	 * @param webUser
	 * @throws Exception
	 */
	public static void changeStatic(Document doc, WebUser webUser) throws Exception {
		try {
			String applicationId = doc.getApplicationid();
			// -------------------------文档已阅未阅功能
			if (doc != null && doc.getState() != null) {
				Collection<ActorRT> actors = doc.getState().getActors();
				for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
					ActorRT actor = iter.next();
					if (webUser.getId().equals(actor.getActorid())) {
						if (!actor.getIsread()) {
							actor.setIsread(true);
							ActorRTProcess process = new ActorRTProcessBean(applicationId);
							process.doUpdate(actor);
						}
						break;
					}
				}
				// 查找抄送人为当前用户
				CirculatorProcess cProcess = (CirculatorProcess) ProcessFactory.createRuntimeProcess(
						CirculatorProcess.class, applicationId);
				Circulator circulator = cProcess.findByCurrDoc(doc.getId(), doc.getState().getId(), false, webUser);
				if (circulator != null) {
					circulator.setRead(true);
					circulator.setReadTime(new Date());
					cProcess.doUpdate(circulator);// 更新为已阅
				}

				// -------------------------------- 选择可执行的流程实例
				if (!StringUtil.isBlank(doc.getId())) {
					FlowStateRTProcess stateProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(
							FlowStateRTProcess.class, applicationId);
					if (stateProcess.isMultiFlowState(doc)) {// 有多个没完成是流程实例
						doc.setState(stateProcess.getCurrFlowStateRT(doc, webUser, null));// 绑定一个可执行的文档实例
						doc.setMulitFlowState(stateProcess.isMultiFlowState(doc, webUser));// 是否存在多个可执行实例
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
