package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.StartNode;
import cn.myapps.core.workflow.element.SubFlow;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistoryVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 流程历史控件
 * <p>
 * 用于满足在表单界面上展示流程审批历史的业务需求
 * </p>
 * 
 * @author Happy
 * 
 */
public class FlowHistoryField extends FormField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5627631912671831820L;

	public static final String SHOW_MODE_TEXT = "text";
	public static final String SHOW_MODE_DIAGRAM = "diagram";
	public static final String SHOW_MODE_TEXT_AND_DIAGRAM = "textAndDiagram";

	/**
	 * 展示方式
	 */
	private String showMode = SHOW_MODE_TEXT;

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	@Override
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		if (displayType == PermissionType.HIDDEN)
			return getHiddenValue();
		return buildFlowHistoryHtml(doc);
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		int displayType = getPrintDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN)
			return getPrintHiddenValue();

		return buildFlowHistoryHtml(doc);
	}

	/**
	 * 生成流程历史HTML字符串
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	private String buildFlowHistoryHtml(Document doc) throws Exception {
		StringBuilder html = new StringBuilder();
		if (doc != null && !StringUtil.isBlank(doc.getStateid())) {

			Collection<FlowStateRT> subFlowInstances = doc.getState()
					.getSubStates();

			html.append("<div moduleType='flowHistoryField' showMode='")
					.append(showMode).append("' mobile='").append(mobile)
					.append("' _name='").append(name)
					.append("' _discript ='").append(discript)
					.append("' flowDiagram='").append(
							Environment.getInstance().getContextPath()
									+ "/uploads/billflow/").append(
							doc.getStateid()).append(".jpg?tempid=").append(
							Math.random());

			html.append(toOtherpropsHtml()).append("> <table >");
			// 表头
			html.append("<tr>").append("<td width='15%'>  {*[core.dynaform.form.formfield.flowhistoryfiled.list.title.node_name]*}</td>")
					.append("<td width='10%'>{*[core.dynaform.form.formfield.flowhistoryfiled.list.title.auditor]*}</td>").append(
							"<td width='20%'>{*[core.dynaform.form.formfield.flowhistoryfiled.list.title.flow_operation]*}</td>").append(
							"<td width='35%'>{*[core.dynaform.form.formfield.flowhistoryfiled.list.title.attitude]*}</td>").append(
							"<td width='20%'>{*[core.dynaform.form.formfield.flowhistoryfiled.list.title.process_time]*}</td>").append("</tr>");
			appendFlowHistoryHtml(html, doc.getState(), subFlowInstances,null);
			html.append("</table></div>");
		}

		return html.toString();
	}

	/**
	 * 拼接一个流程实例的流程历史HTML字符串
	 * @param html
	 * @param flowInstance
	 * @param subFlowInstances
	 * @param parentNodeName
	 * @throws Exception
	 */
	private void appendFlowHistoryHtml(StringBuilder html,
			FlowStateRT flowInstance,Collection<FlowStateRT> subFlowInstances,String parentNodeName) throws Exception {
		RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory
				.createRuntimeProcess(RelationHISProcess.class, flowInstance
						.getApplicationid());
		
		Collection<FlowHistoryVO> historys = hisProcess.getFlowHistorysByFolowStateId(flowInstance.getId());
		
		List<String> subNodIds = new ArrayList<String>();//已添加子流程历史信息的节点id
		
		FlowDiagram flowDiagram = flowInstance.getFlowVO().toFlowDiagram();
		for (Iterator<FlowHistoryVO> iter = historys.iterator(); iter.hasNext();) {
				FlowHistoryVO history = iter.next();
				Node node = flowDiagram.getNodeByID(history.getTargetNodeId());
				String nodeName = parentNodeName==null? history.getStartNodeName() : parentNodeName+"\\"+history.getStartNodeName();
					html.append("<tr>");

					// 审批节点
					html.append("<td>");
					html.append(nodeName);
					html.append("</td>");

					// 处理人
					html.append("<td>");
					if (history.getAgentAuditorName() != null
							&& history.getAgentAuditorName().trim().length() > 0) {
						html.append(history.getAgentAuditorName() + "("
								+ history.getAuditorName() + ")");
					} else {
						html.append(history.getAuditorName());
					}
					html.append("</td>");

					// 签核状态
					html.append("<td>");
					String action = "";
					if (history.getFlowOperation() != null
							&& history.getFlowOperation().length() > 0) {
						if(node instanceof StartNode){
							action = "{*[flow.start]*}";
						}else{
							action = this.toActionName(history.getFlowOperation());
						}
					}
					html.append(action);
					html.append("</td>");

					// 签核意见
					html.append("<td class='attitude'>");
					String attitude = "";
					if (history.getAttitude() != null
							&& history.getAttitude().length() > 0) {
						attitude = history.getAttitude();
					}
					html.append(attitude);
					if(!StringUtil.isBlank(history.getSignature())){
						html.append("<span data-datas='").append(history.getSignature()).append("' ></span>");
					}
					html.append("</td>");

					// 办理时间
					html.append("<td>");
					String pocesstime = "";
					if (history.getProcesstime() != null) {
						pocesstime = DateUtil.getDateTimeStr(history
								.getProcesstime());
					}
					html.append(pocesstime);
					html.append("</td>");

					html.append("</tr>");

				if(node instanceof SubFlow){
					if(subNodIds.contains(history.getStartNodeId())) return;//已经显示流程审批历史的节点,无需重复显示子流程历史
					int index = 1;//子流程流程实例序号
					for(FlowStateRT subFlowInstance : subFlowInstances){
						if(node.id.equals(subFlowInstance.getSubFlowNodeId())){
							nodeName = parentNodeName==null? history.getTargetNodeName() : parentNodeName+"\\"+history.getTargetNodeName();
							appendFlowHistoryHtml(html, subFlowInstance, subFlowInstances,nodeName);//添加子流程历史信息
							index++;
						}
					}
					subNodIds.add(node.id);
				}
			

		}
	}

	private String toActionName(String code) {
		if (FlowType.RUNNING2RUNNING_BACK.equals(code)) {
			return "{*[cn.myapps.core.workflow.reject]*}";
		} else if (FlowType.RUNNING2RUNNING_RETRACEMENT.equals(code)) {
			return "{*[Retracement]*}";
		} else if (FlowType.RUNNING2RUNNING_HANDUP.equals(code)) {
			return "{*[cn.myapps.core.workflow.suspend]*}";
		} else if (FlowType.RUNNING2RUNNING_RECOVER.equals(code)) {
			return "{*[cn.myapps.core.workflow.recover]*}";
		} else {
			return "{*[cn.myapps.core.workflow.doFlow]*}";
		}
	}

}
