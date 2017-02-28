package cn.myapps.mobile2.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.permission.action.PermissionHelper;
import cn.myapps.core.resource.action.ResourceAction;
import cn.myapps.core.resource.action.ResourceHelper;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public class MbMenuHelper {

	/**
	 * 获取菜单列表
	 * 
	 * @param application
	 * @param user
	 * @param type
	 * @return
	 */
	public static ArrayList<ResourceVO> getResources(String applicationid, WebUser user, String type, String id) {
		ArrayList<ResourceVO> resList = new ArrayList<ResourceVO>();
		try {
			ResourceAction resource = new ResourceAction();
			ResourceHelper resHelper = new ResourceHelper();
			PermissionHelper permissionHelper = new PermissionHelper();
			resHelper.setApplicationid(applicationid);
			ParamsTable params = new ParamsTable();
			params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_HTML);

			Collection<ResourceVO> menus = null;
			if (!StringUtil.isBlank(id)) {
				menus = resHelper.searchMobileSubResource(id, 1, user.getDomainid());
			} else {
				menus = resource.get_topmenus(applicationid, user.getDomainid(), params);
			}

			if (menus != null) {
				for (ResourceVO menu : menus) {
					if (permissionHelper.checkPermission(menu, applicationid, user)) {
						if (menu.getLinkType() == null || menu.getLinkType().equals("")) {
							resList.add(menu);
						}else{
							if (type == null && (menu.getLinkType().equals(ResourceVO.LinkType.FORM.getCode())
									|| menu.getLinkType().equals(ResourceVO.LinkType.VIEW.getCode()))) {
								resList.add(menu);
							}
							if (type != null && menu.getLinkType().equals(type)) {
								resList.add(menu);
							}
						}
					}
				}
			}
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<ResourceVO> getResourceList(ApplicationVO application, WebUser user, String type)
			throws Exception {
		ArrayList<ResourceVO> resList = new ArrayList<ResourceVO>();
		try {
			String applicationId = application.getId();
			ResourceAction resource = new ResourceAction();
			ResourceHelper resHelper = new ResourceHelper();
			PermissionHelper permissionHelper = new PermissionHelper();
			resHelper.setApplicationid(application.getId());

			ParamsTable params = new ParamsTable();
			params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_HTML);

			Collection<ResourceVO> topMenus = resource.get_topmenus(application.getId(), user.getDomainid(), params);
			if (topMenus != null) {
				for (ResourceVO topMenu : topMenus) {
					if (permissionHelper.checkPermission(topMenu, applicationId, user)) {
						Collection<ResourceVO> secondMenus = resHelper.searchMobileSubResource(topMenu.getId(), 1,
								user.getDomainid());
						for (ResourceVO secondMenu : secondMenus) {
							if (secondMenu.getLinkType() != null
									&& (secondMenu.getLinkType().equals(type) || secondMenu.getLinkType().equals(""))) {
								boolean isPermission = permissionHelper
										.checkPermission(secondMenu, applicationId, user);
								if (isPermission) {
									resList.add(secondMenu);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resList;
	}

	public static ArrayList<ResourceVO> doGetSubResource(ApplicationVO appvo, ResourceVO secondMenu, WebUser user,
			String type) throws Exception {
		ArrayList<ResourceVO> subResList = new ArrayList<ResourceVO>();
		try {
			ResourceHelper resourceHelper = new ResourceHelper();
			PermissionHelper helper = new PermissionHelper();

			Collection<ResourceVO> thirdMenus = resourceHelper.searchMobileSubResource(secondMenu.getId(), 1,
					user.getDomainid());
			for (ResourceVO thirdMenu : thirdMenus) {
				boolean isPermission = helper.checkPermission(thirdMenu, appvo.getId(), user);
				if (isPermission) {
					if (thirdMenu.getLinkType() != null && thirdMenu.getLinkType().equals(type)) {
						subResList.add(thirdMenu);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return subResList;
	}

	public static Map<String, Map<String, String>> doGetParams(Map<String, ResourceVO> resourceMap) throws Exception {
		Map<String, Map<String, String>> paramsMap = new LinkedHashMap<String, Map<String, String>>();
		if (resourceMap != null && resourceMap.size() > 0) {
			Set<Map.Entry<String, ResourceVO>> set = resourceMap.entrySet();
			for (Iterator<Map.Entry<String, ResourceVO>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, ResourceVO> entry = (Map.Entry<String, ResourceVO>) it.next();
				String id = entry.getKey();
				ResourceVO vo = entry.getValue();
				paramsMap.put(id, createResourceParams(vo));
			}
		}
		return paramsMap;
	}

	public static Map<String, String> createResourceParams(ResourceVO vo) throws Exception {
		try {
			Map<String, String> params = new LinkedHashMap<String, String>();
			if (ResourceVO.LinkType.FORM.getCode().equals(vo.getLinkType())) {
				params.put("_formid", vo.getActionContent());
				params.put("application", vo.getApplicationid());
			} else if (ResourceVO.LinkType.VIEW.getCode().equals(vo.getLinkType())) {
				params.put("_viewid", vo.getActionContent());
				params.put("application", vo.getApplicationid());
			}
			if (!StringUtil.isBlank(vo.getQueryString())) {
				Collection<Object> qs = JsonUtil.toCollection(vo.getQueryString(), JSONObject.class);
				Iterator<Object> iterator = qs.iterator();
				while (iterator.hasNext()) {
					JSONObject object = JSONObject.fromObject(iterator.next());
					params.put(String.valueOf(object.get("paramKey")), String.valueOf(object.get("paramValue")));
				}
			}
			return params;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 检查字符串是否包含特殊符号：<br>
	 * 
	 * @param value
	 * @return
	 */
	public static String replacSpecial(String value) {
		if (value != null) {
			value = value.replace("&", "&amp;");
			value = value.replace(">", "&gt;");
			value = value.replace("<", "&lt;");
			value = value.replace("'", "&apos;");
			value = value.replace("\"", "&quot;");
		}
		return value;
	}
}
