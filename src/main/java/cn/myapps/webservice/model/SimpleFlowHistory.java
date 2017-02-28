
package cn.myapps.webservice.model;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.input.SAXBuilder;

import cn.myapps.constans.Environment;
import cn.myapps.webservice.model.SimpleActorHIS;
import cn.myapps.util.DateUtil;
import cn.myapps.util.StringUtil;
import cn.myapps.webservice.fault.FlowHistoryServiceFault;
/**
 * 
 * @author Administrator
 *
 */
public class SimpleFlowHistory implements Serializable{

	private static final long serialVersionUID = -2075155018105602096L;
	
	/**
	 * 流程历史集合
	 */
	private Collection<SimpleRelationHIS> hisList;

	public Collection<SimpleRelationHIS> getHisList() {
		return hisList;
	}

	public void setHisList(Collection<SimpleRelationHIS> hisList) {
		this.hisList = hisList;
	}
	
	/**
	 * 以图形形式显示流程历史
	 * 
	 * @param cellCount
	 *            每行显示的历史单元格数
	 * @return 流程历史Html
	 * @throws FlowHistoryServiceFault 
	 */
	public String toDiagramHtml(int cellCount) throws FlowHistoryServiceFault {
		try {

			StringBuffer historyHtml = new StringBuffer();
	
			int count = 1;
			int index = 0;
			int storeIndex = 0;
	
			Object[] hisArray = hisList.toArray();
	
			Collection<SimpleRelationHIS> tmp = new ArrayList<SimpleRelationHIS>();
	
			historyHtml.append("<table>");
			for (int i = 0; i < hisArray.length; i++) {
				SimpleRelationHIS relationhis = (SimpleRelationHIS) hisArray[i];
				if (i != 0) {
					// 上一条历史记录
					SimpleRelationHIS preRelationhis = (SimpleRelationHIS) hisArray[i - 1];
	
					if (relationhis.getActiontime().equals(
							preRelationhis.getActiontime())) {
						tmp.add(relationhis);
					} else {
						if (storeIndex + cellCount == index) {
							historyHtml.append("</tr>");
							storeIndex = index;
						}
						if (index % cellCount == 0) {
							historyHtml.append("<tr>");
						}
						historyHtml.append(toDiagramCellHtml(tmp, count));
						// 清空tmp
						tmp.clear();
						// 把当前历史加入tmp
						tmp.add(relationhis);
	
						count++;
						index++;
					}
				} else {
					tmp.add(relationhis);
				}
			}
			// 生成html
			if (tmp.size() > 0) {
				if (index % cellCount == 0) {
					historyHtml.append("</tr><tr>").append(
							toDiagramCellHtml(tmp, count)).append("</tr>");
				} else {
					historyHtml.append(toDiagramCellHtml(tmp, count)).append(
							"</tr>");
				}
			}
			historyHtml.append("</table>");
			return historyHtml.toString();
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new FlowHistoryServiceFault(e.getMessage());
		}
	}

	private String toDiagramCellHtml(Collection<SimpleRelationHIS> tmp, int cellCount) throws Exception {
		Object[] rhises = tmp.toArray();
		StringBuffer buffer = new StringBuffer();

		Date actionTime = ((SimpleRelationHIS) tmp.iterator().next()).getActiontime();
		String actiontimeStr = DateUtil.getDateTimeStr(actionTime);

		String contextPath = Environment.getInstance().getContextPath();
		String ctxpath = contextPath.equals("/") ? "" : contextPath;

		if (cellCount == 1) {
			buffer
					.append("<td style='font-size:7px;line-height:5px' colspan='2'><table   bordercolor='#cccccc' cellspacing='0' cellpadding='3' width='100%' align='left' bgcolor='#ffffff' border='1'>");
		} else {
			buffer
					.append("<td><table ><tr><td><img src='"
							+ ctxpath
							+ "/resource/image/nextStep.gif' width='16' height='16'></td></tr></table></td>");
			buffer
					.append("<td style='font-size:7px'><table  bordercolor='#cccccc' cellspacing='0' cellpadding='3' width='100%' align='left' bgcolor='#ffffff' border='1'>");
		}
		buffer
				.append("<tr bgcolor='#DDDDDD' ><td colspan='2' style='font-size:7px'>");
		buffer.append("<span style='font-weight: bold;font-size:11px'>Step-"
				+ cellCount + " </span>Time:" + actiontimeStr);
		buffer.append("</td></tr>");
		buffer.append("<tr >");
		buffer
				.append("<td style='font-size:9px;line-height:8pt;white-space:nowrap' valign='top'>");
		SimpleRelationHIS first = (SimpleRelationHIS) rhises[0];
		Collection<SimpleActorHIS> simpleActorHISList = first.getActorhiss();
		buffer
				.append("<span style='font-weight: bold;font-size:11px'>{*[From]*}:</span>");
		for (Iterator<SimpleActorHIS> iterator = simpleActorHISList.iterator(); iterator
				.hasNext();) {
			SimpleActorHIS simpleActorHIS = (SimpleActorHIS) iterator.next();
			String actorName = simpleActorHIS.getName();
			buffer.append(actorName + "");
		}
		buffer.append("</td>");
		buffer.append("<td style='font-size:7px;line-height:5px'>");
		buffer
				.append("<table cellspacing='0' style='font-size:7px;line-height:5px'>");
		buffer
				.append("<tr><td style='font-size:9px;line-height:8pt;white-space:nowrap'><span style='font-weight: bold;font-size:11px'>{*[To]*}:</span>");
		
		for (int i = 0; i < rhises.length; i++) {
			SimpleRelationHIS his = (SimpleRelationHIS) rhises[i];//当前历史
			SimpleRelationHIS nexthis = null;//下一条历史
			if(rhises.length - 1 > i)
				nexthis = (SimpleRelationHIS) rhises[i+1];
			if(nexthis != null){
				boolean isExist = false;
				for (Iterator<String> iterator = his.getUserIdList().iterator(); iterator
						.hasNext();) {
					String actorid = iterator.next();
					if(nexthis.getUserIdList().contains(actorid)){
						isExist = true;
						break;
					}
				}
				if(isExist){
					//某用户同一时间提交到分散节点，流程历史属于并列关系
					buffer.append("" + his.getEndnodename());
					buffer.append("</td></tr>");
					buffer.append("<tr><td style='font-size:9px;line-height:8pt;white-space:nowrap'><span style='font-weight: bold;font-size:11px'>{*[To]*}:</span>");
				}else{
					buffer.append("" + his.getEndnodename() + "<span style='font-weight: bold;font-size:11px'>{*[To]*}:</span>");
				}
			}else{
				buffer.append("" + his.getEndnodename());
			}
		}
		buffer.append("</td></tr>");
		buffer.append("</table>");
		buffer.append("</td>");
		buffer.append("</tr>");
		buffer
				.append("<tr ><td colspan='2' style='font-size:9px'><span style='font-weight: bold;font-size:11px'>{*[Remarks]*}:</span>"
						+ (first.getAttitude() == null ? "" : first
								.getAttitude()) + "</td></tr>");
		buffer.append("</table></td>");

		return buffer.toString();
	}

	/**
	 * 以文本形式显示流程历史
	 * 
	 * @return 流程历史Html
	 */
	public String toTextHtml() {
		StringBuffer htmlBuilder = new StringBuffer();
		String contextPath = Environment.getInstance().getContextPath();
		String ctxpath = contextPath.equals("/") ? "" : contextPath;
		htmlBuilder
				.append("<div name='_history' class='flow-history' style='width:100%;height:95%;border:1px solid #b5b8c8;' readonly='true'>");
		htmlBuilder
				.append("<table style='width:98%;table-layout:fixed; word-break:break-all;'><tr>");
		htmlBuilder
				.append("<td width='15%'>{*[Auditor]*}</td><td width='20%'>{*[cn.myapps.core.workflow.audit_time]*}</td><td width='30%'>{*[Remark]*}</td><td width='15%'>{*[Flow]*}{*[State]*}</td><td width='20%'>{*[time.consuming]*}</td>");
		htmlBuilder.append("</tr>");
		
		SimpleRelationHIS tempRelHis =null;
		SimpleActorHIS tempSimpleActorHIS = null;
		for (Iterator<SimpleRelationHIS> iter = hisList.iterator(); iter.hasNext();) {

			SimpleRelationHIS relHis = (SimpleRelationHIS) iter.next();

			// htmlBuilder.append("<td>");
			
			if (relHis.getActorhiss().size() > 0) {
				for (Iterator<SimpleActorHIS> iterator = relHis.getActorhiss()
						.iterator(); iterator.hasNext();) {
					htmlBuilder.append("<tr>");
					SimpleActorHIS simpleActorHIS = (SimpleActorHIS) iterator.next();
					// 流程处理人
					htmlBuilder.append("<td>");
					if(simpleActorHIS.getAgentname()!=null && simpleActorHIS.getAgentname().trim().length()>0){
						htmlBuilder.append(simpleActorHIS.getAgentname()+"("+simpleActorHIS.getName()+")");
					}else{
						htmlBuilder.append(simpleActorHIS.getName());
					}
					htmlBuilder.append("</td>");
					// 审批时间
					htmlBuilder.append("<td>");
					String pocesstime = "";
					if (simpleActorHIS.getProcesstime() != null) {
						pocesstime = DateUtil.getDateTimeStr(simpleActorHIS
								.getProcesstime());
					} else {
						pocesstime = DateUtil.getDateTimeStr(relHis
								.getActiontime());
					}
					htmlBuilder.append(pocesstime);
					htmlBuilder.append("</td>");
					// 审批意见
					htmlBuilder.append("<td>");
					String attitude = "";
					if (simpleActorHIS.getAttitude() != null
							&& simpleActorHIS.getAttitude().length() > 0) {
						attitude = simpleActorHIS.getAttitude();
					}
					htmlBuilder.append(attitude);
					htmlBuilder.append("</td>");
					// 流程标签
					htmlBuilder.append("<td>");
					htmlBuilder.append(StringUtil.dencodeHTML(relHis.getStartnodename()) + "<img src='"+ctxpath+"/portal/share/icon/16x16_0060/arrow_right.png' />" + StringUtil.dencodeHTML(relHis.getEndnodename()));
					htmlBuilder.append("</td>");

					//耗时 
					htmlBuilder.append("<td>");
					long consumeTime = 0;
					String consumeTimeStr = "";
					if (simpleActorHIS.getProcesstime() != null && tempSimpleActorHIS != null) {
						try {
							consumeTime = DateUtil.getDiffDateTime(tempSimpleActorHIS.getProcesstime(),simpleActorHIS.getProcesstime());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			       long day=consumeTime/(24*60*60*1000);
			       long hour=(consumeTime/(60*60*1000)-day*24);
			       long min=((consumeTime/(60*1000))-day*24*60-hour*60);
			       long s=(consumeTime/1000-day*24*60*60-hour*60*60-min*60);
			       consumeTimeStr = ""+day+"{*[Days]*}"+hour+"{*[Hours]*}"+min+"{*[Minutes]*}"+s+"{*[Seconds]*}";
					htmlBuilder.append(consumeTimeStr);
					htmlBuilder.append("</td>");
					
					htmlBuilder.append("</tr>");
					
					tempSimpleActorHIS = simpleActorHIS;
				}
			} else {
				htmlBuilder.append("<tr>");
				// 流程处理人
				htmlBuilder.append("<td>");
				htmlBuilder.append(relHis.getAuditor());
				htmlBuilder.append("</td>");
				// 审批时间
				htmlBuilder.append("<td>");
				String actiontimeStr = DateUtil.getDateTimeStr(relHis
						.getActiontime());
				htmlBuilder.append(actiontimeStr);
				htmlBuilder.append("</td>");
				// 审批意见
				htmlBuilder.append("<td>");
				htmlBuilder.append(relHis.getAttitude());
				htmlBuilder.append("</td>");
				// 流程标签
				htmlBuilder.append("<td>");
				htmlBuilder.append(StringUtil.dencodeHTML(relHis.getStartnodename()) + "<img src='"+ctxpath+"/portal/share/icon/16x16_0060/arrow_right.png' />" + StringUtil.dencodeHTML(relHis.getEndnodename()));
				htmlBuilder.append("</td>");

				//耗时 
				htmlBuilder.append("<td>");
				long consumeTime = 0;
				String consumeTimeStr = "";
				if (relHis.getActiontime() != null && tempRelHis != null) {
					try {
						consumeTime = DateUtil.getDiffDateTime(tempRelHis.getActiontime(),relHis.getActiontime());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		       long day=consumeTime/(24*60*60*1000);
		       long hour=(consumeTime/(60*60*1000)-day*24);
		       long min=((consumeTime/(60*1000))-day*24*60-hour*60);
		       long s=(consumeTime/1000-day*24*60*60-hour*60*60-min*60);
		       consumeTimeStr = ""+day+"{*[Days]*}"+hour+"{*[Hours]*}"+min+"{*[Minutes]*}"+s+"{*[Seconds]*}";
				htmlBuilder.append(consumeTimeStr);
				htmlBuilder.append("</td>");
				
				htmlBuilder.append("</tr>");

			}
			tempRelHis = relHis;
		}
		htmlBuilder.append("</table>");
		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}

	/**
	 * 检查生成xml中，setText是否为null
	 * 
	 * @param object
	 * @return
	 */
	private String checkNull(Object object){
		if(object == null){
			return "";
		}
		
		return String.valueOf(object);
	}
	
	/**
	 * 以XML文本形式显示流程历史
	 * 
	 * @return 显示流程历史
	 */
	public String toTextXml() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("Flow-History");
		
		StringBuffer xmlBuilder = new StringBuffer();
		xmlBuilder.append("<").append("Flow-History").append(">");
			for (Iterator<SimpleRelationHIS> iterator = hisList.iterator(); iterator.hasNext();) {
				SimpleRelationHIS his = (SimpleRelationHIS) iterator.next();
				Element relation = root.addElement("relation-history");
				
				Element id = relation.addElement("id");
				id.setText(his.getId());
				
				Element startnodeid = relation.addElement("startnodeid");
				startnodeid.setText(checkNull(his.getStartnodeid()));
				
				Element startnodename = relation.addElement("startnodename");
				startnodename.setText(checkNull(his.getStartnodename()));
				
				Element endnodeid = relation.addElement("endnodeid");
				endnodeid.setText(checkNull(his.getEndnodeid()));
				
				Element endnodename = relation.addElement("endnodename");
				endnodename.setText(checkNull(his.getEndnodename()));
				
				Element flowid = relation.addElement("flowid");
				flowid.setText(checkNull(his.getFlowid()));
				
				Element flowname = relation.addElement("flowname");
				flowname.setText(checkNull(his.getFlowname()));
				
				Element flowOperation = relation.addElement("flowOperation");
				flowOperation.setText(checkNull(his.getFlowOperation()));
				
				Element flowStateId = relation.addElement("flowStateId");
				flowStateId.setText(checkNull(his.getFlowStateId()));
				
				Element docid = relation.addElement("docid");
				docid.setText(checkNull(his.getDocid()));
				
				Element ispassed = relation.addElement("ispassed");
				ispassed.setText(checkNull(his.getIspassed()));
				
				Element auditor = relation.addElement("auditor");
				auditor.setText(checkNull(his.getAuditor()));
				
				Element actiontime = relation.addElement("actiontime");
				actiontime.setText(checkNull(his.getActiontime()));
				
				Element processtime = relation.addElement("processtime");
				processtime.setText(checkNull(his.getProcesstime()));
				
				Element attitude = relation.addElement("attitude");
				attitude.setText(checkNull(his.getAttitude()));
				
				Element ReminderCount = relation.addElement("ReminderCount");
				ReminderCount.setText(checkNull(his.getReminderCount()));
				
				Collection<SimpleActorHIS> simpleActorHISs = his.getActorhiss();
				for (Iterator<SimpleActorHIS> iterator2 = simpleActorHISs.iterator(); iterator2
						.hasNext();) {
					SimpleActorHIS simpleActorHIS = (SimpleActorHIS) iterator2.next();
					Element actor = relation.addElement("actor-history");
					
					Element actorid = actor.addElement("actorid");
					actorid.setText(checkNull(simpleActorHIS.getActorid()));
					
					Element agentid = actor.addElement("agentid");
					agentid.setText(checkNull(simpleActorHIS.getAgentid()));
					
					Element agentname = actor.addElement("agentname");
					agentname.setText(checkNull(simpleActorHIS.getAgentname()));
					
					Element name = actor.addElement("name");
					name.setText(checkNull(simpleActorHIS.getName()));
					
					Element type = actor.addElement("type");
					type.setText(checkNull((simpleActorHIS.getType())));
					
					Element processtime2 = actor.addElement("processtime");
					processtime2.setText(checkNull(simpleActorHIS.getProcesstime()));
					
					Element attitude2 = actor.addElement("attitude");
					attitude2.setText(checkNull(simpleActorHIS.getAttitude()));
				}
				
				Element version = relation.addElement("version");
				version.setText(checkNull((his.getVersion())));
			}
		return doc.asXML();
	}
	
	/**
	 * 以JSON文本形式显示流程历史
	 * 
	 * @return 显示流程历史
	 */
	public String toTextJson() throws FlowHistoryServiceFault{
		SAXBuilder builder = new SAXBuilder();
		StringBuffer sb = new StringBuffer();
		try {
			org.jdom.Document doc = builder.build(new StringReader(toTextXml()));
			org.jdom.Element root = doc.getRootElement();
			sb.append("[{");
			listNode(root,sb);
			sb.append("}]");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FlowHistoryServiceFault(e.getMessage());
		}
		String json = sb.toString();
		json = json.replaceAll("},}", "}}");
		json = json.replaceAll("\",}", "\"}");
		json = json.replaceAll("},}", "}}");
		return json;
	}
	
	/**
	 * 递归json
	 * 
	 * @param e
	 * @param sb
	 * @return
	 */
	private String listNode(org.jdom.Element e,StringBuffer sb){
		sb.append("\"" + e.getName() + "\":");
		if(e.getChildren().size()==0){
			sb.append("\"" + e.getTextTrim() + "\"");
		}else{
			sb.append("{");
			for(int i = 0;i < e.getChildren().size();i++){
				listNode((org.jdom.Element) e.getChildren().get(i),sb);
			}
			sb.append("}");
		}
		sb.append(",");
		return sb.toString();
	}
}
