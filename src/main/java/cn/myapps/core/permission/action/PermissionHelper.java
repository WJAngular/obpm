package cn.myapps.core.permission.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

public class PermissionHelper extends BaseHelper<PermissionVO> {
	static Logger logger = Logger.getLogger(PermissionHelper.class);

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public PermissionHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(PermissionProcess.class));
	}

	public WebUser user;

	public boolean checkPermission(ResourceVO r, String applicationid, WebUser webUser) throws Exception {
		user = webUser;
		if(ResourceVO.PERMISSION_TYPE_PUBLIC.equals(r.getPermissionType())) return true;
		
		Collection<RoleVO> roleList = user.getRolesByApplication(r.getApplicationid());

		PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		
		return permissionProcess.check(roleList, r.getId(),r.getId(),OperationVO.MENU_INVISIBLE, ResVO.MENU_TYPE,applicationid);
	}

	public WebUser getUser() {
		return user;
	}

	public void setUser(WebUser user) {
		this.user = user;
	}

	public Collection<ResourceVO> get_protectResources(String application) throws Exception {
		ResourceProcess rp = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
		return rp.getProtectResources(application);
	}

	public Collection<ResourceVO> get_topprotectResources(String application) throws Exception {
		ResourceProcess rp = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
		return rp.getTopProtectResources(application);
	}

	public Collection<ResourceVO> getTopResources(String application) throws Exception {
		ResourceProcess rp = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
		return rp.getTopResources(application);
	}

	public String get_childResourcesString(ResourceVO vo, int t, String application) throws Exception {
		StringBuffer html = new StringBuffer();
		Collection<ResourceVO> childs = get_childResources(vo, application);

		if (childs != null && childs.size() > 0) {
			html.append("<table>");
			html.append("<tr><td class='commFont'>");
			int temp = t;
			while (temp > 0) {
				html.append("*");
				temp--;
			}

			html.append("<input type='checkbox' name='_resourcelist' value='" + vo.getId()).append("' />");
			html.append(vo.getDescription());
			html.append("</td></tr>");
			for (Iterator<ResourceVO> iter = childs.iterator(); iter.hasNext();) {
				html.append("<tr>");
				for (int i = 0; i < 5 && iter.hasNext(); i++) {
					ResourceVO rv = iter.next();
					if (rv.getSuperior() != null && rv.getSuperior().getId().equals(vo.getId())) {

						html.append("<td style=\"vertical-align:top\" class=\"commFont\">");
						html.append(get_childResourcesString(rv, t + 1, application));
						html.append("</td>");

					}
				}
				html.append("</tr>");
			}
			html.append("</table>");
		} else {
			html.append("<table>");
			html.append("<tr><td class=\"commFont\">");
			while (t > 0) {
				html.append("*");
				t--;
			}
			html.append("<input type='checkbox' name='_resourcelist' value='" + vo.getId()).append("' />");
			html.append(vo.getDescription());
			html.append("</td></tr>");
			html.append("</table>");
		}
		return html.toString();
	}

	public Collection<ResourceVO> get_childResources(ResourceVO vo, String application) throws Exception {
		if (vo == null)
			return null;
		ResourceProcess rp = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
		Map<String, ResourceVO> childs = new HashMap<String, ResourceVO>();
		Collection<ResourceVO> rs = rp.getProtectResources(application);
		for (Iterator<ResourceVO> iter = rs.iterator(); iter.hasNext();) {
			ResourceVO em = (ResourceVO) iter.next();
			if (em.getSuperior() != null && em.getSuperior().getId().equals(vo.getId())) { //
				childs.put(em.getId(), em);
			}

		}
		return childs.values();
	}

	/**
	 * 获得类型
	 * 
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public String getType(String applicationid, String roleid, String resid) throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("application", applicationid);
		params.setParameter("t_role_id", roleid);
		params.setParameter("t_res_id", resid);
		PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		DataPackage<PermissionVO> datas = permissionProcess.doQuery(params);
		if (datas.rowCount > 0) {
			for (Iterator<PermissionVO> iter = datas.datas.iterator(); iter.hasNext();) {
				PermissionVO permissionVO = iter.next();
				if (permissionVO != null) {
					return permissionVO.getType().toString();
				}
			}
		}
		// 默认允许值为1 禁止值为2
		return "1";
	}

	/**
	 * 设值到类型列表中
	 * 
	 * @param resid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getTypeList(String resid) throws Exception {
		HashMap<String, String> rtn = new LinkedHashMap<String, String>();
		rtn.put("1", "{*[Allow]*}");
		rtn.put("2", "{*[cn.myapps.core.task.banned]*}");

		return rtn;
	}

	/**
	 * 分配权限提示文字
	 * 
	 * @param applicationid
	 * @param roleid
	 * @param resid
	 * @throws Exception
	 */
	public String getappropriated(String applicationid, String roleid, String resid) throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("application", applicationid);
		params.setParameter("t_role_id", roleid);
		params.setParameter("t_res_id", resid);
		PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		DataPackage<?> datas = permissionProcess.doQuery(params);
		if (datas.rowCount > 0) {
			return "<font color='green'>{*[Allocation]*}</font>";
		}
		return "<font color='red'>{*[Unappropriated]*}</font>";
	}

	/**
	 * 菜单的完整路径
	 * 
	 * @param r
	 * @return
	 */
	public String getResourceParent(ResourceVO r, String temp) {
		String str = "";
		str = temp;
		try {
			if (r.getSuperior() != null) {
				if (str == "" || str.equals("")) {
					str = r.getDescription();
				} else {
					str = r.getDescription() + "/" + str;
				}
				str = getResourceParent(r.getSuperior(), str);
			} else {
				if (str == "" || str.equals("")) {
					str = r.getDescription();
				} else {
					str = r.getDescription() + "/" + str;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

}
