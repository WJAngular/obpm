package cn.myapps.mobile2.service;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.action.DocumentHelper;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.util.DateUtil;
import cn.myapps.util.StringUtil;

public class MbFlowHistoryXMLBuilder {
	
	/**
	 * 获取流程历史
	 * @param user
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String toHistoryXML(WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		String docid = params.getParameterAsString("_docid");
		String applicationid = params.getParameterAsString("_applicationid");
		Collection<RelationHIS> colls = null;
		if (!StringUtil.isBlank(docid)) {
			Document doc = null;
			if (!StringUtil.isBlank(docid)) {
				doc = DocumentHelper.getDocumentById(docid, applicationid);
			}
			if (doc != null) {
				colls = StateMachineHelper.getAllRelationHIS(doc.getId(), doc.getState().getFlowid(),
						doc.getApplicationid());
			}
		}
		sb.append("<").append(MobileConstant2.TAG_HISTORY).append(">");
		RelationHIS tempRelHIS = null;
		if (colls != null) {
			for (Iterator<RelationHIS> iter = colls.iterator(); iter.hasNext();) {
				RelationHIS relHis = (RelationHIS) iter.next();
				StringBuffer relHisSB = new StringBuffer();
				if (relHis.getActorhiss().size() > 0) {
					for (Iterator<ActorHIS> iterator = relHis.getActorhiss().iterator(); iterator.hasNext();) {

						ActorHIS actorHis = (ActorHIS) iterator.next();
						String time = "";
						if (actorHis.getProcesstime() != null) {
							time = DateUtil.getDateTimeStr(actorHis.getProcesstime());
						} else {
							time = DateUtil.getDateTimeStr(relHis.getActiontime());
						}

						long consumeTime = 0;
						String consumeTimeStr = "";
						if (relHis.getActiontime() != null && tempRelHIS != null) {
							try {
								consumeTime = DateUtil.getDiffDateTime(tempRelHIS.getActiontime(),
										relHis.getActiontime());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						long day = consumeTime / (24 * 60 * 60 * 1000);
						long hour = (consumeTime / (60 * 60 * 1000) - day * 24);
						long min = ((consumeTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
						long s = (consumeTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
						consumeTimeStr = "" + day + "天" + hour + "时" + min + "分" + s + "秒";

						relHisSB.append("<").append("HISTORYITEM");
						relHisSB.append(" ").append("AUDITOR").append("='").append(actorHis.getName()).append("'");
						relHisSB.append(" ").append("AUDITDATE").append("='").append(time).append("'");
						relHisSB.append(" ").append("REMARK").append("='")
								.append(!StringUtil.isBlank(actorHis.getAttitude()) ? actorHis.getAttitude() : "")
								.append("'");
						relHisSB.append(" ").append("FLOWSTATE").append("='").append(relHis.getStartnodename() + " --> " + relHis.getEndnodename())
								.append("'");
						relHisSB.append(" ").append("SPENDTIME").append("='").append(consumeTimeStr).append("'");
						relHisSB.append(">");
						relHisSB.append("</").append("HISTORYITEM").append(">");
						tempRelHIS = relHis;
					}
				} else {
					long consumeTime = 0;
					String consumeTimeStr = "";
					if (relHis.getActiontime() != null && tempRelHIS != null) {
						try {
							consumeTime = DateUtil.getDiffDateTime(tempRelHIS.getActiontime(), relHis.getActiontime());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					long day = consumeTime / (24 * 60 * 60 * 1000);
					long hour = (consumeTime / (60 * 60 * 1000) - day * 24);
					long min = ((consumeTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
					long s = (consumeTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
					consumeTimeStr = "" + day + "天" + hour + "小时" + min + "分" + s + "秒";

					relHisSB.append("<").append("HISTORYITEM");
					relHisSB.append(" ").append("AUDITOR").append("='")
							.append(StringUtil.isBlank(relHis.getAuditor()) ? relHis.getAuditor() : "").append("'");
					relHisSB.append(" ").append("AUDITDATE").append("='")
							.append(DateUtil.getDateTimeStr(relHis.getActiontime())).append("'");
					relHisSB.append(" ").append("Remark").append("='")
							.append(!StringUtil.isBlank(relHis.getAttitude()) ? relHis.getAttitude() : "").append("'");
					relHisSB.append(" ").append("FLOWSTATE").append("='").append(relHis.getStartnodename() + " --> " + relHis.getEndnodename()).append("'");
					relHisSB.append(" ").append("SPENDTIME").append("='").append(consumeTimeStr).append("'");
					relHisSB.append(">");
					relHisSB.append("</").append("HISTORYITEM").append(">");

					tempRelHIS = relHis;
				}
				sb.append(relHisSB);
			}
		}
		return sb.toString();

	}
	
	public static String toMobileXML(WebUser user,ParamsTable params) throws Exception{
		StringBuffer sb = new StringBuffer();
		String docid = params.getParameterAsString("_docid");
		String applicationid = params.getParameterAsString("_application");
		Collection<RelationHIS> colls = null;
		if(!StringUtil.isBlank(docid)){
			Document doc = null;
			if (!StringUtil.isBlank(docid)) {
				doc = DocumentHelper.getDocumentById(docid, applicationid);
			}
			if(doc != null){
				 colls = StateMachineHelper.getAllRelationHIS(doc.getId(), doc.getState().getFlowid(), doc.getApplicationid());
			}
		}
		
		sb.append("<").append(MobileConstant2.TAG_HISTORY).append(" ").append(MobileConstant2.ATT_TITLE).append("='流程历史'>");
		sb.append("<").append(MobileConstant2.TAG_TH).append(">");
		sb.append("<").append(MobileConstant2.TAG_TD).append(">").append("{*[Auditor]*}").append("</").append(MobileConstant2.TAG_TD).append(">");
		sb.append("<").append(MobileConstant2.TAG_TD).append(">").append("{*[AuditDate]*}").append("</").append(MobileConstant2.TAG_TD).append(">");
		sb.append("<").append(MobileConstant2.TAG_TD).append(">").append("{*[Remark]*}").append("</").append(MobileConstant2.TAG_TD).append(">");
		sb.append("<").append(MobileConstant2.TAG_TD).append(">").append("{*[cn.myapps.core.workflow.flow_state]*}").append("</").append(MobileConstant2.TAG_TD).append(">");
		sb.append("<").append(MobileConstant2.TAG_TD).append(">").append("{*[time.consuming]*}").append("</").append(MobileConstant2.TAG_TD).append(">");
		sb.append("</").append(MobileConstant2.TAG_TH).append(">");
		
		RelationHIS tempRelHIS = null;
		for (Iterator<RelationHIS> iter = colls.iterator(); iter.hasNext();) {
			RelationHIS relHis = (RelationHIS) iter.next();
			StringBuffer relHisSB = new StringBuffer();
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
					relHisSB.append("<").append(MobileConstant2.TAG_TR).append(">");
					relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(actorHis.getName()).append("</").append(MobileConstant2.TAG_TD).append(">");
					relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(time).append("</").append(MobileConstant2.TAG_TD).append(">");
					relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(!StringUtil.isBlank(actorHis.getAttitude()) ? actorHis.getAttitude() : "").append("</").append(MobileConstant2.TAG_TD).append(">");
					relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(relHis.getStartnodename() + " --> " + relHis.getEndnodename()).append("</").append(MobileConstant2.TAG_TD).append(">");
					
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
					consumeTimeStr = ""+day+"天"+hour+"时"+min+"分"+s+"秒";
					relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(consumeTimeStr).append("</").append(MobileConstant2.TAG_TD).append(">");
					
					relHisSB.append("</").append(MobileConstant2.TAG_TR).append(">");
					tempRelHIS = relHis;
				}
			} else {
				StringBuffer actorHISrelHisSB = new StringBuffer();
				relHisSB.append("<").append(MobileConstant2.TAG_TR).append(">");
				relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(!StringUtil.isBlank(relHis.getAuditor()) ? relHis.getAuditor(): "").append("</").append(MobileConstant2.TAG_TD).append(">");
				relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(DateUtil.getDateTimeStr(relHis.getActiontime())).append("</").append(MobileConstant2.TAG_TD).append(">");
				relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(!StringUtil.isBlank(relHis.getAttitude()) ? relHis.getAttitude(): "").append("</").append(MobileConstant2.TAG_TD).append(">");
				relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(!StringUtil.isBlank(relHis.getStartnodename()) ? relHis.getStartnodename() : "").append("</").append(MobileConstant2.TAG_TD).append(">");
				
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
				relHisSB.append("<").append(MobileConstant2.TAG_TD).append(">").append(consumeTimeStr).append("</").append(MobileConstant2.TAG_TD).append(">");
				
				relHisSB.append("</").append(MobileConstant2.TAG_TR).append(">");
				tempRelHIS = relHis;
			}
			sb.append(relHisSB);
		}
		sb.append("</").append(MobileConstant2.TAG_HISTORY).append(">");
		return sb.toString();
	}
	
	
}
