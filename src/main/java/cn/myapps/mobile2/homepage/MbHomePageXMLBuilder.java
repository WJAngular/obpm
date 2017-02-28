package cn.myapps.mobile2.homepage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.homepage.action.HomePageHelper;
import cn.myapps.core.personalmessage.action.PersonalMessageHelper;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.widget.action.PageWidgetHelper;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.util.StringUtil;

public class MbHomePageXMLBuilder {

	/**
	 * 手机端最新接口 获取首页widget以及站内信息
	 * 
	 * @param user
	 * @param params
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static String toHomePageXML(WebUser user) throws JSONException {
		try {
			StringBuffer sb = new StringBuffer();
			
			StringBuffer groupL = new StringBuffer();
			StringBuffer groupM = new StringBuffer();
			StringBuffer groupR = new StringBuffer();
			
			sb.append("<").append("HOMEPAGEDATA").append(">");

			HomePageHelper homePageHelper = new HomePageHelper();

			UserDefined userDefined = homePageHelper.getUserDefined(user);

			Collection<PageWidget> list = homePageHelper.getMyWidgets(user);
			ArrayList<PageWidget> shortCut = MbHomePageHelper.getShortCutWidget(list);
			Map<String, PageWidget> widgetList = MbHomePageHelper.getWidgetMap(list, shortCut);

			sb.append("<").append("WIDGETDATA");
			if (userDefined != null && userDefined.getTemplateElement() != null) {
				String templateElement = userDefined.getTemplateElement();
				JSONObject jsonData = new JSONObject(templateElement);
				String layoutStyle = jsonData.getString("layoutStyle");
				sb.append(" ").append("TYPE").append("='").append(layoutStyle).append("'");
			} else {
				sb.append(" ").append("TYPE").append("='").append("1:1:1").append("'");
			}

			sb.append(">");

			sb.append("<").append("WIDGETSHORTCUT").append(">");

			for (PageWidget pageWidget : shortCut) {
				if (pageWidget.getType().equals(PageWidget.TYPE_SUMMARY)
						|| pageWidget.getType().equals(PageWidget.TYPE_VIEW)
						|| pageWidget.getType().equals(PageWidget.TYPE_SYSTEM_ANNOUNCEMENT)) {
					sb.append(buildWidget(pageWidget, user));
				}
			}

			sb.append("</").append("WIDGETSHORTCUT").append(">");

			if (userDefined != null && userDefined.getTemplateElement() != null) {
				String templateElement = userDefined.getTemplateElement();
				JSONObject jsonData = new JSONObject(templateElement);

				JSONObject appL = jsonData.getJSONObject("appL");
				groupL.append("<").append("WIDGETGROUP").append(">");
				for (Iterator<String> iterator = appL.keys(); iterator.hasNext();) {
					String key = iterator.next();
					PageWidget widget = widgetList.get(key);
					if (widget != null) {
						if (widget.getType().equals(PageWidget.TYPE_SUMMARY)
								|| widget.getType().equals(PageWidget.TYPE_VIEW)) {
							groupL.append(buildWidget(widget, user));
						}
					}
				}
				groupL.append("</").append("WIDGETGROUP").append(">");

				JSONObject appM = jsonData.getJSONObject("appM");
				groupM.append("<").append("WIDGETGROUP").append(">");
				for (Iterator<String> iterator = appM.keys(); iterator.hasNext();) {
					String key = iterator.next();
					PageWidget widget = widgetList.get(key);
					if (widget != null) {
						groupM.append(buildWidget(widget, user));
					}
				}
				groupM.append("</").append("WIDGETGROUP").append(">");

				JSONObject appR = jsonData.getJSONObject("appR");
				groupR.append("<").append("WIDGETGROUP").append(">");
				for (Iterator<String> iterator = appR.keys(); iterator.hasNext();) {
					String key = iterator.next();
					PageWidget widget = widgetList.get(key);
					if (widget != null) {
						groupR.append(buildWidget(widget, user));
					}
				}
				groupR.append("</").append("WIDGETGROUP").append(">");

			} else {
				groupL.append("<").append("WIDGETGROUP").append(">");
				groupM.append("<").append("WIDGETGROUP").append(">");
				groupR.append("<").append("WIDGETGROUP").append(">");
				int i = 0;
				for (Iterator<PageWidget> iterator = widgetList.values().iterator(); iterator.hasNext();) {
					i++;
					PageWidget widget = iterator.next();
					if (widget != null) {
						if (widget.getType().equals(PageWidget.TYPE_SUMMARY)
								|| widget.getType().equals(PageWidget.TYPE_VIEW)) {
							switch(i%3){
								case 1:{
									groupL.append(buildWidget(widget, user));
									break;
								}
								case 2:{
									groupM.append(buildWidget(widget, user));
									break;
								}
								case 0:{
									groupR.append(buildWidget(widget, user));
									break;
								}
							}
						}
					}
				}
				groupL.append("</").append("WIDGETGROUP").append(">");
				groupM.append("</").append("WIDGETGROUP").append(">");
				groupR.append("</").append("WIDGETGROUP").append(">");
			}
			sb.append(groupL.toString());
			sb.append(groupM.toString());
			sb.append(groupR.toString());
			
			sb.append("</").append("WIDGETDATA").append(">");
			sb.append("</").append("HOMEPAGEDATA").append(">");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String buildWidget(PageWidget pageWidget, WebUser user) throws Exception {
		StringBuffer widgetSB = new StringBuffer();
		widgetSB.append("<").append(MobileConstant2.TAG_WIDGET);
		widgetSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='").append(pageWidget.getApplicationid())
				.append("'");
		widgetSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(pageWidget.getId()).append("'");
		widgetSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(pageWidget.getName()).append("'");
		widgetSB.append(" ").append(MobileConstant2.ATT_TYPE).append("='").append(pageWidget.getType()).append("'");
		widgetSB.append(" ").append("ICON").append("='").append(pageWidget.getIcon()).append("'");

		int size = 0;
		if (pageWidget.getType().equals(PageWidget.TYPE_SUMMARY)) {
			size = MbHomePageHelper.getSummaryCount(pageWidget, user, new ParamsTable());
		} else if (pageWidget.getType().equals(PageWidget.TYPE_SYSTEM_ANNOUNCEMENT)) {
			size = MbHomePageHelper.getAnnouncementsCout(user);
		} else if (pageWidget.getType().equals(PageWidget.TYPE_VIEW)) {
			size = 0;
		}

		widgetSB.append(" ").append(MobileConstant2.ATT_SIZE).append("='").append(size).append("'");

		widgetSB.append(">");

		if (pageWidget.getType().equals(PageWidget.TYPE_VIEW)) {
			StringBuffer paramsSB = new StringBuffer();
			paramsSB.append("<").append(MobileConstant2.TAG_PARAMS);
			paramsSB.append(" ").append("KEY").append("='").append("_viewid").append("'");
			paramsSB.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(pageWidget.getActionContent())
					.append("'").append(">");
			paramsSB.append("</").append(MobileConstant2.TAG_PARAMS).append(">");

			paramsSB.append("<").append(MobileConstant2.TAG_PARAMS);
			paramsSB.append(" ").append("KEY").append("='").append("application").append("'");
			paramsSB.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(pageWidget.getApplicationid())
					.append("'").append(">");
			paramsSB.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
			widgetSB.append(paramsSB);
		}

		widgetSB.append("</").append(MobileConstant2.TAG_WIDGET).append(">");
		return widgetSB.toString();
	}

	public static String toPendingXML(WebUser user, ParamsTable params) {
		StringBuffer sb = new StringBuffer();
		try {
			String widgetId = params.getParameterAsString("_widgetid");
			PageWidgetHelper widgetHelper = new PageWidgetHelper();
			PageWidget widget = widgetHelper.getWidget(widgetId);
			if (widget != null) {
				int count = MbHomePageHelper.getSummaryCount(widget, user, new ParamsTable());
				sb.append("<").append("WIDGET");
				sb.append(" ").append(MobileConstant2.ATT_SIZE).append("='").append(count).append("'").append(">");
				Collection<PendingVO> pendingList = MbHomePageHelper.getSummaryPendVO(widget, user, params);
				if (pendingList != null && pendingList.size() > 0) {
					Iterator<PendingVO> pendingIt = pendingList.iterator();
					while (pendingIt.hasNext()) {
						try {
							StringBuffer pendingSB = new StringBuffer();
							PendingVO pvo = pendingIt.next();
							pendingSB.append("<").append(MobileConstant2.TAG_PENDING);
							pendingSB.append(" ").append(MobileConstant2.ATT_DOCID).append("='").append(pvo.getDocId())
									.append("'");
							pendingSB.append(" ").append(MobileConstant2.ATT_FORMID).append("='")
									.append(pvo.getFormid()).append("'");
							pendingSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='")
									.append(pvo.getApplicationid()).append("'");
							pendingSB.append(" ").append(MobileConstant2.ATT_ISREAD).append("='")
									.append(MbHomePageHelper.checkRead(pvo, user)).append("'").append(">");
							pendingSB.append(MbHomePageHelper.replacSpecial(pvo.getSummary()));
							pendingSB.append("</").append(MobileConstant2.TAG_PENDING).append(">");
							sb.append(pendingSB.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				sb.append("</").append("WIDGET").append(">");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String toWidgetMobileXML(WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			String widgetId = params.getParameterAsString("_widgetid");
			PageWidget widget = MbHomePageHelper.getWidgetWithId(widgetId);
			if (widget != null) {
				sb.append("<").append("WIDGETDATA").append(">");
				Collection<PendingVO> pendingList = MbHomePageHelper.getSummaryPendVO(widget, user, params);
				if (pendingList != null && pendingList.size() > 0) {
					Iterator<PendingVO> pendingIt = pendingList.iterator();
					while (pendingIt.hasNext()) {
						try {
							StringBuffer pendingSB = new StringBuffer();
							PendingVO pvo = pendingIt.next();
							pendingSB.append("<").append(MobileConstant2.TAG_PENDING);
							pendingSB.append(" ").append(MobileConstant2.ATT_DOCID).append("='").append(pvo.getDocId())
									.append("'");
							pendingSB.append(" ").append(MobileConstant2.ATT_FORMID).append("='")
									.append(pvo.getFormid()).append("'");
							pendingSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='")
									.append(pvo.getApplicationid()).append("'");
							pendingSB.append(" ").append(MobileConstant2.ATT_ISREAD).append("='")
									.append(MbHomePageHelper.checkRead(pvo, user)).append("'").append(">");
							pendingSB.append(MbHomePageHelper.replacSpecial(pvo.getSummary()));
							pendingSB.append("</").append(MobileConstant2.TAG_PENDING).append(">");
							sb.append(pendingSB.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				sb.append("</").append("WIDGETDATA").append(">");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	public static String toHomePageMobileXML(WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			PersonalMessageHelper pmh = new PersonalMessageHelper();

			sb.append("<").append(MobileConstant2.TAG_HOMEPAGEDATA).append(">");

			sb.append("<").append(MobileConstant2.TAG_MESSAGEDATA);
			sb.append(" ").append(MobileConstant2.ATT_NOREAD).append("='").append(pmh.countMessage(user.getId()))
					.append("'");
			sb.append(" ").append(MobileConstant2.ATT_READ).append("='").append(pmh.countIsReadMessage(user.getId()))
					.append("'");
			sb.append(">");
			sb.append("</").append(MobileConstant2.TAG_MESSAGEDATA).append(">");

			HomePageHelper homepageHelper = new HomePageHelper();
			Collection<PageWidget> widgetlist = homepageHelper.getMyWidgets(user);

			if (widgetlist != null && widgetlist.size() > 0) {
				Iterator<PageWidget> widgetIt = widgetlist.iterator();
				while (widgetIt.hasNext()) {
					try {
						StringBuffer widgetSB = new StringBuffer();
						PageWidget pageWidget = widgetIt.next();
						if (pageWidget != null) {
							// 只支持摘要以及视图
							if (pageWidget.getType().equals(PageWidget.TYPE_SUMMARY)
									|| pageWidget.getType().equals(PageWidget.TYPE_VIEW)) {
								if (PageWidget.TYPE_SUMMARY.equals(pageWidget.getType())) {

									widgetSB.append("<").append(MobileConstant2.TAG_WIDGET);
									widgetSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='")
											.append(pageWidget.getApplicationid()).append("'");
									widgetSB.append(" ").append(MobileConstant2.ATT_ID).append("='")
											.append(pageWidget.getId()).append("'");
									widgetSB.append(" ").append(MobileConstant2.ATT_NAME).append("='")
											.append(pageWidget.getName()).append("'");

									Collection<PendingVO> pendingList = MbHomePageHelper.getSummaryPendVO(pageWidget,
											user, params);
									if (pendingList != null && pendingList.size() > 0) {
										widgetSB.append(" ").append(MobileConstant2.ATT_SIZE).append("='")
												.append(pendingList.size()).append("'");
									} else {
										widgetSB.append(" ").append(MobileConstant2.ATT_SIZE).append("='").append(0)
												.append("'");
									}
									widgetSB.append(">");
									widgetSB.append("</").append(MobileConstant2.TAG_WIDGET).append(">");
								} else if (PageWidget.TYPE_VIEW.equals(pageWidget.getType())) {
									widgetSB.append("<").append(MobileConstant2.TAG_WIDGET);
									widgetSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='")
											.append(pageWidget.getApplicationid()).append("'");
									widgetSB.append(" ").append(MobileConstant2.ATT_ID).append("='")
											.append(pageWidget.getId()).append("'");
									widgetSB.append(" ").append(MobileConstant2.ATT_NAME).append("='")
											.append(pageWidget.getName()).append("'");
									widgetSB.append(" ").append(MobileConstant2.ATT_TYPE).append("='")
											.append(pageWidget.getType()).append("'");
									widgetSB.append(">");

									StringBuffer paramsSB = new StringBuffer();
									paramsSB.append("<").append(MobileConstant2.TAG_PARAMS);
									paramsSB.append(" ").append("KEY").append("='").append("_viewid").append("'");
									paramsSB.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
											.append(pageWidget.getActionContent()).append("'").append(">");
									paramsSB.append("</").append(MobileConstant2.TAG_PARAMS).append(">");

									paramsSB.append("<").append(MobileConstant2.TAG_PARAMS);
									paramsSB.append(" ").append("KEY").append("='").append("application").append("'");
									paramsSB.append(" ").append(MobileConstant2.ATT_VALUE).append("='")
											.append(pageWidget.getApplicationid()).append("'").append(">");
									paramsSB.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
									widgetSB.append(paramsSB);

									widgetSB.append("</").append(MobileConstant2.TAG_WIDGET).append(">");
								}
							}
						}
						sb.append(widgetSB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			sb.append("</").append(MobileConstant2.TAG_HOMEPAGEDATA).append(">");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	public static String toMessageMobileXML(WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			PersonalMessageHelper pmh = new PersonalMessageHelper();

			Collection<PersonalMessageVO> message = null;

			if (params.getParameterAsString("_isRead") != null) {
				boolean isRead = params.getParameterAsBoolean("_isRead");
				if (isRead) {
					message = MbHomePageHelper.doGetReadMessage(user);
				} else {
					message = MbHomePageHelper.doGetNoReadMessage(user);
				}
			}

			sb.append("<").append("MESSAGEDATA").append(">");

			if (message != null && message.size() > 0) {
				for (Iterator<PersonalMessageVO> ites = message.iterator(); ites.hasNext();) {
					StringBuffer messageSB = new StringBuffer();
					PersonalMessageVO vo = ites.next();
					if (vo != null) {
						messageSB.append("<").append(MobileConstant2.TAG_MESSAGE).append(" ");
						messageSB.append(MobileConstant2.ATT_ID).append("='").append(vo.getId()).append("'")
								.append(" ");
						messageSB.append(MobileConstant2.ATT_TYPE).append("='")
								.append(pmh.findDisplayText(vo.getType())).append("'").append(" ");
						if (StringUtil.isBlank(vo.getSenderId())) {
							messageSB.append(MobileConstant2.ATT_SENDERID).append("='").append("系统").append("'")
									.append(" ");
						} else {
							messageSB.append(MobileConstant2.ATT_SENDERID).append("='")
									.append(pmh.findUserName(vo.getSenderId())).append("'").append(" ");
						}
						messageSB.append(MobileConstant2.ATT_ISREAD).append("='").append(vo.isRead()).append("'")
								.append(" ");
						messageSB
								.append(MobileConstant2.ATT_CONTENT)
								.append("='")
								.append(MbHomePageHelper.replacSpecial(MbMessageHelper.replaceHTML(vo.getBody()
										.getContent()))).append("'>");
						messageSB.append(MbHomePageHelper.replacSpecial(MbMessageHelper.replaceHTML(vo.getBody()
								.getTitle())));
						messageSB.append("</").append(MobileConstant2.TAG_MESSAGE).append(">");
					}
					sb.append(messageSB);
				}
			}
			sb.append("</").append("MESSAGEDATA").append(">");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	public static String toMobileXML(WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			PersonalMessageHelper pmh = new PersonalMessageHelper();
			Collection<PersonalMessageVO> message = MbHomePageHelper.doGetPersonalMessage(user);

			sb.append("<").append(MobileConstant2.TAG_HOMEPAGEDATA).append(">");

			sb.append("<").append(MobileConstant2.TAG_GLOBAL).append(">");
			if (message != null && message.size() > 0) {
				for (Iterator<PersonalMessageVO> ites = message.iterator(); ites.hasNext();) {
					StringBuffer messageSB = new StringBuffer();
					PersonalMessageVO vo = ites.next();
					if (vo != null) {
						messageSB.append("<").append(MobileConstant2.TAG_MESSAGE).append(" ");
						messageSB.append(MobileConstant2.ATT_ID).append("='").append(vo.getId()).append("'")
								.append(" ");
						messageSB.append(MobileConstant2.ATT_TYPE).append("='")
								.append(pmh.findDisplayText(vo.getType())).append("'").append(" ");
						if (StringUtil.isBlank(vo.getSenderId())) {
							messageSB.append(MobileConstant2.ATT_SENDERID).append("='").append("系统").append("'")
									.append(" ");
						} else {
							messageSB.append(MobileConstant2.ATT_SENDERID).append("='")
									.append(pmh.findUserName(vo.getSenderId())).append("'").append(" ");
						}
						messageSB.append(MobileConstant2.ATT_ISREAD).append("='").append(vo.isRead()).append("'")
								.append(" ");
						messageSB.append(MobileConstant2.ATT_CONTENT).append("='")
								.append(MbHomePageHelper.replacSpecial(vo.getBody().getContent())).append("'>");
						messageSB.append(MbHomePageHelper.replacSpecial(vo.getBody().getTitle()));
						messageSB.append("</").append(MobileConstant2.TAG_MESSAGE).append(">");
					}
					sb.append(messageSB);
				}
			}
			sb.append("</").append(MobileConstant2.TAG_GLOBAL).append(">");

			HomePageHelper homepageHelper = new HomePageHelper();
			Collection<PageWidget> widgetlist = homepageHelper.getMyWidgets(user);

			if (widgetlist != null && widgetlist.size() > 0) {
				Iterator<PageWidget> widgetIt = widgetlist.iterator();
				while (widgetIt.hasNext()) {
					try {
						StringBuffer widgetSB = new StringBuffer();
						PageWidget pageWidget = widgetIt.next();
						if (PageWidget.TYPE_SUMMARY.equals(pageWidget.getType())) {

							widgetSB.append("<").append(MobileConstant2.TAG_WIDGET);
							widgetSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='")
									.append(pageWidget.getApplicationid()).append("'");
							widgetSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(pageWidget.getId())
									.append("'");
							widgetSB.append(" ").append(MobileConstant2.ATT_NAME).append("='")
									.append(pageWidget.getName()).append("'");
							widgetSB.append(">");

							if (pageWidget != null) {
								Collection<PendingVO> pendingList = MbHomePageHelper.getSummaryPendVO(pageWidget, user,
										params);
								if (pendingList != null && pendingList.size() > 0) {
									Iterator<PendingVO> pendingIt = pendingList.iterator();
									while (pendingIt.hasNext()) {
										try {
											StringBuffer pendingSB = new StringBuffer();
											PendingVO pvo = pendingIt.next();
											pendingSB.append("<").append(MobileConstant2.TAG_PENDING);
											pendingSB.append(" ").append(MobileConstant2.ATT_DOCID).append("='")
													.append(pvo.getDocId()).append("'");
											pendingSB.append(" ").append(MobileConstant2.ATT_FORMID).append("='")
													.append(pvo.getFormid()).append("'");
											pendingSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='")
													.append(pvo.getApplicationid()).append("'");
											pendingSB.append(" ").append(MobileConstant2.ATT_ISREAD).append("='")
													.append(MbHomePageHelper.checkRead(pvo, user)).append("'")
													.append(">");
											pendingSB.append(MbHomePageHelper.replacSpecial(pvo.getSummary()));
											pendingSB.append("</").append(MobileConstant2.TAG_PENDING).append(">");
											widgetSB.append(pendingSB);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							}
							widgetSB.append("</").append(MobileConstant2.TAG_WIDGET).append(">");
						}
						sb.append(widgetSB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			sb.append("</").append(MobileConstant2.TAG_HOMEPAGEDATA).append(">");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}
}
