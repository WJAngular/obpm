package cn.myapps.core.dynaform.form.ejb;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowReminderHistory;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowReminderHistoryProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 流程催办历史控件
 * 
 * @author Happy
 * 
 */
public class FlowReminderHistoryField extends FormField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5685982704967041195L;

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
		return buildFlowReminderHistoryHtml(doc);
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		int displayType = getPrintDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN)
			return getPrintHiddenValue();

		return buildFlowReminderHistoryHtml(doc);
	}

	/**
	 * 生成流程催办历史HTML字符串
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	private String buildFlowReminderHistoryHtml(Document doc) throws Exception {
		StringBuilder html = new StringBuilder();
		if (doc != null && !StringUtil.isBlank(doc.getStateid())) {

			html.append("<div moduleType='flowReminderHistoryField' id='").append(id).append("' ");

			html.append(toOtherpropsHtml()).append("> <table >");
			// 表头
			html.append("<tr>").append("<td width='15%'>  {*[序号]*}</td>")
					.append("<td width='10%'>{*[节点名称]*}</td>").append(
							"<td width='20%'>{*[催单人]*}</td>").append(
							"<td width='20%'>{*[催单时间]*}</td>").append(
							"<td width='35%'>{*[内容]*}</td>").append("</tr>");
			appendFlowReminderHistoryHtml(html, doc);
			html.append("</table></div>");
		}

		return html.toString();
	}

	private void appendFlowReminderHistoryHtml(StringBuilder html,Document doc) throws Exception {
		FlowReminderHistoryProcess process = (FlowReminderHistoryProcess)ProcessFactory.createRuntimeProcess(FlowReminderHistoryProcess.class, doc.getApplicationid());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Collection<FlowReminderHistory> list = process.queryByDocument(doc.getId());
		int index = 1;
		for (Iterator<FlowReminderHistory> iterator = list.iterator(); iterator.hasNext();) {
			FlowReminderHistory flowReminderHistory = iterator.next();
			html.append("<tr>");
			html.append("<td>").append(String.valueOf(index)).append("</td>");
			html.append("<td>").append(flowReminderHistory.getNodeName()).append("</td>");
			html.append("<td>").append(flowReminderHistory.getUserName()).append("</td>");
			html.append("<td>").append(format.format(flowReminderHistory.getProcessTime())).append("</td>");
			html.append("<td>").append(flowReminderHistory.getContent()).append("</td>");
			html.append("</tr>");
			index++;
		}

	}

}
