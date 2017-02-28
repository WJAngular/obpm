package cn.myapps.mobile2.menu;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.work.ejb.WorkVO;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class MbWorkXMLBuilder {

	/**
	 * 获取当前用户代办xml
	 * 
	 * @param user
	 *            当前用户
	 * @return
	 * @throws Exception
	 */
	public static String toPendingMobileXML(WebUser user) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			ApplicationHelper appHelper = new ApplicationHelper();
			sb.append("<").append(MobileConstant2.TAG_WORKDATA).append(">");
			Collection<ApplicationVO> cols = appHelper.getListByWebUser(user);
			for (ApplicationVO appvo : cols) {
				try {
					StringBuffer appSB = new StringBuffer();

					DataPackage<WorkVO> works = MbWorkHelper.getPendingWorkList(appvo, user);

					if (works != null && works.datas.size() >0) {
						appSB.append("<").append(MobileConstant2.TAG_APPLICATION);
						appSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(appvo.getId()).append("'");
						appSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(appvo.getName())
								.append("'").append(">");

						for (WorkVO work : works.datas) {
							try {
								appSB.append(toWorkXML(work));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						appSB.append("</").append(MobileConstant2.TAG_APPLICATION).append(">");
					}
					sb.append(appSB);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sb.append("</").append(MobileConstant2.TAG_WORKDATA).append(">");

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	/**
	 * 获取流程经办xml
	 * 
	 * @param user
	 *            当前用户
	 * @return
	 * @throws Exception
	 */
	public static String toProcessingMobileXML(WebUser user) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			ApplicationHelper appHelper = new ApplicationHelper();
			sb.append("<").append(MobileConstant2.TAG_WORKDATA).append(">");
			Collection<ApplicationVO> cols = appHelper.getListByWebUser(user);
			for (ApplicationVO appvo : cols) {
				try {
					StringBuffer appSB = new StringBuffer();

					DataPackage<WorkVO> works  = MbWorkHelper.getProcessedWorkList(appvo, user);

					if (works != null && works.datas.size() >0) {
						appSB.append("<").append(MobileConstant2.TAG_APPLICATION);
						appSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(appvo.getId()).append("'");
						appSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(appvo.getName())
								.append("'").append(">");

						for (WorkVO work : works.datas) {
							try {
								appSB.append(toWorkXML(work));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						appSB.append("</").append(MobileConstant2.TAG_APPLICATION).append(">");
					}

					sb.append(appSB);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sb.append("</").append(MobileConstant2.TAG_WORKDATA).append(">");

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	public static String toFinishedMobileXML(WebUser user,ParamsTable params) throws Exception{
		StringBuffer sb = new StringBuffer();
		try{
			String applicationid = params.getParameterAsString("_application");
			String subject = params.getParameterAsString("_subject");
			
			subject = (subject != null) ? subject:"";
			
			if(!StringUtil.isBlank(applicationid)){
			
					DataPackage<WorkVO> works = MbWorkHelper.getFinishedWorkList(user, applicationid, subject,params);
					if(works != null && works.rowCount>0){
						sb.append("<").append(MobileConstant2.TAG_WORKDATA);
						sb.append(" ").append("SIZE").append("='").append(works.rowCount).append("'");
						sb.append(">");
						for(WorkVO work:works.datas){
							sb.append(toWorkXML(work));
						}
						sb.append("</").append(MobileConstant2.TAG_WORKDATA).append(">");
					}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	/**
	 * 构造work对象的xml格式
	 * 
	 * @param work
	 *            工作
	 * @return
	 */
	private static String toWorkXML(WorkVO work) {
		StringBuffer sb = new StringBuffer();
		sb.append("<").append(MobileConstant2.TAG_WORK);
		sb.append(" ").append(MobileConstant2.ATT_DOCID).append("='").append(work.getDocId()).append("'");
		sb.append(" ").append(MobileConstant2.ATT_SUBJECT).append("='")
				.append(MbWorkHelper.replacSpecial(work.getSubject())).append("'");
		sb.append(" ").append(MobileConstant2.ATT_STATELABEL).append("='")
				.append(MbWorkHelper.replacSpecial(work.getStateLabel())).append("'");
		sb.append(" ").append(MobileConstant2.ATT_AUDITORNAMES).append("='")
				.append(MbWorkHelper.replacSpecial(work.getAuditorNames())).append("'");
		sb.append(" ").append(MobileConstant2.ATT_LASTPROCESSTIME).append("='").append(work.getLastProcessTime())
				.append("'");
		sb.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='").append(work.getApplicationId()).append("'");
		sb.append(" ").append(MobileConstant2.ATT_FLOWNAME).append("='").append(work.getFlowName()).append("'");
		sb.append(">");
		sb.append("</").append(MobileConstant2.TAG_WORK).append(">");
		return sb.toString();
	}
}
