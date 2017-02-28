package cn.myapps.mobile2.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.activity.action.ActivityUtil;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachineUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class MbStartWorkFlowXMLBuilder {
	public static String toMobileXML(Document doc,WebUser user,ParamsTable params) throws Exception{
		String formid = params.getParameterAsString("_formid");
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form form = (Form) formProcess.doView(formid);
		String docid = params.getParameterAsString("_docid");
		if(form!=null ){
			String module = form.getModule().getId();
			ActivityUtil actUtil = new ActivityUtil();
			StateMachineUtil stateUtil = new StateMachineUtil();
			Map<String, String> flowMap = actUtil.getFlowByModule(module);
			Iterator<Entry<String, String>> iter = flowMap.entrySet().iterator();
			StringBuffer flowSb = new StringBuffer();
			flowSb.append("<").append(MobileConstant2.TAG_SELECTFIELD);
			flowSb.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("selectFlow'").append(">");
			StringBuffer nodeSb = new StringBuffer();
			while (iter != null && iter.hasNext()) {
				Map.Entry<String, String> entry = iter.next();
				flowSb.append("<").append(MobileConstant2.TAG_OPTION)
						.append(" ").append(MobileConstant2.ATT_VALUE)
						.append("='").append(entry.getKey()).append("'>");
				flowSb.append(entry.getValue());
				flowSb.append("</").append(MobileConstant2.TAG_OPTION).append(">");
				if(!StringUtil.isBlank(entry.getKey())){
					LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
					Collection<Node> nodes = stateUtil.getFirstNodeList(docid,entry.getKey());
					if (nodes != null) {
						for (Iterator<Node> iters = nodes.iterator(); iters.hasNext();) {
							ManualNode startNode = (ManualNode) iters.next();
							map.put(startNode.id, startNode.name);
						}
					}
					nodeSb.append("<").append(MobileConstant2.TAG_RADIOFIELD).append(" ").append(MobileConstant2.ATT_NAME).append("='").append(entry.getKey()).append("'>");
					for (Iterator<Entry<String, String>> iter2 = map.entrySet().iterator(); iter2.hasNext();) {
						Map.Entry<String, String> entry2 = iter2.next();
						nodeSb.append("<").append(MobileConstant2.TAG_OPTION).append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(entry2.getKey()).append("'>");
						nodeSb.append(entry2.getValue());
						nodeSb.append("</").append(MobileConstant2.TAG_OPTION).append(">");
					}
					nodeSb.append("</").append(MobileConstant2.TAG_RADIOFIELD).append(">");
				}
			}
			flowSb.append("</"+MobileConstant2.TAG_SELECTFIELD).append(">");
			String result = flowSb.toString() + nodeSb.toString();
			return result;
		}
		return "";
	}
}
