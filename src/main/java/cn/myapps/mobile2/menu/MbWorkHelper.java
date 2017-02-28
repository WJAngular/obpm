package cn.myapps.mobile2.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

import net.sf.json.JSONObject;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.work.ejb.WorkVO;
import cn.myapps.core.permission.action.PermissionHelper;
import cn.myapps.core.resource.action.ResourceAction;
import cn.myapps.core.resource.action.ResourceHelper;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public class MbWorkHelper {

	public static final String DEFAULT_QUERY_REGEX = "&<>'\"";

	/**
	 * 获取当前用户代办
	 * 
	 * @param application
	 *            软件
	 * @param user
	 *            用户
	 * @return
	 * @throws Exception
	 */
	public static DataPackage<WorkVO> getPendingWorkList(ApplicationVO application, WebUser user) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					application.getId());
			DataPackage<WorkVO> workDatas = process.doQueryProcessingWorks(user, "", "", 1, 50, false);
			return workDatas;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
	}

	public static DataPackage<WorkVO> getProcessedWorkList(ApplicationVO application, WebUser user) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					application.getId());
			DataPackage<WorkVO> workDatas = process.doQueryProcessedRunningWorks(user, null, null, 1, 50);
			return workDatas;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
	}

	/**
	 * 获取软件列表 以json格式
	 * 
	 * @param user
	 * @return
	 */
	public static String getApplicationList(WebUser user) {
		try {
			ApplicationHelper helper = new ApplicationHelper();
			Collection<ApplicationVO> applicationList = helper.getListByWebUser(user);
			JSONArray array = new JSONArray();
			if (applicationList != null && !applicationList.isEmpty()) {
				for (ApplicationVO appvo : applicationList) {
					if (appvo != null && appvo.isActivated()) {
						JSONObject obj = new JSONObject();
						obj.put("id", appvo.getId());
						obj.put("name", appvo.getName());
						array.put(obj);
					}
				}
			}
			return array.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static DataPackage<WorkVO> getFinishedWorkList(WebUser user, String applicationid, String subject,
			ParamsTable params) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationid);
			int _currpage = params.getParameterAsInteger("_currpage");
			int _pagelines = params.getParameterAsInteger("_pagelines");
			if (_currpage == 0) {
				_currpage = 1;
			}
			if (_pagelines == 0) {
				_pagelines = 20;
			}
			//TODO 待解决问题： 手机端接收的是所有完成事项，还是个人完成事项("fasle"为显示所有人，"true"为显示本人工作)
			DataPackage<WorkVO> workDatas = process.doQueryProcessedCompletedWorks(user, null, subject,false, _currpage,
					_pagelines);
			return workDatas;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
		return null;
	}

	public static ArrayList<ResourceVO> getResourceList(ApplicationVO application, WebUser user) throws Exception {
		ArrayList<ResourceVO> resList = new ArrayList<ResourceVO>();
		try {
			String applicationId = application.getId();
			ResourceAction resource = new ResourceAction();
			ResourceHelper resHelper = new ResourceHelper();
			PermissionHelper permissionHelper = new PermissionHelper();
			resHelper.setApplicationid(application.getId());
			Collection<ResourceVO> topMenus = resource.get_topmenus(application.getId(), user.getDomainid());
			if (topMenus != null) {
				for (ResourceVO topMenu : topMenus) {
					if (permissionHelper.checkPermission(topMenu, applicationId, user)) {
						Collection<ResourceVO> secondMenus = resHelper.searchSubResource(topMenu.getId(), 1,
								user.getDomainid());
						for (ResourceVO secondMenu : secondMenus) {
							boolean isPermission = permissionHelper.checkPermission(secondMenu, applicationId, user);
							if (isPermission) {
								resList.add(secondMenu);
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

	public static ArrayList<ResourceVO> getResourceList(ApplicationVO application, WebUser user, String type)
			throws Exception {
		ArrayList<ResourceVO> resList = new ArrayList<ResourceVO>();
		try {
			String applicationId = application.getId();
			ResourceAction resource = new ResourceAction();
			ResourceHelper resHelper = new ResourceHelper();
			PermissionHelper permissionHelper = new PermissionHelper();
			resHelper.setApplicationid(application.getId());
			Collection<ResourceVO> topMenus = resource.get_topmenus(application.getId(), user.getDomainid());
			if (topMenus != null) {
				for (ResourceVO topMenu : topMenus) {
					if (permissionHelper.checkPermission(topMenu, applicationId, user)) {
						Collection<ResourceVO> secondMenus = resHelper.searchSubResource(topMenu.getId(), 1,
								user.getDomainid());
						for (ResourceVO secondMenu : secondMenus) {
							boolean isPermission = permissionHelper.checkPermission(secondMenu, applicationId, user);
							if (isPermission) {
								resList.add(secondMenu);
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

			Collection<ResourceVO> thirdMenus = resourceHelper.searchSubResource(secondMenu.getId(), 1,
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
