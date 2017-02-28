package cn.myapps.core.xmpp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.permission.action.PermissionHelper;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class MenuService extends OBPMService {
	private static String name = "MenuService";

	@Override
	public String getChildElementXML() {
		String xml = "<obpm xmlns=\"obpm:iq:service\">";
		xml += "<name>" + name + "</name>";
		xml += getInvoker().toXML();
		xml += "</obpm>";

		return xml;
	}

	public void setInvoker(MethodInvoker invoker) {
		this.invoker = invoker;
	}

	/**
	 * 获取属于此用户所有的菜单
	 * 
	 * @param logginName
	 * @param domainName
	 * @return
	 */
	public String getAllMenusByUser(String userName, String domainName) {
		StringBuffer xml = new StringBuffer();

		if (StringUtil.isBlank(userName))
			return xml.toString();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);

			UserVO user = userProcess.getUserByLoginnoAndDoaminName(userName, domainName);
			WebUser webUser = new WebUser(user);

			DomainVO domain = (DomainVO) process.doView(user.getDomainid());

			Collection<ApplicationVO> apps = domain.getApplications();
			if (apps == null || apps.isEmpty()) {
				return xml.toString();
			}
			PermissionHelper pHelper = new PermissionHelper();
			xml.append("<" + MobileConstant.TAG_APPLICATION + ">");

			for (Iterator<ApplicationVO> it = apps.iterator(); it.hasNext();) {
				ApplicationVO app = it.next();

				if (app != null && !StringUtil.isBlank(app.getId())) {
					xml.append("<" + MobileConstant.TAG_MENU_POP + " ");
					xml.append(MobileConstant.ATT_ID + "='" + app.getId() + "' ");
					// xml.append(MobileConstant.ATT_SRC + "='"
					// +app.getLogourl() + "' ");
					xml.append(MobileConstant.ATT_NAME + "='" + app.getName() + "'>");
					Collection<ResourceVO> topMenus = get_topmenus(app.getId(), user.getDomainid());
					Collection<ResourceVO> temp = topMenus;

					ResourceProcess process1 = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);

					for (Iterator<ResourceVO> it1 = topMenus.iterator(); it1.hasNext();) {
						ResourceVO resvo = (ResourceVO) it1.next();
						if (resvo == null || !pHelper.checkPermission(resvo, app.getId(), webUser)) {
							continue;
						}
						temp = process1.doGetDatasByParent(resvo.getId());
						if (temp != null && temp.size() == 0) {

							String url = resvo.toUrlString(webUser, new ParamsTable());

							xml.append("<" + MobileConstant.TAG_MENU_ITEM + " " + MobileConstant.ATT_NAME + "='"
									+ resvo.getDescription() + "' ");
							// xml.append(MobileConstant.ATT_APPLICATIONID+"='"
							// + app.getId() + "' ");
							xml.append(MobileConstant.ATT_ORDER + "='" + resvo.getOrderno() + "' ");
							xml.append(MobileConstant.ATT_URL + "='" + HtmlEncoder.encode(url) + "'>");
							xml.append("</" + MobileConstant.TAG_MENU_ITEM + ">");
						} else if (app != null && user != null) {
							String nextXml = menuRecursive(resvo, webUser, temp, app.getId(), user.getDomainid(),
									pHelper);
							if (nextXml != null) {
								xml.append(nextXml);
							}
						}
					}

					xml.append("</" + MobileConstant.TAG_MENU_POP + ">");
					try {
						PersistenceUtils.closeSessionAndConnection();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			xml.append("</" + MobileConstant.TAG_APPLICATION + ">");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xml.toString();
	}

	private String menuRecursive(ResourceVO resvo, WebUser webUser, Collection<ResourceVO> coll, String appId,
			String domainId, PermissionHelper pHelper) throws Exception {
		StringBuffer xml = new StringBuffer();
		ResourceProcess process = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
		xml
				.append("<" + MobileConstant.TAG_MENU + " " + MobileConstant.ATT_NAME + "='" + resvo.getDescription()
						+ "' ");
		xml.append(MobileConstant.ATT_ORDER + "='" + resvo.getOrderno() + "'>");
		for (Iterator<ResourceVO> it1 = coll.iterator(); it1.hasNext();) {
			ResourceVO resvo1 = (ResourceVO) it1.next();
			if (resvo1 == null || !pHelper.checkPermission(resvo1, appId, webUser)) {
				continue;
			}
			Collection<ResourceVO> subMenus = process.doGetDatasByParent(resvo1.getId());
			if (subMenus != null && subMenus.size() > 0) {
				xml.append(menuRecursive(resvo1, webUser, subMenus, appId, domainId, pHelper));
			} else {
				String url = resvo1.toUrlString(webUser, new ParamsTable());

				xml.append("<" + MobileConstant.TAG_MENU_ITEM + " " + MobileConstant.ATT_NAME + "='"
						+ resvo1.getDescription() + "' ");
				// xml.append(MobileConstant.ATT_APPLICATIONID+ "='" + appId +
				// "' ");
				xml.append(MobileConstant.ATT_ORDER + "='" + resvo1.getOrderno() + "' ");
				xml.append(MobileConstant.ATT_URL + "='" + HtmlEncoder.encode(url) + "'>");
				xml.append("</" + MobileConstant.TAG_MENU_ITEM + ">");
			}
		}
		xml.append("</" + MobileConstant.TAG_MENU + ">");
		return xml.toString();
	}

	public Collection<ResourceVO> get_topmenus(String application, String domain) throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_MOBILE);
		return get_topmenus(application, domain, params);
	}

	/**
	 * Retrieve the top menus.
	 * 
	 * @return Returns the top menus collection.
	 * @throws Exception
	 */
	public Collection<ResourceVO> get_topmenus(String application, String domain, ParamsTable params) throws Exception {
		return getSubMenus(null, application, domain, params);
	}

	public Collection<ResourceVO> getSubMenus(ResourceVO startNode, String application, String domain,
			ParamsTable params) throws Exception {
		ArrayList<ResourceVO> list = new ArrayList<ResourceVO>();

		params.setParameter("_orderby", "orderno");
		params.setParameter("application", application);
		ResourceProcess process = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
		Collection<ResourceVO> cols = process.doSimpleQuery(params, application);

		Iterator<ResourceVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ResourceVO vo = (ResourceVO) iter.next();
			if (startNode == null) {
				if (vo.getSuperior() == null
						) {
					list.add(vo);
				}
			} else {
				if (vo.getSuperior() != null) {
					ResourceVO superior = vo.getSuperior();
					while (superior != null) {
						if (superior.getId().equals(startNode.getId())) {
							list.add(vo);
							break;
						}
						superior = superior.getSuperior();
					}
				}
			}
		}

		return list;
	}

	public Packet createResultPacket() {
		try {
			String xml = invoker.invoke(this);
			IQfake iq = new IQfake(xml);
			iq.setTo(this.getFrom());
			iq.setFrom(this.getTo());
			iq.setType(IQ.Type.RESULT);
			
			return iq;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private class IQfake extends IQ {
		private String s;

		public IQfake(final String s) {
			super();
			this.s = s;
		}

		public String getChildElementXML() {
			StringBuilder buf = new StringBuilder();
			buf.append("<obpm xmlns=\"obpm:iq:service\">");
			buf.append(s);
			buf.append("</obpm>");
			return buf.toString();
		}
	}
}
