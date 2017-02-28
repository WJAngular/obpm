package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.util.DateUtil;
import cn.myapps.util.StringUtil;

public class FlowHistory {
	Collection<RelationHIS> hisList = new ArrayList<RelationHIS>();

	private final static int DEFAULT_CELL_COUNT = 4;

	public void addHis(RelationHIS his) {
		hisList.add(his);
	}

	public void addAllHis(Collection<RelationHIS> hisList) {
		this.hisList.addAll(hisList);
	}

	/**
	 * 以图形形式显示流程历史
	 * 
	 * @return 流程历史Html
	 */
	public String toDiagramHtml() {
		return toDiagramHtml(DEFAULT_CELL_COUNT);
	}

	/**
	 * 以图形形式显示流程历史
	 * 
	 * @param cellCount
	 *            每行显示的历史单元格数
	 * @return 流程历史Html
	 */
	public String toDiagramHtml(int cellCount) {
		StringBuffer historyHtml = new StringBuffer();

		int count = 1;
		int index = 0;
		int storeIndex = 0;

		Object[] hisArray = hisList.toArray();

		Collection<RelationHIS> tmp = new ArrayList<RelationHIS>();

		historyHtml.append("<table>");
		for (int i = 0; i < hisArray.length; i++) {
			RelationHIS relationhis = (RelationHIS) hisArray[i];
			if (i != 0) {
				// 上一条历史记录
				RelationHIS preRelationhis = (RelationHIS) hisArray[i - 1];

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
	}

	private String toDiagramCellHtml(Collection<RelationHIS> tmp, int cellCount) {
		Object[] rhises = tmp.toArray();
		StringBuffer buffer = new StringBuffer();

		Date actionTime = ((RelationHIS) tmp.iterator().next()).getActiontime();
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
		RelationHIS first = (RelationHIS) rhises[0];
		Collection<ActorHIS> actorhisList = first.getActorhiss();
		buffer
				.append("<span style='font-weight: bold;font-size:11px'>{*[From]*}:</span>");
		for (Iterator<ActorHIS> iterator = actorhisList.iterator(); iterator
				.hasNext();) {
			ActorHIS actorhis = (ActorHIS) iterator.next();
			String actorName = actorhis.getName();
			buffer.append(actorName + "");
		}
		buffer.append("</td>");
		buffer.append("<td style='font-size:7px;line-height:5px'>");
		buffer
				.append("<table cellspacing='0' style='font-size:7px;line-height:5px'>");
		buffer
				.append("<tr><td style='font-size:9px;line-height:8pt;white-space:nowrap'><span style='font-weight: bold;font-size:11px'>{*[To]*}:</span>");
		buffer.append("");
		for (int i = 0; i < rhises.length; i++) {
			RelationHIS tmphis = (RelationHIS) rhises[i];
			buffer.append("" + tmphis.getEndnodename() + "</td></tr>");
		}
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
				.append("<div name='_history' class='flow-history' style='height:95%;border:1px solid #b5b8c8;' readonly='true'>");
		htmlBuilder
				.append("<table style='width:98%;table-layout:fixed; word-break:break-all;'><tr>");
		htmlBuilder
				.append("<td width='10%'>  {*[Auditor]*}</td><td width='20%'>{*[cn.myapps.core.workflow.audit_time]*}</td><td width='30%'>{*[Remark]*}</td><td width='8%'>{*[cn.myapps.core.workflow.action]*}</td><td width='12%'>{*[cn.myapps.core.workflow.flow_state]*}</td><td width='20%'>{*[time.consuming]*}</td>");
		htmlBuilder.append("</tr>");
		
		RelationHIS tempRelHis =null;
		ActorHIS tempActorHIS = null;
		String startNodeid = "";
		Map<String, Object> timeConsMap = new HashMap<String, Object>();
		for (Iterator<RelationHIS> iter = hisList.iterator(); iter.hasNext();) {

			RelationHIS relHis = (RelationHIS) iter.next();

			Node node = relHis.getStartNode();
			
			if (relHis.getActorhiss().size() > 0) {
				for (Iterator<ActorHIS> iterator = relHis.getActorhiss()
						.iterator(); iterator.hasNext();) {
					htmlBuilder.append("<tr>");
					ActorHIS actorHis = (ActorHIS) iterator.next();
					// 流程处理人
					htmlBuilder.append("<td>");
					if(actorHis.getAgentname()!=null && actorHis.getAgentname().trim().length()>0){
						htmlBuilder.append(actorHis.getAgentname()+"("+actorHis.getName()+")");
					}else{
						htmlBuilder.append(actorHis.getName());
					}
					htmlBuilder.append("</td>");
					// 审批时间
					htmlBuilder.append("<td>");
					String pocesstime = "";
					if (actorHis.getProcesstime() != null) {
						pocesstime = DateUtil.getDateTimeStr(actorHis
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
					if (actorHis.getAttitude() != null
							&& actorHis.getAttitude().length() > 0) {
						attitude = actorHis.getAttitude();
					}
					htmlBuilder.append(attitude);
					htmlBuilder.append("</td>");
					// 动作
					htmlBuilder.append("<td>");
					String action = "";
					if (relHis.getFlowOperation() != null
							&& relHis.getFlowOperation().length() > 0) {
						action = this.toActionName(relHis.getFlowOperation());
					}
					htmlBuilder.append(action);
					htmlBuilder.append("</td>");
					// 流程标签
					htmlBuilder.append("<td>");
					htmlBuilder.append(StringUtil.dencodeHTML(relHis.getStartnodename()) + "<img src='"+ctxpath+"/portal/share/icon/16x16_0060/"+this.toPng(relHis.getFlowOperation())+"' />" + StringUtil.dencodeHTML(relHis.getEndnodename()));
					htmlBuilder.append("</td>");

					//耗时 
					htmlBuilder.append("<td>");
					long consumeTime = 0;
					String consumeTimeStr = "";
					if (actorHis.getProcesstime() != null && tempActorHIS != null && tempRelHis != null) {
						try {
							if(node instanceof ManualNode 
									&& Integer.parseInt(((ManualNode)node).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND){
								//有顺序的会签,流程历史耗时从ActorHIS获取
								if(startNodeid.equals(relHis.getStartnodeid()) && !timeConsMap.isEmpty()){
									consumeTime = DateUtil.getDiffDateTime((Date)timeConsMap.get(relHis.getStartnodeid()),actorHis.getProcesstime());
								}else{
									consumeTime = DateUtil.getDiffDateTime(tempActorHIS.getProcesstime(),actorHis.getProcesstime());
								}
							}else{
								//非会签或无顺序的会签,流程历史耗时获取逻辑
								consumeTime = DateUtil.getDiffDateTime(tempRelHis.getActiontime(),actorHis.getProcesstime());
							}
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
					
					tempActorHIS = actorHis;
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
				// 动作
				htmlBuilder.append("<td>");
				String action = "";
				if (relHis.getFlowOperation() != null
						&& relHis.getFlowOperation().length() > 0) {
					action = this.toActionName(relHis.getFlowOperation());
				}
				htmlBuilder.append(action);
				htmlBuilder.append("</td>");
				// 流程标签
				htmlBuilder.append("<td>");
				htmlBuilder.append(StringUtil.dencodeHTML(relHis.getStartnodename()) + "<img src='"+ctxpath+"/portal/share/icon/16x16_0060/"+this.toPng(relHis.getFlowOperation())+"' />" + StringUtil.dencodeHTML(relHis.getEndnodename()));
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
			
			startNodeid = relHis.getStartnodeid() != null ?relHis.getStartnodeid():"";
			timeConsMap.put(relHis.getEndnodeid(), tempActorHIS.getProcesstime());
		}
		htmlBuilder.append("</table>");
		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}
	
	/**
	 * 以文本形式显示流程历史
	 * 
	 * @return 流程历史Html
	 */
	public String toTextHtml4Archive(WebUser user, ParamsTable params) {
		StringBuffer htmlBuilder = new StringBuffer();
		String contextPath = Environment.getInstance().getContextPath();
		String ctxpath = contextPath.equals("/") ? "" : contextPath;
		htmlBuilder
				.append("<div name='_history' class='flow-history' style='height:95%;border:1px solid #b5b8c8;' readonly='true'>");
		htmlBuilder
				.append("<table style='width:98%;table-layout:fixed; word-break:break-all;'><tr>");
		htmlBuilder
				.append("<td width='10%'>  审批人</td><td width='20%'>审批时间</td><td width='30%'>备注</td><td width='8%'>动作</td><td width='12%'>流程状态</td><td width='20%'>耗时</td>");
		htmlBuilder.append("</tr>");
		
		RelationHIS tempRelHis =null;
		ActorHIS tempActorHIS = null;
		String startNodeid = "";
		Map<String, Object> timeConsMap = new HashMap<String, Object>();
		for (Iterator<RelationHIS> iter = hisList.iterator(); iter.hasNext();) {

			RelationHIS relHis = (RelationHIS) iter.next();

			Node node = relHis.getStartNode();
			
			if (relHis.getActorhiss().size() > 0) {
				for (Iterator<ActorHIS> iterator = relHis.getActorhiss()
						.iterator(); iterator.hasNext();) {
					htmlBuilder.append("<tr>");
					ActorHIS actorHis = (ActorHIS) iterator.next();
					// 流程处理人
					htmlBuilder.append("<td>");
					if(actorHis.getAgentname()!=null && actorHis.getAgentname().trim().length()>0){
						htmlBuilder.append(actorHis.getAgentname()+"("+actorHis.getName()+")");
					}else{
						htmlBuilder.append(actorHis.getName());
					}
					htmlBuilder.append("</td>");
					// 审批时间
					htmlBuilder.append("<td>");
					String pocesstime = "";
					if (actorHis.getProcesstime() != null) {
						pocesstime = DateUtil.getDateTimeStr(actorHis
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
					if (actorHis.getAttitude() != null
							&& actorHis.getAttitude().length() > 0) {
						attitude = actorHis.getAttitude();
					}
					htmlBuilder.append(attitude);
					htmlBuilder.append("</td>");
					// 动作
					htmlBuilder.append("<td>");
					String action = "";
					if (relHis.getFlowOperation() != null
							&& relHis.getFlowOperation().length() > 0) {
						action = this.toActionName4Archive(relHis.getFlowOperation());
					}
					htmlBuilder.append(action);
					htmlBuilder.append("</td>");
					// 流程标签
					htmlBuilder.append("<td>");
					htmlBuilder.append(StringUtil.dencodeHTML(relHis.getStartnodename()) + "<img src='" + user.getServerAddr() + params.getContextPath() + "/portal/share/icon/16x16_0060/"+this.toPng(relHis.getFlowOperation())+"' />" + StringUtil.dencodeHTML(relHis.getEndnodename()));
					htmlBuilder.append("</td>");

					//耗时 
					htmlBuilder.append("<td>");
					long consumeTime = 0;
					String consumeTimeStr = "";
					if (actorHis.getProcesstime() != null && tempActorHIS != null && tempRelHis != null) {
						try {
							if(node instanceof ManualNode 
									&& Integer.parseInt(((ManualNode)node).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND){
								//有顺序的会签,流程历史耗时从ActorHIS获取
								if(startNodeid.equals(relHis.getStartnodeid()) && !timeConsMap.isEmpty()){
									consumeTime = DateUtil.getDiffDateTime((Date)timeConsMap.get(relHis.getStartnodeid()),actorHis.getProcesstime());
								}else{
									consumeTime = DateUtil.getDiffDateTime(tempActorHIS.getProcesstime(),actorHis.getProcesstime());
								}
							}else{
								//非会签或无顺序的会签,流程历史耗时获取逻辑
								consumeTime = DateUtil.getDiffDateTime(tempRelHis.getActiontime(),actorHis.getProcesstime());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			       long day=consumeTime/(24*60*60*1000);
			       long hour=(consumeTime/(60*60*1000)-day*24);
			       long min=((consumeTime/(60*1000))-day*24*60-hour*60);
			       long s=(consumeTime/1000-day*24*60*60-hour*60*60-min*60);
			       consumeTimeStr = ""+day+"天"+hour+"小时"+min+"分"+s+"秒";
					htmlBuilder.append(consumeTimeStr);
					htmlBuilder.append("</td>");
					
					htmlBuilder.append("</tr>");
					
					tempActorHIS = actorHis;
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
				// 动作
				htmlBuilder.append("<td>");
				String action = "";
				if (relHis.getFlowOperation() != null
						&& relHis.getFlowOperation().length() > 0) {
					action = this.toActionName(relHis.getFlowOperation());
				}
				htmlBuilder.append(action);
				htmlBuilder.append("</td>");
				// 流程标签
				htmlBuilder.append("<td>");
				htmlBuilder.append(StringUtil.dencodeHTML(relHis.getStartnodename()) + "<img src='"+ctxpath+"/portal/share/icon/16x16_0060/"+this.toPng(relHis.getFlowOperation())+"' />" + StringUtil.dencodeHTML(relHis.getEndnodename()));
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
			
			startNodeid = relHis.getStartnodeid() != null ?relHis.getStartnodeid():"";
			timeConsMap.put(relHis.getEndnodeid(), tempActorHIS.getProcesstime());
		}
		htmlBuilder.append("</table>");
		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}

	/**
	 * 以文本形式显示流程历史,提供给手机客户端使用
	 * 
	 * @return 显示流程历史
	 */
	public String toTextXml() {
		StringBuffer xmlBuilder = new StringBuffer();
		xmlBuilder.append("<").append(MobileConstant.TAG_VIEW).append(" ")
				.append(MobileConstant.ATT_TITLE).append(
						"='{*[Flow]*}{*[History]*}'>");
		xmlBuilder.append("<" + MobileConstant.TAG_TH + ">");
		xmlBuilder.append("<" + MobileConstant.TAG_TD + ">{*[Auditor]*}</"
				+ MobileConstant.TAG_TD + ">");
		xmlBuilder.append("<" + MobileConstant.TAG_TD
				+ ">{*[cn.myapps.core.workflow.audit_time]*}</" + MobileConstant.TAG_TD + ">");
		xmlBuilder.append("<" + MobileConstant.TAG_TD + ">{*[Remark]*}</"
				+ MobileConstant.TAG_TD + ">");
		xmlBuilder.append("<" + MobileConstant.TAG_TD
				+ ">{*[Flow]*}{*[State]*}</" + MobileConstant.TAG_TD + ">");
		xmlBuilder.append("<" + MobileConstant.TAG_TD
				+ ">{*[time.consuming]*}</"+ MobileConstant.TAG_TD + ">");
		xmlBuilder.append("</" + MobileConstant.TAG_TH + ">");
		RelationHIS tempRelHIS = null;
		for (Iterator<RelationHIS> iter = hisList.iterator(); iter.hasNext();) {
			RelationHIS relHis = (RelationHIS) iter.next();
			if (relHis.getActorhiss().size() > 0) {
				for (Iterator<ActorHIS> iterator = relHis.getActorhiss()
						.iterator(); iterator.hasNext();) {
					ActorHIS actorHis = (ActorHIS) iterator.next();
					String time = "";
					if (actorHis.getProcesstime() != null) {
						time = DateUtil.getDateTimeStr(actorHis
								.getProcesstime());
					} else {
						time = DateUtil.getDateTimeStr(relHis.getActiontime());
					}
					xmlBuilder.append("<" + MobileConstant.TAG_TR + ">");
					xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append(actorHis.getName());
					xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append(time);
					xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append(!StringUtils.isBlank(actorHis
							.getAttitude()) ? actorHis.getAttitude() : "");
					xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append(StringUtil.dencodeHTML(relHis.getStartnodename()) + " --> " + StringUtil.dencodeHTML(relHis.getEndnodename()));
					xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
					long consumeTime = 0;
					String consumeTimeStr = "";
					if (relHis.getActiontime() != null && tempRelHIS != null) {
						try {
							consumeTime = DateUtil.getDiffDateTime(tempRelHIS.getActiontime(),relHis
									.getActiontime());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					long day=consumeTime/(24*60*60*1000);
					long hour=(consumeTime/(60*60*1000)-day*24);
					long min=((consumeTime/(60*1000))-day*24*60-hour*60);
					long s=(consumeTime/1000-day*24*60*60-hour*60*60-min*60);
					consumeTimeStr = ""+day+"{*[Days]*}"+hour+"{*[Hours]*}"+min+"{*[Minutes]*}"+s+"{*[Seconds]*}";
					xmlBuilder.append(consumeTimeStr);
					xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
					xmlBuilder.append("</" + MobileConstant.TAG_TR + ">");
					tempRelHIS = relHis;
				}
			} else {
				xmlBuilder.append("<" + MobileConstant.TAG_TR + ">");
				xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
				xmlBuilder
						.append(!StringUtils.isBlank(relHis.getAuditor()) ? relHis
								.getAuditor()
								: "");
				xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
				xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
				xmlBuilder.append(DateUtil.getDateTimeStr(relHis
						.getActiontime()));
				xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
				xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
				xmlBuilder
						.append(!StringUtils.isBlank(relHis.getAttitude()) ? relHis
								.getAttitude()
								: "");
				xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
				xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
				xmlBuilder.append(!StringUtils.isBlank(relHis
						.getStartnodename()) ? StringUtil.dencodeHTML(relHis.getStartnodename()) : "");
				xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
				xmlBuilder.append("<" + MobileConstant.TAG_TD + ">");
				long consumeTime = 0;
				String consumeTimeStr = "";
				if (relHis.getActiontime() != null && tempRelHIS != null) {
					try {
						consumeTime = DateUtil.getDiffDateTime(tempRelHIS.getActiontime(),relHis
								.getActiontime());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				long day=consumeTime/(24*60*60*1000);
				long hour=(consumeTime/(60*60*1000)-day*24);
				long min=((consumeTime/(60*1000))-day*24*60-hour*60);
				long s=(consumeTime/1000-day*24*60*60-hour*60*60-min*60);
				consumeTimeStr = ""+day+"天"+hour+"小时"+min+"分"+s+"秒";
				xmlBuilder.append(consumeTimeStr);
				xmlBuilder.append("</" + MobileConstant.TAG_TD + ">");
				xmlBuilder.append("</" + MobileConstant.TAG_TR + ">");
				tempRelHIS = relHis;
			}
		}
		xmlBuilder.append("</").append(MobileConstant.TAG_VIEW + ">");
		return xmlBuilder.toString();
	}
	
	private String toActionName(String code){
		if(FlowType.RUNNING2RUNNING_BACK.equals(code)){
			return "{*[cn.myapps.core.workflow.reject]*}";
		}else if(FlowType.RUNNING2RUNNING_RETRACEMENT.equals(code)){
			return "{*[Retracement]*}";
		}else if (FlowType.RUNNING2RUNNING_HANDUP.equals(code)){
			return "{*[cn.myapps.core.workflow.suspend]*}";
		}else if(FlowType.RUNNING2RUNNING_RECOVER.equals(code)){
			return "{*[cn.myapps.core.workflow.recover]*}";
		}else{
			return "{*[cn.myapps.core.workflow.doFlow]*}";
		}
	}
	
	private String toActionName4Archive(String code){
		if(FlowType.RUNNING2RUNNING_BACK.equals(code)){
			return "回退";
		}else if(FlowType.RUNNING2RUNNING_RETRACEMENT.equals(code)){
			return "回撤";
		}else if (FlowType.RUNNING2RUNNING_HANDUP.equals(code)){
			return "挂起";
		}else if(FlowType.RUNNING2RUNNING_RECOVER.equals(code)){
			return "恢复";
		}else{
			return "流转";
		}
	}
	
	private String toPng(String code){
		if(FlowType.RUNNING2RUNNING_BACK.equals(code)){
			return "arrow_right_yellow.png";
		}else if(FlowType.RUNNING2RUNNING_RETRACEMENT.equals(code)){
			return "arrow_right_red.png";
		}else{
			return "arrow_right.png";
		}
	}
}
