package cn.myapps.mobile2.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;

public class MbDocumentXMLBuilder {

	public static String toMobileXML(Document doc, WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			if (doc != null) {
				String flowStr = new String();
				StateMachineHelper helper = new StateMachineHelper(doc);
				Form form = doc.getForm();
				if (form != null) {
					form.recalculateDocument(doc, params, user);
				}

				MbDocumentHelper.changeStatic(doc, user);

				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), doc.getApplicationid());
				runner.initBSFManager(doc, params, user, new java.util.ArrayList<ValidateMessage>());
				// add activity xml text.
				Collection<Activity> activityList = MbDocumentHelper.doGetActivity(form, params, doc, user);
				Collection<String> formfields = MbDocumentHelper.doGetFormField(doc, form, params, user);

				/************* FORMDATA START *************/
				sb.append("<").append(MobileConstant2.TAG_FORMDATA);
				if (StringUtil.isBlank(form.getDiscription())) {
					sb.append(" ").append(MobileConstant2.ATT_TITLE).append("='").append(HtmlEncoder.encode(form.getName())).append("'");
				} else {
					sb.append(" ").append(MobileConstant2.ATT_TITLE).append("='").append(HtmlEncoder.encode(form.getDiscription()))
							.append("'");
				}
				sb.append(" ").append(MobileConstant2.ATT_FORMID).append("='").append(doc.getFormid()).append("'");
				sb.append(" ").append(MobileConstant2.ATT_DOCID).append("='").append(doc.getId()).append("'");
				sb.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='").append(doc.getApplicationid())
						.append("'");
				
				String flowid = "";
				if (doc.getState() != null && StringUtil.isBlank(doc.getState().getFlowid())) {
					flowid = doc.getState().getFlowid();
				} else if (!StringUtil.isBlank(form.getOnActionFlow())) {
					flowid = form.getOnActionFlow();
				}
				if(!StringUtil.isBlank(flowid)){
					sb.append(" ").append("FLOWID").append("='").append(flowid).append("'");
				}
				
				sb.append(">");
				
				/************* ACTION START *************/
				for (Iterator<Activity> it = activityList.iterator(); it.hasNext();) {
					try {

						StringBuffer activitySB = new StringBuffer();

						Activity act = it.next();
						if (act.isHidden(runner, form, doc, user, ResVO.FORM_TYPE)) {
							continue;
						}

						if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
							sb.append(MbDocumentHelper.refreshFlowXML(doc, user, params, "init", act.getId()));
							continue;
						}

						activitySB.append("<").append(MobileConstant2.TAG_ACTION);
						activitySB.append(" ").append(MobileConstant2.ATT_TYPE).append("='").append(act.getType())
								.append("'");
						activitySB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(act.getName())
								.append("'");
						if (act.getReadonlyScript() != null && act.getReadonlyScript().trim().length() > 0) {
							IRunner r = JavaScriptFactory.getInstance(params.getSessionid(), form.getApplicationid());
							r.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
							activitySB.append(" ").append(MobileConstant2.ATT_READONLY).append("='")
									.append(act.isReadonly(runner, form.getFullName())).append("'");
						}
						activitySB.append(" ").append(MobileConstant2.ATT_ACTIONID).append("='").append(act.getId())
								.append("'>");
						if (act.getType() == ActivityType.SAVE_SARTWORKFLOW) {
							if (doc.getState() != null && StringUtil.isBlank(doc.getState().getFlowid())) {
								flowid = doc.getState().getFlowid();
							} else if (!StringUtil.isBlank(form.getOnActionFlow())) {
								flowid = form.getOnActionFlow();
							}
							if (!StringUtil.isBlank(flowid)) {
								activitySB.append("<").append(MobileConstant2.TAG_PARAMS);
								activitySB.append(" ").append(MobileConstant2.ATT_KEY).append("='").append("_flowid")
										.append("'");
								activitySB.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(flowid)
										.append("'>");
								activitySB.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
							}
						} else if (act.getType() == ActivityType.SAVE_NEW) { // 保存并新建
							// 是否带旧数据
							if (act.isWithOld()) {
								activitySB.append("<").append(MobileConstant2.TAG_PARAMS);
								activitySB.append(" ").append(MobileConstant2.ATT_KEY).append("='")
										.append("_isWithOld").append("'");
								activitySB.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(true)
										.append("'>");
								activitySB.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
							}
						} else if (act.getType() == ActivityType.WORKFLOW_PROCESS) {
							HttpServletRequest request = ServletActionContext.getRequest();
							flowStr = helper.toFlowXMLText2(doc, user, null, request);
						} else if (act.getType() == ActivityType.FILE_DOWNLOAD) {
							if (!StringUtil.isBlank(act.getFileNameScript())) {
								// 处理文件下载按钮
								if (act.getType() == ActivityType.FILE_DOWNLOAD
										&& !StringUtil.isBlank(act.getFileNameScript())) {

									String script = StringUtil.dencodeHTML(act.getFileNameScript());

									StringBuffer label = new StringBuffer();
									label.append("Activity(").append(act.getId()).append(")." + act.getName())
											.append("fileNameScript");
									String fileUrl = (String) runner.run(label.toString(), script);
									activitySB.append("<").append(MobileConstant2.TAG_PARAMS);
									activitySB.append(" ").append(MobileConstant2.ATT_KEY).append("='")
											.append("_fileUrl").append("'");
									activitySB.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
											.append(fileUrl).append("'>");
									activitySB.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
								}
							}
						}
						activitySB.append("</").append(MobileConstant2.TAG_ACTION).append(">");
						sb.append(activitySB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				/************* ACTION END *************/
				/************* FORMFIELD START *************/
				for (Iterator<String> fieldit = formfields.iterator(); fieldit.hasNext();) {
					try {
						String value = fieldit.next();
						sb.append(value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (!StringUtil.isBlank(flowStr)) {
					sb.append(flowStr);
				}
				/************* FORMFIELD END *************/
				
				HttpServletRequest request = ServletActionContext.getRequest();
				String _targetNode = (String) request.getAttribute("_targetNode");
				//设置审批节点
				if(!StringUtil.isBlank(doc.getStateid())){
					String defaultNodeId = null;
					if(!StringUtil.isBlank(_targetNode)){
						defaultNodeId = _targetNode;
					}
					NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user,defaultNodeId);
					if(nodert !=null) {
						_targetNode = nodert.getNodeid();
					}
				}
				if(!StringUtil.isBlank(_targetNode)){
					sb.append("<").append(MobileConstant2.TAG_PARAMS);
					sb.append(" ").append("KEY").append("='").append("_targetNode").append("'");
					sb.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(_targetNode)
							.append("'").append(">");
					sb.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
				}
				
				sb.append("</").append(MobileConstant2.TAG_FORMDATA).append(">");
				/************* FORMDATA END *************/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String toFlowXML(Document doc, WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<FLOW>");

			String _actionType = params.getParameterAsString("_actionType");
			String xml = MbDocumentHelper.refreshFlowXML(doc, user, params, _actionType, null);

			if (xml != null) {
				sb.append(xml);
			}
			sb.append("</FLOW>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

}
