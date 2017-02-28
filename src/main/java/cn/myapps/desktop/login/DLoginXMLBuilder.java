package cn.myapps.desktop.login;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.homepage.action.HomePageHelper;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.util.ProcessFactory;

public class DLoginXMLBuilder {

	public static String toDesktopXML(WebUser user, ParamsTable params) {
		try{
			HomePageHelper homepageHelper = new HomePageHelper();
			Collection<PageWidget> widgetlist = homepageHelper.getMyWidgets(user);
			StringBuffer xml = new StringBuffer();
			if (widgetlist != null && widgetlist.size() > 0) {
				xml.append("<" + MobileConstant.TAG_PENDING_LIST + ">");
				Iterator<PageWidget> widgetIt = widgetlist.iterator();
				while (widgetIt.hasNext()) {
					try {
						StringBuffer widgetSB = new StringBuffer();
						PageWidget pageWidget = widgetIt.next();
						if (PageWidget.TYPE_SUMMARY.equals(pageWidget.getType())) {
	
							widgetSB.append("<").append(
									MobileConstant.TAG_PENDING_GROUP);
							widgetSB.append(" ")
									.append(MobileConstant.ATT_APPLICATIONID)
									.append("='")
									.append(pageWidget.getApplicationid())
									.append("'");
							widgetSB.append(" ").append(MobileConstant.ATT_ID)
									.append("='").append(pageWidget.getId())
									.append("'");
							widgetSB.append(" ").append(MobileConstant.ATT_NAME)
									.append("='").append(pageWidget.getName())
									.append("'");
							widgetSB.append(">");
	
							if (pageWidget != null) {
								Collection<PendingVO> pendingList = DLoginHelper
										.getSummaryPendVO(pageWidget, user, params);
								if (pendingList != null && pendingList.size() > 0) {
									Iterator<PendingVO> pendingIt = pendingList
											.iterator();
									while (pendingIt.hasNext()) {
										try {
											StringBuffer pendingSB = new StringBuffer();
											PendingVO pvo = pendingIt.next();
											pendingSB
													.append("<")
													.append(MobileConstant.TAG_PENDING_ITEM);
											pendingSB.append(" ")
													.append(MobileConstant.ATT_ID)
													.append("='")
													.append(pvo.getDocId())
													.append("'");
											pendingSB
													.append(" ")
													.append(MobileConstant.ATT_FORMID)
													.append("='")
													.append(pvo.getFormid())
													.append("'");
											pendingSB
													.append(" ")
													.append(MobileConstant.ATT_APPLICATIONID)
													.append("='")
													.append(pvo.getApplicationid())
													.append("'");
											pendingSB
													.append(" ")
													.append("isRead")
													.append("='")
													.append(DLoginHelper.checkRead(
															pvo, user)).append("'")
													.append(">");
											pendingSB.append(pvo.getSummary());
											pendingSB
													.append("</")
													.append(MobileConstant.TAG_PENDING_ITEM)
													.append(">");
											widgetSB.append(pendingSB);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
							widgetSB.append("</")
									.append(MobileConstant.TAG_PENDING_GROUP)
									.append(">");
						}
						xml.append(widgetSB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				xml.append("</" + MobileConstant.TAG_PENDING_LIST + ">");
				return xml.toString();
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String toDesktopContactXML(WebUser user) {
		StringBuffer sb = new StringBuffer();
		try {
			UserProcess process = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			DomainVO domain = user.getDomain();
			if (domain != null) {
				sb.append("<").append(MobileConstant.TAG_CONTACT).append(">");
				sb.append("<").append(MobileConstant.TAG_DEP).append(" n='全部联系人' code='1'").append(">");
				ParamsTable params = new ParamsTable();
				params.setParameter("t_domainid", user.getDomainid());
				DataPackage<UserVO> datas = process.doQuery(params);
				if (datas != null && datas.rowCount > 0) {
					for (Iterator<UserVO> it = datas.datas.iterator(); it
							.hasNext();) {
						UserVO userVO = it.next();
						try {
							StringBuffer userSB = new StringBuffer();
							userSB.append("<").append(MobileConstant.TAG_CONTACT_USER);
							userSB.append(" ").append(MobileConstant.ATT_ID).append("='").append(userVO.getId()).append("'");
							userSB.append(">");
							userSB.append(userVO.getName());
							userSB.append("</").append(MobileConstant.TAG_CONTACT_USER).append(">");
							sb.append(userSB);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				sb.append("</").append(MobileConstant.TAG_DEP).append(">");
				sb.append("</").append(MobileConstant.TAG_CONTACT).append(">");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
