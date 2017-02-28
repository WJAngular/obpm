package cn.myapps.mobile2.service;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.form.ejb.ViewDialogField;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.scheduler.ejb.Job;
import cn.myapps.core.scheduler.ejb.ManualNodeTimingApprovalJob;
import cn.myapps.core.scheduler.ejb.SchedulerFactory;
import cn.myapps.core.scheduler.ejb.TriggerVO;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.OGraphics;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.notification.ejb.NotificationConstant;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.mobile2.document.MbDocumentHelper;
import cn.myapps.mobile2.document.MbDocumentXMLBuilder;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.sequence.Sequence;

public class MbServiceHelper {

	/**
	 * 执行前脚本
	 * 
	 * @param act
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static Document getDocument(Activity act, ParamsTable params, WebUser user) throws Exception {
		String formid = params.getParameterAsString("_formid");
		String parentId = params.getParameterAsString("parentid");
		String docid = params.getParameterAsString("_docid");
		String applicationid = params.getParameterAsString("_application");
		Document doc = null;

		if (StringUtil.isBlank(formid)) {
			formid = act.getOnActionForm();
		}
		if (StringUtil.isBlank(applicationid)) {
			applicationid = act.getApplicationid();
		}

		if (StringUtil.isBlank(parentId) || parentId.equals("null")) {
			params.removeParameter("parentid");
		}
		DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				applicationid);
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) formProcess.doView(formid);
		Document olddoc = (Document) proxy.doView(docid);

		if (olddoc == null) {
			olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
		}

		if (form != null) {
			if (olddoc != null) {
				doc = form.createDocument(olddoc, params, user);
			} else {
				doc = form.createDocument(params, user);
			}
		}
		return doc;
	}

	/**
	 * 执行后脚本
	 */
	public static String doAfter(Activity act, Document doc, ParamsTable params, WebUser user) throws Exception {
		if (doc == null) {
			doc = new Document();
		}
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), act.getApplicationid());
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
		if ((act.getAfterActionScript()) != null && (act.getAfterActionScript()).trim().length() > 0) {

			String script = StringUtil.dencodeHTML(act.getAfterActionScript());

			StringBuffer label = new StringBuffer();
			label.append("Activity Action").append(act.getId()).append("." + act.getName()).append("afterActionScript");
			Object result = runner.run(label.toString(), script);

			if (result != null) {
				if (result instanceof JsMessage) {
					if (((JsMessage) result).getType() == JsMessage.TYPE_ALERT) {
						StringBuffer sb = new StringBuffer();
						sb.append("<").append(MobileConstant.TAG_JSMESSAGE).append(" ");
						sb.append(MobileConstant.ATT_TYPE).append(" = \"").append(((JsMessage) result).getType())
								.append("\">");
						sb.append(((JsMessage) result).getContent());
						sb.append("</").append(MobileConstant.TAG_JSMESSAGE).append(">");
						return sb.toString();
					}
				}
			}
		}
		return "";
	}

	public static void toDocumentXML(Document doc, ParamsTable params, WebUser user) throws Exception {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			Form form = doc.getForm();
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
			request.setAttribute("doc", doc);
			request.setAttribute("form", form);

			ArrayList<Activity> activitylist = MbDocumentHelper.doGetActivity(form, params, doc, user);
			request.setAttribute("activitylist", activitylist);

			ArrayList<String> formfieldlist = MbDocumentHelper.doGetFormField(doc, form, params, user);
			request.setAttribute("formfieldlist", formfieldlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ActivityParent dogetParentById(ParamsTable params) throws Exception {
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		String _viewid = params.getParameterAsString("_viewid");
		String _formid = params.getParameterAsString("_formid");
		if (!StringUtil.isBlank(_viewid)) {
			return (ActivityParent) viewProcess.doView(_viewid);
		} else if (!StringUtil.isBlank(_formid)) {
			return (ActivityParent) formProcess.doView(_formid);
		}
		return null;
	}

	/**
	 * 获取跳转类型url
	 * 
	 * @param act
	 *            activity
	 * @param params
	 *            ParamsTable
	 * @param user
	 *            WebUser
	 * @return
	 * @throws Exception
	 */
	public static String doDispathUrl(Activity act, ParamsTable params, WebUser user) throws Exception {
		if (act.getJumpMode() == Activity.JUMP_TO_URL) {
			if ((act.getDispatcherUrl()) != null && (act.getDispatcherUrl()).trim().length() > 0) {
				StringBuffer label = new StringBuffer();
				// new doc
				Document doc = new Document();

				String script = StringUtil.dencodeHTML(act.getDispatcherUrl());

				StringBuffer url = new StringBuffer();
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
						params.getParameterAsString("application"));
				runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

				label.append(" Activity(").append(act.getId()).append(")." + act.getName())
						.append(".dispatcherUrlScript");
				Object result = runner.run(label.toString(), script);

				if (result != null) {
					url.append(result);
				}

				if (url != null && url.length() > 0) {
					if (act.getDispatcherParams() != null && act.getDispatcherParams() != "") {
						Collection<Object> col = JsonUtil.toCollection(act.getDispatcherParams(), JSONObject.class);
						for (Iterator<Object> iterator = (Iterator<Object>) col.iterator(); iterator.hasNext();) {
							JSONObject object = JSONObject.fromObject(iterator.next());
							if (url.toString().indexOf("?") > -1) {
								url.append("&");
							} else {
								url.append("?");
							}
							url.append(object.get("paramKey")).append("=").append(object.get("paramValue"));
						}
					}
				}
				return url.toString();
			}
		}
		return "";
	}

	public static String doRetracement(WebUser user, ParamsTable params) throws Exception {
		String applicationid = params.getParameterAsString("_application");
		DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				applicationid);
		Document doc = (Document) docProcess.doView(params.getParameterAsString("_docid"));
		if (user.getStatus() == 1) {
			try {
				doc = DocumentHelper.rebuildDocument(doc, params, user);
				DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
						doc.getApplicationid());
				BillDefiVO flowVO = doc.getState().getFlowVO();
				FlowDiagram fd = flowVO.toFlowDiagram();

				Node currNode = null;
				if (doc.getState().isComplete()) {
					RelationHISProcess procss = (RelationHISProcess) ProcessFactory.createRuntimeProcess(
							RelationHISProcess.class, doc.getApplicationid());
					RelationHIS his = procss.getCompleteRelationHIS(doc.getId(), doc.getState().getId());
					currNode = (Node) fd.getElementByID(his.getEndnodeid());

				} else if (doc.getState().getNoderts() != null && doc.getState().getNoderts().size() > 0) {
					NodeRT nodert = (NodeRT) doc.getState().getNoderts().iterator().next();
					currNode = (Node) fd.getElementByID(nodert.getNodeid());
				}

				Node nextNode = StateMachine.getBackNodeByHis(doc, flowVO, currNode.id, user, FlowState.RUNNING);
				if (nextNode != null) {

					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), applicationid);
					runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

					boolean allowRetracement = false;
					if (((ManualNode) nextNode).retracementEditMode == 0 && ((ManualNode) nextNode).cRetracement) {
						allowRetracement = true;
					} else if (((ManualNode) nextNode).retracementEditMode == 1
							&& ((ManualNode) nextNode).retracementScript != null
							&& (((ManualNode) nextNode).retracementScript).trim().length() > 0) {
						StringBuffer label = new StringBuffer();
						label.append(doc.getFormname()).append(".Activity(").append(params.getParameter("_activityid"))
								.append("流程回撤").append(".retracementScript").append(")");
						String script = StringUtil.dencodeHTML(((ManualNode) nextNode).retracementScript);
						Object result = runner.run(label.toString(), script);
						if (result != null && result instanceof Boolean) {
							if (((Boolean) result).booleanValue())
								allowRetracement = true;
						}
					}

					if (allowRetracement) {
						Collection<Node> fromNode = new ArrayList<Node>();
						if (((ManualNode) nextNode).issplit) {

							Collection<NodeRT> noderts = doc.getState().getNoderts();
							Collection<Node> nextNodeList = fd.getNextNodeList(nextNode.id,doc,params,user);
							if (!nextNodeList.isEmpty() && !noderts.isEmpty()) {
								for (Iterator<Node> iterator = nextNodeList.iterator(); iterator.hasNext();) {
									Node node = (Node) iterator.next();
									for (Iterator<NodeRT> iter = noderts.iterator(); iter.hasNext();) {
										NodeRT rt = iter.next();
										if (node.id.equals(rt.getNodeid())) {
											fromNode.add(node);
											break;
										}

									}

								}
							}

						} else {
							fromNode.add(currNode);
						}

						for (Iterator<Node> iterator = fromNode.iterator(); iterator.hasNext();) {
							Node node = (Node) iterator.next();

							// 指的审批人
							String submitTo = "[{\"nodeid\":'" + nextNode.id
									+ "',\"isToPerson\":'true',\"userids\":\"['" + user.getId() + "']\"},]";
							params.setParameter("submitTo", submitTo);

							String[] nextids = { nextNode.id };
							doc = ((DocumentProcess) proxy).doFlow(doc, params, node.id, nextids,
									FlowType.RUNNING2RUNNING_RETRACEMENT, params.getParameterAsString("_attitude"),
									user);

							String _targetNode = params.getParameterAsString("_targetNode");

							// 设置审批节点
							if (!StringUtil.isBlank(doc.getStateid())) {
								String defaultNodeId = null;
								if (!StringUtil.isBlank(_targetNode)) {
									defaultNodeId = _targetNode;
								}
								NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user, defaultNodeId);
								if (nodert != null) {
									ServletActionContext.getRequest().setAttribute("_targetNode", nodert.getNodeid());
									ServletActionContext.getRequest().setAttribute("_targetNodeRT", nodert);
								}
							}
							MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
							return MbDocumentXMLBuilder.toMobileXML(doc, user, params);

						}
					} else {
						return "[ERROR]*" + "此流程状态下不允许回撤";
					}
				} else {
					return "[ERROR]*" + "此流程状态下不允许回撤";
				}
			} catch (OBPMValidateException e) {
				return "[ERROR]*" + e.getValidateMessage();
			} catch (Exception e) {
				return "[ERROR]*" + e.getMessage();
			}
		} else {
			return "[ERROR]*" + "{*[core.user.noeffectived]*}";
		}
		return "";
	}

	/**
	 * 终止流程
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doTerminateFlow(WebUser user, ParamsTable params) throws Exception {
		try {
			String formid = params.getParameterAsString("_formid");
			String docid = params.getParameterAsString("_docid");
			String parentId = params.getParameterAsString("parentid");
			String applicationid = params.getParameterAsString("_application");

			if (!StringUtil.isBlank(formid)) {

				DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(
						DocumentProcess.class, applicationid);
				Document doc = (Document) docProcess.doView(docid);

				if (doc != null) {
					doc = DocumentHelper.rebuildDocument(doc, params, user);
					FlowStateRT instance = doc.getState();
					FlowDiagram fd = instance.getFlowVO().toFlowDiagram();

					boolean update = false;
					Node currNode = (Node) fd.getElementByID(params.getParameterAsString("_currid"));
					Node endNode = new Node(fd) {

						public void paintMobile(OGraphics g) {
						}

						public void showTips(Graphics g) {
						}

						public void paint(OGraphics g) {
						}
					};
					String label = "终止";
					endNode.name = label;
					endNode.id = "terminate_id";
					NodeRTProcess nodeRTProcess = (NodeRTProcess) ProcessFactory.createRuntimeProcess(
							NodeRTProcess.class, doc.getApplicationid());

					if (!instance.isComplete() && !instance.isTerminated()) {
						if (instance.getNoderts() != null && instance.getNoderts().size() > 0) {
							for (Iterator<?> iterator = instance.getNoderts().iterator(); iterator.hasNext();) {
								NodeRT nodert = (NodeRT) iterator.next();
								nodeRTProcess.doRemove(nodert.getId());
							}
							instance.getNoderts().clear();
							update = true;
						}
					}

					if (update) {
						Date actionTime = new Date();
						String attitude = params.getParameterAsString("_attitude");
						// 更新流程实例
						updateFlowStateRT(doc, instance, user, label, actionTime);
						// 更新流程历史
						StateMachine.updatePreviousRelationHIS(instance, currNode, user, actionTime);
						StateMachine.createOrUpdateRelationHIS(instance, currNode, endNode, user, actionTime, attitude,params.getParameterAsString("_signature"),
								FlowType.RUNNING2TERMIATE);
						// 更新文档
						updateDocument(doc, user, label, actionTime);
						// 更新图片
						StateMachine.updateImage(instance);
					}

					MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
					return MbDocumentXMLBuilder.toMobileXML(doc, user, params);
					// this.addActionMessage("{*[成功终止流程]*}");
				}
			}
		} catch (OBPMValidateException e) {
			e.printStackTrace();
			return "[ERROR]*" + e.getValidateMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "[ERROR]*" + e.getMessage();
		}
		return "";
	}

	/**
	 * 执行视图选择框确认脚本
	 */
	public static String ViewDialogConfirmRunScript(ParamsTable params, WebUser user) throws Exception {
		try {
			String applicationid = params.getParameterAsString("_application");
			String fieldName = params.getParameterAsString("_fieldName");

			Document doc = null;
			String formid = params.getParameterAsString("_formid");
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationid);
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			String docid = params.getParameterAsString("_docid");
			Document olddoc = (Document) proxy.doView(docid);

			if (olddoc == null) {
				olddoc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid, user);
			}

			if (olddoc != null) {
				doc = form.createDocument(olddoc, params, user);
			} else {
				doc = form.createDocument(params, user);
			}
			if (docid != null && docid.length() > 0) {
				doc.setId(docid);
			}

			FormField field = form.findFieldByName(fieldName);

			if (field instanceof ViewDialogField) {
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
				runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

				String script = ((ViewDialogField) field).getOkScript();

				script = StringUtil.dencodeHTML(script);

				if (!StringUtil.isBlank(script)) {
					Object result = runner.run(field.getScriptLable("OkScript"), script);
					if (result != null) {
						if (result instanceof org.mozilla.javascript.Undefined) {
							return "";
						} else if (result instanceof JsMessage) {
							JsMessage js = (JsMessage) result;
							return MbJsMessageXMLBuilder.toMobileXML(js);
						} else {
							return (String) result;
						}
					}
				}
			}
			return "SUCCESS";
		} catch (Exception e) {
			throw e;
		}
	}

	private static void updateFlowStateRT(Document doc, FlowStateRT instance, WebUser user, String stateLabel,
			Date actionTime) throws Exception {
		instance.setAuditdate(actionTime);
		instance.setAuditorList("");
		instance.setAuditorNames("");
		instance.setAudituser(user.getId());
		instance.setTerminated(true);
		instance.setLastFlowOperation(FlowType.RUNNING2TERMIATE);
		instance.setLastModified(actionTime);
		instance.setLastModifierId(user.getId());
		instance.setNoderts(new ArrayList<NodeRT>());
		instance.setStateLabel(stateLabel);
		StateMachine.getFlowStateRTProcess(doc.getApplicationid()).doUpdate(instance);
	}

	/**
	 * 流程挂起
	 * 
	 * @return
	 */
	public static String doFlowHandup(WebUser user, ParamsTable params) {
		try {
			String applicationid = params.getParameterAsString("_application");
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationid);
			Document doc = (Document) docProcess.doView(params.getParameterAsString("_docid"));
			Date actionTime = new Date();
			String nodertId = params.getParameterAsString("nodertId");
			NodeRTProcess nodeRTProcess = (NodeRTProcess) ProcessFactory.createRuntimeProcess(NodeRTProcess.class,
					applicationid);
			RelationHISProcess relationHISProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(
					RelationHISProcess.class, applicationid);
			NodeRT nodeRT = (NodeRT) nodeRTProcess.doView(nodertId);
			if (nodeRT != null) {
				nodeRT.setState(1);
				nodeRT.setLastProcessTime(new Date());
				nodeRT.setActorrts(nodeRT.getActorrts());
				nodeRTProcess.doUpdate(nodeRT);

				// 加入流程历史
				BillDefiVO flowVO = doc.getState().getFlowVO();
				// 获取当前结点
				Node currnode = null;
				FlowDiagram fd = null;
				if (flowVO != null) {
					fd = flowVO.toFlowDiagram();
				}
				String currnodeid = nodeRT.getNodeid();
				if (currnodeid != null) {
					currnode = (Node) fd.getElementByID(currnodeid);
				}
				FlowStateRT instance = doc.getState(params.getParameterAsString("_currid"));
				RelationHIS rhis = new RelationHIS();
				rhis.setId(Sequence.getSequence());
				rhis.setFlowStateId(instance.getId());
				rhis.setFlowid(instance.getFlowid());
				rhis.setFlowname(instance.getFlowVO().getSubject());
				rhis.setDocid(instance.getDocid());
				rhis.setStartnodeid(currnode.id);
				rhis.setStartnodename(currnode.name);
				rhis.setEndnodeid(currnode.id);
				rhis.setEndnodename(currnode.name);
				rhis.setIspassed(false);
				rhis.setActiontime(actionTime);
				rhis.setAttitude(params.getParameterAsString("_attitude"));
				rhis.setAuditor(user.getId());
				ActorHIS actorHIS = null;
				if (user.getEmployer() != null) {
					actorHIS = new ActorHIS((new WebUser(user.getEmployer())));
					actorHIS.setAgentid(user.getId());
					actorHIS.setAgentname(user.getName());
				} else {
					actorHIS = new ActorHIS(user);
				}
				actorHIS.setAttitude(params.getParameterAsString("_attitude"));
				actorHIS.setSignature(params.getParameterAsString("_signature"));
				actorHIS.setProcesstime(actionTime);
				rhis.getActorhiss().add(actorHIS);
				rhis.setFlowOperation(FlowType.RUNNING2RUNNING_HANDUP);
				rhis.setReminderCount(0);
				relationHISProcess.doCreate(rhis);

				// 如果是有审批时限设置,取消之前任务
				if (((ManualNode) currnode).isLimited && nodeRT.getDeadline() != null) {
					/*
					 * String key = instance.getDocid() + "_" +
					 * instance.getId()+ "_" + currnode.id; Timer timer =
					 * AutoAuditJobManager.runningTimerMap.get(key);
					 * timer.cancel();
					 * AutoAuditJobManager.runningTimerMap.remove(key);
					 */
					String token = TriggerVO.generateManualNodeTimingApprovalJobToken(doc.getId(), doc.getStateid(),
							nodeRT.getId());
					SchedulerFactory.getScheduler().cancelTrigger(token);
				}
				return MbDocumentXMLBuilder.toMobileXML(doc, user, params);
			}
		} catch (OBPMValidateException e) {
			e.printStackTrace();
			return "[ERROR*]" + "{*[cn.myapps.core.workflow.suspend.fail]*}" + e.getValidateMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "[ERROR]*" + "{*[cn.myapps.core.workflow.suspend.fail]*}";
		}
		return "";
	}
	
	/**
	 * 流程恢复
	 * @return
	 */
	public static String doFlowRecover(WebUser user,ParamsTable params){
		try {
			String applicationid = params.getParameterAsString("_application");
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationid);
			Document doc = (Document) docProcess.doView(params.getParameterAsString("_docid"));
			Date actionTime = new Date();
			String nodertId = params.getParameterAsString("nodertId");
			NodeRTProcess nodeRTProcess = (NodeRTProcess) ProcessFactory.createRuntimeProcess(NodeRTProcess.class, applicationid);
			RelationHISProcess relationHISProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class, applicationid);
			NodeRT nodeRT = (NodeRT) nodeRTProcess.doView(nodertId);
			if(nodeRT != null){
				BillDefiVO flowVO = doc.getState().getFlowVO();
				// 获取当前结点
				Node currnode = null;
				FlowDiagram fd = null;
				if (flowVO != null) {
					fd = flowVO.toFlowDiagram();
				}
				String currnodeid = nodeRT.getNodeid();
				if (currnodeid != null) {
					currnode = (Node) fd.getElementByID(currnodeid);
				}
				
				nodeRT.setState(0);
				Date lastProcessTime = new Date();
				long time = lastProcessTime.getTime() - nodeRT.getLastProcessTime().getTime();
				//更改审批时限
				if(((ManualNode)currnode).isLimited && nodeRT.getDeadline() !=null){
					if(nodeRT.getDeadline() != null){
						Date deadline = new Date(nodeRT.getDeadline().getTime() + time);
						nodeRT.setDeadline(deadline);
					}
				}
				//更改过期通知时间
				Collection<ActorRT> actorRTs = nodeRT.getActorrts();
				Map<String, Object> strategyMap = ((ManualNode)currnode).getNotificationStrategyMap();
				if(strategyMap.containsKey(NotificationConstant.STRTAGERY_OVERDUE)){
					for(Iterator<ActorRT> it = actorRTs.iterator(); it.hasNext();){
						ActorRT actorRT = it.next();
						if(actorRT.getDeadline() != null){
							Date deadline = new Date(actorRT.getDeadline().getTime() + time);
							actorRT.setDeadline(deadline);
						}
					}
				}
				nodeRT.setActorrts(actorRTs);
				nodeRT.setLastProcessTime(lastProcessTime);
				nodeRTProcess.doUpdate(nodeRT);
				
				//加入流程历史
				FlowStateRT instance = doc.getState(params.getParameterAsString("_currid"));
				RelationHIS rhis = new RelationHIS();
				rhis.setId(Sequence.getSequence());
				rhis.setFlowStateId(instance.getId());
				rhis.setFlowid(instance.getFlowid());
				rhis.setFlowname(instance.getFlowVO().getSubject());
				rhis.setDocid(instance.getDocid());
				rhis.setStartnodeid(currnode.id);
				rhis.setStartnodename(currnode.name);
				rhis.setEndnodeid(currnode.id);
				rhis.setEndnodename(currnode.name);
				rhis.setIspassed(false);
				rhis.setActiontime(actionTime);
				rhis.setAttitude(params.getParameterAsString("_attitude"));
				rhis.setAuditor(user.getId());
				ActorHIS actorHIS =null;
				if(user.getEmployer()!=null){
					actorHIS = new ActorHIS((new WebUser(user.getEmployer())));
					actorHIS.setAgentid(user.getId());
					actorHIS.setAgentname(user.getName());
				}else{
					actorHIS = new ActorHIS(user);
				}
				actorHIS.setAttitude(params.getParameterAsString("_attitude"));
				actorHIS.setSignature(params.getParameterAsString("_signature"));
				actorHIS.setProcesstime(actionTime);
				rhis.getActorhiss().add(actorHIS);
				rhis.setFlowOperation(FlowType.RUNNING2RUNNING_RECOVER);
				rhis.setReminderCount(0);
				relationHISProcess.doCreate(rhis);
				
				//调整deadline后重新加入任务
				if(((ManualNode)currnode).isLimited && nodeRT.getDeadline() !=null){
					/*
					SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);

					WebUser admin = new WebUser(superUserProcess.getDefaultAdmin()); // 系统用户
					admin.setDomainid(instance.getDocument().getId());
					admin.setName("system");
					AutoAuditJobManager.addJob(new TimelimitAutoAuditJob(instance.getDocument(),instance, nodeRT,currnode.id, admin));// 添加Job
					AutoAuditJobManager.startJob(instance, currnode.id);*/
					Job job = new ManualNodeTimingApprovalJob(instance.getDocid(),instance.getId(),nodeRT.getId(),instance.getApplicationid(),null);
					TriggerVO trigger = new TriggerVO(job, nodeRT.getDeadline().getTime());
					SchedulerFactory.getScheduler().addTrigger(trigger);
				}
				return MbDocumentXMLBuilder.toMobileXML(doc, user, params);
			}
		} catch (OBPMValidateException e) {
			e.printStackTrace();
			return "[ERROR]*" + "{*[cn.myapps.core.workflow.recover.fail]*}";
		}catch (Exception e) {
			e.printStackTrace();
			return "[ERROR]*" + "{*[cn.myapps.core.workflow.recover.fail]*}";
		}
		return "";
	}

	private static void updateDocument(Document doc, WebUser user, String stateLabel, Date actionTime) throws Exception {
		DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				doc.getApplicationid());
		doc.setAuditdate(actionTime);
		doc.setAuditorList("");
		doc.setAuditorNames("");
		doc.setAudituser(user.getId());
		doc.setLastFlowOperation(FlowType.RUNNING2TERMIATE);
		doc.setLastmodified(actionTime);
		doc.setLastmodifier(user.getId());
		doc.setStateLabel(stateLabel);
		proxy.doCreateOrUpdate(doc, user);
	}
}
