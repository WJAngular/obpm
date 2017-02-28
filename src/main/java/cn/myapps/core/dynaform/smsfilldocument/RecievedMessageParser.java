package cn.myapps.core.dynaform.smsfilldocument;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageVO;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageProcess;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class RecievedMessageParser {

	public static void parse(ReceivedMessageVO recVO) throws DataMessageException, OBPMValidateException {
		int status = 0;
		StringBuffer content = new StringBuffer();
		SMSModeProxy sender = new SMSModeProxy("", recVO.getApplicationid(), recVO.getDomainid());

		String recMsg = recVO.getContent();
		String[] parse = recMsg.split(";");
		if (parse.length > 2) {
			try {
				// ----------短信填单----------
				DataMessageParser parser = new DataMessageParser(parse);
				WebUser user = parser.getUser();
				FormHelper helper = new FormHelper();
				// 第4个为表单关联字段
				Form form = helper.findFormByRelationName(parse[3]);
				String[] values = parser.getFieldValuses();
				content.append("Fill Document[").append(parse[3]);
				for (int i = 0; i < values.length; i++) {
					if (i > 3)
						break;
					content.append(";").append(values[i]);
				}
				content.append("...] ");

				if (form != null) {
					try {
						sender.setDomainid(user.getDomainid());
						sender.setApplication(form.getApplicationid());
						ParamsTable params = parser.getParamsByRelationText(form.getRelationText());
						addFlowId(params, form);
						user.setDefaultApplication(form.getApplicationid());
						Document doc = form.createDocument(params, user);
						DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(
								DocumentProcess.class, form.getApplicationid());
						process.doCreate(doc);
						if (form.isOnSaveStartFlow()) {
							process.doStartFlowOrUpdate(doc, params, user);
						} else {
							process.doUpdate(doc, user);
						}
					} catch (Exception e) {
						// 失败，发送信息提示失败原因
						content.append("Failure! Error: " + e.getMessage());
						status = -2;
					}
					// 成功，发送信息通知用户
					content.append("Successful!");
				} else {
					// 失败，发送信息提示失败原因
					content.append("Failure! Error: No such Form[").append(parse[3]).append("] in Application!");
					status = -1;
				}
			} catch (Exception e) {
				content.append("Fill Document Failure! Error: ").append(e.getMessage());
				status = -2;
			}
		} else {
			try {
				parse = recMsg.split(":");
				if (parse.length == 2) {
					// --------短信审批---------

					String replyCode = parse[0];

					SubmitMessageProcess messageProcess = (SubmitMessageProcess) ProcessFactory
							.createProcess(SubmitMessageProcess.class);
					SubmitMessageVO smVO = messageProcess.getMessageByReplyCode(replyCode, recVO.getSender());

					if (smVO == null) {
						status = -1;
						content.setLength(0);
						content.append("Error: Reply Code is invalid!");
						throw new Exception();
					}
					int yes = -1;

					if (StringUtil.isBlank(smVO.getDocid())) {
						status = -1;
						content.setLength(0);
						content.append("Error: no find document!");
						throw new Exception();
					}
					yes = Integer.parseInt(parse[1]);
					// 0为回退流程 1为提交流程
					if (yes == 0 || yes == 1) {
						DocumentProcess documentProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, smVO.getApplicationid());
						FlowStateRTProcess fProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, smVO.getApplicationid());
						Document doc = (Document) documentProcess.doView(smVO.getDocid());
						UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
						UserVO userVO = (UserVO) userProcess.doView(smVO.getReceiverUserID());
						WebUser user = new WebUser(userVO);
						ParamsTable params = new ParamsTable();
						if (yes == 1) {
							// 提交流程
							documentProcess.doBatchApprove(new String[] { smVO.getDocid() }, user, Environment
									.getInstance(), params, null);
						} else {
							// 回退流程
							NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user,null);
							if (nodert == null)
								throw new Exception();
							BillDefiVO flowVO = doc.getFlowVO();

							Collection<Node> backList = StateMachine.getBackToNodeList(doc, flowVO, nodert,
									user);
							RelationHISProcess hisProcess = StateMachine.getRelationHISProcess(doc.getApplicationid());
							RelationHIS his = hisProcess.doViewLast(doc.getId(), doc.getFlowid());
							if (his == null) {
								throw new Exception();
							}
							if (backList != null && !backList.isEmpty()) {
								for (Iterator<Node> iter = backList.iterator(); iter.hasNext();) {
									// Node backNode = (Node) iter.next();
									Node backNode = iter.next();
									if (backNode.id.equals(his.startnodeid)) {
										fProcess.doApprove(params, doc.getState(), nodert.getNodeid(), new String[] { backNode.id },  FlowType.RUNNING2RUNNING_BACK, "", user);
									}
								}
							}
						}
						status = 0;
						content.setLength(0);
						content.append("Message: Process until a successful!");
					} else {
						status = -1;
						// 审批方式必须是0或1
						content.setLength(0);
						content.append("Error: Processing methods must be 0 or 1");
					}

				}
			} catch (Exception e) {
				status = -1;
				if (StringUtil.isBlank(content.toString())) {
					// 流程处理失败
					content.setLength(0);
					content.append("Exception: Treatment failure process!");
				}
			}
		}

		try {
			sender.send("", content.toString(), recVO.getSender());
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBPMValidateException(content.toString(),new DataMessageException(status, content.toString()));
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

	}

	// @SuppressWarnings("unchecked")
	private static void addFlowId(ParamsTable params, Form form) {
		Collection<Activity> acts = form.getActivitys();
		if (acts != null) {
			for (Iterator<Activity> iter = acts.iterator(); iter.hasNext();) {
				Activity act = iter.next();
				if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
					params.setParameter("_flowid", act.getOnActionFlow());
				}
			}
		}
	}
}
