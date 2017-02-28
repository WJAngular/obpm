package cn.myapps.mobile2.menu;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;

public class MbMenuXMLBuilder {

	/**
	 * 获取一级菜单
	 * 
	 * @param user
	 * @param params
	 * @param type
	 * @return
	 */
	public static String toMobileMenu(WebUser user, ParamsTable params, String type) {
		StringBuffer sb = new StringBuffer();
		try {
			ApplicationHelper appHelper = new ApplicationHelper();
			sb.append("<").append(MobileConstant2.TAG_MENUDATA).append(">");
			Collection<ApplicationVO> appList = appHelper.getListByWebUser(user);
			for (ApplicationVO appvo : appList) {
				StringBuffer applicationSB = new StringBuffer();
				Collection<ResourceVO> resourceList = MbMenuHelper.getResources(appvo.getId(), user, type, null);

				if (resourceList.size() > 0) {
					applicationSB.append("<").append(MobileConstant2.TAG_APPLICATION);

					applicationSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(appvo.getId())
							.append("'");
					applicationSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(appvo.getName())
							.append("'");
					applicationSB.append(">");

					for (Iterator<ResourceVO> resit = resourceList.iterator(); resit.hasNext();) {
						ResourceVO resvo = resit.next();
						StringBuffer resourceSB = new StringBuffer();
						resourceSB.append("<").append(MobileConstant2.TAG_MENUITEM);
						resourceSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(resvo.getId())
								.append("'");
						resourceSB.append(" ").append(MobileConstant2.ATT_ICON).append("='")
								.append(resvo.getMobileIco()).append("'");
						resourceSB.append(" ").append(MobileConstant2.ATT_LINKTYPE).append("='")
								.append(resvo.getLinkType()).append("'");
						resourceSB.append(" ").append(MobileConstant2.ATT_NAME).append("='")
								.append(MbMenuHelper.replacSpecial(resvo.getDescription())).append("'");
						resourceSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='")
								.append(appvo.getId()).append("'");
						resourceSB.append(">");
						resourceSB.append(createParamsXML(resvo));
						resourceSB.append("</").append(MobileConstant2.TAG_MENUITEM).append(">");
						applicationSB.append(resourceSB.toString());
					}

					applicationSB.append("</").append(MobileConstant2.TAG_APPLICATION).append(">");
				}
				sb.append(applicationSB);
			}
			sb.append("</").append(MobileConstant2.TAG_MENUDATA).append(">");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取次级级菜单
	 * 
	 * @param user
	 * @param params
	 * @param type
	 * @return
	 */
	public static String toSecondaryMenu(WebUser user, String applicationid, String type, String id) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<").append(MobileConstant2.TAG_MENUDATA).append(">");
			Collection<ResourceVO> resourceList = MbMenuHelper.getResources(applicationid, user, type, id);

			if (resourceList != null && resourceList.size() > 0) {

				for (Iterator<ResourceVO> resit = resourceList.iterator(); resit.hasNext();) {
					ResourceVO resvo = resit.next();
					StringBuffer resourceSB = new StringBuffer();
					resourceSB.append("<").append(MobileConstant2.TAG_MENUITEM);
					resourceSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(resvo.getId())
							.append("'");
					resourceSB.append(" ").append(MobileConstant2.ATT_ICON).append("='").append(resvo.getMobileIco())
							.append("'");
					resourceSB.append(" ").append(MobileConstant2.ATT_LINKTYPE).append("='")
							.append(resvo.getLinkType()).append("'");
					resourceSB.append(" ").append(MobileConstant2.ATT_NAME).append("='")
							.append(MbMenuHelper.replacSpecial(resvo.getDescription())).append("'");
					resourceSB.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='").append(applicationid)
							.append("'");
					resourceSB.append(">");
					resourceSB.append(createParamsXML(resvo));
					resourceSB.append("</").append(MobileConstant2.TAG_MENUITEM).append(">");
					sb.append(resourceSB.toString());
				}

			}
			sb.append("</").append(MobileConstant2.TAG_MENUDATA).append(">");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String toMobileXML(Document doc, WebUser user, ParamsTable params, String type) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			ApplicationHelper appHelper = new ApplicationHelper();
			sb.append("<").append(MobileConstant2.TAG_MENUDATA).append(">");
			Collection<ApplicationVO> appList = appHelper.getListByWebUser(user);
			for (ApplicationVO appvo : appList) {
				try {
					StringBuffer applicationSB = new StringBuffer();

					Collection<ResourceVO> resourceList = MbMenuHelper.getResourceList(appvo, user, type);
					if (resourceList != null && resourceList.size() > 0) {
						applicationSB.append("<").append(MobileConstant2.TAG_APPLICATION);

						applicationSB.append(" ").append(MobileConstant2.ATT_ID).append("='").append(appvo.getId())
								.append("'");
						applicationSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(appvo.getName())
								.append("'").append(">");

						for (Iterator<ResourceVO> resit = resourceList.iterator(); resit.hasNext();) {
							ResourceVO resvo = resit.next();
							try {
								StringBuffer resourceSB = new StringBuffer();
								Collection<ResourceVO> subResList = MbMenuHelper.doGetSubResource(appvo, resvo, user,
										type);
								if (subResList.size() > 0
										|| (resvo.getLinkType() != null && resvo.getLinkType().equals(type))) {
									resourceSB.append("<").append(MobileConstant2.TAG_MENUITEM);
									resourceSB.append(" ").append(MobileConstant2.ATT_ID).append("='")
											.append(resvo.getId()).append("'");
									resourceSB.append(" ").append(MobileConstant2.ATT_ICON).append("='")
											.append(resvo.getMobileIco()).append("'");
									resourceSB.append(" ").append(MobileConstant2.ATT_LINKTYPE).append("='")
											.append(resvo.getLinkType()).append("'");
									resourceSB.append(" ").append(MobileConstant2.ATT_NAME).append("='")
											.append(MbMenuHelper.replacSpecial(resvo.getDescription())).append("'")
											.append(">");
									resourceSB.append(createParamsXML(resvo));
									if (subResList != null && subResList.size() > 0) {
										for (Iterator<ResourceVO> subResit = subResList.iterator(); subResit.hasNext();) {
											ResourceVO subresvo = subResit.next();
											resourceSB.append("<").append(MobileConstant2.TAG_MENUITEM);
											resourceSB.append(" ").append(MobileConstant2.ATT_ID).append("='")
													.append(subresvo.getId()).append("'");
											resourceSB.append(" ").append(MobileConstant2.ATT_ICON).append("='")
													.append(subresvo.getMobileIco()).append("'");
											resourceSB.append(" ").append(MobileConstant2.ATT_LINKTYPE).append("='")
													.append(subresvo.getLinkType()).append("'");
											resourceSB.append(" ").append(MobileConstant2.ATT_NAME).append("='")
													.append(MbMenuHelper.replacSpecial(subresvo.getDescription()))
													.append("'").append(">");
											resourceSB.append(createParamsXML(subresvo));
											resourceSB.append("</").append(MobileConstant2.TAG_MENUITEM).append(">");
										}
									}
									resourceSB.append("</").append(MobileConstant2.TAG_MENUITEM).append(">");
								}

								applicationSB.append(resourceSB);
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println(resvo.getDescription() + "   " + appvo.getName());
							}
						}
						applicationSB.append("</").append(MobileConstant2.TAG_APPLICATION).append(">");
					}
					sb.append(applicationSB);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sb.append("</").append(MobileConstant2.TAG_MENUDATA).append(">");

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	private static String createParamsXML(ResourceVO resvo) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			Map<String, String> params = MbMenuHelper.createResourceParams(resvo);
			Set<Map.Entry<String, String>> set = params.entrySet();
			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append("<").append(MobileConstant2.TAG_PARAMS);
				sb.append(" ").append("KEY").append("='").append(MbMenuHelper.replacSpecial(key)).append("'");
				sb.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(MbMenuHelper.replacSpecial(value))
						.append("'").append(">");
				sb.append("</").append(MobileConstant2.TAG_PARAMS).append(">");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

}
